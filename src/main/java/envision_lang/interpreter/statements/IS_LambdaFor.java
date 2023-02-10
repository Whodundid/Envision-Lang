package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.ScopeEntry;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionTuple;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.language_errors.error_types.InvalidDatatypeError;
import envision_lang.lang.language_errors.error_types.InvalidTargetError;
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
		
		boolean hasPostArgs = s.post != null;
		//push initializer scope
		interpreter.pushScope();
		//process init block
		//if there is a reference made to the internal counter, this will keep track of it
		ScopeEntry indexEntry = handleInit(interpreter, s);
		long internalIndex = (indexEntry != null) ? ((EnvisionInt) indexEntry.getObject()).int_val : 0L;
		
		while (internalIndex < iterable.size()) {
			//push loop iteration scope
			interpreter.pushScope();
			
			//execute lambda then body
			handleLambdaProductions(interpreter, s, production, iterable, internalIndex);
			if (body != null) interpreter.execute(body);
			
			//pop loop iteration scope
			interpreter.popScope();
			
			if (hasPostArgs) {
				var post = s.post;
				int postSize = post.size();
				for (int i = 0; i < postSize; i++) {
					ParsedExpression postExp = post.get(i);
					interpreter.evaluate(postExp);
				}
				
				// update the internal index with the scopes value to get updates
				if (indexEntry != null) {
					internalIndex = ((EnvisionInt) indexEntry.getObject()).int_val;
				}
			}
			else if (indexEntry != null) {
				long cur = ((EnvisionInt) indexEntry.getObject()).int_val;
				internalIndex = cur + 1L;
				indexEntry.set(EnvisionIntClass.valueOf(internalIndex));
			}
			else {
				internalIndex++;
			}
		}
		
		//pop initializer scope
		interpreter.popScope();
	}
	
	private static ScopeEntry handleInit(EnvisionInterpreter interpreter, Stmt_LambdaFor s) {
		if (s.init == null) return null;
		
		ScopeEntry index = null;
		var scope = interpreter.scope();
		
		if (!(s.init instanceof Stmt_VarDef))
			throw new InvalidDatatypeError("Expected a variable declaration!");
		
		Stmt_VarDef initVars = (Stmt_VarDef) s.init;
		EList<VariableDeclaration> vars = initVars.vars;
		
		//declare and initialize each variable, the first var will be used as the internal counter reference
		for (int i = 0; i < vars.size(); i++) {
			VariableDeclaration varDec = vars.get(i);
			String name = varDec.getName();
			ParsedExpression var_assignment = varDec.assignment_value;
			EnvisionObject value = (var_assignment != null) ? interpreter.evaluate(var_assignment) : null;
			
			//first check if the variable is already defined
			var obj = scope.get(name);
			
			//if this is the first variable index -- attempt to assign as the loop's index reference
			if (i == 0) {
				//if the variable is defined, make sure it's actually an int
				if (obj != null) {
					if (obj instanceof EnvisionInt) {
						index = scope.getTyped(name).setRT(value);
					}
					else throw new InvalidDatatypeError("The object '"+obj+"' is invalid for the expected type (int)!");
				}
				//if not, check if the variable's assignment value is potentially an int
				else if (value != null) {
					if (value instanceof EnvisionInt) {
						index = scope.defineRT(name, EnvisionInt.INT_TYPE, value);
					}
					//define the variable anyways but also automatically define the index
					else {
						//define variable
						scope.define(name, value.getDatatype(), value);
						//throw new InvalidDataTypeError("invilsuqird");
					}
				}
				else {
					index = scope.defineRT(name, EnvisionInt.INT_TYPE, EnvisionInt.ZERO);
				}
			}
			//this is not the first object, attempt to update existing variable
			else if (obj != null) {
				scope.set(name, value);
			}
			else if (value != null) {
				//because EnvisionVariables natively support copying, attempt to cast as such
				if (value.isPassByValue()) {
					scope.define(name, value.getDatatype(), value.copy());
				}
				else {
					scope.define(name, value.getDatatype(), value);
				}
			}
			else {
				scope.define(name, EnvisionStaticTypes.NULL_TYPE, EnvisionNull.NULL);
			}
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
		
		private EList<EnvisionObject> vals;
		
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
			else if (objIn instanceof EnvisionTuple t) vals = new EArrayList<>(t.getInternalList());
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