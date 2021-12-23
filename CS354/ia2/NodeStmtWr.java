public class NodeStmtWr extends NodeStmt {

    // private int pos;
    // private String id;
    NodeExpr expr;

    public NodeStmtWr(NodeExpr expr) {
        this.expr = expr;
    }

    public double eval(Environment env) throws EvalException {
        double val = expr.eval(env);
        System.out.println(val);
        return val;
    }

}
