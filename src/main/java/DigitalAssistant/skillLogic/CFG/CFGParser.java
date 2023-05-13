package DigitalAssistant.skillLogic.CFG;

import java.util.*;
import DigitalAssistant.skillLogic.SkillEditor;
import DigitalAssistant.skillLogic.Word;
import DigitalAssistant.skillLogic.Skill;

public class CFGParser {

    public ArrayList<Rule> grammar;
    public List<Skill> skills;
    public ArrayList<String> words;

    public String skillName;
    public HashMap<String, ArrayList<String>> placeholderValues;
    public boolean isFinished = false;

    public CFGParser(List<Skill> skills){
        this.skills = skills;
        placeholderValues = new HashMap<>();
        loadRules();
    }

    public boolean parse(String sentence) {

        words = processStringCFG(sentence);
        
        for (int i = 0; i < grammar.get(0).expansions.size(); i++) {
            //System.out.println(i);
            ArrayList<String> clonedWords = new ArrayList<String>();
            clonedWords.addAll(words);
            
            if(findRule(getSkillRule(grammar.get(0).expansions.get(i)), clonedWords)){ // Visit each rule
                skillName = grammar.get(0).expansions.get(i);
                break;
            }
            else{
                placeholderValues = new HashMap<>();
            }
        }
        

        return false;
    }

    public boolean findRule(Rule rule, ArrayList<String> words){
        //System.out.println(rule.nonterminal + "  " + words.toString());

        // if(words.size() == 0){//IF WE DELETED EVERYTHING WE SHOULD BE GOOD AND HAPPY AND RETURN
        //     System.out.println("1");

        //     return true;

       // }else{
            ArrayList<String> next = new ArrayList<String>();
            next.addAll(words);

            //Remove every expansion words of rule from sentence
            for (int i = 0; i < rule.expansions.size(); i++) {
                for (int j = 0; j < next.size(); j++) {
                    if(rule.expansions.get(i).equalsIgnoreCase(next.get(j))){
                        next.remove(j);
                    }   
                }
            }

            
            boolean skillFound = false;
            if((words.size() - next.size()) == rule.expansions.size()){
                skillFound = true;
                for (int i = 0; i < skills.size(); i++) {
                    if(skills.get(i).getName().equals(rule.nonterminal)){
                        for (String key : skills.get(i).getPlaceholders().keySet()) {
                            ArrayList<String> list = new ArrayList<>();
                            placeholderValues.put(key, list ); //Add the key with null value to the clonedHashMap
                        }                        
                    }
                }
            }


            if(skillFound){
                for (int i = 0; i < next.size(); i++) {                
                    if(checkPlaceholders(rule, next.get(i),false)){
                        next.remove(i);
                        i = i-1;
                    }
                }
    
                for (int i = 0; i < next.size(); i++) {
                    if(checkPlaceholders(rule, next.get(i), true)){
                        next.remove(i);
                        i = i-1;
                    }
                }
            }

            if(next.size() == 0){
                return true;
            }
            else{
                return false;
            }
            
        //     if(rule.expansions.contains(words.get(0))){//WE FOUND A NON-TERMINAL, SO WE DELETE AND CONTINUE
        //         ArrayList<String> next = new ArrayList<String>();
        //         next.addAll(words);
        //         next.remove(words.get(0));

        //         if(words.size() == 0){
        //             System.out.println("2");
        //                 return true;
        //             }


        //         return findRule(rule, next);
                
        //     }else{//WE DIDNT FIND A TERMINAL
            
        //         boolean placeholderFound = false;
        //         if(checkPlaceholders(rule, words.get(0))){
        //             placeholderFound = true;
                    
        //             ArrayList<String> next = new ArrayList<String>();
        //             next.addAll(words);
        //             next.remove(words.get(0));

        //             if(words.size() == 0){
        //                 System.out.println("3");

        //                 return true;
        //             }
                    
        //             return findRule(rule, next);
        //         }
                
        //         if(placeholderFound != true){
        //             placeholderValues = new ArrayList<String>();//resets any previously set placeholders
        //             return false;
        //         }
        //     }
        //}
        // return false;
    }


    public boolean checkPlaceholders(Rule rule, String wordToCheck , boolean input){

        for(String placeholder : rule.placeholders){
            Rule currentPlaceholder = getSkillRule(placeholder);
            if(currentPlaceholder.expansions.size() == 1){
                if(currentPlaceholder.expansions.get(0).equals("@INPUT")){
                    if(input){
                        placeholderValues.get(currentPlaceholder.nonterminal).add(wordToCheck);
                        return true;
                    }
                }
            }
            for (int i = 0; i < currentPlaceholder.expansions.size(); i++) {
                if(currentPlaceholder.expansions.get(i).equalsIgnoreCase(wordToCheck)){
                    placeholderValues.get(currentPlaceholder.nonterminal).add(wordToCheck);
                    return true;
                }
            }
        }
        return false;
    }

    //If rule cannot be found, null will be returned.
    public Rule getSkillRule(String ruleName){
        Rule failCase = null;
        for(Rule rule : grammar){
            if(rule.nonterminal.equalsIgnoreCase(ruleName)){
                return rule;
            }
        }
        return failCase;
    }

    public void loadRules(){
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Rule startRule = new Rule("<S>", new ArrayList<String>());
        rules.add(startRule);

        for(Skill skill : skills){
            startRule.expansions.add(skill.getName());
            
            Rule newRule = new Rule(skill.getName(), processStringCFG(skill.getPrototype()));
            
            rules.add(newRule);
            
            Set<String> keySet = skill.getPlaceholders().keySet();//Making the hashMap iterable
            ArrayList<String> keyList = new ArrayList<>(keySet);
            
            for(String key : keyList){
                newRule.placeholders.add(key);
                
                Rule placeholderRule = new Rule(key, skill.getPlaceholders().get(key));
                rules.add(placeholderRule);
            }
        }
        grammar = rules;
    }

    public static ArrayList<String> processStringCFG(String str){
        str = str.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|@#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters 

        List<String> strList = Arrays.asList(str.toLowerCase().split(" "));
        ArrayList<String> words = new ArrayList<>();
        words.addAll(strList);
        

        ArrayList<String> toRemove = new ArrayList<>();
        for (String word : words) {
            if (word.charAt(0) == '<' || word.charAt(word.length() - 1) == '>') {
                toRemove.add(word);
            }
        }
        words.removeAll(toRemove);
        return words;
    }

    public class Rule {
        private String nonterminal;
        private List<String> expansions;
        private ArrayList<String> placeholders;

        public Rule(String nonterminal, List<String> expansions) {
            this.nonterminal = nonterminal;
            this.expansions = expansions;
            placeholders = new ArrayList<String>();
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();
        CFGParser parser = new CFGParser(skillEditor.getSkills());
        ArrayList<Rule> grammar = parser.grammar;

        parser.parse("What is the distance between ankara to maastricht?");     
    }
}