%{
	
	import java.lang.Math;
	import java.io.*;
	import java.util.*;

%}

%token INT BOOLEAN IF ELSE FOR RETURN BREAK CONTINUE CLASS VOID CALLOUT TRUE FALSE

%token ASSIGN_OP ID STRING_LITERAL ARITH_OP  REL_OP  EQ_OP  COND_OP INT_LITERAL E_ASSIGN_OP MINUS Program

%left '-' '!' ARITH_OP REL_OP EQ_OP COND_OP

%%

program : CLASS ID '{' declarations '}' 
						{	Decaf_Class program = new Decaf_Class(new Class($1.sval),new Identifier($2.sval),(ArrayList<DeclarationsIntf>)$4.obj);
							program.accept(new VisitorImpl());
						};

declarations : type  fields ';' declarations 
						{ 	ArrayList<DeclarationsIntf> decls=((ArrayList<DeclarationsIntf>)$4.obj); 
						  	decls.add(new FieldDecl((Type)$1.obj, (ArrayList<Fields>)$2.obj)); 
						  	$$.obj=decls; 
						} 
	   | method_decl; 
	   					{  $$=$1; }
	   ;
	   


fields  : field 
						{	ArrayList<Field> field = new ArrayList<Field>(); 
							field.add((Field)$1.obj); 
							$$.obj = field; 
						}
	| field ',' fields 
						{  	ArrayList<Field> field1 = (ArrayList<Field>)$3.obj;
							field1.add((Field)$1.obj); 
							$$.obj=field1;
						}
	;

field : ID  			{	$$.obj = new Field(new Identifier($1.sval));	}
      | ID  '[' INT_LITERAL  ']' 
      					{	$$.obj = new Field1(new Identifier($1.sval), $2.ival);	} 
      ;

method_decl  : type ID  '(' args_decl ')' block method_decl 
						{	ArrayList<DeclarationsIntf> methodDecls1=((ArrayList<DeclarationsIntf>)$7.obj); 
							methodDecls1.add(new MethodDeclaration1((Type)$1.obj,new Identifier($2.sval),(ArrayList<Arguement>)$4.obj,(Block)$6.obj)); 
							$$.obj = methodDecls1;
						}
	     | VOID ID  '(' args_decl ')' block method_decl 
	     				{	ArrayList<DeclarationsIntf> methodDecls2= ((ArrayList<DeclarationsIntf>)$7.obj); 
	     					methodDecls2.add(new MethodDeclaration2((Void)$1.obj,new Identifier($2.sval),(ArrayList<Arguement>)$4.obj,(Block)$6.obj));
	     					$$.obj =methodDecls2;
	     				}
	     | 				{	$$.obj = new ArrayList<MethodDeclarations>();	}
	     ;	

args_decl :arg ',' args_decl 
						{	ArrayList<Arguement> argsDecl=((ArrayList<Arguement>)$3.obj) ; 
							argsDecl.add((Arguement)$1.obj); 
							$$.obj= argsDecl;
						}
		| arg 
						{	ArrayList<Arguement> argsDecl2=new ArrayList<Arguement>(); 
							argsDecl2.add((Arguement)$1.obj); 
							$$.obj= argsDecl2;
						}
	  	| 				{	$$.obj=new ArrayList<Arguement>();	}
	  	;
	  	
arg : type ID ;  		{	$$.obj= new Arguement1((Type)$1.obj, new Identifier($2.sval));	}
	;
	
vars : ID ';'			{ 	ArrayList<Identifier> idList=new ArrayList<Identifier>();
							idList.add(new Identifier($1.sval)); 
							$$.obj=idList; 
						}
     | ID ',' vars 		{	ArrayList<Identifier> idList2=((ArrayList<Identifier>)$3.obj);
     						idList2.add(new Identifier($1.sval));
     						$$.obj=idList2;
     					} 
     ;

var_decl : type vars 	{	$$.obj = new Var_Decl((Type)$1.obj,(ArrayList<Identifier>) $2.obj); 	} 
		 ;

