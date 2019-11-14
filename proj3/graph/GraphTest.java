package graph;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author JosephHayes
 */
public class GraphTest {


    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void smallestVertexTest() {
        GraphObj x = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
        assertEquals(0, x.vertexSize());
        x._nodeKeys.add(1);
        x._nodeKeys.add(2);
        x._nodeKeys.add(4);
        x._nodeKeys.add(5);
        x._nodeKeys.add(6);
        assertEquals(3, x.smallestVertex());
        assertEquals(5, x.vertexSize());
        assertEquals(6, x.maxVertex());

        x._nodeKeys.clear();
        x._nodeKeys.add(0);
        x._nodeKeys.add(1);
        x._nodeKeys.add(2);
        x._nodeKeys.add(3);
        x._nodeKeys.add(4);
        x._nodeKeys.add(5);
        assertEquals(6, x.smallestVertex());
        assertEquals(5, x.vertexSize());
        assertEquals(5, x.maxVertex());

        x._nodeKeys.clear();
        x._nodeKeys.add(0);
        x._nodeKeys.add(1);
        x._nodeKeys.add(2);
        x._nodeKeys.add(3);
        x._nodeKeys.add(4);
        x._nodeKeys.add(5);
        x._nodeKeys.add(6);
        x._nodeKeys.add(8);
        x._nodeKeys.add(100);
        assertEquals(7, x.smallestVertex());
        assertEquals(8, x.vertexSize());
        assertEquals(100, x.maxVertex());
        assertTrue(x.contains(8));
        assertTrue(x.contains(100));
        assertFalse(x.contains(7));
    }

    @Test
    public void addTest() {
        GraphObj test = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
    }

    @Test
    public void addVertexTest() {
        GraphObj s = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };

