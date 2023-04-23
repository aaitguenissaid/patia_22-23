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
    ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();
    ArrayList<ArrayList<Integer>> encodedProblemWithoutGoal = new ArrayList<>();
    Set<Integer> F = new HashSet<>();
    Set<Integer> A = new HashSet<>();
    int nbActions;
    int nbFluents;
    int nbVariables;
    int fluentsFirstIndex;
    int actionsFirstIndex;

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    private int getIndex(int iteration, int firstIndex, int step, int nbVariables) {
        return (iteration + firstIndex) + (step * nbVariables);
    }

    private int getPreviousIndex(int index, int nbVariables) {
        int result;
        boolean isNegative = false;
        if (index < 0) {
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
        encodedProblemWithoutGoal.add(actionClause);
    }

    private void addTransitonClause(int actionIndex, int fluentIndex, boolean positiveFluent) {
        ArrayList<Integer> transitionClause = new ArrayList<>();
        int a = getPreviousIndex(fluentIndex, nbVariables);
        int b = fluentIndex;
        int c = actionIndex;

        if (positiveFluent) {
            b = -b;     // ¬A ∧ B -> C  <==>  A ∨ ¬B ∨ C
        } else {
            a = -a;         // A ∧ ¬B -> C  <==>  ¬A ∨ B ∨ C
        }
        //System.out.println(" ¬A ∧ B -> C  : " + -a + " ∧ " + b + " -> " + c);
        transitionClause.add(a);
        transitionClause.add(b);
        transitionClause.add(c);
        F.add(a);
        F.add(b);
        A.add(c);
        if (positiveFluent) {
            System.out.println("[f,-f[i+1],a] : " + transitionClause);
        } else {
            System.out.println("[-f,f[i+1],a] : " + transitionClause);

        }
        encodedProblemWithoutGoal.add(transitionClause);
    }

    private void addDisjunctionClause(int action1, int action2) {
        ArrayList<Integer> disjunctionClause = new ArrayList<>();
        disjunctionClause.add(action1);
        disjunctionClause.add(action2);
        A.add(action1);
        A.add(action2);
        System.out.println("[¬A, ¬B] : " + disjunctionClause);
        encodedProblemWithoutGoal.add(disjunctionClause);
    }

    private void encodeGoal(int steps) {
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

    }

    private void encodeInit() {
        // get toutes les actions
        List<Action> actions = problem.getActions();
        List<Fluent> fluents = problem.getFluents();

        nbActions = actions.size();
        nbFluents = fluents.size();
        nbVariables = nbFluents + nbActions;
        System.out.println("nbActions = " + nbActions);
        System.out.println("nbFluents = " + nbFluents);
        System.out.println("nbVariables = " + nbVariables);

        fluentsFirstIndex = 1;
        actionsFirstIndex = nbFluents + 1;
        System.out.println("fluentsFirstIndex = " + fluentsFirstIndex);
        System.out.println("actionsFirstIndex = " + actionsFirstIndex);

        // Clause correspondant à l'état initial
        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        BitVector precondNeg = problem.getInitialState().getNegativeFluents();
        System.out.println("precondPos size = " + precondPos.size());
        System.out.println("precondPos = " + precondPos);
        System.out.println("precondNeg size = " + precondNeg.size());
        System.out.println("precondNeg = " + precondNeg);

        ArrayList<Integer> initialStateClause;
        for (int j = 0; j < precondPos.size(); j++) {
            if (precondPos.get(j)) {
                initialStateClause = new ArrayList<>();
                initialStateClause.add(j + fluentsFirstIndex);
                encodedProblemWithoutGoal.add(initialStateClause);
                F.add(j + fluentsFirstIndex);
            }
        }
        for (int j = 0; j < precondNeg.size(); j++) {
            if (precondNeg.get(j)) {
                initialStateClause = new ArrayList<>();
                initialStateClause.add(-(j + fluentsFirstIndex));
                F.add(-(j + fluentsFirstIndex));
                encodedProblemWithoutGoal.add(initialStateClause);
            }
        }

        System.out.println("initialStateClause : " + encodedProblemWithoutGoal);

    }

    public void encodeInitAndSteps(int steps) {
        encodeInit();
        // Steps loop
        for (int step = 0; step < steps; step++) {
            encodeStep(step);
        }
        encodedProblem = encodedProblemWithoutGoal;
        checkIntersection();
    }

    public void encodeInitAndStepsAndGoal(int steps) {
        // Steps loop
        encodeInitAndSteps(steps);
        encodeGoal(steps);
        checkIntersection();
    }

    public void writeToFile() {
        // write the CNF problem to a file
        PrintWriter writer;
        try {
            String fileName = "results/encodedProblem.txt";
            writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
            for (ArrayList<Integer> clause : encodedProblem) {
                writer.println(clause.toString());
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIntersection() {
        // intersection of A and F

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


        System.out.println("\nposA : " + posA);
        System.out.println("posF : " + posF);

        System.out.println("\nnegA : " + negA);
        System.out.println("negF : " + negF);


        System.out.println("A : " + A);
        System.out.println("F : " + F);
        System.out.println("Check intersection of the two sets :");

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

    }

    private void encodeStep(int step) {
        List<Action> actions = problem.getActions();
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


            if (!effectPosAction.equals(effectNegAction) || (effectPosAction.length() == 0 || effectNegAction.length() == 0)) {
                System.err.println("\naction : " + action.getName());
                System.err.println("actionIndex : " + actionFormula.action);
                System.err.println("precondPosAction : " + precondPosAction);
                System.err.println("effectPosAction : " + effectPosAction);
                System.err.println("effectNegAction : " + effectNegAction);

                // ajouter les fluents positifs de la précondition
                for (int k = 0; k < precondPosAction.size(); k++) {
                    if (precondPosAction.get(k)) {
                        int precondition = getIndex(k, fluentsFirstIndex, step, nbVariables);
                        System.out.println("precondition : " + precondition);
                        actionFormula.precondPosAction.add(precondition);
                        addActionClause(actionFormula.action, precondition);
                    }
                }

                // ajouter les fluents positifs de l'effet sur le prochain état
                for (int k = 0; k < effectPosAction.size(); k++) {
                    if (effectPosAction.get(k)) {
                        int posFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        System.out.println("posFluent : " + posFluent);
                        actionFormula.effectPosAction.add(posFluent);
                        addActionClause(actionFormula.action, posFluent);
                        addTransitonClause(actionFormula.action, posFluent, true);
                    }
                }

                // ajouter les fluents négatifs de l'effet sur le prochain état
                for (int k = 0; k < effectNegAction.size(); k++) {
                    if (effectNegAction.get(k)) {
                        int negFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        System.out.println("negFluent : " + negFluent);
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
                        Action secondAction = actions.get(k);
                        BitVector secondEffectPosAction = secondAction.getUnconditionalEffect().getPositiveFluents();
                        BitVector secondEffectNegAction = secondAction.getUnconditionalEffect().getNegativeFluents();
                        //TODO review this condition
                        if ((!effectPosAction.equals(effectNegAction) && !secondEffectPosAction.equals(secondEffectNegAction)) || ((effectPosAction.length() == 0 && effectNegAction.length() == 0) && (secondEffectPosAction.length() == 0 && secondEffectPosAction.length() == 0))) {
                            int action1 = -getIndex(j, actionsFirstIndex, step, nbVariables);
                            int action2 = -getIndex(k, actionsFirstIndex, step, nbVariables);
                            addDisjunctionClause(action1, action2);
                        }
                    }
                }
            } else {
                System.err.println("This Action Was Refused : " + action.getName());
                System.err.println("actionIndex : " + actionFormula.action);
                System.err.println("precondPosAction : " + precondPosAction);
                System.err.println("effectPosAction : " + effectPosAction);
                System.err.println("effectNegAction : " + effectNegAction);
            }
        }
    }


    public void encodeStepWithGoal(int step) {
        encodeStep(step - 1);
        encodedProblem = encodedProblemWithoutGoal;
        encodeGoal(step);
        checkIntersection();
        writeToFile();
    }

    public ArrayList<ArrayList<Integer>> getEncodedProblem() {
        return encodedProblem;
    }

    public Set<Integer> getSetA() {
        return A;
    }
}