package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;
import java.util.*;

public class wxd130130alz140030Agent extends Agent {
    private int agent = 0;  // 0 = not found, 1 = north agent, 2 = south agent
    
    private boolean simpmap = false;
    private boolean empmap = false;
    private boolean hasdied = false;

    private boolean hasFoundMap = false;

    private String startingObstacles = "2222";  // 0 = no obstacle , 1 = obstacle. NESW

    private static boolean initializing = true;    // searching for map type

    private Boolean isSpawnEast;   // null = not found, true = east spawn, false = west spawn

    private Integer agentXLoc;  // current agent X location
    private Integer agentYLoc;  // current agent Y location

    private static HashMap<String, Boolean[][]> potentialMaps;  // hashmap of potential maps

    private boolean useSimpleAgent = false;  // use simple agent (if all else fails...)

    // hardcoded check/start path for Agent 1
    private final String[] startEast = new String[]{"down", "left", "left", "left", "nothing"};
    private final String[] startWest = new String[]{"down", "right", "right", "right", "nothing"};
    private final String[] sendmove = new String[] {"right", "right", "down", "down", "down", "down", "mine", "left", "left", "left", "left", "left", "left"};
    private final String[] sedmove = new String[] {"left", "down", "down", "down", "down", "down", "mine", "left","left", "left", "left", "left", "left", "left", "left"};
    private final String[] swndmove = new String[]{"left","left", "down", "down", "down", "down", "mine", "right", "right", "right","right","right","right","right","right" };
    private final String[] swdmove = new String[] {"right", "down", "down", "down", "down", "down", "mine", "right", "right", "right","right","right","right","right","right" };
    
