package envision_lang.packages.env.file;

import java.io.File;

import envision_lang.exceptions.errors.file.NoSuchFileError;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.internal.JavaObjectWrapper;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import eutil.strings.StringUtil;

public class EnvisionFile extends ClassInstance {
	
	public File iFile;
	public EList<String> toBeWritten = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public EnvisionFile(String pathIn) {
		super(EnvisionFileClass.FILE_CLASS);
		iFile = createWrapFile(pathIn);
	}
	
	private File createWrapFile(String pathIn) {
		String dirPath = EnvisionInterpreter.topDir().getDirFile().getAbsolutePath();
		String argPath = (pathIn != null) ? pathIn : null;
		
		// first determine if the given path is null
		if (argPath == null) return new File(dirPath);
		
		// next, determine if this file is a relative file path off of the base program's default directory
		if (EUtil.contains(new File(dirPath).list(), StringUtil.subStringToString(argPath, "\\", "/"))) {
			return new File(dirPath, argPath);
		}
		
		return new File(argPath);
	}
	
	//---------
	// Methods
	//---------
	
	public File getF() {
		return iFile;
	}
	
	public File gfe() {
		if (iFile.exists()) return iFile;
		throw new NoSuchFileError(iFile);
	}
	
	//-----------------------------------------
	
	public static boolean isFile(Object in) {
		if (in instanceof ClassInstance ci) return isFile(ci);
		return false;
	}
	
	public static boolean isFile(ClassInstance inst) {
		if (EnvisionFileClass.FILE_CLASS.isInstanceof(inst)) {
			JavaObjectWrapper file_wrapper = (JavaObjectWrapper) inst.get("_iFile_");
			return file_wrapper != null && file_wrapper.getJavaObject() instanceof File;
		}
		return false;
	}
	
	public static String getFilePath(ClassInstance inst) {
		if (!isFile(inst)) return null;
		JavaObjectWrapper file_wrapper = (JavaObjectWrapper) inst.get("_iFile_");
		return ((File) file_wrapper.getJavaObject()).getAbsolutePath();
	}
	
}