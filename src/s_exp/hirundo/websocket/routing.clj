(ns s-exp.hirundo.websocket.routing
  (:require [s-exp.hirundo.options :as options]
            [s-exp.hirundo.websocket.listener :as l])
  (:import (io.helidon.http HttpPrologue)
           (io.helidon.http PathMatchers)
           (io.helidon.webserver WebServerConfig$Builder Routing Route)
           (io.helidon.webserver.websocket WsRouting WsRoute WsRouting$Builder)
           (io.helidon.websocket WsListener)
           (java.util.function Supplier)))

(set! *warn-on-reflection* true)

(defn set-websocket-endpoints! ^WebServerConfig$Builder
  [^WebServerConfig$Builder builder endpoints _options]
  (doto builder
    (.addRouting
     ^WsRouting$Builder
     (reduce (fn [^WsRouting$Builder builder [path listener]]
               (.endpoint builder ^String path
                          (l/make-listener-supplier listener)))
             (WsRouting/builder)
             endpoints))))

(defmethod options/set-server-option! :websocket-endpoints
  [^WebServerConfig$Builder builder _ endpoints options]
  (set-websocket-endpoints! builder endpoints options))
