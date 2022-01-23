package envision.parser.statements.types;

import envision.parser.expressions.types.GenericExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.StatementHandler;
import envision.parser.statements.statementUtil.ParserDeclaration;
import eutil.datatypes.EArrayList;

public class GenericStatement implements Statement {
	
	public final EArrayList<GenericExpression> generics = new EArrayList();
	
	public GenericStatement() {}
	public GenericStatement(EArrayList<GenericExpression> genericsIn) {
		generics.addAll(genericsIn);
	}
	
	public GenericStatement addGeneric(GenericExpression in) {
		generics.add(in);
		return this;
	}
	
	@Override
	public ParserDeclaration getDeclaration() {
		return null;
	}
	
	@Override
	public void execute(StatementHandler handler) {
		handler.handleGenericStatement(this);
	}
	
}
