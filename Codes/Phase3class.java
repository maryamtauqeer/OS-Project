//OS Project Phase 3 by Maryam Tauqeer & Minal Sarwar

package Phase3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Phase3class {
    
    public static void main(String args[]) throws FileNotFoundException{

        while (true){
            String pid;
            Scanner sc= new Scanner(System.in);
            System.out.println("\nEnter command. Press 0 to exit");
            String command=sc.nextLine();
            
            if (command.equals("0")){
               break; 
            }
            else{
                switch (command){
                case "load":
                    System.out.println("Enter file name");
                    String fname=sc.nextLine();
                    ProgramLoading.fileReading(fname).displayPCBInfo();
                    break;

                case "run -p":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB process: Scheduling.LoadedPCB){
                        if (pid.equals(process.pID)){
                            Execution.ExecuteProcess(process, 8, false, false);
                            process.displayPCBInfo();
                        }
                    }
                    break;

                case "debug -p":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB process: Scheduling.LoadedPCB){
                        if (pid.equals(process.pID)){
                            Scheduling.enqueue_RunningQueue(process);
                            Execution.ExecuteProcess(process, 8, false, true);
                            process.displayPCBInfo();
                        }
                    }
                    break;

                case "debug -a":
                    for (PCB process: Scheduling.queue1){
                        Execution.ExecuteProcess(process, 8, false, true);
                        process.displayPCBInfo();
                    }
                    
                    for (PCB process: Scheduling.queue2){
                        Execution.ExecuteProcess(process, 8, false, true);
                        process.displayPCBInfo();
                    }
                    break;

                case "run -a":
                    for (PCB process: Scheduling.LoadedPCB){
                        for (PCB blocked_process: Scheduling.blocked){
                            if (!process.pID.equals(blocked_process.pID)){
                             //process added to the ready queue
                             Scheduling.enqueue_ReadyQueue(process); 
                            }
                        } 
                    }
                    
                        System.out.println("Printing queue 1");
                        Scheduling.printPriorityQueue();
                        System.out.println("Printing queue 2");
                        Scheduling.printQueue(Scheduling.queue2);
                        System.out.println();
                        
                        //multi level scheduling with time slice=8
                        Scheduling.multiLevelScheduling(8); 
                    
                    break;

                case "kill -p":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB p: Scheduling.LoadedPCB){
                        if (pid.equals(p.pID)){
                            for (int pageNo=0; pageNo<p.pageTable.pageArray.length; pageNo++){
                                int frameNo=p.pageTable.getFrame(p, pageNo);
                                for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
                                    Memory.mem[frameNo][frameIndex]=0;
                                }
                            }
                            Scheduling.LoadedPCB.remove(p);
                            break;
                        }
                    }
                    for (PCB p: Scheduling.queue1){
                        if (pid.equals(p.pID)){
                            Scheduling.queue1.remove(p);
                            break;
                        }
                    }
                    
                    for (PCB p: Scheduling.queue2){
                        if (pid.equals(p.pID)){
                            Scheduling.queue2.remove(p);
                            break;
                        }
                    }
                    break;
                
                    
                case "block":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    
                    for (PCB process: Scheduling.LoadedPCB){
                        if (pid.equals(process.pID)){
                            Scheduling.blocked.add(process);
                        }
                    }
                    
                    for (PCB p: Scheduling.queue1){
                        if (pid.equals(p.pID)){
                            Scheduling.queue1.remove(p);
                            break;
                        }
                    }
                    
                    for (PCB p: Scheduling.queue2){
                        if (pid.equals(p.pID)){
                            Scheduling.queue2.remove(p);
                            break;
                        }
                    }
                
                 break;
                
                case "unblock":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB process: Scheduling.blocked){
                        if (pid.equals(process.pID)){
                            Scheduling.blocked.remove(process);
                            Scheduling.enqueue_ReadyQueue(process);
                        }
                    }
                    
                break;    
                    
                case "list -a" : 
                    for (PCB process: Scheduling.LoadedPCB){
                        process.displayPCBInfo();
                    }
                    break;
                    
                case "list -b" : 
                    for (PCB process: Scheduling.blocked){
                        process.displayPCBInfo();
                    }
                    break;
                    
                case "list -r":
                    System.out.println("Printing queue 1");
                    Scheduling.printPriorityQueue();
                    System.out.println("Printing queue 2");
                    Scheduling.printQueue(Scheduling.queue2);
                    System.out.println();
                    break;
                
                case "list -e":
                    System.out.print("Running queue:");
                    Scheduling.printQueue(Scheduling.runningQueue);
                    break;
                
                case "display -p":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB process: Scheduling.LoadedPCB){
                        if (pid.equals(process.pID)){
                            process.displayPCBInfo();
                        }
                    }
                    break;
                
                case "display -m":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    for (PCB process: Scheduling.LoadedPCB){
                        if (pid.equals(process.pID)){
                            process.pageTable.displayPageTable(process);
                        }
                    }
                    break;
                
                case "dump":
                    System.out.println("Enter process ID (PID)");
                    pid=sc.nextLine();
                    
                    for (PCB p: Scheduling.LoadedPCB){
                        if (pid.equals(p.pID)){
                            //dump file 
                            String pname=p.name;
                            PrintStream o = new PrintStream(new File("C:\\Maryam's docs\\Assignments\\5th sem\\OS\\DumpFiles\\"+pname+".dump.txt"));
                            PrintStream console = System.out;
                            System.setOut(o);
                            p.displayPCBInfo();
                            System.setOut(console);
                            p.displayPCBInfo();
                            System.setOut(o);
                            //display mem
                            for (int pageNo=0; pageNo<p.pageTable.pageArray.length; pageNo++){
                                int frameNo=p.pageTable.getFrame(p, pageNo);
                                System.out.print("Page"+pageNo+": ");
                                System.setOut(console);
                                System.out.print("Page"+pageNo+": ");
                                System.setOut(o);
                                for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
                                    System.out.print(Integer.toHexString(Byte.toUnsignedInt(Memory.mem[frameNo][frameIndex]))+", ");
                                    System.setOut(console);
                                    System.out.print(Integer.toHexString(Byte.toUnsignedInt(Memory.mem[frameNo][frameIndex]))+", ");
                                    System.setOut(o);
                                }
                                System.out.println("");
                                System.setOut(console);
                                System.out.println("");
                                System.setOut(o);
                            }
                            System.setOut(console);
                            System.out.println("\nDump File Created");
                            break;
                        }
                    }
                    break;
                
                case "frames -f":
                    //assuming that the frameNo indicates the location of the free frame
                    Memory.createFreePageList();
                    System.out.println("Free frames");
                    Memory.displayFreePageList();
                
                break;
                
                case "mem":
                    for (PCB p: Scheduling.LoadedPCB){
                        System.out.println("Memory for "+p.name);
                        for (int pageNo=0; pageNo<p.pageTable.pageArray.length; pageNo++){
                            int frameNo=p.pageTable.getFrame(p, pageNo);
                            System.out.print("Page"+pageNo+": ");
                            for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
                                System.out.print(Integer.toHexString(Byte.toUnsignedInt(Memory.mem[frameNo][frameIndex]))+", ");
                            }
                            System.out.println("");
                        }
                    }
                    System.out.println("");
                    break;
                
                case "frames -a":
                    for (PCB process: Scheduling.LoadedPCB){
                        System.out.println("Process ID: "+process.pID);
                        process.pageTable.displayPageTable(process);
                    }
                    break;
                
                case "registers":
                    for (PCB process: Scheduling.LoadedPCB){
                        System.out.println("Process name: "+process.name+", "+"Process ID: "+process.pID);
                        process.gpr.displayInHexa();
                        process.gpr.displayInDecimal();
                        process.spr.displayInDecimal();
                        process.spr.displayInHexa();
                        process.spr.displayFlagRegister();
                    }
                break;
                
                case "Shutdown":
                    for (PCB p: Scheduling.LoadedPCB){
                        System.out.println("Process ID: "+p.pID);  
                    }
                    
                    for (int frameNo=0; frameNo<512; frameNo++){
                        for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
                            Memory.mem[frameNo][frameIndex]=0;
                        }
                    }
                    
                    for (PCB p: Scheduling.queue1){
                        Scheduling.queue1.clear();
                    }
                    
                    for (PCB p: Scheduling.queue2){
                        Scheduling.queue2.clear();
                    }
                    System.out.println("OS shutdown, and so did we :/ ");
                    break;
                
                default:
                    System.out.println("Error! Incorrect command.");     
                
                }
            }
        }
