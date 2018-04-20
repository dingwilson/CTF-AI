package ctf.environment;

import ctf.agent.*;

import ctf.common.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Frame;

import java.awt.Image;
import java.awt.Dimension;

/**
 * CTF Game between two teams of agents.
 *
 */
class Game {
	
	private static final int NUM_TEAMS = 2; 
	
	private AgentInfo[][] mTeamInfo;
	private ObstacleGrid mObstacleGrid;
	private MineGrid mMineGrid;
	private AgentGrid[] mTeamAgentGrids;
	private FlagGrid[] mTeamFlagGrids;
	private BaseGrid[] mTeamBaseGrids;
	
	private int mBoardSize;
	private int mNumAgentsPerTeam;
	private int mNumTeams = NUM_TEAMS;
	
	private StateChangeListener mStateListener;
	
	private GameColorMap mColorMap;
	
	/**
	 * Constructs a <code>Game</code>.
	 *
	 * @param inTeamAgents array of classes to instantiate for
	 *   building agent set for each team.  Classes should all extend
	 *   <code>Agent</code>.
	 * @param inColorMap color settings for various components.
	 * @param inBoardSet board settings preset.
	 * @param inObstacleSet obstacle location preset.
	 * @param inNumAgentsPerTeam number of agents on a team.
	 * @param inStateChangeListener listener that will get fire
	 *   event callbacks when board-wide state changes during play.
	 *
	 * @throws IllegalArgumentException if number of agents per
	 *   team is not > 0 or if instantiating any of <code>Agent</code> classes
	 *   fails.
	 */
	Game( Class[] inTeamAgents, GameColorMap inColorMap, BoardSet inBoardSet, 
		ObstacleSet inObstacleSet, int inNumAgentsPerTeam,
		StateChangeListener inStateChangeListener )
		throws IllegalArgumentException {
		
		mBoardSize = inBoardSet.getBoardSize();
		
		mColorMap = inColorMap;
		
		if( inNumAgentsPerTeam <= 0 ) {
			throw new IllegalArgumentException( 
				"Number of agents per team must be > 0." );
			}
		
		mNumAgentsPerTeam = inNumAgentsPerTeam;
		
		mStateListener = inStateChangeListener;
		
		mObstacleGrid = new ObstacleGrid( mBoardSize, mColorMap.mLineColor, 
			mColorMap.mObstacleColor );
		EnvironmentLocation obstacleLocations[] =
			inObstacleSet.getObstacleLocations( mBoardSize );
		for( int o=0; o<obstacleLocations.length; o++ ) {
			mObstacleGrid.add( obstacleLocations[o].mX,
				obstacleLocations[o].mY );
			}
		
		mMineGrid = new MineGrid( mBoardSize, mColorMap.mLineColor, 
			mColorMap.mFillColor,
			mColorMap.mMineButtonColor );
		mTeamAgentGrids = new AgentGrid[mNumTeams];
		mTeamFlagGrids = new FlagGrid[mNumTeams];
		mTeamBaseGrids = new BaseGrid[mNumTeams];
		
		mTeamInfo = new AgentInfo[mNumTeams][mNumAgentsPerTeam];
		
		// for each team
		for( int t=0; t<mNumTeams; t++ ) {
			
			int xTeamPosition;
			if( t==0 ) {
				xTeamPosition = 0;
				}
			else {
				xTeamPosition = mBoardSize - 1;
				}
			
			
			// for each agent on team
			for( int a=0; a<mNumAgentsPerTeam; a++ ) {
				mTeamInfo[t][a] = new AgentInfo();
				
				try {
					mTeamInfo[t][a].mAgent = 
						(Agent)( inTeamAgents[t].newInstance() );	
					}
				catch( IllegalAccessException inIAE ) {
					throw new IllegalArgumentException( 
						"Could not instantiate agent class " + t + "." );
					}
				catch( InstantiationException inIE ) {
					throw new IllegalArgumentException( 
						"Could not instantiate agent class " + t + "." );
					}
					
				// pass team color to agent
				mTeamInfo[t][a].mAgent.setColor( mColorMap.mLineColor, 
					mColorMap.mTeamColors[t] );
				
				// set agent location coordinates
				mTeamInfo[t][a].mAgentStartLocation.mX = xTeamPosition;
				int agentYPosition;
				if( a % 2 == 0 ) {
					// agent at top of board
					agentYPosition = a / 2;
					}
				else {
					// agent at bottom of board
					agentYPosition = mBoardSize - 1 - ( a / 2 );
					}
				mTeamInfo[t][a].mAgentStartLocation.mY = agentYPosition;
				mTeamInfo[t][a].mAgentLocation.mX = 
					mTeamInfo[t][a].mAgentStartLocation.mX;
				mTeamInfo[t][a].mAgentLocation.mY = 
					mTeamInfo[t][a].mAgentStartLocation.mY;
				}	// end for each agent on team
				
			// safe, assuming number of agents per team > 0 (checked above)
			mTeamAgentGrids[t] = 
				new AgentGrid( mBoardSize, mTeamInfo[t][0].mAgent );
			
			// add agent locations to agent grid
			for( int a=0; a<mNumAgentsPerTeam; a++ ) {
				mTeamAgentGrids[t].add( mTeamInfo[t][a].mAgentLocation.mX,
					mTeamInfo[t][a].mAgentLocation.mY );
				}
				
			mTeamBaseGrids[t] = new BaseGrid( mBoardSize, 
				mColorMap.mLineColor, mColorMap.mTeamColors[t]  );
			mTeamBaseGrids[t].setLocation( xTeamPosition, mBoardSize / 2 );	
			
			// setup flag grid for this team	
			mTeamFlagGrids[t] = new FlagGrid( mBoardSize, 
				mColorMap.mLineColor, mColorMap.mFlagPoleColor, 
				mColorMap.mTeamColors[t] );
			EnvironmentLocation flagLocation = mTeamBaseGrids[t].getLocation();
			mTeamFlagGrids[t].setLocation( flagLocation.mX, 
				flagLocation.mY );
				
			}	// end for each team
		
		} // end Game constructor
	
