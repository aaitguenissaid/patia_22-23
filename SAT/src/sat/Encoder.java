package sat;

import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.numeric.NumericVariable;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;

import java.util.ArrayList;
import java.util.List;

public class Encoder {

    private Problem problem;

    public Encoder() {
    }

    public Encoder(Problem problem) {
        this.problem = problem;
    }

    public ArrayList<Integer> encode(){

        ArrayList<Integer> encodedProblem = new ArrayList<>();
        //how to calculate the cnf formula that will be passed to the SAT solver


        // get toutes les actions
        List<Action> actions = problem.getActions();

        System.out.println("Initial State");

        System.out.println("Initial State Positive Fluents : " + problem.getInitialState().getPositiveFluents()+"\n");


        // fluent = propositions

        // get tous les actions
        for (Action a: actions) {
            System.out.println("action : " + a.getName());
            System.out.println("Precondition Positive Fluents : " + a.getPrecondition().getPositiveFluents());
            System.out.println("UnconditionalEffect Positive Fluents : " + a.getUnconditionalEffect().getPositiveFluents()+"\n");

        }

        System.out.println("\nGoal State");
        System.out.println("Goal State Positive Fluents : " + problem.getGoal().getPositiveFluents());
        System.out.println("Goal State Negative Fluents : " + problem.getGoal().getNegativeFluents());

        //        for (int i = 0; i < pb.getActions().size(); i++) {
        //            Action a = pb.getActions().get(0);
        //            BitVector e = a.getUnconditionalEffect().getNegativeFluents();
        //            BitVector e1 = a.getUnconditionalEffect().getPositiveFluents();
        //            BitVector p = a.getPrecondition().getNegativeFluents();
        //        }



        return encodedProblem;
    }

}