//        
        Memory.displayMemory();
//        System.out.println("Printing queue 1");
//        Scheduling.printPriorityQueue();
//        System.out.println("Printing queue  2");
//        Scheduling.printQueue(Scheduling.queue2);
        System.out.println();
//        
//        for (PCB process: Scheduling.LoadedPCB){
//            process.displayPCBInfo();
//            System.out.println("");
//        }
//        

            


        
        
//        ProgramLoading.fileReading();
        
//        System.out.println("Printing queue 1");
//        Scheduling.printPriorityQueue();
//        System.out.println("Printing queue 2");
//        Scheduling.printQueue(Scheduling.queue2);
//        System.out.println();
//        
//        
//        //multi level scheduling with time slice=8
//        Scheduling.multiLevelScheduling(8);
//        
//        //printing on console
//        System.out.println("\nPrinting the PCB\n");
//        for (PCB p: Scheduling.terminated) {
//            p.displayPCBInfo();
//            
//            //display mem
//            for (int pageNo=0; pageNo<p.pageTable.pageArray.length; pageNo++){
//                int frameNo=p.pageTable.getFrame(p, pageNo);
//                System.out.print("Page"+pageNo+": ");
//                for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
//                    System.out.print(Integer.toHexString(Byte.toUnsignedInt(Memory.mem[frameNo][frameIndex]))+", ");
//                }
//                System.out.println("");
//            }
//            System.out.println("");
//        }
//        
//        //dump file 
//        PrintStream o = new PrintStream(new File("OSPhase2_dumpfile.txt"));
//        PrintStream console = System.out;
//        System.setOut(o);
//
//        System.out.println("DUMP FILE FOR OS\n\n");
//        System.out.println("\nPrinting the PCB\n");
        for (PCB p: Scheduling.terminated) {
            p.displayPCBInfo();
            
            //display mem
            for (int pageNo=0; pageNo<p.pageTable.pageArray.length; pageNo++){
                int frameNo=p.pageTable.getFrame(p, pageNo);
                System.out.print("Page"+pageNo+": ");
                for (int frameIndex=0; frameIndex<Memory.mem[0].length; frameIndex++){
                    System.out.print(Integer.toHexString(Byte.toUnsignedInt(Memory.mem[frameNo][frameIndex]))+", ");
                }
                System.out.println("");
            }
            System.out.println("");
        }
//        
//        System.setOut(console);

          
//          Memory.displayMemory();
//          System.out.println("Free Page List");
//          Memory.createFreePageList();
//          Memory.displayFreePageList();

    }   
}

class PCB implements Comparable<PCB>{
    String pID;
    int priority;
    int process_size;
    int code_size;
    int data_size;
    String name;
    GPR gpr;
    SPR spr;
    PageTable pageTable;
    int exec_time;
    int waiting_time; 

