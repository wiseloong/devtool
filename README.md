# devtool
wiseloong-clojure-开发组件，方便开发和使用repl。

#### 软件架构

软件架构说明

#### 安装教程

`:profiles {:dev {:dependencies [[wiseloong/devtool "1.1.0"]]}}`

#### 使用说明

## 后端开发

### 生产模式

1. project.clj 配置

```clojure
:dependencies [[org.clojure/clojure "1.9.0"]]
:plugins [[lein-ring "0.12.5"]]
:ring {:handler test.handler/app
       :init    test.handler/init
       :destroy test.handler/destroy}
:profiles {:dev {:dependencies   [[wiseloong/devtool "1.1.0"]]
                 :source-paths   ["env/dev/clj"]
                 :resource-paths ["env/dev/resources"]}}
```

2. env/dev/clj/user.clj

```clojure
(ns user
  (:use [clojure.repl]
        [devtool.server])
  (:require [test.handler :as handler]))
(defmethod server "init" [_] handler/init)
(defmethod server "destroy" [_] handler/destroy)
(defmethod server "handler" [_] #'handler/app)
```

> 使用idea启动repl，或`lein repl`；启动`(start-service)`，test为项目名。

### 测试

1. project.clj 配置

```clojure
:profiles {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                                  [wiseloong/devtool "1.1.0"]]}}
```

2. src/user.clj

> 同上面的user.clj

## 前端开发

### 生产项目模式

> 用于生产项目的开发。

1. project.clj 配置

```clojure
:dependencies [[org.clojure/clojure "1.9.0"]
               [org.clojure/clojurescript "1.10.439"]]
:cljsbuild {:builds [{:id           "dev"
                      :source-paths ["src"]
                      :figwheel     {:on-jsload yyy}
                      :compiler     {:main          xxx
                                     :asset-path    "/cljs/out"
                                     :output-to     "target/cljsbuild/public/cljs/app.js"
                                     :preloads      [devtool.web]}}
                     {:id           "prod"
                      :source-paths ["src" "env/prod/cljs"]
                      :compiler     {:output-to     "resources/public/cljs/app.js"
                                     :output-dir    "target/cljsbuild/public/cljs/prod"
                                     :optimizations :advanced
                                     :infer-externs true
                                     :pretty-print  false}}]}
:profiles {:dev {:dependencies   [[wiseloong/devtool "1.1.0"]]
                 :plugins        [[lein-cljsbuild "1.1.7"]
                                  [lein-figwheel "0.5.17"]]
                 :resource-paths ["target/cljsbuild"]
                 :repl-options   {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
```

> `:figwheel {:on-jsload yyy}`为项目代码修改后自动刷新方法，yyy为namespaces/方法，例如：`hrms.core/on-js-reload`。
>
> `:main xxx` 为项目主入口文件namespaces，例如：`hrms.core`
>
> :cljsbuild :builds 上面的配置只是示例开发的配置，还要加上打包和测试等的配置信息。

2. index.html

> 在根路径下创建`resources/public`目录，创建`index.html`文件。根据项目创建css，js，assets等目录结构。引入`<script src="app.js"></script>`

3. 启动

- 使用`lein figwheel`命令，直接启动后就可以测试，需要在:plugins 里加入lein-figwhee。
- 启动repl，使用`lein repl`命令或者使用idea工具运行一个repl；再运行`(start-web)`，开启服务，浏览器打开[localhost:3449](http://localhost:3449/)，然后就可以测试cljs代码了；推荐这种在idea测试。这种方式可以去掉 :plugins里的lein-figwheel配置。

### 依赖项目模式

> 用于依赖项目的开发，比如用到基础组件库项目里，不需要打包resources文件夹的方式，供其他项目依赖的

1. project.clj 配置

```clojure
:cljsbuild {:builds [{:id           "dev"
                      :source-paths ["src"]
                      :figwheel     true
                      :compiler     {:main          xxx
                                     :asset-path    "/out"
                                     :output-to     "target/cljsbuild/public/app.js"
                                     :preloads      [devtool.web]}}]}
:profiles {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/clojurescript "1.10.439"]
                                  [wiseloong/devtool "1.1.0"]]
                 :plugins        [[lein-figwheel "0.5.17"]]
                 :resource-paths ["target/cljsbuild"]
                 :repl-options   {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
```

> `:main xxx` 改为项目里的主入口文件的namespaces。

2. 启动

- 使用`lein figwheel`命令，直接启动后就可以测试，需要在:plugins 里加入lein-figwhee。
- 启动repl，使用`lein repl`或者使用idea工具运行一个repl；再运行`(start-web)`命令，开启服务，浏览器打开[localhost:3449](http://localhost:3449/)，然后就可以测试cljs代码了；推荐这种在idea测试，这种方式可以不要:plugins配置。

## 前端后段一起启动

可用于测试，在一个repl启动前后段。

```clojure
:profiles {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/clojurescript "1.10.439"]
                                  [wiseloong/devtool "1.1.0"]]
                   :source-paths   ["dev/clj"]
                   :resource-paths ["dev/resources" "target/cljsbuild"]
                   :repl-options   {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}}
```

```clojure
(ns user
  (:use [clojure.repl]
        [devtool.server]
        [devtool.web])
  (:require [demo.handler :as handler]))
(defmethod server "init" [_] handler/init)
(defmethod server "destroy" [_] handler/destroy)
(defmethod server "handler" [_] #'handler/app)
```

启动repl后，最好先启动后端(start-service)，再启动前端(start-web)，因为idea的repl页面在启动前端后，打开浏览器后就变成cljs模式了。