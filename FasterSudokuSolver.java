import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class FasterSudokuSolver {
    
    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    // 4 Constraints: position, row, column, and box
    public static final int CONSTRAINTS = 4;
    
    int Grid[][];

    public static void main(String[] args) {

        // 3x3 test grid set to the "world's hardest sudoku" created by Arto Inkala in 2012
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

        FasterSudokuSolver sudoku = new FasterSudokuSolver();
        
        long startTime = System.currentTimeMillis();
        int[][] solvedGrid = sudoku.solveGrid();
        long endTime = System.currentTimeMillis();
        System.out.println("Solved successfully!");
        String time = String.format("Time: %d milliseconds", endTime - startTime);
        System.out.println(time);
        System.out.println();

        printGrid(solvedGrid);
        System.out.println();

    }

    public int[][] solveGrid() {
        DancingLinks solution = new DancingLinks(Grid);
        int[][] solvedGrid = solution.runAlg(Grid);
        return solvedGrid;
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


    // STEP ONE:
    // Convert the sudoku grid into an exact cover matrix with 0s and 1s
    // For example, a 3x3 sudoku would form a matrix with 324 columns and 729 rows
    // 4 types of constraints and 9 distinct values = 9^2 * 4 = 324 constraints
    // 9 rows * 9 columns * 9 distinct values = 729 rows

    private int coverMatrixIndex(int num, int row, int col) {
        return row * GRID_SIZE * GRID_SIZE + col * GRID_SIZE + num;
    }

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
                for (int num = 0; num < GRID_SIZE; num++) {
                    int index = coverMatrixIndex(num, row, col);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createRowConstraints(int[][] matrix, int header) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int num = 0; num < GRID_SIZE; num++, header++) {
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
            for (int num = 0; num < GRID_SIZE; num++, header++) {
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
                for (int num = 0; num < GRID_SIZE; num++, header++) {
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

    private int[][] convertToCoverMatrix(int[][] grid) {
        int[][] coverMatrix = buildCoverMatrix();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int num = grid[row][col];

                if (num != 0) {
                    for (int digit = 1; digit <= GRID_SIZE; digit++) {
                        if (digit != num) {
                            Arrays.fill(coverMatrix[coverMatrixIndex(num, row, col)], 0);
                        }
                    }
                }
            }
        }
        return coverMatrix;
    }


    // STEP TWO:
    // Create Node class to implement Algorithm X and Dancing Links
    // Node object points to nodes in left, right, up, and down directions
    // ColumnNode links a node to its repsective column in the cover matrix

    public class Node {
        public Node left, right, up, down;
        public ColumnNode column;

        public Node() {
            left = right = up = down = this;
        }

        public Node(ColumnNode col) {
            this();
            column = col;
        }

        public Node linkVertically(Node node) {
            node.down = down;
            node.down.up = node;
            node.up = this;
            down = node;
            return node;
        }

        public Node linkHorizontally(Node node) {
            node.right = right;
            node.right.left = node;
            node.left = this;
            right = node;
            return node;
        }
    }

    public class ColumnNode extends Node {
        public int size;
        public String name;

        public ColumnNode(String n) {
            super();
            size = 0;
            name = n;
            column = this;
        }

        // Delete column and all rows that contain nodes of that column
        // (cover nodes)
        public void cover() {
            // Delete column
            left.right = right;
            right.left = left;

            for (Node row = down; row != this; row = row.down) {
                for (Node col = row.right; col != row; col = col.right) {
                    // Delete row
                    up.down = down;
                    down.up = up;
                    col.column.size--;
                }
            }
        }

        // If algorithm reaches dead end then backtrack and reinsert
        // removed rows and columns (uncover nodes)
        public void uncover() {
            for (Node row = up; row != this; row = row.up) {
                for (Node col = row.left; col != row; col = col.left) {
                    col.column.size++;
                    // Reinsert row
                    up.down = this;
                    down.up = this;
                }
            }

            // Reinsert column
            left.right = this;
            right.left = this;
        }
    }


    // STEP THREE:
    // Create the doubly circular linked list by inputting the cover matrix
    // and outputing its ColumnNode node before implementing Algorithm X

    public class DancingLinks {
        private ColumnNode header;
        private List<Node> partSolution;
        public List<Node> fullSolution;
        
        public DancingLinks(int[][] coverMatrix) {
            header = buildDancingLinks(coverMatrix);
        }

        private ColumnNode selectMinColumn() {
            ColumnNode rightOfHeader = (ColumnNode)header.right;
            ColumnNode minCol = rightOfHeader;
            while (rightOfHeader.right != header) {
                rightOfHeader = (ColumnNode)rightOfHeader.right;
                if (rightOfHeader.size < minCol.size) {
                    minCol = rightOfHeader;
                }
            }
            return minCol;
        }    

        private ColumnNode buildDancingLinks(int[][] grid) {
            final int numColumns = grid[0].length;
            ColumnNode headerNode = new ColumnNode("header");
            List<ColumnNode> columnNodes = new ArrayList<>();

            // First create all the columns
            for (int i = 0; i < numColumns; i++) {
                ColumnNode cn = new ColumnNode(i + "");
                columnNodes.add(cn);
                headerNode = (ColumnNode) headerNode.linkHorizontally(cn);
            }

            headerNode = headerNode.right.column;

            // Next go through the cover matrix to find cells equal to 1 and add nodes
            for (int[] square : grid) {
                Node prev = null;

                for (int i = 0; i < numColumns; i++) {
                    if (square[i] == 1) {
                        ColumnNode col = columnNodes.get(i);
                        Node addedNode = new Node(col);

                        if (prev == null) {
                            prev = addedNode;
                        }
                        
                        col.up.linkVertically(addedNode);
                        prev = prev.linkHorizontally(addedNode);
                        col.size++;
                    }
                }
            }

            headerNode.size = numColumns;

            return headerNode;
        }

    
        // Implement Algorithm X (see README.md)
        private void algx(int i) {
            if (header.right == header) {
                // Algorithm terminates and result is copied
                fullSolution = new LinkedList<>(partSolution);
            }
            else {
                // Choose the next column c with the minimum number of 1s
                ColumnNode maincol = selectMinColumn();
                maincol.cover();

                for (Node row = maincol.down; row != maincol; row = row.down) {
                    // Add this row to the partial solution
                    partSolution.add(row);
                    
                    // Cover the columns
                    for (Node col = row.right; col != row; col = col.right) {
                        col.column.cover();
                    }
                    
                    // Recursively repeat Algorithm X on reduced matrix
                    algx(i + 1);
                    
                    // If algorithm ends unsuccessfully, backtrack
                    row = partSolution.remove(partSolution.size() - 1);
                    maincol = row.column;

                    // Uncover the columns
                    for (Node col = row.left; col != row; col = col.left) {
                        col.column.uncover();
                    }
                }

                maincol.uncover();
            }
        }

        // STEP FOUR:
        // Convert the linked list back to a sudoku grid

        private int[][] convertToSudokuGrid(List<Node> partSolution) {
            int[][] fullSolution = new int[GRID_SIZE][GRID_SIZE];

            for (Node n : partSolution) {
                Node rowColNode = n;
                int min = Integer.parseInt(rowColNode.column.name);

                for (Node temp = n.right; temp != n; temp = temp.right) {
                    int val = Integer.parseInt(temp.column.name);

                    if (val < min) {
                        min = val;
                        rowColNode = temp;
                    }
                }

                int rowCol = Integer.parseInt(rowColNode.column.name);
                int val = Integer.parseInt(rowColNode.right.column.name);
                int row = rowCol / GRID_SIZE;
                int col = rowCol % GRID_SIZE;
                int num = (val % GRID_SIZE) + 1;
                fullSolution[row][col] = num;
            }

            return fullSolution;
        }


        // STEP FIVE:
        // Solve by 1). Converting sudoku to cover matrix,
        // 2). Applying dancing links to cover matrix,
        // 3). Converting dancing links linked list to sudoku grid

        private int[][] runAlg(int[][] grid) {
            int[][] cover = convertToCoverMatrix(grid);
            DancingLinks dlx = new DancingLinks(cover);
            algx(0);
            int[][] solvedGrid = convertToSudokuGrid(dlx.fullSolution);
            return solvedGrid;
        }
    }
}