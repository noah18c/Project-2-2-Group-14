package DigitalAssistant.skillLogic;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public class SpellChecker {
    private static final String DICTIONARY_PATH = "combined.txt";
    private List<String> dictionary;
    
    public SpellChecker() throws IOException {
        this.dictionary = loadDictionary(DICTIONARY_PATH);
    }

    // THAT MAIN METHOD CREATES THE COMBINED.TXT FILE WHICH IS THE COMBINATION OF DICTIONARY FILES

    // public static void main(String[] args) {
    //     String outputFileName = "combined.txt"; // Name of the output file
    //     ArrayList<String> fileNames = new ArrayList<>();
    //     for(char c = 'A'; c <= 'Z'; c++) {
    //         fileNames.add("/Users/user/Downloads/EOWL-v1.1.2/LF Delimited Format/" + c + " Words.txt");
    //     }
        
    //     try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
    //         for (String fileName : fileNames) {
    //             File file = new File(fileName);
    //             BufferedReader reader = new BufferedReader(new FileReader(file));
    //             String line = reader.readLine();
    //             while (line != null) {
    //                 writer.println(line);
    //                 line = reader.readLine();
    //             }
    //             reader.close();
    //         }
    //         System.out.println("Files combined successfully!");
    //     } catch (IOException e) {
    //         System.out.println("Error occurred: " + e.getMessage());
    //     }
    // }

    public static void main(String[] args) throws IOException {
        String input = "What is thhe distnce between <City> to <City>?";
        String[] words = input.split("\\s+");
        SpellChecker spellChecker = new SpellChecker();
        for (String word : words) {
            if (!spellChecker.check(word)) {
                System.out.println("Misspelled word: " + word + ", suggested correction: " + spellChecker.suggest(word));
            }
        }
        // //Correct the spelling mistakes by checking dictionary
        // try{
        //     SpellChecker spellChecker = new SpellChecker();
        //     String regex = input.substring(0, input.length() - 1) + " " + input.charAt(input.length() - 1); // To spearate last char from the sentence for the case there is "?" at the end of the sentence
        //     String[] words = regex.split(" ");//assign every word to array

        //     for (int i = 0; i < words.length ; i++) {
        //         if (!spellChecker.check(words[i])) {
        //             if(!(words[i].length() == 1 && !Character.isLetter(words[i].charAt(0)) && !Character.isWhitespace(words[i].charAt(0)))){ // punctuation checker
        //                 words[i] = spellChecker.suggest(words[i]);
        //             } 
        //         }
        //     }
        //     String asd = "";
        //     for (int i = 0; i < words.length; i++) {
        //         asd += words[i] + " ";
        //     }
        //     System.out.println(asd);
        
        // }catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
    
    private List<String> loadDictionary(String path) throws IOException {
        Path dictionaryPath = Paths.get(path);
        Charset charset = StandardCharsets.UTF_8;
        List<String> lines = Files.readAllLines(dictionaryPath, charset);
        return lines.stream().map(line -> line.trim().toLowerCase()).collect(Collectors.toList());
    }
    
    public boolean check(String word) {
        return dictionary.contains(word.toLowerCase());
    }
    
    public String suggest(String word) {
        List<String> suggestions = new ArrayList<>();
        double smallestDistance = 100;
        for (String candidate : dictionary) {
            double distance = levenshteinDistance(word, candidate);
            if (distance <= 2) {
                if(distance <= smallestDistance){
                    smallestDistance = distance;
                    suggestions.add(0, candidate);
                }
                else{
                    suggestions.add(candidate);
                }
            }
        }
        
        return suggestions.isEmpty() ? word : suggestions.get(0);
    }
    
    public static double levenshteinDistance(String s, String t) {
        int[][] distance = new int[s.length() + 1][t.length() + 1];
     
        for (int i = 0; i <= s.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= t.length(); j++) {
            distance[0][j] = j;
        }
     
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 1; j <= t.length(); j++) {
                if (s.charAt(i-1) == t.charAt(j-1)) {
                    distance[i][j] = distance[i-1][j-1];
                } else {
                    distance[i][j] = Math.min(Math.min(
                        distance[i-1][j] + 1,
                        distance[i][j-1] + 1),
                        distance[i-1][j-1] + 1);
                }
            }
        }
     
        return distance[s.length()][t.length()];
    }
}