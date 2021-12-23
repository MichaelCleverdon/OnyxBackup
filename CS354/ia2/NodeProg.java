public class NodeProg extends Node {

    private NodeBlock block;

    public NodeProg(NodeBlock block) {
        this.block = block;
    }

    public double eval(Environment env) throws EvalException {
        //Put the variable and the evaluated value into the environment
	    return block.eval(env);
    }

}
