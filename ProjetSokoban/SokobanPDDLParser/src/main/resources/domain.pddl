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
        up down right left - direction
        position guard box case 
    )

    ;;;(:constants
    ;;;    free - position
    ;;;    box - position
    ;;;)

    (:predicates
        (guardOn ?x - position ?y - guard)
        (boxOn ?x - position)
        (isNeighbor ?x - position ?y - position ?how - direction)
        (isFree ?x - position)
    )

    (:action move
        :parameters (?g - guard ?from - position ?to - position ?how - direction)
        :precondition (and (guardOn ?from ?g) 
                           (isFree ?to) 
                           (isNeighbor ?from ?to ?how)
        )
        :effect (and (not (guardOn ?from ?g)) 
                     (guardOn ?to ?g) 
                     (isFree ?from) 
                     (not (isFree ?to)))
    )    

    (:action push
        :parameters (?g - guard ?box - box ?from - position ?middle - position ?to - position ?how - direction)
        :precondition (and (guardOn ?from ?g) (boxOn ?middle) 
                           (isFree ?to) 
                           (isNeighbor ?from ?middle ?how) (isNeighbor ?middle ?to ?how))
        :effect (and (not (guardOn ?from ?g)) (guardOn ?middle ?g) 
                     (not (boxOn ?middle)) (boxOn ?to)
                     (isFree ?from) (not (isFree ?to)))
                     
    )

 )   