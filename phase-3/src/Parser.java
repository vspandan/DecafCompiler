//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "parser.y"
	
	import java.lang.Math;
	import java.io.*;
	import java.util.*;

//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short INT=257;
public final static short BOOLEAN=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short FOR=261;
public final static short RETURN=262;
public final static short BREAK=263;
public final static short CONTINUE=264;
public final static short CLASS=265;
public final static short VOID=266;
public final static short CALLOUT=267;
public final static short TRUE=268;
public final static short FALSE=269;
public final static short ASSIGN_OP=270;
public final static short ID=271;
public final static short STRING_LITERAL=272;
public final static short ARITH_OP=273;
public final static short REL_OP=274;
public final static short EQ_OP=275;
public final static short COND_OP=276;
public final static short INT_LITERAL=277;
public final static short E_ASSIGN_OP=278;
public final static short MINUS=279;
public final static short Program=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    5,    5,    4,    4,    4,
    6,    6,    6,    8,    9,    9,   10,   11,   11,    7,
   13,   13,   12,   12,   12,   12,    2,    2,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   16,   16,
   19,   19,   20,   20,   18,   18,   18,   18,   22,   15,
   15,   23,   23,   17,   17,   24,   24,   25,   25,   26,
   26,   27,   27,   27,   27,   27,   27,   21,   21,   29,
   29,   28,   28,
};
final static short yylen[] = {                            2,
    5,    4,    1,    1,    3,    1,    4,    7,    7,    0,
    3,    1,    0,    2,    2,    3,    2,    2,    1,    3,
    1,    2,    2,    1,    1,    0,    1,    1,    4,    2,
    7,    5,    7,    2,    3,    2,    2,    1,    1,    1,
    1,    3,    1,    3,    3,    4,    6,    4,    1,    1,
    4,    1,    1,    3,    1,    3,    1,    3,    1,    3,
    1,    1,    1,    1,    2,    2,    3,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   27,   28,    0,    0,    0,    3,
    0,    1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    2,    0,    5,   14,    0,
    0,    7,    0,    0,    0,   11,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   38,    0,    0,    0,   25,
    0,    0,    0,    0,    0,    9,    8,    0,    0,   70,
   71,   72,    0,    0,   34,    0,   62,    0,   63,    0,
    0,    0,   61,   64,   73,   36,   37,    0,    0,    0,
   17,   18,   23,   20,   22,   39,   40,    0,   30,    0,
    0,    0,    0,   65,   66,    0,   52,   53,   35,    0,
    0,    0,    0,    0,    0,   15,    0,    0,   45,    0,
    0,    0,    0,   67,    0,    0,    0,   60,    0,   48,
   51,   16,   29,    0,   46,    0,    0,   69,    0,    0,
    0,   42,    0,    0,   47,    0,   31,   33,   44,
};
final static short yydgoto[] = {                          2,
    8,   21,   14,   10,   15,   22,   46,   23,   81,   47,
   48,   49,   50,   51,   67,   88,  110,   69,  111,  130,
  131,   54,  100,   70,   71,   72,   73,   74,   75,
};
final static short yysindex[] = {                      -250,
 -243,    0,  -90, -184,    0,    0, -236,  -81, -220,    0,
   40,    0,  -24,   26,   47, -157, -183, -157, -184, -173,
 -169,   62,   61,   11,   66,    0,   17,    0,    0,  -13,
 -157,    0,  -13, -104, -184,    0, -184,   69, -159,  -33,
   55,   57,   79,   30, -148,    0, -157,  -53,    1,    0,
  -53, -249,   65,   85, -144,    0,    0,   -2, -150,    0,
    0,    0,   -2,   -2,    0,   -2,    0,  -58,    0, -143,
 -145, -142,    0,    0,    0,    0,    0, -140,   -2,  -17,
    0,    0,    0,    0,    0,    0,    0,   -2,    0,  -28,
   93,  -31,   -2,    0,    0,  -18,    0,    0,    0,   -2,
   -2,   -2,   -2,    8,  -89,    0, -148,  -56,    0,  -42,
   94,  -13,   25,    0, -143, -145, -142,    0,  -15,    0,
    0,    0,    0,   -2,    0, -123,   -2,    0, -190,   97,
   95,    0,  -13, -117,    0,  -15,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,   -3,    0,   83,  103,    0,  103,   15,    0,
    0,    0,  105,    0,    0,    0,   -3,    0,    0,    0,
  103,    0,    0,   23,   15,    0,   15,    0,    0,    0,
    0,    0,    0,    6,    0,    0,  -93,   24,    0,    0,
   36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   18,
   20,   -1,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  106,
    0,    0,    0,    0,   27,   22,   13,    0,    0,    0,
    0,    0,    0,    0,    0,  -70,    0,    0,   43,    0,
  110,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  133,   41,  144,   60,    0,   -7,  -16,    0,   58,    0,
  125,    0,   42,    0,  -14,    0,  249,  -12,   49,   39,
    0,    0,    0,   76,   78,   75,  -55,    0,    0,
};
final static int YYTABLESIZE=385;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         64,
   99,  124,  123,  121,   64,   34,   66,   94,   95,  112,
   25,   66,  109,   35,    1,   18,   37,   64,   34,   52,
   86,   53,  114,   36,   66,   65,  107,    3,   87,   19,
   64,   19,    4,   52,   11,   53,   52,   66,   53,   59,
    6,  106,   59,   12,    9,   49,   50,  118,  120,   50,
   13,  119,   32,   58,   32,    6,   58,   59,   55,    9,
   57,   55,   56,   57,   50,   56,   17,   54,  127,   34,
   54,   58,    5,    6,   45,   55,   55,   55,   57,   16,
   56,    7,   97,   68,   19,   54,   68,   45,   98,   83,
   20,   59,   85,   24,   56,  126,   57,   27,   50,    5,
    6,   29,   30,   32,   31,   58,   33,   17,   58,   34,
   55,   59,   57,   76,   56,   77,  137,  138,   78,   54,
   79,   59,   80,   89,   90,   84,   91,   93,   50,  102,
  101,  104,   18,  103,  125,   58,  133,  135,  136,   10,
   55,    4,   57,   13,   56,   12,   41,   26,   24,   54,
   43,   26,    5,    6,   38,   97,   39,   40,   41,   42,
   21,   98,   43,   28,  122,   19,   44,   19,   19,   19,
   19,   82,  132,   19,  139,  115,  117,   19,  116,    0,
    0,    0,    0,   97,    0,    0,    0,    0,   32,   98,
   32,   32,   32,   32,    0,    0,   32,    0,    0,    0,
   32,    0,    0,    0,    0,   38,    0,   39,   40,   41,
   42,    0,    0,   43,   97,    0,   97,   44,    0,    0,
   98,    0,   98,    0,    0,    0,    0,    0,    0,    0,
   97,    0,    0,   43,   60,   61,   98,   44,   43,   60,
   61,   97,   44,   62,    0,   63,    0,   98,   62,    0,
   63,   43,   60,   61,   97,   44,  128,    0,    0,    0,
   98,   62,    0,   63,   43,   60,   61,    0,   44,    0,
    0,   59,   59,   59,   62,   50,   63,   59,   50,   50,
   50,   50,    0,   50,   50,   58,   58,   58,   68,    0,
   55,   58,   57,   57,   56,   56,   55,   97,   57,   54,
   56,    0,    0,   98,    0,   54,   92,    0,    0,    0,
    0,    0,    0,    0,   96,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  105,    0,    0,
    0,    0,    0,    0,    0,    0,  108,    0,    0,    0,
    0,  113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  129,    0,    0,
    0,    0,    0,    0,    0,  134,    0,    0,    0,    0,
    0,    0,    0,    0,  129,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   44,   59,   93,   33,  123,   40,   63,   64,   41,
   18,   40,   41,   30,  265,   40,   33,   33,  123,   34,
  270,   34,   41,   31,   40,   59,   44,  271,  278,  123,
   33,  125,  123,   48,  271,   48,   51,   40,   51,   41,
   44,   59,   44,  125,    4,   40,   41,  103,   41,   44,
  271,   44,  123,   41,  125,   59,   44,   59,   41,   19,
   41,   44,   41,   44,   59,   44,   91,   41,   44,  123,
   44,   59,  257,  258,   34,   35,   59,   37,   59,   40,
   59,  266,  273,   41,   59,   59,   44,   47,  279,   48,
   44,   93,   51,  277,   35,  112,   37,  271,   93,  257,
  258,  271,   41,   93,   44,   93,   41,   91,   40,  123,
   93,  271,   93,   59,   93,   59,  133,  134,   40,   93,
   91,  123,  271,   59,   40,  125,  271,  278,  123,  275,
  274,  272,   40,  276,   41,  123,  260,   41,   44,  125,
  123,   59,  123,   41,  123,   41,   41,  125,  125,  123,
   41,   19,  257,  258,  259,  273,  261,  262,  263,  264,
  125,  279,  267,   20,  107,  259,  271,  261,  262,  263,
  264,   47,  124,  267,  136,  100,  102,  271,  101,   -1,
   -1,   -1,   -1,  273,   -1,   -1,   -1,   -1,  259,  279,
  261,  262,  263,  264,   -1,   -1,  267,   -1,   -1,   -1,
  271,   -1,   -1,   -1,   -1,  259,   -1,  261,  262,  263,
  264,   -1,   -1,  267,  273,   -1,  273,  271,   -1,   -1,
  279,   -1,  279,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  273,   -1,   -1,  267,  268,  269,  279,  271,  267,  268,
  269,  273,  271,  277,   -1,  279,   -1,  279,  277,   -1,
  279,  267,  268,  269,  273,  271,  272,   -1,   -1,   -1,
  279,  277,   -1,  279,  267,  268,  269,   -1,  271,   -1,
   -1,  273,  274,  275,  277,  270,  279,  279,  273,  274,
  275,  276,   -1,  278,  279,  273,  274,  275,   40,   -1,
  273,  279,  273,  274,  273,  274,  279,  273,  279,  273,
  279,   -1,   -1,  279,   -1,  279,   58,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   66,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   79,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   88,   -1,   -1,   -1,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  119,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  127,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  136,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,null,null,null,"'('","')'",null,null,"','",
