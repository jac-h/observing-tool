/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.tpe;

import gemini.sp.SpItem;
import gemini.sp.iter.SpIterChop;
import orac.jcmt.SpJCMTConstants;
import orac.jcmt.iter.SpIterJiggleObs;
import orac.jcmt.iter.SpIterRasterObs;
import jsky.app.ot.tpe.feat.TpeChopFeature;
import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Draws the Chop position for JCMT.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class TpeJcmtChopFeature extends TpeChopFeature {

/**
 * Draw the feature.
 */
public void draw(Graphics g, FitsImageInfo fii) {
  if (!_calc(fii)) {
    return;
  }

  if(containsJiggleObservation(_iterChop)) {
    // Need the chop iterator to know what to draw.
    SpIterChop spIterChop = (SpIterChop)_iw.getBaseItem();
    if ((spIterChop == null) || (spIterChop.getSelectedIndex() < 0)) {
      return;
    }

    int chopStepIndex = spIterChop.getSelectedIndex();

    // Opposite chop position specified by angle and -throw.
    double chop2X =
      spIterChop.getThrow(chopStepIndex) * Math.sin((spIterChop.getAngle(chopStepIndex) / 360) * (Math.PI * 2.0));
    double chop2Y =
      spIterChop.getThrow(chopStepIndex) * Math.cos((spIterChop.getAngle(chopStepIndex) / 360) * (Math.PI * 2.0));

    chop2X *= fii.pixelsPerArcsec;
    chop2Y *= fii.pixelsPerArcsec;

    chop2X += _baseX;
    chop2Y += _baseY;


    g.setColor(Color.magenta);

    double [] scienceArea = null;

    // There is an instrument in scope then draw the science area around the
    // base position and chop positions.
    if((_iw.getInstrumentItem() != null) &&
      ((scienceArea = _iw.getInstrumentItem().getScienceArea()) != null)) {

      // ScienceArea is circular (approximately circular detector/array as opposed to
      // circular representation of science area due to Ax/El mounting)
      if(scienceArea.length == 1) {
        double radius = scienceArea[0] * fii.pixelsPerArcsec;
        g.drawArc((int)(_baseX - radius), (int)(_baseY - radius),
                  (int)(2 * radius), (int)(2 * radius), 0, 360);

        g.drawArc((int)(_chopX - radius), (int)(_chopY - radius),
                  (int)(2 * radius), (int)(2 * radius), 0, 360);


        g.drawArc((int)(chop2X - radius), (int)(chop2Y - radius),
                  (int)(2 * radius), (int)(2 * radius), 0, 360);

      }

      // Recangular Science Area.
      if(scienceArea.length == 2) {

        // Draw rectangular Science Area as circle because of Az/El coordinate system.
        if(_drawAsCircle) {
          double radius = Math.sqrt((scienceArea[0] * scienceArea[0]) +
                                 (scienceArea[1] * scienceArea[1]));

          radius *= fii.pixelsPerArcsec;

          g.drawArc((int)(_baseX - radius), (int)(_baseY - radius),
                    (int)(2 * radius), (int)(2 * radius), 0, 360);

          g.drawArc((int)(_chopX - radius), (int)(_chopY - radius),
                    (int)(2 * radius), (int)(2 * radius), 0, 360);

          g.drawArc((int)(chop2X - radius), (int)(chop2Y - radius),
                    (int)(2 * radius), (int)(2 * radius), 0, 360);
        }
        // Draw rectangular Science Area as rectangle.
        else {
          double w = scienceArea[0] * fii.pixelsPerArcsec;
	  double h = scienceArea[1] * fii.pixelsPerArcsec;

          g.drawRect((int)(_baseX - (w / 2.0)),
                     (int)(_baseY - (h / 2.0)),
                     (int)w,
                     (int)h);

          g.drawRect((int)(_chopX - (w / 2.0)),
                     (int)(_chopY - (h / 2.0)),
                     (int)w,
                     (int)h);

          g.drawRect((int)(chop2X - (w / 2.0)),
                     (int)(chop2Y - (h / 2.0)),
                     (int)w,
                     (int)h);
        }
      }
    }

    // Draw the actual chop position (at the centre of the science area) as a small box.
    // This box is used for dragging the position with the mouse.
    g.drawRect((int)_chopX - 2, (int)_chopY - 2, 4, 4);

//   g.setFont(FONT);
    g.drawString(_name + " (Step " + _iterChop.getSelectedIndex() + ")", (int)_chopX + 3, (int)_chopY + 2);

    // If the exact position is not known due to the Az/El coordinate system
    // then draw two circles around the base. The science area is between these two circles.
    if(_drawAsCircle) {
      g.drawArc((int)(_baseX - _chopInnerRadius), (int)(_baseY - _chopInnerRadius),
                (int)(2 * _chopInnerRadius), (int)(2 * _chopInnerRadius), 0, 360);

      g.drawArc((int)(_baseX - _chopOuterRadius), (int)(_baseY - _chopOuterRadius),
                (int)(2 * _chopOuterRadius), (int)(2 * _chopOuterRadius), 0, 360);
    }

    return;
  }

  if(containsRasterObservation(_iterChop)) {
    // Need the chop iterator to know what to draw.
    SpIterChop spIterChop = (SpIterChop)_iw.getBaseItem();
    if ((spIterChop == null) || (spIterChop.getSelectedIndex() < 0)) {
      return;
    }

    int chopStepIndex = spIterChop.getSelectedIndex();

    // Chop position specified by angle and 0.5 * throw.
    double chop1X =
      spIterChop.getThrow(chopStepIndex) * Math.sin(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));
    double chop1Y =
      spIterChop.getThrow(chopStepIndex) * Math.cos(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));

    chop1X /= 2.0;
    chop1Y /= 2.0;

    chop1X *= fii.pixelsPerArcsec;
    chop1Y *= fii.pixelsPerArcsec;

    chop1X += _baseX;
    chop1Y += _baseY;

    // Opposite chop position specified by angle and -0.5 * throw.
    double chop2X =
      spIterChop.getThrow(chopStepIndex) * Math.sin(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));
    double chop2Y =
      spIterChop.getThrow(chopStepIndex) * Math.cos(((180 + spIterChop.getAngle(chopStepIndex)) / 360) * (Math.PI * 2.0));

    chop2X /= -2.0;
    chop2Y /= -2.0;

    chop2X *= fii.pixelsPerArcsec;
    chop2Y *= fii.pixelsPerArcsec;

    chop2X += _baseX;
    chop2Y += _baseY;

    g.setColor(Color.magenta);

    double [] scienceArea = null;

    // There is an instrument in scope then draw the science area around the
    // chop positions.
    if((_iw.getInstrumentItem() != null) &&
      ((scienceArea = _iw.getInstrumentItem().getScienceArea()) != null)) {

      // ScienceArea is circular (approximately circular detector/array as opposed to
      // circular representation of science area due to Az/El mounting)
      if(scienceArea.length == 1) {
        double radius = scienceArea[0] * fii.pixelsPerArcsec;

        g.drawArc((int)(chop1X - radius), (int)(chop1Y - radius),
                  (int)(2 * radius), (int)(2 * radius), 0, 360);

        g.drawArc((int)(chop2X - radius), (int)(chop2Y - radius),
                  (int)(2 * radius), (int)(2 * radius), 0, 360);

      }

      // Recangular Science Area.
      if(scienceArea.length == 2) {

        // Draw rectangular Science Area as circle because of Az/El coordinate system.
        if(_drawAsCircle) {
          double radius = Math.sqrt((scienceArea[0] * scienceArea[0]) +
                                    (scienceArea[1] * scienceArea[1]));

          radius *= fii.pixelsPerArcsec;

          g.drawArc((int)(chop1X - radius), (int)(chop1Y - radius),
                    (int)(2 * radius), (int)(2 * radius), 0, 360);

          g.drawArc((int)(chop2X - radius), (int)(chop2Y - radius),
                    (int)(2 * radius), (int)(2 * radius), 0, 360);
        }
        // Draw rectangular Science Area as rectangle.
        else {
          double w = scienceArea[0] * fii.pixelsPerArcsec;
	  double h = scienceArea[1] * fii.pixelsPerArcsec;

          g.drawRect((int)(chop1X - (w / 2.0)),
                     (int)(chop1Y - (h / 2.0)),
                     (int)w,
                     (int)h);

          g.drawRect((int)(chop2X - (w / 2.0)),
                     (int)(chop2Y - (h / 2.0)),
                     (int)w,
                     (int)h);
        }
      }
    }

    // Draw the actual chop position (at the centre of the science area) as a small box.
    // This box is used for dragging the position with the mouse.
    g.drawRect((int)chop1X - 2, (int)chop1Y - 2, 4, 4);

