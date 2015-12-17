(ns app.db
  (:require [app.jdbc]
            [app.config :refer [config]])
  (:import org.apache.tomcat.jdbc.pool.DataSource))

(defn- format-vals [args likes]
  (let [safe-likes (filter #(contains? args %) likes)]
    (vals
     (if (empty? safe-likes)
       args
       (update-in args safe-likes #(str "%" % "%"))))))


(defn- and-tokens [keys likes ltrees]
  (map #(str (name %)
             (if (contains? likes %) " like " "=")
             (if (contains? ltrees %) "?::ltree" "?"))
       keys))


(defn build-query [sql args & {:keys [likes arg-keys ltrees limit offset]
                                  :or {likes [] ltrees [] limit 1000 offset 0}}]

  (let [safe-args (select-keys args (for [[k v] args :when (and (or (empty? arg-keys)
                                                                    (contains? arg-keys k))
                                                                (not (nil? v)))] k))]
    (if (empty? safe-args)
      [sql]
      (into [(str sql " where "
                  ;; code=? and node like ?
                  (s/join " and " (and-tokens (keys safe-args)
                                              (set likes)
                                              ltrees))
                  ;; limit x offset y
                  " limit " limit " offset " offset)]
            (format-vals safe-args likes)))))

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
