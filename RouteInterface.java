public interface RouteInterface extends Comparable<RouteInterface> {
  String[] getEndpoints();
  int getLength();
  String toString();
}
