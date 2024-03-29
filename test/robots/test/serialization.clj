(ns robots.test.serialization
  (:use [robots.serialization])
  (:use [clojure.test]))

(testing "Serialize"
  (deftest end-of-game-test
    (is (= (serialize [:end-of-game "Robot1"]) "END-OF-GAME: Robot1"))
    )

  (deftest update-test
    (let [field {"Robot1" {:position [0,0] :status :alive}
                 "Robot2" {:position [0,1] :status :alive}}]
      (is (= (serialize [:update-game field]) "FIELD:Robot1,alive,0,0;Robot2,alive,0,1"))
      )
    )
  )

(testing "Deserialize"

  (testing "join game"

    (deftest parses-message-test
      (is (= (deserialize "JOIN-GAME:Robot1") [:join-game "Robot1"])))

    (deftest returns-errors-when-message-is-invalid-test
      (is (= (deserialize "JOIN-GAME:") [:error "Invalid Message: JOIN-GAME:"])))
  )

  (testing "start game"
    (deftest parses-message-test
      (is (= (deserialize "START-GAME") [:start-game])))
  )

  (testing "win"

    (deftest parses-message-test
      (is (= (deserialize "WIN:Robot1") [:win "Robot1"])))

    (deftest returns-errors-when-message-is-invalid-test
      (is (= (deserialize "WIN:") [:error "Invalid Message: WIN:"])))
    )

  (deftest invalid-messages-test
    (is (= (deserialize "INVALID") [:error "Invalid Message: INVALID"])))
  )
