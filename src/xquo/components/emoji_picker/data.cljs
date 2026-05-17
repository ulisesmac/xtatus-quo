(ns xquo.components.emoji-picker.data
  (:require
   [cljs-bean.core :refer [->clj]]
   [clojure.string :as string]
   [react-native.utils :as rn.utils]))

(def group-smileys-emotion 0)
(def group-people-body 1)
(def group-animals-nature 3)
(def group-food-drink 4)
(def group-travel-places 5)
(def group-activity 6)
(def group-objects 7)
(def group-symbols 8)
(def group-flags 9)

(def valid-groups
  #{group-smileys-emotion
    group-people-body
    group-animals-nature
    group-food-drink
    group-travel-places
    group-activity
    group-objects
    group-symbols
    group-flags})

(def ^:private search-gram-size 2)

(defn normalize-search-text
  [value]
  (-> (or value "")
      str
      string/lower-case
      string/trim
      (.normalize "NFD")
      (string/replace #"[\u0300-\u036f]" "")))

(defn search-grams
  [search-text]
  (let [length (count search-text)]
    (if (< length search-gram-size)
      #{}
      (loop [index 0
             grams (transient #{})]
        (if (<= (+ index search-gram-size) length)
          (recur (inc index) (conj! grams (subs search-text index (+ index search-gram-size))))
          (persistent! grams))))))

(defn- emoji-search-text
  [{:keys [label tags] :as emoji}]
  (assoc emoji :search-text (string/join " " (cons label tags))))

(def emoji-data
  (->> (rn.utils/asset-require "emojis/es.json")
       (->clj)
       (filter (comp valid-groups :group))
       (map emoji-search-text)
       (vec)))

(def emoji-search-index
  (reduce (fn [index emoji]
            (reduce (fn [index gram]
                      (update index gram (fnil conj []) emoji))
                    index
                    (search-grams (:search-text emoji))))
          {}
          emoji-data))

(def ^:private random-emoji-data
  (remove #(let [group (:group %)]
             (or (= group group-flags)
                 (= group group-symbols)))
          emoji-data))

(defn random-emoji
  []
  (:unicode (rand-nth random-emoji-data)))

(def categories
  [{:title :t/emoji-people ;; 0 and 1
    :icon  :i/faces
    :id    :people
    :data  []}
   {:title :t/emoji-nature ;; 3
    :icon  :i/nature
    :id    :nature
    :data  []}
   {:title :t/emoji-food ;; 4
    :icon  :i/food
    :id    :food
    :data  []}
   {:title :t/emoji-activity ;; 6
    :icon  :i/activity
    :id    :activity
    :data  []}
   {:title :t/emoji-travel ;; 5
    :icon  :i/travel
    :id    :travel
    :data  []}
   {:title :t/emoji-objects ;; 7
    :icon  :i/objects
    :id    :objects
    :data  []}
   {:title :t/emoji-symbols ;; 8
    :icon  :i/symbols
    :id    :symbols
    :data  []}
   {:title :t/emoji-flags ;; 9
    :icon  :i/flags
    :id    :flags
    :data  []}])

(defn emoji-group->category
  [group]
  (condp = group
    group-smileys-emotion {:index 0 :id :people}
    group-people-body     {:index 0 :id :people}
    group-animals-nature  {:index 1 :id :nature}
    group-food-drink      {:index 2 :id :food}
    group-travel-places   {:index 4 :id :travel}
    group-activity        {:index 3 :id :activity}
    group-objects         {:index 5 :id :objects}
    group-symbols         {:index 6 :id :symbols}
    group-flags           {:index 7 :id :flags}
    nil))

(def ^:private categorized-and-partitioned
  (->> emoji-data
       (reduce (fn [acc {:keys [group] :as emoji}]
                 (update-in acc [(-> group emoji-group->category :index) :data] conj emoji))
               categories)
       (reduce (fn [acc {:keys [data] :as item}]
                 (conj acc (assoc item :data (partition-all 7 data))))
               [])))

(def flatten-data
  (vec
   (mapcat (fn [{:keys [title id data]}]
             (into [{:title title :id id :header? true}] data))
           categorized-and-partitioned)))

(def ^:private section-header-indexes
  (into {}
        (keep-indexed #(when (:header? %2)
                         {(:id %2) %1})
                      flatten-data)))
