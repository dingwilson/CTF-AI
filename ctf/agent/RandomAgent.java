package ctf.agent;


import ctf.common.AgentEnvironment;
import ctf.agent.Agent;

import ctf.common.AgentAction;

/**
 * A sample agent that makes completely random moves and doesn't 
 * examine environment at all.
 * 
 */
public class RandomAgent extends Agent {
	
	// implements Agent.getMove() interface
	public int getMove( AgentEnvironment inEnvironment ) {
		
		double rand = Math.random();
		
		if( rand < 0.25 ) {
			return AgentAction.MOVE_NORTH;
			}
		else if( rand < 0.5 ) {
			return AgentAction.MOVE_SOUTH;
			}
		else if( rand < 0.75 ) {
			return AgentAction.MOVE_EAST;
			}
		else {
			return AgentAction.MOVE_WEST;
			}	
		}

	}
