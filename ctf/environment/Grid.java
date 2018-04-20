package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Generic grid for a capture the flag board.  Each location on board
 * can either contain an item or not, but the item type isn't specified.
 *
 * Designed to be extended by more specific grids.
 *
 * Note that this class only has package access enabled.
 *
 */
abstract class Grid {
	
	private int mBoardSize;		// board width and height
	
	// board object existence map stored in y-major order
	private boolean mObjectMap[][];
	
	protected Color mLineColor = Color.black;
	protected Color mFillColor = Color.gray;
	
	
	
	/**
	 * Constructs a <code>Grid</code>.
	 *
	 * @param inBoardSize the board width and height, in number of grid spaces.
	 */
	protected Grid( int inBoardSize ) {
		mBoardSize = inBoardSize;
		mObjectMap = new boolean[mBoardSize][mBoardSize];
		}
	
	
	
	/**
	 * Constructs a <code>Grid</code>.
	 *
	 * @param inBoardSize the board width and height, in number of grid spaces.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 */
	protected Grid( int inBoardSize, Color inLineColor, Color inFillColor ) {
		this( inBoardSize );
		mLineColor = inLineColor;
		mFillColor = inFillColor;
		}
	
	
	
	/**
	 * Adds an object to the grid.
	 *
	 * @param inX the x component of the object's location.
	 * @param inY the y component of the object's location.
	 */
	void add( int inX, int inY ) {
		mObjectMap[inY][inX] = true;
		}
	
	
	
	/**
	 * Adds an object to the grid.
	 *
	 * @param inLocation location to add object in.
	 */
	void add( EnvironmentLocation inLocation ) {
		mObjectMap[inLocation.mY][inLocation.mX] = true;
		}
	
	
	
	/**
	 * Removes an object to the grid.
	 *
	 * @param inX the x component of the object's location.
	 * @param inY the y component of the object's location.
	 */
	void remove( int inX, int inY ) {
		mObjectMap[inY][inX] = false;
		}
	
	
	
	/**
	 * Removes an object from the grid.
	 *
	 * @param inLocation location to remove object from.
	 */
	void remove( EnvironmentLocation inLocation ) {
		mObjectMap[inLocation.mY][inLocation.mX] = false;
		}
	
	
	
	/**
	 * Clears all objects from the grid.
	 */
	void clear() {
		for( int y=0; y<mBoardSize; y++ ) {
			for( int x=0; x<mBoardSize; x++ ) {
				mObjectMap[y][x] = false;
				}
			}
		}
	
	
	/**
	 * Checks whether an object exists in a particular grid location.
	 *
	 * @param inX the x component of the location to check.
	 * @param inY the y component of the location to check.
	 *
	 * @return true iff an object exists at <code>inX</code> and
	 *   <code>inY</code>.
	 */
	boolean exists( int inX, int inY ) {
		return mObjectMap[inY][inX];
		}                                                          
	
	
	/**
	 * Checks whether an object exists in a particular grid location.
	 *
	 * @param inLocation location to check.
	 *
	 * @return true iff an object exists at <code>inLocation</code>.
	 */
	boolean exists( EnvironmentLocation inLocation ) {
		return mObjectMap[inLocation.mY][inLocation.mX];
		}   
	
	
	
	/**
	 * Set fill color for drawing grid objects.
	 *
	 * @param inFillColor fill color for drawing objects in this grid.
	 */
	public void setFillColor( Color inFillColor ) {
		mFillColor = inFillColor;
		}
		
		
		
	/**
	 * Set line color for drawing grid objects.
	 *
	 * @param inLineColor line color for drawing objects in this grid.
	 */
	public void setLineColor( Color inLineColor ) {
		mLineColor = inLineColor;
		}	
	
	
	
	/**
	 * Draws the grid and all objects in it.
	 *
	 * @param inGraphics the graphics context in which to draw the grid.
	 * @param inStartX the starting x coordinate in <code>inGraphics</code>
	 *   for the grid.
	 * @param inStartY the starting y coordinate in <code>inGraphics</code>
	 *   for the grid. 
	 * @param inWide the width of the grid image.
	 * @param inHigh the height of the grid image.
	 */
	void draw( Graphics inGraphics, int inStartX, int inStartY,
		int inWide, int inHigh ) {
		
		int widthPerSquare = inWide / mBoardSize;
		int heightPerSquare = inHigh / mBoardSize;
		
		for( int y=0; y<mBoardSize; y++ ) {
			for( int x=0; x<mBoardSize; x++ ) {
				if( mObjectMap[y][x] ) {
					drawObject( inGraphics,
					x * widthPerSquare + inStartX, 
					y * heightPerSquare + inStartY,
					widthPerSquare, heightPerSquare );
					} 	
				}
			}
		}
	
	/**
	 * Gets the actual dimensions of the drawn grid when
	 * particular dimensions are requested.
	 * Because of the requirement that each grid space be the same
	 * screen size, a grid drawing can be smaller than requested.
	 *
	 * @param inSuggestedWide width to be requested for draw.
	 * @param inSuggestedHigh height to be requested for draw.
	 *
	 * @return the actual dimensions of the drawn grid.
	 */
	public Dimension getDrawDimension( 
		int inSuggestedWide, int inSuggestedHigh ) {
		Dimension returnDimension = new Dimension();
		
		// round down to nearest whole number of pixels per grid space
		returnDimension.width = mBoardSize * ( inSuggestedWide / mBoardSize );
		returnDimension.height = mBoardSize * ( inSuggestedHigh / mBoardSize );
		
		return returnDimension;
		}
	
	
	/**
	 * Draws a graphical representation of an object in a specific location
	 * in a graphics context.  To be implemented by all non-abstract subclasses
	 * for drawing graphical representations of particular kinds of grid
	 * objects.
	 *
	 * @param inGraphics the graphics context in which to draw the object.
	 * @param inStartX the starting x coordinate in <code>inGraphics</code>
	 *   for the object.
	 * @param inStartY the starting y coordinate in <code>inGraphics</code>
	 *   for the object. 
	 * @param inWide the width of the object image.
	 * @param inHigh the height of the object image.
	 */
	protected abstract void drawObject( 
		Graphics inGraphics, 
		int inStartX, int inStartY,
		int inWide, int inHigh );
		
	}
