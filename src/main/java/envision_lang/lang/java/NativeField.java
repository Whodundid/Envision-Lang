package envision_lang.lang.java;

import java.lang.reflect.Field;

import envision_lang.lang.EnvisionObject;
import envision_lang.lang.language_errors.EnvisionLangError;
import envision_lang.lang.natives.EnvisionStaticTypes;
import envision_lang.lang.natives.IDatatype;
import eutil.reflection.EModifier;

/**
 * 
 * 
 * @author Hunter Bragg
 */
public final class NativeField extends EnvisionObject implements INativeEnvision {

    //========
    // Fields
    //========
    
    private final Field wrappedField;
    private final NativeDatatypeMapper mapper;
    private EnvisionJavaObject wrappedObject;
    private EModifier mods;
    private final boolean isJavaPrivate;
    
    private static void stripPrivate(Field obj) {
        try {
            obj.setAccessible(true);
        }
        catch (Exception e) {
            throw new EnvisionLangError("Failed to bind to Java::Field 'modifiers' field!", e);
        }
    }
    
    private static void setPrivate(Field obj) {
        try {
            obj.setAccessible(false);
        }
        catch (Exception e) {
            throw new EnvisionLangError("Failed to bind to Java::Field 'modifiers' field!", e);
        }
    }
    
    //==============
    // Constructors
    //==============
    
    /**
     * There may not be an actual wrapped Java object when creating a field.
     * 
     * @param fieldToWrap
     */
    public NativeField(Field fieldToWrap) {
        this(null, fieldToWrap);
    }
    
    public NativeField(EnvisionJavaObject wrappedObjectIn, Field fieldToWrap) {
        super(IDatatype.fromJavaClass(fieldToWrap.getType()));
        
        wrappedObject = wrappedObjectIn;
        wrappedField = fieldToWrap;
        mapper = new NativeDatatypeMapper(fieldToWrap);
        mods = EModifier.of(fieldToWrap);
        
        // strong unless explicitly stated otherwise in Java (Object)
        if (internalType != EnvisionStaticTypes.VAR_TYPE) modifierHandler.setStrong();
        
        if (mods.isFinal()) modifierHandler.setFinal();
        if (mods.isStatic()) modifierHandler.setStatic();
        if (mods.isPublic()) modifierHandler.setPublic();
        if (mods.isProtected()) modifierHandler.setProtected();
        if (mods.isPrivate()) modifierHandler.setPrivate();
        
        isJavaPrivate = mods.isPrivate();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EnvisionObject copy() {
        return new NativeField(wrappedObject, wrappedField);
    }
    
    @Override
    public String toString() {
        return String.valueOf(wrappedObject);
    }
    
    @Override
    public void bindToWrappedObject(EnvisionJavaObject instance) {
        wrappedObject = instance;
    }
    
    //=========
    // Getters
    //=========
    
    /** Returns the parent EnvisionJavaObject that this native field is a part of. */
    public EnvisionJavaObject getWrapperObject() { return wrappedObject; }
    
    /**
     * 
     * @return
     */
    public EnvisionObject getFieldValue_Envision() {
        try {
            return mapper.mapToEnvision(getFieldValue_Java());            
        }
        catch (Exception e) {
            throw new EnvisionLangError(e);
        }
    }
    
    /**
     * 
     * @return
     */
    public Object getFieldValue_Java() {
        Object toReturn;
        
        synchronized (wrappedField) {
            try {
                if (isJavaPrivate) stripPrivate(wrappedField);
                toReturn = wrappedField.get(wrappedObject.getJavaObjectInstance());
            }
            catch (Exception e) {
                throw new EnvisionLangError(e);
            }
            finally {
                if (isJavaPrivate) setPrivate(wrappedField);
            }
        }
        
        return toReturn;
    }
    
    public Class<?> getJavaFieldType() { return wrappedField.getType(); }
    
    //=========
    // Setters
    //=========
    
    public void setWrappedObject(EnvisionJavaObject wrapperObjectIn) {
        wrappedObject = wrapperObjectIn;
    }
    
    /**
     * Sets the underlying field value of this NativeField using the given
     * EnvisionObject. The EnvisionObject will be translated into its Java
     * equivalent before being set.
     * 
     * @param object The value to set
     */
    public void setFieldValue(EnvisionObject object) {
        if (mods.isFinal()) throw new EnvisionLangError("Error! Cannot set the underlying native field value because it is final!");
        synchronized (wrappedField) {
            try {
                if (isJavaPrivate) stripPrivate(wrappedField);
                var mapped = mapper.mapToJava(object);
                wrappedField.set(wrappedObject.getJavaObjectInstance(), mapped);
            }
            catch (Exception e) {
                throw new EnvisionLangError("Error! Failure to set field value from EnvisionObject!", e);
            }
            finally {
                if (isJavaPrivate) setPrivate(wrappedField);
            }
        }
    }
    
    /**
     * Sets the underlying field value of this NativeField using the given
     * object. If the object is an EnvisionObject, then the EnvisionObject
     * will be translated into a Java equivalent.
     * 
     * @param object The value to set
     */
    public void setFieldValue(Object object) {
        if (mods.isFinal()) throw new EnvisionLangError("Error! Cannot set the underlying native field value because it is final!");
        
        if (object instanceof EnvisionObject o) {
            setFieldValue(o);
            return;
        }
        
        synchronized (wrappedField) {
            try {
                if (isJavaPrivate) stripPrivate(wrappedField);
                wrappedField.set(wrappedObject.getJavaObjectInstance(), object);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new EnvisionLangError("Error! Failure to set field value from Java Object!", e);
            }
            finally {
                if (isJavaPrivate) setPrivate(wrappedField);
            }
        }
    }
    
}
