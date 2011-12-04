(ns robots.notifier)

(defprotocol Notification
  (notify [this message]))

(defn set-notifier [notifier]
  (def notifier notifier))
