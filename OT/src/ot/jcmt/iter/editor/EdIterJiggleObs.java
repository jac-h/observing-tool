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
import java.awt.CardLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;
import gemini.sp.obsComp.SpInstObsComp;
import orac.jcmt.inst.SpInstSCUBA;
import orac.jcmt.iter.SpIterJiggleObs;

/**
 * This is the editor for Jiggle Observe Mode iterator component.
 * 
 * @author modified by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterJiggleObs extends EdIterJCMTGeneric {

  private IterJiggleObsGUI _w;       // the GUI layout panel

  private SpIterJiggleObs _iterObs;

  /**
   * The constructor initializes the title, description, and presentation source.
   */
  public EdIterJiggleObs() {
    super(new IterJiggleObsGUI());

    _title       ="Jiggle Iterator";
    _presSource  = _w = (IterJiggleObsGUI)super._w;
    _description ="Jiggle Observation Mode";

    _w.jigglePattern.setChoices(SpIterJiggleObs.JIGGLE_PATTERNS);

    _w.jigglePattern.addWatcher(this);
  }

  /**
   * Override setup to store away a reference to the Focus Iterator.
   */
  public void setup(SpItem spItem) {
    _iterObs = (SpIterJiggleObs) spItem;
    super.setup(spItem);
  }

  protected void _updateWidgets() {
    _w.jigglePattern.setValue(_iterObs.getJigglePattern());

    super._updateWidgets();
  }

  public void dropDownListBoxAction(DropDownListBoxWidgetExt ddlbwe, int index, String val) {
    if(ddlbwe == _w.jigglePattern) {
      _iterObs.setJigglePattern(val);
      return;
    }

    super.dropDownListBoxAction(ddlbwe, index, val);
  }

  public void setInstrument(SpInstObsComp spInstObsComp) {
    super.setInstrument(spInstObsComp);

    if((spInstObsComp != null) && (spInstObsComp instanceof SpInstSCUBA)) {
      //_w.switchingMode.setValue(SWITCHING_MODES[SWITCHING_MODE_CHOP]);
      //((CardLayout)_w.switchingModePanel.getLayout()).show(_w.switchingModePanel, SWITCHING_MODES[SWITCHING_MODE_CHOP]);
      //_w.switchingMode.setEnabled(false);
      _w.acsisPanel.setVisible(false);
    }
    else {
      //_w.switchingMode.setEnabled(true);
      _w.acsisPanel.setVisible(true);
    }
  }
}

