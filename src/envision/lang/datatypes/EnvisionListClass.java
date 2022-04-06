package envision.lang.datatypes;

import static envision.lang.util.Primitives.BOOLEAN;
import static envision.lang.util.Primitives.INT;
import static envision.lang.util.Primitives.LIST;
import static envision.lang.util.Primitives.STRING;
import static envision.lang.util.Primitives.VAR;
import static envision.lang.util.Primitives.VAR_A;

import java.util.List;

import envision.interpreter.EnvisionInterpreter;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.IPrototypeHandler;
import envision.lang.util.InstanceFunction;
import envision.lang.util.Primitives;

public class EnvisionListClass extends EnvisionClass {

	/**
	 * The singular, static List class for which all Envision:List
	 * objects are derived from.
	 */
	public static final EnvisionListClass LIST_CLASS = new EnvisionListClass();
	
	private static final IPrototypeHandler LIST_PROTOTYPES = new IPrototypeHandler();
	
	static {
		LIST_PROTOTYPES.addFunction("add", BOOLEAN, VAR);
		LIST_PROTOTYPES.addFunction("addR", VAR, VAR);
		LIST_PROTOTYPES.addFunction("addRT", LIST, VAR);
		LIST_PROTOTYPES.addFunction("clear", LIST);
		LIST_PROTOTYPES.addFunction("contains", BOOLEAN, VAR);
		LIST_PROTOTYPES.addFunction("copy", LIST);
		LIST_PROTOTYPES.addFunction("fill", LIST, VAR_A);
		LIST_PROTOTYPES.addFunction("flip", LIST);
		LIST_PROTOTYPES.addFunction("get", VAR, INT);
		LIST_PROTOTYPES.addFunction("getFirst", VAR);
		LIST_PROTOTYPES.addFunction("getLast", VAR);
		LIST_PROTOTYPES.addFunction("getListType", STRING);
		LIST_PROTOTYPES.addFunction("hasOne", BOOLEAN);
		LIST_PROTOTYPES.addFunction("isEmpty", BOOLEAN);
		LIST_PROTOTYPES.addFunction("isNotEmpty", BOOLEAN);
		LIST_PROTOTYPES.addFunction("isSizeLocked", BOOLEAN);
		LIST_PROTOTYPES.addFunction("lockSize", LIST);
		LIST_PROTOTYPES.addFunction("notContains", BOOLEAN);
		LIST_PROTOTYPES.addFunction("push", LIST, VAR);
		LIST_PROTOTYPES.addFunction("pop", VAR);
		LIST_PROTOTYPES.addFunction("remove", VAR, INT);
		LIST_PROTOTYPES.addFunction("removeFirst", VAR);
		LIST_PROTOTYPES.addFunction("removeLast", VAR);
		LIST_PROTOTYPES.addFunction("set", LIST, INT, VAR);
		LIST_PROTOTYPES.addFunction("setFirst", LIST, VAR);
		LIST_PROTOTYPES.addFunction("setLast", LIST, VAR);
		LIST_PROTOTYPES.addFunction("shiftLeft", LIST).addOverload(LIST, INT);
		LIST_PROTOTYPES.addFunction("shiftRight", LIST).addOverload(LIST, INT);
		LIST_PROTOTYPES.addFunction("shuffle", LIST);
		LIST_PROTOTYPES.addFunction("size", INT);
		LIST_PROTOTYPES.addFunction("swap", LIST, INT, INT);
	}
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Hide constructor to prevent any more than the single, static
	 * instance from being created.
	 */
	private EnvisionListClass() {
		super(Primitives.LIST);
		
		//set final to prevent user-extension
		setFinal();
	}
	
	//---------------------
	// Static Constructors
	//---------------------
	
	public static EnvisionList newList() { return newList(EnvisionDatatype.VAR_TYPE); }
	public static EnvisionList newList(Primitives type) { return newList(type.toDatatype()); }
	public static EnvisionList newList(EnvisionDatatype type) {
		EnvisionList list = new EnvisionList(type);
		LIST_CLASS.defineScopeMembers(list);
		return list;
	}
	
