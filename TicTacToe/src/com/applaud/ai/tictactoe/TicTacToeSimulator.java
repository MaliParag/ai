package com.applaud.ai.tictactoe;

import java.util.Scanner;

import com.applaud.ai.tictactoe.constants.Constants;

public class TicTacToeSimulator {

/**
 * @param args command line args
 * Driver program for tic tac toe
 */
public static void main(String[] args)
{
		TicTacToe ticTacToe = new TicTacToe();
		int row, col;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Current state : \n " + ticTacToe);
		System.out.println("Enter row and column where you want to put a X\n");
		
		while(ticTacToe.isMoveAvailable())
		{
			System.out.println("***Your turn***");
			row = scanner.nextInt();
			col = scanner.nextInt();
			
			if(ticTacToe.setBoard(row, col, Constants.HUMAN))
			{
				System.out.println("Current state : \n " + ticTacToe);
			}
			else
			{
				System.out.println("You can only play at empty positions.");
				continue;
			}
			
			System.out.println("***My turn***");
	
			Move move = ticTacToe.findBestMove();
			
			if(null != move && null != move.getRow() && null != move.getCol())
			{
				ticTacToe.setBoard(move.getRow(), move.getCol(), Constants.COMPUTER);
			}
			
			System.out.println("Current state : \n " + ticTacToe);
			int state = ticTacToe.evaluateState(0);
			
			if(state == Constants.WIN)
			{
				System.out.println("I won!");
				break;
			}
			else if(state == Constants.LOSE)
			{
				System.out.println("You won!");
				break;
			}
			else if(!ticTacToe.isMoveAvailable())
			{
				System.out.println("Draw!");
			}
		}
		
		scanner.close();
	
	}

}
