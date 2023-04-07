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

public class Encoder {

    private Problem problem;

    public Encoder() {
    }

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    public ArrayList<ArrayList<Integer>> encode(){

        ArrayList<ArrayList<Integer>> encodedProblem = new ArrayList<>();
        //how to calculate the cnf formula that will be passed to the SAT solver


        // get toutes les actions
        List<Action> actions = problem.getActions();

        System.out.println("Fluents");

        // fluent = proposition
        // assiger un numéro à chaque fluent
        HashMap<Fluent, Integer> dictFluents = new HashMap<>();
        int compteur = 1;
        for (Fluent fluent : problem.getFluents()) {
            System.out.println(compteur + " : " + fluent);
            dictFluents.put(fluent, compteur++);
        }

        System.out.println("\nActions");

        // assigner un numéro à chaque action
        HashMap<Action, Integer> dictActions = new HashMap<>();
        for (Action action : actions) {
            if (dictActions.containsKey(action)) {
                System.out.println("Action already exists"); // vérifier que les actions sont bien uniques
            } else {
                System.out.println(compteur + " : " + action.getName() + " " + Arrays.toString(action.getParameters()));
                dictActions.put(action, compteur++);
            }
        }

        System.out.println("\nInitial State");
        System.out.println("Cardinality : " + problem.getInitialState().cardinality());
        
        BitVector precondPos = problem.getInitialState().getPositiveFluents();
        System.out.println("Initial State Positive Fluents : " + precondPos);

        // itération sur un bitvector de fluents : ici les fluents positifs de l'état initial
        // (index + 1) pour matcher avec le numéro du fluent dans le dictionnaire dictFluents
        int index = precondPos.nextSetBit(0);
        while (index >= 0) {
            System.out.println("Fluent " + (index + 1) + " est dans les fluents positifs");
            index = precondPos.nextSetBit(index + 1);
        }

        BitVector precondNeg = problem.getInitialState().getNegativeFluents();
        System.out.println("Initial State Negative Fluents : " + precondNeg);

        
        // TENTATIVE : Clause correspondant à l'état initial
        // Nb : à ce niveau, on est sur : s0 && {&& nonF | f not€ s0}
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
        System.out.println("Clause de l'état initial : " + initialStateClause);
        encodedProblem.add(initialStateClause);

        // BROUILLON : ALTERNATIVE pour ajouter les fluents négatifs de l'état initial
        // Considérer que les fluents négatifs de l'état initial sont ceux qui ne sont pas dans l'état initial
        // loop through dictFluents and add -fluent if not in initialStateClause
        // for (Fluent fluent : problem.getFluents()) {
        //     if (!initialStateClause.contains(dictFluents.get(fluent))) {
        //         initialStateClause.add(-dictFluents.get(fluent));
        //     }
        // }

        System.out.println();

        // boucle sur les actions
        int i = 1;
        for (Action a: actions) {


            System.out.println(i + " - action : " + a.getName());
            i++;
            
            // AFFICHAGE : action

            BitVector precondPosAction = a.getPrecondition().getPositiveFluents();
            System.out.println("Precondition Positive Fluents : " + precondPosAction);

            BitVector effectPosAction = a.getUnconditionalEffect().getPositiveFluents();
            System.out.println("UnconditionalEffect Positive Fluents : " + effectPosAction);
            
            BitVector effectNegAction = a.getUnconditionalEffect().getNegativeFluents();
            System.out.println("UnconditionalEffect Negative Fluents : " + effectNegAction);

            
            // TENTATIVE : Clause correspondant à l'action
            // Nb : à ce niveau, on est sur : Ai -> {&& precondition(Ai)} &&  {&& effectPo(Ai)} && {&& effectNeg(Ai)}
            
            ArrayList<Integer> actionClause = new ArrayList<>();
            
            // ajouter le numéro de l'action
            actionClause.add(dictActions.get(a));
            System.out.println("Identifiant de l'action : " + dictActions.get(a) + "");

            // ajouter les fluents positifs de la précondition
            index = precondPosAction.nextSetBit(0);
            while (index >= 0) {
                actionClause.add(index + 1);
                index = precondPosAction.nextSetBit(index + 1);
            }
            // ajouter les fluents positifs de l'effet
            index = effectPosAction.nextSetBit(0);
            while (index >= 0) {
                actionClause.add(index + 1);
                index = effectPosAction.nextSetBit(index + 1);
            }
            // ajouter les fluents négatifs de l'effet
            index = effectNegAction.nextSetBit(0);
            while (index >= 0) {
                actionClause.add(-(index + 1));
                index = effectNegAction.nextSetBit(index + 1);
            }
            System.out.println("Clause de l'action : " + actionClause + "\n");

            encodedProblem.add(actionClause);
            
        }

        System.out.println("\nGoal State");

        // AFFICHAGE : état final

        BitVector goalPos = problem.getGoal().getPositiveFluents();
        System.out.println("Goal State Positive Fluents : " + goalPos);

        BitVector goalNeg = problem.getGoal().getNegativeFluents();
        System.out.println("Goal State Negative Fluents : " + goalNeg);

        // TENTATIVE : Clause correspondant à l'état final
        // Nb : à ce niveau, on est sur : {&& f | f € goal} && {&& nonF | nonF € goal}
        ArrayList<Integer> goalStateClause = new ArrayList<>();
        index = goalPos.nextSetBit(0);
        while (index >= 0) {
            goalStateClause.add(index + 1);
            index = goalPos.nextSetBit(index + 1);
        }
        index = goalNeg.nextSetBit(0);
        while (index >= 0) {
            goalStateClause.add(-(index + 1));
            index = goalNeg.nextSetBit(index + 1);
        }
        System.out.println("Clause de l'état final : " + goalStateClause);
        encodedProblem.add(goalStateClause);

        return encodedProblem;
    }

}
