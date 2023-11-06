package envision_lang.lang.datatypes;

import java.util.Collections;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.functions.FunctionPrototype;
import envision_lang.lang.language_errors.error_types.InvalidArgumentError;
import envision_lang.lang.language_errors.error_types.NoOverloadError;
import envision_lang.lang.language_errors.error_types.listErrors.EmptyTupleError;
import envision_lang.lang.language_errors.error_types.listErrors.IndexOutOfBoundsError;
import envision_lang.lang.language_errors.error_types.objects.UnsupportedOverloadError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.tokenizer.Operator;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

/**
 * A script datatype that represents an immutable grouping of multiple
 * objects. Internally backed by a Java::ArrayList.
 * 
 * @author Hunter Bragg
 */
public final class EnvisionTuple extends ClassInstance {
    
    public static final IDatatype TUPLE_TYPE = EnvisionStaticTypes.TUPLE_TYPE;
    
    public static final EnvisionTuple EMPTY_TUPLE = EnvisionTupleClass.newTuple();
    
    //========
    // Fields
    //========
    
    /**
     * Internal Array list.
     */
    public final EList<EnvisionObject> internalList;
    
    //==============
    // Constructors
    //==============
    
    EnvisionTuple() {
        super(EnvisionTupleClass.TUPLE_CLASS);
        internalList = new EArrayList<>(0);
    }
    
    EnvisionTuple(EList<EnvisionObject> listIn) {
        super(EnvisionTupleClass.TUPLE_CLASS);
        internalList = new EArrayList<>(listIn);
    }
    
    EnvisionTuple(EnvisionTuple in) {
        super(EnvisionTupleClass.TUPLE_CLASS);
        internalList = new EArrayList<>(in.internalList);
    }
    
