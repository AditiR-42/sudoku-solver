

public class Node {
    public Node up;
    public Node down;
    public Node left;
    public Node right;
    public ColumnNode column;

    // Node object points to adjacent nodes in left, right, up, and down directions
    public Node() {
        this.up = this.down = this.left = this.right = this;
    }

    public Node(ColumnNode column) {
        this();
        this.column = column;
    }

    // Insert node vertically by rearranging the node links below it
    public Node insertVertically(Node node) {
        node.down = this.down;
        node.down.up = node;
        node.up = this;
        this.down = node;
        return node;
    }

    // Insert node horizontally by rearranging the node to its right
    public Node insertHorizontally(Node node) {
        node.right = this.right;
        node.right.left = node;
        node.left = this;
        this.right = node;
        return node;
    }

    // Delete column by unlinking node from its horizontally adjacent nodes
    public void deleteColumn() {
        this.left.right = this.right;
        this.right.left = this.left;
    }

    // Reinsert column by applying the saved links to its horizontal neighbors
    public void insertColumn() {
        this.left.right = this;
        this.right.left = this;
    }

    // Delete row by unlinking node from its vertically adjacent nodes
    public void deleteRow() {
        this.up.down = this.down;
        this.down.up = this.up;
    }

    // Reinsert row by applying the saved links to its vertical neighbors
    public void insertRow() {
        this.up.down = this;
        this.down.up = this;
    }
}