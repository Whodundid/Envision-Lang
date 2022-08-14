package envision_lang.packages;

public interface Buildable {
	
	default void buildFunctions() {}
	default void buildFields() {}
	default void buildClasses() {}
	default void buildPackages() {}
	
}
