import java.util.Arrays;

public final class UnmodifiableMatrix {
    private final int rows;
    private final int columns;
    private final double[][] elements;

    // 构造函数，接受一个二维数组作为矩阵的元素
    public UnmodifiableMatrix(double[][] e) {
        if (e.length == 0 || e[0].length == 0) {
            throw new IllegalArgumentException("Matrix dimensions must be greater than zero");
        }

        this.rows = e.length;
        this.columns = e[0].length;

        // 使用Arrays.copyOf确保元素数组的不可变性
        this.elements = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            if (e[i].length != columns) {
                throw new IllegalArgumentException("All rows must have the same number of columns");
            }
            this.elements[i] = Arrays.copyOf(e[i], columns);
        }
    }

    // 构造函数，接受一个Matrix类型
    public UnmodifiableMatrix(Matrix m) {
        if (m.data.length == 0 || m.data[0].length == 0) {
            throw new IllegalArgumentException("Matrix dimensions must be greater than zero");
        }

        this.rows = m.data.length;
        this.columns = m.data[0].length;

        // 使用Arrays.copyOf确保元素数组的不可变性
        this.elements = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            if (m.data[i].length != columns) {
                throw new IllegalArgumentException("All rows must have the same number of columns");
            }
            this.elements[i] = Arrays.copyOf(m.data[i], columns);
        }
    }

    // 获取矩阵的行数
    public int getRows() {
        return rows;
    }

    // 获取矩阵的列数
    public int getColumns() {
        return columns;
    }

    // 获取指定位置的元素值
    public double get(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return elements[row][column];
    }

    // 返回一个新的矩阵，表示两个矩阵的和
    public UnmodifiableMatrix add(UnmodifiableMatrix other) {
        if (this.rows != other.rows || this.columns != other.columns) {
            throw new IllegalArgumentException("Matrix dimensions must be the same");
        }

        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = this.get(i, j) + other.get(i, j);
            }
        }

        return new UnmodifiableMatrix(result);
    }

    // 返回一个新的矩阵，表示该矩阵乘以一个标量
    public UnmodifiableMatrix scalarMultiply(double scalar) {
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = this.get(i, j) * scalar;
            }
        }

        return new UnmodifiableMatrix(result);
    }

    // 返回一个新的矩阵，表示两个矩阵相乘
    public UnmodifiableMatrix multiply(UnmodifiableMatrix other) {
        assert this.columns == other.rows : "Number of columns in the first matrix must be equal to the number of rows in the second matrix";
        double[][] result = new double[this.rows][other.columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.columns; j++) {
                double sum = 0;
                for (int k = 0; k < this.columns; k++) {
                    sum += this.get(i, k) * other.get(k, j);
                }
                result[i][j] = sum;
            }
        }

        return new UnmodifiableMatrix(result);
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(elements[i][j] + " ");
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

        UnmodifiableMatrix um1 = MathUtils.getUnmodifiableMatrix(matrix1);
        UnmodifiableMatrix um2 = MathUtils.getUnmodifiableMatrix(matrix2);

        System.out.println("UnmodifiableMatrix 1:");
        um1.print();

        System.out.println("UnmodifiableMatrix 2:");
        um1.print();

        System.out.println("UnmodifiableMatrix 1 * UnmodifiableMatrix 2:");
        UnmodifiableMatrix product = um1.multiply(um2);
        product.print();

        System.out.println("UnmodifiableMatrix 1 <= UnmodifiableMatrix 2:");
        um1 = um2;
        um1.print();

        System.out.println("UnmodifiableMatrix 1 + UnmodifiableMatrix 2:");
        UnmodifiableMatrix sum = um1.add(um2);
        sum.print();


    }
}
