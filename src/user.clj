(ns user
  (:require [devtool.web :as w]))

(defn stop-web []
  (w/stop-web))

(defn start-web []
  (w/start-web))