"'-'",null,null,null,null,null,null,null,null,null,null,null,null,null,"';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"INT","BOOLEAN","IF","ELSE","FOR",
"RETURN","BREAK","CONTINUE","CLASS","VOID","CALLOUT","TRUE","FALSE","ASSIGN_OP",
"ID","STRING_LITERAL","ARITH_OP","REL_OP","EQ_OP","COND_OP","INT_LITERAL",
"E_ASSIGN_OP","MINUS","Program",
};
final static String yyrule[] = {
"$accept : program",
"program : CLASS ID '{' declarations '}'",
"declarations : type fields ';' declarations",
"declarations : method_decl",
"fields : field",
"fields : field ',' fields",
"field : ID",
"field : ID '[' INT_LITERAL ']'",
"method_decl : type ID '(' args_decl ')' block method_decl",
"method_decl : VOID ID '(' args_decl ')' block method_decl",
"method_decl :",
"args_decl : arg ',' args_decl",
"args_decl : arg",
"args_decl :",
"arg : type ID",
"vars : ID ';'",
"vars : ID ',' vars",
"var_decl : type vars",
"var_decls : var_decl var_decls",
"var_decls : var_decl",
"block : '{' block_body '}'",
"statements : statement",
"statements : statement statements",
"block_body : var_decls statements",
"block_body : var_decls",
"block_body : statements",
"block_body :",
"type : INT",
"type : BOOLEAN",
"statement : location ASSGN_OP expr ';'",
"statement : method_call ';'",
"statement : IF '(' expr ')' block ELSE block",
"statement : IF '(' expr ')' block",
"statement : FOR ID E_ASSIGN_OP expr ',' expr block",
"statement : RETURN ';'",
"statement : RETURN expr ';'",
"statement : BREAK ';'",
"statement : CONTINUE ';'",
"statement : block",
"ASSGN_OP : ASSIGN_OP",
"ASSGN_OP : E_ASSIGN_OP",
"exprs : expr",
"exprs : expr ',' exprs",
"callout_args : callout_arg",
"callout_args : callout_arg ',' callout_args",
"method_call : method_name '(' ')'",
"method_call : method_name '(' exprs ')'",
"method_call : CALLOUT '(' STRING_LITERAL ',' callout_args ')'",
"method_call : CALLOUT '(' STRING_LITERAL ')'",
"method_name : ID",
"location : ID",
"location : ID '[' expr ']'",
"ARTH_OP : ARITH_OP",
"ARTH_OP : MINUS",
"expr : expr ARTH_OP term1",
"expr : term1",
"term1 : term1 REL_OP term2",
"term1 : term2",
"term2 : term2 EQ_OP term3",
"term2 : term3",
"term3 : term3 COND_OP term4",
"term3 : term4",
"term4 : location",
"term4 : method_call",
"term4 : literal",
"term4 : MINUS term4",
"term4 : '!' term4",
"term4 : '(' expr ')'",
"callout_arg : expr",
"callout_arg : STRING_LITERAL",
"bool_literal : TRUE",
"bool_literal : FALSE",
"literal : INT_LITERAL",
"literal : bool_literal",
};

