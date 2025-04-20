/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import java.util.Comparator;
import java.util.Optional;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS<G extends Game> {

    public static void main(String[] args) {
        // MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        // Node<G> root = mcts.root;
        // View the full game logic and run the main class in TicTacToe.java or Chess.java
    }

    public MCTS(Node<G> root) {
        this(root, 1.414);
    }

    public MCTS(Node<G> root, double explorationParameter) {
        this.root = root;
        this.explorationParameter = explorationParameter;
    }

    public void runIterations(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<G> selected = select(root);
            if (!selected.isLeaf()) {
                selected.explore();
                if (!selected.children().isEmpty()) {
                    selected = selected.children().iterator().next();
                }
            }
            int result = simulate(selected);
            backPropagate(selected, result);
        }
    }

    private Node<G> select(Node<G> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestChildUCB(node);
        }
        return node;
    }

    private int simulate(Node<G> node) {
        State<G> st = node.state();
        int rootPlayer = root.state().player();
        // if already terminal:
        if (st.isTerminal()) {
        Optional<Integer> w = st.winner();
        if (w.isEmpty()) return 0;
        return w.get() == rootPlayer ? 1 : -1;
        }
        int currentPlayer = st.player();
        // roll‐out
        while (!st.isTerminal()) {
        Move<G> m = st.chooseMove(currentPlayer);
        st = st.next(m);
        currentPlayer = 1 - currentPlayer;
        }
        Optional<Integer> w = st.winner();
        if (w.isEmpty()) return 0;
        return w.get() == rootPlayer ? 1 : -1;
    }

    private void backPropagate(Node<G> node, int result) {
        Node<G> current = node;
        while (current != null) {
            current.setPlayouts(current.playouts() + 1);
            int parentPlayer = current.parent() == null ? root.state().player() : current.parent().state().player();
            double reward = 0.0;
            if (result == 1) {
                reward = (parentPlayer == root.state().player()) ? 1.0 : 0.0; // Win for opener
            } else if (result == -1) {
                reward = (parentPlayer == root.state().player()) ? 0.0 : 1.0; // Loss for opener
            } else if (result == 0) {
                reward = 0.5; // Draw
            }
            current.setWins(current.wins() + reward);
            current = current.parent();
        }
    }

    private Node<G> bestChildUCB(Node<G> node) {
        return node.children().stream()
            .max(Comparator.comparingDouble(c -> calculateUCB(c, node)))
            .orElseThrow(() -> new IllegalStateException("No children in bestChildUCB"));
    }

    private double calculateUCB(Node<G> child, Node<G> node) {
        if (child.playouts() == 0) return Double.POSITIVE_INFINITY;
        double exploitation = child.wins() / child.playouts();
        double exploration = explorationParameter * Math.sqrt(Math.log(node.playouts()) / child.playouts());
        return exploitation + exploration;
    }

    public Node<G> getBestMove() {
        return root.children().stream()
            .max(Comparator.comparingInt(Node::playouts))
            .orElseThrow(() -> new IllegalStateException("No children in getBestMove"));
    }

    public void printTreeStructure(Node<G> node, int depth, int printDepth) {
        // debug
        Node<G> currentNode = node;
        if (currentNode == null) {
            currentNode = root;
        }
        // double value = (currentNode.playouts() == 0 ? 0 : (double) currentNode.wins() / currentNode.playouts());
        double value = currentNode.parent() == null ? Double.NaN : calculateUCB(currentNode, currentNode.parent());
        double wins = currentNode.state().player() == root.state().player() ? (double) currentNode.playouts() - currentNode.wins() : currentNode.wins();
        String status = currentNode.state().isTerminal() ? (currentNode.state().winner().isPresent() ? (currentNode.state().winner().get() == root.state().player() ? "Winner" : "Loser") : "Draw") : "In Progress";
        System.out.println("----".repeat(depth) + "Node: " + currentNode.state().toString().replace("\n", "|") + " Wins: " + wins + 
                           " Playouts: " + currentNode.playouts() + " Value: " + String.format("%.2f", value) + " Status: " + status);
        if (depth <= printDepth) {
            for (Node<G> child : currentNode.children()) {
                printTreeStructure(child, depth + 1, printDepth);
            }
        }
    }

    public void printBestPath(Node<G> node, int printDepth) {
        // debug
        Node<G> currentNode = node;
        if (currentNode == null) {
            currentNode = root;
        }
        for (int i = 0; i < printDepth && !currentNode.children().isEmpty(); i++) {
            double value = currentNode.parent() == null ? Double.NaN : calculateUCB(currentNode, currentNode.parent());
            double wins = currentNode.state().player() == root.state().player() ? (double) currentNode.playouts() - currentNode.wins() : currentNode.wins();
            String status = currentNode.state().isTerminal() ? (currentNode.state().winner().isPresent() ? (currentNode.state().winner().get() == root.state().player() ? "Winner" : "Loser") : "Draw") : "In Progress";
            System.out.println("----".repeat(i) + "Node: " + currentNode.state().toString().replace("\n", "|") + " Wins: " + wins +
                               " Playouts: " + currentNode.playouts() + " Value: " + String.format("%.2f", value) + " Status: " + status);
            currentNode = bestChildUCB(currentNode);
            if (currentNode == null) break;
        }
    }

    private final double explorationParameter;
    private final Node<G> root;
}