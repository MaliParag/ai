package com.applaud.ai.tictactoe;

import com.applaud.ai.tictactoe.constants.Constants;

public class TicTacToe {
	
	/**
	 * Tic Tac Toe
	 */
	
	char board[][] = new char[3][3]; 
	
	/**
	 * Initialize the board with - 
	 */
	public TicTacToe() 
	{	
		for(int i = 0; i < 3; i++) 
		{
			for(int j = 0; j < 3; j++) {
				board[i][j] = Constants.EMPTY;
			}
		}
	}
	
	/**
	 * @return the board
	 */
	public char[][] getBoard()
	{
		return board;
	}
	
	/**
	 * @param row : Row of the board
	 * @param col : Column of the board
	 * @param player : X or O depending on the player
	 * @return true if board is successfully updated
	 */
	protected Boolean setBoard(int row, int col, char player) 
	{
		
		Boolean success = false;
		
		if(board[row][col] == Constants.EMPTY)
		{
			board[row][col] = player;
			success = true;
		}
		
		return success;
	}
	
	/**
	 * Convert the board to string
	 */
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				stringBuilder.append("\t" + board[i][j]);
			}
			stringBuilder.append("\n");
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * @return if valid move is available then return true else return false
	 */
	public Boolean isMoveAvailable()
	{
		Boolean isMoveAvailable = false;
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(board[i][j] == Constants.EMPTY)
				{
					isMoveAvailable = true;
					break;
				}
			}
		}
		
		return isMoveAvailable;
	}
	
	/**
	 * It checks current state of the board and returns score accordingly
	 */
	public Integer evaluateState(int depth)
	{
		Integer score = 0;
		
	    for(int row = 0; row < 3; row++)
	    {
	        if(board[row][0] == board[row][1] &&
	           board[row][1] == board[row][2])
	        {
	            if(board[row][0] == Constants.COMPUTER)
	                return Constants.WIN;
	            else if(board[row][0] == Constants.HUMAN)
	                return Constants.LOSE;
	        }
	    }
	 
	    for(int col = 0; col < 3; col++)
	    {
	        if(board[0][col]==board[1][col] &&
	           board[1][col]==board[2][col])
	        {
	            if (board[0][col] == Constants.COMPUTER)
	                return Constants.WIN;
	 
	            else if (board[0][col] == Constants.HUMAN)
	                return Constants.LOSE;
	        }
	    }
	 
	    if(board[0][0]==board[1][1] && board[1][1]==board[2][2])
	    {
	        if(board[0][0] == Constants.COMPUTER)
	            return Constants.WIN;
	        else if(board[0][0] == Constants.HUMAN)
	            return Constants.LOSE;
	    }
	 
	    if(board[0][2] == board[1][1] && board[1][1] == board[2][0])
	    {
	        if (board[0][2] == Constants.COMPUTER)
	            return Constants.WIN;
	        else if (board[0][2] == Constants.HUMAN)
	            return Constants.LOSE;
	    }
	 
		return score;
	}
	
	
	/**
	 * This function checks each move one by one to find out the best move
	 */
	protected Move findBestMove() 
	{
		Integer bestScore = Integer.MIN_VALUE;
		Integer bestRow = null;
		Integer bestCol = null;
		
		Move bestMove = null;
		
		for(int i = 0; i < 3; i++) 
		{
			for(int j = 0; j < 3; j++) 
			{
				
				if(board[i][j] == Constants.EMPTY)
				{
					//Make a move
					board[i][j] = Constants.COMPUTER;
					
					//find score of this move
					//initially depth will be 0
					int score = minimax(0 /*depth*/, false /*isPlayer*/);
					board[i][j] = Constants.EMPTY;
					
					//store best score
					if(bestScore < score)
					{
						bestScore = score;
						bestRow = i;
						bestCol = j;
					}
				}
				
			}
		}
		
		bestMove = new Move(bestScore, bestRow, bestCol);

		return bestMove;
	}
	
	/** 
	 * @param depth : It can be used to change score so that shortest path to win and 
	 * longest path to lose is taken
	 * @param isPlayer : It is true when we need to maximize the score 
	 * @return score calculated based on the current state
	 */
	private int minimax(Integer depth, Boolean isPlayer)
	{
		int score = evaluateState(depth);
		int value;
	
		if(score == Constants.WIN || score == Constants.LOSE)
		{
			return score;
		}
		
		if(!isMoveAvailable())
		{
			score = Constants.DRAW;
		}
		else if(isPlayer)
		{
			score = Integer.MIN_VALUE;
			
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					//make a move to check if it is best move
					if(board[i][j] == Constants.EMPTY)
					{
						board[i][j] = Constants.COMPUTER;
						
						value = minimax(depth + 1, !isPlayer);
						
						//Find maximum score
						if(value > score)
						{
							score = value;
						}
						
						board[i][j] = Constants.EMPTY;
					}
				}
			}
		}
		else
		{
			score = Integer.MAX_VALUE;
			
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					if(board[i][j] == Constants.EMPTY)
					{
						board[i][j] = Constants.HUMAN;
						
						value = minimax(depth + 1, !isPlayer);
						
						//Find minimum score
						if(score > value)
						{
							score = value;
						}
						
						board[i][j] = Constants.EMPTY;
					}
				}
			}
			
		}
		
		return score;
	}		
}
