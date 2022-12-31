package envision_lang.interpreter.statements;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creationUtil.ObjectCreator;
import envision_lang.interpreter.util.interpreterBase.StatementExecutor;
import envision_lang.interpreter.util.throwables.Break;
import envision_lang.interpreter.util.throwables.EnvisionException;
import envision_lang.interpreter.util.throwables.ReturnValue;
import envision_lang.lang.EnvisionObject;
import envision_lang.parser.expressions.Expression;
import envision_lang.parser.statements.Statement;
import envision_lang.parser.statements.statement_types.Stmt_SwitchCase;
import envision_lang.parser.statements.statement_types.Stmt_SwitchDef;
import envision_lang.tokenizer.Token;

public class IS_Switch extends StatementExecutor<Stmt_SwitchDef> {

	public IS_Switch(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_SwitchDef s) {
		new IS_Switch(in).run(s);
	}

	@Override
	public void run(Stmt_SwitchDef s) {
		Expression exprVal = s.expression;
		EnvisionObject exprObj = evaluate(exprVal);
		
		//ensure the value being switched upon is not null
		//(NOT SURE IF ACTUALLY DESIRED)
		assertNotNull(exprObj);
		
		//grab the default case (handy)
		Stmt_SwitchCase defaultCase = s.defaultCase;
		boolean caseMatched = false;
		//if this switch is switching on an enum value -- grab the enum that holds it
		//EnvisionEnum theEnum = (exprObj instanceof EnumValue) ? ((EnumValue) exprObj).theEnum : null;
		
		//run inside of a try/catch to catch for breaks
		try {
			//find a matching case (if any)
			for (Stmt_SwitchCase c : s.cases) {
				if (!caseMatched && !c.isDefault) {
					Token<?> caseName = c.caseName;
					EnvisionObject caseNameValue = null;
					
					//determine type of value the case name represents
					if (caseName == null) throw new EnvisionLangError("Null switch case value!");
					//else if (theEnum != null) caseNameValue = theEnum.getValue(caseName.getLexeme());
					else if (caseName.isLiteral()) caseNameValue = ObjectCreator.wrap(caseName.getLiteral());
					else caseNameValue = scope().get(caseName.getLexeme());
					
					//not sure how to handle yet
					if (isNull(caseNameValue)) throw new EnvisionException("NULL SWITCH CASE");
					
					//if the case matched -- set the matched state to true and execute each case block from here on out
					if (isEqual(exprObj, caseNameValue)) caseMatched = true;
				}
				
				//if the case matched (at any point) -- execute the current case block
				if (caseMatched)
					for (Statement stmt : c.body)
						execute(stmt);
			}
			
			//if a default case exists and no case matched the given input, execute the default case
			if (!caseMatched && defaultCase != null)
				for (Statement stmt : defaultCase.body)
					execute(stmt);
		}
		//leave the switch
		catch (Break e) {}
		//handle return statements
		catch (ReturnValue e) { throw e; }
		//something wrong happened
		catch (Exception e) { e.printStackTrace(); }
	}
	
}