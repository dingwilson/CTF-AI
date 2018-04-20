package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Mine grid for game of capture the flag.
 * Independent of agents.
 *
 * Note that this class only has package access enabled.
 *
 */
class MineGrid extends Grid {
	
	private static Color mMineButtonColor = new Color( 1.0f, .5f, 0.0f );

	/**
	 * Constructs a <code>MineGrid</code>.
	 * 
	 * @param inBoardSize dimension of grid.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 * @param inMineButtonColor fill color for the mine button.
	 */
	MineGrid( int inBoardSize, Color inLineColor, Color inFillColor, 
		Color inMineButtonColor ) {
		super( inBoardSize, inLineColor, inFillColor );
		mMineButtonColor = inMineButtonColor;
		}
		
		
		
	/**
	 * Set fill color for drawing mine button.
	 *
	 * @param inMineButtonColor fill color for drawing mine button.
	 */
	public void setMineButtonColor( Color inMineButtonColor ) {
		mMineButtonColor = inMineButtonColor;
		}
		
		
			
	// implements the Grid.drawObject method
	protected void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh ) {
				

		//inGraphics.fillOval( inStartX, inStartY,
		//	inWide, inHigh );
		
		int endX = inStartX + inWide - 1;
		int endY = inStartY + inHigh - 1;
		
		int edgeOffsetX = inWide / 4;
		int edgeOffsetY = inHigh / 4;
		
		
		// draw a diamond
		int xVerts[] = new int[4];
		xVerts[0] = inStartX + ( inWide / 2 ) - ( 2 * edgeOffsetX );
		xVerts[1] = inStartX + ( inWide / 2 );
		xVerts[2] = inStartX + ( inWide / 2 ) + ( 2 * edgeOffsetX );
		xVerts[3] = inStartX + ( inWide / 2 );
		
		int yVerts[] = new int[4];
		yVerts[0] = inStartY + ( inHigh / 2 );
		yVerts[1] = inStartY + ( inHigh / 2 ) - ( 2 * edgeOffsetY );
		yVerts[2] = inStartY + ( inHigh / 2 );
		yVerts[3] = inStartY + ( inHigh / 2 ) + ( 2 * edgeOffsetY );
		
		inGraphics.setColor( mFillColor );
		inGraphics.fillPolygon( xVerts, yVerts, 4 );
		inGraphics.setColor( mLineColor );
		inGraphics.drawPolygon( xVerts, yVerts, 4 );
		
		// draw mine button
		inGraphics.setColor( mMineButtonColor );
		inGraphics.fillRect( inStartX + ( inWide / 2 ) - edgeOffsetX, 
							inStartY + ( inWide / 2 ) - edgeOffsetY, 
							2 * edgeOffsetX, 
							2 * edgeOffsetY );
		inGraphics.setColor( mLineColor );
		inGraphics.drawRect( inStartX + ( inWide / 2 ) - edgeOffsetX, 
							inStartY + ( inWide / 2 ) - edgeOffsetY, 
							2 * edgeOffsetX, 
							2 * edgeOffsetY );
		}
		
	}
