package envision.parser.statements.stages;

import static envision.tokenizer.Keyword.*;
import static envision.tokenizer.Keyword.KeywordType.DATATYPE;

import envision.parser.ParserStage;
import envision.parser.expressions.types.VarExpression;
import envision.parser.statements.Statement;
import envision.parser.statements.statementUtil.DeclarationStage;
import envision.parser.statements.statementUtil.ParserDeclaration;
import envision.parser.statements.types.BlockStatement;
import envision.parser.statements.types.ClassStatement;
import envision.parser.statements.types.MethodDeclarationStatement;
import envision.tokenizer.Keyword;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

import java.util.Iterator;

public class PS_Class extends ParserStage {
	
	/**
	 * Attempts to parse a class from tokens.
	 * @return Statement
	 */
	public static Statement classDeclaration() { return classDeclaration(new ParserDeclaration()); }
	public static Statement classDeclaration(ParserDeclaration declaration) {
		declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.TYPE);
		Token name = consume(IDENTIFIER, "Expected a valid class name!");
		ParserStage.curClassName = name;
		
		//check for parameters
		if (check(LESS_THAN)) {
			for (Token t : getParameters()) {
				declaration.addParameter(t);
			}
		}
		
		ClassStatement cs = new ClassStatement(name, declaration);
		
		if (match(COLON)) {
			do {
				if (check(IDENTIFIER)) consume(IDENTIFIER, "Expected super class name.");
				else if (checkType(DATATYPE)) consumeAny("Expected a valid datatype.", BOOLEAN, INT, DOUBLE, CHAR, STRING, NUMBER, OBJECT);
				else error("Expected a valid super class type!");
				cs.addSuper(new VarExpression(previous()));
			}
			while (match(COMMA));
		}
		
		//read in class body
		consume(SCOPE_LEFT, "Expected '{' after class declaration!");
		
		//get the base class body then proceed to isolate static members and constructors
		EArrayList<Statement> body = getBlock();
		EArrayList<Statement> staticMembers = new EArrayList();
		//EArrayList<MethodDeclarationStatement> methods = new EArrayList();
		EArrayList<MethodDeclarationStatement> constructors = new EArrayList();
		
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
			if (s instanceof MethodDeclarationStatement) {
				MethodDeclarationStatement meth = (MethodDeclarationStatement) s;
				if (meth.isConstructor) {
					constructors.add((MethodDeclarationStatement) body.remove(i));
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
		cs.setConstructors(constructors);
		
		//System.out.println("Body: " + body);
		//System.out.println("Static: " + staticMembers);
		//System.out.println("Constr: " + constructors);
		
		//return curClassName to null
		ParserStage.curClassName = null;
		
		return cs;
	}
	
	private static EArrayList<MethodDeclarationStatement> isolateConstructors(BlockStatement in) {
		EArrayList<MethodDeclarationStatement> constructors = new EArrayList();
		Iterator<Statement> it = in.statements.iterator();
		
		while (it.hasNext()) {
			Statement s = it.next();
			
			if (s instanceof BlockStatement) {
				constructors.addAll(isolateConstructors((BlockStatement) s));
			}
			else if (s instanceof MethodDeclarationStatement) {
				MethodDeclarationStatement m = (MethodDeclarationStatement) s;
				
				if (m.isConstructor) {
					m.name = Token.create(Keyword.STRING_LITERAL, "init", m.name.line);
					constructors.add(m);
					it.remove();
				}
			}
		}
		
		return constructors;
	}
	
}
