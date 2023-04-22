package sat;

import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.parser.DefaultParsedProblem;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.plan.SequentialPlan;
import fr.uga.pddl4j.planners.AbstractPlanner;
import fr.uga.pddl4j.problem.DefaultProblem;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.State;
import fr.uga.pddl4j.problem.operator.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import picocli.CommandLine;

import java.util.*;

/**
 * The class is an example. It shows how to create a simple SAT search planner able to
 * solve an ADL problem by choosing the heuristic to used and its weight.
 *
 * @author D. Pellier
 * @version 4.0 - 30.11.2021
 */
@CommandLine.Command(name = "SAT", version = "SAT 1.0", description = "Solves a specified planning problem using SAT search strategy.", sortOptions = false, mixinStandardHelpOptions = true, headerHeading = "Usage:%n", synopsisHeading = "%n", descriptionHeading = "%nDescription:%n%n", parameterListHeading = "%nParameters:%n", optionListHeading = "%nOptions:%n")
public class SAT extends AbstractPlanner {

    /**
     * The class logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(SAT.class.getName());

    /**
     * The weight of the heuristic.
     */
    private double heuristicWeight;

    /**
     * The name of the heuristic used by the planner.
     */

    private StateHeuristic.Name heuristic;

    /**
     * Instantiates the planning problem from a parsed problem.
     *
     * @param problem the problem to instantiate.
     * @return the instantiated planning problem or null if the problem cannot be instantiated.
     */
    @Override
    public Problem instantiate(DefaultParsedProblem problem) {
        final Problem pb = new DefaultProblem(problem);
        pb.instantiate();
        return pb;
    }

    /**
     * Search a solution plan to a specified encoded problem using SAT4J.
     *
     * @param problem the problem to solve.
     * @return the plan found or null if no plan was found.
     */
    @Override
    public Plan solve(final Problem problem) {
        // First we create an instance of the heuristic to use to guide the search
        setHeuristic(StateHeuristic.Name.FAST_FORWARD);
        final StateHeuristic heuristic = StateHeuristic.getInstance(this.getHeuristic(), problem);
        final State init = new State(problem.getInitialState());
        int estimatedSteps = heuristic.estimate(init, problem.getGoal());
        System.out.println("estimatedSteps = " + estimatedSteps);

        //ENCODER LE PROBLEME AVANT DE LE RESOUDRE
        Encoder satEncoder = new Encoder(problem);      //créer l'encodeur
        // TODO : get the value of steps from command line
        int MAX_LOOP = 1;
        satEncoder.encodeInitAndStepsAndGoal(estimatedSteps);

        for (int steps = estimatedSteps; steps < estimatedSteps + MAX_LOOP; steps++) {
            //satEncoder.encodeStepWithGoal(steps);
            ArrayList<ArrayList<Integer>> encodedProblem = satEncoder.getEncodedProblem();  //récupérer le plan
            ISolver solver = SolverFactory.newDefault();
            solver.setTimeout(600);

            try {
                // formatter le problème pour SAT4J
                for (ArrayList<Integer> clause : encodedProblem) {
                    int[] formattedClause = clause.stream().mapToInt(i -> i).toArray();
                    System.err.println("formattedClause : " + Arrays.toString(formattedClause));
                    solver.addClause(new VecInt(formattedClause));
                }

                // tester si le problème est satisfiable et afficher le résultat
                if (solver.isSatisfiable()) {

                    System.out.println("\nSatisfiable !");

                    // récupérer le modèle calculé par SAT4J
                    int[] model = solver.findModel();

                    // convertir le modèle en plan séquentiel
                    SequentialPlan plan = new SequentialPlan();

                    List<Action> actions = problem.getActions();
                    int nbActions = actions.size();
                    int nbFluents = problem.getFluents().size();
                    int nbVariables = nbFluents + nbActions;
                    System.out.println("nbFluents : " + nbFluents);
                    System.out.println("nbActions : " + nbActions);
                    System.out.println("nbVariables : " + nbVariables);

                    // pour chaque littéral positif du modèle calculé par SAT4J, ajouter l'action correspondante au plan
                    System.out.println("model : " + Arrays.toString(model));

                    String modelPos = "";
                    String actionsPos = "";

                    Set<Integer> A = satEncoder.getSetA();
                    Set<Integer> modelSet = new HashSet<>();
                    for (int m : model) {
                        if (m > 0) modelSet.add(m);
                    }
                    Set<Integer> I = new HashSet<>(A);
                    I.retainAll(modelSet); // I now contains the intersection of A and F

                    System.out.println("intersection : " + I);
                    /*
                    for (int m : model) {
                        //m = m-1;
                        if (m > 0) {
                            modelPos.append(m).append(" ");
                            int actionIndex = m % nbVariables;
                            if (actionIndex >= nbFluents) {
                                actionsPos.append(actionIndex).append(" ");
                                Action action = actions.get(actionIndex); //TODO : calculer l'index de l'action
                                plan.add(0, action);
                            }
                        }
                    }*/
                    System.out.println("\nSteps : " + steps);
                    System.out.println("ModelPos : " + modelPos);
                    System.out.println("actionsPos : " + actionsPos);
                    System.out.print("\n");
                    if (!plan.isEmpty()) return plan;
                } else {
                    System.out.println("Unsatisfiable !");
                }
            } catch (ContradictionException e) {
                System.out.println("Unsatisfiable (trivial)!");
            } catch (TimeoutException e) {
                System.out.println("Timeout, sorry!");
            }
        }
        return null;
    }

    @Override
    public boolean isSupported(Problem problem) {
        return false;
    }

    /**
     * Sets the weight of the heuristic.
     *
     * @param weight the weight of the heuristic. The weight must be greater than 0.
     * @throws IllegalArgumentException if the weight is strictly less than 0.
     */
    @CommandLine.Option(names = {"-w", "--weight"}, defaultValue = "1.0", paramLabel = "<weight>", description = "Set the weight of the heuristic (preset 1.0).")
    public void setHeuristicWeight(final double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight <= 0");
        }
        this.heuristicWeight = weight;
    }

    /**
     * Set the name of heuristic used by the planner to the solve a planning problem.
     *
     * @param heuristic the name of the heuristic.
     */
    @CommandLine.Option(names = {"-e", "--heuristic"}, defaultValue = "FAST_FORWARD", description = "Set the heuristic : AJUSTED_SUM, AJUSTED_SUM2, AJUSTED_SUM2M, COMBO, " + "MAX, FAST_FORWARD SET_LEVEL, SUM, SUM_MUTEX (preset: FAST_FORWARD)")
    public void setHeuristic(StateHeuristic.Name heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Returns the name of the heuristic used by the planner to solve a planning problem.
     *
     * @return the name of the heuristic used by the planner to solve a planning problem.
     */
    public final StateHeuristic.Name getHeuristic() {
        return this.heuristic;
    }

    /**
     * Returns the weight of the heuristic.
     *
     * @return the weight of the heuristic.
     */
    public final double getHeuristicWeight() {
        return this.heuristicWeight;
    }

    /**
     * The main method of the <code>SAT</code> planner.
     *
     * @param args the arguments of the command line.
     */
    public static void main(String[] args) {
        try {
            final SAT planner = new SAT();
            CommandLine cmd = new CommandLine(planner);
            cmd.execute(args);
        } catch (IllegalArgumentException e) {
            LOGGER.fatal(e.getMessage());
        }
    }
}