	public static EnvisionList newList(EnvisionDatatype type, List<EnvisionObject> data) {
		EnvisionList list = newList(type);
		list.addAll(data);
		return list;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public EnvisionList newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		//bypass class construct for primitive type
		return buildInstance(interpreter, args);
	}
	
	@Override
	protected EnvisionList buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionList list = new EnvisionList();
		
		//load any args
		for (var a : args) list.add(a);
		
		//define scope members
		defineScopeMembers(list);
		
		//return built list instance
		return list;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//define members
		LIST_PROTOTYPES.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	/**
	 * Adds an object to the current list instance.
	 * Throws error if size is locked.
	 * 
	 * @param Object some object to add
	 * 
	 * @return true if successful
	 */
	public static class IFunc_add<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_add(E instIn) { super(instIn, BOOLEAN, "add", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.add(args[0]));
		}
	}
	
	/**
	 * Adds an object to the current list instance.
	 * Throws error if size is locked.
	 * 
	 * @param Object some object to add
	 * 
	 * @return the given object
	 */
	public static class IFunc_addR<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_addR(E instIn) { super(instIn, VAR, "addR", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.addR(args[0]));
		}
	}
	
	/**
	 * Adds an object to the current list instance.
	 * Throws error if size is locked.
	 * 
	 * @param Object some object to add
	 * 
	 * @return the list being added to
	 */
	public static class IFunc_addRT<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_addRT(E instIn) { super(instIn, LIST, "addRT", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.addRT(args[0]));
		}
	}
	
	/**
	 * Clears all objects from this list.
	 * Throws error if size is locked.
	 * 
	 * @return the current list instance
	 */
	public static class IFunc_clear<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_clear(E instIn) { super(instIn, LIST, "clear"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.clear());
		}
	}
	
	/**
	 * Checks if the given object is present within the current list
	 * instance.
	 * 
	 * @param some object to check if contains
	 * 
	 * @return boolean true if contains
	 */
	public static class IFunc_contains<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_contains(E instIn) { super(instIn, BOOLEAN, "contains", VAR); }
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
	public static class IFunc_notContains<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_notContains(E instIn) { super(instIn, BOOLEAN, "notContains", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.notContains(args[0]));
		}
	}

	/**
	 * Creates a shallow copy of this list.
	 * Note: does not actually create deep copies of the contents of the list.
	 * 
	 * @return EnvisionList the shallow list copy
	 */
	public static class IFunc_copy<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_copy(E instIn) { super(instIn, LIST, "copy"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(new EnvisionList(inst));
		}
	}
	
	public static class IFunc_fill<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_fill(E instIn) { super(instIn, LIST, "fill", VAR_A); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			inst.fill(args);
			ret(inst);
		}
	}
	
	/**
	 * Returns a new EnvisionList with the contents of the original list
	 * in reverse order.
	 * 
	 * @return EnvisionList the list with elements in reversed order
	 */
	public static class IFunc_flip<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_flip(E instIn) { super(instIn, LIST, "flip"); }
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
	public static class IFunc_get<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_get(E instIn) { super(instIn, VAR, "get", INT); }
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
	public static class IFunc_getFirst<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_getFirst(E instIn) { super(instIn, VAR, "getFirst"); }
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
	public static class IFunc_getLast<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_getLast(E instIn) { super(instIn, VAR, "getLast"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getLast());
		}
	}
	
	public static class IFunc_getListType<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_getListType(E instIn) { super(instIn, STRING, "getListType"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getListType());
		}
	}
	
	/**
	 * Returns true if the given list instance contains exactly one element.
	 * 
	 * @return EnvisionBoolean true if the list has one element
	 */
	public static class IFunc_hasOne<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_hasOne(E instIn) { super(instIn, BOOLEAN, "hasOne"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.hasOne());
		}
	}
	
	/**
	 * Returns true if the given list instance contains zero elements.
	 * 
	 * @return EnvisionBoolean true if the list is empty
	 */
	public static class IFunc_isEmpty<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_isEmpty(E instIn) { super(instIn, BOOLEAN, "isEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isEmpty());
		}
	}
	
	/**
	 * Returns true if the given list instance contains at least one element.
	 * 
	 * @return EnvisionBoolean true if the list is not empty
	 */
	public static class IFunc_isNotEmpty<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_isNotEmpty(E instIn) { super(instIn, BOOLEAN, "isNotEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isNotEmpty());
		}
	}
	
	public static class IFunc_isSizeLocked<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_isSizeLocked(E instIn) { super(instIn, BOOLEAN, "isSizeLocked"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isSizeLocked());
		}
	}
	
	public static class IFunc_lockSize<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_lockSize(E instIn) { super(instIn, LIST, "lockSize", BOOLEAN); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setSizeLocked((EnvisionBoolean) args[0]));
		}
	}
	
	public static class IFunc_push<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_push(E instIn) { super(instIn, LIST, "push", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.push(args[0]));
		}
	}
	
	public static class IFunc_pop<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_pop(E instIn) { super(instIn, VAR, "pop"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.pop());
		}
	}
	
	public static class IFunc_remove<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_remove(E instIn) { super(instIn, VAR, "remove", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.remove((EnvisionInt) args[0]));
		}
	}
	
	public static class IFunc_removeFirst<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_removeFirst(E instIn) { super(instIn, VAR, "removeFirst"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.removeFirst());
		}
	}
	
	public static class IFunc_removeLast<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_removeLast(E instIn) { super(instIn, VAR, "removeLast"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.removeLast());
		}
	}
	
	public static class IFunc_set<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_set(E instIn) { super(instIn, VAR, "set", INT, VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.set((EnvisionInt) args[0], args[1]));
		}
	}
	
	public static class IFunc_setFirst<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_setFirst(E instIn) { super(instIn, VAR, "setFirst", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setFirst(args[0]));
		}
	}
	
	public static class IFunc_setLast<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_setLast(E instIn) { super(instIn, VAR, "setLast", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setLast(args[0]));
		}
	}
	
	public static class IFunc_setSize<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_setSize(E instIn) {
			super(instIn, LIST, "setSize", INT);
			//allow setSize with default object value
			addOverload(LIST, INT, VAR);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setSize(args));
		}
	}
	
	/**
	 * Shifts the entire contents of the list over to the left by a given
	 * amount or 1 if no amount is given.
	 * 
	 * @param amount The amount to shift left by
	 * 
	 * @return EnvisionList the current list instance
	 */
	public static class IFunc_shiftLeft<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_shiftLeft(E instIn) {
			super(instIn, VAR, "shiftLeft");
			//allow shiftLeft(int amount)
			addOverload(LIST, INT);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 0) ret(inst.shiftLeft());
			ret(inst.shiftLeft((EnvisionInt) args[0]));
		}
	}
	
	/**
	 * Shifts the entire contents of the list over to the right by a given
	 * amount or 1 if no amount is given.
	 * 
	 * @param amount The amount to shift right by
	 * 
	 * @return EnvisionList the current list instance
	 */
	public static class IFunc_shiftRight<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_shiftRight(E instIn) {
			super(instIn, VAR, "shiftRight");
			//allow shiftLeft(int amount)
			addOverload(LIST, INT);
		}
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 0) ret(inst.shiftRight());
			ret(inst.shiftRight((EnvisionInt) args[0]));
		}
	}
	
	/**
	 * Returns a new EnvisionList with the contents of the original list
	 * positioned in a random ordering.
	 * 
	 * @return EnvisionList a shuffled list
	 */
	public static class IFunc_shuffle<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_shuffle(E instIn) { super(instIn, LIST, "shuffle"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.shuffle());
		}
	}
	
	/**
	 * Returns the total number of elements in this list.
	 * 
	 * @return the size of the list
	 */
	public static class IFunc_size<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_size(E instIn) { super(instIn, INT, "size"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.size());
		}
	}
	
	/**
	 * Swaps the objects at the two given indexes.
	 * Throws an EmptyListError if the list is empty.
	 * 
	 * @param a The index of the first object
	 * @param b The index of the second object
	 * 
	 * @return EnvisionList the list being modified
	 */
	public static class IFunc_swap<E extends EnvisionList> extends InstanceFunction<E> {
		IFunc_swap(E instIn) { super(instIn, LIST, "swap", INT, INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.swap((EnvisionInt) args[0], (EnvisionInt) args[1]));
		}
	}
	
	//-------------------------
	// Static Member Functions
	//-------------------------
	
	
	
}