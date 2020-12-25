public class MinHeap<T>{

    private Float prioridad; //less priority means more important
    private T clave;
    public MinHeap<T> hijo_iz;
    private MinHeap<T> hijo_der;

    public MinHeap(){
        this.prioridad = null;
        this.clave = null;
        this.hijo_iz = null;
        this.hijo_der = null;
    }

    public MinHeap<T> unir(MinHeap<T> hh2){
        MinHeap<T> union;
        MinHeap<T> hh1 = this;

        if(hh1.vacio() && !hh2.vacio()) union = hh2;
        else if(!hh1.vacio() && hh2.vacio()) union = hh1;
        else{
            if(hh1.prioridad < hh2.prioridad){
                
                union = new MinHeap<T>();
                union.clave = hh1.clave;
                union.prioridad = hh1.prioridad;
                union.hijo_der = (hh1.hijo_iz != null) ? hh1.hijo_iz : new MinHeap<T>();
                union.hijo_iz = (hh1.hijo_der != null) ? hh1.hijo_der.unir(hh2)
                                        : new MinHeap<T>().unir(hh2);
                
            }
            else if(hh1.prioridad > hh2.prioridad){
                    union = new MinHeap<T>();
                    union.clave = hh2.clave;
                    union.prioridad = hh2.prioridad;
                    union.hijo_der = (hh2.hijo_iz != null) ? hh2.hijo_iz : new MinHeap<T>();
                    union.hijo_iz = hh2.hijo_der != null ? hh2.hijo_der.unir(hh1)
                                            : new MinHeap<T>().unir(hh1);
                    
            }
            else union = null; 
        }

        return union;
    }

    public MinHeap<T> insertar(Float prio, T newKey){
        MinHeap<T> nodoAInsertar = new MinHeap<T>();
        nodoAInsertar.clave = newKey;
        nodoAInsertar.prioridad = prio;

        return this.unir(nodoAInsertar);
    }
    
    
    public MinHeap<T> borrar(){
        if(this.hijo_iz != null && this.hijo_der != null)
            return this.hijo_iz.unir(this.hijo_der);
        else if(this.hijo_iz == null) return this.hijo_der;
        else return this.hijo_iz;
    }

    public MinHeap<T> decrecerClave(T clave, Float newPrio){

        MinHeap<T> hClave = this.buscarClaveYCorta(clave);
        if(hClave == this){
            hClave.prioridad = newPrio;
            return hClave;
        }
        else return this.unir(hClave);
    }

    private MinHeap<T> buscarClaveYCorta(T clave){
        MinHeap<T> found = null;
        
        if(this.clave.equals(clave)){
            found = this;
        }
        if(found == null && this.hijo_der != null && !this.hijo_der.vacio()){
            if(this.hijo_der.clave.equals(clave)){
                found = this.hijo_der;
                this.hijo_der = null;
            }
            else found = this.hijo_der.buscarClaveYCorta(clave);
        }
        if(found == null && this.hijo_iz != null && !this.hijo_iz.vacio()){
            if(this.hijo_iz.clave.equals(clave)){
                found = this.hijo_iz;
                this.hijo_iz = null;
            }
            else found = this.hijo_iz.buscarClaveYCorta(clave);
        }
            
        return found;
    }

    public boolean vacio(){
        return this.clave == null;
    }

    public T peek(){
        return this.clave;
    }
}