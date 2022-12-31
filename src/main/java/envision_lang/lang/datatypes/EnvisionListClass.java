package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import java.util.List;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.internal.IPrototypeHandler;
import envision_lang.lang.internal.InstanceFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;
import envision_lang.lang.natives.StaticTypes;

public class EnvisionListClass extends EnvisionClass {

	/**
	 * The singular, static List class for which all Envision:List
	 * objects are derived from.
	 */
	public static final EnvisionListClass LIST_CLASS = new EnvisionListClass();
	
	private static final IPrototypeHandler LIST_PROTOTYPES = new IPrototypeHandler();
	
	static {
		LIST_PROTOTYPES.define("add", BOOLEAN, VAR).assignDynamicClass(IFunc_add.class);
		LIST_PROTOTYPES.define("addR", VAR, VAR).assignDynamicClass(IFunc_addR.class);
		LIST_PROTOTYPES.define("addRT", LIST, VAR).assignDynamicClass(IFunc_addRT.class);
		LIST_PROTOTYPES.define("clear", LIST).assignDynamicClass(IFunc_clear.class);
		LIST_PROTOTYPES.define("contains", BOOLEAN, VAR).assignDynamicClass(IFunc_contains.class);
		LIST_PROTOTYPES.define("copy", LIST).assignDynamicClass(IFunc_copy.class);
		LIST_PROTOTYPES.define("fill", LIST, VAR_A).assignDynamicClass(IFunc_fill.class);
		LIST_PROTOTYPES.define("reverse", LIST).assignDynamicClass(IFunc_reverse.class);
		LIST_PROTOTYPES.define("get", VAR, INT).assignDynamicClass(IFunc_get.class);
		LIST_PROTOTYPES.define("getFirst", VAR).assignDynamicClass(IFunc_getFirst.class);
		LIST_PROTOTYPES.define("getLast", VAR).assignDynamicClass(IFunc_getLast.class);
		LIST_PROTOTYPES.define("getListType", STRING).assignDynamicClass(IFunc_getListType.class);
		LIST_PROTOTYPES.define("hasOne", BOOLEAN).assignDynamicClass(IFunc_hasOne.class);
		LIST_PROTOTYPES.define("isEmpty", BOOLEAN).assignDynamicClass(IFunc_isEmpty.class);
		LIST_PROTOTYPES.define("isNotEmpty", BOOLEAN).assignDynamicClass(IFunc_isNotEmpty.class);
		LIST_PROTOTYPES.define("isSizeLocked", BOOLEAN).assignDynamicClass(IFunc_isSizeLocked.class);
		LIST_PROTOTYPES.define("lockSize", LIST).assignDynamicClass(IFunc_lockSize.class);
		LIST_PROTOTYPES.define("notContains", BOOLEAN, VAR).assignDynamicClass(IFunc_notContains.class);
		LIST_PROTOTYPES.define("push", LIST, VAR).assignDynamicClass(IFunc_push.class);
		LIST_PROTOTYPES.define("pop", VAR).assignDynamicClass(IFunc_pop.class);
		LIST_PROTOTYPES.define("remove", VAR, INT).assignDynamicClass(IFunc_remove.class);
		LIST_PROTOTYPES.define("removeFirst", VAR).assignDynamicClass(IFunc_removeFirst.class);
		LIST_PROTOTYPES.define("removeLast", VAR).assignDynamicClass(IFunc_removeLast.class);
		LIST_PROTOTYPES.define("set", LIST, INT, VAR).assignDynamicClass(IFunc_set.class);
		LIST_PROTOTYPES.define("setFirst", LIST, VAR).assignDynamicClass(IFunc_setFirst.class);
		LIST_PROTOTYPES.define("setLast", LIST, VAR).assignDynamicClass(IFunc_setLast.class);
		LIST_PROTOTYPES.define("setSize", LIST, INT);
		LIST_PROTOTYPES.define("shiftLeft", LIST).addOverload(LIST, INT).assignDynamicClass(IFunc_shiftLeft.class);
		LIST_PROTOTYPES.define("shiftRight", LIST).addOverload(LIST, INT).assignDynamicClass(IFunc_shiftRight.class);
		LIST_PROTOTYPES.define("shuffle", LIST).assignDynamicClass(IFunc_shuffle.class);
		LIST_PROTOTYPES.define("size", INT).assignDynamicClass(IFunc_size.class);
		LIST_PROTOTYPES.define("swap", LIST, INT, INT).assignDynamicClass(IFunc_swap.class);
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
	
	public static EnvisionList newList() { return newList(StaticTypes.VAR_TYPE); }
	public static EnvisionList newList(IDatatype type) {
		EnvisionList list = new EnvisionList(type);
		LIST_CLASS.defineScopeMembers(list);
		return list;
	}
	
	public static EnvisionList newList(IDatatype type, List<? extends EnvisionObject> data) {
		EnvisionList list = newList(type);
		list.getInternalList().ensureCapacity(data.size());
		for (var o : data) list.add(o);
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
		//define scope members
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
		public IFunc_add() { super(BOOLEAN, "add", VAR); }
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
		public IFunc_addR() { super(VAR, "addR", VAR); }
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
		public IFunc_addRT() { super(LIST, "addRT", VAR); }
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
		public IFunc_clear() { super(LIST, "clear"); }
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
	public static class IFunc_notContains<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_notContains() { super(BOOLEAN, "notContains", VAR); }
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
		public IFunc_copy() { super(LIST, "copy"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(new EnvisionList(inst));
		}
	}
	
	public static class IFunc_fill<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_fill() { super(LIST, "fill", VAR_A); }
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
	public static class IFunc_reverse<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_reverse() { super(LIST, "reverse"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.reverse());
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
	public static class IFunc_getFirst<E extends EnvisionList> extends InstanceFunction<E> {
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
	public static class IFunc_getLast<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_getLast() { super(VAR, "getLast"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getLast());
		}
	}
	
	public static class IFunc_getListType<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_getListType() { super(STRING, "getListType"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.getListTypeString());
		}
	}
	
	/**
	 * Returns true if the given list instance contains exactly one element.
	 * 
	 * @return EnvisionBoolean true if the list has one element
	 */
	public static class IFunc_hasOne<E extends EnvisionList> extends InstanceFunction<E> {
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
	public static class IFunc_isEmpty<E extends EnvisionList> extends InstanceFunction<E> {
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
	public static class IFunc_isNotEmpty<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_isNotEmpty() { super(BOOLEAN, "isNotEmpty"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isNotEmpty());
		}
	}
	
	public static class IFunc_isSizeLocked<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_isSizeLocked() { super(BOOLEAN, "isSizeLocked"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.isSizeLocked());
		}
	}
	
	public static class IFunc_lockSize<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_lockSize() { super(LIST, "lockSize", BOOLEAN); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setSizeLocked((EnvisionBoolean) args[0]));
		}
	}
	
