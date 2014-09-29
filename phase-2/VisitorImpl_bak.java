public class VisitorImpl_bak implements Visitor {

	@Override
	public void visit(Decaf_Class decaf_Class) {
		decaf_Class.cls.accept(this);
		decaf_Class.id.accept(this);

		System.out.print(" { ");
		for (DeclarationsIntf decl : decaf_Class.decls) {
			decl.accept(this);
		}
		System.out.print(" } ");

	}

	@Override
	public void visit(Declarations1 declarations1) {
		for (DeclarationsIntf d : declarations1.methodDecls)
			d.accept(this);
	}

	@Override
	public void visit(FieldDecl fieldDecl) {

		fieldDecl.type.accept(this);
		for (Fields f : fieldDecl.fields)
			f.accept(this);
	}

	@Override
	public void visit(Fields1 fields1) {
		fields1.field.accept(this);
	}

	@Override
	public void visit(Identifier identifier) {

		System.out.print(" " + identifier.id + " ");
	}

	@Override
	public void visit(Field field) {
		field.id.accept(this);
	}

	@Override
	public void visit(MethodDeclaration1 methodDeclaration1) {
		methodDeclaration1.type.accept(this);
		methodDeclaration1.id.accept(this);
		System.out.print("(");
		for (Arguement a : methodDeclaration1.argList)
			a.accept(this);
		System.out.print(")");
		methodDeclaration1.block.accept(this);
	}

	@Override
	public void visit(MethodDeclaration2 methodDeclaration2) {
		methodDeclaration2.type.accept(this);
		methodDeclaration2.id.accept(this);
		System.out.print("(");
		for (Arguement a : methodDeclaration2.argList)
			a.accept(this);
		System.out.print(")");
		methodDeclaration2.block.accept(this);

	}

	@Override
	public void visit(Arguement1 arguement1) {
		arguement1.type.accept(this);
		arguement1.id.accept(this);
	}

	@Override
	public void visit(Var_Decl var_Decl) {
		var_Decl.type.accept(this);
		for (Identifier id : var_Decl.vars)
			id.accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(Statement1 statement1) {
		statement1.expr.accept(this);
		System.out.print(" " + statement1.op + " ");
		statement1.loc.accept(this);

	}

	@Override
	public void visit(ReturnStatement returnStatement) {
		returnStatement.s.accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(ReturnStatement1 returnStatement1) {
		returnStatement1.s.accept(this);
		returnStatement1.expr.accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(BreakStatement breakStatement) {
		breakStatement.accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(ContinueStatement continueStatement) {
		continueStatement.accept(this);
		System.out.print(";" );

	}

	@Override
	public void visit(IfStatement ifStatement) {
		ifStatement.s1.accept(this);
		System.out.print("(");
		ifStatement.expr.accept(this);
		System.out.print(")");
		ifStatement.block.accept(this);

	}

	@Override
	public void visit(IfElseStatement ifElseStatement) {
		ifElseStatement.s1.accept(this);
		System.out.print("(");
		ifElseStatement.expr.accept(this);
		System.out.print(")");
		ifElseStatement.s1.accept(this);
		ifElseStatement.s2.accept(this);
		ifElseStatement.elseBlock.accept(this);
	}

	@Override
	public void visit(ForStatement forStatement) {
		forStatement.s.accept(this);
		forStatement.id.accept(this);
		System.out.print(forStatement.ch);
		forStatement.expr1.accept(this);
		System.out.print(" , ");
		forStatement.expr2.accept(this);
		forStatement.block.accept(this);
	}

	@Override
	public void visit(Block1 block) {
		System.out.print(" { ");
		for (Var_Decl var : block.vars)
			var.accept(this);
		for (StatementIntf stmt : block.stmts)
			stmt.accept(this);
		System.out.print(" } ");

	}

	@Override
	public void visit(IntegerType integerType) {
		System.out.print(" " + integerType.s + " ");
	}

	@Override
	public void visit(BooleanType booleanType) {
		System.out.print(" " + booleanType.s + " ");

	}

	@Override
	public void visit(Void void1) {
		System.out.print(" " + void1.s + " ");

	}

	@Override
	public void visit(callout_arg callout_arg) {
		System.out.print(" " + callout_arg.s + " ");

	}

	@Override
	public void visit(MethodStatement1 methodStatement1) {
		methodStatement1.id.accept(this);
		System.out.print("(");
		System.out.print(")");
		System.out.print(";");
	}

	@Override
	public void visit(MethodStatement2 methodStatement2) {
		methodStatement2.id.accept(this);
		System.out.print("(");
		for (ExpressionIntf expr : methodStatement2.exprs)
			expr.accept(this);
		System.out.print("(");
		System.out.print(";");

	}

	@Override
	public void visit(MethodStatementCallOut1 methodStatementCallOut1) {
		methodStatementCallOut1.s.accept(this);
		System.out.print("(");
		System.out.print(methodStatementCallOut1.s1);
		System.out.print(" , ");
		for (callout_arg arg : methodStatementCallOut1.calloutargs)
			arg.accept(this);
		System.out.print(")");

	}

	@Override
	public void visit(MethodStatementCallOut2 methodStatementCallOut2) {
		methodStatementCallOut2.s.accept(this);
		System.out.print("(");
		System.out.print(methodStatementCallOut2.s1);
		System.out.print(")");

	}

	@Override
	public void visit(Location1 location) {
		location.id.accept(this);

	}

	@Override
	public void visit(Location2 location) {
		location.id.accept(this);
		System.out.print(" [ ");
		location.expr.accept(this);
		System.out.print(" ] ");
	}

	@Override
	public void visit(Expression1 expression1) {
		expression1.expr.accept(this);
		System.out.print(" " + expression1.operator + " ");
		expression1.term1.accept(this);
	}

	@Override
	public void visit(Expression2 expression2) {
		System.out.print(" " + expression2.operator + " ");
		expression2.term5.accept(this);

	}

	@Override
	public void visit(Expression3 expression3) {
		System.out.print("(");
		expression3.term5.accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(IntegerLiteral integerLiteral) {
		System.out.print(" " + integerLiteral.value + " ");
	}

	@Override
	public void visit(BooleanLiteral booleanLiteral) {
		System.out.print(" " + booleanLiteral.value + " ");

	}

	@Override
	public void visit(Return return1) {
		System.out.print(" " + return1.s + "  ");

	}

	@Override
	public void visit(Break break1) {
		System.out.print(" " + break1.s + "   ");

	}

	@Override
	public void visit(Continue continue1) {
		System.out.print(" " + continue1.s + "  ");

	}

	@Override
	public void visit(Class class1) {
		System.out.print(" " + class1.id + " ");

	}

	@Override
	public void visit(ELSE else1) {
		System.out.print(" " + else1.s + " ");

	}

	@Override
	public void visit(IF if1) {
		System.out.print(" " + if1.s1 + " ");

	}

	@Override
	public void visit(For for1) {
		System.out.print(" " + for1.s + " ");

	}

	@Override
	public void visit(StringLiteral stringLiteral) {
		System.out.print(" " + stringLiteral.s + " ");

	}

	@Override
	public void visit(CallOut callOut) {
		System.out.print(" " + callOut.s + " ");

	}

	@Override
	public void visit(Field1 field1) {
		field1.id.accept(this);
		System.out.print(" [ ");
		System.out.print(field1.val);
		System.out.print(" ] ");
	}

	@Override
	public void visit(Block2 block2) {
		for (Var_Decl var : block2.vars)
			var.accept(this);
		System.out.print(";");

	}

	@Override
	public void visit(Block3 block3) {
		for (StatementIntf stmt : block3.stmts)
			stmt.accept(this);

	}

}
