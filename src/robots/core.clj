(ns robots.core
  (:use [robots notifier server queue game])
  (:import (robots.queue RobotsConnection)))

(defn play-game []
  (def c1 (create-topic-connection {:host "localhost"} "amqp-server-to-client"))
  (def c2 (create-topic-connection {:host "localhost"} "amqp-server-to-client"))

  (.start (Thread. #(while true
                      (println (read-message c1)))))

  (deftype PrintlnNotifier []
    Notification
    (notify [_ message]
      (println message)))

  (def amqp-server-to-client (create-topic-connection {:host "localhost"} "amqp-server-to-client"))

  (deftype AMQPNotifier []
    Notification
    (notify [_ message]
      (send-message amqp-server-to-client message)))

  (set-notifier (AMQPNotifier.))

  (def client-to-server (create-robots-connection {:host "localhost"} "client-to-server"))

  (while (not (game-is-over?))
    (server (read-message client-to-server)))

  (close-robots-connection client-to-server))

