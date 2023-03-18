(define (problem Scoria-Level10)
(:domain SOKOBAN)
(:objects P11 P12 P13 P21 P22 P23 P24 P25 P32 P33 P34 P35 P41 P42 P43 P45 P51 P52 P53 P54 P55  - position 
 g23  - guard  
 B22 B32 B42  - box 
  left right up down - direction )
(:init (isFree P11)
(isFree P12)
(isNeighbor P12 P11 left )
(isNeighbor P11 P12 right )
(isFree P13)
(isNeighbor P13 P12 left )
(isNeighbor P12 P13 right )
(isFree P21)
(isNeighbor P21 P11 up )
(isNeighbor P11 P21 down )
(boxOn P22)
(isNeighbor P22 P12 up )
(isNeighbor P12 P22 down )
(isNeighbor P22 P21 left )
(isNeighbor P21 P22 right )
(guardOn P23 G23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isNeighbor P23 P22 left )
(isNeighbor P22 P23 right )
(isFree P24)
(isNeighbor P24 P23 left )
(isNeighbor P23 P24 right )
(isFree P25)
(isNeighbor P25 P24 left )
(isNeighbor P24 P25 right )
(boxOn P32)
(isNeighbor P32 P22 up )
(isNeighbor P22 P32 down )
(isFree P33)
(isNeighbor P33 P23 up )
(isNeighbor P23 P33 down )
(isNeighbor P33 P32 left )
(isNeighbor P32 P33 right )
(isFree P34)
(isNeighbor P34 P24 up )
(isNeighbor P24 P34 down )
(isNeighbor P34 P33 left )
(isNeighbor P33 P34 right )
(isFree P35)
(isNeighbor P35 P25 up )
(isNeighbor P25 P35 down )
(isNeighbor P35 P34 left )
(isNeighbor P34 P35 right )
(isFree P41)
(boxOn P42)
(isNeighbor P42 P32 up )
(isNeighbor P32 P42 down )
(isNeighbor P42 P41 left )
(isNeighbor P41 P42 right )
(isFree P43)
(isNeighbor P43 P33 up )
(isNeighbor P33 P43 down )
(isNeighbor P43 P42 left )
(isNeighbor P42 P43 right )
(isFree P45)
(isNeighbor P45 P35 up )
(isNeighbor P35 P45 down )
(isFree P51)
(isNeighbor P51 P41 up )
(isNeighbor P41 P51 down )
(isFree P52)
(isNeighbor P52 P42 up )
(isNeighbor P42 P52 down )
(isNeighbor P52 P51 left )
(isNeighbor P51 P52 right )
(isFree P53)
(isNeighbor P53 P43 up )
(isNeighbor P43 P53 down )
(isNeighbor P53 P52 left )
(isNeighbor P52 P53 right )
(isFree P54)
(isNeighbor P54 P53 left )
(isNeighbor P53 P54 right )
(isFree P55)
(isNeighbor P55 P45 up )
(isNeighbor P45 P55 down )
(isNeighbor P55 P54 left )
(isNeighbor P54 P55 right )
)
(:goal (and (boxOn P32)
(boxOn P34)
(boxOn P42)
))
)