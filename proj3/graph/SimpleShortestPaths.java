package graph;

/** A partial implementation of ShortestPaths that contains the weights of
 *  the vertices and the predecessor edges. The client needs to
 *  supply only the two-argument getWeight method.
 *  @author JosephHayes
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /** Vertex weights. */
    private double[] _vertWeight;
    /** Predecessors edges. */
    private int[] _predecessor;

    /** The shortest paths in G from SOURCE. */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public SimpleShortestPaths(Graph G, int source, int dest) {
        super(G, source, dest);
        _vertWeight = new double[_G.vertexSize() * 2];
        _predecessor = new int[_G.vertexSize() * 2];
    }

    /** Returns the current weight of edge (U, V) in the graph. If (U, V) is
     *  not in the graph, returns positive infinity. */
    @Override
    protected abstract double getWeight(int u, int v);

    /** Returns the current weight of vertex V in the graph. If V is
     *  not in the graph, returns positive infinity. */
    @Override
    public double getWeight(int v) {
        if (_G.contains(v)) {
            return _vertWeight[v];
        }

        return Double.POSITIVE_INFINITY;
    }

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    @Override
    protected void setWeight(int v, double w) {
        _vertWeight[v] = w;
    }

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    @Override
    public int getPredecessor(int v) {
        if (_G.contains(v)) {
            return _predecessor[v];
        }
        return 0;
    }

    /** Set getPredecessor(V) to U. */
    @Override
    protected void setPredecessor(int v, int u) {
        _predecessor[v] = u;
    }
}
