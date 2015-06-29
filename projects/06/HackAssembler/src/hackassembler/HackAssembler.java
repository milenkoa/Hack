/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackassembler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author milenko
 */
public class HackAssembler {
    
    private static boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch(Exception ex )
        {
            return false;
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //check suplied parameters:
        // Usage: HackAssembler *.asm
        if (args.length != 1) {
            System.out.println("Error!!!");
            return;
        }
        Map<String, Integer> symbolTable = new HashMap<String, Integer>();
        
        //initialize symboltable:
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KWD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        
        // first pass
        int currentLine = 0;
        String line1 = null;        
        BufferedReader reader1 = null;
        
        try
        {
            reader1 = new BufferedReader(
                                    new FileReader(args[0]));
        }
        catch (FileNotFoundException ex) {
            System.err.println("File not found.");
            ex.printStackTrace();
            return;
        }
        
        
        
        while((line1 = reader1.readLine()) != null) {
                if (line1.length() == 0) {
                    // this is empty line, just continue
                    System.out.println("Empty line " + Integer.toString(currentLine));
                    continue;
                }
                
                if (line1.startsWith("//")) {
                    //this is comment line, just continue
                    System.out.println("Comment line " + Integer.toString(currentLine));
                    continue;
                }
            if (line1.startsWith("(") && line1.endsWith(")")) {
                //we are seeing a new symbol             
                String key = line1.substring(1, line1.length()-1);
                System.out.println("New label: (" + key + ", " + Integer.toString(currentLine) + ")");
                symbolTable.put(key, currentLine);  
                continue;
            }
            currentLine++;
            
        }

        
        String outputFileName = args[0].substring(0, args[0].length()-4);
        System.out.println("Output file: " + outputFileName + ".hack");
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(outputFileName + ".hack");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HackAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader reader;
        try
        {
            reader = new BufferedReader(
                                    new FileReader(args[0]));
        }
        catch (FileNotFoundException ex) {
            System.err.println("File not found.");
            ex.printStackTrace();
            return;
        }
        
        
        String line = null;
        int declarationCounter = 16;
        try {
            while((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    // this is empty line, just continue
                    continue;
                }
                
                if (line.startsWith("//")) {
                    //this is comment line, just continue
                    continue;
                }
                if (line.startsWith("(") && line.endsWith(")")) {
                    continue;
                }
                // now remove heading spaces and inline comments
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//") - 1);
                                   
                }
                line = line.trim(); 
                
                
                //check if a-instruction or c-instruction
                if (line.startsWith("@")) {
                    //a-line
                    System.out.print("A-instruction: " + line + "; ");
                    String value = line.substring(1, line.length());
                    boolean isInt = isInteger(value);
                    if (isInt) {                 
                    System.out.print("Value: " + value + "; ");
                    int i = Integer.parseInt(value);
                    System.out.print("Instruction: 0"); //op-code
                    System.out.println(String.format("%15s", Integer.toBinaryString(i)).replace(' ', '0'));
                    System.out.println();
                    writer.println("0"+String.format("%15s", Integer.toBinaryString(i)).replace(' ', '0'));
                    }
                    else {
                        //here we have to check symbol table
                        // is it a new declaration?
                        if (symbolTable.containsKey(value)) {
                            //already in symbol table, just replace it
                            
                        }
                        else {
                            // this is new declaration - store it into symbol table
                            // starting from address 16
                            symbolTable.put(value, declarationCounter);
                            declarationCounter++;
                        }
                        System.out.print("Symbol: " + value + "; Value: " + symbolTable.get(value).toString());
                        System.out.print("Instruction: 0"); //op-code
                        System.out.println(String.format("%15s", Integer.toBinaryString(symbolTable.get(value))).replace(' ', '0'));
                        System.out.println();
                        writer.println("0"+String.format("%15s", Integer.toBinaryString(symbolTable.get(value))).replace(' ', '0'));
                    }
                }
                else {
                    //c-line
                    System.out.println("C-instruction: " + line + "; ");

                    //get jump code
                    String jumpCode;
                    if (line.contains(";")) {
                        
                        int index = line.indexOf(';');
                        
                        String jumpString = line.substring(index+1, line.length());
                        line = line.substring(0, index);
                        System.out.print("Jump: " + jumpString + "; ");
                        switch (jumpString) {
                            case "JMP":
                                jumpCode = "111";
                                break;
                            
                            case "JLE":
                                jumpCode = "110";
                                break;
                            
                            case "JNE":
                                jumpCode = "101";
                                break;
                            
                            case "JLT":
                                jumpCode = "100";
                                break;
                 
                            case "JGE":
                                jumpCode = "011";
                                break;
                             
                            case "JEQ":
                                jumpCode = "010";
                                break;
                                
                            case "JGT":
                                jumpCode = "001";
                                break;
                                
                            default:
                                jumpCode = "000";
                                break;
                        }
                    }
                    else {
                        jumpCode = "000";
                    }
                    System.out.println("Jump code: " + jumpCode + "; ");
                    
                    //get destination code
                    String destinationCode;
                    System.out.println("Line: " + line);
                    
                    if (line.contains("=")) {
                        int index = line.indexOf('=');
                        
                        String destinationString = line.substring(0, index);
                        line = line.substring(index+1, line.length());
                        System.out.print("Destination: " + destinationString + "; ");
                        switch (destinationString) {
                            case "M":
                                destinationCode = "001";
                                break;
                                
                            case "D":
                                destinationCode = "010";
                                break;
                                
                            case "MD":
                                destinationCode = "011";
                                break;
                                
                            case "A":
                                destinationCode = "100";
                                break;
                                
                            case "AM":
                                destinationCode = "101";
                                break;
                                
                            case "AD":
                                destinationCode = "110";
                                break;
                                
                            case "AMD":
                                destinationCode = "111";
                                break;
                                
                            default:
                                destinationCode = "000";
                                break;
                        }
                        
                    }
                    else {
                        System.out.print("No Destination; ");
                        destinationCode = "000";
                    }
                    
                    System.out.println("Destination code: " + destinationCode + "; ");
                    
                    
                    //get operation code
                    //System.out.println();
                    String operationCodeA = null;
                    String operationCodeC = null;
                    System.out.println("Line: " + line);
                    
                    switch (line) {
                        case "0":
                            operationCodeA="0";
                            operationCodeC="101010";
                            break;
                        
                        case "1":
                            operationCodeA="0";
                            operationCodeC="111111";
                            break;
                            
                        case "-1":
                            operationCodeA="0";
                            operationCodeC="111010";
                            break;
                            
                        case "D":
                            operationCodeA="0";
                            operationCodeC="001100";
                            break;
                            
                        case "A":
                            operationCodeA="0";
                            operationCodeC="110000";
                            break;
                            
                        case "M":
                            operationCodeA="1";
                            operationCodeC="110000";
                            break;
                            
                        case "!D":
                            operationCodeA="0";
                            operationCodeC="001101";
                            break;
                            
                        case "!A":
                            operationCodeA="0";
                            operationCodeC="110001";
                            break;
                            
                        case "!M":
                            operationCodeA="1";
                            operationCodeC="110001";
                            break;
                            
                        case "-D":
                            operationCodeA="0";
                            operationCodeC="001111";
                            break;
                            
                        case "-A":
                            operationCodeA="0";
                            operationCodeC="110011";
                            break;
                            
                        case "-M":
                            operationCodeA="1";
                            operationCodeC="110011";
                            break;
                            
                        case "D+1":
                            operationCodeA="0";
                            operationCodeC="011111";
                            break;
                            
                        case "A+1":
                            operationCodeA="0";
                            operationCodeC="110111";
                            break;
                            
                        case "M+1":
                            operationCodeA="1";
                            operationCodeC="110111";
                            break;
                            
                        case "D-1":
                            operationCodeA="0";
                            operationCodeC="001110";
                            break;
                        
                        case "A-1":
                            operationCodeA="0";
                            operationCodeC="110010";
                            break;
                            
                        case "M-1":
                            operationCodeA="1";
                            operationCodeC="110010";
                            break;
                            
                        case "D+A":
                            operationCodeA="0";
                            operationCodeC="000010";
                            break;
                            
                        case "D+M":
                            operationCodeA="1";
                            operationCodeC="000010";
                            break;
                            
                        case "D-A":
                            operationCodeA="0";
                            operationCodeC="010011";
                            break;
                            
                        case "D-M":
                            operationCodeA="1";
                            operationCodeC="010011";
                            break;
                            
                        case "A-D":
                            operationCodeA="0";
                            operationCodeC="000111";
                            break;
                            
                        case "M-D":
                            operationCodeA="1";
                            operationCodeC="000111";
                            break;
                            
                        case "D&A":
                            operationCodeA="0";
                            operationCodeC="000000";
                            break;
                            
                        case "D&M":
                            operationCodeA="1";
                            operationCodeC="000000";
                            break;
                            
                        case "D|A":
                            operationCodeA="0";
                            operationCodeC="010101";
                            break;
                            
                        case "D|M":
                            operationCodeA="1";
                            operationCodeC="010101";
                            break;                      
                 
                    }
                    System.out.println("Operation code: 111" + operationCodeA + operationCodeC);
                    
                    writer.println("111" + operationCodeA + operationCodeC + destinationCode + jumpCode);
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HackAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.close();
        //System.out.println("Line: " + line);
        return;
    }
    
}
