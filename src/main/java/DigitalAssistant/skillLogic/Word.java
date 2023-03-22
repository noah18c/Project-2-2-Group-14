package DigitalAssistant.skillLogic;

public class Word {

    private String value;

    public Word(String wordToTokenize){
        this.value = wordToTokenize;
    }

    public String getValue(){return this.value;}
    public boolean isPlaceholder(){
        if(value.charAt(0) == '!' && value.charAt(value.length()-1) == '?'){
            return true;
        }else{
            return false;
        }
    }
    //         if(time.isMonth(phrase.getValue(), this)){
    //             //LOGIC TO LOOK FOR DAY OF MONTH

    //             return true;
    //         }//checks to see if the phrase is a month 

    //     }
    //     return false;
    // }
}