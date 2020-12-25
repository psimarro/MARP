import java.util.Iterator;

public class Dijkstra {

    private Grafo grafo;
    private Float costeMin[];
    private Integer predecesor[];

    public Dijkstra(Grafo g){
        this.grafo = g;
        costeMin = new Float[g.getN()];
        predecesor = new Integer[g.getN()];
    }

    public void ejecuta(){
        MinHeap<Integer> M = new MinHeap<>();
        Iterator<Grafo.Arista.Info> it;
        int N = this.grafo.getN();
        costeMin[0] = 0f;
        predecesor[0] = null;

        for(int i = 1; i < N; ++i){
            costeMin[i] = Float.MAX_VALUE;
            predecesor[i] = 0;
        }

        it = this.grafo.adyacentes(0);
        while(it.hasNext()){
            Grafo.Arista.Info info = it.next();
            costeMin[info.getDest()] = info.getVal();
            M = M.insertar(info.getVal(), info.getDest());
        }

        for(int i = 0; i < N-1;++i){
            Integer elegido = M.peek();
            M = M.borrar();
            it = this.grafo.adyacentes(elegido);
            while(it.hasNext()){
                Grafo.Arista.Info info_arista_adj = it.next();
                int adj = info_arista_adj.getDest();
                float coste = costeMin[elegido] + info_arista_adj.getVal();

                if(coste < costeMin[adj]){
                    costeMin[adj] = coste;
                    predecesor[adj] = elegido;
                    M = M.decrecerClave(adj,
                         coste);
                }
            }
        }

    }
}