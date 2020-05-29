import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {

    // Clase privada que representa un set de datos para el ejemplo.
    public static class SetDatos {
        protected Integer f[], d[];
        protected Float v[];
        protected int n;
        protected TreeMap<Float, Integer> vi_di;

        public SetDatos(int n){
            if(n > 0){
                f = new Integer[n];
                d = new Integer[n];
                v = new Float[n];
            }
            this.n = n;
            vi_di = new TreeMap<>();
        }
        
        public SetDatos(ArrayList<Integer> ff, ArrayList<Integer> dd, ArrayList<Float> vv){
            f = ff.toArray(new Integer[ff.size()]);
            d = dd.toArray(new Integer[dd.size()]);
            v = vv.toArray(new Float[ff.size()]);
            this.n = f.length;
            vi_di = new TreeMap<>();

            for (int i = 0; i < this.n; ++i) {
                vi_di.put(v[i] / d[i], i);
            }
        }
    }

    public static void main(String[] args) {
        SetDatos e = menu(args);
        if(e != null) ejecuta(e);
    }

    /* Método que lanza las 3 ejecuciones del problema, 
    // compara las soluciones y muestra los resultados*/
    static void ejecuta(SetDatos e){
        ExamJuanita ex1 = new ExamJuanita(ExamJuanita.TIPO_COTA.SIN_COTA);
        MyThread sin_cota = new MyThread(ex1, e);
        sin_cota.start();
        
        ExamJuanita ex2 = new ExamJuanita(ExamJuanita.TIPO_COTA.LINEAL);
        MyThread lineal = new MyThread(ex2, e);
        lineal.start();

        ExamJuanita ex3 = new ExamJuanita(ExamJuanita.TIPO_COTA.VORAZ);
        MyThread voraz = new MyThread(ex3, e);
        voraz.start();

        try {
            sin_cota.join();
            lineal.join();
            voraz.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        System.out.println(sin_cota + "\n" + lineal + "\n" + voraz);
        if(voraz.getSol().equals(lineal.getSol()) && lineal.getSol().equals(sin_cota.getSol())
            && sin_cota.getSol().equals(voraz.getSol())){
                    System.out.println("Soluciones iguales\nGenerando archivo con la solución...") ;
                    escribeResul(voraz.getSol());
            }
        else System.out.println("Soluciones diferentes");
    }

    

    // Menú de la aplicación
    static SetDatos menu(String args[]){
        SetDatos e = new SetDatos(0);
        if(args.length == 0){
            System.out.println("Debe introducir argumentos. Use -h para mostrar la ayuda");
            e = null;
        }
        else if(args[0].equals("-h")){
            String help = "";
            help += "   -h        Muestra esta ayuda";
            help += "\n   -r <n>    Crea un ejemplo de prueba con tamaño n, lo guarda en random.csv y ejecuta el algoritmo";
            help += "\n   -e FILE   Lee el arhivo FILE y ejecuta el algoritmo [.csv requerido]";
            e = null;
            System.out.println(help);
        }else{
            if(args[0].equals("-r")){
                if(args.length != 2){
                    System.out.println("Los parámetros de entrada no son correctos!\nSaliendo...");
                    e = null;
                }
                else{
                    e = generaEjemplo(Integer.parseInt(args[1]));
                }
            }
            else if(args[0].equals("-e") && args.length == 1){
                System.out.println("Debe introducir un archivo!\nSaliendo...");
                e = null;
            }
            else if(args[0].equals("-e") && args[1].endsWith(".csv"))
                e = leeArchivo(args[1]);
            else{
                System.out.println("Los parámetros de entrada no son correctos!\nSaliendo...");
                e = null;
            }  
        }
        
        return e;
    }


    // Método que genera un set de datos aleatorio, y lo guarda en un fichero random.csv
    static SetDatos generaEjemplo(int n) {
        SetDatos e = new SetDatos(n);
        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < e.n; ++i) {
            e.f[i] = 1 + r.nextInt(e.n);
            e.d[i] = 1 + r.nextInt(e.n/10); // n/10 para que haya más asignaturas posibles para elegir
            float valor = r.nextFloat() * 10; //damos un valor entre 0 y 10
            BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP); //redondeo a la centésima
            e.v[i] = bd.floatValue();
            float vi_di = e.v[i]/e.d[i];
            if(e.vi_di.containsKey(vi_di)){
                // para asegurarnos de que hay una tupla para cada asignatura
                // aunque la construcción es más costosa
                e.vi_di.put(vi_di+ 0.0001f, i); 
            } 
            else  e.vi_di.put(vi_di, i);
        }
        Arrays.parallelSort(e.f);

        File prueba = new File("random.csv");
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter(prueba));
            for (int i = 0; i < e.n; ++i) {
                bfw.write(String.format("%d, %d, %f\n", e.f[i], e.d[i], e.v[i]));
            }
            bfw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return e;
    }


    // Método que lee un archivo .csv con datos y construye el set.
    public static SetDatos leeArchivo(String archivo) {
        ArrayList<Integer> ff = new ArrayList<>();
        ArrayList<Integer> dd = new ArrayList<>();
        ArrayList<Float> vv = new ArrayList<>();
        try {
            File a = new File(archivo);
            BufferedReader bfr = new BufferedReader(new FileReader(a));
            String linea = "";
            while((linea = bfr.readLine()) != null){
                String tokens[] = linea.split(", ");
                ff.add(Integer.parseInt(tokens[0]));
                dd.add(Integer.parseInt(tokens[1]));
                tokens[2] = tokens[2].replace(',', '.');
                vv.add(Float.parseFloat(tokens[2]));
            }
            bfr.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SetDatos newSet = new SetDatos(ff, dd, vv);
        return newSet;
    }

    //Método que escribe el resultado del algoritmo
    private static void escribeResul(Solucion s) {   
        try {
            File a = new File("resul.txt");
            BufferedWriter bfr = new BufferedWriter(new FileWriter(a));
            for(int i = 0; i < s.sol.length; ++i){
                bfr.write(i + "\t" + String.valueOf(s.sol[i]));
                bfr.newLine();
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



