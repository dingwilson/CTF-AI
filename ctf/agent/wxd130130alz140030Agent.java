package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;

import ctf.common.AgentAction;

public class wxd130130alz140030Agent extends Agent 
{   
    boolean firstmove=true;
    int northagent=true; // 1 designates north agent, 2 designates southagent
    static boolean waiting=false;
    
    
    public static Tile[][] map;    // map of environment, initialize after hitting flag
    
    int agentLocationX;
    int agentLocationY;
    
    public int getMove(AgentEnvironment inEnvironment) {
        if(firstmove)
        {        
             if (inEnvironment.isFlagNorth(OUR_TEAM, true)&&!inEnvironment.isObstacleNorthImmediate())           
             {
                 northagent=2;      
                 return AgentAction.MOVE_NORTH;  
             }
             
             if (inEnvironment.isFlagSouth(OUR_TEAM, true)&&!inEnvironment.isObstacleSouthImmediate()) 
             {
                 northagent=1;
                 return AgentAction.MOVE_SOUTH;
             }
             
             if (inEnvironment.isObstacleSouthImmediate())
             {
                 if (!waiting)
                 {
                     waiting=true;
                     return AgentAction.DO_NOTHING;
                 }
                  if (waiting)
                {
                     waiting=false;
                     return AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;         
                }       
                 
             }
         
             if (inEnvironment.isObstacleNorthImmediate())
             {
             if (!waiting)
                 {
                     waiting=true;
                     return AgentAction.DO_NOTHING;
                 }
                if (waiting)
                {
                     waiting=false;
                     return AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;         
                }                        
             }    
        } 
        
        else {
            getEnvironmentInfo(inEnvironment);
        
            heuristic1();
            ....
            heuristicn();
        
            makeMove();
        }
    }

    private Tile getEnvironmentInfo(AgentEnvironment inEnvironment, boolean firstmove) {
        if (firstmove) {
            return null;
        }
        
        
    }

    public class Tile {
        // public boolean goalNorth;    // is goal north?
        // public boolean goalSouth;    // is goal south?
        // public boolean goalEast;    // is goal east?
        // public boolean goalWest;    // is goal west?
        
        public boolean isObstacle;    // is tile obstacle?
        public boolean isBase;    // is tile a base?
        
        public Tile() {
            
        }
    }
}
