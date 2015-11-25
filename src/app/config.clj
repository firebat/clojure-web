(ns app.config)

(defn env []
  (or (System/getenv "ENVIRONMENT")
      (System/getenv "DEPLOY_TYPE")
      "dev"))

(def config
  ((keyword (env))
   {:dev {:name "dev"
          :jdbc {:driver "org.sqlite.JDBC"
                 :url "jdbc:sqlite:test.db"
                 ;; jdbc:postgresql://localhost:5432/test
                 :username ""
                 :password ""}}

    :prod {:name "prod"
           :jdbc {:driver "org.postgresql.Driver"
                  :url "jdbc:postgresql://localhost:5492/test"
                  :username "test"
                  :password "test"}}}))
