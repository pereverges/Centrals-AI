package com.ia.fib.centrales;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Choose experiment:");
        System.out.println("1) Operators");
        System.out.println("2) Initial state");
        System.out.println("3) SA parameters");
        System.out.println("4) Incrementation");
        System.out.println("5) Proportions");
        System.out.println("6) Increment Data Centers");
        System.out.println("7) Special Experiment");
        System.out.println("*) Manual experiments");

        Scanner scanner = new Scanner(System.in);
        Integer option = scanner.nextInt();

        switch (option) {
            case 1:
                Experiments.operators();
                break;
            case 2:
                Experiments.initialStates();
                break;
            case 3:
                Experiments.parameters();
                break;
            case 4:
                Experiments.increments();
                break;
            case 5:
                Experiments.proportion();
                break;
            case 6:
                Experiments.dataCenters();
                break;
            case 7:
                Experiments.especial();
                break;
            default:
                System.out.println("Manual search will start:");

                System.out.print("Algorithm (HC | SA): ");
                String usedAlgorithm = scanner.next();

                System.out.print("Number of A centrals: ");
                int a = scanner.nextInt();
                System.out.print("Number of B centrals: ");
                int b = scanner.nextInt();
                System.out.print("Number of C centrals: ");
                int c = scanner.nextInt();

                Board.CENRALES_NUMBERS = new int[]{a,b,c};

                System.out.print("Number of clients: ");
                Board.CLIENTES_NUMBER = scanner.nextInt();

                System.out.print("Proportion of XG clients: ");
                double xg = scanner.nextDouble();
                System.out.print("Proportion of MG clients: ");
                double mg = scanner.nextDouble();
                System.out.print("Proportion of G clients: ");
                double g = scanner.nextDouble();

                Board.CLIENTES_PROPORTION = new double[]{xg,mg,g};

                System.out.print("Proportion of clients with guaranteed service: ");
                Board.CLIENTES_SERVICIO_GARANTIZADO = scanner.nextDouble();

                Long time = System.currentTimeMillis();
                Board board = new Board(2);

                //SuccessorsHC.CHOSEN_OPERATOR = Utils.OperatorsEnum.DESASIGNAR_CLIENTE;

                SuccessorFunction successorFunction = usedAlgorithm.equals("HC")
                        ? new SuccessorsHC()
                        : new SuccessorsSA();

                Problem p = new Problem(board, successorFunction, new Goal(), new Heuristic());
                Search alg = usedAlgorithm.equals("HC")
                        ? new HillClimbingSearch()
                        : new SimulatedAnnealingSearch(10000, 100, 25, 0.1);

                SearchAgent searchAgent = new SearchAgent(p, alg);

                printActions(searchAgent.getActions());
                printInstrumentation(searchAgent.getInstrumentation());

                // Print cost and information
                System.out.println();

                board.printSolution();
                board.printAsignados();

                time = System.currentTimeMillis() - time;
                System.out.println("Total time -> " + time + "ms");
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