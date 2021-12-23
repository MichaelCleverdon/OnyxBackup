import java.util.ArrayList;
import java.util.List;

public class NodeBlock extends NodeStmt {

    private List<NodeStmt> stmts = new ArrayList<NodeStmt>();

    public NodeBlock(List<NodeStmt> stmts) {
        this.stmts = stmts;
    }

    public double eval(Environment env) throws EvalException {
        for(NodeStmt nodeStmt : stmts){
            nodeStmt.eval(env);
        }
        return Double.NaN;
    }

}
