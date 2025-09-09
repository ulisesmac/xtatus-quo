(ns utils.worklets.chat.messenger.composer)

(def ^:private worklets #js{})

(defn scroll-down-button-opacity
  [chat-list-scroll-y composer-focused? window-height]
  (.scrollDownButtonOpacity ^js worklets chat-list-scroll-y composer-focused? window-height))
