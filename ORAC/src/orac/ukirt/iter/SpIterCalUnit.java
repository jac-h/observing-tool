// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package orac.ukirt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import gemini.sp.iter.IterConfigItem;
import gemini.sp.iter.SpIterConfigObs;

/**
 * The CalUnit configuration iterator.
 */
public class SpIterCalUnit extends SpIterConfigObs
{

	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "calUnit" , "Cal Unit (Advanced)" );

	// Register the prototype.
	static
	{
		SpFactory.registerPrototype( new SpIterCalUnit() );
	}

	/**
	 * Default constructor.
	 */
	public SpIterCalUnit()
	{
		super( SP_TYPE );
	}

	/**
	 * Get the name of the item being iterated over.  Subclasses must
	 * define.
	 */
	public String getItemName()
	{
		return "Cal Unit";
	}

	/**
	 * Get the array containing the IterConfigItems offered by the Cal Unit.
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem iciLamps = new IterConfigItem( "Lamp" , SpCalUnitConstants.ATTR_LAMP + "Iter" , SpCalUnitConstants.CGS4_ARC_LAMPS );
		IterConfigItem iciFilters = new IterConfigItem( "Filter" , SpCalUnitConstants.ATTR_FILTER + "Iter" , SpCalUnitConstants.CGS4_FILTERS );
		IterConfigItem iciModes = new IterConfigItem( "Mode" , SpCalUnitConstants.ATTR_MODE + "Iter" , SpCalUnitConstants.MODES );
		IterConfigItem[] iciA = { iciLamps , iciFilters , iciModes , getExposureTimeConfigItem() , getCoaddsConfigItem() };

		return iciA;
	}
}