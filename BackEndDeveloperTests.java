// --== CS400 File Header Information ==--
// Name: Jijie Zhang
// Email: jzhang998@wisc.edu
// Team: IA Blue
// Role: Backend Developer
// TA: Sid
// Lecturer: Gary
// Notes to Grader: This test uses letters to represent cities for convenience.

import org.junit.Test;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BackEndDeveloperTests {
  /**
   * This method tests that the given city(used nodes for convenience) and routes was read and added
   * properly to the graph. It contains 6 cities(nodes).
   */
  @Test
  public void testReadStartingData() {
    Backend b = new Backend(new StringReader(
        "city1, city2,length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n"+
            "C,F,9"));
    assertEquals(b.getVertexCount(), 6);
  }

  /**
   * This method tests to make sure that getShortestPath() works as expected.
   */
  @Test
  public void testGetShortestPath() {
    Backend b = new Backend(new StringReader(
        "city1, city2,length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n" +
            "C,F,9"));
    assertEquals(b.getShortestPath("A","F"),"[A, C, F]");
    assertEquals(b.getShortestPath("D","A"),"[D, E, A]");
    assertEquals(b.getShortestPath("D","C"),"[D, E, A, C]");
    assertEquals(b.getShortestPath("C","A"),"[C, A]");
    assertEquals(b.getShortestPath("F","A"),"[F, C, A]");
  }

  /**
   * This method tests to make sure that getCities() works as expected.
   */
  @Test
  public void testGetCities() {
    Backend b = new Backend(new StringReader(
        "city1, city2,length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n" +
            "C,F,9"));
    assertTrue(
        b.getCities("A").toString().contains("E") &&
            b.getCities("A").toString().contains("B") &&
            b.getCities("A").toString().contains("C") &&
            b.getCities("A").toString().contains("D"));
  }

  /**
   * This method tests to make sure that getCitiesWithinDistance() works as expected.
   */
  @Test
  public void testGetCitiesWithinDistance() {
    Backend b = new Backend(new StringReader(
        "city1, city2,length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n" +
            "C,F,9"));
    assertTrue(b.getCitiesWithinDistance("A", 4).toString().contains("E") &&
        b.getCitiesWithinDistance("A", 4).toString().contains("B") &&
        b.getCitiesWithinDistance("A", 4).toString().contains("D") &&
        b.getCitiesWithinDistance("A", 4).toString().contains("C"));
  }

  /**
   * This method tests to make sure that getFurthestCity() works as expected.
   */
  @Test
  public void testGetFurthestCity() {
    Backend b = new Backend(new StringReader(
        "city1, city2, length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n" +
            "C,F,9"));
    assertEquals(b.getFurthestCity("D"), "F");
    assertEquals(b.getFurthestCity("F"), "B");
    assertEquals(b.getFurthestCity("E"), "F");
  }

  /**
   * This method tests to make sure that getPath() works as expected.
   */
  @Test
  public void testGetPath() {
    Backend b = new Backend(new StringReader(
        "city1, city2,length\n"+
            "A,B,2\n"+
            "A,D,4\n"+
            "A,E,1\n"+
            "B,C,5\n"+
            "C,A,3\n"+
            "D,B,3\n"+
            "D,C,7\n"+
            "D,E,1\n"+
            "E,C,8\n" +
            "C,F,9"));
    List<String> stops = new ArrayList<>();
    stops.add("A");
    stops.add("F");
    stops.add("E");
    assertEquals(b.getPath(stops).toString(),"[A to F (12), F to E (13)]");
    stops.clear();
    stops.add("D");
    stops.add("C");
    stops.add("B");
    assertEquals(b.getPath(stops).toString(),"[D to C (5), C to B (5)]");
  }
}
