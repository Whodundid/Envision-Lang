package envision.interpreter.statements;

import envision.exceptions.errors.InvalidDataTypeError;
import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.creationUtil.VariableUtil;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.objects.EnvisionList;
import envision.lang.objects.EnvisionNullObject;
import envision.lang.util.EnvisionDataType;
import envision.lang.variables.EnvisionInt;
import envision.lang.variables.EnvisionString;
import envision.lang.variables.EnvisionVariable;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.CompoundExpression;
import envision.parser.expressions.types.LambdaExpression;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.statementUtil.VariableDeclaration;
import envision.parser.statements.types.LambdaForStatement;
import envision.parser.statements.types.VariableStatement;
import eutil.datatypes.EArrayList;

public class IS_LambdaFor extends StatementExecutor<LambdaForStatement> {

	LambdaForStatement s;
	Iterable iterable;
	//if there is a reference made to the internal counter, this will keep track of it
	EnvisionInt index = null;
	CompoundExpression production;
	boolean hasPostArgs = false;
	
	//----------------------------------------------
	
	public IS_LambdaFor(EnvisionInterpreter in) {
		super(in);
	}
	
	//----------------------------------------------

	public static void run(EnvisionInterpreter in, LambdaForStatement s) {
		new IS_LambdaFor(in).run(s);
	}
	
	//----------------------------------------------------------------------
	
	@Override
	public void run(LambdaForStatement s) {
		//first check if the lambda is iterating over an iterable object
		LambdaExpression lambda = (this.s = s).lambda;
		CompoundExpression input = lambda.inputs;
		if (input.isEmpty()) throw new InvalidTargetError("Lambda For loops must specify a target!");
		if (!input.hasOne()) throw new InvalidTargetError("Too many targets! Lambda For loops can ONLY specify ONE target!");
		iterable = new Iterable(evaluate(input.getFirst()));
		production = lambda.production;
		
		//push initializer scope
		pushScope();
		//process init block
		handleInit();
		hasPostArgs = s.post != null && s.post.isNotEmpty();
		
		while (((Number) index.get()).longValue() < iterable.size()) {
			//push loop iteration scope
			pushScope();
			
			//execute lambda then body
			handleLambdaProductions((int) ((Number) index.get()).longValue());
			execute(s.body);
			
			//pop loop iteration scope
			popScope();
			
			if (!hasPostArgs) { VariableUtil.incrementValue(index, false); }
			else {
				for (Expression postExp : s.post) evaluate(postExp);
			}
		}
		
		//pop initializer scope
		popScope();
	}
	
	private void handleInit() {
		if (s.init != null) {
			if (s.init instanceof VariableStatement) {
				VariableStatement initVars = (VariableStatement) s.init;
				EArrayList<VariableDeclaration> vars = initVars.vars;
				
				//declare and initialize each variable, the first var will be used as the internal counter reference
				for (int i = 0; i < vars.size(); i++) {
					VariableDeclaration varDec = vars.get(i);
					String name = varDec.getName();
					Object value = EnvisionVariable.convert(evaluate(varDec.value));
					
					//first check if the variable is already defined
					EnvisionObject obj = scope().get(name);
					
					if (i == 0) {
						if (obj != null) {
							if (obj instanceof EnvisionInt) {
								index = (EnvisionInt) obj;
								if (value != null) { index.set(value); }
							}
							else { throw new InvalidDataTypeError("The object '" + obj + "' is invalid for the expected type (int)!"); }
						}
						else if (value != null) {
							if (value instanceof Long) {
								scope().define(name, index = new EnvisionInt(name, ((Number) value).longValue()));
							}
							//define the variable anyways but also define the index
							else {
								//define index
								scope().define(name, index = new EnvisionInt(name));
								//define variable
								scope().define(name, EnvisionDataType.getDataType(value), ObjectCreator.wrap(value));
								//throw new InvalidDataTypeError("invilsuqird");
							}
						}
						else { scope().define(name, index = new EnvisionInt(name)); }
					}
					else {
						if (obj != null) {
							if (obj instanceof EnvisionVariable) { ((EnvisionVariable) obj).set(value); }
							else { scope().set(name, ObjectCreator.createObject(name, value)); }
						}
						else if (value != null) {
							if (value instanceof EnvisionVariable) {
								scope().define(name, ObjectCreator.createObject(name, EnvisionObject.convert(value)));
							}
							else scope().define(name, EnvisionDataType.getDataType(value), ObjectCreator.wrap(value));
						}
						else { scope().define(name, new EnvisionNullObject()); }
					}
				}
			}
			else {
				//throw error
			}
		}
		
		//if there are no initializers, set index to zero by default;
		if (index == null) {
			index = new EnvisionInt();
		}
	}
	
	private void handleLambdaProductions(int i) {
		boolean first = true;
		for (Expression e : production.expressions) {
			if (first) {
				
				if (e instanceof VarExpression) {
					VarExpression v = (VarExpression) e;
					String name = v.getName();
					scope().define(name, ObjectCreator.wrap(iterable.get(i)));
				}
				else {
					throw new InvalidTargetError("The given expression '" + e + "' is an invalid target for"
											   + "the primary lambda production of: " + s.lambda.inputs + "!");
				}
				
				first = false;
			}
			else {
				evaluate(e);
			}
		}
	}
	
	//----------------------------------------------------------------------
	
	private static class Iterable {
		
		private EArrayList vals;
		
		public Iterable(Object objIn) {
			if (objIn instanceof EnvisionList) { vals = new EArrayList(((EnvisionList) objIn).getEArrayList()); }
			else if (objIn instanceof EnvisionString) { vals = new EArrayList(((EnvisionString) objIn).toList().getEArrayList()); }
			else if (objIn instanceof String) { vals = new EnvisionString((String) objIn).toList().getEArrayList(); }
			else { throw new InvalidTargetError("'" + objIn + "' is not a valid iterable object!"); }
		}
		
		@Override public String toString() { return vals.toString(); }
		
		public Object get(int i) { return vals.get(i); }
		public int size() { return vals.size(); }
		
	}
	
}