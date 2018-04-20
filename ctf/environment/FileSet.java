package ctf.environment;

import java.io.*;

/**
 * Board and obstacle settings read from a file.
 *
 * Note that this class only has package access enabled.
 *
 */
class FileSet implements ObstacleSet, BoardSet {
	
	private static float mObstacleRatio = 0.25f;
	private int mBoardSize;
	private EnvironmentLocation mLocations[];
	
	private int mGrid[][];
	
	/**
	 * Constructs a new FileSet.
	 *
	 * @param inFile the file to read board set from.
	 *
	 * @throws IOException if any of the io operations involved fail.
	 */
	FileSet( File inFile ) throws IOException {
		
		StreamTokenizer tokenizer = 
			new StreamTokenizer( new FileInputStream( inFile ) );
		tokenizer.eolIsSignificant( false );
		
		tokenizer.nextToken();
		mBoardSize = (int)tokenizer.nval;
		
		mGrid = new int[mBoardSize][mBoardSize];
		
		int numLocations = 0;
		
		for( int y=0; y<mBoardSize; y++ ) {
			for( int x=0; x<mBoardSize; x++ ) {
				tokenizer.nextToken();
				if( tokenizer.nval == 1 ) {
					numLocations++;
					mGrid[y][x] = 1;
					}
				else {
					mGrid[y][x] = 0;
					}
				}
			}
		
		mLocations = new EnvironmentLocation[numLocations];
		int locationInd = 0;
		for( int y=0; y<mBoardSize; y++ ) {
			for( int x=0; x<mBoardSize; x++ ) {
				if( mGrid[y][x] == 1 ) {
					mLocations[locationInd] = new EnvironmentLocation();
					mLocations[locationInd].mX = x;
					mLocations[locationInd].mY = y;
					locationInd++;
					}
				}
			}
		}
	
	
	// implements ObstacleSet.getObstacleLocations()
	public EnvironmentLocation[] getObstacleLocations( int inBoardSize ) {
		return mLocations;
		}
		
	// implements BoardSet.getBoardSize()	
	public int getBoardSize() {
		return mBoardSize;
		}
	}
