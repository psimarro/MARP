
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
/*
CLASE PRINCIPAL DEL PROYECTO
Esta clase representa instancias de resolución del problema de los exámenes de Juanita.
Recibe como entrada el tipo de cota para el algoritmo de entre SIN_COTA, LINEAL O VORAZ.
Tiene como atributos:
    - float cotaOpt, cotaPes: representan los valores de las cotas optimista y pesismista
        en un nivel k del árbol.
    - TIPO_COTA cota: el tipo de cota empleado
    - TreeMap<Float, Integer> copia_vi_di: este árbol ordenado empareja 
        tuplas (v_asignatura/d_asignatura, asignatura) necesarios para el ejemplo voraz,
        siendo v_asignatura el valor de la asignatura y d_asignatura los días necesarios
        para estudiarla
    - TreeMap<Float, Integer> por_procesar: subconjunto de copia_vi_di que representa las tuplas
        aún no procesadas.
    - PriorityQueue<Nodo> q: esta es la cola de prioridad de nodos para el algoritmo
*/
public class ExamJuanita {
    private float cotaOpt, cotaPes;
    private TIPO_COTA cota;
    private TreeMap<Float, Integer> copia_vi_di;
    private TreeMap<Float, Integer> por_procesar;
    private PriorityQueue<Nodo> q;

    //Constructor de la clase que resuelve el problema
    public ExamJuanita(TIPO_COTA tipo){
        cotaOpt = Float.MAX_VALUE;
        cotaPes = 0;
        cota = tipo;
        copia_vi_di = new TreeMap<>();
        por_procesar = new TreeMap<>();
        q = new PriorityQueue<>();
    }
    
    public enum TIPO_COTA{
        SIN_COTA, LINEAL, VORAZ;
    }

    /* *************************************
    // *************************************
    // **************************************/
    
    /*Método que resuelve el problema
        Recibe:
            d[], f[], v[]:
                d[i] = dias necesarios para estudiar la asignatura i
                f[i] = fecha del examen de la asignatura i
                v[i] = valor de la asignatura i si se estudia completamente.
            orden_vi_di: arbol ordenado con las tuplas (v[i]/d[i], i) siendo i una asignatura
        Devuelve:
            Solucion que agrupa:
                boolean sol[] : solución del problema donde sol[i] = true si se estudia la asignatura i
                    o false si no se estudia
                benefMejor: mejor beneficion obtenido
                nodosExplorados: los nodos explorados en la ejeución del algoritmo
    */
    public Solucion rp_examenes(Integer d[], Integer f[], Float v[], TreeMap<Float, Integer> orden_vi_di){
        //INICIALIZAMOS VARIABLES Y NODO RAIZ
        int n = d.length;
        Nodo raiz = new Nodo(n);
        Solucion s = new Solucion(n);
        calculaCotas(d, f, v, raiz.diasAcum, raiz.valorAcum, raiz.k, orden_vi_di);
        raiz.valorOpt += cotaOpt;
        s.benefMejor = cotaPes;
        q.add(raiz);
        ////////////////////////

        /* Una vez añadido el nodo raíz, vamos explorando nodos mientras el primero de la cola (maximo) 
        // sea prometedor (su valor optimista es todavía mejor
        // que el mejor beneficio alcanzado hasta el momento).*/
        while(!q.isEmpty() && q.peek().valorOpt >= s.benefMejor){
            s.nodosExplorados++;
            Nodo padre = q.poll();
            Nodo hijo = new Nodo(padre.sol, padre.k, 
                padre.diasAcum, padre.valorAcum, padre.valorOpt);
            /*
            // CASO 1: ASIGNAMOS LA ASIGNATURA SI ES FACTIBLE
            */
            if(hijo.diasAcum + d[hijo.k] < f[hijo.k]){ 
                hijo.sol[hijo.k] = true;
                hijo.valorAcum += v[hijo.k];
                hijo.diasAcum += d[hijo.k];
                hijo.k++;
                if(hijo.k == n){
                    //El nodo hijo es solución
                    if(hijo.valorAcum >= s.benefMejor){
                        s.copiaSol(hijo.sol);
                        s.benefMejor = hijo.valorAcum;
                    }
                }
                else{
                    calculaCotas(d, f, v, hijo.diasAcum, hijo.valorAcum, hijo.k, orden_vi_di);
                    hijo.valorOpt = this.cotaOpt;
                    if(hijo.valorOpt >= s.benefMejor){
                        q.add(hijo);
                        s.benefMejor = Math.max(s.benefMejor, this.cotaPes);
                    }
                }
            }
            

            /*
            CASO 2: NO ASIGNAMOS LA ASIGNATURA; SIEMPRE ES FACTIBLE
            */
            Nodo hijo2 = new Nodo(padre.sol, padre.k, 
                padre.diasAcum, padre.valorAcum, padre.valorOpt);
            hijo2.sol[hijo2.k] = false;
            hijo2.k++;
            if(hijo2.k == n){ //El nodo contiene un solución completa
                if(hijo2.valorAcum >= s.benefMejor){
                    s.copiaSol(hijo2.sol);
                    s.benefMejor = hijo2.valorAcum;
                }
            }
            else{
                calculaCotas(d, f, v, hijo2.diasAcum, hijo2.valorAcum, hijo2.k, orden_vi_di);
                hijo2.valorOpt = this.cotaOpt;
                if(hijo2.valorOpt >= s.benefMejor){
                    q.add(hijo2);
                    s.benefMejor = Math.max(s.benefMejor, this.cotaPes);
                }
            }
        }

        q = null;
        this.copia_vi_di = null;
        this.por_procesar = null;
        return s;
    } 

