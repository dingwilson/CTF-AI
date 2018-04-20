package ctf.environment;

import ctf.common.AgentEnvironment;

/**
 * Snapshot of the environment state.
 *
 */
class EnvironmentSnapshot implements AgentEnvironment {
	
	private ObstacleGrid mObstacleGrid;
	private AgentGrid mOurTeamGrid, mEnemyTeamGrid;
	private BaseGrid mOurBaseGrid, mEnemyBaseGrid;
	private FlagGrid mOurFlagGrid, mEnemyFlagGrid;
	private AgentInfo mCentralAgentInfo;
	
	private int mBoardSize;
	private int mAgentX;
	private int mAgentY;
	
	/**
	 * Constructs an <code>EnvironmentSnapshot</code>.
	 *
	 * @param inObstacleGrid grid of obstacles.
	 * @param inOurTeamGrid grid of agent positions for our team.
	 * @param inEnemyTeamGrid grid of enemy agent positions.
	 * @param inOurFlagGrid grid of our flag position.
	 * @param inEnemyFlagGrid grid of enemy flag position.
	 * @param inCentralAgentInfo info about agent that this snapshot's
	 *   <code>AgentEnvironment</code> methods should deal with.
	 */
	EnvironmentSnapshot( ObstacleGrid inObstacleGrid,
							AgentGrid inOurTeamGrid,
							AgentGrid inEnemyTeamGrid,
							BaseGrid inOurBaseGrid,
							BaseGrid inEnemyBaseGrid,
							FlagGrid inOurFlagGrid,
							FlagGrid inEnemyFlagGrid,
							AgentInfo inCentralAgentInfo,
							int inBoardSize ) {
			
		mObstacleGrid = inObstacleGrid;
		
		mOurTeamGrid = inOurTeamGrid;
		mEnemyTeamGrid = inEnemyTeamGrid;
		
		mOurBaseGrid = inOurBaseGrid;	
		mEnemyBaseGrid = inEnemyBaseGrid;
		
		mOurFlagGrid = inOurFlagGrid;	
		mEnemyFlagGrid = inEnemyFlagGrid;
		
		mCentralAgentInfo = inCentralAgentInfo;				
		
		mBoardSize = inBoardSize;
		mAgentX = mCentralAgentInfo.mAgentLocation.mX;
		mAgentY = mCentralAgentInfo.mAgentLocation.mY;
		}	
	
	
	
	/**
	 * Gets the opposite team indicator.
	 *
	 * @param inTeam the team to take the opposite of.
	 * 
	 * @return the opposite team of <code>inTeam</code>.
	 */
	private int getOppositeTeam( int inTeam ) {
		if( inTeam == OUR_TEAM ) {
			return ENEMY_TEAM;
			}
		return OUR_TEAM;
		}
	
	
	
	/**
	 * Gets the agent grid of a team.
	 *
	 * @param inTeam team to get agent grid of.
	 *
	 * @return the agent grid of the specified team.
	 */
	private AgentGrid getTeamAgentGrid( int inTeam ) {
		if( inTeam == OUR_TEAM ) {
			return mOurTeamGrid;
			}
		return mEnemyTeamGrid;
		}
	
	
	
	/**
	 * Gets the base grid of a team.
	 *
	 * @param inTeam team to get agent grid of.
	 *
	 * @return the Base grid of the specified team.
	 */
	private BaseGrid getTeamBaseGrid( int inTeam ) {
		if( inTeam == OUR_TEAM ) {
			return mOurBaseGrid;
			}
		return mEnemyBaseGrid;
		}
	
	
	
	/**
	 * Gets the flag grid of a team.
	 *
	 * @param inTeam team to get flag grid of.
	 *
	 * @return the flag grid of the specified team.
	 */
	private FlagGrid getTeamFlagGrid( int inTeam ) {
		if( inTeam == OUR_TEAM ) {
			return mOurFlagGrid;
			}
		return mEnemyFlagGrid;
		}



