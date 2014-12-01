import java.io.File;

import org.jllvm.LLVMAddInstruction;
import org.jllvm.LLVMBasicBlock;
import org.jllvm.LLVMCallInstruction;
import org.jllvm.LLVMConstant;
import org.jllvm.LLVMConstantArray;
import org.jllvm.LLVMConstantInteger;
import org.jllvm.LLVMConstantString;
import org.jllvm.LLVMDivideInstruction;
import org.jllvm.LLVMDivideInstruction.DivisionType;
import org.jllvm.LLVMExecutionEngine;
import org.jllvm.LLVMIntegerComparison;
import org.jllvm.LLVMPointerType;
import org.jllvm.LLVMRemainderInstruction.RemainderType;
import org.jllvm.LLVMArrayType;
import org.jllvm.LLVMBranchInstruction;
import org.jllvm.LLVMFunction;
import org.jllvm.LLVMFunctionType;
import org.jllvm.LLVMGetElementPointerInstruction;
import org.jllvm.LLVMGlobalVariable;
import org.jllvm.LLVMIntegerType;
import org.jllvm.LLVMLoadInstruction;
import org.jllvm.LLVMMultiplyInstruction;
import org.jllvm.LLVMRemainderInstruction;
import org.jllvm.LLVMReturnInstruction;
import org.jllvm.LLVMStackAllocation;
import org.jllvm.LLVMStoreInstruction;
import org.jllvm.LLVMSubtractInstruction;
import org.jllvm.LLVMType;
import org.jllvm.LLVMUnaryBitwiseInstruction;
import org.jllvm.LLVMUnaryBitwiseInstruction.UnaryBitwiseInstructionType;
import org.jllvm.LLVMValue;
import org.jllvm.LLVMVoidType;
import org.jllvm.bindings.Analysis;
import org.jllvm.bindings.LLVMIntPredicate;
import org.jllvm.bindings.LLVMLinkage;
import org.jllvm.bindings.LLVMVerifierFailureAction;

public class VisitorImpl implements Visitor {
	static {
		System.loadLibrary("jllvm");

	}
	LLVMExecutionEngine ex = null;

	LLVMFunction iffunc1 = null;;
	LLVMBasicBlock ifelse1 = null;
	LLVMBasicBlock ifend1 = null;

	LLVMBasicBlock fblock = null;
	LLVMFunction f = null;;
	LLVMBasicBlock forInc1 = null;
	LLVMBasicBlock forEnd1 = null;

	boolean hasMainFunc = false; // disable this flag for removing main method
									// constraint
	boolean funcReturnReq = false;
	boolean errorFlag = false;
	boolean breakOrContStat = false;
	boolean operandIncompatible = false;
	boolean isForStatement = false;
	boolean isMainFunc = false;
	boolean isBoolFunc = false;

	@Override
	public LLVMValue visit(Decaf_Class decaf_Class) {
		try {
			ex = new LLVMExecutionEngine(module);

			decaf_Class.cls.accept(this);

			for (DeclarationsIntf decl : decaf_Class.decls) {
				decl.accept(this);
			}

			/*
			 * Disabling output file name to be class name; for time being
			 * seting it to default
			 */
			// String className = decaf_Class.id.accept(this);
			// String llvmBitCodeFileName = className + ".bc";
			String llvmBitCodeFileName = "decaf.out";
			File file = new File(llvmBitCodeFileName);
			if (file.exists())
				file.delete();

			if (!errorFlag) {
				if (hasMainFunc) {
					// module.writeBitcodeToFile(llvmBitCodeFileName);
					Analysis.LLVMVerifyModule(module.getInstance(),
							LLVMVerifierFailureAction.LLVMPrintMessageAction,
							null);
					module.writeBitcodeToFile(llvmBitCodeFileName);
					// ex.linkInJit();
					// ex.runFunction(module.getNamedFunction("main"), new
					// LLVMGenericValue[]{});
				} else {
					System.err.println("Main Function: Undefined ");
				}
			} else {
				System.err.println("Compilation Errors");
			}

		} catch (Exception e1) {
			System.err.println(e1.getMessage());
			;
		} finally {
			System.exit(0);
		}

		return null;
	}

	@Override
	public LLVMValue visit(Declarations1 declarations1) {
		for (DeclarationsIntf d : declarations1.methodDecls)
			d.accept(this);
		return null;
	}

