package com.phasmidsoftware.dsaipg.projects.mcts.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Position {

    /**
     * Parse a string of X, O, and . to form a Position.
     *
     * @param grid the grid represented as a String.
     * @param last the last player.
     * @return a Position.
     */
    static Position parsePosition(final String grid, final int last) {
        int[][] matrix = new int[gridSize][gridSize];
        int count = 0;
        String[] rows = grid.split("\\n", gridSize);
        for (int i = 0; i < gridSize; i++) {
            String[] cells = rows[i].split(" ", gridSize);
            for (int j = 0; j < gridSize; j++) {
                int cell = parseCell(cells[j].trim());
                if (cell >= 0) count++;
                matrix[i][j] = cell;
            }
        }
        return new Position(matrix, count, last, -1, -1);
    }

    /**
     * Method to parse a single cell.
     *
     * @param cell the String for the cell.
     * @return a number between -1 and one inclusive.
     */
    static int parseCell(String cell) {
        return switch (cell.toUpperCase()) {
            case "O", "0" -> 0;
            case "X", "1" -> 1;
            default -> -1;
        };
    }

    /**
     * Effect a player's move on this Position.
     *
     * @param player the player (0: O, 1: X)
     * @param x      the first dimension value.
     * @param y      the second dimension value.
     * @return the new Position.
     */
    public Position move(int player, int x, int y) {
        if (full()) throw new RuntimeException("Position is full");
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        int[][] matrix = copyGrid();
        if (matrix[x][y] < 0) {
            matrix[x][y] = player;
            int newCount = count + 1;
            int newLast = player;
            return new Position(matrix, newCount, newLast, x, y);
        }
        throw new RuntimeException("Position is occupied: " + x + ", " + y);
    }

    /**
     * Method to yield all the possible moves available on this Position.
     *
     * @return a list of [x,y] arrays.
     */
    public List<int[]> moves(int player) {
        // if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        // List<int[]> result = new ArrayList<>();
        // for (int i = 0; i < gridSize; i++)
        //     for (int j = 0; j < gridSize; j++)
        //         if (grid[i][j] < 0)
        //             result.add(new int[]{i, j});
        // return result;

        // To optimize the performance, we will not yield all the possible moves here
        // Instead, we will only yield the positions that has at least one occupied cell within the 3x3 area
        if (player == last) throw new RuntimeException("consecutive moves by same player: " + player);
        // If no moves, yield the central 3x3 area
        if (count == 0) {
            List<int[]> result = new ArrayList<>();
            for (int i = 8; i < 11; i++)
                for (int j = 8; j < 11; j++)
                    if (grid[i][j] < 0)
                        result.add(new int[]{i, j});
            return result;
        }
        // Otherwise, yield the positions that has at least one occupied cell within the 3x3 area
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] < 0 && hasNearbyOccupiedCell(i, j)) {
                    candidates.add(new int[]{i, j});
                }
            }
        }
        // We will attempt to move on this position, if nInARow is larger, then this position is more critical
        List<List<int[]>> criticalPositions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            criticalPositions.add(new ArrayList<>());
        }
        // Calculate the critical level for each candidate position, and we will only compute the critical positions to reduce computational cost
        for (int[] candidate : candidates) {
            grid[candidate[0]][candidate[1]] = player; // Try move
            int na = nInARow(candidate[0], candidate[1]); // Calculate critical level
            grid[candidate[0]][candidate[1]] = 1 - player; // Try move for opponent
            int nb = nInARow(candidate[0], candidate[1]); // Calculate critical level
            int n = Math.max(na, nb); // Take the maximum of both players
            grid[candidate[0]][candidate[1]] = -1; // Undo move
            if (n >= 5) criticalPositions.get(0).add(candidate); // Critical position
            else if (n == 4) criticalPositions.get(1).add(candidate); // Less critical position
            else if (n == 3) criticalPositions.get(2).add(candidate); // Less critical position
            else if (n == 2) criticalPositions.get(3).add(candidate); // Less critical position
        }
        if (criticalPositions.get(0).size() > 0) {
            criticalPositions.get(0).addAll(criticalPositions.get(1)); // Return critical positions
            // criticalPositions.get(0).addAll(criticalPositions.get(2)); // Return critical positions
            return criticalPositions.get(0);
        }
        if (criticalPositions.get(1).size() > 0) {
            criticalPositions.get(1).addAll(criticalPositions.get(2)); // Return less critical positions
            // criticalPositions.get(1).addAll(criticalPositions.get(3)); // Return less critical positions
            return criticalPositions.get(1);
        }
        // If there's no critical position, we will yield all the candidates
        return candidates;
    }

    private boolean hasNearbyOccupiedCell(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
                    if (grid[newX][newY] >= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method to rotate this Position by 90 degrees clockwise.
     * TESTME
     *
     * @return a new Position which is rotated from this.
     */
    public Position rotate() {
        int[][] matrix = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++)
                matrix[i][j] = grid[j][gridSize - i - 1];
        return new Position(matrix, count, last, lastY, gridSize - lastX - 1);
    }

    public Optional<Integer> winner() {
        if (count > 8 && fiveInARow()) return Optional.of(last);
        return Optional.empty();
    }

    boolean fiveInARow() {
        if (lastX >= 0 && lastY >= 0 && lastX < gridSize && lastY < gridSize && grid[lastX][lastY] == last) {
            // If we have already labelled the last move, we can check if it is a winning move
            return nInARow(lastX, lastY) >= 5;
        }
        // Otherwise, we need to check all the cells in the grid, which is less efficient
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == last) {
                    if (nInARow(i, j) >= 5) return true;
                }
            }
        }
        return false;
    }

    int nInARow(int x, int y) {
        // Check if the cell is empty
        int current = grid[x][y];
        if (current == -1) return 0;
        List<Integer> list = new ArrayList<>();
        // Check up -> down
        int n = 1;
        for (int i = 1; i < 5; i++) {
            if (x + i < gridSize && grid[x + i][y] == current) n++;
            else break;
        }
        for (int i = 1; i < 5; i++) {
            if (x - i >= 0 && grid[x - i][y] == current) n++;
            else break;
        }
        list.add(n);
        // Check left -> right
        n = 1;
        for (int i = 1; i < 5; i++) {
            if (y + i < gridSize && grid[x][y + i] == current) n++;
            else break;
        }
        for (int i = 1; i < 5; i++) {
            if (y - i >= 0 && grid[x][y - i] == current) n++;
            else break;
        }
        list.add(n);
        // Check diagonal up-left -> down-right
        n = 1;
        for (int i = 1; i < 5; i++) {
            if (x + i < gridSize && y + i < gridSize && grid[x + i][y + i] == current) n++;
            else break;
        }
        for (int i = 1; i < 5; i++) {
            if (x - i >= 0 && y - i >= 0 && grid[x - i][y - i] == current) n++;
            else break;
        }
        list.add(n);
        // Check diagonal up-right -> down-left
        n = 1;
        for (int i = 1; i < 5; i++) {
            if (x + i < gridSize && y - i >= 0 && grid[x + i][y - i] == current) n++;
            else break;
        }
        for (int i = 1; i < 5; i++) {
            if (x - i >= 0 && y + i < gridSize && grid[x - i][y + i] == current) n++;
            else break;
        }
        list.add(n);
        // Return max count
        return list.stream().max(Integer::compareTo).orElse(0);
    }

    int[] projectRow(int i) {
        return grid[i];
    }

    int[] projectCol(int j) {
        int[] result = new int[gridSize];
        for (int i = 0; i < gridSize; i++)
            result[i] = grid[i][j];
        return result;
    }

    int[] projectDiag(boolean b) {
        int[] result = new int[gridSize];
        for (int j = 0; j < gridSize; j++) {
            int i = b ? j : gridSize - j - 1;
            result[j] = grid[i][j];
        }
        return result;
    }

    boolean full() {
        return count == gridSize * gridSize;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                sb.append(render(grid[i][j]));
                if (j < gridSize - 1) sb.append(' ');
            }
            if (i < gridSize - 1) sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                sb.append(grid[i][j]);
                if (j < gridSize - 1) sb.append(',');
            }
            if (i < gridSize - 1) sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return Arrays.deepEquals(grid, position.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    Position(int[][] grid, int count, int last, int lastX, int lastY) {
        this.grid = grid;
        this.count = count;
        this.last = last;
        this.lastX = lastX;
        this.lastY = lastY;
    }

    public Position(int last) {
        this.grid = new int[gridSize][gridSize];
        this.count = 0;
        this.last = last;
        this.lastX = -1;
        this.lastY = -1;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = -1;
            }
        }
    }

    private int[][] copyGrid() {
        int[][] result = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
            result[i] = Arrays.copyOf(grid[i], gridSize);
        return result;
    }

    public static char render(int x) {
        return switch (x) {
            case 0 -> 'O';
            case 1 -> 'X';
            default -> '.';
        };
    }

    private final int[][] grid;
    final int last;
    final int lastX;
    final int lastY;
    private final int count;
    private final static int gridSize = 19;
}
