package envision_lang.interpreter.util.throwables;

/**
 * Thrown when executed Envision Code has requested the interpreter to
 * shutdown. This call stops all further code execution.
 */
public class LangShutdownCall extends RuntimeException {}
