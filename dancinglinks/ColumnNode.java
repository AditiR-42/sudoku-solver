package dancinglinks;

public class ColumnNode extends Node {
    public int size;
    private final int name;

    // Create ColumnNode class to specify the head of each column
    ColumnNode(final int name) {
        super();
        size = 0;
        this.name = name;
        this.column = this;
    }

    // Delete column and all rows that contain nodes of that column
    public void cover() {
        deleteColumn();
        for (Node row = this.down; row != this; row = row.down) {
            for (Node col = row.right; col != row; col = col.right) {
                col.deleteRow();
                col.column.size--;
            }
        }
    }

    // If dead end reached, backtrack and reinsert removed rows and columns
    public void uncover() {
        for (Node row = this.up; row != this; row = row.up) {
            for (Node col = row.left; col != row; col = col.left) {
                col.insertRow();
                col.column.size++;
            }
        }
        insertColumn();
    }
    
    // Return the column name as an integer representation of the column
    public int getName() {
        return this.name;
    }
}
