package com.ia.fib.centrales;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import IA.Energia.Centrales;
import IA.Energia.Clientes;
import IA.Energia.Central;
import IA.Energia.Cliente;

public class Board {

    static int[] CENRALES_NUMBERS;
    static int CLIENTES_NUMBER;
    static double[] CLIENTES_PROPORTION;
    static double CLIENTES_SERVICIO_GARANTIZADO;

    static Vector<Vector<Integer>> SOLUTION;

    static ArrayList<Central> centrales;
    static ArrayList<Cliente> clientes;

    public Vector<Vector<Integer>> solution;
    private Vector<Integer> clientesNoAssignados;
    private Vector<Double> consumoCentral;
    static Vector<Integer> CLIENTES_NO_ASIGNADOS;
    static Vector<Double> CONSUMO_CENTRAL;

    static  Double GUAN_TOTAL;
    public  Double guanyTotal;
    private Double consumoCentralUsado;
    private Double consumoCentralTotal;

    /**
     * Default constructor
     */
    Board(Integer seed) {
        generateBoard(seed);
        generateInitialState();
        consumoCentralTotal = 0.0;
        consumoCentralUsado = 0.0;
        for(int j = 0; j < consumoCentral.size(); j++){
            consumoCentralUsado += consumoCentral.get(j);
        }
        for(int j = 0; j < centrales.size(); j++){
            consumoCentralTotal += centrales.get(j).getProduccion();
        }

        // Printing solution
        for(int i = 0; i < solution.size(); i++){
            Vector<Integer> s1 = solution.get(i);
            for(int j = 0; j < s1.size(); j++){
                System.out.print(s1.get(j) + " - ");
            }
            System.out.println(" /// ");
        }

        for(int j = 0; j < consumoCentral.size(); j++){
            System.out.print(consumoCentral.get(j) + " - ");
        }
        System.out.println(" /// ");

        for(int j = 0; j < centrales.size(); j++){
            System.out.print(centrales.get(j).getProduccion() + " - ");
        }
        System.out.println(" /// ");

        for(int j = 0; j < clientesNoAssignados.size(); j++){
            System.out.print(clientesNoAssignados.get(j) + " - ");
        }
        System.out.println(" /// ");

        System.out.println("Guany general " + guanyTotal);
        System.out.println("Cost produccio " + getCostProduccio());
        System.out.println("Guany real " + getGuanyRealInici());

        SOLUTION  = new Vector<>(0);
    }

    public Vector<Vector<Integer> > getInitialState(){
        return solution;
    }

    public Vector<Integer> getClientesNoAssignados(){
        return clientesNoAssignados;
    }

    public Vector<Double> getConsumoCentral(){
        return consumoCentral;
    }

    public Double getGuanyRealInici(){
        return guanyTotal - getCostProduccio();
    }

    public Double getCostProduccio(){
        Double Costproduccio = 0.0;
        for(int i = 0; i < centrales.size(); ++i){
            Double ConsumCentral = consumoCentral.get(i);
            Integer tipo = centrales.get(i).getTipo();
            if(ConsumCentral > 0.0){
                if(tipo == 0){
                    if(ConsumCentral > 250)
                        Costproduccio += (5 * consumoCentral.get(i)) + 2000;
                    else
                        Costproduccio += (5*250)+2000;
                }
                else if(tipo == 1){
                    if(ConsumCentral > 100)
                        Costproduccio += (8 * consumoCentral.get(i)) + 1000;
                    else
                        Costproduccio += (8*100)+1000;
                }
                else{
                    if(ConsumCentral > 10)
                        Costproduccio += (15 * consumoCentral.get(i)) + 500;
                    else
                        Costproduccio += (15*10)+500;
                }
            }
            else{
                if(tipo == 0){
                    Costproduccio += 1500;
                }
                else if(tipo == 1){
                    Costproduccio += 500;
                }
                else{
                    Costproduccio += 150;
                }
            }
        }
        return Costproduccio;
    }

    public Double guanyClient(Cliente x, Boolean indemnitzat){
        Double guany = 0.0;
        Double consum = x.getConsumo();
        Integer tipo = x.getTipo();
        if(indemnitzat) guany -= 5*consum;
        else{
            if(x.getContrato() == 0){
                if(tipo == 0) guany += consum*40;
                else if(tipo == 1) guany += consum*50;
                else guany += consum*60;
            }
            else {
                if(tipo == 0) guany += consum*30;
                else if(tipo == 1) guany += consum*40;
                else guany += consum*50;
            }
        }
        return guany;
    }

