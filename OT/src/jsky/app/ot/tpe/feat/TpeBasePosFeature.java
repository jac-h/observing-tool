// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe.feat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import jsky.app.ot.fits.gui.FitsImageInfo;
import jsky.app.ot.fits.gui.FitsPosMapEntry;
import jsky.app.ot.fits.gui.FitsMouseEvent;

import gemini.sp.SpTelescopePos;

import jsky.app.ot.OtCfg;
import jsky.app.ot.tpe.TpeImageWidget;
import jsky.app.ot.tpe.TpePositionMap;
import java.awt.geom.Point2D;


public class TpeBasePosFeature extends TpePositionFeature {

    /**
     * Construct the feature with its name and description. 
     */
    public TpeBasePosFeature() {
	super("Base", "Location of the base position.");
    }

    public void	reinit(TpeImageWidget iw, FitsImageInfo fii) {
	super.reinit(iw, fii);

	// Tell the position map that the base position is visible.
	TpePositionMap pm = TpePositionMap.getMap(iw);
	pm.setFindBase(true);
    }

    public void	unloaded() {
	// Tell the position map that the base position is no longer visible.
	TpePositionMap pm = TpePositionMap.getExistingMap(_iw);
	if (pm != null) pm.setFindBase(false);

	super.unloaded();
    }

    /**
     */
    public boolean erase(FitsMouseEvent fme) {
	// You can't erase the base positon
	return false;
    }

    /**
     * @see jsky.app.ot.tpe.TpeSelectableFeature
     */
    public Object select(FitsMouseEvent fme)  {
	TpePositionMap pm = TpePositionMap.getMap(_iw);
 
	int x = fme.xWidget;
	int y = fme.yWidget;
 
	FitsPosMapEntry pme;
	pme = pm.getPositionMapEntry( SpTelescopePos.BASE_TAG );
	if ((pme != null) && (positionIsClose( pme, x, y ))) {
	    pme.telescopePos.select();
	    return pme.telescopePos;
	}
	return null;
    }

    /**
     */
    public void draw(Graphics g, FitsImageInfo fii) {
	TpePositionMap pm = TpePositionMap.getMap(_iw);

	Point2D.Double base = pm.getLocationFromTag( SpTelescopePos.BASE_TAG );
	if (base == null) {
	    return;
	}

	int r = 5;
	int d = 2*r;

	// Draw crosshairs
	g.setColor(Color.yellow);
	g.drawOval((int)(base.x - r), (int)(base.y - r), d, d);
	g.drawLine((int)base.x, (int)(base.y - r), (int)base.x, (int)(base.y + r));
	g.drawLine((int)(base.x - r), (int)base.y, (int)(base.x + r), (int)base.y);
    }

    /**
     */
    public boolean dragStart(FitsMouseEvent fme, FitsImageInfo fii) {
	TpePositionMap      pm  = TpePositionMap.getMap(_iw);
	FitsPosMapEntry pme = pm.getPositionMapEntry( SpTelescopePos.BASE_TAG );
	if (positionIsClose( pme, fme.xWidget, fme.yWidget )) {
	    _dragObject = pme;
	    return true;
	}
	return false;
    }

    /**
     */
    public void drag(FitsMouseEvent fme) {
	if (_dragObject != null) {
	    _dragObject.screenPos.x = fme.xWidget;
	    _dragObject.screenPos.y = fme.yWidget;
 
	    SpTelescopePos tp = (SpTelescopePos) _dragObject.telescopePos;
	    //tp.noNotifySetXY(fme.ra, fme.dec);
	    tp.setXY(fme.ra, fme.dec);
	}
    }


    /**
     * Get the feature's name.
     */
    public String getName() {
	try {
	    return OtCfg.telescopeUtil.getBaseTag();
	}
	catch(Exception e) {
            return super.getName();
	}
    }
}

