import java.util.HashMap;
import java.util.Map;

// This class provides a stubbed-out environment.
// You are expected to implement the methods.
// Accessing an undefined variable should throw an exception.

// Hint!
// Use the Java API to implement your Environment.
// Browse:
//   https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
// Read about Collections.
// Focus on the Map interface and HashMap implementation.
// Also:
//   https://www.tutorialspoint.com/java/java_map_interface.htm
//   http://www.javatpoint.com/java-map
// and elsewhere.

public class Environment {
    //Thing to store the variables. Stores in key:value pairs
    private Map<String, Double> variables = new HashMap<String, Double>();
    
    //Adds a varaible to the map
    public double put(String var, double val) { 
        variables.put(var, val); 
        return val; 
    }

    //Gets the value from the map. Throws an exception if the variable doesn't exist
    public double get(int pos, String var) throws EvalException { 
        if(variables.get(var) == null){
            throw new EvalException(pos, "Could not retrieve value from variable " + var);
        }
        else{
            return (double)variables.get(var);
        }
    }

}
