(ns xtatus-quo.components.markdown.text
  (:require
   [clojure.string :as string]
   [quo.context :as quo.context]
   ;[react-native.corex :as rn]
   [react-native.utils :as rn.utils]
   [reagent-extended-compiler.utils.transforms :as transforms]
   [xtatus-quo.context :as context]
   [xtatus-quo.foundations.colors :as colors]
   [xtatus-quo.foundations.typography :as typography]))

(defn text-style
  [{:keys [size align weight style]} theme]
  (merge (case (or weight :regular)
           :regular   typography/font-regular
           :medium    typography/font-medium
           :semi-bold typography/font-semi-bold
           :bold      typography/font-bold
           :monospace typography/monospace
           :code      typography/code
           :inherit   nil)
         (case (or size :paragraph-1)
           :label       typography/label
           :paragraph-2 typography/paragraph-2
           :paragraph-1 typography/paragraph-1
           :heading-2   typography/heading-2
           :heading-1   typography/heading-1
           :inherit     nil)
         {:text-align (or align :auto)}
         (if (:color style)
           style
           (assoc style
                  :color
                  (if (= theme :theme/dark) colors/white colors/neutral-100)))))

(def font-type-style
  (memoize
   (fn [font-style]
     (let [[weight size] (string/split (name font-style) #"-")
           weight-style (case (keyword weight)
                          :regular "Inter-Regular"
                          :medium "Inter-Medium"
                          :semibold "Inter-SemiBold"
                          :bold "Inter-Bold"
                          :monospace "InterStatus-Regular"
                          :code "UbuntuMono-Regular"
                          nil)
           size-style   (case (int size)
                          11 typography/label
                          13 typography/paragraph-2
                          15 typography/paragraph-1
                          19 typography/heading-2
                          27 typography/heading-1
                          {:font-size      (int size)
                           ;:line-height    (+ (int size) 4)
                           :letter-spacing (typography/tracking (int size))})]
       (assoc size-style :font-family weight-style)))))


(defn text-styles
  [{:keys [size align weight style font-style]} theme]
  (if font-style
    (let [font-type  (font-type-style font-style)
          text-align (or align :auto)
          color      (or (:color style)
                         (if (= theme :theme/dark) colors/white colors/neutral-100))]
      [(assoc font-type
         :text-align text-align
         :color color)
       (dissoc style :color)])
    (let [font-weight (case (or weight :regular)
                        :regular typography/font-regular
                        :medium typography/font-medium
                        :semi-bold typography/font-semi-bold
                        :bold typography/font-bold
                        :monospace typography/monospace
                        :code typography/code
                        :inherit nil)
          font-size   (case (or size :paragraph-1)
                        :label typography/label
                        :paragraph-2 typography/paragraph-2
                        :paragraph-1 typography/paragraph-1
                        :heading-2 typography/heading-2
                        :heading-1 typography/heading-1
                        :inherit nil)
          text-align  {:text-align (or align :auto)}
          color       (or (:color style)
                          (assoc style
                            :color (if (= theme :theme/dark) colors/white colors/neutral-100)))]
      [font-weight font-size text-align color style])))

(defn text* [& argv]
  (let [[props children] (rn.utils/get-props-and-children argv)
        theme  (quo.context/use-theme)
        styles (text-styles props theme)]
    (into [:rn/text (assoc (dissoc props :style :size :align :weight :color)
                     :style styles)]
          children)))

(def text2-style
  (memoize
   (fn [{:keys [font color align]
         :or   {font :font/regular-15}}
        theme]
     (let [align        (or align :auto)
           color        (or color
                            (if (= theme :theme/dark)
                              colors/white
                              colors/neutral-100))
           inner-styles (assoc (font-type-style font)
                          :text-align align
                          :color color)]
       (transforms/->js-prop-obj inner-styles)))))

(defn text [& argv]
  (let [[props children] (rn.utils/props argv)
        theme        (context/use-theme)
        input-styles (get props :style [])
        styles       (rn.utils/add-styles (text2-style props theme) input-styles)]
    (into [:rn/text (assoc (dissoc props :style :size :align :weight :color)
                      :style styles)]
          children)))
