/* ============================================================== */
/*                                                              */
/* UK Astronomy Technology Centre */
/* Royal Observatory, Edinburgh */
/* Joint Astronomy Centre, Hilo */
/* Copyright (c) PPARC 2001 */
/*                                                              */
/* ============================================================== */
// $Id$
package edfreq;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JFrame;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Hashtable;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**
 * This class contains all information related to defined emission lines, and
 * their associated molecules and transitions.
 * 
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger
 *         (M.Folger@roe.ac.uk)
 */
public class EmissionLines extends JPanel implements MouseListener , ChangeListener
{

	int xSize;
	int ySize;
	JPopupMenu popup = null;
	private Hashtable popupLineTable = new Hashtable();
	private Hashtable popupLinePosTable = new Hashtable();
	private JMenuItem[] samplerMenus;
	JMenuItem item;
	private double lowLimit;
	private double highLimit;
	private double halfrange;
	private double restHalfrange;
	private LineDetails[] lineStore;
	private LineDetails[] altLineStore;
	private LineCatalog lineCatalog;
	private Image buffer = null;
	private Graphics ig;
	private double mainLineFreq = 0;
	private int mainLinePos = -1;
	private double sideLineFreq = 0;
	private int sideLinePos = -1;
	// Added by MFO (October 15, 2002)
	private LineDetails selectedLine;
	private double popupLineFreq = 0;

	/**
     * Position of the line temporarily selected in the popup menu while the
     * popup menu is visible.
     */
	private int popupLinePos = -1;
	private double redshift;
	private double restLowLimit;
	private double restHighLimit;
	public static int FREQUENCY_DISPLAY = 1;
	public static int VELOCITY_DISPLAY = 0;
	private int _currentDisplayMode = FREQUENCY_DISPLAY;

	public EmissionLines( double lowLimit , double highLimit , double redshift , int xSize , int ySize , int samplerCount )
	{
		super();

		int j;

		this.xSize = xSize;
		this.ySize = ySize;
		restLowLimit = lowLimit;
		restHighLimit = highLimit;

		this.lowLimit = restLowLimit * ( 1.0 + redshift );
		this.highLimit = restHighLimit * ( 1.0 + redshift );
		this.redshift = redshift;

		restHalfrange = 0.5 * ( highLimit - lowLimit );
		halfrange = 0.5 * ( this.highLimit - this.lowLimit );

		/* Select the emission lines within the frequency range */

		lineStore = new LineDetails[ xSize ];
		try
		{
			lineCatalog = LineCatalog.getInstance();
		}
		catch( Exception e )
		{
			System.out.println( e.getMessage() );
		}

		for( j = 0 ; j < xSize ; j++ )
			lineStore[ j ] = null;

		lineCatalog.returnLines( this.lowLimit , this.highLimit , xSize , lineStore );

		/* Set up the graphics */

		setPreferredSize( new Dimension( xSize , ySize ) );
		setSize( xSize , ySize );
		addMouseListener( this );

		samplerMenus = new JMenuItem[ samplerCount ];
		for( int i = 0 ; i < samplerCount ; i++ )
			samplerMenus[ i ] = new JMenuItem( "" + i );
	}

	/**
     * Get lines in the alternate sideband. Only used for velocity space.
     * Whether or nore these lines are drawn is dependent of the current display
     * mode
     */
	public void showAlternateSideband( int currentSideband , double feIF )
	{
		altLineStore = new LineDetails[ xSize ];
		if( lineCatalog == null )
		{
			try
			{
				lineCatalog = LineCatalog.getInstance();
			}
			catch( Exception e )
			{
				e.printStackTrace();
				return;
			}
		}
		// Initialise all elements
		for( int j = 0 ; j < xSize ; j++ )
			altLineStore[ j ] = null;

		// Calculate the frequency limits
		double altLowLimit , altHighLimit;
		if( currentSideband == EdFreq.SIDE_BAND_LSB )
		{
			altLowLimit = 2.0 * feIF + lowLimit;
			altHighLimit = 2.0 * feIF + highLimit;
		}
		else
		{
			altLowLimit = lowLimit - 2.0 * feIF;
			altHighLimit = highLimit - 2.0 * feIF;
		}

		lineCatalog.returnLines( altLowLimit , altHighLimit , xSize , altLineStore );
	}

