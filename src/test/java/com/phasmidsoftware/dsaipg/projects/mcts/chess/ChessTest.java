package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Optional;

import org.junit.Test;

public class ChessTest {

     @Test
    public void runGame() {
        long seed = 0L;
        Chess target = new Chess(seed); // games run here will all be deterministic.
        State<Chess> state = target.runGame();
        Optional<Integer> winner = state.winner();
        if (winner.isPresent()) assertEquals(Integer.valueOf(Chess.X), winner.get());
        else fail("no winner");
    }
    
}
