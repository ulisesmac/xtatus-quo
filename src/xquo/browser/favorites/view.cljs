(ns xquo.browser.favorites.view
  (:require [react-native.utils :as rn.utils]
            [xquo.browser.dapp-item.view :as dapp-item]
            [xquo.browser.favorites.style :as style]))

(defn favorites [{:keys [items] :as props}]
  (into [:rn/scroll-view (-> props
                             (dissoc :items)
                             (assoc :horizontal                        true
                                    :shows-horizontal-scroll-indicator false
                                    :style                             (rn.utils/add-styles style/container (:style props))
                                    :content-container-style           (rn.utils/add-styles style/content (:content-container-style props))))]
        (map (fn [item]
               [dapp-item/dapp-item (dissoc item :key)]))
        items))