	/**
     * Set the redshift of the source.
     * 
     * @param redshift
     *            Redshift (Z) of source.
     */
	public void setRedshift( double redshift )
	{
		this.redshift = redshift;
		lowLimit = restLowLimit * ( 1.0 + redshift );
		highLimit = restHighLimit * ( 1.0 + redshift );
		halfrange = 0.5 * ( highLimit - lowLimit );
		updateLines();
		repaint();
	}

	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		int j;

		if( buffer == null )
		{
			buffer = createImage( xSize , ySize );

			// added by MFO, 16 November 2001
			if( buffer == null )
				return;

			ig = buffer.getGraphics();
		}

		ig.setColor( getBackground() );
		ig.fillRect( 0 , 0 , xSize , ySize );
		ig.setColor( getForeground() );

		for( j = 0 ; j < xSize ; j++ )
		{
			if( lineStore[ j ] != null )
				ig.drawLine( j , 0 , j , ySize );
		}
		if( mainLinePos >= 0 )
		{
			ig.setColor( Color.red );
			ig.drawLine( mainLinePos , 0 , mainLinePos , ySize );
		}
		if( sideLinePos >= 0 )
		{
			ig.setColor( Color.magenta );
			ig.drawLine( sideLinePos , 0 , sideLinePos , ySize );
		}
		// Added by MFO (October 15, 2002)
		if( popupLinePos >= 0 )
		{
			ig.setColor( Color.green );
			ig.drawLine( popupLinePos , 0 , popupLinePos , ySize );
		}

		if( _currentDisplayMode == VELOCITY_DISPLAY && altLineStore != null )
		{
			ig.setColor( Color.blue );
			for( int i = 0 ; i < altLineStore.length ; i++ )
			{
				if( altLineStore[ i ] != null )
					ig.drawLine( i , 0 , i , ySize );
			}
		}

