


import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class DancingLinks {
    private ColumnNode header;
    public List<Node> partSolution;
    public List<Node> fullSolution;

    // Create dancing links that convert a matrix to a linked list and
    // create arraylists for the partial and full solutions
    public DancingLinks(final int[][] matrix) {
        this.header = toLinkedList(matrix);
        this.partSolution = new ArrayList<>();
        this.fullSolution = new ArrayList<>();
    }

    // Convert binary matrix to a linked list
    ColumnNode toLinkedList(final int[][] matrix) {
        ColumnNode header = new ColumnNode(-1);

        for (int i = 0; i < matrix[0].length; i++) {
            ColumnNode newColumn = new ColumnNode(i);
            header = (ColumnNode) header.insertHorizontally(newColumn);
        }

        header = header.right.column;

        for (int[] row : matrix) {
            Node left = null;
            ColumnNode current = header;

            for (int j = 0; j < matrix[0].length; j++) {
                current = (ColumnNode) current.right;

                if (row[j] == 1) {
                    Node newNode = new Node(current);

                    if (left == null) {
                        left = newNode;
                    }

                    current.up.insertVertically(newNode);
                    left = left.insertHorizontally(newNode);
                    current.size++;
                }
            }
        }

        header.size = matrix[0].length;
        return header;
    }

    // Implement Algorithm X (see README.md)
    public void algorithmX(int i) {
        // If the linked list is empty, the algorithm successfully completed
        if (this.header.right == this.header) {
            this.fullSolution = new LinkedList<>(this.partSolution);
        }
        else {
            // Choose the next column with the least number of 1's and cover it
            ColumnNode colNode = selectMinColumn();
            colNode.cover();
            // Cover all rows containing the node and add the column to the partial solution
            for (Node rowNode = colNode.down; rowNode != colNode; rowNode = rowNode.down) {
                this.partSolution.add(rowNode);
                
                for (Node col = rowNode.right; col != rowNode; col = col.right) {
                    col.column.cover();
                }
                // Recursively repeat Algorithm X on the reduced matrix
                algorithmX(++i);
                // If columns remain, algorithm was unsuccessful so backtrack and uncover
                rowNode = partSolution.remove(partSolution.size() - 1);
                colNode = rowNode.column;

                for (Node col = rowNode.left; col != rowNode; col = col.left) {
                    col.column.uncover();
                }
            }
            colNode.uncover();
        }
    }

    // Select the column with the fewest 1's to be covered next by Algorithm X
    private ColumnNode selectMinColumn() {
        int min = Integer.MAX_VALUE;
        ColumnNode minColumn = null;
        for (ColumnNode current = (ColumnNode) this.header.right; current != header; current = (ColumnNode) current.right) {
            if (min > current.size) {
                min = current.size;
                minColumn = current;
            }
        }
        return minColumn;
    }

}
