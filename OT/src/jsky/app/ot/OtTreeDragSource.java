/*
 * Copyright 2000 Association for Universities for Research in Astronomy, Inc.,
 * Observatory Control System, Gemini Telescopes Project.
 *
 * $Id$
 */

package jsky.app.ot;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import jsky.app.ot.util.DnDUtils;
import gemini.sp.SpItem;

/**
 * Drag&Drop source for tree widgets.
 * Based on an example in the book: "Core Swing, Advanced Programming".
 * 
 * MFO: This class has been updated by copying jsky.app.ot.viewer.SPTreeDragSource from ot-0.9.0
 *      to this class and making modifications.

 * @author Allan Brighton
 */
public class OtTreeDragSource implements DragGestureListener, DragSourceListener {
    
    // Use the default DragSource
    protected DragSource _dragSource = DragSource.getDefaultDragSource();

    /** Target tree widget */
    protected OtTreeWidget _spTree;

    /** The internal JTree widget */
    protected JTree _tree;

    /** Saved reference to drag object, for use in OtTreeDropTarget */
    public static OtDragDropObject _dragObject;

    /**
     * Constructor
     */
    public OtTreeDragSource(OtTreeWidget spTree) {
	_spTree = spTree;
	_tree = _spTree.getTree();

	// Create a DragGestureRecognizer and register as the listener
	_dragSource.createDefaultDragGestureRecognizer(_tree, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    /** Implementation of DragGestureListener interface. */
    public void dragGestureRecognized(DragGestureEvent dge) {
	// don't conflict with the popup menus
	InputEvent e = dge.getTriggerEvent();
	if (e instanceof MouseEvent && ((MouseEvent)e).isPopupTrigger())
	    return;
	
	// Get the mouse location and convert it to
	// a location within the tree.
	Point location = dge.getDragOrigin();
	TreePath dragPath = _tree.getPathForLocation(location.x, location.y);
	if (dragPath != null && _tree.isPathSelected(dragPath)) {
    // MFO ot-0.9.0 (with possibly some changes)
    /*
	
	    // Get the list of selected nodes and create a Transferable
	    ISPRemoteNode [] nodes = _spTree.getSelectedNodes();
	    if (nodes != null && nodes.length > 0) {
		_dragObject = new OtDragDropObject(nodes, _spTree);
		try {dge.startDrag(null, _dragObject, this);} catch(Exception ex) {
		    DnDUtils.debugPrintln("OtTreeDragSource.dragGestureRecognized: " + ex);
		}
	    }
	}
     */
    // MFO ot-0.5

	    // Get the list of selected nodes and create a Transferable
	    // The list of nodes is saved for use when the drop completes.
	    TreePath [] paths = _spTree.getSelectionPaths();
	    if (paths != null && paths.length > 0) {
		SpItem[] spItems = new SpItem[paths.length];
		for (int i = 0; i < paths.length; i++) {
		    OtTreeNodeWidget node = (OtTreeNodeWidget)paths[i].getLastPathComponent();
		    spItems[i] = node.getItem();
		}

	        if (spItems != null && spItems.length > 0) {
		    _dragObject = new OtDragDropObject(spItems, _spTree);
		    try {dge.startDrag(DragSource.DefaultMoveNoDrop, _dragObject, this);} catch(Exception ex) {
		      DnDUtils.debugPrintln("OtTreeDragSource.dragGestureRecognized: " + ex);
		    }
	        }
	    }
	}

    
    }

    // Implementation of DragSourceListener interface
    public void dragEnter(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dragEnter, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragOver(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dragOver, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragExit(DragSourceEvent dse) {
	DnDUtils.debugPrintln("Drag Source: dragExit");
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: dropActionChanged, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction()));
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
	DnDUtils.debugPrintln("Drag Source: drop completed, drop action = " 
			      + DnDUtils.showActions(dsde.getDropAction())
			      + ", success: " + dsde.getDropSuccess());
    }

}
