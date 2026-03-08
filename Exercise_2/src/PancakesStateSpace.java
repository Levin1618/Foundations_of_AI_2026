// Done by Levin and Omar

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


// Implementation of StateSpace
public class PancakesStateSpace implements StateSpace {

    // fields:
    private PancakesState initState;
    private PancakesState goalState;
    private int numberOfPancakes;


    // constructor
    private PancakesStateSpace(PancakesState initState, int numberOfPancakes) {
        this.initState = initState;
        ArrayList<Integer> goalPancakes = new ArrayList<Integer>();
        for (int i = 1; i <= numberOfPancakes; i++) {
            goalPancakes.add(i);
        }
        this.goalState = new PancakesState(goalPancakes);
        this.numberOfPancakes = numberOfPancakes;

        System.out.println("Instantiating problem instance with "
                + numberOfPancakes + " pancakes...");
    }

    // Implementation of the State interface
    private static class PancakesState implements State {

        // fields
        public ArrayList<Integer> pancakes;

        // constructor
        public PancakesState(ArrayList<Integer> pancakes) {
            this.pancakes = new ArrayList<Integer>(pancakes);
        }

        public boolean equals(PancakesState other) {
            return pancakes.equals(other.pancakes);
        }

        public String toString() {
            return pancakes.toString(); // toString is already implemented for ArrayList<Integer>
        }
    }


    // Implementation of the Action interface
    private static class PancakesAction implements Action {

        // fields
        public int flipIndex;

        // Constructor
        public PancakesAction(int flipIndex) {
            this.flipIndex = flipIndex;
        }

        public int cost() {
            return 1;
        }

        public String toString() {
            return "flipped at index " + flipIndex;
        }
    }


    public State init() {
        // Just return the initial state that we stored.
        return initState;
    }


    public boolean isGoal(State s_) {
        PancakesState s = (PancakesState) s_;
        return s.equals(goalState);
    }


    public ArrayList<ActionStatePair> succ(State s_) {
        PancakesState s = (PancakesState) s_;
        ArrayList<ActionStatePair> result = new ArrayList<ActionStatePair>();
        for (int k = 2; k <= numberOfPancakes; k++) { // We start for k = 2 since flipping the top pancake doesn't do anything
            result.add(createSuccessor(s, k));
        }
        return result;
    }


    // Helper function that generates a single (action, successorState) pair
    private ActionStatePair createSuccessor(PancakesState s, int k) {

        // reverse first (= top) k pancakes
        ArrayList<Integer> newPancakes = new ArrayList<Integer>(s.pancakes);
        ArrayList<Integer> flippedPart = new ArrayList<Integer>();
        for (int i = k - 1; i >= 0; i--) {
            flippedPart.add(s.pancakes.get(i));
        }
        for (int i = 0; i < k; i++) {
            newPancakes.set(i, flippedPart.get(i));
        }

        PancakesAction action = new PancakesAction(k);
        PancakesState succState = new PancakesState(newPancakes);

        return new ActionStatePair(action, succState);
    }

    public int cost(Action a) {
        return a.cost();
    }

    // part b)
    public static StateSpace buildFromCmdline(ArrayList<String> args) {
        if (args.size() != 1) {
            Errors.usageError("need one input file argument");
        }

        String filename = args.get(0);
        System.out.println("Reading input from file " + filename + "...");
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            Errors.fileError("input file not found: " + filename);
            scanner = new Scanner(""); // unreachable; silences compiler
        }

        //int numBlocks = scanner.nextInt();
        PancakesState init = scanState(scanner);
        if (scanner.hasNext())
            Errors.fileError("expected end of file");
        scanner.close();

        return new PancakesStateSpace(init, init.pancakes.size());
    }


    private static PancakesState scanState(Scanner scanner) {
        ArrayList<Integer> pancakes = new ArrayList<Integer>();

        while (scanner.hasNextInt()) {
            pancakes.add(scanner.nextInt());
        }

        if (pancakes.isEmpty()) {
            Errors.fileError("there are no pancakes");
        }

        int numPancakes = pancakes.size();
        boolean[] usedPancakes = new boolean[numPancakes + 1];
        //int blocksRemaining = numBlocks;

        // Check if pancakes are valid and if there are duplicates
        for (int pancake : pancakes) {
            if (pancake <= 0 || pancake > numPancakes) {
                Errors.fileError("invalid pancake");
            }
            if (usedPancakes[pancake]) {
                Errors.fileError("duplicate pancake");
            }
            usedPancakes[pancake] = true;
        }

        // Check if there are missing pancakes
        for (int i = 1; i <= numPancakes; i++) {
            if (!usedPancakes[i]) {
                Errors.fileError("missing pancake");
            }
        }

        return new PancakesState(pancakes);
    }
}

