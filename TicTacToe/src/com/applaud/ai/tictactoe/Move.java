package com.applaud.ai.tictactoe;

/**
 * A class to store score calculated for move
 */
public class Move
{
	Integer value;
	Integer row;
	Integer col;
	
	public Move(Integer value, Integer row, Integer col) {
		super();
		this.value = value;
		this.row = row;
		this.col = col;
	}

	public Integer getValue() {
		return value;
	}
	
	public Integer getRow() {
		return row;
	}
	
	public Integer getCol() {
		return col;
	}
	
	private void setValue(Integer value) {
		this.value = value;
	}
	
	private void setRow(Integer row) {
		this.row = row;
	}
	
	private void setCol(Integer col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "Move [value=" + value + ", row=" + row + ", col=" + col + "]";
	}
	
}
