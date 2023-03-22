package DigitalAssistant.skillLogic;
import java.time.Month;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    public ArrayList<ArrayList<Word>> messages = new ArrayList();
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
        ArrayList<Word> words = new ArrayList();
        Scanner scanner = new Scanner(stringToParse);
        scanner.useDelimiter(" ");
        
        while(scanner.hasNext()){
            String currentString = scanner.next();
            words.add(new Word(currentString));
        }
        scanner.close();
        messages.add(words);
        lastMessageIndex++;
    }

   

    public static int getInt(String str){
        String strReg = str.replaceAll("[^0-9:]", "");
        if(strReg.equals("") || strReg.contains(":")){return -1;}
        return Integer.valueOf(strReg);
    }
    
    public ArrayList<ArrayList<Word>> getMessages() {
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
        String str = "!DAY? - #weekDays||#numericDays||";
        Scanner scanner = new Scanner(str);
        scanner.useDelimiter(" ");
        System.out.println(scanner.next());
        scanner.next();
        String nextPart = scanner.next();
        scanner.close();
        Scanner scanner2 = new Scanner(nextPart);
        scanner2.useDelimiter("||");
        System.out.println("Placeholders:");

        while(scanner2.hasNext()){
            System.out.println(scanner2.next());
        }
      
    }
}
