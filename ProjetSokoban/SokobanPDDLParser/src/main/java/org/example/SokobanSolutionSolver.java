package org.example;


import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.parser.ParsedAction;
import fr.uga.pddl4j.plan.SequentialPlan;
import fr.uga.pddl4j.planners.InvalidConfigurationException;
import fr.uga.pddl4j.planners.LogLevel;
import fr.uga.pddl4j.planners.statespace.HSP;
import fr.uga.pddl4j.problem.operator.Action;

import java.io.IOException;

public class SokobanSolutionSolver {


    public SokobanSolutionSolver() {
    }

    public void solve(String domain, String problem){
        // Creates the planner
        HSP planner = new HSP();
        // Sets the domain of the problem to solve
        planner.setDomain(domain);
        // Sets the problem to solve
        planner.setProblem(problem);
        // Sets the timeout of the search in seconds
        planner.setTimeout(1000);
        // Sets log level
        planner.setLogLevel(LogLevel.INFO);
        // Selects the heuristic to use
        planner.setHeuristic(StateHeuristic.Name.MAX);
        // Sets the weight of the heuristic
        planner.setHeuristicWeight(1.2);

        // Solve and print the result
        try {
            planner.solve();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

}

