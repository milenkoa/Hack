// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.



(EVENT_LOOP)
@KBD
D=M
@BLACK_SCREEN
D;JNE
@WHITE_SCREEN
0;JMP
@EVENT_LOOP
0;JMP


(BLACK_SCREEN)
 @SCREEN
 D=A
 @address
 M=D            // address = SCREEN; points to the beginning of memory map
 
 @8192          // screen has 256 rows, each row 32 words; 256*32=8192
 //@2          // screen has 256 rows, each row 32 words; 256*32=8192
 D=A
 @n
 M=D            // n = 8192
 
 @i
 M=0            // i = 0
 
 (LOOP_BLACK)
 @i
 D=M
 @n
 D=D-M
 @EVENT_LOOP
 D;JGE

 @i
 D=M
 @address
 A=D+M
 M=-1
 @i
 M=M+1
 @LOOP_BLACK
 0;JMP
 
(WHITE_SCREEN)
 @SCREEN
 D=A
 @address
 M=D            // address = SCREEN; points to the beginning of memory map
 
 @8192          // screen has 256 rows, each row 32 words; 256*32=8192
 D=A
 @n
 M=D            // n = 8192
 
 @i
 M=0            // i = 0
 
 (LOOP_WHITE)
 @i
 D=M
 @n
 D=D-M
 @EVENT_LOOP
 D;JGE

 @i
 D=M
 @address
 A=D+M
 M=0
 @i
 M=M+1
 @LOOP_WHITE
 0;JMP

@EVENT_LOOP
0;JMP
