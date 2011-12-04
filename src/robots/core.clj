(ns robots.core)

(use 'com.mefesto.wabbitmq)
(use 'robots.server)
(use 'robots.queue)
(import 'robots.queue.RobotsConnection)

;(initialize)
;(initialize-client "client1")
;(initialize-client "client2")

;(purge-all-queues)

;(def rc (create-server-robots-connection {:host "localhost"} "client-to-server"))

;(send-message rc "my secret message")
;(println "after I sent a message")
;(println (read-message rc))
;(println "bbb")


;(def c (RobotsConnection. {:host "localhost"} 2 3 4))
;
;(println (open c))
;(send-message c "message")

;(defn client-1 [message]
;  (println (str "client 1 received " message)))
;
;(defn client-2 [message]
;  (println (str "client 2 received " message)))
;



(def c1 (create-client-connection {:host "localhost"} "boomsp"))
(def c2 (create-client-connection {:host "localhost"} "boomsp"))

(println @client-connections)

(close-client-connections)

;(def server- (create-robots-connection {:host "localhost"} "client-to-server"))


;(server-send-message "hello")
;
;(client-register-listener "client1" client-1)
;(client-register-listener "client2" client-2)

;(send-message "JOIN-GAME:Robot1")
;(send-message "JOIN-GAME:Robot2")
;(send-message "JOIN-GAME:Robot3")
;(send-message "START-GAME")
;(send-message "WIN:Robot2")
;
;(register-listener server)
;


