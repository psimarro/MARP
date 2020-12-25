import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Grafo {
    protected class Arista{
        protected class Info{
            private Integer dest;
            private Float val;

            public Info(Integer dest, Float val){
                this.dest = dest;
                this.val= val;
            }     

            public Integer getDest(){
                return this.dest;
            }

            public Float getVal(){
                return this.val;
            }
        }
        private Integer orig;
        private Info info;

        public Arista(Integer a, Integer b, Float val){
            this.orig = a;
            this.info = new Info(b, val);
        }

        public Arista(Integer orig, Arista.Info pareja) {
            this.info = pareja;
            this.orig = orig;
        }  
        
        public Info getInfo(){
            return this.info;
        }

        public Integer getOrig(){
            return this.orig;
        }
    }
    private int N;
    private ArrayList<LinkedList<Arista.Info>> adj;


    public Grafo(int N){
        this.N = N;
        adj = new ArrayList<>();
        for (int i = 0; i < N;++i) adj.add(new LinkedList<>());
    }

    public void addEdge(int orig, int dest, float val){
        Arista a = new Arista(orig, dest, val);
        adj.get(orig).add(a.getInfo());
    }

    public Iterator<Arista.Info> adyacentes(int orig){
        return adj.get(orig).iterator();
    }

    public Integer getN(){
        return this.N;
    }
}
