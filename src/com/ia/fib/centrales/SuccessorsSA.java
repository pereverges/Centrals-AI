package com.ia.fib.centrales;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SuccessorsSA implements SuccessorFunction{

    public List getSuccessors(Object state) {
        ArrayList<Successor> childrenStates = new ArrayList<>();
        Board board = (Board) state;

        Random random = new Random();
        Board successorBoard = new Board(board);
        int algorithm = random.nextInt(2); //operador al azar


        if (algorithm == 0) { //asignar cliente no garantizado
            int i = random.nextInt(successorBoard.getSolutionSize()); //todos los parametros serán también al azar

            int j = 0;
            int aux = successorBoard.getClientesNoAssignados().size();
            boolean empty = false;
            if (aux == 0) empty = true;
            else j = random.nextInt(aux);

            if (!empty && successorBoard.asignarClienteNoGarantizado(j,i)) {
                childrenStates.add(new Successor("asignados clientes no garantizados a " + i, successorBoard));
            }
        }
        else {
            int i = random.nextInt(successorBoard.getSolutionSize());

            int j = 0;
            int aux = successorBoard.getCentralSize(i);
            boolean empty = false;
            if (aux == 0) empty = true;
            else j = random.nextInt(aux); //petaba porque no habia nadie en la central xddd

            int k = random.nextInt(successorBoard.getSolutionSize());
                if (k>i) {
                    int l = 0;
                    aux = successorBoard.getCentralSize(k);
                    if (aux == 0) empty = true;
                    else l = random.nextInt(aux);

                    if(!empty && successorBoard.intercambiarCliente(j,l,i,k)) childrenStates.add(new Successor("intercambiado cliente de la central " + i + " a la " + k, successorBoard));
                }
        }
        return childrenStates;
    }
}
