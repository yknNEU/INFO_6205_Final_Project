/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * @return the State of the Game G that this Node represents.
     */
    public State<TicTacToe> state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    /**
     * @return the parent of this Node.
     */
    public Node<TicTacToe> parent() {
        return parent;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(State<TicTacToe> state) {
        children.add(new TicTacToeNode(state, this));
    }

    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate() {
        // Handled by the MCTS class, no implementation here
    }

    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public double wins() {
        return wins;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int playouts() {
        return playouts;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }

    public void setPlayouts(int playouts) {
        this.playouts = playouts;
    }

    public Node<TicTacToe> bestChild() {
        if (children.isEmpty()) return null;
        Node<TicTacToe> bestChild = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (Node<TicTacToe> child : children) {
            double value = child.playouts();
            if (value > bestValue) {
                bestValue = value;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public TicTacToeNode(State<TicTacToe> state, Node<TicTacToe> parent) {
        this.state = state;
        this.parent = parent;
        children = new ArrayList<>();
        playouts = 0;
        wins = 0;
        // initializeNodeData();
    }

    public TicTacToeNode(State<TicTacToe> state) {
        this(state, null);
    }

    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent())
                // wins = 2; // CONSIDER check that the winner is the correct player. We shouldn't need to.
                wins = (winner.get() == state.game().opener()) ? 2 : 0; // a win for the player who plays to this node.
            else
                wins = 1; // a draw.
        }
    }

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    private final Node<TicTacToe> parent;
    private double wins;
    private int playouts;
}