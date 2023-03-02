package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Skill {

    private String name;
    private String prototype;
    private HashMap<String, ArrayList<String>> placeholders;
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

    public void setPlaceholderValue(String placeholder, ArrayList<String> values) {
        placeholders.put(placeholder, values);
    }

    // public String getPlaceholderValue(String placeholder) {
    //     return placeholders.get(placeholder);
    // }



    public void setAction(SkillAction action) {
        this.action = action;
    }

    public SkillAction getAction() {
        return action;
    }



    public void performAction(){
        // action.performAction(placeholders);
    }
    

    //Converts this Skill to txt format of the skill.
    public String toSaveString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(":").append(prototype).append("\n");
        for (String placeholder : placeholders.keySet()) {
            sb.append(placeholder).append("=").append(placeholders.get(placeholder)).append("\n");
        }
        return sb.toString();
    }

}