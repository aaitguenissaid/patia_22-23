(define (problem Scoria3-Level4)
(:domain SOKOBAN)
(:objects P00 P01 P10 P11 P13 P14 P15 P16 P17 P20 P23 P27 P32 P33 P34 P35 P37 P41 P42 P43 P44 P45 P46 P47 P51 P52 P53 P54 P55 P57 P61 P62 P65 P66 P67 P71 P72  - position 
 g47  - guard  
 B42 B43 B44 B45 B46  - box 
  left right up down - direction )
(:init (isFree P00)
(isFree P01)
(isNeighbor P01 P00 left )
(isNeighbor P00 P01 right )
(isFree P10)
(isNeighbor P10 P00 up )
(isNeighbor P00 P10 down )
(isFree P11)
(isNeighbor P11 P01 up )
(isNeighbor P01 P11 down )
(isNeighbor P11 P10 left )
(isNeighbor P10 P11 right )
(isFree P13)
(isFree P14)
(isNeighbor P14 P13 left )
(isNeighbor P13 P14 right )
(isFree P15)
(isNeighbor P15 P14 left )
(isNeighbor P14 P15 right )
(isFree P16)
(isNeighbor P16 P15 left )
(isNeighbor P15 P16 right )
(isFree P17)
(isNeighbor P17 P16 left )
(isNeighbor P16 P17 right )
(isFree P20)
(isNeighbor P20 P10 up )
(isNeighbor P10 P20 down )
(isFree P23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isFree P27)
(isNeighbor P27 P17 up )
(isNeighbor P17 P27 down )
(isFree P32)
(isFree P33)
(isNeighbor P33 P23 up )
(isNeighbor P23 P33 down )
(isNeighbor P33 P32 left )
(isNeighbor P32 P33 right )
(isFree P34)
(isNeighbor P34 P33 left )
(isNeighbor P33 P34 right )
(isFree P35)
(isNeighbor P35 P34 left )
(isNeighbor P34 P35 right )
(isFree P37)
(isNeighbor P37 P27 up )
(isNeighbor P27 P37 down )
(isFree P41)
(boxOn P42)
(isNeighbor P42 P32 up )
(isNeighbor P32 P42 down )
(isNeighbor P42 P41 left )
(isNeighbor P41 P42 right )
(boxOn P43)
(isNeighbor P43 P33 up )
(isNeighbor P33 P43 down )
(isNeighbor P43 P42 left )
(isNeighbor P42 P43 right )
(boxOn P44)
(isNeighbor P44 P34 up )
(isNeighbor P34 P44 down )
(isNeighbor P44 P43 left )
(isNeighbor P43 P44 right )
(boxOn P45)
(isNeighbor P45 P35 up )
(isNeighbor P35 P45 down )
(isNeighbor P45 P44 left )
(isNeighbor P44 P45 right )
(boxOn P46)
(isNeighbor P46 P45 left )
(isNeighbor P45 P46 right )
(guardOn P47 G47)
(isNeighbor P47 P37 up )
(isNeighbor P37 P47 down )
(isNeighbor P47 P46 left )
(isNeighbor P46 P47 right )
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
(isNeighbor P54 P44 up )
(isNeighbor P44 P54 down )
(isNeighbor P54 P53 left )
(isNeighbor P53 P54 right )
(isFree P55)
(isNeighbor P55 P45 up )
(isNeighbor P45 P55 down )
(isNeighbor P55 P54 left )
(isNeighbor P54 P55 right )
(isFree P57)
(isNeighbor P57 P47 up )
(isNeighbor P47 P57 down )
(isFree P61)
(isNeighbor P61 P51 up )
(isNeighbor P51 P61 down )
(isFree P62)
(isNeighbor P62 P52 up )
(isNeighbor P52 P62 down )
(isNeighbor P62 P61 left )
(isNeighbor P61 P62 right )
(isFree P65)
(isNeighbor P65 P55 up )
(isNeighbor P55 P65 down )
(isFree P66)
(isNeighbor P66 P65 left )
(isNeighbor P65 P66 right )
(isFree P67)
(isNeighbor P67 P57 up )
(isNeighbor P57 P67 down )
(isNeighbor P67 P66 left )
(isNeighbor P66 P67 right )
(isFree P71)
(isNeighbor P71 P61 up )
(isNeighbor P61 P71 down )
(isFree P72)
(isNeighbor P72 P62 up )
(isNeighbor P62 P72 down )
(isNeighbor P72 P71 left )
(isNeighbor P71 P72 right )
)
(:goal (and (boxOn P43)
(boxOn P44)
(boxOn P45)
(boxOn P46)
(boxOn P47)
))
)