	@Override
	public LLVMValue visit(FieldDecl fieldDecl) {
		LLVMIntegerType ty = fieldDecl.type.accept(this);

		if (ty instanceof LLVMIntegerType) {
			long width = ty.getWidth();
			LLVMIntegerType inty = null;
			if (width == 1)
				inty = LLVMIntegerType.i1;
			else
				inty = LLVMIntegerType.i32;
			for (Fields f : fieldDecl.fields) {
				f.accept(this);
				if (f instanceof Field1) {
					int size = ((Field1) f).val;
					if (size > 0) {
						LLVMArrayType arrTy = new LLVMArrayType(inty, size);
						LLVMConstantInteger instConsts[] = new LLVMConstantInteger[size];
						for (int i = 0; i < size; i++) {
							instConsts[i] = LLVMConstantInteger
									.constantInteger(inty, 0, false);
						}
						LLVMConstantArray arry = new LLVMConstantArray(inty,
								instConsts);
						LLVMGlobalVariable var = module.addGlobal(arrTy,
								((Field1) f).id.id);
						var.setInitializer(arry);
						var.setLinkage(LLVMLinkage.LLVMCommonLinkage);
						namedValues.put(((Field1) f).id.id, var);

					} else {
						errorFlag = true;
						System.err
								.println("Size of an array must be greater than zero");
					}
				}
				if (f instanceof Field) {
					LLVMConstantInteger val = LLVMConstantInteger
							.constantInteger(inty, 0, false);

					LLVMGlobalVariable var = module.addGlobal(inty,
							((Field) f).id.id);
					var.setInitializer(val);
					var.setLinkage(LLVMLinkage.LLVMExternalLinkage);
					namedValues.put(((Field) f).id.id, var);

				}
			}
		}
		return null;
	}

	@Override
	public void visit(Fields1 fields1) {
		fields1.field.accept(this);
	}

	@Override
	public String visit(Identifier identifier) {
		return identifier.id;
	}

	@Override
	public void visit(Field field) {
		field.id.accept(this);
	}

	@Override
	public LLVMFunction visit(MethodDeclaration1 methodDeclaration1) {

		LLVMType typeLLVM = methodDeclaration1.type.accept(this);
		if (typeLLVM instanceof LLVMIntegerType) {

			if (typeLLVM.equals(LLVMIntegerType.i1))
				isBoolFunc = true;
			String functionName = methodDeclaration1.id.accept(this);
			if (namedValues.containsKey(functionName)) {
				System.err.println(functionName + ": IDentifier already used");
				errorFlag = true;
				return null;
			}
			funcReturnReq = true;

			LLVMType[] args = new LLVMType[methodDeclaration1.argList.size()];
			int size = methodDeclaration1.argList.size();
			NameTypePair ntPair[] = new NameTypePair[size];

			for (int i = 0; i < size; i++) {
				ntPair[i] = methodDeclaration1.argList.get(i).accept(this);
				args[i] = ntPair[i].type;
			}
			if (functionName.equals("main") && args.length == 0) {
				hasMainFunc = true;
			}
			LLVMFunctionType FT = new LLVMFunctionType(typeLLVM, args, false);
			f = new LLVMFunction(module, functionName, FT);

			fblock = f.appendBasicBlock("entry");
			builder.positionBuilderAtEnd(fblock);

			for (int i = 0; i < f.countParameters(); i++) {
				f.getParameter(i).setValueName(ntPair[i].name + ".formal");
				LLVMValue argStackPtr = new LLVMStackAllocation(builder, f
						.getParameter(i).typeOf(), null, ntPair[i].name);
				namedValues.put(ntPair[i].name, argStackPtr);
				new LLVMStoreInstruction(builder, f.getParameter(i),
						argStackPtr);

			}

			methodDeclaration1.block.accept(this);

			if (funcReturnReq) {
				System.err.println("No Return Statement");
				errorFlag = true;
			}
			namedValues.put(functionName, f);
			return f;

		}
		return null;
	}

