/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA2 ;
import orac.jcmt.iter.SpIterStareObs;
import orac.jcmt.util.ScubaNoise;
import orac.jcmt.util.HeterodyneNoise;
import orac.jcmt.SpJCMTConstants ;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import gemini.util.MathUtil ;

import jsky.app.ot.gui.TextBoxWidgetExt ;
import jsky.app.ot.gui.TextBoxWidgetWatcher ;

/**
 * This is the editor for the Stare Observe Mode iterator component (ACSIS).
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterStareObs extends EdIterJCMTGeneric implements ActionListener , CheckBoxWidgetWatcher , TextBoxWidgetWatcher , SpJCMTConstants
{

  private IterStareObsGUI _w;       // the GUI layout panel

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterStareObs()
	{
		super( new IterStareObsGUI() );

		_title = "Photometry/Stare";
		_presSource = _w = ( IterStareObsGUI ) super._w;
		_description = "Photometry/Stare Observation Mode";
		_w.widePhotom.addActionListener( this );
		_w.contModeCB.addWatcher( this ) ;
		_w.integrationTime.addWatcher( this ) ;
		
		super._w.arrayCentred.addWatcher( this ) ;
	}

    protected void _updateWidgets()
	{
    	if( _iterObs != null )
    	{
    		_w.widePhotom.setSelected( (( SpIterStareObs )_iterObs).getWidePhotom() ) ;
    		SpInstObsComp instrument = SpTreeMan.findInstrument( _iterObs ) ;
    		if( instrument instanceof SpInstHeterodyne )
    		{
    			_w.contModeCB.setSelected( _iterObs.isContinuum() ) ;
    			
    			SpInstHeterodyne heterodyne = ( SpInstHeterodyne )instrument ;
    			SpIterStareObs _iterStareObs = ( SpIterStareObs )_iterObs ;
    			boolean criteria = heterodyne.getFrontEnd().equals( "HARP" ) && _iterObs.getSwitchingMode() == SWITCHING_MODE_POSITION ;
    			super._w.arrayCentredLabel.setVisible( criteria ) ;
    			super._w.arrayCentred.setVisible( criteria ) ;
    			if( criteria )
    				super._w.arrayCentred.setSelected( _iterStareObs.isArrayCentred() ) ;
    			else
    				_iterStareObs.rmArrayCentred() ;
    		}
    		_w.integrationTime.setText( "" + _iterObs.getSampleTime() ) ;
    		_w.secsPerCycle.setText( "" + _iterObs.getSecsPerCycle() ) ;
    	}
		super._updateWidgets();
	}


    public void setInstrument( SpInstObsComp spInstObsComp )
	{
    	if( spInstObsComp == null )
    		return ;
		if( spInstObsComp instanceof SpInstHeterodyne )
		{
			_w.acsisPanel.setVisible( true );
			_w.scuba2Panel.setVisible( false ) ;
			_w.widePhotom.setVisible( false );
			_w.widePhotom.setSelected( false );
			_iterObs.setupForHeterodyne() ;
		}
		else if( spInstObsComp instanceof SpInstSCUBA2 )
		{
			_w.informationPanel.setVisible( false ) ;
			_w.acsisPanel.setVisible( false ) ;
			_w.scuba2Panel.setVisible( true ) ;
			_w.widePhotom.setVisible( false ) ;
			_w.widePhotom.setSelected( false ) ;
			_iterObs.setupForSCUBA2() ;
		}
		else
		{
			_w.acsisPanel.setVisible( false );
			_w.widePhotom.setVisible( true );
		}

    super.setInstrument(spInstObsComp);
  }

  	protected double calculateNoise( int integrations , double wavelength , double nefd , int[] status )
	{
		return ScubaNoise.noise_level( integrations , wavelength , "PHOT" , nefd , status );
	}

    protected double calculateNoise( SpInstHeterodyne inst , double airmass , double tau )
	{
		double tSys = HeterodyneNoise.getTsys( inst.getFrontEnd() , tau , airmass , inst.getRestFrequency( 0 ) / 1.0e9 , inst.getMode().equalsIgnoreCase( "ssb" ) );

		_noiseToolTip = "airmass = " + ( Math.rint( airmass * 10 ) / 10 ) + ", Tsys = " + ( Math.rint( tSys * 10 ) / 10 );
		if( "acsis".equalsIgnoreCase( inst.getBackEnd() ) )
			return MathUtil.round( HeterodyneNoise.getHeterodyneNoise( ( SpIterStareObs ) _iterObs , inst , tau , airmass ) , 3 ) ;
		else
			return -999.9;
	}

    public void actionPerformed( ActionEvent e )
	{
		( ( SpIterStareObs )_iterObs ).setWidePhotom( _w.widePhotom.isSelected() );
	}
    
    public void checkBoxAction( CheckBoxWidgetExt cbwe )
	{
		if( cbwe == _w.contModeCB )
			_iterObs.setContinuumMode( _w.contModeCB.isSelected() );
		else if( cbwe == super._w.arrayCentred )
			( ( SpIterStareObs )_iterObs).setArrayCentred( super._w.arrayCentred.isSelected() ) ;
		super.checkBoxAction( cbwe );
	}
    
    public void textBoxAction( TextBoxWidgetExt tbwe )
    {
    	if( tbwe == _w.secsPerCycle )
    		_iterObs.setSecsPerCycle( _w.secsPerCycle.getText() ) ;
    	else if( tbwe == _w.integrationTime )
    		_iterObs.setSampleTime( _w.integrationTime.getText() ) ;
    }
    
    public void textBoxKeyPress( TextBoxWidgetExt tbwe )
    {
    	if( tbwe == _w.secsPerCycle )
    		_iterObs.setSecsPerCycle( _w.secsPerCycle.getText() ) ;
    	else if( tbwe == _w.integrationTime )
    		_iterObs.setSampleTime( _w.integrationTime.getText() ) ;    	
    }
}
