(define (problem BLOCKS-4-103)
(:domain BLOCKS)

(:objects A B C D E - BLOCK)
; ON A B -> A is on B
(:INIT (ONTABLE E) (ON D E) (ON C D) (ON B C) (ON A B) 
(CLEAR A) (HANDEMPTY))

(:goal (AND (ONTABLE A) (ON B A) (ON C B) (ON D C) (ON E D) (CLEAR E)))
)