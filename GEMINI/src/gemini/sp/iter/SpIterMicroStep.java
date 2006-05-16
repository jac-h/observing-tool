// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;

import gemini.sp.SpItem;
import gemini.sp.SpFactory;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpType;
import gemini.sp.SpTreeMan;

import gemini.sp.iter.SpIterOffset;

import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpMicroStepUser;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import gemini.util.MathUtil ;


/**
 * Microstep Iterator item.
 *
 * The microstep iterator is similar to the offset iterator. The differences are:
 *
 * <ul>
 *   <li> If a microstep iterator is inside an offset iterator and if it is the
 *        only item inside the offset iterator then the offset commands
 *        in the exec file created by the translator are net offsets from the
 *        base position, e.g.
 *        <pre>
 *          exec offset command 1 = offset iterator position 1 + microstep position 1
 *          exec offset command 2 = offset iterator position 1 + microstep position 2
 *          exec offset command 3 = offset iterator position 1 + microstep position 3
 *          exec offset command 4 = offset iterator position 1 + microstep position 4
 *          exec offset command 5 = offset iterator position 2 + microstep position 1
 *          exec offset command 6 = offset iterator position 2 + microstep position 2
 *          ...
 *        </pre>
 *
 *   <li> In order to retain a record of the nested nature of the above net offsets
 *        the translator adds FITS the following FITS headers (taken from
 *        document <i>WFCAM HDS container and FITS headers</i> by Alan Pickup)
 *        <pre>
 *          NJITTER	Number of positions in telescope jitter pattern
 *          JITTER_I	Serial number in this telescope jitter pattern
 *          JITTER_X	[arcsec] X (RA) offset in telescope jitter pattern 
 *          JITTER_Y	[arcsec] Y (Dec) offset in telescope jitter pattern
 *          NUSTEP	Number of positions in microstep pattern
 *          USTEP_I	Serial number in this microstep pattern
 *          USTEP_X	[arcsec] X (RA) offset in microstep pattern
 *          USTEP_Y	[arcsec] Y (Dec) offset in microstep pattern
 *        </pre>
 * 
 *   <li> The microstep iterator does not put any restrictions on what a microstep pattern can look
 *        like and where it comes from.<p>
 *        However, the microstep iterator editor classes restricts the
 *        choice offered to the user to the microstep patterns defined by those instrument classes that
 *        implement the SpMicroStepUser interface and provide a list of meaningful instrument specific
 *        microstep patterns. For all other instruments the only microstep pattern option is
 *        SpIterMicroStep.NO_PATTERN.<p>
 *        If the microstep pattern is set to SpIterMicroStep.NO_PATTERN then a single microstep postion is
 *        added at 0.0. This means that there will be one "microstep" at each of the offset positions
 *        if the microstep iterator is inside an offset iterator or one "microstep" at the telescope base
 *        position if the microstep iterator is not inside an offset iterator. In either case
 *        the microstep iterator has no effect.
 * </ul>
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterMicroStep extends SpIterOffset implements SpTranslatable
{
   public static String NO_PATTERN = "NONE";

   public static String ATTR_PATTERN = "pattern";

   public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "microstep", "MicroStep");

// Register the prototype.
static {
   SpFactory.registerPrototype(new SpIterMicroStep());
}


/**
 * Default constructor.
 */
public SpIterMicroStep()
{
   super(SP_TYPE);

   _avTable.noNotifySet(ATTR_PATTERN, NO_PATTERN, 0);
}

/**
 * Get the instrument item in the scope of the base item.
 */
public SpInstObsComp
getInstrumentItem()
{
   SpItem _baseItem = parent();
   return (SpInstObsComp) SpTreeMan.findInstrument(_baseItem);
}


/**
 * Override getTitle to return the observe count.
 */
public String
getTitle()
{
   if (getTitleAttr() != null) {
      return super.getTitle();
   }

   return "MicroStep";
}


/**
 * Get the microstep pattern.
 */
public String
getPattern()
{
   return _avTable.get(ATTR_PATTERN);
}
    
/**
 * Set the microstep pattern.
 */
public void
setPattern(String pattern)
{
   _avTable.set(ATTR_PATTERN, pattern);
}


public String title_offset() {
   return "microstep";
}

