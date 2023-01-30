package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.NumberHelper;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.exceptions.errors.InvalidTargetError;
import envision_lang.lang.natives.EnvisionNull;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Compound;
import envision_lang.parser.expressions.expression_types.Expr_Lambda;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_LambdaFor;
import envision_lang.parser.statements.statement_types.Stmt_VarDef;
import envision_lang.parser.util.VariableDeclaration;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

@Broken(reason="Now that primitives are fully immutable, NumberHelper is completely broken here")
public class IS_LambdaFor extends AbstractInterpreterExecutor {

	public static void run(EnvisionInterpreter interpreter, Stmt_LambdaFor s) {
		
		//first check if the lambda is iterating over an iterable object
		Expr_Lambda lambda = s.lambda;
		Expr_Compound input = lambda.inputs;
		if (input.isEmpty()) throw new InvalidTargetError("Lambda For loops must specify a target!");
		if (!input.hasOne()) throw new InvalidTargetError("Too many targets! Lambda For loops can ONLY specify ONE target!");
		Iterable iterable = new Iterable(interpreter.evaluate(input.getFirst()));
		Expr_Compound production = lambda.production;
		ParsedStatement body = s.body;
		
		//if there is a reference made to the internal counter, this will keep track of it
		EnvisionInt index = null;
		
		//push initializer scope
		interpreter.pushScope();
		//process init block
		index = handleInit(interpreter, s);
		boolean hasPostArgs = s.post != null && s.post.isNotEmpty();
		
		while (index.int_val < iterable.size()) {
			//push loop iteration scope
			interpreter.pushScope();
			
			//execute lambda then body
			handleLambdaProductions(interpreter, s, production, iterable, index.int_val);
			if (body != null) interpreter.execute(body);
			
			//pop loop iteration scope
			interpreter.popScope();
			
			if (!hasPostArgs) {
				NumberHelper.increment(index, false);
			}
			else {
				for (ParsedExpression postExp : s.post) {
					interpreter.evaluate(postExp);
				}
			}
		}
		
		//pop initializer scope
		interpreter.popScope();
	}
	
	private static EnvisionInt handleInit(EnvisionInterpreter interpreter, Stmt_LambdaFor s) {
		EnvisionInt index = null;
		
		if (s.init == null) {
			index = EnvisionInt.ZERO;
			return index;
		}
		
		if (s.init instanceof Stmt_VarDef var_stmt) {
			Stmt_VarDef initVars = (Stmt_VarDef) s.init;
			EList<VariableDeclaration> vars = initVars.vars;
			
			//declare and initialize each variable, the first var will be used as the internal counter reference
			for (int i = 0; i < vars.size(); i++) {
				VariableDeclaration varDec = vars.get(i);
				String name = varDec.getName();
				ParsedExpression var_assignment = varDec.assignment_value;
				EnvisionObject value = (var_assignment != null) ? interpreter.evaluate(var_assignment) : null;
				
				//first check if the variable is already defined
				EnvisionObject obj = interpreter.scope().get(name);
				
				//If this is the first variable index -- attempt to assign as the loop's index reference
				if (i == 0) {
					//if the variable is defined, make sure it's actually an int
					if (obj != null) {
						if (obj instanceof EnvisionInt env_int) {
							index = env_int;
//							if (value != null) {
//								index.set(value);
//							}
						}
						else throw new InvalidDatatypeError("The object '"+obj+"' is invalid for the expected type (int)!");
					}
					//if not, check if the variable's assignment value is potentially an int
					else if (value != null) {
						if (value instanceof EnvisionInt l_value) {
							index = l_value;
							interpreter.scope().define(name, EnvisionStaticTypes.INT_TYPE, index);
						}
						//define the variable anyways but also automatically define the index
						else {
							//define index
							EnvisionInt new_int = EnvisionInt.ZERO;
							index = new_int;
							interpreter.scope().define(name, EnvisionStaticTypes.INT_TYPE, new_int);
							//define variable
							interpreter.scope().define(name, value.getDatatype(), value);
							//throw new InvalidDataTypeError("invilsuqird");
						}
					}
					//if there's no object already declared for the index and there's no assignment value
					//simply create a new int to hold the loop's reference index
					else {
						EnvisionInt new_int = EnvisionInt.ZERO;
						index = new_int;
						interpreter.scope().define(name, EnvisionStaticTypes.INT_TYPE, new_int);
					}
				}
				//this is not the first object, attempt to create new loop-level variable
				else if (obj != null) {
					interpreter.scope().set(name, value);
				}
				else if (value != null) {
					//because EnvisionVariables natively support copying, attempt to cast as such
					if (value.isPassByValue()) {
						interpreter.scope().define(name, value.getDatatype(), value.copy());
					}
					else {
						interpreter.scope().define(name, value.getDatatype(), value);
					}
				}
				else {
					interpreter.scope().define(name, EnvisionStaticTypes.NULL_TYPE, EnvisionNull.NULL);
				}
			}
		}
		else {
			//throw error
		}
		
		//if there are no initializers, set index to zero by default;
		if (index == null) {
			index = EnvisionInt.ZERO;
		}
		
		return index;
	}
	
	private static void handleLambdaProductions(EnvisionInterpreter interpreter,
												Stmt_LambdaFor s,
												Expr_Compound production,
												Iterable iterable,
												long i)
	{
		boolean first = true;
		for (ParsedExpression e : production.expressions) {
			if (!first) {
				interpreter.evaluate(e);
				continue;
			}
			
			if (e instanceof Expr_Var var_expr) {
				String name = var_expr.getName();
				EnvisionObject cur_obj = iterable.get(i);
				EnvisionObject created_obj = null;
				
				if (cur_obj instanceof EnvisionVariable env_var) {
					created_obj = env_var.copy();
				}
				else created_obj = cur_obj;
				
				interpreter.scope().define(name, created_obj.getDatatype(), created_obj);
			}
			else {
				throw new InvalidTargetError("The given expression '" + e + "' is an invalid target for"
										     + "the primary lambda production of: " + s.lambda.inputs + "!");
			}
			
			first = false;
		}
	}
	
	//----------------------------------------------------------------------
	
	private static class Iterable {
		
		private EArrayList<EnvisionObject> vals;
		
		/**
		 * Determines what the iterable object will actually be created from.
		 * An iterable object could be an EnvisionList, EnvisionString, or a
		 * Java:String. Note: If the given object is not a valid iterable
		 * object, an InvalidTargetError is thrown instead.
		 * 
		 * @param objIn The potential iterable object
		 */
		public Iterable(EnvisionObject objIn) {
			if (objIn instanceof EnvisionList l) vals = new EArrayList<>(l.getInternalList());
			else if (objIn instanceof EnvisionString str) vals = new EArrayList<>(str.toList_i());
			else throw new InvalidTargetError("'" + objIn + "' is not a valid iterable object!");
		}
		
		@Override
		public String toString() {
			return vals.toString();
		}
		
		public EnvisionObject get(long i) { return vals.get((int) i); }
		public EnvisionObject get(int i) { return vals.get(i); }
		public int size() { return vals.size(); }
		
	}
	
}