var_decls : var_decl var_decls 
						{	ArrayList<Var_Decl> var_decls = ((ArrayList<Var_Decl>)$2.obj);
							var_decls.add((Var_Decl) $1.obj); 
							$$.obj =var_decls;
						}
	 | var_decl 		{	ArrayList<Var_Decl> var_decls1= new ArrayList<Var_Decl>(); 
	 						var_decls1.add((Var_Decl)$1.obj); 
	 						$$.obj =var_decls1;
	 					}
	 ;

block  : '{' block_body '}' 
						{	$$ = $2;	}
       ;

statements : statement 	{	ArrayList<StatementIntf> statement1 = new ArrayList<StatementIntf>();
							statement1.add((StatementIntf)$1.obj);  
							$$.obj = statement1;
						}
	   | statement statements 
	   					{	ArrayList<StatementIntf> statement2 = ((ArrayList<StatementIntf>)$1.obj);
	   						statement2.add((StatementIntf)$1.obj); 
	   						$$.obj = statement2;
	   					};

block_body : var_decls statements 
						{	if($1.obj!=null && $2.obj!=null){
								$$.obj= new Block1((ArrayList<Var_Decl>)$1.obj, (ArrayList<StatementIntf>)$2.obj);
								
							}
							if($1.obj!=null && $2.obj==null){
								$$.obj= new Block2((ArrayList<Var_Decl>)$1.obj);
							}
							if ($1.obj==null && $2.obj!=null){
								$$.obj= new Block3((ArrayList<StatementIntf>)$2.obj);
							}
						}
	   | var_decls   	{	$$.obj = new Block2((ArrayList<Var_Decl>)$1.obj);	} 
	   | statements 	{	$$.obj = new Block3((ArrayList<StatementIntf>)$1.obj);	} 
	   |				{	} 
	   ;

type : INT 				{	$$.obj=new IntegerType($1.sval);	}
     | BOOLEAN 			{	$$.obj=new BooleanType($1.sval);	} 
     ;

statement: location ASSGN_OP expr ';' 
						{ 	$$.obj = new Statement1((Location)$1.obj,(ExpressionIntf)$3.obj, (char)$2.sval.charAt(0));	} 
	 	 | method_call ';' 
	 	 				{	$$=$1;	}
         | IF '(' expr  ')' block  ELSE block 
         				{	$$.obj = new IfElseStatement(new IF($1.sval),(ExpressionIntf)$3.obj,(Block)$5.obj, new ELSE($6.sval),(Block)$7.obj);	} 
	 	 | IF '(' expr  ')' block  
	 	 				{	$$.obj = new IfStatement(new IF($1.sval),(ExpressionIntf)$3.obj,(Block)$5.obj);	}
         | FOR ID  E_ASSIGN_OP expr ',' expr  block 
         				{	$$.obj = new ForStatement(new For($1.sval),new Identifier($2.sval),(char)$3.sval.charAt(0),(ExpressionIntf)$4.obj,(ExpressionIntf)$6.obj,(Block)$7.obj); 	}
         | RETURN ';' 	{	$$.obj= new ReturnStatement(new Return($1.sval));	}
	 	 | RETURN expr ';' 
	 	 				{	$$.obj= new ReturnStatement1(new Return($1.sval),(ExpressionIntf)$2.obj);	}
         | BREAK ';'	{	$$.obj= new BreakStatement(new Break($1.sval));		}
         | CONTINUE ';' {	$$.obj= new ContinueStatement(new Continue($1.sval));	} 
         | block  		{	$$=$1;	};

ASSGN_OP : ASSIGN_OP 	{	$$=$1;	}
	 | E_ASSIGN_OP 		{	$$=$1;	}
	 ;
	 
exprs : expr 			{  	ArrayList<ExpressionIntf> exprs1= new ArrayList<ExpressionIntf>();
							exprs1.add((ExpressionIntf)$1.obj);  
							$$.obj = exprs1;
						}
	| expr ',' exprs  	{ 	ArrayList<ExpressionIntf> exprs2= ((ArrayList<ExpressionIntf>)($3.obj));
							exprs2.add((ExpressionIntf)$1.obj);  
							$$.obj =exprs2;
						}
	;

