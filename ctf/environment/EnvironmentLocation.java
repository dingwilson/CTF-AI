package ctf.environment;

/**
 * A location in the environment.
 *
 */
class EnvironmentLocation {
	public int mX, mY;
	
	public boolean equals( Object inObject ) {
		if( inObject instanceof EnvironmentLocation ) {
			EnvironmentLocation other = (EnvironmentLocation)inObject;
			if( other.mX == mX && other.mY == mY ) {
				return true;
				}
			}
		return false;
		}
	}
