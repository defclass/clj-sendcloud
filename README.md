# clj-sendcloud

A Clojure library designed to support Sendcloud API.

As Sendcloud APIv2 don't support HTTPS, this lib just support APIv1.

## Usage

clj-sendcloud use clj-http as http client , and use cheshire to decode json. Make sure add clj-http, cheshire dependencies.
eg:
```
[clj-http "2.1.0"]
[cheshire "5.6.1"]
```

Add clj-sendcloud dependence:

```
[clj-sendcloud "0.1.0-SNAPSHOT"]
```

Examples:

```clojure
(require '[clj-sendcloud.core :refer s])

;; tlist
(s/tlist {:api_user "XXXXXXXX" :api_key "XXXXXXXX"} {})
; => 
;["https://sendcloud.sohu.com/webapi/template.get.json" {:api_user "XXXXXXXX", :api_key "XXXXXXXX"}]

;; wrap-request
(->> (tlist {:api_user "XXXXXXXX" :api_key "XXXXXXXX"} {}) 
     s/wrap-request)
; =>     
;["https://sendcloud.sohu.com/webapi/template.get.json"
; {:body "api_user=XXXXXXXX&api_key=XXXXXXXX",
;  :content-type "application/x-www-form-urlencoded",
;  :as :json,
;  :coerce :always}]

;; clj-http request
(require '[clj-http.client :as c])

(->> (s/tlist {:api_user "XXXXX" :api_key  "XXXXX"} {})
     s/wrap-request 
     (apply c/post))

; => 
;{:status 200,
; :headers {"Server" "nginx",
;           "Date" "Wed, 15 Jun 2016 08:08:31 GMT",
;           "Content-Type" "application/json;charset=UTF-8",
;           "Transfer-Encoding" "chunked",
;           "Connection" "close"},
; :body ... ,
; :request-time 246,
; :trace-redirects ["https://sendcloud.sohu.com/webapi/template.get.json"],
; :orig-content-encoding "gzip",
; :cookies ... }

```

## License

Copyright © 2016 Michael Wong

Distributed under the Eclipse Public License .
