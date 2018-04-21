package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;

public class wxd130130alz140030Agent extends Agent {   
    int agent = 0; // 1 designates north agent, 2 designates southagent

    int stepsToInitialize = 0;  // counter to find size of map

    int agentLocationX; // current x location of agent
    int agentLocationY; // current y location of agent

    boolean initializing = true;    // initialization phase
    boolean initializeMine = false; // planted mine
    
    public static Tile[][] map;    // map of environment, initialize after hitting flag
    
    public int getMove(AgentEnvironment inEnvironment) {
        if (initializing) {
            if (agent == 0) {   // set agent number if not set
                agent = getAgentNumber(inEnvironment);
            }

            if (agent == 1) {
                if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, true)) {
                    if (!initializeMine) {
                        initializeMine = true;
                        return AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;
                    } else {
                        // TODO: figure out what side (left or right) of map we are on
                        // TODO: move west or east 2x, then south
                    }
                } else {
                    stepsToInitialize++;
                    return AgentAction.MOVE_SOUTH;
                }
            } else if (agent == 2) {
                if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, true)) {
                    if (!initializeMine) {
                        initializeMine = true;
                        return AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;
                    } else {
                        // TODO: figure out what side (left or right) of map we are on
                        // TODO: move west or east 1x, then plant mine, then up one, then plant mine, then up
                    }
                } else {
                    stepsToInitialize++;
                    return AgentAction.MOVE_NORTH;
                }
            }

            return AgentAction.MOVE_WEST;
        } else {
            didAgentDie(inEnvironment); // check if agent has died. 
            getEnvironmentInfo(inEnvironment, agentLocationX, agentLocationY);

            // make move
        }

        return AgentAction.DO_NOTHING;  // TODO: remove
    }

    private int getAgentNumber(AgentEnvironment inEnvironment) {
        if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, false)) {
            return 1;
        } else if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, false)) {
            return 2;
        } else {
            return 3;   // something went wrong. This should NEVER happen.
        }
    }

    // update all available info for current location in environment
    private void getEnvironmentInfo(AgentEnvironment inEnvironment, int agentLocationX, int agentLocationY) {
        if (!initializing) {    // should not run this while initializing
            if (inEnvironment.isObstacleNorthImmediate() && agentLocationY != 0) {
                map[agentLocationX][agentLocationY - 1].isObstacle = true;
            }

            if (inEnvironment.isObstacleSouthImmediate()) {  // TODO: need to add "&& agentLocationY != <value of N>" to if statement
                map[agentLocationX][agentLocationY + 1].isObstacle = true;
            }

            if (inEnvironment.isObstacleEastImmediate()) {  // TODO: need to add "&& agentLocationX != <value of N>" to if statement
                map[agentLocationX + 1][agentLocationY].isObstacle = true;
            }

            if (inEnvironment.isObstacleWestImmediate() && agentLocationX != 0) {
                map[agentLocationX - 1][agentLocationY].isObstacle = true;
            }
        }
    }

    // checks if agent died. if so, reset agent location, initializing, etc.
    private void didAgentDie(AgentEnvironment inEnvironment) {
        if (!initializing) {    // should not run this while initializing
            if (agent == 1) {
                if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, false)) { // died
                    // TODO: reset agentLocationX
                    // TODO: reset agentLocationY

                    resetDeadAgent();
                }
            } else if (agent == 2) {
                if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, false)) { // died
                    // TODO: reset agentLocationX
                    // TODO: reset agentLocationY

                    resetDeadAgent();
                }
            }
        }
    }

    // generic death reset things (specific things belong in didAgentDie())
    private void resetDeadAgent() {
        initializing = true;
        initializeMine = false;
    }

    // Tile object to hold whether each tile is an obstacle or a base
    public class Tile {
        public boolean isObstacle;    // is tile obstacle?
        public boolean isBase;    // is tile a base?
        public boolean isMine;      // is tile mine?
        
        public Tile() { // init
            
        }
    }
}
