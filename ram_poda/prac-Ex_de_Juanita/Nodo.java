//Clase  que representa los nodos del árbol de exploración del algortimo
public class Nodo implements Comparable<Nodo>{
    protected boolean sol[];
    protected int k;
    protected int diasAcum;
    protected float valorAcum;
    protected float valorOpt;

    public Nodo(int n){
        sol = new boolean[n];
        k = 0;
        diasAcum = 0;
        valorAcum = 0;
        valorOpt = 0;
    }

    //Constructor de un nuevo nodo
    public Nodo(boolean s[], int k, int dias, float valorA, float valorOpt){
        this.sol = new boolean[s.length];
        System.arraycopy(s, 0, this.sol, 0, s.length);
        this.k = k;
        this.diasAcum = dias;
        this.valorAcum = valorA;
        this.valorOpt = valorOpt;
    }

    //Método que nos permite comparar dos nodos mediante el valor optimista
    @Override
    public int compareTo(Nodo arg0) {
        if(valorOpt > arg0.valorOpt) return -1;
        else if(valorOpt == arg0.valorOpt) return 0;
        else return 1;
    }

    public String toString(){
        return "{dias:  " + diasAcum + ",opt: " + valorOpt + ", valorAcum: "  + valorAcum + "}";
    }

    
}