    public PCB(String pID, int priority, int process_size, int code_size, int data_size, String name, int numOfPages){
        this.pID=pID;
        this.priority=priority;
        this.process_size=process_size;
        this.code_size=code_size;
        this.data_size=data_size;
        this.name=name;
        gpr= new GPR();
        spr= new SPR();
        pageTable= new PageTable(numOfPages);
    }
    
    
    @Override
    public int compareTo(PCB pcbObj) {
        return pcbObj.priority < this.priority ? 1 : -1;
    }

    
    public void displayPCBInfo(){
        System.out.println("Name: "+this.name);
        System.out.println("Priority: "+this.priority);
        System.out.println("Process ID: "+this.pID);
        System.out.println("Data size: "+this.data_size);
        System.out.println("Code size: "+this.code_size);
        System.out.println("Process size: "+this.process_size);
        System.out.println("Execution time: "+this.exec_time);
        System.out.println("Waiting time: "+this.waiting_time);
        this.gpr.displayInHexa();
        this.gpr.displayInDecimal();
        this.spr.displayInDecimal();
        this.spr.displayInHexa();
        this.spr.displayFlagRegister();
        this.pageTable.displayPageTable(this);
        System.out.println("\n");
    }

}  

class ProgramLoading{
    static int frameNo=0;
    static int frameIndex=0;
    
    public static PCB fileReading(String fname){
        PCB pcbObj = null;
        //File reading for the 6 processes
        try{
          File folder= new File("C:\\Maryam's docs\\Assignments\\5th sem\\OS\\Files");
          File[] files = folder.listFiles();
          File file=null;
          
          for (int i = 0; i < files.length; i++){
            if (fname.equals(files[i].getName())){
                file=files[i];
                break;
            }
          }
          
            FileInputStream f=new FileInputStream(file);
            
            int byteRead;
            int byteCounter=0;
            String pID="";
            int priority=0;
            int process_size;
            int code_size;
            int data_size=0;
            int stacksize=0;
            String name=file.getName();
            String temp_datasize="";
            
            String SpID="";
            String Sdatasize="";

            int firstFrameNo=frameNo;
          
            while ((byteRead = f.read() )!=-1){
                byteCounter++;
                
                //Extracting attributes for PCB
                if (byteCounter<=5){ switch (byteCounter){
                    case 1:
                        priority=byteRead;
                        break;
                    case 2:
                        SpID=String.format("%02X", byteRead);break;
                    case 3:
                        SpID=SpID+String.format("%02X", byteRead);
                        pID=SpID;break;
                    case 4:
                        Sdatasize=String.format("%02X", byteRead);break;
                    case 5:
                        Sdatasize=Sdatasize+String.format("%02X", byteRead);
                        if (Integer.parseInt(Sdatasize,16)==8224)data_size=0;
                        else data_size=Integer.parseInt(Sdatasize,16);break;   
                }}
                
                //Inserting the process bytes into memory frames
                
                //if frameIndex==127
                if (byteCounter<=8) continue;
                if (frameIndex==Memory.mem[0].length){
                    frameIndex=0;
                    frameNo++;
                }
                Memory.insertInMemory(frameNo, frameIndex, byteRead);
                frameIndex++;
            }

            process_size=byteCounter;
            code_size=process_size-data_size-8;
            
            
            
            //creating PCB after validating the priority, data size and code size
            if (validatingPCB(priority, data_size, code_size)){
                
                //reserving 50 bytes for stack
                System.out.println(frameIndex);
                frameIndex+=50;
                if (frameIndex>=Memory.mem[0].length-1){
                    stacksize=(short)(127-(frameIndex-50));
                }
                else{
                    stacksize=50;
                }
                
                int numOfPages=(int)Math.ceil((process_size+stacksize-8)/128.0); //for page table creation
                pcbObj=new PCB(pID, priority, process_size, code_size, data_size, name, numOfPages);
                pcbObj.pageTable.fillPageTable(pcbObj, firstFrameNo); //page table filled
                
                //process added to the ready queue
                Scheduling.enqueue_ReadyQueue(pcbObj);

                //add to loaded linkedlist
                Scheduling.LoadedPCB.add(pcbObj);
                
                // once the process is loaded completely in memory, code and data segmentation is done
                pcbObj.spr.stackSize=(short)stacksize;
                Memory.segmentation(pcbObj);

                
                System.out.println("Process "+file.getName()+" loaded successfully");
                
                //once a process is inserted in memory, the next process is inserted in the next frame
                frameIndex=0;
                frameNo++;
            }

            f.close();
            
//            //reserving 50 bytes for stack
//            frameIndex+=50;
//            if (frameIndex>=Memory.mem[0].length-1){
//                
//                frameIndex=0;
//                frameNo++;
//            }
//
//            //once a process is inserted in memory, the next process is inserted in the next frame
//            frameIndex=0;
//            frameNo++;
//
//            System.out.println("Process "+file.getName()+" loaded successfully");
//            
        }

        catch(Exception e){
            System.out.println(e);   
        }
        return pcbObj;
    }
    
    public static boolean validatingPCB(int priority, int data_size, int code_size){
        if ((priority>=0 && priority<=31)){
            return true;
        }
        else return false;
    }
    
}

class Scheduling{
    public static PriorityQueue<PCB> queue1=new PriorityQueue<PCB>();
    public static Queue<PCB> queue2=new LinkedList<PCB>(); 
    public static Queue<PCB> runningQueue=new LinkedList<PCB>();
    public static Queue<PCB> terminated=new LinkedList<PCB>();
    public static LinkedList<PCB> LoadedPCB= new LinkedList<PCB>();
    public static Queue<PCB> blocked=new LinkedList<PCB>();
    
