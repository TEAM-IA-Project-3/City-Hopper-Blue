// --== CS400 File Header Information ==--
// Name: Jijie Zhang
// Email: jzhang998@wisc.edu
// Team: IA Blue
// Role: Backend Developer
// TA: Sid
// Lecturer: Gary
// Notes to Grader: This backend is modified from original CS400Graph.java and added several methods
//                  to realize functionalities we want. One distinctive difference is the path
//                  between two cities is two-sided which makes more sense in real world that we
//                  can travel from one to another using the same route.
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * This is the Backend class.
 */
public class Backend implements BackendInterface {
  /**
   * This realizes the functionality of taking a city and get its connected cities.
   * @param city
   * @return List<String>
   * @throws NoSuchElementException no city exist
   */
  public List<String> getCities(String city) throws NoSuchElementException {
    List<String> connect = new ArrayList<>();
    if (!containsVertex(city) || city == null) {
      throw new NoSuchElementException("city not exist");
    }
    Vertex cur = vertices.get(city);
    for (Edge each: cur.edgesLeaving) {
      connect.add(each.target.data);
    }
    return connect;
  }

  /**
   * This realizes the functionality of get cities within given distance.
   * @param cityName the starting city
   * @param distance distance in reach
   * @return list of city names
   */
  public List<String> getCitiesWithinDistance(String cityName, int distance) {
    List<String> cities = new ArrayList<>();
    for (String each: vertices.keySet()) { //loop over keys
      if (!cityName.equals(each)) { //not itself
        if (getPathCost(cityName,each) <= distance) {
          cities.add(each);
        }
      }
    }
    return cities;
  }

  /**
   * This uses dijkstras ShortestPath algorithm to get shortest path between two cities.
   * @param city1 start
   * @param city2 end
   * @return shortest path
   */
  public String getShortestPath(String city1, String city2) {
    return shortestPath(city1, city2).toString();
  }

  /**
   * This gives the furthest city from a given city
   * @param cityName start
   * @return furthest city name
   */
  public String getFurthestCity(String cityName) {
    String city = null;
    int distance = 0;
    for (String each: vertices.keySet()) {
      if (!cityName.equals(each)) {
        int cityCost = getPathCost(cityName,each);
        if (cityCost >= distance) {
          distance = cityCost; //update if find a further one
          city = each;
        }
      }
    }
    return city;
  }

  /**
   * This gives a path with given waypoints
   * @param waypoints List of city names(strings) that should pass
   * @return list of RouteInterfaces
   */
  public List<RouteInterface> getPath(List<String> waypoints) {
    List<RouteInterface> allRoutes = new ArrayList<>();
    for (int i = 0; i < waypoints.size()-1; i++) {
      //get the route from each waypoint to another
      Route route = new Route(waypoints.get(i), waypoints.get(i+1), getPathCost(waypoints.get(i),waypoints.get(i+1)));
      allRoutes.add(route);
    }
    return allRoutes;
  }


  /**
   * Vertex objects group a data field with an adjacency list of weighted
   * directed edges that lead away from them.
   */
  protected class Vertex {
    public String data; // vertex label or application specific data
    public LinkedList<Edge> edgesLeaving;

    public Vertex(String data) {
      this.data = data;
      this.edgesLeaving = new LinkedList<>();
    }
  }

  /**
   * Edge objects are stored within their source vertex, and group together
   * their target destination vertex, along with an integer weight.
   */
  protected class Edge {
    public Vertex target;
    public int weight;

    public Edge(Vertex target, int weight) {
      this.target = target;
      this.weight = weight;
    }
  }

  protected Hashtable<String, Vertex> vertices; // holds graph verticies, key=data

  /**
   * This constructs Backend with given reader.
   * @param data
   */
  public Backend(Reader data) {
    vertices = new Hashtable<>();
    RouteReaderInterface reader= new RouteReader(data);
    List<String> cities = reader.getCities();
    List<RouteInterface> routes = reader.getRoutes();
    for (String city: cities) { //add all cities
      this.insertVertex(city);
    }
    for (RouteInterface route: routes) { //add both directions
      this.insertEdge(route.getEndpoints()[0],route.getEndpoints()[1],route.getLength());
      this.insertEdge(route.getEndpoints()[1],route.getEndpoints()[0],route.getLength());
    }
  }

