(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :refer [response resource-response redirect]]
            [clojure.java.jdbc :as jdbc]
            [app.db :refer [connection]]))

(defn json-response [data & {:keys [status message] :or {status 0 message ""}}]
  (response {:data data
             :status status
             :message message}))

(defroutes api-routes
  (GET "/" {params :params body :body}
       (json-response params))

  (GET "/ex" []
       (assert (= 1 2)))

  (GET "/db" []
       (json-response
        (jdbc/query
         (connection)
         ["select ? as name" "alice"]))))


(defroutes app-routes
  (GET "/" [] "Hello World")

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
          (handler request)
          (catch Throwable e
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