    public static void enqueue_ReadyQueue(PCB pcbObj){
        if (pcbObj.priority<=15){
            queue1.add(pcbObj);
        }else{
            queue2.add(pcbObj);
        }
    }
    
    public static void enqueue_RunningQueue(PCB pcbObj){
        runningQueue.add(pcbObj);
    }
    
    public static void dequeue_RunningQueue(){
        runningQueue.remove();
    }
    
    public static PCB dequeuePriorityQueue(){
        return queue1.poll();
    }
    
    public static PCB dequeueQueue(Queue<PCB> queue){
        return queue.poll();
    }
    
    public static void printPriorityQueue(){
        int count=0;
        for (PCB process: queue1) {
            count++;
//            System.out.print(process.name+","+process.priority+"  ");
            process.displayPCBInfo();
        }
        System.out.println("");
    }
    
     public static void printQueue(Queue<PCB> queue){
        int count=0;
        for (PCB process: queue) {
            count++;
//            System.out.print(process.name+","+process.priority+"  ");
            process.displayPCBInfo();
        }
        System.out.println("");
    }
    
    public static void priorityScheduling(){
        
        while (!queue1.isEmpty()){
            System.out.print("Queue 1: ");
            Scheduling.printPriorityQueue();
            
            enqueue_RunningQueue(dequeuePriorityQueue());
            
            System.out.print("Running queue:");
            Scheduling.printQueue(Scheduling.runningQueue);
            
            Execution.ExecuteProcess(runningQueue.peek(), 8, false, false); //false indicates it's not round robin
            
            Scheduling.dequeue_RunningQueue();
        }
        
    }
    
    public static void roundRobin(int timeSlice){
        while (!queue2.isEmpty()){
            System.out.print("Queue 2: ");
            Scheduling.printQueue(Scheduling.queue2);
            
            enqueue_RunningQueue(Scheduling.dequeueQueue(queue2));
            
            System.out.print("Running queue:");
            Scheduling.printQueue(Scheduling.runningQueue);
            
            if (queue2.size()>=1){
                PCB process=Execution.ExecuteProcess(runningQueue.peek(), timeSlice, true, false);//true indicates it is round robin
                if (process.spr.ir!=243){
                Scheduling.enqueue_ReadyQueue(process);
                }
            }
            else{
                Execution.ExecuteProcess(runningQueue.peek(), timeSlice, false, false);
            }
            
            if (!Scheduling.runningQueue.isEmpty()){
                Scheduling.dequeue_RunningQueue();
            }
            
        }
    }
    
    public static void multiLevelScheduling(int timeSlice){

        priorityScheduling();

        roundRobin(timeSlice);
    }
    
}

class PageTable {

    int[] pageArray;
    
    PageTable(int numOfPages){
        pageArray= new int[numOfPages];
    }
    
    public void fillPageTable(PCB obj, int firstFrameIndex){
        int frameCounter=firstFrameIndex;
        for (int i=0; i<obj.pageTable.pageArray.length; i++){
            obj.pageTable.pageArray[i]=frameCounter;
            frameCounter++;
        }
    }
    
    public void displayPageTable(PCB obj){
        System.out.println("Page Table for "+obj.name);
        for (int i=0; i<obj.pageTable.pageArray.length; i++){
            System.out.println("Page: "+i+", Frame: "+obj.pageTable.pageArray[i]);
        }
    }
    
    public int getFrame(PCB pcbObj, int page){
        return pcbObj.pageTable.pageArray[page];
    }
}

class Memory{
    //128 size for each frame
    //512 frames in the memory
    public static byte [][] mem= new byte[512][128];
    public static ArrayList<Integer> freePageList = new ArrayList<Integer>();
    
    
    public static void insertInMemory(int frameNo, int frameIndex, int byteRead){
        mem[frameNo][frameIndex]=(byte)byteRead;
    }
    
    public static void displayMemory(){
        for (int frameNo=0; frameNo<=30; frameNo++){
            System.out.println("Frame= "+frameNo);
            for (int frameIndex=0; frameIndex<mem[0].length; frameIndex++){
                System.out.print(Integer.toHexString(Byte.toUnsignedInt(mem[frameNo][frameIndex]))+", ");
            }
            System.out.println("");
        }
    }
    
    //assuming frames are occupied contiguously in memory
    //so free pages after the last occupied frame
    public static void createFreePageList(){
//        for (int i=0; i<mem.length; i++){
////            if (mem[i][0]==0 && mem[i][2]==0 && mem[i][125]==0 && mem[i][127]==0){
//                for (PCB process: Scheduling.LoadedPCB){
//                    for (int j=0; j<process.pageTable.pageArray.length;  j++){
//                        if (i!=process.pageTable.pageArray[j]){
//                            freePageList.add(i);
//                        }
//                    }
//                    
//                }
//        }

        for (int i=0; i<mem.length; i++){
            if (mem[i][0]==0 && mem[i][2]==0 && mem[i][64]==0&& mem[i][125]==0 && mem[i][127]==0){
                freePageList.add(i);
            }
        }
    }
    
    public static void displayFreePageList(){
        for (int i=0; i<freePageList.size(); i++){
            System.out.println(freePageList.get(i));
        }
    }
    
