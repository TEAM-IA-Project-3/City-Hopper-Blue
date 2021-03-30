import java.util.List;

public interface AdvancedGraphADT<T> extends GraphADT<T> {
	  List<T> getConnectedVertices(T vertex);
}

