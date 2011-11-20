(ns robots.core)

(use 'com.mefesto.wabbitmq)
(use 'robots.server)
(use 'robots.queue)

(initialize)

(purge-all-queues)



;(send-message "JOIN-GAME:Robot1")
;(send-message "JOIN-GAME:Robot2")
;(send-message "JOIN-GAME:Robot3")
;(send-message "START-GAME")
;(send-message "WIN:Robot2")
;
;(register-listener server)
;



