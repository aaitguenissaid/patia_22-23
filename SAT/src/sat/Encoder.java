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
        if (index < 0) index = -index;
        if (index <= nbVariables)
            System.err.println("Error: index=" + index + " is smaller than nbVariables=" + nbVariables + " -> does not have a previous index");
        return index - nbVariables;
    }

    public ArrayList<ArrayList<Integer>> encode(int steps) {

        ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();

        // get toutes les actions
        List<Action> actions = problem.getActions();
        List<Fluent> fluents = problem.getFluents();
        Set<Integer> F = new HashSet<>();
        Set<Integer> A = new HashSet<>();

        int nbActions = actions.size();
        int nbFluents = fluents.size();
        int nbVariables = nbFluents + nbActions;
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

        ArrayList<Integer> initialStateClause = new ArrayList<>();
        for (int j = 0; j < precondPos.size(); j++) {
            if (precondPos.get(j)) {
                initialStateClause.add(j + 1);
                F.add(j + 1);
            }
        }
        for (int j = 0; j < precondNeg.size(); j++) {
            if (precondNeg.get(j)) {
                initialStateClause.add(-(j + 1));
                F.add(-(j + 1));
            }
        }

        encodedProblem.add(initialStateClause);
        System.out.println("initialStateClause : " + initialStateClause);

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
                        actionFormula.precondPosAction.add(getIndex(k, fluentsFirstIndex, step, nbVariables));
                    }
                }

                // ajouter les fluents positifs de l'effet sur le prochain état
                for (int k = 0; k < effectPosAction.size(); k++) {
                    if (effectPosAction.get(k)) {
                        actionFormula.effectPosAction.add(getIndex(k, fluentsFirstIndex, step + 1, nbVariables));
                    }
                }
                // ajouter les fluents négatifs de l'effet sur le prochain état
                for (int k = 0; k < effectNegAction.size(); k++) {
                    if (effectNegAction.get(j)) {
                        actionFormula.effectNegAction.add(-getIndex(k, fluentsFirstIndex, step + 1, nbVariables));
                    }
                }

                ArrayList<ArrayList<Integer>> terms = new ArrayList<>();
                terms.add(actionFormula.precondPosAction);
                terms.add(actionFormula.effectPosAction);
                terms.add(actionFormula.effectNegAction);

                System.out.println("\nActions " + step + " : ");
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < terms.get(k).size(); l++) {
                        //Clauses correspondant  à l'action
                        ArrayList<Integer> actionClause = new ArrayList<>();
                        actionClause.add(-actionFormula.action);
                        actionClause.add(terms.get(k).get(l));
                        A.add(-actionFormula.action);
                        F.add(terms.get(k).get(l));
                        System.out.println("[-a, ±f] : " + actionClause);
                        encodedProblem.add(actionClause);
                    }
                }

                System.out.println("\nPositive Transitions " + step + " : ");
                // ¬ fluent[i-1] ∧ fluent[i] -> action[i-1]
                // ¬A ∧ B -> C
                // A ∨ ¬B ∨ C
                for (int k = 0; k < actionFormula.effectPosAction.size(); k++) {
                    ArrayList<Integer> transitionClause = new ArrayList<>();
                    int a = getPreviousIndex(actionFormula.effectPosAction.get(k), nbVariables);
                    int b = actionFormula.effectPosAction.get(k);
                    int c = actionFormula.action;
                    //System.out.println(" ¬A ∧ B -> C  : " + -a + " ∧ " + b + " -> " + c);
                    transitionClause.add(a);
                    transitionClause.add(-b);
                    transitionClause.add(c);
                    F.add(a);
                    F.add(-b);
                    A.add(c);
                    System.out.println("[f,-f[i+1],a] : " + transitionClause);
                    encodedProblem.add(transitionClause);
                }


                System.out.println("\nNegative Transitions " + step + " : ");
                // fluent[i-1] ∧ ¬fluent[i] -> action[i-1]
                // A ∧ ¬B -> C
                // ¬A ∨ B ∨ C
                for (int k = 0; k < actionFormula.effectNegAction.size(); k++) {
                    ArrayList<Integer> transitionClause = new ArrayList<>();
                    int a = (-actionFormula.effectNegAction.get(k)) - nbVariables;
                    int b = (-actionFormula.effectNegAction.get(k));
                    int c = actionFormula.action;

                    //System.out.println(" A ∧ ¬B -> C  : " + a + " ∧ " + -b + " -> " + c);
                    transitionClause.add(-a);
                    transitionClause.add(b);
                    transitionClause.add(c);
                    F.add(a);
                    F.add(-b);
                    A.add(c);
                    System.out.println("[-f,f[i+1],a] : " + transitionClause);
                    encodedProblem.add(transitionClause);
                }
            }

            System.out.println("\nActions disjunctions " + step + " : ");
            // Action disjunction: at least one action must be executed at each step.
            // ¬Ai v ¬Bi
            for (int i = 0; i < actions.size() - 1; i++) {
                ArrayList<Integer> actionClause = new ArrayList<>();
                int action = getIndex(i, actionsFirstIndex, step, nbVariables);
                int nextAction = action + 1;
                actionClause.add(-action);
                actionClause.add(-nextAction);
                A.add(-action);
                A.add(-nextAction);
                System.out.println("[¬A, ¬(A+1)] : " + actionClause);
                encodedProblem.add(actionClause);
            }
        }

        System.out.println("\nGoal : ");
        // ETAT FINAL
        BitVector goalPos = problem.getGoal().getPositiveFluents();
        BitVector goalNeg = problem.getGoal().getNegativeFluents();

        for (int i = 0; i < goalPos.size(); i++) {
            if (goalPos.get(i)) {
                ArrayList<Integer> goalStateClause = new ArrayList<>();
                goalStateClause.add(getIndex(i, fluentsFirstIndex, steps, nbVariables));
                F.add(getIndex(i, fluentsFirstIndex, steps, nbVariables));
                encodedProblem.add(goalStateClause);
                System.out.println("goalStateClause : " + goalStateClause);
            }
        }
        for (int i = 0; i < goalNeg.size(); i++) {
            if (goalNeg.get(i)) {
                ArrayList<Integer> goalStateClause = new ArrayList<>();
                goalStateClause.add(-getIndex(i, fluentsFirstIndex, steps, nbVariables));
                F.add(-getIndex(i, fluentsFirstIndex, steps, nbVariables));
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

        String posA = "";
        String negA = "";
        for (int element : A) {
            if (element > 0) {
                posA += element + " ";
            } else {
                negA += element + " ";
            }
        }

        String posF = "";
        String negF = "";
        for (int element : F) {
            if (element > 0) {
                posF += element + " ";
            } else {
                negF += element + " ";
            }
        }
        // print positive values of hashset.
        System.out.println("posA : " + posA);
        System.out.println("negA : " + negA);
        System.out.println("posF : " + posF);
        System.out.println("negF : " + negF);


        Set<Integer> I = new HashSet<>(A);
        I.retainAll(F); // I now contains the intersection of A and F
        System.out.println("I : " + I); // prints intersection

        return encodedProblem;
    }

}
