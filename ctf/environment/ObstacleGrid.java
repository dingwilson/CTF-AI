package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Obstacle grid for game of capture the flag.
 * Independent of agents.
 *
 * Note that this class only has package access enabled.
 *
 */
class ObstacleGrid extends Grid {

	/**
	 * Constructs an <code>ObstacleGrid</code>.
	 * 
	 * @param inBoardSize dimension of grid.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 */
	ObstacleGrid( int inBoardSize, Color inLineColor, Color inFillColor ) {
		super( inBoardSize, inLineColor, inFillColor );
		}
		
	// implements the Grid.drawObject method
	protected void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh ) {
				
		inGraphics.setColor( mFillColor );

		inGraphics.fillRect( inStartX, inStartY,
			inWide, inHigh );
		
		inGraphics.setColor( mLineColor );

		inGraphics.drawRect( inStartX, inStartY,
			inWide, inHigh );	
		}
		
	}