	private int mDelay = 1000;
	private int mPauseCheckDelay = 250;
	
	private boolean mPaused = true;
	private boolean mCanceled = false;
	
	/**
	 * Sets whether game is paused or not.
	 *
	 * @pararm inPaused true iff game should be set to paused.
	 */
	synchronized void setPause( boolean inPaused ) {
		mPaused = inPaused;
		}
	
	
	/**
	 * Gets whether game is paused or not.
	 *
	 * @return true iff game should be set to paused.
	 */
	private synchronized boolean isPaused() {
		return mPaused;
		}
		
	
	/**
	 * Cancels the current round of play.
	 *
	 */
	synchronized void cancel() {
		mCanceled = true;
		}
	
	
	/**
	 * Gets whether game is canceled or not.
	 *
	 * @return true iff game has been canceled.
	 */
	private synchronized boolean isCanceled() {
		return mCanceled;
		}
	
	
	/**
	 * Set delay between each step of play.
	 *
	 * @param inDelayInMS delay in milliseconds.
	 */
	synchronized void setDelay( int inDelayInMS ) {
		mDelay = inDelayInMS;
		}
	
	
	
	// score flags for this round
	private boolean mScored[] = { false, false };
	
	/**
	 * Plays a one-round game.  One round constitutes play
	 * up to the first flag capture (or up to draw threshold).
	 * (Note that state is reset to init. before start of each game).
	 *
	 * @param inDrawThreshold the number of state transitions
	 *   to make with no flag capture before declaring a draw.
	 *
	 * @return index of the winning team. (-1 if draw or cancel, and
	 *   2 if both teams score simultaneously).
	 */
	int playRound( int inDrawThreshold ) {
		reset();
		
		// fire event signifying that board setup is done
		mStateListener.stateChanged();
		
		try {
			for( int i=0; i<inDrawThreshold; i++ ) {
				if( isCanceled() ) {
					return -1;
					}
				while( isPaused() ) {
					Thread.sleep( mPauseCheckDelay );
					}
				Thread.sleep( mDelay );
				step();
				// check for scoring after this step
				if( mScored[0] && mScored[1] ) {
					// both scored at same time
					mStateListener.stateChanged();
					return 2;
					}
				else if( mScored[0] ) {
					mStateListener.stateChanged();
					return 0;//mTeamInfo[0][0].mAgent.getClass();
					}
				else if( mScored[1] ) {
					mStateListener.stateChanged();
					return 1;//mTeamInfo[1][0].mAgent.getClass();
					}
				mStateListener.stateChanged();			
				}
			}
		catch( InterruptedException inIE ) {
			}
	
		return -1;
		}



