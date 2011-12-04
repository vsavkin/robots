(ns robots.queue)

(import (com.rabbitmq.client ConnectionFactory Connection Channel QueueingConsumer))

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

(defn create-topic-connection [credentials exchange-name]
  (let [connection (.newConnection (doto (ConnectionFactory.) (.setHost (:host credentials))))
        channel (.createChannel connection)
        exchange (.exchangeDeclare channel exchange-name "fanout")
        queue-name (.getQueue (.queueDeclare channel))
        consumer (QueueingConsumer. channel)
        ]
    (.queueBind channel queue-name exchange-name "")
    (.basicConsume channel queue-name true consumer)
    (RobotsConnection. connection channel consumer exchange-name queue-name)
    ))

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


