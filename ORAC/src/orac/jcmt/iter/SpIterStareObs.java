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

import gemini.sp.iter.SpIterEnumeration;
import gemini.sp.iter.SpIterObserveBase;
import gemini.sp.iter.SpIterStep;
import gemini.sp.iter.SpIterValue;

import gemini.sp.obsComp.SpInstConstants;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpStareCapability;

import java.util.Enumeration;


/**
 * Stare Iterator for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterStareObs extends SpIterJCMTObs
{
   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "stareObs", "Photom/Sample");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterStareObs());
}


/**
 * Default constructor.
 */
public SpIterStareObs()
{
   super(SP_TYPE);
}

    public void setWidePhotom (boolean flag) {
	_avTable.set( ATTR_WIDE_PHOTOMETRY, flag );
    }

    public boolean getWidePhotom () {
	boolean isSet = false;
	if ( _avTable.exists( ATTR_WIDE_PHOTOMETRY ) && _avTable.getBool ( ATTR_WIDE_PHOTOMETRY ) ) {
	    isSet = true;
	}
	return isSet;
    }

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );
		double overhead = 0.0;
		double totalIntegrationTime = 0.0;

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
			overhead = SCUBA_STARTUP_TIME + 8 ;
			if( polPhot )
			{
				// 8 seconds per integration
				totalIntegrationTime = 8 ;
			}
			else
			{
				// 18 seconds per integration
				if( getWidePhotom() )
				{
					totalIntegrationTime = 24 ;
				}
				else
				{
					totalIntegrationTime = 18 ;
				}
			}
		}
		else if( instrument instanceof orac.jcmt.inst.SpInstHeterodyne )
		{
			double overheadFactor = 1.2;
			totalIntegrationTime = getSecsPerCycle() * overheadFactor;
		}
		return ( overhead + totalIntegrationTime );
	}

	public void setupForHeterodyne()
	{
		if( _avTable.get( ATTR_SWITCHING_MODE ) == null || _avTable.get( ATTR_SWITCHING_MODE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SWITCHING_MODE , SWITCHING_MODE_CHOP , 0 );
		if( _avTable.get( ATTR_SECS_PER_CYCLE ) == null || _avTable.get( ATTR_SECS_PER_CYCLE ).equals( "" ) )
			_avTable.noNotifySet( ATTR_SECS_PER_CYCLE , "60" , 0 );
	}

	public void setupForSCUBA()
	{
		_avTable.noNotifyRm( ATTR_SWITCHING_MODE );
		_avTable.noNotifyRm( ATTR_SECS_PER_CYCLE );
		_avTable.noNotifyRm( ATTR_CONT_CAL );
	}

    public String [] getSwitchingModeOptions() {
        return new String [] {
            SWITCHING_MODE_BEAM,
            SWITCHING_MODE_POSITION,
            SWITCHING_MODE_FREQUENCY_S,
            SWITCHING_MODE_FREQUENCY_F,
            SWITCHING_MODE_NONE 
        };
    }
}


