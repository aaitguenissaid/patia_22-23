(define (problem Scoria3-Level1)
(:domain SOKOBAN)
(:objects P00 P01 P10 P11 P13 P14 P20 P23 P24 P32 P33 P34 P41 P42 P43 P44 P51 P53 P54 P55 P61 P62 P63 P64 P65 P66 P67 P74 P75 P76 P77 P80 P81 P82  - position 
 g51  - guard  
 B42 B44 B53 B54 B62  - box 
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
(isFree P23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isFree P24)
(isNeighbor P24 P14 up )
(isNeighbor P14 P24 down )
(isNeighbor P24 P23 left )
(isNeighbor P23 P24 right )
(isFree P32)
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
(boxOn P44)
(isNeighbor P44 P34 up )
(isNeighbor P34 P44 down )
(isNeighbor P44 P43 left )
(isNeighbor P43 P44 right )
(guardOn P51 G51)
(isNeighbor P51 P41 up )
(isNeighbor P41 P51 down )
(boxOn P53)
(isNeighbor P53 P43 up )
(isNeighbor P43 P53 down )
(boxOn P54)
(isNeighbor P54 P44 up )
(isNeighbor P44 P54 down )
(isNeighbor P54 P53 left )
(isNeighbor P53 P54 right )
(isFree P55)
(isNeighbor P55 P54 left )
(isNeighbor P54 P55 right )
(isFree P61)
(isNeighbor P61 P51 up )
(isNeighbor P51 P61 down )
(boxOn P62)
(isNeighbor P62 P61 left )
(isNeighbor P61 P62 right )
(isFree P63)
(isNeighbor P63 P53 up )
(isNeighbor P53 P63 down )
(isNeighbor P63 P62 left )
(isNeighbor P62 P63 right )
(isFree P64)
(isNeighbor P64 P54 up )
(isNeighbor P54 P64 down )
(isNeighbor P64 P63 left )
(isNeighbor P63 P64 right )
(isFree P65)
(isNeighbor P65 P55 up )
(isNeighbor P55 P65 down )
(isNeighbor P65 P64 left )
(isNeighbor P64 P65 right )
(isFree P66)
(isNeighbor P66 P65 left )
(isNeighbor P65 P66 right )
(isFree P67)
(isNeighbor P67 P66 left )
(isNeighbor P66 P67 right )
(isFree P74)
(isNeighbor P74 P64 up )
(isNeighbor P64 P74 down )
(isFree P75)
(isNeighbor P75 P65 up )
(isNeighbor P65 P75 down )
(isNeighbor P75 P74 left )
(isNeighbor P74 P75 right )
(isFree P76)
(isNeighbor P76 P66 up )
(isNeighbor P66 P76 down )
(isNeighbor P76 P75 left )
(isNeighbor P75 P76 right )
(isFree P77)
(isNeighbor P77 P67 up )
(isNeighbor P67 P77 down )
(isNeighbor P77 P76 left )
(isNeighbor P76 P77 right )
(isFree P80)
(isFree P81)
(isNeighbor P81 P80 left )
(isNeighbor P80 P81 right )
(isFree P82)
(isNeighbor P82 P81 left )
(isNeighbor P81 P82 right )
)
(:goal (and (boxOn P34)
(boxOn P42)
(boxOn P44)
(boxOn P54)
(boxOn P62)
))
)