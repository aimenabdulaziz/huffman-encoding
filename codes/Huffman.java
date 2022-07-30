import java.io.*;
import java.util.*;

/**
 * Program that uses Huffman encoding to compress and decompress txt files
 * @author Aimen Abdulaziz, Winter 2022
 */

public class Huffman {
    Map<Character,Integer> characterFrequencyMap;               // character -> frequency
    BinaryTree<CharacterElement> node;
    PriorityQueue<BinaryTree<CharacterElement>> priorityQueue;

    public Huffman() {
        characterFrequencyMap = new HashMap<Character,Integer>();
    }

    /**
     * Creates a character frequency map by reading the file
     * @param pathName location of the file
     * @throws Exception
     */
    public void characterCount(String pathName) throws Exception {
        // open the file
        BufferedReader input = openBufferedReader(pathName);

        // read the file
        try {
            // check if file is not empty and add to map
            int line;
            while ((line = input.read()) != -1){
                char character = (char) line; // cast the unicode encoding to char
                if (!characterFrequencyMap.containsKey(character)){
                    // add char if the frequency map doesn't contain the char already
                    characterFrequencyMap.put(character, 1);
                }
                else{
                    characterFrequencyMap.put(character, characterFrequencyMap.get(character) + 1); // increment the value/frequency
                }
            }
        }
        catch (IOException e) {
            System.out.println("I/O Error\n" + e.getMessage());
        }
        finally {
            closeBufferedReader(input);
        }
    }

    /**
     * Opens the file in the specified pathName and return a BufferedReader
     * @param pathName directory of file
     * @return BufferedReader of the opened file
     */
    public BufferedReader openBufferedReader(String pathName) {
        BufferedReader reader = null;
        // open the file, if possible
        try {
            reader = new BufferedReader(new FileReader(pathName));
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
        }
        return reader;
    }

    /**
     * Closes the BufferedReader passed as a parameter
     * @param reader BufferedReader
     */
    public void closeBufferedReader(BufferedReader reader) {
        // close the file, if possible
        try {
            reader.close();
        }
        catch (IOException e) {
            System.err.println("Cannot close file.\n" + e.getMessage());
        }
    }

    /**
     * Creates the priority queue of trees from the frequency map
     */
    public void createQueue() {
        // handle boundary case when the file is empty
        if (characterFrequencyMap.isEmpty()) {
            return;
        }

        priorityQueue = new PriorityQueue<BinaryTree<CharacterElement>>(characterFrequencyMap.size(), new TreeComparator());
        Set<Character> keySet = characterFrequencyMap.keySet();
        // iterate through each key and add the tree to the priorityQueue
        for (Character key : keySet) {
            CharacterElement characterAndFrequency = new CharacterElement(key, characterFrequencyMap.get(key));
            // create initial single-character trees
            BinaryTree<CharacterElement> initialTree = new BinaryTree<CharacterElement>(characterAndFrequency);
            //add the initial trees to the priority queue
            priorityQueue.add(initialTree);
        }
    }

    /**
     * Combines all the trees in the priority queue into one big tree.
     */
    public void createTree() {
        // handle boundary case when the file is empty
        if (characterFrequencyMap.isEmpty()) {
            return;
        }

        // create two small tree and combine them in a new tree with node freq = tree1.frequency and tree2.frequency
        while (priorityQueue.size() > 1) {
            BinaryTree<CharacterElement> tree1 = priorityQueue.remove();
            BinaryTree<CharacterElement> tree2 = priorityQueue.remove();
            int frequencySum = tree1.getData().getFrequency() + tree2.getData().getFrequency();
            BinaryTree<CharacterElement> mainTree = new BinaryTree<CharacterElement>(new CharacterElement(null, frequencySum), tree1, tree2);
            priorityQueue.add(mainTree);
        }

        // initializes the completed tree
        node = priorityQueue.peek(); // last element in the tree
    }

    /**
     * Gets the code of 1s and 0s for each character
     * @return a map that has the location (in the tree) of each character
     */
    public Map<Character,String> codeRetrieval() {
        // handle boundary case when the file is empty
        if (characterFrequencyMap.isEmpty()) {
            return new HashMap<Character,String>();
        }

        String pathSoFar = "";
        Map<Character,String> characterLocationMap = new HashMap<Character,String>(); // character --> code
        codeRetrievalHelper(node, characterLocationMap, pathSoFar);

        //boundary case when file contains only one letter or one letter repeated several times
        if (characterLocationMap.size() == 1 && !priorityQueue.isEmpty()) { // check priority queue is not empty
            // give it a location in the tree
            characterLocationMap.put(priorityQueue.peek().getData().getCharacter(), "1");
        }

        return characterLocationMap;
    }

