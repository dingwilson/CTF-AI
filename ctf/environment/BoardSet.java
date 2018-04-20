package ctf.environment;


/**
 * Miscelaneous board settings preset.
 *
 * Note that this class only has package access enabled.
 *
 */
interface BoardSet {
	
	/**
	 * Gets the dimension of the board.
	 *
	 * @return the size of the board in number of spaces across.
	 */
	public abstract int getBoardSize();
	
	}
