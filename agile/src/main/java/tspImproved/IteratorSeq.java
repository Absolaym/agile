package tspImproved;

import java.util.Collection;
import java.util.Iterator;

public class IteratorSeq implements Iterator<Integer> {

    private Integer[] candidates;
    private int nbCandidates;
    private int[][] costs;
    private int currentVertex;

    /**
     * Creates an iterator to iterate of the collection of unseen vertexes 
     *
     * @param unseenVertexes
     * @param currentVertex
     */
    public IteratorSeq(Collection<Integer> unseenVertexes, int currentVertex, int[][] cost) {
        this.candidates = new Integer[unseenVertexes.size()];
        this.costs = cost;
        this.currentVertex = currentVertex;
        nbCandidates = 0;
        for (Integer s : unseenVertexes) {
            candidates[nbCandidates++] = s;
        }
        sortCandidates();
    }

    @Override
    public boolean hasNext() {
        return nbCandidates > 0;
    }

    @Override
    public Integer next() {
        return candidates[--nbCandidates];
    }

    @Override
    public void remove() {
    }

    /**
     * Sorts the candidates relatively to their shortest path to the current
     * vertex.
     * The vertex the most far away will be placed at the beginning of the table
     */
    private void sortCandidates() {
        int i, j, temp;
        boolean swapped;
        for (i = 0; i < candidates.length - 1; i++) {
            swapped = false;
            for (j = 0; j < candidates.length - i - 1; j++) {
                if (costs[currentVertex][candidates[j]] < costs[currentVertex][candidates[j + 1]]) {
                    temp = candidates[j];
                    candidates[j] = candidates[j + 1];
                    candidates[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }

        }
    }

}
