package ctf.environment;

import ctf.agent.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;

import java.awt.Image;

import java.io.*;

/**
 * Frame for running agent CTF games w/visualization and GUI controls.
 *
 */
public class TestPlaySurface extends Frame implements StateChangeListener {
	
	Button mPauseButton = new Button( "Un-Pause" );
	Label mColorChoiceLabel = new Label( "Color map:" );
	Choice mColorChoice = new Choice(); 
	
	String mSetsDirName = "sets";
	Label mObstacleChoiceLabel = new Label( "Board set:" );
	Choice mObstacleChoice = new Choice();
	Label mTeam1ChoiceLabel = new Label( "Team 1:" );
	Choice mTeam1Choice = new Choice();
	Label mTeam2ChoiceLabel = new Label( "Team 2:" );
	Choice mTeam2Choice = new Choice();
	
	Label mNumAgentsChoiceLabel = new Label( "Agents Per Team:" );
	Choice mNumAgentsChoice = new Choice();
	
	Label mDelayChoiceLabel = new Label( "Delay:" );
	Choice mDelayChoice = new Choice();
	
	int mTeam1Score = 0;
	int mTeam2Score = 0; 
	
	Label mScoreLabel = new Label( "Score:" );
	Label mTeam1ScoreLabel = new Label( "Team1: " + mTeam1Score );
	Label mTeam2ScoreLabel = new Label( "Team2: " + mTeam2Score );
	
	Button mQuitButton = new Button( "Quit" );
	
	int mDrawThreshold = 100000;
	
	Game mGame;
	BoardSet mBoardSet;
	ObstacleSet mObstacleSet;
	
	Class mAvailableClasses[];
	
	Class mTeamClasses[];
	GameColorMap mColorMap = new DefaultGameColorMap();
	
	int mNumAgentsPerTeam = 2;
	
	
	boolean mReadyToPaint;
	
	public static void main( String [] argv ) {
		new TestPlaySurface();
		}
	
	public TestPlaySurface() {
		super( "Test play surface" );
		
		Frame controlFrame = new Frame();
		
		
		setSize( 500, 500 );
		
		controlFrame.setSize( 250, 400 );
		controlFrame.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		
		Panel pausePanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		pausePanel.add( mPauseButton );
		
		Panel colorPanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		colorPanel.add( mColorChoiceLabel );
		colorPanel.add( mColorChoice );
		
		Panel obstPanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		obstPanel.add( mObstacleChoiceLabel );
		obstPanel.add( mObstacleChoice );
		
		Panel team1Panel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		team1Panel.add( mTeam1ChoiceLabel );
		team1Panel.add( mTeam1Choice );
		Panel team2Panel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		team2Panel.add( mTeam2ChoiceLabel );
		team2Panel.add( mTeam2Choice );
		
		Panel scorePanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		scorePanel.add( mScoreLabel );
		scorePanel.add( mTeam1ScoreLabel );
		scorePanel.add( mTeam2ScoreLabel );
		
		Panel numPanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		numPanel.add( mNumAgentsChoiceLabel );
		numPanel.add( mNumAgentsChoice );
		
		Panel delayPanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		delayPanel.add( mDelayChoiceLabel );
		delayPanel.add( mDelayChoice );
		
		Panel quitPanel = new Panel( new FlowLayout( FlowLayout.CENTER ) );
		quitPanel.add( mQuitButton );
		
		controlFrame.add( pausePanel );
		controlFrame.add( obstPanel );
		controlFrame.add( team1Panel );
		controlFrame.add( team2Panel );
		controlFrame.add( numPanel );
		controlFrame.add( delayPanel );
		controlFrame.add( colorPanel );
		controlFrame.add( scorePanel );
		controlFrame.add( quitPanel );
		
		for( int c=0; c<GameColorMap.mKnownImpementingClasses.length; c++ ) {
			mColorChoice.add( GameColorMap.mKnownImpementingClasses[c] );
			}
		
		// set directory
		File setFile = new File( mSetsDirName );
		String setFiles[] = setFile.list();
		for( int i=0; i<setFiles.length; i++ ) {
			mObstacleChoice.add( setFiles[i] );
			}
		mObstacleChoice.add( "Random" );
		mObstacleChoice.add( "Large Random" );
		mObstacleChoice.select( "Random" );
		
		mPauseButton.addActionListener( new ButtonListener() );
		mColorChoice.addItemListener( new ColorChoiceListener() );
		mObstacleChoice.addItemListener( new ObstacleChoiceListener() );
		
		mQuitButton.addActionListener( new QuitActionListener() );
		
		for( int i=100; i<=1000; i+=100 ) {
			mDelayChoice.addItem( "" + i );
			}
		mDelayChoice.select( "1000" );
		mDelayChoice.addItemListener( new DelayChoiceListener() );
		
		mNumAgentsChoice.addItemListener( new NumAgentsChoiceListener() );
			
		
		
		String agentClassesFileName =  "AgentClasses.txt";
		try {
			StreamTokenizer tokenizer = 
				new StreamTokenizer( new FileInputStream( agentClassesFileName ) );
			tokenizer.eolIsSignificant( false );

			tokenizer.nextToken();
			int numClasses = (int)( tokenizer.nval );
			mAvailableClasses = new Class[numClasses];
			try {
				for( int i=0; i<numClasses; i++ ) {
					tokenizer.nextToken();
					String className = tokenizer.sval;
					mAvailableClasses[i] = Class.forName( className );
					}
				}
			catch( ClassNotFoundException inCNFE ) {
				System.out.println( "Class not found from AgentClasses.txt" );
				throw new Exception();
				}
			}
		catch( Exception inE ) {
			try {
				mAvailableClasses = new Class[1];
				mAvailableClasses[0] = Class.forName( "ctf.agent.ChenRandomAgent" );
				}
			catch( ClassNotFoundException inCNFE ) {
				System.out.println( "Class not found: " +
					"ctf.agent.ChenRandomAgent" + ", exiting.");
				System.exit(1);	
				}
			}
		
		for( int i=0; i<mAvailableClasses.length; i++ ) {
			mTeam1Choice.add( mAvailableClasses[i].getName() );
			mTeam2Choice.add( mAvailableClasses[i].getName() );
			}
		mTeam1Choice.addItemListener( new Team1ChoiceListener() );
		mTeam2Choice.addItemListener( new Team2ChoiceListener() );
		
		mTeamClasses = new Class[2];
		mTeamClasses[0] = mAvailableClasses[0];
		mTeamClasses[1] = mAvailableClasses[0];
		
		RandomSet randSet = new RandomSet();
		mBoardSet = randSet;
		mObstacleSet = randSet;
		
		resetNumAgentsChoice();
		
		mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
			mObstacleSet, mNumAgentsPerTeam, this );
		
		
		mReadyToPaint = true;
		setVisible( true );	
		controlFrame.setVisible( true );

