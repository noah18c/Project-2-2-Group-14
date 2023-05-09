package DigitalAssistant.skillLogic.CFG;

import java.util.*;

public class CFGParser {

    private List<Rule> grammar;
    
    public CFGParser(List<Rule> grammar) {
        this.grammar = grammar;
    }

    public boolean parse(List<String> sentence) {
        return parseHelper("S", sentence, 0, sentence.size() - 1);
    }

    private boolean parseHelper(String nonterminal, List<String> sentence, int start, int end) {
        if (start > end) {
            return false;
        }

        for (Rule rule : grammar) {
            if (rule.nonterminal.equals(nonterminal)) {
                List<List<String>> expansions = rule.expansions;
                for (List<String> expansion : expansions) {
                    if (expansion.size() == 1) {
                        String symbol = expansion.get(0);
                        if (!isNonterminal(symbol)) {
                            if (symbol.equals(sentence.get(start))) {
                                return true;
                            }
                        } else {
                            if (parseHelper(symbol, sentence, start, end)) {
                                return true;
                            }
                        }
                    } else if (expansion.size() == 2) {
                        String first = expansion.get(0);
                        String second = expansion.get(1);
                        if (isNonterminal(first) && isNonterminal(second)) {
                            for (int i = start; i < end; i++) {
                                if (parseHelper(first, sentence, start, i) && parseHelper(second, sentence, i + 1, end)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    

    private boolean isNonterminal(String symbol) {
        for (Rule rule : grammar) {
            if (rule.nonterminal.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static class Rule {
        private String nonterminal;
        private List<List<String>> expansions;

        public Rule(String nonterminal, List<List<String>> expansions) {
            this.nonterminal = nonterminal;
            this.expansions = expansions;
        }
    }

    public static void main(String[] args) {
        List<CFGParser.Rule> grammar = new ArrayList<>();
        grammar.add(new CFGParser.Rule("<S>", Arrays.asList(Arrays.asList("<ACTION>"))));
        grammar.add(new CFGParser.Rule("<ACTION>", Arrays.asList(Arrays.asList("<LOCATION>"), Arrays.asList("<SCHEDULE>"))));
        grammar.add(new CFGParser.Rule("<SCHEDULE>", Arrays.asList(Arrays.asList("Which", "lectures", "are", "there", "<TIMEEXPRESSION>"), Arrays.asList("<TIMEEXPRESSION>", "which", "lectures"))));
        grammar.add(new CFGParser.Rule("<TIMEEXPRESSION>", Arrays.asList(Arrays.asList("on", "<DAY>", "at", "<TIME>"), Arrays.asList("at", "<TIME>", "on", "<DAY>"))));
        grammar.add(new CFGParser.Rule("<TIME>", Arrays.asList(Arrays.asList("9"), Arrays.asList("12"))));
        grammar.add(new CFGParser.Rule("<LOCATION>", Arrays.asList(Arrays.asList("Where", "is", "<ROOM>"), Arrays.asList("How", "do", "<PRO>", "get", "to", "<ROOM>"), Arrays.asList("Where", "is", "<ROOM>", "located"))));
        grammar.add(new CFGParser.Rule("<PRO>", Arrays.asList(Arrays.asList("I"), Arrays.asList("you"), Arrays.asList("he"), Arrays.asList("she"))));
        grammar.add(new CFGParser.Rule("<ROOM>", Arrays.asList(Arrays.asList("DeepSpace"), Arrays.asList("SpaceBox"))));
        grammar.add(new CFGParser.Rule("<DAY>", Arrays.asList(Arrays.asList("Monday"), Arrays.asList("Tuesday"), Arrays.asList("Wednesday"), Arrays.asList("Thursday"), Arrays.asList("Friday"), Arrays.asList("Saturday"), Arrays.asList("Sunday"))));
        
        CFGParser parser = new CFGParser(grammar);

        List<String> sentence1 = Arrays.asList("Where", "is", "DeepSpace");
        boolean result1 = parser.parse(sentence1);
        System.out.println("Sentence 1: " + sentence1 + " is " + (result1 ? "valid" : "invalid"));

        List<String> sentence2 = Arrays.asList("What", "lectures", "are", "there", "on", "Monday", "at", "9");
        boolean result2 = parser.parse(sentence2);
        System.out.println("Sentence 2: " + sentence2 + " is " + (result2 ? "valid" : "invalid"));

        // List<String> sentence3 = Arrays.asList("How", "do", "I", "get", "to", "SpaceBox");
        // boolean result3 = parser.parse(sentence3);
        // System.out.println("Sentence 3: " + sentence3 + " is " + (result3 ? "valid" : "invalid"));

        // List<String> sentence4 = Arrays.asList("Where", "is", "the", "moon");
        // boolean result4 = parser.parse(sentence4);
        // System.out.println("Sentence 4: " + sentence4 + " is " + (result4 ? "valid" : "invalid"));
    
    }
}

