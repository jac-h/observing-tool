/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Point;
import java.util.*;
import java.io.*;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;


/**
 * @author Dennis Kelly ( bdk@roe.ac.uk ), modified by Martin Folger (M.Folger@roe.ac.uk)
 */
public class FrontEnd extends JPanel implements ActionListener, FrequencyEditorConstants,
                                                DocumentListener
{

   private JComboBox feChoice;
   private JComboBox feBandModeChoice;
   private String currentFE = "";
   private JPanel fePanel;
   private JPanel displayPanel;
   private JPanel rangePanel;
   private Box linePanel;
   private JPanel mol1Panel;
   private JPanel mol2Panel;
   private JPanel northPanel;
   private JLabel lowFreq;
   private JLabel highFreq;
   private JTextField velocity;
   private JTextField overlap;
   private SideBandDisplay sideBandDisplay = new SideBandDisplay(this);
   private LineCatalog lineCatalog = new LineCatalog();
   private JComboBox feBand;
   private JComboBox feMode;
   private JComboBox moleculeChoice;
   private JComboBox moleculeChoice2;
   private JTextField moleculeFrequency;
   private JTextField moleculeFrequency2;
   private JComboBox transitionChoice;
   private JComboBox transitionChoice2;
   private JComboBox bandWidthChoice;
   private JScrollPane scrollPanel;
   private double redshift = 0.0;
   private double subBandWidth = 0.25E9;
   private double loMin;
   private double loMax;
   private double feIF;
   private double feBandWidth;
   private double feOverlap = 0.0;
   private String defaultStoreFile = "hetsetup.txt";
   private FlowLayout flowLayoutLeft  = new FlowLayout(FlowLayout.LEFT);
   private FlowLayout flowLayoutRight = new FlowLayout(FlowLayout.RIGHT);
   // Changed to avoid security exception in applet.
   private String defaultStoreDirectory = ".;"; //System.getProperty("user.dir");
   private boolean editFlag = false;
   // Commented out to avoid security exception in applet.
   // private JFileChooser fileChooser = new JFileChooser ( );

   //private JButton sideBandButton = new JButton("Show Side Band Display");

   /** Parser for XML input/output. (MFO, 29 November 2001) */
   private XMLReader _xmlReader = null;

   /**
    * A list off samplers whose band widths must be updated when the
    * band width on the front end panel is changed.
    */
   private Vector _samplerList = new Vector();

   /**
    * A static configuration object which can used by classes throughout this
    * package to configure themselves.
    */
   protected static FrequencyEditorCfg cfg = null;

   public FrontEnd ( )
   {
      this(null);
   }

   public FrontEnd ( InputStream inputStream )
   {
      if(inputStream != null) {
         cfg = FrequencyEditorCfg.getCfg(inputStream);
      }
      else {
         // Use default.
	 cfg = new FrequencyEditorCfg();
      }

      setLayout(new BorderLayout());
/* Create the choice of frontends */

      fePanel = new JPanel(flowLayoutLeft);
      fePanel.add ( new JLabel ( "Choose Front End" ) );

      feChoice = new JComboBox ( cfg.frontEnds );
      lowFreq = new JLabel ( "215" );
      lowFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );
      highFreq = new JLabel ( "272" );
      highFreq.setBorder ( new BevelBorder ( BevelBorder.LOWERED ) );

      feChoice.addActionListener ( this );

      feMode = new JComboBox ( cfg.frontEndModes );
      feBandModeChoice = new JComboBox ( );
      feBandModeChoice.addActionListener ( this );

      overlap = new JTextField ( 10 );
      overlap.setText ( "0.0" );
      overlap.addActionListener ( this );

      fePanel.add ( feChoice );
      fePanel.add ( feMode );
      fePanel.add ( feBandModeChoice );
      fePanel.add ( new JLabel ( "    Overlap (MHz)" ) );
      fePanel.add ( overlap );

/* Create the display */

      displayPanel = new JPanel(flowLayoutLeft);
      displayPanel.add ( new JLabel ( "Low Limit (GHz)" ) );
      displayPanel.add ( lowFreq );
      displayPanel.add ( new JLabel ( "    High Limit (GHz)" ) );
      displayPanel.add ( highFreq );
      velocity = new JTextField ( 10 );
      velocity.setText ( "0.0" );
      velocity.addActionListener ( this );

      displayPanel.add ( new JLabel ( "    Velocity (Km/s)" ) );
      displayPanel.add ( velocity );

      rangePanel = new JPanel();
      feBand = new JComboBox ( new String[] { "usb", "lsb", "optimum" } );
      feBand.addActionListener ( this );

/* Main molecular line choice - used to set front-end LO1 to put the line
   in the nominated sideband */

      moleculeChoice = new JComboBox();
      moleculeChoice.setForeground ( Color.red );
      moleculeChoice.addActionListener ( this );
      transitionChoice = new JComboBox();
      transitionChoice.setForeground ( Color.red );
      transitionChoice.addActionListener ( this );
      moleculeFrequency = new JTextField();
      moleculeFrequency.setColumns(12);
      moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency.setForeground ( Color.red );
      moleculeFrequency.getDocument().addDocumentListener(this);
      bandWidthChoice = new JComboBox();
      bandWidthChoice.addActionListener( this );
      mol1Panel = new JPanel(flowLayoutRight);
      mol1Panel.add ( feBand );
      mol1Panel.add ( moleculeChoice );
      mol1Panel.add ( transitionChoice );
      mol1Panel.add ( moleculeFrequency );
      mol1Panel.add ( new JLabel("MHz    BW:") );
      mol1Panel.add ( bandWidthChoice );

/* Secondary moleculare line choice - displayed just for convenience of
   astronomer */

      moleculeChoice2 = new JComboBox();
      moleculeChoice2.setForeground ( Color.magenta );
      moleculeChoice2.addActionListener ( this );
      transitionChoice2 = new JComboBox();
      transitionChoice2.setForeground ( Color.magenta );
      transitionChoice2.addActionListener ( this );
      moleculeFrequency2 = new JTextField();
      moleculeFrequency2.setColumns(12);
      moleculeFrequency2.setText ( "0.0000" );
      moleculeFrequency2.setForeground ( Color.magenta );
      moleculeFrequency2.getDocument().addDocumentListener(this);
      mol2Panel = new JPanel(flowLayoutRight);
      mol2Panel.add ( moleculeChoice2 );
      mol2Panel.add ( transitionChoice2 );
      mol2Panel.add ( moleculeFrequency2 );
      mol2Panel.add ( new JLabel("MHz") );

      linePanel = Box.createVerticalBox();
      linePanel.add ( mol1Panel );

      // MFO (May 03, 2002): mol2Panel is currently not used.
      //linePanel.add ( mol2Panel );

      //linePanel.add ( sideBandButton );

      //sideBandButton.addActionListener(this);

/* Assemble the display */

      rangePanel.add ( linePanel );

      northPanel = new JPanel();
      northPanel.setLayout ( new BoxLayout ( northPanel, 
        BoxLayout.Y_AXIS ) );

      northPanel.add ( fePanel );
      northPanel.add ( displayPanel );
      northPanel.add ( rangePanel );
      add ( northPanel, BorderLayout.NORTH );

      scrollPanel = new JScrollPane();
      //scrollPanel.setPreferredSize ( new Dimension ( 600, 150 ) );

      add ( scrollPanel, BorderLayout.CENTER );
   
      // MFO trigger additional initialising.
      feChoiceAction(null);
      updateSideBandDisplay();
      feMolecule2Action(null);
      feMoleculeAction(null);
   
      // Initialize parser (MFO, 29 November 2001)
      try {
         _xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
	 
	 _xmlReader.setContentHandler(new DefaultHandler() {

            public void startElement(String namespaceURI,
                                     String localName,
                                     String qName,
                                     Attributes atts) throws SAXException {

	       
               if(qName.equals(XML_ELEMENT_HETERODYNE_GUI)) {

                  feChoice.setSelectedItem(atts.getValue(XML_ATTRIBUTE_FE_NAME));
                  feMode.setSelectedItem(atts.getValue(XML_ATTRIBUTE_MODE));
                  feBandModeChoice.setSelectedItem(getObject(feBandModeChoice, atts.getValue(XML_ATTRIBUTE_BAND_MODE)));
                  double overlapGuiValue = Double.parseDouble(atts.getValue(XML_ATTRIBUTE_OVERLAP)) / 1.0E6;
	          overlap.setText("" + overlapGuiValue);
                  velocity.setText(atts.getValue(XML_ATTRIBUTE_VELOCITY));
                  feBand.setSelectedItem(atts.getValue(XML_ATTRIBUTE_BAND));
                  moleculeChoice.setSelectedItem(getObject(moleculeChoice, atts.getValue(XML_ATTRIBUTE_MOLECULE)));
                  transitionChoice.setSelectedItem(getObject(transitionChoice, atts.getValue(XML_ATTRIBUTE_TRANSITION)));
                  moleculeChoice2.setSelectedItem(getObject(moleculeChoice2, atts.getValue(XML_ATTRIBUTE_MOLECULE2)));
                  transitionChoice2.setSelectedItem(getObject(transitionChoice2, atts.getValue(XML_ATTRIBUTE_TRANSITION2)));
	       }
            }
         });
      }
      //catch(SAXException e) {
      catch(Exception e) {
          JOptionPane.showMessageDialog(this,
	                                "Could not initialize XMLReader: " + e,
					"XMLReader problem.", JOptionPane.ERROR_MESSAGE);

      }
   }

   public void actionPerformed ( ActionEvent ae )
   {

      if ( ae.getSource() == feBand )
      {
         feBandAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice )
      {
         feMoleculeAction ( ae );
      }
      else if ( ae.getSource() == transitionChoice )
      {
         feTransitionAction ( ae );
      }
      else if ( ae.getSource() == moleculeChoice2 )
      {
         feMolecule2Action ( ae );
      }
      else if ( ae.getSource() == transitionChoice2 )
      {
         feTransition2Action ( ae );
      }
      else if ( ae.getSource() == feChoice )
      {
         feChoiceAction ( ae );
      }
      else if ( ae.getSource() == feBandModeChoice )
      {
         feBandModeChoiceAction ( ae );
      }
      else if ( ae.getSource() == velocity )
      {
         feVelocityAction ( ae );
      }
      else if ( ae.getSource() == overlap )
      {
         feOverlapAction ( ae );
      }
      else if ( ae.getSource() == bandWidthChoice )
      {
         for(int i = 0; i < _samplerList.size(); i++) {
            ((Sampler)_samplerList.get(i)).setBandWidthAndGui((String)bandWidthChoice.getSelectedItem());
	 }
      }

   }


    public void bandWidthChoiceAction ( ActionEvent ae ) {
      for(int i = 0; i < _samplerList.size(); i++) {
         ((Sampler)_samplerList.get(i)).setBandWidthAndGui((String)bandWidthChoice.getSelectedItem());
      }
    }


   public void changedUpdate(DocumentEvent e) { }

   public void insertUpdate(DocumentEvent e) {
      moleculeFrequencyChanged();
      moleculeFrequency2Changed();
   }
  
   public void removeUpdate(DocumentEvent e) {
      moleculeFrequencyChanged();
      moleculeFrequency2Changed();
   }



   public void feBandAction ( ActionEvent ae )
   {
      feTransitionAction ( ae );
   }


   public void feMoleculeAction ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice.getSelectedItem();

      transitionChoice.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency.setText ( "0.0000" );

      feTransitionAction(null);
   }


   public void feMolecule2Action ( ActionEvent ae )
   {
      SelectionList species = (SelectionList)moleculeChoice2.getSelectedItem();

      transitionChoice2.setModel ( 
        new DefaultComboBoxModel ( species.objectList ) );

      moleculeFrequency2.setText ( "0.0000" );

      feTransition2Action(null);
   }


   public void feTransitionAction ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency.setText ( "" + transition.frequency/1.0E6 );
         String band = (String)feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( transition.frequency );

            double obsFrequency = transition.frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - feIF );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + feIF );
            }
         }

      }
   }


   public void feTransition2Action ( ActionEvent ae )
   {
      Transition transition = (Transition)transitionChoice2.getSelectedItem();

      if ( transition != null )
      {

         moleculeFrequency2.setText ( "" + transition.frequency/1.0E6 );

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setSideLine ( transition.frequency );

         }

      }
   }


   public void moleculeFrequencyChanged()
   {
      try {
         double frequency = Double.parseDouble(moleculeFrequency.getText()) * 1.0E6;
         
	 String band = (String)feBand.getSelectedItem();

         if ( sideBandDisplay != null )
         {

            sideBandDisplay.setMainLine ( frequency );

            double obsFrequency = frequency / 
              ( 1.0 + redshift );
            if ( band.equals ( "usb" ) )
            {
               sideBandDisplay.setLO1 ( obsFrequency - feIF );
            }
            else
            {
               sideBandDisplay.setLO1 ( obsFrequency + feIF );
            }
         }

      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void moleculeFrequency2Changed()
   {
      try {
         double frequency = Double.parseDouble(moleculeFrequency2.getText()) * 1.0E6;
      
         if ( sideBandDisplay != null )
         {
            sideBandDisplay.setSideLine ( frequency );

         }

      }
      catch(NumberFormatException e) {
        // ignore
      }
   }


   public void feBandModeChoiceAction ( ActionEvent ae ) {
      updateSideBandDisplay();
   }

   public void updateSideBandDisplay() {
      double loRange[];
      double mid;
      int subBandCount;

      BandSpec currentBandSpec = (BandSpec)feBandModeChoice.getSelectedItem();

      if(currentBandSpec == null) {
         feBandModeChoice.setSelectedIndex(0);
	 currentBandSpec = (BandSpec)feBandModeChoice.getSelectedItem();
      }


/* Update display of sidebands and subbands */
      Point sideBandDisplayLocation = new Point(100, 100);

      mid = 0.5 * ( loMin + loMax );

      subBandCount = currentBandSpec.numBands;
      subBandWidth = currentBandSpec.bandWidths[0];

      sideBandDisplay.updateDisplay ( currentFE, loMin, loMax,
        feIF, feBandWidth,
        redshift,
        currentBandSpec.getBandWidths(feOverlap),
        currentBandSpec.getChannels(  feOverlap),
        subBandCount );
   }


   public void feChoiceAction ( ActionEvent ae )
   {

      double loRange[];
      double mid;
      int subBandCount;
      Receiver r;
      double obsmin;
      double obsmax;

      String newFE = (String)feChoice.getSelectedItem();
      currentFE = newFE;
      r = (Receiver)cfg.receivers.get ( currentFE );

      loMin = r.loMin;
      loMax = r.loMax;
      feIF = r.feIF;
      feBandWidth = r.bandWidth;

      lowFreq.setText ( "" + (int)(loMin*1.0E-9) );
      highFreq.setText ( "" + (int)(loMax*1.0E-9) );

/* Update choice of sub-band configurations */

      feBandModeChoice.setModel ( 
        new DefaultComboBoxModel ( r.bandspecs ) );


      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Reset line frequency report */

      moleculeFrequency.setText ( "0.0000" );
      moleculeFrequency2.setText ( "0.0000" );


/* Update display of sidebands and subbands */

      updateSideBandDisplay();
      //if ( sideBandDisplay != null )
      //{
      //   sideBandDisplay.dispose ( );
      //}

   }


   public void feVelocityAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;
      double obsmin;
      double obsmax;

      svalue = velocity.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      redshift = dvalue / EdFreq.LIGHTSPEED;

      obsmin = loMin - feIF - ( feBandWidth * 0.5 );
      obsmax = loMax + feIF + ( feBandWidth * 0.5 );

/* Update choice of molecules */

      moleculeChoice.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
          obsmax*(1.0+redshift) ) ) );
      moleculeChoice2.setModel ( 
        new DefaultComboBoxModel ( 
        lineCatalog.returnSpecies ( obsmin*(1.0+redshift),
        obsmax*(1.0+redshift) ) ) );

