package DigitalAssistant.Utilities;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Rule {

    private String action, output, slotValueString;
    private ObservableList<SlotValuePair> slotValuePairs;

    public Rule(ObservableList<SlotValuePair>  slotValuePairs, String action, String output){
        this.slotValuePairs = slotValuePairs;
        this.slotValuesToString();
        this.action = action;
        this.output = output;
    }

    private void slotValuesToString(){
        slotValueString = "";

        for(SlotValuePair pair : slotValuePairs){
            slotValueString += pair.getSlot()+": "+pair.getValue()+", ";
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getSlotValueString() {
        return slotValueString;
    }

    public ObservableList<SlotValuePair> getSlotValuePairs() {
        return slotValuePairs;
    }

    public void setSlotValuePairs(ObservableList<SlotValuePair> slotValuePairs) {
        this.slotValuePairs = slotValuePairs;
    }
}
