(ns informatic_tools.parse
  (:require (clojure set)
            [clojure.string :as string]
            [clojure.contrib.seq :as seq]))

(defn split-and-filter-blanks
  "Splits the raw text on new lines and
   eliminates lines that are blank."
  [raw-text]
  (filter (complement string/blank?)
          (string/split raw-text #"\n")))

(defn split-on-tabs
  "Splits a line of text on tabs."
  [line]
  (string/split line #"\t"))

(defn resultize
  "Takes the set of lines and turn thems into maps
   using the first line as a the keys with the rest
   of lines as a values."
  [lines]
  (let [header (split-on-tabs (first lines))]
    (for [line (rest lines)]
      (zipmap header (split-on-tabs line)))))

(defn combine-results-on-identifier
  "Takes an arbritrary set of records and composes
   them into results based on their identifier."
  [identifier-attribute-name records]
  (let [combined-records (reduce into (doall records))
        grouped-by (group-by #(% identifier-attribute-name) combined-records)]
    (map #(merge (first (second %)) (second (second %)) ) grouped-by)))

(defn extract-sample-group
  "Give results and a set of sample-identifiers only returns
   those result entries"
  [results sample-group-identifiers identifier-attribute-name]
  (filter #(seq/includes? sample-group-identifiers (% "Sample ID")) results))

(defn list-attributes
  "Captures all the attributes contained within the results."
  [results identifier-attribute-name]
  (filter (partial not= identifier-attribute-name) (first (distinct (map keys results)))))

(defn list-sample-identifiers
  "Captures all the attributes contained within the results."
  [results identifier-attribute-name]
  (map #(% identifier-attribute-name) results))

(defn sample-result
  [results identifier-attribute-name identifier-attribute]
  (first (filter #(= (% identifier-attribute-name) identifier-attribute) results)))

(defn measurement-values
  "Retrieves all the measurement values for the attribute provided."
  [results attribute]
  (map #(Double. %) (filter (partial not= "BDL") (map #(% attribute) results))))

(defn measurement-value
  [results identifier-attribute-name identifier-attribute attribute]
  (sample-result results identifier-attribute-name identifier-attribute) attribute)