
(define (problem robot01)
  (:domain ROBOT)
  (:objects R - robot l1 l2 l3 l4 l5 - room)
  (:init (at R l1) (isConnected l1 l2) (isConnected l2 l3) (isConnected l3 l4) (isConnected l4 l5))
  (:goal (at R l5))
)