//#line 255 "parser.y"


  
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
//#line 423 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 18 "parser.y"
{	Decaf_Class program = new Decaf_Class(new Class(val_peek(4).sval),new Identifier(val_peek(3).sval),(ArrayList<DeclarationsIntf>)val_peek(1).obj);
							program.accept(new VisitorImpl());
						}
break;
case 2:
//#line 23 "parser.y"
{ 	ArrayList<DeclarationsIntf> decls= new ArrayList<DeclarationsIntf>();
							decls.add(new FieldDecl((Type)val_peek(3).obj, (ArrayList<Fields>)val_peek(2).obj));
							decls.addAll((ArrayList<DeclarationsIntf>)val_peek(0).obj); 
						  	 
						  	yyval.obj=decls; 
						}
break;
case 3:
//#line 30 "parser.y"
{  yyval=val_peek(0); }
break;
case 4:
//#line 36 "parser.y"
{  	ArrayList<Fields> field = new ArrayList<Fields>();
							field.add((Fields)val_peek(0).obj);
							yyval.obj = field; 
						}
break;
case 5:
//#line 41 "parser.y"
{  	ArrayList<Fields> field1 = (ArrayList<Fields>)val_peek(0).obj;
							field1.add((Fields)val_peek(2).obj); 
							yyval.obj=field1;
						}
