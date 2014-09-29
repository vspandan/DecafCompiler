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
						

declarations : type  fields ';' declarations 
	   | method_decl; 
	   ;
	   


fields  : field 
	| field ',' fields 
	;

field : ID  
      | ID  '[' INT_LITERAL  ']' 
      ;

method_decl  : type ID  '(' args_decl ')' block method_decl 
	     | VOID ID  '(' args_decl ')' block method_decl 
	     | 	
	     ;	

args_decl :arg ',' args_decl 
		| arg 
	  	| 
	  	;
	  	
arg : type ID ;  
	;
	
vars : ID ';'	
     | ID ',' vars 
     ;

var_decl : type vars 
		 ;

var_decls : var_decl var_decls 
	 | var_decl
	 ;

block  : '{' block_body '}' 
       ;

statements : statement 
	   | statement statements 

block_body : var_decls statements 
	   | var_decls 
	   | statements
	   |		
	   ;

type : INT 		
     | BOOLEAN 		
     ;

statement: location ASSGN_OP expr ';' 
	 	 | method_call ';' 
         | IF '(' expr  ')' block  ELSE block 
 	 | IF '(' expr  ')' block  
         | FOR ID  E_ASSIGN_OP expr ',' expr  block 
         | RETURN ';' 
 	 | RETURN expr ';' 
         | BREAK ';'	
         | CONTINUE ';' 
         | block  	

ASSGN_OP : ASSIGN_OP 	
	 | E_ASSIGN_OP 	
	 ;
	 
exprs : expr 		
	| expr ',' exprs  
	;

callout_args : callout_arg 
             | callout_arg ',' callout_args 
	     ;

method_call  : method_name '('   ')' 
	 | method_name '(' exprs ')'  
     | CALLOUT '(' STRING_LITERAL ',' callout_args ')'  
	 | CALLOUT '(' STRING_LITERAL  ')' 
	 ;

method_name : ID ;

location : ID	
         | ID  '[' expr  ']' 
         ;

ARTH_OP : ARITH_OP	
	| MINUS 	
	;

expr  : expr ARTH_OP term1 
      | term1 
      ;

term1 : term1 REL_OP term2
      | term2 	
      ;


term2 : term2 EQ_OP term3 
      | term3 	
      ;


term3 : term3 COND_OP term4 
      | term4 		
      ;

term4 : location    	
      | method_call 	
      | literal     	
      | MINUS term4 	
      | '!' term4 	
      | '(' expr  ')' 	
      ;

callout_arg  : expr  	
             | STRING_LITERAL 
             ;

bool_literal : TRUE 
             | FALSE
             ;

literal  : INT_LITERAL 
//       | CHAR_LITERAL  
         | bool_literal
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
