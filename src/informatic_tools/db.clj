(ns informatic_tools.db
  (:refer-clojure
   :exclude [compile take drop sort distinct conj! disj! case])        
  (:use (clojureql core predicates))
  (:require [informatic_tools.config :as config]))

(defn find-samples-by-identifier
  [identifier]
  (:id (first @(-> (table config/*db* :samples)
                   (select (where (= :identifier identifier)))))))   

(defn store-lipidomics-species-measurement
  [sample-identifier lipid-species value]
  (conj! (table config/*db* :lipid_species_results)
         {:lipid_species lipid-species
          :value value
          :sample_id (find-samples-by-identifier sample-identifier)}))   

