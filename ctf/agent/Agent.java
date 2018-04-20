package ctf.agent;


import ctf.common.AgentEnvironment;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Abstract interface class that must be implemented by an agent.  
 * Forces agents to implement a function
 * that maps environment state to an action.<BR>
 * <BR>
 * Your agent class MUST subclass the <code>Agent</code>.  See
 * {@link ctf.agent.SimpleAgent} for an example of how to do this.<BR>
 * <BR>
 * The icon that is drawn to represent an agent is created in
 * <code>drawIcon</code>.  If you want to create a custom icon for your agent,
 * override the <code>drawIcon</code> method.  Since making a custom icon
 * isn't necessary, we don't provide an example agent that does this.<BR>
 * <BR>
 * Feel free to implement other internal methods in your <code>Agent</code> 
 * subclass that aren't defined in this interface.  You're also free to define
 * any needed inner classes, as well as any internal data structures.  
 * One restriction
 * is that all of your code must be in a single java file called
 * "NetIDAgent.java", where NetID is the net-id of one of the human members
 * of your team.  Thus, if you want to use more than one class (for example,
 * to implement data structures), you must make inner classes.  This is
 * due to the Java restriction that each non-inner class must be defined in
 * its own .java file.<BR>
 * <BR>
 * Note:<BR>
 * All agents must also implement a parameterless constructor!  (An easy way
 * to meet this requirement is to write no constructor at all.)
 *
 */
public abstract class Agent {
	
	private Color mFillColor = Color.gray;
	private Color mLineColor = Color.black;
	
	/**
	 * Gets the agent's next move.  Implement this method in a class that
	 * extends <code>Agent</code>.
	 *
	 * @param inEnvironment the current agent-centric environment state.
	 *
	 * @return an action constant.  Valid values are defined in
	 *   {@link ctf.common.AgentAction}.
	 */
	public abstract int getMove( AgentEnvironment inEnvironment );

	
	
	/**
	 * Draws the icon representation for this agent.  Override this method
	 * in your class that extends <code>Agent</code> if you want to draw 
	 * a custom agent icon.
	 *
	 * @param inGraphics graphics context to draw agent icon in. Icon should
	 *   be drawn in graphics in region defined by (0,0) and 
	 *   (inWide-1, inHigh-1).
	 * @param inTeamColor color associated with this team (can be ignored
	 *   if color-independent icon alone is distinguishing enough).
	 * @param inWide width of agent icon to be drawn.
	 * @param inHigh height of agent icon to be drawn.
	 */
	public void drawIcon( Graphics inGraphics, int inWide, int inHigh ) {
		
		// set team's color as graphics drawing color
		inGraphics.setColor( mFillColor );
		
		// draw a triangle of the team's color

		// x component of triangle vertices
		int xVerts[] = new int[3];
		xVerts[0] = 0;
		xVerts[1] = inWide - 1;
		xVerts[2] = inWide / 2;
		
		// y component of triangle vertices
		int yVerts[] = new int[3];
		yVerts[0] = inHigh - 1;
		yVerts[1] = inHigh - 1;
		yVerts[2] = 0;
		
		// fill the triangle
		inGraphics.fillPolygon( xVerts, yVerts, 3 );
		
		// line with black
		inGraphics.setColor( mLineColor );
		inGraphics.drawPolygon( xVerts, yVerts, 3 );
		}



	/**
	 * Sets the team color of this agent.
	 *
	 * @param inLineColor line color for drawing agent.
	 * @param inFillColor fill color for drawing agent.
	 */
	public final void setColor( Color inLineColor, Color inFillColor ) {
		mLineColor = inLineColor;
		mFillColor = inFillColor;
		}


	}
