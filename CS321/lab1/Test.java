import java.io.*;
import java.util.StringTokenizer;

import static java.lang.System.exit;

public class Test<T> {
    static BufferedReader br;
    static StringTokenizer stringTokenizer;
    static int cache1Hits, cache2Hits, cache1References, cache2References;
    public static <T> void main(String[] args){
        int cache1Size, cache2Size;
        Cache<T> cache1, cache2;
        switch(args[0]){
            case "1":
                cache1Size = Integer.parseInt(args[1]);
                initBuffer(args[2]);
                cache1 = new Cache<T>(cache1Size);
                test1Cache(cache1);
                printResultsFor1Cache();
                break;
            case "2":
                cache1Size = Integer.parseInt(args[1]);
                cache2Size = Integer.parseInt(args[2]);
                if(cache1Size < 0 || cache2Size < 0){
                    Usage();
                }
                if(cache1Size >= cache2Size){
                    System.out.println("Cache 1 size must be smaller than cache 2 size");
                    exit(1);
                }
                initBuffer(args[3]);
                cache1 = new Cache<T>(cache1Size);
                cache2 = new Cache<T>(cache2Size);
                test2Caches(cache1, cache2);
                printResultsFor2Caches();
                break;
        }
        exit(0);
    }
    private static void Usage(){
        System.out.println("Usage: java Test 1 <cache size> <input textfilename> or \njava Test2 <1st-level cache size> <2nd-level cache size> <input textfile name>");
        exit(1);
    }
  
    @SuppressWarnings("unchecked")
    private static <T> void test1Cache(Cache<T> cache1){
        String line;
        StringTokenizer tokens;
        try {
            while (br.ready()) {
                line = br.readLine();
                if (line == null) return;
                tokens = new StringTokenizer(line, " ");
                while (tokens.hasMoreTokens()) {
                    cache1References++;
                    T object = (T) tokens.nextToken();
                    //If hit
                    if (!object.equals("")) {
                        if (cache1.getObject(object)) {
                            cache1Hits++;
                            cache1.moveToTop(object);
                        } else {
                            cache1.addObject(object);
                        }
                    }
                }
            }
        }
        catch(IOException ex){
            exit (1);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T> void test2Caches(Cache<T> cache1, Cache<T> cache2){
        try {
            String line;
            StringTokenizer tokens;
            while (br.ready()) {
                line = br.readLine();
                if (line == null) return;
                tokens = new StringTokenizer(line, " ");
                while (tokens.hasMoreTokens()) {
                    cache1References++;
                    T object = (T) tokens.nextToken();
                    //If hit
                    if(cache1.getObject(object)){
                        cache1Hits++;
                        cache1.moveToTop(object);
                        cache2.moveToTop(object);
                    }
                    else{
                        cache2References++;
                        if(cache2.getObject(object)){
                            cache2Hits++;
                            cache2.moveToTop(object);
                            cache1.addObject(object);
                        }
                        else{
                            cache1.addObject(object);
                            cache2.addObject(object);
                        }
                    }
                }
            }
        }
        catch(IOException ex){
            exit(1);
        }
    }

    private static void printResultsFor1Cache(){
        System.out.println("The number of 1st level references: " + cache1References);
        System.out.println("The number of 1st level hits: " + cache1Hits);
        System.out.println("Global hit ratio: " + (cache1Hits/cache1References));
        System.out.println();
    }

    private static void printResultsFor2Caches(){
        System.out.println("The number of global references: " + cache1References);
        System.out.println("The number of global hits: " + (cache1Hits+cache2Hits));
        System.out.println("Global hit ratio: " + ((double)(cache1Hits+cache2Hits)/cache1References));
        System.out.println();
        System.out.println("The number of 1st level references: " + cache1References);
        System.out.println("The number of 1st level hits: " + cache1Hits);
        System.out.println("1st level cache hit ratio: " + ((double)cache1Hits/cache1References));
        System.out.println();
        System.out.println("The number of 2nd level references: " + cache2References);
        System.out.println("The number of 2nd level hits: " + cache2Hits);
        System.out.println("2nd level cache hit ratio: " + ((double)cache2Hits/cache2References));
        System.out.println();
    }

    private static void initBuffer(String fileName){
        try{
            br = new BufferedReader(new FileReader(fileName));
        }
        catch(FileNotFoundException fnfEx){
            System.out.println("Please provide a proper file name. It must be the full path name");
            exit(1);
        }
    }
}
