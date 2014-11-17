import java.util.ArrayList;
import org.jllvm.LLVMFunction;
import org.jllvm.LLVMIntegerType;
import org.jllvm.LLVMValue;
import org.jllvm.LLVMVoidType;



abstract class DecafIntf {
	public abstract LLVMValue accept(Visitor v);
}

/*
 * program : CLASS Program '{' declarations '}'
 */
class Class {
	String id = null;

	public Class(String id) {
		super();
		this.id = id;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class Decaf_Class extends DecafIntf {

	Class cls = null;
	Identifier id = null;
	ArrayList<DeclarationsIntf> decls = null;

	public Decaf_Class(Class cls, Identifier id,
			ArrayList<DeclarationsIntf> decls) {
		super();
		this.cls = cls;
		this.id = id;
		this.decls = decls;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * declarations : type fields ';' declarations | method_decl;
 */

abstract class DeclarationsIntf extends DecafIntf {
	public abstract LLVMValue accept(Visitor v);

}

class Declarations1 extends DeclarationsIntf {
	ArrayList<MethodDeclarations> methodDecls = null;

	public Declarations1(ArrayList<MethodDeclarations> methodDecls) {
		super();
		this.methodDecls = methodDecls;
	}

	public void setMethodDecls(ArrayList<MethodDeclarations> methodDecls) {
		this.methodDecls = methodDecls;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

abstract class FieldDeclIntf extends DeclarationsIntf {
	public abstract LLVMValue accept(Visitor v);

}

class FieldDecl extends FieldDeclIntf {
	Type type = null;
	ArrayList<Fields> fields = null;

	public FieldDecl(Type type, ArrayList<Fields> fields) {
		super();
		this.type = type;
		this.fields = fields;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * fields : field | field ',' fields ;
 */

abstract class Fields{
	public abstract void accept(Visitor v);

}

class Fields1 extends Fields {
	Field field = null;

	public Fields1(Field field) {
		super();
		this.field = field;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}

/*
 * field : ID | ID '[' INT_LITERAL ']' ;
 */


class Identifier {
	String id = null;

	public Identifier(String id) {
		super();
		this.id = id;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class Field extends Fields {
	Identifier id = null;

	public Field(Identifier id) {
		super();
		this.id = id;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}

class Field1 extends Fields {
	Identifier id = null;
	int val = -1;

	public Field1(Identifier id, int val) {
		super();
		this.id = id;
		this.val = val;
	}

	public void accept(Visitor v) {
		v.visit(this);

	}
}

abstract class MethodDeclarations extends DeclarationsIntf {
	public abstract LLVMValue accept(Visitor v);

}

class MethodDeclaration1 extends MethodDeclarations {
	Type type = null;
	Identifier id = null;
	ArrayList<Arguement> argList = null;
	Block block = null;

	public MethodDeclaration1(Type type, Identifier id,
			ArrayList<Arguement> argList, Block block) {
		super();
		this.type = type;
		this.id = id;
		this.argList = argList;
		this.block = block;
	}

	public LLVMFunction accept(Visitor v) {
		return v.visit(this);
	}

}

class MethodDeclaration2 extends MethodDeclarations {
	Void type = null;
	Identifier id = null;
	ArrayList<Arguement> argList = null;
	Block block = null;

	public MethodDeclaration2(Void type, Identifier id,
			ArrayList<Arguement> argList, Block block) {
		super();
		this.type = type;
		this.id = id;
		this.argList = argList;
		this.block = block;
	}

	public LLVMFunction accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * args_decl :arg ',' args_decl | arg | ; arg : type ID ;
 */
abstract class Arguement {
	public abstract NameTypePair accept(Visitor v);

}

class Arguement1 extends Arguement {
	Type type = null;
	Identifier id = null;

	public Arguement1(Type type, Identifier sym) {
		super();
		this.type = type;
		this.id = sym;
	}

	public NameTypePair accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * vars : ID ';' | ID ',' vars; var_decl : type vars ; var_decls : var_decl
 * var_decls | var_decl ;
 */

class Var_Decl {
	Type type;
	ArrayList<Identifier> vars = null;

	public Var_Decl(Type type, ArrayList<Identifier> vars) {
		super();
		this.type = type;
		this.vars = vars;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}

/*
 * statements : statement | statement statements ;
 */

/*
 * statement: location ASSGN_OP expr ';' | method_call ';' {$$=$1;} | IF '('
 * expr ')' block ELSE block | IF '(' expr ')' block | FOR ID E_ASSIGN_OP expr
 * ',' expr block | RETURN ';' | RETURN expr ';' | BREAK ';' | CONTINUE ';' |
 * block {$$=$1;};
 */

abstract class StatementIntf extends ExpressionIntf {
	public abstract LLVMValue accept(Visitor v);

}

class Statement1 extends StatementIntf {
	Location loc = null;
	ExpressionIntf expr = null;
	String op=null;

	public Statement1(Location loc, ExpressionIntf expr, String op) {
		super();
		this.loc = loc;
		this.expr = expr;
		this.op = op;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Return {
	String s = null;

	public Return(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class ReturnStatement extends StatementIntf {

	Return s = null;

	public ReturnStatement(Return s) {
		super();
		this.s = s;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class ReturnStatement1 extends StatementIntf {
	Return s = null;
	ExpressionIntf expr = null;

	public ReturnStatement1(Return s, ExpressionIntf expr) {
		super();
		this.s = s;
		this.expr = expr;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Break {
	String s = null;

	public Break(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class BreakStatement extends StatementIntf {
	Break s = null;

	public BreakStatement(Break s) {
		super();
		this.s = s;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Continue {

	String s = null;

	public Continue(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class ContinueStatement extends StatementIntf {
	Continue s = null;

	public ContinueStatement(Continue s) {
		super();
		this.s = s;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class IfStatement extends StatementIntf {
	public IF s1 = null;
	public ExpressionIntf expr = null;
	public Block block = null;

	public IfStatement(IF s1, ExpressionIntf expr, Block block) {
		super();
		this.s1 = s1;
		this.expr = expr;
		this.block = block;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class IF {
	public String s1 = null;

	public IF(String s1) {
		super();
		this.s1 = s1;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class ELSE {
	String s = null;

	public ELSE(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class IfElseStatement extends StatementIntf {
	IF s1 = null;
	public ExpressionIntf expr = null;
	public Block ifBlock = null;
	public ELSE s2 = null;
	public Block elseBlock = null;

	public IfElseStatement(IF s1, ExpressionIntf expr, Block ifBlock, ELSE s2,
			Block elseBlock) {
		super();
		this.s1 = s1;
		this.expr = expr;
		this.ifBlock = ifBlock;
		this.s2 = s2;
		this.elseBlock = elseBlock;
	}

	public LLVMValue accept(Visitor v) {
		return  v.visit(this);
	}

}

class For {
	String s = null;

	public For(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class ForStatement extends StatementIntf {
	public For s = null;
	public Identifier id = null;
	public String ch = null;
	public ExpressionIntf expr1 = null;
	public ExpressionIntf expr2 = null;
	public Block block = null;

	public ForStatement(For s, Identifier id, String ch, ExpressionIntf expr1,
			ExpressionIntf expr2, Block block) {
		super();
		this.s = s;
		this.id = id;
		this.ch = ch;
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.block = block;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * 
 * block_body : var_decls statements | var_decls | statements ;
 */

abstract class Block extends StatementIntf {
	public abstract LLVMValue accept(Visitor v);
}

class Block1 extends Block {
	ArrayList<Var_Decl> vars = null;
	ArrayList<StatementIntf> stmts = null;

	public Block1(ArrayList<Var_Decl> vars, ArrayList<StatementIntf> stmts) {
		
		super();
		this.vars = vars;
		this.stmts = stmts;
		
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}
}

class Block2 extends Block {
	ArrayList<Var_Decl> vars = null;

	public Block2(ArrayList<Var_Decl> vars) {
		this.vars = vars;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}
}

class Block3 extends Block {
	ArrayList<StatementIntf> stmts = null;

	public Block3(ArrayList<StatementIntf> stmts) {
		this.stmts = stmts;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}
}
class Block4 extends Block{

	@Override
	public LLVMValue accept(Visitor v) {
		
		return null;
	}
	
}

/*
 * 
 * type : INT | BOOLEAN ;
 */

abstract class Type {
	public abstract LLVMIntegerType accept(Visitor v);

}

class IntegerType extends Type {
	String s = null;

	public IntegerType(String s) {
		super();
		this.s = s;
	}

	public LLVMIntegerType accept(Visitor v) {
		return v.visit(this);
	}

}

class BooleanType extends Type {
	String s = null;

	public BooleanType(String s) {
		super();
		this.s = s;
	}

	public LLVMIntegerType accept(Visitor v) {
		return v.visit(this);
	}

}

class Void {

	String s = null;

	public Void(String s) {
		super();
		this.s = s;
	}

	public LLVMVoidType accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * exprs : expr | expr ',' exprs ;
 */
abstract class ExpressionIntf {
	public abstract LLVMValue accept(Visitor v);

}

/*
 * callout_args : callout_arg | callout_arg ',' callout_args ;
 */

/*
 * callout_arg : expr | STRING_LITERAL ;
 */

abstract class callout_args extends ExpressionIntf{
	public abstract LLVMValue accept(Visitor v);

}

class callout_arg extends callout_args {
	String s = null;

	public callout_arg(String s) {
		super();
		this.s = s;
	}

	public LLVMValue accept(Visitor v) {
		return  v.visit(this);
	}

}
class callout_arg1 extends callout_args {
	ExpressionIntf expr=null;

	public callout_arg1(ExpressionIntf expr) {
		super();
		this.expr=expr;
	}

	public LLVMValue accept(Visitor v) {
		return  v.visit(this);
	}

}

/*
 * method_call : method_name '(' ')' | method_name '(' exprs ')' | CALLOUT '('
 * STRING_LITERAL ',' callout_args ')' | CALLOUT '(' STRING_LITERAL ')' ;
 */

/*
 * method_name : ID
 */

class MethodStatement1 extends StatementIntf {
	Identifier id = null;

	public MethodStatement1(Identifier id) {
		super();
		this.id = id;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class MethodStatement2 extends StatementIntf {
	Identifier id = null;

	ArrayList<ExpressionIntf> exprs = null;

	public MethodStatement2(Identifier id, ArrayList<ExpressionIntf> exprs) {
		super();
		this.id = id;
		this.exprs = exprs;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class StringLiteral {
	String s = null;

	public StringLiteral(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class MethodStatementCallOut1 extends StatementIntf {
	CallOut s = null;
	StringLiteral s1 = null;
	ArrayList<ExpressionIntf> calloutargs = null;

	public MethodStatementCallOut1(CallOut s, StringLiteral s1,
			ArrayList<ExpressionIntf> calloutargs) {
		super();
		this.s = s;
		this.s1 = s1;
		this.calloutargs = calloutargs;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class CallOut {
	String s = null;

	public CallOut(String s) {
		super();
		this.s = s;
	}

	public String accept(Visitor v) {
		return v.visit(this);
	}

}

class MethodStatementCallOut2 extends StatementIntf {
	CallOut s = null;
	StringLiteral s1 = null;

	public MethodStatementCallOut2(CallOut s, StringLiteral s1) {
		super();
		this.s = s;
		this.s1 = s1;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * location : ID { $$.obj = new Identifier($1.sval); } | ID '[' expr ']' ;
 */
abstract class Location extends ExpressionIntf {
	public Identifier id;

	public abstract LLVMValue accept(Visitor v);

}

class Location1 extends Location {

	public Location1(Identifier id) {
		super();
		super.id = id;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Location2 extends Location {
	ExpressionIntf expr = null;

	public Location2(Identifier id, ExpressionIntf expr) {
		super();
		super.id = id;
		this.expr = expr;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * expr : expr ARTH_OP term1 | term1 ; term1 : term1 REL_OP term2 | term2 ;
 * term2 : term2 EQ_OP term3 | term3 ; term3 : term3 COND_OP term4 | term4 ;
 * term4 : location | method_call | literal | MINUS term4 | '!' term4 | '(' expr
 * ')' ;
 */

class Expression1 extends ExpressionIntf {
	ExpressionIntf expr = null;
	ExpressionIntf term1 = null;
	String operator = null;

	public Expression1(ExpressionIntf expr, ExpressionIntf term1, String operator) {
		super();
		this.expr = expr;
		this.term1 = term1;
		this.operator = operator;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Expression2 extends ExpressionIntf {
	ExpressionIntf term5 = null;
	String operator;

	public Expression2(ExpressionIntf term5, String operator) {
		super();
		this.term5 = term5;
		this.operator = operator;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class Expression3 extends ExpressionIntf {
	ExpressionIntf term5 = null;

	public Expression3(ExpressionIntf term5) {
		super();
		this.term5 = term5;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

/*
 * literal : INT_LITERAL | CHAR_LITERAL | bool_literal ;
 */

abstract class Literal extends ExpressionIntf {
	public abstract LLVMValue accept(Visitor v);

}

class IntegerLiteral extends Literal {
	public int value = 0;

	public IntegerLiteral(int value) {
		super();
		this.value = value;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}

class BooleanLiteral extends Literal {
	public String value;

	public BooleanLiteral(String value) {
		super();
		this.value = value;
	}

	public LLVMValue accept(Visitor v) {
		return v.visit(this);
	}

}