  /**
   * This constructs Backend with given string.
   * @param data
   * @throws FileNotFoundException
   */
  public Backend(String data) throws FileNotFoundException {
    this(new FileReader(data));
  }
  /**
   * Insert a new vertex into the graph.
   *
   * @param data the data item stored in the new vertex
   * @return true if the data can be inserted as a new vertex, false if it is
   *     already in the graph
   * @throws NoSuchElementException if data is null
   */
  public boolean insertVertex(String data) {
    if(data == null)
      throw new NoSuchElementException("Cannot add null vertex");
    if(vertices.containsKey(data)) return false; // duplicate values are not allowed
    vertices.put(data, new Vertex(data));
    return true;
  }

  /**
   * Remove a vertex from the graph.
   * Also removes all edges adjacent to the vertex from the graph (all edges
   * that have the vertex as a source or a destination vertex).
   *
   * @param data the data item stored in the vertex to remove
   * @return true if a vertex with *data* has been removed, false if it was not in the graph
   * @throws NoSuchElementException if data is null
   */
  public boolean removeVertex(String data) {
    if(data == null) throw new NoSuchElementException("Cannot remove null vertex");
    Vertex removeVertex = vertices.get(data);
    if(removeVertex == null) return false; // vertex not found within graph
    // search all vertices for edges targeting removeVertex
    for(Vertex v : vertices.values()) {
      Edge removeEdge = null;
      for(Edge e : v.edgesLeaving)
        if(e.target == removeVertex)
          removeEdge = e;
      // and remove any such edges that are found
      if(removeEdge != null) v.edgesLeaving.remove(removeEdge);
    }
    // finally remove the vertex and all edges contained within it
    return vertices.remove(data) != null;
  }

  /**
   * Insert a new directed edge with a positive edge weight into the graph.
   *
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @param weight the weight for the edge (has to be a positive integer)
   * @return true if the edge could be inserted or its weight updated, false
   *     if the edge with the same weight was already in the graph
   * @throws NoSuchElementException if either source or target or both are null
   */
  public boolean insertEdge(String source, String target, int weight) {
    if(source == null || target == null) {
      throw new NoSuchElementException("City not exist");
    }
    Vertex sourceVertex = this.vertices.get(source);
    Vertex targetVertex = this.vertices.get(target);
    if(sourceVertex == null || targetVertex == null)
      throw new NoSuchElementException("Cannot add edge with vertices that do not exist");
    if(weight < 0)
      throw new NoSuchElementException("Cannot add edge with negative weight");
    // handle cases where edge already exists between these verticies
    for(Edge e : sourceVertex.edgesLeaving)
      if(e.target == targetVertex) {
        if(e.weight == weight) return false; // edge already exists
        else e.weight = weight; // otherwise update weight of existing edge
        return true;
      }
    // otherwise add new edge to sourceVertex
    sourceVertex.edgesLeaving.add(new Edge(targetVertex,weight));
    return true;
  }

  /**
   * Remove an edge from the graph.
   *
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return true if the edge could be removed, false if it was not in the graph
   * @throws NoSuchElementException if either source or target or both are null
   */
  public boolean removeEdge(String source, String target) {
    if(source == null || target == null) throw new NoSuchElementException("Cannot remove edge with null source or target");
    Vertex sourceVertex = this.vertices.get(source);
    Vertex targetVertex = this.vertices.get(target);
    if(sourceVertex == null || targetVertex == null) throw new NoSuchElementException("Cannot remove edge with vertices that do not exist");
    // find edge to remove
    Edge removeEdge = null;
    for(Edge e : sourceVertex.edgesLeaving)
      if(e.target == targetVertex)
        removeEdge = e;
    if(removeEdge != null) { // remove edge that is successfully found
      sourceVertex.edgesLeaving.remove(removeEdge);
      return true;
    }
    return false; // otherwise return false to indicate failure to find
  }