	/**
	 * Search a grid for an object.  Search takes place in range
	 * [inStartX, inEndX] x [inStartY, inEndY].
	 *
	 * @param inGrid grid to search for object in.
	 * @param inStartX start x of grid range to search.
	 * @param inEndX end x of grid range to search.
	 * @param inStartY start y of grid range to search.
	 * @param inEndY end y of grid range to search.
	 *
	 * @return true iff at least one object found in grid range.
	 */
	private boolean search( Grid inGrid, 
		int inStartX, int inEndX, int inStartY, int inEndY ) {
		if( inStartX < 0 ) {
			inStartX = 0;
			}
		if( inStartY < 0 ) {
			inStartY = 0;
			}
		if( inEndX >= mBoardSize ) {
			inEndX = mBoardSize - 1;
			}
		if( inEndY >= mBoardSize ) {
			inEndY = mBoardSize - 1;
			}	
		for( int y=inStartY; y<=inEndY; y++ ) {
			for( int x=inStartX; x<=inEndX; x++ ) {
				if( inGrid.exists( x, y ) ) {
					return true;
					}
				}
			}
		return false;
		}
	
	
	
	/**
	 * Searches grid to north of current agent position for an object.
	 *
	 * @param inGrid grid to search.
	 * @param inImmediate set to true to constrain search to single grid
	 *   space immediately north of agent.
	 *
	 * @return true iff at least one object exists in grid to north of agent.
	 */
	private boolean searchNorth( Grid inGrid, boolean inImmediate ) {
		if( inImmediate ) {
			return search( inGrid, mAgentX, mAgentX, mAgentY - 1, mAgentY - 1 );
			}
		return search( inGrid, 0, mBoardSize - 1, 0, mAgentY - 1 );
		}



	/**
	 * Searches grid to south of current agent position for an object.
	 *
	 * @param inGrid grid to search.
	 * @param inImmediate set to true to constrain search to single grid
	 *   space immediately south of agent.
	 *
	 * @return true iff at least one object exists in grid to south of agent.
	 */
	private boolean searchSouth( Grid inGrid, boolean inImmediate ) {
		if( inImmediate ) {
			return search( inGrid, mAgentX, mAgentX, mAgentY + 1, mAgentY + 1 );
			}
		return search( inGrid, 0, mBoardSize - 1, mAgentY + 1, mBoardSize - 1 );
		}



	/**
	 * Searches grid to east of current agent position for an object.
	 *
	 * @param inGrid grid to search.
	 * @param inImmediate set to true to constrain search to single grid
	 *   space immediately east of agent.
	 *
	 * @return true iff at least one object exists in grid to east of agent.
	 */
	private boolean searchEast( Grid inGrid, boolean inImmediate ) {
		if( inImmediate ) {
			return search( inGrid, mAgentX + 1, mAgentX + 1, mAgentY, mAgentY );
			}
		return search( inGrid, mAgentX + 1, mBoardSize - 1, 0, mBoardSize - 1 );
		}



	/**
	 * Searches grid to west of current agent position for an object.
	 *
	 * @param inGrid grid to search.
	 * @param inImmediate set to true to constrain search to single grid
	 *   space immediately west of agent.
	 *
	 * @return true iff at least one object exists in grid to west of agent.
	 */
	private boolean searchWest( Grid inGrid, boolean inImmediate ) {
		if( inImmediate ) {
			return search( inGrid, mAgentX - 1, mAgentX - 1, mAgentY, mAgentY );
			}
		return search( inGrid, 0, mAgentX - 1, 0, mBoardSize - 1 );
		}
	
	
		
	// implements AgentEnvironment.isFlagNorth()
	public boolean isFlagNorth( int inTeam, boolean inImmediate ) {
		return searchNorth( getTeamFlagGrid( inTeam ), inImmediate );
		}



