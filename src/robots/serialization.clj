(ns robots.serialization)

(use 'clojure.contrib.string)

(defn serialize [[type & args]]
  (cond
    (= type :end-of-game ) (str "END-OF-GAME: " (first args))
    :else "BOOM"))

(defn construct-error [message]
  [:error (str "Invalid Message: " message)])

(defn deserialize-join-game [message args]
  (if (nil? args)
    (construct-error message)
    [:join-game (first args)]))

(defn deserialize-win [message args]
  (if (nil? args)
    (construct-error message)
    [:win (first args)]))

(defn deserialize [message]
  (let [[type & args] (split #":" message)]
    (cond
      (= type "JOIN-GAME")(deserialize-join-game message args)
      (= type "WIN")(deserialize-win message args)
      (= type "START-GAME")[:start-game]
      :else (construct-error message)
      )))