(ns devtool.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [clojure.java.browse :refer [browse-url]]))

(defonce ^:private jetty-server (atom nil))

(defn- stop-app
  "Stop the server started by the serve macro."
  ([] (stop-app nil))
  ([destroy]
   (when @jetty-server
     (.stop @jetty-server)
     (reset! jetty-server nil)
     (println "Stopped web server")
     (when destroy (destroy)))))

(defn- start-app
  ([handler port]
   (run-jetty handler {:port port :join? false})))

(defn- serve* [handler [port open-browser?]]
  (reset! jetty-server (start-app handler port))
  (println "Started web server on port" port)
  (when open-browser?
    (browse-url (str "http://localhost:" port "/")))
  nil)

(defn serve "启动后端服务，多次调用可以实现先停止再重启"
  [handler & [{:keys [init destroy open-browser? port]}]]
  (let [port (if port port 3000)
        destroy (when destroy (memoize destroy))
        handler (wrap-reload handler)]
    (when destroy (stop-app destroy))
    (when init (init))
    (serve* handler [port open-browser?])))

(defmulti server (fn [x] (x :method)))

(defn start-server []
  (serve (server {:method "handler"})
         {:init (server {:method "init"})}))

(defn stop-server []
  (serve (server {:method "destroy"})))
