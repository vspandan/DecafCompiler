import java.util.ArrayList;
import java.util.HashMap;

import org.jllvm.LLVMContext;
import org.jllvm.LLVMFunction;
import org.jllvm.LLVMInstructionBuilder;
import org.jllvm.LLVMIntegerType;
import org.jllvm.LLVMModule;
import org.jllvm.LLVMValue;
import org.jllvm.LLVMVoidType;


public interface Visitor {
	
	public static final LLVMContext context = LLVMContext.getGlobalContext();
	
	public static final LLVMModule module=new LLVMModule("Compilers Proj",context);
	
	public static final HashMap<String, LLVMValue> namedValues = new HashMap<String, LLVMValue>();
	
	public static final ArrayList<String> localVar = new ArrayList<String>();
	
	public static final LLVMInstructionBuilder builder = new LLVMInstructionBuilder();

	LLVMValue visit(Decaf_Class decaf_Class);

	LLVMValue visit(Declarations1 declarations1);

	LLVMValue visit(FieldDecl fieldDecl);

	void visit(Fields1 fields1);

	String visit(Identifier identifier);

	void visit(Field field);

	LLVMFunction visit(MethodDeclaration1 methodDeclaration1);

	LLVMFunction visit(MethodDeclaration2 methodDeclaration2);

	NameTypePair visit(Arguement1 arguement1);

	void visit(Var_Decl var_Decl);

	LLVMValue visit(Statement1 statement1);

	LLVMValue visit(ReturnStatement returnStatement);

	LLVMValue visit(ReturnStatement1 returnStatement1);

	LLVMIntegerType visit(IntegerType integerType);

	LLVMIntegerType visit(BooleanType booleanType);

	LLVMVoidType visit(Void void1);

	LLVMValue visit(callout_arg callout_arg);
	
	LLVMValue visit(callout_arg1 callout_arg1);

	LLVMValue visit(IntegerLiteral integerLiteral);

	LLVMValue visit(BooleanLiteral booleanLiteral);

	String visit(Return return1);

	String visit(Break break1);

	String visit(Continue continue1);

	String visit(Class class1);

	String visit(ELSE else1);

	String visit(IF if1);

	String visit(For for1);

	String visit(StringLiteral stringLiteral);

	String visit(CallOut callOut);

	void visit(Field1 field1);

	LLVMValue visit(Block1 block);

	LLVMValue visit(Block2 block2);

	LLVMValue visit(Block3 block3);

	LLVMValue visit(IfStatement ifStatement);

	LLVMValue visit(IfElseStatement ifElseStatement);

	LLVMValue visit(Location1 location);

	LLVMValue visit(Expression1 expression1);

	LLVMValue visit(Expression2 expression2);

	LLVMValue visit(Expression3 expression3);

	LLVMValue visit(BreakStatement breakStatement);
	
	LLVMValue visit(ContinueStatement continueStatement);

	LLVMValue visit(Location2 location);

	LLVMValue visit(ForStatement forStatement);

	LLVMValue visit(MethodStatement1 methodStatement1);

	LLVMValue visit(MethodStatement2 methodStatement2);

	LLVMValue visit(MethodStatementCallOut1 methodStatementCallOut1);

	LLVMValue visit(MethodStatementCallOut2 methodStatementCallOut2);

	

}