	/**
	 * Resets all grids back to init state.
	 */
	private void reset() {
		mScored[0] = false;
		mScored[1] = false;
		mPaused = true;
		mMineGrid.clear();
		for( int t=0; t<mNumTeams; t++ ) {
			for( int a=0; a<mNumAgentsPerTeam; a++ ) {
				mTeamAgentGrids[t].remove( mTeamInfo[t][a].mAgentLocation );
				
				mTeamInfo[t][a].mAgentLocation.mX = 
					mTeamInfo[t][a].mAgentStartLocation.mX;
				mTeamInfo[t][a].mAgentLocation.mY = 
					mTeamInfo[t][a].mAgentStartLocation.mY;
				mTeamInfo[t][a].mHasFlag = false;
				
				mTeamAgentGrids[t].add( mTeamInfo[t][a].mAgentLocation );
				
				}
			mTeamFlagGrids[t].setLocation( mTeamBaseGrids[t].getLocation() );	
			}		
		}



	int lastTeamToGoFirst = 1;

	/**
	 * Carries out one full-board state transition.
	 * Agents from teams get to make moves in the order that they appear
	 * in their team array, and teams make moves in interleaved order.
	 */
	private void step() {
		int newLastTeamToGoFirst = ( lastTeamToGoFirst + 1 ) % mNumTeams; 
		for( int a=0; a<mNumAgentsPerTeam; a++ ) {
		
			int t = newLastTeamToGoFirst;
			boolean done = false;
			// alternate between teams for each agent index
			while( !done ) {
				if( t == lastTeamToGoFirst ) {
					done = true;
					}
				int oppositeTeamIndex = (t + 1) % mNumTeams;
				// prepare snapshot for this agent
				EnvironmentSnapshot envSnapshot = 
					new EnvironmentSnapshot( mObstacleGrid,
							mTeamAgentGrids[t],
							mTeamAgentGrids[oppositeTeamIndex],
							mTeamBaseGrids[t],
							mTeamBaseGrids[oppositeTeamIndex],
							mTeamFlagGrids[t],
							mTeamFlagGrids[oppositeTeamIndex],
							mTeamInfo[t][a],
							mBoardSize );
				// pass the agent the environment snapshot
				// and get the agent's move as a result			
				int agentAction = 
					mTeamInfo[t][a].mAgent.getMove( envSnapshot );
				
				doAgentAction( mTeamAgentGrids[t],
					mTeamAgentGrids[oppositeTeamIndex],
					mTeamFlagGrids[t],
					mTeamFlagGrids[oppositeTeamIndex],
					mTeamBaseGrids[t],
					mTeamBaseGrids[oppositeTeamIndex],
					mTeamInfo[t][a],
					agentAction );
					
				t = ( t + 1 ) % mNumTeams;	
				}
			}
		lastTeamToGoFirst = newLastTeamToGoFirst;	
		}
	
	
	/** 
	 * Callback from state transition functions when an agent scores.
	 *
	 * @pararm inAgent the info wrapper for the agent that scored.
	 */
	private void agentScored( AgentInfo inAgent ) {
		for( int t=0; t<mNumTeams; t++ ) {
			for( int a=0; a<mNumAgentsPerTeam; a++ ) {
				if( mTeamInfo[t][a] == inAgent ) {
					mScored[t] = true;
					return;
					}
				}
			}
		}
	
	
	/**
	 * Gets <code>AgentInfo</code> for an agent in a grid position.
	 *
	 * @param inGrid the grid containing the agent.
	 * @pararm inLocation the location in the grid where the agent is.
	 *
	 * @return info wrapper for the matching agent, or <code>null</code>
	 *   if no matching agent found.
	 */
	private AgentInfo getAgentAtPosition( AgentGrid inGrid, 
		EnvironmentLocation inLocation ) {
		
		for( int t=0; t<mNumTeams; t++ ) {
			if( mTeamAgentGrids[t] == inGrid ) {
				for( int a=0; a<mNumAgentsPerTeam; a++ ) {
					if( mTeamInfo[t][a].mAgentLocation.equals( 
						inLocation ) ) {
						
						return mTeamInfo[t][a];
						}
					}
				// else none of agents on matching team have this position	
				return null;
				}
			}
		// else no teams match this grid	
		return null;		
		}
	
	
	
