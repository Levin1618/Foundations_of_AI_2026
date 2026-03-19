import java.util.ArrayList;

public class IterativeDeepeningSearch extends SearchAlgorithmBase {

    private long nodes_count;
    private long total_nodes_count = 0;

    public IterativeDeepeningSearch(String args[]) {
        super(args);
    }

    // Iterative Deepening DFS Slide 24/35
    @Override
    protected ArrayList<Action> run() {

        for (int depth_limit = 0; true; depth_limit +=  1) {
            nodes_count = 0;
            ArrayList<Action> solution = depth_limited_search(stateSpace.init(), depth_limit);

            System.out.println("depth " + depth_limit + ": "
                    + nodes_count + " generated nodes");

            total_nodes_count += nodes_count;

            if (solution != null) {
                System.out.println("total generated nodes: " + total_nodes_count);
                return solution;
            }
        }
    }


    // Like on slide 23/35
    private ArrayList<Action> depth_limited_search(State s, int depth_limit) {

        nodes_count += 1; // Print the number of generated nodes (i.e., the number of recursive calls to depth-limited search

        if (stateSpace.isGoal(s)) {
            return new ArrayList<Action>();
        }

        if (depth_limit > 0) {

            for (ActionStatePair pair : stateSpace.succ(s)) {
                ArrayList<Action> solution = depth_limited_search(pair.state, depth_limit - 1);

                if (solution != null) {
                    solution.add(0, pair.action);  // We add to the front of the array: "push_front(a)"
                    return solution;
                }
            }
        }

        return null;
    }




    public static void main(String args[]) {
        new IterativeDeepeningSearch(args).runSearchAlgorithm();
    }

}