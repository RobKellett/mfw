(ns mfw.slack
  (require [mfw.conf :as conf]
           [clj-http.client :as http]))

(defn message [text attachments]
  {:text text :attachments attachments})

(defn image-attachment [image]
  {:author_name (:name (:source image)) :image_url (:url image)})

(defn image-attachment-choice [image]
  (assoc (image-attachment image)
    :actions [{:name "post" :text "Select" :type "button" :value (:url image)}]))

(def cancel-attachment-choice
  {:actions [{:name "cancel" :text "Cancel" :type "button"}]})

(defn slack-oauth [code]
  (http/post "https://slack.com/api/oauth.access"
             {:form-params {:client_id (:id conf/slack-oauth)
                            :client_secret (:secret conf/slack-oauth)
                            :code code}}))
