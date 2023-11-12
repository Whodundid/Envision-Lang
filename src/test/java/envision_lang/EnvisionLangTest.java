package envision_lang;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;

import envision_lang._launch.EnvisionCodeFile;
import envision_lang._launch.EnvisionProgram;
import envision_lang.debug.DebugTokenPrinter;
import envision_lang.interpreter.EnvisionInterpreter;
import envision_lang.interpreter.util.scope.IScope;
import envision_lang.lang.EnvisionObject;
import envision_lang.lang.datatypes.EnvisionBoolean;
import envision_lang.lang.datatypes.EnvisionChar;
import envision_lang.lang.datatypes.EnvisionDouble;
import envision_lang.lang.datatypes.EnvisionInt;
import envision_lang.lang.datatypes.EnvisionString;
import envision_lang.lang.datatypes.EnvisionVariable;
import envision_lang.lang.natives.NativeTypeManager;
import envision_lang.parser.EnvisionLangParser;
import envision_lang.parser.expressions.ParsedExpression;
import envision_lang.parser.expressions.expression_types.Expr_Literal;
import envision_lang.parser.expressions.expression_types.Expr_Var;
import envision_lang.parser.statements.ParsedStatement;
import envision_lang.parser.statements.StatementHandler;
import envision_lang.parser.statements.statement_types.Stmt_Expression;
import envision_lang.parser.statements.statement_types.Stmt_If;
import envision_lang.tokenizer.IKeyword;
import envision_lang.tokenizer.ReservedWord;
import envision_lang.tokenizer.Token;
import envision_lang.tokenizer.Tokenizer;
import eutil.strings.EStringBuilder;

public abstract class EnvisionLangTest {
    
    protected static EnvisionProgram program;
    protected static EnvisionCodeFile codeFile;
    protected static EnvisionInterpreter interpreter;
    protected static boolean debugOutput;
    
    @BeforeEach
    protected void build() {
        NativeTypeManager.init();
        
        codeFile = new EnvisionCodeFile(new File(UUID.randomUUID().toString()));
        program = new EnvisionProgram("envision_test_program", codeFile);
        
        // force the code file to be valid because our test 'file' doesn't actually exist
        codeFile.forceValid();
        
//        try {
//            // hack the program's working dir and main file fields to our test ones
//            var workingDirField = program.getClass().getDeclaredField("dir");
//            workingDirField.setAccessible(true);
//            workingDirField.set(program, codeFile.getWorkingDir());
//            
//            var mainFileField = program.getClass().getDeclaredField("mainCodeFile");
//            mainFileField.setAccessible(true);
//            mainFileField.set(program, codeFile);
//            
//            var mainScopeField = program.getClass().getDeclaredField("mainFileScope");
//            mainScopeField.setAccessible(true);
//            mainScopeField.set(program, codeFile.scope());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        
        interpreter = new EnvisionInterpreter(program, codeFile);
    }
    
    protected static class DebugStatement extends ParsedStatement {
        public boolean run = false;
        
        public DebugStatement() {
            super(Token.EOF(0));
        }
        
        @Override
        public void execute(StatementHandler handler) {
            run = true;
        }
    }
    
    protected void assertTestRunsForAtLeastNSeconds(int seconds, Runnable r) {
        new MinimumExecutionTimeVerifier(seconds, r).start();
    }
    
    protected void assertTestRunsForAtLeastNTime(TimeUnit unit, long timeAmount, Runnable r) {
        new MinimumExecutionTimeVerifier(unit, timeAmount, r).start();
    }
    
    /**
     * A specialized class that ensures that a given runnable runs for AT LEAST
     * the specified amount of time.
     * <p>
     * In practice, the actual running time seems to vary wildly.. (up to a
     * half a second difference)
     * 
     * @author Hunter Bragg
     */
    protected static class MinimumExecutionTimeVerifier {
        private Thread timerThread, runnerThread;
        private TimeUnit unit;
        private volatile boolean end = false;
        
        public MinimumExecutionTimeVerifier(long seconds, Runnable r) {
            this(TimeUnit.SECONDS, seconds, r);
        }
        
        public MinimumExecutionTimeVerifier(TimeUnit unit, long timeAmount, Runnable r) {
            Objects.requireNonNull(unit);
            Objects.requireNonNull(r);
            this.unit = unit;
            
            // thread to keep track of minimum time to run for
            timerThread = new Thread(() -> {
                long start = curTime();
                runnerThread.start();
                while (!end) {
                    if ((curTime() - start) > timeAmount) break;
                }
                runnerThread.interrupt();
                assertFalse(end);
            });
            
            // thread to run the actual runner target and keep
            // track of whether or not it finishes
            runnerThread = new Thread(() -> {
                r.run();
                end = true;
            });
            
            timerThread.setDaemon(false);
        }
        
        private long curTime() {
            return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
        
        public void start() {
            timerThread.start();
            
            // force current thread to not die until timerThread finishes
            try {
                timerThread.join();
            }
            catch (Exception e) {}
        }
    }
    
    protected static ParsedExpression var(String name) {
        return new Expr_Var(new Token(ReservedWord.IDENTIFIER, name, name, 0));
    }
    
