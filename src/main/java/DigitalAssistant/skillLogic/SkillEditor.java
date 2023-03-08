package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class SkillEditor {
    private List<Skill> skills;
    private String filename;
    
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

                        ph = reader.readLine();        // what is this for? reading next line in file?
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

    public static void main(String[] args) {
    //  SkillEditor skillEditor = new SkillEditor("anan.txt");
        
        // Create a new skill with placeholders for day and time
        //Skill skill = new Skill("Lecture","Which lectures are there on ¡DAY¿ at ¡TIME¿");
        Skill skill = new Skill("Lecture","Schedule a meeting on ¡DAY¿ at ¡TIME¿");
        //skill.setPlaceholderValue("¡DAY¿", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        //skill.setPlaceholderValue("¡TIME¿", new String[]{"9", "11", "15"});
        
        // Define the action for the skill
        SkillAction action = new SkillAction() {

            @Override
            public void performAction(HashMap<String, String> parameters) {
                // TODO Auto-generated method stub
                // store meetings in some data structure

                System.out.println("Schedule a new event for tomorrow 9 AM.");
                //parameters.put(day, time);
                System.out.println("Success.");
            }
            
        };

        skill.setAction(action);
        
        // skill.setAction( new SkillAction() {

        //     @Override
        //     public void performAction(HashMap<String, String[]> arguments) {
        //         String[] day = arguments.get("$DAY$");
        //         String[] time = arguments.get("¡TIME¿");
                
        //         // Perform the action based on the day and time
        //         if (day.equals("Monday") && time.equals("9")) {
        //             System.out.println("There is a Math lecture at 9 AM on Monday.");
        //         } else if (day.equals("Tuesday") && time.equals("11")) {
        //             System.out.println("There is a Physics lecture at 11 AM on Tuesday.");
        //         } else if (day.equals("Wednesday") && time.equals("15")) {
        //             System.out.println("There is a Chemistry lecture at 3 PM on Wednesday.");
        //         } else {
        //             System.out.println("There are no lectures scheduled for that day and time.");
        //         }
        //     }

        // });
        
        // Test the skill
        String input = "Which lectures are there on Monday at 9?";
        skill.performAction();
     }
}
