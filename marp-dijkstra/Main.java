public class Main {
    public static void main(String args[]){

        Heap h = new Heap();
        h = h.insertar(2);
        h = h.insertar(3);
        h = h.insertar(1);
        h = h.insertar(5);

        h = h.borrar();
    }
}
