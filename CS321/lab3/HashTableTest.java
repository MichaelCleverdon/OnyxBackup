import java.io.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.exit;
import static java.lang.System.load;

public class HashTableTest {

    private static int inputType, debugLevel;
    private static double loadFactor;
    public static void main(String[] args) {
        //Parse Commands
        //1 for java.util.Random, 2 for System.currentTimeMillis(), 3 for word-list file
        inputType = Integer.parseInt(args[0]);
        if(inputType < 1 || inputType > 3 ){
            usage();
        }
        loadFactor = Double.parseDouble(args[1]);
        if(loadFactor > 1 || loadFactor <= 0){
            usage();
        }

        //if they gave a debug level
        if(args.length > 2){
            debugLevel = Integer.parseInt(args[2]);
        }
        else{
            debugLevel = 0;
        }

        PrimeCalculator primeCalculator = new PrimeCalculator();
        int tableSize = primeCalculator.calculatePrime();

        BufferedReader br = null;
        switch(inputType){
            case 1:
                System.out.println("Data source type: java.util.Random");
                System.out.println();
                break;
            case 2:
                System.out.println("Data source type: System.currentTimeMillis()");
                System.out.println();
                break;
            case 3:
                System.out.println("Data source type: word-list");
                System.out.println();
                try {
                    br = new BufferedReader(new FileReader("./word-list"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        Object newObject;
        double currentLoadFactor = 0.0, currentLoop = 0;
        HashTable hashTableDouble = new HashTable(tableSize, loadFactor);
        HashTable hashTableLinear = new HashTable(tableSize, loadFactor);
        Double randomNumber;
        Long currentTime;
        while(currentLoadFactor < loadFactor) {
            switch(inputType){
                case 1:
                    randomNumber = Math.random()*Integer.MAX_VALUE;
                    hashTableLinear.insertLinear(randomNumber);
                    hashTableDouble.insertDouble(randomNumber);
                    break;
                case 2:
                    currentTime = currentTimeMillis();
                    hashTableLinear.insertLinear(currentTime);
                    hashTableDouble.insertDouble(currentTime);
                    break;
                case 3:
                    String word = "";
                    try {
                        word = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hashTableLinear.insertLinear(word);
                    hashTableDouble.insertDouble(word);
                    break;
            }
            currentLoadFactor = (double)(hashTableLinear.size)/tableSize;
        }


        int totalItemsLinear = 0, duplicatesLinear = 0, probesLinear = 0;
        int totalItemsDouble = 0, duplicatesDouble = 0, probesDouble = 0;
        float avgProbesLinear, avgProbesDouble;
        for(int i = 0; i < tableSize; i++){
            if(hashTableLinear.hashTable[i] != null){
                totalItemsLinear++;
                duplicatesLinear += hashTableLinear.hashTable[i].duplicateCount;
                probesLinear += hashTableLinear.hashTable[i].probeCount;
            }
            if(hashTableDouble.hashTable[i] != null){
                totalItemsDouble++;
                duplicatesDouble += hashTableDouble.hashTable[i].duplicateCount;
                probesDouble += hashTableDouble.hashTable[i].probeCount;
            }
        }
        avgProbesLinear = (float)probesLinear/(float)totalItemsLinear;
        avgProbesDouble = (float)probesDouble/(float)totalItemsDouble;

        System.out.println("Using Linear Hashing...");
        System.out.println(String.format("Input %d elements, of which %d were duplicates\nload factor = %f, Avg. no. of probes: %f\n", totalItemsLinear+duplicatesLinear, duplicatesLinear, loadFactor, avgProbesLinear));

        System.out.println("Using Double Hashing...");
        System.out.println(String.format("Input %d elements, of which %d were duplicates\nload factor = %f, Avg. no. of probes: %f\n", totalItemsDouble+duplicatesDouble, duplicatesDouble, loadFactor, avgProbesDouble));


        if(debugLevel == 1){
            try {
                //Make files just in case they're not made yet
                File linearFile = new File("./linear-dump");
                File doubleFile = new File("./double-dump");
                linearFile.createNewFile();
                doubleFile.createNewFile();

                for (int i = 0; i < tableSize; i++) {
                    if (hashTableLinear.hashTable[i] != null){
                        dumpLinearDebug(hashTableLinear.hashTable[i], i);
                    }
                    if(hashTableDouble.hashTable[i] != null){
                        dumpDoubleDebug(hashTableDouble.hashTable[i], i);
                    }
                }
            } catch(IOException ex){
                System.out.println("An error occurred");
                ex.printStackTrace();
            }
        }


    }

    private static void usage(){
        System.out.println("java HashTest <input type> <load factor> [<debug level>]");
        System.out.println("input type should be 1, 2, 3");
        System.out.println("load factor should be a number between 0 and 1");
        System.out.println("debug level should be a 0 or a 1. Default is 0");
        exit(0);
    }

    private static void dumpLinearDebug(HashObject hashObject, int index){
        try {
            FileWriter writeLinearFile = new FileWriter("linear-dump");
            writeLinearFile.write("table["+index+"]: " + hashObject.toString());
        }
        catch(IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    private static void dumpDoubleDebug(HashObject hashObject, int index){
        try {
            FileWriter writeDoubleFile = new FileWriter("double-dump");
            writeDoubleFile.write("table["+index+"]: " + hashObject.toString());
        }
        catch(IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