	/**
	 * Perform an agent action in the environment.
	 *
	 * @param inAgentGrid grid of agent positions for this team.
	 * @param inEnemyGrid grid of enemy agent positions.
	 * @param inOurFlagGrid grid of active agent team's flag position.
	 * @param inEnemyFlagGrid grid of enemy flag position.
	 * @pararm inOurBaseGrid grid of our base position.
	 * @pararm inEnemyBaseGrid grid of our base position.
	 * @pararm inActiveAgentInfo info wrapper for agent performing action.
	 * @pararm inAction action agent is performing.  Valid values defined
	 *   in {@link ctf.common.AgentAction}.
	 */
	private void doAgentAction( AgentGrid inAgentGrid,
		AgentGrid inEnemyGrid,
		FlagGrid inOurFlagGrid,
		FlagGrid inEnemyFlagGrid,
		BaseGrid inOurBaseGrid,
		BaseGrid inEnemyBaseGrid,
		AgentInfo inActiveAgentInfo, int inAction ) {
		
		if( inAction == AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE ) {
			mMineGrid.add( inActiveAgentInfo.mAgentLocation );
			}
		else {
			doAgentMove( inAgentGrid, inEnemyGrid,
				inOurFlagGrid, inEnemyFlagGrid, inOurBaseGrid,
				inEnemyBaseGrid, 
				inActiveAgentInfo, inAction );
			}
		}
	
	
	
