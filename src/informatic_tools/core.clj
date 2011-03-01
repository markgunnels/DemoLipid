(ns informatic_tools.core
  (:use (compojure core)
        (sandbar stateful-session))
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as response] 
            [informatic_tools.views :as views]
            [informatic_tools.actions :as actions]
            [informatic_tools.parse :as parse]
            [informatic_tools.calculations :as calculations]))

  (defroutes main-routes
    (GET "/" [] (views/summary (session-get :results)
                               (session-get :sample-group-map)))
    (GET "/results/new" [] (views/new-results-form))
    (POST "/results/" [results-name results]
          (let [r (actions/add-results results-name results (session-get :results-map))]
            (session-put! :results-map (first r))
            (session-put! :results (second r)))          
          (response/redirect "/"))
    (GET "/sample-groups/new" []
         (views/new-sample-group-form))
    (POST "/sample-groups/" [sample-group-name sample-group-identifiers]
          (session-put! :sample-group-map
                        (actions/add-sample-group-to-sample-group-map
                         sample-group-name
                         (str  sample-group-identifiers)
                         (session-get :sample-group-map)))
          (response/redirect "/"))
    (GET "/calculations/t-test/new" []
         (views/t-test-form
          (session-get :results)
          (session-get :sample-group-map)))
    (POST "/calculations/t-test"
          [attribute
           sample-group-one-name
           sample-group-two-name]
          (views/t-test-results (actions/calculate-t-test attribute
                                                          sample-group-one-name
                                                          sample-group-two-name
                                                          (session-get :sample-group-map)
                                                          (session-get :results))
                                sample-group-one-name
                                sample-group-two-name))
    (GET "/calculations/summary-statistics/new" []
         (views/summary-statistics-form
          (session-get :results)
          (session-get :sample-group-map)))
    (POST "/calculations/summary-statistics/" [sample-group-one-name
                                               attribute]
          (views/summary-statistics-results attribute
                                            sample-group-one-name
                                            (actions/calculate-summary-statistics
                                             attribute
                                             sample-group-one-name
                                             (session-get :sample-group-map)
                                             (session-get :results))))
    (GET "/bar-chart"
         [label-one value-one label-two value-two]
         (actions/t-test-bar-chart label-one value-one label-two value-two))
    (GET "/box-plot" [attribute sample-group-one-name]
         (actions/summary-statistics-box-plot attribute sample-group-one-name
                                              (session-get :sample-group-map)
                                              (session-get :results)))
    (route/files "/public")
    (route/not-found "Page not found"))



(def app (-> main-routes
             wrap-stateful-session
             handler/api))