    // hardcoded paths for simple and empty (es) maps
    private final String[] esEast = new String[]{"left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "up", "up", "right", "right", "right", "right", "right", "right", "right", "right", "right", "down", "nothing"};
    private final String[] esWest = new String[]{"right", "right", "right", "right", "right", "right", "right", "right", "right", "up", "up", "up", "up", "up", "left", "left", "left", "left", "left", "left", "left", "left", "left", "down", "nothing"};

    // hardcoded paths for x, wall, traps (xwt) maps
    private final String[] xwtEast = new String[]{"left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "up", "down", "down", "down", "down", "right", "right", "right", "right", "right", "right", "right", "right", "right", "up", "up", "up", "up", "nothing"};
    private final String[] xwtWest = new String[]{"right", "right", "right", "right", "right", "right", "right", "right", "right", "up", "up", "up", "up", "down", "down", "down", "down", "left", "left", "left", "left", "left", "left", "left", "left", "left", "up", "up", "up", "up", "nothing"};

    private static String[] path;  // chosen path
    private static String [] move2 = new String[30];

    private int clock = 0;  // move clock
    private int clock2 = 0;

    public wxd130130alz140030Agent() {
        initializing = true;
        potentialMaps = setupPotentialMaps();
        path = new String[0];
        move2 = new String[0];
    }

    public int getMove(AgentEnvironment inEnvironment) {
        if (useSimpleAgent) {   // if simple agent...
            return simpleAgent(inEnvironment);
        }

        if (initializing) {
            if (isSpawnEast == null) {  // get spawn location (if possible)
                isSpawnEast = isSpawnEast(inEnvironment);

                if (isSpawnEast == null) {
                    useSimpleAgent = true;
                    return simpleAgent(inEnvironment);
                }
            }

            if (agent == 0) {   // get agent number if not set
                potentialMaps = setupPotentialMaps();
                startingObstacles = getObstacleString(inEnvironment);

                agent = getAgentNumber(inEnvironment);

                agentDidDie();
            }

            checkPotentialMaps(inEnvironment);

            if (agent == 1) {   // agent = 1 = north agent
                System.out.println(clock);
                System.out.println(initializing);

                if (!hasFoundMap) {
                    // TESTING FOR X
                    if ((clock == 1 && isSpawnEast && inEnvironment.isObstacleWestImmediate()) || (clock == 1 && !isSpawnEast && inEnvironment.isObstacleEastImmediate())) {
                        if (isSpawnEast) {
                            path = Arrays.copyOf(xwtEast, xwtEast.length);
                        } else {
                            path = Arrays.copyOf(xwtWest, xwtWest.length);
                        }

                        hasFoundMap = true;
                    }

                    // TESTING FOR TRAPS
                    if ((clock == 2 && isSpawnEast && inEnvironment.isObstacleWestImmediate()) || (clock == 2 && !isSpawnEast && inEnvironment.isObstacleEastImmediate())) {
                        if (isSpawnEast) {
                            path = Arrays.copyOf(xwtEast, xwtEast.length);
                        } else {
                            path = Arrays.copyOf(xwtWest, xwtWest.length);
                        }

                        hasFoundMap = true;
                    }

                    // TESTING FOR SIMPLE
                    // TODO: Loses to SimpleAgent
                    if (clock == 3 && inEnvironment.isObstacleSouthImmediate()) {

                        if (isSpawnEast) {
                            path = Arrays.copyOf(esEast, esEast.length);
                        } else {
                            path = Arrays.copyOf(esWest, esWest.length);
                        }
                        simpmap=true;

                        hasFoundMap = true;
                    }

                    if (clock == 4) {
                        // TESTING FOR WALL
                        if ((isSpawnEast && inEnvironment.isObstacleWestImmediate()) || !isSpawnEast && inEnvironment.isObstacleEastImmediate()) {
                            if (isSpawnEast) {
                                path = Arrays.copyOf(xwtEast, xwtEast.length);
                            } else {
                                path = Arrays.copyOf(xwtWest, xwtWest.length);
                            }
                        } else {    // OTHERWISE EMPTY
                            // TODO: Loses to SimpleAgent
                            if (isSpawnEast) {
                                path = Arrays.copyOf(esEast, esEast.length);
                            } else {
                                path = Arrays.copyOf(esWest, esWest.length);
                            }
                            empmap=true;
                        }

                        hasFoundMap = true;
                    }
                }

                if (clock == 4) {
                    initializing = false;

                    clock = 0;
                    return AgentAction.DO_NOTHING;
                } else {
                    if (clock >= startEast.length) {    // startEast.length == startWest.length
                        useSimpleAgent = true;
                        return simpleAgent(inEnvironment);
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
                            return moveWest(inEnvironment);
                        case "right":
                            return moveEast(inEnvironment);
                        case "up":
                            return moveNorth(inEnvironment);
                        case "down":
                            return moveSouth(inEnvironment);
                        default:
                            return AgentAction.DO_NOTHING;
                    }
                }
            } else if (agent == 2) {    // agent = 2 = south agent
                if (isSpawnEast != null) {
                    clock++;

                    if (isSpawnEast) {
                        return moveWest(inEnvironment);
                    } else {
                        return moveEast(inEnvironment);
                    }
                } else {
                    return AgentAction.DO_NOTHING;
                }
            } else {    // this should never happen...
                useSimpleAgent = true;
                return simpleAgent(inEnvironment);
            }
        } else {
            if (isAgentDead(inEnvironment)) {
                hasdied=true;
                agentDidDie();
            }

            checkPotentialMaps(inEnvironment);

            if (clock >= path.length) {
                useSimpleAgent = true;
                return simpleAgent(inEnvironment);
            }
            //marker
            String move = path[clock];
            clock++;
            if (agent == 1)
            {

                if (!hasdied&&isSpawnEast)
                {
                    move2 = Arrays.copyOf(sendmove, sendmove.length);
                    System.out.println("This array");
                }
                if (hasdied&&isSpawnEast)
                {
                    move2 = Arrays.copyOf(sedmove, sedmove.length);
                }
                if (!hasdied&&!isSpawnEast)
                {
                    move2 = Arrays.copyOf(swndmove, swndmove.length);

                }
                if (hasdied&&!isSpawnEast)
                {
                    move2 = Arrays.copyOf(swdmove, swdmove.length);
                }
                
                if (clock2 >= move2.length) {
                    return AgentAction.DO_NOTHING;
                }

                switch (move2[clock2]) {
                case "left":
                    clock2++;
                    return AgentAction.MOVE_WEST;
                case "right":
                    clock2++;
                    return AgentAction.MOVE_EAST;
                case "up":
                    clock2++;
                    return AgentAction.MOVE_NORTH;
                case "down":
                    clock2++;
                    return AgentAction.MOVE_SOUTH;
                case "mine":
                    clock2++;
                    return AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;
                    default:
                        clock2++;
                        return AgentAction.DO_NOTHING;
                }


            } else if (agent == 2) {
                switch (move) {
                    case "left":
                        return moveWest(inEnvironment);
                    case "right":
                        return moveEast(inEnvironment);
                    case "up":
                        return moveNorth(inEnvironment);
                    case "down":
                        return moveSouth(inEnvironment);
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

        useSimpleAgent = true;  // for all given maps, you should always be able to tell isSpawnEast on first try.
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
        if (inEnvironment.hasFlag(inEnvironment.OUR_TEAM)) {
            return false;
        }

        return startingObstacles.equals(getObstacleString(inEnvironment));
    }

    // reset things when agent dies
    private void agentDidDie() {
        clock = 0;
        clock2 = 0;
        
        if (isSpawnEast) {
            agentXLoc = 9;
        } else {
            agentXLoc = 0;
        }

        if (agent == 1) {
            agentYLoc = 0;
        } else if (agent == 2) {
            agentYLoc = 9;
        }
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
    private void checkPotentialMaps(AgentEnvironment inEnvironment) {
        if (agentXLoc != null && agentYLoc != null) {
            String currentObstacles = getObstacleString(inEnvironment);

            Iterator mapIterator = potentialMaps.entrySet().iterator();

            while (mapIterator.hasNext()) {
                Map.Entry<String, Boolean[][]> pair = (Map.Entry<String, Boolean[][]>)mapIterator.next();

                Boolean[][] currentPossibleMap = pair.getValue();

                StringBuilder mapObstacles = new StringBuilder();

                if (agentYLoc == 0) {
                    mapObstacles.append("1");
                } else if (currentPossibleMap[agentXLoc][agentYLoc - 1]) {
                    mapObstacles.append("1");
                } else {
                    mapObstacles.append("0");
                }

                if (agentXLoc == 9) {
                    mapObstacles.append("1");
                } else if (currentPossibleMap[agentXLoc + 1][agentYLoc]) {
                    mapObstacles.append("1");
                } else {
                    mapObstacles.append("0");
                }

                if (agentYLoc == 9) {
                    mapObstacles.append("1");
                } else if (currentPossibleMap[agentXLoc][agentYLoc + 1]) {
                    mapObstacles.append("1");
                } else {
                    mapObstacles.append("0");
                }

                if (agentXLoc == 0) {
                    mapObstacles.append("1");
                } else if (currentPossibleMap[agentXLoc - 1][agentYLoc]) {
                    mapObstacles.append("1");
                } else {
                    mapObstacles.append("0");
                }

                if (!currentObstacles.equals(mapObstacles.toString())) {
                    System.out.println("Agent " + agent + " removed map: " + pair.getKey());
                    mapIterator.remove();
                }
            }

            // TODO: explore incorrectly removed maps before using this
            // if (potentialMaps.isEmpty()) {
            //     useSimpleAgent = true;
            //     return simpleAgent(inEnvironment);
            // }
        }
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

    // move north
    private int moveNorth(AgentEnvironment inEnvironment) {
        if (!inEnvironment.isObstacleNorthImmediate()) {
            agentYLoc--;
        }

        return AgentAction.MOVE_NORTH;
    }

    // move east
    private int moveEast(AgentEnvironment inEnvironment) {
        if (!inEnvironment.isObstacleEastImmediate()) {
            agentXLoc++;
        }

        return AgentAction.MOVE_EAST;
    }

    // move sourth
    private int moveSouth(AgentEnvironment inEnvironment) {
        if (!inEnvironment.isObstacleSouthImmediate()) {
            agentYLoc++;
        }

        return AgentAction.MOVE_SOUTH;
    }

    // move west
    private int moveWest(AgentEnvironment inEnvironment) {
        if (!inEnvironment.isObstacleWestImmediate()) {
            agentXLoc--;
        }

        return AgentAction.MOVE_WEST;
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
                    inEnvironment.ENEMY_TEAM, false);

            goalSouth = inEnvironment.isFlagSouth(
                    inEnvironment.ENEMY_TEAM, false);

            goalEast = inEnvironment.isFlagEast(
                    inEnvironment.ENEMY_TEAM, false);

            goalWest = inEnvironment.isFlagWest(
                    inEnvironment.ENEMY_TEAM, false);
        } else {
            // we have enemy flag, make goal our base
            goalNorth = inEnvironment.isBaseNorth(
                    inEnvironment.OUR_TEAM, false);

            goalSouth = inEnvironment.isBaseSouth(
                    inEnvironment.OUR_TEAM, false);

            goalEast = inEnvironment.isBaseEast(
                    inEnvironment.OUR_TEAM, false);

            goalWest = inEnvironment.isBaseWest(
                    inEnvironment.OUR_TEAM, false);
        }

        // check for immediate obstacles blocking our path
        boolean obstNorth = inEnvironment.isObstacleNorthImmediate();
        boolean obstSouth = inEnvironment.isObstacleSouthImmediate();
        boolean obstEast = inEnvironment.isObstacleEastImmediate();
        boolean obstWest = inEnvironment.isObstacleWestImmediate();

        // if the goal is north only, and we're not blocked
        if (goalNorth && !goalEast && !goalWest && !obstNorth) {
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
        if (goalSouth && !goalEast && !goalWest && !obstSouth) {
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