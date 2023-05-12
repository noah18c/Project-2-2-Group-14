package DigitalAssistant.skillLogic.CFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import DigitalAssistant.skillLogic.Skill;
import DigitalAssistant.skillLogic.SkillEditor;

public class CFG {

    public ArrayList<Rule> grammar;
    public List<Skill> skills;

    public CFG(List<Skill> skills){
        this.skills = skills;
        loadRules();
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
                Rule placeholderRule = new Rule(key, skill.getPlaceholders().get(key));
                rules.add(placeholderRule);
            }
        }
        grammar = rules;
    }

    /**This method processes a string for use in defining rules for the CFG algorithm (for now just used for prototype sentences). 
     * The method takes a string as input and returns an arraylist wherein all words are stripped of unnecessary characters and 
     * placeholder/terminal words.
     * @param str A string of any length.
     * @return An ArrayList<String> containing all processed words.
     */
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

        public Rule(String nonterminal, List<String> expansions) {
            this.nonterminal = nonterminal;
            this.expansions = expansions;
        }
    }

    public static void main(String[] args) {
        SkillEditor skillEditor = new SkillEditor();

        CFG cfg = new CFG(skillEditor.getSkills());
        ArrayList<Rule> grammar = cfg.grammar;
        for(Rule rule : grammar){
            System.out.printf("---------\n"+"Rule %d\nNon-Terminal: %s\n Terminals: %s\n", grammar.indexOf(rule), rule.nonterminal, rule.expansions.toString());
        }
    }
}