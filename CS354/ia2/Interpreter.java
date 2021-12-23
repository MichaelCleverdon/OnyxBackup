// This is the main class/method for the interpreter.
// Each command-line argument is a complete program,
// which is scanned, parsed, and evaluated.
// All evaluations share the same environment,
// so they can share variables.

public class Interpreter {

    public static void main(String[] args) {
	Parser parser=new Parser();
	Environment env=new Environment();
	for (String stmt: args)
	    try {
			double value = parser.parse(stmt).eval(env);
			//NaN is returned by NodeBlocks after evaluating all of their statements. This prevents it from being written to STDOut
			if(!Double.isNaN(value)){
				System.out.println(value);
			}
	    } catch (SyntaxException e) {
		System.err.println(e);
	    } catch (EvalException e) {
		System.err.println(e);
	    }
    }

}
