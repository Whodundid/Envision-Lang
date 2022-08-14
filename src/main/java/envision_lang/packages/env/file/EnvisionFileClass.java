package envision_lang.packages.env.file;

import static envision_lang.lang.natives.Primitives.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import envision_lang.exceptions.EnvisionLangError;
import envision_lang.exceptions.errors.InvalidArgumentError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.Scope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionBooleanClass;
import envision_lang.lang.datatypes.EnvisionList;
import envision_lang.lang.datatypes.EnvisionListClass;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionStringClass;
import envision_lang.lang.internal.EnvisionNull;
import envision_lang.lang.internal.InstanceFunction;
import envision_lang.lang.natives.IDatatype;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.lang.util.ParameterData;

public class EnvisionFileClass extends EnvisionClass {

	/**
	 * Constant file datatype reference. Use throughout.
	 * Do not redefine.
	 */
	public static final IDatatype FILE_DATATYPE = NativeTypeManager.datatypeOf("File");
	
	public static final EnvisionFileClass FILE_CLASS = new EnvisionFileClass();
	
	//--------------
	// Constructors
	//--------------
	
	private EnvisionFileClass() {
		super(FILE_DATATYPE.getType());
	}

	/**
	 * Creates an EnvisionFile class instance from the given file name.
	 */
	public static ClassInstance newFile(String fileName) {
		EnvisionFile file = new EnvisionFile(fileName);
		FILE_CLASS.defineScopeMembers(file);
		return file;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	/**
	 * Goes through the process of physically creating the Envision class
	 * structure backend as well as producing a wrapped Java File object
	 * which can be internally referenced.
	 */
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, EnvisionObject[] args) {
		EnvisionFile file = null;
		
		if (args.length != 1) throw InvalidArgumentError.expectedExactlyOne();
		
		EnvisionObject arg_val = args[0];
		
		//don't accept null arguments
		if (arg_val == null) throw InvalidArgumentError.nullArgument();
		
		//attempt creation from arg
		if (arg_val instanceof EnvisionString env_str) file = new EnvisionFile(env_str.toString());
		else if (arg_val instanceof EnvisionFile env_file) file = new EnvisionFile(env_file.iFile.getAbsolutePath());
		
		//if null, creation failed!
		if (file == null) throw InvalidArgumentError.conversionError(arg_val, getDatatype());
		
		//define scope members
		defineScopeMembers(file);
				
		//return built list instance
		return file;
	}
	
