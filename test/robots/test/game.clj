(ns robots.test.game
  (:use [robots.game])
  (:use [clojure.test]))

(testing "update-game"
  (testing "join-game"
    (update-game :join-game ["Robot1"])
    (is (= (all-names) ["Robot1"]))
    )

  (testing "start-game"
    (update-game :start-game [])
    (is (= (game-status) :in-progress))
    )

  (testing "win"
    (update-game :win ["Robot1"])
    (is (= (game-status) :completed))
    (is (= (game-winner) "Robot1"))
    )
  )
