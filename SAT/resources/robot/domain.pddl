
(define (domain ROBOT)

  (:requirements :strips :typing)
  (:types room robot)
  (:predicates (at ?r - robot ?l - room))

  (:action move
    :parameters (?r - robot ?from - room ?to - room)
    :precondition (and (at ?r ?from))
    :effect (and (not (at ?r ?from))
                 (at ?r ?to)))
)