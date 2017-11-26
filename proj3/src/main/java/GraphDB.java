import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, edges, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    private Map<Long, Node> nodes = new HashMap<>();

    static class Node {
        long id;
        double longitude;
        double latitude;
        Map<Long, Edge> edges = new HashMap<>();
        String name = null;

        Node(long id, double longitude, double latitude) {
            this.id = id;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    static class Edge {
        Node node;
        Map<String, String> info;

        Edge(Node node) {
            this.node = node;
        }
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> graphNodes = new HashSet<>(nodes.keySet());
        for (long v : graphNodes) {
            if (nodeHasNoNeighbours(v)) {
                removeNode(v);
            }
        }
    }

    /* Returns true if a vertex has no edges vertices. */
    private boolean nodeHasNoNeighbours(long v) {
        return nodes.get(v).edges.size() == 0;
    }

    /** Adds a vertex to the graph. */
    void addNode(long id, double longitude, double latitude){
        nodes.put(id, new Node(id, longitude, latitude));
    }

    /** Set the name of the vertex "id". */
    void setNodeName(long id, String name) {
        nodes.get(id).name = name;
    }

    /** Adds an edge between vertices v and w. */
    private void addEdge(long v, long w, Map<String, String> edgeInfo) {
        Node vNode = nodes.get(v);
        Node wNode = nodes.get(w);

        vNode.edges.put(w, new Edge(wNode));
        vNode.edges.get(w).info = edgeInfo;

        wNode.edges.put(v, new Edge(vNode));
        wNode.edges.get(v).info = edgeInfo;
    }

    /** Adds edges between the nodes in the way. */
    void addWay(Map<Long, Long> adjacentNodesOnWay, Map<String, String> wayInfo) {
        for (Long v : adjacentNodesOnWay.keySet()) {
            addEdge(v, adjacentNodesOnWay.get(v), wayInfo);
        }
    }

    /** Remove a vertex from the graph. */
    private void removeNode(long v) {
        nodes.remove(v);
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /** Returns ids of all vertices edges to v. */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).edges.keySet();
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        Node vNode = nodes.get(v);
        Node wNode = nodes.get(w);
        return Math.sqrt(
                Math.pow(vNode.longitude - wNode.longitude, 2)
                + Math.pow(vNode.latitude - wNode.latitude, 2));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        // -1 is used as the id for the reference node because all OSM node ids are positive.
        addNode(-1, lon, lat);

        double minDistance = Double.MAX_VALUE;
        long closest = -1;

        for (Long v : nodes.keySet()) {
            double currentDistance = distance(-1, v);
            if (v != -1 &&  currentDistance < minDistance) {
                minDistance = currentDistance;
                closest = v;
            }

            if (minDistance == 0) {
                break;
            }
        }

        removeNode(-1);
        return closest;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return nodes.get(v).longitude;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return nodes.get(v).latitude;
    }
}
