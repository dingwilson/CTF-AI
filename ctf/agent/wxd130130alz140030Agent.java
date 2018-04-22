package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;

public class wxd130130alz140030Agent extends Agent {
    int agent = 0; // 1 designates north agent, 2 designates southagent

    int agentLocationX; // current x location of agent
    int agentLocationY; // current y location of agent

    boolean initializing = true;    // initialization phase
    
    public static Tile[][] map;    // map of environment, initialize after hitting flag
    public static int mapSizeN; // the value of N, given the NxN map

    public static Boolean isEast;   // figure out east vs west

    public static int agent1NumTiles = 1;   // number of steps for agent 1 to hit flag
    public static int agent2NumTiles = 1;   // number of steps for agent 2 to hit flag
    public static boolean agent1DoneInitializing = false;   // is agent 1 done finding numTiles
    public static boolean agent2DoneInitializing = false;   // is agent 2 done finding numTiles

    public int getMove(AgentEnvironment inEnvironment) {
        if (initializing) {
            if (agent == 0) {   // set agent number if not set
                agent = getAgentNumber(inEnvironment);
            }

            if (isEast == null) {   // find out spawn loc in east or west if null
                isEast = isAgentEast(inEnvironment);
            }

            if (agent == 1) {
                if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, true)) {
                    agent1DoneInitializing = true;

                    // TODO: logic to place mines for agent 1, then set initialize to false

                } else {
                    agent1NumTiles++;
                    return AgentAction.MOVE_SOUTH;
                }
            } else if (agent == 2) {
                if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, true)) {
                    agent2DoneInitializing = true;

                    // TODO: logic to place mines for agent 2, then set initialize to false

                } else {
                    agent2NumTiles++;
                    return AgentAction.MOVE_NORTH;
                }
            } else {
                return AgentAction.DO_NOTHING;  // this should never occur
            }
        } else {
            if (map == null && agent1DoneInitializing && agent2DoneInitializing) {  // if map is null, instantiate map
                mapSizeN = agent1NumTiles + agent2NumTiles + 1;    // agent1 + agent2 + flag
                map = new Tile[mapSizeN][mapSizeN];   // create map object

                if (agent == 1) {

                } else if (agent == 2) {

                }
            }

            didAgentDie(inEnvironment); // check if agent has died.

            if (map != null) {
                getEnvironmentInfo(inEnvironment, agentLocationX, agentLocationY);
            }

            // TODO: this occurs after initialization. make move - heuristics go here
        }

        return AgentAction.DO_NOTHING;  // this should never occur
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
            } else {
                map[agentLocationX][agentLocationY - 1].isObstacle = false;
            }

            if (inEnvironment.isObstacleSouthImmediate() && agentLocationY != mapSizeN - 1) {
                map[agentLocationX][agentLocationY + 1].isObstacle = true;
            } else {
                map[agentLocationX][agentLocationY + 1].isObstacle = false;
            }

            if (inEnvironment.isObstacleEastImmediate() && agentLocationX != mapSizeN - 1) {
                map[agentLocationX + 1][agentLocationY].isObstacle = true;
            } else {
                map[agentLocationX + 1][agentLocationY].isObstacle = false;
            }

            if (inEnvironment.isObstacleWestImmediate() && agentLocationX != 0) {
                map[agentLocationX - 1][agentLocationY].isObstacle = true;
            } else {
                map[agentLocationX - 1][agentLocationY].isObstacle = false;
            }
        }
    }

    // finds out if agent is east or west starting location
    private Boolean isAgentEast(AgentEnvironment inEnvironment) {
        if (inEnvironment.isObstacleEastImmediate() && !inEnvironment.isObstacleWestImmediate()) {
            return false;
        } else if (inEnvironment.isObstacleWestImmediate() && !inEnvironment.isObstacleEastImmediate()) {
            return true;
        }

        return null;
    }

    // checks if agent died. if so, reset agent location, initializing, etc.
    private void didAgentDie(AgentEnvironment inEnvironment) {
        if (!initializing) {    // should not run this while initializing
            if (agent == 1) {
                if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, false)) { // died
                    if (isEast) {
                        agentLocationX = mapSizeN - 1;
                    } else {
                        agentLocationX = 0;
                    }

                    agentLocationY = 0;

                    resetDeadAgent();
                }
            } else if (agent == 2) {
                if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, false)) { // died
                    if (isEast) {
                        agentLocationX = mapSizeN - 1;
                    } else {
                        agentLocationX = 0;
                    }

                    agentLocationY = mapSizeN - 1;

                    resetDeadAgent();
                }
            }
        }
    }

    // generic death reset things (specific things belong in didAgentDie())
    private void resetDeadAgent() {
        initializing = true;
    }

    // Tile object to hold whether each tile is an obstacle or a base
    public class Tile {
        public boolean isObstacle;    // is tile obstacle?
        public boolean isBase;    // is tile a base?
        
        public Tile() { // init
            
        }
    }
}