	@Override
	public LLVMFunction visit(MethodDeclaration2 methodDeclaration2) {
		LLVMType voidType = methodDeclaration2.type.accept(this);
		if (voidType instanceof LLVMVoidType) {

			String functionName = methodDeclaration2.id.accept(this);
			if (namedValues.containsKey(functionName)) {
				System.err.println(functionName + ": IDentifier already used");
				errorFlag = true;
				return null;
			}
			LLVMType[] args = new LLVMType[methodDeclaration2.argList.size()];
			int size = methodDeclaration2.argList.size();
			NameTypePair ntPair[] = new NameTypePair[size];

			for (int i = 0; i < size; i++) {
				ntPair[i] = methodDeclaration2.argList.get(i).accept(this);
				args[i] = ntPair[i].type;
			}
			LLVMFunctionType FT = new LLVMFunctionType(voidType, args, false);
			f = new LLVMFunction(module, functionName, FT);

			fblock = f.appendBasicBlock("entry");
			builder.positionBuilderAtEnd(fblock);
			for (int i = 0; i < f.countParameters(); i++) {
				f.getParameter(i).setValueName(ntPair[i].name + ".formal");
				LLVMValue argStackPtr = new LLVMStackAllocation(builder, f
						.getParameter(i).typeOf(), null, ntPair[i].name);
				namedValues.put(ntPair[i].name, argStackPtr);
				new LLVMStoreInstruction(builder, f.getParameter(i),
						argStackPtr);
			}
			methodDeclaration2.block.accept(this);
			new LLVMReturnInstruction(builder, null);
			namedValues.put(functionName, f);
			return f;
		}
		return null;

	}

	@Override
	public NameTypePair visit(Arguement1 arguement1) {
		return new NameTypePair(arguement1.id.accept(this),
				arguement1.type.accept(this));
	}

	@Override
	public void visit(Var_Decl var_Decl) {
		LLVMType ty = var_Decl.type.accept(this);
		for (Identifier id : var_Decl.vars) {
			String ident = id.accept(this);
			if (namedValues.containsKey(ident)) {

				if (localVar.contains(ident)) {
					System.err.println(ident + ": IDentifier already used");
					errorFlag = true;
					return;
				}
			}
			if (ty instanceof LLVMIntegerType) {
				long width = ((LLVMIntegerType) ty).getWidth();
				LLVMIntegerType inty = null;
				if (width == 1)
					inty = LLVMIntegerType.i1;
				else
					inty = LLVMIntegerType.i32;

				LLVMConstantInteger intConst = LLVMConstantInteger
						.constantInteger(inty, 0, false);
				namedValues.put(ident, new LLVMStackAllocation(builder, inty,
						intConst, ident));
				localVar.add(ident);
			}

		}
	}

	@Override
	public LLVMValue visit(Statement1 statement1) {
		LLVMValue rhs = statement1.expr.accept(this);
		String idLHS = statement1.loc.id.id;
		if (rhs == null) {
			errorFlag = true;
			return null;
		}
		if (rhs.typeOf() instanceof LLVMVoidType) {
			System.err.println("Invalid Operand: void function found");
			errorFlag = true;
			return null;
		}
		if (!(rhs.typeOf() instanceof LLVMIntegerType)) {
			rhs = new LLVMLoadInstruction(builder, rhs, "1");
		}

		if (namedValues.containsKey(idLHS)) {
			LLVMValue v = namedValues.get(idLHS);
			;
			if (statement1.loc instanceof Location2) {
				LLVMValue v1 = statement1.loc.accept(this);

				new LLVMStoreInstruction(builder, rhs, v1);
				return null;
			}
			if (v.typeOf() instanceof LLVMPointerType) {
				// TODO optimize this..

				if (((LLVMPointerType) v.typeOf()).getElementType() instanceof LLVMIntegerType
						&& rhs.typeOf() instanceof LLVMIntegerType) {
					long lw = ((LLVMIntegerType) ((LLVMPointerType) v.typeOf())
							.getElementType()).getWidth();
					long rw = ((LLVMIntegerType) rhs.typeOf()).getWidth();
					if (lw != rw) {
						operandIncompatible = true;
						errorFlag = true;
						return null;
					}

				}

			}
			switch (statement1.op) {
			case "=":
				new LLVMStoreInstruction(builder, rhs, v);
				return null;

			case "+=":

				new LLVMStoreInstruction(builder, new LLVMAddInstruction(
						builder, rhs, new LLVMLoadInstruction(builder, v, "2"),
						false, "add"), v);

				return null;
			case "-=":

				new LLVMStoreInstruction(builder, new LLVMSubtractInstruction(
						builder, rhs, new LLVMLoadInstruction(builder, v, "2"),
						false, "add"), v);
				return null;

			default:
				System.err.println(statement1.op
						+ ": Assignment operator is expected");
			}

		} else {
			System.err.println("Variable Not Declared");
			errorFlag = true;
		}
		return null;
	}

	@Override
	public LLVMValue visit(ReturnStatement returnStatement) {
		returnStatement.s.accept(this);
		return new LLVMReturnInstruction(builder, null);
	}

