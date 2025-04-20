package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import org.junit.Test;

import static org.junit.Assert.*;

public class MCTSTest {

    @Test
    public void testInit() {
        MCTS<TicTacToe> mcts = new MCTS<TicTacToe>(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        try {
            java.lang.reflect.Field rootField = MCTS.class.getDeclaredField("root");
            rootField.setAccessible(true);
            TicTacToeNode root = (TicTacToeNode) rootField.get(mcts);
            assertEquals(0, root.playouts());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access root field: " + e.getMessage());
        }
    }

    @Test
    public void testRunIterations() {
        MCTS<TicTacToe> mcts = new MCTS<TicTacToe>(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.runIterations(1673);
        try {
            java.lang.reflect.Field rootField = MCTS.class.getDeclaredField("root");
            rootField.setAccessible(true);
            TicTacToeNode root = (TicTacToeNode) rootField.get(mcts);
            assertEquals(1673, root.playouts());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access root field: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBestMove() {
        MCTS<TicTacToe> mcts = new MCTS<TicTacToe>(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.getBestMove();
    }

    @Test
    public void testGetBestMoveWithValidState() {
        MCTS<TicTacToe> mcts = new MCTS<TicTacToe>(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.runIterations(1);
        assertNotNull(mcts.getBestMove());
    }

}