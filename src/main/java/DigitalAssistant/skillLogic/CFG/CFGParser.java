package DigitalAssistant.skillLogic.CFG;

import java.util.*;

import DigitalAssistant.skillLogic.SkillEditor;
import DigitalAssistant.skillLogic.Word;
import DigitalAssistant.skillLogic.Skill;

public class CFGParser {

    public ArrayList<Rule> grammar;
    public List<Skill> skills;
    public ArrayList<String> words;

    public String currentPlaceholderSearch;
    public boolean firstIteration = true;
    public int inputCount = 0;

    public String skillName;
    public HashMap<String, ArrayList<String>> placeholderValues;

    public boolean isFinished = false;

    public CFGParser(List<Skill> skills){
        this.skills = skills;
        placeholderValues = new HashMap<String, ArrayList<String>>();
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
        System.out.println(inputCount);

        return false;
    }

    public boolean findRule(Rule rule, ArrayList<String> words){
        System.out.println(rule.nonterminal + "  " + words.toString());

        if(words.size() == 0){//IF WE DELETED EVERYTHING WE SHOULD BE GOOD AND HAPPY AND RETURN
            System.out.println("1");
            if(isInputRule(rule)){
                if(inputCount == rule.placeholderSkillCount){
                    return true;
                }else{
                    inputCount = 0;
                    return false;
                }
            }else{
                return true;
            }


        }else{
            if(rule.expansions.contains(words.get(0))){//WE FOUND A NON-TERMINAL, SO WE DELETE AND CONTINUE
                ArrayList<String> next = new ArrayList<String>();
                next.addAll(words);
                next.remove(words.get(0));

                if(words.size() == 0){
                    if(isInputRule(rule)){
                        if(inputCount == rule.placeholderSkillCount){
                            return true;
                        }else{
                            inputCount = 0;
                            return false;
                        }
                    }else{
                        return true;
                    }
                }


                return findRule(rule, next);
                
            }else{//WE DIDNT FIND A TERMINAL
                System.out.println("HERE");
                boolean placeholderFound = false;
                if(checkPlaceholders(rule, words.get(0), false)){
                    placeholderFound = true;
                    
                    ArrayList<String> next = new ArrayList<String>();
                    next.addAll(words);
                    next.remove(words.get(0));

                    if(words.size() == 0){
                        if(isInputRule(rule)){
                            if(inputCount == rule.placeholderSkillCount){
                                return true;
                            }else{
                                inputCount = 0;
                                return false;
                            }
                        }else{
                            return true;
                        }
                    }
                    
                    return findRule(rule, next);
                }else if(isInputRule(rule)){

                    System.out.println("isInputRule is true");
                    placeholderFound = true;
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(words.get(0));
                    placeholderValues.put("@INPUT", temp);
                    inputCount++;

                    ArrayList<String> next = new ArrayList<String>();
                    next.addAll(words);
                    next.remove(words.get(0));

                    if(words.size() == 0){
                        System.out.println("4");
                        if(inputCount==rule.placeholderSkillCount){
                            return true;
                        }else{
                            inputCount = 0;
                            return false;
                        }
                        
                    }
                    
                    return findRule(rule, next);

                }
                
                if(placeholderFound != true){
                    placeholderValues = new HashMap<String, ArrayList<String>>();//resets any previously set placeholders
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

    public boolean checkPlaceholders(Rule rule, String wordToCheck , boolean input){


        for(Rule placeholder : rule.placeholders){
            Rule currentPlaceholder = placeholder;
            String placeholderName = placeholder.nonterminal;

            // if(currentPlaceholder.expansions.size() == 1){
            //     if(currentPlaceholder.expansions.get(0).equals("@INPUT")){
            //         if(input){
            //             placeholderValues.get(currentPlaceholder.nonterminal).add(wordToCheck);
            //             return true;
            //         }
            //     }
            // }
            
            if(placeholder.expansions.contains(wordToCheck)){
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(wordToCheck);
                placeholderValues.put(currentPlaceholder.nonterminal, temp);
                return true;

            }else if(placeholderName.charAt(0) == '<' || placeholderName.charAt(placeholderName.length() - 1) == '>'){//recursive call in case of nested placeholders
                System.out.println(getSkillRule(placeholderName));
                if(firstIteration){
                    currentPlaceholderSearch = placeholderName;
                    firstIteration = false;
                }
                if(checkPlaceholders(getSkillRule(placeholderName), wordToCheck, false)){
                    firstIteration = true;
                    //System.out.print("HERE");
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(wordToCheck);
                    placeholderValues.put(currentPlaceholder.nonterminal, temp);
                    return true;
                }
                
            }

            if(!firstIteration){firstIteration = true;}//only occurs if we searched a placeholder and couldnt find anything
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

            ArrayList<Rule> skillRules = new ArrayList<Rule>();



            Set<String> keySet = skill.getPlaceholders().keySet();//Making the hashMap iterable
            ArrayList<String> keyList = new ArrayList<>(keySet);
            int count = 0;
            for(String key : keyList){

                ArrayList<String> placeholderRuleExpansions = new ArrayList<String>();
                for(String expansion : skill.getPlaceholders().get(key)){
                    placeholderRuleExpansions.add(expansion.toLowerCase());
                }

                Rule placeholderRule = new Rule(key, placeholderRuleExpansions);
                rules.add(placeholderRule);
                skillRules.add(placeholderRule);
                count++;

            }
            Rule newRule = new Rule(skill.getName(), processStringCFG(skill.getPrototype()));
            newRule.placeholderSkillCount = count;
            newRule.placeholders.addAll(skillRules);
            rules.add(newRule);
        }
        grammar = rules;
    }

    public static ArrayList<String> processStringCFG(String str){
        str = str.replaceAll("[!" + 
        ".?'\"\\,;:()\\[\\]{}\\\\|@#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters 

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
        private ArrayList<Rule> placeholders;
        private int placeholderSkillCount = 0;

        public Rule(String nonterminal, List<String> expansions) {
            this.nonterminal = nonterminal;
            this.expansions = expansions;
            placeholders = new ArrayList<Rule>();
        }
        @Override
        public String toString(){
            String str = "nonterminal: " + this.nonterminal + "\nexpansions: " + this.expansions.toString();
            return str;
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();
        CFGParser parser = new CFGParser(skillEditor.getSkills());
        ArrayList<Rule> grammar = parser.grammar;

        parser.parse("Which lectures are on Monday at 9?");     
    }
}