    protected static ParsedExpression literal(Object value) {
        IKeyword type = null;
        if (value instanceof Character) type = ReservedWord.CHAR_LITERAL;
        else if (value instanceof Integer || value instanceof Long) type = ReservedWord.INT_LITERAL;
        else if (value instanceof Float || value instanceof Double) type = ReservedWord.DOUBLE_LITERAL;
        else if (value instanceof String) type = ReservedWord.STRING_LITERAL;
        else if (value == Boolean.TRUE) type = ReservedWord.TRUE;
        else if (value == Boolean.FALSE) type = ReservedWord.FALSE;
        return new Expr_Literal(new Token(type, String.valueOf(value), value, 0), value);
    }
    
    public static void enableDebugOutput() {
        debugOutput = true;
    }
    
    protected ScopeManager scope() {
        return new ScopeManager(extractScope());
    }
    
    protected static void execute(String statement) {
        stmt(statement);
        interpreter.executeStatements(codeFile.getStatements(), interpreter.scope());
    }
    
    protected static void execute(ParsedStatement statement) {
        interpreter.execute(statement);
    }
    
    protected static void buildCodeFile(String lineToAdd) {
        Tokenizer t = new Tokenizer(lineToAdd);
        codeFile.getLineTokens().clearThenAddAll(t.getLineTokens());
        codeFile.getTokens().clearThenAddAll(t.getTokens());
        codeFile.getLines().clearThenAddAll(t.getLines());
    }
    
    protected static EnvisionInterpreter code(String codeToInject) {
        buildCodeFile(codeToInject);
        var stmts = EnvisionLangParser.parse(codeFile);
        codeFile.getStatements().clearThenAddAll(stmts);
        return interpreter;
    }
    
    protected static void execute() {
        interpreter.executeStatements(codeFile.getStatements(), interpreter.scope());
    }
    
    protected static void executeCode(String codeToExecute) {
        code(codeToExecute);
        interpreter.executeStatements(codeFile.getStatements(), interpreter.scope());
    }
    
    protected static void injectJavaObject(String asName, Object objectToInject) {
        interpreter.injectJavaObject(asName, objectToInject);
    }
    
    protected static <E extends ParsedStatement> E stmt(String line) { return stmt(line, 0); }
    protected static <E extends ParsedStatement> E stmt(String line, int stmtIndex) {
        buildCodeFile(line);
        
        if (debugOutput) DebugTokenPrinter.printTokensInDepth("TEST_DEBUG", codeFile.getTokens());
        var stmts = EnvisionLangParser.parse(codeFile);
        codeFile.getStatements().clearThenAddAll(stmts);
        
        return (E) stmts.get(stmtIndex);
    }
    
    protected static <E extends ParsedExpression> E expr(String line) { return expr(line, 0); }
    protected static <E extends ParsedExpression> E expr(String line, int exprIndex) {
        buildCodeFile(line);
        
        if (debugOutput) DebugTokenPrinter.printTokensInDepth("TEST_DEBUG", codeFile.getTokens());
        var stmt = EnvisionLangParser.parse(codeFile);
        
        return (E) ((Stmt_Expression) stmt.get(exprIndex)).expression;
    }
    
    protected void injectScope(IScope scope) {
        try {
            var working_scope_field = interpreter.getClass().getDeclaredField("working_scope");
            working_scope_field.setAccessible(true);
            working_scope_field.set(interpreter, scope);
            working_scope_field.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected IScope extractScope() {
        return interpreter.scope();
    }
    
    public <E extends EnvisionObject> E get(String name) {
        return (E) scope().get(name);
    }
    
    public <E> E get_i(String name) {
        var value = scope().get(name);
        if (value instanceof EnvisionVariable v) return (E) v.get_i();
        return (E) value;
    }
    
    protected static void println(Object... objs) {
        var sb = new EStringBuilder();
        for (int i = 0; i < objs.length; i++) {
            sb.a(objs[i]);
            if (i < objs.length - 1) sb.a(" ");
        }
        System.out.println(sb);
    }
    
    protected EnvisionObject def(String name, EnvisionObject o) { return scope().def(name, o); }
    protected EnvisionBoolean defBool(String name, boolean val) { return scope().defBool(name, val); }
    protected EnvisionChar defChar(String name, char val) { return scope().defChar(name, val); }
    protected EnvisionInt defInt(String name, long val) { return scope().defInt(name, val); }
    protected EnvisionDouble defDouble(String name, double val) { return scope().defDouble(name, val); }
    protected EnvisionString defString(String name, String val) { return scope().defString(name, val); }
    
    //============
    // Assertions
    //============
    
    protected static void assertRun(DebugStatement s) { assertTrue(s.run); }
    protected static void assertNotRun(DebugStatement s) { assertFalse(s.run); }
    
    //===============
    // If Statements
    //===============
    
    protected static Stmt_If stmt_if(boolean condition, ParsedStatement then_branch, ParsedStatement else_branch) {
        return stmt_if(literal(condition), then_branch, else_branch);
    }
    protected static Stmt_If stmt_if(ParsedExpression condition, ParsedStatement then_branch,
        ParsedStatement else_branch) {
        return new Stmt_If(Token.EOF(0), condition, then_branch, else_branch);
    }
    
}