  /**
   * Check if the graph contains a vertex with data item *data*.
   *
   * @param data the data item to check for
   * @return true if data item is stored in a vertex of the graph, false otherwise
   * @throws NoSuchElementException if *data* is null
   */
  public boolean containsVertex(String data) {
    if(data == null) throw new NoSuchElementException("Cannot contain null data vertex");
    return vertices.containsKey(data);
  }

  /**
   * Check if edge is in the graph.
   *
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return true if the edge is in the graph, false if it is not in the graph
   * @throws NoSuchElementException if either source or target or both are null
   */
  public boolean containsEdge(String source, String target) {
    if(source == null || target == null) throw new NoSuchElementException("Cannot contain edge adjacent to null data");
    Vertex sourceVertex = vertices.get(source);
    Vertex targetVertex = vertices.get(target);
    if(sourceVertex == null) return false;
    for(Edge e : sourceVertex.edgesLeaving)
      if(e.target == targetVertex)
        return true;
    return false;
  }

  /**
   * Return the weight of an edge.
   *
   * @param source the data item contained in the source vertex for the edge
   * @param target the data item contained in the target vertex for the edge
   * @return the weight of the edge (0 or positive integer)
   * @throws NoSuchElementException if edge is not in the graph
   */
  public int getWeight(String source, String target) {
    if(source == null || target == null) throw new NoSuchElementException("Cannot contain weighted edge adjacent to null data");
    Vertex sourceVertex = vertices.get(source);
    Vertex targetVertex = vertices.get(target);
    if(sourceVertex == null || targetVertex == null) throw new NoSuchElementException("Cannot retrieve weight of edge between vertices that do not exist");
    for(Edge e : sourceVertex.edgesLeaving)
      if(e.target == targetVertex)
        return e.weight;
    throw new NoSuchElementException("No directed edge found between these vertices");
  }

  /**
   * Return the number of edges in the graph.
   *
   * @return the number of edges in the graph
   */
  public int getEdgeCount() {
    int edgeCount = 0;
    for(Vertex v : vertices.values())
      edgeCount += v.edgesLeaving.size();
    return edgeCount;
  }

  /**
   * Return the number of vertices in the graph
   *
   * @return the number of vertices in the graph
   */
  public int getVertexCount() {
    return vertices.size();
  }

  /**
   * Check if the graph is empty (does not contain any vertices or edges).
   *
   * @return true if the graph does not contain any vertices or edges, false otherwise
   */
  public boolean isEmpty() {
    return vertices.size() == 0;
  }

  /**
   * Path objects store a discovered path of vertices and the overal distance of cost
   * of the weighted directed edges along this path. Path objects can be copied and extended
   * to include new edges and verticies using the extend constructor. In comparison to a
   * predecessor table which is sometimes used to implement Dijkstra's algorithm, this
   * eliminates the need for tracing paths backwards from the destination vertex to the
   * starting vertex at the end of the algorithm.
   */
  protected class Path implements Comparable<Path> {
    public Vertex start; // first vertex within path
    public int distance; // sumed weight of all edges in path
    public List<String> dataSequence; // ordered sequence of data from vertices in path
    public Vertex end; // last vertex within path

    /**
     * Creates a new path containing a single vertex.  Since this vertex is both
     * the start and end of the path, it's initial distance is zero.
     * @param start is the first vertex on this path
     */
    public Path(Vertex start) {
      this.start = start;
      this.distance = 0;
      this.dataSequence = new LinkedList<String>();
      this.dataSequence.add(start.data);
      this.end = start;
    }

    /**
     * This extension constructor makes a copy of the path passed into it as an argument
     * without affecting the original path object (copyPath). The path is then extended
     * by the Edge object extendBy.
     * @param copyPath is the path that is being copied
     * @param extendBy is the edge the copied path is extended by
     */
    public Path(Path copyPath, Edge extendBy) {
      this(copyPath.start);
      this.end = extendBy.target;
      this.distance = copyPath.distance + extendBy.weight;
      this.dataSequence = new LinkedList<>(copyPath.dataSequence);
      this.dataSequence.add(extendBy.target.data);
      //this.end.edgesLeaving.add(extendBy);
    }

