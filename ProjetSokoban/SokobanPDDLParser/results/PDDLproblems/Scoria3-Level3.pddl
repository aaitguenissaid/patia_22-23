(define (problem Scoria3-Level3)
(:domain SOKOBAN)
(:objects P00 P10 P12 P13 P14 P15 P16 P20 P22 P23 P25 P26 P27 P32 P33 P34 P35 P37 P41 P42 P44 P45 P46 P47 P51 P52 P53 P54 P56 P63 P64 P65 P66 P70 P71 P73 P74 P80 P81  - position 
 g15  - guard  
 B25 B33 B42 B56 B63  - box 
  left right up down - direction )
(:init (isFree P00)
(isFree P10)
(isNeighbor P10 P00 up )
(isNeighbor P00 P10 down )
(isFree P12)
(isFree P13)
(isNeighbor P13 P12 left )
(isNeighbor P12 P13 right )
(isFree P14)
(isNeighbor P14 P13 left )
(isNeighbor P13 P14 right )
(guardOn P15 G15)
(isNeighbor P15 P14 left )
(isNeighbor P14 P15 right )
(isFree P16)
(isNeighbor P16 P15 left )
(isNeighbor P15 P16 right )
(isFree P20)
(isNeighbor P20 P10 up )
(isNeighbor P10 P20 down )
(isFree P22)
(isNeighbor P22 P12 up )
(isNeighbor P12 P22 down )
(isFree P23)
(isNeighbor P23 P13 up )
(isNeighbor P13 P23 down )
(isNeighbor P23 P22 left )
(isNeighbor P22 P23 right )
(boxOn P25)
(isNeighbor P25 P15 up )
(isNeighbor P15 P25 down )
(isFree P26)
(isNeighbor P26 P16 up )
(isNeighbor P16 P26 down )
(isNeighbor P26 P25 left )
(isNeighbor P25 P26 right )
(isFree P27)
(isNeighbor P27 P26 left )
(isNeighbor P26 P27 right )
(isFree P32)
(isNeighbor P32 P22 up )
(isNeighbor P22 P32 down )
(boxOn P33)
(isNeighbor P33 P23 up )
(isNeighbor P23 P33 down )
(isNeighbor P33 P32 left )
(isNeighbor P32 P33 right )
(isFree P34)
(isNeighbor P34 P33 left )
(isNeighbor P33 P34 right )
(isFree P35)
(isNeighbor P35 P25 up )
(isNeighbor P25 P35 down )
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
(isFree P44)
(isNeighbor P44 P34 up )
(isNeighbor P34 P44 down )
(isFree P45)
(isNeighbor P45 P35 up )
(isNeighbor P35 P45 down )
(isNeighbor P45 P44 left )
(isNeighbor P44 P45 right )
(isFree P46)
(isNeighbor P46 P45 left )
(isNeighbor P45 P46 right )
(isFree P47)
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
(isNeighbor P53 P52 left )
(isNeighbor P52 P53 right )
(isFree P54)
(isNeighbor P54 P44 up )
(isNeighbor P44 P54 down )
(isNeighbor P54 P53 left )
(isNeighbor P53 P54 right )
(boxOn P56)
(isNeighbor P56 P46 up )
(isNeighbor P46 P56 down )
(boxOn P63)
(isNeighbor P63 P53 up )
(isNeighbor P53 P63 down )
(isFree P64)
(isNeighbor P64 P54 up )
(isNeighbor P54 P64 down )
(isNeighbor P64 P63 left )
(isNeighbor P63 P64 right )
(isFree P65)
(isNeighbor P65 P64 left )
(isNeighbor P64 P65 right )
(isFree P66)
(isNeighbor P66 P56 up )
(isNeighbor P56 P66 down )
(isNeighbor P66 P65 left )
(isNeighbor P65 P66 right )
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
(:goal (and (boxOn P34)
(boxOn P46)
(boxOn P53)
(boxOn P54)
(boxOn P64)
))
)