;;; {
;;; 	"title": {
;;; 		"2": "Scoria - Level 1"
;;; 	},
;;; 	"testIn": "#####\n
;;;                #   #\n
;;;                # G #\n
;;;                #   #\n
;;;                #####",
;;;                
;;;                
;;;                
;;; 	"isTest": "true",
;;; 	"isValidator": "true"
;;; }

(define (problem sokoban_problem)
(:domain SOKOBAN)
(:objects p1 p2 p3 p4 p5 p6 p7 p8 p9 p10 p11 - position
          g - guard
          b - box)
(:init (on A one)
(on B two)
(on C six)
(on D seven)
(on E three)
(on F four)
(on G free)
(on H five)
(on I eight)
(permutable A D)
(permutable A B)
(permutable B C)
(permutable B E)
(permutable B C)
(permutable C F)
(permutable D E)
(permutable E F)
(permutable D G)
(permutable G H)
(permutable E H)
(permutable H I)
(permutable F I))

(:goal (and (on A one)
(on B two)
(on C three)
(on D four)
(on E five)
(on F six)
(on G seven)
(on H eight)
(on I free)))
)
