package DigitalAssistant.skillLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Skill skill : skills) {
                writer.write(skill.toSaveString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing skills to file: " + e.getMessage());
        }
    }
    
    private void loadSkills() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Skill skill = Skill.fromSaveString(line);
                if (skill != null) {
                    skills.add(skill);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading skills from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor("anan.txt");
        
        // Create a new skill with placeholders for day and time
        Skill skill = new Skill("Lecture","Which lectures are there on ¡DAY¿ at ¡TIME¿");
        skill.setPlaceholderValue("¡DAY¿", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        skill.setPlaceholderValue("¡TIME¿", new String[]{"9", "11", "15"});
        
        // Define the action for the skill
        skill.setAction(new SkillAction() {
            @Override
            public void performAction(HashMap<String, String[]> arguments) {
                String[] day = arguments.get("$DAY$");
                String[] time = arguments.get("¡TIME¿");
                
                // Perform the action based on the day and time
                if (day.equals("Monday") && time.equals("9")) {
                    System.out.println("There is a Math lecture at 9 AM on Monday.");
                } else if (day.equals("Tuesday") && time.equals("11")) {
                    System.out.println("There is a Physics lecture at 11 AM on Tuesday.");
                } else if (day.equals("Wednesday") && time.equals("15")) {
                    System.out.println("There is a Chemistry lecture at 3 PM on Wednesday.");
                } else {
                    System.out.println("There are no lectures scheduled for that day and time.");
                }
            }
        });
        
        // Test the skill
        String input = "Which lectures are there on Monday at 9?";
        skill.performAction();
    }
}
