//OS Project Phase 1 by Maryam Tauqeer & Minal Sarwar 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class OSProjectPhase1 {
    
    public static void main(String[] args) throws FileNotFoundException{
        //File reading
        try {
            int ind=0;
            File myObj = new File("C:\\Users\\Home\\OneDrive\\Desktop\\p1.txt");
            Scanner myReader = new Scanner(myObj);
            
            //Storing in memory
            while (myReader.hasNext()) {
              int data = myReader.nextInt();
              System.out.println("Data: "+ data);
              Memory.mem[ind]=(byte)data;
              ind++;
              SPR.cc++;
            }
            //Byte may result in negative value hence converting to unsigned int
            System.out.println("Memory");
            for (int i=0; i<12; i++){
                System.out.print(Byte.toUnsignedInt(Memory.mem[i])+", "); //hex values: 30 01 00 01 30 02 7f ff 19 01 02 f3
            }
            System.out.println("");
            SPR.cc=(short)(ind-1);
            myReader.close();
            
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        //Traversing through the memory
        SPR.pc=SPR.cb; //setting pc to cb
        String opcode=Integer.toHexString(Byte.toUnsignedInt(Memory.mem[SPR.pc])).toUpperCase();
       
        //while pc is less than or equal to cc
        while_loop: while (SPR.pc<=SPR.cc){
            SPR.ir=(short)Byte.toUnsignedInt(Memory.mem[SPR.pc]);
            
            //display methods of GPR & SPR
            GPR.displayInDecimal();
            GPR.displayInHexa();
            
            SPR.updatingSPR();
            SPR.displayInDecimal();
            SPR.displayInHexa();
            SPR.updatingFlag();
            SPR.displayFlagRegister();
            
            int R1=0; //R1 and R2 are GPR arrays indexes to identify the registers
            int R2=0;
            int num2; //num2 used in flag and memory instructions
            short num;
            
            //whatever the instruction, method is invoked accordingly and pc is incremented inside that method
            switch (opcode){
                
                //Register-Register Instructions
                case "16": //Copy Register Contents
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.MOV(R1, R2);
                    break;
                	
                case "17": //Add Register Contents
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); 
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.ADD(R1, R2);
                    break;
                    
                case "18"://Subtract
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); 
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.SUB(R1, R2);
                    break;
                    
                case "19"://Multiply
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); //direct addressing, no hex translation
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.MUL(R1, R2);
                    break;
                
                case "1A"://Division
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); 
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.DIV(R1, R2);
                    break;
                	
                case "1B"://Logical AND
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); 
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.AND(R1, R2);
                    break;
                	
                case "1C"://Logical OR
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); 
                    R2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_R.OR(R1, R2);
                    break;
                	
                //Register-Immediate Instructions
                case "30"://Copy Immediate to register
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]); //direct addressing, no hex translation
                    num= (short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.MOVI(R1, num);
                    break;
                
                case "31"://Add
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.ADDI(R1, num);
                    break;
                    
                case "32"://Subtract
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.SUBI(R1, num);
                    break;
                	
                case "33"://Multiply
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.MULI(R1, num);
                    break;
                    
                case "34":   //Divide
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.DIVI(R1, num);
                    break;
                	
                case "35": //Logical AND
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.ANDI(R1, num);
                    break;
                    
                case "36"://Logical OR
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num=(short) (Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]));
                    R_I.ORI(R1, num);
                    break;
                    
                case "37"://Branch equal to zero
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+1])+Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_I.BZ(num2);
                   
                case "38"://Branch if not zero
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+1])+Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_I.BNZ(num2);
                    
                case "39"://Branch if carry
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+1])+Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_I.BC(num2);
                    
                case "3A"://Branch if sign
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+1])+Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_I.BS(num2);
                
                case "3B"://Jump
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+1])+Byte.toUnsignedInt(Memory.mem[SPR.pc+2]);
                    R_I.JMP(num2);
                
                //Memory Instructions
                    
                case "51": //Load Word from memory
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]); //will add both numbers & then mov in R1
                    MemoryInst.MOVL(R1, num2);
                    
                case "52"://Store Word in memory
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    num2=Byte.toUnsignedInt(Memory.mem[SPR.pc+2])+Byte.toUnsignedInt(Memory.mem[SPR.pc+3]);
                    MemoryInst.MOVS(R1, num2);
                
                //Single Operand Instructions
                
                case "71"://Shift Left Logical
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.SHL(R1);
                    break;
                    
                case "72"://Shift Right Logical
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.SHR(R1);
                    break;

                case "73"://Rotate Left
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.RTL(R1);
                    break;
                        
                case "74"://Rotate Right
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.RTR(R1);
                    break;

                case "75"://Increment
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.INC(R1);
                    break;
                    
                case "76"://Decrement
                    R1=Byte.toUnsignedInt(Memory.mem[SPR.pc+1]);
                    SingleOp.DEC(R1);
                    break;
                    
                //No Operand Instructions
                
                case "F2":
                    NoOp.NOOP();
                    break;
                    
                case "F3": //end of process
                    NoOp.END();
                    break while_loop;
                
                default:
                    System.out.println("ERROR - INVALID OPCODE");
            }
            R1=0;R2=0;num=0;num2=0;
            opcode=Integer.toHexString(Byte.toUnsignedInt(Memory.mem[SPR.pc])).toUpperCase();
            
        }
        //display methods of GPR & SPR
        GPR.displayInDecimal();
        GPR.displayInHexa();

        SPR.updatingSPR();
        SPR.displayInDecimal();
        SPR.displayInHexa();
        SPR.updatingFlag();
        SPR.displayFlagRegister();
        
        System.out.println("Program terminated");
    }
    
    
}

