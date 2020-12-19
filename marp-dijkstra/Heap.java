public class Heap{

    private Integer min;
    private Heap hijo_iz;
    private Heap hijo_der;

    public Heap(int clave){
        this.min = clave;
        this.hijo_iz = null;
        this.hijo_der = null;
    }

    public Heap(){
        this.min = null;
        this.hijo_iz = null;
        this.hijo_der = null;
    }

    public Heap(Integer min, Heap izq, Heap der){
        this.min = min;
        this.hijo_iz = izq;
        this.hijo_der = der;
    }

    public Heap unir(Heap hh2){
        Heap union;
        Heap hh1 = this;

        if(hh1.vacio()  && !hh2.vacio()) union = hh2;
        else if(!hh1.vacio() && hh2.vacio()) union = hh1;
        else if(hh1.min < hh2.min){
            if(hh1.hijo_iz != null && hh1.hijo_der != null)
                union = new Heap(hh1.min, hh1.hijo_der.unir(hh2), hh1.hijo_iz);
            else
                union = new Heap(hh1.min, new Heap().unir(hh2), new Heap());
            
        }
        else{
            if(hh2.hijo_iz != null && hh2.hijo_der != null) 
                union = new Heap(hh2.min, hh2.hijo_der.unir(hh1), hh2.hijo_iz);
            else union = new Heap(hh2.min, new Heap().unir(hh1), new Heap());
        }

        return union;
    }

    public Heap insertar(int newNode){
        Heap nodoAInsertar = new Heap(newNode);

        return this.unir(nodoAInsertar);
    }

    public Heap borrar(){
        Heap result = this.hijo_iz.unir(this.hijo_der);
        return result;
    }

    public boolean vacio(){
        return this.min == null;
    }


}