package main.logic.cplex;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class TransportationSolver {

    private boolean isSolved;
    private String status;
    private double solutionCost;
    private double[][] solution;

    public TransportationSolver() {
        isSolved = false;
    }

    public void solve(Integer[] supply, Integer[] demand, Integer[][] cost) {
        try  {
            IloCplex cplex = new IloCplex();

            IloNumVar[][] x = new IloNumVar[supply.length][];

            for (int i = 0; i < supply.length; i++) {
                x[i] = cplex.numVarArray(demand.length, 0., Double.MAX_VALUE);
            }

            for (int i = 0; i < supply.length; i++) // supply must meet demand
                cplex.addEq(cplex.sum(x[i]), supply[i]);

            for (int j = 0; j < demand.length; j++) { // demand must meet supply
                IloLinearNumExpr v = cplex.linearNumExpr();
                for(int i = 0; i < supply.length; i++)
                    v.addTerm(1., x[i][j]);
                cplex.addEq(v, demand[j]);
            }

            IloLinearNumExpr expr = cplex.linearNumExpr();
            for (int i = 0; i < supply.length; ++i) {
                for (int j = 0; j < demand.length; ++j) {
                    expr.addTerm(x[i][j], cost[i][j]);
                }
            }

            cplex.addMinimize(expr);

            if ( cplex.solve() ) {
                isSolved = true;
                status = cplex.getStatus().toString();
                solutionCost = cplex.getObjValue();
                solution = new double[supply.length][demand.length];

                for (int i = 0; i < supply.length; ++i) {
                    for (int j = 0; j < demand.length; ++j)
                        solution[i][j] = cplex.getValue(x[i][j]);
                }
            } else {
                isSolved = false;
            }
        }
        catch (IloException exc) {
            isSolved = false;
            exc.printStackTrace();
        }
    }
}

