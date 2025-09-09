(ns utils.worklets.chat.messenger.messages)

(def ^:private worklets #js{})

(defn messages-list-on-scroll
  [distance-from-list-top chat-list-scroll-y callback]
  (.messagesListOnScroll ^js worklets distance-from-list-top chat-list-scroll-y callback))
