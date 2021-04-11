import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.Assert.*;

public class BackEndDeveloperTests {
  /**
   * This method tests that the given data was read and added properly to the graph.
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
            "E,C,8\n"));
    assertEquals(b.getVertexCount(), 5);
  }

  /**
   * This method tests to make sure that the data is ordered correctly and that inOrderList() works properly.
   */
  @Test
  public void testShortestPath() {
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
  }

  /**
   * This method tests to make sure that the output list slicing works properly, and that if the data is overrun the
   * backend does not throw an error.
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
        b.getCities("A").toString().contains("D"));
  }

  /**
   * This method tests that the insert() method works properly.
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
        b.getCitiesWithinDistance("A", 4).toString().contains("D"));
  }

  /**
   * This method tests to make sure that the contains() and getCar() methods work properly. (They are direct calls to
   * the red/black tree's methods, but I modified the red/black tree's contains() method to add the get() method, so
   * I'm testing both here.
   */
  @Test
  public void testGetFurthestCity() {
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
    assertEquals(b.getFurthestCity("D"), "E");
  }
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
    assertEquals(b.getPath(stops).get(0).toString(),"A to F (12)");
  }
}
