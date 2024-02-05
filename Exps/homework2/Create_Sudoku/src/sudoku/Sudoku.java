package sudoku;

import java.util.Scanner;

public class Sudoku {
    /**
     * sovle the given sudoku
     * @param tsudoku: the sudoku to be completed
     * @return : the finished sudoku
     */
    public static boolean solve_sudoku( int[][] tsudoku, int index ) {
        if( index == 81 ) return true;
        int row = index / 9;
        int column = index % 9;
        if( tsudoku[row][column] != 0 ) return solve_sudoku( tsudoku, index + 1 );
        else {
            for( int i = 1; i < 10; i++ ) {
                if( isRepeat( tsudoku, row, column, i) ) {
                    tsudoku[row][column] = i;
                    if( solve_sudoku( tsudoku, index + 1 ) ) return true;
                    tsudoku[row][column] = 0;
                }
            }
            return false;
        }
    }

    public static boolean isRepeat( int[][] tsudoku, int row, int column, int i ) {
        for( int j = 0; j < 9; j++ ) {
            if( tsudoku[row][j] == i ) return false;
            if( tsudoku[j][column] == i ) return false;
            if( tsudoku[3 * (row/3) + j / 3][3 * (column/3) + j % 3] == i ) return false;
        }
        return true;
    }

    /**
     * get the input sudoku
     * @return : a 2-d array of the input sudoku
     */
    public static int[][] input( int[][] tsudoku, int[] num ) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                tsudoku[i][j] = isLegalnum(scanner.next());
        return tsudoku;
    }

    /**
     * judge whether the input character is a number 1~9
     * @param str: one of the elements in the 9*9 sudoku
     * @return -1: blank or todo
     *        1~9: proper numbers or the prompts
     */
    public static int isLegalnum( String str ) {
        try {
            int num = Integer.parseInt(str);
            if( num > 0 && num < 10 ) return num;
            else return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void print_sudoku( int[][] tsudoku ) {
        System.out.println("The sudoku to be solved is: ");
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (j == 8) {
                    if(tsudoku[i][j] == 0 ) System.out.println(".");
                    else System.out.println(tsudoku[i][j]);
                    if( (i+1) % 3 == 0 && i != 8 ) System.out.println("--------+---------+--------");
                }
                else if( (j+1) % 3 == 0 )
                    if(tsudoku[i][j] == 0 ) System.out.print(". | ");
                    else System.out.printf("%d | ", tsudoku[i][j]);
                else
                    if(tsudoku[i][j] == 0 ) System.out.print(".  ");
                    else System.out.printf("%d  ", tsudoku[i][j]);
            }
    }
}
