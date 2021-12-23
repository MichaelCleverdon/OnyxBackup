import java.util.LinkedList;
public class Cache<T>{
    int maxSize;
    int currentSize = 0;
    LinkedList<T> cache;
    public Cache(int size){
        this.maxSize = size;
        cache = new LinkedList<T>();
    }

    public boolean getObject(T object){
        return cache.contains(object);
    }

    public void addObject(T object){
        if(currentSize == maxSize){
            removeObject(maxSize-1);
        }
        cache.add(0, object);
        currentSize++;
    }

    public void removeObject(int index){
        cache.remove(index);
        currentSize--;
    }

    public void removeObject(T object){
        cache.remove(object);
        currentSize--;
    }

    public void clearCache(){
        cache = new LinkedList<T>();
        currentSize = 0;
    }

    public void moveToTop(T object){
        removeObject(object);
        addObject(object);
    }
}
