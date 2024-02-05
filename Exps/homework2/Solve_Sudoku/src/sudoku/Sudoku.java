package sudoku;

import java.util.Scanner;

public class Sudoku {
    /**
     * sovle the given sudoku
     * @param tsudoku: the sudoku to be completed
     * @return : the finished sudoku
     */
    public static boolean solve_sudoku( int[][] tsudoku, int[][] saved, int index, long[] num ) {
        if( index == 81 ) {
            num[0] = num[0] + 1;
            if( num[0] == 1 )
                for (int i = 0; i < 9; i++)
                    System.arraycopy(tsudoku[i], 0, saved[i], 0, 9);
            return true;
        }
        int row = index / 9;
        int column = index % 9;
        if( tsudoku[row][column] != -1 ) return solve_sudoku( tsudoku, saved, index + 1, num );
        else {
            boolean flag = false;
            for( int i = 1; i < 10; i++ ) {
                if( isRepeat( tsudoku, row, column, i) ) {
                    tsudoku[row][column] = i;
                    if( solve_sudoku( tsudoku, saved, index + 1, num ) ) {
                        flag = true;
                        //continue;
                    }
                    tsudoku[row][column] = -1;
                }
            }
            return flag;
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
    public static int[][] input( int[][] tsudoku ) {
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
            else return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void print_sudoku( int[][] tsudoku, long[] num ) {
        if (num[0] == 0) {
            System.out.println("The input sudoku has no solution!");
        } else {
            if (num[0] == 1) System.out.println("The input sudoku has only one solution!");
            else {
                System.out.printf("The input sudoku has %d solution!\n", num[0]);
                System.out.println("One of the solutions is: ");
            }
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    if (j == 8) {
                        System.out.println(tsudoku[i][j]);
                        if ((i + 1) % 3 == 0 && i != 8) System.out.println("--------+---------+--------");
                    } else if ((j + 1) % 3 == 0) System.out.printf("%d | ", tsudoku[i][j]);
                    else System.out.printf("%d  ", tsudoku[i][j]);
        }
    }
}
