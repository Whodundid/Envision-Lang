package envision.interpreter.expressions;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.ExpressionExecutor;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.natives.IDatatype;
import envision.parser.expressions.expression_types.Expr_ListInitializer;

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
			IDatatype type = IDatatype.dynamicallyDetermineType(e);
			EnvisionClass typeClass = interpreter.getTypeManager().getTypeClass(type);
			EnvisionObject[] args = { evaluate(e) };
			ClassInstance obj = typeClass.newInstance(interpreter, args);
			l.add(obj);
		}
		
		return l;
	}
	
}
