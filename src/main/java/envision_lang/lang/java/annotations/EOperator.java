package envision_lang.lang.java.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface EOperator {
	
	/** The Envision code that relates to this field or function. */
	String value() default "";
	
	String returnsType() default "";
	
}
