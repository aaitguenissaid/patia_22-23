./run.sh pddl/hanoi_domain.pddl pddl/hanoi_problem.pddl


./run.sh ../ProjetSokoban/SokobanPDDLParser/src/main/resources/domain.pddl ../ProjetSokoban/SokobanPDDLParser/results/PDDLproblems/Scoria-Level1.pddl 


            // TODO : make arrays larger with size of steps. 
            
            // TODO : add transitions...
        /**
         * InitialState
         * Actions
         * Fluents x steps.
         * Goal
         * */

        System.out.println("\nInitial state:");
        System.out.println("Length " + precondPos.length());
        System.out.println("precondPos: " + precondPos);
        System.out.println("precondNeg: " + precondNeg);

