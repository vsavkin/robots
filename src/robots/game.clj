(ns robots.game
  (:use (robots notifier)))

(def robots (ref []))
(def main-robot (ref nil))
(def status (ref :waiting-for-robots))
(def winner (ref nil))

(defn main-robot-name []
  @main-robot
  )

(defn join-game [robot & args]
  (dosync
    (ref-set robots (cons robot @robots))
    (if (= 1 (count @robots))
      (ref-set main-robot (first @robots)))
))

(defn generate-field []
 [])

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
  @robots)

(defn game-status [] @status)

(defn game-winner [] @winner)

(defn game-is-over? []
  (= (game-status) :completed))


