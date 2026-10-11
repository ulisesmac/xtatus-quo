(ns xquo.dropdown.style
  (:require [react-native.utils :refer [defstyle style]]
            [xquo.foundations.colors :as colors]))

(def icon-size {24 12, 32 20, 40 20})
(def ^:private image-size {24 11, 32 15, 40 15})
(def font-type {24 :font/medium-13, 32 :font/medium-15, 40 :font/medium-15})
(def ^:private radius {24 8, 32 10, 40 12})
(def ^:private left-padding {24 8, 32 12, 40 16})
(def ^:private content-padding {24 8, 32 8, 40 12})
(def ^:private right-padding {24 8, 32 8, 40 12})
(def ^:private vertical-padding {24 3, 32 5, 40 9})

(defn- foreground [{:keys [dark-theme?]} type]
  (colors/get-color (if (or dark-theme? (= type :customisation))
                      :color/white
                      :color/neutral-100)))

(defn leading-color [{:keys [theme dark-theme?]} type blur? photo? input?]
  (cond
    (= type :customisation)
    (colors/get-color :color/white-70)

    (or blur? photo?)
    (colors/get-color (if dark-theme? :color/white :color/neutral)
                      (if dark-theme? 50 80)
                      (if input? 70 40))

    :else
    (colors/themed theme :color/neutral-50 :color/neutral-40)))

(defn arrow-colors [{:keys [theme dark-theme?] :as context} type blur? photo?]
  [(cond
     (= type :customisation)
     (colors/get-color :color/white-20)

     (and dark-theme? blur? (= type :grey))
     (colors/get-color :color/white-20)

     (or blur? photo?)
     (colors/themed theme :color/neutral-80-10 :color/white-10)

     :else
     (colors/themed theme :color/neutral-20 :color/neutral-80))
   (foreground context type)])

(defn- fill [{:keys [theme dark-theme?]} color type size content-only? active? blur? photo?]
  (cond
    (= type :customisation)
    (colors/get-color color (cond
                              (and dark-theme? active?) 50
                              dark-theme?               60
                              active?                   60
                              :else                     50))

    (and (= type :grey) photo?)
    (colors/get-color (if dark-theme? :color/neutral :color/white)
                      (if dark-theme? 80 50)
                      (if active? 50 40))

    (and (or blur? photo?)
         (or (= type :grey) (and (= type :ghost) active?)))
    (colors/get-color (if dark-theme? :color/white :color/neutral)
                      (if dark-theme? 50 80)
                      (if active? 10 5))

    (= type :grey)
    (colors/get-color :color/neutral
                      (cond
                        (and dark-theme? active? content-only?)
                        70

                        (and dark-theme? active?)
                        80

                        dark-theme?
                        90

                        (and active? content-only? (< size 40))
                        30

                        active?
                        20

                        (= size 40)
                        5

                        :else
                        10))

    (and (= type :ghost) active?)
    (colors/themed theme :color/neutral-10 :color/neutral-80)))

(defn- border [{:keys [theme dark-theme?]} active? blur? photo?]
  (if (or blur? photo?)
    (colors/get-color (if dark-theme? :color/white :color/neutral)
                      (if dark-theme? 50 80)
                      (if active? 10 5))
    (colors/themed theme
                   (if active? :color/neutral-40 :color/neutral-30)
                   (if active? :color/neutral-60 :color/neutral-70))))

(defn button [context color type size content content-only? active? blur? photo?]
  (let [border-width (if (= type :outline) 1 0)
        padding      (if content content-padding left-padding)]
    (style {:align-self       :flex-start
            :max-width        "100%"
            :width            (when content-only? size)
            :height           size
            :border-radius    (radius size)
            :border-width     border-width
            :border-color     (if (= type :outline) (border context active? blur? photo?) "transparent")
            :background-color (or (fill context color type size content-only? active? blur? photo?) "transparent")
            :padding-left     (if content-only? 0 (- (padding size) border-width))
            :padding-right    (if content-only? 0 (- (right-padding size) border-width))
            :padding-top      (if content-only? 0 (- (vertical-padding size) border-width))
            :padding-bottom   (if content-only? 0 (- (vertical-padding size) border-width))})))

(defn leading-width [{:keys [light-theme?]} type size active? disabled? blur? photo?]
  (if (and (= size 32)
           (= type :grey)
           (not active?)
           (not disabled?)
           (or photo? (and light-theme? (not blur?))))
    19
    (icon-size size)))

(defstyle row
  {:flex-direction :row
   :align-items    :center
   :flex-shrink    1
   :gap            4})

(defn leading [size width]
  (style {:width           width
          :height          size
          :align-items     :center
          :justify-content :center}))

(defn icon-scale [size width]
  (style {:transform [{:scale-x (/ width size)}]}))

(defn image [size]
  (style {:width  (image-size size)
          :height (image-size size)}))

(defn text [context type size]
  (style {:color                (foreground context type)
          :include-font-padding false
          :flex-shrink          1
          :height               (if (= size 24) 18 22)
          :line-height          (if (= size 24) 18 21.75)}))

(defstyle input-container
  {:width "100%"
   :gap   8})

(defstyle input-row
  {:flex           1
   :flex-direction :row
   :align-items    :center
   :gap            8})

(defstyle input-text
  {:flex      1
   :min-width 0})

(defn input [{:keys [theme dark-theme? light-theme?]} content active? disabled? blur?]
  (style {:height           40
          :width            "100%"
          :border-radius    12
          :border-width     1
          :border-color     (if blur?
                              (colors/get-color (if dark-theme? :color/white :color/neutral)
                                                (if dark-theme? 50 80)
                                                (cond
                                                  (and active? dark-theme?) 40
                                                  active?                   20
                                                  :else                     10))
                              (colors/themed theme
                                             (if active? :color/neutral-40 :color/neutral-20)
                                             (if active? :color/neutral-60 :color/neutral-70)))
          :background-color (if blur? "transparent" (colors/themed theme :color/white :color/neutral-95))
          :padding-left     (dec (if (or content (and light-theme? blur? (not disabled?))) 12 16))
          :padding-right    11
          :padding-top      8
          :padding-bottom   8}))

(defn input-label [{:keys [theme]} blur?]
  (style {:height               18
          :line-height          18.2
          :include-font-padding false
          :color                (if blur?
                                  (colors/themed theme :color/neutral-80-40 :color/white-40)
                                  (colors/themed theme :color/neutral-50 :color/neutral-40))}))
