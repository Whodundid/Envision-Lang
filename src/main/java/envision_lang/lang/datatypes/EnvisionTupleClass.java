package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;

public final class EnvisionTupleClass extends EnvisionClass {

	/**
	 * The singular, static Tuple class for which all Envision:Tuple
	 * objects are derived from.
	 */
	public static final EnvisionTupleClass TUPLE_CLASS = new EnvisionTupleClass();
	
	private static final IPrototypeHandler TUPLE_PROTOTYPES = new IPrototypeHandler();
	
	static {
		TUPLE_PROTOTYPES.define("contains", BOOLEAN, VAR).assignDynamicClass(IFunc_contains.class);
		TUPLE_PROTOTYPES.define("copy", LIST).assignDynamicClass(IFunc_copy.class);
		TUPLE_PROTOTYPES.define("flip", LIST).assignDynamicClass(IFunc_flip.class);
		TUPLE_PROTOTYPES.define("get", VAR, INT).assignDynamicClass(IFunc_get.class);
		TUPLE_PROTOTYPES.define("getFirst", VAR).assignDynamicClass(IFunc_getFirst.class);
		TUPLE_PROTOTYPES.define("getLast", VAR).assignDynamicClass(IFunc_getLast.class);
		TUPLE_PROTOTYPES.define("hasOne", BOOLEAN).assignDynamicClass(IFunc_hasOne.class);
		TUPLE_PROTOTYPES.define("isEmpty", BOOLEAN).assignDynamicClass(IFunc_isEmpty.class);
		TUPLE_PROTOTYPES.define("isNotEmpty", BOOLEAN).assignDynamicClass(IFunc_isNotEmpty.class);
		TUPLE_PROTOTYPES.define("notContains", BOOLEAN, VAR).assignDynamicClass(IFunc_notContains.class);
		TUPLE_PROTOTYPES.define("random", VAR).assignDynamicClass(IFunc_random.class);
		TUPLE_PROTOTYPES.define("shuffle", LIST).assignDynamicClass(IFunc_shuffle.class);
		TUPLE_PROTOTYPES.define("size", INT).assignDynamicClass(IFunc_size.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionTupleClass() {
		super(Primitives.TUPLE);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionTuple newTuple() {
		EnvisionTuple tuple = new EnvisionTuple();
		TUPLE_CLASS.defineScopeMembers(tuple);
		return tuple;
	}
	
	public static EnvisionTuple newTuple(List<? extends EnvisionObject> data) {
		if (data.isEmpty()) return EnvisionTuple.EMPTY_TUPLE;
		
		EnvisionTuple tuple = newTuple();
		var list = tuple.getInternalList();
		list.ensureCapacity(data.size());
		for (var o : data) list.add(o);
		return tuple;
	}
	
    /**
     * Attempts to create a new EnvisionTuple using the given 'data'
     * argument as its initial values.
     * 
     * @param dataIn
     * 
     * @return An EnvisionTuple populated with the given 'data'
     */
    public static EnvisionTuple newTuple(Object dataIn) {
        if (dataIn == null) throw new NullPointerException("Error! Cannot create EnvisionTuple from Java::NULL!");
        
        // collection path
        if (dataIn instanceof Collection<?> c) return newTupleFromCollection(c);
        // array path
        else if (dataIn.getClass().isArray()) return newTupleFromArray(dataIn);
        
        // object wrap path
        EnvisionTuple tuple = new EnvisionTuple();
        tuple.add(ObjectCreator.wrap(dataIn));
        TUPLE_CLASS.defineScopeMembers(tuple);
        return tuple;
    }
	
    /**
     * 
     * @param collectionIn
     * @return
     */
    public static EnvisionTuple newTupleFromCollection(Collection<?> collectionIn) {
        EnvisionTuple tuple = newTuple();
        var it = collectionIn.iterator();
        while (it.hasNext()) {
            var value = it.next();
            IDatatype type = IDatatype.dynamicallyDetermineType(value);
            tuple.add(ObjectCreator.createObject(type, value));
        }
        
        TUPLE_CLASS.defineScopeMembers(tuple);
        return tuple;
    }
    
    /**
     * 
     * @param arrayObject
     * @return
     */
    public static EnvisionTuple newTupleFromArray(Object arrayObject) {
        if (arrayObject == null || !arrayObject.getClass().isArray()) {
            throw new EnvisionLangError("Error! Cannot create an EnvisionList from a Java::NULL or non-array object!");
        }
        
        final int length = Array.getLength(arrayObject);
        EnvisionTuple tuple = newTuple();
        
        Class<?> arrayClass = arrayObject.getClass();
        Class<?> arrayComponentType = arrayClass.componentType();
        
        IDatatype type = IDatatype.fromJavaClass(arrayComponentType);
        for (int i = 0; i < length; i++) {
            Object value = Array.get(arrayObject, i);
            tuple.add(ObjectCreator.createObject(type, value, true));
        }
        
        TUPLE_CLASS.defineScopeMembers(tuple);
        return tuple;
    }
    
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionTuple newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected EnvisionTuple buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		if (args.length == 0) return EnvisionTuple.EMPTY_TUPLE;
		
		EnvisionTuple tuple = new EnvisionTuple();
		var list = tuple.getInternalList();
		
		//load any args
		list.addA(args);
		
		//define scope members
		defineScopeMembers(tuple);
		
		//return built list instance
		return tuple;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define scope members
		TUPLE_PROTOTYPES.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	/**
	 * Checks if the given object is present within the current list
	 * instance.
	 * 
	 * @param some object to check if contains
	 * 
	 * @return boolean true if contains
	 */
	public static class IFunc_contains extends InstanceFunction<EnvisionTuple> {
		public IFunc_contains() { super(BOOLEAN, "contains", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.contains(args[0]));
		}
	}
	
	/**
	 * Checks if the given object is not present within the current list
	 * instance.
	 * 
	 * @param some object to check if not contains
	 * 
	 * @return boolean true if not contains
	 */
	public static class IFunc_notContains extends InstanceFunction<EnvisionTuple> {
		public IFunc_notContains() { super(BOOLEAN, "notContains", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.notContains(args[0]));
		}
	}

	/**
	 * Creates a shallow copy of this list.
	 * Note: does not actually create deep copies of the contents of the list.
	 * 
	 * @return EnvisionTuple the shallow list copy
	 */
	public static class IFunc_copy extends InstanceFunction<EnvisionTuple> {
		public IFunc_copy() { super(LIST, "copy"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(new EnvisionTuple(inst));
		}
	}
	
	/**
	 * Returns a new EnvisionTuple with the contents of the original list
	 * in reverse order.
	 * 
	 * @return EnvisionTuple the list with elements in reversed order
	 */
	public static class IFunc_flip extends InstanceFunction<EnvisionTuple> {
		public IFunc_flip() { super(LIST, "flip"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.flip());
		}
	}
	
	/**
	 * Returns the element at the given index from the current list instance.
	 * Throws an EmptyListError if the current list is empty.
	 * 
	 * @param index The index of the object to retrieve
	 * 
	 * @return EnvisionObject the object at the given index
	 */
	public static class IFunc_get extends InstanceFunction<EnvisionTuple> {
		public IFunc_get() { super(VAR, "get", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.get((EnvisionInt) args[0]));
		}
	}
	
	/**
	 * Returns the first element from the current list instance.
	 * Throws an EmptyListError if the current list is empty.
	 * 
	 * @return EnvisionObject the object in the last index
	 */
	public static class IFunc_getFirst extends InstanceFunction<EnvisionTuple> {
		public IFunc_getFirst() { super(VAR, "getFirst"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getFirst());
		}
	}
	
	/**
	 * Returns the last element from the current list instance.
	 * Throws an EmptyListError if the current list is empty.
	 * 
	 * @return EnvisionObject the object in the last index
	 */
	public static class IFunc_getLast extends InstanceFunction<EnvisionTuple> {
		public IFunc_getLast() { super(VAR, "getLast"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getLast());
		}
	}
	
	/**
	 * Returns true if the given list instance contains exactly one element.
	 * 
	 * @return EnvisionBoolean true if the list has one element
	 */
	public static class IFunc_hasOne extends InstanceFunction<EnvisionTuple> {
		public IFunc_hasOne() { super(BOOLEAN, "hasOne"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.hasOne());
		}
	}
	
	/**
	 * Returns true if the given list instance contains zero elements.
	 * 
	 * @return EnvisionBoolean true if the list is empty
	 */
	public static class IFunc_isEmpty extends InstanceFunction<EnvisionTuple> {
		public IFunc_isEmpty() { super(BOOLEAN, "isEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isEmpty());
		}
	}
	
	/**
	 * Returns true if the given list instance contains at least one element.
	 * 
	 * @return EnvisionBoolean true if the list is not empty
	 */
	public static class IFunc_isNotEmpty extends InstanceFunction<EnvisionTuple> {
		public IFunc_isNotEmpty() { super(BOOLEAN, "isNotEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isNotEmpty());
		}
	}
	
	/**
	 * Returns a random element from this tuple.
	 * 
	 * @return Random element in this tuple
	 */
	public static class IFunc_random extends InstanceFunction<EnvisionTuple> {
		public IFunc_random() { super(VAR, "random"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.random());
		}
	}
	
	/**
	 * Returns a new EnvisionTuple with the contents of the original list
	 * positioned in a random ordering.
	 * 
	 * @return EnvisionTuple a shuffled list
	 */
	public static class IFunc_shuffle extends InstanceFunction<EnvisionTuple> {
		public IFunc_shuffle() { super(LIST, "shuffle"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.shuffle());
		}
	}
	
	/**
	 * Returns the total number of elements in this list.
	 * 
	 * @return the size of the list
	 */
	public static class IFunc_size extends InstanceFunction<EnvisionTuple> {
		public IFunc_size() { super(INT, "size"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.size());
		}
	}
	
}