package DigitalAssistant.skillLogic.CFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import DigitalAssistant.skillLogic.Skill;
import DigitalAssistant.skillLogic.SkillEditor;

public class CFG {

    public ArrayList<Rule> grammar;
    public List<Skill> skills;
    public HashMap<String,ArrayList<String>> parsedValues;
    public String skillName;

    public CFG(List<Skill> skills){
        this.skills = skills;
        loadRules();
        parsedValues = new HashMap<>();
    }

    public void start(String sentence) {

        sentence = sentence.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters
        
        for (int i = 0; i < grammar.get(0).nonterminal.size(); i++) {

            String str = parse(getRule(grammar.get(0).nonterminal.get(i)), sentence);

            if(str.length() == 0){
                skillName = grammar.get(0).nonterminal.get(i);
                break;
            }
            else{
                parsedValues = new HashMap<>();
            }
        }

    }
    
    public String parse(Rule rule, String sentence) {

        sentence = extract(rule.terminal, sentence, rule.ruleName);

        for (int i = 0; i < rule.nonterminal.size(); i++) {
            sentence = parse(getRule(rule.nonterminal.get(i)), sentence);
        }

        return sentence;
    }

    public String extract(ArrayList<String> terminals , String sentence, String ruleName){

        List<String> strList = Arrays.asList(sentence.toLowerCase().split(" "));
        ArrayList<String> words = new ArrayList<>();
        words.addAll(strList);
        
        ArrayList<String> toRemove = new ArrayList<>();

        for (String word : words) {
            if(terminals.contains(word)){
                toRemove.add(word);
                terminals.remove(word);
            }
        }

        parsedValues.put(ruleName, toRemove);
        words.removeAll(toRemove);

        String str = words.toString();

        str = str.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "") ;

        return str;
    }

    // public boolean isInputRule(Rule rule){
    //     for (int i = 0; i < rule.terminal.size(); i++) {
    //         if(rule.terminal.get(i).equalsIgnoreCase("@input")){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // public int numberOfInput(Rule rule, int number){
    //     if(isInputRule(rule)){
    //         return number++;
    //     }
    //     for (int i = 0; i < rule.nonterminal.size(); i++) {
    //         number+=  numberOfInput(getRule(rule.nonterminal.get(i)), number);
    //     }
    //     return number;
    // }
    
    public Rule getRule(String name){
        for (int i = 0; i < grammar.size(); i++) {
            if(grammar.get(i).ruleName.equalsIgnoreCase(name)){
                return grammar.get(i);
            }
        }
        return null;
    }

    public void loadRules(){
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Rule startRule = new Rule("<S>", new ArrayList<String>() , new ArrayList<String>());
        rules.add(startRule);

        for(Skill skill : skills){
            startRule.nonterminal.add(skill.getName()); //Add the skill name to the initial rule

            Rule newRule = new Rule(skill.getName(), findTerminals(skill.getPrototype()), findNonTerminals(skill.getPrototype())); // It is for the rules of protoype sentence
            rules.add(newRule);

            HashMap<String,ArrayList<String>> placeholders = skill.getPlaceholders();
            ArrayList<String> keySet = new ArrayList<>(placeholders.keySet());

            for (int i = 0; i < keySet.size(); i++) {
                String name = keySet.get(i);

                ArrayList<String> Values = placeholders.get(keySet.get(i));

                Rule slotRule = new Rule(name, findTerminals(Values.toString()), findNonTerminals(Values.toString()));

                boolean alreadyAdded = false;

                for (int j = 0; j < rules.size(); j++) {
                    if(rules.get(j).checkEqualRule(slotRule)){
                        alreadyAdded = true;
                    }
                }
                if(!alreadyAdded){
                    rules.add(slotRule);
                }
            }
        }

        grammar = rules;
    }

    public ArrayList<String> findTerminals(String str){
        str = str.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters 

        List<String> strList = Arrays.asList(str.toLowerCase().split(" "));
        ArrayList<String> words = new ArrayList<>();
        words.addAll(strList);
        
        ArrayList<String> toRemove = new ArrayList<>();
        for (String word : words) {
            if(!(word.length() == 0)){
                if ((word.charAt(0) == '<' || word.charAt(word.length() - 1) == '>')) {
                    toRemove.add(word);
                }
            }
        }
        words.removeAll(toRemove);
        return words;
    }

    public  ArrayList<String> findNonTerminals(String str){
        str = str.replaceAll("[!" + 
        ".?'\"\\-,;:()\\[\\]{}\\\\|#$%^&*_+=~`]", "");//Regex replaces all non-alphabetic characters 

        List<String> strList = Arrays.asList(str.toLowerCase().split(" "));
        ArrayList<String> words = new ArrayList<>();
        words.addAll(strList);
        
        ArrayList<String> nonTerminals = new ArrayList<>();
        
        for (String word : words) {
            if ((word.charAt(0) == '<' || word.charAt(word.length() - 1) == '>') ) {
                if(!nonTerminals.contains(word)){
                    nonTerminals.add(word);
                }
            }
        }

        return nonTerminals;
    }

    public class Rule {
        public ArrayList<String> terminal;
        public String ruleName;
        public ArrayList<String> nonterminal;

        public Rule(String ruleName, ArrayList<String> terminal , ArrayList<String> nonterminal) {
            this.nonterminal = nonterminal;
            this.terminal = terminal;
            this.ruleName = ruleName;
        }

        public boolean checkEqualRule(Rule rule){
            if(this.ruleName.equalsIgnoreCase(rule.ruleName)){
                if(this.terminal.equals(rule.terminal)){
                    if(this.nonterminal.equals(rule.nonterminal)){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();

        CFG cfg = new CFG(skillEditor.getSkills());
        ArrayList<Rule> grammar = cfg.grammar;

        cfg.start("Mymother is in NewYork today. What is the weather?");

        for(Rule rule : grammar){
            System.out.println(grammar.indexOf(rule)+ " " +  rule.ruleName + " " +  rule.terminal.toString() + " " + rule.nonterminal.toString());
        }

        System.out.println(cfg.parsedValues.toString());
    }
}