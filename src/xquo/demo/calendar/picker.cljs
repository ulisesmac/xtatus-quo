(ns xquo.demo.calendar.picker
  (:require [react-native.react.core :as react]
            [xquo.context :as context]
            [xquo.core :as xquo]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(def ^:private years
  (vec (range 2010 2031)))

(defn screen [_]
  (let [[month set-month!]         (react/use-state "2023-03")
        [mode set-mode!]           (react/use-state :range)
        [value set-value!]         (react/use-state {:start "2023-03-07" :end "2023-03-23"})
        [locale set-locale!]       (react/use-state nil)
        [first-day set-first-day!] (react/use-state 0)
        [bounds? set-bounds!]      (react/use-state false)
        [years? set-years!]        (react/use-state false)
        [color set-color!]         (react/use-state nil)
        on-change!                 (react/use-callback (fn [value]
                                                         (set-value! value)
                                                         (js/alert (str "Selected " (pr-str value))))
                                                       [])
        on-month-change!           (react/use-callback (fn [month]
                                                         (set-month! month)
                                                         (js/alert (str "Month " month)))
                                                       [])
        on-mode-change!            (react/use-callback (fn [mode]
                                                         (set-mode! mode)
                                                         (set-value! nil))
                                                       [])
        on-bounds-change!          (react/use-callback (fn [enabled?]
                                                         (set-bounds! enabled?)
                                                         (when enabled?
                                                           (set-month! "2023-03")))
                                                       [])]
    [context/provider {:color color}
     [screen/screen {:title   "Calendar picker"
                     :preview [xquo/calendar.picker {:month             month
                                                     :mode              mode
                                                     :value             value
                                                     :on-change         on-change!
                                                     :on-month-change   on-month-change!
                                                     :today             "2023-03-24"
                                                     :locale            locale
                                                     :first-day-of-week first-day
                                                     :years             (when years? years)
                                                     :min-date          (when bounds? "2023-03-05")
                                                     :max-date          (when bounds? "2023-03-25")}]}
      [controls/choice {:label     ":mode"
                        :value     mode
                        :on-change on-mode-change!
                        :options   [{:value :single :label ":single"}
                                    {:value :range :label ":range"}]}]
      [xquo/text {:font :font/regular-13}
       (str ":value " (pr-str value))]
      [controls/choice {:label     ":month examples"
                        :value     month
                        :on-change set-month!
                        :options   [{:value "2023-03" :label "2023-03"}
                                    {:value "2024-02" :label "2024-02"}
                                    {:value "2020-08" :label "2020-08"}]}]
      [controls/choice {:label     ":locale"
                        :value     locale
                        :on-change set-locale!
                        :options   [{:value nil :label "Device"}
                                    {:value "en" :label "English"}
                                    {:value "es" :label "Español"}]}]
      [controls/choice {:label     ":first-day-of-week"
                        :value     first-day
                        :on-change set-first-day!
                        :options   [{:value 0 :label "0 — Sunday"}
                                    {:value 1 :label "1 — Monday"}]}]
      [controls/checkbox {:label     ":min-date 2023-03-05 / :max-date 2023-03-25"
                          :value     bounds?
                          :on-change on-bounds-change!}]
      [controls/checkbox {:label ":years 2010–2030" :value years? :on-change set-years!}]
      [controls/choice {:label     "context :color"
                        :value     color
                        :on-change set-color!
                        :options   [{:value nil :label "Inherited"}
                                    {:value :color/primary :label ":color/primary"}
                                    {:value :color/success :label ":color/success"}
                                    {:value :color/danger :label ":color/danger"}]}]]]))
