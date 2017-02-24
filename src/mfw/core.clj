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
        (let [results (search conf/sources text)]
          (response
            (message (str "Found " (count results) " " (if (= (count results) 1) "match" "matches") " for \"" text "\"")
                     (conj (vec (map image-attachment-choice results)) cancel-attachment-choice)))))
  (POST "/slack-action" [payload]
        (let [data (json/parse-string payload true) user (:user data) button (first (:actions data))]
          (response (assoc
            (if (= (:name button) "post")
              (message nil (image-attachment {:source {:name (:name user)} :url (:value button)}))
              nil)
            :delete_original true))))
  (GET "/slack-oauth" [code]
       (slack-oauth code)))

(def app
  (->
    (handler/site api-routes)
    wrap-json-response))
