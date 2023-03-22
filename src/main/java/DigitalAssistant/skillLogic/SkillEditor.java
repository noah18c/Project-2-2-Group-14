package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

<<<<<<< HEAD

=======
>>>>>>> 2355ce325f46de977ba0d04c30cf0884415a19b0
public class SkillEditor {
    private List<Skill> skills;
    private String filename;
    public static ArrayList<Event> events = new ArrayList<>();
    
    public SkillEditor(String filename) {
        skills = new ArrayList<>();
        this.filename = filename;
        loadSkills();
    }

    public List<Skill> getSkills() {
        return skills;
    }
  
    public void saveSkill(Skill skill) {
    }

    private void loadSkills() {//Reads skills.txt and makes skill objects from each skill already contained in the file
<<<<<<< HEAD
        try (BufferedReader reader = new BufferedReader(new FileReader("skills.txt"))) {

            String currentLine;
            String phKey = "";       // placeholder i.e. !DAY?
            ArrayList<String> phValues = new ArrayList<>();     // list of placeholders
    
            currentLine = reader.readLine();
            while (!currentLine.equals(".....")) {      //Until the end of defined skill
                if(currentLine.equals("*****")){        // '*****' indicates the start of a new skill
                    String skillName = reader.readLine();
                    String skillPrototype = reader.readLine();
                    
                    String ph = reader.readLine();
                    while(!ph.equals("*****") || !ph.equals(".....")){
                        
                        // TODO: test placeholder extraction

                        Scanner scanner = new Scanner(ph);
                        scanner.useDelimiter(" ");
                        phKey = scanner.next(); 
                        System.out.println(phKey);
                        String temp = scanner.next();
                        Scanner scan2 = new Scanner(temp);
                        scan2.useDelimiter("||");

                        while(scan2.hasNext()){
                            phValues.add(scanner.next());       // add placeholder values to arrayList
                        }

                        ph = reader.readLine();        
                        scanner.close();
                        scan2.close();
                    }
                    
                    Skill newSkill = new Skill(skillName, skillPrototype);
                    //set Placeholders
                    newSkill.setPlaceholderValue(phKey, phValues);
                    skills.add(newSkill);
                    currentLine = ph;
                }


            }
        } catch (IOException e) {
            System.err.println("Error reading skills from file: " + e.getMessage());
        }
    }

    /**
     * Method to extract info from user input when asking him to define an unknown skill!
     * Needed ONLY when the assistant recognizes unfamiliar skill -> gets stuck
     * if skill (prototype or placeholder) unknown then do questionnaire & store answers; if info complete then declareSKill(input)
     * Dynamic Questionnaire implemented in front end !?
     * extraction of answers in back end in this method !
     * 
     * input: the stored user answers
     * method creates a skill object which is added to skills list and can be written to the skills.txt file
     * @param ArrayList of strings
     * ---------------------------
     * Questions to tunnel him:
     * I don't get this. Would you like to define a new skill? I'd be happy to learn more to help you with your needs.
     *  A: Yes / No 
     * Perfect! What would you like to call the skill? 
     *  A0: ¡skillname¿
     * Provide a list of placeholders formatted with ¡¿ and seperated by commas
     *  A1: ¡sports¿, ¡cook timer¿ etc.
     * Enter a prototype sentence for the skill ¡skillname¿. It must include the placeholders!
     *  A2: ¡This is a prototype sentence with ¡placeholder¿ . 
     * Provide values for the placeholder(s)! separated by commas
     *  A3: Volleyball, Handball, Windsurfing
     * Your new skill ¡skillName¿ has been defined!
     *
     */

    public void declareSkill(ArrayList<String> input){
        String skillName = "";
        String prototype = "";
        String phName = "";
        ArrayList<String> values = new ArrayList<String>(); // placeholder values

        // extract skill name, prototype sentence, placeholder and its values if specified
        skillName = input.get(0);
        prototype = input.get(2);
        phName = input.get(1);
        String temp = input.get(3);
        Scanner scan = new Scanner(temp);
        scan.useDelimiter(",");
        while (scan.hasNext()){
            values.add(scan.next());
        }
        scan.close();

        // create skill object with that stuff
        Skill skill = new Skill(skillName, prototype);
        addSkill(skill);
        skill.setPlaceholderValue(phName, values);          // placeholders is a hashmap of String and ArrayList
        saveSkills();           // write the new skill to skills.txt
        loadSkills();           // !? creates a loaded skill again!?
        /** 
        if (in.toLowerCase().equals("yes")){
            // post next response to the chat --> "Perfect! What would you like to call the skill?"
            // botWriter(false, "Perfect! What would you like to call the skill?")

            skillName = in.toLowerCase();
            // post next response to the chat --> "Enter a prototype sentence to use the skill ¡skillname¿ with."
            // botWriter(false, "Enter a prototype sentence to use the skill ¡skillname¿ with.")
            // get new input
            prototype = in;

        }
        if (in.toLowerCase().equals("no")){
            System.out.println("Try a different skill command. Here is a list of skills I understand.");
        }
        */
    }

