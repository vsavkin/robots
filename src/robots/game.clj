(ns robots.game
  (:use (robots notifier)))

(use 'clojure.set)

(def robots (ref []))
(def main-robot (ref nil))
(def status (ref :waiting-for-robots))
(def winner (ref nil))
(def field (ref nil))

(defn main-robot-name []
  @main-robot
  )

(defn join-game [robot & args]
  (dosync
    (ref-set robots (cons robot @robots))
    (if (= 1 (count @robots))
      (ref-set main-robot (first @robots)))
))

(defn generate-position [positions]
  (let [
        field-size (+ 2 (count positions))
        x (rand-int field-size)
        y (rand-int field-size)
        ]
    
    (if (some #(= [x,y] %) positions)
      (generate-position positions)
      [x, y])))

(defn generate-field-element [field robot]
  (let [all-positions (map :position field)]
   (assoc field robot {:status :alive
                       :position (generate-position all-positions)
                       })))

(defn generate-field [robots]
  (reduce generate-field-element {} robots)
  )

(defn update-game-status [st]
  (dosync
    (ref-set status st)))

(defn run-event [args]
  (dosync
    (ref-set winner (first args))
    (update-game-status :completed)))

(defn game-field []
  @field
  )

(defn all-names []
  @robots)

(defn start-game []
  (update-game-status :in-progress)
  (dosync
   (ref-set field (generate-field (all-names))))
  )

(defn robot-move [attrs positions]
  (let [position (attrs :position)
        direction (attrs :direction)
        x (first position)
        y (second position)
        new-position (cond
                      (= direction :north) [x, (+ 1 y)]
                      (= direction :south) [x, (- y 1)]
                      (= direction :west) [(- x 1), y]
                      (= direction :east) [(+ x 1), y]
                      )
        ]
    (if (some #(= new-position %) positions) attrs
        (assoc attrs :position new-position))
    
    )
  )

(defn robot-rotate [attrs rotation]
  (let [direction (attrs :direction)]
    (cond
     (= rotation :left) (cond
                         (= direction :north) (assoc attrs :direction :west)
                         (= direction :east) (assoc attrs :direction :north)
                         (= direction :south) (assoc attrs :direction :east)
                         (= direction :west) (assoc attrs :direction :south)
                         )
     (= rotation :right) (cond
                         (= direction :north) (assoc attrs :direction :east)
                         (= direction :east) (assoc attrs :direction :south)
                         (= direction :south) (assoc attrs :direction :west)
                         (= direction :west) (assoc attrs :direction :north)
                         )
     )
    )
  )

(defn between [value from to]
  (and (>= value from) (<= value to))
  )

(defn robot-position-in-range [position x-range y-range]
  (let [x (first position) y (second position)]
    (and (between x (first x-range) (second x-range))
     (between y (first y-range) (second y-range))
     )))

(defn select-robots [field x-range y-range]
  (select #(robot-position-in-range ((second %) :position) x-range y-range) field))

(defn closest-robot [direction robots]
  (cond
   (= direction :north) (first (first (min-key #(second ((second %) :position)) robots)))
   )
  )

(defn robot-shoot-internal [attrs field]
  (let [direction (attrs :direction)
        x (first (attrs :position))
        y (second (attrs :position))
        ]
    (cond
     (= direction :north) (closest-robot direction (select-robots field [x x] [(+ 1 y) 1000000]))
     
     )
    )
  )

(defn robot-shoot [attrs field]
  (println (robot-shoot-internal attrs field))
  (robot-shoot-internal attrs field)
  )

 (defn game-move [old-field robot-name operation]
  (let [robot (old-field robot-name)]
    (cond
     (= operation :move) (robot-move :direction)
     )
    )
  
  )

(defn update-game [type args]
  (cond
   (= type :join-game) (join-game args)
   (= type :start-game) (start-game)
   (= type :win) (run-event args)
   :else  (notify notifier "ERROR!")))

(defn game-status [] @status)

(defn game-winner [] @winner)

(defn game-is-over? []
  (= (game-status) :completed))


