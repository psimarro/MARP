
// Clase que construye threads para una ejecución del problema.
// Recupera el tiempo transcurrido por el algoritmo de r&p y los nodos explorados.
public class MyThread extends Thread{

    ExamJuanita ex;
    Solucion s;
    Main.SetDatos e;
    long timeElapsed;
    float t_nodo;
    public MyThread (ExamJuanita ex, Main.SetDatos e){
        this.ex = ex;
        this.e = e;
    }

    public void run(){
        System.out.println("Ejecutando ejemplo: " + ex + "...");
        long init = System.currentTimeMillis();
        s = ex.rp_examenes(e.d, e.f, e.v, e.vi_di);
        this.timeElapsed = System.currentTimeMillis() - init;//en ms
        this.t_nodo = (float)timeElapsed/s.nodosExplorados; //en ms
    }

    public Solucion getSol(){
        return this.s;
    }

    public String toString(){
        return ex + " ha tardado " + timeElapsed + " ms, " + s + 
            ", t/nodo = " + this.t_nodo*1000 + " ns";
    }
}
