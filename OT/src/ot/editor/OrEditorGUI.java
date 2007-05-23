// $Id$
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 * @version 1.0
 */
package ot.editor;

import java.awt.*;
import javax.swing.*;
import jsky.app.ot.gui.*;

/**
 * Or folder GUI. Modified version of jsky.app.ot.editor.TitleEditorGUI.
 *
 * @see jsky.app.ot.editor.TitleEditorGUI
 * @author Martin Folger (M.Folger@roe.ac.uk),
 *         based on Allan Brighton (jsky/app/ot/editor/TitleEditorGUI.java)
 */
public class OrEditorGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    TextBoxWidgetExt itemTitle = new TextBoxWidgetExt();
  JLabel jLabel2 = new JLabel();
  DropDownListBoxWidgetExt numberOfItems = new DropDownListBoxWidgetExt();
  JLabel jLabel3 = new JLabel();

    public OrEditorGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setForeground(Color.black);
        jLabel1.setText("Title");
        this.setPreferredSize(new Dimension(279, 271));
        this.setLayout(gridBagLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setForeground(Color.black);
    jLabel2.setText("Select");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setForeground(Color.black);
    jLabel3.setText("items");
    this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        this.add(itemTitle, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    this.add(numberOfItems, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
    this.add(jLabel3, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    }
}