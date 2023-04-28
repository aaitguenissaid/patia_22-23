package sat;

import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;
import org.sat4j.core.VecInt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Encoder {

    protected final int nbActions;
    protected final int nbFluents;
    final Set<Integer> F = new HashSet<>();
    final Set<Integer> A = new HashSet<>();
    final int nbVariables;
    final int fluentsFirstIndex;
    final int actionsFirstIndex;
    private final Problem problem;
    ArrayList<VecInt> encodedProblem = new ArrayList<>();
    ArrayList<VecInt> encodedGoal = new ArrayList<>();
    ArrayList<VecInt> encodedInit = new ArrayList<>();


    public Encoder(Problem problem) {
        this.problem = problem;
        nbFluents = problem.getFluents().size();
        nbActions = problem.getActions().size();
        nbVariables = nbFluents + nbActions;
        fluentsFirstIndex = 1;
        actionsFirstIndex = nbFluents + 1;
    }

    protected int getIndex(int iteration, int firstIndex, int step, int nbVariables) {
        return (iteration + firstIndex) + (step * nbVariables);
    }

    // [-a, ±f]
    private void addActionClause(int actionIndex, int fluentIndex) {
        VecInt actionClause = new VecInt(new int[]{actionIndex, fluentIndex});
        A.add(Math.abs(actionIndex));
        F.add(fluentIndex);
        encodedProblem.add(actionClause);
    }

    private void addTransitonClause(int precPosFluent, int fluentIndex, int actionIndex) {
        VecInt transitionClause = new VecInt(new int[]{precPosFluent, fluentIndex, actionIndex});
        F.add(precPosFluent);
        F.add(fluentIndex);
        A.add(Math.abs(actionIndex));
        encodedProblem.add(transitionClause);
    }

    private void addDisjunctionClause(int action1, int action2) {
        VecInt disjunctionClause = new VecInt(new int[]{action1, action2});
        A.add(Math.abs(action1));
        A.add(Math.abs(action2));
        encodedProblem.add(disjunctionClause);
    }

    private void encodeGoal(int steps) {
        encodedGoal = new ArrayList<>();
        BitVector goalPos = problem.getGoal().getPositiveFluents();
        BitVector goalNeg = problem.getGoal().getNegativeFluents();

        for (int i = 0; i < goalPos.size(); i++) {
            if (goalPos.get(i)) {
                int goalIndex = getIndex(i, fluentsFirstIndex, steps, nbVariables);
                VecInt goalStateClause = new VecInt(new int[]{goalIndex});
                F.add(goalIndex);
                encodedGoal.add(goalStateClause);
            }
        }

        for (int i = 0; i < goalNeg.size(); i++) {
            if (goalNeg.get(i)) {
                int goalIndex = getIndex(i, fluentsFirstIndex, steps, nbVariables);
                VecInt goalStateClause = new VecInt(new int[]{-goalIndex});
                F.add(goalIndex);
                encodedGoal.add(goalStateClause);
            }
        }

    }

    private void encodeInit() {
        encodedProblem = new ArrayList<>();
        // Clause correspondant à l'état initial

        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        for (int j = 0; j < problem.getFluents().size(); j++) {
            if (precondPos.get(j)) {
                VecInt initialStateClause = new VecInt(new int[]{(j + fluentsFirstIndex)});
                encodedProblem.add(initialStateClause);
                F.add(j + fluentsFirstIndex);
            } else {
                VecInt initialStateClause = new VecInt(new int[]{-(j + fluentsFirstIndex)});
                encodedProblem.add(initialStateClause);
                F.add(-(j + fluentsFirstIndex));
            }
        }
        encodedInit = (ArrayList<VecInt>) encodedProblem.clone();
    }

    public void encodeInitAndSteps(int steps) {
        encodeInit();
        // Steps loop
        for (int step = 0; step < steps; step++) {
            encodeStep(step);
        }
        checkIntersection();
    }

    public void encodeInitStepsAndGoal(int steps) {
        // Steps loop
        encodeInitAndSteps(steps);
        encodeGoal(steps);
        checkIntersection();
    }

    public void writeToFile(ArrayList<VecInt> encodedProblemWithGoal) {
        // write the CNF problem to a file
        FileWriter fw;
        try {
            String fileName = "results/encodedProblem.txt";
            fw = new FileWriter(fileName, false);
            for (VecInt clause : encodedProblemWithGoal) {
                fw.write("{" + clause.toString() + "}\n");
            }
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

            // recuperer l'indice de l'action
            int actionIndex = getIndex(j, actionsFirstIndex, step, nbVariables);

            if (!effectPosAction.intersects(effectNegAction) && !(effectPosAction.size() == 0 && effectNegAction.size() == 0)) {
                // ajouter les fluents positifs de la précondition
                for (int k = 0; k < precondPosAction.size(); k++) {
                    if (precondPosAction.get(k)) {
                        int precondition = getIndex(k, fluentsFirstIndex, step, nbVariables);
                        // [-a, +f]
                        addActionClause(-actionIndex, precondition);
                    }
                }

                // ajouter les fluents positifs de l'effet sur le prochain état
                for (int k = 0; k < effectPosAction.size(); k++) {
                    if (effectPosAction.get(k)) {
                        int posFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        // [-a, +f]
                        addActionClause(-actionIndex, posFluent);
                    }
                }

                // ajouter les fluents négatifs de l'effet sur le prochain état
                for (int k = 0; k < effectNegAction.size(); k++) {
                    if (effectNegAction.get(k)) {
                        int negFluent = getIndex(k, fluentsFirstIndex, step + 1, nbVariables);
                        // [-a, -f]
                        addActionClause(-actionIndex, -negFluent);
                    }
                }

                // Action disjunction: at least one action must be executed at each step.
                for (int k = j + 1; k < actions.size(); k++) {
                    int secondActionIndex = getIndex(k, actionsFirstIndex, step, nbVariables);
                    // ¬Ai v ¬Bi
                    addDisjunctionClause(-actionIndex, -secondActionIndex);
                }
            } else {
                //System.err.println("Action " + action.getName() + " has no effect.");
                //System.err.println("positive fluents: " + effectPosAction);
                //System.err.println("negative fluents: " + effectNegAction);
            }
        }

        for (int i = 0; i < problem.getFluents().size(); i++) {
            VecInt posClause = new VecInt();
            VecInt negClause = new VecInt();
            int currentFluentIndex = getIndex(i, fluentsFirstIndex, step, nbVariables);
            int nextFluentIndex = getIndex(i, fluentsFirstIndex, step + 1, nbVariables);
            posClause.push(currentFluentIndex);
            posClause.push(-nextFluentIndex);
            negClause.push(-currentFluentIndex);
            negClause.push(nextFluentIndex);

            for (int j = 0; j < actions.size(); j++) {
                Action action = actions.get(j);
                BitVector effectPosAction = action.getUnconditionalEffect().getPositiveFluents();
                BitVector effectNegAction = action.getUnconditionalEffect().getNegativeFluents();
                if (!effectPosAction.intersects(effectNegAction) && !(effectPosAction.size() == 0 && effectNegAction.size() == 0)) {
                    int actionIndex = getIndex(j, actionsFirstIndex, step, nbVariables);

                    // ¬f ∧ f1 -> a  <==>  f ∨ ¬f1 ∨ {aj0 ∨ aj1 ...}
                    if (action.getUnconditionalEffect().getPositiveFluents().get(i)) {
                        posClause.push(actionIndex);
                    }

                    // f ∧ ¬f1 -> a  <==>  ¬f ∨ f1 ∨ {ai0 ∨ ai1 ...}
                    if (action.getUnconditionalEffect().getNegativeFluents().get(i)) {
                        negClause.push(actionIndex);
                    }
                }
            }
            if (posClause.size() > 2) {
                encodedProblem.add(posClause);
            }
            if (negClause.size() > 2) {
                encodedProblem.add(negClause);
            }
        }
    }

    public void encodeStepWithGoal(int step) {
        encodeStep(step - 1);
        encodeGoal(step);
        checkIntersection();
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

    public ArrayList<VecInt> getEncodedProblem() {
        @SuppressWarnings("unchecked") ArrayList<VecInt> encodedProblemWithGoal = (ArrayList<VecInt>) encodedProblem.clone();
        encodedProblemWithGoal.addAll(encodedGoal);
        //writeToFile(encodedProblemWithGoal);
        return encodedProblemWithGoal;
    }

    public int decodeIndex(int encodeedIndex) {
        int res;
        int modulo = encodeedIndex % nbVariables;
        res = (modulo == 0) ? nbVariables : modulo;
        return res;
    }

    public Set<Integer> getSetA() {
        return A;
    }

    public ArrayList<VecInt> getEncodedGoal() {
        return encodedGoal;
    }

    public ArrayList<VecInt> getEncodedInit() {
        return encodedGoal;
    }
}