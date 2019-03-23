package com.ia.fib.centrales;
import Utils.OperatorsEnum;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SuccessorsHC implements SuccessorFunction {
    //static OperatorsEnum CHOSEN_OPERATOR;
    public List getSuccessors(Object state) {
        ArrayList<Successor> childrenStates = new ArrayList<>(); //la función de evaluación recorrera esta lista (creo)
        Board board = (Board) state;
        Board successorBoard = new Board(board);
        for (int i = 0; i < board.getSolutionSize(); ++i) {
            for (int j = 0; j < board.getCentralSize(i); ++j) { //en este for intentare intercambiar clientes
                for (int k = 0; k < board.getSolutionSize(); ++k) {
                    if (k > i) {
                        for (int l = 0; l < board.getCentralSize(k); ++l) {
                            if (successorBoard.intercambiarCliente(j, l, i, k)) {
                                childrenStates.add(new Successor("Intercambiado cliente " + j + " (Central " + i + ") con cliente "  + l + " (Central " + k +")", successorBoard));
                                successorBoard = new Board(board);
                            }
                        }
                    }
                }
            }

            successorBoard = new Board(board);
            Vector<Integer> aux = successorBoard.getClientesNoAssignados();
            for (int h = 0; h < aux.size(); ++h) { //en este intento meter clientes no asignados
                if (successorBoard.asignarClienteNoGarantizado(h, i)) {
                    childrenStates.add(new Successor("asignado cliente desasignado a " + i, successorBoard));
                    successorBoard = new Board(board);
                }
            }
        }
        return childrenStates;
    }
}