class Memory{
    public static byte[] mem= new byte[65536];
}

class GPR{
    public static short[] GPR=new short[16];
    
    //Displaying GPR in decimal
    public static void displayInDecimal(){
        System.out.println("GPR in decimal: ");
        System.out.print("R0"+"="+GPR[0]);
        for (int i=1; i<GPR.length; i++){
            System.out.print(", R"+(i)+"="+GPR[i]);
        }
        System.out.println("");
    }
    //Displaying GPR in hexa
    public static void displayInHexa(){
        System.out.println("GPR in hexa: ");
        System.out.print("R0"+"="+GPR[0]);
        for (int i=1; i<GPR.length; i++){
            System.out.print(", R"+(i)+"="+Integer.toHexString(GPR[i]));
        }
        System.out.println("\n");
    }
}


class SPR{
    //creating an array of size 15 for the SPR (except the flag register, which
    // is created separately
    public static short[] SPR=new short[15];
    public static boolean[] flag= new boolean[16];
    public static short cb, cl, cc, pc, ir=0;    
    public static boolean carry, zero, sign, overflow;
    
    static String[] sprNames= {"CB","CL", "CC", "DB", "DL", "DC"
    , "SB", "SC", "SL", "PC", "IR" };
    static String[] flagNames= {"Carry", "Zero", "Sign", "Overflow"};
 
    //in phase 1 we have not updated CL, DB or DL bec there were no defined data and code segments for memory
    public static void updatingSPR(){
        SPR[0]=cb;
        SPR[1]=cl;
        SPR[2]=cc;
        SPR[9]=pc;
        SPR[10]=ir;
        //CB is SPR[0]
        //CL is SPR[1]
        //CC is SPR[2]
        //DB is SPR[3]
        //DL is SPR[4]
        //DC is SPR[5]
        //PC is SPR[9]
        //IR is SPR[10]
    }
    
    //updating the 4 flag registers -carry, zero, sign, overflow
    public static void updatingFlag(){
        flag[0]=carry;
        flag[1]=zero;
        flag[2]=sign;
        flag[3]=overflow;
    }
    
    //Displaying SPR in decimal
    public static void displayInDecimal(){
        System.out.println("SPR in decimal: ");
        for (int i=0; i<=9; i++){
            System.out.print(sprNames[i]+"="+SPR[i]+", ");
        }
        System.out.println(sprNames[10]+"="+SPR[10]);
    }
    
  //Displaying SPR in hexa
    public static void displayInHexa(){
        System.out.println("SPR in hexa: ");
        for (int i=0; i<=9; i++){
            System.out.print(sprNames[i]+"="+Integer.toHexString(SPR[i])+", ");
        }
        System.out.println(sprNames[10]+"="+Integer.toHexString(SPR[10]));
    }
    
    //Displaying flag register values
    public static void displayFlagRegister(){
        System.out.println("Flag Register: ");
        for (int i=0; i<=2; i++){
            System.out.print(flagNames[i]+"="+Boolean.compare(flag[i], false)+", ");
        }
        System.out.println(flagNames[3]+"="+Boolean.compare(flag[3], false)+"\n");
    }
    
    //flag[0]=carry, flag[1]=zero, flag[2]=sign, flag[3]=overflow

    public static void setCarry(short value){
        if (value>=Math.pow(2,15)){ //2^15 to check if the most significant bit is 1
            carry = true;
        }
    }
    
    public static void setZero(int R1){
        zero = GPR.GPR[R1]==0;
    }
    
    public static void setSign(int R1){
        sign = GPR.GPR[R1]<0;
    }
    
    public static void setOverflow(short result){
        if (result>=Math.pow(2,15)){
            overflow = true;
        }
    }
    
    public static void resetCarry(){
        carry=false;
    }
    
    public static void resetOverflow(){
        overflow=false;
    }
    
    
}


class R_R{
    
    public static void MOV(int R1, int R2){
        GPR.GPR[R1]=GPR.GPR[R2];
        SPR.pc+=3; //pc is incrememnted by 3 to jump on the next instruction
    }
    
    public static void ADD(int R1, int R2){
        GPR.GPR[R1]+=GPR.GPR[R2];
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=3;
    }
    
