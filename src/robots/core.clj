(ns robots.core)
(use 'robots.notifier)
(use 'robots.server)
(use 'robots.queue)
(use 'robots.game)
(import 'robots.queue.RobotsConnection)

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

(send-message client-to-server "JOIN-GAME:Robot1")
(send-message client-to-server "JOIN-GAME:Robot2")
(send-message client-to-server "JOIN-GAME:Robot3")
(send-message client-to-server "START-GAME")
(send-message client-to-server "WIN:Robot2")

(while (not (game-is-over?))
  (server (read-message client-to-server)))

(close-robots-connection client-to-server)



