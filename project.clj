(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.4.0"]
                 [org.apache.tomcat/tomcat-jdbc "8.0.28"]
                 [clj-http "2.0.0"]
                 [org.postgresql/postgresql "9.4-1205-jdbc41"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler app.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
