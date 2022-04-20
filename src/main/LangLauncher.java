package main;

import static envision.EnvisionSettings.LaunchArgs.*;

import envision.Envision;
import envision.EnvisionErrorCallback;
import envision.EnvisionSettings;
import envision.EnvisionSettings.LaunchArgs;
import envision.exceptions.EnvisionError;
import eutil.random.RandomUtil;
import eutil.sys.TracingPrintStream;

@SuppressWarnings("unused")
public class LangLauncher extends EnvisionErrorCallback {
	
	public static void main(String[] args) throws Exception {
		TracingPrintStream.enableTrace();
		TracingPrintStream.enableTracingEmptyLines(true);
		TracingPrintStream.disableTrace();
		
		/*
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Thing a = new Thing(RandomUtil.getRoll(0, 5), RandomUtil.getRoll(0, 5), RandomUtil.getRoll(0, 5));
			Thing b = new Thing(RandomUtil.getRoll(0, 4), RandomUtil.getRoll(0, 4), RandomUtil.getRoll(0, 4));
			
			System.out.println(i + " " + a + " | " + b + " | " + (a.add(b)));
		}
		System.out.println(System.currentTimeMillis() - start);
		*/
		new LangLauncher();
	}
	
	public LangLauncher() throws Exception {
		//Thread.sleep(20000);
		
		Envision env = null;
		//env = new Envision();
		//env = new Envision(PRELOAD_LANGUAGE);
		//env = new Envision(TOKENIZE, PARSE_STATEMENTS, DONT_EXECUTE);
		env = new Envision(PARSE_STATEMENTS, TOKENIZE);
		env.setErrorCallback(this);
		
		//long preStart = System.currentTimeMillis();
		//EnvisionVM.compileByteCode(new File("program_compiled\\main.nvisc"));
		//EnvisionVM.interpretByteCode(new File("program_compiled\\main.nviscc"));
		//EnvisionVM.interpretByteCode(new File("program_compiled\\main.nviscc"));
		
		//env.runProgram("program");
		//env.runProgram("program");
		//env.runProgram("program");
		
		//long start = System.currentTimeMillis();
		{
			env.runProgram("program");
			//EnvisionVM.compileByteCode(new File("program_compiled\\main.nvisc"));
			//EnvisionVM.interpretByteCode(new File("program_compiled\\main.nviscc"));
		}
		//System.out.print("END: " + (System.currentTimeMillis() - start) + " ms");
		//System.out.println(" Total: " + (System.currentTimeMillis() - preStart) + " ms");
	}

	@Override
	public void handleError(EnvisionError e) {
		handleException(e);
	}

	@Override
	public void handleException(Exception e) {
		e.printStackTrace();
	}
	
}

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