(ns robots.server)
(use 'robots.queue)
(use 'robots.game)
(use 'robots.serialization)

(defn print-game-status []
  (cond
    (= (game-status) :in-progress)(println "game is in progress")
    (= (game-status) :completed)(println (str "winner is " (game-winner)))
    )
  )

(defn end-game-if-it-is-completed []
  (if (= (game-status) :completed)
    (throw (Exception. "Game is Over")) nil))

(defn tick [type args]
  (do
    (update-game type args)
    (print-game-status)
    (end-game-if-it-is-completed)
  ))

(defn server [message]
  (let [[type & args] (deserialize message)]
    (if (= type :error)(println "Error!")
      (tick type args))))
