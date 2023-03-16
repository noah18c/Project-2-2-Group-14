package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class SkillEditor {
    private List<Skill> skills;
    private String filename;
    public static ArrayList<Event> events = new ArrayList<>();
    
    public SkillEditor(String filename) {
        skills = new ArrayList<>();
        this.filename = filename;
        loadSkills();
    }
    
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    
    public List<Skill> getSkills() {
        return skills;
    }
  
    public void saveSkills() {
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        //     for (Skill skill : skills) {
        //         writer.write(skill.toSaveString() + "\n");
        //     }
        // } catch (IOException e) {
        //     System.err.println("Error writing skills to file: " + e.getMessage());
        // }
    }
    
    private void loadSkills() {//Reads skills.txt and makes skill objects from each skill already contained in the file
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
