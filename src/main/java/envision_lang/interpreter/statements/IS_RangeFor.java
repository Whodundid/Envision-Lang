package envision_lang.interpreter.statements;

import envision_lang.exceptions.errors.InvalidDatatypeError;
import envision_lang.interpreter.AbstractInterpreterExecutor;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.NumberHelper;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.Continue;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionIntClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.natives.StaticTypes;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Range;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_RangeFor;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box3;
import eutil.datatypes.util.EList;
import eutil.debug.Broken;

@Broken(value="Appears to not increment up to correct 2nd value", since="9/28/2022")
public class IS_RangeFor extends AbstractInterpreterExecutor {
	
	public static void run(EnvisionInterpreter interpreter, Stmt_RangeFor s) {
		EList<Expr_Range> ranges = s.ranges;
		EList<Box3<EnvisionVariable, Long, Long>> rangeValues = EList.newList();
		ParsedStatement body = s.body;
		interpreter.pushScope();
		
		//handle initializers (if there are any)
		if (s.init != null) interpreter.execute(s.init);
		
		//build range values
		for (int i = 0; i < ranges.size(); i++) {
			Expr_Range range_expr = ranges.get(i);
			
			ParsedExpression left_expr = range_expr.left;
			ParsedExpression right_expr = range_expr.right;
			ParsedExpression by_expr = range_expr.by;
			
			// There is an issue here where actual arithmetic expressions are being created as compound expressions.
			
			//System.out.println(left + " : " + right + " : " + by);
			//System.out.println(right.getClass());
			
			EnvisionObject left = handleLeft(interpreter, left_expr);
			EnvisionObject right = interpreter.evaluate(right_expr);
			EnvisionObject by = (by_expr != null) ? interpreter.evaluate(by_expr) : EnvisionIntClass.newInt(1);
			
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
			
			rangeValues.add(new Box3<>(leftObject, right_val, by_val));
		}
		
		var lastRange = rangeValues.getLast();
		
		TOP:
		while (true) {
			while (checkLess(lastRange)) {
				try {
					//run body
					interpreter.execute(body);
					//increment
				}
				catch (Continue c) { inc(lastRange.a, lastRange.c); break; }
				catch (Break b) { break TOP; }
				catch (Exception e) { throw e; }
				
				inc(lastRange.a, lastRange.c);
			}
			
			zeroOut(lastRange);
			
			//if there are no other ranges, break out
			if (rangeValues.hasOne()) break TOP;
			
			//start at the second to last index
			int curRangeIndex = rangeValues.size() - 2;
			//go until i < 0
			while (curRangeIndex >= 0) {
				var curRange = rangeValues.get(curRangeIndex);
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
	

	
	//-------------------------------
	
	private static void inc(EnvisionObject obj, Number amount) {
		NumberHelper.increment(obj, amount, true);
	}
	
	private static void inc(Box3<EnvisionVariable, Long, Long> range) {
		NumberHelper.increment(range.a, range.c, true);
	}
	
	private static void zeroOut(Box3<EnvisionVariable, Long, Long> range) {
		range.a.set_i(0l);
	}
	
	private static boolean checkLess(Box3<EnvisionVariable, Long, Long> range) {
		return (long) range.a.get_i() < range.b;
	}
	
	private static boolean checkLessOne(Box3<EnvisionVariable, Long, Long> range) {
		return (long) range.a.get_i() + 1 < range.b;
	}
	
	private static EnvisionObject handleLeft(EnvisionInterpreter interpreter, ParsedExpression left) {
		if (left instanceof Expr_Var var) {
			return interpreter.defineIfNot(var.getName(), StaticTypes.INT_TYPE, EnvisionIntClass.newInt());
		}
		return interpreter.evaluate(left);
	}
	
	private static boolean checkKeepGoing(EList<Box3<EnvisionVariable, Long, Long>> list) {
		//calculate whether or not the loop is done
		for (var box : list) {
			long a = (long) box.a.get_i();
			long b = box.b;
			if (a < b - 1) return true;
		}
		
		return false;
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