    //Code and data segmentation
    public static void segmentation(PCB pcbObj){
        pcbObj.spr.db=0;
        if (pcbObj.data_size==0) pcbObj.spr.dl=0;
        else pcbObj.spr.dl=(short)(pcbObj.data_size-1);
        pcbObj.spr.dc=pcbObj.spr.db;
        if (pcbObj.data_size==0) pcbObj.spr.cb=0;
        else pcbObj.spr.cb=(short)(pcbObj.spr.dl+1);
        pcbObj.spr.cl=(short)(pcbObj.spr.cb+pcbObj.code_size-1);
        pcbObj.spr.cc=pcbObj.spr.cb;
        pcbObj.spr.sb=(short)(pcbObj.process_size-8);
        pcbObj.spr.sl=(short)(pcbObj.spr.sb+pcbObj.spr.stackSize-1);
        pcbObj.spr.sc=pcbObj.spr.sb;
        pcbObj.spr.pc=pcbObj.spr.cb;
        pcbObj.spr.ir=0;
        
        pcbObj.spr.updatingSPR();
    }
    
}

class Execution{
    
    public static int[] sprTranslation(PCB pcbObj, short spr){
        int arr[]=new int[2];
        
        arr[0]=pcbObj.pageTable.getFrame(pcbObj,(int)Math.floor(spr/128.0)); 
        if (spr+1<=128) arr[1]=spr; 
        else arr[1]=(spr%128);
        
        return arr;
    }
    
    
    public static PCB ExecuteProcess(PCB pcbObj, int timeSlice, boolean roundRobin, boolean debug){
        try{
        //translating the spr
        //{frameNo, frameIndex} in memory
        
//        int[]cb= new int[2]; int[]cl= new int[2];
//        int[]cc= new int[2]; int[]db= new int[2];
//        int[]dl= new int[2]; int[]dc= new int[2];
//        int[]sb= new int[2]; int[]sc= new int[2];
//        int[]sl= new int[2]; 
//        int[]ir= new int[2];
//        
//        int firstFrame=pcbObj.pageTable.getFrame(pcbObj, 0);
//        
//        db[0]=firstFrame; db[1]=pcbObj.spr.db; //db
//        dl=sprTranslation(pcbObj, pcbObj.spr.dl);//dl
//        dc[0]=db[0]; dc[1]=db[1]; //dc
//        cb=sprTranslation(pcbObj, pcbObj.spr.cb);//cb
//        cl=sprTranslation(pcbObj, pcbObj.spr.cl);//cl
//        cc[0]=cb[0]; cc[1]=cb[1]; //cc
//        sb=sprTranslation(pcbObj, pcbObj.spr.sb);//sb
//        sl=sprTranslation(pcbObj, pcbObj.spr.sl);//sl
//        sc[0]=sb[0]; sc[1]=sb[1]; //sc
        

        System.out.println("Executing "+pcbObj.name);
        int numOfInst= timeSlice/2; //num of instructions to be executed acc to the given time 
        int instCounter=0; //counts num of instructions executed currently
        boolean terminated=false;
        
        //reading from memory
        int[]pc= new int[2];
        pc=sprTranslation(pcbObj, pcbObj.spr.pc);
        String opcode=Integer.toHexString(Byte.toUnsignedInt(Memory.mem[pc[0]][pc[1]])).toUpperCase();

//        System.out.println("PC= "+pcbObj.spr.pc);
//        System.out.println("OPCODE= "+opcode);

        while_loop: while (pcbObj.spr.pc<=pcbObj.spr.cl){
            instCounter++;
            if (instCounter==numOfInst && roundRobin==true){
                terminated=false;
                System.out.println("Process "+pcbObj.name+ " removed");
                break while_loop;
            }
            
            if (instCounter==2 && debug==true){
                System.out.println("Instruction debugged");
                break while_loop;
            }    
                
            pcbObj.spr.ir=(short)Byte.toUnsignedInt(Memory.mem[pc[0]][pc[1]]);
            
            //display methods of GPR & SPR
//            pcbObj.gpr.displayInDecimal();
//            pcbObj.gpr.displayInHexa();
            
            pcbObj.spr.updatingSPR();
//            SPR.displayInDecimal();
//            SPR.displayInHexa();
            pcbObj.spr.updatingFlag();
//            pcbObj.spr.displayFlagRegister();
            
            int R1=0; //R1 and R2 are GPR arrays indexes to identify the registers
            int R2=0;
            int num2; //num2 used in flag and memory instructions
            short num;
            
            //whatever the instruction, method is invoked accordingly and pc is incremented inside that method
            switch (opcode){
                
                //Register-Register Instructions
                case "16": //Copy Register Contents
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.MOV(R1, R2, pcbObj);
                    break;
                	
                case "17": //Add Register Contents
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.ADD(R1, R2, pcbObj);
                    break;
                    
                case "18"://Subtract
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.SUB(R1, R2, pcbObj);
                    break;
                    
                case "19"://Multiply
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.MUL(R1, R2, pcbObj);
                    break;
                
                case "1A"://Division
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.DIV(R1, R2, pcbObj);
                    break;
                	
                case "1B"://Logical AND
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.AND(R1, R2, pcbObj);
                    break;
                	
                case "1C"://Logical OR
                    R1=R_R.defRegisters(pcbObj)[0];
                    R2=R_R.defRegisters(pcbObj)[1];
                    R_R.OR(R1, R2, pcbObj);
                    break;
                	
                //Register-Immediate Instructions
                case "30"://Copy Immediate to register
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.MOVI(R1, num, pcbObj);
                    break;
                
                case "31"://Add
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.ADDI(R1, num, pcbObj);
                    break;
                    
                case "32"://Subtract
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.SUBI(R1, num, pcbObj);
                    break;
                	
                case "33"://Multiply
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.MULI(R1, num, pcbObj);
                    break;
                    
                case "34":   //Divide
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.DIVI(R1, num, pcbObj);
                    break;
                	
                case "35": //Logical AND
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.ANDI(R1, num, pcbObj);
                    break;
                    
                case "36"://Logical OR
                    R1=R_I.defRegisters(pcbObj)[0];
                    num= (short)R_I.defRegisters(pcbObj)[1];
                    R_I.ORI(R1, num, pcbObj);
                    break;
                    
                case "37"://Branch equal to zero
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.BZ(num2, pcbObj);
                    break;
                   
                case "38"://Branch if not zero
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.BNZ(num2, pcbObj);
                    break;
                    
                case "39"://Branch if carry
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.BC(num2, pcbObj);
                    break;
                    
                case "3A"://Branch if sign
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.BS(num2, pcbObj);
                    break;
                    
                case "3B"://Jump
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.JMP(num2, pcbObj);
                    break;
                    
                case "3C"://CALL
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.CALL(num2, pcbObj);
                    break;
                    
                case "3D"://ACT
                    num2=R_I.defRegistersBRANCH(pcbObj);
                    R_I.ACT(num2, pcbObj);
                    break;
                    
                //Memory Instructions
                    
                case "51": //Load Word from memory
                    R1=R_I.defRegisters(pcbObj)[0];
                    num2= R_I.defRegisters(pcbObj)[1];
                    MemoryInst.MOVL(R1, num2, pcbObj);
                    break;
                    
                case "52"://Store Word in memory
                    R1=R_I.defRegisters(pcbObj)[0];
                    num2= R_I.defRegisters(pcbObj)[1];
                    MemoryInst.MOVS(R1, num2, pcbObj);
                    break;
                
                //Single Operand Instructions
                
                case "71"://Shift Left Logical
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.SHL(R1, pcbObj);
                    break;
                    
                case "72"://Shift Right Logical
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.SHR(R1, pcbObj);
                    break;

                case "73"://Rotate Left
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.RTL(R1, pcbObj);
                    break;
                        
                case "74"://Rotate Right
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.RTR(R1, pcbObj);
                    break;

                case "75"://Increment
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.INC(R1, pcbObj);
                    break;
                    
                case "76"://Decrement
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.DEC(R1, pcbObj);
                    break;
                    
                case "77"://Push
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.PUSH(R1, pcbObj);
                    break;
                    
                case "78"://Pop
                    R1=SingleOp.defRegister(pcbObj);
                    SingleOp.POP(R1, pcbObj);
                    break;
                    
                //No Operand Instructions
                
                case "F2":
                    NoOp.NOOP(pcbObj);
                    break;
                    
                case "F3": //end of process
                    terminated=true;
                    Scheduling.terminated.add(pcbObj);
                    break while_loop;
                
                default:
                    System.out.println("ERROR - INVALID OPCODE");
                    terminated=false;
                    System.out.println("Process "+pcbObj.name+ " removed");
                    break while_loop;
                
                
                    
            }
            R1=0;R2=0;num=0;num2=0;
//            System.out.println("PC= "+pcbObj.spr.pc);
            pc=sprTranslation(pcbObj, pcbObj.spr.pc);
            opcode=Integer.toHexString(Byte.toUnsignedInt(Memory.mem[pc[0]][pc[1]])).toUpperCase();
//            System.out.println("OPCODE= "+opcode);
            
        }
        
        
//        display methods of GPR & SPR
//        pcbObj.gpr.displayInDecimal();
//        pcbObj.gpr.displayInHexa();

        pcbObj.spr.updatingSPR();
//        SPR.displayInDecimal();
//        SPR.displayInHexa();
        pcbObj.spr.updatingFlag();
//        pcbObj.spr.displayFlagRegister();
        
        if (terminated){
            System.out.println("Process "+pcbObj.name+ " terminated completely");
        }
        
        pcbObj.exec_time+=numOfInst*2; //every instruction takes 2 clockcycles
        
        }
        catch(ArithmeticException e) {
         System.out.println("Divide by zero");
         System.out.println("Process "+pcbObj.name+ " removed");
         Scheduling.queue1.remove(pcbObj);
         Scheduling.queue2.remove(pcbObj);
         Scheduling.runningQueue.remove(pcbObj);
        }
        
        catch(ArrayIndexOutOfBoundsException e) {
         System.out.println("Stack overflow/underflow");
         System.out.println("Process "+pcbObj.name+ " removed");
         Scheduling.queue1.remove(pcbObj);
         Scheduling.queue2.remove(pcbObj);
         Scheduling.runningQueue.remove(pcbObj);
        }
        
        return pcbObj;
        
    }
}

