package main;

import envision_lang.EnvisionLang;
import envision_lang._launch.EnvisionEnvironmnetSettings.EnvironmentSetting;
import envision_lang._launch.EnvisionLangErrorCallBack;
import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import envision_lang.lang.language_errors.EnvisionLangError;
import eutil.sys.TracingPrintStream;

@SuppressWarnings("unused")
public class DefaultLangLauncher implements EnvisionLangErrorCallBack {
	
	public static void main(String[] args) throws Exception {
		TracingPrintStream.enableTrace();
		TracingPrintStream.enableTracingEmptyLines(true);
		TracingPrintStream.disableTrace();
		
		//performanceReference();
		new DefaultLangLauncher();
	}
	
	//====================================================================================
	
	public DefaultLangLauncher() throws Exception {
		//Thread.sleep(20000);
		EnvisionLang.setErrorCallback(this);
		EnvisionLang.getInstance();
		
//		TestPoint t = new TestPoint(10, 5);
//		System.out.println("JAVA: " + t);
		EnvisionProgram program = new EnvisionProgram("program");
//		program.addJavaObjectToProgram("t", t);
		
		EnvisionLang.setLaunchSettings(
			EnvironmentSetting.ENABLE_BLOCK_STATEMENT_PARSING,
			EnvironmentSetting.ENABLE_BLOCKING_STATEMENTS
//			PRELOAD_LANGUAGE
//			DEBUG_MODE,
//			LIVE_MODE,
//			,TOKENIZE
//			TOKENIZE_IN_DEPTH
//			,PARSE_STATEMENTS
//			,DONT_EXECUTE
		);
		
		long start = System.currentTimeMillis();
		{
			var runner = new EnvisionProgramRunner(program);
			runner.execute();
			//EnvisionLang.runProgram(program);
		}
		System.out.println("END: " + (System.currentTimeMillis() - start) + " ms");
//		System.out.println("JAVA: " + t);
		
//		var runner = new EnvisionProgramRunner(program);
//        runner.execute();
//        
//        System.out.println("JAVA: " + t);
	}
	
	//====================================================================================
	
	@Override public void handleError(EnvisionLangError e) { handleException(e); }
	@Override public void handleException(Exception e) { e.printStackTrace(); }
	
	private void performanceReference() {
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < 10000000; i++) {
//			var a = new Java_Performance_Reference(ERandomUtil.getRoll(0, 5), ERandomUtil.getRoll(0, 5), ERandomUtil.getRoll(0, 5));
//			var b = new Java_Performance_Reference(ERandomUtil.getRoll(0, 4), ERandomUtil.getRoll(0, 4), ERandomUtil.getRoll(0, 4));
//			
//			System.out.println(i + " " + a + " | " + b + " | " + (a.add(b)));
//		}
//		System.out.println(System.currentTimeMillis() - start);
	}
	
}

/*
+class Vector {
	// Fields
	-int x, y, z
	
	// Constructors
	+init(x, y, z)
	
	// Functions
	+func toString() -> "<{x}, {y}, {z}>"
	+func add(Vector t) -> Vector(x + t.x, y + t.y, z + t.z)
	
	// Operator Overloads
	+operator +(Vector t) -> add(t)
}

for (int i = 0; i < 100000; i++) {
	a = Vector(randInt(0, 5), randInt(0, 5), randInt(0, 5))
	b = Vector(randInt(0, 4), randInt(0, 4), randInt(0, 4))
	
	println(i, a, "|", b, "|", a + b)
}
 */

/*
public class Vector {
	int x, y, z
	public {
		init(x, y, z)
		
		string p() -> "<" + x + ", " + y + ", " + z + ">"
		
		Vector add(v) -> Vector(x + v.x, y + v.y, z + v.z)
		Vector sub(v) -> Vector(x - v.x, y - v.y, z - v.z)
	}
}
*/

/*
+class Vector {
	-int x, y, z
	
	+init(x, y, z)
	
	+func toString() -> "<{x}, {y}, {z}>"
	
	+func Vector [add:+, sub:-](Vector v) -> Vector(x @ v.x, y @ v.y, z @ v.z)
	+operator Vector [+:add(v), -:sub(v)](Vector v) -> @
}
*/

/*
int partition(list arr, int begin, end) {
    int pivot = arr[end]
    int i = (begin - 1)

    for (int j = begin; j < end; j++) {
        if (arr[j] <= pivot) {
			arr.swap(++i, j)
        }
    }

	arr.swap(i + 1, end)

    return i + 1
}

void quickSort(list arr, int begin, end) {
    if (begin < end) {
        int p = partition(arr, begin, end)

        quickSort(arr, begin, p - 1)
        quickSort(arr, p + 1, end)
    }
}
void quickSort(list arr) -> quickSort(arr, 0, arr.size() - 1)

//---------------------------------------------------

list l = [12, 43, 2, 439, 27, 12, 32]

print l
quickSort(l)
print l
*/

/*
void bubbleSort(list arr) {
	int n = arr.size()
	for (i to (n - 1), j to (n - i - 1))
		if (arr[j] > arr[j + 1])
			arr.swap(j, j + 1)
}

//---------------------------------------------------

list l = [12, 43, 2, 439, 27, 12, 32]

print l

int start = millis()
bubbleSort(l)
int end = millis() - start

print l + " : " + end + "ms"
*/

/*
list l = [12, 43, 2, 439, 27, 12, 32]

void merge(list arr, int l, m, r) {
	int n1 = m - l + 1
	int n2 = r - m

	list L = [], R = []

	for (i to n1) L.add(arr[l + i])
	for (i to n2) R.add(arr[m + 1 + i])

	int i = 0, j = 0, k = l
	
	while (i < n1 && j < n2)
		if (L[i] <= R[j]) arr.set(k++, L[i++])
		else arr.set(k++, R[j++])
		
	//copy remaining L and R elements
	while (i < n1) arr.set(k++, L[i++])
	while (j < n2) arr.set(k++, R[j++])
}

void sort(list arr, int l, r) {
	if (l < r) {
		// Find the middle point
		int m = l + (r - l) / 2;
		
		// Sort first and second halves
		sort(arr, l, m);
		sort(arr, m + 1, r);
		
		// Merge the sorted halves
		merge(arr, l, m, r);
	}
}
void sort(list arr) -> sort(arr, 0, arr.size() - 1)

//---------------------------------------------------

print l
sort(l)
print l
*/

/*
int power(n, p) -> (p != 0) ? n * power(n, p - 1) : 1

for (i to 5)
	print "4^" + i + ": " + power(4, i)
*/