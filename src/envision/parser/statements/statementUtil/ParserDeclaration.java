package envision.parser.statements.statementUtil;

import envision.lang.util.VisibilityType;
import envision.lang.util.data.DataModifier;
import envision.parser.expressions.expressions.GenericExpression;
import envision.tokenizer.Token;
import eutil.datatypes.EArrayList;

public class ParserDeclaration {
	
	public static int num = 0;
	
	public int id;
	public DeclarationStage stage = DeclarationStage.VISIBILITY;
	private DeclarationType declarationType = null;
	private VisibilityType vis = VisibilityType.SCOPE;
	private EArrayList<Token> parameters = new EArrayList();
	private EArrayList<DataModifier> modifiers = new EArrayList();
	private EArrayList<GenericExpression> generics = new EArrayList();
	private Token returnType = null;
	
	public ParserDeclaration() { this(VisibilityType.SCOPE, new EArrayList<Token>(), new EArrayList<DataModifier>(), new EArrayList<GenericExpression>()); }
	public ParserDeclaration(VisibilityType visIn) { this(visIn, new EArrayList<Token>(), new EArrayList<DataModifier>(), new EArrayList<GenericExpression>()); }
	public ParserDeclaration(VisibilityType visIn, EArrayList<Token> paramsIn, EArrayList<DataModifier> modsIn, EArrayList<GenericExpression> genericsIn) {
		vis = visIn;
		parameters = paramsIn;
		modifiers = modsIn;
		generics = genericsIn;
		id = num++;
	}
	
	public ParserDeclaration(ParserDeclaration in) {
		stage = in.stage;
		vis = in.vis;
		parameters = new EArrayList<Token>(in.getParams());
		modifiers = new EArrayList<DataModifier>(in.getMods());
		generics = new EArrayList<GenericExpression>(in.getGenerics());
		returnType = in.returnType;
		id = num++;
	}
	
	@Override
	public String toString() {
		String m = (modifiers.isEmpty()) ? "" : " " + modifiers.toString();
		String g = (generics.isEmpty()) ? "" : " <" + generics.toString() + ">";
		String r = (returnType != null) ? " " + returnType.lexeme : "";
		String p = "";
		if (parameters.isNotEmpty()) {
			p += "<";
			String parm = "";
			for (Token t : parameters) {
				parm += t.lexeme + ", ";
			}
			parm = parm.substring(0, parm.length() - 2);
			p += parm + ">";
		}
		String out = vis + "" + declarationType + m + g + r + p + " ";
		//out = "{[" + stage + "] " + out + "}";
		return out;
	}
	
	public ParserDeclaration applyVisibility(VisibilityType visIn) { vis = visIn; return this; }
	public ParserDeclaration applyParams(EArrayList<Token> paramsIn) { parameters.addAll(paramsIn); return this; }
	public ParserDeclaration applyDataMods(EArrayList<DataModifier> modsIn) { modifiers.addAll(modsIn); return this; }
	public ParserDeclaration applyGenerics(EArrayList<GenericExpression> genericsIn) { generics.addAll(genericsIn); return this; }
	public ParserDeclaration applyReturnType(Token returnTypeIn) { returnType = returnTypeIn; return this; }
	public ParserDeclaration applyDeclarationType(DeclarationType typeIn) { declarationType = typeIn; return this; }
	
	public ParserDeclaration setValues(ParserDeclaration in) {
		DeclarationStage s = in.getStage();
		switch (s) {
		case TYPE: applyReturnType(in.returnType);
		case GENERICS: applyGenerics(in.generics);
		case DATAMODS: applyDataMods(in.modifiers);
		case VISIBILITY: applyVisibility(in.vis);
		default: break;
		}
		//setStage(s);
		return this;
	}
	
	public ParserDeclaration addParameter(Token paramIn) { parameters.add(paramIn); return this; }
	public ParserDeclaration addModifier(DataModifier modIn) { modifiers.add(modIn); return this; }
	public ParserDeclaration addGeneric(GenericExpression genericIn) { generics.add(genericIn); return this; }
	
	public ParserDeclaration advanceStage() { stage = stage.next(); return this; }
	public ParserDeclaration setStage(DeclarationStage in) { stage = in; return this; }
	
	public boolean hasDataMods() { return modifiers.isNotEmpty(); }
	public boolean hasParams() { return parameters.isNotEmpty(); }
	public boolean hasGenerics() { return generics.isNotEmpty(); }
	
	public boolean isFinal() { return modifiers.contains(DataModifier.FINAL); }
	public boolean isStatic() { return modifiers.contains(DataModifier.STATIC); }
	public boolean isAbstract() { return modifiers.contains(DataModifier.ABSTRACT); }
	public boolean isOverriding() { return modifiers.contains(DataModifier.OVERRIDE); }
	public boolean isStrong() { return modifiers.contains(DataModifier.STRONG); }
	public boolean isPublic() { return vis == VisibilityType.PUBLIC; }
	public boolean isPrivate() { return vis == VisibilityType.PRIVATE; }
	public boolean isProtected() { return vis == VisibilityType.PROTECTED; }
	
	public DeclarationStage getStage() { return stage; }
	public DeclarationType getDeclarationType() { return declarationType; }
	public VisibilityType getVisibility() { return vis; }
	public EArrayList<Token> getParams() { return parameters; }
	public EArrayList<DataModifier> getMods() { return modifiers; }
	public EArrayList<GenericExpression> getGenerics() { return generics; }
	public Token getReturnType() { return returnType; }
	
	public static ParserDeclaration createIfNull() { return createIfNull(DeclarationStage.VISIBILITY); }
	public static ParserDeclaration createIfNull(DeclarationStage stageIn) {
		return new ParserDeclaration().setStage(stageIn);
	}
	
}
