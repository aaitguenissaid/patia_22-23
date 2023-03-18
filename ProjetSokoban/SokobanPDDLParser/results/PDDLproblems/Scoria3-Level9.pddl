(define (problem Scoria3-Level9)
(:domain SOKOBAN)
(:objects P00 P01 P10 P13 P14 P15 P22 P23 P24 P25 P31 P32 P33 P34 P41 P42 P43 P44 P45 P46 P47 P53 P54 P55 P56 P57 P60 P61 P64 P65 P70 P71 P72 P74 P75 P80 P81 P82  - position 
 g45  - guard  
 B34 B43 B44 B46 B64  - box 
  left right up down - direction )
(:init (isFree P00)
(isFree P01)
(isNeighbor P01 P00 left )
(isNeighbor P00 P01 right )
(isFree P10)
(isNeighbor P10 P00 up )
(isNeighbor P00 P10 down )
(isFree P13)
(isFree P14)
(isNeighbor P14 P13 left )
(isNeighbor P13 P14 right )
(isFree P15)
(isNeighbor P15 P14 left )
(isNeighbor P14 P15 right )
(isFree P22)
(isFree P23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isNeighbor P23 P22 left )
(isNeighbor P22 P23 right )
(isFree P24)
(isNeighbor P24 P14 up )
(isNeighbor P14 P24 down )
(isNeighbor P24 P23 left )
(isNeighbor P23 P24 right )
(isFree P25)
(isNeighbor P25 P15 up )
(isNeighbor P15 P25 down )
(isNeighbor P25 P24 left )
(isNeighbor P24 P25 right )
(isFree P31)
(isFree P32)
(isNeighbor P32 P22 up )
(isNeighbor P22 P32 down )
(isNeighbor P32 P31 left )
(isNeighbor P31 P32 right )
(isFree P33)
(isNeighbor P33 P23 up )
(isNeighbor P23 P33 down )
(isNeighbor P33 P32 left )
(isNeighbor P32 P33 right )
(boxOn P34)
(isNeighbor P34 P24 up )
(isNeighbor P24 P34 down )
(isNeighbor P34 P33 left )
(isNeighbor P33 P34 right )
(isFree P41)
(isNeighbor P41 P31 up )
(isNeighbor P31 P41 down )
(isFree P42)
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
(guardOn P45 G45)
(isNeighbor P45 P44 left )
(isNeighbor P44 P45 right )
(boxOn P46)
(isNeighbor P46 P45 left )
(isNeighbor P45 P46 right )
(isFree P47)
(isNeighbor P47 P46 left )
(isNeighbor P46 P47 right )
(isFree P53)
(isNeighbor P53 P43 up )
(isNeighbor P43 P53 down )
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
(isFree P56)
(isNeighbor P56 P46 up )
(isNeighbor P46 P56 down )
(isNeighbor P56 P55 left )
(isNeighbor P55 P56 right )
(isFree P57)
(isNeighbor P57 P47 up )
(isNeighbor P47 P57 down )
(isNeighbor P57 P56 left )
(isNeighbor P56 P57 right )
(isFree P60)
(isFree P61)
(isNeighbor P61 P60 left )
(isNeighbor P60 P61 right )
(boxOn P64)
(isNeighbor P64 P54 up )
(isNeighbor P54 P64 down )
(isFree P65)
(isNeighbor P65 P55 up )
(isNeighbor P55 P65 down )
(isNeighbor P65 P64 left )
(isNeighbor P64 P65 right )
(isFree P70)
(isNeighbor P70 P60 up )
(isNeighbor P60 P70 down )
(isFree P71)
(isNeighbor P71 P61 up )
(isNeighbor P61 P71 down )
(isNeighbor P71 P70 left )
(isNeighbor P70 P71 right )
(isFree P72)
(isNeighbor P72 P71 left )
(isNeighbor P71 P72 right )
(isFree P74)
(isNeighbor P74 P64 up )
(isNeighbor P64 P74 down )
(isFree P75)
(isNeighbor P75 P65 up )
(isNeighbor P65 P75 down )
(isNeighbor P75 P74 left )
(isNeighbor P74 P75 right )
(isFree P80)
(isNeighbor P80 P70 up )
(isNeighbor P70 P80 down )
(isFree P81)
(isNeighbor P81 P71 up )
(isNeighbor P71 P81 down )
(isNeighbor P81 P80 left )
(isNeighbor P80 P81 right )
(isFree P82)
(isNeighbor P82 P72 up )
(isNeighbor P72 P82 down )
(isNeighbor P82 P81 left )
(isNeighbor P81 P82 right )
)
(:goal (and (boxOn P32)
(boxOn P34)
(boxOn P43)
(boxOn P55)
(boxOn P56)
))
)