package envision.interpreter.statements;

import envision.exceptions.errors.InvalidDatatypeError;
import envision.exceptions.errors.InvalidTargetError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.creationUtil.ObjectCreator;
import envision.interpreter.util.creationUtil.VariableUtil;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.lang.datatypes.EnvisionInt;
import envision.lang.datatypes.EnvisionIntClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.datatypes.EnvisionVariable;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.EnvisionDatatype;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Compound;
import envision.parser.expressions.expression_types.Expr_Lambda;
import envision.parser.expressions.expression_types.Expr_Var;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_LambdaFor;
import envision.parser.statements.statement_types.Stmt_VarDef;
import envision.parser.util.VariableDeclaration;
import eutil.datatypes.EArrayList;

public class IS_LambdaFor extends StatementExecutor<Stmt_LambdaFor> {

	Stmt_LambdaFor s;
	Iterable iterable;
	//if there is a reference made to the internal counter, this will keep track of it
	EnvisionInt index = null;
	Expr_Compound production;
	boolean hasPostArgs = false;
	
	//----------------------------------------------
	
	public IS_LambdaFor(EnvisionInterpreter in) {
		super(in);
	}
	
	//----------------------------------------------

	public static void run(EnvisionInterpreter in, Stmt_LambdaFor s) {
		new IS_LambdaFor(in).run(s);
	}
	
	//----------------------------------------------------------------------
	
	@Override
	public void run(Stmt_LambdaFor s) {
		//first check if the lambda is iterating over an iterable object
		Expr_Lambda lambda = (this.s = s).lambda;
		Expr_Compound input = lambda.inputs;
		if (input.isEmpty()) throw new InvalidTargetError("Lambda For loops must specify a target!");
		if (!input.hasOne()) throw new InvalidTargetError("Too many targets! Lambda For loops can ONLY specify ONE target!");
		iterable = new Iterable(evaluate(input.getFirst()));
		production = lambda.production;
		Statement body = s.body;
		
		//push initializer scope
		pushScope();
		//process init block
		handleInit();
		hasPostArgs = s.post != null && s.post.isNotEmpty();
		
		while (index.long_val < iterable.size()) {
			//push loop iteration scope
			pushScope();
			
			//execute lambda then body
			handleLambdaProductions(index.long_val);
			if (body != null) execute(body);
			
			//pop loop iteration scope
			popScope();
			
			if (!hasPostArgs) {
				VariableUtil.incrementValue(index, false);
			}
			else {
				for (Expression postExp : s.post) evaluate(postExp);
			}
		}
		
		//pop initializer scope
		popScope();
	}
	
	private void handleInit() {
		if (s.init != null) {
			if (s.init instanceof Stmt_VarDef var_stmt) {
				Stmt_VarDef initVars = (Stmt_VarDef) s.init;
				EArrayList<VariableDeclaration> vars = initVars.vars;
				
				//declare and initialize each variable, the first var will be used as the internal counter reference
				for (int i = 0; i < vars.size(); i++) {
					VariableDeclaration varDec = vars.get(i);
					String name = varDec.getName();
					Expression var_assignment = varDec.assignment_value;
					Object assignment_val = (var_assignment != null) ? evaluate(var_assignment) : null;
					Object value = EnvisionVariable.convert(assignment_val);
					
					//first check if the variable is already defined
					EnvisionObject obj = scope().get(name);
					
					//If this is the first variable index -- attempt to assign as the loop's index reference
					if (i == 0) {
						//if the variable is defined, make sure it's actually an int
						if (obj != null) {
							if (obj instanceof EnvisionInt env_int) {
								index = env_int;
								if (value != null) index.set_i(value);
							}
							else throw new InvalidDatatypeError("The object '"+obj+"' is invalid for the expected type (int)!");
						}
						//if not, check if the variable's assignment value is potentially an int
						else if (value != null) {
							if (value instanceof Long l_value) {
								EnvisionInt new_int = EnvisionIntClass.newInt(l_value);
								index = new_int;
								scope().define(name, EnvisionDatatype.INT_TYPE, new_int);
							}
							//define the variable anyways but also define the index
							else {
								//define index
								EnvisionInt new_int = EnvisionIntClass.newInt();
								index = new_int;
								scope().define(name, EnvisionDatatype.INT_TYPE, new_int);
								//define variable
								EnvisionDatatype creation_type = EnvisionDatatype.dynamicallyDetermineType(value);
								EnvisionObject created_obj = ObjectCreator.createObject(creation_type, value, false, false);
								scope().define(name, creation_type, created_obj);
								//throw new InvalidDataTypeError("invilsuqird");
							}
						}
						//if there's no object already declared for the index and there's no assignment value
						//simply create a new int to hold the loop's reference index
						else {
							EnvisionInt new_int = EnvisionIntClass.newInt();
							index = new_int;
							scope().define(name, EnvisionDatatype.INT_TYPE, new_int);
						}
					}
					//this is not the first object, attempt to create new loop-level variable
					else if (obj != null) {
						if (obj instanceof EnvisionVariable env_var) {
							env_var.set_i(value);
						}
						else {
							EnvisionDatatype val_type = EnvisionDatatype.dynamicallyDetermineType(value);
							EnvisionObject val_obj = ObjectCreator.createObject(val_type, value, false, false);
							scope().set(name, val_obj);
						}
					}
					else if (value != null) {
						//because EnvisionVariables natively support copying, attempt to cast as such
						if (value instanceof EnvisionVariable env_var) {
							scope().define(name, env_var.getDatatype(), env_var.copy());
						}
						else {
							EnvisionDatatype creation_type = EnvisionDatatype.dynamicallyDetermineType(value);
							EnvisionObject created_obj = ObjectCreator.createObject(creation_type, value, false, false);
							scope().define(name, creation_type, created_obj);
						}
					}
					else {
						scope().define(name, EnvisionNull.NULL);
					}
				}
			}
			else {
				//throw error
			}
		}
		
		//if there are no initializers, set index to zero by default;
		if (index == null) {
			index = EnvisionIntClass.newInt();
		}
	}
	
	private void handleLambdaProductions(long i) {
		boolean first = true;
		for (Expression e : production.expressions) {
			if (first) {
				
				if (e instanceof Expr_Var var_expr) {
					String name = var_expr.getName();
					Object cur_obj = iterable.get(i);
					
					EnvisionObject created_obj = null;
					if (cur_obj instanceof EnvisionVariable env_var) {
						created_obj = env_var.copy();
					}
					else created_obj = (EnvisionObject) cur_obj;
					
					scope().define(name, created_obj.getDatatype(), created_obj);
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
		
		/**
		 * Determines what the iterable object will actually be created from.
		 * An iterable object could be an EnvisionList, EnvisionString, or a
		 * Java:String. Note: If the given object is not a valid iterable
		 * object, an InvalidTargetError is thrown instead.
		 * 
		 * @param objIn The potential iterable object
		 */
		public Iterable(Object objIn) {
			if (objIn instanceof EnvisionList l) vals = new EArrayList(l.getList());
			else if (objIn instanceof EnvisionString str) vals = new EArrayList(str.toList().getList());
			else if (objIn instanceof String str) vals = EnvisionStringClass.newString(str).toList().getList();
			else throw new InvalidTargetError("'" + objIn + "' is not a valid iterable object!");
		}
		
		@Override
		public String toString() {
			return vals.toString();
		}
		
		public Object get(long i) { return vals.get((int) i); }
		public Object get(int i) { return vals.get(i); }
		public int size() { return vals.size(); }
		
	}
	
}