	@Override
	public LLVMValue visit(ReturnStatement1 returnStatement1) {
		returnStatement1.s.accept(this);

		funcReturnReq = false;

		LLVMValue val = returnStatement1.expr.accept(this);

		if (!operandIncompatible) {
			if (val == null) {
				errorFlag = true;
				return null;
			}
			if (val.typeOf() instanceof LLVMVoidType) {
				errorFlag = true;
				System.err.println("Void Function doesn't return any value");
				return null;
			}
			if (!(val.typeOf() instanceof LLVMIntegerType)) {
				val = new LLVMLoadInstruction(builder, val, "ret");
			}
			if (val.typeOf() instanceof LLVMIntegerType) {
				if (isBoolFunc) {
					isBoolFunc = false;
					if (((LLVMIntegerType) val.typeOf()).getWidth() == 1) {
						new LLVMReturnInstruction(builder, val);
					} else {
						System.err
								.println("incompatible return type : boolean expected; int found");
						errorFlag = true;
					}
				} else {
					if (((LLVMIntegerType) val.typeOf()).getWidth() == 1) {
						System.err
								.println("incompatible return type : int expected; boolean found");
						errorFlag = true;
					}
					new LLVMReturnInstruction(builder, val);
				}

			}

		} else {
			System.err.println("Operand Incompatible");
			errorFlag = true;
		}
		return null;
	}

	@Override
	public LLVMIntegerType visit(IntegerType integerType) {
		return LLVMIntegerType.i32;
	}

	@Override
	public LLVMIntegerType visit(BooleanType booleanType) {
		return LLVMIntegerType.i1;
	}

	@Override
	public LLVMVoidType visit(Void void1) {
		LLVMVoidType voidType = new LLVMVoidType();
		return voidType;
	}

	@Override
	public LLVMValue visit(callout_arg callout_arg) {
		String arg = callout_arg.s;
		if (arg.charAt(0) == '\"')
			arg = arg.substring(1, arg.length() - 1).trim();
		return new LLVMConstantString(arg, true);

	}

	@Override
	public LLVMValue visit(callout_arg1 callout_arg) {
		return callout_arg.expr.accept(this);
	}

	@Override
	public LLVMValue visit(IntegerLiteral integerLiteral) {
		return LLVMConstantInteger.constantInteger(LLVMIntegerType.i32,
				integerLiteral.value, false);
	}

	@Override
	public LLVMValue visit(BooleanLiteral booleanLiteral) {
		if (booleanLiteral.value.equalsIgnoreCase("True"))
			return LLVMConstantInteger.constantInteger(LLVMIntegerType.i1, 1,
					false);
		if (booleanLiteral.value.equalsIgnoreCase("False"))
			return LLVMConstantInteger.constantInteger(LLVMIntegerType.i1, 0,
					false);
		return null;

	}

	@Override
	public String visit(Return return1) {
		return return1.s;

	}

	@Override
	public String visit(Break break1) {
		return break1.s;

	}

	@Override
	public String visit(Continue continue1) {
		return continue1.s;

	}

	@Override
	public String visit(Class class1) {
		return class1.id;
	}

	@Override
	public String visit(ELSE else1) {
		return else1.s;

	}

	@Override
	public String visit(IF if1) {
		return if1.s1;

	}

	@Override
	public String visit(For for1) {
		return for1.s;

	}

	@Override
	public String visit(StringLiteral stringLiteral) {
		return stringLiteral.s;

	}

	@Override
	public String visit(CallOut callOut) {
		return callOut.s;

	}

	@Override
	public void visit(Field1 field1) {
		field1.id.accept(this);
	}

	@Override
	public LLVMValue visit(Block1 block) {
		for (Var_Decl var : block.vars)
			var.accept(this);
		for (StatementIntf stmt : block.stmts)
			stmt.accept(this);
		return null;
	}

	@Override
	public LLVMValue visit(Block2 block2) {
		for (Var_Decl var : block2.vars)
			var.accept(this);
		return null;

	}

	@Override
	public LLVMValue visit(Block3 block3) {
		for (StatementIntf stmt : block3.stmts)
			if (!breakOrContStat)
				stmt.accept(this);
		return null;
	}

