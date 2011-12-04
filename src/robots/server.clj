(ns robots.server)
(use 'robots.queue)
(use 'robots.game)
(use 'robots.serialization)
(use 'robots.notifier)

(defn print-game-status []
  (cond
    (= (game-status) :in-progress)(notify notifier "game is in progress")
    (= (game-status) :completed)(notify notifier (str "winner is " (game-winner)))
    )
  )


(defn tick [type args]
  (do
    (update-game type args)
    (print-game-status)
  ))

(defn server [message]
  (let [[type & args] (deserialize message)]
    (if (= type :error) (notify notifier "Error!") 
      (tick type args))))