    /*************************************
    *************************************
    **************************************/
    
    // Método de transición que llama a los distintos métodos que calculan las cotas.
    private void calculaCotas(Integer[] d, Integer[] f, Float[] v, int diasAcum, float valorAcum, int k,
            TreeMap<Float, Integer> orden_vi_di) {
        switch(this.cota){
            case VORAZ: cotaVoraz(d, f, v, diasAcum, valorAcum, k, orden_vi_di); break;
            case LINEAL: cotaLineal(d, f, v, diasAcum, valorAcum, k, orden_vi_di); break;
            case SIN_COTA: break;
            default: break;
        }
    
    }
    
    /*************************************
    *************************************
    **************************************/
    
    /*   COTA LINEAL
    // Las cotas optimista y pesimista tienen como valor inicial el valor acumulado hasta el nivel k.
    // Para la cota optimista, se van sumando los valores de las asignaturas que quedan por procesar 
    // si son factibles con sus fechas, sin importar el resto.
    // Para la cota pesimista vamos acumulando esos valores que sumamos y vemos si las asignaturas son factibles con ese valor 
    // acumulado.
    // El coste de este algoritmo es claramente lineal.*/
    private void cotaLineal(Integer[] d, Integer[] f, Float[] v, int diasAcum, float valorAcum,
            int k, TreeMap<Float, Integer> orden_densidad_valor) {
        float opt = valorAcum, pes = valorAcum;
        int diasPes = diasAcum;
        
        for(int i = k; i < d.length; ++i){
            if(diasAcum + d[i] < f[i]){
                opt += v[i];
            }
            if(diasPes + d[i] < f[i]){
                pes += v[i];
                diasPes += d[i];
            }
        }

        this.cotaOpt = opt;
        this.cotaPes = pes;
    }

    /* *************************************
    // *************************************
    // **************************************/
    
    // COTA VORAZ:
    private void cotaVoraz(Integer[] d, Integer[] f, Float[] v, int diasAcum, float valorAcum, int k,
        TreeMap<Float, Integer> orden_vi_di) {
        /* Al igual que la cota lineal, ambos valores de las cotas inician en el valor acumulado para el nivel k
        // Primero sacamos las tuplas (v[i]/d[i], i) aún sin procesar, y los días que aún quedan disponibles (diasRestantes)
        // Además tenemos una variable diasPes que va acumulando los días que se estudian para la cota pesimista.*/
        float opt = valorAcum, pes = valorAcum;
        this.sacar_por_procesar(orden_vi_di, k);
        int diasRestantes = f[f.length-1] - diasAcum;
        int diasPes = diasAcum;
        
        /* Como TreeMap ordena ascendentemente y la solución voraz "ordena" descendentemente los valores (v[i]/d[i]) 
        // hay que ir sacando por el más grande.  */
        Map.Entry<Float, Integer> greatest = this.por_procesar.pollLastEntry();
        int asignatura = greatest.getValue();
        /* Si aún quedan días para estudiar la asignatura que acabamos de sacar, entonces, 
        // La cota optimista tomará el valor completo independientemente de si se cumplen o no las fechas, 
        // mientras que la cota pesimista sí lo hace.*/
        while(!this.por_procesar.isEmpty() && d[asignatura] < diasRestantes){
            diasRestantes -= d[asignatura]; //Restamos los días disponibles
            opt += v[asignatura]; //Se toma el valor completo para la cota opt
            if(d[asignatura] + diasPes < f[asignatura]){ //Si se van cumpliendo las fechas
                pes += v[asignatura];
                diasPes += d[asignatura];
            }
            greatest = this.por_procesar.pollLastEntry(); 
            asignatura = greatest.getValue();
        }
        //Si diasRestantes > 0 se fracciona el último valor para la cotaOpt
        opt += (diasRestantes)*greatest.getKey();
        
        this.cotaOpt = opt;
        this.cotaPes = pes;
    }
    // *************************************
    // *************************************
    // *************************************
    
    /* Funcion que nos devuelve un árbol ordenado ascendentemente 
    // con las parejas clave-valor(v_asignatura/d_asignatura, asignatura) aún sin procesar.
    // Para ello copiamos las parejas de entrada en copia_vi_di, y vamos sacando la mayor pareja.
    // Si greatest.asignatura >= k, entonces la asignatura no se ha procesado aún y la añadimos a la salida.
    // Cada acceso a los árboles tiene coste log(n) según la documentación de TreeMap,
    // por tanto el coste de este algoritmo es en el peor caso del orden n·log(n)*/
    private TreeMap<Float, Integer> sacar_por_procesar(TreeMap<Float, Integer> orden_vi_di, int k){
        this.copia_vi_di.putAll(orden_vi_di.descendingMap());

        while(!copia_vi_di.isEmpty()){
            Map.Entry<Float, Integer> greatest = copia_vi_di.pollLastEntry();
            int asignatura = greatest.getValue();
            float vi_di = greatest.getKey();
            if(asignatura >= k) por_procesar.put(vi_di, asignatura);
        }

        return por_procesar;
    }
    // *************************************
    // *************************************
    // *************************************
    
    public String toString(){
        String s = "";
        switch(this.cota){
            case SIN_COTA: s += "SIN COTAS"; break;
            case VORAZ: s+= "VORAZ"; break;
            case LINEAL: s+= "LINEAL"; break;
        }
        return s;
    }
}