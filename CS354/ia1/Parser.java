// This class is a recursive-descent parser,
// modeled after the programming language's grammar.
// It constructs and has-a Scanner for the program
// being parsed.

import java.util.*;

public class Parser {

    private Scanner scanner;

	//Matches a string (will throw exception if it doesn't match) and advanced the cursor to the next token
    private void match(String s) throws SyntaxException {
		scanner.match(new Token(s));
    }

	//Returns the current token
    private Token curr() throws SyntaxException {
		return scanner.curr();
    }

	//Returns the current position of the token from the original program string
    private int pos() {
		return scanner.pos();
    }

	//Parse multiplication operators
    private NodeMulop parseMulop() throws SyntaxException {
	if (curr().equals(new Token("*"))) {
	    match("*");
	    return new NodeMulop(pos(),"*");
	}
	if (curr().equals(new Token("/"))) {
	    match("/");
	    return new NodeMulop(pos(),"/");
	}
	return null;
    }

	//Parses add operators
    private NodeAddop parseAddop() throws SyntaxException {
	if (curr().equals(new Token("+"))) {
	    match("+");
	    return new NodeAddop(pos(),"+");
	}
	if (curr().equals(new Token("-"))) {
	    match("-");
	    return new NodeAddop(pos(),"-");
	}
	return null;
    }

	//Obtains a unary value if one exists, otherwise it returns null
	private NodeUnary parseUnary() throws SyntaxException{
		int x = 1;
		//Parses infinite -'s and will flip the sign for each one
		while(curr().equals(new Token("-"))){
			match("-");
			x *= -1;
		}
		if(x == -1){
			return new NodeUnary("-");
		}
		return null;
	}

    private NodeFact parseFact() throws SyntaxException {
		//Check if there's a negative
		NodeUnary negative = parseUnary();

		if (curr().equals(new Token("("))) {
			match("(");
			NodeExpr expr=parseExpr();
			match(")");
			//Create a new expression within the () and recurse
			return new NodeFactExpr(expr, negative);
		}
		if (curr().equals(new Token("id"))) {
			Token id=curr();
			match("id");
			//Create a new NodeFact that will obtain the value of an id later at evaluation time
			return new NodeFactId(pos(),id.lex(), negative);
		}
		Token num=curr();
		match("num");
		//Return a number
		return new NodeFactNum(num.lex(), negative);
    }

    private NodeTerm parseTerm() throws SyntaxException {
	NodeFact fact=parseFact();
	//Parse a multiplicative operator. This is done above add ops so that they evaluate first
	NodeMulop mulop=parseMulop();
	if (mulop==null)
	    return new NodeTerm(fact,null,null);
	NodeTerm term=parseTerm();
	term.append(new NodeTerm(fact,mulop,null));
	return term;
    }

    private NodeExpr parseExpr() throws SyntaxException {
		//Parse a term
	NodeTerm term=parseTerm();
	//Then parse an add operator
	NodeAddop addop=parseAddop();
	if (addop==null)
	    return new NodeExpr(term,null,null);
	NodeExpr expr=parseExpr();
	expr.append(new NodeExpr(term,addop,null));
	return expr;
    }

    private NodeAssn parseAssn() throws SyntaxException {
	//First part of assignment is the id
	Token id=curr();
	//Make sure the token we just got is an id
	match("id");
	//Next up should be an equals sign
	match("=");
	//Parse out the expression after the equals sign
	NodeExpr expr=parseExpr();
	//Assign the expression to the id and return the new object
	NodeAssn assn=new NodeAssn(id.lex(),expr);
	return assn;
    }

    private NodeStmt parseStmt() throws SyntaxException {
	//Parses assignment
	NodeAssn assn=parseAssn();
	//Expects semi after assignment statement
	match(";");
	//Creates a new NodeStatement from the assignment
	NodeStmt stmt=new NodeStmt(assn);
	return stmt;
    }

	//Starting point for parsing
    public Node parse(String program) throws SyntaxException {
	scanner=new Scanner(program);
	scanner.next();
	//Starts parsing statement
	NodeStmt stmt=parseStmt();
	match("EOF");
	return stmt;
    }

}