class GPR{
    public short[] GPR=new short[16];
    
    //Displaying GPR in decimal
    public void displayInDecimal(){
        System.out.println("GPR in decimal: ");
        System.out.print("R0"+"="+GPR[0]);
        for (int i=1; i<GPR.length; i++){
            System.out.print(", R"+(i)+"="+Short.toUnsignedInt(GPR[i]));
        }
        System.out.println("");
    }
    //Displaying GPR in hexa
    public void displayInHexa(){
        System.out.println("GPR in hexa: ");
        System.out.print("R0"+"="+GPR[0]);
        for (int i=1; i<GPR.length; i++){
            System.out.print(", R"+(i)+"="+Integer.toHexString(Short.toUnsignedInt(GPR[i])));
        }
        System.out.println("");
    }
}


class SPR{
    //creating an array of size 15 for the SPR (except the flag register, which
    // is created separately
    short[] SPR=new short[15];
    boolean[] flag= new boolean[16];
    short cb, cl, cc, db, dl, dc, sb, sc, sl, pc, ir; 
    short stackSize;
    public boolean carry, zero, sign, overflow;
    
    static String[] sprNames= {"CB","CL", "CC", "DB", "DL", "DC"
    , "SB", "SC", "SL", "PC", "IR" };
    static String[] flagNames= {"Carry", "Zero", "Sign", "Overflow"};

