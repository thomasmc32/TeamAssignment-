/**

 * Project: Crack DES Team Assignment
 * Course: IST 242
 * Author: Group 3
 * Date Developed: 12/ /24
 * Last Date Changed:12/12/24
 * Citations:
 *
 *
 *
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BruteForceDecrypt {

    // Define the alphabet
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static int totalKeysTested = 0; // Track the number of keys tested
    //ayoub start
    // Load dictionary from file into a set
    public static Set<String> loadDictionary(String dictionaryFile) {
        Set<String> dictionary = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading the dictionary file: " + e.getMessage());
        }
        return dictionary;
    }
//ayoub end

//Bhav start
// Count valid words in a decrypted text
public static int countValidWords(String text, Set<String> dictionary) {
    String[] words = text.split("\\W+"); // Split by non-word characters
    int validWordCount = 0;

    for (String word : words) {
        if (dictionary.contains(word.toLowerCase())) {
            validWordCount++;
        }
    }
    return validWordCount;
}

//Bhav end
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
//aayudh end
    //Thomas
    /**
     * Tries to break the encrypted text (ciphertext) by testing all possible keys
     * up to a certain length. It checks if the decrypted text makes sense using a dictionary.
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