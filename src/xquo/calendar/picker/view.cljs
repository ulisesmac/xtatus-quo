(ns xquo.calendar.picker.view
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :as rn.utils]
            [xquo.calendar.day.view :as day]
            [xquo.calendar.picker.date :as date]
            [xquo.calendar.picker.style :as style]
            [xquo.calendar.year.view :as year]
            [xquo.components.button.view :as button]
            [xquo.components.text.view :as text]
            [xquo.context :as context]
            [xquo.foundations.colors :as colors]))

(defn- year-item [{:keys [value month min-month max-month on-month-change]}]
  (let [[selected-year month-number] (date/month-parts month)
        target-month                 (date/month-string value month-number)
        next-month                   (cond
                                       (and min-month (neg? (compare target-month min-month)))
                                       min-month

                                       (and max-month (pos? (compare target-month max-month)))
                                       max-month

                                       :else
                                       target-month)
        disabled?                    (or (not on-month-change)
                                         (date/outside-bounds? (str value)
                                                               (some-> min-month (subs 0 4))
                                                               (some-> max-month (subs 0 4))))
        on-press!                    (react/use-callback #(on-month-change next-month)
                                                         [on-month-change next-month])]
    [year/calendar-year {:selected? (= value selected-year)
                         :disabled? disabled?
                         :on-press  on-press!}
     value]))