        addNodes(s, 5);
        assertEquals(6, s.add());
        s._nodeKeys.remove(3);
        s._vertices.remove(3);
        assertEquals(3, s.add());
        assertEquals(7, s.add());
        s._nodeKeys.remove(1);
        s._vertices.remove(1);
        assertEquals(1, s.add());


    }

    @Test
    public void addEdgeTest() {
        GraphObj directed = new GraphObj() {
            @Override
            public boolean isDirected() {
                return true;
            }
            @Override
            public int inDegree(int v) {
                return 0;
            }
            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
        addNodes(directed, 5);
        directed.add(1, 2);
        directed.add(1, 3);
        directed.add(1, 4);
        directed.add(1, 5);

        GraphObj undirected = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
        addNodes(undirected, 5);
        undirected.add(1, 2);
        undirected.add(1, 3);
        undirected.add(1, 4);
        undirected.add(1, 5);
    }

    @Test
    public void removeVertexTest() {
        GraphObj t = new UndirectedGraph();

        addNodes(t, 5);

        t.add(1, 5);
        t.add(1, 3);
        t.add(2, 4);
        t.add(4, 5);
        t.add(3, 2);
        t.add(5, 3);


        t._nodeKeys.add(100);
        t._vertices.add(new ArrayList<>());
        t.add(100, 5);
        t.add(100, 1);
        t.add(100, 4);

        assertEquals(9, t.edgeSize());



        t.remove(100);
        assertEquals(6, t.edgeSize());




    }

    @Test
    public void removeEdgeTest() {
        GraphObj st = new UndirectedGraph();

        addNodes(st, 5);

        st.add(1, 5);
        st.add(1, 3);
        st.add(2, 4);
        st.add(4, 5);
        st.add(3, 2);
        st.add(5, 3);

        assertEquals(6, st.edgeSize());
        st._nodeKeys.add(100);
        st._vertices.add(new ArrayList<>());
        assertEquals(0, st.outDegree(100));

        st.add(100, 5);
        st.add(100, 1);
        st.add(100, 4);
        st.add(4, 100);

        assertEquals(9, st.edgeSize());
        assertEquals(3, st.outDegree(100));
        assertEquals(3, st.outDegree(1));
        assertEquals(3, st.outDegree(3));
        assertEquals(0, st.outDegree(12));




        st.remove(100, 5);

        st.remove(100, 4);
        assertEquals(1, st.outDegree(100));
        assertEquals(7, st.edgeSize());




        st.remove(100);




        assertEquals(4, st.edgeSize());

        st.remove(1);
        st.remove(2);
        st.remove(4);

        st.remove(3);





    }

    @Test
    public void vertexIterTest() {
        GraphObj x = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
        addNodes(x, 10);
        Iteration<Integer> iter = x.vertices();

        int index = 1;

        while (iter.hasNext()) {
            assertEquals(x._nodeKeys.get(index), iter.next());
            index++;
        }
    }

    @Test
    public void vertexSuccessorTest() {
        GraphObj z = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };
        addNodes(z, 10);
        z.add(1, 2);
        z.add(1, 3);
        z.add(1, 1);
        z.add(1, 9);
        z.add(1, 10);
        z.add(1, 4);
        z.add(2, 4);
        z.add(2, 9);

        Iteration<int[]> zEdges = z.edges();
        while (zEdges.hasNext()) {

            zEdges.next();

        }

        Iteration<Integer> iter = z.successors(1);

        int index = 0;
        int indexV = z._nodeKeys.indexOf(1);

        while (iter.hasNext()) {
            assertEquals(z._vertices.get(indexV).get(index), iter.next());

            index++;
        }

        Iteration<Integer> iter2 = z.successors(2);
        index = 0;
        indexV = z._nodeKeys.indexOf(2);

        while (iter2.hasNext()) {
            assertEquals(z._vertices.get(indexV).get(index), iter2.next());

            index++;
        }
    }

    @Test
    public void edgeArray() {
        GraphObj f = new GraphObj() {
            @Override
            public boolean isDirected() {
                return false;
            }

            @Override
            public int inDegree(int v) {
                return 0;
            }

            @Override
            public Iteration<Integer> predecessors(int v) {
                return null;
            }
        };

        addNodes(f, 10);
        f.add(1, 2);
        f.add(1, 3);
        f.add(1, 10);
        f.add(1, 9);
        f.add(2, 2);
        f.add(2, 4);
        f.add(3, 9);
        f.add(3, 8);
        f.add(7, 3);
        f.add(7, 4);
        f.add(10, 4);

        Iteration<int[]> iter = f.edges();
        while (iter.hasNext()) {

            iter.next();

        }
    }

    @Test
    public void undirectedTest() {
        GraphObj nondirect = new UndirectedGraph();
        addNodes(nondirect, 100);
        removeVertexRange(nondirect, 11, 99);
        fillEdges(nondirect, 1, 2, 5, 0);
        fillEdges(nondirect, 2, 1, 4, 2);
        fillEdges(nondirect, 3, 4, 4, 0);
        fillEdges(nondirect, 10, 4, 9, 0);
        fillEdges(nondirect, 4, 3, 5, 0);
        fillEdges(nondirect, 100, 3, 5, 0);

        assertEquals(18, nondirect.edgeSize());
        assertEquals(11, nondirect.vertexSize());
        assertEquals(100, nondirect.maxVertex());
        assertEquals(4, nondirect.outDegree(1));
        assertEquals(4, nondirect.inDegree(1));

        for (int x = 1; x < nondirect.vertexSize(); x++) {
            assertTrue(nondirect.contains(x));
        }

        nondirect.remove(10);
        assertEquals(12, nondirect.edgeSize());
        assertEquals(10, nondirect.vertexSize());
        assertEquals(100, nondirect.maxVertex());
        nondirect.add();
        assertEquals(12, nondirect.edgeSize());
        assertEquals(11, nondirect.vertexSize());
        assertEquals(100, nondirect.maxVertex());
        nondirect.remove(1, 2);
        assertEquals(11, nondirect.edgeSize());
        assertEquals(11, nondirect.vertexSize());
        assertTrue(nondirect.contains(3, 100));
        assertTrue(nondirect.contains(100, 3));
        assertTrue(nondirect.contains(4, 100));
        assertTrue(nondirect.contains(5, 100));
        assertTrue(nondirect.contains(3, 1));
        assertTrue(nondirect.contains(1, 3));

        Iteration<Integer> nonVertices = nondirect.vertices();
        int index = 1;
        while (nonVertices.hasNext()) {
            int test = nonVertices.next();
            assertTrue(nondirect._nodeKeys.get(index) ==  test);

            index++;
        }

        Iteration<int[]> nonSuccess = nondirect.edges();
        while (nonSuccess.hasNext()) {
            nonSuccess.next();
        }
        Traversal t = new BreadthFirstTraversal(nondirect);
        nondirect.add(1, 2);
        nondirect.add(2, 100);
        t.traverse(1);
    }

    @Test
    public void directedTest() {
        GraphObj direct = new DirectedGraph();
        addNodes(direct, 100);
        removeVertexRange(direct, 11, 99);
        fillEdges(direct, 1, 2, 5, 0);
        fillEdges(direct, 2, 1, 4, 2);
        fillEdges(direct, 3, 4, 4, 0);
        fillEdges(direct, 10, 4, 9, 0);
        fillEdges(direct, 4, 3, 5, 0);
        fillEdges(direct, 100, 3, 5, 0);
        assertEquals(20, direct.edgeSize());
        assertEquals(11, direct.vertexSize());
        assertEquals(100, direct.maxVertex());
        assertEquals(4, direct.outDegree(1));
        assertEquals(1, direct.inDegree(1));

        for (int x = 1; x <= direct.vertexSize(); x++) {
            int vertex = direct._nodeKeys.get(x);
            assertTrue(direct.contains(vertex));
        }

        direct.remove(10);
        assertEquals(14, direct.edgeSize());
        assertEquals(10, direct.vertexSize());
        assertEquals(100, direct.maxVertex());

        direct.add();
        assertEquals(14, direct.edgeSize());
        assertEquals(11, direct.vertexSize());
        assertEquals(100, direct.maxVertex());

        direct.remove(1, 2);
        assertEquals(13, direct.edgeSize());
        assertEquals(11, direct.vertexSize());

        assertFalse(direct.contains(3, 100));
        assertTrue(direct.contains(100, 3));
        assertFalse(direct.contains(4, 100));
        assertFalse(direct.contains(5, 100));
        assertFalse(direct.contains(3, 1));
        assertTrue(direct.contains(1, 3));

        Iteration<Integer> nonVertices = direct.vertices();
        int index = 1;
        while (nonVertices.hasNext()) {
            assertTrue(direct.contains(nonVertices.next()));
            index++;
        }

        Iteration<int[]> nonSuccess = direct.edges();
        while (nonSuccess.hasNext()) {

            nonSuccess.next();
        }
        Traversal t = new BreadthFirstTraversal(direct);
        t.traverse(1);
    }

    @Test (timeout = 1000)
    public void labelTest() {
        GraphObj w = new DirectedGraph();
        LabeledGraph<Integer, Integer> n = new LabeledGraph<>(w);

        assertEquals(1, n.add(100));
        assertEquals(2, n.add(3));
        assertEquals((Object) 100, n.getLabel(1));
        assertEquals((Object) 3, n.getLabel(2));
        assertEquals(7, n.add(1, 2, 22));
        assertEquals((Object) 22, n.getLabel(1, 2));
        assertEquals(2, n.getSuccessor(1, 22));

        fillEdges(n, 1, 1, 2, 0);

    }

    @Test
    public void edgeLabelTest() {

        GraphObj undirect = new UndirectedGraph();
        LabeledGraph<Integer, Integer> labeled = new LabeledGraph<>(undirect);
        addNodes(labeled, 10);
        labeled.add(2, 5);
        labeled.add(2, 3);
        labeled.add(2, 6);
        labeled.add(3, 7);
        labeled.add(3, 8);
        labeled.add(8, 1);
        labeled.add(8, 9);
        labeled.add(8, 8);
        labeled.add(1, 1);
        labeled.add(2, 2);
        labeled.add(1, 2);
        labeled.add(1, 3);
        labeled.add(1, 4);
        labeled.add(8, 10);
        labeled.add(10, 7);

        Iteration<int[]> test = labeled.edges();
        ArrayList<int[]> edges = new ArrayList<>();
        int labelID = 30;
        while (test.hasNext()) {
            int[] pair = test.next();
            int u = pair[0];
            int v = pair[1];
            labeled.setLabel(u, v, labelID);
            labelID++;
        }
    }

    @Test
    public void edgeIDTest() {

    }
    /** Adds edges to VERTEX from MIN to MAX. Skips vertex skip, 0 if skip
     * none */
    private void fillEdges(Graph g, int vertex, int min, int max, int skip) {
        g.checkMyVertex(vertex);
        g.checkMyVertex(min);
        g.checkMyVertex(max);

        for (int i = min; i <= max; i++) {

            if (i != skip) {
                g.add(vertex, i);
            }
        }
    }

    /** Removes a range of vertices*/
    private void removeVertexRange(Graph graph, int start, int end) {
        for (int i = start; i <= end; i++) {
            graph.remove(i);
        }
    }

    /** Adds LIMIT number of vertices to G. */
    private void addNodes(Graph g, int limit) {
        for (int test = 0; test < limit; test++) {
            g.add();
        }
    }
}
