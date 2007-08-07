// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.tpe;

import java.awt.Component ;
import java.awt.Graphics2D ;
import java.awt.Color ;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.JDesktopPane;
import jsky.app.jskycat.JSkyCat;
import jsky.app.ot.fits.gui.FitsImageWidget;
import jsky.app.ot.fits.gui.FitsMouseEvent;
import jsky.app.ot.gui.DrawUtil;
import gemini.util.CoordSys;
import gemini.sp.SpBasePosObserver;
import gemini.sp.SpPosAngleObserver;
import gemini.sp.SpItem;
import gemini.sp.SpObsData;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.util.RADecMath;
import jsky.coords.wcscon;

/**
 * This class is concerned with drawing targets, WFS etc., on a DSS image.
 */
public class TpeImageWidget extends FitsImageWidget implements SpBasePosObserver , SpPosAngleObserver
{
	private SpItem _baseItem;
	private SpObsData _obsData;
	private Vector _featureList = new Vector();
	private TpeDraggableFeature _dragObject;
	private double _ra = 0. ; // RA of base position
	private double _dec = 0. ; // Dec of base position
	private double _posAngle = 0. ; // Rotator position angle
	private boolean _baseOutOfView = false; // Base pos not visible
	
	/** Used in {@link #telescopePosToImageWidget(gemini.util.TelescopePos)}. */
	private Point2D.Double _convertedPosition = new Point2D.Double();

	public TpeImageWidget( Component parent )
	{
		super( parent );
	}

	/** 
	 * Open up another window like this one and return a reference to it.
	 * <p>
	 * Note: derived classes should redefine this to return an instance of the
	 * correct class, which should be derived JFrame or JInternalFrame.
	 */
	public Component newWindow()
	{
		return new TpeImageDisplayFrame();
	}

	/** 
	 * Return true if the image has been cleared.
	 * (Overrides parent class version to stop the table plotting code from
	 * generating new blank images when plotting tables, since the blank
	 * images are generated by OT code).
	 */
	public boolean isClear()
	{
		return false;
	}

	/**
	 * Free the resources used by this ImageWidget, making it quit observing
	 * anything and no longer have any features.
	 */
	public void free()
	{
		if( _obsData != null )
		{
			_obsData.deleteBasePosObserver( this );
			_obsData.deletePosAngleObserver( this );
		}

		for( int i = _featureList.size() - 1 ; i >= 0 ; --i )
		{
			TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
			_featureList.removeElement( tif );
			tif.unloaded();
		}

		_featureList = null;
		_baseItem = null;
		_obsData = null;

		super.free();
	}

	/**
	 * Reset internal state to view a new position table.
	 */
	public void reset( SpItem spItem )
	{
		SpObsData od = spItem.getObsData();
		if( ( _obsData != null ) && ( od != _obsData ) )
		{
			_obsData.deleteBasePosObserver( this );
			_obsData.deletePosAngleObserver( this );
		}

		_baseItem = spItem;
		_obsData = od;

		if( _obsData != null )
		{
			_obsData.addBasePosObserver( this );
			_obsData.addPosAngleObserver( this );

			setBasePos( od.getXaxis() , od.getYaxis() , od.getCoordSys() );
			setPosAngle( od.getPosAngle() );
		}
		repaint();
	}

	/**
	 * Get the base item.
	 */
	public SpItem getBaseItem()
	{
		return _baseItem;
	}

	/**
	 * Get the telescope item in the scope of the base item.
	 */
	public SpItem getTelescopeItem()
	{
		return SpTreeMan.findTargetList( _baseItem );
	}

	/**
	 * Get the instrument item in the scope of the base item.
	 */
	public SpInstObsComp getInstrumentItem()
	{
		return ( SpInstObsComp )SpTreeMan.findInstrument( _baseItem );
	}

	/**
	 * Get the pixel dimensions of the science area for the instrument
	 * in the current item's scope.
	 */
	public TpeSciArea getSciArea()
	{
		if( !_imgInfoValid )
			return null;

		SpInstObsComp spInst = getInstrumentItem();
		if( spInst == null )
			return null;

		TpeSciArea tsa = new TpeSciArea();
		tsa.update( spInst , _imgInfo );
		return tsa;
	}

	/**
	 */
	public void addFeature( TpeImageFeature tif )
	{
		if( !featureAdded( tif ) )
		{
			_featureList.addElement( tif );
			if( _imgInfoValid )
				tif.reinit( this , _imgInfo );

			repaint();
		}
	}

	/**
	 */
	public final boolean featureAdded( TpeImageFeature tif )
	{
		return _featureList.contains( tif );
	}

	/**
	 */
	public void deleteFeature( TpeImageFeature tif )
	{
		if( featureAdded( tif ) )
		{
			_featureList.removeElement( tif );
			tif.unloaded();
			repaint();
		}
	}

	/**
	 */
	public void dragStart( FitsMouseEvent evt )
	{
		if( !_imgInfoValid )
		{
			for( int i = 0 ; i < _featureList.size() ; ++i )
			{
				TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
				if( tif instanceof TpeDraggableFeature )
				{
					TpeDraggableFeature tdf = ( TpeDraggableFeature )tif;
					if( tdf.dragStart( evt , _imgInfo ) )
					{
						_dragObject = tdf;
						drag( evt );
						return;
					}
				}
			}
		}
	}

	/**
	 */
	public void drag( FitsMouseEvent evt )
	{
		if( _dragObject != null )
			_dragObject.drag( evt );
	}

