package envision_lang.lang.packages;

public interface Buildable {
	public default void buildFunctions() {}
	public default void buildFields() {}
	public default void buildClasses() {}
	public default void buildPackages() {}
}
