(ns app.db
  (:require [app.jdbc]
            [app.config :refer [config]])
  (:import org.apache.tomcat.jdbc.pool.DataSource))

;; data source
(defn pooled-data-source
  [spec]
  (let [datasource (doto (DataSource.)
                     (.setDriverClassName (:driver spec))
                     (.setUrl (:url spec))
                     (.setUsername (:username spec))
                     (.setPassword (:password spec))
                     (.setInitialSize 1)
                     (.setMaxActive 10)
                     (.setMaxIdle 5)
                     (.setMaxWait 5000)
                     (.setMinIdle 1)
                     (.setRemoveAbandoned true)
                     (.setRemoveAbandonedTimeout 30)
                     (.setTestOnBorrow true)
                     (.setValidationQuery "select 1")
                     (.setValidationInterval 500000))]
    {:datasource datasource}))

(def pool
  (delay
   (pooled-data-source (:jdbc config))))

(defn connection [] @pool)
