package ctf.agent;


import ctf.common.AgentEnvironment;
import ctf.agent.Agent;

import ctf.common.AgentAction;

/**
 * A sample agent implementing the Agent interface.
 * Your class should look similar to this, except it should be called
 * "YourNetIDAgent".
 * <BR>
 * This agent plays in a rather naive fashion.  It has no notion of
 * defense.  It heads towards the enemy flag until it gets the flag,
 * and then it heads back towards its own base.  It can avoid some 
 * obstacles, but still gets stuck permanently in certain situations.
 * It has no notion of its teammates or its enemies, so it doesn't try to 
 * avoid them at all.
 * 
 */
public class SimpleAgent extends Agent {
	
	// implements Agent.getMove() interface
	public int getMove( AgentEnvironment inEnvironment ) {
		
		// booleans describing direction of goal
		// goal is either enemy flag, or our base
		boolean goalNorth;
		boolean goalSouth;
		boolean goalEast;
		boolean goalWest;

		
		if( !inEnvironment.hasFlag() ) {
			// make goal the enemy flag
			goalNorth = inEnvironment.isFlagNorth( 
				inEnvironment.ENEMY_TEAM, false );
		
			goalSouth = inEnvironment.isFlagSouth( 
				inEnvironment.ENEMY_TEAM, false );
		
			goalEast = inEnvironment.isFlagEast( 
				inEnvironment.ENEMY_TEAM, false );
		
			goalWest = inEnvironment.isFlagWest( 
				inEnvironment.ENEMY_TEAM, false );
			}
		else {
			// we have enemy flag.
			// make goal our base
			goalNorth = inEnvironment.isBaseNorth( 
				inEnvironment.OUR_TEAM, false );
		
			goalSouth = inEnvironment.isBaseSouth( 
				inEnvironment.OUR_TEAM, false );
		
			goalEast = inEnvironment.isBaseEast( 
				inEnvironment.OUR_TEAM, false );
		
			goalWest = inEnvironment.isBaseWest( 
				inEnvironment.OUR_TEAM, false );
			}
		
		// now we have direction booleans for our goal	
		
		// check for immediate obstacles blocking our path		
		boolean obstNorth = inEnvironment.isObstacleNorthImmediate();
		boolean obstSouth = inEnvironment.isObstacleSouthImmediate();
		boolean obstEast = inEnvironment.isObstacleEastImmediate();
		boolean obstWest = inEnvironment.isObstacleWestImmediate();
		
		
		// if the goal is north only, and we're not blocked
		if( goalNorth && ! goalEast && ! goalWest && !obstNorth ) {
			// move north
			return AgentAction.MOVE_NORTH;
			}
			
		// if goal both north and east
		if( goalNorth && goalEast ) {
			// pick north or east for move with 50/50 chance
			if( Math.random() < 0.5 && !obstNorth ) {
				return AgentAction.MOVE_NORTH;
				}
			if( !obstEast ) {	
				return AgentAction.MOVE_EAST;
				}
			if( !obstNorth ) {	
				return AgentAction.MOVE_NORTH;
				}
			}	
			
		// if goal both north and west	
		if( goalNorth && goalWest ) {
			// pick north or west for move with 50/50 chance
			if( Math.random() < 0.5 && !obstNorth ) {
				return AgentAction.MOVE_NORTH;
				}
			if( !obstWest ) {	
				return AgentAction.MOVE_WEST;
				}
			if( !obstNorth ) {	
				return AgentAction.MOVE_NORTH;
				}	
			}
		
		// if the goal is south only, and we're not blocked
		if( goalSouth && ! goalEast && ! goalWest && !obstSouth ) {
			// move south
			return AgentAction.MOVE_SOUTH;
			}
		
		// do same for southeast and southwest as for north versions	
		if( goalSouth && goalEast ) {
			if( Math.random() < 0.5 && !obstSouth ) {
				return AgentAction.MOVE_SOUTH;
				}
			if( !obstEast ) {
				return AgentAction.MOVE_EAST;
				}
			if( !obstSouth ) {
				return AgentAction.MOVE_SOUTH;
				}
			}
				
		if( goalSouth && goalWest && !obstSouth ) {
			if( Math.random() < 0.5 ) {
				return AgentAction.MOVE_SOUTH;
				}
			if( !obstWest ) {
				return AgentAction.MOVE_WEST;
				}
			if( !obstSouth ) {
				return AgentAction.MOVE_SOUTH;
				}
			}
		
		// if the goal is east only, and we're not blocked
		if( goalEast && !obstEast ) {
			return AgentAction.MOVE_EAST;
			}
		
		// if the goal is west only, and we're not blocked	
		if( goalWest && !obstWest ) {
			return AgentAction.MOVE_WEST;
			}	
		
		// otherwise, make any unblocked move
		if( !obstNorth ) {
			return AgentAction.MOVE_NORTH;
			}
		else if( !obstSouth ) {
			return AgentAction.MOVE_SOUTH;
			}
		else if( !obstEast ) {
			return AgentAction.MOVE_EAST;
			}
		else if( !obstWest ) {
			return AgentAction.MOVE_WEST;
			}	
		else {
			// completely blocked!
			return AgentAction.DO_NOTHING;
			}	
		}

	}