	public static class IFunc_push<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_push() { super(LIST, "push", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.push(args[0]));
		}
	}
	
	public static class IFunc_pop<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_pop() { super(VAR, "pop"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.pop());
		}
	}
	
	public static class IFunc_remove<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_remove() { super(VAR, "remove", INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.remove((EnvisionInt) args[0]));
		}
	}
	
	public static class IFunc_removeFirst<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_removeFirst() { super(VAR, "removeFirst"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.removeFirst());
		}
	}
	
	public static class IFunc_removeLast<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_removeLast() { super(VAR, "removeLast"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.removeLast());
		}
	}
	
	public static class IFunc_set<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_set() { super(VAR, "set", INT, VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.set((EnvisionInt) args[0], args[1]));
		}
	}
	
	public static class IFunc_setFirst<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_setFirst() { super(VAR, "setFirst", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setFirst(args[0]));
		}
	}
	
	public static class IFunc_setLast<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_setLast() { super(VAR, "setLast", VAR); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.setLast(args[0]));
		}
	}
	
	public static class IFunc_setSize<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_setSize() {
			super(LIST, "setSize", INT);
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
		public IFunc_shiftLeft() {
			super(VAR, "shiftLeft");
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
		public IFunc_shiftRight() {
			super(VAR, "shiftRight");
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
	public static class IFunc_size<E extends EnvisionList> extends InstanceFunction<E> {
		public IFunc_size() { super(INT, "size"); }
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
		public IFunc_swap() { super(LIST, "swap", INT, INT); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(inst.swap((EnvisionInt) args[0], (EnvisionInt) args[1]));
		}
	}
	
}