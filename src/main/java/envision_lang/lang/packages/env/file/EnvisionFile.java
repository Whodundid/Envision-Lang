package envision_lang.lang.packages.env.file;

import java.io.File;
import java.util.Objects;

import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.exceptions.errors.file.NoSuchFileError;
import eutil.EUtil;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public class EnvisionFile extends ClassInstance {
	
	/** The internal Java::File object for which this EnvisionFile wraps. */
	public File iFile;
	/** Any lines that will be written to a file. */
	public EList<String> toBeWritten = EList.newList();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionFile(String pathIn) {
		super(EnvisionFileClass.FILE_CLASS);
		iFile = createWrapFile(pathIn);
	}
	
	public EnvisionFile(File fileIn) {
		super(EnvisionFileClass.FILE_CLASS);
		iFile = Objects.requireNonNull(fileIn);
	}
	
	public EnvisionFile(String parent, String child) {
		super(EnvisionFileClass.FILE_CLASS);
		EUtil.requireNonNull(parent, child);
		iFile = new File(parent, child);
	}
	
	public EnvisionFile(File parent, String child) {
		super(EnvisionFileClass.FILE_CLASS);
		EUtil.requireNonNull(parent, child);
		iFile = new File(parent, child);
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private File createWrapFile(String pathIn) {
		String dirPath = EnvisionInterpreter.topDir().getDirFile().getAbsolutePath();
		String argPath = (pathIn != null) ? pathIn : null;
		
		// first determine if the given path is null
		if (argPath == null) return new File(dirPath);
		
		// next, determine if this file is a relative file path off of the base program's default directory
		if (EUtil.contains(new File(dirPath).list(), EStringUtil.subStringToString(argPath, "\\", "/"))) {
			return new File(dirPath, argPath);
		}
		
		return new File(argPath);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Returns the internal file for which this EnvisionFile wraps.
	 * <p>
	 * NOTE: while the the returned file should never be Java::NULL, it may not
	 * actually exist within this filesystem's scope.
	 * 
	 * @throws IllegalStateException if the internal file is somehow Java::NULL.
	 */
	public File getF() {
		if (iFile == null) throw new IllegalStateException("Error! Internal Java::File is null!");
		return iFile;
	}
	
	/**
	 * Returns the internal file but only if it exists.
	 * <p>
	 * If the internal file does not actually exist within this filesystem's
	 * scope a 'NoSuchFileError' is thrown instead.
	 * 
	 * @throws IllegalStateException if the internal file is somehow Java::NULL.
	 * @throws NoSuchFileError if the internal file doesn't actually exist.
	 * 
	 * @return The file.
	 */
	public File gfe() {
		if (iFile == null) throw new IllegalStateException("Error! Internal Java::File is null!");
		if (iFile.exists()) return iFile;
		throw new NoSuchFileError(iFile);
	}
	
	//-----------------------------------------
	
	/**
	 * Returns true if the given object is an instance of an
	 * EnvisionFile (not Java::File).
	 * 
	 * @param in The object to check
	 * 
	 * @return True if the given object is an EnvisionFile
	 */
	public static boolean isFile(Object in) {
		return (in instanceof ClassInstance ci) ? isFile(ci) : false;
	}
	
	/**
	 * Returns true if the given class instance if an instance of an
	 * EnvisionFile.
	 * 
	 * @param inst The class instance to check
	 * 
	 * @return True if the given Envision class instance is an EnvisionFile
	 */
	public static boolean isFile(ClassInstance inst) {
		return EnvisionFileClass.FILE_CLASS.isInstanceof(inst);
	}
	
	/**
	 * Extracts the 'Java::File::getAbsolutePath' value from the given
	 * EnvisionFile.
	 * <p>
	 * NOTE: if the given EnvisionFile is Java::NULL, then Java::NULL will be
	 * returned instead.
	 * 
	 * @param inst The class instance to extract a file path from
	 * 
	 * @return The EnvisionFile's internal file's extracted file path
	 */
	public static String getFilePath(EnvisionFile inst) {
		if (inst == null) return null;
		return inst.iFile.getAbsolutePath();
	}
	
}