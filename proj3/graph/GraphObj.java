package graph;

import java.util.ArrayList;
import java.util.Collections;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *
 *  @author JosephHayes
 */

abstract class GraphObj extends Graph {

    /** All vertices and their adjacency lists.*/
    protected ArrayList<ArrayList<Integer>> _vertices;
    /** A list of all vertices. */
    protected ArrayList<Integer> _nodeKeys;
    /** Count of all edges in this graph. */
    protected int _edges;

    /** A new, empty Graph. */
    GraphObj() {

        _vertices = new ArrayList<>();
        _vertices.add(new ArrayList<>(Collections.emptyList()));
        _nodeKeys = new ArrayList<>();
        _nodeKeys.add(0);
        _edges = 0;
    }

    /** Returns the smallest unused vertex number. */
    protected int smallestVertex() {
        for (int vertex = 1; vertex < _nodeKeys.size(); vertex++) {
            if (_nodeKeys.get(vertex) - _nodeKeys.get(vertex - 1) > 1) {
                return vertex;
            }
        }
        return _nodeKeys.size();
    }

    /** Returns the number of vertices in me. */
    @Override
    public int vertexSize() {
        return _nodeKeys.size() - 1;
    }

    /** Returns my maximum vertex number, or 0 if I am empty. */
    @Override
    public int maxVertex() {
        return _nodeKeys.get(vertexSize());
    }

    /** Returns the number of edges in this Graph. */
    @Override
    public int edgeSize() {
        return _edges;
    }

    /** Returns true iff I am a directed graph. */
    @Override
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V, or 0 if V is not
     *  one of my vertices. */
    @Override
    public int outDegree(int v) {
        if (!contains(v)) {
            return 0;
        }
        int indexV = _nodeKeys.indexOf(v);
        return _vertices.get(indexV).size();
    }

    /** Returns the number of incoming edges incident to V, or 0 if V is not
     *  one of my vertices. */
    @Override
    public abstract int inDegree(int v);

    /** Returns true iff U is one of my vertices. */
    @Override
    public boolean contains(int u) {
        if (u > 0) {
            return _nodeKeys.contains(u);
        }
        return false;
    }

    /** Returns true iff U and V are my vertices AND I have an edge (U, V). */
    @Override
    public boolean contains(int u, int v) {
        int indexU = _nodeKeys.indexOf(u);
        int indexV = _nodeKeys.indexOf(v);


        if (contains(u) && contains(v)) {
            if (!isDirected()) {
                return _vertices.get(indexU).contains(v)
                        || _vertices.get(indexV).contains(u);
            }
            return _vertices.get(indexU).contains(v);
        }
        return false;
    }

    /** Returns a new vertex and adds it to this graph with no incident edges.
     *  The vertex number is always the smallest integer >= 1 that is not
     *  currently one of this graph's vertex numbers.  */
    @Override
    public int add() {
        if (vertexSize() == 0) {
            _nodeKeys.add(1);
            _vertices.add(new ArrayList<>());
            return 1;
        } else if (vertexSize() > 0) {
            int vertex = smallestVertex();

            if (vertex - 1 == maxVertex()) {
                _vertices.add(new ArrayList<>());
                _nodeKeys.add(vertex, vertex);
                return vertex;
            } else {
                _vertices.add(vertex, new ArrayList<>());
                _nodeKeys.add(vertex, vertex);
                return vertex;
            }
        }
        return 0;
    }

    /** Add an EDGE incident on U and V. If I am directed, the edge is
     *  directed (leaves U and enters V). Requires that U and V are my
     *  vertices. Has no effect if there is already an edge from U to
     *  V. Returns a unique positive number identifying the edge,
     *  different from those returned for any other existing edge. */
    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);

        int indexU = _nodeKeys.indexOf(u);
        int indexV = _nodeKeys.indexOf(v);


        if (!isDirected() && !contains(u, v)) {
            if (u != v) {
                _vertices.get(indexV).add(u);
            }

            _vertices.get(indexU).add(v);
            _edges++;
            return edgeId(u, v);
        }
        return 0;
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

    /** Remove edge (U, V) from this graph, if present. */
    @Override
    public void remove(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);

        if (contains(u, v)) {
            int indexU = _nodeKeys.indexOf(u);
            int indexV = _vertices.get(indexU).indexOf(v);

            _vertices.get(indexU).remove(indexV);
            _edges = _edges - 1;
        }
    }

    /** Returns an Iteration over all vertices in numerical order. */
    @Override
    public Iteration<Integer> vertices() {

        ArrayList<Integer> vertices = new ArrayList<>();

        for (int i = 1; i < _nodeKeys.size(); i++) {
            vertices.add(_nodeKeys.get(i));
        }
        return Iteration.iteration(vertices);
    }

    /** Returns an iteration over all successors of V.
     *  Empty if V is not my vertex. */
    @Override
    public Iteration<Integer> successors(int v) {
        if (!contains(v)) {
            return Iteration.iteration(Collections.emptyIterator());
        }
        int index = _nodeKeys.indexOf(v);

        return Iteration.iteration(_vertices.get(index));
    }

    /** Returns an iteration over all predecessors of V.
     *  Empty if V is not my vertex. */
    @Override
    public abstract Iteration<Integer> predecessors(int v);


    /** Returns an iteration over all edges in me. Edges are returned
     *  as two-element arrays (u, v), which are directed if the graph
     *  is. The values in the array returned by .next() may have changed
     *  on the next call of .next() (that is, .next() is free to use the same
     *  array to return all results). */
    @Override
    public Iteration<int[]> edges() {
        ArrayList<int[]> myEdges = new ArrayList<>();

        for (int x : vertices()) {

            Iteration<Integer> vertex = successors(x);
            while (vertex.hasNext()) {

                int success = vertex.next();
                int[] next = {x, success};
                myEdges.add(next);

                if (!isDirected() && success != x) {
                    int [] reverse = {success, x};
                    myEdges.add(reverse);
                }
            }
        }

        return Iteration.iteration(myEdges);
    }

    /** Throw exception if V is not one of my vertices. */
    @Override
    protected void checkMyVertex(int v) {
        if (!contains(v) || v == 0) {
            throw new IllegalArgumentException("Error vertex " + v
                    + " is not in this graph.");
        } else {
            return;
        }
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
            if (contains(v, u)) {
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
