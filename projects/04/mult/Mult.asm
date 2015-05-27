// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
@sum  // now A points to location sum, and we should sum=M[A]=0
M=0   // sum=0
@i    // now A point to i, and we should i=M[i]=1
M=0
M=M+1 // i=1
(LOOP)
@i    // now A point to i
D=M
@R1   // now A points to RAM[1]
D=D-M // D = i - RAM[1]
@END
D;JGT // if (i - RAM[1] > 0) goto END
@R0
D=M
@sum
M=D+M
@i
M=M+1
@LOOP
0;JMP

(END)
@sum
D=M
@R2
M=D
(INFINITE)
@INFINITE
0;JMP
