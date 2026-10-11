(ns xquo.worklets.notification
  (:require [react-native.utils :refer [asset-require]]))

(def ^:private worklets (asset-require "xquo/worklets/notification_content.js"))
(def overlay (asset-require "xquo/worklets/notification_overlay.js"))

(def use-notification-motion (.-useNotificationMotion worklets))
