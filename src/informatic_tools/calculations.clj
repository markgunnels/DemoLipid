(ns informatic_tools.calculations
  (:require [informatic_tools.parse :as parse]
            [incanter.stats :as stats]
            (incanter core)))

(defn outlier-bounds
  "Calculates the outlier bounds"
  [dataset]
  (let [[qMin q25 q50 q75 qMax] (stats/quantile dataset)]
    [(- q25 (* 1.5 (- q75 q25)))
     (+ q75 (* 1.5 (- q75 q25)))])) 

(defn exclude-outliers
  "Filters outliers out of a sample-group."
  [dataset]
  (let [outlier-range (outlier-bounds dataset)]
    (filter #(and (> % (first outlier-range))
                  (< % (second outlier-range))) dataset)))

(defn t-test
  "Runs a t-set on the provided datasets"
  [dataset-one dataset-two]
  ;(println dataset-one)
  ;(println "*************************************")
  ;(println dataset-two)
  (stats/t-test dataset-one :y dataset-two))

;(defun false-discovery-correction (p-values &key (rate 0.05))
;  (let ((number-of-tests (length p-values))
;        (sorted-p-values (sort p-values #'>)))
;    (do ((p-value (pop sorted-p-values) (pop sorted-p-values))
;         (tests-to-go number-of-tests (1- tests-to-go)))
;        ((or (null p-value)
;             (<= p-value (* rate (/ tests-to-go number-of-tests))))
;                  (values tests-to-go (* rate (/ tests-to-go number-of-tests)))))))

;recurse
;((p-value (pop sorted-p-values) (pop sorted-p-values))
;         (tests-to-go number-of-tests (1 - tests-to-go)))

(defn false-discovery-correction
  [p-values] 
  (let [number-of-tests (count p-values)
        sorted-p-values (sort > p-values)
        rate 0.05]
    (loop [remaining-sorted-p-values sorted-p-values
           p-value (first remaining-sorted-p-values)
           tests-to-go (Integer. number-of-tests)]
      (if-not (or (nil? p-value)
                  (<= p-value (* rate (/ tests-to-go number-of-tests))))
        (recur (rest remaining-sorted-p-values)
               (first (rest remaining-sorted-p-values))
               (- tests-to-go 1))
        [tests-to-go (* rate (/ tests-to-go number-of-tests))]))))
