package DigitalAssistant.skillLogic;

import java.util.ArrayList;

public class Event {
    private String eventName;
    private ArrayList<String> parameters;

    public Event(String eventName, ArrayList<String> parameters){
        this.eventName = eventName;
        this.parameters = parameters;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }


}
