package graph;

import java.util.Collections;
import java.util.ArrayList;

/** Represents a general, unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author JosephHayes
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    /** Remove V, if present, and all adjacent edges. */
    @Override
    public void remove(int v) {
        checkMyVertex(v);

        int index = _nodeKeys.indexOf(v);

        for (int x = 1; x < _vertices.size(); x++) {
            int u = _nodeKeys.get(x);

            if (_vertices.get(x).contains(v)) {
                remove(u, v);
            }
        }
        _edges = _edges - _vertices.get(index).size();
        _nodeKeys.remove(index);
        _vertices.remove(index);
    }

    /** Returns the number of incoming edges incident to V, or 0 if V is not
     *  one of my vertices. */
    @Override
    public int inDegree(int v) {
        if (!contains(v)) {
            return 0;
        }
        int inCount = 0;

        for (int i = 0; i < _vertices.size(); i++) {
            if (_vertices.get(i).contains(v)) {
                inCount++;
            }
        }
        return inCount;
    }

    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);

        int indexU = _nodeKeys.indexOf(u);

        if (isDirected() && !contains(u, v)) {
            _vertices.get(indexU).add(v);
            _edges++;
            return edgeId(u, v);
        } else {
            return 0;
        }
    }

    /** All V's pointing to this V. */
    @Override
    public Iteration<Integer> predecessors(int v) {

        if (!contains(v)) {
            return Iteration.iteration(Collections.emptyIterator());
        }
        ArrayList<Integer> preds = new ArrayList<>();
        int listIndex;
        int vertex;

        for (ArrayList x : _vertices) {

            if (x.contains(v)) {
                listIndex = _vertices.indexOf(x);
                vertex = _nodeKeys.get(listIndex);
                preds.add(vertex);
            }
        }
        return Iteration.iteration(preds);
    }

    /** Returns a unique positive identifier for the edge (U, V), if it
     *  is present, or 0 otherwise. If edges are not removed from the graph,
     *  this value should be a small multiple of the number of the edges in
     *  the graph. Its purpose is to provide a mapping of edges to integers
     *  for use by classes such as LabeledGraph. It is the same value as that
     *  returned by add(u, v). */
    @Override
    protected int edgeId(int u, int v) {

        if (contains(u, v)) {
            int one = u + v;
            int two = u + v + 1;
            return (one * two + v) / 2;
        }
        return 0;
    }


}