break;
case 6:
//#line 47 "parser.y"
{	yyval.obj = new Field(new Identifier(val_peek(0).sval));	}
break;
case 7:
//#line 49 "parser.y"
{	yyval.obj = new Field1(new Identifier(val_peek(3).sval), val_peek(1).ival); }
break;
case 8:
//#line 53 "parser.y"
{	ArrayList<DeclarationsIntf> methodDecls1=new ArrayList<DeclarationsIntf>();
							methodDecls1.add(new MethodDeclaration1((Type)val_peek(6).obj,new Identifier(val_peek(5).sval),(ArrayList<Arguement>)val_peek(3).obj,(Block)val_peek(1).obj));
							methodDecls1.addAll((ArrayList<DeclarationsIntf>)val_peek(0).obj); 
							yyval.obj = methodDecls1;
						}
break;
case 9:
//#line 59 "parser.y"
{	ArrayList<DeclarationsIntf> methodDecls2=new ArrayList<DeclarationsIntf>();
	     					 
	     					methodDecls2.add(new MethodDeclaration2(new Void(val_peek(6).sval),new Identifier(val_peek(5).sval),(ArrayList<Arguement>)val_peek(3).obj,(Block)val_peek(1).obj));
	     					methodDecls2.addAll((ArrayList<DeclarationsIntf>)val_peek(0).obj);
	     					yyval.obj =methodDecls2;
	     				}
