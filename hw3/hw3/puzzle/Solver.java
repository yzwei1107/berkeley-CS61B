package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Solves puzzles using A* algorithm
 * @author moboa
 */

public class Solver {
    private int moves;
    private Iterable<WorldState> sequenceToSolution;

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState state;
        private int movesFromInitialState;
        private SearchNode previousNode;
        private int estimatedDistanceToGoal;

        private SearchNode(WorldState state, int moves, SearchNode previousNode) {
            this.state = state;
            this.movesFromInitialState = moves;
            this.previousNode = previousNode;
            this.estimatedDistanceToGoal = state.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            int thisPriority = this.movesFromInitialState + this.estimatedDistanceToGoal;
            int oPriority = o.movesFromInitialState + o.estimatedDistanceToGoal;

            return thisPriority - oPriority;
        }
    }

    public Solver(WorldState initial) {
        SearchNode goalNode = findGoalNode(initial);
        moves = goalNode.movesFromInitialState;
        sequenceToSolution = pathToNode(goalNode);
    }

    /* Finds and returns the goal node */
    private SearchNode findGoalNode(WorldState initial) {
        MinPQ<SearchNode> nodePQ = new MinPQ<>();
        Set<WorldState> addedStates = new HashSet<>();

        nodePQ.insert(new SearchNode(initial, 0, null));
        addedStates.add(initial);

        while (nodePQ.min().estimatedDistanceToGoal != 0) {
            SearchNode currentMinNode = nodePQ.delMin();

            for (WorldState neighbourState : currentMinNode.state.neighbors()) {
                if (!addedStates.contains(neighbourState)) {
                    int movesFromStart = currentMinNode.movesFromInitialState + 1;
                    nodePQ.insert(new SearchNode(neighbourState, movesFromStart, currentMinNode));
                    addedStates.add(neighbourState);
                }
            }
        }
        return nodePQ.min();
    }

    /* Returns an iterable path to the input node */
    private LinkedList<WorldState> pathToNode(SearchNode node) {
        LinkedList<WorldState> path = new LinkedList<WorldState>();
        SearchNode currentNode = node;

        while (currentNode != null) {
            path.addFirst(currentNode.state);
            currentNode = currentNode.previousNode;
        }

        return path;
    }

    public int moves() {
        return moves;

    }

    public Iterable<WorldState> solution() {
        return sequenceToSolution;
    }
}
