package tspImproved;

import java.util.ArrayList;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

    @Override
    protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
        return new IteratorSeq(nonVus, sommetCrt, cout);
    }

    @Override
    /**
     * Calcule une heuristique: une borne minimale du plus court chemin passant par les noeuds de nonVus
     * et revenant au warehouse
     * Somme de:
     * - cout minimum reliant le sommet courant aux sommets non vus
     * - pour chaque sommet non vu: cout minimum reliant ce sommet aux sommets non vus ou au warehouse
     * @return la borne minimum dans la meme unite que les couts
     */
    protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree) {
    	int bound = findMinCostUnseen(sommetCourant, nonVus, cout);

        for (Integer sommetNonVu : nonVus) {
            bound += findMinCostUnseenOrWarehouse(sommetNonVu, nonVus, cout);
        }

        return bound;
    }

    private int findMinCostUnseen(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        int min = Integer.MAX_VALUE;
        for (Integer i : nonVus) {
            if (i == sommetCourant) {
                continue;
            }
            if (cout[sommetCourant][i] < min) {
                min = cout[sommetCourant][i];
            }
        }
        if (min == Integer.MAX_VALUE) {
            return 0;
        }
        return min;
    }

    private int findMinCostUnseenOrWarehouse(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout) {
        int min = findMinCostUnseen(sommetCourant, nonVus, cout);
        if (cout[sommetCourant][0] < min) {
            min = cout[sommetCourant][0];
        }
        return min;
    }

}
