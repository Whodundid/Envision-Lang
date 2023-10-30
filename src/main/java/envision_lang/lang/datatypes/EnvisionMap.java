package envision_lang.lang.datatypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import eutil.datatypes.util.EList;

public class EnvisionMap extends ClassInstance {
    
    public static final IDatatype MAP_TYPE = EnvisionStaticTypes.MAP_TYPE;
    
    //========
    // Fields
    //========
    
    /**
     * Internal Java HashMap.
     */
    public final Map<EnvisionObject, EnvisionObject> internalMap;
    
    /**
     * The parameterized key type.
     */
    private final IDatatype mapKeyType;
    
    /**
     * The parameterized value type.
     */
    private final IDatatype mapValueType;
    
    //==============
    // Constructors
    //==============
    
    EnvisionMap() { this(EnvisionStaticTypes.VAR_TYPE, EnvisionStaticTypes.VAR_TYPE); }
    EnvisionMap(IDatatype keyType, IDatatype valueType) {
        super(EnvisionMapClass.MAP_CLASS);
        mapKeyType = keyType;
        mapValueType = valueType;
        internalMap = new HashMap<>();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof EnvisionMap m && m.internalMap == obj);
    }
    
    @Override
    public EnvisionMap copy() {
        // shallow copy
        var m = EnvisionMapClass.newMap(mapKeyType, mapValueType);
        
        for (var e : internalMap.entrySet()) {
            var key = e.getKey();
            var value = e.getValue();
            
            EnvisionObject keyToPut = key;
            EnvisionObject valueToPut = value;
            
            if (key.isPrimitive()) keyToPut = key.copy();
            if (value.isPrimitive()) valueToPut = value.copy();
            
            m.internalMap.put(keyToPut, valueToPut);
        }
        
        return m;
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionInt size() { return EnvisionIntClass.valueOf(size_i()); }
    public long size_i() { return internalMap.size(); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionBoolean isEmpty() { return EnvisionBoolean.of(isEmpty_i()); }
    public boolean isEmpty_i() { return internalMap.isEmpty(); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionBoolean containsKey(EnvisionObject key) { return EnvisionBoolean.of(containsKey_i(key)); }
    public boolean containsKey_i(EnvisionObject key) { return internalMap.containsKey(key); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionBoolean containsValue(EnvisionObject value) { return EnvisionBoolean.of(containsValue_i(value)); }
    public boolean containsValue_i(EnvisionObject value) { return internalMap.containsValue(value); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionObject get(EnvisionObject key) {
        var v = get_i(key);
        return (v != null) ? v : EnvisionNull.NULL;
    }
    public EnvisionObject get_i(EnvisionObject key) {
        return internalMap.get(key);
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionObject put(EnvisionObject key, EnvisionObject value) {
        var v = put_i(key, value);
        return (v != null) ? v : EnvisionNull.NULL;
    }
    public EnvisionObject put_i(EnvisionObject key, EnvisionObject value) {
        return internalMap.put(key, value);
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionObject remove(EnvisionObject key) {
        var v = remove_i(key);
        return (v != null) ? v : EnvisionNull.NULL;
    }
    public EnvisionObject remove_i(EnvisionObject key) {
        return internalMap.remove(key);
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    public void putAll(EnvisionMap map) { internalMap.putAll(map.internalMap); }
    public void clear() { internalMap.clear(); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionList keys() { return EnvisionListClass.newList(keys_i()); }
    public Collection<EnvisionObject> keys_i() { return internalMap.keySet(); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionList values() { return EnvisionListClass.newList(values_i()); }
    public Collection<EnvisionObject> values_i() { return internalMap.values(); }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionList entries() {
        var entries = entries_i();
        EnvisionList list = new EnvisionList(entries.size());
        for (var e : entries) {
            var key = e.getKey();
            var value = e.getValue();
            
            // tupleize the entries
            var tuple = EnvisionTupleClass.newTuple();
            tuple.add(key);
            tuple.add(value);
            
            list.add(tuple);
        }
        return list;
    }
    
    public EList<Map.Entry<EnvisionObject, EnvisionObject>> entries_i() {
        return EList.of(internalMap.entrySet());
    }
    
    //------------------------------------------------------------------------------------------------------------
    
    public EnvisionObject putIfAbsent(EnvisionObject key, EnvisionObject value) {
        var v = get_i(key);
        if (v == null) {
            v = put_i(key, value);
        }
        return v;
    }
    
}
