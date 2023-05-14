import java.util.Scanner;

/**
 * This class is for the command line interface between the user
 * and the B+Tree functions. Only works for the case of String keys.
 * @author Thomas Lagkalis
 */
public class Menu {

    static BpTree tree;

    //Constructors
    public Menu(){
        tree = new BpTree<String, String>();
    }

    public void start(){
        String input;
        int choice = -1;
        boolean exit = false;
        Scanner myScanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Welcome to B+Tree. Type help for information");

        do{
            System.out.print(">>>>");
            input = myScanner.next();
            choice = myMap(input);

            switch (choice) {
                case 1 -> testTheTree("1.txt", "2.txt");
                case 2 -> {
                    if (myScanner.hasNext()) {
                        input = myScanner.next();
                        insertFile(input);
                    }else System.out.println("Missing argument");
                }
                case 3 -> {
                    if (myScanner.hasNext()) {
                        input = myScanner.next();
                        System.out.println(searchWord(input));
                    }else System.out.println("Missing argument");
                }
                case 4 -> {
                    System.out.println("Commands:\n\ttest - Performs 100 searches with random words from 1.txt and 2.txt and prints results.\n");
                    System.out.println("\tinsert <file name> - Inserts all the words of <file name> in the B+Tree.\n");
                    System.out.println("\tsearch <word> - Searches for <word> in the B+Tree and prints the results if exist.\n");
                    System.out.println("\tquit");
                }
                case 5 ->{
                    exit = true;
                }
                case -1 -> System.out.println("Command " + input + " not found");
            }

        }while(!exit);
    }

    /**
     * Maps a string with the corresponding function
     * test --> 1
     * insert --> 2
     * search --> 3
     * help --> 4
     * quit --> 5
     * No match --> -1
     *
     * @param s the input string to map
     * @return the corresponding number of the function or -1 if it's no match.
     */
    private static int myMap(String s){
        if (s.equalsIgnoreCase("test")) return 1;
        else if (s.equalsIgnoreCase("insert")) return 2;
        else if (s.equalsIgnoreCase("search")) return 3;
        else if (s.equalsIgnoreCase("help")) return 4;
        else if (s.equalsIgnoreCase("quit")) return 5;
        else return -1;
    }

    /**
     * This method performs a test for the B+Tree search with 100 test cases
     * (50 from each file). This test includes only the files 1.txt and 2.txt
     * taking 50 random keys (words) from each.
     *
     * @param fileName1 the name of the first file to test
     * @param fileName2 the name of the second file to test
     */
    private static void testTheTree(String fileName1, String fileName2){
        String[] words1 = MyUtils.splitFileToWords(fileName1);
        String[] words2 = MyUtils.splitFileToWords(fileName2);

        //generate 50 existing random words from each file
        Object[] arr1 = MyUtils.randomElementsOfArray(words1, 50);
        Object[] arr2 = MyUtils.randomElementsOfArray(words2, 50);
        Object[] testCases = MyUtils.concatanateArrays(arr1, arr2);

        //Counter 1 --> number of nodes accesses
        //Counter 2 --> number of comparisons
        MultiCounter.resetCounter(1);
        MultiCounter.resetCounter(2);

        assert testCases != null;
        for (Object testCase : testCases) tree.get((String) testCase);

        //Get averages
        float avgAccesses = (float) (MultiCounter.getCount(1)/100.0);
        float avgComparisons = (float) (MultiCounter.getCount(2)/100.0);

        System.out.println("M=20");
        System.out.println("Average Nodes Accesses = "+avgAccesses);
        System.out.println("Average Comparisons = "+avgComparisons);
    }

    /**
     * Finds and returns the string representation of the key's value.
     * @param key the key to search
     * @return the String representation of the key's value
     */
    private static String searchWord(String key){
        try {
            return tree.get(key).toString();
        }catch (Exception e){ return "";}

    }

    /**
     * Inserts the words of a file in the B+Tree
     * @param fileName the name of the file
     */
    private static void insertFile(String fileName){
        // Split the file into String array containing the words of the file.
        String[] words = MyUtils.splitFileToWords(fileName);
        // Insert the words in the tree.
        insertTree(words, fileName, tree);
    }


    /**
     * Inserts the keys in the B+Tree. It is not a generalized function.
     * Works only for this type of application in order to clear up the
     * main method.
     *
     * @param keys array of key to be inserted
     * @param tree the tree in which the keys will be inserted
     */
    private static void insertTree(String[] keys,String file, BpTree<String, LinkedList<String, Integer>> tree){
        if (keys!=null) {
            for (int i = 0; i < keys.length; i++) {
                LinkedList<String, Integer> val = tree.get(keys[i]);
                if (val == null) {
                    tree.put(keys[i], new LinkedList<String, Integer>(file, i + 1));
                } else {
                    val.insert(file, i + 1);
                }
            }
        }
    }
}
