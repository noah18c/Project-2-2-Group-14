package DigitalAssistant.skillLogic;

import java.util.HashMap;
import java.util.Map;

public class Skill {

    private String name;
    private String prototype;
    private HashMap<String, String[]> placeholders;
    private SkillAction action;

    public Skill(String name, String prototype) {
        this.name = name;
        this.prototype = prototype;
        this.placeholders = new HashMap<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPlaceholderValue(String placeholder, String[] value) {
        placeholders.put(placeholder, value);
    }

    public String[] getPlaceholderValue(String placeholder) {
        return placeholders.get(placeholder);
    }

    public void setAction(SkillAction action) {
        this.action = action;
    }

    public SkillAction getAction() {
        return action;
    }

    public void performAction(){
        action.performAction(placeholders);
    }

    
    public boolean matches(String input) {
        String[] words = input.split(" ");
        String[] prototypeWords = prototype.split(" ");
        if (words.length != prototypeWords.length) {
            return false;
        }
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String prototypeWord = prototypeWords[i];
            if (prototypeWord.startsWith("¡") && prototypeWord.endsWith("¿")) {
                String placeholder = prototypeWord.substring(1, prototypeWord.length() - 1);
                //placeholders.put(placeholder, word);
            } else if (!word.equals(prototypeWord)) {
                return false;
            }
        }
        return true;
    }
    
    public String toSaveString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":").append(prototype).append("\n");
        for (String placeholder : placeholders.keySet()) {
            sb.append(placeholder).append("=").append(placeholders.get(placeholder)).append("\n");
        }
        return sb.toString();
    }
    
    public static Skill fromSaveString(String saveString) {
        String[] lines = saveString.split("\n");
        String[] parts = lines[0].split(":");
        String name = parts[0];
        String prototype = parts[1];
        Skill skill = new Skill(name, prototype);
        for (int i = 1; i < lines.length; i++) {
            String[] keyValue = lines[i].split("=");
            String placeholder = keyValue[0];
            String value = keyValue[1];
            //skill.setPlaceholderValue(placeholder, value);
        }
        return skill;
    }
}
