package ot;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import gemini.sp.SpInsertData;
import gemini.sp.SpItem;
import gemini.sp.SpTreeMan;
import jsky.app.ot.util.DnDUtils;
import jsky.util.gui.DialogUtil;

import jsky.app.ot.OtTreeDragSource;
import jsky.app.ot.OtTreeDropTarget;
import jsky.app.ot.OtDragDropObject;
import jsky.app.ot.OtTreeWidget;
import jsky.app.ot.OtTreeNodeWidget;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;


/**
 * Implements "Drop Inside" vs "Drop After".
 * 
 * Implements a Drag and Drop features that was part of the FreeBongo OT but are not
 * part of the normal behaviour of the Swing Drag and Drop:
 * The user can choose whether to drop a source node after (i.e under) or inside the target node.
 * The behaviour is indicated by a white arrow in an orange blob. Pointing downwards for insertion after
 * the target node and pointing right/downwards ("South-East") for insertion inside the target node.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class OtAdvancedTreeDropTarget extends OtTreeDropTarget {
  protected String INSERT_INSIDE_GIF = "ot/images/insert_inside.gif";
  protected String INSERT_AFTER_GIF  = "ot/images/insert_after.gif";
  protected String INSERT_NO_GIF     = "ot/images/insert_no.gif";

  protected Image [] insertImage = new Image[3];

  /**
   * Insert source node inside target node.
   */
  public static final int INSERT_INSIDE = 0;

  /**
   * Insert source node after target node.
   */
  public static final int INSERT_AFTER  = 1;

  /**
   * Do not insert source node.
   *
   * Reject it.
   */
  public static final int INSERT_NO     = 2;

  /**
   * Determines the feedback the user gets during dragging a node over this target node.
   * 
   * Set to one of these values:
   *
   * {@link #INSERT_INSIDE}  "Right/down arrow", will be dropped inside this target node.
   * {@link #INSERT_AFTER}   "Down arrow",       will be dropped after  this target node.
   * {@link #INSERT_NO}      "X",                drop will be rejected.
   */
  protected int currentInsertMode = INSERT_INSIDE;

  /**
   * Offset for switching between inserting inside or after target node.
   *
   * If the mouse is dragged to a position OFFSET_INSERT_INSIDE pixels or more
   * right of the left boundary of the target node then the INSERT_INSIDE_GIF is displayed
   * and the source node is inserted inside the target node.
   * Otherwise the INSERT_AFTER_GIF is displayed and the source node is inserted after the target node.
   */
  protected int OFFSET_INSERT_INSIDE = 40;

  protected Point previousLocation = new Point();
  protected Point location         = new Point();

  public OtAdvancedTreeDropTarget(OtTreeWidget treeWidget) {
    super(treeWidget);

    insertImage[INSERT_INSIDE] = (new ImageIcon(ClassLoader.getSystemClassLoader().getResource(INSERT_INSIDE_GIF))).getImage();
    insertImage[INSERT_AFTER]  = (new ImageIcon(ClassLoader.getSystemClassLoader().getResource(INSERT_AFTER_GIF ))).getImage();
    insertImage[INSERT_NO]     = (new ImageIcon(ClassLoader.getSystemClassLoader().getResource(INSERT_NO_GIF    ))).getImage();
  }

  public void dragExit(DropTargetEvent dte) {
    super.dragExit(dte);
	
    // repaint tree to get rid of insertImage that might be left over.
    tree.repaint();
  }

  public void dragOver(DropTargetDragEvent dtde) {
    boolean okToInsertInside = isAcceptableDropLocation(dtde, INSERT_INSIDE);
    boolean okToInsertAfter  = isAcceptableDropLocation(dtde, INSERT_AFTER);

    location.x = tree.getPathBounds(tree.getPathForLocation(dtde.getLocation().x, dtde.getLocation().y)).x;
    location.y = tree.getPathBounds(tree.getPathForLocation(dtde.getLocation().x, dtde.getLocation().y)).y;
        		
    // If mouse has moved over another node, repaint tree to get rid of orange arrow that
    // might still be on top of a previous node.
    if(previousLocation.x != location.x ||
       previousLocation.y != location.y) {
	  
      tree.repaint();
    }

    if(okToInsertAfter && okToInsertInside) {
      if(dtde.getLocation().x < (location.x + OFFSET_INSERT_INSIDE)) {
        currentInsertMode = INSERT_AFTER;
      }
      else {
        currentInsertMode = INSERT_INSIDE;
      }
    }
    else if(okToInsertAfter) {
      currentInsertMode = INSERT_AFTER;
    }
    else if(okToInsertInside) {
      currentInsertMode = INSERT_INSIDE;
    }
    else {
      currentInsertMode = INSERT_NO;
    }

    dtde.getDropTargetContext().getComponent().getGraphics().drawImage(insertImage[currentInsertMode],
	                                                               location.x,
								       location.y,
								       null);

    previousLocation.x = location.x;
    previousLocation.y = location.y;

    super.dragOver(dtde);
  }

    // This method handles a drop for a list of files
    protected boolean dropNodes(int action, Transferable transferable, Point location) 
	throws IOException, UnsupportedFlavorException {

	OtDragDropObject ddo = (OtDragDropObject)transferable.getTransferData(OtDragDropObject.dataFlavor);

        // MFO: The following 7 lines are retained from ot-0.5.
	OtTreeNodeWidget node = getNode(location);
	if (node == null) 
	    return false;
		
	DnDUtils.debugPrintln((action == DnDConstants.ACTION_COPY ? "Copy" : "Move") +
			      " item " + ddo.getSpItem() +
			      " to targetNode " +node);

	// Highlight the drop location while we perform the drop
	_spTree.setIgnoreSelection(true);
	_spTree.selectNode(getNode(location));
	_spTree.setIgnoreSelection(false);


        // MFO: The following 32 lines are retained from ot-0.5.
	//      (except: try/catch and _spTree.setProgram(prog); from ot-0.9.0)

	// Copy source object to the target
	SpInsertData spID = getSpInsertData(ddo, node, currentInsertMode);
	if (spID == null) 
	    return false;
 
	OtTreeWidget ownerTW = ddo.getOwner();
	SpItem spItem = ddo.getSpItem();
	SpItem[] newItems = spID.items;

	try {
	  if (ownerTW == _spTree) {
	    // The dragged item was owned by this tree, so move or copy according to action.
	    if(action == DnDConstants.ACTION_COPY) {
              // Copying items      
	      for (int i=0; i<newItems.length; ++i) {
	        newItems[i] = newItems[i].deepCopy();
	      }
	      newItems = _spTree.addItems( spID );
	    }
	    else {
	      // Moving items
	      _spTree.mvItems( spID );
	    }
	  } else {
	    if (ownerTW != null) {
		// Make a copy, since these items are owned by another tree.
		for (int i=0; i<newItems.length; ++i) {
		    newItems[i] = newItems[i].deepCopy();
		}
	    }
	    newItems = _spTree.addItems( spID );
	  }
          // force redrawing of the tree
	  _spTree.resetProg(_spTree.getProg());
	}
	catch(Exception e) {
          DialogUtil.error(e);
	}
	
	if (newItems == null) 
	    return false;
   	return true;

    }



  protected boolean isAcceptableDropLocation(DropTargetDragEvent dtde, int insertMode) {
    OtDragDropObject ddo = OtTreeDragSource._dragObject;
    if (ddo == null)
      return false;

    // get the node under the mouse
    OtTreeNodeWidget node = getNode(dtde.getLocation());
    if (node == null) 
      return false;
	
    return (getSpInsertData(ddo, node, insertMode) != null);
  }


  /**
   *
   * @param insertMode set to INSERT_INSIDE or to INSERT_AFTER
   */
  protected SpInsertData getSpInsertData(OtDragDropObject ddo, OtTreeNodeWidget node, int insertMode) {
    // See whether we can insert the newItem.
    SpItem[] newItems = ddo.getSpItems();
    SpItem spItem = node.getItem();
    
    SpInsertData spID = null;
	
    if(insertMode == INSERT_AFTER) {
      spID = SpTreeMan.evalInsertAfter(newItems, spItem);
    }
    else if(insertMode == INSERT_INSIDE) {
      spID = SpTreeMan.evalInsertInside(newItems, spItem);
    }

    return spID;
  }
}

