(ns informatic_tools.views
  (:use (hiccup core)
        (hiccup page-helpers form-helpers)
        (sandbar stateful-session))
  (:require [informatic_tools.config :as config]
            [informatic_tools.parse :as parse]
            [clojure.string :as string]))

(defn wrap-in-layout
  [title body message error]
  (html
   [:html
    [:head
     [:title config/SITE-TITLE (when title (str " - " title))]
     (include-css "/public/css/style.css")
     (include-js "/public/js/informatics.js")
     (include-js
      "https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js")]
    [:body
     [:center
      [:div#wrapper
      (when message [:div.message message])
      (when error [:div.error error])
      [:div#header
       [:h1
        (link-to config/SITE-URL config/SITE-TITLE)]
       [:div.desc
        (link-to config/SITE-URL config/SITE-DESCRIPTION)]]
      [:div#menubar
       (link-to "/results/new" "Add Results") "&nbsp;|&nbsp;"
       (link-to "/sample-groups/new" "Add Sample Group") "&nbsp;|&nbsp;"
       (link-to "/calculations/t-test/new" "Perform T-Test") "&nbsp;|&nbsp;"
       (link-to "/calculations/summary-statistics/new" "Summary Statistics")]
      [:div#content
       [:div.content body]]]]
     ]]))

(defn results-summary
  [results]
  (let [attributes (doall (sort (parse/list-attributes
                                 results
                                 config/SAMPLE-IDENTIFIER-ATTRIBUTE-NAME))) 
        sample-identifiers (doall (sort (parse/list-sample-identifiers
                                         results
                                         config/SAMPLE-IDENTIFIER-ATTRIBUTE-NAME)))]
    [:div#table
     [:table
      [:tr
       [:td#column-label config/SAMPLE-IDENTIFIER-ATTRIBUTE-NAME]
       (for [attribute attributes]
         [:td#column-label attribute])]
      (for [sample-identifier sample-identifiers]
        (let [sample-result (parse/sample-result results
                                                 config/SAMPLE-IDENTIFIER-ATTRIBUTE-NAME
                                                 sample-identifier)]
          [:tr#results-row
           [:td sample-identifier]
           (for [attribute attributes]
             [:td (sample-result attribute)])]))]]))

(defn summary
  [results sample-group]
  (wrap-in-layout
   "Home"
   [:div#menu
    [:div#results (results-summary results)]
    [:div#sample-group (str sample-group)]]
   nil
   nil))

(defn new-results-form
  []
  (wrap-in-layout
   "Add New Results"
   [:div#new-results
    (form-to [:post "/results/"]
             [:fieldset
              [:legend "Add New Results"]
              [:ol
               [:li
                (label "results-name" "Results Name")
                (text-field "results-name")  
                ]
               [:li
                (label "results" "Results")
                (text-area "results")
                ]]]
             (submit-button "Add Results"))]
   nil
   nil))

(defn new-sample-group-form
  []
  (wrap-in-layout
   "Add New Sample Group"
   [:div#new-sample-group
    (form-to [:post "/sample-groups/"]
             [:fieldset
              [:legend "Add New Sample Group"]
              [:ol
               [:li
                (label "sample-group-name" "Sample Group Name")
                (text-field "sample-group-name")  
                ]
               [:li
                (label "sample-group-identifiers" "Sample Group Identifiers")
                (text-area "sample-group-identifiers")
                ]]]
             (submit-button "Add Sample Group"))]
   nil
   nil))

(defn t-test-form
  [results sample-group-map]
  (wrap-in-layout
   "Run T-Test"
   [:div#form
    (form-to [:post "/calculations/t-test"]
             [:fieldset
              [:legend "T-Test" ]
              [:ol
               [:li
                (label "sample-group-one-name" "Sample Group Name")
                (drop-down "sample-group-one-name"
                           (sort (keys sample-group-map)))]
               [:li
                (label "sample-group-two-name" "Sample Group Name")
                (drop-down "sample-group-two-name"
                           (sort (keys sample-group-map)))]
               [:li
                (label "attribute"
                       "Attribute") 
                (drop-down "attribute"
                           (sort (parse/list-attributes results
                                                        "Sample ID")))]]]
             
             (submit-button "Run T-Test"))]
   nil
   nil))

(defn summary-statistics-form
  [results sample-group-map]
  (wrap-in-layout
   "Run T-Test"
   [:div#form
    (form-to [:post "/calculations/summary-statistics/"]
             [:fieldset
              [:legend "Summary Statistics" ]
              [:ol
               [:li
                (label "sample-group-one-name" "Sample Group Name")
                (drop-down "sample-group-one-name"
                           (sort (keys sample-group-map)))]
               [:li
                (label "attribute"
                       "Attribute") 
                (drop-down "attribute"
                           (sort (parse/list-attributes results
                                                        "Sample ID")))]]]
             
             (submit-button "Run Summary Statistics"))]
   nil
   nil))

(defn map-to-vertical-table
  [m]
  [:table
   (let [s (sort (seq m))]
     (for [pair s]
       [:tr
      [:td#cell-label
       (first pair)]
      [:td#cell-value
       (str (second pair))]]))])

(defn t-test-results
  [t-test-results
   sample-group-one-name
   sample-group-two-name]
  (println t-test-results)
  (wrap-in-layout
   "T-Test Results"
   [:div#t-test-results
    [:div
     (map-to-vertical-table t-test-results)]
     [:div
    (image (str "/bar-chart?" (string/join "&"
                                           [(str "label-one=" sample-group-one-name)
                                            (str "label-two=" sample-group-two-name) 
                                            (str "value-one=" (:x-mean t-test-results))
                                            (str "value-two=" (:y-mean t-test-results))])))]]
   
   nil
   nil))

(defn summary-statistics-results
  [attribute
   sample-group-one-name
   summary-statistics-results]
  (wrap-in-layout
   "Summary Statistics Results"
   [:div#summary-statistics-results
    [:div
     (map-to-vertical-table summary-statistics-results)]
    [:div
     (image (str "/box-plot?" (string/join "&"
                                           [(str "sample-group-one-name=" sample-group-one-name)
                                            (str "attribute=" attribute)])))]]
   nil
   nil))



