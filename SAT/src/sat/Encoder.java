package sat;

import fr.uga.pddl4j.problem.Fluent;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Encoder {

    private final Problem problem;

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    private int getIndex(int iteration, int firstIndex, int step, int nbVariables) {
        return (iteration + firstIndex) + (step * nbVariables);
    }

    private int getPreviousIndex(int index, int nbVariables) {
        int result;
        boolean isNegative = false;
        if (index < 0)  {
            isNegative = true;
            index = -index;
        }

        if (index <= nbVariables) {
            System.err.println("Error: index=" + index + " is smaller than nbVariables=" + nbVariables + " -> does not have a previous index");
        }

        result = index - nbVariables;

        if (isNegative) {
            result = -result;
        }
        return result;
    }

    // [-a, ±f]
    private void addActionClause(int actionIndex, int fluentIndex) {
        ArrayList<Integer> actionClause = new ArrayList<>();
        actionClause.add(-actionIndex);
        actionClause.add(fluentIndex);
        A.add(-actionIndex);
        F.add(fluentIndex);
        System.out.println("[-a, ±f] : " + actionClause);
        encodedProblem.add(actionClause);
    }

    private void addTransitonClause(int actionIndex, int fluentIndex, boolean positiveFluent) {
        ArrayList<Integer> transitionClause = new ArrayList<>();
        int a = getPreviousIndex(fluentIndex, nbVariables);
        int b = fluentIndex;
        int c = actionIndex;

        if(positiveFluent) {
            b = -b;     // ¬A ∧ B -> C  <==>  A ∨ ¬B ∨ C
        } else {
          a=-a;         // A ∧ ¬B -> C  <==>  ¬A ∨ B ∨ C
        }
        //System.out.println(" ¬A ∧ B -> C  : " + -a + " ∧ " + b + " -> " + c);
        transitionClause.add(a);
        transitionClause.add(b);
        transitionClause.add(c);
        F.add(a);
        F.add(b);
        A.add(c);
        System.out.println("[f,-f[i+1],a] : " + transitionClause);
        encodedProblem.add(transitionClause);
    }

    private void addDisjunctionClause(int action1, int action2){
        ArrayList<Integer> disjunctionClause = new ArrayList<>();
        disjunctionClause.add(action1);
        disjunctionClause.add(action2);
        A.add(action1);
        A.add(action2);
        System.out.println("[¬A, ¬B] : " + disjunctionClause);
        encodedProblem.add(disjunctionClause);
    }


    ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();
    Set<Integer> F = new HashSet<>();
    Set<Integer> A = new HashSet<>();
    int nbActions;
    int nbFluents;
    int nbVariables;

    public ArrayList<ArrayList<Integer>> encode(int steps) {


        // get toutes les actions
        List<Action> actions = problem.getActions();
        List<Fluent> fluents = problem.getFluents();

        nbActions = actions.size();
        nbFluents = fluents.size();
        nbVariables = nbFluents + nbActions;
        System.out.println("nbActions = " + nbActions);
        System.out.println("nbFluents = " + nbFluents);
        System.out.println("nbVariables = " + nbVariables);

        int fluentsFirstIndex = 1;
        int actionsFirstIndex = nbFluents + 1;
        System.out.println("fluentsFirstIndex = " + fluentsFirstIndex);
        System.out.println("actionsFirstIndex = " + actionsFirstIndex);

        // Clause correspondant à l'état initial
        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        BitVector precondNeg = problem.getInitialState().getNegativeFluents();

        ArrayList<Integer> initialStateClause;
        for (int j = 0; j < precondPos.size(); j++) {
            if (precondPos.get(j)) {
                initialStateClause = new ArrayList<>();
                initialStateClause.add(j + fluentsFirstIndex);
                encodedProblem.add(initialStateClause);
                F.add(j + fluentsFirstIndex);
            }
        }
        for (int j = 0; j < precondNeg.size(); j++) {
            if (precondNeg.get(j)) {
                initialStateClause = new ArrayList<>();
                initialStateClause.add(-(j + fluentsFirstIndex));
                F.add(-(j + fluentsFirstIndex));
                encodedProblem.add(initialStateClause);
            }
        }

        System.out.println("initialStateClause : " + encodedProblem);

        // Steps loop
        for (int step = 0; step < steps; step++) {
            // boucle sur les actions
            for (int j = 0; j < actions.size(); j++) {
                Action action = actions.get(j);
                BitVector precondPosAction = action.getPrecondition().getPositiveFluents();
                BitVector effectPosAction = action.getUnconditionalEffect().getPositiveFluents();
                BitVector effectNegAction = action.getUnconditionalEffect().getNegativeFluents();

                // formule de l'action
                ActionFormula actionFormula = new ActionFormula();

                // ajouter le numéro de l'action
                actionFormula.action = getIndex(j, actionsFirstIndex, step, nbVariables);

                // ajouter les fluents positifs de la précondition
                for (int k = 0; k < precondPosAction.size(); k++) {
                    if (precondPosAction.get(k)) {
                        int precondition = getIndex(k, fluentsFirstIndex, step, nbVariables);
                        actionFormula.precondPosAction.add(precondition);
                        addActionClause(actionFormula.action, precondition);
                    }
                }

                // ajouter les fluents positifs de l'effet sur le prochain état
                for (int k = 0; k < effectPosAction.size(); k++) {
                    if (effectPosAction.get(k)) {
                        int posFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        actionFormula.effectPosAction.add(posFluent);
                        addActionClause(actionFormula.action, posFluent);
                        addTransitonClause(actionFormula.action, posFluent, true);
                    }
                }

                // ajouter les fluents négatifs de l'effet sur le prochain état
                for (int k = 0; k < effectNegAction.size(); k++) {
                    if (effectNegAction.get(k)) {
                        int negFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        actionFormula.effectNegAction.add(negFluent);
                        addActionClause(actionFormula.action, -negFluent);
                        addTransitonClause(actionFormula.action, negFluent, false);
                    }
                }

                System.out.println("\nActions disjunctions : ");
                // Action disjunction: at least one action must be executed at each step.
                // ¬Ai v ¬Bi
                for (int k = 0; k < actions.size(); k++) {
                    if (j != k) {
                        int action1 = -getIndex(j, actionsFirstIndex, step, nbVariables);
                        int action2 = -getIndex(k, actionsFirstIndex, step, nbVariables);
                        addDisjunctionClause(action1, action2);
                    }
                }
            }
        }

        System.out.println("\nGoal : ");
        // ETAT FINAL
        BitVector goalPos = problem.getGoal().getPositiveFluents();
        BitVector goalNeg = problem.getGoal().getNegativeFluents();

        for (int i = 0; i < goalPos.size(); i++) {
            if (goalPos.get(i)) {
                ArrayList<Integer> goalStateClause = new ArrayList<>();
                int goalIndex = getIndex(i, fluentsFirstIndex, steps, nbVariables);
                goalStateClause.add(goalIndex);
                F.add(goalIndex);
                encodedProblem.add(goalStateClause);
                System.out.println("goalStateClause : " + goalStateClause);
            }
        }
        for (int i = 0; i < goalNeg.size(); i++) {
            if (goalNeg.get(i)) {
                ArrayList<Integer> goalStateClause = new ArrayList<>();
                int goalIndex = -getIndex(i, fluentsFirstIndex, steps, nbVariables);
                goalStateClause.add(goalIndex);
                F.add(goalIndex);
                encodedProblem.add(goalStateClause);
                System.out.println("goalStateClause : " + goalStateClause);
            }
        }


        // write the CNF problem to a file
        PrintWriter writer;
        try {
            writer = new PrintWriter("results/encodedProblem.txt", StandardCharsets.UTF_8);
            for (ArrayList<Integer> clause : encodedProblem) {
                writer.println(clause.toString());
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // intersection of A and F
        System.out.println("Check intersection of the two sets :");

        // just for printing the sets.
        StringBuilder posA = new StringBuilder();
        StringBuilder negA = new StringBuilder();
        for (int element : A) {
            if (element > 0) {
                posA.append(element).append(" ");
            } else {
                negA.append(element).append(" ");
            }
        }

        StringBuilder posF = new StringBuilder();
        StringBuilder negF = new StringBuilder();
        for (int element : F) {
            if (element > 0) {
                posF.append(element).append(" ");
            } else {
                negF.append(element).append(" ");
            }
        }

        Set<Integer> I = new HashSet<>(A);
        I.retainAll(F); // I now contains the intersection of A and F
        if (I.size() > 0) {
            // print positive values of hashset.
            System.err.println("nbActions = " + nbActions);
            System.err.println("nbFluents = " + nbFluents);
            System.err.println("nbVariables = " + nbVariables);

            System.err.println("\nfluentsFirstIndex = " + fluentsFirstIndex);
            System.err.println("actionsFirstIndex = " + actionsFirstIndex);

            System.err.println("\nposA : " + posA);
            System.err.println("posF : " + posF);

            System.err.println("\nnegA : " + negA);
            System.err.println("negF : " + negF);

            System.err.println("\nError in encoding Intersection of Actions and Fluents is not empty!"); // prints intersection
            System.err.println("I : " + I); // prints intersection
        }

        return encodedProblem;
    }

}