	/**
	 * Perform an agent move in the environment.
	 *
	 * @param inAgentGrid grid of agent positions for this team.
	 * @param inEnemyGrid grid of enemy agent positions.
	 * @param inOurFlagGrid grid of active agent team's flag position.
	 * @param inEnemyFlagGrid grid of enemy flag position.
	 * @pararm inOurBaseGrid grid of our base position.
	 * @pararm inEnemyBaseGrid grid of our base position.
	 * @pararm inActiveAgentInfo info wrapper for agent performing action.
	 * @pararm inMove agent is performing.  Valid values are moves defined
	 *   in {@link ctf.common.AgentAction}, along with <code>DO_NOTHING</code>.
	 */
	private void doAgentMove( AgentGrid inAgentGrid, 
		AgentGrid inEnemyGrid,
		FlagGrid inOurFlagGrid, FlagGrid inEnemyFlagGrid, 
		BaseGrid inOurBaseGrid,
		BaseGrid inEnemyBaseGrid,
		AgentInfo inActiveAgentInfo, int inMove ) {
		
		int currentX = inActiveAgentInfo.mAgentLocation.mX;
		int currentY = inActiveAgentInfo.mAgentLocation.mY;
		boolean blocked;
		switch( inMove ) {
			case( AgentAction.MOVE_NORTH ):
				blocked = ( currentY == 0 );
				if( !blocked ) {
					// check for obstacles, teamates, and ourbase+ourflag
					blocked = 
						mObstacleGrid.exists( currentX, currentY - 1 )
						|| inAgentGrid.exists( currentX, currentY - 1 )
						|| ( inOurBaseGrid.exists( currentX, currentY - 1 )
						&& inOurFlagGrid.exists( currentX, currentY - 1 ) 
						&& !inActiveAgentInfo.mHasFlag
						&& !inEnemyGrid.exists( currentX, currentY - 1 ) );
					if( !blocked ) {
						inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
						inActiveAgentInfo.mAgentLocation.mY =
							currentY - 1;
						inAgentGrid.add( inActiveAgentInfo.mAgentLocation );	
						}
					}
				break;
			case( AgentAction.MOVE_SOUTH ):
				blocked = ( currentY == mBoardSize - 1 );
				if( !blocked ) {
					// check for obstacles, teamates, and ourbase+ourflag
					blocked = 
						mObstacleGrid.exists( currentX, currentY + 1 )
						|| inAgentGrid.exists( currentX, currentY + 1 )
						|| ( inOurBaseGrid.exists( currentX, currentY + 1 )
						&& inOurFlagGrid.exists( currentX, currentY + 1 ) 
						&& !inActiveAgentInfo.mHasFlag
						&& !inEnemyGrid.exists( currentX, currentY + 1 ) );
					if( !blocked ) {
						inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
						inActiveAgentInfo.mAgentLocation.mY =
							currentY + 1;
						inAgentGrid.add( inActiveAgentInfo.mAgentLocation );
						}
					}
				break;
			case( AgentAction.MOVE_EAST ):
				blocked = ( currentX == mBoardSize - 1 );
				if( !blocked ) {
					// check for obstacles, teamates, and ourbase+ourflag
					blocked = 
						mObstacleGrid.exists( currentX + 1, currentY )
						|| inAgentGrid.exists( currentX + 1, currentY )
						|| ( inOurBaseGrid.exists( currentX + 1, currentY )
						&& inOurFlagGrid.exists( currentX + 1, currentY ) 
						&& !inActiveAgentInfo.mHasFlag
						&& !inEnemyGrid.exists( currentX + 1, currentY ) );
					if( !blocked ) {
						inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
						inActiveAgentInfo.mAgentLocation.mX =
							currentX + 1;
						inAgentGrid.add( inActiveAgentInfo.mAgentLocation );	
						}
					}
				break;
			case( AgentAction.MOVE_WEST ):
				blocked = ( currentX == 0 );
				if( !blocked ) {
					// check for obstacles, teamates, and ourbase+ourflag
					blocked = 
						mObstacleGrid.exists( currentX - 1, currentY )
						|| inAgentGrid.exists( currentX - 1, currentY )
						|| ( inOurBaseGrid.exists( currentX - 1, currentY )
						&& inOurFlagGrid.exists( currentX - 1, currentY ) 
						&& !inActiveAgentInfo.mHasFlag
						&& !inEnemyGrid.exists( currentX - 1, currentY ) );
					if( !blocked ) {
						inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
						inActiveAgentInfo.mAgentLocation.mX =
							currentX - 1;
						inAgentGrid.add( inActiveAgentInfo.mAgentLocation );	
						}
					}
				break;
			default:
				blocked = false;
				break;	
			}
		
		
		if( inActiveAgentInfo.mHasFlag ) {
			// move flag with agent
			inEnemyFlagGrid.setLocation( inActiveAgentInfo.mAgentLocation );
			}
		else {
			// at end of move, if agent is on enemy flag, agent picks up flag
			if( inEnemyFlagGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
				inActiveAgentInfo.mHasFlag = true;
				}
			}
		
		// at end of move, check if agent is sitting on an enemy.
		// if so, send both back to their starting positions, and
		// return any flags that they have
		if( inEnemyGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
			if( inOurFlagGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
				// send our flag back to our base
				inOurFlagGrid.setLocation( inOurBaseGrid.getLocation() );
				}
			if( inEnemyFlagGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
				// send enemy flag back to enemy base
				inEnemyFlagGrid.setLocation( inEnemyBaseGrid.getLocation() );
				}
				
			// move enemy agent back to start
			AgentInfo enemyInfo = getAgentAtPosition( inEnemyGrid,
				inActiveAgentInfo.mAgentLocation );
			if( enemyInfo != null ) {
				inEnemyGrid.remove( enemyInfo.mAgentLocation );
				inEnemyGrid.add( enemyInfo.mAgentStartLocation );
				enemyInfo.mAgentLocation.mX =
					enemyInfo.mAgentStartLocation.mX;
				enemyInfo.mAgentLocation.mY = 
					enemyInfo.mAgentStartLocation.mY;	
				enemyInfo.mHasFlag = false;
				}
			
				
			// move our agent back to start	
			inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
			inAgentGrid.add( inActiveAgentInfo.mAgentStartLocation );
			inActiveAgentInfo.mAgentLocation.mX = 
				inActiveAgentInfo.mAgentStartLocation.mX;
			inActiveAgentInfo.mAgentLocation.mY = 
				inActiveAgentInfo.mAgentStartLocation.mY;
			inActiveAgentInfo.mHasFlag = false;
			
				
			}

		// at end of move, check if agent is sitting on a mine.
		// if so, mine goes off, and agent goes back to starting location.
		if( mMineGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
			// agent on a mine
			mMineGrid.remove( inActiveAgentInfo.mAgentLocation ); 
			
			inAgentGrid.remove( inActiveAgentInfo.mAgentLocation );
			inAgentGrid.add( inActiveAgentInfo.mAgentStartLocation );
			
			inActiveAgentInfo.mAgentLocation.mX = 
				inActiveAgentInfo.mAgentStartLocation.mX;
			inActiveAgentInfo.mAgentLocation.mY = 
				inActiveAgentInfo.mAgentStartLocation.mY;
			if( inActiveAgentInfo.mHasFlag ) {
				// move flag back to enemy base
				inEnemyFlagGrid.setLocation( inEnemyBaseGrid.getLocation() );
				inActiveAgentInfo.mHasFlag = false;
				}		
			}
		
		// finally, if the agent made it through all of those checks...
		// check if agent has enemy flag and has made it back to its own base
		if( inActiveAgentInfo.mHasFlag ) {
			if( inOurBaseGrid.exists( inActiveAgentInfo.mAgentLocation ) ) {
				// agent scored!
				agentScored( inActiveAgentInfo );
				}
			}
		}
	
	
	
