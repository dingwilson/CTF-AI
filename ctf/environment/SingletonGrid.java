package ctf.environment;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Grid that supports exactly one existing object.
 *
 * Note that this class only has package access enabled.
 *
 */
abstract class SingletonGrid extends Grid {

	// object location
	// only one object per grid 
	private int mX = 0;
	private int mY = 0;



	/**
	 * Constructs a <code>SingletonGrid</code>.
	 * 
	 * @param inBoardSize dimension of grid.
	 * @param inLineColor line color for drawing objects in this grid.
	 * @param inFillColor fill color for drawing objects in this grid.
	 */
	protected SingletonGrid( int inBoardSize, Color inLineColor, 
		Color inFillColor ) {
		super( inBoardSize, inLineColor, inFillColor );
		}
	
	
	
	/**
	 * Set object location.
	 *
	 * @param inX x coordinate of object location.
	 * @param inY y coordinate of object location.
	 */
	void setLocation( int inX, int inY ) {
		remove( mX, mY );
		mX = inX;
		mY = inY;
		add( mX, mY );
		}
	
	/**
	 * Set object location.
	 *
	 * @param inLocation new location of object.
	 */
	void setLocation( EnvironmentLocation inLocation ) {
		remove( mX, mY );
		mX = inLocation.mX;
		mY = inLocation.mY;
		add( mX, mY );
		}
	
	
	
	/**
	 * Get object location.
	 *
	 * @return location of base in grid.
	 */
	EnvironmentLocation getLocation() {
		EnvironmentLocation returnLocation = new EnvironmentLocation();
		returnLocation.mX = mX;
		returnLocation.mY = mY;
		return returnLocation;
		}
		
	}
