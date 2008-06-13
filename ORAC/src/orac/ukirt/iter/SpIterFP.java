/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.ukirt.iter ;

import gemini.sp.SpFactory ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpType ;
import gemini.sp.iter.IterConfigItem ;

import gemini.util.TranslationUtils ;

import java.util.Vector ;
import java.util.List ;
import java.util.Enumeration ;

/**
 * The FP configuration iterator.
 */
public class SpIterFP extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "instFP" , "FP" ) ;

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterFP() ) ;
	}

	/**
	 * Default constructor.
	 */
	public SpIterFP()
	{
		super( SP_TYPE ) ;
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "FP" ;
	}

	/**
	 * Override adding a configuration item to use the no default version.
	 */
	public void addConfigItem( IterConfigItem ici , int size )
	{
		super.addConfigItemNoDef( ici , size ) ;

		// Then set a default value
		setConfigStep( ici.attribute , "300" , 0 ) ;
	}

	/**
	 * Get the array containing the IterConfigItems offered by FP.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem iciFPX = new IterConfigItem( "FPXPos" , "FPX" , null ) ;
		IterConfigItem iciFPY = new IterConfigItem( "FPYPos" , "FPY" , null ) ;
		IterConfigItem iciFPZ = new IterConfigItem( "FPZPos" , "FPZ" , null ) ;
		IterConfigItem[] iciA = { iciFPX , iciFPY , iciFPZ } ;

		return iciA ;
	}

	public void translateProlog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translateEpilog( Vector<String> sequence ) throws SpTranslationNotSupportedException{}
	
	public void translate( Vector<String> v ) throws SpTranslationNotSupportedException
	{
		List l = getConfigAttribs() ;
		if( l != null && l.size() != 0 )
		{
			List vals = getConfigSteps( ( String )l.get( 0 ) ) ;
			for( int i = 0 ; i < vals.size() ; i++ )
			{
				for( int j = 0 ; j < l.size() ; j++ )
					v.add( ( String )l.get( j ) + " " + ( String )getConfigSteps( ( String )l.get( j ) ).get( i ) ) ;
				// Now loop through all the child elements
				Enumeration e = this.children() ;
				TranslationUtils.recurse( e , v ) ;
			}
		}
	}
}
