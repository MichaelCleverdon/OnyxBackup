public class HashObject<T> {
    T object;
    int duplicateCount, probeCount;

    public HashObject(T object){
        this.object = object;
    }

    public HashObject(T object, int duplicateCount, int probeCount){
        this.object = object;
        this.duplicateCount = duplicateCount;
        this.probeCount = probeCount;
    }

    @Override
    public boolean equals(Object obj){
        return object.equals(obj);
    }

    @Override
    public String toString(){
        return object.toString() + " " + duplicateCount + " " + probeCount;
    }

    public T getKey(){
        return object;
    }
}
