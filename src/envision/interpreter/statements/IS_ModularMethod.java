package envision.interpreter.statements;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.DuplicateObjectError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.interpreterBase.StatementExecutor;
import envision.parser.expressions.Expression;
import envision.parser.expressions.expressions.AssignExpression;
import envision.parser.expressions.expressions.BinaryExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statements.BlockStatement;
import envision.parser.statements.statements.ExpressionStatement;
import envision.parser.statements.statements.ModularMethodStatement;
import envision.parser.statements.statements.ReturnStatement;
import envision.tokenizer.Token;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;
import main.Experimental_Envision;

@Experimental_Envision
public class IS_ModularMethod extends StatementExecutor<ModularMethodStatement> {

	public IS_ModularMethod(EnvisionInterpreter in) {
		super(in);
	}
	
	public static void run(EnvisionInterpreter in, ModularMethodStatement s) {
		new IS_ModularMethod(in).run(s);
	}
	
	//----------------------------------------------------------------------------------
	
	@Override
	public void run(ModularMethodStatement s) {
		BoxList<Token, Token> associations = s.associations;
		
		for (Box2<Token, Token> modAssociation : associations) {
			//Token refName = s.name;
			//String methName = ((refName.isModular()) ? "" : refName.lexeme) + modAssociation.getA().lexeme;
			//String methName = refName.lexeme;
			
			//attempt to find any already existing base method within the given scope
			Object base = scope().get(s.name.lexeme);
			
			//if there is already an existing method of the same name, this is invalid!
			if (base != null) {
				throw new DuplicateObjectError(s.name.lexeme);
			}
			else {
				//if the statement is valid, iterate across all of the method's scope statements
				//and replace MODULAR_VALUE tokens with the association value.
				EArrayList<Statement> statements = s.body;
				EArrayList<Statement> newStatements = new EArrayList();
				
				for (Statement stmt : statements) {
					
					//unbox block statements
					if (stmt instanceof BlockStatement) {
						EArrayList<Statement> found = ((BlockStatement) stmt).statements;
						EArrayList<BlockStatement> workList = new EArrayList();
						
						while (workList.isNotEmpty()) {
							EArrayList<BlockStatement> newWork = new EArrayList();
							
							for (BlockStatement bs : workList) {
								EArrayList<Statement> bsStatements = bs.statements;
								for (Statement bss : bsStatements)
									if (bss instanceof BlockStatement) newWork.add((BlockStatement) bss);
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
					else if (stmt instanceof ReturnStatement) {
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
		if (s instanceof BlockStatement) throw new EnvisionError("Replace Modular: this shouldn't be possible.");
		//if (s instanceof ReturnStatement) { replaceExpression(((ReturnStatement) s).retVals, ass); }
		if (s instanceof ExpressionStatement) {
			ExpressionStatement ss = ((ExpressionStatement) s).copy();
			replaceExpression(ss.expression, ass);
		}
		
	}
	
	private void replaceExpression(Expression e, Token ass) {
		if (e instanceof AssignExpression) {
			//AssignExpression c = (AssignExpression) e;
			//if (c.name.isModular()) c.name = ass;
			//if (c.operator.isModular()) c.operator = ass;
			//replaceExpression(c.value, ass);
		}
		else if (e instanceof BinaryExpression) {
			//BinaryExpression c = (BinaryExpression) e;
			//if (c.operator.isModular()) { c.operator = ass; c.modular = false; }
			//replaceExpression(c.left, ass);
			//replaceExpression(c.right, ass);
		}
	}
	
}