	// implements AgentEnvironment.isFlagSouth()
	public boolean isFlagSouth( int inTeam, boolean inImmediate ) {
		return searchSouth( getTeamFlagGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isFlagEast()
	public boolean isFlagEast( int inTeam, boolean inImmediate ) {
		return searchEast( getTeamFlagGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isFlagWest()
	public boolean isFlagWest( int inTeam, boolean inImmediate ) {
		return searchWest( getTeamFlagGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isBaseNorth()
	public boolean isBaseNorth( int inTeam, boolean inImmediate ) {
		return searchNorth( getTeamBaseGrid( inTeam ), inImmediate );
		}
	
	
	// implements AgentEnvironment.isBaseSouth()
	public boolean isBaseSouth( int inTeam, boolean inImmediate ) {
		return searchSouth( getTeamBaseGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isBaseEast()
	public boolean isBaseEast( int inTeam, boolean inImmediate ) {
		return searchEast( getTeamBaseGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isBaseWest()
	public boolean isBaseWest( int inTeam, boolean inImmediate ) {
		return searchWest( getTeamBaseGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isAgentNorth()
	public boolean isAgentNorth( int inTeam, boolean inImmediate ) {
		return searchNorth( getTeamAgentGrid( inTeam ), inImmediate );
		}



	// implements AgentEnvironment.isAgentSouth()
	public boolean isAgentSouth( int inTeam, boolean inImmediate ) {
		return searchSouth( getTeamAgentGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isAgentEast()
	public boolean isAgentEast( int inTeam, boolean inImmediate ) {
		return searchEast( getTeamAgentGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.isAgentWest()
	public boolean isAgentWest( int inTeam, boolean inImmediate ) {
		return searchWest( getTeamAgentGrid( inTeam ), inImmediate );
		}
	
	
	
	// implements AgentEnvironment.hasFlag();
	public boolean hasFlag() {
		return mCentralAgentInfo.mHasFlag;
		}
	
	
	
	// implements AgentEnvironment.hasFlag();
	public boolean hasFlag( int inTeam ) {
		FlagGrid oppositeTeamGrid = 
			getTeamFlagGrid( getOppositeTeam( inTeam ) );
		EnvironmentLocation flagLocation = oppositeTeamGrid.getLocation();
		
		AgentGrid teamGrid = getTeamAgentGrid( inTeam );
		
		// if an agent is sitting on the opposite team's flag,
		// then the agent has their flag.
		return teamGrid.exists( flagLocation );	
		}
	
	
	
	// implements AgentEnvironment.isObstacleNorthImmediate()
	public boolean isObstacleNorthImmediate() {
		// search grid space to north
		boolean actualObstacle = searchNorth( mObstacleGrid, true );
		
		//boundary also counts as obstacle
		boolean boundary = ( mAgentY == 0 );
		
		return actualObstacle || boundary;
		}


	// implements AgentEnvironment.isObstacleSouthImmediate()
	public boolean isObstacleSouthImmediate() {
		// search grid space to north
		boolean actualObstacle = searchSouth( mObstacleGrid, true );
		
		//boundary also counts as obstacle
		boolean boundary = ( mAgentY == mBoardSize - 1 );
		
		return actualObstacle || boundary;
		}
	
	
	
	// implements AgentEnvironment.isObstacleEastImmediate()
	public boolean isObstacleEastImmediate() {
		// search grid space to north
		boolean actualObstacle = searchEast( mObstacleGrid, true );
		
		//boundary also counts as obstacle
		boolean boundary = ( mAgentX == mBoardSize - 1 );
		
		return actualObstacle || boundary;
		}
	
	
	
	// implements AgentEnvironment.isObstacleWestImmediate()
	public boolean isObstacleWestImmediate() {
		// search grid space to north
		boolean actualObstacle = searchWest( mObstacleGrid, true );
		
		//boundary also counts as obstacle
		boolean boundary = ( mAgentX == 0 );
		
		return actualObstacle || boundary;
		}
	
	}
