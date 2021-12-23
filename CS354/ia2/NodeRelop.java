public class NodeRelop extends Node {

    private String relop;

    public NodeRelop(int pos, String relop) {
		this.pos=pos;
		this.relop=relop;
    }

    public double op(double o1, double o2) throws EvalException {
	if (relop.equals("<"))
	    return o1<o2 ? 7 : 0;
	if (relop.equals(">"))
	    return o1>o2 ? 7 : 0;
	if (relop.equals("<="))
	    return o1<=o2 ? 7 : 0;
	if (relop.equals(">="))
	    return o1>=o2 ? 7 : 0;
	if (relop.equals("<>"))
	    return o1!=o2 ? 7 : 0;
	if (relop.equals("=="))
	    return o1==o2 ? 7 : 0;
	throw new EvalException(pos,"bogus relop: "+relop);
    }

}
