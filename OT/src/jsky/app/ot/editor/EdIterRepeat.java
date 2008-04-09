// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import gemini.sp.iter.SpIterRepeat;

/**
 * This is the editor for Repeat iterator component.
 *
 * <p>
 * <em>Note</em> there is a bug in this class in that typing a repeat
 * count directly will not trigger an update to the attribute.
 */
public final class EdIterRepeat extends OtItemEditor implements ActionListener
{
	private IterRepeatGUI _w; // the GUI layout panel

	/**
	 * The constructor initializes the title, description, and presentation source.
	 */
	public EdIterRepeat()
	{
		_title = "Repeat Iterator";
		_presSource = _w = new IterRepeatGUI();
		_description = "Repeat exposures or other iterators.";

		// Note: The original bongo code used a SpinBoxWidget, but since Swing doesn't have one, try using a JComboBox instead...
		Object[] ar = new Object[ 99 ];
		for( int i = 0 ; i < 99 ; i++ )
			ar[ i ] = new Integer( i + 1 );
		_w.repeatComboBox.setModel( new DefaultComboBoxModel( ar ) );
		_w.repeatComboBox.addActionListener( this );
	}

	/**
	 * Implements the _updateWidgets method from OtItemEditor in order to
	 * setup the widgets to show the current values of the item.
	 */
	protected void _updateWidgets()
	{
		JComboBox sbw;

		SpIterRepeat iterRepeat = ( SpIterRepeat )_spItem;

		// Repetitions
		sbw = _w.repeatComboBox;
		_w.repeatComboBox.removeActionListener( this );
		sbw.setSelectedItem( new Integer( iterRepeat.getCount() ) );
		_w.repeatComboBox.addActionListener( this );
	}

	/**
	 * Called when the value in the combo box is changed.
	 */
	public void actionPerformed( ActionEvent evt )
	{
		SpIterRepeat iterRepeat = ( SpIterRepeat )_spItem;

		JComboBox sbw = _w.repeatComboBox;
		int i = ( ( Integer )( sbw.getSelectedItem() ) ).intValue();
		iterRepeat.setCount( i );
		_spItem.setTitleAttr( "Repeat " + i + "X" );
	}
}