		g.drawImage( buffer , 0 , 0 , null );
	}

	/** Pops up information about a line when the line is pressed. */
	public void mousePressed( MouseEvent e )
	{
		int j;
		int xPos;

		// Emission lines in the interval [-range, range] are displayed. (Added
        // by MFO, October 15, 2002)
		int range = 200;

		j = e.getX();
		xPos = j;
		if( xPos > xSize / 2 )
			xPos = xPos - 200;

		popup = new JPopupMenu();

		// The popupLineTable could probably be cleared at this point but it is
        // not necessary
		// to clear the it and this class has never been tested with the
        // following line enabled.
		// popupLineTable.clear();

		for( int i = j - range ; i <= j + range ; i++ )
		{
			if( ( i >= 0 ) && ( i < lineStore.length ) && ( lineStore[ i ] != null ) )
			{
				item = new JMenuItem( lineStore[ i ].name + "  " + lineStore[ i ].transition + "  " + lineStore[ i ].frequency / 1.0e3 );

				popupLineTable.put( item , lineStore[ i ] );
				popupLinePosTable.put( item , new int[] { i } );

				item.addChangeListener( this );
				popup.add( item );
			}
		}

		if( _currentDisplayMode == VELOCITY_DISPLAY )
		{
			for( int i = j - range ; i <= j + range ; i++ )
			{
				if( i >= 0 && i < altLineStore.length && altLineStore[ i ] != null )
				{
					item = new JMenuItem( altLineStore[ i ].name + " " + altLineStore[ i ].transition + " " + altLineStore[ i ].frequency / 1.0E3 );
					popupLineTable.put( item , altLineStore[ i ] );
					popupLinePosTable.put( item , new int[] { i } );
					item.addChangeListener( this );
					popup.add( item );
				}
			}
		}

		if( popup != null )
			popup.show( this , xPos , e.getY() );
	}

	public void mouseClicked( MouseEvent e ){}

	public void mouseEntered( MouseEvent e ){}

	public void mouseExited( MouseEvent e ){}

	/** Clears the line information popup */
	public void mouseReleased( MouseEvent e )
	{
		if( popup != null )
			popup.setVisible( false );
	}

	/**
     * Updates line details in response to state change.
     */
	public void stateChanged( ChangeEvent e )
	{
		if( e.getSource() instanceof JMenuItem )
		{
			// update popupLinePos
			popupLinePos = ( ( int[] )popupLinePosTable.get( e.getSource() ) )[ 0 ];

			repaint();

			selectedLine = ( ( LineDetails )popupLineTable.get( e.getSource() ) );
			popupLineFreq = selectedLine.frequency * 1.0E6;

			return;
		}

		double value;

		/* Find lines using allowing for velocity offset */

		value = EdFreq.SLIDERSCALE * ( double )( ( JSlider )e.getSource() ).getValue();

		restLowLimit = value - restHalfrange;
		restHighLimit = value + restHalfrange;
		lowLimit = restLowLimit * ( 1.0 + redshift );
		highLimit = restHighLimit * ( 1.0 + redshift );
		halfrange = 0.5 * ( highLimit - lowLimit );

		updateLines();

		repaint();
	}

	/**
     * Update the emission lines available.
     */
	public void updateLines()
	{
		int j;

		for( j = 0 ; j < xSize ; j++ )
			lineStore[ j ] = null;

		lineCatalog.returnLines( lowLimit , highLimit , xSize , lineStore );

		if( ( ( mainLineFreq > lowLimit ) && ( mainLineFreq < highLimit ) ) || _currentDisplayMode == VELOCITY_DISPLAY )
		{
			if( _currentDisplayMode == FREQUENCY_DISPLAY )
				mainLinePos = ( int )( ( ( double )xSize ) * ( mainLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
			else
				mainLinePos = xSize / 2;
		}
		else
		{
			mainLinePos = -1;
		}

		if( ( sideLineFreq > lowLimit ) && ( sideLineFreq < highLimit ) )
			sideLinePos = ( int )( ( ( double )xSize ) * ( sideLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
		else
			sideLinePos = -1;

		if( ( popupLineFreq > lowLimit ) && ( popupLineFreq < highLimit ) )
			popupLinePos = ( int )( ( ( double )xSize ) * ( popupLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
		else
			popupLinePos = -1;
	}

	/**
     * Sets the main line based on input from the Frequency Editor GUI.
     * 
     * @param frequency
     *            Frequency of main line in Hz.
     */
	public void setMainLine( double frequency )
	{
		mainLineFreq = frequency;

		if( ( ( mainLineFreq > lowLimit ) && ( mainLineFreq < highLimit ) ) || _currentDisplayMode == VELOCITY_DISPLAY )
		{
			if( _currentDisplayMode == FREQUENCY_DISPLAY )
				mainLinePos = ( int )( ( ( double )xSize ) * ( mainLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
			else
				mainLinePos = xSize / 2;
		}
		else
		{
			mainLinePos = -1;
		}

		repaint();
	}

	/**
     * Sets the side line based on input from the Frequency Editor GUI.
     * 
     * @param frequency
     *            Frequency of side line in Hz.
     */
	public void setSideLine( double frequency )
	{
		sideLineFreq = frequency;

		if( ( sideLineFreq > lowLimit ) && ( sideLineFreq < highLimit ) )
			sideLinePos = ( int )( ( ( double )xSize ) * ( sideLineFreq - lowLimit ) / ( highLimit - lowLimit ) );
		else
			sideLinePos = -1;

		repaint();

	}

	public void setDisplayMode( int displayMode )
	{
		if( displayMode == _currentDisplayMode )
			return;

		switch( displayMode )
		{
			case 1 : // Frequency display
				_currentDisplayMode = FREQUENCY_DISPLAY;
				updateLines();
				repaint();
				break;
			case 0 : // Velocity display
				_currentDisplayMode = VELOCITY_DISPLAY;
				updateLines();
				repaint();
				break;
			default :
				// Do nothing
		}
		updateLines();
		repaint();
	}

	/**
     * Get the information about the currently selected line.
     */
	public LineDetails getSelectedLine()
	{
		return selectedLine;
	}


	public static void main( String[] args )
	{
		EmissionLines el = new EmissionLines( 3.651E+11 , 3.749E+11 , 0 , 800 , 20 , 1 );
		el.setMainLine( 3.69498216E+11 );

		JFrame frame = new JFrame( "EmissionLine Display" );
		frame.setResizable( false );
		frame.getContentPane().add( el );
		frame.setLocation( 100 , 100 );
		frame.pack();
		frame.setVisible( true );
	}
}