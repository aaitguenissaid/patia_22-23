;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; pursuit-evasion problem
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (problem PURSUIT-PROBLEM)
    (:domain PURSUIT)
    (:objects
        intruder - intruder
        pursuer1 - pursuer
        node1 node2 node3 node4 node5 - node
    )
    (:init
        (connected node1 node2)
        (connected node2 node3)
        (connected node3 node4)
        (connected node4 node2)
        (connected node2 node5)

        (connected node2 node1)
        (connected node3 node2)
        (connected node4 node3)
        (connected node2 node4)
        (connected node5 node2)

        (onnodeIntruder intruder node1)
        (onnodePursuer pursuer1 node4)

        (explored node1)
        (explored node4)

        (unexplored node2)
        (unexplored node3)
        (unexplored node5)
    )
    (:goal
        (captured intruder)
    )
)