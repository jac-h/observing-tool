// $Id$

/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton (modified for MicroStep Iterator by Martin Folger)
 * @version 1.0
 */
package ot.jcmt.iter.editor;

import java.awt.GridBagLayout ;
import java.awt.GridBagConstraints ;
import java.awt.Dimension ;
import java.awt.Color ;
import java.awt.Insets ;

import javax.swing.JPanel ;
import javax.swing.JLabel ;

import jsky.app.ot.gui.DropDownListBoxWidgetExt ;

public class IterMicroStepGUI extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JLabel jLabel5 = new JLabel();

	DropDownListBoxWidgetExt microStepPattern = new DropDownListBoxWidgetExt();

	public IterMicroStepGUI()
	{
		try
		{
			jbInit();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		this.setMinimumSize( new Dimension( 280 , 278 ) );
		this.setPreferredSize( new Dimension( 280 , 278 ) );
		this.setLayout( gridBagLayout1 );
		jLabel5.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		jLabel5.setForeground( Color.black );
		jLabel5.setText( "Micro Step Pattern" );
		microStepPattern.setFont( new java.awt.Font( "Dialog" , 0 , 12 ) );
		this.add( jLabel5 , new GridBagConstraints( 0 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.NONE , new Insets( 5 , 5 , 5 , 5 ) , 0 , 0 ) );
		this.add( microStepPattern , new GridBagConstraints( 1 , 0 , 1 , 1 , 0.0 , 0.0 , GridBagConstraints.CENTER , GridBagConstraints.HORIZONTAL , new Insets( 5 , 0 , 5 , 0 ) , 0 , 0 ) );
	}
}