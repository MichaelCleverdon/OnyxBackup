public class PrimeCalculator {
    private int lowerBound, upperBound;

    PrimeCalculator(){
        lowerBound = 95500;
        upperBound = 96000;
    }
    public int calculatePrime(){
        int prime = lowerBound;
        //If even
        if(prime%2 == 0){
            prime++;
        }

        int exponentBase = generateExponentBase(prime);

        //loop to find prime
        double finalResult = 0;
        boolean firstPrimeFound = false;
        //Keep prime odd
        for(int i = prime; i < upperBound; i=i+2){
            finalResult = squareAndMultiply(i, exponentBase);
            if(finalResult == 1){
                //Have to double check the prime calculation with a different exponentBase
                exponentBase = generateExponentBase(i);
                finalResult = squareAndMultiply(i, exponentBase);

                if(finalResult == 1){
                    if(firstPrimeFound) {
                        //Found the prime
                        System.out.println("A good table size is found: " + i);
                        prime = i;
                        break;
                    }
                    else{
                        firstPrimeFound = true;
                    }
                }
            }
            //Reset FirstPrimeFound if the twin hasn't been found
            if(finalResult != 1 && firstPrimeFound){
                firstPrimeFound = false;
            }
        }
        return prime;
    }
    private int generateExponentBase(int prime){
        double randomNumber = Math.random();
        return (randomNumber == 0 ? (int)(randomNumber * prime) + 1 : (int)(randomNumber * prime));
    }

    private double squareAndMultiply(int prime, int exponentBase){
        String binaryString = Integer.toBinaryString(prime-1);
        boolean foundFirstOne = false;
        double temporaryResult = 0;
        for(int k = 0; k < binaryString.length(); k++){
            //Find the first 1 and then perform logic after that
            if(!foundFirstOne){
                foundFirstOne = binaryString.charAt(k) == '1';
                temporaryResult = exponentBase;
                continue;
            }
            else{
                if(binaryString.charAt(k) == '1'){
                    temporaryResult = Math.pow(temporaryResult, 2.0) % prime;
                    temporaryResult = (temporaryResult * exponentBase) % prime;
                }
                else if(binaryString.charAt(k) == '0'){
                    temporaryResult = Math.pow(temporaryResult,2.0) % prime;
                }
            }
        }
        return temporaryResult;
    }
}