break;
case 10:
//#line 65 "parser.y"
{	yyval.obj = new ArrayList<MethodDeclarations>();	}
break;
case 11:
//#line 69 "parser.y"
{	ArrayList<Arguement> argsDecl=((ArrayList<Arguement>)val_peek(0).obj) ; 
							argsDecl.add((Arguement)val_peek(2).obj); 
							yyval.obj= argsDecl;
						}
break;
case 12:
//#line 74 "parser.y"
{	ArrayList<Arguement> argsDecl2=new ArrayList<Arguement>(); 
							argsDecl2.add((Arguement)val_peek(0).obj); 
							yyval.obj= argsDecl2;
						}
break;
case 13:
//#line 78 "parser.y"
{	yyval.obj=new ArrayList<Arguement>();	}
break;
case 14:
//#line 81 "parser.y"
{	yyval.obj= new Arguement1((Type)val_peek(1).obj, new Identifier(val_peek(0).sval));	}
break;
case 15:
//#line 84 "parser.y"
{ 	ArrayList<Identifier> idList=new ArrayList<Identifier>();
							idList.add(new Identifier(val_peek(1).sval)); 
							yyval.obj=idList; 
						}
break;
case 16:
//#line 88 "parser.y"
{	ArrayList<Identifier> idList2=((ArrayList<Identifier>)val_peek(0).obj);
     						idList2.add(new Identifier(val_peek(2).sval));
     						yyval.obj=idList2;
     					}
break;
case 17:
//#line 94 "parser.y"
{	yyval.obj = new Var_Decl((Type)val_peek(1).obj,(ArrayList<Identifier>) val_peek(0).obj); 	}
break;
case 18:
//#line 98 "parser.y"
{	ArrayList<Var_Decl> var_decls = ((ArrayList<Var_Decl>)val_peek(0).obj);
							var_decls.add((Var_Decl) val_peek(1).obj); 
							yyval.obj =var_decls;
						}
break;
case 19:
//#line 102 "parser.y"
{	ArrayList<Var_Decl> var_decls1= new ArrayList<Var_Decl>(); 
	 						var_decls1.add((Var_Decl)val_peek(0).obj); 
	 						yyval.obj =var_decls1;
	 					}
break;
case 20:
//#line 109 "parser.y"
{	yyval = val_peek(1);	}
break;
case 21:
//#line 112 "parser.y"
{	ArrayList<StatementIntf> statement1 = new ArrayList<StatementIntf>();
							statement1.add((StatementIntf)val_peek(0).obj);  
							yyval.obj = statement1;
						}
break;
case 22:
//#line 117 "parser.y"
{	ArrayList<StatementIntf> statement2 = new ArrayList<StatementIntf>();
	   						statement2.add((StatementIntf)val_peek(1).obj);
	   						statement2.addAll((ArrayList<StatementIntf>)val_peek(0).obj); 
	   						yyval.obj = statement2;
	   					}
break;
case 23:
//#line 124 "parser.y"
{	if(val_peek(1).obj!=null && val_peek(0).obj!=null){
								yyval.obj= new Block1((ArrayList<Var_Decl>)val_peek(1).obj, (ArrayList<StatementIntf>)val_peek(0).obj);
								
							}
							if(val_peek(1).obj!=null && val_peek(0).obj==null){
								yyval.obj= new Block2((ArrayList<Var_Decl>)val_peek(1).obj);
							}
							if (val_peek(1).obj==null && val_peek(0).obj!=null){
								yyval.obj= new Block3((ArrayList<StatementIntf>)val_peek(0).obj);
							}
						}