public int getNOffsets() {
    int nOffs = 1;
    SpInstObsComp inst = SpTreeMan.findInstrument(this);
    if ( inst instanceof SpMicroStepUser ) {
        Hashtable microStepTable = ((SpMicroStepUser)inst).getMicroStepPatterns();
        double [][] microSteps = (double[][])microStepTable.get(getPattern());
        if (microSteps != null) nOffs =  microSteps.length;
    }
    return nOffs;
}

	public void translate( Vector v ) throws SpTranslationNotSupportedException
	{
		// In order to get the microstep pattern, we need to get the current instrument and make
		// sure it implemeents SpMicroStepUser interface
		SpInstObsComp inst = SpTreeMan.findInstrument( this );
		if( !( inst instanceof SpMicroStepUser ) )
		{
			throw new SpTranslationNotSupportedException( "Current instrument can not be used with microsteps" );
		}
		Hashtable microStepTable = ( ( SpMicroStepUser ) inst ).getMicroStepPatterns();
		double[][] microSteps = ( double[][] ) microStepTable.get( getPattern() );
		if( microSteps == null )
			return;
		// Find out whether we are inside an offset pattern
		boolean inOffset = false;
		SpItem parent = parent();
		while( parent != null )
		{
			if( parent instanceof SpIterOffset && !( parent instanceof SpIterMicroStep ) )
			{
				inOffset = true;
				break;
			}
			parent = parent.parent();
		}
		
		if( inOffset )
		{
			// parent is the SpIterOffset object at this point, although we probably don't need it
			for( int i = 0 ; i < ( ( SpIterOffset ) parent ).getPosList().size() ; i++ )
			{
				double xOff = ( ( SpIterOffset ) parent ).getPosList().getPositionAt( i ).getXaxis();
				double yOff = ( ( SpIterOffset ) parent ).getPosList().getPositionAt( i ).getYaxis();
				for( int j = 0 ; j < microSteps.length ; j++ )
				{
					v.add( "title jitter " + ( i + 1 ) + ", ustep " + ( j + 1 ) );
					v.add( "-setHeader NJITTER " + ( ( SpIterOffset ) parent ).getPosList().size() );
					v.add( "-setHeader JITTER_I " + ( i + 1 ) );
					v.add( "-setHeader JITTER_X " + xOff );
					v.add( "-setHeader JITTER_Y " + yOff );
					v.add( "-setHeader NUSTEP " + microSteps.length );
					v.add( "-setHeader USTEP_I " + ( j + 1 ) );
					v.add( "-setHeader USTEP_X " + MathUtil.round( microSteps[ j ][ 0 ] , 3 ) );
					v.add( "-setHeader USTEP_Y " + MathUtil.round( microSteps[ j ][ 1 ] , 3 ) );
					v.add( "offset " + MathUtil.round( xOff + microSteps[ j ][ 0 ] , 3 ) + " " + MathUtil.round( yOff + microSteps[ j ][ 1 ] , 3 ) ) ;
					
					Enumeration e = this.children();
					while( e.hasMoreElements() )
					{
						SpItem child = ( SpItem ) e.nextElement();
						if( child instanceof SpTranslatable )
						{
							( ( SpTranslatable ) child ).translate( v );
						}
					}
				}
			}
		}
		else
		{
			double j0 , j1 ;
			for( int j = 0 ; j < microSteps.length ; j++ )
			{
				j0 = MathUtil.round( microSteps[ j ][ 0 ] , 3 ) ;
				j1 = MathUtil.round( microSteps[ j ][ 1 ] , 3 ) ;
				v.add( "title jitter 1, ustep " + ( j + 1 ) );
				v.add( "-setHeader NJITTER 1" );
				v.add( "-setHeader JITTER_I 1" );
				v.add( "-setHeader JITTER_X 0.0" );
				v.add( "-setHeader JITTER_Y 0.0" );
				v.add( "-setHeader NUSTEP " + microSteps.length );
				v.add( "-setHeader USTEP_I " + ( j + 1 ) );
				v.add( "-setHeader USTEP_X " + j0 );
				v.add( "-setHeader USTEP_Y " + j1 );
				v.add( "offset " + j0 + " " + j1 ) ;
				
				Enumeration e = this.children();
				while( e.hasMoreElements() )
				{
					SpItem child = ( SpItem ) e.nextElement();
					if( child instanceof SpTranslatable )
					{
						( ( SpTranslatable ) child ).translate( v );
					}
				}
			}
		}
	}

}


