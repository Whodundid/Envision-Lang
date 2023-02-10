package envision_lang.interpreter.statements;

import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import eutil.datatypes.util.EList;

public class IS_RangeFor extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_RangeFor s) {
		EList<Expr_Range> ranges = s.ranges;
		int size = ranges.size();
		
		//EList<Box3<EnvisionVariable, Long, Long>> rangeValues = EList.newList();
		IncNumber[] rangeValues = new IncNumber[size];
		ParsedStatement body = s.body;
		IScope theScope = interpreter.pushScope();
		
		//handle initializers (if there are any)
		if (s.init != null) interpreter.execute(s.init);
		
		//build range values
		for (int i = 0; i < size; i++) {
			Expr_Range range_expr = ranges.get(i);
			
			ParsedExpression left_expr = range_expr.left;
			ParsedExpression right_expr = range_expr.right;
			ParsedExpression by_expr = range_expr.by;
			
			if (!(left_expr instanceof Expr_Var)) throw new EnvisionLangError("Expected a var type here!");
			
			Expr_Var leftVarExpr = (Expr_Var) left_expr;
			String varName = leftVarExpr.getName();
			
			if (!theScope.exists(varName)) {
				theScope.defineFast(varName, EnvisionInt.INT_TYPE, EnvisionInt.ZERO);
			}
			
			EnvisionObject left = handleLeft(interpreter, left_expr);
			EnvisionObject right = interpreter.evaluate(right_expr);
			EnvisionObject by = (by_expr != null) ? interpreter.evaluate(by_expr) : EnvisionInt.ZERO;
			
			long right_val = 0;
			long by_val = 1;
			
			try {
				//check left value
				if (left instanceof EnvisionVariable env_var) {
					//ensure that the variable being used is an integer
					if (!(env_var instanceof EnvisionInt)) {
						throw new InvalidDatatypeError("Expected an int for range for start bound!");
					}
				}
				else throw new InvalidDatatypeError("Expected an int for range for start bound!");
				
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
			
			rangeValues[i] = new IncNumber(theScope, varName, right_val, by_val);
		}
		
		var lastRange = rangeValues[size - 1];
		
		TOP:
		while (true) {
			while (checkLess(lastRange)) {
				try {
					//run body
					interpreter.execute(body);
					//increment
				}
				catch (Continue c) { inc(lastRange); break; }
				catch (Break b) { break TOP; }
				catch (Exception e) { throw e; }
				
				inc(lastRange);
			}
			
			zeroOut(lastRange);
			
			//if there are no other ranges, break out
			if (rangeValues.length == 1) break TOP;
			
			//start at the second to last index
			int curRangeIndex = size - 2;
			//go until i < 0
			while (curRangeIndex >= 0) {
				var curRange = rangeValues[curRangeIndex];
				//System.out.println("CUR INDEX: " + curRangeIndex + " : " + curRange);
				
				//check if curRange is less than its upper limit
				if (checkLessOne(curRange)) {
					inc(curRange);
					break;
				}
				else {
					zeroOut(curRange);
					if (curRangeIndex == 0) break TOP;
				}
				
				curRangeIndex--;
			}
		}
		
		interpreter.popScope();
	}
	
	private static class IncNumber {
		IScope scope;
		String scopeName; // 'i'
		long upperBound; // i to '5' <-
		long incrementAmount; // 'by'
		
		public IncNumber(IScope scopeIn, String scopeNameIn, long upperBoundIn, long incrementAmountIn) {
			scope = scopeIn;
			scopeName = scopeNameIn;
			upperBound = upperBoundIn;
			incrementAmount = incrementAmountIn;
		}
		
		public void inc() {
			var value = (EnvisionInt) scope.get(scopeName);
			scope.setFast(scopeName, EnvisionIntClass.valueOf(value.int_val + 1L));
		}
		
		public void zeroOut() {
			scope.setFast(scopeName, EnvisionInt.ZERO);
		}
		
		public boolean checkLess() {
			var value = (EnvisionInt) scope.get(scopeName);
			return value.int_val < upperBound;
		}
		
		public boolean checkLessOne() {
			var value = (EnvisionInt) scope.get(scopeName);
			return (value.int_val + 1) < upperBound;
		}
	}
	
	//-------------------------------
	
	private static void inc(IncNumber num) { num.inc(); }
	private static void zeroOut(IncNumber num) { num.zeroOut(); }
	private static boolean checkLess(IncNumber num) { return num.checkLess(); }
	private static boolean checkLessOne(IncNumber num) { return num.checkLessOne(); }
	
	private static EnvisionObject handleLeft(EnvisionInterpreter interpreter, ParsedExpression left) {
		if (left instanceof Expr_Var var) {
			return interpreter.defineIfNot(var.getName(), EnvisionStaticTypes.INT_TYPE, EnvisionInt.ZERO);
		}
		return interpreter.evaluate(left);
	}
	
}

/*
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
				inc(next.a, next.c);
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
*/
