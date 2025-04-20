package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import java.util.ArrayList;
import java.util.Collection;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class ChessNode implements Node<Chess> {

    public boolean isLeaf() {
        return state().isTerminal();
    }

    public State<Chess> state() {
        return state;
    }

    public boolean white() {
        return state.player() == state.game().opener();
    }

    public Collection<Node<Chess>> children() {
        return children;
    }

    public Node<Chess> parent() {
        return parent;
    }

    public void addChild(State<Chess> state) {
        children.add(new ChessNode(state, this));
    }

    public void backPropagate() {
        // null
    }

    public double wins() {
        return wins;
    }

    public int playouts() {
        return playouts;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }

    public void setPlayouts(int playouts) {
        this.playouts = playouts;
    }

    public Node<Chess> bestChild() {
        if (children.isEmpty()) return null;
        Node<Chess> bestChild = null;
        double bestValue = Double.NEGATIVE_INFINITY;
        for (Node<Chess> child : children) {
            double value = child.playouts();
            if (value > bestValue) {
                bestValue = value;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public ChessNode(State<Chess> state, Node<Chess> parent) {
        this.state = state;
        this.parent = parent;
        children = new ArrayList<>();
        playouts = 0;
        wins = 0;
    }

    public ChessNode(State<Chess> state) {
        this(state, null);
    }
    
    private final State<Chess> state;
    private final ArrayList<Node<Chess>> children;
    private final Node<Chess> parent;
    private double wins;
    private int playouts;
}
