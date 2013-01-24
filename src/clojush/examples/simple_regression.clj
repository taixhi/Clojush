;; simple_regression.clj
;; an example problem for clojush, a Push/PushGP system written in Clojure
;; Lee Spector, lspector@hampshire.edu, 2010

(ns clojush.examples.simple-regression
  (:use [clojush.pushgp.pushgp]
        [clojush.pushstate]
        [clojush.interpreter]
        [clojure.math.numeric-tower]))

;;;;;;;;;;;;
;; Integer symbolic regression of x^3 - 2x^2 - x (problem 5 from the 
;; trivial geography chapter) with minimal integer instructions and an 
;; input instruction that uses the auxiliary stack.

(define-registered 
  in 
  (fn [state] (push-item (stack-ref :auxiliary 0 state) :integer state)))

(pushgp 
  :error-function (fn [program]
                    (doall
                      (for [input (range 10)]
                        (let [state (run-push program 
                                              (push-item input :auxiliary 
                                                         (push-item input :integer 
                                                                    (make-push-state))))
                              top-int (top-item :integer state)]
                          (if (number? top-int)
                            (abs (- top-int 
                                    (- (* input input input) 
                                       (* 2 input input) input)))
                            1000)))))
  :atom-generators (list (fn [] (rand-int 10))
                         'in
                         'integer_div
                         'integer_mult
                         'integer_add
                         'integer_sub)
  :initial-population "data/1359057013000.ser")
