package envision.interpreter.statements;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.AlreadyDefinedError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.lang.EnvisionObject;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expression_types.Expr_Assign;
import envision.parser.expressions.expression_types.Expr_Binary;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.Stmt_Block;
import envision.parser.statements.statement_types.Stmt_Expression;
import envision.parser.statements.statement_types.Stmt_ModularFuncDef;
import envision.parser.statements.statement_types.Stmt_Return;
import envision.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.debug.Experimental;

@Experimental
public class IS_ModularFunc extends StatementExecutor<Stmt_ModularFuncDef> {

	public IS_ModularFunc(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, Stmt_ModularFuncDef s) {
		new IS_ModularFunc(in).run(s);
	}
	
	//----------------------------------------------------------------------------------
	
	@Override
	public void run(Stmt_ModularFuncDef s) {
		BoxList<Token, Token> associations = s.associations;
		
		for (Box2<Token, Token> modAssociation : associations) {
			//Token refName = s.name;
			//String methName = ((refName.isModular()) ? "" : refName.lexeme) + modAssociation.getA().lexeme;
			//String methName = refName.lexeme;
			
			//attempt to find any already existing base method within the given scope
			EnvisionObject base = scope().get(s.name.lexeme);
			
			//if there is already an existing method of the same name, this is invalid!
			if (base != null) {
				throw new AlreadyDefinedError(s.name.lexeme);
			}
			else {
				//if the statement is valid, iterate across all of the method's scope statements
				//and replace MODULAR_VALUE tokens with the association value.
				EArrayList<Statement> statements = s.body;
				EArrayList<Statement> newStatements = new EArrayList();
				
				for (Statement stmt : statements) {
					
					//unbox block statements
					if (stmt instanceof Stmt_Block) {
						EArrayList<Statement> found = ((Stmt_Block) stmt).statements;
						EArrayList<Stmt_Block> workList = new EArrayList();
						
						while (workList.isNotEmpty()) {
							EArrayList<Stmt_Block> newWork = new EArrayList();
							
							for (Stmt_Block bs : workList) {
								EArrayList<Statement> bsStatements = bs.statements;
								for (Statement bss : bsStatements)
									if (bss instanceof Stmt_Block) newWork.add((Stmt_Block) bss);
									else found.add(bss);
							}
							
							workList.clear();
							workList.addAll(newWork);
						}
						
						//now iterate through all the found statements and attempt to replace
						//the MODULAR_VALUE token with the association value.
						for (Statement f : found) {
							Statement copy = f.copy();
							newStatements.add(copy);
							replaceModular(copy, modAssociation.getB());
						}
					}
					else if (stmt instanceof Stmt_Return) {
						Statement copy = stmt.copy();
						newStatements.add(copy);
						replaceModular(copy, modAssociation.getB());
					}
					
					//System.out.println(s.name.lexeme + modAssociation.getA().lexeme + " : " + s);
				}
				
				//build the method against the current scope from the given declaration
				//EnvisionFunction m = FunctionCreator.buildMethod(interpreter, s, methName, newStatements, scope());
				//scope().define(methName, m);
			}
		}
	}
	
	// Not really sure this is the best way to go about doing this right now..
	private void replaceModular(Statement s, Token ass) {
		//this is gonna be painful
		if (s instanceof Stmt_Block) throw new EnvisionError("Replace Modular: this shouldn't be possible.");
		//if (s instanceof ReturnStatement) { replaceExpression(((ReturnStatement) s).retVals, ass); }
		if (s instanceof Stmt_Expression) {
			Stmt_Expression ss = ((Stmt_Expression) s).copy();
			replaceExpression(ss.expression, ass);
		}
		
	}
	
	private void replaceExpression(Expression e, Token ass) {
		if (e instanceof Expr_Assign) {
			//AssignExpression c = (AssignExpression) e;
			//if (c.name.isModular()) c.name = ass;
			//if (c.operator.isModular()) c.operator = ass;
			//replaceExpression(c.value, ass);
		}
		else if (e instanceof Expr_Binary) {
			//BinaryExpression c = (BinaryExpression) e;
			//if (c.operator.isModular()) { c.operator = ass; c.modular = false; }
			//replaceExpression(c.left, ass);
			//replaceExpression(c.right, ass);
		}
	}
	
}