    /**
     * Used when user wants to add new values to existing placeholders of defined skills
     * @param input
     */
    public void addValue(String input){
        ArrayList<String> values = new ArrayList<String>();
        Scanner scan = new Scanner(input);
        scan.useDelimiter(",");
        while (scan.hasNext()){
            values.add(scan.next());
        }
        scan.close();
        // save values

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
=======
        try (Scanner scanner = new Scanner(new File(filename))) {
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
                    //System.out.println(skill.getPlaceholders().keySet());

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
                        HashMap<String,String> actionValues = new HashMap<>(); 
                        String actionType = new String();
                        String actionInput = new String();

                        int counter = 0;// assign the value for corresponding key just one time, so ignore the placholder if there is in the action sentence
                        for (int i = 0; i < actionLineString.length; i++) {
                            for (int j = 0; j < placeholders.size(); j++) {
                                if(actionLineString[i].equals(placeholders.get(j))){
                                    counter++;
                                    actionValues.put(placeholders.get(j), actionLineString[i+1]);
                                }
                            }
                            if(counter == placeholders.size()){
                                break;
                            }
                        }

                        for (int i = 0; i < actionLineString.length; i++) {
                            if(actionLineString[i].startsWith("|") && actionLineString[i].endsWith("|")){
                                actionType = actionLineString[i];
                                for (int j = i+1; j < actionLineString.length; j++) {
                                    actionInput += actionLineString[j] + " ";
                                }
                            }
                        }

                        //System.out.println(actionType + " " + actionInput);
                        //System.out.println(actionValues);

                        Action action = new Action(actionType, actionInput, actionValues);
                        skill.addAction(action);
                    }
                }
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
>>>>>>> 2355ce325f46de977ba0d04c30cf0884415a19b0
        }
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

    public static void main(String[] args) {
<<<<<<< HEAD
        System.out.println("Before adding event");
        loadEvents();
        for(Event event : SkillEditor.events){
            System.out.printf("Event: %s\nParameters: {", event.getEventName());
            for(String para : event.getParameters()){
                System.out.printf("%s, ", para);
            }
            System.out.printf("}\n\n");
        }

        System.out.println("After adding event");
        ArrayList<String> tmpPara = new ArrayList<>();
        tmpPara.add("monday");
        tmpPara.add("15:00");
        tmpPara.add("tuesday");
        tmpPara.add("16:00");
        addEventToFile("swimming", tmpPara);

        loadEvents();
        for(Event event : SkillEditor.events){
            System.out.printf("Event: %s\nParameters: {", event.getEventName());
            for(String para : event.getParameters()){
                System.out.printf("%s, ", para);
            }
            System.out.printf("}\n\n");
        }


    // //  SkillEditor skillEditor = new SkillEditor("anan.txt");
        
    //     // Create a new skill with placeholders for day and time
    //     //Skill skill = new Skill("Lecture","Which lectures are there on ¡DAY¿ at ¡TIME¿");
    //     Skill skill = new Skill("Lecture","Schedule a meeting on ¡DAY¿ at ¡TIME¿");
    //     //skill.setPlaceholderValue("¡DAY¿", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
    //     //skill.setPlaceholderValue("¡TIME¿", new String[]{"9", "11", "15"});
        
    //     // Define the action for the skill
    //     SkillAction action = new SkillAction() {

    //         @Override
    //         public void performAction(HashMap<String, String> parameters) {
    //             // TODO Auto-generated method stub
    //             // store meetings in some data structure

    //             System.out.println("Schedule a new event for tomorrow 9 AM.");
    //             //parameters.put(day, time);
    //             System.out.println("Success.");
    //         }
            
    //     };

    //     skill.setAction(action);
        
    //     // skill.setAction( new SkillAction() {

    //     //     @Override
    //     //     public void performAction(HashMap<String, String[]> arguments) {
    //     //         String[] day = arguments.get("$DAY$");
    //     //         String[] time = arguments.get("¡TIME¿");
                
    //     //         // Perform the action based on the day and time
    //     //         if (day.equals("Monday") && time.equals("9")) {
    //     //             System.out.println("There is a Math lecture at 9 AM on Monday.");
    //     //         } else if (day.equals("Tuesday") && time.equals("11")) {
    //     //             System.out.println("There is a Physics lecture at 11 AM on Tuesday.");
    //     //         } else if (day.equals("Wednesday") && time.equals("15")) {
    //     //             System.out.println("There is a Chemistry lecture at 3 PM on Wednesday.");
    //     //         } else {
    //     //             System.out.println("There are no lectures scheduled for that day and time.");
    //     //         }
    //     //     }

    //     // });
        
    //     // Test the skill
    //     String input = "Which lectures are there on Monday at 9?";
    //     skill.performAction();
    }
}
=======
        SkillEditor skillEditor = new SkillEditor("/Users/user/Documents/GitHub/Project-2-2-Group-14/src/main/java/DigitalAssistant/skillLogic/skills.txt");
        String input = "What is the distance between Ankara to Paris?";
        System.out.println(input);
        for (int i = 0; i < skillEditor.skills.size(); i++) {
            skillEditor.skills.get(i).match(input);
        }
    }
    
}
>>>>>>> 2355ce325f46de977ba0d04c30cf0884415a19b0
