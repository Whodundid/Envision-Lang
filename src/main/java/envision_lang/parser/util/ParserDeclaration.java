package envision_lang.parser.util;

import envision_lang.lang.natives.DataModifier;
import envision_lang.lang.natives.EnvisionVisibilityModifier;
import envision_lang.parser.expressions.expression_types.Expr_Generic;
import envision_lang.tokenizer.Token;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class ParserDeclaration {
	
	//========
	// Fields
	//========
	
	public DeclarationStage stage = DeclarationStage.VISIBILITY;
	private DeclarationType declarationType;
	private EnvisionVisibilityModifier vis;
	private EList<Token<?>> parameters;
	private EList<DataModifier> modifiers;
	private EList<Expr_Generic> generics;
	private Token<?> returnType;
	private Token<?> startToken;
	
	//==============
	// Constructors
	//==============
	
	public ParserDeclaration() {
		vis = EnvisionVisibilityModifier.SCOPE;
		parameters = new EArrayList<>(0);
		modifiers = new EArrayList<>(0);
		generics = new EArrayList<>(0);
	}
	
	public ParserDeclaration(EnvisionVisibilityModifier visIn) {
		vis = visIn;
		parameters = new EArrayList<>(0);
		modifiers = new EArrayList<>(0);
		generics = new EArrayList<>(0);
	}
	
	public ParserDeclaration(EnvisionVisibilityModifier visIn,
							 EList<Token<?>> paramsIn,
							 EList<DataModifier> modsIn,
							 EList<Expr_Generic> genericsIn)
	{
		vis = visIn;
		parameters = paramsIn;
		modifiers = modsIn;
		generics = genericsIn;
	}
	
	public ParserDeclaration(ParserDeclaration in) {
		stage = in.stage;
		vis = in.vis;
		parameters = new EArrayList<>(in.getParams());
		modifiers = new EArrayList<>(in.getMods());
		generics = new EArrayList<>(in.getGenerics());
		returnType = in.returnType;
	}
	
	@Override
	public String toString() {
		String m = (modifiers.isEmpty()) ? "" : " " + modifiers.toString();
		String g = (generics.isEmpty()) ? "" : " <" + generics.toString() + ">";
		String r = (returnType != null) ? " " + returnType.getLexeme() : "";
		String p = "";
		if (parameters.isNotEmpty()) {
			p += "<";
			String parm = "";
			for (Token<?> t : parameters) {
				parm += t.getLexeme() + ", ";
			}
			parm = parm.substring(0, parm.length() - 2);
			p += parm + ">";
		}
		String out = vis + "" + declarationType + m + g + r + p + " ";
		//out = "{[" + stage + "] " + out + "}";
		return out;
	}
	
	public ParserDeclaration applyVisibility(EnvisionVisibilityModifier visIn) { vis = visIn; return this; }
	public ParserDeclaration applyParams(EList<Token<?>> paramsIn) { parameters.addAll(paramsIn); return this; }
	public ParserDeclaration applyDataMods(EList<DataModifier> modsIn) { modifiers.addAll(modsIn); return this; }
	public ParserDeclaration applyGenerics(EList<Expr_Generic> genericsIn) { generics.addAll(genericsIn); return this; }
	public ParserDeclaration applyReturnType(Token<?> returnTypeIn) { returnType = returnTypeIn; return this; }
	public ParserDeclaration setDeclarationType(DeclarationType typeIn) { declarationType = typeIn; return this; }
	
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
	
	public ParserDeclaration addParameter(Token<?> paramIn) { parameters.add(paramIn); return this; }
	public ParserDeclaration addModifier(DataModifier modIn) { modifiers.add(modIn); return this; }
	public ParserDeclaration addGeneric(Expr_Generic genericIn) { generics.add(genericIn); return this; }
	
	public ParserDeclaration advanceStage() { stage = stage.next(); return this; }
	public ParserDeclaration setStage(DeclarationStage in) { stage = in; return this; }
	public ParserDeclaration setStartToken(Token<?> t) { startToken = t; return this; }
	
	public boolean hasDataMods() { return modifiers.isNotEmpty(); }
	public boolean hasParams() { return parameters.isNotEmpty(); }
	public boolean hasGenerics() { return generics.isNotEmpty(); }
	
	public boolean isFinal() { return modifiers.contains(DataModifier.FINAL); }
	public boolean isStatic() { return modifiers.contains(DataModifier.STATIC); }
	public boolean isAbstract() { return modifiers.contains(DataModifier.ABSTRACT); }
	public boolean isStrong() { return modifiers.contains(DataModifier.STRONG); }
	public boolean isPublic() { return vis == EnvisionVisibilityModifier.PUBLIC; }
	public boolean isPrivate() { return vis == EnvisionVisibilityModifier.PRIVATE; }
	public boolean isProtected() { return vis == EnvisionVisibilityModifier.PROTECTED; }
	
	public DeclarationStage getStage() { return stage; }
	public DeclarationType getDeclarationType() { return declarationType; }
	public EnvisionVisibilityModifier getVisibility() { return vis; }
	public EList<Token<?>> getParams() { return parameters; }
	public EList<DataModifier> getMods() { return modifiers; }
	public EList<Expr_Generic> getGenerics() { return generics; }
	public Token<?> getReturnType() { return returnType; }
	public Token<?> getStartToken() { return startToken; }
	
	public static ParserDeclaration createIfNull() { return createIfNull(DeclarationStage.VISIBILITY); }
	public static ParserDeclaration createIfNull(DeclarationStage stageIn) {
		return new ParserDeclaration().setStage(stageIn);
	}
	
}
