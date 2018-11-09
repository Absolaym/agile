package tspImproved;

import java.util.ArrayList;
import java.util.Iterator;


public class TSP1 extends TemplateTSP {

	@Override
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		return new IteratorSeq(nonVus, sommetCrt, cout);
	}

	@Override
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
		//int bound = 0;
		
		//find 
		//for (Integer sommet : nonVus)
		
		
		return 0;
	}
	
	
}
