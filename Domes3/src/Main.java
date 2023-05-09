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
        String[] arr1 = (String[]) MyUtils.randomElementsOfArray(words1, 50);
        String[] arr2 = (String[]) MyUtils.randomElementsOfArray(words2, 50);

        String[] testCases = (String[]) MyUtils.concatanateArrays(arr1, arr2);



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