//   g.setFont(FONT);
    g.drawString(_name + " (Step " + _iterChop.getSelectedIndex() + ")", (int)chop1X + 3, (int)chop1Y + 2);

    // If the exact position is not known due to the Az/El coordinate system
    // then draw two circles around the base. The science area is between these two circles.
    if(_drawAsCircle) {
      g.drawArc((int)(_baseX - (_chopInnerRadius / 2.0)), (int)(_baseY - (_chopInnerRadius / 2.0)),
                (int)_chopInnerRadius, (int)_chopInnerRadius, 0, 360);

      g.drawArc((int)(_baseX - (_chopOuterRadius / 2.0)), (int)(_baseY - (_chopOuterRadius / 2.0)),
                (int)_chopOuterRadius, (int)_chopOuterRadius, 0, 360);
    }

    return;
  }

  super.draw(g, fii);
}


/**
 * Drag to a new location.
 */
public void drag(FitsMouseEvent fme)
{
   if (_dragObject != null) {
      _dragX = fme.xWidget;
      _dragY = fme.yWidget;

      _iterChop.setAngle(getAngle(fme.xOffset, fme.yOffset), _iterChop.getSelectedIndex());

      if(containsRasterObservation(_iterChop)) {
        _iterChop.setThrow(getThrow(fme.xOffset, fme.yOffset) * 2.0, _iterChop.getSelectedIndex());
      }
      else {
        _iterChop.setThrow(getThrow(fme.xOffset, fme.yOffset), _iterChop.getSelectedIndex());
      }
   }

   _iw.repaint();

}


  /**
   * Checks whether there is a Jiggle Observation inside this Chop iterator.
   */
  protected static boolean containsJiggleObservation(SpIterChop spIterChop) {
    SpItem child = spIterChop.child();
    while(child != null) {
      if(child instanceof SpIterJiggleObs) {
        return true;
      }

      child = child.next();
    }

    return false;
  }

  /**
   * Checks whether there is a Scan/Raster Observation inside this Chop iterator.
   */
  protected static boolean containsRasterObservation(SpIterChop spIterChop) {
    SpItem child = spIterChop.child();
    while(child != null) {
      if(child instanceof SpIterRasterObs) {
        return true;
      }

      child = child.next();
    }

    return false;
  }



  /**
   * Return true if the chop position should be drawn as circle
   * given the coordFrame.
   */
  protected boolean drawAsCircle(String coordFrame) {
    if(coordFrame.equals(SpJCMTConstants.CHOP_SYSTEMS[SpJCMTConstants.CHOP_SYSTEM_TARCKING])) {
      return false;
    }
    else {
      return super.drawAsCircle(coordFrame);
    }
  }
}

