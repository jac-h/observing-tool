/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$
package orac.jcmt.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;
import gemini.sp.SpPosAngleObserver;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.util.Format;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.inst.SpJCMTInstObsComp;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.inst.SpInstSCUBA2;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.util.SpMapItem;

import java.util.Vector;
import java.util.StringTokenizer;

/**
 * Raster Iterator for ACSIS/JCMT.
 *
 * The Raster iterator (ACSIS) and the Scan iterator share a lot of fuctionality
 * and should in future be either made the same class or share code by other
 * means such as inheritance.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterRasterObs extends SpIterJCMTObs implements SpPosAngleObserver , SpMapItem
{

	/** TCS XML constants. */
	private static final String TX_OBS_AREA = "obsArea";
	private static final String TX_SCAN_AREA = "SCAN_AREA";
	private static final String TX_AREA = "AREA";
	private static final String TX_SCAN = "SCAN";
	private static final String TX_PA = "PA";
	private static final String TX_HEIGHT = "HEIGHT";
	private static final String TX_WIDTH = "WIDTH";
	private static final String TX_SCAN_VELOCITY = "VELOCITY";
	private static final String TX_SCAN_SYSTEM = "SYSTEM";
	private static final String TX_SCAN_DY = "DY";

	/** Default values for rosw/ref and rows/cal */
	private final String ROWS_PER_REF_DEFAULT = "1";
	private final String ROWS_PER_CAL_DEFAULT = "1";

	/** Needed for XML parsing. */
	private String _xmlPaAncestor;
	
	public static final double HARP_SAMPLE = 7.2761 ;
	public static final double HARP_FULL_ARRAY = 116.4171 ;

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "rasterObs" , "Scan/Raster" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterRasterObs() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterRasterObs()
	{
		super( SP_TYPE );

		_avTable.noNotifySet( ATTR_SCANAREA_WIDTH , "180.0" , 0 );
		_avTable.noNotifySet( ATTR_SCANAREA_HEIGHT , "180.0" , 0 );

		_avTable.noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , SCAN_SYSTEMS[ 0 ] , 0 );
	}

	/** Get area width (map width). */
	public double getWidth()
	{
		return _avTable.getDouble( ATTR_SCANAREA_WIDTH , 0. );
	}

	/** Set area width (map width). */
	public void setWidth( double width )
	{
		_avTable.set( ATTR_SCANAREA_WIDTH , Math.rint( width * 10. ) / 10. );
	}

	/** Set area width (map width). */
	public void setWidth( String widthStr )
	{
		setWidth( Format.toDouble( widthStr ) );
	}

	/** Get area height (map height). */
	public double getHeight()
	{
		return _avTable.getDouble( ATTR_SCANAREA_HEIGHT , 0. );
	}

	/** Set area height (map height). */
	public void setHeight( double height )
	{
		_avTable.set( ATTR_SCANAREA_HEIGHT , Math.rint( height * 10. ) / 10. );
	}

	/** Set area height (map height). */
	public void setHeight( String heightStr )
	{
		setHeight( Format.toDouble( heightStr ) );
	}

	/**
	 * Get area position angle (map position angle).
	 */
	public double getPosAngle()
	{
		return _avTable.getDouble( ATTR_SCANAREA_PA , 0. );
	}

	/**
	 * Set area postition angle (map postition angle).
	 */
	public void setPosAngle( double theta )
	{
		_avTable.set( ATTR_SCANAREA_PA , theta );

		if( _parent instanceof SpIterOffset )
			( ( SpIterOffset )_parent ).setPosAngle( getPosAngle() );
	}

	/**
	 * Set area position angle (map position angle).
	 */
	public void setPosAngle( String thetaStr )
	{
		setPosAngle( Format.toDouble( thetaStr ) );
	}

	/**
	 * Get n<sup>th<sup>scan angle.
	 */
	public double getScanAngle( int n )
	{
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_PA , n , 0. );
	}

	/**
	 * Get scan angle.
	 */
	public Vector getScanAngles()
	{
		return _avTable.getAll( ATTR_SCANAREA_SCAN_PA );
	}

	/**
	 * Set n<sup>th<sup> scan angle.
	 */
	public void setScanAngle( double theta , int n )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_PA , theta , n );
	}

	/**
	 * Set scan angle.
	 */
	public void setScanAngles( String thetaStr )
	{
		if( thetaStr == null )
		{
			_avTable.rm( ATTR_SCANAREA_SCAN_PA );
		}
		else
		{
			StringTokenizer stringTokenizer = new StringTokenizer( thetaStr , ",; " );
			int i = 0;
			while( stringTokenizer.hasMoreTokens() )
			{
				setScanAngle( Format.toDouble( stringTokenizer.nextToken() ) , i );
				i++ ;
			}
		}
	}

	/** Get scan velocity. */
	public double getScanVelocity()
	{

		// No scan velocity set yet. Try to calculate of the default velocity according to the instrument used.
		if( _avTable.getDouble( ATTR_SCANAREA_SCAN_VELOCITY , 0. ) == 0. )
		{
			SpInstObsComp inst = SpTreeMan.findInstrument( this );
			if( inst != null )
			{
				double scanVelocity = ( ( SpJCMTInstObsComp )inst ).getDefaultScanVelocity();
				_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "" + scanVelocity , 0 );
			}
		}
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_VELOCITY , 0. );
	}

	/** Set scan velocity. */
	public void setScanVelocity( double value )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , value );
	}

	/** Set scan velocity. */
	public void setScanVelocity( String value )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , Format.toDouble( value ) );
	}

	/**
	 * Get scan dx.
	 *
	 * Calculates scan dx in an instrument specific way.
	 *
	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public double getScanDx() throws UnsupportedOperationException
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this );
		if( inst == null )
		{
			throw new UnsupportedOperationException( "Could not find instrument in scope.\n" + "Needed for calculation of sample spacing." );
		}
		else
		{
			double dx = 0.;
			if( inst instanceof SpInstSCUBA )
				dx = getScanVelocity() / ( ( SpInstSCUBA )inst ).getChopFrequency();
			else if( inst instanceof SpInstHeterodyne )
				dx = getScanVelocity() * getSampleTime();
			else if( inst instanceof SpInstSCUBA2 )
				dx = getScanVelocity();

			return dx;
		}
	}

	/**
	 * Set scan dx.
	 *
	 * Sets scan in an instrument specific way.

	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public void setScanDx( double dx ) throws UnsupportedOperationException
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this );
		if( inst == null )
			throw new UnsupportedOperationException( "Could not find instrument in scope.\n" + "Needed for calculation of scan velocity." );
		else if( inst instanceof SpInstSCUBA )
			_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , ( ( SpInstSCUBA )inst ).getChopFrequency() * dx );
		else
			_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , ( dx / getSampleTime() ) );
	}

	/**
	 * Set scan dx.
	 *
	 * Sets scan in an instrument specific way.
	 *
	 * @throws java.lang.UnsupportedOperationException No instrument in scope.
	 */
	public void setScanDx( String dx ) throws UnsupportedOperationException
	{
		setScanDx( Format.toDouble( dx ) );
	}

	/** Get scan dy. */
	public double getScanDy()
	{
		// No scan velocity set yet. Try to calculate of the default velocity according to the instrument used.
		if( _avTable.getDouble( ATTR_SCANAREA_SCAN_DY , 0. ) == 0. )
		{
			SpInstObsComp inst = SpTreeMan.findInstrument( this );
			if( inst != null )
			{
				double scanDy = ( ( SpJCMTInstObsComp )inst ).getDefaultScanDy();
				_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "" + scanDy , 0 );
			}
		}
		return _avTable.getDouble( ATTR_SCANAREA_SCAN_DY , 10. );
	}

	/** Set scan dy. */
	public void setScanDy( double dy )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_DY , dy );
	}

	/** Set scan dy. */
	public void setScanDy( String dy )
	{
		_avTable.set( ATTR_SCANAREA_SCAN_DY , Format.toDouble( dy ) );
	}

	/**
	 * Get Scan System.
	 *
	 * Refers to TCS XML:
	 * <pre>
	 * &lt;SCAN_AREA&gt;
	 *   &lt;SCAN <b>SYSTEM="FPLANE"</b>&gt;
	 *   &lt;/SCAN&gt;
	 * &lt;SCAN_AREA&gt;
	 * </pre>
	 */
	public String getScanSystem()
	{
		return _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM );
	}

	/**
	 * Set Scan System.
	 *
	 * Refers to TCS XML:
	 * <pre>
	 * &lt;SCAN_AREA&gt;
	 *   &lt;SCAN <b>SYSTEM="FPLANE"</b>&gt;
	 *   &lt;/SCAN&gt;
	 * &lt;SCAN_AREA&gt;
	 * </pre>
	 */
	public void setScanSystem( String system )
	{
		if( system == null )
			_avTable.rm( ATTR_SCANAREA_SCAN_SYSTEM );
		else
			_avTable.set( ATTR_SCANAREA_SCAN_SYSTEM , system );
	}

	public String getRasterMode()
	{
		return _avTable.get( ATTR_RASTER_MODE );
	}

	public void setRasterMode( String value )
	{
		_avTable.set( ATTR_RASTER_MODE , value );
	}

	public String getRowsPerCal()
	{
		if( !( _avTable.exists( ATTR_ROWS_PER_CAL ) ) )
			setRowsPerCal( ROWS_PER_CAL_DEFAULT );

		return _avTable.get( ATTR_ROWS_PER_CAL );
	}

	public void setRowsPerCal( String value )
	{
		_avTable.set( ATTR_ROWS_PER_CAL , value );
	}

	public String getRowsPerRef()
	{
		return _avTable.get( ATTR_ROWS_PER_REF );
	}

	public void setRowsPerRef( String value )
	{
		_avTable.set( ATTR_ROWS_PER_REF , value );
	}

	public boolean getRowReversal()
	{
		return _avTable.getBool( ATTR_ROW_REVERSAL );
	}

	public void setRowReversal( boolean value )
	{
		_avTable.set( ATTR_ROW_REVERSAL , value );
	}

	public void setSampleTime( String value )
	{
		SpInstObsComp inst = SpTreeMan.findInstrument( this );
		if( inst instanceof SpInstHeterodyne )
		{
			// leave the old value
			if( Format.toDouble( value ) == 0. )
				return;
			else
				_avTable.set( ATTR_SCANAREA_SCAN_VELOCITY , getScanDx() / Format.toDouble( value ) );
		}
		super.setSampleTime( value );
	}

	public void posAngleUpdate( double posAngle )
	{
		// Do not use setPosAngle(posAngle) here as it would reset the posAngle of the class
		// calling posAngleUpdate(posAngle) which would then call posAngleUpdate(posAngle)
		// again an so on causing an infinite loop.
		_avTable.set( ATTR_SCANAREA_PA , posAngle );
	}

	public double getSecsPerRow()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
		{
			double mapWidth = getWidth();
			double sampleDX = getScanDx();
			double scanRate = sampleDX * SCAN_MAP_CHOP_FREQUENCY;
			return mapWidth / scanRate;
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			double rowOverhead = 17.14 ;
			double samplesPerRow = numberOfSamplesPerRow();

			double timeOnRow = samplesPerRow * getSampleTime();
			double timeOffRow = Math.sqrt( samplesPerRow ) * getSampleTime();
			return 1.05 * ( timeOnRow + timeOffRow + rowOverhead ) ;
		}
		return 0. ;
	}

	/**
	 * Convenience method.
	 * Gives the number of samples per row, which means the longest edge.
	 * @return
	 */
	public double numberOfSamplesPerRow()
	{
		return numberOfSamplesOnSide( true );
	}

	/**
	 * Convenience method.
	 * Gives the number of samples per column, which means the shortest edge.
	 * @return
	 */
	public double numberOfSamplesPerColumn()
	{
		return numberOfSamplesOnSide( false );
	}

	/**
	 * 
	 * @param row 
	 * 	true = number of samples per row,
	 *  false = number of samples per column
	 * @return
	 * 
	 */
	protected double numberOfSamplesOnSide( boolean row )
	{
		double samplesPerRow = getWidth();
		double samplesPerColumn = getHeight();

		boolean swap = false;
		
		// if AUTOMATIC and height > width
		// else if USER DEF and abs( scan angle - map angle ) is < 45
		// else if USER DEF and abs( scan angle - map angle ) is > 135
		boolean columnGreaterThanRow = samplesPerColumn > samplesPerRow;
		if( ( getScanAngles() == null ) || ( getScanAngles().size() == 0 ) )
			swap = columnGreaterThanRow ;
		else if( Math.abs( ( normalise( getScanAngle( 0 ) ) ) - ( normalise( getPosAngle() ) ) ) < 45. )
			swap = true;
		else if( Math.abs( ( normalise( getScanAngle( 0 ) ) ) - ( normalise( getPosAngle() ) ) ) > 135. )
			swap = true;

		if( swap )
		{
			double temp = samplesPerRow;
			samplesPerRow = samplesPerColumn;
			samplesPerColumn = temp;
		}

		samplesPerRow = ( Math.floor( samplesPerRow / getScanDx() ) ) + 1.;
		samplesPerColumn = ( Math.floor( samplesPerColumn / getScanDy() ) ) + 1.;

		if( ( ( ( int )samplesPerRow ) & 1 ) == 0 )
			samplesPerRow++ ;

		if( row )
			return samplesPerRow;
		else
			return samplesPerColumn;
	}

	private double normalise( double angle )
	{
		double returnable = angle ;

			while( returnable < 0 )
				returnable += 180. ;
			while( returnable > 180. )
				returnable -= 180. ;		

		return returnable ;
	}
	
	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
		{
			int nWaveplates = 0;
			double factor = 1. ;

			// Get information specified by user in the OT.
			double mapWidth = getWidth();
			double mapHeight = getHeight();
			double sampleDX = getScanDx();
			double sampleDY = getScanDy();

			// Calculate seconds per integration.
			double scanRate = sampleDX * SCAN_MAP_CHOP_FREQUENCY;
			double noOfRows = Math.ceil( ( mapHeight + 120 ) / sampleDY );
			double lengthOfRow = mapWidth;
			double secsPerRow = lengthOfRow / scanRate;
			double secsPerIntegration = noOfRows * secsPerRow;
			double calculatedOverhead = 515 * Math.pow( ( mapWidth * mapHeight ) , -0.1523 ) / 100;

			// Go through the parents and see if any of them are SpIterPOLS
			SpItem parent = parent();
			while( parent != null )
			{
				if( parent instanceof SpIterPOL )
					nWaveplates += ( ( SpIterPOL )parent ).getConfigSteps( "POLIter" ).size();
		
				parent = parent.parent();
			}
			if( nWaveplates > 1 )
				factor = 1. + ( ( double )nWaveplates - 1 ) / ( nWaveplates );

			// Overhead is 50 percent for scan map.
			return SCUBA_STARTUP_TIME * factor * ( ( 1. + calculatedOverhead ) * secsPerIntegration );
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			/*
			 * Based on real timing data 
			 * http://wiki.jach.hawaii.edu/staff_wiki-bin/wiki/20060925_jcmtfco
			 */
			double samplesPerRow = numberOfSamplesPerRow() ;
			double samplesPerColumn = numberOfSamplesPerColumn() ;

			if( ( ( ( int )samplesPerRow ) & 1 ) == 0 )
				samplesPerRow++ ;
			double time = getSecsPerRow() * samplesPerColumn + 80. ;
			return time;
		}
		return 0.;
	}

	public void setScanStrategy( String strategy )
	{
		if( _avTable.exists( ATTR_SCAN_STRATEGY ) )
		{
			_avTable.set( SpJCMTConstants.ATTR_SCAN_STRATEGY , strategy , 0 );
		}
	}

	public String getScanStrategy()
	{
		return _avTable.get( SpJCMTConstants.ATTR_SCAN_STRATEGY , 0 );
	}

	/** Creates JAC TCS XML. */
	protected void processAvAttribute( String avAttr , String indent , StringBuffer xmlBuffer )
	{
		// ATTR_SCANAREA_HEIGHT is an AV attribute that occurs once in a SpIterOffset's AV table
		// When processAvAttribute is called with ATTR_SCANAREA_HEIGHT as avAttr then append the entire
		// TCS XML representation of this item to the xmlBuffer.
		// For all other calls to processAvAttribute ignore the AV attributes, except meta attribues
		// such as ".gui.collapsed" which are delegated to the super class.
		if( avAttr.equals( ATTR_SCANAREA_HEIGHT ) )
		{
			// Append <obsArea> element.
			xmlBuffer.append( "\n" + indent + "  <" + TX_OBS_AREA + ">" );
			xmlBuffer.append( "\n" + indent + "    <" + TX_PA + ">" + getPosAngle() + "</" + TX_PA + ">" );

			xmlBuffer.append( "\n" + indent + "    <" + TX_SCAN_AREA + ">" );
			xmlBuffer.append( "\n" + indent + "      <" + TX_AREA + " " + TX_HEIGHT + "=\"" + getHeight() + "\" " + TX_WIDTH + "=\"" + getWidth() + "\"/>" );
			xmlBuffer.append( "\n" + indent + "      <" + TX_SCAN + " " + TX_SCAN_DY + "=\"" + getScanDy() + "\" " + TX_SCAN_VELOCITY + "=\"" + getScanVelocity() + "\" " + TX_SCAN_SYSTEM + "=\"" + getScanSystem() + "\">" );
			if( getScanAngles() != null )
			{
				for( int i = 0 ; i < getScanAngles().size() ; i++ )
					xmlBuffer.append( "\n" + indent + "        <" + TX_PA + ">" + getScanAngle( i ) + "</" + TX_PA + ">" );
			}
			xmlBuffer.append( "\n" + indent + "      </" + TX_SCAN + ">" );
			xmlBuffer.append( "\n" + indent + "    </" + TX_SCAN_AREA + ">" );

			xmlBuffer.append( "\n" + indent + "  </" + TX_OBS_AREA + ">" );
		}
		else if( avAttr.startsWith( TX_SCAN_AREA ) )
		{
			// Ignore. Dealt with in <obsArea> element (see above).
			;
		}
		else
		{
			super.processAvAttribute( avAttr , indent , xmlBuffer );
		}
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementStart( String name )
	{
		if( ( name != null ) && ( !name.equals( TX_PA ) ) )
			_xmlPaAncestor = name;

		super.processXmlElementStart( name );
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementContent( String name , String value )
	{

		// Ignore XML elements whose do not contain characters themselves but only
		// XML attributes or XML child elements.
		if( name.equals( TX_OBS_AREA ) || name.equals( TX_SCAN_AREA ) || name.equals( TX_AREA ) || name.equals( TX_SCAN ) )
		{
			;
		}
		else if( name.equals( TX_PA ) )
		{
			if( ( _xmlPaAncestor != null ) && _xmlPaAncestor.equals( TX_SCAN ) )
			{
				if( getScanAngles() == null )
					setScanAngle( Format.toDouble( value ) , 0 );
				else
					setScanAngle( Format.toDouble( value ) , getScanAngles().size() );
			}
			else
			{
				setPosAngle( value );
			}
		}
		else
		{
			super.processXmlElementContent( name , value );
		}
	}

	/** JAC TCS XML parsing. */
	public void processXmlElementEnd( String name )
	{
		// save() just means reset() in this context.
		if( name.equals( TX_OBS_AREA ) )
			getAvEditFSM().save();
		else
			super.processXmlElementEnd( name );
	}

	/** JAC TCS XML parsing. */
	public void processXmlAttribute( String elementName , String attributeName , String value )
	{
		if( elementName.equals( TX_AREA ) )
		{
			if( attributeName.equals( TX_HEIGHT ) )
				setHeight( value );
			else if( attributeName.equals( TX_WIDTH ) )
				setWidth( value );
		}
		else if( elementName.equals( TX_SCAN ) )
		{
			if( attributeName.equals( TX_SCAN_DY ) )
				setScanDy( value );
			else if( attributeName.equals( TX_SCAN_VELOCITY ) )
				setScanVelocity( value );
			else  if( attributeName.equals( TX_SCAN_SYSTEM ) )
				setScanSystem( value );
		}
		else
		{
			super.processXmlAttribute( elementName , attributeName , value );
		}
	}

	public void setDefaults()
	{
		setContinuumMode( false );
		setRowsPerRef( ROWS_PER_REF_DEFAULT );
		// Set the rows per cal to give about 10 minutes between calibrations.
		double rowsPerRef = ( 10 * 60 ) / getSecsPerRow();
		if( rowsPerRef < 1 )
			rowsPerRef = 1. ;
		else if( rowsPerRef > Math.ceil( getHeight() / getScanDy() ) )
			rowsPerRef = Math.ceil( getHeight() / getScanDy() );

		int iRowsPerRef = ( int )Math.rint( rowsPerRef );
		setRowsPerCal( "" + iRowsPerRef );
	}

	public void setupForHeterodyne()
	{
		if( _avTable.get( ATTR_SWITCHING_MODE ) == null || _avTable.get( ATTR_SWITCHING_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , SWITCHING_MODE_POSITION , 0 );

		if( _avTable.get( ATTR_ROWS_PER_CAL ) == null || _avTable.get( ATTR_ROWS_PER_CAL ).equals( "" ) )
			_avTable.noNotifySet( ATTR_ROWS_PER_CAL , ROWS_PER_CAL_DEFAULT , 0 );

		if( _avTable.get( ATTR_ROWS_PER_REF ) == null || _avTable.get( ATTR_ROWS_PER_REF ).equals( "" ) )
			_avTable.noNotifySet( ATTR_ROWS_PER_REF , ROWS_PER_REF_DEFAULT , 0 );

		if( _avTable.get( ATTR_SAMPLE_TIME ) == null || _avTable.get( ATTR_SAMPLE_TIME ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SAMPLE_TIME , "4" , 0 );

		if( _avTable.get( ATTR_CONTINUUM_MODE ) == null || _avTable.get( ATTR_CONTINUUM_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_CONTINUUM_MODE , "false" , 0 );

		if( _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM ) == null || _avTable.get( ATTR_SCANAREA_SCAN_SYSTEM ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_SYSTEM , SpJCMTConstants.SCAN_SYSTEMS[ 0 ] , 0 );

		if( _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "0.0" , 0 );

		if( _avTable.get( ATTR_SCANAREA_SCAN_DY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_DY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "10.0" , 0 );

		_avTable.noNotifyRm( ATTR_SCAN_STRATEGY );
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_ROWS_PER_CAL );
		_avTable.noNotifyRm( ATTR_ROWS_PER_REF );
		_avTable.noNotifyRm( ATTR_SAMPLE_TIME );
		_avTable.noNotifyRm( ATTR_CONTINUUM_MODE );
		if( _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanVelocity() , 0 );
		if( _avTable.get( ATTR_SCANAREA_SCAN_DY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_DY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanDy() , 0 );
		_avTable.noNotifyRm( ATTR_SCAN_STRATEGY );
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_ROWS_PER_CAL );
		_avTable.noNotifyRm( ATTR_ROWS_PER_REF );
		_avTable.noNotifyRm( ATTR_SAMPLE_TIME );
		_avTable.noNotifyRm( ATTR_CONTINUUM_MODE );
		if( _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_VELOCITY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_VELOCITY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanVelocity() , 0 );
		if( _avTable.get( ATTR_SCAN_STRATEGY ) == null || _avTable.get( ATTR_SCAN_STRATEGY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCAN_STRATEGY , SpJCMTConstants.SCAN_STRATAGIES[ 0 ] , 0 );
		if( _avTable.get( ATTR_SCANAREA_SCAN_DY ) == null || _avTable.get( ATTR_SCANAREA_SCAN_DY ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SCANAREA_SCAN_DY , "" + ( ( SpJCMTInstObsComp )SpTreeMan.findInstrument( this ) ).getDefaultScanDy() , 0 );
	}

	public String[] getSwitchingModeOptions()
	{
		return new String[] { SWITCHING_MODE_POSITION , SWITCHING_MODE_CHOP };
	}
}