(ns informatic_tools.actions
  (:require [informatic_tools.parse :as parse]
            [informatic_tools.calculations :as calculations]
            [clojure.string :as string]
            (incanter.core)
            [incanter.charts :as charts]
            [incanter.stats :as stats]
            [compojure.response :as response]))

(defn add-result-to-results-collection
  [results-name raw-input results-collection]
  (assoc results-collection
    results-name
    (-> raw-input
        (parse/split-and-filter-blanks)
        (parse/resultize))))

(defn add-sample-group-to-sample-group-map
  [sample-group-name sample-identifiers sample-group-collection]
  (assoc sample-group-collection
    sample-group-name
    (string/split sample-identifiers #",")))

(defn calculate-t-test
  [attribute sample-group-one-name sample-group-two-name sample-group-map results]
  (let [sample-group-one-identifiers (sample-group-map sample-group-one-name)
        sample-group-two-identifiers (sample-group-map sample-group-two-name)
        sample-group-one (parse/extract-sample-group results
                                                     (set sample-group-one-identifiers)
                                                     "Sample ID")
        sample-group-two (parse/extract-sample-group results
                                                     (set sample-group-two-identifiers)
                                                     "Sample ID")
        sample-group-one-values (parse/measurement-values sample-group-one attribute)
        sample-group-two-values (parse/measurement-values sample-group-two attribute)]
    (calculations/t-test sample-group-one-values
                         sample-group-two-values)))

(defn calculate-summary-statistics
  [attribute sample-group-one-name sample-group-map results]
  (let [sample-group-one-identifiers (sample-group-map sample-group-one-name)
        sample-group-one (parse/extract-sample-group results
                                                     (set sample-group-one-identifiers)
                                                     "Sample ID")
        sample-group-one-values (parse/measurement-values sample-group-one attribute)
        q (stats/quantile sample-group-one-values)]

    {:min (first q)
     :twenty-fifth (second q)
     :fiftieth (nth q 2)
     :seventy-fifth (nth q 3)
     :max (last q)
     :mean (stats/mean q)
     :median (stats/median q)
     }))

(defn add-results
  [results-name results results-map]
  (let [r (add-result-to-results-collection results-name
                                            (str  results)
                                            results-map)]
    [r (parse/combine-results-on-identifier "Sample ID"
                                            (vals r))]))

(defn t-test-bar-chart
  [label-one value-one label-two value-two]
  (let [c (charts/bar-chart [label-one label-two]
                            (doall (map #(Double. %) [value-one value-two])) 
                            :x-label "Species"
                            :y-label "Moles")
        out-stream (java.io.ByteArrayOutputStream.)]
    (do
      (incanter.core/save c out-stream)
      (java.io.ByteArrayInputStream. (.toByteArray out-stream)))))

(defn summary-statistics-box-plot
  [attribute sample-group-one-name sample-group-map results]
  (let [sample-group-one-identifiers (sample-group-map sample-group-one-name)
        sample-group-one (parse/extract-sample-group results
                                                     (set sample-group-one-identifiers)
                                                     "Sample ID")
        sample-group-one-values (parse/measurement-values sample-group-one attribute)
        c (charts/box-plot sample-group-one-values)
        out-stream (java.io.ByteArrayOutputStream.)]
    (do
      (incanter.core/save c out-stream)
      (java.io.ByteArrayInputStream. (.toByteArray out-stream)))))