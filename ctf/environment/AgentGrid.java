package ctf.environment;

import ctf.agent.Agent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

/**
 * Grid of agent locations for a particular team
 *
 * Note that this class only has package access enabled.
 *
 */
class AgentGrid extends Grid {
	
	// Instance of the agent class to be represented by this grid.
	// This instance is called to draw the image for all agents on the grid.
	private Agent mAgent; 

	/**
	 * Constructs an <code>AgentGrid</code>.
	 *
	 * @param inBoardSize the board width and height, in number of grid spaces.
	 * @param inAgentImage image to represent agents on grid when grid is drawn.
	 */
	AgentGrid( int inBoardSize, Agent inAgent ) {
		super( inBoardSize );
		mAgent = inAgent;
		}
		
	// implements the Grid.drawObject method
	protected void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh ) {

		Graphics agentGraphics = 
			inGraphics.create( inStartX, inStartY, inWide, inHigh );
		
		mAgent.drawIcon( agentGraphics, inWide, inHigh );
		}
		
	}
