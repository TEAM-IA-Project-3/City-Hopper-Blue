import java.util.List;

public interface BackendInterface {
  List<String> getCities(String cityName);
  List<String> getCitiesWithinDistance(String cityName, int distance);
  String getShortestPath(String city1, String city2);
  String getFurthestCity(String cityName);
  List<RouteInterface> getPath(List<String> waypoints);
}