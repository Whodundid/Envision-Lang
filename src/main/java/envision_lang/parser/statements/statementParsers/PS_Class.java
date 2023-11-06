package envision_lang.parser.statements.statementParsers;

import static envision_lang.tokenizer.Operator.*;
import static envision_lang.tokenizer.ReservedWord.*;

import java.util.Iterator;

import envision_lang.parser.ParserHead;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.statement_types.Stmt_Block;
import envision_lang.parser.statements.statement_types.Stmt_Class;
import envision_lang.parser.statements.statement_types.Stmt_FuncDef;
import envision_lang.parser.util.DeclarationStage;
import envision_lang.parser.util.ParserDeclaration;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class PS_Class extends ParserHead {
	
	/**
	 * Attempts to parse a class from tokens.
	 * @return Statement
	 */
	public static ParsedStatement classDeclaration() { return classDeclaration(new ParserDeclaration()); }
	public static ParsedStatement classDeclaration(ParserDeclaration declaration) {
	    // increment the class definition depth
	    ParserHead.pushClassDef();
	    
	    declaration = (declaration != null) ? declaration : new ParserDeclaration().setStage(DeclarationStage.TYPE);
		Token<?> name = consume(IDENTIFIER, "Expected a valid class name!");
		
		//removing class parameter parsing for now
		//check for parameters
		/*
		if (check(LT)) {
			for (Token t : getParameters()) {
				declaration.addParameter(t);
			}
		}
		*/
		
		Stmt_Class cs = new Stmt_Class(declaration.getStartToken(), name, declaration);
		if (declaration.isBlockingStatement()) cs.setBlockStatement(true);
		
//		if (match(COLON)) {
//			do {
//				if (check(IDENTIFIER)) consume(IDENTIFIER, "Expected super class name.");
//				else if (checkType(KeywordType.DATATYPE)) consume("Expected a valid datatype.", BOOLEAN, INT, DOUBLE, CHAR, STRING, NUMBER);
//				else error("Expected a valid super class type!");
//				cs.addSuper(new Expr_Var(previousNonTerminator()));
//			}
//			while (match(COMMA));
//		}
		
		
		// read in class body
		consume(CURLY_L, "Expected '{' after class declaration!");
		
		// get the base class body then proceed to isolate static members and constructors
		EList<ParsedStatement> body = getBlock();
		EList<ParsedStatement> staticMembers = EList.newList();
		EList<Stmt_FuncDef> constructors = EList.newList();
		
		//unpack top level block statements from body
		int bodySize = body.size();
		
		for (int i = 0; i < bodySize; i++) {
			ParsedStatement s = body.get(i);
			//check if constructor -- remove from body
			if (s instanceof Stmt_Block) {
				Stmt_Block b = (Stmt_Block) body.remove(i);
				bodySize--;
				for (ParsedStatement bs : b.statements) {
					body.add(i, bs);
					bodySize++;
					i++;
				}
				i--; //decrement to realign the loop index
			}
		}
		
		//isolate static members
		for (int i = 0; i < bodySize; i++) {
			ParsedStatement s = body.get(i);
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
			ParsedStatement s = body.get(i);
			
			if (s instanceof Stmt_Block b) {
				constructors.addAll(isolateConstructors(b));
			}
			
			//check if constructor -- remove from body
			if (s instanceof Stmt_FuncDef) {
				Stmt_FuncDef meth = (Stmt_FuncDef) s;
				if (meth.isConstructor) {
					constructors.add((Stmt_FuncDef) body.remove(i));
					bodySize--;
					i--;
				}
			}
		}
		
		//apply body, static members, and constructors
		cs.setBody(body);
		cs.setStaticMembers(staticMembers);
		cs.setInitializers(constructors);
		
        // pop this current class definition off of the stack
        ParserHead.popClassDef();
		
		return cs;
	}
	
	/** Recursively pulls out constructors out of blocks. */
	private static EList<Stmt_FuncDef> isolateConstructors(Stmt_Block in) {
		EList<Stmt_FuncDef> constructors = new EArrayList<>();
		Iterator<ParsedStatement> it = in.statements.iterator();
		
		while (it.hasNext()) {
			ParsedStatement s = it.next();
			
			if (s instanceof Stmt_Block bs) {
				constructors.addAll(isolateConstructors(bs));
			}
			else if (s instanceof Stmt_FuncDef funcDef && funcDef.isConstructor) {
				funcDef.name = Token.create(ReservedWord.STRING_LITERAL, "init", funcDef.name.getLineNum());
				constructors.add(funcDef);
				it.remove();
			}
		}
		
		return constructors;
	}
	
}
