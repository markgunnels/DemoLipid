>(ns informatic_tools.misc
  (:require [informatic_tools.parse :as parse]))

(def r18
     (-> 
         (slurp "/home/mark/projects/informatic_tools/jan18")
         (parse/split-and-filter-blanks)
         (parse/resultize)))

(def r19
     (-> 
         (slurp "/home/mark/projects/informatic_tools/jan19")
         (parse/split-and-filter-blanks)
         (parse/resultize)))

(def wt30 {:name "WT30"
           :species '("WT-30c-1" 
                           "WT-30c-2"
                           "WT-30c-3"
                           "WT-30c-4"
                           "WT-30c-5"
                           "WT-30c-6"
                           "WT-30c-7" 
                           "WT-30c-8")})

(def pf30 {:name "PF30"
           :species '("PFA4-30-1" 
                      "PFA4-30-2" 
                      "PFA4-30-3" 
                      "PFA4-30-4" 
                      "PFA4-30-5" 
                      "PFA4-30-6" 
                      "PFA4-30-7" 
                      "PFA4-30-8")})

(def wt39 {:name "WT39"
           :species '("WT-39-1" 
                      "WT-39-2" 
                      "WT-39-3" 
                      "WT-39-4" 
                      "WT-39-5" 
                      "WT-39-6" 
                      "WT-39-7" 
                      "WT-39-8")})

(def pf39 {:name "PF39"
           :species '("PFA4-39-1" 
                      "PFA4-39-2" 
                      "PFA4-39-3" 
                      "PFA4-39-4" 
                      "PFA4-39-5" 
                      "PFA4-39-6" 
                      "PFA4-39-7" 
                      "PFA4-39-8")})

(def r19-18 (parse/combine-results-on-identifier "Sample ID"  [r19 r18]))
(def wt39-sample-group (parse/extract-sample-group r19-18 (:species wt39)  "Sample ID"))
(def wt30-sample-group (parse/extract-sample-group r19-18 (:species wt30)  "Sample ID"))

(def aOH-PhytoC26-Cer-PFA30 '(3855.8 5305.4 4294.7 4095.1
                                     4607.2 5261.1 5755.3
                                     5792.9))

(def aOH-PhytoC26-Cer-PFA39 '(3911.6
                              4872.4
                              5216.2
                              5942.9
                              4874.2
                              6364.1
                              5772.2
                              6449.8))

(def aOH-PhytoC26-1-Cer-PFA30 '(24.8
                                27.9
                                20.8
                                22.1
                                25.8
                                28.8
                                27.3
                                30.8))

(def aOH-PhytoC26-1-Cer-PFA39 '(42.3
                                51.2
                                56.2
                                64.1
                                48.1
                                69.1
                                48.9
                                50.6))

(def aOH-PhytoC26-1-Cer-PFA (into aOH-PhytoC26-1-Cer-PFA30
                                  aOH-PhytoC26-1-Cer-PFA39))


(def aOH-PhytoC26-1-Cer-WT30 '( 22.2
                              24.9
                              25.6
                              30.5
                              24.8
                              27.9
                              23.3
                              29.1))

(def aOH-PhytoC26-1-Cer-WT39 '(                              49.7
                                                             54.7
                                                             49.7
                                                             48.1
                                                             43.5
                                                             38.6
                                                             41.4
                                                             39.5))

(def aOH-PhytoC26-1-Cer-WT (into aOH-PhytoC26-1-Cer-WT30
                                 aOH-PhytoC26-1-Cer-WT39))


(def aOH-PhytoC28-Cer-PFA30 '( 19.0
                               24.2
                               13.8
                               18.2
                               21.1
                               22.6
                               26.1
                               24.9))


(def aOH-PhytoC28-Cer-PFA39 '( 28.6
                               32.1
                               35.4
                               39.0
                               32.2
                               39.4
                               32.4
                               38.0))

