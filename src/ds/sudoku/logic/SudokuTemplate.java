package ds.sudoku.logic;

public class SudokuTemplate {
	
	public static final int CLUE_VALUE = 0;
	
	private int[][] template;
	
	public SudokuTemplate(int[][] template) {
		this.template = template;
	}
	
	/**
	 * Get the value for the specified field. 
	 * If the value is zero, it can be filled by the user.
	 * 
	 * @param row		one-based row index
	 * @param column	one-based column index
	 * @return
	 */
	public int getValue(int row, int column) {
		return template[row-1][column-1];
	}
	
	public int[][] getTemplate() {
		return template;
	}

}