	@Override
	protected void defineScopeMembers(ClassInstance inst) {
		//define super object's members
		super.defineScopeMembers(inst);
		
		//cast to EnvisionFile
		EnvisionFile f = (EnvisionFile) inst;
		
		//extract instance scope
		Scope inst_scope = f.getScope();
		
		//define members
		inst_scope.defineFunction(new IFunc_create());
		inst_scope.defineFunction(new IFunc_getName());
		inst_scope.defineFunction(new IFunc_getParent());
		inst_scope.defineFunction(new IFunc_getParentFile());
		inst_scope.defineFunction(new IFunc_getPath());
		inst_scope.defineFunction(new IFunc_canRead());
		inst_scope.defineFunction(new IFunc_canWrite());
		inst_scope.defineFunction(new IFunc_exists());
		inst_scope.defineFunction(new IFunc_isDirectory());
		inst_scope.defineFunction(new IFunc_isFile());
		inst_scope.defineFunction(new IFunc_delete());
		inst_scope.defineFunction(new IFunc_lsn());
		inst_scope.defineFunction(new IFunc_ls());
		inst_scope.defineFunction(new IFunc_mkdir());
		inst_scope.defineFunction(new IFunc_mkdirs());
		inst_scope.defineFunction(new IFunc_rename());
		inst_scope.defineFunction(new IFunc_clear());
		inst_scope.defineFunction(new IFunc_lines());
		inst_scope.defineFunction(new IFunc_write());
		inst_scope.defineFunction(new IFunc_writeln());
		inst_scope.defineFunction(new IFunc_writeLines());
		inst_scope.defineFunction(new IFunc_flush());
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
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
			
			ret(EnvisionBooleanClass.newBoolean(r));
		}
	}
	
	/**
	 * Returns the immediate name of this file.
	 */
	private static class IFunc_getName<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getName() { super(STRING, "getName"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getName()));
		}
	}
	
	/**
	 * Returns the name of the parent directory.
	 */
	private static class IFunc_getParent<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getParent() { super(STRING, "getParent"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getParent()));
		}
	}
	
	/**
	 * Returns the parent directory as a file object.
	 */
	private static class IFunc_getParentFile<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getParentFile() { super(EnvisionFileClass.FILE_DATATYPE, "getParentFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			//ret(buildInstance(interpreter, new Object[] {getF().getParentFile()}));
		}
	}
	
	/**
	 * Returns the string system path to this file on this computer.
	 */
	private static class IFunc_getPath<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_getPath() { super(STRING, "getPath"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getPath()));
		}
	}
	
	/**
	 * Returns true if this file can be read from.
	 */
	private static class IFunc_canRead<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_canRead() { super(BOOLEAN, "canRead"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.canRead()));
		}
	}
	
	/**
	 * Returns true if this file can be written to.
	 */
	private static class IFunc_canWrite<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_canWrite() { super(BOOLEAN, "canWrite"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.canWrite()));
		}
	}
	
	/**
	 * Returns true if this path actually exists on the given computer's
	 * file system.
	 */
	private static class IFunc_exists<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_exists() { super(BOOLEAN, "exists"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.exists()));
		}
	}
	
	/**
	 * Returns true if this path represents a directory (folder) instead
	 * of a singluar file object.
	 */
	private static class IFunc_isDirectory<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_isDirectory() { super(BOOLEAN, "isDirectory"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.isDirectory()));
		}
	}
	
	/**
	 * Returns true if this path represents a file object vs. a directory
	 * object.
	 */
	private static class IFunc_isFile<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_isFile() { super(BOOLEAN, "isFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.isFile()));
		}
	}
	
	/**
	 * Attempts to delete this file.
	 */
	private static class IFunc_delete<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_delete() { super(BOOLEAN, "delete"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.delete()));
		}
	}
	
	/**
	 * Returns a list of file names visible within this directory.
	 */
	private static class IFunc_lsn<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_lsn() { super(LIST, "lsn"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			EnvisionList names = EnvisionListClass.newList(STRING);
			for (String n : inst.gfe().list()) names.add(EnvisionStringClass.newString(n));
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
			//EArrayList l = EUtil.map(gfe().list(), p -> EnvFile.buildFileInstance(interpreter, p)).collect(EArrayList.toEArrayList());
			//EnvisionList list = new EnvisionList(new EnvisionDatatype("File"), "ls");
			//for (Object o : l) list.add(o);
			//ret(list);
		}
	}
	
	/**
	 * If this file represents a directory path, this will create the
	 * directory if it does not already exist.
	 */
	private static class IFunc_mkdir<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_mkdir() { super(BOOLEAN, "mkdir"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.mkdir()));
		}
	}
	
	/**
	 * Creates the necessary directories required in order to make the
	 * requested file/directory creation possible.
	 */
	private static class IFunc_mkdirs<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_mkdirs() { super(BOOLEAN, "mkdirs"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.mkdirs()));
		}
	}
	
	/**
	 * Renames this file to a new name. Operates based on file paths.
	 */
	private static class IFunc_rename<E extends EnvisionFile> extends InstanceFunction<E> {
		public IFunc_rename() { super(BOOLEAN, "rename", new ParameterData(STRING)); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 0) ret(EnvisionBoolean.FALSE);
			String name = ((EnvisionString) args[0]).toString();
			boolean val = inst.iFile.renameTo(new File(name));
			ret(EnvisionBooleanClass.newBoolean(val));
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
			
			File f = inst.gfe();
			
			//prevent reads if this is not a file
			if (!f.isFile()) ret(EnvisionNull.NULL);
			
			try (Scanner reader = new Scanner(f)) {
				while (reader.hasNextLine()) {
					lines.add(EnvisionStringClass.newString(reader.nextLine()));
				}
			}
			catch (FileNotFoundException e) {
				throw new EnvisionLangError(e);
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
				
				for (Object o : list.getList()) {
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
		public IFunc_flush() { super(BOOLEAN, "flush", new ParameterData(STRING)); }
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
	
}