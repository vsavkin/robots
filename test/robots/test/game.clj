(ns robots.test.game
  (:use [robots.game])
  (:use [clojure.test]))

(testing "update-game"
  (deftest join-game-test
    (update-game :join-game ["Robot1"])
    (is (= (all-names) ["Robot1"]))
    )

  (deftest start-game-test
    (update-game :start-game [])
    (is (= (game-status) :in-progress))
    )

  (deftest win-test
    (update-game :win ["Robot1"])
    (is (= (game-status) :completed))
    (is (= (game-winner) "Robot1"))
    )
  )
