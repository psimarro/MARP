
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Clase principal del programa
 * 
 * Parsea la linea de comandos y tiene 3 modos de ejecución:
 * - Modo random: crea un grafo aleatorio con un numero de vertices dado y una densidad dada, 
 *  y ejecuta Dijkstra sobre él
 * 
 * - Modo para gráficas: crea grafos aleatorios con una densidad dada, y 
 *  guarda los tiempos de ejecución de Dijkstra en un archivo
 * 
 * - Modo desde archivo: carga un grafo definido en un archivo y ejecuta Dijkstra sobre él
 * 
 * @author Pedro Simarro Guerra
 */
public class Main {
    public static void main(String args[]) {
        Options opt = creaOpciones();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(opt, args);

            parseaCMD(line, opt);

        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método que crea las opciones del programa
     * @return las opciones del programa
     */
    public static Options creaOpciones(){
        Options opt = new Options();
        // Opciones excluyentes entre sí
        OptionGroup group = new OptionGroup();
        Option help = new Option("h", "help", false, "muestra esta ayuda");
        group.addOption(Option.builder("g")
                .desc("EJUCUCIÓN PARA GRÁFICAS: \n"
                        + "Genera archivos de prueba para las gráficas usando grafos aleatorios con misma densidad para todos\n"
                        + "(usando [-d]) o con vértices fijos pero con densidad variable (con [-v]).\n"
                        + "Los resultados se guardan en un archivo dado en la opción [-o]\n")

                .build());
        group.addOption(Option.builder("i").hasArg().argName("in_file")
                .desc("EJECUCIÓN DESDE ARCHIVO:\nEjecuta Dijkstra para un grafo definido en el archivo in_file")
                .build());
        group.addOption(Option.builder("r").hasArg(false)
                .desc("EJECUCIÓN RANDOM:\nEjecuta una instacia de Dijkstra sobre un grafo aleatorio con [-v] vertices"
                        + " y [-d] densidad")
                .build());
        group.addOption(help);
        opt.addOptionGroup(group);

        // Opciones adicionales
        opt.addOption(Option.builder("o").hasArg().argName("out_file")
                .desc("Guarda los valores de tiempo de las gráficas en out_file").build());
        opt.addOption(Option.builder("d").longOpt("densidad").hasArg().argName("disp")
                .desc("define la densidad para grafo aleatorios").build());
        opt.addOption(Option.builder("v").hasArg().argName("vert")
                .desc("define el numero de vertices para ejecución random").build());

        return opt;
    }

    /**
     * 
     * @param line la linea de comandos del programa con los argumentos
     * @param opt las opciones del programa
     * @throws ParseException si existe algún error a la hora de parsear la linea de comandos
     */
    public static void parseaCMD(CommandLine line, Options opt) throws ParseException {
        if (line.hasOption("g")) { //Modo para gráficas
            if(line.hasOption("-o")){
                try {
                    if (line.hasOption("d") && !line.hasOption("-v")){
                        graficas(Float.parseFloat(line.getOptionValue("d")), line.getOptionValue("o"), true);
                    }
                    else if(line.hasOption("-v") && !line.hasOption("-d")){
                        graficas(Integer.parseInt(line.getOptionValue("v")), line.getOptionValue("o"), false);
                    }
                    else
                        throw new ParseException("Para opción -g, añadir -d ó -v con -o"); 
                } catch (NumberFormatException n) {
                    throw new ParseException("añade un valor correcto de densidad\n");
                }
                
            }
            else
                throw new ParseException("Para opción -g, añadir -d ó -v con -o"); 
             
            
        } 
        else if(line.hasOption("i")){ //Modo desde archivo
            ejecutaArchivo(line.getOptionValue("i"));
        }
        else if(line.hasOption("r")){ //Modo random
            if (line.hasOption("d") && line.hasOption("v")){
                try {
                    random(Float.parseFloat(line.getOptionValue("d")), Integer.parseInt(line.getOptionValue("v")));
                } catch (NumberFormatException n) {
                    throw new ParseException("añade un valor correcto de densidad o de vértices");
                }
            }
            else throw new ParseException("Para opción -r, añadir -d, -v");
        }
        else if (line.hasOption("h")) { //Ayuda
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java Main", opt, true);
        }
        else {
            throw new ParseException("Argumentos inválidos, use -h para ver la ayuda.");
        }
    }

    /**
     * Crea un grafo aleatorio, ejecuta Dijkstra, y muestra los resultados.
     * @param densidad La densidad del grafo aleatorio a crear
     * @param vertices El número de vertices del grafo dado
     */
    private static void random(Float densidad, Integer vertices){
        Grafo g = new Grafo(vertices);
        g.randomGraph(densidad);
        System.out.println("Creando grafo con densidad " + densidad
                + vertices + " vertices ...");
    
        Dijkstra dij = new Dijkstra(g);
        System.out.println(g);
        double elapsed = ejecutaDijkstra(dij);
        System.out.println(dij);
        System.out.println(String.format("Tiempo transcurrido: %f milisegundos", elapsed));
        System.out.println();
    }

    /**
     * Lee un archivo, carga un grafo, ejecuta Dijkstra y muestra los resultados
     * @param out_file el archivo donde se encuentra el grafo definido
     */
    private static void ejecutaArchivo(String out_file) {
        File output = new File(out_file);
        try {
            BufferedReader br = new BufferedReader(new FileReader(output));

            int N = Integer.parseInt(br.readLine());
            String linea;
            int cont = 0;

            Grafo g = new Grafo(N);
            while((linea = br.readLine()) != null && cont < (N*(N-1))){
                String tokens[] = linea.split(" ");

                g.addEdge(Integer.parseInt(tokens[0]), 
                          Integer.parseInt(tokens[1]),
                          Float.parseFloat(tokens[2]));
                cont++;
            }
            g.setAristas(cont);

            Dijkstra dij = new Dijkstra(g);
            System.out.println(g);
            double elapsed = ejecutaDijkstra(dij);
            System.out.println(dij);
            System.out.println(String.format("Tiempo transcurrido: %.3f milisegundos", elapsed));
            br.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    /**
     * En este modo, se crean grafos aleatorios con una disperisión dada y se guardan los tiempos de 
     * ejecución de Dijkstra en el archivo de salida dado.
     * @param entrada densidad o vertices de los grafos aleatorio
     * @param out_file archivo de salida donde se guardaran los tiempos de ejecución
     * @param modo true si es densidad fija y vertices variables, y false si es densidad variable y vertices fijos
     */
    private static void graficas(float entrada, String out_file, boolean modo) {

        File output = new File(out_file);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            float i = 0, init = 0, j = 0, N = 0;
            if(modo){ //densidad fija, vertices variables
                init = 500;
                j = 500;
                N = 8500;
                System.out.println("Creando grafos con densidad " + entrada
                        + " de 500 a 8000 vertices ...");
            }
            else { //densidad variable, vertices fijos
                init = 0.1f;
                j = 0.1f;
                N = 1f;
                System.out.println("Creando grafos con densidad de 0.1 a 1" 
                        + " de " + (int)entrada + " vertices ...");
            }
            
            for(i = init; i<N; i=i+j){
                Grafo g = modo ? new Grafo((int) i) : new Grafo((int) entrada);
                if(modo) g.randomGraph(entrada); else g.randomGraph(i);
                
                System.out.print((modo ? "V=" : "D=")+ i + ", ");
                System.out.println("A=" + g.getNAristas() + ": ");
                
                double medidaFinal = 0;
                for(int k = 0; k<3;++k){
                    System.out.print(k+1 + " ... ");
                    medidaFinal += ejecutaDijkstra(new Dijkstra(g));
                }
                System.out.println();
                medidaFinal /= 3;

                if(modo) bw.write(i + " " + medidaFinal+ "\n");
                else bw.write(g.getNAristas() + " " + medidaFinal + "\n");
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    /**
     * Método que ejecuta una instancia de Dijkstra y recupera el tiempo de ejecución.
     * @param d Instanca de Dijkstra que va a ejecutarse
     * @return tiempo en milisegundos de la ejecución
     */
    private static double ejecutaDijkstra(Dijkstra d){
        double start = System.nanoTime();
        d.ejecuta();
        double end = System.nanoTime();
        return (double) (end-start)/(Math.pow(10, 6));
    }
}