    public void updatingSPR(){
        SPR[0]=cb;
        SPR[1]=cl;
        SPR[2]=cc;
        SPR[3]=db;
        SPR[4]=dl;
        SPR[5]=dc;
        SPR[6]=sb;
        SPR[7]=sc;
        SPR[8]=sl;
        SPR[9]=pc;
        SPR[10]=ir;
    }
    
    //updating the 4 flag registers -carry, zero, sign, overflow
    public void updatingFlag(){
        flag[0]=carry;
        flag[1]=zero;
        flag[2]=sign;
        flag[3]=overflow;
    }
    
    //Displaying SPR in decimal
    public void displayInDecimal(){
        System.out.println("SPR in decimal: ");
        for (int i=0; i<=9; i++){
            System.out.print(sprNames[i]+"="+SPR[i]+", ");
        }
        System.out.println(sprNames[10]+"="+SPR[10]);
    }
    
  //Displaying SPR in hexa
    public void displayInHexa(){
        System.out.println("SPR in hexa: ");
        for (int i=0; i<=9; i++){
            System.out.print(sprNames[i]+"="+Integer.toHexString(SPR[i])+", ");
        }
        System.out.println(sprNames[10]+"="+Integer.toHexString(SPR[10]));
    }
    
    //Displaying flag register values
    public void displayFlagRegister(){
        System.out.println("Flag Register: ");
        for (int i=0; i<=2; i++){
            System.out.print(flagNames[i]+"="+Boolean.compare(flag[i], false)+", ");
        }
        System.out.println(flagNames[3]+"="+Boolean.compare(flag[3], false));
    }
    
    //flag[0]=carry, flag[1]=zero, flag[2]=sign, flag[3]=overflow

    public void setCarry(short value){
        if (value>=Math.pow(2,15)){ //2^15 to check if the most significant bit is 1
            carry = true;
        }
    }
    
    public void setZero(int R1, PCB pcbObj){
        zero = pcbObj.gpr.GPR[R1]==0;
    }
    
    public void setSign(int R1, PCB pcbObj){
        sign = pcbObj.gpr.GPR[R1]<0;
    }
    
    public void setOverflow(short result){
        if (result>=Math.pow(2,15)){
            overflow = true;
        }
    }
    
    public void resetCarry(){
        carry=false;
    }
    
    public void resetOverflow(){
        overflow=false;
    }   
}

//
class R_R{
    
    public static int[] defRegisters(PCB pcbObj){
        int[] reg= new int[2];
        int arr[]=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+1));
        reg[0]=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+2));
        reg[1]=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        return reg;
    }
    
    public static void MOV(int R1, int R2, PCB p){
        p.gpr.GPR[R1]=p.gpr.GPR[R2];
        p.spr.pc+=3; //pc is incrememnted by 3 to jump on the next instruction
    }
    
    public static void ADD(int R1, int R2, PCB p){
        p.gpr.GPR[R1]+=p.gpr.GPR[R2];
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
    public static void SUB(int R1, int R2, PCB p){
        p.gpr.GPR[R1]-=p.gpr.GPR[R2];
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
    public static void MUL(int R1, int R2, PCB p){
        p.gpr.GPR[R1]*=p.gpr.GPR[R2];
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
    public static void DIV(int R1, int R2, PCB p){
        p.gpr.GPR[R1]/=p.gpr.GPR[R2];
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
    public static void AND(int R1, int R2, PCB p){
        p.gpr.GPR[R1]=(short)(p.gpr.GPR[R1] & p.gpr.GPR[R2]);
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
    public static void OR(int R1, int R2, PCB p){
        p.gpr.GPR[R1]=(short)(p.gpr.GPR[R1] | p.gpr.GPR[R2]);
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=3;
    }
    
}
//
class R_I{
    
    public static int[] defRegisters(PCB pcbObj){
        int[] reg= new int[2];
        
        int arr[]=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+1));
        reg[0]=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+2));
        short num=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+3));
        num+=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        reg[1]=num;
        return reg;
    }
    
    public static int defRegistersBRANCH(PCB pcbObj){
        int num;
        int arr[]=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+1));
        num=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+2));
        num+=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+3));
        num+=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        return num;
        
    }
    
    public static void MOVI(int R1, short num, PCB p){
        p.gpr.GPR[R1]=num;
        p.spr.pc+=4;//pc is incrememnted by 4 to jump on the next instruction   
    }
    
    public static void ADDI(int R1, short num, PCB p){
        p.gpr.GPR[R1]+=num;
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void SUBI(int R1, short num, PCB p){
         p.gpr.GPR[R1]-=num;
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void MULI(int R1, short num, PCB p){
        p.gpr.GPR[R1]*=num;
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void DIVI(int R1, short num, PCB p){
        p.gpr.GPR[R1]/=num;
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void ANDI(int R1, short num, PCB p){
        p.gpr.GPR[R1]=(short) (p.gpr.GPR[R1] & num);
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void ORI(int R1, short num, PCB p){
        p.gpr.GPR[R1]=(short) (p.gpr.GPR[R1] | num);
        p.spr.setOverflow(p.gpr.GPR[R1]);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=4;
    }
    
    public static void BZ(int offset, PCB p){
        if (p.spr.zero){
            p.spr.pc=(short)(p.spr.cb+offset);
        }
        else{
            p.spr.pc+=4;
        }
        
    }
    
    public static void BNZ(int offset, PCB p){
        if (!p.spr.zero){
            p.spr.pc=(short)(p.spr.cb+offset);
            
        }
        else{
            p.spr.pc+=4;
        }
    }
    
    public static void BC(int offset, PCB p){
        if (p.spr.carry){
            p.spr.pc=(short)(p.spr.cb+offset);
        }
        else{
            p.spr.pc+=4;
        }
    }
    
    public static void BS(int offset, PCB p){
        if (p.spr.sign){
            p.spr.pc=(short)(p.spr.cb+offset);
        }
        else{
            p.spr.pc+=4;
        }
    }
    
    public static void JMP(int offset, PCB p){
        p.spr.pc=(short)(p.spr.cb+offset);
    }
    
    public static void CALL(int offset, PCB p){
        //assuming sc points to the next free memory location after the top of stack 
        int arr[];
        if ((byte)p.spr.pc>255){
            arr=Execution.sprTranslation(p, (short)(p.spr.sc));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.spr.pc-255);
            arr=Execution.sprTranslation(p, (short)(p.spr.sc+1));
            Memory.mem[arr[0]][arr[1]]=(byte)255;
            p.spr.sc+=2;
        }
        else{
            arr=Execution.sprTranslation(p, (short)(p.spr.sc));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.spr.pc);
            p.spr.sc++;
        }
        
        p.spr.pc=(short)(p.spr.cb+offset-1);
    }
    
    public static void ACT(int offset, PCB p){
        p.spr.pc+=4;
    }
    
}

class MemoryInst{
    
    public static int[] defRegisters(PCB pcbObj){
        int[] reg= new int[2];
        
        int arr[]=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+1));
        reg[0]=Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+2));
        short num=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        arr=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+3));
        num+=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        reg[1]=num;
        return reg;
    }
    
    public static void MOVL(int R1, int offset, PCB p){
        //note: location is the index value of the mem array
        //DB is SPR[3] and new location=DB+Offset
        int arr[]=Execution.sprTranslation(p, (short)(p.spr.db+offset));
        p.gpr.GPR[R1]=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        p.spr.pc+=4;
    }
    
    public static void MOVS(int R1, int offset, PCB p){
        //note: location is the index value of the mem array
        //DB is SPR[3] and new location=DB+Offset
        
        //we are breaking down the short value inside the register bec data is stored in bytes in the data section
        //of the main memory
        int arr[];
        if ((byte)p.gpr.GPR[R1]>255){
            arr=Execution.sprTranslation(p, (short)(p.spr.SPR[3]+offset));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.gpr.GPR[R1]-255);
            arr=Execution.sprTranslation(p, (short)(p.spr.SPR[3]+offset+1));
            Memory.mem[arr[0]][arr[1]]=(byte)255;
        }
        else{
            arr=Execution.sprTranslation(p, (short)(p.spr.SPR[3]+offset));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.gpr.GPR[R1]);
        }
        p.spr.pc+=4;
        
    }
}

