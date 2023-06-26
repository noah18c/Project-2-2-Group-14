package DigitalAssistant.skillLogic.BERT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.bouncycastle.its.ITSPublicEncryptionKey.symmAlgorithm;

import DigitalAssistant.skillLogic.Skill;
import DigitalAssistant.skillLogic.SkillEditor;
import DigitalAssistant.skillLogic.CFG.CFGParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BERTparser {
    private List<Skill> skills;
    private boolean isInputSkill = false;
    private Skill skillToCheck;
    private String originalString = "";
    private HashMap<String, ArrayList<String>> foundPlaceholders;
    private String foundSkillStr;
    

    public BERTparser(List<Skill> skills){
        this.skills = skills;
    }

    public void parse(String stringToParse){
        originalString = stringToParse;

        writeInput();
        runClassifier();
        getOutput();

        HashMap<String, ArrayList<String>> extractedPlaceholders = findPlaceholders();
        if(extractedPlaceholders==null){
            System.out.println("Failed to extract placeholders, either caused by input-type sentence or incorrect prediction");
        }else{
            foundPlaceholders = extractedPlaceholders;
        }
    }

    public String runClassifier(){
        //Something something exectute pyj4 magic
 
        try {
            // Command to execute the Python script

            String output = "";
            String directoryPath = System.getProperty("user.dir");
            String fileName = "classifier_script.py";
            String filePath = directoryPath + "/" + "src/main/java/DigitalAssistant/skillLogic/BERT/" + fileName;

            String pythonScript = "src/main/java/DigitalAssistant/skillLogic/BERT/classifier_script.py";                    

            // Create the ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder("C:/Users/domin/AppData/Local/Programs/Python/Python310/python.exe", pythonScript);
            //ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);
            
            //ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript);

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output from the Python script
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Process the output line from the Python script
                //System.out.println(line);
                output += line + " \n";

            }
            
            // Wait for the Python script to complete
            int exitCode = process.waitFor();
            //System.out.println("Python script exited with code: " + exitCode);
            return output;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "Error";

    }


    

    //This method searches the original sentence for placeholders relating 
    public HashMap<String, ArrayList<String>> findPlaceholders(){
        ArrayList<String> words = CFGParser.processStringCFG(originalString);
        HashMap<String,ArrayList<String>> skillPlaceholders = skillToCheck.getPlaceholders();
        
        isInputSkill = false;
        setIsInputSkill(skillToCheck);

        Set<String> keySet = skillPlaceholders.keySet();//Making the hashMap iterable

        HashMap<String,ArrayList<String>> foundPlaceholders = new HashMap<String,ArrayList<String>>();
        for(String key : keySet){
            foundPlaceholders.put(key, new ArrayList<String>());
        }

        ArrayList<String> keyList = new ArrayList<>(keySet);
        for(String word : words){
            for(String key : keySet){
                for(String possiblePlaceholder : skillPlaceholders.get(key)){
                    if(word.equalsIgnoreCase(possiblePlaceholder)){
                        foundPlaceholders.get(key).add(word);
                    }
                }
            }
        }

        for(String key : keySet){
            if(foundPlaceholders.get(key).size() == 0){
                return null;
            }
        }
        return foundPlaceholders;

    }

    public void setIsInputSkill(Skill skill){
         Set<String> keySet = skill.getPlaceholders().keySet();
        for(String key: keySet){
            if(key.equalsIgnoreCase("@INPUT")){isInputSkill = true;}
        }
    }

    public void setOriginalString(String string){originalString = string;}

    public void setSkillToCheck(String skillName){
        switch(skillName){
            case "DistanceFinder":
                for(Skill skill : skills){
                    if(skill.getName().equals("DistanceFinder")){skillToCheck = skill;}
                }
                break;
            case "MariekeSkill":
            for(Skill skill : skills){
                    if(skill.getName().equals("MariekeSkill")){skillToCheck = skill;}
                        }
                break;
            case "Exams":
                for(Skill skill : skills){
                        if(skill.getName().equals("Exams")){skillToCheck = skill;}
                    }
                break;
            case "Subjects":
                    for(Skill skill : skills){
                        if(skill.getName().equals("Subjects")){skillToCheck = skill;}
                    }
                break;
            case "Timer":
                for(Skill skill : skills){
                        if(skill.getName().equals("Timer")){skillToCheck = skill;}
                    }
            case "CFGTest": 
                for(Skill skill : skills){
                    if(skill.getName().equals("CFGTest")){skillToCheck = skill;}
                }
                break;
            case "Translator":
                for(Skill skill : skills){
                    if(skill.getName().equals("Translator")){skillToCheck = skill;}
                }
                break;
            case "OpeningTimes":
                for(Skill skill : skills){
                    if(skill.getName().equals("OpeningTimes")){skillToCheck = skill;}
                }
                break;
            case "Fixing":
                for(Skill skill : skills){
                    if(skill.getName().equals("Fixing")){skillToCheck = skill;}
                }
                break;
        }

    }

    public void writeInput(){
        String directoryPath = System.getProperty("user.dir");
        String fileName = "BERT_Input.txt";
        String filePath = directoryPath + "/" + "src/main/java/DigitalAssistant/skillLogic/BERT/" + fileName;

        try {
            FileWriter file = new FileWriter(filePath, false);
            PrintWriter writer = new PrintWriter(file);

            writer.print(originalString);
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getOutput(){
        String directoryPath = System.getProperty("user.dir");
        String fileName = "BERT_Output.txt";
        String filePath = directoryPath + "/" + "src/main/java/DigitalAssistant/skillLogic/BERT/" + fileName;

        String skillFound = "";
         try (Scanner scanner = new Scanner(new File(filePath))) {
            skillFound = scanner.nextLine().trim();

         }catch(FileNotFoundException e){
            System.out.println("BERTparser file Error");
            System.err.println("Error: File not found");
         }
         System.out.println("TEST: SkillFound: " + skillFound);
         setSkillToCheck(skillFound);
         foundSkillStr = skillFound;

    }

    public String getFoundSkillName(){
        return foundSkillStr;
    }
    public void printFoundPlaceholders(){
         Set<String> keySet = foundPlaceholders.keySet();//Making the hashMap iterable
         for(String key : keySet){
            System.out.printf("-------\nKey: %s\nPlaceholders: %s\n------\n", key, foundPlaceholders.get(key).toString());
         }
    }


    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();
        
        BERTparser parser = new BERTparser(skillEditor.getSkills());

        parser.parse("How do I get from Maastricht to Heerlen at 9?");


        System.out.println("Found skill string: " + parser.getFoundSkillName());
        parser.printFoundPlaceholders();

        // System.out.println(parser.runClassifier());
        // parser.writeInput();
        //parser.getOutput();

        // parser.setSkillToCheck("Opening");
        // parser.setOriginalString("What are the opening times of the university?");
        // HashMap<String,ArrayList<String>> placeholders = parser.findPlaceholders();

        //  Set<String> keySet = placeholders.keySet();//Making the hashMap iterable
        //  for(String key : keySet){
        //     System.out.printf("Key: %s\nPlaceholders: ", key);
        //     for(String place : placeholders.get(key)){
        //         System.out.printf("%s, ", place);
        //     }
        //     System.out.printf("\n-----------------\n", key);
        //  }
    }
}
