(ns app.job
  (:use [cronj.core]))


(defn print-task [t opts]
  ;; use t to arrange job
  (prn "Job is runnning at" t " with options " opts))



(def cj (cronj :entries [{:id :print-task
                          :schedule "/5 * * * * * *"
                          :handler print-task
                          :opts {:db-url "jdbc://...."}}]))

(defn start-all []
  (if (not (running? cj))
    (start! cj)))


(defn stop-all []
  (stop! cj))
