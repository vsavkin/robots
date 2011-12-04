(ns robots.queue)

(use 'com.mefesto.wabbitmq)
(import 'com.rabbitmq.client.ConnectionFactory)
(import 'com.rabbitmq.client.Connection)
(import 'com.rabbitmq.client.Channel)
(import 'com.rabbitmq.client.QueueingConsumer)

(defprotocol ConnectionProtocol
  (get-connection [this])
  (get-channel [this])
  (send-message [this message])
  (read-message [this]))

(deftype RobotsConnection [connection channel consumer exchange-name queue-name]
  ConnectionProtocol

  (get-connection [this] connection)

  (get-channel [this] channel)

  (send-message [this message]
    (.basicPublish channel exchange-name "" nil (.getBytes message)))

  (read-message [message]
    (String. (.getBody (.nextDelivery consumer))))
 )

(defn create-robots-connection [credentials exchange-name]
  (let [connection (.newConnection (doto (ConnectionFactory.) (.setHost (:host credentials))))
        channel (.createChannel connection)
        exchange (.exchangeDeclare channel exchange-name "direct")
        queue-name (.getQueue (.queueDeclare channel))
        consumer (QueueingConsumer. channel)
        ]
    (.queueBind channel queue-name exchange-name "")
    (.basicConsume channel queue-name true consumer)
    (RobotsConnection. connection channel consumer exchange-name queue-name)
    )
  )

(defn close-robots-connection [robots-connection]
  (.close (get-channel robots-connection))
  (.close (get-connection robots-connection)))


(def client-connections (ref []))

(defn create-client-connection [credentials exchange-name]
  (let [cc (create-robots-connection credentials exchange-name)]
    (dosync
      (ref-set client-connections  (cons cc @client-connections))
      )
  ))

(defn close-client-connections []
  (dosync
    (doseq [c @client-connections]
      (println (str "closing .." c))
      (close-robots-connection c))))

(defn broadcast-message [message]
  (dosync
    (doseq [c @client-connections]
      (send-message c message))))
