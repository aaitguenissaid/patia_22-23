package fr.uga.pddl4j.examples.sat;

import fr.uga.pddl4j.problem.Problem;
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
    // encode a problem in SAT-CNF
    public ArrayList<Integer> encode(){

        ArrayList<Integer> encodedProblem = new ArrayList<>();

        // get toutes les actions
        List<Action> actions = problem.getActions();

        // fluent = propositions

        for (Action a: actions) {
            BitVector act = a.get
            BitVector posFluents = a.getUnconditionalEffect().getPositiveFluents();
            BitVector negFluents = a.getUnconditionalEffect().getNegativeFluents();

            //BitVector posConds = a.getPrecondition().getPositiveFluents();
            BitVector p = a.getPrecondition().getNegativeFluents();


        }

        //        for (int i = 0; i < pb.getActions().size(); i++) {
        //            Action a = pb.getActions().get(0);
        //            BitVector e = a.getUnconditionalEffect().getNegativeFluents();
        //            BitVector e1 = a.getUnconditionalEffect().getPositiveFluents();
        //            BitVector p = a.getPrecondition().getNegativeFluents();
        //        }



        return encodedProblem;
    }

}
