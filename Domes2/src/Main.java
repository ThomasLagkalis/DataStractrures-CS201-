import java.util.Arrays;
import java.util.Random;

public class Main {
    static int[] M = {200, 500, 1000, 10000, 30000, 50000, 70000, 100000};
    static final int N_MAX = 65536;//2^16
    static final int TEST_CASES = 100;
    public static void main(String[] args) {
        System.out.printf("%30s %42s \n\n", "Kd Tree", "PR Quad Tree");
        System.out.printf("%6s \t\t%-20s %-15s|   %-20s %-15s\n", "M", "successions(depth)", "failed(depth)", "successions(depth)", "failed(depth)");
        for (int m: M){
            Coordinates[] keys = generateCoordinates(m);
            Coordinates[] existingKeys = generateExistingCoordinates(TEST_CASES, keys);
            Coordinates[] nonExistingKeys = generateNonExistingCoordinates(TEST_CASES, keys);
            KdTree kdTree = new KdTree();
            PRQuadTree qTree = new PRQuadTree(N_MAX);
            PRQuadTree.Node[] insertions = new PRQuadTree.Node[m];
            for (int i=0; i<m; i++){
                kdTree.insert(keys[i]);
                insertions[i] = qTree.insert(keys[i]);
            }
            int[] depth = new int[4];
            for (int i=0; i<TEST_CASES; i++){
                // Searches for kd Tree.
                KdTree.Node result1 = kdTree.search(existingKeys[i]);
                depth[0] += MultiCounter.getCount(1);      //counter[0] contains the counts for both successes and failures.
                MultiCounter.resetCounter(1);          // Sets counter[0] to zero in order to count the next search.
                KdTree.Node result2 = kdTree.search(nonExistingKeys[i]);
                depth[1] += MultiCounter.getCount(1);
                MultiCounter.resetCounter(1);          // Sets counter[0] to zero in order to count the next search.

                // Searches for Quad Tree.
                PRQuadTree.Node search1 = qTree.search(existingKeys[i]);
                depth[2] += MultiCounter.getCount(2);
                MultiCounter.resetCounter(2);
                PRQuadTree.Node search2 = qTree.search(nonExistingKeys[i]);
                depth[3] += MultiCounter.getCount(2);
                MultiCounter.resetCounter(2);

            }
            System.out.printf("%6d \t\t\t %-18.2f %-12.2f| %12.2f %17.2f \n" , m , depth[0]/(float)TEST_CASES , depth[1]/(float)TEST_CASES , depth[2]/(float)TEST_CASES , depth[3]/(float)TEST_CASES);
            //System.out.println(m+"      "+depth[0]/(float)TEST_CASES + "                 "+ depth[1]/(float)TEST_CASES + m+"      "+depth[2]/(float)TEST_CASES + "                 "+ depth[3]/(float)TEST_CASES );
        }
    }

    public static Coordinates[] generateNonExistingCoordinates(int n, Coordinates[] coordinates){
        /*
         * This method produces (pseudo)random coordinates which don't exist in the program.
         * @param  n number of pseudo random coordinates to produce.
         * @return Coordinates array randomly produced.
         *
         */
        Coordinates[] keys = new Coordinates[n];
        Random randomGenerator = new Random();
        for (int i=0; i<n; i++) {
            int x;
            int y;
            do {
                x = randomGenerator.nextInt(N_MAX);
                y = randomGenerator.nextInt(N_MAX);
            } while (Arrays.asList(coordinates).contains(x) && Arrays.asList(coordinates).contains(y));

            keys[i] = new Coordinates(x, y);
        }
        return keys;
    }
    public static Coordinates[] generateExistingCoordinates(int n, Coordinates[] coordinates){
        /*
         * @param n number of coordinates to return.
         * @param coordinates generate random coordinates existing in coordinates[] array.
         * @return the random array of existing coordinates.
         * This method picks (pseudo)randomly coordinates from coordinates[] array.
         */
        Coordinates[] keys = new Coordinates[n];
        Random randomGenerator = new Random();
        for (int i=0; i<n; i++) {;
            keys[i] = coordinates[randomGenerator.nextInt(coordinates.length-1)];
        }
        return keys;
    }
    public static Coordinates[] generateCoordinates(int n) {
        /*
         * @param n (number) of random instances to generate.
         * @return Coordinates array with random values.
         */
        Coordinates[] pairs = new Coordinates[n];
        Random randomGenerator = new Random();

        for (int i = 0; i < n; i++) {
            int x;
            int y;
            do {
                x = randomGenerator.nextInt(N_MAX);
                y = randomGenerator.nextInt(N_MAX);
            } while (Arrays.asList(pairs).contains(x) && Arrays.asList(pairs).contains(y));

            pairs[i] = new Coordinates(x, y);
        }
        return pairs;
    }

    }
