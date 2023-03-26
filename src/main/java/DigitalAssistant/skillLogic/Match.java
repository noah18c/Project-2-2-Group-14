package DigitalAssistant.skillLogic;

import java.util.ArrayList;
import javafx.util.Pair;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Match {
    private String input;
    private List<Skill> skills;
    private ArrayList<String> protoypes;

    public Match(String input, List<Skill> skills){
        this.input = input;
        this.skills = skills;

        //prototype sentences of all skills
        protoypes = new ArrayList<>();
        for (int i = 0; i < skills.size(); i++) {
            protoypes.add(deletePlaceholder(skills.get(i).getPrototype()));
        }
    }

    public static void main(String[] args) {
        //SkillEditor skillEditor = new SkillEditor("/Users/user/Documents/GitHub/Project-2-2-Group-14/src/main/java/DigitalAssistant/skillLogic/skills.txt");
        // TODO: Pick right path
        SkillEditor skillEditor = new SkillEditor();
        String input = "How do I get from Maastricht to Herleen ?";
        
        //System.out.println(input);
        Match match = new Match(input, skillEditor.getSkills());
        Skill simSkill= match.searchSkill();
        //TODO: beware... skill=null gives errors
        System.out.println(simSkill.getPrototype());
    }

    public Skill searchSkill() {
        Skill maxSimilaritySkill = null;
        double maxSimilarity = 0.0;
        for (int i = 0; i < skills.size(); i++) {
            String skillproto = skills.get(i).getPrototype();
            double simrating = Math.round(stringSimilarity(input, skillproto));
            // TODO: adjust sensitivity (simrating)
            if ((simrating > maxSimilarity)&&(simrating > 30)) {
                maxSimilarity = simrating;
                maxSimilaritySkill = skills.get(i);
            }
        }
        return maxSimilaritySkill;
    }

    //Returns similarity rating between 2 sentences using edit distance
    private double stringSimilarity(String s1, String s2){
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
    private ArrayList<String> GetSimillarSentences(String sentence){
        
        //Fill the Arrays
        //1. Sentences
        //2. Rating
        ArrayList<String> simSentences=new ArrayList<String>();
        ArrayList<Double> ratingSentences=new ArrayList<Double>();

        //Copy Sentences
        for (int i=0; i< protoypes.size();i++){
            simSentences.add(protoypes.get(i));
        }
        //Get SimRating for each sentences
        for (int i=0;i<protoypes.size();i++){
            ratingSentences.add(stringSimilarity(sentence,simSentences.get(i)));
        }

        //Sort the Arrays
        //Sort the simSentences ArrayList based on the sorted ratingSentences ArrayList
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

    public String deletePlaceholder(String protoype){
        String regex = protoype.substring(0, protoype.length() - 1) + " " + protoype.charAt(protoype.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        String[] words = regex.split(" ");

        for (int i = 0; i < words.length; i++) {
            if(words[i].startsWith("<") && words[i].endsWith(">")){
                words[i] = "";
            }
        }
        protoype = "";
        for (int i = 0; i < words.length; i++) {
            protoype +=words[i] + " ";
        }
        return protoype;
    }
}