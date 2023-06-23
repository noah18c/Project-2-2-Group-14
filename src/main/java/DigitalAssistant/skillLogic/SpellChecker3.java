package DigitalAssistant.skillLogic;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

import DigitalAssistant.skillLogic.CFG.CFG;
import DigitalAssistant.skillLogic.CFG.CFG.Rule;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;
import java.util.List;

public class SpellChecker3 {
    //private static final Set<String> DICTIONARY = new HashSet<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    private SkillEditor skillEditor;
    private Set<String> DICTIONARY = new HashSet<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "What", "are", "do"));
    private List<Skill> skills;
    private CFG cfg;

    public SpellChecker3(SkillEditor skillEditor){
        this.skillEditor = skillEditor;
        skills = skillEditor.getSkills();
        cfg = new CFG(skills);
        addWordsToDictionary();

    }

    public void addWordsToDictionary() {
        ArrayList<Rule> grammar = cfg.grammar;

        for (Rule rule : grammar) {
            wordsSeparator(rule.ruleName);

            for (int i = 0; i < rule.nonterminal.size(); i++) {
                wordsSeparator(rule.nonterminal.get(i));
            }

            for (int i = 0; i < rule.terminal.size(); i++) {
                wordsSeparator(rule.terminal.get(i));
            }
        }
        //System.out.println(DICTIONARY);
    }

    private void wordsSeparator(String sentence) {
        if (sentence != null) {
            String[] words = sentence.split("\\s+");
            DICTIONARY.addAll(Arrays.asList(words));
        }
    }

    public String search(String sentence){

        String[] words = sentence.split("\\s+");

        int threshold = 2;

        for (int i = 0; i < words.length; i++) {
            String result = findWordWithLevenshteinDistance(words[i], threshold);
            if(result != null){
                words[i] = result;
            }
            //System.out.println("Word in dictionary: " + result);
        }

        String output = "";
        
        for (int i = 0; i < words.length; i++) {
            output += words[i] + " ";
        }
        
       return output;
    }


    public static void main(String[] args) {

        SkillEditor skillEditor = new SkillEditor();
        SpellChecker3 sp3 = new SpellChecker3(skillEditor);
    }

    private String findWordWithLevenshteinDistance(String word, int threshold) {
        String result = null;
        int bestDistance = Integer.MAX_VALUE;
    
        for (String dictWord : DICTIONARY) {
            int distance = calculateLevenshteinDistance(word.toLowerCase(), dictWord.toLowerCase());

            if (distance < bestDistance) {
                bestDistance = distance;
                result = dictWord;
            }
        }

        if (bestDistance <= threshold) {
            return result;
        } else {
            return null;
        }
    }
    

    private int calculateLevenshteinDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= word2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(Math.min(insert, delete), replace);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }
}
