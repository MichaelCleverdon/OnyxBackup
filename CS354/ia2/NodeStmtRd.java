public class NodeStmtRd extends NodeStmt {

    private String id;

    public NodeStmtRd(String id) {
	    this.id = id;
    }

    public double eval(Environment env) throws EvalException {
	    java.util.Scanner userInput = new java.util.Scanner(System.in);
        double val = userInput.nextDouble();
        env.put(id, val);
        userInput.close();
        return val;
    }

}
