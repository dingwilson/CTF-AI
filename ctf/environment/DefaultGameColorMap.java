package ctf.environment;

import java.awt.Color;

/**
 * Standard color map for ctf game.
 *
 */
class DefaultGameColorMap extends GameColorMap {
	
	String mName = "Default";
	
	/**
	 * Constructs a <code>DefaultGameColorMap</code>.
	 */
	DefaultGameColorMap() {
		mTeamColors = new Color[2];
	
		mLineColor = Color.black;
		
		// fill color not actually used...
		mFillColor = new Color( 0.5f, 0.5f, 0.5f );
		
		mBackgroundColor = Color.white;
		
		mTeamColors[0] = Color.red;
		mTeamColors[1] = Color.green;
		
	
		mMineButtonColor = new Color( 1.0f, 0.5f, 0.0f );
		mFlagPoleColor = new Color( 0.75f, 0.75f, 0.75f );;
	
		mObstacleColor = Color.black;
		}
	}
