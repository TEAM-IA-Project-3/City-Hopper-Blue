import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class RouteReader implements RouteReaderInterface {
  private ArrayList<String> cities;
  private ArrayList<RouteInterface> routes;

  private String csvHeader;

  public RouteReader(Reader dataIn) {
    Scanner sc = new Scanner(dataIn);
    cities = new ArrayList<>();
    routes = new ArrayList<>();
    csvHeader = sc.nextLine();

    while (sc.hasNextLine()) {
      String line = sc.nextLine().strip();
      String[] fields = line.split(",");
      if (!cities.contains(fields[0])) {
        cities.add(fields[0]);
      }
      if (!cities.contains(fields[1])) {
        cities.add(fields[1]);
      }
      routes.add(new Route(fields[0], fields[1], Integer.parseInt(fields[2])));
    }
    Collections.sort(cities);
    Collections.sort(routes);
  }

  @Override
  public List<String> getCities() {
    return cities;
  }
  @Override
  public List<RouteInterface> getRoutes() {
    return routes;
  }
}
