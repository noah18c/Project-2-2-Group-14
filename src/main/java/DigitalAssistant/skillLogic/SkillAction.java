package DigitalAssistant.skillLogic;

import java.util.HashMap;

public abstract class SkillAction {
    public abstract void performAction(HashMap<String, String[]> parameters);
}
