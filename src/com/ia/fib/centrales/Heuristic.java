package com.ia.fib.centrales;
import aima.search.framework.HeuristicFunction;

public class Heuristic implements HeuristicFunction {

    public double getHeuristicValue(Object n) {
        return ((Board) n).heuristic();
    }
}