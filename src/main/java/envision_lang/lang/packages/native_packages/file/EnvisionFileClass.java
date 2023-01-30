package envision_lang.lang.packages.native_packages.file;

import static envision_lang.lang.natives.Primitives.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionNull;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.exceptions.EnvisionLangError;
import envision_lang.lang.exceptions.errors.InvalidArgumentError;
import envision_lang.lang.functions.IPrototypeHandler;
import envision_lang.lang.functions.InstanceFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import eutil.EUtil;
import eutil.file.EFileUtil;

public class EnvisionFileClass extends EnvisionClass {

	/**
	 * Constant file datatype reference. Use throughout.
	 * Do not redefine.
	 */
	public static final IDatatype FILE_DATATYPE = NativeTypeManager.datatypeOf("File");
	
	public static final EnvisionFileClass FILE_CLASS = new EnvisionFileClass();
	
	/**
	 * File function prototypes.
	 */
	private static final IPrototypeHandler prototypes = new IPrototypeHandler();
	
	//statically define function prototypes
	static {
		prototypes.define("toString", STRING).assignDynamicClass(IFunc_toString.class);
		prototypes.define("create", BOOLEAN).assignDynamicClass(IFunc_create.class);
		prototypes.define("getName", STRING).assignDynamicClass(IFunc_getName.class);
		prototypes.define("getParent", STRING).assignDynamicClass(IFunc_getParent.class);
		prototypes.define("getParentFile", EnvisionFileClass.FILE_DATATYPE).assignDynamicClass(IFunc_getParentFile.class);
		prototypes.define("getPath", STRING).assignDynamicClass(IFunc_getPath.class);
		prototypes.define("canRead", BOOLEAN).assignDynamicClass(IFunc_canRead.class);
		prototypes.define("canWrite", BOOLEAN).assignDynamicClass(IFunc_canWrite.class);
		prototypes.define("exists", BOOLEAN).assignDynamicClass(IFunc_exists.class);
		prototypes.define("isDirectory", BOOLEAN).assignDynamicClass(IFunc_isDirectory.class);
		prototypes.define("isFile", BOOLEAN).assignDynamicClass(IFunc_isFile.class);
		prototypes.define("delete", BOOLEAN).assignDynamicClass(IFunc_delete.class);
		prototypes.define("lsn", TUPLE).assignDynamicClass(IFunc_lsn.class);
		prototypes.define("ls", TUPLE).assignDynamicClass(IFunc_ls.class);
		prototypes.define("mkdir", BOOLEAN).assignDynamicClass(IFunc_mkdir.class);
		prototypes.define("mkdirs", BOOLEAN).assignDynamicClass(IFunc_mkdirs.class);
		prototypes.define("rename", BOOLEAN).assignDynamicClass(IFunc_rename.class);
		prototypes.define("clear", BOOLEAN).assignDynamicClass(IFunc_clear.class);
		prototypes.define("lines", TUPLE).assignDynamicClass(IFunc_lines.class);
		prototypes.define("write", BOOLEAN, VAR).assignDynamicClass(IFunc_write.class);
		prototypes.define("writeln", BOOLEAN, VAR).assignDynamicClass(IFunc_writeln.class);
		prototypes.define("writeLines", BOOLEAN, TUPLE).assignDynamicClass(IFunc_writeLines.class);
		prototypes.define("flush", BOOLEAN).assignDynamicClass(IFunc_flush.class);
		prototypes.define("randomLine", STRING).assignDynamicClass(IFunc_randomLine.class);
	}
	
	//--------------
	// Constructors
	//--------------
	
	private EnvisionFileClass() {
		super(FILE_DATATYPE.getStringValue());
	}

	/**
	 * Creates an EnvisionFile class instance from the given file name.
	 */
	public static EnvisionFile newFile(String fileName) {
		EnvisionFile file = new EnvisionFile(fileName);
		FILE_CLASS.defineScopeMembers(file);
		return file;
	}
	
