package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;

public class Stmt_Class extends ParsedStatement {

	//========
	// Fields
	//========
	
	public final Token<?> name;
	public final EList<Expr_Var> parentclasses = EList.newList();
	public final EList<ParsedStatement> body = EList.newList();
	public final EList<ParsedStatement> staticMembers = EList.newList();
	public final EList<Stmt_FuncDef> methods = EList.newList();
	public final EList<Stmt_FuncDef> initializers = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_Class(Token<?> start, Token<?> nameIn, ParserDeclaration declarationIn) {
		super(start, declarationIn);
		name = nameIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String p = (parentclasses.isEmpty()) ? "" : " " + parentclasses.toString();
		String b = (body.isNotEmpty()) ? "\n\t\tbody:" : "";
		String c = (initializers.isNotEmpty()) ? "\n\t\tintializers:" : "";
		String s = (staticMembers.isNotEmpty()) ? "\n\t\tstatic:" : "";
		for (int i = 0; i < initializers.size(); i++) c += "\n\t\t\t" + initializers.get(i);
		for (int i = 0; i < body.size(); i++) b += "\n\t\t\t" + body.get(i);
		for (int i = 0; i < staticMembers.size(); i++) s += "\n\t\t\t" + staticMembers.get(i);
		return declaration + "class '" + name.getLexeme() + "'" + p + c + b + s;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleClassStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addSuper(Expr_Var in) { parentclasses.add(in); }
	public void addStatement(ParsedStatement in) { body.add(in); }
	
	public void setSupers(EList<Expr_Var> supersIn) { parentclasses.clearThenAddAll(supersIn); }
	public void setBody(EList<ParsedStatement> bodyIn) { body.clearThenAddAll(bodyIn); }
	public void setStaticMembers(EList<ParsedStatement> staticsIn) { staticMembers.clearThenAddAll(staticsIn); }
	public void setMethods(EList<Stmt_FuncDef> methodsIn) { methods.clearThenAddAll(methodsIn); }
	public void setInitializers(EList<Stmt_FuncDef> constructorsIn) { initializers.clearThenAddAll(constructorsIn); }
	
}