/* Update display of sidebands */

      if ( sideBandDisplay != null )
      {
         sideBandDisplay.setRedshift ( redshift );
      }
   }


   public void feOverlapAction ( ActionEvent ae )
   {
      String svalue;
      double dvalue;

      svalue = overlap.getText();
      dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      BandSpec currentBandSpec = (BandSpec)feBandModeChoice.getSelectedItem();

      _updateBandWidthChoice(currentBandSpec.getBandWidths(feOverlap));

      bandWidthChoiceAction(null);
   }


   /**
    * Not used anymore.
    *
    * @deprecated replaced by {@link #toXML()}.
    */
   public void saveAsASCII ( PrintWriter out )
   {
      out.println ( "<heterodyne" );

      out.println ( "  feName=" + (String)feChoice.getSelectedItem() );
      out.println ( "  mode=" + (String)feMode.getSelectedItem() );
      out.println ( "  bandMode=" + 
        (feBandModeChoice.getSelectedItem()).toString() );
      out.println ( "  velocity=" + velocity.getText() );
      out.println ( "  band=" + (String)feBand.getSelectedItem() );

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      out.println ( "  overlap=" + feOverlap );

      out.println ( "  LO1=" + sideBandDisplay.getLO1() );

      out.println ( ">" );

      sideBandDisplay.saveAsASCII ( out );

      out.println ( "</heterodyne>" );

   }

   /**
    * Creates XML representation of the frequency editor settings.
    *
    * (MFO, November 2001)
    */
   public String toXML() {
      StringBuffer stringBuffer = new StringBuffer();
      String value = "";

      stringBuffer.append ("<" + XML_ELEMENT_HETERODYNE_GUI + "\n" );

      stringBuffer.append ("    " + XML_ATTRIBUTE_FE_NAME + "=\"" + (String)feChoice.getSelectedItem() + "\"\n");
      stringBuffer.append ("    " + XML_ATTRIBUTE_MODE + "=\"" + (String)feMode.getSelectedItem() + "\"\n");
      try {
        value = feBandModeChoice.getSelectedItem().toString();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append("    " + XML_ATTRIBUTE_BAND_MODE + "=\"" + value + "\"\n");

      stringBuffer.append("    " + XML_ATTRIBUTE_VELOCITY + "=\"" + velocity.getText() + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_BAND + "=\"" + (String)feBand.getSelectedItem() + "\"\n");

      String svalue = overlap.getText();
      double dvalue = (Double.valueOf(svalue)).doubleValue();
      feOverlap = 1.0E6 * dvalue;

      stringBuffer.append("    " + XML_ATTRIBUTE_OVERLAP + "=\"" + feOverlap + "\"\n");

      try {
	value = "" + sideBandDisplay.getLO1();
      }
      catch(NullPointerException e) {
        value = "";
        // ignore for now.
	// Probable reason for NullPointerException: toXML is called in _updateWidgets of another class
	// and many things in the frequency editor are currently only initialised after the user has made
	// some choices/settings.
      }
      stringBuffer.append("    " + XML_ATTRIBUTE_LO1 + "=\"" + value + "\"\n");

      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE    + "=\"" + moleculeChoice.getSelectedItem()     + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_TRANSITION  + "=\"" + transitionChoice.getSelectedItem()   + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE_FREQUENCY  + "=\"" + moleculeFrequency.getText()  + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE2 + "=\"" + moleculeChoice2.getSelectedItem()      + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_TRANSITION2 + "=\"" + transitionChoice2.getSelectedItem()  + "\"\n");
      stringBuffer.append("    " + XML_ATTRIBUTE_MOLECULE_FREQUENCY2 + "=\"" + moleculeFrequency2.getText() + "\"");
      stringBuffer.append(">\n");

      stringBuffer.append(sideBandDisplay.toXML() + "\n");

      stringBuffer.append ( "</" + XML_ELEMENT_HETERODYNE_GUI + ">\n" );

      return "\n" +
             "<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->\n" +
             "<!-- XML representing the content of the OT GUI widgets in front end and frequency editor. -->\n" +
             "<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->\n" +
             stringBuffer.toString() +
             "\n" +
             "<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->\n" +
             "<!-- XML used by the translator to create the ACSIS config file.                           -->\n" +
             "<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->\n" +
             toConfigXML();
   }

  public String toConfigXML() {
    String restFrequencyId = moleculeChoice.getSelectedItem() + " (" + transitionChoice.getSelectedItem() + ")";
    String sidebandString = (String)feBand.getSelectedItem();

    int sideband = 0;

    if(sidebandString.equals("lsb"))     { sideband = -1; }
    if(sidebandString.equals("usb"))     { sideband =  1; }
    if(sidebandString.equals("optimum")) { sideband =  0; } // will have to set to -1 or 1 by the OT/Frequency Editor
							    // And sidebandString must be set to "lsb" or "usb"
							    // for the front end configuration XML.

    String indent = "";


    // ------------------- Front end configuration ------------------------------------
    String frontendConfigXml = 
        indent + "<frontend_configure>\n" +
        indent + "  <rest_frequency units=\"GHz\" value=\"" +
                 (Double.parseDouble(moleculeFrequency.getText()) * 1.0E6) + "\"/>\n" +
        indent + "  <if_centre_freq units=\"GHz\" value=\"" + feIF + "\"/>\n" +
        indent + "  <sideband value=\"" + sidebandString.toUpperCase() + "\"/>\n" +
        indent + "  <sb_mode value=\"" + ((String)feMode.getSelectedItem()).toUpperCase() + "\"/>\n" +
        indent + "  <freq_offset_scale units=\"MHz\" value=\"???\"/>\n" +
        indent + "  <dopple_tracking value=\"ON\"/>\n" +	// Options are ON | OFF. Default to ON for now.
        indent + "  <optimize value=\"DISABLE\"/>\n";		// Options are ENABLE | DISABLE. Default to DIABLE for now.

    if(((String)feChoice.getSelectedItem()).equals("HARP-B")) {
      frontendConfigXml +=
        indent + "  <channel_mask>\n" + // Array of (OFF | ON | NEED). Use Pixeltool to switch pixels ON/OFF. NEED???
        indent + "    <CHAN_MASK_VALUE CHAN=\"00\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"01\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"02\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"03\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"04\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"05\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"06\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"07\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"08\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"09\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"10\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"11\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"12\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"13\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"14\" VALUE=\"ON\"/>\n" +
        indent + "    <CHAN_MASK_VALUE CHAN=\"15\" VALUE=\"ON\"/>\n" +
        indent + "  </channel_mask>\n";
    }

    frontendConfigXml +=
        indent + "</frontend_configure>\n\n";

    return
      frontendConfigXml +

      // ------------------- ACSIS configuration ----------------------------------------

      // Line list
      indent + "<line_list>\n" +
      indent + "  <rest_frequency id=\"" + restFrequencyId + "\" units=\"GHz\">" +
        moleculeFrequency.getText() +
        "</rest_frequency>\n" +
      indent + "</line_list>\n\n" +

      // Acsis spectral windows list
      indent + "<acsis_spw_list>\n" +
      indent + "  <doppler_field ref=\"TCS.RV.DOPPLER???\"/>\n" +
      indent + "  <spectral_window_id_field ref=\"SPECTRAL_WINDOW_ID???\">\n" +
      indent + "  <front_end_lo_freq_field ref=\"FE.STATE.LO_FREQ\">\n" +

      // Spectral windows
      sideBandDisplay.toConfigXML(restFrequencyId, sideband,
        "<!-- <base_line_fit> etc. not implemented yet. -->", indent + "  ") +
	
      indent + "</acsis_spw_list>\n\n";
  }

   /**
    * Set FrequencyTable from XML.
    *
    * Added by Martin Folger (14 November 2001)
    */
   public void update(String xml) throws Exception
   {
      // If there is no xml then trigger reset.
      if((xml == null) || xml.trim().equals("")) {
         feChoice.setSelectedIndex(0);
	 feTransition2Action(null);
	 feTransitionAction(null);
	 return;
      }
 
      //try {
        _xmlReader.parse(new InputSource(new StringReader(xml)));


        // Trigger frequency calculation, spectral line display and centering of sideband.
//	feTransitionAction(null);
//	feTransition2Action(null);

      //}
      //catch(Exception e) {
         // There seems to be a class cast exception at the first attempt to parse which
	 // disappears at the second attempt.
	 // This problem will disappear with jdk1.4
	// System.out.println("FrontEnd.update(): SAXParser.parse bug in jdk1.3. Ignore.");
	 //_parser.parse(new InputSource(new StringReader(xml)));
      //}

      updateSideBandDisplay();
      sideBandDisplay.update(xml);
   }

   public static void main ( String args[] )
   {
      new FrontEndFrame(new FrontEnd());
   }


   private Object getObject(JComboBox comboBox, String name) {
     for(int i = 0; i < comboBox.getItemCount(); i++) {
       if(comboBox.getItemAt(i).toString().equals(name)) {
         return comboBox.getItemAt(i);
       }
     }

     return null;
   }

   public void setSideBandDisplayVisible(boolean visible) {
      sideBandDisplay.setVisible(visible);
   }

   public void setSideBandDisplayLocation(int x, int y) {
      sideBandDisplay.setLocation(x, y);
   }

   // Added by MFO (8 January 2002)
   /**
    * Returns "usb" (Upper Side Band) or "lsb" (Lower Side Band).
    *
    * Needed by {@link edfreq.SideBand} to shift LO1 when top SideBands are changed.
    */
   protected String getFeBand() {
      return (String)feBand.getSelectedItem();
   }

   /**
    * Add a sampler to a list off samplers so that its band widths can be updated when the
    * band width on the front end panel is changed.
    */
   protected void addToSamplerList(Sampler sampler) {
      _samplerList.add(sampler);


      if(bandWidthChoice.getItemCount() != sampler.getBandWidthOptions().length) {
         _updateBandWidthChoice(sampler.getBandWidthOptions());
	 return;
      }

      for(int i = 0; i < bandWidthChoice.getItemCount(); i++) {
         if(Double.parseDouble((String)bandWidthChoice.getItemAt(i)) != sampler.getBandWidthOptions()[i]) {
            _updateBandWidthChoice(sampler.getBandWidthOptions());
            return;
	 }
      }
   }

   /**
    * Add a sampler to a list off samplers so that its band widths can be updated when the
    * band width on the front end panel is changed.
    */
   protected void clearSamplerList() {
      _samplerList.clear();
   }

   private void _updateBandWidthChoice(double [] values) {
      bandWidthChoice.removeActionListener(this);
      bandWidthChoice.removeAllItems();
      for(int i = 0; i < values.length; i++) {
         bandWidthChoice.addItem("" + (Math.rint(values[i] * 1.0E-6) / 1000.0));
      }
      bandWidthChoice.addActionListener(this);
   }
}
