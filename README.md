# clojure-web

A project to show how to :

* read request parameter
* post form/json
* render string/template/json
* load static resources
* sub routes
* database query

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

### Profiles

    cat ~/.lein/profiles.clj
    {:user {:plugins [[cider/cider-nrepl "0.13.0"]] 
            :mirrors {"central" {:name "...Nexus"
                                 :url "http://.../nexus/content/groups/public/"}}
            :deploy-repositories {"snapshots" {:url "http://.../nexus/content/repositories/snapshots/"
                                               :username "..."
                                               :password "..."}}}
     :repl {:dependencies [[org.clojure/tools.nrepl "0.2.12"]]}}
                                                                                                                                    

- mirrors use 'lein deps` download packages
- deploy-repositories use `lein deploy` deploy snapshot packages

## Running

To start a web server for the application, run:

    lein ring server [port]

## Deploy

    lein ring uberwar
    cp target/xxx.war to your-webapp-server

## Optional

- [cronj](https://github.com/zcaudate/cronj), A simple to use, cron-inspired task scheduler
- [postal](https://github.com/drewr/postal) Internet email library for Clojure

## IDE

- [ac-cider](https://github.com/clojure-emacs/ac-cider)
- [cursive](https://cursive-ide.com/)

## License

Copyright © 2015 FIXME
