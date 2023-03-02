package DigitalAssistant.skillLogic;

public class Time {
    public static Month[] months = {Month.JANUARY, Month.FEBRUARY, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};

    public static enum Month{
        JUNE("june", 6),JULY("july", 7),AUGUST("august", 8),
         SEPTEMBER("september", 9), OCTOBER("october", 10)
        ,NOVEMBER("november", 11), DECEMBER("December", 12),
        JANUARY("january", 1),FEBRUARY("february", 2), 
        MARCH("march", 3), APRIL("april", 4), MAY("may", 5); 

        private String name;
        private int value;
        private Month(String name, int monthValue){
            this.name = name;
            this.value = monthValue;
        }

        public String getName(){return this.name;}
        public int getValue(){return this.value;}
        @Override
        public String toString(){
            return this.name;
        }
    }

    public boolean isMonth(String str, Parser parser){
        for(Month month : months){
            if(str.toLowerCase().equals(month.getName())){
                parser.month = month;
                return true;
            }
        }
        return false;
    }

    public static boolean dayIsValid(int day, Month month, boolean isLeap){
        if(day < 1 || day > 31){
            return false;
        }
        switch(month.getValue()){
            case 2:
                if(isLeap){
                    if(day <= 29){return true;}
                }else{
                    if(day <= 28){return true;}
                }
                return false;
            case 4: 
                if(day<=30){return true;}
                return false;
            case 6:
                if(day<=30){return true;}
                return false;
            case 9:
                if(day<=30){return true;}
                return false;
            case 11: 
                if(day<=30){return true;}
                return false;
            }

        return true;
    }
    
    //https://nrc.canada.ca/en/certifications-evaluations-standards/canadas-official-time/what-years-are-leap-years
    public static boolean isLeapYear(int year){
        if(year % 400 == 0){
            return true;
       }else if(year % 100 == 0){
            return false; 
       }else if(year % 4 == 0){
            return true;
       }
       return false;
    }

    /*TODO:

    - Same Structure as Month enum for:
        o DAYS, TIMES, INTERVALS (for alarm/stopwatch), LOCATION, EVENT
    - Boolean checks for each time-type
    - Getters/setters etc...
     */
}
