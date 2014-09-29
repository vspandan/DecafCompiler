%%

%byaccj

%{
  /* store a reference to the parser object */
  Parser yyparser;

  /* constructor taking an additional parser object */
  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}


DIGIT		=       [0-9]
HEX_DIGIT  	=     	[0-9a-fA-F]
HEX_INTEGER     = 	0[Xx]{HEX_DIGIT}+
INTEGER         = 	{DIGIT}+
EXPONENT        = 	[Ee][-+]?{INTEGER}
DOUBLE          =	{INTEGER}"."{DIGIT}*{EXPONENT}?
STRING  	= 	 	"\".*?\""
IDENTIFIER      =  	[a-zA-Z][a-zA-Z_0-9]*
ARITH_OP	=	([+/*%])
REL_OP		=	[<|>]
WHITE_SPACE_CHAR=[\ \n\r\t\f]

%%
"boolean"               { yyparser.yylval=new ParserVal(yytext());return yyparser.BOOLEAN; }
"break"                 { yyparser.yylval=new ParserVal(yytext()); return yyparser.BREAK; }
"callout"               { yyparser.yylval=new ParserVal(yytext()); return yyparser.CALLOUT; }
"class"                 { yyparser.yylval=new ParserVal(yytext()); return yyparser.CLASS; }
"true"                  { yyparser.yylval=new ParserVal(yytext()); return yyparser.TRUE;       }
"false"                 { yyparser.yylval=new ParserVal(yytext()); return yyparser.FALSE;        }
"for"                   { yyparser.yylval=new ParserVal(yytext()); return yyparser.FOR;         }
"if"                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.IF;          }
"else"                  { yyparser.yylval=new ParserVal(yytext()); return yyparser.ELSE;        }
"return"                { yyparser.yylval=new ParserVal(yytext()); return yyparser.RETURN;      }
"void"                  { yyparser.yylval=new ParserVal(yytext()); return yyparser.VOID;         }
"int"                   { yyparser.yylval=new ParserVal(yytext()); return yyparser.INT;         }
"continue"		{ yyparser.yylval=new ParserVal(yytext()); return yyparser.CONTINUE;  }
//"Program"		{ yyparser.yylval=new ParserVal(yytext()); return yyparser.Program; }
{STRING} 		{ yyparser.yylval=new ParserVal(yytext()); return yyparser.STRING_LITERAL; }
{INTEGER}		{ yyparser.yylval=new ParserVal(Integer.parseInt(yytext())); return yyparser.INT_LITERAL; }
{DOUBLE}		{ yyparser.yylval=new ParserVal(Double.parseDouble(yytext())); return yyparser.INT_LITERAL; }
{HEX_INTEGER}     	{ yyparser.yylval=new ParserVal(Integer.parseInt(yytext(),16)); return yyparser.INT_LITERAL; }
{IDENTIFIER}            { yyparser.yylval=new ParserVal(yytext()); return yyparser.ID;  }
"="			{ yyparser.yylval=new ParserVal(yytext()); return yyparser.E_ASSIGN_OP; }
"-="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.ASSIGN_OP; }
"+="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.ASSIGN_OP; }
"<="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.EQ_OP;   }
">="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.EQ_OP;}
"=="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.EQ_OP;       }
"!="                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.EQ_OP;    }
"&&"                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.COND_OP;         }
"||"                    { yyparser.yylval=new ParserVal(yytext()); return yyparser.COND_OP;          }
{REL_OP}                { yyparser.yylval=new ParserVal(yytext()); return yyparser.REL_OP;  }
{ARITH_OP}              { yyparser.yylval=new ParserVal(yytext()); return yyparser.ARITH_OP; }
"-"			{ yyparser.yylval=new ParserVal(yytext()); return yyparser.MINUS ; }
"{"			{ yyparser.yylval=new ParserVal(yytext());return '{'	;}
"}"			{ yyparser.yylval=new ParserVal(yytext()); return '}'	; }
";"			{ yyparser.yylval=new ParserVal(yytext()); return ';'	;}
"["                     { yyparser.yylval=new ParserVal(yytext()); return '[';}
"]"                     { yyparser.yylval=new ParserVal(yytext()); return ']';}
"("                     { yyparser.yylval=new ParserVal(yytext()); return '(';}
")"                     { yyparser.yylval=new ParserVal(yytext()); return ')';}
","			{ yyparser.yylval=new ParserVal(yytext()); return ',';}
"!"			{ yyparser.yylval=new ParserVal(yytext()); return '!';}
{WHITE_SPACE_CHAR} 				{	}