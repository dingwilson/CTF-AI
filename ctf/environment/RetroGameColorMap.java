package ctf.environment;

import java.awt.Color;

/**
 * Vector green color map for ctf game.
 *
 */
class RetroGameColorMap extends GameColorMap {
	
	String mName = "Retro";
	
	/**
	 * Constructs a <code>RetroGameColorMap</code>.
	 */
	RetroGameColorMap() {
		mTeamColors = new Color[2];
	
		mLineColor = new Color( 0.0f, 1.0f, 0.0f );
		
		mFillColor = new Color( 0.0f, 0.4f, 0.0f );
		
		mBackgroundColor = Color.black;
		
		mTeamColors[0] = mBackgroundColor;
		mTeamColors[1] = mFillColor;
		
	
		mMineButtonColor = mBackgroundColor;
		mFlagPoleColor = mBackgroundColor;
	
		mObstacleColor = mFillColor;
		}
	}