	@Override
	public LLVMValue visit(IfStatement ifStatement) {

		ifStatement.s1.accept(this);

		LLVMFunction iffunc = builder.getInsertBlock().getParent();
		LLVMBasicBlock ifelse = iffunc.appendBasicBlock("if.then");
		LLVMBasicBlock ifend = iffunc.appendBasicBlock("if.end");
		
		LLVMValue cond = ifStatement.expr.accept(this);
		
		this.iffunc1 = iffunc;
		this.ifend1 = ifend;
		this.ifelse1 = ifelse;
		if (!operandIncompatible) {

			if (cond != null) {
				new LLVMBranchInstruction(builder, cond, ifelse, ifend);
				builder.positionBuilderAtEnd(ifelse);
				ifStatement.block.accept(this);
				if (!breakOrContStat) {
					new LLVMBranchInstruction(builder, ifend);
				}
				breakOrContStat = false;

				builder.positionBuilderAtEnd(ifend);
			}

			else {
				builder.positionBuilderAtEnd(ifelse);
				ifStatement.block.accept(this);
				if (!breakOrContStat) {
					new LLVMBranchInstruction(builder, ifend);
				}
				breakOrContStat = false;

				builder.positionBuilderAtEnd(ifend);
			}
		} else {
			builder.positionBuilderAtEnd(ifelse);
			builder.positionBuilderAtEnd(ifend);

			System.err.println("Incompatible Operands in IF condition");
			errorFlag = true;
		}
		return null;
	}

	@Override
	public LLVMValue visit(IfElseStatement ifElseStatement) {

		ifElseStatement.s1.accept(this);
		ifElseStatement.s2.accept(this);
		
		LLVMFunction iffunc = builder.getInsertBlock().getParent();
		LLVMBasicBlock ifelse = iffunc.appendBasicBlock("if.then");
		LLVMBasicBlock ifend = iffunc.appendBasicBlock("if.end");
		LLVMBasicBlock ifelseend = iffunc.appendBasicBlock("ifelse.end");
		this.iffunc1 = iffunc;
		this.ifend1 = ifend;
		this.ifelse1 = ifelse;
		
		LLVMValue cond = ifElseStatement.expr.accept(this);
		
		if (!operandIncompatible) {
			if (cond != null) {
				new LLVMBranchInstruction(builder, cond, ifelse, ifend);
				builder.positionBuilderAtEnd(ifelse);
				ifElseStatement.ifBlock.accept(this);
				if (!breakOrContStat) {
					new LLVMBranchInstruction(builder, ifelseend);
				}
				breakOrContStat = false;
				builder.positionBuilderAtEnd(ifend);
				ifElseStatement.elseBlock.accept(this);
				new LLVMBranchInstruction(builder, ifelseend);
				builder.positionBuilderAtEnd(ifelseend);
			} else {
				builder.positionBuilderAtEnd(ifelse);
				ifElseStatement.ifBlock.accept(this);
				if (!breakOrContStat) {
					new LLVMBranchInstruction(builder, ifelseend);
				}
				breakOrContStat = false;
				builder.positionBuilderAtEnd(ifend);
				ifElseStatement.elseBlock.accept(this);
				new LLVMBranchInstruction(builder, ifelseend);
				builder.positionBuilderAtEnd(ifelseend);
			}
		} else {
			builder.positionBuilderAtEnd(ifelse);
			builder.positionBuilderAtEnd(ifend);

			System.err.println("Incompatible Operands - If Statement");
			errorFlag = true;
		}
		return null;
	}

	@Override
	public LLVMValue visit(Location1 location) {
		String id = location.id.accept(this);
		LLVMValue val = null;
		if (namedValues.containsKey(id)) {
			val = namedValues.get(id);
			if (val == null) {
				System.err.println(id + ": Variable not initilised");
				errorFlag = true;
			}
		} else {
			System.err.println(id + ": Variable not declared");
			errorFlag = true;
		}
		return val;
	}

