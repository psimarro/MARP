import java.util.Arrays;

//Clase representa la solución al problema
public class Solucion{
    protected boolean sol[];
    protected float benefMejor;
    protected int nodosExplorados;


    public Solucion(boolean s[], float mejorV, int nodos){
        sol = new boolean[s.length];
        System.arraycopy(s, 0, this.sol, 0, s.length);
        benefMejor = mejorV;
        nodosExplorados = nodos;
    }

    public Solucion(int n){
        sol = new boolean[n];
        benefMejor = 0;
        nodosExplorados = 0;
    }

	public void copiaSol(boolean[] s) {
        System.arraycopy(s, 0, this.sol, 0, s.length);
    }

    public String toString(){
        return nodosExplorados + " nodos explorados";
    }

    public boolean equals(Solucion s2){
        if(Arrays.equals(this.sol, s2.sol) && this.benefMejor == s2.benefMejor) return true;
        return false;
    }
}