break;
case 24:
//#line 135 "parser.y"
{	yyval.obj = new Block2((ArrayList<Var_Decl>)val_peek(0).obj);	}
break;
case 25:
//#line 136 "parser.y"
{	yyval.obj = new Block3((ArrayList<StatementIntf>)val_peek(0).obj);	}
break;
case 26:
//#line 137 "parser.y"
{	yyval.obj=new Block4();}
break;
case 27:
//#line 140 "parser.y"
{	yyval.obj=new IntegerType(val_peek(0).sval);	}
break;
case 28:
//#line 141 "parser.y"
{	yyval.obj=new BooleanType(val_peek(0).sval);	}
break;
case 29:
//#line 145 "parser.y"
{ 	yyval.obj = new Statement1((Location)val_peek(3).obj,(ExpressionIntf)val_peek(1).obj, val_peek(2).sval);	}
break;
case 30:
//#line 147 "parser.y"
{	yyval=val_peek(1);	}
break;
case 31:
//#line 149 "parser.y"
{	yyval.obj = new IfElseStatement(new IF(val_peek(6).sval),(ExpressionIntf)val_peek(4).obj,(Block)val_peek(2).obj, new ELSE(val_peek(1).sval),(Block)val_peek(0).obj);	}
break;
case 32:
//#line 151 "parser.y"
{	yyval.obj = new IfStatement(new IF(val_peek(4).sval),(ExpressionIntf)val_peek(2).obj,(Block)val_peek(0).obj);	}
break;
case 33:
//#line 153 "parser.y"
{	yyval.obj = new ForStatement(new For(val_peek(6).sval),new Identifier(val_peek(5).sval),val_peek(4).sval,(ExpressionIntf)val_peek(3).obj,(ExpressionIntf)val_peek(1).obj,(Block)val_peek(0).obj); 	}
break;
case 34:
//#line 154 "parser.y"
{	yyval.obj= new ReturnStatement(new Return(val_peek(1).sval));	}
break;
case 35:
//#line 156 "parser.y"
{	yyval.obj= new ReturnStatement1(new Return(val_peek(2).sval),(ExpressionIntf)val_peek(1).obj);	}
break;
case 36:
//#line 157 "parser.y"
{	yyval.obj= new BreakStatement(new Break(val_peek(1).sval));		}
break;
case 37:
//#line 158 "parser.y"
{	yyval.obj= new ContinueStatement(new Continue(val_peek(1).sval));	}
break;
case 38:
//#line 159 "parser.y"
{	yyval=val_peek(0);	}
break;
case 39:
//#line 161 "parser.y"
{	yyval=val_peek(0);	}
break;
case 40:
//#line 162 "parser.y"
{	yyval=val_peek(0);	}
break;
case 41:
//#line 165 "parser.y"
{  	ArrayList<ExpressionIntf> exprs1= new ArrayList<ExpressionIntf>();
							exprs1.add((ExpressionIntf)val_peek(0).obj);  
							yyval.obj = exprs1;
						}
break;
case 42:
//#line 169 "parser.y"
{ 	ArrayList<ExpressionIntf> exprs2= ((ArrayList<ExpressionIntf>)(val_peek(0).obj));
							exprs2.add((ExpressionIntf)val_peek(2).obj);  
							yyval.obj =exprs2;
						}
break;
case 43:
//#line 176 "parser.y"
{	ArrayList<ExpressionIntf> calloutargs1 =  new ArrayList<ExpressionIntf>();
							calloutargs1.add((ExpressionIntf)val_peek(0).obj); 
							yyval.obj = calloutargs1;
						}
break;
case 44:
//#line 181 "parser.y"
{	ArrayList<ExpressionIntf> calloutargs2 =  new ArrayList<ExpressionIntf>();
             				calloutargs2.add((ExpressionIntf)val_peek(2).obj);
             				calloutargs2.addAll((ArrayList<ExpressionIntf>)(val_peek(0).obj));
             				
             				yyval.obj = calloutargs2;
             			}
