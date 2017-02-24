(ns mfw.slack
  (require [mfw.conf :as conf]
           [clj-http.client :as http]))

(defn message [text attachments]
  {:text text :attachments attachments})

(defn image-attachment [image]
  {:author_name (:name (:source image)) :title (:url image) :image_url (:url image) :fallback (:url image)})

(defn image-attachment-choice [image]
  (assoc (image-attachment image)
    :callback_id (:url image)
    :actions [{:name "post" :text "Post this" :type "button" :value (:url image)}]))

(def cancel-attachment-choice
  {:callback_id "cancel" :color "danger" :text "Didn't find what you're looking for?"
   :fallback "Cancel" :actions [{:name "cancel" :text "Cancel" :type "button"}]})

(defn slack-oauth [code]
  (http/post "https://slack.com/api/oauth.access"
             {:form-params {:client_id (:id conf/slack-oauth)
                            :client_secret (:secret conf/slack-oauth)
                            :code code}}))