	/**
	 * Set game-wide color map.
	 *
	 * @param inColorMap new color map for game objects.
	 */
	synchronized void setColorMap( GameColorMap inColorMap ) {
		mColorMap = inColorMap;
		mMineGrid.setLineColor( mColorMap.mLineColor );
		mMineGrid.setFillColor( mColorMap.mFillColor );
		mMineGrid.setMineButtonColor( mColorMap.mMineButtonColor );
		
		mObstacleGrid.setLineColor( mColorMap.mLineColor );
		mObstacleGrid.setFillColor( mColorMap.mObstacleColor );
		
		for( int t=0; t<mNumTeams; t++ ) {
			mTeamFlagGrids[t].setLineColor( mColorMap.mLineColor );
			mTeamBaseGrids[t].setLineColor( mColorMap.mLineColor );
			
			mTeamFlagGrids[t].setFillColor( mColorMap.mFlagPoleColor );
			mTeamBaseGrids[t].setFillColor( mColorMap.mTeamColors[t] );
			mTeamFlagGrids[t].setFlagClothColor( mColorMap.mTeamColors[t] );
			
			for( int a=0; a<mNumAgentsPerTeam; a++ ) {
				mTeamInfo[t][a].mAgent.setColor( mColorMap.mLineColor,
					mColorMap.mTeamColors[t] );
				}
			}
		}
	
	
	
	/**
	 * Draws the current game state in a graphics context.  Not double-buffered.
	 *
	 * @param inGraphics the context in which to draw the current game state.
	 * @param inStartX the starting x location in the context where the
	 *   game should be drawn.
	 * @param inStartY the starting y location in the context where the
	 *   game should be drawn.
	 * @param inWide the width of the game image in the context.
	 * @param inHigh the width of the game image in the context.
	 */
	synchronized void draw( Graphics inGraphics, int inStartX, int inStartY, 
		int inWide, int inHigh ) {
		
		inGraphics.setColor( mColorMap.mBackgroundColor );
		
		// get dimensions of grid when drawn
		Dimension actualGridSize = mObstacleGrid.getDrawDimension( inWide,
			inHigh );
		
		// draw background of this size	
		inGraphics.fillRect( inStartX, inStartY, 
				actualGridSize.width, actualGridSize.height );
		
		inGraphics.setColor( mColorMap.mLineColor );

		// draw outline background	
		inGraphics.drawRect( inStartX, inStartY, 
				actualGridSize.width, actualGridSize.height );

		
		// draw various layers of board
		mObstacleGrid.draw( inGraphics, 
			inStartX, inStartY, inWide, inHigh );
		mMineGrid.draw( inGraphics, 
			inStartX, inStartY, inWide, inHigh );
		for( int t=0; t<mNumTeams; t++ ) {
			mTeamBaseGrids[t].draw( inGraphics, 
				inStartX, inStartY, inWide, inHigh );
			}
		for( int t=0; t<mNumTeams; t++ ) {
			mTeamAgentGrids[t].draw( inGraphics, 
				inStartX, inStartY, inWide, inHigh );
			}
		for( int t=0; t<mNumTeams; t++ ) {
			mTeamFlagGrids[t].draw( inGraphics, 
				inStartX, inStartY, inWide, inHigh );
			}	
		}
	
	}
