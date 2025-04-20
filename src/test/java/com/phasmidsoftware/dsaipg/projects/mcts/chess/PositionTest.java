package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class PositionTest {

    @Test(expected = RuntimeException.class)
    public void testMove() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . x o . . . . . . .\n" + 
                      ". . . . . . . . . . x . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 0);
        target.move(0, 9, 10);
    }

    @Test
    public void testMove2() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . x o . . . . . . .\n" + 
                      ". . . . . . . . . . x . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 0);
        String expected = ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . x . . . . . . . .\n" + 
                          ". . . . . . . . . . x o . . . . . . .\n" + 
                          ". . . . . . . . . . x . . . . . . . .\n" + 
                          ". . . . . . . . . x o . . . . . . . .\n" + 
                          ". . . . . . . . o . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .\n" + 
                          ". . . . . . . . . . . . . . . . . . .";
        Position moved = target.move(1, 6, 10);
        Position updated = Position.parsePosition(expected, 1);
        assertEquals(updated, moved);
    }

    @Test
    public void testMoves() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . x o . . . . . . .\n" + 
                      ". . . . . . . . . . x . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 0);
        List<int[]> moves = target.moves(1);
        assertEquals(20, moves.size()); // Only moves around existing pieces is valid
    }
    
    @Test
    public void testMoves2() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . o x . . . . . . .\n" + 
                      ". . . . . . . . o . x . . . . . . . .\n" + 
                      ". . . . . . . . o x o . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 0);
        List<int[]> moves = target.moves(1);
        assertEquals(2, moves.size());
        assertArrayEquals(new int[]{6, 12}, moves.get(0));
        assertArrayEquals(new int[]{11, 7}, moves.get(1)); // Only moves that will immediately win or lose are valid
    }

    @Test
    public void testWinner0() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . o x . . . . . . .\n" + 
                      ". . . . . . . . o . x . . . . . . . .\n" + 
                      ". . . . . . . . o x o . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 0);
        assertTrue(target.winner().isEmpty());
    }

    @Test
    public void testWinner1() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . o x . . . . . . .\n" + 
                      ". . . . . . . . o . x . . . . . . . .\n" + 
                      ". . . . . . . . o x o . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . x . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 1);
        Optional<Integer> winner = target.winner();
        assertTrue(winner.isPresent());
        assertEquals(Integer.valueOf(1), winner.get());
    }

    @Test
    public void testWinner2() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . o . . . . . . .\n" + 
                      ". . . . . . . o x x x o . . . . . . .\n" + 
                      ". . . . . . . . . . o . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o x o . o . . . . . .\n" + 
                      ". . . . . . . . . x o x . . . . . . .\n" + 
                      ". . . . . . . . . x x . . . . . . . .\n" + 
                      ". . . . . . . . . x . . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . o . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 1);
        Optional<Integer> winner = target.winner();
        assertTrue(winner.isPresent());
        assertEquals(Integer.valueOf(1), winner.get());
    }

    @Test
    public void testParseCell() {
        assertEquals(0, Position.parseCell("0"));
        assertEquals(0, Position.parseCell("O"));
        assertEquals(0, Position.parseCell("o"));
        assertEquals(1, Position.parseCell("X"));
        assertEquals(1, Position.parseCell("x"));
        assertEquals(1, Position.parseCell("1"));
        assertEquals(-1, Position.parseCell("."));
        assertEquals(-1, Position.parseCell("a"));
    }

    @Test
    public void testnInARow() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . o . o . . . . . . .\n" + 
                      ". . . . . . . o x x x o . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o x o . o . . . . . .\n" + 
                      ". . . . . . . . . x o x . . . . . . .\n" + 
                      ". . . . . . . . . x x . . . . . . . .\n" + 
                      ". . . . . . . . . x . . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . o . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 1);
        assertEquals(7, target.nInARow(7, 9)); // horizontal
    }

    @Test
    public void testFull() {
        String grid = "x o x o x o x o x o x o x o x o x o x\n" + 
                      "x o x o x o x o x o x o x o x o x o x\n" + 
                      "x o o o o o o o o o o o o o o o o o o\n" + 
                      "o o o o o o x x x x x x x x x x x x o\n" + 
                      "x x x x x x o x o x o o o x o x o x o\n" + 
                      "o x o x o x o o x x x o x x x x x x x\n" + 
                      "x x x x x x x x x x o x x x x o o o o\n" + 
                      "o o o o o o x o o x o x o x o x o x o\n" + 
                      "x o x o x o x o o x o o o x o x o x o\n" + 
                      "x o x o x o x o o x o x x o x o x o x\n" + 
                      "x o x o x o x o x x x x o x o x o x o\n" + 
                      "x o x o x o x o x x x o x o x o x o o\n" + 
                      "x o x o x x x x x x x x x x x x x x x\n" + 
                      "x x o o o o x o o o o o o o o o o o x\n" + 
                      "o o o o o o o o o x o x o x o x o x x\n" + 
                      "o x o x o x o x x o x o x o x o x o o\n" + 
                      "x o x o x o x o x o x o x o x o x o o\n" + 
                      "x o x x x x x x x x x x x x x x o o o\n" + 
                      "o o o o o o o o o x o x o x o x o x x";
        Position target = Position.parsePosition(grid, 1);
        assertTrue(target.full());
        String gric = "x o x o x o x o x o x o x o x o x o x\n" + 
                      "x o x o x o x o x o x o x o x o x o x\n" + 
                      "x o o o o o o o o o o o o o o o o o o\n" + 
                      "o o o o o o x x x x x x x x x x x x o\n" + 
                      "x x x x x x o x o x o o o x o x o x o\n" + 
                      "o x o x o x o o x x x o x x x x x x x\n" + 
                      "x x x x x x x x x x o x x x x o o o o\n" + 
                      "o o o o o o x o o x o x o x o x o x o\n" + 
                      "x o x o x o x o o x o o o x o x o x o\n" + 
                      "x o x o x o x o o x o x x o x o x o x\n" + 
                      "x o x o x o x o . x x x o x o x o x o\n" + 
                      "x o x o x o x o x x x o x o x o x o o\n" + 
                      "x o x o x x x x x x x x x x x x x x x\n" + 
                      "x x o o o o x o o o o o o o o o o o x\n" + 
                      "o o o o o o o o o x o x o x o x o x x\n" + 
                      "o x o x o x o x x o x o x o x o x o o\n" + 
                      "x o x o x o x o x o x o x o x o x o o\n" + 
                      "x o x x x x x x x x x x x x x x o o o\n" + 
                      "o o o o o o o o o x o x o x o x o x x";
        Position targetc = Position.parsePosition(gric, 1);
        assertFalse(targetc.full());
    }

    @Test
    public void testRender() {
        String grid = ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . o . . . . . . .\n" + 
                      ". . . . . . . o x x x o . . . . . . .\n" + 
                      ". . . . . . . . . . o . . . . . . . .\n" + 
                      ". . . . . . . . . x o . . . . . . . .\n" + 
                      ". . . . . . . . o x o . o . . . . . .\n" + 
                      ". . . . . . . . . x o x . . . . . . .\n" + 
                      ". . . . . . . . . x x . . . . . . . .\n" + 
                      ". . . . . . . . . x . . . . . . . . .\n" + 
                      ". . . . . . . . x . . . . . . . . . .\n" + 
                      ". . . . . . . o . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .\n" + 
                      ". . . . . . . . . . . . . . . . . . .";
        Position target = Position.parsePosition(grid, 1);
        assertEquals(grid.toLowerCase(), target.render().toLowerCase());
    }
}
