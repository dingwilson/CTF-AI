package ctf.common;


/**
 * Class that defines action options available to each
 * agent at each time step.  Agents must pick exactly one of these
 * options for each move.
 *
 */
public class AgentAction {

	public static final int MOVE_NORTH = 0;
	public static final int MOVE_SOUTH = 1;
	public static final int MOVE_EAST = 2;
	public static final int MOVE_WEST = 3;
	
	/**
	 * Planted mine becomes active in next time step.<BR>
	 * When activated, mine will blow up any agent that enters
	 * this space on the play surface.<BR>
	 * Mines are invisible to all agents.<BR>
	 */
	public static final int PLANT_HYPERDEADLY_PROXIMITY_MINE = 379037;
	
	public static final int DO_NOTHING = -1;
	}
