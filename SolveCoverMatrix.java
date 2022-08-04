import java.util.Arrays;
import java.util.List;

public class SolveCoverMatrix {
    private final int EMPTY_CELL = 0;
    private final int CONSTRAINTS = 4;

    private int GRID_SIZE;
    private int BOX_SIZE;

    // Convert a sudoku grid into an exact cover matrix with 0s and 1s
    public int[][] convertToCoverMatrix(int[][] grid) {
        GRID_SIZE = grid.length;
        BOX_SIZE = (int) Math.sqrt(grid.length);
        
        int[][] coverMatrix = buildCoverMatrix();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int num = grid[row][col];

                if (num != EMPTY_CELL) {
                    for (int digit = 1; digit <= GRID_SIZE; digit++) {
                        if (digit != num) {
                            Arrays.fill(coverMatrix[coverMatrixIndex(digit, row, col)], 0);
                        }
                    }
                }
            }
        }
        return coverMatrix;
    }

    // Return index in cover matrix for a given number, row, and column
    // For example, a 3x3 sudoku would form a matrix with 324 columns and 729 rows
    // 4 types of constraints and 9 distinct values = 9^2 * 4 = 324 constraints
    // 9 rows * 9 columns * 9 distinct values = 729 rows
    private int coverMatrixIndex(int num, int row, int col) {
        return row * GRID_SIZE * GRID_SIZE + col * GRID_SIZE + (num - 1);
    }

    // Create cover matrix with 4 constraints: position, row, column, and box
    private int[][] buildCoverMatrix() {
        int[][] coverMatrix =
            new int[GRID_SIZE * GRID_SIZE * GRID_SIZE][GRID_SIZE * GRID_SIZE * CONSTRAINTS];
        
        int header = 0;
        header = createCellConstraints(coverMatrix, header);
        header = createRowConstraints(coverMatrix, header);
        header = createColumnConstraints(coverMatrix, header);
        createBoxConstraints(coverMatrix, header);
        
        return coverMatrix;
    }

    private int createCellConstraints(int[][] matrix, int header) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++, header++) {
                for (int num = 1; num <= GRID_SIZE; num++) {
                    int index = coverMatrixIndex(num, row, col);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createRowConstraints(int[][] matrix, int header) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int num = 1; num <= GRID_SIZE; num++, header++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    int index = coverMatrixIndex(num, row, col);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createColumnConstraints(int[][] matrix, int header) {
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int num = 1; num <= GRID_SIZE; num++, header++) {
                for (int row = 0; row < GRID_SIZE; row++) {
                    int index = coverMatrixIndex(num, row, col);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createBoxConstraints(int[][] matrix, int header) {
        for (int row = 0; row < GRID_SIZE; row += BOX_SIZE) {
            for (int col = 0; col < GRID_SIZE; col += BOX_SIZE) {
                for (int num = 1; num <= GRID_SIZE; num++, header++) {
                    for (int rowChange = 0; rowChange < BOX_SIZE; rowChange++) {
                        for (int colChange = 0; colChange < BOX_SIZE; colChange++) {
                            int index = coverMatrixIndex(num, row + rowChange, col + colChange);
                            matrix[index][header] = 1;
                        }
                    }
                }
            }
        }
        return header;
    }

    public void solve(int[][] grid) {
        printGrid(grid);
        System.out.println();

        long startTime = System.currentTimeMillis();
        DancingLinks links = new DancingLinks(convertToCoverMatrix(grid));
        links.algorithmX(0);
        long endTime = System.currentTimeMillis();
        System.out.println("Solved successfully!");
            String time = String.format("Time: %d milliseconds", endTime - startTime);
            System.out.println(time);
            System.out.println();
        printGrid(DLXtoMatrix(links.fullSolution));    

    }

    private void printGrid(int[][] grid) {
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

    // Convert full solution linked list back to a matrix (the solved sudoku)
    private int[][] DLXtoMatrix(List<Node> fullSolution) {
        int[][] solvedGrid = new int[GRID_SIZE][GRID_SIZE];

        for (Node current : fullSolution) {
            Node rowColNode = current;
            int min = rowColNode.column.getName();

            for (Node temp = current.right; temp != current; temp = temp.right) {
                if (temp.column.getName() < min) {
                    min = temp.column.getName();
                    rowColNode = temp;
                }
            }

            solvedGrid[rowColNode.column.getName() / GRID_SIZE][rowColNode.column.getName() % GRID_SIZE]
                = (rowColNode.right.column.getName() % GRID_SIZE) + 1;
        }
        return solvedGrid;
    }
}
