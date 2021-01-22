import java.util.LinkedList;

/**
 * Clase que define montículos de mínimos
 * @author Pedro Simarro Guerra
 * @param T tipo de los datos que se van a guardar en el móntículo
 */

public class MinHeap{

    static class Arbol{
        private Integer item;
        private Float prioridad;
        private Arbol hijo_iz, hijo_der, padre;


        public Arbol(){
            this.item = null;
            this.hijo_der = null;
            this.hijo_iz = null;
            this.padre = null;
        }

        public boolean vacio(){
            return item == null && hijo_iz == null 
            && hijo_der == null && padre == null;
        }
    }


    private Arbol min;
    private LinkedList<Arbol> posiciones;

    /**
     * Constructor de un montículo vacío
     * @param N : numero maximos de elementos en el montículo
     */
    public MinHeap(int N){
        this.posiciones = new LinkedList<>();
        for(int i = 0; i < N; ++i) this.posiciones.add(null);
        min = null;
    }

    
    /** 
     * Método que une dos arboles. Se unen los arboles t1 y t2 y se devuelve el resultado
     * @param t1
     * @param t2
     * @return Arbol : resultado de la unión
     */
    public Arbol unir(Arbol t1, Arbol t2){
        Arbol union = null;
        

        if((t1 == null || t1.vacio()) && (t2 != null && !t2.vacio())){
            union = t2;
            union.padre = t2.padre;
        } 
        else if((t1 != null && !t1.vacio()) && (t2 == null || t2.vacio())){
            union = t1;
            union.padre = t1.padre;
        }
        else if(t1 == null && t2 == null) union = new Arbol(); //la union es un arbol vacio
        else if(t1.prioridad < t2.prioridad){
            
            union = new Arbol();
            union.item = t1.item;
            union.prioridad = t1.prioridad;
            
            union.hijo_der = (t1.hijo_iz != null) ? t1.hijo_iz : new Arbol();
            
            union.hijo_iz = (t1.hijo_der != null) ? this.unir(t1.hijo_der, t2) 
                                    : this.unir(new Arbol(), t2);
            
            union.padre = t1.padre; 
        }
        else if(t1.prioridad >= t2.prioridad){
            union = new Arbol();
            union.item = t2.item;
            union.prioridad = t2.prioridad;
            
            union.hijo_der = (t2.hijo_iz != null) ? t2.hijo_iz : new Arbol();
            
            union.hijo_iz = (t2.hijo_der != null) ? this.unir(t2.hijo_der, t1) 
                                    : this.unir(new Arbol(), t1);
            union.padre = t2.padre;  
        }
        else union = new Arbol();

        if(!union.vacio()) this.actualizaPosiciones(union);
        if(union.hijo_iz != null && !union.hijo_iz.vacio()) union.hijo_iz.padre = union;
        if(union.hijo_der != null && !union.hijo_der.vacio()) union.hijo_der.padre = union;

        return union;
    }

    private void actualizaPosiciones(Arbol t){
        if(t.item < this.posiciones.size())
            this.posiciones.remove((int) t.item); 
        this.posiciones.add(t.item, t);
    
        if(t.hijo_der != null && !t.hijo_der.vacio()){
            if(t.hijo_der.item < this.posiciones.size())
                this.posiciones.remove((int) t.hijo_der.item); 
            this.posiciones.add(t.hijo_der.item, t.hijo_der);
        }
    
        if(t.hijo_iz != null && !t.hijo_iz.vacio()){
            if(t.hijo_iz.item < this.posiciones.size())
                this.posiciones.remove((int) t.hijo_iz.item); 
            this.posiciones.add(t.hijo_iz.item, t.hijo_iz);
        }
    }

    /**
     * Método que inserta un nuevo elemento en el mónticulo que llama a insertar.
     * Usa el método unir y devuelve el resultado de la inserción.
     * @see #unir(MinHeap)
     * @param clave : clave del elemento newItem.
     *               Cuanto más baja, más cerca de la raíz se encontrará el nuevo elemento.
     * @param newItem ; Elemento ntero a insertar
     */
    public void insertar(Float clave, Integer newItem){
        Arbol nodoAInsertar = new Arbol();
        nodoAInsertar.item = newItem;
        nodoAInsertar.prioridad = clave;

        this.min = this.unir(this.min, nodoAInsertar);
    }
    
    
    
    /** 
     * Método que borra el mínimo del montículo y devuelve el resultado. 
     * @see #unir(MinHeap)
     * @return MinHeap<T> : resultado del borrado del minimo
     */
    public void borrar(){
        int posicion_borrada = min.item;
        this.posiciones.remove(posicion_borrada);
        this.posiciones.add(posicion_borrada, null);
        this.min = this.unir(min.hijo_iz, min.hijo_der);
    }

    
    /** 
     * Método que busca un elemento del mónticulo y decrece su clave.
     * Si encuentra el elemento, corta el monticulo en ese elemenento cambia la clave, y une el corte al monticulo anterior.
     * Si no lo encuentra devuele nulo.
     * @param item : elemento al que se le decrece la clave
     * @param newKey : nueva clave
     * @return MinHeap<T>: montículo resultante de la operación si existe, nulo en caso contrario
     */
    public boolean decrecerClave(Integer item, Float newKey){

        Arbol arbol_a_cambiar = this.posiciones.get(item);
        if(arbol_a_cambiar != null){
            arbol_a_cambiar.item = item;
            arbol_a_cambiar.prioridad = newKey;
            arbol_a_cambiar.padre = null;

            Arbol padre = arbol_a_cambiar.padre;
            //Cortamos
            if(padre != null){ // arbol_a_cambiar no es el minimo
                if(padre.hijo_iz == arbol_a_cambiar) padre.hijo_iz = null;
                else if(padre.hijo_der == arbol_a_cambiar) padre.hijo_der = null;
                this.min = this.unir(this.min, arbol_a_cambiar);
            }

            return true;
        }
        else return false;

    }

    

    
    /** 
     * Si el item es nulo, significa que el monticulo llamante es vacio.
     * @return boolean
     */
    public boolean vacio(){
        return min.vacio();
    }

    
    /** 
     * Devuelve el minimo del montículo llamante.
     * @return T
     */
    public Integer peek(){
        return min.item;
    }
}