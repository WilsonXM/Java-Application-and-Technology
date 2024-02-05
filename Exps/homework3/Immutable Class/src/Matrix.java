public class Matrix {
    int rows;           // Number of rows in the matrix
    int columns;        // Number of columns in the matrix
    double[][] data;    // 2D array to store matrix elements

    /**
     * Constructs a matrix with the specified number of rows and columns.
     *
     * @param rows    The number of rows in the matrix.
     * @param columns The number of columns in the matrix.
     */
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    /**
     * Gets the number of rows in the matrix.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the matrix.
     *
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the element at the specified row and column in the matrix.
     *
     * @param row    The row index.
     * @param column The column index.
     * @return The element at the specified position.
     */
    public double getElement(int row, int column) {
        return data[row][column];
    }

    /**
     * Sets the element at the specified row and column in the matrix.
     *
     * @param row    The row index.
     * @param column The column index.
     * @param value  The value to set at the specified position.
     */
    public void setElement(int row, int column, double value) {
        data[row][column] = value;
    }

    /**
     * Adds another matrix to this matrix and returns the result.
     *
     * @param other The matrix to be added.
     * @return A new matrix representing the sum of this matrix and the other matrix.
     * @throws IllegalArgumentException If the dimensions of the matrices are incompatible for addition.
     */
    public Matrix add(Matrix other) {
        if (this.rows != other.rows || this.columns != other.columns) {
            throw new IllegalArgumentException("Matrix dimensions must be the same for addition");
        }

        Matrix result = new Matrix(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result.setElement(i, j, this.getElement(i, j) + other.getElement(i, j));
            }
        }

        return result;
    }

    /**
     * Multiplies this matrix by another matrix and returns the result.
     *
     * @param other The matrix to be multiplied.
     * @return A new matrix representing the product of this matrix and the other matrix.
     * @throws IllegalArgumentException If the dimensions of the matrices are incompatible for multiplication.
     */
    public Matrix multiply(Matrix other) {
        if (this.columns != other.rows) {
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix");
        }

        Matrix result = new Matrix(this.rows, other.columns);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.columns; j++) {
                double sum = 0;
                for (int k = 0; k < this.columns; k++) {
                    sum += this.getElement(i, k) * other.getElement(k, j);
                }
                result.setElement(i, j, sum);
            }
        }

        return result;
    }

    /**
     * Prints the matrix to the console.
     */
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Main method for testing the Matrix class.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Example usage of the Matrix class
        Matrix matrix1 = new Matrix(2, 3);
        matrix1.setElement(0, 0, 1);
        matrix1.setElement(0, 1, 2);
        matrix1.setElement(0, 2, 3);
        matrix1.setElement(1, 0, 4);
        matrix1.setElement(1, 1, 5);
        matrix1.setElement(1, 2, 6);

        Matrix matrix2 = new Matrix(3, 2);
        matrix2.setElement(0, 0, 7);
        matrix2.setElement(0, 1, 8);
        matrix2.setElement(1, 0, 9);
        matrix2.setElement(1, 1, 10);
        matrix2.setElement(2, 0, 11);
        matrix2.setElement(2, 1, 12);

        System.out.println("Matrix 1:");
        matrix1.print();

        System.out.println("Matrix 2:");
        matrix2.print();

        System.out.println("Matrix 1 * Matrix 2:");
        Matrix product = matrix1.multiply(matrix2);
        product.print();

        System.out.println("Matrix 1 + Matrix 2:");
        Matrix sum = matrix1.add(matrix2);
        sum.print();
    }
}
