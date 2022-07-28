Naive Sudoku Solver:
Uses a recursive backtracking algorithm.

-----------------------------------------------------

Faster Sudoku Solver:
Applies Donald Knuth's Algorithm X and Dancing Links technique to the Exact Cover problem. 

Given a matrix A consisting of 0s and 1s, the goal of the Exact Cover problem is to choose a subset of rows such that 1 appears in each column only once.

Algorithm X works as follows:
"If the matrix A has no columns, the current partial solution is a valid solution; terminate successfully.
    1. Otherwise choose a column c (with the minimum number of 1s in it).
    2. Choose a row r such that A[r][c] = 1 (nondeterministically).
    3. Include row r in the partial solution.
    4. For each column j such that A[r][j] = 1,
        for each row i such that A[i][j] = 1,
            delete row i from matrix A.
        delete column j from matrix A.
    5. Repeat this algorithm recursively on the reduced matrix A."

The Dancing Links technique allows us to apply Algorithm X to binary matrices. Dancing Links represent a binary matrix as a doubly circular linked list, where each node contains pointers to its previous node as well as the next node, and the first and last nodes contain each other's addresses in their previous and next pointers, respectively. So each node removed has all the information you need to easily add it back (in cases where we need to backtrack).

-----------------------------------------------------

References:
https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X
https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-1/ 
https://www.geeksforgeeks.org/exact-cover-problem-algorithm-x-set-2-implementation-dlx/ 
https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/sudoku.paper.html#mySolver 
https://medium.com/javarevisited/building-a-sudoku-solver-in-java-with-dancing-links-180274b0b6c1