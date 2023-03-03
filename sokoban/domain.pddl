;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sokoban
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;  #  is a wall
;;;  $  is a box
;;;  .  is a destination
;;;  *  is a box on a storage place
;;;  @  is the warehouse guard
;;;  +  is this guard on a storage place
;;; ' ' a space character is the floor

(define (domain SOKOBAN)
    (:requirements :strips :typing)

    (:types
        position guard box case
    )

    (:constants
        free - position
        box - position
    )

    (:predicates
        (guardOn ?x - position ?y - guard)
        (boxOn ?x - position ?y - box)
        (isNeighbor ?x - position ?y - position)
        (isFree ?x)
    )

    (:action move
        :parameters (?g - guard ?from - position ?to - position)
        :precondition (and (guardOn ?from ?g) 
        (isFree ?to) 
        (isNeighbor ?from ?to)
        )
        :effect (and (not (guardOn ?from ?g)) 
        (guardOn ?to ?g) 
        (isFree ?from) 
        (not (isFree ?to)))
    )    

    (:action push
        :parameters (?g - guard ?box - box ?from - position ?middle - position ?to - position)
        :precondition (and (guardOn ?from ?g) (boxOn ?middle ?box) 
                           (isFree ?to) 
                           (isNeighbor ?from ?middle) (isNeighbor ?middle ?to))
        :effect (and (not (guardOn ?from ?g)) (guardOn ?middle ?g) 
                     (not (boxOn ?middle ?box)) (boxOn ?to ?box)
                     (isFree ?from) (not (isFree ?to)))
    )

 )   