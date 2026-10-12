import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

// NAME: Victor Li
// DATE: 5/12->5/21
// TEACHER: HULETT
// CLASS: CS AP A/ CS II K
/*
    DESCRIPTION: This is all the hamming back-end stuff. It's main method are the encode and decode, which take the file names to be read from and written to as parameters
 */
public class Hamming {
    /*
    This method should read in the input file one byte at a time, create two code blocks (with a leading 0 in each) and write them out to the output file.
     */
    private static final int[][] generator = {{1, 1, 0, 1},{1, 0, 1, 1},{1, 0, 0, 0},{0, 1, 1, 1},{0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};

    private static final int[][] R = {{0, 0, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 0, 0, 1, 0}, {0, 0, 0, 0, 0, 0, 1}};



    public static int[] encodeRow(int[] data){
        // This is easy method
        if(data.length!=4){
            throw new IllegalArgumentException("Data size must be four");
        }
        int[] hammingCode = new int[7];
        // In a (7,4) Hamming code, regular data bits are placed indexes are not powers of 2
        // however, because java idexes start at one, i subtract 1
        hammingCode[2] = data[0];
        hammingCode[4]= data[1];
        hammingCode[5] = data[2];
        hammingCode[6] = data[3];
        /*
            P1 P2 D1 P3 D2 D3 D4
            0  1   2  3  4  5  6
         */
        // Checks d1, d2, and d4
        hammingCode[0] = (data[0]+data[1]+data[3])%2;// AKA parity bit 1 :)
        // Checks d1, d3, and d4
        hammingCode[1] = (data[0]+data[2]+data[3])%2;
        // checks d2, d3, and d4
        hammingCode[3] = (data[1]+data[2]+data[3])%2;
        return hammingCode;
    }
    public static int[] encodeRowUsingMatrices(int[] data){
        // Harder
        if(data.length!=4){
            throw new IllegalArgumentException("Data size must be four");
        }
        int[] encoded = new int[7];
        //https://www.youtube.com/watch?v=v_GmF1xJzGo&t=438s use wikipedia
        for(int r = 0; r< generator.length; r++){
            int sum = 0;
            for(int c = 0; c<generator[r].length; c++){
                sum+=generator[r][c]*data[c];
            }
            encoded[r] = sum%2;
        }

        return encoded;

    }
    public static void encode(String inFileName, String outFileName) throws IOException {
        FileWriter f = new FileWriter(outFileName, false);
        PrintWriter pw = new PrintWriter(f);
        Scanner sc = new Scanner(new File(inFileName));

        while(sc.hasNext()){
            String s = sc.next();

            // Skip tokens that aren't valid 4-bit binary strings (like stray numbers/headers)
            if(s.length() != 4){
                continue;
            }

            int[] data = new int[4];
            for(int i = 0; i < data.length; i++){
                data[i] = Integer.parseInt(s.substring(i, i + 1));
            }

            int[] encodedRow = encodeRowUsingMatrices(data);
            for(int k : encodedRow){
                pw.print(k + "");
            }
            pw.println();
        }
        pw.close();
    }
    public static void decode(String inFileName, String outFileName) throws IOException {
        //https://docs.oracle.com/javase/8/docs/api/java/io/FileWriter.html
        FileWriter f = new FileWriter(outFileName, false);
        // https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html
        PrintWriter pw = new PrintWriter(f);
        Scanner sc = new Scanner(new File(inFileName));

        while(sc.hasNext()){
            String s = sc.next();
            int[] data = new int[7];
            for(int i = 0; i<data.length; i++){
                data[i] = Integer.parseInt(s.substring(i, i+1));
            }
            int[] decodedRow = decodeRowUsingMatrices(data);
            for(int k:decodedRow){
                pw.print(k+"");
            }
            pw.println();
        }
        pw.close();
    }
    public static int[] decodeRowUsingMatrices(int[] data) {
        if(data.length!=7){
            throw new IllegalArgumentException("Data size must be seven for decoding");
        }
        int[] decoded = new int[4];
        data = parityCheck(data);
        for(int r= 0; r<R.length; r++){
            int sum = 0;
            for(int c = 0; c<R[r].length; c++){
                sum+=R[r][c]*data[c];
            }
            decoded[r] = sum%2;
        }
        return decoded;
    }

    /**
     * If is data is correct, parity check just returns the array
     * else it will fix it and then return data
     * @param data
     * @return correct data
     */
    private static int[] parityCheck(int[] data){
        if(data.length!=7){
            throw new IllegalArgumentException("Data must be length 7");
        }
        int[] syndromeVec = new int[3];
        int[][] first = {{1, 0, 1, 0, 1, 0, 1},{0, 1, 1, 0, 0, 1, 1},{0, 0, 0, 1, 1, 1, 1}};
        for(int r = 0; r<first.length; r++){
            int sum = 0;
            for(int c = 0; c<first[r].length; c++){
                sum+=first[r][c]*data[c];
            }
            syndromeVec[r] = sum;
        }
        for(int i =0; i< syndromeVec.length; i++){
            syndromeVec[i]%=2;
        }
        if(!isNullVector(syndromeVec)){
            // System.out.println("Syndrome Vector "+Arrays.toString(syndromeVec));

            int errorIndex = 0;
            if(syndromeVec[0]==1)errorIndex++;
            if(syndromeVec[1]==1)errorIndex+=2;
            if(syndromeVec[2]==1)errorIndex+=4;

            // index based on one
            if(errorIndex>0) {
                data[errorIndex - 1] = 1 - data[errorIndex - 1];
                System.out.println("Bit"+errorIndex+" was corrupted. Error has been corrected");
            }
            else{
                System.out.println("Error in parity bits, cannot correct data bits");
            }

        }

        return data;
    }


    public static void main(String[] args) throws IOException {
        encode("tbe.txt", "ofn.txt");
        decode("ofn.txt","decodedResults.txt");
        runAllTests();
    }
    public static void runAllTests() throws FileNotFoundException {
        runEncodingTest("testEncoding1.txt");
        runDecodingTest("testDecoding1.txt");
        parityCheckTest("parityCheckTest.txt");
    }
    private static void runEncodingTest(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        int size = sc.nextInt();
        sc.nextLine();
        boolean passedAll = true;
        for(int i = 0; i<size; i++){
            String input = sc.next();
            int[] tbe = new int[4];
            for(int j = 0; j <tbe.length; j++){
                tbe[j] = Integer.parseInt(input.substring(j, j+1));
            }
            int[] result1 = encodeRow(tbe);
            int[] result2 = encodeRowUsingMatrices(tbe);
            String expected = sc.next();


            if(!extractString(result1).equals(expected)){
                System.out.println("Failed test case in encodeRow");
                System.out.println("Expected: "+expected+" Result: "+extractString(result1));
                passedAll = false;
            }
            if(!extractString(result2).equals(expected)){
                System.out.println("Failed test case in encodeRowUsing Matrices");
                System.out.println("Expected "+expected+"Result: "+extractString(result2));
                passedAll = false;
            }
        }
        if(passedAll)System.out.println("Passed ALL Encoding Test");
    }
    private static void runDecodingTest(String fileName) throws FileNotFoundException {
        boolean passedAll = true;
        Scanner sc = new Scanner(new File(fileName));
        int size = sc.nextInt();
        sc.nextLine();
        for(int i = 0; i<size; i++){
            String input = sc.next();
            String expected = sc.next();
            int[] data = new int[7];
            for(int j = 0; j<data.length; j++){
                data[j] = Integer.parseInt(input.substring(j, j+1));
            }
            String result = extractString(decodeRowUsingMatrices(data));
            if(!result.equals(expected)){
                System.out.println("Failed Test case in decoding test");
                System.out.println("Expected "+expected+"Result: "+(result));
                passedAll = false;
            }
        }
        if(passedAll) System.out.println("Passed ALL decoding test cases");
    }
    private static void parityCheckTest(String fileName)throws FileNotFoundException{
        boolean passedAll = true;
        Scanner sc = new Scanner(new File(fileName));
        String original = sc.next();
        while(sc.hasNext()){
            String input = sc.next();
            int[] corruptedData = new int[7];
            for(int i = 0; i<corruptedData.length; i++){
                corruptedData[i] = Integer.parseInt(input.substring(i, i+1));
            }
            int[] result = parityCheck(corruptedData);
            String res = extractString(result);

            if(!res.equals(original)){
                System.out.println("Failed test case in parityCheckTest");
                System.out.println("Expected: "+original+" Received:"+res);
                passedAll = false;
            }
        }
        if(passedAll) System.out.println("Passed ALL parity check decoding");
    }
    private static String extractString(int[] data){
        StringBuilder s = new StringBuilder();
        for (int d : data) {
            s.append(d);
        }
        return s.toString();
    }
    private static boolean isNullVector(int[] arr){
        for (int j : arr) {
            if (j != 0) return false;
        }
        return true;
    }
    // used this to check: https://www.dcode.fr/hamming-error-correction

    /*
    Expected Output:
    0110011
    0101010
    0011001
     0111100
     1100110
     0001111
      0101010
      0011001
      0111100
      0110011
      0011001
       1100110
     */
}