(ns robots.game
  (:use (robots notifier)))

(def robots (ref {}))
(def status (ref :waiting-for-robots))
(def winner (ref nil))

(defn join-game [args]
  (dosync
    (ref-set robots (assoc @robots (first args) {}))
    (if (= 1 (count @robots))
      (def main-robot (first (keys @robots))))
))

(defn update-game-status [st]
  (dosync
    (ref-set status st)))

(defn run-event [args]
  (dosync
    (ref-set winner (first args))
    (update-game-status :completed)))

(defn update-game [type args]
  (cond
    (= type :join-game)(join-game args)
    (= type :start-game)(update-game-status :in-progress)
    (= type :win)(run-event args)
    :else  (notify notifier "ERROR!")))

(defn all-names []
  (keys @robots))

(defn game-status [] @status)

(defn game-winner [] @winner)

(defn game-is-over? []
  (= (game-status) :completed))


