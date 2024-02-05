public class Vector {
    double[] elements;  // Array to store vector elements

    /**
     * Constructs a vector with the specified elements.
     *
     * @param elements The array of elements for the vector.
     */
    public Vector(double[] elements) {
        this.elements = elements.clone();  // Using clone to avoid external modification of the array
    }

    /**
     * Gets the number of elements in the vector.
     *
     * @return The number of elements.
     */
    public int size() {
        return elements.length;
    }

    /**
     * Gets the element at the specified index in the vector.
     *
     * @param index The index of the element.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public double get(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return elements[index];
    }

    /**
     * Sets the element at the specified index in the vector.
     *
     * @param index The index of the element.
     * @param value The value to set at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void set(int index, double value) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        elements[index] = value;
    }

    /**
     * Adds another vector to this vector and returns the result.
     *
     * @param another The vector to be added.
     * @return A new vector representing the sum of this vector and the other vector.
     * @throws IllegalArgumentException If the vectors have different sizes for addition.
     */
    public Vector add(Vector another) {
        if (this.size() != another.size()) {
            throw new IllegalArgumentException("Vectors must have the same size for addition");
        }

        double[] result = new double[this.size()];
        for (int i = 0; i < this.size(); i++) {
            result[i] = this.get(i) + another.get(i);
        }

        return new Vector(result);
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param another The vector to compute the dot product with.
     * @return The dot product of the two vectors.
     * @throws IllegalArgumentException If the vectors have different sizes for dot product.
     */
    public double dotProduct(Vector another) {
        if (this.size() != another.size()) {
            throw new IllegalArgumentException("Vectors must have the same size for dot product");
        }

        double result = 0;
        for (int i = 0; i < this.size(); i++) {
            result += this.get(i) * another.get(i);
        }

        return result;
    }

    /**
     * Prints the vector to the console.
     */
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

        System.out.println("Vector 1:");
        vector1.print();

        System.out.println("Vector 2:");
        vector2.print();

        System.out.println("Vector 1 + Vector 2:");
        Vector sum = vector1.add(vector2);
        sum.print();

        System.out.println("Dot Product of Vector 1 and Vector 2: " + vector1.dotProduct(vector2));
    }
}
