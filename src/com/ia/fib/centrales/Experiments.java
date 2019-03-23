package com.ia.fib.centrales;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Random;

class Experiments {

    private static final int REPLICATIONS = 10;
    private static BufferedWriter[] bufferedWriters;

    /* Experiment 1 */
    static void operators() throws Exception {
        Board.CENRALES_NUMBERS = new int[]{5,10,25};
        Board.CLIENTES_NUMBER = 1000;
        Board.CLIENTES_PROPORTION = new double[]{.25,.3,.45};
        Board.CLIENTES_SERVICIO_GARANTIZADO = .75;

        Long time = System.currentTimeMillis();
        Random r = new Random();
        int seed = r.nextInt(10000);

        System.out.println("Semilla número " + seed);

        Board board = new Board(1234);
        Problem p = new Problem(board, new SuccessorsHC(), new Goal(), new Heuristic());
        //Problem p = new Problem(board, new SuccessorsSA(), new Goal(), new Heuristic());
        //SearchAgent searchAgent = new SearchAgent(p, new SimulatedAnnealingSearch(10000, 100, 25, 0.1));
        SearchAgent searchAgent = new SearchAgent(p, new HillClimbingSearch());

        printActions(searchAgent.getActions());
        printInstrumentation(searchAgent.getInstrumentation());
        // Print cost and information
        System.out.println();

        board.printSolution();
        //board.printAsignados();


        time = System.currentTimeMillis() - time;
        System.out.println("Total time -> " + time + "ms");
    }

    /* Experiment 2 */
    static void initialStates() throws Exception {
        System.out.println("Not implemented yet!");
    }

    /* Experiment 3 */
    static void parameters() throws Exception {
        System.out.println("Not implemented yet!");
    }

    /* Experiment 4 */
    static void increments() throws Exception {
        System.out.println("Not implemented yet!");
    }

    /* Experiment 5 */
    static void proportion() throws Exception {
        System.out.println("Not implemented yet!");
    }

    /* Experiment 6 */
    static void dataCenters() throws Exception {
        System.out.println("Not implemented yet!");
    }

    /* Experiment 7 */
    static void especial() throws Exception {
        Board.CENRALES_NUMBERS = new int[]{5,10,25};
        Board.CLIENTES_NUMBER = 1000;
        Board.CLIENTES_PROPORTION = new double[]{.25,.3,.45};
        Board.CLIENTES_SERVICIO_GARANTIZADO = .75;

        Long time = System.currentTimeMillis();

        Board board = new Board(1234);
        Problem p = new Problem(board, new SuccessorsHC(), new Goal(), new Heuristic());
        SearchAgent searchAgent = new SearchAgent(p, new HillClimbingSearch());

        printActions(searchAgent.getActions());
        printInstrumentation(searchAgent.getInstrumentation());
        // Print cost and information
        System.out.println();

        board.printSolution();
        board.printAsignados();

        time = System.currentTimeMillis() - time;
        System.out.println("Total time -> " + time + "ms");
    }

    /*----------*/

    private static void generateBufferedWriters(String filePath, String... fileNames) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path);
        bufferedWriters = new BufferedWriter[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            bufferedWriters[i] = new BufferedWriter(new FileWriter(filePath + fileNames[i] + ".txt"));
        }
    }

    private static void closeWriters() throws IOException {
        for (BufferedWriter bufferedWriter : bufferedWriters) {
            bufferedWriter.close();
        }
        bufferedWriters = null;
    }

    private static void printData(String... data) throws IOException {
        assert data.length == bufferedWriters.length;
        for (int i = 0; i < bufferedWriters.length; i++) {
            bufferedWriters[i].append(data[i]);
        }
    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        for (Object action1 : actions) {
            String action = action1.toString();
            System.out.println(action);
        }
    }
}
