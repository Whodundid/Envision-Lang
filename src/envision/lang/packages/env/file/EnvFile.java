package envision.lang.packages.env.file;

import static envision.lang.util.Primitives.*;

import envision.exceptions.EnvisionError;
import envision.exceptions.errors.file.NoSuchFileError;
import envision.interpreter.EnvisionInterpreter;
import envision.interpreter.util.scope.Scope;
import envision.lang.EnvisionObject;
import envision.lang.classes.ClassInstance;
import envision.lang.classes.EnvisionClass;
import envision.lang.objects.EnvisionFunction;
import envision.lang.objects.EnvisionList;
import envision.lang.util.EnvisionDatatype;
import envision.lang.util.Primitives;
import envision.lang.util.data.ParameterData;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;
import eutil.strings.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class EnvFile extends EnvisionClass {
	
	/**
	 * Constant file datatype reference. Use throughout.
	 * Do not redefine.
	 */
	public static final EnvisionDatatype FILE_DATATYPE = new EnvisionDatatype("File");
	
	private BoxList<String, Object> ifields = new BoxList();
	private EArrayList<EnvFileMeth> imethods = new EArrayList();
	private EArrayList<String> toBeWritten = new EArrayList();
	
	public EnvFile(EnvisionInterpreter interpreter) {
		super(FILE_DATATYPE.getType());
		classScope = new Scope(interpreter.scope());
		addConstructor(new EnvisionFunction(new ParameterData(STRING)));
		//addConstructor(new EnvisionMethod(new ParameterData("File")));
		build();
	}
	
	//-----------------------------------------
	
	private void build() {
		try {
			createMethods();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new EnvisionError(e);
		}
	}
	
	private void createMethods() throws Exception {
		imethods.add(new Create());
		imethods.add(new GetName());
		imethods.add(new GetParent());
		imethods.add(new GetParentFile());
		imethods.add(new GetPath());
		imethods.add(new CanRead());
		imethods.add(new CanWrite());
		imethods.add(new Exists());
		imethods.add(new IsDirectory());
		imethods.add(new IsFile());
		imethods.add(new Delete());
		imethods.add(new LSN());
		imethods.add(new LS());
		imethods.add(new Mkdir());
		imethods.add(new Mkdirs());
		imethods.add(new Rename());
		imethods.add(new Clear());
		imethods.add(new Lines());
		imethods.add(new Write());
		imethods.add(new Writeln());
		imethods.add(new WriteLines());
		imethods.add(new Flush());
	}
	
	//-----------------------------------------
	
	@Override
	/**
	 * Goes through the process of physically creating the Envision class
	 * structure backend as well as producing a wrapped Java File object
	 * which can be internally referenced.
	 */
	protected ClassInstance buildInstance(EnvisionInterpreter interpreter, Object[] args) {
		// standard envision class creation
		Scope instanceScope = new Scope(classScope);
		ClassInstance inst = new ClassInstance(this, instanceScope);
		instanceScope.define("this", inst);
		
		// handle Java file wrapping
		File f = createWrapFile(interpreter, args[0]);
		EnvisionObject wrappedFile = EnvisionObject.javaObjectWrapper("_iFile_", f);
		// restrict access
		wrappedFile.setRestricted();
		wrappedFile.setFinal();
		// define the internal field
		instanceScope.define("_iFile_", wrappedFile);
		
		// creates instances of each file method onto this newly created instance
		for (EnvFileMeth m : imethods) {
			m.setPublic();
			m.setInst(inst);
			m.setScope(instanceScope);
			instanceScope.define(m.getName(), m);
		}
		
		return inst;
	}
	
	/** Creates an EnvisionFile class instance from the given file name. */
	public static ClassInstance buildFileInstance(EnvisionInterpreter interpreter, String fileName) {
		EnvFile file = new EnvFile(interpreter);
		return file.buildInstance(interpreter, new Object[]{fileName}); // wrapping in a list because the method expects a list
	}
	
	private File createWrapFile(EnvisionInterpreter interpreter, Object pathIn) {
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
	
	public static boolean isFile(Object in) {
		if (in instanceof ClassInstance) return isFile((ClassInstance) in);
		return false;
	}
	
	public static boolean isFile(ClassInstance inst) {
		return inst != null &&
			   inst.getTypeString().equals("File") &&
			   inst.get("_iFile_") != null &&
			   inst.get("_iFile_").getJavaObject() instanceof File;
	}
	
	public static String getFilePath(ClassInstance inst) {
		if (!isFile(inst)) return null;
		return ((File) inst.get("_iFile_").getJavaObject()).getAbsolutePath();
	}
	
	//-----------------------------------------
	
	/** A specialized type of method wrapper that deals with mapping Java File objects to Envision. */
	private abstract class EnvFileMeth extends EnvisionFunction {
		
		private ClassInstance inst;
		
		//-----------------------------------------
		
		public EnvFileMeth(Primitives rTypeIn, String nameIn) {
			super(rTypeIn, nameIn, new ParameterData());
		}
		
		public EnvFileMeth(Primitives rTypeIn, String nameIn, ParameterData paramsIn) {
			super(rTypeIn, nameIn, paramsIn);
		}
		
		public EnvFileMeth(EnvisionDatatype rTypeIn, String nameIn) {
			super(rTypeIn, nameIn, new ParameterData());
		}
		
		public EnvFileMeth(EnvisionDatatype rTypeIn, String nameIn, ParameterData paramsIn) {
			super(rTypeIn, nameIn, paramsIn);
		}
		
		//-----------------------------------------
		
		public void setInst(ClassInstance instIn) {
			inst = instIn;
		}
		
		/** Returns the wrapped Java File object. Does not guarantee the file location actually exists however. */
		protected File getF() {
			EnvisionObject obj = inst.get("_iFile_");
			File f = (File) obj.getJavaObject();
			return f;
		}
		
		/** Returns the file if, and only if, the file exists, otherwise a 'NoSuchFileError' is thrown instead.
		 *  Stands for 'Get File Exists' */
		protected File gfe() {
			File f = getF();
			if (!f.exists()) throw new NoSuchFileError(f);
			return f;
		}
	}
	
	//-----------------------------------------
	
	/** Attempts to create a file under the same path/name as this file object. */
	private class Create extends EnvFileMeth {
		public Create() { super(BOOLEAN, "create"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			boolean r = false;
			
			try {
				r = getF().createNewFile();
			}
			catch (Exception e) {
				throw new EnvisionError(e);
			}
			
			ret(r);
		}
	}
	
	/** Returns the immediate name of this file. */
	private class GetName extends EnvFileMeth {
		public GetName() { super(STRING, "getName"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().getName()); }
	}
	
	/** Returns the name of the parent directory. */
	private class GetParent extends EnvFileMeth {
		public GetParent() { super(STRING, "getParent"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().getParent()); }
	}
	
	/** Returns the parent directory as a file object. */
	private class GetParentFile extends EnvFileMeth {
		public GetParentFile() { super(new EnvisionDatatype("File"), "getParentFile"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			ret(buildInstance(interpreter, new Object[] {getF().getParentFile()}));
		}
	}
	
	/** Returns the string system path to this file on this computer. */
	private class GetPath extends EnvFileMeth {
		public GetPath() { super(STRING, "getPath"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().getPath()); }
	}
	
	/** Returns true if this file can be read from. */
	private class CanRead extends EnvFileMeth {
		public CanRead() { super(BOOLEAN, "canRead"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().canRead()); }
	}
	
	/** Returns true if this file can be written to. */
	private class CanWrite extends EnvFileMeth {
		public CanWrite() { super(BOOLEAN, "canWrite"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().canWrite()); }
	}
	
	/** Returns true if this path actually exists on the given computer's file system. */
	private class Exists extends EnvFileMeth {
		public Exists() { super(BOOLEAN, "exists"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().exists()); }
	}
	
	/** Returns true if this path represents a directory (folder) instead of a singluar file object. */
	private class IsDirectory extends EnvFileMeth {
		public IsDirectory() { super(BOOLEAN, "isDirectory"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().isDirectory()); }
	}
	
	/** Returns true if this path represents a file object vs. a directory object. */
	private class IsFile extends EnvFileMeth {
		public IsFile() { super(BOOLEAN, "isFile"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().isFile()); }
	}
	
	/** Attempts to delete this file. */
	private class Delete extends EnvFileMeth {
		public Delete() { super(BOOLEAN, "delete"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().delete()); }
	}
	
	/** Returns a list of file names visible within this directory. */
	private class LSN extends EnvFileMeth {
		public LSN() { super(LIST, "lsn"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			ret(EnvisionList.of(gfe().list()));
		}
	}
	
	/** Returns a list of file objects representing this directory's visible files. */
	private class LS extends EnvFileMeth {
		public LS() { super(LIST, "ls"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			EArrayList l = EUtil.map(gfe().list(), p -> EnvFile.buildFileInstance(interpreter, p)).collect(EArrayList.toEArrayList());
			EnvisionList list = new EnvisionList(new EnvisionDatatype("File"), "ls");
			for (Object o : l) list.add(o);
			ret(list);
		}
	}
	
	/** If this file represents a directory path, this will create the directory if it does not already exist. */
	private class Mkdir extends EnvFileMeth {
		public Mkdir() { super(BOOLEAN, "mkdir"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().mkdir()); }
	}
	
	/** Creates the necessary directories required in order to make the requested file/directory creation possible. */
	private class Mkdirs extends EnvFileMeth {
		public Mkdirs() { super(BOOLEAN, "mkdirs"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) { ret(getF().mkdirs()); }
	}
	
	/** Renames this file to a new name. Operates based on file paths. */
	private class Rename extends EnvFileMeth {
		public Rename() { super(BOOLEAN, "rename", new ParameterData(STRING)); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			if (args.length == 0) ret(false);
			ret(getF().renameTo(new File((String) cv(args[0]))));
		}
	}
	
	/** Sets whether this file will append or overwrite data. */
	/*
	private class Append extends EnvFileMeth {
		public Append() { super(VOID, "setAppend", new ParameterData(BOOLEAN)); }
		public void invoke(EnvisionInterpreter interpreter, Object... args) {
			if (args.isNotEmpty()) {
				appendMode = (boolean) convert(args.getFirst());
			}
		}
	}
	*/
	
	/** Returns true if this file is in append mode. */
	/*
	private class IsAppend extends EnvFileMeth {
		public IsAppend() { super(BOOLEAN, "isAppend"); }
		public void invoke(EnvisionInterpreter interpreter, Object... args) { ret(appendMode); }
	}
	*/
	
	/** Attempts to remove the contents of this file, returns true if successful. */
	private class Clear extends EnvFileMeth {
		public Clear() { super(BOOLEAN, "clear"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			File f = getF();
			
			//prevent reads if this is not a file
			if (!f.isFile()) ret(false);
			
			try (PrintWriter writer  = new PrintWriter(f)) {
				writer.print("");
				ret(true);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			ret(false);
		}
	}
	
	/** Returns all of the lines within the file as a list. */
	private class Lines extends EnvFileMeth {
		public Lines() { super(LIST, "lines"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			EnvisionList lines = new EnvisionList(Primitives.STRING.toDatatype(), "lines");
			
			File f = gfe();
			
			//prevent reads if this is not a file
			if (!f.isFile()) ret(false);
			
			try (Scanner reader = new Scanner(f)) {
				while (reader.hasNextLine()) {
					lines.add(reader.nextLine());
				}
			}
			catch (FileNotFoundException e) {
				throw new EnvisionError(e);
			}
			
			ret(lines);
		}
	}
	
	/** Writes the given object to the file, does not add a new line. */
	private class Write extends EnvFileMeth {
		public Write() { super(BOOLEAN, "write"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			File f = getF();
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(false);
			
			if (args.length == 1) {
				String s = convert(args[0]).toString();
				
				toBeWritten.add(s);
				ret(true);
			}
			
			ret(false);
		}
	}
	
	/** Writes the given object to the file, adds a new line. */
	private class Writeln extends EnvFileMeth {
		public Writeln() { super(BOOLEAN, "writeln"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			File f = getF();
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(false);
			
			if (args.length == 1) {
				String s = convert(args[0]).toString();
				s += "\n";
				
				toBeWritten.add(s);
				ret(true);
			}
			
			ret(false);
		}
	}
	
	/** Takes in a list and writes each value to the file line by line. */
	private class WriteLines extends EnvFileMeth {
		public WriteLines() { super(BOOLEAN, "writeLines"); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			File f = getF();
			
			//prevent writes if this is not a file
			if (!f.isFile()) ret(false);
			
			if (args.length > 0 && args[0] instanceof EnvisionList) {
				EnvisionList list = (EnvisionList) args[0];
				
				for (Object o : list.getEArrayList()) {
					String s = o.toString();
					s += "\n";
					toBeWritten.add(s);
				}
				
				ret(true);
			}
			
			ret(false);
		}
	}
	
	private class Flush extends EnvFileMeth {
		public Flush() { super(BOOLEAN, "flush", new ParameterData(STRING)); }
		public void invoke(EnvisionInterpreter interpreter, Object[] args) {
			File f = getF();
			
			//prevent writes if this is not a file
			if (!f.isFile() || toBeWritten.isEmpty()) ret(false);
			
			String str = "";
			for (String s : toBeWritten) {
				str += s;
			}
			
			try {
				Files.write(f.toPath(), str.getBytes(), StandardOpenOption.APPEND);
				toBeWritten.clear();
				ret(true);
			}
			catch (IOException e) {
				throw new EnvisionError(e);
			}
			
			ret(false);
		}
	}
	
}
