package sat;

import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.numeric.NumericVariable;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;

import fr.uga.pddl4j.problem.Fluent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

public class Encoder {

    private Problem problem;

    public Encoder() {
    }

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    public ArrayList<ArrayList<Integer>> encode(){

        int index;
        ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();

        // get toutes les actions
        List<Action> actions = problem.getActions();

        // assiger un numéro à chaque fluent
        HashMap<Fluent, Integer> dictFluents = new HashMap<>();
        int compteur = 1;
        for (Fluent fluent : problem.getFluents()) {
            dictFluents.put(fluent, compteur++);
        }

        // assigner un numéro à chaque action
        HashMap<Action, Integer> dictActions = new HashMap<>();
        for (Action action : actions) {
            dictActions.put(action, compteur++);
        }

        // Clause correspondant à l'état initial
        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        BitVector precondNeg = problem.getInitialState().getNegativeFluents();
        
        ArrayList<Integer> initialStateClause = new ArrayList<>();
        index = precondPos.nextSetBit(0);
        while (index >= 0) {
            initialStateClause.add(index + 1);
            index = precondPos.nextSetBit(index + 1);
        }
        index = precondNeg.nextSetBit(0);
        while (index >= 0) {
            initialStateClause.add(-(index + 1));
            index = precondNeg.nextSetBit(index + 1);
        }
        encodedProblem.add(initialStateClause);

        // boucle sur les actions
        int i = 1;
        for (Action a: actions) {
            i++;
            BitVector precondPosAction = a.getPrecondition().getPositiveFluents();
            BitVector effectPosAction = a.getUnconditionalEffect().getPositiveFluents();
            BitVector effectNegAction = a.getUnconditionalEffect().getNegativeFluents();

            // formule de l'action
            ArrayList<Integer> actionFormula = new ArrayList<>();
            
            // ajouter le numéro de l'action
            actionFormula.add(dictActions.get(a));

            // ajouter les fluents positifs de la précondition
            index = precondPosAction.nextSetBit(0);
            while (index >= 0) {
                actionFormula.add(index + 1);
                index = precondPosAction.nextSetBit(index + 1);
            }
            // ajouter les fluents positifs de l'effet
            index = effectPosAction.nextSetBit(0);
            while (index >= 0) {
                actionFormula.add(index + 1);
                index = effectPosAction.nextSetBit(index + 1);
            }
            // ajouter les fluents négatifs de l'effet
            index = effectNegAction.nextSetBit(0);
            while (index >= 0) {
                actionFormula.add(-(index + 1));
                index = effectNegAction.nextSetBit(index + 1);
            }

            //Clauses correspondant à l'action  
            ArrayList<Integer> actionClause = new ArrayList<>();
            for(int j = 1; j < actionFormula.size(); j++) {
                actionClause = new ArrayList<>();
                actionClause.add(-dictActions.get(a));
                actionClause.add(actionFormula.get(j));
                encodedProblem.add(actionClause);
            }
        }

        // ETAT FINAL
        BitVector goalPos = problem.getGoal().getPositiveFluents();
        BitVector goalNeg = problem.getGoal().getNegativeFluents();

        ArrayList<Integer> goalStateClause = new ArrayList<>();
        index = goalPos.nextSetBit(0);
        while (index >= 0) {
            goalStateClause.add(index + 1);
            index = goalPos.nextSetBit(index + 1);
            encodedProblem.add(goalStateClause);
            goalStateClause = new ArrayList<>();
        }
        index = goalNeg.nextSetBit(0);
        while (index >= 0) {
            goalStateClause.add(index + 1);
            index = goalNeg.nextSetBit(index + 1);
            encodedProblem.add(goalStateClause);
            goalStateClause = new ArrayList<>();
        }


        for (ArrayList<Integer> clause : encodedProblem) {
            System.out.println(clause);
        }

        return encodedProblem;
    }

}
