import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        long startNode = g.closest(stlon, stlat);
        long destNode = g.closest(destlon, destlat);

        Map<Long, Double> distanceTo = new HashMap<>();
        Map<Long, Long> edgeTo = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        MinPQ<Node> fringe = new MinPQ<>();

        setAllDistancesToInfinityExceptStart(g.vertices(), distanceTo);
        fringe.insert(new Node(startNode, 0, g.distance(startNode, destNode)));

        while (fringe.min().id != destNode) {
            Node currentNode = fringe.delMin();
            long v = currentNode.id;

            if (visited.contains(v)) {
                continue;
            }

            visited.add(v);
            for (long adjV : g.adjacent(v)) {
                double distanceVToAdjV = g.distance(v, adjV);
                if (currentNode.distanceFromStart + distanceVToAdjV < distanceTo.get(adjV)) {
                    edgeTo.put(adjV, v);
                    distanceTo.put(adjV, currentNode.distanceFromStart + distanceVToAdjV);
                    fringe.insert(new Node(adjV, distanceTo.get(adjV), g.distance(adjV, destNode)));
                }
            }
        }

        return buildShortestPath(edgeTo, startNode, destNode);
    }

    /* Set every distance to the start to infinity except for the distance from the start. */
    private static void setAllDistancesToInfinityExceptStart(Iterable<Long> vertices,
                                                             Map<Long, Double> distanceTo) {
        for (long v : vertices) {
            distanceTo.put(v, Double.MAX_VALUE);
        }

    }

    /* Build shortest path list from filled shortest edge maps. */
    private static LinkedList<Long> buildShortestPath(Map<Long, Long> edgeTo, long startNode,
                                                      long destNode) {
        LinkedList<Long> shortestPath = new LinkedList<>();
        shortestPath.add(destNode);

        long currentNode = destNode;

        while (currentNode != startNode) {
            shortestPath.addFirst(edgeTo.get(currentNode));
            currentNode = shortestPath.getFirst();
        }

        return shortestPath;
    }

    static class Node implements Comparable<Node> {
        long id;
        double distanceFromStart;
        double distanceToDest;

        Node(long id, double distanceFromStart, double distanceToDest) {
            this.id = id;
            this.distanceFromStart = distanceFromStart;
            this.distanceToDest = distanceToDest;
        }

        @Override
        public int compareTo(Node oNode) {
            Double priority = this.distanceFromStart + this.distanceToDest;
            Double oPriority = oNode.distanceFromStart + oNode.distanceToDest;

            return priority.compareTo(oPriority);
        }
    }
}
