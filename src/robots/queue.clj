(ns robots.queue)

(use 'com.mefesto.wabbitmq)

(def credentials {:host "localhost" :username "guest" :password "guest"})
(def client-to-server-exchange "client-to-server.exchange")
(def client-to-server-queue "client-to-server.queue")
(def client-to-server-routing-key "client-to-server")

(def server-to-client-exchange "server-to-client.exchange")
(def server-to-client-queue "server-to-client.queue")
(def server-to-client-routing-key "server-to-client")

;(defn create-connection [exchange queue routing-key connection-type]
;  {
;
;    }
;  )

(defn initialize []
  (with-broker credentials
    (with-channel
      (exchange-declare client-to-server-exchange "direct")
      (queue-declare client-to-server-queue)
      (queue-bind client-to-server-queue client-to-server-exchange client-to-server-routing-key))

    (with-channel
      (exchange-declare server-to-client-exchange "topic")
      (queue-declare server-to-client-queue)
      (queue-bind server-to-client-queue server-to-client-exchange server-to-client-routing-key))
    ))

(defn purge-all-queues []
  (dotimes [n 100] (read-message)))

(defn send-message [message]
  (with-broker credentials
    (with-channel
      (with-exchange client-to-server-exchange
        (publish client-to-server-routing-key (.getBytes message))))))

(defn broadcast-message [message]
  (with-broker credentials
    (with-channel
      (with-exchange server-to-client-exchange
        (publish server-to-client-routing-key (.getBytes message))))))

(defn parse-message [message]
  (if (nil? message)
    nil
    (String. (:body message))))

(defn read-message []
  (with-broker credentials
    (with-channel
      (with-queue client-to-server-queue
        (parse-message (first (consuming-seq true 10)))))))

(defn register-listener [listener]
  (try
    (with-broker credentials
      (with-channel
        (with-queue client-to-server-queue
          (doseq [message (consuming-seq true)]
            (listener (parse-message message))))))
    (catch Throwable t)))
