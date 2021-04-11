public class Route implements RouteInterface {
  private String[] endpoints;
  private int length;

  public Route(String city1, String city2, int length) {
    if (city1.compareTo(city2) <= 0) {
      endpoints = new String[] {city1, city2};
    } else {
      endpoints = new String[] {city2, city1};
    }
    this.length = length;
  }

  @Override
  public String[] getEndpoints() {
    return endpoints;
  }
  @Override
  public int getLength() {
    return length;
  }

  @Override
  public String toString() {
    return endpoints[0] + " to " + endpoints[1] + " (" + length + ")";
  }

  @Override
  public int compareTo(RouteInterface other) {
    if (length == other.getLength()) {
      return endpoints[0].compareTo(other.getEndpoints()[0]);
    } else {
      return length - other.getLength();
    }
  }
}
