(ns mfw.slack)

(defn message [text attachments]
  {:text text :attachments attachments})

(defn image-attachment [image]
  {:author_name (:name (:source image)) :image_url (:url image)})

(defn image-attachment-choice [image]
  (assoc (image-attachment image)
    :actions [{:name "post" :text "Select" :type "button" :value (:url image)}]))

(def cancel-attachment-choice
  {:actions [{:name "cancel" :text "Cancel" :type "button"}]})
