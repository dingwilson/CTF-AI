package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;
import java.util.*;

public class wxd130130alz140030Agent extends Agent {
    private int agent = 0;  // 0 = not found, 1 = north agent, 2 = south agent

    private String startingObstacles = "2222";  // 0 = no obstacle , 1 = obstacle. NESW

    private static boolean initializing = true;    // searching for map type

    private static Boolean isSpawnEast;   // null = not found, true = east spawn, false = west spawn

    private int agentXLoc;  // current agent X location
    private int agentYLoc;  // current agent Y location

    private static HashMap<String, Boolean[][]> potentialMaps;  // hashmap of potential maps

    private boolean useSimpleAgent = false;  // use simple agent (if all else fails...)

    // hardcoded check/start path for Agent 1
    private final String[] startEast = new String[]{"down", "left", "left", "left"};
    private final String[] startWest = new String[]{"down", "right", "right", "right"};

    // hardcoded paths for simple and empty (es) maps
    private final String[] esEast = new String[]{"left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "up", "up", "right", "right", "right", "right", "right", "right", "right", "right", "right", "down"};
    private final String[] esWest = new String[]{"right", "right", "right", "right", "right", "right", "right","right", "right", "up", "up", "up", "up", "up", "left", "left", "left", "left", "left", "left", "left", "left", "left", "down"};

    // hardcoded paths for x, wall, traps (xwt) maps
    private final String[] xwtEast = new String[]{"left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "up", "down", "down", "down", "down", "right", "right", "right", "right", "right", "right", "right", "right", "right", "up", "up", "up","left", "left", "up", "up", "right", "right", "down"};
    private final String[] xwtWest = new String[]{"right", "right", "right", "right", "right", "right", "right", "right", "right", "up", "up", "up", "up", "down", "down", "down", "down","left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "right", "right", "up", "up", "left", "left", "down"};

    private static String[] path;  // chosen path

    private int clock = 0;  // move clock

    public int getMove(AgentEnvironment inEnvironment) {
        if (useSimpleAgent) {   // if simple agent...
            return simpleAgent(inEnvironment);
        }

        if (initializing) {
            if (agent == 0) {   // get agent number if not set
                potentialMaps = setupPotentialMaps();

                agent = getAgentNumber(inEnvironment);
                startingObstacles = getObstacleString(inEnvironment);
            }

            if (isSpawnEast == null) {  // get spawn location (if possible)
                isSpawnEast = isSpawnEast(inEnvironment);
            }

            if (agent == 1) {   // agent = 1 = north agent
                // TESTING FOR X
                if ((clock == 1 && isSpawnEast && inEnvironment.isObstacleWestImmediate()) || (clock == 1 && !isSpawnEast && inEnvironment.isObstacleEastImmediate())) {
                    if (isSpawnEast) {
                        path = new String[xwtEast.length];
                        path = Arrays.copyOf(xwtEast,xwtEast.length);
                    } else {
                        path = new String[xwtWest.length];
                        path = Arrays.copyOf(xwtWest,xwtWest.length);
                    }
                    
                    System.out.println("the map is X");

                    initializing = false;
                    clock = 0;
                    return AgentAction.MOVE_NORTH;
                }
            
                // TESTING FOR ???
                if ((clock == 2 && isSpawnEast && inEnvironment.isObstacleWestImmediate()) || (clock == 2 && !isSpawnEast && inEnvironment.isObstacleEastImmediate())) {
                    if (isSpawnEast) {
                        path = new String[xwtEast.length];
                        path = Arrays.copyOf(xwtEast,xwtEast.length);
                    } else {
                        path = new String[xwtWest.length];
                        path = Arrays.copyOf(xwtWest,xwtWest.length);
                    }

                    initializing = false;
                }

                // TESTING FOR SIMPLE
                if (clock == 3 && inEnvironment.isObstacleSouthImmediate()) {
                    System.out.println("SimpleMap");
                    initializing = false;
                    
                    if (isSpawnEast) {
                        path = new String[esEast.length];
                        path = Arrays.copyOf(esEast, esEast.length);
                    } else {
                        path = new String[esWest.length];
                        path = Arrays.copyOf(esWest, esWest.length);
                    }
                }

                // TESTING FOR ???
                if (clock == 4) {
                    if ((isSpawnEast && inEnvironment.isObstacleWestImmediate()) || !isSpawnEast && inEnvironment.isObstacleEastImmediate()) {
                        if (isSpawnEast) {
                            path = new String[xwtEast.length];
                            path = Arrays.copyOf(xwtEast,xwtEast.length);
                        } else {
                            path = new String[xwtWest.length];
                            path = Arrays.copyOf(xwtWest,xwtWest.length);
                        }
                    }
                } else if (clock == 4) {    // OTHERWISE EMPTY
                    System.out.println("Empty");
                    initializing = false;

                    if (isSpawnEast) {
                        path = new String[esEast.length];
                        path = Arrays.copyOf(esEast, esEast.length);
                    } else {
                        path = new String[esWest.length];
                        path = Arrays.copyOf(esWest, esWest.length);
                    }    
                }

                String move;

                if (isSpawnEast) {
                    move = startEast[clock];
                } else {
                    move = startWest[clock];
                }

                clock++;

                switch (move) {
                    case "left":
                        return AgentAction.MOVE_WEST;
                    case "right":
                        return AgentAction.MOVE_EAST;
                    case "up":
                        return AgentAction.MOVE_NORTH;
                    case "down":
                        return AgentAction.MOVE_SOUTH;
                    default:
                        return AgentAction.DO_NOTHING;
                }
            } else if (agent == 2) {    // agent = 2 = south agent
                return AgentAction.DO_NOTHING;
                // TODO: Follow intial setup for agent 2

            } else {    // this should never happen...
                useSimpleAgent = true;
                return simpleAgent(inEnvironment);
            }
        } else {
            String move = path[clock];
            clock++;

            if (isAgentDead(inEnvironment)) {
                clock = 0;
            }

            if (agent == 1) {
                switch (move) {
                    case "left":
                        return AgentAction.MOVE_WEST;
                    case "right":
                        return AgentAction.MOVE_EAST;
                    case "up":
                        return AgentAction.MOVE_SOUTH;  // flipped for north agent
                    case "down":
                        return AgentAction.MOVE_NORTH;  // flipped for north agent
                    default:
                        return AgentAction.DO_NOTHING;
                }
            } else if (agent == 2) {
                switch (move) {
                    case "left":
                        return AgentAction.MOVE_WEST;
                    case "right":
                        return AgentAction.MOVE_EAST;
                    case "up":
                        return AgentAction.MOVE_NORTH;
                    case "down":
                        return AgentAction.MOVE_SOUTH;
                    default:
                        return AgentAction.DO_NOTHING;
                }
            } else {    // this should never happen...
                useSimpleAgent = true;
                return simpleAgent(inEnvironment);
            }
        }
    }

    // gets agent number
    private int getAgentNumber(AgentEnvironment inEnvironment) {
        if (inEnvironment.isFlagSouth(inEnvironment.OUR_TEAM, false)) {
            return 1;
        } else if (inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, false)) {
            return 2;
        } else {
            return 3;   // something went wrong. This should NEVER happen.
        }
    }

    // finds out if agent is east or west starting location
    private Boolean isSpawnEast(AgentEnvironment inEnvironment) {
        if (inEnvironment.isObstacleEastImmediate() && !inEnvironment.isObstacleWestImmediate()) {
            return true;
        } else if (inEnvironment.isObstacleWestImmediate() && !inEnvironment.isObstacleEastImmediate()) {
            return false;
        }

        return null;
    }

    // Obstacle string defined as NESW, 0 = no obstacle, 1 = obstacle.
    private String getObstacleString(AgentEnvironment inEnvironment) {
        StringBuilder obstacleString = new StringBuilder();

        if (inEnvironment.isObstacleNorthImmediate()) {
            obstacleString.append("1");
        } else {
            obstacleString.append("0");
        }

        if (inEnvironment.isObstacleEastImmediate()) {
            obstacleString.append("1");
        } else {
            obstacleString.append("0");
        }

        if (inEnvironment.isObstacleSouthImmediate()) {
            obstacleString.append("1");
        } else {
            obstacleString.append("0");
        }

        if (inEnvironment.isObstacleWestImmediate()) {
            obstacleString.append("1");
        } else {
            obstacleString.append("0");
        }

        return obstacleString.toString();
    }

    // checks if agent has died
    private boolean isAgentDead(AgentEnvironment inEnvironment) {
        return startingObstacles.equals(getObstacleString(inEnvironment));
    }

    // setup potential maps
    private HashMap<String, Boolean[][]> setupPotentialMaps() {
        HashMap<String, Boolean[][]> result = new HashMap<String, Boolean[][]>();

        // add empty
        Boolean[][] empty = createBooleanArray(10, "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
        result.put("empty", empty);

        // add simple
        Boolean[][] simple = createBooleanArray(10, "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 1 1 0 0 0 0 1 1 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 1 1 0 0 0 0 1 1 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
        result.put("simple", simple);

        // add traps
        Boolean[][] traps = createBooleanArray(10, "0 0 1 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 1 1 1 1 1 1 0 0 0 0 1 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 1 0 0 0 0 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
        result.put("traps", traps);

        // add wall
        Boolean[][] wall = createBooleanArray(10, "0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
        result.put("wall", wall);

        // add x
        Boolean[][] x = createBooleanArray(10, "0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0");
        result.put("x", x);

        return result;
    }

    // check maps and remove impossible maps
    private void checkPotentialMaps() {
        // TODO: Implement
    }

    // create boolean array
    private Boolean[][] createBooleanArray(int split, String numbers) {
        String[] numbersArray = numbers.split("\\s+");

        if (numbersArray.length != split * split) {  // this should never happen...
            System.out.println("Error... The boolean array maps are not evenly split.");
            System.exit(1);
        }

        Boolean[][] results = new Boolean[split][split];

        for (int i = 0; i < split; i++) {
            for (int j = 0; j < split; j++) {
                results[i][j] = (numbersArray[(i * split) + j]).equals("1");
            }
        }

        return results;
    }

    // simple agent
    private int simpleAgent(AgentEnvironment inEnvironment) {
        // booleans describing direction of goal
        // goal is either enemy flag, or our base
        boolean goalNorth;
        boolean goalSouth;
        boolean goalEast;
        boolean goalWest;

        if (!inEnvironment.hasFlag()) {
            // make goal the enemy flag
            goalNorth = inEnvironment.isFlagNorth( 
                inEnvironment.ENEMY_TEAM, false );
        
            goalSouth = inEnvironment.isFlagSouth( 
                inEnvironment.ENEMY_TEAM, false );
        
            goalEast = inEnvironment.isFlagEast( 
                inEnvironment.ENEMY_TEAM, false );
        
            goalWest = inEnvironment.isFlagWest( 
                inEnvironment.ENEMY_TEAM, false );
        } else {
            // we have enemy flag, make goal our base
            goalNorth = inEnvironment.isBaseNorth( 
                inEnvironment.OUR_TEAM, false );
        
            goalSouth = inEnvironment.isBaseSouth( 
                inEnvironment.OUR_TEAM, false );
        
            goalEast = inEnvironment.isBaseEast( 
                inEnvironment.OUR_TEAM, false );
        
            goalWest = inEnvironment.isBaseWest( 
                inEnvironment.OUR_TEAM, false );
        }
        
        // check for immediate obstacles blocking our path      
        boolean obstNorth = inEnvironment.isObstacleNorthImmediate();
        boolean obstSouth = inEnvironment.isObstacleSouthImmediate();
        boolean obstEast = inEnvironment.isObstacleEastImmediate();
        boolean obstWest = inEnvironment.isObstacleWestImmediate();
        
        // if the goal is north only, and we're not blocked
        if (goalNorth && ! goalEast && ! goalWest && !obstNorth) {
            // move north
            return AgentAction.MOVE_NORTH;
        }
            
        // if goal both north and east
        if (goalNorth && goalEast) {
            // pick north or east for move with 50/50 chance
            if (Math.random() < 0.5 && !obstNorth) {
                return AgentAction.MOVE_NORTH;
            }

            if (!obstEast) {   
                return AgentAction.MOVE_EAST;
            }

            if (!obstNorth) {  
                return AgentAction.MOVE_NORTH;
            }
        }   
            
        // if goal both north and west  
        if (goalNorth && goalWest) {
            // pick north or west for move with 50/50 chance
            if (Math.random() < 0.5 && !obstNorth) {
                return AgentAction.MOVE_NORTH;
            }

            if (!obstWest) {   
                return AgentAction.MOVE_WEST;
            }

            if (!obstNorth) {  
                return AgentAction.MOVE_NORTH;
            }
        }
        
        // if the goal is south only, and we're not blocked
        if (goalSouth && ! goalEast && ! goalWest && !obstSouth) {
            // move south
            return AgentAction.MOVE_SOUTH;
        }
        
        // do same for southeast and southwest as for north versions    
        if (goalSouth && goalEast) {
            if (Math.random() < 0.5 && !obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }

            if (!obstEast) {
                return AgentAction.MOVE_EAST;
            }

            if (!obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }
        }
                
        if (goalSouth && goalWest && !obstSouth) {
            if (Math.random() < 0.5) {
                return AgentAction.MOVE_SOUTH;
            }

            if (!obstWest) {
                return AgentAction.MOVE_WEST;
            }

            if (!obstSouth) {
                return AgentAction.MOVE_SOUTH;
            }
        }
        
        // if the goal is east only, and we're not blocked
        if (goalEast && !obstEast) {
            return AgentAction.MOVE_EAST;
        }
        
        // if the goal is west only, and we're not blocked  
        if (goalWest && !obstWest) {
            return AgentAction.MOVE_WEST;
        }   
        
        // otherwise, make any unblocked move
        if (!obstNorth) {
            return AgentAction.MOVE_NORTH;
        } else if (!obstSouth) {
            return AgentAction.MOVE_SOUTH;
        } else if (!obstEast) {
            return AgentAction.MOVE_EAST;
        } else if (!obstWest) {
            return AgentAction.MOVE_WEST;
        } else {    // completely blocked!
            return AgentAction.DO_NOTHING;
        }   
    }
}
