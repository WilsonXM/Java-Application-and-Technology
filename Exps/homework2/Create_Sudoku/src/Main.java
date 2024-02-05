// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.util.Scanner;

import sudoku.Sudoku;
import sudoku.Sudoku.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");

        // get the prompt numbers
        System.out.println("Please enter the number of prompts: ");
        Scanner scanner = new Scanner(System.in);
        int prompt_Num = scanner.nextInt();
        while( prompt_Num <= 17 || prompt_Num > 81 ) {
            System.out.println("The prompts is not proper, a proper prompt should between 20 and 30!");
            System.out.println("Please enter the number of prompts again: ");
            prompt_Num = scanner.nextInt();
        }
        create_print_sudoku( prompt_Num );
    }

    /**
     * create a sudoku with given prompts, and print it if creating successfully
     * @param prompt_num: the input prompts
     */
    public static void create_print_sudoku( int prompt_num ) {
        int[][] sudoku = new int[9][9];
        Random random = new Random();

        // initialise the first row of sudoku to {1, 2, 3, 4, 5, 6, 7, 8, 9}
        // and resort it randomly
        for( int i = 0; i < 9; i++ ) sudoku[0][i] = i + 1;
        for( int i = 0; i < 9; i++ ) {
            int p = random.nextInt( i + 1 );
            int temp = sudoku[0][i];
            sudoku[0][i] = sudoku[0][p];
            sudoku[0][p] = temp;
        }

        // solve the sudoku
        Sudoku.solve_sudoku( sudoku, 0 );

        // hollowed given number of blanks
        for( int i = 0; i < 81-prompt_num; i++ ) {
            int p = random.nextInt(81);
            int temp = sudoku[(p / 9)][(p % 9)];
            if( temp != 0 ) sudoku[(p / 9)][(p % 9)] = 0;
            else i--;
        }

        // output the sudoku
        Sudoku.print_sudoku( sudoku );
    }

}