	@Override
	public LLVMValue visit(Expression1 expression1) {
		LLVMValue lhs = expression1.expr.accept(this);
		LLVMValue rhs = expression1.term1.accept(this);
		if (rhs == null || lhs == null) {
			errorFlag = true;
			return null;
		}

		if (!(lhs.typeOf() instanceof LLVMIntegerType)) {
			lhs = new LLVMLoadInstruction(builder, lhs, "1");
		}
		if (!(rhs.typeOf() instanceof LLVMIntegerType)) {
			rhs = new LLVMLoadInstruction(builder, rhs, "2");
		}
		String op = expression1.operator;

		long lw = ((LLVMIntegerType) lhs.typeOf()).getWidth();
		long rw = ((LLVMIntegerType) rhs.typeOf()).getWidth();
		if (lw != rw) {
			operandIncompatible = true;
			errorFlag = true;
			return null;
		}

		switch (op) {
		case "+":
			return new LLVMAddInstruction(builder, lhs, rhs, false, "add1");
		case "-":
			return new LLVMSubtractInstruction(builder, lhs, rhs, false, "sub1");
		case "/":
			return new LLVMDivideInstruction(builder, lhs, rhs,
					DivisionType.UNSIGNEDINT, "divl");
		case "*":
			return new LLVMMultiplyInstruction(builder, lhs, rhs, false,
					"mul instruction");
		case "%":
			return new LLVMRemainderInstruction(builder, lhs, rhs,
					RemainderType.UNSIGNEDINT, "mod1");

		case ">":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntSGT, lhs, rhs, "gt1");
		case "<":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntSLT, lhs, rhs, "lt1");
		case ">=":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntSGE, lhs, rhs, "ge1");
		case "<=":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntSLE, lhs, rhs, "le1");
		case "==":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntEQ, rhs, lhs, "eq1");
		case "!=":
			return new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntNE, rhs, lhs, "neq1");
		case "||":
			if (rw == 1 && lw == 1) {
				LLVMValue cmp1 = new LLVMIntegerComparison(builder,
						LLVMIntPredicate.LLVMIntEQ, lhs,
						LLVMConstantInteger.constantInteger(LLVMIntegerType.i1,
								0, false), "orOp1");
				LLVMBasicBlock nextCond = iffunc1.appendBasicBlock("next");
				new LLVMBranchInstruction(builder, cmp1, nextCond, ifelse1);
				builder.positionBuilderAtEnd(nextCond);
				LLVMValue cmp2 = new LLVMIntegerComparison(builder,
						LLVMIntPredicate.LLVMIntEQ, rhs,
						LLVMConstantInteger.constantInteger(LLVMIntegerType.i1,
								0, false), "orOp2");
				new LLVMBranchInstruction(builder, cmp2, ifend1, ifelse1);
			} else {
				operandIncompatible = true;
			}

			return null;

		case "&&":
			if (lw == 1 && rw == 1) {

				LLVMValue cmp3 = new LLVMIntegerComparison(builder,
						LLVMIntPredicate.LLVMIntEQ, lhs,
						LLVMConstantInteger.constantInteger(LLVMIntegerType.i1,
								0, false), "andOp1");
				LLVMBasicBlock b1 = iffunc1.appendBasicBlock("next");
				new LLVMBranchInstruction(builder, cmp3, ifend1, b1);
				builder.positionBuilderAtEnd(b1);
				LLVMValue cmp4 = new LLVMIntegerComparison(builder,
						LLVMIntPredicate.LLVMIntEQ, rhs,
						LLVMConstantInteger.constantInteger(LLVMIntegerType.i1,
								0, false), "andOp2");
				new LLVMBranchInstruction(builder, cmp4, ifend1, ifelse1);
			} else {
				operandIncompatible = true;
			}

			return null;
		default:
			System.err.println(op + ": Invalid operator");

		}

		return null;

	}

	@Override
	public LLVMValue visit(Expression2 expression2) {
		String op = expression2.operator;
		LLVMValue val = expression2.term5.accept(this);

		switch (op) {
		case "-":
			if (!(val.typeOf() instanceof LLVMIntegerType)) {
				return new LLVMSubtractInstruction(builder,
						LLVMConstantInteger.constantInteger(
								LLVMIntegerType.i32, 0, false),
						new LLVMLoadInstruction(builder, val, "tmp"), false,
						"u_minus");
			}
			return new LLVMUnaryBitwiseInstruction(
					UnaryBitwiseInstructionType.NEGATIVE, builder, val,
					"u_minus");
		case "!":
			return new LLVMUnaryBitwiseInstruction(
					UnaryBitwiseInstructionType.NOT, builder,
					new LLVMLoadInstruction(builder, val, "uminus"), "u_not");
		default:
			System.err.println(op + ": Invalid Operator");
		}
		return null;

	}

	@Override
	public LLVMValue visit(Expression3 expression3) {
		return expression3.term5.accept(this);
	}

	@Override
	public LLVMValue visit(BreakStatement breakStatement) {
		if (isForStatement) {
			breakStatement.s.accept(this);
			breakOrContStat = true;
			new LLVMBranchInstruction(builder, forEnd1);
		} else {
			errorFlag = true;
			System.err.println("Error: Break must be inside for statement");
		}
		return null;
	}

	@Override
	public LLVMValue visit(ContinueStatement continueStatement) {
		if (isForStatement) {
			continueStatement.s.accept(this);
			breakOrContStat = true;
			new LLVMBranchInstruction(builder, forInc1);
		} else {
			errorFlag = true;
			System.err.println("Error: Continue must be inside for statement");
		}
		return null;
	}

	@Override
	public LLVMValue visit(Location2 location) {

		String id = location.id.accept(this);
		LLVMValue val = null;
		if (namedValues.containsKey(id)) {
			val = namedValues.get(id);

			if (val == null) {
				System.err.println(id + ": Variable not initilised");
				errorFlag = true;
			} else {
				LLVMValue position = location.expr.accept(this);
				if (position.typeOf() instanceof LLVMPointerType) {
					position = new LLVMLoadInstruction(builder, position, "tmp");
				}
				if (position.typeOf() instanceof LLVMIntegerType) {

					LLVMValue in = LLVMConstantInteger.constantInteger(
							LLVMIntegerType.i32, 0, false);
					return new LLVMGetElementPointerInstruction(builder, val,
							new LLVMValue[] { in, position }, "arrayEle");

				}
				System.err.println("Invalid Array Position");
				errorFlag = true;
			}
		} else {
			System.err.println(id + ": Variable not declared");
			errorFlag = true;
		}
		return val;

	}

	@Override
	public LLVMValue visit(ForStatement forStatement) {
		isForStatement = true;
		forStatement.s.accept(this);

		LLVMValue initialVal = forStatement.expr1.accept(this);
		LLVMValue condVal = forStatement.expr2.accept(this);
		if (!operandIncompatible) {
			LLVMFunction forFunc = builder.getInsertBlock().getParent();

			LLVMBasicBlock forCond = forFunc.appendBasicBlock("for.cond");
			if (condVal.typeOf() instanceof LLVMPointerType) {
				condVal = new LLVMLoadInstruction(builder, condVal, "%1");
			}
			if (initialVal.typeOf() instanceof LLVMPointerType) {
				initialVal = new LLVMLoadInstruction(builder, initialVal, "%2");
			}
			LLVMValue cmpResult = null;

			String id = forStatement.id.accept(this);
			LLVMValue idVal = new LLVMStackAllocation(builder,
					initialVal.typeOf(), null, id);
			namedValues.put(id, idVal);
			new LLVMStoreInstruction(builder, initialVal, idVal);

			new LLVMBranchInstruction(builder, forCond);
			builder.positionBuilderAtEnd(forCond);
			cmpResult = new LLVMIntegerComparison(builder,
					LLVMIntPredicate.LLVMIntSLT, new LLVMLoadInstruction(
							builder, idVal, "tmp"), condVal, "cond");
			LLVMBasicBlock forBody = forFunc.appendBasicBlock("for.body");
			LLVMBasicBlock forEnd = forFunc.appendBasicBlock("for.end");
			this.forEnd1 = forEnd;
			new LLVMBranchInstruction(builder, cmpResult, forBody, forEnd);
			builder.positionBuilderAtEnd(forBody);
			forStatement.block.accept(this);
			LLVMBasicBlock forInc = forFunc.appendBasicBlock("for.inc");
			this.forInc1 = forInc;
			if (!breakOrContStat) {
				new LLVMBranchInstruction(builder, forInc);
			}
			breakOrContStat = false;
			builder.positionBuilderAtEnd(forInc);

			LLVMValue newIdval = new LLVMAddInstruction(builder,
					new LLVMLoadInstruction(builder, idVal, "tmp"),
					LLVMConstantInteger.constantInteger(LLVMIntegerType.i32, 1,
							false), false, "inc");

			new LLVMStoreInstruction(builder, newIdval, idVal);

			new LLVMBranchInstruction(builder, forCond);
			builder.positionBuilderAtEnd(forEnd);
		} else {
			System.err
					.println("Initial and Ending expression of for must be of integer type");
			errorFlag = true;
		}

		isForStatement = false;

		return null;
	}

	@Override
	public LLVMValue visit(MethodStatement1 methodStatement1) {

		String functionName = methodStatement1.id.accept(this);

		LLVMFunction f = module.getNamedFunction(functionName);
		if (f.getInstance() == null) {
			System.err.println(functionName
					+ ": Error: No such function exists");
			errorFlag = true;
		} else {
			if (namedValues.containsKey(functionName)) {
				long arg_size = f.countParameters();
				if (arg_size != 0) {
					System.err.println("Error: Invalid Number of arguments");
					errorFlag = true;
				} else {
					return new LLVMCallInstruction(builder, f,
							new LLVMValue[0], "");
				}
			} else {
				System.err
						.println("Recurision is not implemented part of this language");
				errorFlag = true;
			}
		}
		return null;
	}

	@Override
	public LLVMValue visit(MethodStatement2 methodStatement2) {
		String functionName = methodStatement2.id.accept(this);

		LLVMFunction f = module.getNamedFunction(functionName);
		if (f.getInstance() == null) {
			System.err.println(functionName
					+ ": Error: No such function exists");
			errorFlag = true;
		} else {
			if (namedValues.containsKey(functionName)) {
				long arg_size = f.countParameters();
				if (arg_size != methodStatement2.exprs.size()) {
					System.err.println("Error: Invalid Number of arguments");
					errorFlag = true;
				} else {
					LLVMValue[] args1 = new LLVMValue[methodStatement2.exprs
							.size()];
					LLVMValue exprVal = null;
					int i = 0;
					for (ExpressionIntf expr : methodStatement2.exprs) {

						exprVal = expr.accept(this);
						if (exprVal.typeOf() instanceof LLVMPointerType)
							args1[i++] = new LLVMLoadInstruction(builder,
									exprVal, "1" + i);
						else
							args1[i++] = exprVal;
						long lw = ((LLVMIntegerType) args1[i - 1].typeOf())
								.getWidth();
						long rw = ((LLVMIntegerType) f.getParameter(i - 1)
								.typeOf()).getWidth();
						if (lw != rw) {
							System.err
									.println("Formal and actual parameters are not type compatible");
							errorFlag = true;
							return null;
						}
					}

					return new LLVMCallInstruction(builder, f, args1, "");
				}
			} else {
				System.err
						.println("Recurision is not implemented part of this language");
				errorFlag = true;
			}
		}
		return null;

	}

	@Override
	public LLVMValue visit(MethodStatementCallOut1 methodStatementCallOut1) {

		LLVMGlobalVariable s = null;
		int i = 0;
		String ident = "str";
		String functionName = null;

		methodStatementCallOut1.s.accept(this);
		functionName = methodStatementCallOut1.s1.accept(this);
		functionName = functionName.substring(1, functionName.length() - 1)
				.trim();
		LLVMFunction f = null;
		if (module.getNamedFunction(functionName).getInstance() == null) {

			LLVMFunctionType fT = new LLVMFunctionType(LLVMIntegerType.i32,
					new LLVMType[] {}, true);
			f = new LLVMFunction(module, functionName, fT);
			f.setLinkage(LLVMLinkage.LLVMExternalLinkage);
		} else
			f = module.getNamedFunction(functionName);

		LLVMValue[] args = new LLVMValue[methodStatementCallOut1.calloutargs
				.size()];
		LLVMValue in = LLVMConstantInteger.constantInteger(LLVMIntegerType.i32,
				0, false);

		for (ExpressionIntf callout_arg : methodStatementCallOut1.calloutargs) {
			LLVMValue arg = callout_arg.accept(this);
			if (arg == null) {
				errorFlag = true;
				return null;
			}
			if (arg.typeOf() instanceof LLVMIntegerType) {
				args[i++] = arg;
				continue;
			}
			if (arg.typeOf() instanceof LLVMPointerType) {
				args[i++] = new LLVMLoadInstruction(builder, arg, "0");
				continue;
			}
			if (arg.typeOf() instanceof LLVMVoidType) {
				System.err.println("Invalid Operand: void function found");
				errorFlag = true;
				return null;
			}
			long p = ((LLVMArrayType) arg.typeOf()).getLength();
			s = module.addGlobal(new LLVMArrayType(LLVMIntegerType.i8, p),
					ident + i);
			s.setInitializer((LLVMConstant) arg);
			s.setConstant(true);
			s.setLinkage(LLVMLinkage.LLVMPrivateLinkage);

			args[i++] = new LLVMGetElementPointerInstruction(builder, s,
					new LLVMValue[] { in, in }, "" + i);
		}

		return new LLVMCallInstruction(builder, f, args, "call");
	}

	@Override
	public LLVMValue visit(MethodStatementCallOut2 methodStatementCallOut2) {
		String functionName = methodStatementCallOut2.s1.accept(this);
		functionName = functionName.substring(1, functionName.length() - 1)
				.trim();

		LLVMFunction f = null;
		if (module.getNamedFunction(functionName).getInstance() == null) {

			LLVMFunctionType fT = new LLVMFunctionType(LLVMIntegerType.i32,
					new LLVMType[] {}, true);
			f = new LLVMFunction(module, functionName, fT);
			f.setLinkage(LLVMLinkage.LLVMExternalLinkage);
		} else
			f = module.getNamedFunction(functionName);

		return new LLVMCallInstruction(builder, f, new LLVMValue[] {}, "call");

	}

}