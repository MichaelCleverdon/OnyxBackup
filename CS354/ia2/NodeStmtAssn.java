public class NodeStmtAssn extends NodeStmt {

    private NodeAssn assn;

    public NodeStmtAssn(NodeAssn assn) {
        this.assn=assn; 
    }

    public double eval(Environment env) throws EvalException {
	    return assn.eval(env);
    }

}
