(define (problem Scoria2-Level10)
(:domain SOKOBAN)
(:objects P00 P01 P10 P11 P13 P14 P20 P21 P23 P24 P25 P26 P33 P34 P35 P36 P41 P42 P43 P44 P45 P51 P52 P53 P54 P63 P64 P70 P71 P73 P74 P80 P81  - position 
 g63  - guard  
 B33 B43 B53 B64  - box 
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
(isFree P20)
(isNeighbor P20 P10 up )
(isNeighbor P10 P20 down )
(isFree P21)
(isNeighbor P21 P11 up )
(isNeighbor P11 P21 down )
(isNeighbor P21 P20 left )
(isNeighbor P20 P21 right )
(isFree P23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isFree P24)
(isNeighbor P24 P14 up )
(isNeighbor P14 P24 down )
(isNeighbor P24 P23 left )
(isNeighbor P23 P24 right )
(isFree P25)
(isNeighbor P25 P24 left )
(isNeighbor P24 P25 right )
(isFree P26)
(isNeighbor P26 P25 left )
(isNeighbor P25 P26 right )
(boxOn P33)
(isNeighbor P33 P23 up )
(isNeighbor P23 P33 down )
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
(isFree P36)
(isNeighbor P36 P26 up )
(isNeighbor P26 P36 down )
(isNeighbor P36 P35 left )
(isNeighbor P35 P36 right )
(isFree P41)
(isFree P42)
(isNeighbor P42 P41 left )
(isNeighbor P41 P42 right )
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
(isFree P45)
(isNeighbor P45 P35 up )
(isNeighbor P35 P45 down )
(isNeighbor P45 P44 left )
(isNeighbor P44 P45 right )
(isFree P51)
(isNeighbor P51 P41 up )
(isNeighbor P41 P51 down )
(isFree P52)
(isNeighbor P52 P42 up )
(isNeighbor P42 P52 down )
(isNeighbor P52 P51 left )
(isNeighbor P51 P52 right )
(boxOn P53)
(isNeighbor P53 P43 up )
(isNeighbor P43 P53 down )
(isNeighbor P53 P52 left )
(isNeighbor P52 P53 right )
(isFree P54)
(isNeighbor P54 P44 up )
(isNeighbor P44 P54 down )
(isNeighbor P54 P53 left )
(isNeighbor P53 P54 right )
(guardOn P63 G63)
(isNeighbor P63 P53 up )
(isNeighbor P53 P63 down )
(boxOn P64)
(isNeighbor P64 P54 up )
(isNeighbor P54 P64 down )
(isNeighbor P64 P63 left )
(isNeighbor P63 P64 right )
(isFree P70)
(isFree P71)
(isNeighbor P71 P70 left )
(isNeighbor P70 P71 right )
(isFree P73)
(isNeighbor P73 P63 up )
(isNeighbor P63 P73 down )
(isFree P74)
(isNeighbor P74 P64 up )
(isNeighbor P64 P74 down )
(isNeighbor P74 P73 left )
(isNeighbor P73 P74 right )
(isFree P80)
(isNeighbor P80 P70 up )
(isNeighbor P70 P80 down )
(isFree P81)
(isNeighbor P81 P71 up )
(isNeighbor P71 P81 down )
(isNeighbor P81 P80 left )
(isNeighbor P80 P81 right )
)
(:goal (and (boxOn P24)
(boxOn P34)
(boxOn P44)
(boxOn P54)
))
)