public class HashTable {
    HashObject[] hashTable;
    public int size = 0;
    private int tableSize = 0;
    private double alpha = 0;

    public HashTable(int size, double alpha){
        hashTable = new HashObject[size];
        this.alpha = alpha;
        tableSize = size;
    }

    public int LinearHash(Object hashObject, int probeNumber){
        int hashCode = hashObject.hashCode();
        return (PositiveMod(hashCode, tableSize)+probeNumber)%tableSize;
    }

    public int DoubleHash(Object hashObject, int probeNumber){
        int hashCode = hashObject.hashCode();
        int h1 = PositiveMod(hashCode, tableSize);
        int h2 = PositiveMod(hashCode, tableSize-2);
        return PositiveMod(h1 + (probeNumber*h2), tableSize);
    }

    @SuppressWarnings("unchecked")
    public void insertLinear(Object object){
        HashObject newObject = new HashObject(object);
        for(int i = 0; i < tableSize; i++) {
                //Vanilla Linear Hash
                int index = LinearHash(object, i);
                HashObject hashObject = hashTable[index];
                if (hashObject == null) {
                    newObject.probeCount = i+1;
                    hashTable[index] = newObject;
                    size++;
                    return;
                } else if (hashObject.equals(newObject.getKey())) {
                    hashTable[index].duplicateCount++;
                    return;
                }
        }
    }

    @SuppressWarnings("unchecked")
    public void insertDouble(Object object){
        HashObject newObject = new HashObject(object);
        for(int i = 0; i < tableSize; i++) {
            //Double hash, so quirky
            int index = DoubleHash(object, i);
            HashObject hashObject = hashTable[index];
            if (hashObject == null) {
                newObject.probeCount = i+1;
                hashTable[index] = newObject;
                size++;
                return;
            } else if (hashObject.equals(newObject.getKey())) {
                hashTable[index].duplicateCount++;
                return;
            }
        }
    }

    private int PositiveMod(int dividend, int divisor){
        int value = dividend % divisor;
        if (value < 0){
            value += divisor;
        }
        return value;
    }
}

