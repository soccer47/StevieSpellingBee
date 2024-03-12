import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Stevie K. Halprin
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // calls subCreate method on the user input, starts with an empty string to which
        // all the possible word variations will be added, then added to the "words" ArrayList
        subCreate("", letters);
    }

    // SubCreate creates every possible variation of the user input, adding them all to "words"
    // SubCreate takes in a String (the current variation of the letters)
    // Also takes in the remaining letters from the input that still can be used
    public void subCreate(String s, String remainders) {
        // If all of the letters from the input have been used, return
        if (remainders.isEmpty()){
            return;
        }

        // Create new variations of String s by adding the remaining letters one at a time
        for (int i = 0; i < remainders.length(); i++){
            // Adds each new variation to "words"
            words.add(s + remainders.charAt(i));
            // Then calls subCreate on this new variation
            // Plugs in the other letters from the remainders String as the new remaining letters
            subCreate(s + remainders.charAt(i),
                    remainders.substring(0, i) + remainders.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    // Sorts all of the words by alphabetical order
    public void sort() {
        // Calls mergesort on "words", starts with 0 as the lowest index
        words = mergeSort(0, words.size() - 1);
    }

    // Sorts "words" by alphabetical order with mergeSort sorting method
    public ArrayList<String> mergeSort(int low, int high)  {
        // If the section of "words" inputted is only one element, return that element in a new ArrayList
        // Base case
        if (low == high){
            ArrayList<String> sub = new ArrayList<String>();
            sub.add(words.get(low));
            return sub;
        }

        // Divides "words" in half into two smaller groups
        // Doesn't actually alter "words", just creates two smaller sections
        int med = (low + high) / 2;
        // Calls mergeSort method on two the two smaller groups, determined by int mid/the middle index
        // Recursive step
        ArrayList<String> subs1 = mergeSort(low, med);
        ArrayList<String> subs2 = mergeSort(med + 1, high);

        // returns the two merged subsections (calls merge method)
        return merge(subs1, subs2);
    }

    // Takes in two ArrayLists, then merges them into one, returning the new, sorted ArrayList
    public ArrayList<String> merge(ArrayList<String> subs1, ArrayList<String> subs2) {
        // Creates new empty ArrayList of Strings called merged
        ArrayList<String> merged = new ArrayList<String>();

        // While both ArrayLists still have elements, add the lowest remaining element
        // out of the two to merged
        while (!subs1.isEmpty() && !subs2.isEmpty()) {
            if (subs1.get(0).compareTo(subs2.get(0)) < 0){
                merged.add(subs1.remove(0));
            }
            else {
                merged.add(subs2.remove(0));
            }
        }

        // Then, add any remaining elements of the two ArrayLists to merged
        while (!subs1.isEmpty()){
            merged.add(subs1.remove(0));
        }
        while (!subs2.isEmpty()){
            merged.add(subs2.remove(0));
        }

        // Return the merged ArrayList
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.

    // Checks each of the words in words, makes sure they exist in dictionary.txt
    public void checkWords() {
        // Goes through each word in words, if dictCheck returns false, take the word out of "words"
        for (int i = 0; i < words.size(); i++) {
            if (!dictCheck(words.get(i), 0, DICTIONARY_SIZE - 1)) {
                words.remove(i--);
            }
        }
    }

    // Recursively checks inputted String with the dictionary to see if the word exists
    // Takes in the inputted word, and the remaining section of the dictionary that the word may be in
    public boolean dictCheck(String s, int low, int high){
        // Base case
        // If there are no remaining indexes that the word could be in, the word doesn't exist, return false
        // If there is only one index left, and the word matches the String at that index, return true
        if (low >= high) {
            if (s.equals(DICTIONARY[high]) || s.equals(DICTIONARY[low])) {
                return true;
            }
            return false;
        }

        // calculates the middle index of the remaining section based off of the low and high indexes
        int mid = low + (high - low) / 2;
        // If the word is the same as the middle index, return true
        if (s.equals(DICTIONARY[mid])){
            return true;
        }
        // Recursive step(s)
        // If the word is less than the middle index in alphabetical order, recursively call dictCheck
        // On the section between low and the middle index
        else if (s.compareTo(DICTIONARY[mid]) < 0){
            return dictCheck(s, low, mid - 1);
        }
        // Otherwise (if the word is greater than the middle index in alphabetical order) recursively call dictCheck
        // On the section between the middle index and high
        else {
            return dictCheck(s, mid + 1, high);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