    public static void SUB(int R1, int R2){
        GPR.GPR[R1]-=GPR.GPR[R2];
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=3;
    }
    
    public static void MUL(int R1, int R2){
        GPR.GPR[R1]*=GPR.GPR[R2];
        SPR.setZero(R1);
        SPR.setSign(R1);
        System.out.println("\nMUL performed\n");
        SPR.pc+=3;
    }
    
    public static void DIV(int R1, int R2){
        GPR.GPR[R1]/=GPR.GPR[R2];
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=3;
    }
    
    public static void AND(int R1, int R2){
        GPR.GPR[R1]=(short)(GPR.GPR[R1] & GPR.GPR[R2]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=3;
    }
    
    public static void OR(int R1, int R2){
        GPR.GPR[R1]=(short)(GPR.GPR[R1] | GPR.GPR[R2]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=3;
    }
    
}

class R_I{
    
    public static void MOVI(int R1, short num){
        GPR.GPR[R1]=num;
        System.out.println("\nMOVI performed\n");
        SPR.pc+=4;//pc is incrememnted by 4 to jump on the next instruction
        
    }
    
    public static void ADDI(int R1, short num){
        GPR.GPR[R1]+=num;
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void SUBI(int R1, short num){
        GPR.GPR[R1]-=num;
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void MULI(int R1, short num){
        GPR.GPR[R1]*=num;
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void DIVI(int R1, short num){
        GPR.GPR[R1]/=num;
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void ANDI(int R1, short num){
        GPR.GPR[R1]=(short) (GPR.GPR[R1] & num);
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void ORI(int R1, short num){
        GPR.GPR[R1]=(short) (GPR.GPR[R1] | num);
        SPR.setOverflow(GPR.GPR[R1]);
        SPR.setZero(R1);
        SPR.setSign(R1);
        SPR.pc+=4;
    }
    
    public static void BZ(int offset){
        if (SPR.zero){
            SPR.pc=(short)(SPR.cb+offset);
        }
        else{
            SPR.pc+=3;
        }
        
    }
    
    public static void BNZ(int offset){
        if (!SPR.zero){
            SPR.pc=(short)(SPR.cb+offset);
        }
        else{
            SPR.pc+=3;
        }
    }
    
    public static void BC(int offset){
        if (SPR.carry){
            SPR.pc=(short)(SPR.cb+offset);
        }
        else{
            SPR.pc+=3;
        }
    }
    
    public static void BS(int offset){
        if (SPR.sign){
            SPR.pc=(short)(SPR.cb+offset);
        }
        else{
            SPR.pc+=3;
        }
    }
    
    public static void JMP(int offset ){
        SPR.pc=(short)(SPR.cb+offset);
        SPR.pc+=3;
    }
    
}

class MemoryInst{
    
    public static void MOVL(int R1, int offset){
        //note: location is the index value of the mem array
        //DB is SPR[3] and new location=DB+Offset
        
        GPR.GPR[R1]=(short)Byte.toUnsignedInt(Memory.mem[SPR.SPR[3]+offset]);
    }
    
    public static void MOVS(int R1, int offset){
        //note: location is the index value of the mem array
        //DB is SPR[3] and new location=DB+Offset
        
        //we are breaking down the short value inside the register bec data is stored in bytes in the data section
        //of the main memory
        if ((byte)GPR.GPR[R1]>255){
            Memory.mem[SPR.SPR[3]+offset]=(byte)(GPR.GPR[R1]-255);
            Memory.mem[SPR.SPR[3]+offset+1]=(byte)255;
        }
        else{
            Memory.mem[SPR.SPR[3]+offset]=(byte)(GPR.GPR[R1]);
        }
        
    }
}

class SingleOp{
    
    public static void SHL(int R1){
        SPR.setCarry(GPR.GPR[R1]);
        GPR.GPR[R1]=(short)(GPR.GPR[R1]<<1);
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    public static void SHR(int R1){
        SPR.setCarry(GPR.GPR[R1]);
        GPR.GPR[R1]=(short)(GPR.GPR[R1]>>1);
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    public static void RTL(int R1){
        SPR.setCarry(GPR.GPR[R1]);
        GPR.GPR[R1] = (short)Integer.rotateLeft(GPR.GPR[R1], 1);
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    public static void RTR(int R1){
        SPR.setCarry(GPR.GPR[R1]);
        GPR.GPR[R1] = (short)Integer.rotateRight(GPR.GPR[R1], 1);
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    public static void INC(int R1){
        GPR.GPR[R1]++;
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    public static void DEC(int R1){
        GPR.GPR[R1]--;
        SPR.setZero(R1);
        SPR.setSign(R1);
    }
    
    
}

class NoOp{
    public static void RETURN(){
        //no implementation of stack operations in phase I
    }
    
    public static void NOOP(){
        
    }
    
    public static void END(){
        SPR.ir=243;
    }
}