    /**
     * Helper function for codeRetrieval that recurse through the tree to find the code for each character
     * @param tree the final tree made in createTree
     * @param pathSoFar the string that holds the path for the character
     */
    public void codeRetrievalHelper(BinaryTree<CharacterElement> tree,
                                           Map<Character,String> characterLocationMap, String pathSoFar) {
        // if the tree has a left child, recurse with that child and add 0 because you are going left
        if (tree.hasLeft()) {
            codeRetrievalHelper(tree.getLeft(), characterLocationMap, pathSoFar + "0");
        }
        // if the tree has a right child, recurse with that child and add 1 because you are going right
        if (tree.hasRight()) {
            codeRetrievalHelper(tree.getRight(), characterLocationMap, pathSoFar + "1");
        }
        // if the node is a leaf, add the char to the map
        if (tree.isLeaf()) {
            characterLocationMap.put(tree.data.getCharacter(), pathSoFar);
        }
    }

    /**
     * Compresses the file
     * @param pathName location of the file
     * @throws IOException
     */
    public void compressor(Map<Character,String> characterLocationMap, String pathName) throws IOException {
        // open the file
        BufferedReader input = null;

        // new file to save the compressed data
        String compressedPathName = pathName.substring(0, pathName.indexOf('.')) + "_compressed.txt";
        BufferedBitWriter bitOutput = null;

        // open the files, if possible
        try {
            input = new BufferedReader(new FileReader(pathName));
            bitOutput = new BufferedBitWriter(compressedPathName);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
        }

        // read the files
        try {
            // handle boundary case when the file is empty
            if (characterFrequencyMap.isEmpty()) {
                return;
            }

            // while the file is not empty
            int line;
            while ((line = input.read()) != -1){
                char character = (char) line; // cast the unicode encoding of the reading to char
                String characterCode = characterLocationMap.get(character);

                // loops through characters and assign true or false values depending on the character
                for (char bit : characterCode.toCharArray()) {
                    if (bit == '0') {
                        bitOutput.writeBit(false);
                    }
                    else if (bit == '1') {
                        bitOutput.writeBit(true);
                    }
                }
            }
        }
        finally {
            // close the file, if possible
            try {
                input.close();
                bitOutput.close();
            }
            catch (IOException e) {
                System.err.println("Cannot close file.\n" + e.getMessage());
            }
        }
    }

    /**
     * Decompresses a file compressed by Huffman
     * @param pathName location of the file
     * @throws IOException r
     */
    public void decompress(String pathName) throws IOException {
        String decompressedPathName = pathName.substring(0, pathName.indexOf('_')) + "_decompressed.txt"; // parse str
        BufferedBitReader bitInput = null;
        BufferedWriter output = null;

        // open the files, if possible
        try {
            output = new BufferedWriter(new FileWriter(decompressedPathName));
            bitInput = new BufferedBitReader(pathName);
        }
        catch (IOException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
        }

        // handle boundary case when the file is empty
        if (characterFrequencyMap.isEmpty()) {
            return;
        }
        BinaryTree<CharacterElement> tempTree = node;
        try{
            while (bitInput.hasNext()) { // while the file is not empty
                boolean bit = bitInput.readBit();
                if (!bit) {
                    if (tempTree.getLeft().isLeaf()){
                        output.write(tempTree.getLeft().getData().getCharacter()); // write character
                        tempTree = node; // reset to decompress another character
                    }
                    else{
                        tempTree = tempTree.getLeft(); // reset tempTree to the left child
                    }
                }
                if (bit) {
                    if (tempTree.hasRight()){
                        if (tempTree.getRight().isLeaf()){
                            output.write(tempTree.getRight().getData().getCharacter()); // write character
                            tempTree = node; // reset to decompress another character
                        }
                        else{
                            tempTree = tempTree.getRight(); // reset tempTree to right child
                        }
                    }
                    // if it is true and doesn't have right, it is a single character
                    else{
                        output.write(tempTree.getData().getCharacter()); // decompress the single character
                    }
                }
            }
        }
        finally {
            // Close the file, if possible
            try {
                output.close();
                bitInput.close();
            }
            catch (IOException e) {
                System.err.println("Cannot close file.\n" + e.getMessage());
            }
        }
    }

    /**
     * Calls all the methods required for compression
     * @param pathName location of the file that needs to be compressed
     * @throws Exception
     */
    public void compress(String pathName) throws Exception {
        characterCount(pathName);
        createQueue();
        createTree();
        Map<Character,String> charCode = codeRetrieval();
        compressor(charCode, pathName);
    }

    /**
     * Tests all the methods we have created so far.
     * Check boundary cases as well.
     */
    public static void main(String[] args) throws Exception {
        Huffman huff = new Huffman();

        // Boundary case 1: empty file
        huff.compress("inputs/EmptyFile.txt");
        huff.decompress("inputs/EmptyFile_compressed.txt");

        // Boundary case 2: single character
        huff.compress("inputs/SingleCharacter.txt");
        huff.decompress("inputs/SingleCharacter_compressed.txt");

        // Boundary case 3: single character repeated a bunch of times
        huff.compress("inputs/SingleCharacterRepeated.txt");
        huff.decompress("inputs/SingleCharacterRepeated_compressed.txt");

        // War And Peace
        huff.compress("inputs/WarAndPeace.txt");
        huff.decompress("inputs/WarAndPeace_compressed.txt");

        // US Constitution
        huff.compress("inputs/USConstitution.txt");
        huff.decompress("inputs/USConstitution_compressed.txt");
    }
}