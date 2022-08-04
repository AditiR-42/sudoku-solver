public class FastSudokuSolver {

    public static void main(String[] args) {

        // 25x25 test grid
        int[][] grid = {
            {16, 23, 7, 0, 0, 24, 0, 4, 0, 0, 0, 10, 0, 0, 0, 1, 0, 18, 0, 0, 8, 21, 14, 0, 17},
            {0, 0, 20, 0, 0, 19, 15, 16, 0, 0, 0, 0, 0, 5, 24, 4, 0, 2, 14, 23, 0, 0, 18, 0, 7},
            {9, 2, 12, 0, 0, 0, 0, 0, 20, 11, 13, 0, 0, 7, 0, 0, 0, 0, 0, 6, 0, 0, 10, 25, 1},
            {4, 0, 0, 0, 19, 0, 0, 0, 14, 0, 8, 0, 0, 23, 21, 10, 0, 9, 7, 17, 0, 0, 0, 0, 0},
            {18, 0, 0, 0, 0, 0, 1, 17, 10, 0, 11, 15, 19, 0, 0, 12, 0, 20, 0, 0, 0, 13, 0, 0, 0},
            {0, 7, 1, 3, 0, 0, 12, 0, 0, 0, 0, 0, 16, 0, 0, 8, 20, 11, 0, 0, 0, 0, 0, 9, 21},
            {0, 6, 0, 10, 0, 0, 2, 21, 18, 0, 12, 19, 23, 0, 0, 0, 0, 0, 24, 16, 1, 0, 0, 14, 0},
            {8, 20, 0, 18, 16, 11, 0, 0, 24, 0, 9, 0, 0, 0, 3, 0, 0, 0, 22, 0, 12, 0, 0, 10, 4},
            {0, 0, 0, 0, 0, 1, 0, 0, 9, 22, 4, 0, 0, 0, 0, 0, 17, 23, 2, 0, 24, 8, 13, 0, 0},
            {15, 21, 0, 17, 9, 8, 0, 0, 0, 0, 0, 18, 7, 2, 0, 0, 1, 0, 0, 0, 0, 0, 19, 0, 0},
            {0, 4, 0, 16, 0, 0, 0, 14, 0, 0, 0, 22, 0, 10, 0, 0, 11, 17, 8, 0, 21, 24, 9, 0, 0},
            {0, 10, 11, 22, 0, 0, 0, 0, 0, 21, 24, 3, 0, 17, 1, 7, 0, 0, 18, 0, 5, 0, 0, 0, 14},
            {0, 0, 0, 0, 17, 10, 4, 0, 0, 20, 0, 0, 0, 0, 0, 25, 0, 0, 9, 5, 16, 0, 0, 0, 0},
            {25, 0, 0, 0, 6, 0, 16, 0, 0, 19, 14, 13, 0, 8, 9, 23, 0, 0, 0, 0, 0, 12, 4, 18, 0},
            {0, 0, 23, 21, 20, 0, 7, 18, 13, 0, 0, 4, 0, 6, 0, 0, 0, 3, 0, 0, 0, 17, 0, 19, 0},
            {0, 0, 10, 0, 0, 0, 0, 0, 17, 0, 0, 7, 14, 12, 0, 0, 0, 0, 0, 4, 25, 16, 0, 22, 19},
            {0, 0, 14, 11, 13, 0, 10, 19, 12, 0, 0, 0, 0, 0, 16, 18, 15, 0, 0, 7, 0, 0, 0, 0, 0},
            {5, 16, 0, 0, 24, 0, 14, 0, 0, 0, 17, 0, 0, 0, 11, 0, 19, 0, 0, 1, 6, 10, 0, 4, 18},
            {0, 18, 0, 0, 3, 21, 11, 0, 0, 0, 0, 0, 6, 13, 22, 0, 25, 24, 10, 0, 0, 5, 0, 23, 0},
            {12, 19, 0, 0, 0, 0, 0, 8, 2, 23, 0, 0, 9, 0, 0, 0, 0, 0, 6, 0, 0, 7, 15, 11, 0},
            {0, 0, 0, 9, 0, 0, 0, 12, 0, 7, 0, 0, 10, 24, 14, 0, 5, 19, 1, 0, 0, 0, 0, 0, 13},
            {0, 0, 0, 0, 0, 22, 23, 24, 0, 14, 21, 12, 0, 0, 17, 0, 9, 0, 0, 0, 10, 0, 0, 0, 3},
            {23, 25, 18, 0, 0, 4, 0, 0, 0, 0, 0, 9, 0, 0, 20, 6, 24, 0, 0, 0, 0, 0, 12, 1, 16},
            {14, 0, 19, 0, 0, 15, 3, 1, 0, 9, 7, 5, 0, 0, 0, 0, 0, 8, 11, 12, 0, 0, 17, 0, 0},
            {11, 0, 16, 5, 1, 0, 0, 13, 0, 8, 0, 0, 0, 25, 0, 0, 0, 10, 0, 14, 0, 0, 24, 2, 23}
        };
        
        System.out.println();
        printOriginalGrid(grid);
        SolveCoverMatrix newSudoku = new SolveCoverMatrix();
        newSudoku.solve(grid);
        System.out.println();
    }

    private static void printOriginalGrid(int[][] grid) {
        int GRID_SIZE = grid.length;
        int BOX_SIZE = (int) Math.sqrt(grid.length);
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % BOX_SIZE == 0 && row != 0) {
                for (int i = 0; i < GRID_SIZE * 3 + BOX_SIZE; i++) {
                    System.out.print("-");
                }
                System.out.println();
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % BOX_SIZE == 0 && col != 0) {
                    System.out.print("| ");
                }
                String num = String.format("%02d", grid[row][col]);
                System.out.print(num);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}