    /**
     * Allows the natural ordering of paths to be increasing with path distance.
     * When path distance is equal, the string comparison of end vertex data is used to break ties.
     * @param other is the other path that is being compared to this one
     * @return -1 when this path has a smaller distance than the other,
     *         +1 when this path has a larger distance that the other,
     *         and the comparison of end vertex data in string form when these distances are tied
     */
    public int compareTo(Path other) {
      int cmp = this.distance - other.distance;
      if(cmp != 0) return cmp; // use path distance as the natural ordering
      // when path distances are equal, break ties by comparing the string
      // representation of data in the end vertex of each path
      return this.end.data.toString().compareTo(other.end.data.toString());
    }
  }

  /**
   * Uses Dijkstra's shortest path algorithm to find and return the shortest path
   * between two vertices in this graph: start and end. This path contains an ordered list
   * of the data within each node on this path, and also the distance or cost of all edges
   * that are a part of this path.
   * @param start data item within first node in path
   * @param end data item within last node in path
   * @return the shortest path from start to end, as computed by Dijkstra's algorithm
   * @throws NoSuchElementException when no path from start to end can be found,
   *     including when no vertex containing start or end can be found
   */
  protected Path dijkstrasShortestPath(String start, String end) {
    //check if contains or null
    if (!vertices.containsKey(start) || !vertices.containsKey(end) || start == null || end == null)
    {
      throw new NoSuchElementException("Fail to find a path");
    }
    PriorityQueue<Path> pq = new PriorityQueue(); //create a priority queue
    Path startPath = new Path(vertices.get(start));
    pq.add(startPath);
    Hashtable<String, Vertex> visitedVertices = new Hashtable<>(); //record visited
    //if pq not empty, loop
    while(!pq.isEmpty()) {
      Path temp = pq.remove(); //remove once visited
      if (temp.end.data.equals(end)) { //end matched, return temp(shortest path)
        return temp;
      }
      //add to visited
      if (!visitedVertices.containsKey(temp.end.data)) {
        int edgeNum = vertices.get(temp.end.data).edgesLeaving.size();
        for (int i = 0; i < edgeNum; i++)
          //pq add path, it ranks the shortest in the first because it is priority queue
          pq.add(new Path(temp,vertices.get(temp.end.data).edgesLeaving.get(i)));
        visitedVertices.put(temp.end.data,temp.end); //put vertex into visited after checked all edges
      }
    }
    return null; // does not find
    //throw new NoSuchElementException("Fail to find a path"); //throw if not returned
  }

  /**
   * Returns the shortest path between start and end.
   * Uses Dijkstra's shortest path algorithm to find the shortest path.
   *
   * @param start the data item in the starting vertex for the path
   * @param end the data item in the destination vertex for the path
   * @return list of data item in vertices in order on the shortest path between vertex
   * with data item start and vertex with data item end, including both start and end
   * @throws NoSuchElementException when no path from start to end can be found
   *     including when no vertex containing start or end can be found
   */
  public List<String> shortestPath(String start, String end) {
    Path p = dijkstrasShortestPath(start,end);
    if (p == null) {
      return new ArrayList<>();
    }
    return p.dataSequence;
  }

  /**
   * Returns the cost of the path (sum over edge weights) between start and end.
   * Uses Dijkstra's shortest path algorithm to find the shortest path.
   *
   * @param start the data item in the starting vertex for the path
   * @param end the data item in the end vertex for the path
   * @return the cost of the shortest path between vertex with data item start
   * and vertex with data item end, including all edges between start and end
   * @throws NoSuchElementException when no path from start to end can be found
   *     including when no vertex containing start or end can be found
   */
  public int getPathCost(String start, String end) {
    Path p = dijkstrasShortestPath(start, end);
    if (p == null) {
      return -1;
    }
    return p.distance;
  }

}
