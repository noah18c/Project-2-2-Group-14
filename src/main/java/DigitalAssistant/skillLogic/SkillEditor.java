package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import DigitalAssistant.Utilities.Rule;
import DigitalAssistant.Utilities.SkillsUserInput;
import DigitalAssistant.Utilities.SlotValuePair;
import DigitalAssistant.skillLogic.CFG.CFG;
import DigitalAssistant.skillLogic.CFG.CFGParser;

public class SkillEditor {
    private List<Skill> skills;
    public static ArrayList<Event> events = new ArrayList<>();
    
    public SkillEditor() {
        skills = new ArrayList<>();
        loadSkills();
        loadGrammar();
    }

    public void loadGrammar(){
        CFG cfg = new CFG(skills);
        ArrayList<CFG.Rule> arrayList = cfg.grammar;
        try {
            FileWriter fileWriter = new FileWriter("grammar.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (CFG.Rule element : arrayList) {
                printWriter.println(element.ruleName + " " + element.terminal + " " + element.nonterminal);
            }

            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
        this.saveSkills();
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();
        String input = "How is the weather in NewYork tomorrow?";
        System.out.println(input);
        System.out.println(skillEditor.search(input));
    }

    public String search(String input){

        CFGParser parser = new CFGParser(skills);
        parser.parse(input);

        CFG cfg = new CFG(skills);
        cfg.start(input);


        //A Parser
        if(getSkillWithName(parser.skillName) != null){
            String str = getSkillWithName(parser.skillName).start(parser.placeholderValues);
            if(!str.equals("I couldn't find the response for the given value(s), please be more precise.")){
                return str;
            }
        }

        //B CFG 
        if(getSkillWithName(cfg.skillName) != null){
            String str = getSkillWithName(cfg.skillName).start(cfg.parsedValues);

            if(!str.equals("I couldn't find the response for the given value(s), please be more precise.")){
                return str;
            }
        }
        
        //C Match
        Match match = new Match(input, getSkills());
        if(match.searchSkill() != null){
            if(!(match.searchSkill().match(input).equalsIgnoreCase("I couldn't find the response for the given value(s), please be more precise."))){
                return match.searchSkill().match(input);
            }
        }

        for (int i = 0; i < skills.size(); i++) {
            if(!(skills.get(i).match(input).equalsIgnoreCase("I couldn't find the response for the given value(s), please be more precise."))){
                return skills.get(i).match(input);
            }
        }


        return "No Skill Found For Given Input!";
    }

    public Skill getSkillWithName(String skillName){
        for (int i = 0; i < skills.size(); i++) {
            if(skills.get(i).getName().equalsIgnoreCase(skillName)){
                return skills.get(i);
            }
        }
        return null;
    }

    /*
     *This method goes through the skills from first added to last added skill and deletes a skill object if it's name matches another skill further down the list.
     */
    public void deleteDuplicateSkills(){
        ArrayList<Skill> skillsToRemove = new ArrayList<>();
      
        for(int i = 0; i < skills.size(); i++){
            for(int j = i; j < skills.size(); j++){
                if(i != j && skills.get(i).getName().equals(skills.get(j).getName())){
                    skillsToRemove.add(skills.get(i));
                    System.out.println("Removing " + skills.get(i).getName());
                }
            }
        }
        skills.removeAll(skillsToRemove);
    }
    
    /**
     * This method takes all defined skill objects and overwrites the skills.txt file with all newly saved skills by printing each object in the correct format.
     */
    public void saveSkills() {
        deleteDuplicateSkills();//Gets rid of duplicate skills before saving new file
        for(Skill skill : skills){
            System.out.println(skill.getName());
        }
        String directoryPath = System.getProperty("user.dir");
        String fileName = "skills.txt";
        String filePath = directoryPath + "/" + fileName;

        try {
            FileWriter file = new FileWriter(filePath, false);
            PrintWriter writer = new PrintWriter(file);
            //Prints to file the skill object in loadSkill() readable format 
            for(Skill skill : skills){
                if(skills.indexOf(skill) == skills.size()-1){
                    writer.print(skill.toFileFormatString());
                }else{
                    writer.println(skill.toFileFormatString());
                }
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSkills() {//Reads skills.txt and makes skill objects from each skill already contained in the file
        this.skills = new ArrayList<>();//Resets list for use outside of constructor
        
        String directoryPath = System.getProperty("user.dir");
        String fileName = "skills.txt";
        String filePath = directoryPath + "/" + fileName;

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim();

                if (line.equals("-----")) {
                    // Start of new skill
                    String name = scanner.nextLine().trim();
                    name = name.replace("Name ", ""); // Name of the Skill

                    String prototype = scanner.nextLine().trim();
                    prototype = prototype.replace("Question ", ""); // Prototype Sentence


                    Skill skill = new Skill(name, prototype);
                    ArrayList<String> placeholders = new ArrayList<>(); //We will assign placeholder keys to the arraylist in order to use later with creation of Actions.
                    
                    // Read slots (placeholders key value)
                    while (true) {
                        String slotLine = scanner.nextLine().trim();

                        if (slotLine.startsWith("--")) { //Represents the end of slots and the beggining of actions
                            // Done reading slots, start reading actions
                            break;
                        }

                        slotLine = slotLine.replace("Slot ", ""); // Deletes the word "Slot " from the line 
                        String[] slot = slotLine.split(" "); //Splits every word then assgins all of the wors to the array

                        String slotKey = slot[0]; // the key of the line for example it can be <DAY>
                        placeholders.add(slotKey);// we will use these key values later

                        String[] slotValuesArray = Arrays.copyOfRange(slot, 1, slot.length); // Removed the key <DAY> ,and assign rest of the line to another array
                        ArrayList<String> slotValues = new ArrayList<>();

                        for (int i = 0; i < slotValuesArray.length; i++) {
                            slotValues.add(slotValuesArray[i]); // Convert String Array to ArrayList
                        }

                        skill.setPlaceholderValue(slotKey, slotValues); // For each slot we created a key and the arraylist of values for corresponding key, then assigned the key value pair to the hashmap of skill.
                    }                    
                

                    // Read actions
                    while (scanner.hasNextLine()) {

                        String actionLine = scanner.nextLine().trim();
                        actionLine = actionLine.replace("Action ", ""); // Delete the "Action " from the line

                        if (actionLine.equals("+++++") || actionLine.equals(null)) { // end of the skill.
                            // End of skill
                            skills.add(skill);
                            break;
                        }

                        String[] actionLineString = actionLine.split(" "); // Split every word of line and add every word to array

                        /*  
                         * Example Line: "Action <DAY> Monday <TIME>  9 |Print| We start the week with math"
                         * Already deleted the "Action " part so we have: "<DAY> Monday <TIME>  9 |Print| We start the week with math".
                         * The HashMap actionValues contains the keu value for this example it contains <DAY> MONDAY
                         *                                                                              <TIME> 9
                         * actionType is the "|Print|" which means we will use print action for the "MONDAY" and "9"
                         * actionInput is the line  "We start the week with math" ,so this is the sentence we will print.
                         */
                        HashMap<String,ArrayList<String>> actionValues = new HashMap<>(); 
                        String actionType = new String();
                        String actionInput = new String();
                        

                        for (int i = 0; i < actionLineString.length; i++) {
                            if(actionLineString[i].startsWith("|") && actionLineString[i].endsWith("|")){
                                actionType = actionLineString[i];
                                for (int j = i+1; j < actionLineString.length; j++) {
                                    if(j == actionLineString.length-1){
                                        actionInput += actionLineString[j];
                                    }
                                    else{
                                        actionInput += actionLineString[j] + " ";
                                    }
                                    
                                }
                            }   
                        }

                        actionLine = actionLine.replace(actionType, "");  //delete the actionType part to reach actionValues
                        actionLine = actionLine.replace(actionInput, ""); //delete the actionInput part to reach actionValues
                        actionLineString = actionLine.split(" "); // Split every word of line and add every word to array

                        for (int j = 0; j < placeholders.size(); j++) { 
                            ArrayList<String> valuesForPlaceholder = new ArrayList<>();
                            
                            for (int i = 0; i < actionLineString.length; i++) {
                                if(actionLineString[i].equals(placeholders.get(j))){
                                    valuesForPlaceholder.add(actionLineString[i+1]);
                                }
                            }

                            actionValues.put(placeholders.get(j), valuesForPlaceholder);
                        }


                        //System.out.println(actionType + " " + actionInput);
                        // System.out.println(actionValues);
                        // System.out.println(" -------- ");

                        Action action = new Action(actionType, actionInput, actionValues);
                        skill.addAction(action);
                    }
                }
                
            }
        } catch (FileNotFoundException e) {
            System.out.println("LoadEvents");
            System.err.println("Error: File not found");
        }
    }

    public static void loadEvents(){
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\DigitalAssistant\\skillLogic\\Events.txt"))) {
            String currentLine = "";
            int i = 0;
            while(currentLine != null){//While the file has not ended
                //  System.out.println(currentLine);
                
                i++;
                //System.out.println("Event " + i);

                currentLine = reader.readLine();
                if(currentLine == null){break;}
                String eventName = currentLine;
                String parametersStr = reader.readLine();//string for scanner

                ArrayList<String> parameters = new ArrayList<>();//list to append to
                Scanner paraScanner = new Scanner(parametersStr);
                paraScanner.useDelimiter("-");//seperates the parametersStr by the '-' character


                while(paraScanner.hasNext()){
                    parameters.add(paraScanner.next());//adds each parameter to the list
                }
                paraScanner.close();

                Event temp = new Event(eventName, parameters);//creates event from read
                // System.out.println("Adding " + eventName);
                SkillEditor.events.add(temp);        
                
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading skills from file: " + e.getMessage());
        }
    }

    /**
     * Method to extract info from user input when asking him to define an unknown skill!
     * Needed ONLY when the assistant recognizes unfamiliar skill -> gets stuck
     * if skill (prototype or placeholder) unknown then do questionnaire & store answers; if info complete then declareSKill(input)
     * 
     * input: the stored user answers
     * method creates a skill object which is added to skills list and can be written to the skills.txt file
     * @param ArrayList of strings
     */

    public void declareSkill(ArrayList<String> input){
        String skillName = "";
        String prototype = "";
        String phName = "";
        ArrayList <String> phList = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>(); // placeholder values

        // extract skill name, prototype sentence, placeholder(s) and its value(s) if specified
        skillName = input.get(0);
        prototype = input.get(1);
        // create skill object with that stuff
        Skill skill = new Skill(skillName, prototype);
        addSkill(skill);

        Scanner in = new Scanner(prototype);
        in.useDelimiter(" ");
        int i = 0;

        // make the list of slot / placeholder keys (i.e. <Time>, <Day>, <City>, etc.)
        while(in.hasNext()){
            temp.add(in.next());
            char c = '<';
            if (temp.get(i).charAt(0) == c){
                phList.add(temp.get(i));
            }
            i++;
        }
        in.close();
        
        int d = 0;
        int max = phList.size();
        
        // put together the pairs of placeholder keys and their corresponding values
        while (d<max){
            String tempStr = input.get(d+2);
            Scanner scan = new Scanner(tempStr);
            scan.useDelimiter(",");
            phName = phList.get(d);
            while (scan.hasNext()){
                values.add(scan.next());
            }
            skill.setPlaceholderValue(phName, values);   // this is a hashmap of placeholder keys (Strings) and their corresponding values (ArrayList)
            d++;
            scan.close();
        }
       
        saveSkills();         
        loadSkills();               
    }

    /**
     * Used when user wants to add new values to existing placeholders of defined skills
     * @param prototype
     * @param input
     * user prompt:
     * Add values to this skill ¡skillName¿ / this prototype ¡prototype¿
     * or when bot gets stuck:
     * Would you like to define a new skill / prototype or add new values to an existing skill / prototype?
     * A: add values to existing prototype
     * Specify the prototype in the following format: ¡prototype¿
     * A: ¡prototype¿ // use as input!
     * Specify new values you would like to add to this prototype // use as input!
     */
    public void addValue(String prototype, String input){
        ArrayList<String> values = new ArrayList<String>();
        Scanner scan = new Scanner(input);
        scan.useDelimiter(",");
        while (scan.hasNext()){
            values.add(scan.next());
        }
        scan.close();
        // save values
        // updateSkills();
    }

    /**Converts a skillUserInput object from the GUI to a Skill object.
     * @param skillUserInput
     * @return Skill object
     */
    public Skill convertToSkill(SkillsUserInput skillUserInput){
        Skill newSkill = new Skill(skillUserInput.getSkillName(), skillUserInput.getProtoSentence());

        
        //Converting SlotValuePairs->placeholders

        ArrayList<SlotValuePair> slotValuePairs = new ArrayList<SlotValuePair>(skillUserInput.getSlotValuePairs());
        for(SlotValuePair pair : slotValuePairs){
            if(newSkill.getPlaceholders().containsKey(pair.getSlot())){//Check to see if the slot (ex: <CITY>) has been added yet
                newSkill.getPlaceholders().get(pair.getSlot()).add(pair.getValue());//Adds to existing arraylist
            }else{//creates a new hashmap key value pair and adds the new value to the new ArrayList
                newSkill.getPlaceholders().put(pair.getSlot(), new ArrayList<String>());
                newSkill.getPlaceholders().get(pair.getSlot()).add(pair.getValue());
            }
        }

        //Converting Rules->Actions
        ArrayList<Rule> rules = new ArrayList<Rule>(skillUserInput.getRules());
        for(Rule rule : rules){
            
            ArrayList<SlotValuePair> slotValuePairsTwo = new ArrayList<SlotValuePair>(rule.getSlotValuePairs());
            HashMap<String, ArrayList<String>> actionValues = new HashMap<String, ArrayList<String>>();

            for(SlotValuePair pair : slotValuePairsTwo){
                if(actionValues.containsKey(pair.getSlot())){
                    actionValues.get(pair.getSlot()).add(pair.getValue());
                }else{
                    actionValues.put(pair.getSlot(), new ArrayList<>());
                    actionValues.get(pair.getSlot()).add(pair.getValue());
                }
            }

            Action newAction = new Action(rule.getAction(), rule.getOutput(), actionValues);
            newSkill.addAction(newAction);
        }
        return newSkill;
    }

    public static void addEventToFile(String eventName, ArrayList<String> parameters){
        try {
            FileWriter file = new FileWriter("src\\main\\java\\DigitalAssistant\\skillLogic\\Events.txt", true);
            PrintWriter writer = new PrintWriter(file);
            writer.println("\n"+eventName);
            String paraStr = "";
            for(String parameter : parameters){
                if(parameters.indexOf(parameter) == parameters.size()-1){
                    paraStr += parameter;
                }else{
                    paraStr += parameter+"-";
                }
                
            }
            writer.println(paraStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}