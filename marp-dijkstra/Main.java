
public class Main {
    public static void main(String args[]){
        
        Grafo g = new Grafo(5);
        g.addEdge(0, 1, 50);
        g.addEdge(0, 2, 30);
        g.addEdge(0, 3, 100);
        g.addEdge(0, 4, 10);
        g.addEdge(4, 3, 10);
        g.addEdge(3, 2, 50);
        g.addEdge(3, 1, 20);
        g.addEdge(2, 1, 5);
        Dijkstra d = new Dijkstra(g);
        d.ejecuta();
    }
}