		GameThread t = new GameThread();
		t.start();
		}
	
	
	
	private void scored( int inTeam ) {
		if( inTeam == 0 ) {
			mTeam1Score++;
			}
		else if( inTeam == 1 ) {
			mTeam2Score++;
			}
		else if( inTeam == 2 ) {
			// both teams scored at the same time
			mTeam1Score++;
			mTeam2Score++;
			}
		mTeam1ScoreLabel.setText( "Team1: " + mTeam1Score );
		mTeam2ScoreLabel.setText( "Team2: " + mTeam2Score );
		}
	
	
	
	private class GameThread extends Thread {
		public void run() {
			String delayName = mDelayChoice.getSelectedItem();
			try {
				int delayInt = Integer.parseInt( delayName );
				mGame.setDelay( delayInt );
				}
			catch( NumberFormatException inNFE ) {
				}
			
			int winner = mGame.playRound( mDrawThreshold );
			while( winner != -1 ) {
				// game ended in a win... reset board and start again.
				mGame.setPause( true );
				scored( winner );
				mPauseButton.setLabel( "Un-Pause" );
				winner = mGame.playRound( mDrawThreshold );
				}
			// else game was canceled or was a draw.
			// do nothing but return	
			}
		}
	
	
	
	public void paint( Graphics inGraphics ) {
		if( mReadyToPaint ) {
			
			int width = getSize().width;
			int height = getSize().height;
			
			Image graphicsBuffer = createImage( width, height );
			Graphics bufferGraphics = graphicsBuffer.getGraphics();
			
			int gridBorder = 50;

			int gridHigh = height - 2 * gridBorder;
			int gridWide = width - 2 * gridBorder;
			
			if( gridWide < gridHigh ) {
				gridHigh = gridWide;
				}
			else {
				gridWide = gridHigh;
				}
			
			mGame.draw( bufferGraphics, 
				gridBorder, gridBorder, gridWide, gridHigh );
				
			inGraphics.drawImage( graphicsBuffer, 0, 0, null );
			}
		}
	public void update( Graphics inGraphics ) {
		// override to reduce flicker
		paint( inGraphics );
		}
	
	public void stateChanged() {
		repaint();
		}
	
	
	
	private class QuitActionListener implements ActionListener {
		public void actionPerformed( ActionEvent inAE ) {
			System.exit(0);
			}
		}
	
	
	
	private class ColorChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {
			String mapName = mColorChoice.getSelectedItem();
			try {
				mColorMap = (GameColorMap)(
					Class.forName( "ctf.environment." + 
						mapName ).newInstance() );
				mGame.setColorMap( mColorMap );
				repaint();	
				}
			catch( Exception inE ) { 
				System.out.println( "Failed to load game color map: " +
					mapName );	
				}
			}
		}
	
	
	// used to reset the num agents choice box when the board size changes.
	private void resetNumAgentsChoice() {
		mNumAgentsChoice.removeAll();
		int maxNumAgents = mBoardSet.getBoardSize() - 2;
		for( int i=1; i<=maxNumAgents; i++ ) {
			mNumAgentsChoice.addItem( "" + i );
			}
		if( mNumAgentsPerTeam > maxNumAgents ) {
			mNumAgentsPerTeam = maxNumAgents;
			}
		mNumAgentsChoice.select( "" + mNumAgentsPerTeam );	
		}
	
		
	private class ObstacleChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {		
			if( mObstacleChoice.getSelectedItem().equals( "Random" ) ) {
				RandomSet randSet = new RandomSet();
				mBoardSet = randSet;
				mObstacleSet = randSet;
				resetNumAgentsChoice();
				mGame.cancel();
				mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
					mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
				mPauseButton.setLabel( "Un-Pause" );	
				
				GameThread t = new GameThread();
				t.start();
				repaint();
				return;
				}
			else if( mObstacleChoice.getSelectedItem().equals( "Large Random" ) ) {
				LargeRandomSet randSet = new LargeRandomSet();
				mBoardSet = randSet;
				mObstacleSet = randSet;
				resetNumAgentsChoice();
				mGame.cancel();
				mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
					mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
				mPauseButton.setLabel( "Un-Pause" );	
				
				GameThread t = new GameThread();
				t.start();
				repaint();
				return;
				}	
			String fileName = mSetsDirName + File.separatorChar 
				+ mObstacleChoice.getSelectedItem();
			try {
				FileSet fileSet = new FileSet( new File( fileName ) );
				mBoardSet = fileSet;
				mObstacleSet = fileSet;
				resetNumAgentsChoice();
				mGame.cancel();
				mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
					mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
				mPauseButton.setLabel( "Un-Pause" );	
				
				GameThread t = new GameThread();
				t.start();
				repaint();
				}
			catch( IOException inIOE ) {
				System.out.println( "Failed to load board set :" +
					fileName );
				}				
			}
		}	
	
	
	
	private class Team1ChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {		
			String className = mTeam1Choice.getSelectedItem();
			
			for( int i=0; i<mAvailableClasses.length; i++ ) {
				if( mAvailableClasses[i].getName().equals( className ) ) {
					mTeamClasses[0] = mAvailableClasses[i];
					mGame.cancel();
					mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
						mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
					mPauseButton.setLabel( "Un-Pause" );	
				
					GameThread t = new GameThread();
					t.start();
					repaint();
					return;
					}
				}
			}
		}
	
	private class Team2ChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {		
			String className = mTeam2Choice.getSelectedItem();
			
			for( int i=0; i<mAvailableClasses.length; i++ ) {
				if( mAvailableClasses[i].getName().equals( className ) ) {
					mTeamClasses[1] = mAvailableClasses[i];
					mGame.cancel();
					mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
						mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
					mPauseButton.setLabel( "Un-Pause" );	
				
					GameThread t = new GameThread();
					t.start();
					repaint();
					return;
					}
				}
			}
		}
	
	
	
	private class DelayChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {		
			String delayName = mDelayChoice.getSelectedItem();
			
			try {
				int delayInt = Integer.parseInt( delayName );
				mGame.setDelay( delayInt );
				}
			catch( NumberFormatException inNFE ) {
				}
			
			}
		}
	
	private class NumAgentsChoiceListener implements ItemListener {
		public void itemStateChanged( ItemEvent inIE ) {		
			String numName = mNumAgentsChoice.getSelectedItem();
			
			try {
				int numInt = Integer.parseInt( numName );
				mNumAgentsPerTeam = numInt;
				mGame.cancel();
				mGame = new Game( mTeamClasses, mColorMap, mBoardSet, 
					mObstacleSet, mNumAgentsPerTeam, TestPlaySurface.this );
				mPauseButton.setLabel( "Un-Pause" );	

				GameThread t = new GameThread();
				t.start();
				repaint();
				return;
				}
			catch( NumberFormatException inNFE ) {
				}
			
			}
		}
	
				
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent inAE ) {
			if( inAE.getSource() == mPauseButton ) {
				if( mPauseButton.getLabel().equals( "Un-Pause" ) ) {
					mPauseButton.setLabel( "Pause" );
					mGame.setPause( false );
					}
				else {
					mPauseButton.setLabel( "Un-Pause" );
					mGame.setPause( true );
					}
				}		
			}
		}
	
	}
