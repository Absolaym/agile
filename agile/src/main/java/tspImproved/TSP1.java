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
		//bound = min(cout(sommetcourant, unSommetNonVu))
		int bound = findMinCostUnseen(sommetCourant, nonVus, cout);
		
		// + for each sommets from nonVus
		//min cout(sommetNonVu, the other sommets from nonVus or the warehouse)
		for (Integer sommetNonVu : nonVus){
			bound += findMinCostUnseenOrWarehouse(sommetNonVu, nonVus, cout);
		}
		
		return bound;
	}
	
	private int findMinCostUnseen(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout){
		int min = Integer.MAX_VALUE;
		for(Integer i : nonVus){
			if(i == sommetCourant) continue;
			if(cout[sommetCourant][i] < min){
				min = cout[sommetCourant][i];
			}
		}
		if(min == Integer.MAX_VALUE){
			//System.out.println("Min cost unseen did not have any results in TSP1");
			return 0;
		}
		return min;
	}
	
	private int findMinCostUnseenOrWarehouse(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout){
		int min = findMinCostUnseen(sommetCourant, nonVus, cout);
		if(cout[sommetCourant][0]<min) min = cout[sommetCourant][0];
		return min;
	}
	
}
