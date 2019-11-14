package graph;

import java.util.Collections;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished. Likewise for successors and predecessors.
 *
 *  @author JosephHayes
 */
public class UndirectedGraph extends GraphObj {


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
        _nodeKeys.remove(index);
        _vertices.remove(index);
    }

    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);

        int indexU = _nodeKeys.indexOf(u);
        int indexV = _nodeKeys.indexOf(v);


        if (!contains(u, v)) {
            if (u != v) {
                _vertices.get(indexV).add(u);
            }

            _vertices.get(indexU).add(v);
            _edges++;
            return edgeId(u, v);
        } else {
            return 0;
        }
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {

        return outDegree(v);
    }

    /** Returns the number of in and out degrees for undirected graph.
     * @param v is a vertex in this graph. */
    private int totalDegree(int v) {
        if (!contains(v)) {
            return 0;
        }
        int inCount = 0;

        for (int i = 0; i < _vertices.size(); i++) {
            if (_vertices.get(i).contains(v)) {
                inCount++;
            }
        }
        int indexV = _nodeKeys.indexOf(v);

        return _vertices.get(indexV).size();
    }

    /** Assume this means all V's pointing to this V?. */
    @Override
    public Iteration<Integer> predecessors(int v) {

        if (!contains(v)) {
            return Iteration.iteration(Collections.emptyIterator());
        }
        return successors(v);
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
            if (v >= u) {
                int swap = v;
                v = u;
                u = swap;
            }
            int one = u + v;
            int two = u + v + 1;
            int cantor = (one * two + v) / 2;
            return cantor;
        }
        return 0;
    }


}
