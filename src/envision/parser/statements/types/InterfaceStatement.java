package envision.parser.statements.types;

import envision.parser.expressions.types.GenericExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class InterfaceStatement implements Statement {
	
	public final ParserDeclaration declaration;
	public final EArrayList<GenericExpression> generics = new EArrayList();
	
	public InterfaceStatement(ParserDeclaration declarationIn) { declaration = declarationIn; }
	public InterfaceStatement(ParserDeclaration declarationIn, EArrayList<GenericExpression> genericsIn) {
		declaration = declarationIn;
		generics.addAll(genericsIn);
	}
	
	public InterfaceStatement addGeneric(GenericExpression in) {
		generics.add(in);
		return this;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return declaration;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleInterfaceStatement(this);
	}
	
}