	public static EnvisionFile newFile(File fileIn) {
		EnvisionFile file = new EnvisionFile(fileIn);
		FILE_CLASS.defineScopeMembers(file);
		return file;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	/**
	 * Goes through the process of physically creating the Envision class
	 * structure back-end as well as producing a wrapped Java File object
	 * which can be internally referenced.
	 */
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionFile file = null;
		
		if (args.length == 0) throw InvalidArgumentError.expectedAtLeast(1);
		if (args.length > 2) throw InvalidArgumentError.expectedAtMost(2);
		
		EnvisionObject first = args[0];
		
		//don't accept null arguments
		if (first == null) throw InvalidArgumentError.nullArgument();
		
		if (args.length == 2) {
			EnvisionObject second = args[1];
			
			//don't accept null arguments
			if (second == null) throw InvalidArgumentError.nullArgument();
			
			//if 2 args, 1st must be a file object or a String
			//and second must be a string object
			if (!(second instanceof EnvisionString)) throw InvalidArgumentError.conversionError(second, STRING);
			
			String child = ((EnvisionString) second).get_i();
			
			if (first instanceof EnvisionFile f) file = new EnvisionFile(f.iFile, child);
			else if (first instanceof EnvisionString s) file = new EnvisionFile(s.get_i(), child);
			else throw InvalidArgumentError.conversionError(second, FILE_DATATYPE);
		}
		else {
			//attempt creation from single arg
			if (first instanceof EnvisionString env_str) file = new EnvisionFile(env_str.toString());
			else if (first instanceof EnvisionFile env_file) file = new EnvisionFile(env_file.iFile.getAbsolutePath());
		}
		
		//if null, creation failed!
		if (file == null) throw InvalidArgumentError.conversionError(first, getDatatype());
		
		//define scope members
		defineScopeMembers(file);
				
		//return built list instance
		return file;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		//define file members
		prototypes.defineOn(inst);
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	/**
	 * Creates the necessary directories required in order to make the
	 * requested file/directory creation possible.
	 */
	private static class IFunc_toString<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_toString() { super(STRING, "toString"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.valueOf(String.valueOf(inst.iFile).replace("\\", "\\\\")));
		}
	}
	
	/**
	 * Attempts to create a file under the same path/name as this file
	 * object.
	 */
	private class IFunc_create<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_create() { super(BOOLEAN, "create"); }
		public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			boolean r = false;
			
			try {
				r = inst.iFile.createNewFile();
			}
			catch (Exception e) {
				throw new EnvisionLangError(e);
			}
			
