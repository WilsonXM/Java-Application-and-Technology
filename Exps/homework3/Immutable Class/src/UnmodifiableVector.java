import java.util.Arrays;

public final class UnmodifiableVector {
    private final double[] elements;

    // 构造函数，接受一个数组作为向量的元素
    public UnmodifiableVector(double[] e) {
        // 使用Arrays.copyOf确保元素数组的不可变性
        this.elements = Arrays.copyOf(e, e.length);
    }

    // 构造函数，接受一个Vector类型
    public UnmodifiableVector(Vector v) {
        this.elements = Arrays.copyOf(v.elements, v.elements.length);
    }

    // 获取向量的长度（元素个数）
    public int size() {
        return elements.length;
    }

    // 获取指定索引处的元素值
    public double get(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return elements[index];
    }

    // 计算两个向量的点积
    public double dotProduct(UnmodifiableVector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }

        double result = 0;
        for (int i = 0; i < this.size(); i++) {
            result += this.get(i) * other.get(i);
        }

        return result;
    }

    // 返回一个新的向量，表示两个向量的和
    public UnmodifiableVector add(UnmodifiableVector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }

        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.get(i) + other.get(i);
        }

        return new UnmodifiableVector(result);
    }

    // 返回一个新的向量，表示两个向量的差
    public UnmodifiableVector subtract(UnmodifiableVector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }

        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.get(i) - other.get(i);
        }

        return new UnmodifiableVector(result);
    }

    // 返回一个新的向量，表示该向量乘以一个标量
    public UnmodifiableVector scalarMultiply(double scalar) {
        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.get(i) * scalar;
        }

        return new UnmodifiableVector(result);
    }

    public void print() {
        System.out.print("[ ");
        for (double element : elements) {
            System.out.print(element + " ");
        }
        System.out.println("]");
    }

    /**
     * Main method for testing the Vector class.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Example usage of the Vector class
        double[] vector1Array = {1.0, 2.0, 3.0};
        double[] vector2Array = {4.0, 5.0, 6.0};

        Vector vector1 = new Vector(vector1Array);
        Vector vector2 = new Vector(vector2Array);

        UnmodifiableVector uv1 = MathUtils.getUnmodifiableVector(vector1);
        UnmodifiableVector uv2 = MathUtils.getUnmodifiableVector(vector2);

        System.out.println("UnmodifiableVector 1:");
        uv1.print();

        System.out.println("UnmodifiableVector 2:");
        uv2.print();

        System.out.println("UnmodifiableVector 1 + UnmodifiableVector 2:");
        UnmodifiableVector sum = uv1.add(uv2);
        sum.print();

        System.out.println("Dot Product of UnmodifiableVector 1 and UnmodifiableVector 2: " + uv1.dotProduct(uv2));

        System.out.println("UnmodifiableVector 1 <= {0, 0, 0}");
        uv1.print();
    }
}