    /**
     *
     * Child state constructor
     *
     * @param board Parent state board.
     */
    Board(Board board) {

        solution  = new Vector<>(0);
        SOLUTION  = new Vector<>(0);
        for(int i = 0; i < centrales.size(); i++){
            solution.add(new Vector<>(board.solution.get(i)));
            SOLUTION.add(new Vector<>(board.solution.get(i)));
        }

        clientesNoAssignados = new Vector<>(board.clientesNoAssignados);
        CLIENTES_NO_ASIGNADOS = new Vector<>(board.clientesNoAssignados);
        CONSUMO_CENTRAL = new Vector<>(board.consumoCentral);
        consumoCentral = new Vector<>(board.consumoCentral);
        guanyTotal = board.guanyTotal;
        GUAN_TOTAL = board.guanyTotal;
        consumoCentralUsado = board.consumoCentralUsado;
        consumoCentralTotal = board.consumoCentralTotal;
    }

    /**
     * Generates random board with N centrals and N clients
     */
    private void generateBoard(Integer seed) {
        try {
            centrales = new Centrales(CENRALES_NUMBERS, seed);
            clientes = new Clientes(CLIENTES_NUMBER, CLIENTES_PROPORTION, CLIENTES_SERVICIO_GARANTIZADO, seed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateInitialState(){
        clientesNoAssignados = new Vector<>(0);

        guanyTotal = 0.0;
        initializeSolution();
        initializeConsumoCentral();

        int aux = 0;
        for(int i = 0; i < clientes.size(); ++i){
            if(clientes.get(i).getContrato() == 0){
                ++aux;
            }
        }
        System.out.println("Ini real " + aux);
        int j = 0;

        for(int i = 0; i < clientes.size(); ++i){
            if(j == centrales.size()) j = 0;
            if(clientes.get(i).getContrato() == 0){
                Central c = centrales.get(j);
                Cliente cl = clientes.get(i);
                Double consum = getConsumoReal(c, cl);
                if(consumoCentral.get(j) + consum <= c.getProduccion()) {
                    solution.get(j).add(i);
                    consumoCentral.set(j, consumoCentral.get(j) + consum);
                    guanyTotal += guanyClient(cl, false);
                }
                else --i;
                ++j;
            }
        }

        for(int i = 0; i < clientes.size(); ++i){
            if(clientes.get(i).getContrato() == 1){
                clientesNoAssignados.add(i);
                guanyTotal += guanyClient(clientes.get(i), true);
            }
        }
    }

    /*
    private void generateInitialState2(){

        Vector<Integer> noGarantizados = new Vector<>(0);
        Vector<Integer> noAssignadosAux = new Vector<>(0);
        clientesNoAssignados = new Vector<>(0);
        guanyTotal = 0.0;

        initializeSolution();
        initializeConsumoCentral();

        int j = 0;
        for(int i = 0; i < centrales.size() && j < clientes.size(); i++){

            Double consumoActual = 0.0;
            Central a = centrales.get(i);
            Double produccionActual = a.getProduccion();
            while(consumoActual < produccionActual*0.5 && j < clientes.size()){
                Cliente c = clientes.get(j);
                if(c.getContrato() == 1) noGarantizados.add(j);
                else if(consumoActual + getConsumoReal(a, c) <= produccionActual){
                    // Debugging
                    System.out.println(consumoActual);
                    System.out.println(getConsumoReal(a, c));
                    System.out.println(produccionActual);
                    solution.get(i).add(j);
                    guanyTotal += guanyClient(c, false);
                    consumoActual += getConsumoReal(a, c);
                    consumoCentral.set(i, consumoActual);
                }
                else break;

                j++;
            }
        }

        if (j < clientes.size()-1){         //Esto se hace por si en la solucion inicial no caben todos los garantizados
            while(j < clientes.size()){
                noAssignadosAux.add(j);
                clientesNoAssignados.add(j);
                guanyTotal += guanyClient(clientes.get(j), true);
                j++;
            }

            // TODO: NullPointerException
            int aux = 0;
            while (aux < noGarantizados.size()) {
                clientesNoAssignados.add(noGarantizados.get(aux));
                ++aux;
            }
        }
        else {
            j = 0;
            for (int i = 0; i < centrales.size() && j < noGarantizados.size(); i++) {
                Double consumoActual = consumoCentral.get(i);
                Double produccionActual = centrales.get(i).getProduccion();
                Central a = centrales.get(i);
                while (consumoActual < produccionActual*0.5 && j < noGarantizados.size()) {
                    Cliente c = clientes.get(noGarantizados.get(j));
                    if (consumoActual + getConsumoReal(a, c) <= produccionActual) {
                        solution.get(i).add(noGarantizados.get(j));
                        guanyTotal += guanyClient(c, false);
                        consumoActual += getConsumoReal(a, c);
                        consumoCentral.set(i, consumoActual);
                        j++;
                    } else break;
                }
            }
            if (j < noGarantizados.size()-1){
                while(j < noGarantizados.size()){     //Esto se puede mejorar (solo hace falta poner el vector noGarantizados desde j hasta el final en el vector clientes noAssignados
                    noAssignadosAux.add(noGarantizados.get(j));
                    j++;
                }
                clientesNoAssignados = noAssignadosAux;
            }
        }

        for(int i = 0; i < clientesNoAssignados.size(); ++i){
            guanyTotal += guanyClient(clientes.get(clientesNoAssignados.get(i)), true);
        }

    }
    */

    private void initializeSolution(){
        solution  = new Vector<>(0);
        for(int i = 0; i < centrales.size(); i++){
            solution.add(new Vector<>(0));
        }
    }

    private void initializeConsumoCentral(){
        consumoCentral = new Vector<>(centrales.size());
        for(int i = 0; i < centrales.size(); i++){
            consumoCentral.add(0.0);
        }
    }

    public Double getConsumoReal(Central a, Cliente b){
        int CentralX = a.getCoordX();
        int CentralY = a.getCoordY();
        int ClientX = b.getCoordX();
        int ClientY = b.getCoordX();
        double distancia = Math.sqrt(Math.pow(Math.abs(ClientX-CentralX), 2) + Math.pow(Math.abs(ClientY-CentralY),2));
        double consum = b.getConsumo();
        if (distancia <= 10) return consum;
        if (distancia <= 25) return consum*1.1;
        if (distancia <= 50) return consum*1.2;
        if (distancia <= 75) return consum*1.4;
        return consum*1.6;
    }

    public boolean cambiarCliente (int cl, int c1, int c2){ //cl = cliente, c1=central origen, c2=central destino
        Central centralDestino = centrales.get(c2);

        int aux = solution.get(c1).get(cl); //para sacar el indice en el Array de clientes
        Cliente cliente = clientes.get(aux);

        double consRealDestino = this.getConsumoReal(centralDestino,cliente);
        double consumo = consumoCentral.get(c2) + consRealDestino; //el cambio que consumo total genera

        if(consumo <= centralDestino.getProduccion()){ //si no nos pasamos
            solution.get(c2).add(cl);
            solution.get(c1).remove(cl);

            consumoCentral.set(c2,consumo); //actualizamos nuestro valor

            double consRealOrigen = this.getConsumoReal(centrales.get(c1), cliente);

            consumoCentral.set(c1,consumoCentral.get(c1)-consRealOrigen); //actualizamos el consumoCentral de la central origen

            consumoCentralUsado -= consRealOrigen;
            consumoCentralUsado += consRealDestino; //actualizamos el valor para el heurístico
            return true;
        }
        else return false;
    }

    public boolean desasignarCliente(int cl, int central){
        int aux = solution.get(central).get(cl); //para sacar el indice en el Array de clientes
        Cliente cliente = clientes.get(aux);
        if(cliente.getContrato()!=0){ // es un no garantizado
            solution.get(central).remove(cl);
            clientesNoAssignados.add(aux);

            Central central_actualiza = centrales.get(central);
            double consumo_nuevo = consumoCentral.get(central) - this.getConsumoReal(central_actualiza,cliente);

            consumoCentral.set(central, consumo_nuevo); //restado del pavo que hemos quitado
            guanyTotal += guanyClient(cliente,true); //porque devuelve un número negativo
            guanyTotal += -guanyClient(cliente,false); //porque devuelve un número negativo

            return true;
        }
        else return false;
    }

    public boolean asignarClienteNoGarantizado(int cl, int central) {

        Central central_asig = centrales.get(central);
        int aux = clientesNoAssignados.get(cl);
        Cliente cliente = clientes.get(aux);

        double consumo_cliente = this.getConsumoReal(central_asig,cliente);
        double consumo = consumoCentral.get(central) + consumo_cliente; //el cambio que consumo total genera

        if (consumo <= central_asig.getProduccion()) { //si no nos pasamos again
            solution.get(central).add(aux);
            consumoCentral.set(central, consumo);
            clientesNoAssignados.remove(cl);
            consumoCentralUsado += consumo_cliente;

            guanyTotal += guanyClient(cliente, false);
            guanyTotal += -guanyClient(cliente, true);

            return true;
        }
        else return false;
    }

    public boolean intercambiarCliente(int cl1, int cl2, int cen1, int cen2){
        Central central1 = centrales.get(cen1);
        Central central2 = centrales.get(cen2);

        int aux1 = solution.get(cen1).get(cl1); //para sacar el indice en el Array de clientes
        Cliente cliente1 = clientes.get(aux1);

        int aux2 = solution.get(cen2).get(cl2);
        Cliente cliente2 = clientes.get(aux2);

        double consumoReal = this.getConsumoReal(central1,cliente2);
        double consumoReal2 = this.getConsumoReal(central2, cliente1);

        double consumo = consumoCentral.get(cen1) + consumoReal - getConsumoReal(central1,cliente1); //el cambio que consumo total genera
        double consumo2 = consumoCentral.get(cen2) + consumoReal2 - getConsumoReal(central2,cliente2);

        if(consumo <= central1.getProduccion() && consumo2 <= central2.getProduccion()){ //si no nos pasamos
            solution.get(cen1).remove(cl1); //Cliente 1 y cliente 2 fuera de sus centrales
            solution.get(cen2).remove(cl2);

            solution.get(cen2).add(aux1); //swap
            solution.get(cen1).add(aux2);

            consumoCentral.set(cen1,consumo); //actualizamos nuestro valor
            consumoCentral.set(cen2,consumo2);

            consumoCentralUsado = consumoCentralUsado - this.getConsumoReal(central1, cliente1) - this.getConsumoReal(central2, cliente2);
            consumoCentralUsado = consumoCentralUsado + consumoReal + consumoReal2;

            return true;
        }
        else return false;
    }

    public int getSolutionSize() { return solution.size();}

    public int getCentralSize(int i) { return solution.get(i).size(); }

    boolean isGoal() {
        return false;
    }

    Double heuristic() {
        // return (double) new Random().nextInt(5); random
        if(guanyTotal < 0) return -guanyTotal;
        else return -((guanyTotal) +((consumoCentralTotal -consumoCentralUsado)/consumoCentralTotal)); //hay que probarlo

    }

    public void printSolution(){



        int aux1 = 0;
        for(int i = 0; i < solution.size(); i++){
            Vector<Integer> s1 = solution.get(i);
            for(int j = 0; j < s1.size(); j++){
                ++aux1;
            }
        }
        System.out.println(" initial " + aux1);


        System.out.println(" SOLUTION ------------------------------------------------- ");


        int aux = 0;
        for(int i = 0; i < SOLUTION.size(); i++){
            Vector<Integer> s1 = SOLUTION.get(i);
            for(int j = 0; j < s1.size(); j++){
                System.out.print(s1.get(j) + " - ");
                ++aux;
            }
            System.out.println(" /// ");
        }
        System.out.println(" AUX "+ aux);


        for(int j = 0; j < CONSUMO_CENTRAL.size(); j++){
            System.out.print(CONSUMO_CENTRAL.get(j) + " - ");
        }
        System.out.println(" /// ");

        for(int j = 0; j < centrales.size(); j++){
            System.out.print(centrales.get(j).getProduccion() + " - ");
        }
        System.out.println(" /// ");

        for(int j = 0; j < CLIENTES_NO_ASIGNADOS.size(); j++){
            System.out.print(CLIENTES_NO_ASIGNADOS.get(j) + " - ");
        }
        System.out.println(" /// ");
        System.out.println(" GUANY "  + GUAN_TOTAL);
    }

    public void printAsignados(){
        for(int i = 0; i < SOLUTION.size(); i++){
            Vector<Integer> s1 = SOLUTION.get(i);
            for(int j = 0; j < s1.size(); j++){
                System.out.print(s1.get(j) + " - ");
            }
            System.out.println(" /// ");
        }

        int asignados = 0;
        for(int i = 0; i < SOLUTION.size(); i++){
            asignados += SOLUTION.get(i).size();
        }
        System.out.println("Clientes asignados -> " + asignados );
    }
}
