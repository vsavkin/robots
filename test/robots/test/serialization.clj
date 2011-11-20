(ns robots.test.serialization
  (:use [robots.serialization])
  (:use [clojure.test]))

(testing "Serialize"
  (testing "end of game"
    (is (= (serialize [:end-of-game "Robot1"]) "END-OF-GAME: Robot1"))
    ))

(testing "Deserialize"

  (testing "join game"

    (testing "parses message"
      (is (= (deserialize "JOIN-GAME:Robot1") [:join-game "Robot1"])))

    (testing "returns errors when message is invalid"
      (is (= (deserialize "JOIN-GAME:") [:error "Invalid Message: JOIN-GAME:"])))
  )

  (testing "start game"
    (testing "parses message"
      (is (= (deserialize "START-GAME") [:start-game])))
  )

  (testing "win"

    (testing "parses message"
      (is (= (deserialize "WIN:Robot1") [:win "Robot1"])))

    (testing "returns errors when message is invalid"
      (is (= (deserialize "WIN:") [:error "Invalid Message: WIN:"])))
    )

  (testing "invalid messages"
    (is (= (deserialize "INVALID") [:error "Invalid Message: INVALID"])))
  )
