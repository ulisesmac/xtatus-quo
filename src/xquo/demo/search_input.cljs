(ns xquo.demo.search-input
  (:require [applied-science.js-interop :as j]
            [react-native.react.core :as react]
            [react-native.utils :refer [defstyle]]
            [xquo.core :as xquo]
            [xquo.context :as context]
            [xquo.demo.assets :as assets]
            [xquo.demo.controls :as controls]
            [xquo.demo.screen :as screen]))

(defstyle ^:private preview-style
  {:width "100%" :padding-horizontal 20})

(defn- pressed! [] (js/alert "Pressed"))

(def ^:private context-tags
  [{:type        :context
    :context-tag {:label "Alex" :image-source assets/profile-testing-1 :on-press pressed!}}
   {:type        :context
    :context-tag {:label "Pedro" :image-source assets/profile-testing-2 :on-press pressed!}}
   {:type        :context
    :context-tag {:label "Freya Odinson" :image-source assets/profile-testing-3 :on-press pressed!}}])

(def ^:private standard-tags
  [{:tag {:label "Crypto" :content {:type :emoji :text "💰"} :on-press pressed!}}
   {:tag {:label "Blockchain" :content {:type :emoji :text "🔗"} :on-press pressed!}}
   {:tag {:label "DeFi" :content {:type :emoji :text "📈"} :on-press pressed!}}
   {:tag {:label "Entertainment" :content {:type :emoji :text "🍿"} :on-press pressed!}}])

(defn screen [_]
  (let [[value set-value!]             (react/use-state "")
        [placeholder set-placeholder!] (react/use-state "Search...")
        [tag-mode set-tag-mode!]       (react/use-state nil)
        [color set-color!]             (react/use-state :color/primary)
        [blur? set-blur!]              (react/use-state nil)
        [disabled? set-disabled!]      (react/use-state nil)
        [clearable? set-clearable!]    (react/use-state true)
        input-ref                      (react/use-ref nil)
        clear!                         (react/use-callback #(set-tag-mode! nil) [])
        focus!                         (react/use-callback (fn []
                                                             (when-let [native (j/get input-ref :current)]
                                                               (j/call native :focus)))
                                                           [input-ref])
        select!                        (react/use-callback (fn []
                                                             (when-let [native (j/get input-ref :current)]
                                                               (j/call native :focus)
                                                               (j/call native :setSelection 0 (count value))))
                                                           [input-ref value])]
    [screen/screen {:title   "Search input"
                    :blur?   blur?
                    :preview [context/provider {:color color}
                              [:rn/view {:style preview-style}
                               [xquo/search-input {:value             value
                                                   :on-change-text    set-value!
                                                   :placeholder       placeholder
                                                   :tags              (case tag-mode
                                                                        :context (take 1 context-tags)
                                                                        :contexts context-tags
                                                                        :standard (take 1 standard-tags)
                                                                        :standards standard-tags
                                                                        :mixed [(first context-tags) (nth standard-tags 2) (last standard-tags)]
                                                                        nil)
                                                   :input-ref         input-ref
                                                   :on-clear          clear!
                                                   :on-submit-editing pressed!
                                                   :clearable?        clearable?
                                                   :disabled?         disabled?
                                                   :blur?             blur?}]]]}
     [controls/choice {:label     ":tags"
                       :value     tag-mode
                       :on-change set-tag-mode!
                       :options   [{:value nil :label "None"}
                                   {:value :context :label "One context"}
                                   {:value :contexts :label "Context tags"}
                                   {:value :standard :label "One standard"}
                                   {:value :standards :label "Standard tags"}
                                   {:value :mixed :label "Mixed"}]}]
     [controls/choice {:label     "Customization color"
                       :value     color
                       :on-change set-color!
                       :options   [{:value :color/primary :label "Primary"}
                                   {:value :color/success :label "Green"}
                                   {:value :color/danger :label "Red"}]}]
     [xquo/input {:label ":value" :value value :on-change-text set-value!}]
     [xquo/input {:label ":placeholder" :value placeholder :on-change-text set-placeholder!}]
     [xquo/button {:size 32 :type :outline :on-press focus!} "Focus search"]
     [xquo/button {:size 32 :type :outline :on-press select! :disabled? (empty? value)} "Select text"]
     [controls/checkbox {:label ":clearable?" :value clearable? :on-change set-clearable!}]
     [controls/checkbox {:label ":disabled?" :value disabled? :on-change set-disabled!}]
     [controls/checkbox {:label ":blur?" :value blur? :on-change set-blur!}]]))