	/**
	 */
	public void dragStop( FitsMouseEvent evt )
	{
		if( _dragObject != null )
		{
			_dragObject.dragStop( evt );
			_dragObject = null;
		}
	}

	/**
	 */
	public boolean create( FitsMouseEvent fme , TpeImageFeature tif , String label )
	{
		if( !_imgInfoValid )
			return false;

		if( !_featureList.contains( tif ) )
			return false;

		if( !( tif instanceof TpeCreateableFeature ) )
			return false;

		TpeCreateableFeature tcf = ( TpeCreateableFeature )tif;
		return tcf.create( fme , _imgInfo , label );
	}

	/**
	 */
	public boolean erase( FitsMouseEvent fme )
	{
		if( !_imgInfoValid )
			return false;

		for( int i = 0 ; i < _featureList.size() ; ++i )
		{
			TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
			if( tif instanceof TpeEraseableFeature )
			{
				TpeEraseableFeature tef = ( TpeEraseableFeature )tif;
				if( tef.erase( fme ) )
					return true;
			}
		}
		return false;
	}

	/**
	 */
	public Object select( FitsMouseEvent fme )
	{
		if( !_imgInfoValid )
			return null;

		for( int i = 0 ; i < _featureList.size() ; ++i )
		{
			TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
			if( tif instanceof TpeSelectableFeature )
			{
				TpeSelectableFeature tsf = ( TpeSelectableFeature )tif;
				Object o = tsf.select( fme );
				if( o != null )
					return o;
			}
		}
		return null;
	}

	/**
	 * The Base position has been updated.
	 */
	public void basePosUpdate( double x , double y , double xoff , double yoff , int coordSys )
	{
		setBasePos( x , y , xoff , yoff , coordSys );
		repaint();
	}

	/**
	 * The position angle has been updated.
	 */
	public void posAngleUpdate( double posAngle )
	{
		setPosAngle( posAngle );
		repaint();
	}

	/**
	 */
	public void repaint( TpeImageFeature tif )
	{
		repaint();
	}

	public boolean setPosAngle( double posAngle )
	{
		_posAngle = posAngle;

		if( super.setPosAngle( _posAngle ) )
		{
			for( int i = 0 ; i < _featureList.size() ; ++i )
			{
				TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
				tif.posAngleUpdate( _imgInfo );
			}
			return true;
		}
		return false;
	}

	/**
	 * Interprets coordinates as FK5 and sets base position.
	 *
	 * @see #setBasePos(double,double,int)
	 */
	public boolean setBasePos( double x , double y )
	{
		return setBasePos( x , y , CoordSys.FK5 );
	}

	public boolean setBasePos( double x , double y , int coordSys )
	{
		return setBasePos( x , y , 0.0 , 0.0 , coordSys );
	}

	/**
	 * Converts coordinates to FK5 and sets base position.
	 */
	public boolean setBasePos( double x , double y , double xoff , double yoff , int coordSys )
	{
		_convertedPosition.x = RADecMath.getAbsolute( x , y , xoff , yoff )[ 0 ];
		_convertedPosition.y = RADecMath.getAbsolute( x , y , xoff , yoff )[ 1 ];

		switch( coordSys )
		{
			case CoordSys.FK4 :
				wcscon.fk425( _convertedPosition );
				break;
			case CoordSys.GAL :
				wcscon.gal2fk5( _convertedPosition );
				break;
		}

		_ra = _convertedPosition.x;
		_dec = _convertedPosition.y;

		if( !super.setBasePos( _ra , _dec ) )
			return false;

		for( int i = 0 ; i < _featureList.size() ; ++i )
		{
			TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
			tif.reinit( this , _imgInfo );
		}

		_baseOutOfView = false;
		if( _imgInfoValid )
		{
			Point2D.Double p = _imgInfo.baseScreenPos;
			if( ( p.x < 0 ) || ( p.y < 0 ) || ( p.x >= getWidth() ) || ( p.y >= getHeight() ) )
				_baseOutOfView = true;
		}
		return true;
	}

	/** Return the JDesktopPane, if using internal frames, otherwise null */
	public JDesktopPane getDesktop()
	{
		return JSkyCat.getDesktop();
	}

	/** Return true if this is the main application window (enables exit menu item) */
	public boolean isMainWindow()
	{
		return false;
	}

	/**
	 * Overrides the base class version to add the OT graphics.
	 * 
	 * @param g the graphics context
	 * @param region if not null, the region to paint
	 */
	public synchronized void paintLayer( Graphics2D g , Rectangle2D region )
	{
		super.paintLayer( g , region );

		if( !_imgInfoValid )
		{
			if( !setBasePos( _ra , _dec ) )
				return;
			setPosAngle( _posAngle );
		}

		for( int i = 0 ; i < _featureList.size() ; ++i )
		{
			TpeImageFeature tif = ( TpeImageFeature )_featureList.elementAt( i );
			tif.draw( g , _imgInfo );
		}

		if( _unsupportedProjection )
		{
			g.setFont( TpeImageFeature.FONT );
			String s = "Unsupported coordinate system.";
			DrawUtil.drawString( g , s , Color.yellow , Color.black , 10 , 10 );

		}
		else if( _baseOutOfView )
		{
			g.setFont( TpeImageFeature.FONT );
			String s = "Base position is out of view.";
			DrawUtil.drawString( g , s , Color.yellow , Color.black , 10 , 10 );
		}
	}
}
