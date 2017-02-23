(ns mfw.search
  (require [clj-http.client :as http]))

(defn search-single [source query]
  (let [response (http/get (:endpoint source) {:accept :json :as :json :query-params {"q" query}})]
    (if (= (:status response) 200)
      (map (fn [x] {:source source :url x}) (:results (:body response)))
      [])))

(defn search [sources query]
  (mapcat (fn [x] (search-single x query)) sources))
