package envision_lang.interpreter.expressions;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.interpreterBase.ExpressionExecutor;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.parser.expressions.expression_types.Expr_ListInitializer;

public class IE_ListInitializer extends ExpressionExecutor<Expr_ListInitializer> {

	public IE_ListInitializer(EnvisionInterpreter in) {
		super(in);
	}
	
	public static EnvisionObject run(EnvisionInterpreter in, Expr_ListInitializer e) {
		return new IE_ListInitializer(in).run(e);
	}

	@Override
	public EnvisionObject run(Expr_ListInitializer expression) {
		//create new generic list
		EnvisionList l = EnvisionListClass.newList();
		
		//add initializer expression values
		for (var e : expression.values) {
			EnvisionObject o = evaluate(e);
			//pass primitives by value -- not reference
			if (o.isPrimitive()) l.add(o.copy());
			//pass everything else by reference
			else l.add(o);
		}
		
		return l;
	}
	
}
