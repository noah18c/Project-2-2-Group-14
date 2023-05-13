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
    public ArrayList<String> placeholderValues;

    public boolean isFinished = false;

    public CFGParser(List<Skill> skills){
        this.skills = skills;
        placeholderValues = new ArrayList();
        loadRules();
    }

    public boolean parse(String sentence) {

        words = processStringCFG(sentence);
        
        for (int i = 0; i < grammar.get(0).expansions.size(); i++) {
            System.out.println(i);
            ArrayList<String> clonedWords = new ArrayList<String>();
            clonedWords.addAll(words);
            
            if(findRule(getSkillRule(grammar.get(0).expansions.get(i)), clonedWords)){ // Visit each rule
                skillName = grammar.get(0).expansions.get(i);
                break;
            }
            
        }
        
        System.out.println("---------");
        System.out.println(skillName + "  " + placeholderValues.toString());

        return false;
    }

    public boolean findRule(Rule rule, ArrayList<String> words){
        System.out.println(rule.nonterminal + "  " + words.toString());

        if(words.size() == 0){//IF WE DELETED EVERYTHING WE SHOULD BE GOOD AND HAPPY AND RETURN
            System.out.println("1");

            return true;

        }else{
            if(rule.expansions.contains(words.get(0))){//WE FOUND A NON-TERMINAL, SO WE DELETE AND CONTINUE
                ArrayList<String> next = new ArrayList<String>();
                next.addAll(words);
                next.remove(words.get(0));

                if(words.size() == 0){
                    System.out.println("2");
                        return true;
                    }


                return findRule(rule, next);
                
            }else{//WE DIDNT FIND A TERMINAL
            
                boolean placeholderFound = false;
                if(checkPlaceholders(rule, words.get(0))){
                    placeholderFound = true;
                    
                    ArrayList<String> next = new ArrayList<String>();
                    next.addAll(words);
                    next.remove(words.get(0));

                    if(words.size() == 0){
                        System.out.println("3");

                        return true;
                    }
                    
                    return findRule(rule, next);
                }else if(isInputRule(rule)){

                    System.out.println("isInputRule is true");
                    placeholderFound = true;
                    placeholderValues.add(words.get(0));

                    ArrayList<String> next = new ArrayList<String>();
                    next.addAll(words);
                    next.remove(words.get(0));

                    if(words.size() == 0){
                        System.out.println("4");

                        return true;
                    }
                    
                    return findRule(rule, next);

                }
                
                if(placeholderFound != true){
                    placeholderValues = new ArrayList<String>();//resets any previously set placeholders
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isInputRule(Rule rule){
        for(Skill skill : skills){
            if(rule.nonterminal.equals(skill.getName())){

                Set<String> keySet = skill.getPlaceholders().keySet();//Making the hashMap iterable
                ArrayList<String> keyList = new ArrayList<>(keySet);

                for(String key : keySet){
                    if(skill.getPlaceholders().get(key).get(0).equals("@INPUT")){
                        return true;
                    }
                }

            }
        }

        return false;
    }

    public boolean checkPlaceholders(Rule rule, String wordToCheck){
        for(String placeholder : rule.placeholders){
            Rule currentPlaceholder = getSkillRule(placeholder);

            for (int i = 0; i < currentPlaceholder.expansions.size(); i++) {
                if(currentPlaceholder.expansions.get(i).equalsIgnoreCase(wordToCheck)){
                    placeholderValues.add(wordToCheck);
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

        parser.parse("What is the distance between Maastricht to Dubai?");     
    }
}