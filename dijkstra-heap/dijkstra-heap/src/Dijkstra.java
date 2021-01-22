import java.util.Iterator;

/**
 * Clase que resuelve el problema de caminos mínimos de un grafo usando el algoritmo
 * de Dijkstra con montículo.
 * @author Pedro Simarro Guerra
 *  */
public class Dijkstra {
    
    /**
     * Atributos de la clase
     * Grafo grafo: grafo sobre el que se va aplicar el algoritmo
     * Float costeMin[]: vector de costes minimos que indica el coste minimo para llegar
     *                  vertice i desde el vertice 0.
     * Integer predecesor[] : vertice predecesor en el camino minimo del vertice i
     */
    private Grafo grafo;
    private Float costeMin[];
    private Integer predecesor[];

    /**
     * Constructora de un nuevo problema
     * @param g : grafo sobre el que se va a aplicar el algoritmo.
     */ 
    public Dijkstra(Grafo g){
        this.grafo = g;
        costeMin = new Float[g.getN()];
        predecesor = new Integer[g.getN()];
    }

    /**
     * Método principal que resuelve el problema usando el algoritmo de Dijkstra
     * Rellena los vectores de costesMin y predecesor.
     */
    public void ejecuta(){
        //Se inicializa las variables del problema 
        Iterator<Grafo.InfoArista> it;
        int N = this.grafo.getN();
        MinHeap M = new MinHeap(N);

        //Se inicializa el resultado del vertice 0 (inicio) 
        costeMin[0] = 0f;
        predecesor[0] = null;

        //Se inicializan los resultados para el resto de vertices
        for(int i = 1; i < N; ++i){
            costeMin[i] = Float.MAX_VALUE;
            predecesor[i] = -1;
        }

        //Las aristas directas del vertice 0 a sus adyacentes,
        // actualizan los resultados de estos
        it = this.grafo.itAdyacentes(0);
        while(it.hasNext()){
            Grafo.InfoArista a = it.next();
            costeMin[a.getDest()] = a.getVal();
            predecesor[a.getDest()] = 0;
            M.insertar(a.getVal(), a.getDest());
        }

        while(!M.vacio()){
            Integer elegido = M.peek();
            M.borrar();
            
            if(elegido != null){
                it = this.grafo.itAdyacentes(elegido);
                while(it != null && it.hasNext()){
                    Grafo.InfoArista arista_adj = it.next();
                    int adj = arista_adj.getDest();
                    float coste = costeMin[elegido] + arista_adj.getVal();
    
                    if(coste < costeMin[adj]){
                        costeMin[adj] = coste;
                        predecesor[adj] = elegido; 
                        modifica(M, adj, coste);
                    }
                }
            }
            
        }

    }

    private void modifica(MinHeap M, Integer adj, float coste){
        if(!M.decrecerClave(adj, coste)){
            M.insertar(coste, adj);
        }
    }

    public String toString(){
        String s = "";

        s += "Costes mínimos:\n";
        for(int i = 0; i < grafo.getN(); ++i){
            s += String.format("   (%d) : %s\n", i, 
            (costeMin[i] == Float.MAX_VALUE) ? "none" : String.valueOf(costeMin[i]));
        }
        s += "----\nCaminos:\n";
        for(int i = 1; i < grafo.getN(); ++i)
            s += String.format(" (%d): %s\n", i, 
                predecesor[i] != -1 ? this.printCaminos(predecesor[i]) + "," + i  
                    : "sin camino");
        
        return s;
    }

    private String printCaminos(int i){
        String s = "";

        if(i == -1) s += "-1";
        else if (i == 0) s += "0";
        else s += printCaminos(predecesor[i]) + "," + i;

        return s;
    }
}