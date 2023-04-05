import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class Main {
    static final String FILE1 = "file1.bin",  FILE2 = "file2.bin",  FILE3 = "file3.bin";
    static final int PAGESIZE = 256; //Each disk page contains 256 bytes.



    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        int[] capacityOfRecords = {59, 31}; //Capacity of records (4bytes the key and the rest for the data).
        int[] N = {50, 100, 200, 500, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; //Number of records for each test case.

        //This loop is for each number of the capacity of records (59 and 31 bytes).
        for (int m=0; m<2; m++){
            System.out.println("Bytes: " + capacityOfRecords[m]);
            System.out.println("N          Method A                 Method B                Method C");
            System.out.println("    Disk Accesses|time(ns)   Disk Accesses|time(ns)     Disk Accesses|time(ns)");
            System.out.println();
            //This loop is for each number of records (N).
            for (int n : N) {
                DataClass[] record = new DataClass[n];
                KeyPage[] keyPagePair = new KeyPage[n];
                KeyPage[] sortedKeyPagePair = new KeyPage[n];
                byte[] bytesArray1 = new byte[n * capacityOfRecords[m]];
                byte[] bytesArray2 = new byte[n * 8];
                byte[] bytesArray3 = new byte[n * 8];
                Random randomGenerator = new Random();
                int[] randomKeys = randomGenerator.ints(1, 2 * n ).distinct().limit(n).toArray();

                //Creating the array of DataClass and KeyPage instances and converting it to byte arrays.
                for (int i = 0; i < n; i++) {
                    Files.deleteIfExists(Paths.get(FILE1));
                    Files.deleteIfExists(Paths.get(FILE2));
                    Files.deleteIfExists(Paths.get(FILE3));
                    record[i] = new DataClass(randomKeys[i], getAlphaNumericString(capacityOfRecords[m] - 4));
                    keyPagePair[i] = new KeyPage(record[i].getKey(), i/(PAGESIZE/capacityOfRecords[m])); // The index of the page: i/(PAGESIZE/capacityOfRecords).
                    sortedKeyPagePair[i] = new KeyPage(record[i].getKey(), i/(PAGESIZE/capacityOfRecords[m]));
                    byte[] bb1 = record[i].toByteArray(capacityOfRecords[m]);
                    byte[] bb2 = keyPagePair[i].toByteArray();
                    System.arraycopy(bb1, 0, bytesArray1, i * capacityOfRecords[m], bb1.length);
                    System.arraycopy(bb2, 0, bytesArray2, i * 8, bb2.length);
                }
                Arrays.sort(sortedKeyPagePair);
                for (int i=0; i<n; i++) System.arraycopy(sortedKeyPagePair[i].toByteArray(), 0, bytesArray3, i*8, 8);
                DataPage[] pages = toDiskPageRecord(capacityOfRecords[m], n, bytesArray1);
                DataPage[] pagesKeys = toDiskPageRecord(8, n, bytesArray2);
                DataPage[] pagesKeysSorted = toDiskPageRecord(8, n, bytesArray3);
                int accesses1 = writeToDisk(pages, FILE1);
                int accesses2 = writeToDisk(pagesKeys, FILE2);
                int accesses3 = writeToDisk(pagesKeysSorted, FILE3);

                int sumOfDiskAccessesA=0, sumOfDiskAccessesB = 0, sumOfDiskAccessesC = 0 ; // sum of disk accesses for each test case and for each method(A,B,C).
                long sumOfTimeA=0, sumOfTimeB=0, sumOfTimeC=0;
                //The following block of code is for 1000 searches (test cases) in the disk file.
                int timesFoundKeyA=0, timesFoundKeyB=0, timesFoundKeyC=0;
                for(int i=0; i<1000; i++){
                    Random random = new Random();
                    int randKey = random.nextInt(2*n) +1;// +1 because nextInt returns in range [0,bound).
                    int diskAccessesA = 0, diskAccessesB = 0, diskAccessesC = 0;
                    boolean foundA = false, foundB=false, foundC=false;
                    //Block of code for searches with method A.
                    long startTime = System.nanoTime();
                    while(diskAccessesA<=accesses1 && !foundA){
                        try {

                            byte[] byteArray = readFromDisk(FILE1, diskAccessesA);
                            diskAccessesA++;
                            DataPage page = new DataPage(byteArray);
                            DataClass[] data = pageToDataClass(page, capacityOfRecords[m]);
                            for (DataClass datum : data) {
                                if (datum.getKey() == randKey) {
                                    timesFoundKeyA++;
                                    foundA = true;
                                    break;
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    long endTime = System.nanoTime();
                    long timeA = endTime-startTime;

                    //Block of code for searches with method B.
                    startTime = System.nanoTime();
                    while(diskAccessesB<=accesses2 && !foundB){
                        try {
                            byte[] byteArray = readFromDisk(FILE2, diskAccessesB);
                            diskAccessesB++;
                            DataPage page = new DataPage(byteArray);
                            KeyPage[] pairs = pageToKeyPage(page);
                            for(KeyPage pair : pairs){
                                if(pair.getKey() == randKey){
                                    byte[] b = readFromDisk(FILE1, pair.getPageIndex());
                                    DataPage p  = new DataPage(b);
                                    DataClass[] data = pageToDataClass(p, capacityOfRecords[m]);
                                    for (DataClass datum : data) {
                                        if (datum.getKey() == randKey) {
                                            timesFoundKeyB++;
                                            foundB = true;
                                            break;
                                        }
                                    }
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    endTime = System.nanoTime();
                    long timeB = endTime-startTime;

                    //Block of code for searches with method C.
                    startTime = System.nanoTime();
                    int[] result = binarySearchFile(FILE3, 0, accesses3, randKey, diskAccessesC);
                    if (result[0] != -1) {
                        byte[] b = readFromDisk(FILE1, result[0]);
                        DataPage p  = new DataPage(b);
                        DataClass[] data = pageToDataClass(p, capacityOfRecords[m]);
                        for (DataClass datum : data) {
                            if (datum.getKey() == randKey) {
                                timesFoundKeyC++;
                                foundC = true;
                                break;
                            }
                        }
                    }
                    endTime = System.nanoTime();
                    long timeC = endTime-startTime;

                    sumOfDiskAccessesA += diskAccessesA;
                    sumOfTimeA += timeA;
                    sumOfDiskAccessesB += diskAccessesB;
                    sumOfTimeB += timeB;
                    sumOfDiskAccessesC += result[1];
                    sumOfTimeC += timeC;

                }
                if (accesses1 == -1 || accesses2 == -1 || accesses3 ==-1) {
                    System.out.println("Something went wrong writing file to disk!");
                } else {
                    System.out.printf("%-5d%-5s %.2f|%-17.2f %.2f|%-17.2f %.2f|%-17.2f \n", n,":", (float)sumOfDiskAccessesA/1000.0, (float)sumOfTimeA/1000.0, (float)sumOfDiskAccessesB/1000.0, (float)sumOfTimeB/1000.0, (float)sumOfDiskAccessesC/1000.0, (float)sumOfTimeC/1000.0);

                }

            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Total Time: " + (end - start) + "ms");

    }

    static private DataClass[] pageToDataClass(DataPage page, int capacityOfRecords){
        /**
         * Takes parameters a DataPage instance and the capacity of records. Returns a DataClass array with the data of the page.
         */
        int numberOfRecords;
        if(page.getDataSize()% capacityOfRecords == 0){numberOfRecords  = page.getDataSize()/capacityOfRecords;}
        else {numberOfRecords = page.getDataSize()/capacityOfRecords ;}
        DataClass[] dataRecord = new DataClass[numberOfRecords];
        byte[] bb = page.getData();
        for (int i=0; i<numberOfRecords; i++){
            dataRecord[i] = toData(Arrays.copyOfRange(bb, i*capacityOfRecords, i*capacityOfRecords + capacityOfRecords));
        }
        return dataRecord;
    }

    static private KeyPage[] pageToKeyPage(DataPage page){
        /*Takes parameters a DataPage instance and the capacity of records. Returns a KeyPage array
         * with the data of the page. */
        int numberOfRecords;
        if(page.getDataSize()% 8 == 0){numberOfRecords  = page.getDataSize()/8;}
        else {numberOfRecords = page.getDataSize()/8 + 1;}
        KeyPage[] dataRecord = new KeyPage[numberOfRecords];
        byte[] bb = page.getData();

        for(int i=0; i<numberOfRecords; i++){
            dataRecord[i] = toKeyPagePair(Arrays.copyOfRange(bb, i*8, i*8+8));
        }
        return dataRecord;
    }

    private static DataClass toData(byte[] buffer){
        /*This method takes parameter the buffer (as read from disk) which contains the data (only
        for a record) and converts it to a DataClass instance.*/
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        int someInt = bb.getInt();
        byte[] byteArray = new byte[buffer.length -4];
        bb.get(byteArray); //Transfers bytes from bb to byteArray (starting from current position of bb).
        String someString = new String(byteArray, java.nio.charset.StandardCharsets.US_ASCII);
        return new DataClass(someInt, someString);
    }

    private static KeyPage toKeyPagePair(byte[] buffer){
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        int key =bb.getInt();
        int pageIndex = bb.getInt();
        return new KeyPage(key, pageIndex);
    }

    static private byte[] readFromDisk(String fileName, int position){
        /*Takes parameters the file name and the position (number/index of page in disk).
         * Returns a byte array of the disk page.*/
        try{
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            raf.seek((long) position *PAGESIZE);
            byte[] bytes = new byte[PAGESIZE];
            raf.read(bytes);
            raf.close();
            return bytes;
        }catch (Exception e){
            e.printStackTrace();
            return new byte[PAGESIZE]; // if exception occurred return an all 0 array.
        }
    }
    static private int writeToDisk(DataPage[] record, String file){
        /*(over)Writes the DataPage instances to disk. Returns the number of disk accesses
         * or -1 if exception occurred.*/
        try
        {
            for (DataPage page : record) {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                raf.seek(raf.length());
                if (page != null) {
                    raf.write(page.getData());
                }
                raf.close();
            }
            return record.length;
        }catch (Exception e) {
            e.printStackTrace();
            return -1; }
    }

    static private DataPage[] toDiskPageRecord(int capacityOfRecords, int numberOfRecords, byte[] data){
        /*Takes parameter the byte array of data and return DataPage array (each instance contains
         * a byte array with its size equal to pageSize)*/
        int remaining = Main.PAGESIZE , numOfPages, k=0;
        byte[] newData = new byte[Main.PAGESIZE];
        numOfPages = numberOfRecords/(Main.PAGESIZE /capacityOfRecords) + 1;
        DataPage[] pages = new DataPage[numOfPages];
        for (int i=0; i< data.length; i++)
        {
            newData[i% (Main.PAGESIZE - Main.PAGESIZE %capacityOfRecords)] = data[i];
            remaining--;
            if (remaining==Main.PAGESIZE %capacityOfRecords)
            {
                remaining = Main.PAGESIZE;
                byte[] b;
                b = Arrays.copyOfRange(newData, 0, newData.length);
                pages[k++]= new DataPage(b);
            }
        }
        byte[] b;
        b = Arrays.copyOfRange(newData, 0, newData.length);
        pages[k]= new DataPage(b);
        return pages;
    }

    private static String getAlphaNumericString(int n){
        /*Generates random string of size n.*/

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private static int[] binarySearchFile(String file, int l, int r, int x, int diskAccesses){
        /* (binary) Search for x through a file (containing Key-Index of page pairs). returns an int array of 2 integers:
        * int[0] = index of page (or -1 if not found)
        * int[1] = amount of disk accesses*/
        int[] arr = {-1, diskAccesses};
        if (r<l) return arr;
        else {
            int mid = (r + l) / 2;

            DataPage page = new DataPage(readFromDisk(file, mid));
            diskAccesses++;
            KeyPage[] pairs = pageToKeyPage(page);
            for (KeyPage pair : pairs){
                if (pair.getKey() == x){
                    arr[0] = pair.getPageIndex();
                    return arr;
                }
            }
            if (x<pairs[0].getKey()) return binarySearchFile(file, l, mid-1, x, diskAccesses);
            else return binarySearchFile(file, mid+1, r, x, diskAccesses);


        }

    }

}