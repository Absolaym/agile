package tspImproved;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq implements Iterator<Integer> {

    private Integer[] candidats;
    private int nbCandidats;
    private int[][] couts;
    private int sommetCourant;

    /**
     * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
     *
     * @param nonVus
     * @param sommetCrt
     */
    public IteratorSeq(Collection<Integer> nonVus, int sommetCrt, int[][] cout) {
        this.candidats = new Integer[nonVus.size()];
        this.couts = cout;
        this.sommetCourant = sommetCrt;
        nbCandidats = 0;
        for (Integer s : nonVus) {
            candidats[nbCandidats++] = s;
        }
        sortCandidates();
    }

    @Override
    public boolean hasNext() {
        return nbCandidats > 0;
    }

    @Override
    public Integer next() {
        return candidats[--nbCandidats];
    }

    @Override
    public void remove() {
    }

    private void sortCandidates() {
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < candidats.length - 1; i++) {
            swapped = false;
            for (j = 0; j < candidats.length - i - 1; j++) {
                if (couts[sommetCourant][candidats[j]] < couts[sommetCourant][candidats[j + 1]]) {
                    temp = candidats[j];
                    candidats[j] = candidats[j + 1];
                    candidats[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }

        }
    }

}
