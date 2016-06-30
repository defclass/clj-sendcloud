(ns clj-sendcloud.core
  (:refer-clojure :exclude [send])
  (:require [clj-sendcloud.util :refer [defaction defalias] :as util]
            [cheshire.core :as json]))

(defalias wrap-request util/wrap-request)

(defn mk-credential [api-user api-key]
  {:api_user api-user :api_key api-key})


;; refer to http://sendcloud.sohu.com/doc/email/send_email

(defaction send [:from :to :subject :html] "mail.send.json")

(defaction send-tpl [:from :template_invoke_name] "mail.send_template.json"
  (fn [m]
    (if (:substitution_vars m)
      (update-in m [:substitution_vars] #(json/generate-string %))
      m)))

(defn sendt [credential-map from template-invoke-name body-map]
  (send-tpl credential-map
            {:from from
             :template_invoke_name template-invoke-name
             :substitution_vars {:to (:mail body-map)
                                 :sub (dissoc body-map :mail)}}))

;; refer http://sendcloud.sohu.com/doc/email/template_do
;; template action

(defaction tlist [] "template.get.json")

(defaction tadd [:invoke_name :name :html :subject :email_type] "template.add.json")

(defaction tdel [:invoke_name] "template.delete.json")

(defaction tupdate [:invoke_name] "template.update.json")
