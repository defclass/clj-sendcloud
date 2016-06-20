(ns clj-sendcloud.util
  (:require [ring.util.codec :as codec]
            [clojure.string :as str]))

(def base-url "https://sendcloud.sohu.com/webapi/")

(defn assemble-url [uri]
  (str base-url uri))

(defn wrap-request [[url body]]
  [url
   {:body (codec/form-encode body)
    :content-type "application/x-www-form-urlencoded"
    :as :json
    :coerce :always}])

(defn check-fields [fields body-map]
  (assert (vector? fields) "fields is not vector.")
  (if (every? #(some? (% body-map)) fields)
    (select-keys body-map fields)
    (throw (Exception.
             (format "Check fields fails. Fields: [ %s ] need to be specified"
                     (str/join ", " (map str fields)))))))

(defn gen-action [credential-map fields need-check-fields uri & [adjust-fn]]
  (let [adjust-fn (if adjust-fn adjust-fn identity)]
    (check-fields need-check-fields fields)
    [(assemble-url uri) (merge credential-map (adjust-fn fields))]))

(defmacro defaction
  "All action need to passed credential-map and relate fields."
  [symbol check-fields uri & [adjust-fn]]
  `(defn ~symbol [~'credential-map ~'fields]
     (gen-action ~'credential-map ~'fields ~check-fields ~uri ~adjust-fn)))

(defmacro defalias [sym var-sym]
  `(let [v# (var ~var-sym)]
     (intern *ns* (with-meta (quote ~sym) (meta v#)) @v#)))