(defn- year-list [{:keys [years month height dark-theme? min-month max-month on-month-change]}]
  (let [[selected-year] (date/month-parts month)
        selected-index  (first (keep-indexed (fn [index year]
                                               (when (= year selected-year) index))
                                             years))
        scroll-offset   (max 0 (- (+ (* 36 (or selected-index 0)) 40) height))
        scroll-ref      (react/use-ref nil)
        scroll-to-year! (react/use-callback (fn []
                                              (when-let [scroll (j/get scroll-ref :current)]
                                                (j/call scroll :scrollTo #js {:y scroll-offset :animated false})))
                                            [scroll-ref scroll-offset])]
    (react/use-effect scroll-to-year! [scroll-to-year!])
    [:rn/view {:style [style/years (style/year-frame height)]}
     (into [:rn/scroll-view {:ref                             scroll-ref
                             :style                           style/scroll
                             :content-container-style         style/year-content
                             :shows-vertical-scroll-indicator false
                             :on-content-size-change          scroll-to-year!}]
           (map (fn [year]
                  [year-item {:value           year
                              :month           month
                              :min-month       min-month
                              :max-month       max-month
                              :on-month-change on-month-change}])
                years))
     [:rn/view {:style (style/year-gradient dark-theme?) :pointer-events :none}]
     [:svg/svg {:width          1
                :height         height
                :style          style/sidebar-line
                :pointer-events :none}
      [:svg/line {:x1               0.5
                  :x2               0.5
                  :y1               0
                  :y2               height
                  :stroke-width     1
                  :stroke-dasharray "2 4"
                  :stroke           (colors/get-color (if dark-theme? :color/neutral-80 :color/neutral-20))}]]]))

(defn- month-header [{:keys [month locale min-month max-month on-month-change]}]
  (let [previous-month (date/shift-month month -1)
        next-month     (date/shift-month month 1)
        previous!      (react/use-callback #(on-month-change previous-month)
                                           [on-month-change previous-month])
        next!          (react/use-callback #(on-month-change next-month)
                                           [on-month-change next-month])]
    [:rn/view {:style style/header}
     [button/button {:type      :outline
                     :size      24
                     :icon      {:name :icon/chevron-left}
                     :disabled? (or (not on-month-change)
                                    (date/outside-bounds? previous-month min-month max-month))
                     :on-press  previous!}]
     [text/text {:font :font/semibold-15 :number-of-lines 1 :style style/month-title}
      (date/month-label month locale)]
     [button/button {:type      :outline
                     :size      24
                     :icon      {:name :icon/chevron-right}
                     :disabled? (or (not on-month-change)
                                    (date/outside-bounds? next-month min-month max-month))
                     :on-press  next!}]]))

(defn- month-day [{:keys [cell last? value mode today min-date max-date on-change]}]
  (let [{:keys [date current-month?]} cell
        range?                        (= mode :range)
        start                         (when range? (:start value))
        end                           (when range? (:end value))
        selected?                     (if range? (or (= date start) (= date end)) (= date value))
        disabled?                     (or (not current-month?)
                                          (date/outside-bounds? date min-date max-date))
        on-press!                     (react/use-callback (fn []
                                                            (when on-change
                                                              (on-change (if range? (date/next-range value date) date))))
                                                          [on-change range? value date])]
    [:rn/view {:style (if last? style/last-cell style/cell)}
     [day/calendar-day {:selected? selected?
                        :today?    (= date today)
                        :disabled? disabled?
                        :on-press  on-press!}
      (:day cell)]]))

(defn- month-week [{:keys [week value mode today min-date max-date on-change dark-theme?]}]
  (let [{:keys [start end]} (when (= mode :range) value)
        start-index         (first (keep-indexed (fn [index cell]
                                                   (when (= start (:date cell)) index))
                                                 week))
        end-index           (first (keep-indexed (fn [index cell]
                                                   (when (= end (:date cell)) index))
                                                 week))
        range-visible?      (and start
                                 end
                                 (not= start end)
                                 (not (pos? (compare start (:date (last week)))))
                                 (not (neg? (compare end (:date (first week))))))]
    [:rn/view {:style style/row}
     (when range-visible?
       [:rn/view {:pointer-events :none
                  :style          (style/range-fill dark-theme? start-index end-index)}])
     (for [[index cell] (map-indexed vector week)]
       ^{:key (:date cell)}
       [month-day {:cell      cell
                   :last?     (= index 6)
                   :value     value
                   :mode      mode
                   :today     today
                   :min-date  min-date
                   :max-date  max-date
                   :on-change on-change}])]))

(defn calendar-picker [{:keys [month mode value on-change on-month-change today min-date max-date
                               locale first-day-of-week years]
                        :or   {mode :single first-day-of-week 0}
                        :as   props}]
  (let [dark-theme? (:dark-theme? (context/use-theme-color))
        [year]      (date/month-parts month)
        years       (or years (range (- year 7) (inc year)))
        weeks       (date/weeks month first-day-of-week)
        height      (+ 88 (* 36 (count weeks)))
        locale      (date/resolve-locale locale)
        weekdays    (date/weekday-labels locale first-day-of-week)
        today       (or today (date/today))
        min-month   (some-> min-date (subs 0 7))
        max-month   (some-> max-date (subs 0 7))]
    [:rn/view (-> props
                  (dissoc :month
                          :mode
                          :value
                          :on-change
                          :on-month-change
                          :today
                          :min-date
                          :max-date
                          :locale
                          :first-day-of-week
                          :years
                          :style)
                  (assoc :style (rn.utils/add-styles style/container (:style props))))
     [:rn/view {:style [style/content (style/content-style dark-theme?)]}
      [year-list {:years           years
                  :month           month
                  :height          height
                  :dark-theme?     dark-theme?
                  :min-month       min-month
                  :max-month       max-month
                  :on-month-change on-month-change}]
      [:rn/view {:style style/month}
       [month-header {:month           month
                      :locale          locale
                      :min-month       min-month
                      :max-month       max-month
                      :on-month-change on-month-change}]
       [:rn/view {:style style/days}
        (into [:rn/view {:style style/row}]
              (map-indexed (fn [index label]
                             [:rn/view {:style (if (= index 6) style/last-cell style/cell)}
                              [text/text {:font :font/medium-13 :style (style/weekday dark-theme?)}
                               label]])
                           weekdays))
        (for [week weeks]
          ^{:key (:date (first week))}
          [month-week {:week        week
                       :value       value
                       :mode        mode
                       :today       today
                       :min-date    min-date
                       :max-date    max-date
                       :on-change   on-change
                       :dark-theme? dark-theme?}])]]]
     [:rn/view {:style (style/border dark-theme?) :pointer-events :none}]]))
