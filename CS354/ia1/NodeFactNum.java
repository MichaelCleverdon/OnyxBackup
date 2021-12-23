public class NodeFactNum extends NodeFact {

    private String num;
    private NodeUnary negative;

    public NodeFactNum(String num) {
	this.num=num;
    }
    
    public NodeFactNum(String num, NodeUnary negative) {
        this.num=num;
        this.negative = negative;
    }

    public double eval(Environment env) throws EvalException {
	return negative != null ? -1*Double.parseDouble(num) : Double.parseDouble(num);
    }

}
