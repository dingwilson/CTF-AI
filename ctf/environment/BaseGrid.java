package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Base grid for game of capture the flag.  Only one base per grid.
 * (A separate grid should be used for each team.)
 *
 * Note that this class only has package access enabled.
 *
 */
class BaseGrid extends SingletonGrid {


	/**
	 * Constructs a <code>BaseGrid</code>.
	 * 
	 * @param inBoardSize dimension of grid.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 */
	BaseGrid( int inBoardSize, Color inLineColor, Color inFillColor ) {
		super( inBoardSize, inLineColor, inFillColor );
		}
	
		
		
	// implements the Grid.drawObject method
	protected void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh ) {
				
		inGraphics.setColor( mFillColor );
		
		// draw an X in team color lined with black
		
		int endX = inStartX + inWide - 1;
		int endY = inStartY + inHigh - 1;
		
		int lineWidthX = inWide / 4;
		int lineWidthY = inHigh / 4;
		
		// first, team color X
		int rightVerts[][] = new int[2][4];
		rightVerts[0][0] = inStartX;
		rightVerts[1][0] = inStartY + lineWidthY;
		rightVerts[0][1] = endX - lineWidthX;
		rightVerts[1][1] = endY;
		rightVerts[0][2] = endX;
		rightVerts[1][2] = endY - lineWidthY;
		rightVerts[0][3] = inStartX + lineWidthX;
		rightVerts[1][3] = inStartY;		 
		int leftVerts[][] = new int[2][4];
		leftVerts[0][0] = endX - lineWidthX;
		leftVerts[1][0] = inStartY;
		leftVerts[0][1] = inStartX;
		leftVerts[1][1] = endY - lineWidthY;
		leftVerts[0][2] = inStartX + lineWidthX;
		leftVerts[1][2] = endY;
		leftVerts[0][3] = endX;
		leftVerts[1][3] = inStartY + lineWidthY;
		
		inGraphics.fillPolygon( rightVerts[0], rightVerts[1], 4 );
		inGraphics.fillPolygon( leftVerts[0], leftVerts[1], 4 );
		
		// now draw outline
		inGraphics.setColor( mLineColor );
		
		inGraphics.drawPolygon( rightVerts[0], rightVerts[1], 4 );
		inGraphics.drawPolygon( leftVerts[0], leftVerts[1], 4 );
		}
		
	}
