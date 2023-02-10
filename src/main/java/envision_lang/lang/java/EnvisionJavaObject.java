package envision_lang.lang.java;

public abstract class EnvisionJavaObject extends EnvisionBridge {
	
	public void set(String name, Object value) {
		set_class_i(name, value);
	}
	
	public <E> E get(String name) {
		return get_class_i(name);
	}
	
}
