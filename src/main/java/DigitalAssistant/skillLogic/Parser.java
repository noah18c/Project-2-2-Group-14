package DigitalAssistant.skillLogic;
import java.time.Month;
import java.util.Scanner;

public class Parser {
    public String lastParsedString;
    public Time.Month month;
    public Time time = new Time();
    public int day;
    public int year = 2020;
    public boolean isLeap;

    public Parser(){
        isLeap = Time.isLeapYear(year);
    }

    public void getSkillInputs(String stringToParse){
        Scanner scanner = new Scanner(stringToParse);
        String previousString = "";
        scanner.useDelimiter(" ");
        while(scanner.hasNext()){
            String currentSring = scanner.next();

            /*If a month is detected, save it and check to see if a day is entered before or after and save it, 
                otherwise display error
             */
            time.isMonth(currentSring, this);
            
    
            previousString = currentSring;
        }
        scanner.close();
    }

    public static int getInt(String str){
        String strReg = str.replaceAll("[^0-9:]", "");
        if(strReg.equals("") || strReg.contains(":")){return -1;}
        return Integer.valueOf(strReg);
    }
    
    public static void main(String[] args) {
        Parser parser = new Parser();
        String example = "Place a meeting on the 14th of February";
        parser.getSkillInputs(example);
        System.out.println(parser.month);
        System.out.println(parser.day);
      
    }

}
