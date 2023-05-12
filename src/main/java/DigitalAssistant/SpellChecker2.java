//package DigitalAssistant;
//
//import com.swabunga.spell.engine.SpellDictionary;
//import com.swabunga.spell.engine.SpellDictionaryHashMap;
//import com.swabunga.spell.event.StringWordTokenizer;
//
//import java.io.File;
//import java.util.List;
//
//public class SpellChecker2 {
//
//    private static SpellDictionaryHashMap dictionary = null;
//    private static com.swabunga.spell.event.SpellChecker spellChecker = null;
//
//    public static void main(String[] args) {
//        try {
//            // The dictionary can be any text file containing list of words.
//            // English dictionary file can be downloaded from jazzy's sourceforge site.
//            File dict = new File("C:\\Users\\Simeon Gunchev\\Documents\\Project 2-2 !!!!\\Project-2-2-Group-14\\src\\main\\java\\DigitalAssistant\\SpellChecker\\english.txt");
//            dictionary = new SpellDictionaryHashMap(dict);
//            spellChecker = new com.swabunga.spell.event.SpellChecker((SpellDictionary) dictionary);
//
//            String input = "Helo world";
//            System.out.println("Corrected: " + correctSentence(input));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String correctSentence(String input) {
//        StringBuilder sb = new StringBuilder();
//        StringWordTokenizer tokenizer = new StringWordTokenizer(input);
//
//        while (tokenizer.hasNext()) {
//            String word = (String) tokenizer.next();
//            if (!spellChecker.isCorrect(word)) {
//                // Get all the possible suggestions
//                List suggestions = spellChecker.getSuggestions(word, 0);
//                if (suggestions.size() > 0) {
//                    // Replace the word with the first suggestion
//                    word = suggestions.get(0).toString();
//                }
//            }
//            sb.append(word).append(" ");
//        }
//
//        return sb.toString().trim();
//    }
//}

//} // end class SpellChecker