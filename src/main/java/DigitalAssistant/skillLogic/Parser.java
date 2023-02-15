package DigitalAssistant.skillLogic;
import java.time.Month;
import java.util.Scanner;

public class Parser {
    public String lastParsedString;
    public Time.Month month;
    public Time time = new Time();

    public Parser(){}

    public void getSkillInputs(String stringToParse){
        Scanner scanner = new Scanner(stringToParse);
        scanner.useDelimiter(" ");
        while(scanner.hasNext()){
            String currentSring = scanner.next();
            //System.out.println(currentSring);
            time.isMonth(currentSring, this);
            
        }
        scanner.close();
    }

    
    public static void main(String[] args) {
        Parser parser = new Parser();
        String example = "Place a meeting on the 14th of February";
        parser.getSkillInputs(example);
        System.out.println(parser.month);
    }

}
