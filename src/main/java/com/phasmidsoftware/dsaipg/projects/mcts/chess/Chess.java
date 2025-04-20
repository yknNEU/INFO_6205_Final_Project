package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import com.phasmidsoftware.dsaipg.projects.mcts.tictactoe.MCTS;

public class Chess implements Game<Chess> {

    public static final int X = 1;
    public static final int O = 0;
    public static final int blank = -1;

    public static void main(String[] args) {
        // State<Chess> state = new Chess().runGame();
        // State<Chess> state = new Chess().runGameWithAI();
        State<Chess> state = new Chess().runGameInteractive();
        if (state.winner().isPresent()) System.out.println("Chess: winner is: " + Position.render(state.winner().get()));
        else System.out.println("Chess: draw");
    }

    static Position startingPosition() {
        return new Position(blank);
    }

    State<Chess> runGame() {
        State<Chess> state = start();
        state.render();
        int player = opener();
        while (!state.isTerminal()) {
            state = state.next(state.chooseMove(player));
            player = 1 - player;
            state.render();
        }
        return state;
    }

    State<Chess> runGameWithAI() {
        State<Chess> state = start();
        state.render();
        int player = opener();
        while (!state.isTerminal()) {
            Node<Chess> root = new ChessNode(state); 
            // System.out.println("Available choices: " + state.moves(player).size());
            MCTS mcts = new MCTS(root);
            long startTime = System.currentTimeMillis();
            mcts.runIterations(100000);
            long endTime = System.currentTimeMillis();
            Node<Chess> bestNode = mcts.getBestMove();
            if (bestNode == null) throw new RuntimeException("AI give up for player " + player);
            state = bestNode.state();
            player = 1 - player;
            state.render();
            System.out.println("MCTS chose the branch: " + bestNode.wins() + " wins, " + bestNode.playouts() + " playouts, " + (endTime - startTime) + "ms, "
                               + String.format("%.2f", bestNode.wins() / (double) bestNode.playouts()) + " win rate, " + root.children().size() + " available moves");
        }
        return state;
    }

    private static State<Chess> humanMove(State<Chess> state) {
        while (true) {
            System.out.print("Enter your move (row and column): ");
            try {
                Scanner scanner = new Scanner(System.in);
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                return state.next(new ChessMove(state.player(), row, col));
            } catch (RuntimeException e) {
                System.out.print("Invalid move. Try again: ");
            }
        }
    }

    State<Chess> runGameInteractive() {
        State<Chess> state = start();
        state.render();
        int player = opener();
        while (!state.isTerminal()) {
            if (player == X) {
                state = humanMove(state);
                state.render();
            } else {
                Node<Chess> root = new ChessNode(state); 
                MCTS mcts = new MCTS(root);
                long startTime = System.currentTimeMillis();
                mcts.runIterations(100000);
                long endTime = System.currentTimeMillis();
                Node<Chess> bestNode = mcts.getBestMove();
                // mcts.printTreeStructure(null, 0, 0); // debug
                if (bestNode == null) throw new RuntimeException("AI give up");
                state = bestNode.state();
                System.out.println("MCTS chose the branch: " + bestNode.wins() + " wins, " + bestNode.playouts() + " playouts, " + (endTime - startTime) + "ms, "
                               + String.format("%.2f", bestNode.wins() / (double) bestNode.playouts()) + " win rate, " + root.children().size() + " available moves");
                state.render();
            }
            player = 1 - player;
        }
        return state;
    }

    public int opener() {
        return X;
    }

    public State<Chess> start() {
        return new ChessState();
    }

    public Chess(Random random) {
        this.random = random;
    }

    public Chess(long seed) {
        this(new Random(seed));
    }

    public Chess() {
        this(System.currentTimeMillis());
    }

    private final Random random;

    static class ChessMove implements Move<Chess> {

        public int player() {
            return player;
        }

        public ChessMove(int player, int i, int j) {
            this.player = player;
            this.i = i;
            this.j = j;
        }

        public int[] move() {
            return new int[]{i, j};
        }


        private final int player;
        private final int i;
        private final int j;
    }

    class ChessState implements State<Chess> {

        public Chess game() {
            return Chess.this;
        }
    
        public int player() {
            return switch (position.last) {
                case 0, -1 -> X;
                case 1 -> O;
                default -> blank;
            };
        }

        public Position position() {
            return this.position;
        }

        public Optional<Integer> winner() {
            return position.winner();
        }

        public Random random() {
            return random;
        }

        public Collection<Move<Chess>> moves(int player) {
            if (player == position.last) throw new RuntimeException("consecutive moves by same player: " + player);
            List<int[]> moves = position.moves(player);
            ArrayList<Move<Chess>> list = new ArrayList<>();
            for (int[] coordinates : moves) list.add(new ChessMove(player, coordinates[0], coordinates[1]));
            return list;
        }

        public State<Chess> next(Move<Chess> move) {
            ChessMove chessMove = (ChessMove) move;
            int[] ints = chessMove.move();
            return new ChessState(position.move(move.player(), ints[0], ints[1]));
        }

        public boolean isTerminal() {
            return position.full() || position.winner().isPresent();
        }

        public void render() {
            System.out.println("-".repeat(37));
            System.out.println(position.render());
            System.out.println("-".repeat(37));
        }

        @Override
        public String toString() {
            return "Chess{\n" +
                    position +
                    "\n}";
        }

        public ChessState(Position position) {
            this.position = position;
        }
        
        public ChessState() {
            this(startingPosition());
        }

        private final Position position;

    }
}
