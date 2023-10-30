package envision_lang.lang.datatypes;

import static envision_lang.lang.natives.Primitives.*;

import java.util.Map;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.creation_util.ObjectCreator;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.Primitives;

public class EnvisionMapClass extends EnvisionClass {
    
    /**
     * The singular, static Map class for which all Envision:Map
     * objects are derived from.
     */
    public static final EnvisionMapClass MAP_CLASS = new EnvisionMapClass();
    
    /**
     * The set of functions that a map is capable of performing.
     */
    private static final IPrototypeHandler MAP_PROTOTYPES = new IPrototypeHandler();
    
    static {
        MAP_PROTOTYPES.define("size", INT).assignDynamicClass(IFunc_size.class);
        MAP_PROTOTYPES.define("isEmpty", BOOLEAN).assignDynamicClass(IFunc_isEmpty.class);
        MAP_PROTOTYPES.define("containsKey", BOOLEAN, VAR).assignDynamicClass(IFunc_containsKey.class);
        MAP_PROTOTYPES.define("containsValue", BOOLEAN, VAR).assignDynamicClass(IFunc_containsValue.class);
        MAP_PROTOTYPES.define("get", VAR, VAR).assignDynamicClass(IFunc_get.class);
        MAP_PROTOTYPES.define("put", VAR, VAR, VAR).assignDynamicClass(IFunc_put.class);
        MAP_PROTOTYPES.define("remove", VAR, VAR).assignDynamicClass(IFunc_remove.class);
        MAP_PROTOTYPES.define("putAll", VOID).assignDynamicClass(IFunc_putAll.class);
        MAP_PROTOTYPES.define("clear", VOID).assignDynamicClass(IFunc_clear.class);
        MAP_PROTOTYPES.define("keys", LIST).assignDynamicClass(IFunc_keys.class);
        MAP_PROTOTYPES.define("values", LIST).assignDynamicClass(IFunc_values.class);
        MAP_PROTOTYPES.define("entries", LIST).assignDynamicClass(IFunc_entries.class);
        MAP_PROTOTYPES.define("putIfAbsent", VAR, VAR, VAR).assignDynamicClass(IFunc_putIfAbsent.class);
    }
    
    //==============
    // Constructors
    //==============
    
    private EnvisionMapClass() {
        super(Primitives.MAP);
        
        // set final to prevent user-extension
        setFinal();
    }
    
    //=====================
    // Static Constructors
    //=====================
    
    public static EnvisionMap newMap() {
        return newMap(EnvisionStaticTypes.VAR_TYPE, EnvisionStaticTypes.VAR_TYPE);
    }
    
    public static EnvisionMap newMap(IDatatype keyType, IDatatype valueType) {
        EnvisionMap map = new EnvisionMap(keyType, valueType);
        MAP_CLASS.defineScopeMembers(map);
        return map;
    }
    
    public static EnvisionMap newMap(Object dataIn) {
        if (dataIn == null) throw new NullPointerException("Error! Cannot create EnvisionMap from Java::NULL!");
        
        // Java Map path
        if (dataIn instanceof Map<?, ?> m) return newMapFromJavaMap(m);
        
        throw new EnvisionLangError("Unexpected map object target type!");
    }
    
    public static EnvisionMap newMapFromJavaMap(Map<?, ?> mapIn) {
        EnvisionMap map = newMap();
        
        var it = mapIn.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            var key = entry.getKey();
            var value = entry.getValue();
            
            IDatatype keyType = IDatatype.dynamicallyDetermineType(key);
            IDatatype valueType = IDatatype.dynamicallyDetermineType(value);
            
            var eKey = ObjectCreator.createObject(keyType, key);
            var eValue = ObjectCreator.createObject(valueType, value);
            
            map.put_i(eKey, eValue);
        }
        
        MAP_CLASS.defineScopeMembers(map);
        
        return map;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EnvisionMap newInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        // bypass class construct for primitive type
        return buildInstance(interpreter, args);
    }
    
    @Override
    protected EnvisionMap buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
        EnvisionMap map = new EnvisionMap();
        
        // there should probably be an error if there are any args here
        
        // define scope members
        defineScopeMembers(map);
        
        // return built list instance
        return map;
    }
    
    @Override
    protected void defineScopeMembers(ClassInstance inst) {
        // define super object's members
        super.defineScopeMembers(inst);
        // define scope members
        MAP_PROTOTYPES.defineOn(inst);
    }
    
    //===========================
    // Instance Member Functions
    //===========================
    
    public static class IFunc_size extends InstanceFunction<EnvisionMap> {
        public IFunc_size() { super(INT, "size"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.size());
        }
    }
    
    public static class IFunc_isEmpty extends InstanceFunction<EnvisionMap> {
        public IFunc_isEmpty() { super(BOOLEAN, "isEmpty"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.isEmpty());
        }
    }
    
    public static class IFunc_containsKey extends InstanceFunction<EnvisionMap> {
        public IFunc_containsKey() { super(BOOLEAN, "containsKey", VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.containsKey(args[0]));
        }
    }
    
    public static class IFunc_containsValue extends InstanceFunction<EnvisionMap> {
        public IFunc_containsValue() { super(BOOLEAN, "containsValue", VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.containsValue(args[0]));
        }
    }
    
    public static class IFunc_get extends InstanceFunction<EnvisionMap> {
        public IFunc_get() { super(VAR, "get", VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.get(args[0]));
        }
    }
    
    public static class IFunc_put extends InstanceFunction<EnvisionMap> {
        public IFunc_put() { super(VAR, "put", VAR, VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.put(args[0], args[1]));
        }
    }
    
    public static class IFunc_remove extends InstanceFunction<EnvisionMap> {
        public IFunc_remove() { super(VAR, "remove", VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.remove(args[0]));
        }
    }
    
    public static class IFunc_putAll extends InstanceFunction<EnvisionMap> {
        public IFunc_putAll() { super(VOID, "putAll", MAP); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            inst.putAll((EnvisionMap) args[0]);
        }
    }
    
    public static class IFunc_clear extends InstanceFunction<EnvisionMap> {
        public IFunc_clear() { super(BOOLEAN, "clear"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            inst.clear();
        }
    }
    
    public static class IFunc_keys extends InstanceFunction<EnvisionMap> {
        public IFunc_keys() { super(LIST, "keys"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.keys());
        }
    }
    
    public static class IFunc_values extends InstanceFunction<EnvisionMap> {
        public IFunc_values() { super(LIST, "values"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.values());
        }
    }
    
    public static class IFunc_entries extends InstanceFunction<EnvisionMap> {
        public IFunc_entries() { super(LIST, "entries"); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.entries());
        }
    }
    
    public static class IFunc_putIfAbsent extends InstanceFunction<EnvisionMap> {
        public IFunc_putIfAbsent() { super(VAR, "putIfAbsent", VAR, VAR); }
        @Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
            ret(inst.putIfAbsent(args[0], args[1]));
        }
    }
    
}
