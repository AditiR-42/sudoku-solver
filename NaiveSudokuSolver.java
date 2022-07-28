public class NaiveSudokuSolver {

    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static void main(String[] args) {

        // 3x3 test grid set to the "world's hardest sudoku" created by Arto Inkala in 2012
        // Average solve time = 30.5 milliseconds
        int[][] grid = {
            {8, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 6, 0, 0, 0, 0, 0},
            {0, 7, 0, 0, 9, 0, 2, 0, 0},
            {0, 5, 0, 0, 0, 7, 0, 0, 0},
            {0, 0, 0, 0, 4, 5, 7, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 3, 0},
            {0, 0, 1, 0, 0, 0, 0, 6, 8},
            {0, 0, 8, 5, 0, 0, 0, 1, 0},
            {0, 9, 0, 0, 0, 0, 4, 0, 0}
        };

        printGrid(grid);
        System.out.println();

        long startTime = System.currentTimeMillis();
        if (solveGrid(grid)) {
            long endTime = System.currentTimeMillis();
            System.out.println("Solved successfully!");
            String time = String.format("Time: %d milliseconds", endTime - startTime);
            System.out.println(time);
            System.out.println();
        }
        else {
            System.out.println("Board unsolvable");
        }

        printGrid(grid);
        System.out.println();

    }

    private static void printGrid(int[][] grid) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % BOX_SIZE == 0 && row != 0) {
                for (int i = 0; i < GRID_SIZE + BOX_SIZE - 1; i++) {
                    System.out.print("-");
                }
                System.out.println();
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % BOX_SIZE == 0 && col != 0) {
                    System.out.print("|");
                }
                System.out.print(grid[row][col]);
            }
            System.out.println();
        }
    }

    private static boolean numberInRow(int[][] grid, int num, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (grid[row][i] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean numberInColumn(int[][] grid, int num, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (grid[i][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean numberInBox(int[][] grid, int num, int row, int col) {
        int localBoxRow = row - (row % BOX_SIZE);
        int localBoxColumn = col - (col % BOX_SIZE);

        for (int i = localBoxRow; i < localBoxRow + BOX_SIZE; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + BOX_SIZE; j++) {
                if (grid[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidPlacement(int[][] grid, int num, int row, int col) {
        return !numberInRow(grid, num, row)
            && !numberInColumn(grid, num, col) && 
            !numberInBox(grid, num, row, col);
    }

    private static boolean solveGrid(int[][] grid) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col ++) {
                if (grid[row][col] == 0) {
                    for (int testNum = 1; testNum <= GRID_SIZE; testNum++) {
                        if (isValidPlacement(grid, testNum, row, col)) {
                            grid[row][col] = testNum;

                            if (solveGrid(grid)) {
                                return true;
                            }
                            else {
                                grid[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

}