(define (problem Scoria2-Level2)
(:domain SOKOBAN)
(:objects P11 P12 P21 P22 P31 P32 P33 P34 P35 P36 P42 P43 P44 P45 P46 P51 P52 P53 P55 P61 P62 P63 P64 P65  - position 
 g64  - guard  
 B33 B42 B43 B45  - box 
  left right up down - direction )
(:init (isFree P11)
(isFree P12)
(isNeighbor P12 P11 left )
(isNeighbor P11 P12 right )
(isFree P21)
(isNeighbor P21 P11 up )
(isNeighbor P11 P21 down )
(isFree P22)
(isNeighbor P22 P12 up )
(isNeighbor P12 P22 down )
(isNeighbor P22 P21 left )
(isNeighbor P21 P22 right )
(isFree P31)
(isNeighbor P31 P21 up )
(isNeighbor P21 P31 down )
(isFree P32)
(isNeighbor P32 P22 up )
(isNeighbor P22 P32 down )
(isNeighbor P32 P31 left )
(isNeighbor P31 P32 right )
(boxOn P33)
(isNeighbor P33 P32 left )
(isNeighbor P32 P33 right )
(isFree P34)
(isNeighbor P34 P33 left )
(isNeighbor P33 P34 right )
(isFree P35)
(isNeighbor P35 P34 left )
(isNeighbor P34 P35 right )
(isFree P36)
(isNeighbor P36 P35 left )
(isNeighbor P35 P36 right )
(boxOn P42)
(isNeighbor P42 P32 up )
(isNeighbor P32 P42 down )
(boxOn P43)
(isNeighbor P43 P33 up )
(isNeighbor P33 P43 down )
(isNeighbor P43 P42 left )
(isNeighbor P42 P43 right )
(isFree P44)
(isNeighbor P44 P34 up )
(isNeighbor P34 P44 down )
(isNeighbor P44 P43 left )
(isNeighbor P43 P44 right )
(boxOn P45)
(isNeighbor P45 P35 up )
(isNeighbor P35 P45 down )
(isNeighbor P45 P44 left )
(isNeighbor P44 P45 right )
(isFree P46)
(isNeighbor P46 P36 up )
(isNeighbor P36 P46 down )
(isNeighbor P46 P45 left )
(isNeighbor P45 P46 right )
(isFree P51)
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
(isFree P55)
(isNeighbor P55 P45 up )
(isNeighbor P45 P55 down )
(isFree P61)
(isNeighbor P61 P51 up )
(isNeighbor P51 P61 down )
(isFree P62)
(isNeighbor P62 P52 up )
(isNeighbor P52 P62 down )
(isNeighbor P62 P61 left )
(isNeighbor P61 P62 right )
(isFree P63)
(isNeighbor P63 P53 up )
(isNeighbor P53 P63 down )
(isNeighbor P63 P62 left )
(isNeighbor P62 P63 right )
(guardOn P64 G64)
(isNeighbor P64 P63 left )
(isNeighbor P63 P64 right )
(isFree P65)
(isNeighbor P65 P55 up )
(isNeighbor P55 P65 down )
(isNeighbor P65 P64 left )
(isNeighbor P64 P65 right )
)
(:goal (and (boxOn P43)
(boxOn P45)
(boxOn P53)
(boxOn P61)
))
)