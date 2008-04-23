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

import gemini.sp.obsComp.SpInstObsComp;

import gemini.sp.iter.SpIterChop;
import gemini.util.Format;

/**
 * Stare Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterStareObs extends SpIterJCMTObs
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "stareObs" , "Stare" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterStareObs() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterStareObs()
	{
		super( SP_TYPE );
	}

	public void setWidePhotom( boolean flag )
	{
		_avTable.set( ATTR_WIDE_PHOTOMETRY , flag );
	}

	public boolean getWidePhotom()
	{
		return ( _avTable.exists( ATTR_WIDE_PHOTOMETRY ) && _avTable.getBool( ATTR_WIDE_PHOTOMETRY ) ) ;
	}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		double overhead = 0. ;
		double totalIntegrationTime = 0. ;

		if( instrument instanceof orac.jcmt.inst.SpInstSCUBA )
		{
			// Go through all of this items parents to see if any are SpIterPOLs
			boolean polPhot = false;
			SpItem parent = parent();
			while( parent != null )
			{
				if( parent instanceof SpIterPOL )
				{
					polPhot = true;
					break;
				}
				parent = parent.parent();
			}
			overhead = SCUBA_STARTUP_TIME + 8;
			if( polPhot )
			{
				// 8 seconds per integration
				totalIntegrationTime = 8;
			}
			else
			{
				// 18 seconds per integration
				if( getWidePhotom() )
					totalIntegrationTime = 24;
				else
					totalIntegrationTime = 18;
			}
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			/*
			 * Based on real timing data 
			 * http://www.jach.hawaii.edu/software/jcmtot/het_obsmodes.html 2007-07-12
			 * 
			 * Overridden in SpIterFolder, these timings are single position if outside an iterator
			 */
			double T_on = getSecsPerCycle();
			String switchingMode = getSwitchingMode();

			if( SWITCHING_MODE_POSITION.equals( switchingMode ) )
				totalIntegrationTime = 2.45 * T_on + 80.;
			else if( SWITCHING_MODE_BEAM.equals( switchingMode ) )
				totalIntegrationTime = 2.3 * T_on + 100.;

			if( isContinuum() )
				totalIntegrationTime *= 1.2;
		}
		return( overhead + totalIntegrationTime );
	}

	public boolean insideChop()
	{
		return insideComponent( SpIterChop.class ) != null ;
	}
	
	public boolean insidePOL()
	{
		Object returned = insideComponent( SpIterPOL.class ) ;
		return ( returned != null && (( SpIterPOL )returned).getTable().exists( "continuousSpin" ) ) ;
	}
	
	private Object insideComponent( Class component )
	{
		boolean inside = false;
		SpItem parent = this.parent();
		while( parent != null )
		{
			inside = component.isInstance( parent ) ;
			if( inside )
				break;
			parent = parent.parent();
		}
		return parent ;		
	}

	public void setupForHeterodyne()
	{
		if( insideChop() )
		{
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , SWITCHING_MODE_BEAM , 0 );
			rmSeparateOffs();
		}
		
		_avTable.noNotifyRm( ATTR_SAMPLE_TIME ) ;

		if( _avTable.get( ATTR_SWITCHING_MODE ) == null || _avTable.get( ATTR_SWITCHING_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , getSwitchingModeOptions()[ 0 ] , 0 );
		if( _avTable.get( ATTR_SECS_PER_CYCLE ) == null || _avTable.get( ATTR_SECS_PER_CYCLE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SECS_PER_CYCLE , "60" , 0 );
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONT_CAL );
	}

	public void setupForSCUBA2()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_SAMPLE_TIME );
		_avTable.noNotifyRm( ATTR_CONT_CAL );
		_avTable.noNotifyRm( ATTR_WIDE_PHOTOMETRY );
		if( _avTable.get( ATTR_SECS_PER_CYCLE ) == null || _avTable.get( ATTR_SECS_PER_CYCLE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SECS_PER_CYCLE , "4.0" , 0 );
	}

	public String[] getSwitchingModeOptions()
	{
		return new String[] 
		{ 
				SWITCHING_MODE_BEAM , SWITCHING_MODE_POSITION ,
				SWITCHING_MODE_FREQUENCY_S ,
				SWITCHING_MODE_FREQUENCY_F ,
				SWITCHING_MODE_NONE
		};
	}

	public void setArrayCentred( boolean enable )
	{
		_avTable.set( ATTR_ARRAY_CENTRED , enable );
	}

	public boolean isArrayCentred()
	{
		return _avTable.getBool( ATTR_ARRAY_CENTRED );
	}

	public void rmArrayCentred()
	{
		_avTable.noNotifyRm( ATTR_ARRAY_CENTRED );
	}

	public void setSeparateOffs( boolean enable )
	{
		_avTable.set( SEPARATE_OFFS , enable );
	}

	public boolean hasSeparateOffs()
	{
		return _avTable.getBool( SEPARATE_OFFS );
	}

	public void rmSeparateOffs()
	{
		_avTable.noNotifyRm( SEPARATE_OFFS );
	}
	
	public boolean separateOffsExist()
	{
		return _avTable.exists( SEPARATE_OFFS ) ;
	}
	
	/**
	 * Get area position angle (map position angle).
	 */
	public double getPosAngle()
	{
		return _avTable.getDouble( ATTR_STARE_PA , 0. ) ;
	}

	/**
	 * Set area postition angle (map postition angle).
	 */
	public void setPosAngle( double theta )
	{
		_avTable.set( ATTR_STARE_PA , Math.rint( theta * 10. ) / 10. ) ;
	}

	/**
	 * Set area position angle (map position angle).
	 */
	public void setPosAngle( String thetaStr )
	{
		setPosAngle( Format.toDouble( thetaStr ) ) ;
	}
	
	/**
	 * Set map coordinate system.
	 */
	public void rmPosAngle()
	{
		_avTable.noNotifyRm( ATTR_STARE_PA ) ;
	}

	/**
	 * Get map coordinate system.
	 */
	public String getCoordSys()
	{
		if( !_avTable.exists( ATTR_STARE_SYSTEM ) )
			setCoordSys( STARE_SYSTEMS[ 0 ] ) ;
		return _avTable.get( ATTR_STARE_SYSTEM ) ;
	}

	/**
	 * Set map coordinate system.
	 */
	public void setCoordSys( String coordSys )
	{
		_avTable.set( ATTR_STARE_SYSTEM , coordSys ) ;
	}
	
	/**
	 * Set map coordinate system.
	 */
	public void rmCoordSys()
	{
		_avTable.noNotifyRm( ATTR_STARE_SYSTEM ) ;
	}
}
