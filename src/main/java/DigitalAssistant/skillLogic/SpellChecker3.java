package DigitalAssistant.skillLogic;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;

public class SpellChecker3 {
    public static void main(String[] args) {
        try {
            Language language = Languages.getLanguageForShortCode("en-US");
            JLanguageTool langTool = new JLanguageTool(language);
            String text = "Helo world. This is a tst.";

            List<RuleMatch> matches = langTool.check(text);

            for (int i = matches.size() - 1; i >= 0; i--) {
                RuleMatch match = matches.get(i);
                List<String> suggestions = match.getSuggestedReplacements();
                if (suggestions.size() > 0) {
                    if (match.getFromPos() < text.length() && match.getToPos() <= text.length()) {
                        String bestSuggestion = getBestSuggestion(match.getMessage(), suggestions);
                        text = text.substring(0, match.getFromPos()) + bestSuggestion + text.substring(match.getToPos());
                    }
                }
            }

            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getBestSuggestion(String original, List<String> suggestions) {
        LevenshteinDistance ld = new LevenshteinDistance();
        String bestSuggestion = suggestions.get(0);
        int bestDistance = ld.apply(original, bestSuggestion);

        for (String suggestion : suggestions) {
            int distance = ld.apply(original, suggestion);
            if (distance < bestDistance) {
                bestSuggestion = suggestion;
                bestDistance = distance;
            }
        }

        return bestSuggestion;
    }
}

