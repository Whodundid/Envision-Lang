package envision.packages.env.file;

import static envision.lang.util.Primitives.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.InvalidArgumentError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.datatypes.EnvisionBoolean;
import envision.lang.datatypes.EnvisionBooleanClass;
import envision.lang.datatypes.EnvisionList;
import envision.lang.datatypes.EnvisionListClass;
import envision.lang.datatypes.EnvisionString;
import envision.lang.datatypes.EnvisionStringClass;
import envision.lang.internal.EnvisionNull;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.InstanceFunction;
import envision.lang.util.ParameterData;
import eutil.EUtil;
import eutil.strings.StringUtil;

public class EnvisionFileClass extends EnvisionClass {

	/**
	 * Constant file datatype reference. Use throughout.
	 * Do not redefine.
	 */
	public static final EnvisionDatatype FILE_DATATYPE = new EnvisionDatatype("File");
	
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
		
		if (args.length != 1) throw new InvalidArgumentError("Expected exactly one argument passed!");
		
		EnvisionObject arg_val = args[0];
		
		//don't accept null arguments
		if (arg_val == null) throw new InvalidArgumentError("Passed argument cannot be null!");
		
		//attempt creation from arg
		if (arg_val instanceof EnvisionString env_str) file = new EnvisionFile(env_str.string_val);
		else if (arg_val instanceof EnvisionFile env_file) file = new EnvisionFile(env_file.iFile.getAbsolutePath());
		
		//if null, creation failed!
		if (file == null)
			throw new InvalidArgumentError("Cannot convert the value '"+arg_val+"' to an "+getDatatype()+"!");
		
		//define scope members
		defineScopeMembers(file);
				
