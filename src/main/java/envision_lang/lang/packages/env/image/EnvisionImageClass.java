package envision_lang.lang.packages.env.image;

import static envision_lang.lang.natives.Primitives.*;

import java.io.File;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.exceptions.errors.InvalidArgumentError;
import envision_lang.lang.internal.IPrototypeHandler;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.packages.env.file.EnvisionFile;

public class EnvisionImageClass extends EnvisionClass {

	public static final IDatatype IMAGE_DATATYPE = NativeTypeManager.datatypeOf("Image");
	public static final EnvisionImageClass IMAGE_CLASS = new EnvisionImageClass();
	
	/**
	 * Image member function prototypes.
	 */
	private static final IPrototypeHandler IMAGE_PROTOS = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		IMAGE_PROTOS.define("width", INT).assignDynamicClass(null);
		IMAGE_PROTOS.define("height", INT).assignDynamicClass(null);
		IMAGE_PROTOS.define("getPixel", INT, INT, INT).assignDynamicClass(null);
		IMAGE_PROTOS.define("setPixel", IMAGE_DATATYPE, INT, INT, INT).assignDynamicClass(null);
	}
	
	//--------------
	// Constructors
	//--------------
	
	private EnvisionImageClass() {
		super(IMAGE_DATATYPE.getStringValue());
	}
	
	/**
	 * Creates an EnvisionImage class instance from the given dimensions.
	 */
	public static ClassInstance newImage(int widthIn, int heightIn) {
		EnvisionImage img = new EnvisionImage(widthIn, heightIn);
		IMAGE_CLASS.defineScopeMembers(img);
		return img;
	}
	
	/**
	 * Creates an EnvisionImage class instance from the given file name.
	 */
	public static ClassInstance newImage(String fileName) {
		EnvisionImage img = new EnvisionImage(fileName);
		IMAGE_CLASS.defineScopeMembers(img);
		return img;
	}
	
	/**
	 * Creates an EnvisionImage class instance from the given file.
	 */
	public static ClassInstance newImage(File file) {
		EnvisionImage img = new EnvisionImage(file);
		IMAGE_CLASS.defineScopeMembers(img);
		return img;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionImage img = null;
		
		if (args.length == 0) throw InvalidArgumentError.expectedAtLeastOne();
		if (args.length > 2) throw InvalidArgumentError.expectedAtMost(2);
		
		if (args.length == 1) {
			EnvisionObject one_arg = args[0];
			
			//don't accept null arguments
			if (one_arg == null) throw InvalidArgumentError.nullArgument();
			
			//attempt creation from arg
			if (one_arg instanceof EnvisionString env_str) img = new EnvisionImage(env_str.toString());
			if (one_arg instanceof EnvisionFile env_file) img = new EnvisionImage(env_file.iFile);
		}
		else if (args.length == 2) {
			EnvisionObject arg1 = args[0];
			EnvisionObject arg2 = args[1];
			
			//don't accept null arguments
			if (arg1 == null || arg2 == null) throw InvalidArgumentError.nullArgument();
			
			//attempt creation from args
			if (arg1 instanceof EnvisionInt n1 && arg2 instanceof EnvisionInt n2) {
				img = new EnvisionImage(n1.get_i(), n2.get_i());
			}
		}
		
		//if null, creation failed!
		if (img == null) throw InvalidArgumentError.conversionError(getDatatype());
		
		//define scope members
		defineScopeMembers(img);
		
		return img;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to EnvisionImage
		//EnvisionImage img = 
	}
	
}
