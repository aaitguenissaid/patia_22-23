;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; pursuit-evasion
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define (domain PURSUIT)
    (:requirements :strips :typing)
    (:types intruder pursuer node)
    (:predicates
        (onnodeIntruder ?x - intruder ?y - node)
        (onnodePursuer ?x - pursuer ?y - node)
        (connected ?x - node ?y - node)
        (explored ?x - node)
        (unexplored ?x - node)
        (captured ?x - intruder)
    )

    (:action intruder_move
        :parameters (?x - intruder ?y - node ?z - node)
        :precondition (and (onnodeIntruder ?x ?y) (connected ?y ?z) (explored ?z))
        :effect (and (not (onnodeIntruder ?x ?y)) (onnodeIntruder ?x ?z))
    )

    (:action pursuer_move
        :parameters (?x - pursuer ?y - node ?z - node)
        :precondition (and (onnodePursuer ?x ?y) (connected ?y ?z) (unexplored ?z))
        :effect (and (not (onnodePursuer ?x ?y)) (onnodePursuer ?x ?z) (not(unexplored ?z)) (explored ?z))
    )

    (:action capture
        :parameters (?x - pursuer ?y - intruder ?z - node)
        :precondition (and (onnodePursuer ?x ?z) (onnodeIntruder ?y ?z) (explored ?z))
        :effect (captured ?y)
    )

)