(ns robots.test.game
  (:use [robots.game])
  (:use [clojure.test]))

(defn clean-environment []
  (dosync (ref-set robots [])
          (ref-set main-robot nil)))

(deftest join-game-test
  (clean-environment)
  (update-game :join-game "Robot1")
  (is (= (all-names) ["Robot1"]))
  )

(deftest set-main-robot-test
  (clean-environment)
  (update-game :join-game "Main Robot")
  (is (= (main-robot-name) "Main Robot")))

(deftest start-game-test
  (clean-environment)
  (update-game :start-game [])
  (is (= (game-status) :in-progress))
  )

(deftest win-test
  (clean-environment)
  (update-game :win ["Robot1"])
  (is (= (game-status) :completed))
  (is (= (game-winner) "Robot1"))
  )

(deftest location-test
  (clean-environment)
  (update-game :join-game "Robot1")
  (update-game :start-game [])
  (is (not (= (generate-field) nil)))
  (is (= (generate-field ) ))
  )