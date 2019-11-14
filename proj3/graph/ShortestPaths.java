package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/** The shortest paths through an edge-weighted graph.
 *  By overriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results. By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author JosephHayes
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        _fringe = new TreeSet<>(compare);

    }

    /** Heuristic comparator for TreeSet. Resolves the case that
     * V1.compareTo(V2) = 0. */
    private final Comparator<Integer> compare = new Comparator<Integer>() {
        @Override
        public int compare(Integer u, Integer v) {
            double path1 = estimatedDistance(u) + getWeight(u);
            double path2 = estimatedDistance(v) + getWeight(v);

            if (path1 > path2) {
                return 1;
            }
            if (path1 == path2) {
                return Math.abs(u - v);
            } else {
                return -1;
            }
        }
    };

    /** Initialize the shortest paths. Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        _fringe.add(getSource());

        for (Integer x : _G.vertices()) {
            setWeight(x, Double.POSITIVE_INFINITY);
        }
        setWeight(getSource(), 0);

        while (!_fringe.isEmpty()) {
            Integer vertex = _fringe.pollFirst();
            if (vertex.equals(getDest())) {
                break;
            }
            for (Integer w : _G.successors(vertex)) {
                if (getWeight(vertex) + getWeight(vertex, w) < getWeight(w)) {
                    _fringe.remove(w);
                    setWeight(w, getWeight(vertex) + getWeight(vertex, w));
                    _fringe.add(w);
                    setPredecessor(w, vertex);
                }
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;

    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;

    }

    /** Returns the current weight of vertex V in the graph. If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V. Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> sPath = new ArrayList<>();

        while (getPredecessor(v) != 0) {
            sPath.add(0, v);
            v = getPredecessor(v);

        }
        sPath.add(0, getSource());

        return sPath;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** Fringe for traversal of paths. */
    private TreeSet<Integer> _fringe;



}
