package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.util.EnvisionDatatype;
import envision.parser.expressions.expression_types.Expr_ListInitializer;

public class IE_ListInitializer extends ExpressionExecutor<Expr_ListInitializer> {

	public IE_ListInitializer(EnvisionInterpreter in) {
		super(in);
	}
	
	public static Object run(EnvisionInterpreter in, Expr_ListInitializer e) {
		return new IE_ListInitializer(in).run(e);
	}

	@Override
	public Object run(Expr_ListInitializer expression) {
		//create new generic list
		EnvisionList l = EnvisionListClass.newList();
		
		//add initializer expression values
		for (var e : expression.values) {
			EnvisionDatatype type = EnvisionDatatype.dynamicallyDetermineType(e);
			EnvisionObject obj = ObjectCreator.createObject(type, e, false, false);
			l.add(obj);
		}
		
		return l;
	}
	
}
