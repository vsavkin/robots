(ns robots.test.game
  (:use [robots.game])
  (:use [clojure.test]))


(deftest join-game-test
  (update-game :join-game ["Robot1"])
  (is (= (all-names) ["Robot1"]))
  )

(deftest set-main-robot-test
  (update-game :join-game ["Main Robot"])
  (is (= main-robot "Main Robot")))

(deftest start-game-test
  (update-game :start-game [])
  (is (= (game-status) :in-progress))
  )

(deftest win-test
  (update-game :win ["Robot1"])
  (is (= (game-status) :completed))
  (is (= (game-winner) "Robot1"))
  )

