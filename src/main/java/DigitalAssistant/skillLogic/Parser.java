package DigitalAssistant.skillLogic;
import java.time.Month;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    public ArrayList<ArrayList<String>> messages = new ArrayList();
    public int lastMessageIndex = -1;
    public String lastParsedString;
    public Time.Month month;
    public Time time = new Time();
    public int day;
    public int year = 2020;
    public boolean isLeap;

    public Parser(){
        isLeap = Time.isLeapYear(year);
    }

    public void tokenizeMessage(String stringToParse){
        ArrayList<String> words = new ArrayList();
        Scanner scanner = new Scanner(stringToParse);
        scanner.useDelimiter(" ");
        
        while(scanner.hasNext()){
            String currentSring = scanner.next();
            words.add(currentSring);
        }
        scanner.close();
        messages.add(words);
        lastMessageIndex++;
    }

    public boolean findDates(int messageIndex){
        for(String phrase : messages.get(messageIndex)){
            if(time.isMonth(phrase, this)){
                //LOGIC TO LOOK FOR DAY OF MONTH

                return true;
            }//checks to see if the phrase is a month 

        }
        return false;
    }

    public static int getInt(String str){
        String strReg = str.replaceAll("[^0-9:]", "");
        if(strReg.equals("") || strReg.contains(":")){return -1;}
        return Integer.valueOf(strReg);
    }
    
    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public int getLastMessageIndex() {
        return lastMessageIndex;
    }

    public String getLastParsedString() {
        return lastParsedString;
    }

    public Time.Month getMonth() {
        return month;
    }

    public Time getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public boolean isLeap() {
        return isLeap;
    }
    
    public static void main(String[] args) {
        Parser parser = new Parser();
        String example = "Place a meeting on the 14th of February";
        parser.tokenizeMessage(example);
        ArrayList<String> message = parser.getMessages().get(parser.getLastMessageIndex());
        for(String word : message){
            System.out.println(word);
        }
      
    }


}