			ret(EnvisionBooleanClass.valueOf(r));
		}
	}
	
	/**
	 * Returns the immediate name of this file.
	 */
	private static class IFunc_getName<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getName() { super(STRING, "getName"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.valueOf(inst.iFile.getName()));
		}
	}
	
	/**
	 * Returns the name of the parent directory.
	 */
	private static class IFunc_getParent<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getParent() { super(STRING, "getParent"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.valueOf(inst.iFile.getParentFile()));
		}
	}
	
	/**
	 * Returns the parent directory as a file object.
	 */
	private static class IFunc_getParentFile<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getParentFile() { super(EnvisionFileClass.FILE_DATATYPE, "getParentFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionFileClass.newFile(inst.iFile.getParentFile()));
		}
	}
	
	/**
	 * Returns the string system path to this file on this computer.
	 */
	private static class IFunc_getPath<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getPath() { super(STRING, "getPath"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.valueOf(inst.iFile.getPath().replace("\\", "\\\\")));
		}
	}
	
	/**
	 * Returns true if this file can be read from.
	 */
	private static class IFunc_canRead<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_canRead() { super(BOOLEAN, "canRead"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.canRead()));
		}
	}
	
	/**
	 * Returns true if this file can be written to.
	 */
	private static class IFunc_canWrite<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_canWrite() { super(BOOLEAN, "canWrite"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.canWrite()));
		}
	}
	
	/**
	 * Returns true if this path actually exists on the given computer's
	 * file system.
	 */
	private static class IFunc_exists<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_exists() { super(BOOLEAN, "exists"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.exists()));
		}
	}
	
	/**
	 * Returns true if this path represents a directory (folder) instead
	 * of a singular file object.
	 */
	private static class IFunc_isDirectory<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_isDirectory() { super(BOOLEAN, "isDirectory"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.isDirectory()));
		}
	}
	
	/**
	 * Returns true if this path represents a file object vs. a directory
	 * object.
	 */
	private static class IFunc_isFile<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_isFile() { super(BOOLEAN, "isFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.isFile()));
		}
	}
	
	/**
	 * Attempts to delete this file.
	 */
	private static class IFunc_delete<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_delete() { super(BOOLEAN, "delete"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.delete()));
		}
	}
	
	/**
	 * Returns a list of file names visible within this directory.
	 */
	private static class IFunc_lsn<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_lsn() { super(LIST, "lsn"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList names = EnvisionListClass.newList(STRING);
			for (String n : inst.gfe().list()) names.add(EnvisionStringClass.valueOf(n));
			ret(names);
		}
	}
	
	/**
	 * Returns a list of file objects representing this directory's
	 * visible files.
	 */
	private static class IFunc_ls<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_ls() { super(LIST, "ls"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			var files = EUtil.mapList(inst.iFile.listFiles(), f -> newFile(f));
			ret(EnvisionListClass.newList(FILE_DATATYPE, files));
		}
	}
	
	/**
	 * If this file represents a directory path, this will create the
	 * directory if it does not already exist.
	 */
	private static class IFunc_mkdir<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_mkdir() { super(BOOLEAN, "mkdir"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.mkdir()));
		}
	}
	
	/**
	 * Creates the necessary directories required in order to make the
	 * requested file/directory creation possible.
	 */
	private static class IFunc_mkdirs<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_mkdirs() { super(BOOLEAN, "mkdirs"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.valueOf(inst.iFile.mkdirs()));
		}
	}
	
	/**
	 * Renames this file to a new name. Operates based on file paths.
	 */
	private static class IFunc_rename<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_rename() { super(BOOLEAN, "rename", STRING); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 0) ret(EnvisionBoolean.FALSE);
			String name = ((EnvisionString) args[0]).toString();
			boolean val = inst.iFile.renameTo(new File(name));
			ret(EnvisionBooleanClass.valueOf(val));
		}
	}
	
	/**
	 * Sets whether this file will append or overwrite data.
	 */
	/*
	private static class Append<E extends EnvFile> extends InstanceFunction<E> {
		Append() { super(VOID, "setAppend", new ParameterData(BOOLEAN)); }
		@Override public void invoke(EnvisionInterpreter interpreter, Object... args) {
			if (args.isNotEmpty()) {
				appendMode = (boolean) convert(args.getFirst());
			}
		}
	}
	*/
	
	/**
	 * Returns true if this file is in append mode.
	 */
	/*
	private static class IsAppend<E extends EnvFile> extends InstanceFunction<E> {
		IsAppend() { super(BOOLEAN, "isAppend"); }
		@Override public void invoke(EnvisionInterpreter interpreter, Object... args) {
			ret(appendMode);
		}
	}
	*/
	
	/**
	 * Attempts to remove the contents of this file, returns true if
	 * successful.
	 */
	private static class IFunc_clear<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_clear() { super(BOOLEAN, "clear"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent reads if this is not a file
			if (!f.isFile()) ret(EnvisionBoolean.FALSE);
			
			try (PrintWriter writer  = new PrintWriter(f)) {
				writer.print("");
				ret(EnvisionBoolean.TRUE);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	/**
	 * Returns all of the lines within the file as a list.
	 */
	private static class IFunc_lines<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_lines() { super(LIST, "lines"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList lines = EnvisionListClass.newList(STRING);
			
			try {
				File f = inst.gfe();
				Files.lines(f.toPath()).forEach(EnvisionStringClass::valueOf);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			ret(lines);
		}
	}
	
	/**
	 * Writes the given object to the file, does not add a new line.
	 */
	private static class IFunc_write<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_write() { super(BOOLEAN, "write"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(EnvisionBoolean.FALSE);
			
			if (args.length == 1) {
				String s = convert(args[0]).toString();
				
				inst.toBeWritten.add(s);
				ret(EnvisionBoolean.TRUE);
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	/**
	 * Writes the given object to the file, adds a new line.
	 */
	private static class IFunc_writeln<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_writeln() { super(BOOLEAN, "writeln"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(EnvisionBoolean.FALSE);
			
			if (args.length == 1) {
				String s = convert(args[0]).toString();
				s += "\n";
				
				inst.toBeWritten.add(s);
				ret(EnvisionBoolean.TRUE);
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	/**
	 * Takes in a list and writes each value to the file line by line.
	 */
	private static class IFunc_writeLines<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_writeLines() { super(BOOLEAN, "writeLines"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(EnvisionBoolean.FALSE);
			
			if (args.length > 0 && args[0] instanceof EnvisionList) {
				EnvisionList list = (EnvisionList) args[0];
				
				for (Object o : list.getInternalList()) {
					String s = o.toString();
					s += "\n";
					inst.toBeWritten.add(s);
				}
				
				ret(EnvisionBoolean.TRUE);
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	private static class IFunc_flush<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_flush() { super(BOOLEAN, "flush", STRING); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent writes if this is not a file
			if (!f.isFile() || inst.toBeWritten.isEmpty()) ret(EnvisionBoolean.FALSE);
			
			StringBuilder str = new StringBuilder();
			for (String s : inst.toBeWritten) {
				str.append(s);
			}
			
			try {
				Files.write(f.toPath(), str.toString().getBytes(), StandardOpenOption.APPEND);
				inst.toBeWritten.clear();
				ret(EnvisionBoolean.TRUE);
			}
			catch (IOException e) {
				throw new EnvisionLangError(e);
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
	/**
	 * Returns a random line from the current file.
	 * <p>
	 * If the file is null, an error is thrown.
	 * <p>
	 * If the file is empty, Envision:NULL is returned.
	 * <p>
	 * If the file only has one line, that one line is always returned.
	 */
	private static class IFunc_randomLine<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_randomLine() { super(STRING, "randomLine"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			String randLine = EFileUtil.randomLine(inst.gfe());
			if (randLine != null) ret(EnvisionStringClass.valueOf(randLine));
			ret(EnvisionNull.NULL);
		}
	}
	
}