package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Flag grid for game of capture the flag.  Only one flag per grid.
 * (A separate grid should be used for each team.)
 *
 * Note that this class only has package access enabled.
 *
 */
class FlagGrid extends SingletonGrid {

	private Color mFlagClothColor;

	/**
	 * Constructs a <code>FlagGrid</code>.
	 * 
	 * @param inBoardSize dimension of grid.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 * @param inFlagClothColor fill color for the cloth part of the flag.
	 */
	FlagGrid( int inBoardSize, Color inLineColor, Color inFillColor,
		Color inFlagClothColor ) {
		super( inBoardSize, inLineColor, inFillColor );
		mFlagClothColor = inFlagClothColor;
		}
		
	
	
	/**
	 * Set fill color for drawing flag cloth.
	 *
	 * @param inFillColor fill color for flag cloth.
	 */
	public void setFlagClothColor( Color inFlagClothColor ) {
		mFlagClothColor = inFlagClothColor;
		}	
		
	
		
	// implements the Grid.drawObject method
	protected void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh ) {
				
		inGraphics.setColor( mFillColor );
		
		// flag pole
		inGraphics.fillRect( inStartX, inStartY, inWide / 4,
			inHigh );
		
		// flag cloth triangle
		int[] clothX = new int[3];
		clothX[0] = inStartX + inWide / 4;
		clothX[1] = inStartX + inWide / 4;
		clothX[2] = inStartX + inWide;
		
		int[] clothY = new int[3];
		clothY[0] = inStartY + inWide / 2;
		clothY[1] = inStartY;
		clothY[2] = inStartY;
		
		inGraphics.setColor( mFlagClothColor );
		inGraphics.fillPolygon( clothX, clothY, 3 );
		
		// outline
		inGraphics.setColor( mLineColor );
		inGraphics.drawRect( inStartX, inStartY, inWide / 4,
			inHigh );
		inGraphics.drawPolygon( clothX, clothY, 3 );	
		}
		
	}
