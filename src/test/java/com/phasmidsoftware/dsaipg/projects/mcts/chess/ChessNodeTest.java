package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChessNodeTest {

    @Test
    public void state() {
        Chess.ChessState state = new Chess().new ChessState();
        ChessNode node = new ChessNode(state);
        assertEquals(state, node.state());
    }

    @Test
    public void white() {
        Chess.ChessState state = new Chess().new ChessState();
        ChessNode node = new ChessNode(state);
        assertTrue(node.white());
    }

    @Test
    public void children() {
        Chess.ChessState state = new Chess().new ChessState();
        ChessNode node = new ChessNode(state);
        assertTrue(node.children().isEmpty());
    }

    @Test
    public void addChild() {
        Chess.ChessState state = new Chess().new ChessState();
        ChessNode node = new ChessNode(state);
        node.addChild(state);
        assertEquals(1, node.children().size());
        assertEquals(state, node.children().iterator().next().state());
    }

}