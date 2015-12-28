(ns app.handler
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :refer [response resource-response redirect]]
            [clojure.java.jdbc :as jdbc]
            [app.db :refer [connection]]
            [app.job :as job]))

(defn json-response [data & {:keys [status message] :or {status 0 message ""}}]
  (response {:data data
             :status status
             :message message}))

(defroutes api-routes

  (GET "/user/:name" [name]
       (json-response {:name  name}))

  (POST "/user/:name" {params :params body :body session :session}
        (log/info "name" (:name params))
        (log/info "data" body)
        (json-response params))

  (GET "/ex" []
       (assert (= 1 2)))

  (GET "/db" []
       (json-response
        (jdbc/query
         (connection)
         ["select ? as name, ? as age" "alice" 20]
         ;; [optional] list => entity
         :result-set-fn first
         ;; [optional] format entity
         :row-fn (fn [x] (assoc x :text (str (:name x) " already " (:age x) " years old.")))
         ))))


(defroutes app-routes

  (GET "/" [] "Hello World")
  (GET "/request" request (str request))

  (GET "/check" []
       (app.job/start-all)
       "OK")

  (context "/api/v1" request api-routes)
  (route/resources "/")
  (route/not-found "Not Found"))


(defn wrap-app [handler]
  (fn [request]
    (let [headers (get-in request [:headers])
          session (get-in request [:session])
          cookies (get-in request [:cookies])
          uri (:uri request)]

      ;; (if (or (contains session :uid)
      ;;         (contains #{"/healthcheck.html" "/login"} uri))
      (if true
        ;; authorized
        (try
          (log/debug "request from" (:remote-addr request) "to" (get-in request [:headers "host"]))
          (handler request)
          (catch Throwable e
            (log/error e)
            (-> (response {:status -1 :message (.getMessage e) :data nil}))))
        
        ;; login
        (redirect (str "/login?ret=" uri))))))

(def app
  (-> app-routes
      (wrap-app)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-cookies)
      (wrap-session)
      (wrap-defaults api-defaults)))
