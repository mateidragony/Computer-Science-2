import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {


    public static String intToBits(int in){

        String bits = Integer.toBinaryString(in);
        bits = String.format("%15s", bits).replaceAll(" ", "0");

        return bits;
    }

    public static void compareTo(ArrayList<String> binaryInst) throws FileNotFoundException{
        File compare = new File("Nand2Tetris/src/main/java/instructionsCompare.txt");
        Scanner s = new Scanner(compare);
        ArrayList<String> compareTo = new ArrayList<>();

        while(s.hasNextLine()){
            String inst = s.nextLine();
            compareTo.add(inst.replaceAll(" ",""));
        }

        boolean comparisonSuccessful = true;
        for(int i=0; i<compareTo.size(); i++){
            if(!binaryInst.get(i).equals(compareTo.get(i))){
                comparisonSuccessful=false;
                System.out.println("Comparison broke at line "+i);
                break;
            }
        }

        if(comparisonSuccessful && binaryInst.size()==compareTo.size())
            System.out.println("Comparison Successful");
        else
            System.out.println("Comparison Unsuccessful");

    }



    public static void main(String[] args) throws FileNotFoundException {

        File instructions = new File("Nand2Tetris/src/main/java/instructions.txt");
        Scanner s = new Scanner(instructions);
        ArrayList<String> binaryInst = new ArrayList<>();

        //This array List will contain each variable/label
        //followed immediately by its value
        //ex. if the only label is LOOP, the
        //arraylist will be {"LOOP" , 3}
        //if the code contains i,sum, and LOOP
        //the arraylist would be:
        //{"i",16,"sum",17,"LOOP",3}
        ArrayList<String> vars = new ArrayList<>();

        //Add all the predefined labels to vars
        vars.add("SP"); vars.add(""+0);
        vars.add("LCL"); vars.add(""+1);
        vars.add("ARG"); vars.add(""+2);
        vars.add("THIS"); vars.add(""+3);
        vars.add("THAT"); vars.add(""+4);
        vars.add("SCREEN"); vars.add(""+16384);
        vars.add("KBD"); vars.add(""+24576);
        for(int i=0; i<16; i++){
            vars.add("R"+i); vars.add(""+i);
        }

        //--------------------------------
        //First go through and find labels
        //--------------------------------
        int count = 0;
        while(s.hasNextLine()){

            String inst = s.nextLine();
            inst = inst.trim();

            //if declaring a new label add it to vars
            //with its corresponding instruction number
            if(!inst.isEmpty() && inst.charAt(0)=='(') {
                vars.add(inst.substring(inst.indexOf('(') + 1, inst.indexOf(')')));
                vars.add(""+count);
            }

            if(!inst.isEmpty() && inst.charAt(0)!='/' && inst.charAt(0)!='(')
                count++;
        }

        int fakeSize = vars.size();

        s.close();
        s = new Scanner(instructions);


        //--------------------------------
        //Then go through and find vars
        //--------------------------------
        while(s.hasNextLine()){

            String inst = s.nextLine();

            //if A-instruction that initializes a new variable
            //That isn't already a variable/label, add it to
            //var list with its corresponding memory address
            if(!inst.isEmpty() && inst.charAt(0)=='@' && !vars.contains(inst.substring(1))
                && Character.isLetter(inst.charAt(1))){
                vars.add(inst.substring(1));
                vars.add(""+(16+(vars.size()-fakeSize)/2));
            }
        }

        s.close();
        s = new Scanner(instructions);


        //--------------------------------
        //Now go through the instructions
        //--------------------------------
        while(s.hasNextLine()){

            String word = "";
            String inst = s.nextLine();
            inst = inst.replaceAll(" ","");
            inst = inst.trim();

            if(inst.contains("/"))
                inst = inst.substring(0,inst.indexOf('/'));

            //if the instruction wasn't a comment
            if(!inst.isEmpty()){

                //If A instruction
                if(inst.charAt(0) == '@') {
                    word += "0";

                    if(Character.isLetter(inst.charAt(1))) {
                        int varsIndex = (vars.indexOf(inst.substring(1).trim()))+1;
                        word += intToBits(Integer.parseInt(vars.get(varsIndex)));
                    }
                    else {
                        word += intToBits(Integer.parseInt(inst.substring(1)));
                    }
                }


                //Else if C Instruction
                else if(inst.charAt(0) != '(' && inst.charAt(0) != '/'){
                    word+="111";

                    //The comp spec

                    String comp;
                    if(!inst.contains("=") && !inst.contains(";"))
                        comp = inst;
                    else if(inst.contains("=") && !inst.contains(";"))
                        comp = inst.substring(inst.indexOf('=')+1);
                    else if(!inst.contains("=") && inst.contains(";"))
                        comp = inst.substring(0,inst.indexOf(';'));
                    else
                        comp = inst.substring(inst.indexOf('=')+1,inst.indexOf(';'));


                    switch (comp) {
                        case "0" -> word += "0101010";
                        case "1" -> word += "0111111";
                        case "-1" -> word += "0111010";
                        case "D" -> word += "0001100";
                        case "A" -> word += "0110000";
                        case "!D" -> word += "0001101";
                        case "!A" -> word += "0110001";
                        case "-D" -> word += "0001111";
                        case "-A" -> word += "0110011";
                        case "D+1" -> word += "0011111";
                        case "A+1" -> word += "0110111";
                        case "D-1" -> word += "0001110";
                        case "A-1" -> word += "0110010";
                        case "D+A" -> word += "0000010";
                        case "A+D" -> word += "0000010";
                        case "D-A" -> word += "0010011";
                        case "A-D" -> word += "0000111";
                        case "D&A" -> word += "0000000";
                        case "A&D" -> word += "0000000";
                        case "D|A" -> word += "0010101";
                        case "A|D" -> word += "0010101";
                        case "M" -> word += "1110000";
                        case "!M" -> word += "1110001";
                        case "-M" -> word += "1110011";
                        case "M+1" -> word += "1110111";
                        case "M-1" -> word += "1110010";
                        case "D+M" -> word += "1000010";
                        case "M+D" -> word += "1000010";
                        case "D-M" -> word += "1010011";
                        case "M-D" -> word += "1000111";
                        case "D&M" -> word += "1000000";
                        case "M&D" -> word += "1000000";
                        case "D|M" -> word += "1010101";
                        case "M|D" -> word += "1010101";
                        default -> System.out.print("ERROR!");
                    }

                    //The Dest Spec
                    if(!inst.contains("="))
                        word+="000";
                    else{
                        switch (inst.substring(0, inst.indexOf('='))) {
                            case "M" -> word += "001";
                            case "D" -> word += "010";
                            case "MD" -> word += "011";
                            case "A" -> word += "100";
                            case "AM" -> word += "101";
                            case "AD" -> word += "110";
                            case "AMD" -> word += "111";
                        }
                    }

                    //The Jump Spec
                    if(!inst.contains(";"))
                        word+="000";
                    else{
                        switch (inst.substring(inst.indexOf(';') + 1)) {
                            case "JGT" -> word += "001";
                            case "JEQ" -> word += "010";
                            case "JGE" -> word += "011";
                            case "JLT" -> word += "100";
                            case "JNE" -> word += "101";
                            case "JLE" -> word += "110";
                            case "JMP" -> word += "111";
                        }
                    }
                }
            }

            //If the word isn't a comment or a label
            if(!word.isEmpty()) {
                //System.out.print(inst.trim() + " : ");
                System.out.print(word);
                System.out.println();
                binaryInst.add(word);
            }
        }

        System.out.println();
        compareTo(binaryInst);

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("Nand2Tetris/myInstruction.hack"));

            for(String str: binaryInst) {
                writer.write(str);
                writer.write("\n");
            }

            writer.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }


//        System.out.println();
//        for(int i=0; i<vars.size()-1; i+=2){
//            System.out.println(vars.get(i)+" = "+vars.get(i+1));
//        }


    }
}