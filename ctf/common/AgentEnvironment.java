package ctf.common;


/**
 * Interface that can be passed to an agent to let it know
 * what's going on in the environment.  This is an agent's 
 * only window into the state of the environment, and the information
 * provided is agent-centric (in other words, the point of 
 * reference is the agent's current location).<BR>
 * <BR>
 * All methods return booleans that describe whether the particular
 * environment property is true.<BR>
 * <BR>
 * During the game, an <code>AgentEnvironment</code> object will be passed
 * in to your agent through your agent's <code>getMove</code> method.  
 * You can inspect the environment using the methods provided by the
 * <code>AgentEnvironment</code>.
 *
 */
public interface AgentEnvironment {
	
	/**
	 * Our team identifier constant.
	 */
	public static final int OUR_TEAM = 0;
	
	/**
	 * Enemy team identifier constant.
	 */
	public static final int ENEMY_TEAM = 1;
	
	
	/* Note:
	 * All methods that check for flag locations return true
	 * if the flag in the specified location is either being carried
	 * by an agent or is sitting on the playing surface.
	 *
	 * Our team can't carry our own flag (only the enemy team can carry
	 * our flag).  As soon as the carrying enemy is tagged, our 
	 * flag is returned to our base.  Vice versa for the case where
	 * we are tagged while carrying the enemy flag.
	 */
	
	
	/**
	 * Returns true iff the specified team's flag is 
	 * somewhere to the north of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for flag of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the north of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isFlagNorth( int inTeam, boolean inImmediate );



	/**
	 * Returns true iff the specified team's flag is 
	 * somewhere to the south of this
	 * agent (below agent's current vertical position).
	 *
	 * @param inTeam check for flag of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the south of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isFlagSouth( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's flag is 
	 * somewhere to the east of this
	 * agent (to right of agent's current horizontal position).
	 *
	 * @param inTeam check for flag of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the east of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isFlagEast( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's flag is 
	 * somewhere to the west of this
	 * agent (to left of agent's current horizontal position).
	 *
	 * @param inTeam check for flag of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the west of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isFlagWest( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's base is 
	 * somewhere to the north of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for base of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the north of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isBaseNorth( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's base is 
	 * somewhere to the south of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for base of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the south of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isBaseSouth( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's base is 
	 * somewhere to the east of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for base of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the east of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isBaseEast( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff the specified team's base is 
	 * somewhere to the west of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for base of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the west of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isBaseWest( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff an agent from the specified team is 
	 * somewhere to the north of this
	 * agent (above agent's current vertical position).
	 *
	 * @param inTeam check for agent of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the north of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isAgentNorth( int inTeam, boolean inImmediate );



	/**
	 * Returns true iff an agent from the specified team is 
	 * somewhere to the south of this
	 * agent (below agent's current vertical position).
	 *
	 * @param inTeam check for agent of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the south of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isAgentSouth( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff an agent from the specified team is 
	 * somewhere to the east of this
	 * agent (to right of agent's current horizontal position).
	 *
	 * @param inTeam check for agent of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the east of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isAgentEast( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff an agent from the specified team is 
	 * somewhere to the west of this
	 * agent (to left of agent's current horizontal position).
	 *
	 * @param inTeam check for agent of this team, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 * @param inImmediate set to true if trying to check only
	 *   space immediately to the west of this agent (and thus
	 *   spaces further away are ignored).
	 */
	public boolean isAgentWest( int inTeam, boolean inImmediate );
	
	
	
	/**
	 * Returns true iff this agent has the enemy flag.
	 */
	public boolean hasFlag();
	
	
	
	/**
	 * Returns true iff an agent on the specified team has the 
	 * other team's flag.
	 *
	 * @param inTeam check if this team has the other team's flag, valid values
	 *   in [OUR_TEAM, ENEMY_TEAM].
	 */
	public boolean hasFlag( int inTeam );
	
	
	
	/* Note:
	 * Agents can't see obstacles that are more than one space away.
	 */
	
	/**
	 * Returns true an obstacle is
	 * in the space immediately
	 * north of this agent.
	 */
	public boolean isObstacleNorthImmediate();



	/**
	 * Returns true an obstacle is
	 * in the space immediately
	 * south of this agent.
	 */
	public boolean isObstacleSouthImmediate();
	
	
	
	/**
	 * Returns true an obstacle is
	 * in the space immediately
	 * east of this agent.
	 */
	public boolean isObstacleEastImmediate();
	
	
	
	/**
	 * Returns true an obstacle is
	 * in the space immediately
	 * north of this agent.
	 */
	public boolean isObstacleWestImmediate();
	
	}
