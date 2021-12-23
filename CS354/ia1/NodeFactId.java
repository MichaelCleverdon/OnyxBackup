public class NodeFactId extends NodeFact {

    private String id;
    private NodeUnary negative;

    public NodeFactId(int pos, String id) {
	this.pos=pos;
	this.id=id;
    }

    public NodeFactId(int pos, String id, NodeUnary negative) {
        this.pos=pos;
        this.id=id;
        this.negative = negative;
    }

    public double eval(Environment env) throws EvalException {
	    return negative != null ? -1*env.get(pos,id) : env.get(pos, id);
    }

}
