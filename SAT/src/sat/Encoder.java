package sat;

import fr.uga.pddl4j.problem.Fluent;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Encoder {

    private final Problem problem;

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    public ArrayList<ArrayList<Integer>> encode(int steps) {

        ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();

        // get toutes les actions
        List<Action> actions = problem.getActions();
        int nbActions = actions.size();
        List<Fluent> fluents = problem.getFluents();
        int nbFluents = fluents.size();

        int nbVariables = nbFluents + nbActions;

        int fluentsFirstIndex = 1;

        int actionsFirstIndex = (int) pow(2, ceil(log(nbFluents * (steps + 1)) / log(2)));

        System.out.println("fluentsFirstIndex = " + fluentsFirstIndex);
        System.out.println("actionsFirstIndex = " + actionsFirstIndex);

        // Clause correspondant à l'état initial
        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        BitVector precondNeg = problem.getInitialState().getNegativeFluents();

        ArrayList<Integer> initialStateClause = new ArrayList<>();
        for (int j = 0; j < precondPos.size(); j++) {
            if (precondPos.get(j)) {
                initialStateClause.add(j + 1);
            }
        }
        for (int j = 0; j < precondNeg.size(); j++) {
            if (precondNeg.get(j)) {
                initialStateClause.add(-(j + 1));
            }
        }

        encodedProblem.add(initialStateClause);
        System.out.println("initialStateClause : " + initialStateClause);

        // boucle sur les actions
        for (int step = 0; step < steps; step++) {
            for (int j = 0; j < actions.size(); j++) {
                Action action = actions.get(j);
                BitVector precondPosAction = action.getPrecondition().getPositiveFluents();
                BitVector effectPosAction = action.getUnconditionalEffect().getPositiveFluents();
                BitVector effectNegAction = action.getUnconditionalEffect().getNegativeFluents();

                // formule de l'action
                ActionFormula actionFormula = new ActionFormula();

                // ajouter le numéro de l'action
                actionFormula.action = (j + 1) + actionsFirstIndex + (step * nbVariables);

                // ajouter les fluents positifs de la précondition
                for (int k = 0; k < precondPosAction.size(); k++) {
                    if (precondPosAction.get(k)) {
                        actionFormula.precondPosAction.add((k + 1) + (step * nbFluents));
                    }
                }

                // ajouter les fluents positifs de l'effet sur le prochain état
                for (int k = 0; k < effectPosAction.size(); k++) {
                    if (effectPosAction.get(k)) {
                        actionFormula.effectPosAction.add((k + 1) + ((step + 1) * nbFluents));
                    }
                }
                // ajouter les fluents négatifs de l'effet sur le prochain état
                for (int k = 0; k < effectNegAction.size(); k++) {
                    if (effectNegAction.get(j)) {
                        actionFormula.effectNegAction.add(-((k + 1) + ((step + 1) * nbFluents)));
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
                    int a = actionFormula.effectPosAction.get(k) - nbFluents;
                    int b = actionFormula.effectPosAction.get(k);
                    int c = actionFormula.action;
                    //System.out.println(" ¬A ∧ B -> C  : " + -a + " ∧ " + b + " -> " + c);
                    transitionClause.add(a);
                    transitionClause.add(-b);
                    transitionClause.add(c);
                    System.out.println("[f,-f[i+1],a] : " + transitionClause);
                    encodedProblem.add(transitionClause);
                }


                System.out.println("\nNegative Transitions " + step + " : ");
                // fluent[i-1] ∧ ¬fluent[i] -> action[i-1]
                // A ∧ ¬B -> C
                // ¬A ∨ B ∨ C
                for (int k = 0; k < actionFormula.effectNegAction.size(); k++) {
                    ArrayList<Integer> transitionClause = new ArrayList<>();
                    int a = (-actionFormula.effectNegAction.get(k)) - nbFluents;
                    int b = (-actionFormula.effectNegAction.get(k));
                    int c = actionFormula.action;

                    //System.out.println(" A ∧ ¬B -> C  : " + a + " ∧ " + -b + " -> " + c);
                    transitionClause.add(-a);
                    transitionClause.add(b);
                    transitionClause.add(c);
                    System.out.println("[-f,f[i+1],a] : " + transitionClause);
                    encodedProblem.add(transitionClause);
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
                goalStateClause.add((i + 1) + (steps * nbFluents));
                encodedProblem.add(goalStateClause);
                System.out.println("goalStateClause : " + goalStateClause);
            }
        }
        for (int i = 0; i < goalNeg.size(); i++) {
            if (goalNeg.get(i)) {
                ArrayList<Integer> goalStateClause = new ArrayList<>();
                goalStateClause.add(-((i + 1) + (steps * nbFluents)));
                encodedProblem.add(goalStateClause);
                System.out.println("goalStateClause : " + goalStateClause);
            }
        }

        // TODO : Action disjunction: at least one action must be executed at each step.


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
        return encodedProblem;
    }

}
