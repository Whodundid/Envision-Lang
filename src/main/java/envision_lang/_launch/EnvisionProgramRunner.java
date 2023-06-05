package envision_lang._launch;

import envision_lang.EnvisionLang;
import envision_lang.interpreter.EnvisionInterpreter;

public class EnvisionProgramRunner {
	
	private EnvisionInterpreter interpreter;
	private EnvisionProgram program;
	private volatile boolean isRunning = false;
	
	public EnvisionProgramRunner(EnvisionProgram programIn) {
		program = programIn;
	}
	
	public synchronized void execute() {
		if (!isRunning) {
			isRunning = true;
			
			try {
				interpreter = EnvisionLang.runProgram(program);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void executeNext() {
		if (!isRunning) {
			execute();
		}
		
		if (interpreter != null) {
			interpreter.executeNext();
		}
	}
	
	public synchronized void terminate() {
		interpreter.terminate();
		isRunning = false;
	}
	
	public synchronized boolean hasNextInstruction() {
		return (isRunning && interpreter != null && interpreter.hasNext());
	}
	
	public boolean isRunning() { return isRunning; }
	
}
