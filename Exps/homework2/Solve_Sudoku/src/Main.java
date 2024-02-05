// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

/*
  Solve a 9*9 sudoku with several prompts
  Note: there exists the situation that the sudoku has no solution
 */

import sudoku.Sudoku;

public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        // get the input sudoku
        System.out.println("Please enter the sudoku:");
        long[] solutions = new long[1];
        int[][] sudoku = new int[9][9];
        int[][] saved_sudoku = new int [9][9];
        Sudoku.solve_sudoku( Sudoku.input( sudoku ), saved_sudoku, 0, solutions );
        Sudoku.print_sudoku( saved_sudoku, solutions );
    }
}