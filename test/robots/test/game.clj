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
  (start-game)
  (is (= (game-status) :in-progress))
  (is (not (= (game-field) nil)))
  )

(deftest win-test
  (clean-environment)
  (update-game :win ["Robot1"])
  (is (= (game-status) :completed))
  (is (= (game-winner) "Robot1"))
  )

(deftest robot-move-test
  (is (= (robot-move {:direction :north :position [0,0]} [])
         {:position [0,1] :direction :north
          }))
  (is (= (robot-move {:direction :east :position [0,0]} [])
         {:position [1,0] :direction :east
          }))
  (is (= (robot-move {:direction :west :position [0,0]} [])
         {:position [-1,0] :direction :west}))
  (is (= (robot-move {:direction :south :position [0,0]} [])
         {:position [0,-1] :direction :south}))
  (is (= (robot-move {:direction :south :position [0,0]} [[0,-1]]) {:position [0,0] :direction :south}))
  )


(deftest robot-rotate-test
  (is (= (robot-rotate {:direction :north} :left) {:direction :west}))
  )

;; (deftest move-test
;;   (clean-environment)
;;   (let [old-field {"Robot1" {:position [0,0] :direction :north}}
;;         new-field (game-move old-field "Robot1" :move)
;;         ]
;;     (is (= ((new-field "Robot1") :position)) [0,1])
;;     (is (= ((new-field "Robot1") :direction)) :north)
;;     )
;;   )

(deftest generate-position-test
  (is (= (count (generate-position [])) 2))
  (is (not (= (generate-position [[0,0]]) [0,0])))
  )

(deftest generate-field-test
  (clean-environment)
  (update-game :join-game "Robot1")
  (update-game :join-game "Robot2")
  (update-game :start-game [])
  (let [field (generate-field (all-names))]
    (is (= (count field) 2))
    (is (= (:status (field "Robot1")) :alive))
    (is (= (:status (field "Robot2")) :alive))
    (is (not (= (:position (field "Robot1")) nil)))
    (is (not (= (:position (field "Robot2")) nil)))
    )
  )