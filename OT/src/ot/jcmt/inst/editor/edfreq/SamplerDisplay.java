/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.edfreq;

import javax.swing.*;

/**
 * @author Dennis Kelly ( bdk@roe.ac.uk )
 */
public class SamplerDisplay extends JLabel implements SamplerWatcher
{

   public SamplerDisplay ( String text )
   {
      super ( text );
   }

   public void updateSamplerValues ( double centre, double width,
     int channels )
   {
      setText ( String.valueOf ( centre ) );
   }
}
