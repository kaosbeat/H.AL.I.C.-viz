(defproject halic-viz  "0.1.0-SNAPSHOT"
  :description "basetemplate livecoding"
  :url "http://halic.be"
  :license {:name "@lambda_sonic"
            :url "http://halic.be"}
  :repl-options {:host "0.0.0.0" :port 21337}
  :resource-paths [
                   "resources/Syphon/library/jsyphon.jar"
                   "resources/Syphon/library/libJSyphon.jnilib"
                   "resources/Syphon/library/Syphon.framework"
                   "resources/Syphon/library/Syphon.jar"
                   ]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [overtone "0.10.1"]
;                 [leipzig "0.10.0"]
                 [quil "2.2.0"]
;                 [clj-sockets "0.1.0"]
;                 [glgraphics "1.0.0"]
                 ]
  :jvm-opts ^:replace []

  :main main.botpop)
