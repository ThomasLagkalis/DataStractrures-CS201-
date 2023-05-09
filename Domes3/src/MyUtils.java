import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Thomas Lagkalis
 * A class with useful methods.
 */
public class MyUtils {

    /**
     * This method joins together two arrays. The length of the return array
     * is arr1.length + arr2.length.
     *
     * @param arr1 the first array to merge
     * @param arr2 the second array to merge
     * @return a bigger array containing the elements of arr1 and arr2 or null if either of two arrays is null
     */
    public static <T> T[] concatanateArrays(T[] arr1, T[] arr2){
        if (arr1 == null || arr2 == null) return null;
        T[] arrTotal = (T[]) new Object[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, arrTotal, 0, arr1.length);
        System.arraycopy(arr2, 0, arrTotal, arr2.length, arr2.length);

        return (T[]) arrTotal;
    }

    /**
     * Picks randomly elements from an array. A.k.a produce existing keys from an array
     *
     * @param arr The array of which to choose n random elements
     * @param n The number of elements to choose. Must be 0<n<=arr.length
     * @return an array of n elements chosen randomly from arr or null if n is bigger than the sizeof arr
     */
    public static <T> T[] randomElementsOfArray(T[] arr, int n){
        if (n> arr.length) return null;
        else {
            T[] generatedArr = (T[]) new Object[n];
            Random randomGenerator = new Random();
            for (int i=0; i<n; i++) generatedArr[i] = arr[randomGenerator.nextInt(arr.length)];
            return (T[]) generatedArr;
        }
    }

    /**
     *
     * @param fileName a string with the name of the file
     * @return an array of strings with the words in the file
     */
    public static String[] splitToWords(String fileName){
        Vector<String> words = new Vector<String>();
        int coountWords = 0;
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNext()){words.add(sc.next()); coountWords++;}
            return words.toArray(new String[coountWords]);
        } catch (FileNotFoundException e) {throw new RuntimeException(e);}

    }
}
