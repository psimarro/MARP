import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Clase que define un grafo dirigido y valorado
 * Contiene clases anidadas que definen las aristas y su información.
 * @author Pedro Simarro Guerra
 */
public class Grafo {

    /**
     * Clase que define una arista
     */
    protected static class InfoArista{
        private Integer dest;
        private Float val;

        public InfoArista(Integer des, Float val){
            this.dest = des;
            this.val = val;
        }     

        public Integer getDest(){
            return this.dest;
        }
        public Float getVal(){
            return this.val;
        }        
        
    }
    private int N, E;//N vertices, E aristas
    private ArrayList<LinkedList<InfoArista>> adj;

    /**
     * Constructor de un grafo vacío
     * @param N  numero de nodos del vector
     */
    public Grafo(int N){
        this.N = N;
        this.E = 0;
        adj = new ArrayList<>();
        for (int i = 0; i < N;++i) adj.add(new LinkedList<>());
    }

    /**
     * Método que añade una nueva arista al grafo
     * @param orig
     * @param dest
     * @param val
     */
    public void addEdge(int orig, int dest, float val){
        InfoArista a = new InfoArista(dest, val);
        adj.get(orig).add(a);
    }
    
    /**
     * Método que devuelve un iterator de la lista de adyacentes de un vértice
     * @param orig : vertice 
     * @return Iterator<Arista.Info> : iterador del la lista de adyacentes del vertice orig
     */
    public Iterator<InfoArista> itAdyacentes(int orig){
        return adj.get(orig).iterator();
    }
    /***
     * Método getter que devuelve el número de vértices de un grafo
     * @return : numero de vertices del grafo
     */
    public Integer getN(){
        return this.N;
    }


    public void randomGraph(double prob_densidad){
        Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if(i != j && r.nextDouble() <= prob_densidad){ 
                    this.addEdge(i, j, (float)(10000*r.nextDouble()));
                    E++;
                }
            }
        }
    }

    public String toString(){
        String s = "";
        for(int i = 0; i < N; ++i){
            Iterator<InfoArista> it = this.itAdyacentes(i);
            s += String.format("(%d) : ", i);
            while(it.hasNext()){
                InfoArista info = it.next();
                s += String.format("(%d, %f), ", info.getDest(), 
                    info.getVal());
            }
            s += "\n";
        }

        s += "\n\nNumero de aristas = " + this.E;
        return s;
    }

	public int getNAristas() {
		return this.E;
	}

	public void setAristas(int cont) {
		this.E = cont;
		
	}
    
}
