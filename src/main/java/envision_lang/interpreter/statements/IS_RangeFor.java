package envision_lang.interpreter.statements;

import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.NumberHelper;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.util.StaticTypes;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import eutil.datatypes.Box3;
import eutil.datatypes.EArrayList;

public class IS_RangeFor extends StatementExecutor<Stmt_RangeFor> {
	
	public IS_RangeFor(EnvisionInterpreter intIn) {
		super(intIn);
	}
	
	@Override
	public void run(Stmt_RangeFor statement) {
		EArrayList<Expr_Range> ranges = statement.ranges;
		EArrayList<Box3<EnvisionVariable, Long, Long>> rangeValues = new EArrayList();
		Statement body = statement.body;
		pushScope();
		
		//handle initializers (if there are any)
		if (statement.init != null) interpreter.execute(statement.init);
		
		//build range values
		for (int i = 0; i < ranges.size(); i++) {
			Expr_Range range_expr = ranges.get(i);
			
			Expression left_expr = range_expr.left;
			Expression right_expr = range_expr.right;
			Expression by_expr = range_expr.by;
			
			// There is an issue here where actual arithmetic expressions are being created as compound expressions.
			
			//System.out.println(left + " : " + right + " : " + by);
			//System.out.println(right.getClass());
			
			EnvisionObject left = handleLeft(left_expr);
			EnvisionObject right = evaluate(right_expr);
			EnvisionObject by = (by_expr != null) ? evaluate(by_expr) : EnvisionIntClass.newInt(1);
			
			EnvisionVariable leftObject = null;
			
			long right_val = 0;
			long by_val = 1;
			
			try {
				//handle left value
				if (left instanceof EnvisionVariable env_var) {
					//ensure that the variable being used is an integer
					if (!(env_var instanceof EnvisionInt)) throw new Exception();
					leftObject = env_var;
				}
				else throw new Exception();
				
				{
				//handle right
				if (right instanceof EnvisionList env_list) 		right_val = env_list.size_i();
				else if (right instanceof EnvisionString env_str) 	right_val = env_str.length_i();
				else if (right instanceof EnvisionInt env_int) 		right_val = env_int.int_val;
				else throw new InvalidDatatypeError("Expected a valid int conversion target but got '" +
													right + "' instead!");
				}
				{
				//handle by
				if (by instanceof EnvisionList env_list) 			by_val = env_list.size_i();
				else if (by instanceof EnvisionString env_str) 		by_val = env_str.length_i();
				else if (by instanceof EnvisionInt env_int) 		by_val = env_int.int_val;
				else throw new InvalidDatatypeError("Expected a valid int conversion target but got '" +
													right + "' instead!");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new InvalidDatatypeError("Range expression must use integer based numbers.");
			}
			
			rangeValues.add(new Box3(leftObject, right_val, by_val));
		}
		
		//ripple carry across ranges
		boolean done = false;
		boolean canCarry = rangeValues.size() > 1;
		
		TOP:
		while (!done) {
			Box3<EnvisionVariable, Long, Long> range = rangeValues.getLast();
			
			while (checkLess(range)) {
				try {
					//run body
					execute(body);
					//increment
				}
				catch (Continue c) { inc(range.a, range.c); break; }
				catch (Break b) { break TOP; }
				catch (Exception e) { throw e; }
				
				inc(range.a, range.c);
			}
			
			if (canCarry) {
				//go to the next position
				boolean carrying = true;
				int i = ranges.size() - 2;
				
				Box3<EnvisionVariable, Long, Long> next = rangeValues.get(i);
				//first check if the range should continue
				if (!checkKeepGoing(rangeValues)) break;
				
				//begin the carry by zeroing the first
				range.a.set_i(0);
				
				while (carrying && i >= 0) {
					next = rangeValues.get(i);
					
					//if the next most position is less than it's respective limit, increment it
					if (checkLess(next)) {
						NumberHelper.increment(next.a, next.c, true);
						carrying = false;
					}
					//otherwise, zero it out then continue until a range is found that is not at the end
					else {
						next.a.set_i(0);
						i--;
					}
				}
			}
			
			//if not all values are fully set, restart the loop
			if (checkKeepGoing(rangeValues)) continue;
			
			done = true;
		}
		
		popScope();
	}
	
	public static void run(EnvisionInterpreter in, Stmt_RangeFor s) {
		new IS_RangeFor(in).run(s);
	}
	
	//-------------------------------
	
	private void inc(EnvisionObject obj, Number amount) {
		NumberHelper.increment(obj, amount, true);
	}
	
	private boolean checkLess(Box3<EnvisionVariable, Long, Long> box) {
		return (long) box.a.get_i() < box.b;
	}
	
	private EnvisionObject handleLeft(Expression left) {
		if (left instanceof Expr_Var var) {
			return defineIfNot(var.getName(), StaticTypes.INT_TYPE, EnvisionIntClass.newInt());
		}
		return evaluate(left);
	}
	
	private boolean checkKeepGoing(EArrayList<Box3<EnvisionVariable, Long, Long>> list) {
		//calculate whether or not the loop is done
		for (var box : list) {
			long a = (long) box.a.get_i();
			long b = box.b;
			//System.out.println("checking: " + a + ":" + b + " = " + (a < b - 1));
			if (a < b) return true;
		}
		
		return false;
	}
	
}

/*
package envision.interpreter.statements;

import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.interpreter.util.throwables.Break;
import envision.interpreter.util.throwables.Continue;
import envision.interpreter.util.variables.VariableUtil;
import envision.lang.objects.EnvisionObject;
import envision.lang.objects.objects.EnvisionList;
import envision.lang.objects.variables.EnvisionInt;
import envision.lang.objects.variables.EnvisionString;
import envision.lang.objects.variables.EnvisionVariable;
import envision.parser.expressions.Expression;
import envision.parser.expressions.types.RangeExpression;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.types.RangeForStatement;
import eutil.storage.EArrayList;
import eutil.storage.TrippleBox;

public class IS_RangeFor extends StatementExecutor<RangeForStatement> {
	
	public IS_RangeFor(EnvisionInterpreter intIn) {
		super(intIn);
	}
	
	@Override
	public Void run(RangeForStatement statement) {
		EArrayList<RangeExpression> ranges = statement.ranges;
		EArrayList<TrippleBox<EnvisionVariable, Long, Long>> rangeValues = new EArrayList();
		Statement body = statement.body;
		pushScope();
		
		//handle initializers (if there are any)
		interpreter.execute(statement.init);
		
		//build range values
		for (int i = 0; i < ranges.size(); i++) {
			RangeExpression r = ranges.get(i);
			
			Expression left = r.left;
			Expression right = r.right;
			Expression by = r.by;
			
			// There is an issue here where actual arithmetic expressions are being created as compound expressions.
			
			//System.out.println(left + " : " + right + " : " + by);
			//System.out.println(right.getClass());
			
			Object lVal = handleLeft(left);
			Object rVal = evaluate(right);
			Object bVal = (by != null) ? evaluate(by) : (long) 1;
			
			EnvisionVariable leftObject = null;
			
			try {
				//handle right
				if (lVal instanceof Number) {
					//ensure that the number being used is an integer
					if (!(lVal instanceof Integer) && !(lVal instanceof Long)) { throw new Exception(); }
					leftObject = new EnvisionInt((Number) lVal);
				}
				else if (lVal instanceof EnvisionVariable) {
					EnvisionVariable v = (EnvisionVariable) lVal;
					//ensure that the variable being used is an integer
					if (!(v instanceof EnvisionInt)) { throw new Exception(); }
					leftObject = v;
				}
				else { throw new Exception(); }
				
				rVal = (rVal instanceof EnvisionVariable) ? ((EnvisionVariable) rVal).get() : rVal;
				bVal = (bVal instanceof EnvisionVariable) ? ((EnvisionVariable) bVal).get() : bVal;
				
				//handle right
				if (rVal instanceof EnvisionList) { rVal = (long) ((EnvisionList) rVal).size() - 1; }
				else if (rVal instanceof String) { rVal = (long) ((String) rVal).length(); }
				else if (rVal instanceof EnvisionString) { rVal = (long) ((EnvisionString) rVal).length(); }
				else if (rVal instanceof EnvisionInt) { rVal = (long) ((EnvisionInt) rVal).get(); }
				else { rVal = ((Number) rVal).longValue(); }
				
				//handle by
				if (bVal instanceof EnvisionList) { bVal = (long) ((EnvisionList) bVal).size(); }
				else if (bVal instanceof String) { bVal = (long) ((String) bVal).length(); }
				else if (bVal instanceof EnvisionString) { bVal = (long) ((EnvisionString) bVal).length(); }
				else if (bVal instanceof EnvisionInt) { bVal = (long) ((EnvisionInt) bVal).get(); }
				else { bVal = ((Number) bVal).longValue(); }
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Range expression must use integer based numbers.");
			}
			
			rangeValues.add(new TrippleBox(leftObject, rVal, bVal));
		}
		
		//ripple carry across ranges
		boolean done = false;
		boolean canCarry = rangeValues.size() > 1;
		
		TOP:
		while (!done) {
			TrippleBox<EnvisionVariable, Long, Long> range = rangeValues.getLast();
			Expression inc = ranges.getLast().left;
			
			while (checkLess(range)) {
				try {
					//run body
					execute(body);
					//increment
				}
				catch (Continue c) { inc(range.a, range.c); break; }
				catch (Break b) { break TOP; }
				catch (Exception e) { throw e; }
				
				inc(range.a, range.c);
			}
			
			if (canCarry) {
				//go to the next position
				boolean carrying = true;
				int i = ranges.size() - 2;
				
				TrippleBox<EnvisionVariable, Long, Long> next = rangeValues.get(i);
				//first check if the range should continue
				if (!checkKeepGoing(rangeValues)) { break; }
				
				//begin the carry by zeroing the first
				range.a.set(0);
				
				while (carrying && i >= 0) {
					next = rangeValues.get(i);
					
					//if the next most position is less than it's respective limit, increment it
					if ((long) next.a.get() < (next.b)) {
						VariableUtil.incrementValue(next.a, next.c, true);
						carrying = false;
					}
					//otherwise, zero it out then continue until a range is found that is not at the end
					else {
						next.a.set(0);
						i--;
					}
				}
			}
			
			//if not all values are fully set, restart the loop
			if (checkKeepGoing(rangeValues)) { continue; }
			
			done = true;
		}
		
		popScope();
		
		return null;
	}
	
	public static Void run(EnvisionInterpreter in, RangeForStatement s) {
		return new IS_RangeFor(in).run(s);
	}
	
	//-------------------------------
	
	private void inc(EnvisionObject obj, Number amount) { VariableUtil.incrementValue(obj, amount, true); }
	
	private Object handleLeft(Expression left) {
		if (left instanceof VarExpression) {
			VarExpression leftVar = (VarExpression) left;
			return (EnvisionVariable) defineIfNot(leftVar.name, new EnvisionInt(0));
		}
		return evaluate(left);
	}
	
	private boolean checkLess(TrippleBox<EnvisionVariable, Long, Long> box) {
		return (long) box.a.get() <= box.b;
	}
	
	private boolean checkKeepGoing(EArrayList<TrippleBox<EnvisionVariable, Long, Long>> list) {
		//cacluate if the loop is done
		for (TrippleBox<EnvisionVariable, Long, Long> box : list) {
			long a = (long) box.a.get();
			long b = box.b;
			//System.out.println("checking: " + a + ":" + b + " = " + (a < b - 1));
			if (a < b) {
				return true;
			}
		}
		
		return false;
	}
	
}
*/
