import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    private static final String FILE1 = "1.txt";
    private static final String FILE2 = "2.txt";
    public static void main(String[] args) {
        BpTree<String, LinkedList<String, Integer>> bpTree = new BpTree<>();

        String[] words1 = MyUtils.splitToWords(FILE1);
        String[] words2 = MyUtils.splitToWords(FILE2);

        //insert the words in the B-Plus-Tree
        insertTree(words1, FILE1, bpTree);
        insertTree(words2, FILE2, bpTree);

        //generate 50 existing random words from each file
        Object[] arr1 = MyUtils.randomElementsOfArray(words1, 50);
        Object[] arr2 = MyUtils.randomElementsOfArray(words2, 50);
        Object[] testCases = MyUtils.concatanateArrays(arr1, arr2);

        //Counter 1 --> number of nodes accesses
        //Counter 2 --> number of comparisons
        MultiCounter.resetCounter(1);
        MultiCounter.resetCounter(2);

        for (int i=0; i<testCases.length; i++) bpTree.get((String) testCases[i]);

        //Get averages
        float avgAccesses = (float) (MultiCounter.getCount(1)/100.0);
        float avgComparisons = (float) (MultiCounter.getCount(2)/100.0);

        System.out.println("M=20");
        System.out.println("Average Accesses = "+avgAccesses);
        System.out.println("Average Comparisons = "+avgComparisons);
    }



    /**
     * Inserts the keys in the B+Tree. It is not a generalized function.
     * Works only for this type of application in order to clear up the
     * main method.
     *
     * @param keys array of key to be inserted
     * @param tree the tree in which the keys will be inserted
     */
    public static void insertTree(String[] keys,String file, BpTree<String, LinkedList<String, Integer>> tree){
        for (int i=0 ; i<keys.length; i++){
            LinkedList<String, Integer> val = tree.get(keys[i]);
            if(val==null){
                tree.put(keys[i], new LinkedList<String, Integer>(file, i+1));
            }else{ val.insert(file, i+1);}
        }
    }

}