    EnvisionTuple(EnvisionList listIn) {
        super(EnvisionTupleClass.TUPLE_CLASS);
        internalList = new EArrayList<>(listIn.getInternalList());
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof EnvisionTuple env_tuple && env_tuple.internalList == internalList);
    }
    
    @Override
    public EnvisionTuple copy() {
        if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
        
        //shallow copy
        var l = EnvisionTupleClass.newTuple();
        for (var o : internalList) {
            if (o.isPrimitive()) l.internalList.add(o.copy());
            else l.internalList.add(o);
        }
        return l;
    }
    
    @Override
    public String toString() {
        return "(" + EStringUtil.combineAll(internalList, ", ") + ")";
    }
    
    @Override
    public boolean supportsOperator(Operator op) {
        return switch (op) {
        case EQUALS, NOT_EQUALS -> true;
        case MUL -> true;
        case ADD_ASSIGN -> true;
        default -> false;
        };
    }
    
    @Override
    public EnvisionObject handleOperatorOverloads(EnvisionInterpreter interpreter, String scopeName, Operator op,
        EnvisionObject obj) throws UnsupportedOverloadError {
        //Special case -- EnvisionLists do natively support null additions
        
        //support '+=' operator
        if (op == Operator.ADD_ASSIGN) {
            EnvisionObject toAdd = obj;
            
            //attempt to add the incoming object to this list
            if (obj instanceof EnvisionVariable<?> v) toAdd = v.copy();
            
            EnvisionTuple newTuple = new EnvisionTuple(this);
            newTuple.internalList.add(toAdd);
            
            return newTuple;
        }
        else if (op == Operator.MUL) {
            if (!(obj instanceof EnvisionInt)) throw new InvalidArgumentError("Expected an integer value here!");
            EnvisionInt mulValue = (EnvisionInt) obj;
            long byAmount = mulValue.int_val;
            
            if (byAmount < 1) {
                throw new InvalidArgumentError("Expected an integer greater than or equal to '1' here!");
            }
            
            EArrayList<EnvisionObject> toCopy = new EArrayList<>(internalList.size());
            for (var o : internalList)
                toCopy.add(o);
            
            EnvisionTuple newTuple = new EnvisionTuple(this);
            
            for (int i = 0; i < (byAmount - 1); i++) {
                for (var o : toCopy) {
                    if (o.isPrimitive()) newTuple.internalList.add(o.copy());
                    else newTuple.internalList.add(o);
                }
            }
            
            return newTuple;
        }
        
        return super.handleOperatorOverloads(interpreter, scopeName, op, obj);
    }
    
    @Override
    protected EnvisionObject handlePrimitive(FunctionPrototype proto, EnvisionObject[] args) {
        String funcName = proto.getFunctionName();
        if (!proto.hasOverload(args)) throw new NoOverloadError(funcName, args);
        
        return switch (funcName) {
        case "contains" -> contains(args[0]);
        case "copy" -> copy();
        case "get" -> get((EnvisionInt) args[0]);
        case "getFirst" -> getFirst();
        case "getLast" -> getLast();
        case "hasOne" -> hasOne();
        case "isEmpty" -> isEmpty();
        case "isNotEmpty" -> isNotEmpty();
        case "notContains" -> notContains(args[0]);
        case "random" -> random();
        case "shuffle" -> shuffle();
        case "size" -> size();
        default -> super.handlePrimitive(proto, args);
        };
    }
    
    //=========
    // Methods
    //=========
    
    public EList<EnvisionObject> getInternalList() { return internalList; }
    
    public void add(EnvisionObject o) { internalList.add(o); }
    
    public EnvisionInt size() { return EnvisionIntClass.valueOf(internalList.size()); }
    public long size_i() { return internalList.size(); }
    
    public EnvisionBoolean isEmpty() { return EnvisionBooleanClass.valueOf(internalList.isEmpty()); }
    public boolean isEmpty_i() { return internalList.isEmpty(); }
    
    public EnvisionBoolean hasOne() { return EnvisionBooleanClass.valueOf(internalList.hasOne()); }
    public EnvisionBoolean isNotEmpty() { return EnvisionBooleanClass.valueOf(internalList.isNotEmpty()); }
    
    public EnvisionBoolean contains(EnvisionObject o) {
        return EnvisionBooleanClass.valueOf(internalList.contains(o));
    }
    public EnvisionBoolean notContains(EnvisionObject o) {
        return EnvisionBooleanClass.valueOf(internalList.notContains(o));
    }
    
    public EnvisionObject[] toArray() {
        int size = (int) size_i();
        EnvisionObject[] arr = new EnvisionObject[size];
        for (int i = 0; i < size && i < Integer.MAX_VALUE; i++) {
            arr[i] = internalList.get(i);
        }
        return arr;
    }
    
    //===================
    // Tuple Get Methods
    //===================
    
    public EnvisionObject get(EnvisionInt index) { return get((int) index.int_val); }
    public EnvisionObject get(long index) { return get((int) index); }
    public EnvisionObject get(int index) { return internalList.get(checkEmpty(checkIndex(index))); }
    
    public EnvisionObject getFirst() { checkEmpty(); return internalList.getFirst(); }
    public EnvisionObject getLast() { checkEmpty(); return internalList.getLast(); }
    
    //---------------
    // Tuple Methods
    //---------------
    
    public EnvisionTuple flip() {
        if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
        return new EnvisionTuple(internalList.reverse());
    }
    
    public EnvisionTuple shuffle() {
        if (this == EMPTY_TUPLE) return EMPTY_TUPLE;
        EList<EnvisionObject> l = EList.newList(internalList);
        Collections.shuffle(l);
        return new EnvisionTuple(l);
    }
    
    public EnvisionObject random() {
        if (isEmpty_i()) return EnvisionNull.NULL;
        return internalList.getRandom();
    }
    
    //------------------
    // Internal Methods
    //------------------
    
    private void checkEmpty() { checkEmpty(0); }
    private int checkEmpty(int index) {
        if (internalList.isEmpty()) throw new EmptyTupleError(this);
        return index;
    }
    
    private int checkIndex(int index) {
        if (index < 0 || index >= internalList.size()) throw new IndexOutOfBoundsError(index, this);
        return index;
    }
    
    //---------------------------------------
    
    /**
     * Creates an empty tuple.
     */
    public static EnvisionTuple empty() {
        return EMPTY_TUPLE;
    }
    
    public static EnvisionTuple of(EnvisionObject obj) {
        EnvisionTuple tuple = EnvisionTupleClass.newTuple();
        tuple.internalList.add(obj);
        return tuple;
    }
    
    public static EnvisionTuple of(EnvisionObject a, EnvisionObject b) {
        EnvisionTuple tuple = EnvisionTupleClass.newTuple();
        var list = tuple.internalList;
        list.add(a);
        list.add(b);
        return tuple;
    }
    
    public static EnvisionTuple of(EnvisionObject a, EnvisionObject b, EnvisionObject c) {
        EnvisionTuple tuple = EnvisionTupleClass.newTuple();
        var list = tuple.internalList;
        list.add(a);
        list.add(b);
        list.add(c);
        return tuple;
    }
    
    public static EnvisionTuple of(EnvisionObject... vals) {
        EnvisionTuple tuple = new EnvisionTuple();
        tuple.internalList.addA(vals);
        return tuple;
    }
    
}
