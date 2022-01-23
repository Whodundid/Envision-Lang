package main;

import eutil.datatypes.EArrayList;

/** Used in conjunction with EnvisionLists to create a dynamic multidimensional array construct. */
public class DimensionalList<E> {
	
	/** The top level array containing n level of dimensionsional data. */
	private EArrayList array = new EArrayList();
	private final int numDimensions;
	private int[] dimLengths;
	
	public DimensionalList() { this(1, 0); }
	public DimensionalList(int numDimensionsIn) { this(numDimensionsIn, numDimensionsIn); }
	public DimensionalList(int numDimensionsIn, int... dimLengthsIn) {
		numDimensions = numDimensionsIn;
		dimLengths = dimLengthsIn;
		pad();
		build();
	}
	
	private DimensionalList(EArrayList listIn, int dimsIn) {
		array = listIn;
		numDimensions = dimsIn;
	}
	
	@Override
	public String toString() {
		return array.toString();
	}
	
	/** Ensures that the number of dimensional lengths given is at least as big as the number of dimensions total.
	 *  If the number of lengths given is less than the number of dimensions, 0 is automatically set by default. */
	private void pad() {
		if (dimLengths.length < numDimensions) {
			int[] a = new int[numDimensions];
			for (int i = 0; i < numDimensions; i++) {
				if (i < dimLengths.length) { a[i] = dimLengths[i]; }
				else if (i <= numDimensions - 2) { a[i] = numDimensions; }
				else { a[i] = 0; }
			}
			dimLengths = a;
		}
	}
	
	private void build() {
		if (numDimensions <= 0) { /* too small */ }
		if (numDimensions > 9) { /* too big */ }
		
		fillDepth(array, dimLengths[0], 0);
	}
	
	private void fillDepth(EArrayList in, int amount, int depth) {
		if (depth < numDimensions) {
			for (int i = 0; i < amount; i++) {
				EArrayList a = new EArrayList();
				
				//the next dimensional array size
				int newAmount = (depth + 1 < dimLengths.length) ? dimLengths[depth + 1] : 0;
				
				fillDepth(a, newAmount, depth + 1);
				if (depth == numDimensions - 2) {
					a.add((Object) null);
				}
				//System.out.println(depth + " a: " + a);
				
				in.add(a);
			}
		}
	}
	
	public Object get(int... point) {
		if (point.length != numDimensions) {
			throw new RuntimeException("Invalid array dimension size! Expected (" + numDimensions + ") but got (" +  point.length + ")");
		}
		
		int curDim = 0;
		EArrayList cur = array;
		
		while (curDim < numDimensions) {
			cur = (EArrayList) cur.get(point[curDim++]);
		}
		
		return cur.get(point[point.length -1 ]);
	}
	
	public EArrayList set(Object obj, int... point) {
		if (point.length != numDimensions) {
			throw new RuntimeException("Invalid array dimension size! Expected (" + numDimensions + ") but got (" +  point.length + ")");
		}
		
		//System.out.println("the point: " + EUtil.toString(point));
		
		int curDim = 0;
		EArrayList cur = array;
		
		while (curDim < numDimensions - 1) {
			cur = (EArrayList) cur.get(point[curDim++]);
		}
		
		//System.out.println("set: " + cur);
		cur.set(point[point.length - 1], obj);
		
		return cur;
	}
	
	public DimensionalList getList(int point) {
		if (numDimensions <= 1) { throw new RuntimeException("Dimension error! This list does not have multiple dimensions!"); }
		if (point < numDimensions - 1) {
			throw new RuntimeException("Invalid array dimension size! Expected (" + (numDimensions - 1) + ") but got (" +  point + ")");
		}
		
		return null;
	}
	
	public int getDimensionSize() { return numDimensions; }
	
	private static DimensionalList createFrom(EArrayList in, int numDimensionsIn) {
		return new DimensionalList(in, numDimensionsIn);
	}
	
}
