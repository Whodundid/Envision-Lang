package main;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Target;

@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE, TYPE_PARAMETER, TYPE_USE })
public @interface Experimental_Envision {
	
}
