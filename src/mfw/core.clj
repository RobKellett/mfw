(ns mfw.core
  (:use compojure.core
        mfw.slack
        mfw.search)
  (:require [mfw.conf :as conf]
            [cheshire.core :as json]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]))

(defroutes api-routes
  (POST "/slack" [user_id text]
        (let [results (take 5 (search conf/sources text))]
          (response
            (if (= (count results) 0)
              (message (str "No results found for \"" text "\"") nil)
              (message (str "Found " (count results) " " (if (= (count results) 1) "match" "matches") " for \"" text "\"")
                       (conj (vec (map image-attachment-choice results)) cancel-attachment-choice))))))
  (POST "/slack-action" [payload]
        (let [data (json/parse-string payload true) user (:user data) button (first (:actions data))]
          (response (assoc
            (if (= (:name button) "post")
              (message (str "<@" (:id user) "|" (:name user) ">") [(image-attachment {:source {:name nil} :url (:value button)})])
              nil)
            :delete_original true
            :response_type "in_channel"))))
  (GET "/slack-oauth" [code]
       (slack-oauth code)))

(def app
  (->
    (handler/site api-routes)
    wrap-json-response))
