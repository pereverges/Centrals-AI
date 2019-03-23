package com.ia.fib.centrales;
import aima.search.framework.GoalTest;

public class Goal implements GoalTest {

    public boolean isGoalState(Object state) {
        return ((Board) state).isGoal();
    }
}