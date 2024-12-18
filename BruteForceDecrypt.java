/**

 * Project: Crack DES Team Assignment
 * Course: IST 242
 * Author: Group 3
 * Date Developed: 12/ /24
 * Last Date Changed:12/12/24
 * Citations:
 * https://github.com/joeoakes/javaBruteForceAllChars
 * https://github.com/joeoakes/javaBruteForceDictionary/
 *
 */

// Ayoub start
import java.io.BufferedReader; // For efficient reading of text files
import java.io.FileReader; // To read character files
import java.io.IOException; // To handle input/output exceptions
import java.util.HashSet; // For storing unique dictionary words
import java.util.Set; // To define a collection of unique elements

/**
 * A utility class for decrypting ciphertext using brute-force methods.
 * This class prepares inputs, loads a dictionary for validation, and tests all possible keys up to a specified length.
 *
 //* @param dictionaryFile The file path to a dictionary containing valid words (one word per line).
 */

public class BruteForceDecrypt {

    // prepares the inputs and starts the brute-force operation by loading dictionary into Hashset
    // used for quick lookups and reads the ciphertext from a file into a string
    // It sets a maximum key length for brute force and initiates the decryption process
    // tests all possible keys within the specified length

    // Define the alphabet
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    // Track the number of keys tested during brute force decryption
    private static int totalKeysTested = 0;

    /**
     * Loads a dictionary of valid words from a specified file into a HashSet for fast lookups.
     *
     * @param dictionaryFile The file path to a dictionary containing valid words (one word per line).
     * @return A Set of unique dictionary words, all converted to lowercase for consistency.
     */

    // Load dictionary from file into a set
    public static Set<String> loadDictionary(String dictionaryFile) {
        Set<String> dictionary = new HashSet<>();
        // Open the file and set up efficient reading
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            // Read each line (word) from the file until the end
            while ((word = br.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            // Print an error message if something goes wrong while reading the file
            System.err.println("Error reading the dictionary file: " + e.getMessage());
        }
        // Return the HashSet containing all the loaded dictionary words
        return dictionary;
    }
    //Ayoub end
// Bhav start
// Function: Count valid words in a given text
// Output:
//   - Returns the number of valid words found in the text.

    public static int countValidWords(String text, Set<String> dictionary) {
        // Split the text into words wherever there are non-letter characters (like spaces or punctuation).
        // For example: "Hello, World!" becomes ["Hello", "World"]
        String[] words = text.split("\\W+");

        // Start a counter to keep track of how many valid words we find.
        int validWordCount = 0;

        // Go through each word from the text.
        for (String word : words) {
            // Convert the word to lowercase and check if it is in the dictionary of valid words.
            if (dictionary.contains(word.toLowerCase())) {
                // If the word is valid, increase the counter by 1.
                validWordCount++;
            }
        }

        // Return the total number of valid words found.
        return validWordCount;
    }
// Bhav end

    //aayudh start
    public static void main(String[] args) {
        String cipherFile = "ciphertext.txt"; // File containing the ciphertext
        String dictionaryFile = "dictionary.txt"; // File containing the dictionary
        String ciphertext = "";

        // Load the dictionary
        Set<String> dictionary = loadDictionary(dictionaryFile);

        // Read the ciphertext from the file
        try (BufferedReader br = new BufferedReader(new FileReader(cipherFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            ciphertext = sb.toString();
        } catch (IOException e) {
            System.err.println("Error reading the ciphertext file: " + e.getMessage());
            return;
        }

        // Maximum key length to test
        int maxKeyLength = 8; // Adjust as needed
        System.out.println("Brute-forcing with keys up to length " + maxKeyLength + "...");

        // Perform brute force
        for (int length = 1; length <= maxKeyLength; length++) {
            bruteForce("", length, ciphertext, dictionary);
        }

        // Print total keys tested
        System.out.println("Total keys tested: " + totalKeysTested);
    }
// end
    //Thomas
    /**
     * Tries to break the ciphertext by testing all possible keys up to a certain length.
     * It also checks if the decrypted text makes sense using a dictionary.
     *
     * @param currentKey   The key being built. Start with an empty string ("").
     * @param maxLength    The maximum length the key can be.
     * @param ciphertext   The encrypted message that needs to be unlocked.
     * @param dictionary   A list of valid words to check if the decrypted text is correct.
     */
    private static void bruteForce(String currentKey, int maxLength, String ciphertext, Set<String> dictionary) {
        // Base case: if the key is as long as it should be, test it
        if (currentKey.length() == maxLength) {
            totalKeysTested++; // Increment the count of tested keys

            // Try to decode the ciphertext with the current key
            String decryptedText = decrypt(ciphertext, currentKey);

            // Count the number of valid words in the decoded text
            int validWords = countValidWords(decryptedText, dictionary);

            // Show the current key and its effect on the ciphertext
            System.out.println("Testing Key: " + currentKey);
            System.out.println("Decrypted Text: \n" + decryptedText);
            System.out.println("Words found in dictionary: " + validWords + "\n");

            // If the decoded text has enough valid words, assume the key is correct
            if (validWords > 5) { // Change the threshold as needed
                System.out.println("** Key Found: " + currentKey + " **");
                System.out.println("Decrypted Text with Key: " + currentKey + ":\n" + decryptedText);
                System.out.println("Words found in dictionary: " + validWords);
            }
            return; // Exit this recursive path
        }

        // Recursive case: build the key by adding one more letter at a time
        for (char c : ALPHABET) { // ALPHABET is assumed to be a predefined character array
            bruteForce(currentKey + c, maxLength, ciphertext, dictionary);
        }
    }
    //Thomas
    //aidan start
    // Decrypt the ciphertext using the given key
    private static String decrypt(String ciphertext, String key) {
        StringBuilder decrypted = new StringBuilder();
        int keyIndex = 0;

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shifted = (c - base - (key.charAt(keyIndex % key.length()) - 'a') + 26) % 26;
                decrypted.append((char) (shifted + base));
                keyIndex++;
            } else {
                decrypted.append(c); // Keep non-alphabetic characters unchanged
            }
        }

        return decrypted.toString();
    }
}
//aidan end