class SingleOp{
    
    public static int defRegister(PCB pcbObj){
        int arr[]=Execution.sprTranslation(pcbObj, (short)(pcbObj.spr.pc+1));
        return Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
    }
    
    public static void SHL(int R1, PCB p){
        p.spr.setCarry(p.gpr.GPR[R1]);
        p.gpr.GPR[R1]=(short)(p.gpr.GPR[R1]<<1);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
    }
    
    public static void SHR(int R1,PCB p){
        p.spr.setCarry(p.gpr.GPR[R1]);
        p.gpr.GPR[R1]=(short)(p.gpr.GPR[R1]>>1);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
    }
    
    public static void RTL(int R1, PCB p){
        p.spr.setCarry(p.gpr.GPR[R1]);
        p.gpr.GPR[R1] = (short)Integer.rotateLeft(p.gpr.GPR[R1], 1);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
    }
    
    public static void RTR(int R1, PCB p){
        p.spr.setCarry(p.gpr.GPR[R1]);
        p.gpr.GPR[R1] = (short)Integer.rotateRight(p.gpr.GPR[R1], 1);
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
        
    }
    
    public static void INC(int R1, PCB p){
        p.gpr.GPR[R1]++;
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
    }
    
    public static void DEC(int R1, PCB p){
        p.gpr.GPR[R1]--;
        p.spr.setZero(R1, p);
        p.spr.setSign(R1, p);
        p.spr.pc+=2;
    }
    
    public static void PUSH(int R1, PCB p){
        //assuming sc points to the next free memory location after the top of stack 
        int arr[];
        if ((byte)p.gpr.GPR[R1]>255){
            arr=Execution.sprTranslation(p, (short)(p.spr.sc));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.gpr.GPR[R1]-255);
            arr=Execution.sprTranslation(p, (short)(p.spr.sc+1));
            Memory.mem[arr[0]][arr[1]]=(byte)255;
            p.spr.sc+=2;
        }
        else{
            arr=Execution.sprTranslation(p, (short)(p.spr.sc));
            Memory.mem[arr[0]][arr[1]]=(byte)(p.gpr.GPR[R1]);
            p.spr.sc++;
        }
        p.spr.pc+=2;
    }
    
    public static void POP(int R1, PCB p){
        int arr[]=Execution.sprTranslation(p, (short)(p.spr.sc-1));
        p.gpr.GPR[R1]=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        Memory.mem[arr[0]][arr[1]]=0;
        p.spr.pc+=2;
    }
    
}

class NoOp{
    public static void RETURN(PCB p){
        int arr[]=Execution.sprTranslation(p, (short)(p.spr.sc-1));
        p.spr.pc=(short)Byte.toUnsignedInt(Memory.mem[arr[0]][arr[1]]);
        Memory.mem[arr[0]][arr[1]]=0;
        p.spr.pc+=1;
    }
    
    public static void NOOP(PCB p){
        p.spr.pc+=1;
        
    }
    
    public static void END(PCB p){
        p.spr.ir=243;
    }
}


