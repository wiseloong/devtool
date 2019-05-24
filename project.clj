(defproject wiseloong/devtool "1.1.0"
  :description "wiseloong-开发工具"
  :url "www.wiseloong.com"
  :license {:name "wiseloong"}

  :dependencies [[binaryage/devtools "0.9.10"]
                 [figwheel-sidecar "0.5.17"]
                 [cider/piggieback "0.4.0"]
                 [ring "1.7.1"]
                 [ring/ring-core "1.7.1"]
                 [ch.qos.logback/logback-classic "1.2.3"]]

  :cljsbuild {:builds
              [{:id           "dev"
                :source-paths ["src"]
                :figwheel     true
                :compiler     {:main       devtool.web
                               :asset-path "/out"
                               :output-to  "target/cljsbuild/public/app.js"}}]}

  :profiles {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                                    [org.clojure/clojurescript "1.10.439"]]
                   :resource-paths ["target/cljsbuild"]
                   :repl-options   {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
