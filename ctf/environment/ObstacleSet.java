package ctf.environment;


/**
 * Obstacle location preset.  Note that obstacle set is required
 * to leave left and right columns of board free of obstacles.
 *
 * Note that this class only has package access enabled.
 *
 */
interface ObstacleSet {
	
	/**
	 * Gets an array of obstacle locations specified by this set.
	 *
	 * @param inBoardSize the dimension of the board.
	 *
	 * @return an array of obstacle locations.
	 */
	public abstract EnvironmentLocation[] getObstacleLocations(
		int inBoardSize );
	
	}
