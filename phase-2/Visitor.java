
public interface Visitor {

	void visit(Decaf_Class decaf_Class);

	void visit(Declarations1 declarations1);

	void visit(FieldDecl fieldDecl);

	void visit(Fields1 fields1);

	void visit(Identifier identifier);

	void visit(Field field);

	void visit(MethodDeclaration1 methodDeclaration1);

	void visit(MethodDeclaration2 methodDeclaration2);

	void visit(Arguement1 arguement1);

	void visit(Var_Decl var_Decl);

	void visit(Statement1 statement1);

	void visit(ReturnStatement returnStatement);

	void visit(ReturnStatement1 returnStatement1);

	void visit(BreakStatement breakStatement);

	void visit(ContinueStatement continueStatement);

	void visit(IfStatement ifStatement);

	void visit(IfElseStatement ifElseStatement);

	void visit(ForStatement forStatement);

	void visit(IntegerType integerType);

	void visit(BooleanType booleanType);

	void visit(Void void1);

	void visit(callout_arg callout_arg);

	void visit(MethodStatement1 methodStatement1);

	void visit(MethodStatement2 methodStatement2);

	void visit(MethodStatementCallOut1 methodStatementCallOut1);

	void visit(MethodStatementCallOut2 methodStatementCallOut2);

	void visit(Expression1 expression1);

	void visit(Expression2 expression2);

	void visit(Expression3 expression3);

	void visit(IntegerLiteral integerLiteral);

	void visit(BooleanLiteral booleanLiteral);

	void visit(Return return1);

	void visit(Break break1);

	void visit(Continue continue1);

	void visit(Class class1);

	void visit(ELSE else1);

	void visit(IF if1);

	void visit(For for1);

	void visit(StringLiteral stringLiteral);

	void visit(CallOut callOut);

	void visit(Location2 location2);

	void visit(Location1 location1);

	void visit(Field1 field1);

	void visit(Block1 block);

	void visit(Block2 block2);

	void visit(Block3 block3);

}

