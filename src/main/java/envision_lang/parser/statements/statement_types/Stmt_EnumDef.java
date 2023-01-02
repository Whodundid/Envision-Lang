package envision_lang.parser.statements.statement_types;

import envision_lang.parser.expressions.expression_types.Expr_Enum;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.Token;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class Stmt_EnumDef extends ParsedStatement {
	
	//========
	// Fields
	//========
	
	public final Token<?> name;
	public final EList<Expr_Var> superEnums = EList.newList();
	public final EList<Expr_Enum> values = EList.newList();
	public final EList<ParsedStatement> body = EList.newList();
	
	//==============
	// Constructors
	//==============
	
	public Stmt_EnumDef(Token<?> nameIn, ParserDeclaration declarationIn) {
		super(nameIn, declarationIn);
		name = nameIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public String toString() {
		String s = (superEnums.isEmpty()) ? "" : " " + superEnums.toString();
		String b = (body != null && body.isNotEmpty()) ? " : {" + EStringUtil.toString(body, ", ") + " }" : "";
		return declaration + " enum " + name.getLexeme() + s + " { " + values + b + " }";
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleEnumStatement(this);
	}
	
	//=========
	// Methods
	//=========
	
	public void addSuper(Expr_Var in) { superEnums.add(in); }
	public void addValue(Expr_Enum in) { values.add(in); }
	public void addStatement(ParsedStatement in) { body.add(in); }
	
	public void setSupers(EList<Expr_Var> supersIn) { superEnums.clearThenAddAll(supersIn); }
	public void setValues(EList<Expr_Enum> valuesIn) { values.clearThenAddAll(valuesIn); }
	public void setBody(EList<ParsedStatement> bodyIn) { body.clearThenAddAll(bodyIn); }
	
}
