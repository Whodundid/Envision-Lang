package envision.parser.statements.statementParsers;

import static envision.tokenizer.ReservedWord.*;
import static envision.tokenizer.Operator.*;
import static envision.tokenizer.KeywordType.*;

import envision.parser.GenericParser;
import envision.parser.expressions.expression_types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statement_types.BlockStatement;
import envision.parser.statements.statement_types.ClassStatement;
import envision.parser.statements.statement_types.FuncDefStatement;
import envision.parser.util.DeclarationStage;
import envision.parser.util.ParserDeclaration;
import envision.tokenizer.ReservedWord;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

import java.util.Iterator;

public class PS_Class extends GenericParser {
	
	/**
	 * Attempts to parse a class from tokens.
	 * @return Statement
	 */
	public static Statement classDeclaration() { return classDeclaration(new ParserDeclaration()); }
	public static Statement classDeclaration(ParserDeclaration declaration) {
		declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.TYPE);
		Token name = consume(IDENTIFIER, "Expected a valid class name!");
		//ParserStage.curClassName = name;
		
		//check for parameters
		if (check(LT)) {
			for (Token t : getParameters()) {
				declaration.addParameter(t);
			}
		}
		
		ClassStatement cs = new ClassStatement(name, declaration);
		
		if (match(COLON)) {
			do {
				if (check(IDENTIFIER)) consume(IDENTIFIER, "Expected super class name.");
				else if (checkType(DATATYPE)) consume("Expected a valid datatype.", BOOLEAN, INT, DOUBLE, CHAR, STRING, NUMBER);
				else error("Expected a valid super class type!");
				cs.addSuper(new VarExpression(previous()));
			}
			while (match(COMMA));
		}
		
		//read in class body
		consume(CURLY_L, "Expected '{' after class declaration!");
		
		//get the base class body then proceed to isolate static members and constructors
		EArrayList<Statement> body = getBlock();
		EArrayList<Statement> staticMembers = new EArrayList();
		//EArrayList<MethodDeclarationStatement> methods = new EArrayList();
		EArrayList<FuncDefStatement> constructors = new EArrayList();
		
		//unpack top level block statements from body
		int bodySize = body.size();
		
		for (int i = 0; i < bodySize; i++) {
			Statement s = body.get(i);
			//check if constructor -- remove from body
			if (s instanceof BlockStatement) {
				BlockStatement b = (BlockStatement) body.remove(i);
				bodySize--;
				for (Statement bs : b.statements) {
					body.add(i, bs);
					bodySize++;
					i++;
				}
				i--; //decrement to realign the loop
			}
		}
		
		//isolate static members
		for (int i = 0; i < bodySize; i++) {
			Statement s = body.get(i);
			ParserDeclaration dec = s.getDeclaration();
			
			//check if static -- remove from body
			if (dec != null && dec.isStatic()) {
				staticMembers.add(body.remove(i));
				bodySize--;
				i--;
			}
		}
		
		//isolate constructors and methods
		for (int i = 0; i < bodySize; i++) {
			Statement s = body.get(i);
			
			if (s instanceof BlockStatement) {
				constructors.addAll(isolateConstructors((BlockStatement) s));
				//System.out.println("add: " + constructors);
			}
			
			//check if constructor -- remove from body
			if (s instanceof FuncDefStatement) {
				FuncDefStatement meth = (FuncDefStatement) s;
				if (meth.isConstructor) {
					constructors.add((FuncDefStatement) body.remove(i));
					bodySize--;
					i--;
				}
				//else methods.add((MethodDeclarationStatement) body.remove(i));
				//bodySize--;
				//i--;
			}
		}
		
		//apply body, static members, and constructors
		cs.setBody(body);
		cs.setStaticMembers(staticMembers);
		//cs.setMethods(methods);
		cs.setInitializers(constructors);
		
		//System.out.println("Body: " + body);
		//System.out.println("Static: " + staticMembers);
		//System.out.println("Constr: " + constructors);
		
		//return curClassName to null
		//ParserStage.curClassName = null;
		
		return cs;
	}
	
	private static EArrayList<FuncDefStatement> isolateConstructors(BlockStatement in) {
		EArrayList<FuncDefStatement> constructors = new EArrayList();
		Iterator<Statement> it = in.statements.iterator();
		
		while (it.hasNext()) {
			Statement s = it.next();
			
			if (s instanceof BlockStatement bs) {
				constructors.addAll(isolateConstructors(bs));
			}
			else if (s instanceof FuncDefStatement mds && mds.isConstructor) {
				mds.name = Token.create(ReservedWord.STRING_LITERAL, "init", mds.name.line);
				constructors.add(mds);
				it.remove();
			}
		}
		
		return constructors;
	}
	
}
