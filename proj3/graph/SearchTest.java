package graph;

import org.junit.Test;

import java.util.ArrayList;

/** Unit tests for the Graph class.
 *  @author JosephHayes
 */

public class SearchTest {

    @Test
    public void bftTest() {

        GraphObj p = new DirectedGraph();
        ArrayList<Integer> travList = new ArrayList<>();

        addNodes(p, 100);
        fillEdges(p, 1, 1, 12, 0);
        fillEdges(p, 2, 3, 12, 0);
        fillEdges(p, 10, 1, 12, 10);
        for (int i = 1; i < p.vertexSize(); i++) {
            travList.add(i);
        }
        Traversal bft = new BreadthFirstTraversal(p);
        Traversal dft = new BreadthFirstTraversal(p);
        bft.traverse(travList);
        dft.traverse(travList);
    }

    /** Adds LIMIT number of vertices to G. */
    private void addNodes(Graph g, int limit) {
        for (int test = 0; test < limit; test++) {
            g.add();
        }
    }
    /** Removes a range of vertices*/
    private void removeVertexRange(Graph graph, int start, int end) {
        for (int i = start; i <= end; i++) {
            graph.remove(i);
        }
    }

    /** Adds edges to VERTEX from MIN to MAX. Skips vertex skip, 0
     * if skip none */
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
}