callout_args : callout_arg 
						{	ArrayList<callout_arg> calloutargs1 =  new ArrayList<callout_arg>();
							calloutargs1.add((callout_arg)$1.obj); 
							$$.obj = calloutargs1;
						} 
             | callout_arg ',' callout_args ;
             			{	ArrayList<callout_arg> calloutargs2 =  ((ArrayList<callout_arg>)($3.obj));
             				calloutargs2.add((callout_arg)$1.obj);
             				$$.obj = calloutargs2;
             			};

method_call  : method_name '('   ')' 
						{	$$.obj= new MethodStatement1((Identifier)$1.obj);	}
	 | method_name '(' exprs ')'  
	 					{	$$.obj= new MethodStatement2((Identifier)$1.obj,(ArrayList<ExpressionIntf>)$3.obj);		}
     | CALLOUT '(' STRING_LITERAL ',' callout_args ')'  
     					{	$$.obj = new MethodStatementCallOut1 (new CallOut($1.sval), new StringLiteral($3.sval),(ArrayList<callout_arg>) $5.obj);	}
	 | CALLOUT '(' STRING_LITERAL  ')' 
	 					{	$$.obj = new MethodStatementCallOut2 (new CallOut($1.sval), new StringLiteral($3.sval));	} 
	 ;

method_name : ID 		{	$$.obj = new Identifier($1.sval); }
			;

location : ID			{	$$.obj = new Location1(new Identifier($1.sval)); 	} 
         | ID  '[' expr  ']' 
         				{	$$.obj = new Location2(new Identifier($1.sval), (ExpressionIntf) $2.obj); 	} 
         ;

ARTH_OP : ARITH_OP		{	$$=$1;	}
	| MINUS 			{	$$=$1;	}
	;

expr  : expr ARTH_OP term1 
						{	$$.obj = new Expression1((ExpressionIntf)$1.obj,(ExpressionIntf)$3.obj, (char)$2.sval.charAt(0));	}
      | term1 			{	$$=$1;	} 
      ;

term1 : term1 REL_OP term2
						{	$$.obj = new Expression1((ExpressionIntf)$1.obj,(ExpressionIntf)$3.obj, (char)$2.sval.charAt(0));	}
      | term2 			{	$$=$1 ;} 
      ;


term2 : term2 EQ_OP term3 
						{	$$.obj = new Expression1((ExpressionIntf)$1.obj,(ExpressionIntf)$3.obj, (char)$2.sval.charAt(0));	}
      | term3 			{ 	$$=$1 ;	} 
      ;


term3 : term3 COND_OP term4 
						{	$$.obj = new Expression1((ExpressionIntf)$1.obj,(ExpressionIntf)$3.obj, (char)$2.sval.charAt(0));	}
      | term4 			{	$$=$1; } 
      ;

term4 : location    	{	$$=$1; 	}
      | method_call 	{ 	$$=$1; 	}
      | literal     	{	$$=$1;	}
      | MINUS term4 	{	$$.obj = new Expression2((ExpressionIntf)$2.obj, (char)$1.sval.charAt(0));	}
      | '!' term4 		{	$$.obj = new Expression2((ExpressionIntf)$2.obj, (char)$1.sval.charAt(0));	}
      | '(' expr  ')' 	{	$$.obj = new Expression3((ExpressionIntf)$2.obj);	}
      ;

callout_arg  : expr  	{	$$=$1;	}
             | STRING_LITERAL 
             			{	$$.obj = new callout_arg($1.sval);	} 
             ;

bool_literal : TRUE {$$=$1;}
             | FALSE {$$=$1;}
             ;

literal  : INT_LITERAL  {	$$.obj = new IntegerLiteral($1.ival);	}
//       | CHAR_LITERAL  
         | bool_literal {	$$.obj = new BooleanLiteral($1.sval);	}
         ;

%%


  
  private Yylex lexer;

  
  private int yylex () {
  		int yyl_return = -1;
    	try {
      		yyl_return = lexer.yylex();
    	}
    	catch (IOException e) {
      		System.err.println("IO error :"+e);
    	}
    	return yyl_return;
  }

  
  public void yyerror (String error) {
  		System.err.println ("Error: " + error);
  }

  
  public Parser(Reader r) {
  		lexer = new Yylex(r, this);
  }

  
  public static void main(String args[]) throws IOException {
  
  	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Parser yyparser = new Parser(br);
    yyparser.yyparse();
        
  }