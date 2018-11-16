package pld.agile;

import java.util.ArrayList;

import junit.framework.TestCase;
import tspImproved.IteratorSeq;
import tspImproved.TSP1;

public class tspImprovementsTest extends TestCase {
	
	public void testIterator() {
		int nbSommets = 4;
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
    for (int i = 1; i < nbSommets; i++) {
        nonVus.add(i);
    }
    int[][] cout = new int [nbSommets][nbSommets];
    cout[0] = new int[]{0,2,10,5};
		IteratorSeq iterator = new IteratorSeq(nonVus, 0, cout);
		
		Integer[] expectedResult = new Integer[] {1,3,2};
		
		for(int i = 0; i < expectedResult.length; i++) {
			assertEquals(iterator.next(), expectedResult[i]);
		}
	}
	
	public void testBestSolution() {
		int nbSommets = 4;
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
    for (int i = 1; i < nbSommets; i++) {
        nonVus.add(i);
    }
    int[][] cout = new int [][]{{0,2,10,5}, {5,0,19,7}, {6,6,0,1},  {20,2,3,0}};
    int [] duree = new int[] {2,4,3,2};
		
    TSP1 tsp = new TSP1();
    tsp.chercheSolution(5000, nbSommets, cout, duree);
    
    int[] expectedResult = new int[] {0,1,3,2};
    for(int i = 0; i < expectedResult.length; i++) {
			assertEquals((int) tsp.getMeilleureSolution(i), expectedResult[i]);
		}

		
	}
	

}