		//return built list instance
		return file;
	}
	
	private File createWrapFile(EnvisionInterpreter interpreter, EnvisionString pathIn) {
		String dirPath = interpreter.workingDir().getDirFile().getAbsolutePath();
		String argPath = (pathIn != null) ? (String) convert(pathIn) : null;
		
		// first determine if the given path is null
		if (argPath == null) return new File(dirPath);
		
		// next, determine if this file is a relative file path off of the base program's default directory
		if (EUtil.contains(new File(dirPath).list(), StringUtil.subStringToString(argPath, "\\", "/"))) {
			return new File(dirPath, argPath);
		}
		
		return new File(argPath);
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
		inst_scope.defineFunction(new IFunc_create(f));
		inst_scope.defineFunction(new IFunc_getName(f));
		inst_scope.defineFunction(new IFunc_getParent(f));
		inst_scope.defineFunction(new IFunc_getParentFile(f));
		inst_scope.defineFunction(new IFunc_getPath(f));
		inst_scope.defineFunction(new IFunc_canRead(f));
		inst_scope.defineFunction(new IFunc_canWrite(f));
		inst_scope.defineFunction(new IFunc_exists(f));
		inst_scope.defineFunction(new IFunc_isDirectory(f));
		inst_scope.defineFunction(new IFunc_isFile(f));
		inst_scope.defineFunction(new IFunc_delete(f));
		inst_scope.defineFunction(new IFunc_lsn(f));
		inst_scope.defineFunction(new IFunc_ls(f));
		inst_scope.defineFunction(new IFunc_mkdir(f));
		inst_scope.defineFunction(new IFunc_mkdirs(f));
		inst_scope.defineFunction(new IFunc_rename(f));
		inst_scope.defineFunction(new IFunc_clear(f));
		inst_scope.defineFunction(new IFunc_lines(f));
		inst_scope.defineFunction(new IFunc_write(f));
		inst_scope.defineFunction(new IFunc_writeln(f));
		inst_scope.defineFunction(new IFunc_writeLines(f));
		inst_scope.defineFunction(new IFunc_flush(f));
	}
	
	//---------------------------
	// Instance Member Functions
	//---------------------------
	
	/**
	 * Attempts to create a file under the same path/name as this file
	 * object.
	 */
	private class IFunc_create<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_create(E instIn) { super(instIn, BOOLEAN, "create"); }
		public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			boolean r = false;
			
			try {
				r = inst.iFile.createNewFile();
			}
			catch (Exception e) {
				throw new EnvisionError(e);
			}
			
			ret(EnvisionBooleanClass.newBoolean(r));
		}
	}
	
	/**
	 * Returns the immediate name of this file.
	 */
	private static class IFunc_getName<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_getName(E instIn) { super(instIn, STRING, "getName"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getName()));
		}
	}
	
	/**
	 * Returns the name of the parent directory.
	 */
	private static class IFunc_getParent<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_getParent(E instIn) { super(instIn, STRING, "getParent"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getParent()));
		}
	}
	
	/**
	 * Returns the parent directory as a file object.
	 */
	private static class IFunc_getParentFile<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_getParentFile(E instIn) { super(instIn, EnvisionFileClass.FILE_DATATYPE, "getParentFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			//ret(buildInstance(interpreter, new Object[] {getF().getParentFile()}));
		}
	}
	
	/**
	 * Returns the string system path to this file on this computer.
	 */
	private static class IFunc_getPath<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_getPath(E instIn) { super(instIn, STRING, "getPath"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionStringClass.newString(inst.iFile.getPath()));
		}
	}
	
	/**
	 * Returns true if this file can be read from.
	 */
	private static class IFunc_canRead<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_canRead(E instIn) { super(instIn, BOOLEAN, "canRead"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.canRead()));
		}
	}
	
	/**
	 * Returns true if this file can be written to.
	 */
	private static class IFunc_canWrite<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_canWrite(E instIn) { super(instIn, BOOLEAN, "canWrite"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.canWrite()));
		}
	}
	
	/**
	 * Returns true if this path actually exists on the given computer's
	 * file system.
	 */
	private static class IFunc_exists<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_exists(E instIn) { super(instIn, BOOLEAN, "exists"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.exists()));
		}
	}
	
	/**
	 * Returns true if this path represents a directory (folder) instead
	 * of a singluar file object.
	 */
	private static class IFunc_isDirectory<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_isDirectory(E instIn) { super(instIn, BOOLEAN, "isDirectory"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.isDirectory()));
		}
	}
	
	/**
	 * Returns true if this path represents a file object vs. a directory
	 * object.
	 */
	private static class IFunc_isFile<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_isFile(E instIn) { super(instIn, BOOLEAN, "isFile"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.isFile()));
		}
	}
	
	/**
	 * Attempts to delete this file.
	 */
	private static class IFunc_delete<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_delete(E instIn) { super(instIn, BOOLEAN, "delete"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.delete()));
		}
	}
	
	/**
	 * Returns a list of file names visible within this directory.
	 */
	private static class IFunc_lsn<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_lsn(E instIn) { super(instIn, LIST, "lsn"); }
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
		IFunc_ls(E instIn) { super(instIn, LIST, "ls"); }
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
		IFunc_mkdir(E instIn) { super(instIn, BOOLEAN, "mkdir"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.mkdir()));
		}
	}
	
	/**
	 * Creates the necessary directories required in order to make the
	 * requested file/directory creation possible.
	 */
	private static class IFunc_mkdirs<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_mkdirs(E instIn) { super(instIn, BOOLEAN, "mkdirs"); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			ret(EnvisionBooleanClass.newBoolean(inst.iFile.mkdirs()));
		}
	}
	
	/**
	 * Renames this file to a new name. Operates based on file paths.
	 */
	private static class IFunc_rename<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_rename(E instIn) { super(instIn, BOOLEAN, "rename", new ParameterData(STRING)); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			if (args.length == 0) ret(EnvisionBoolean.FALSE);
			String name = ((EnvisionString) args[0]).string_val;
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
		IFunc_clear(E instIn) { super(instIn, BOOLEAN, "clear"); }
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
		IFunc_lines(E instIn) { super(instIn, LIST, "lines"); }
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
				throw new EnvisionError(e);
			}
			
			ret(lines);
		}
	}
	
	/**
	 * Writes the given object to the file, does not add a new line.
	 */
	private static class IFunc_write<E extends EnvisionFile> extends InstanceFunction<E> {
		IFunc_write(E instIn) { super(instIn, BOOLEAN, "write"); }
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
		IFunc_writeln(E instIn) { super(instIn, BOOLEAN, "writeln"); }
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
		IFunc_writeLines(E instIn) { super(instIn, BOOLEAN, "writeLines"); }
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
		IFunc_flush(E instIn) { super(instIn, BOOLEAN, "flush", new ParameterData(STRING)); }
		@Override public void invoke(EnvisionInterpreter interpreter, EnvisionObject[] args) {
			File f = inst.iFile;
			
			//prevent writes if this is not a file
			if (!f.isFile() || inst.toBeWritten.isEmpty()) ret(EnvisionBoolean.FALSE);
			
			String str = "";
			for (String s : inst.toBeWritten) {
				str += s;
			}
			
			try {
				Files.write(f.toPath(), str.getBytes(), StandardOpenOption.APPEND);
				inst.toBeWritten.clear();
				ret(EnvisionBoolean.TRUE);
			}
			catch (IOException e) {
				throw new EnvisionError(e);
			}
			
			ret(EnvisionBoolean.FALSE);
		}
	}
	
}