break;
case 45:
//#line 189 "parser.y"
{	yyval.obj= new MethodStatement1((Identifier)val_peek(2).obj);	}
break;
case 46:
//#line 191 "parser.y"
{	yyval.obj= new MethodStatement2((Identifier)val_peek(3).obj,(ArrayList<ExpressionIntf>)val_peek(1).obj);		}
break;
case 47:
//#line 193 "parser.y"
{	yyval.obj = new MethodStatementCallOut1 (new CallOut(val_peek(5).sval), new StringLiteral(val_peek(3).sval),(ArrayList<ExpressionIntf>) val_peek(1).obj);	}
break;
case 48:
//#line 195 "parser.y"
{	yyval.obj = new MethodStatementCallOut2 (new CallOut(val_peek(3).sval), new StringLiteral(val_peek(1).sval));	}
break;
case 49:
//#line 198 "parser.y"
{	yyval.obj = new Identifier(val_peek(0).sval); }
break;
case 50:
//#line 201 "parser.y"
{	yyval.obj = new Location1(new Identifier(val_peek(0).sval)); 	}
break;
case 51:
//#line 203 "parser.y"
{	yyval.obj = new Location2(new Identifier(val_peek(3).sval), (ExpressionIntf) val_peek(1).obj); 	}
break;
case 52:
//#line 206 "parser.y"
{	yyval=val_peek(0);	}
break;
case 53:
//#line 207 "parser.y"
{	yyval=val_peek(0);	}
break;
case 54:
//#line 211 "parser.y"
{	yyval.obj = new Expression1((ExpressionIntf)val_peek(2).obj,(ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 55:
//#line 212 "parser.y"
{	yyval=val_peek(0);	}
break;
case 56:
//#line 216 "parser.y"
{	yyval.obj = new Expression1((ExpressionIntf)val_peek(2).obj,(ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 57:
//#line 217 "parser.y"
{	yyval=val_peek(0) ;}
break;
case 58:
//#line 222 "parser.y"
{	yyval.obj = new Expression1((ExpressionIntf)val_peek(2).obj,(ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 59:
//#line 223 "parser.y"
{ 	yyval=val_peek(0) ;	}
break;
case 60:
//#line 228 "parser.y"
{	yyval.obj = new Expression1((ExpressionIntf)val_peek(2).obj,(ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 61:
//#line 229 "parser.y"
{	yyval=val_peek(0); }
break;
case 62:
//#line 232 "parser.y"
{	yyval=val_peek(0); 	}
break;
case 63:
//#line 233 "parser.y"
{ 	yyval=val_peek(0); 	}
break;
case 64:
//#line 234 "parser.y"
{	yyval=val_peek(0);	}
break;
case 65:
//#line 235 "parser.y"
{	yyval.obj = new Expression2((ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 66:
//#line 236 "parser.y"
{	yyval.obj = new Expression2((ExpressionIntf)val_peek(0).obj, val_peek(1).sval);	}
break;
case 67:
//#line 237 "parser.y"
{	yyval.obj = new Expression3((ExpressionIntf)val_peek(1).obj);	}
break;
case 68:
//#line 240 "parser.y"
{	yyval=val_peek(0);	}
break;
case 69:
//#line 242 "parser.y"
{	yyval.obj = new callout_arg(val_peek(0).sval);	}
break;
case 70:
//#line 245 "parser.y"
{yyval=val_peek(0);}
break;
case 71:
//#line 246 "parser.y"
{yyval=val_peek(0);}
break;
case 72:
//#line 249 "parser.y"
{	yyval.obj = new IntegerLiteral(val_peek(0).ival);	}
break;
case 73:
//#line 251 "parser.y"
{	yyval.obj = new BooleanLiteral(val_peek(0).sval);	}
break;
//#line 935 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
