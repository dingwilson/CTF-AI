package ctf.environment;

import ctf.agent.Agent;

/**
 * Class containing an agent and current information about the agent
 * that pertains to the current game.
 *
 */
class AgentInfo {
	
	public Agent mAgent;
	
	public EnvironmentLocation mAgentStartLocation = new EnvironmentLocation();
	
	public EnvironmentLocation mAgentLocation = new EnvironmentLocation();
	
	public boolean mHasFlag = false; 
	}
