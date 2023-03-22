package DigitalAssistant.skillLogic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SentenceComparator {

    //private static ArrayList<ArrayList<String>> CommonPlaceholder =  new ArrayList<>();
    private static ArrayList<String> CommonSentences=new ArrayList<String>();
    private static ArrayList<String> TunnelSentences=new ArrayList<String>();
    private static String input;


    //INTIALIZE
    //Need template sentences without placeholders
    //Need Tunnel sentences (Questions to give user for each template sentences)
    //TODO: Provide:
    // * Sentences without placeholders
    // * Tunnel Questions
    // * input of user (without placeholders if possible)
    public SentenceComparator(ArrayList<String>CommonSentences, ArrayList<String>TunnelSentences, String input){
        this.input=input;
        this.CommonSentences=CommonSentences;
        this.TunnelSentences=TunnelSentences;
    }

    public static void main(String[] args){

        //Input
        FillArrays();
        input = "Whats the lecture on monday bro ?";

        //Similar sentences with associated templates in order
        ArrayList<String> simSentences= GetSimillarSentences(input);
        ArrayList<String> simTemplates= GetSimillarTunnels(input);
        ArrayList<Double> ratingArray= GetRatingSentences(input,simSentences);

        //Manage Outputs
        System.out.println(ManageOutput(3,simSentences,simTemplates,ratingArray));

    }



    //Returns similarity rating between 2 sentences using edit distance
    private static double stringSimilarity(String s1, String s2){
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }
        int distance = dp[len1][len2];
        double maxLen = Math.max(len1, len2);
        double similarityPercentage = (maxLen - distance) / maxLen * 100;
        return similarityPercentage;
    }

    //return arraylists of most similar sentences
    private static ArrayList<String> GetSimillarSentences(String sentence){
        //Fill the Arrays
        //1. Sentences
        //2. Rating
        ArrayList<String> simSentences=new ArrayList<String>();
        ArrayList<Double> ratingSentences=new ArrayList<Double>();

        //Copy Sentences
        for (int i=0; i< CommonSentences.size();i++){
            simSentences.add(CommonSentences.get(i));
        }
        //Get SimRating for each sentences
        for (int i=0;i<CommonSentences.size();i++){
            ratingSentences.add(stringSimilarity(sentence,simSentences.get(i)));
        }

        //Sort the Arrays
        // Sort the simSentences ArrayList based on the sorted ratingSentences ArrayList
        ArrayList<String> sortedSimSentences = new ArrayList<String>();
        ArrayList<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();
        for (int i = 0; i < simSentences.size(); i++) {
            Pair<String, Double> pair = new Pair<String, Double>(simSentences.get(i), ratingSentences.get(i));
            pairs.add(pair);
        }
        Collections.sort(pairs, new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
                return Double.compare(p2.getValue(), p1.getValue()); // sorting in descending order
            }
        });
        for (Pair<String, Double> pair : pairs) {
            sortedSimSentences.add(pair.getKey());
        }

        return sortedSimSentences;
    }

    //return arraylists of most similar sentences
    private static ArrayList<Double> GetRatingSentences(String sentence, ArrayList<String> OrderedSentence){
        ArrayList<Double> ratingSentences=new ArrayList<Double>();

        for (int i=0;i<OrderedSentence.size();i++){
            ratingSentences.add(stringSimilarity(sentence,OrderedSentence.get(i)));
        }
        return ratingSentences;
    }

    //Same as above but returns tunnels, not sentences
    private static ArrayList<String> GetSimillarTunnels(String sentence){
        //Fill the Arrays
        //1. Tunnels
        //2. Rating
        ArrayList<String> simTunnels=new ArrayList<String>();
        ArrayList<Double> ratingSentences=new ArrayList<Double>();

        //Copy Sentences
        for (int i=0; i< TunnelSentences.size();i++){
            simTunnels.add(TunnelSentences.get(i));
        }
        //Get SimRating for each sentences
        for (int i=0;i<CommonSentences.size();i++){
            ratingSentences.add(stringSimilarity(sentence,simTunnels.get(i)));
        }

        //Sort the Arrays
        // Sort the simSentences ArrayList based on the sorted ratingSentences ArrayList
        ArrayList<String> sortedSimTunnels = new ArrayList<String>();
        ArrayList<Pair<String, Double>> pairs = new ArrayList<Pair<String, Double>>();
        for (int i = 0; i < simTunnels.size(); i++) {
            Pair<String, Double> pair = new Pair<String, Double>(simTunnels.get(i), ratingSentences.get(i));
            pairs.add(pair);
        }
        Collections.sort(pairs, new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
                return Double.compare(p2.getValue(), p1.getValue()); // sorting in descending order
            }
        });
        for (Pair<String, Double> pair : pairs) {
            sortedSimTunnels.add(pair.getKey());
        }

        return sortedSimTunnels;
    }

    //Get the sentences
    private static void FillArrays(){
        CommonSentences.add("How far from !CITY? to !CITY??");
        //CommonPlaceholder.add(new ArrayList<>(Arrays.asList("Maastricht","Chicago","Paris","London")));
        TunnelSentences.add("Do you want to know the distances between 2 cities?");

        CommonSentences.add("What lecture do I have on !DAY? at !TIME?");
        //CommonPlaceholder.add(new ArrayList<>(Arrays.asList("Monday","Tuesday","11AM","3PM")));
        TunnelSentences.add("Do you want to know what lecture do you have on a day?");

        CommonSentences.add("Add an event on !DAY? at !TIME?.");
        //CommonPlaceholder.add(new ArrayList<>(Arrays.asList("Monday","Tuesday","11AM","3PM")));
        TunnelSentences.add("Do you want to know add an event on a certain time?");

        CommonSentences.add("Is there a <EVENT> game on !DAY?");
        //CommonPlaceholder.add(new ArrayList<>(Arrays.asList("Baseball","Football","Monday","Tuesday")));
        TunnelSentences.add("Do you want to know if there is a game on a certain day?");

        CommonSentences.add("What do I have scheduled on !DAY? at !TIME??");
        //CommonPlaceholder.add(new ArrayList<>(Arrays.asList("Monday","Tuesday","11AM","3PM")));
        TunnelSentences.add("Do you want to know what do you have scheduled on a certain day and time?");
    }

    //What to answer to the user ?
    private static String ManageOutput(int rank,ArrayList<String> simSentences, ArrayList<String> simTemplates, ArrayList<Double> ratingArray){
        rank = rank-1;
        String out= simSentences.get(rank)+ " "+Math.round(ratingArray.get(rank))+"% \n"+simTemplates.get(rank);

        if(stringSimilarity(simSentences.get(rank),input)>80){
            return out;
            //TODO: Call class with template sentence
            //Sentence: simSentences.get(rank)      (rank=0 by default)
        }

        if(stringSimilarity(simSentences.get(rank),input)<=80){
            return out;
            //TODO: Call tunnel class
            //tunnel question: simTemplates.get  (rank=0 by default)
        }

        return "";
    }

    private static String RemovePlaceHolder(String input) {

        return input;
    }
}

