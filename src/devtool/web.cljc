(ns devtool.web
  (:require #?(:clj [figwheel-sidecar.repl-api :as f])
            #?(:cljs [devtools.core :as devtools])))

#?(:clj (defn- start-fw []
          (f/start-figwheel!)))

#?(:clj (defn- cljs-repl []
          (f/cljs-repl)))

#?(:clj (defn stop-web []
          (f/stop-figwheel!)))

#?(:clj (defn start-web []
          (start-fw)
          (cljs-repl)))


;#?(:cljs (devtools/set-pref! :dont-detect-custom-formatters true))

#?(:cljs (devtools/install! [:formatters :hints]))

;#?(:cljs (devtools/install!))

#?(:cljs (enable-console-print!))

#?(:cljs (println "开发模式！"))
