package ctf.environment;


/**
 * A large completely randomized board setting.
 *
 * Note that this class only has package access enabled.
 *
 */
class LargeRandomSet implements ObstacleSet, BoardSet {
	
	private static float mObstacleRatio = 0.25f;
	private static int mBoardSize = 30;
	
	
	// implements ObstacleSet.getObstacleLocations()
	public EnvironmentLocation[] getObstacleLocations( int inBoardSize ) {
		
		int numObstacles = 
			(int)( inBoardSize * inBoardSize * mObstacleRatio );
		
		EnvironmentLocation[] returnLocations = 
			new EnvironmentLocation[numObstacles];
		
		for( int o=0; o<numObstacles; o++ ) {
			returnLocations[o] = new EnvironmentLocation();
			// leave left and right columns free of obstacles
			returnLocations[o].mX = 
				(int)( Math.random() * ( inBoardSize - 2 ) + 1 );
			returnLocations[o].mY = 
				(int)( Math.random() * inBoardSize );
			}
		
		return returnLocations;
		}
		
	// implements BoardSet.getBoardSize()	
	public int getBoardSize() {
		return mBoardSize;
		}
	}
