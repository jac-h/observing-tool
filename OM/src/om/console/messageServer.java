package om.console;

import om.sciProgram.AlertBox;
import om.frameList.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.rmi.server.*;
import java.rmi.*;

import om.dramaMessage.*;

/** messageServer class is about
    providing service to process message from a socket.
    Please note this extends UnicastRemoteObject and
    implements messageServerInterface
    REMEMBER:this class has to be compiled by a rmic

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final public class messageServer extends UnicastRemoteObject implements messageServerInterface
{

/** public messageServer(String instrument) is
    the class constructor. The class has only one constructor so far.

    @param String
    @return  none
    @throws RemoteException
*/
  public messageServer(String instrument, FrameList frs) throws RemoteException
  {
    instName=instrument;
    consoleList=frs;
  }

  /**public void initSequence() is  a public method to
    initialise the sequence. when the socket is *first*
    connected by drama socket task

    @param none
    @return  none
    @throws RemoteException
 */
  public void initSequence() throws RemoteException
  {
    cSent.setInit();
    cSent.setLink("SOCK_"+instName);
  }


  /** public void processMessage(String m) is a public method to
    process incoming messages from the socket.

    @param String m
    @return  none
    @throws RemoteException
 */
  public void processMessage(String m) throws RemoteException
  {


    if((System.getProperty("DEBUG_PARSER") != null) && System.getProperty("DEBUG_PARSER").equalsIgnoreCase("ON")) {
      System.out.println ("(original message)"+m);
    }	

    // special case: is parsed here and not using dramaMessage package
    if(m.startsWith("EXECLIST::")) {
      execlistMessage(m.substring(10)); //the exec sequence list
      return;
    }

    AbstractMessage message = null;
    
    try {
      message = MessageParser.parse(m);
    }
    catch(MessageParseException e) {
      new AlertBox("" + e);
    }

  
    if(message.getClass().getName().equals("dramaMessage.DramaMessage")) {
      DramaMessage dramaMessage = (DramaMessage)message;
      
      if((System.getProperty("DEBUG_PARSER") != null) && System.getProperty("DEBUG_PARSER").equalsIgnoreCase("ON")) {
        System.out.println("(from parser     )" + dramaMessage.toMsgString());
      }
      
      if(dramaMessage.type == DramaMessage.DHS_TASK) {
	dhsMessage(dramaMessage.name, dramaMessage.value);  //the status of the dhs
	return;
      }
      
      
      if(dramaMessage.type == DramaMessage.INSTRUMENT_TASK) {
	instMessage(dramaMessage.name, dramaMessage.value);  //the status of the instrument
	return;
      }
      
      if(dramaMessage.type == DramaMessage.OOS_TASK) {
	if(dramaMessage.name.equals("State")) {
	  stateMessage(dramaMessage.value); //the state of the OOS task
	  return;
	}
      
      
	if(dramaMessage.name.equals("CmdIndex")) {
	  indexMessage(dramaMessage.value);  //the index of the sequence
	  return;
	}

        if((System.getProperty("DEBUG_PARSER") != null) && System.getProperty("DEBUG_PARSER").equalsIgnoreCase("ON")) {
	  System.out.println("\"msg: PARA:" + dramaMessage.name + "\" ignored.");
	}

	return;
      }
      
      if(dramaMessage.type == DramaMessage.MESSAGE) {
	if(dramaMessage.value.equals("entered uftiStartMovie")) {
	  movieMessage(dramaMessage.value); //the movie messsage
	  return;
	}

	if(dramaMessage.value.equals("the monitor is ready.")) {
	  Properties temp =System.getProperties();
	  
	  if(System.getProperty("DBUG_MESS").equals("ON"))
	    System.out.println(temp.getProperty("execFilename"));
	    
	  if(temp.containsKey("execFilename"))
	    cSent.setLoad(temp.getProperty("execFilename"));
	    
	  return;
	}
      }
      
      if(dramaMessage.type == DramaMessage.ERROR) {
	//show error messages from drama tasks
	ErrorBox err=new ErrorBox("Error in the Drama tasks:"+m.substring(5));
	return;
      }
    }
     
    if(message.getClass().getName().equals("dramaMessage.CompletionMessage")) {
      CompletionMessage completionMessage = (CompletionMessage)message;
      
      if((System.getProperty("DEBUG_PARSER") != null) && System.getProperty("DEBUG_PARSER").equalsIgnoreCase("ON")) {
        System.out.println("(from parser     )" + completionMessage.toMsgString());
      }
      
      if(completionMessage.status == 0) {
        if(completionMessage.type.equals("obeyw")) {
          if(completionMessage.taskName.equals(instName)) {
	    if(completionMessage.action.equals("START_MOVIE")) {
	      movieMessage(m);
	      return;
	    }
	  }

	  if(completionMessage.taskName.equals("MONITOR_"+instName)) {
	    if(completionMessage.action.equals("LINK")) {
	      if(mLink==3) {
		System.out.println ("Setting link to OOS");
	        cSent.setLink("OOS_"+instName);
	        mLink--;
	      } else if(mLink==1) {
		// Set link to DES task: get name from property list or
		// default to just "DES".
	        String taskName = System.getProperty ("DES", "DES");
		System.out.println ("Setting link to "+taskName);
	        cSent.setLink(taskName);
	        mLink--;
 	      } else if(mLink==4) {
	        // When linking to the instrument task need to get the name of
	        // the instrument task from system properties. Default to task
		// name same as instrument name. (AB 24Apr00)
	        String taskName = System.getProperty (instName, instName);
		System.out.println ("Setting link to "+taskName);
	        cSent.setLink(taskName);
	        mLink--;
	      } else if(mLink==2) {
		System.out.println ("Setting link to DHS");
	        cSent.setLink("DHSPOOL_"+instName);
	        mLink--;
	      } else {
	        cSent.setStart();
	      }
	      return;
	    }

            if(completionMessage.action.equals("GETP")) {
	      cSent.setMonitorOn();
	      return;
	    }
	  }
	  
	  if(completionMessage.taskName.equals("OOS_"+instName)) {
            if(completionMessage.action.equals("breakPoint")) {
	      breakPointMessage();
	      return;
	    }
	    
            if(completionMessage.action.equals("cancelStop")) {
	      cancelBreakPoint();
	      return;
	    }

	    if(completionMessage.action.equals("load")) {
	      items.Init();
	      return;
	    }
	    
	    if(completionMessage.action.equals("init")) {
	      // if this inst is the one connected to the tcs then
	      // ensure it really is!. Setting to none will do this.
	      if (consoleList.getConnectedInstrument().equals(instName)) {
	        consoleList.setConnectedInstrument("NONE");
	      }
	      return;
	    }

	    if(completionMessage.action.equals("stop")) {
	      return;
	    }

	    if(completionMessage.action.equals("exit")) {
	      return;
	    }

	    if(completionMessage.action.equals("target")) {

	      if(consoleList.getConnectedInstrument().equals("NONE")) {
	        uPanel.getTCSconnection().setText("Connected!!");
	        consoleList.setConnectedInstrument(instName);
	      }
	      return;
	    }
	  }
          
	  if(completionMessage.taskName.equals("OOS_"+consoleList.getConnectedInstrument())) {
            if(completionMessage.action.equals("target")) {
	      sequenceFrame temp;
	      for(int i=0;i<consoleList.getList().size();i++) {
	        temp = (sequenceFrame)consoleList.getList().elementAt(i);
	        if(temp.getInstrument().equals(consoleList.getConnectedInstrument())) {
	          temp.getUpperPanel().getTCSconnection().setText("disconnected");
	          break;
	        }
	      }
	    
	      consoleList.setConnectedInstrument("NONE");

	      return;
	    }
	  }
        }
      }

      if(completionMessage.status == 2) {
        if(completionMessage.type.equals("obeyw")) {
          if(completionMessage.taskName.equals("OOS_"+instName)) {
	    if(completionMessage.action.equals("target")) {

	      new AlertBox("Failed in a TCS connection attempt");
	      return;
	    }
          }
	}  
      }
    }
  }
    
  /** public void movieMessage(String s) is a public method to
    process messages about the movie status.

    @param String m
    @return  none
    @throws RemoteException
 */
  public void movieMessage(String s) throws RemoteException
  {
    items.Movie(s);
  }


  /** public void stateMessage(String s) is a public method to
    process messages about the oos "State".

    @param String m
    @return  none
    @throws RemoteException
 */
  public void stateMessage(String s) throws RemoteException
  {
    if(s.equals("Running"))
    {
      items.Run();
      seqPanel.getMyList().setEnabled(false);//disable user selection now
      if(breakPoint>0)
      if(breakPoint<seqPanel.getMyList().getSelectedIndex()-1)
      {
        if(seqPanel.getListData().elementAt
          (seqPanel.getMyList().getSelectedIndex()-1).toString().length()>17)
           if(seqPanel.getListData().elementAt
          (seqPanel.getMyList().getSelectedIndex()-1).toString().
            substring(0,17).equals("*****************"))
            cancelBreakPoint();
      }

    }
    else if(s.equals("Stopped"))
    {
      items.Stop();
      seqPanel.getMyList().setEnabled(true);
      if(breakPoint>0)
      if(breakPoint<seqPanel.getMyList().getSelectedIndex()-1)
        cancelBreakPoint();
    }
    else if(s.equals("Paused"))
    {
      items.Pause();
    }
    else if (s.equals("Idle"))
    {
      items.Idle();
    }

    uPanel.messageString("", s);
  }

/** public void execlistMessage(String s) is a public method to
    process messages about the oos's exec sequence list.

    @param String m
    @return  none
    @throws RemoteException
 */
  public void execlistMessage(String s) throws RemoteException
  {
    if (s.length()>10) {
      if (s.substring(0,10).equals("Completed:")) {
 	if (System.getProperty("DBUG_MESS").equals("ON"))
 	  System.out.println("Data list:"+seqData);

	for (int i=0;i<seqData.size();i++) {
	  if (seqData.elementAt(i).toString().length()>=10) {
	    if (seqData.elementAt(i).toString().substring(0,10).
		equals("breakPoint")) {
	      seqData.setElementAt(new String
				   ("_ _ _ _ _ _ _ _ _ _ break point"),i);
	    } else if (seqData.elementAt(i).toString().substring(0,10).
		       equals("-breakPoin")) {
	      seqData.setElementAt(new String
				   ("-_ _ _ _ _ _ _ _ _ _ break point"),i);
	    }
	  }
	}
	seqPanel.setDataList(seqData);

	return;

      } else if(s.substring(0,10).equals("start_exec")) {

	seqData.removeAllElements();
	seqData.trimToSize();
	return;
      }
    }
    seqData.addElement(s);
  }

  /** public void instMessage(String name, String value) is a public method to
    process messages about the instrument status.
    "INST@name::value"

    @param String m
    @return  none
    @throws RemoteException
 */
  public void instMessage(String name, String value) throws RemoteException
  {

    System.out.println ("Got message " + name + " " + value);
    if(name.equals("expTime"))
    {
      _movie.setExposureTime(value);
    }
    
    ((messageInterface)sFrame).messageString(name, value);

  }

  /** public void dhsMessage(String name, String value) is a public method to
    process messages about the dhs status.
    "DHS@name::value"

    @param String m
    @return none
    @throws RemoteException
 */
  public void dhsMessage(String name, String value)  throws RemoteException
  {
    ((messageInterface)dPanel).messageString(name, value);
  }

  /** public void stateMessage(String s) is a public method to
    process messages about the index position of the seq panel.

    @param String m
    @return  none
    @throws RemoteException
 */
  public void indexMessage(String s) throws RemoteException
  {

    int index=0;
    try {
      index=Integer.parseInt(s)-1;
    } catch (NumberFormatException e) {
      System.out.println("Sequence index indecipherable!" + e.getMessage());
    }

    try {

      if (seqPanel.getListData().size()<seqPanel.getExecData().size()) {
	if (index >= seqPanel.getIndexMap().length) {
	  //	  index=seqPanel.getIndexMap().length-1;
	  index = 0;
	}
	index=seqPanel.getIndexMap()[index];
      }else {
	if (index >= seqPanel.getExecData().size()) {
	  index = 0;
	}
      }

    }catch (NullPointerException  e) {
      System.out.println ("Unexpected error: Sequence panel null!");
    }

    Point pos =seqPanel.getJScrollPane().getViewport().getViewPosition();

    if (index>2) pos.y =(int)((double)index*13.1)-30;
    
    if (index>=0) seqPanel.getMyList().setSelectedIndex(index);

    seqPanel.getJScrollPane().getViewport().setViewPosition(pos);
    
    if (breakPoint>0) {
      if (breakPoint<seqPanel.getMyList().getSelectedIndex()-1) {
	if (seqPanel.getListData().elementAt 
	    (seqPanel.getMyList().getSelectedIndex()-1).
	    toString().length()>17) {
	  if(seqPanel.getListData().elementAt
	     (seqPanel.getMyList().getSelectedIndex()-1).toString().
	     substring(0,17).equals("*****************")) {
	    cancelBreakPoint();
	  }
	}
      }
    }

    seqPanel.validate();
  }

  /** public void breakPointMessage() is a public method to
    process messages about setting a break point

    @param none
    @return  none
    @throws RemoteException
 */
  public void breakPointMessage() throws RemoteException
  {
    int index=seqPanel.getMyList().getSelectedIndex();
    int i=seqPanel.getListData().size();

    if(index>=0)
    for(i=index; i<seqPanel.getListData().size();i++)
    if(seqPanel.getListData().elementAt(i).toString().length()>20)
    {
      if(seqPanel.getListData().elementAt(i).toString().substring(0,20).
        equals("_ _ _ _ _ _ _ _ _ _ "))
      {
        breakPoint=index;
        seqPanel.getListData().setElementAt(new String("***************** break point"),i);
        break;
      }  else if(seqPanel.getListData().elementAt(i).toString().
        substring(0,21).equals("-_ _ _ _ _ _ _ _ _ _ "))
      {
        breakPoint=index;
        seqPanel.getListData().setElementAt(new String("-***************** break point"),i);
        break;
      }
    }

    if(i<seqPanel.getListData().size())
      items.BreakPoint();
  }

  /**public void cancelBreakPoint() is a public method to
    process messages about canceling a break point

    @param none
    @return  none
    @throws RemoteException
 */
  public void cancelBreakPoint() throws RemoteException
  {
    int i;
    for(i=0; i<seqPanel.getListData().size();i++)
    if(seqPanel.getListData().elementAt(i).toString().length()>18)
    {
      if(seqPanel.getListData().elementAt(i).toString().
        substring(0,17).equals("*****************"))
      {
        breakPoint=0;
        seqPanel.getListData().setElementAt(new String("_ _ _ _ _ _ _ _ _ _ break point"),i);
        break;
      } else if(seqPanel.getListData().elementAt(i).toString().
          substring(0,18).equals("-*****************"))
      {
        breakPoint=0;
        seqPanel.getListData().setElementAt(new String("-_ _ _ _ _ _ _ _ _ _ break point"),i);
        break;
       }
     }
    //if(i<seqPanel.getListData().size())
      items.CancelBreak();
  }

 /**linkItemsShown (itemsShown i) is a public method to
    link the messageServer with a ItemShonw object

    @param itemsShown
    @return  none
    @throws RemoteException
 */
  public void linkItemsShown (itemsShown i) throws RemoteException {items=i;}

 /** public void linkCommandSent (sendCmds c) is a public method to
    link the messageServer with a sendCmds object

    @param sendCmds
    @return  none
    @throws RemoteException
 */
  public void linkCommandSent (sendCmds c)  throws RemoteException {cSent=c;}

 /**public void linkSequencePanel (sequencePanel seq) is a public method to
    link the messageServer with a sequencePanel object

    @param sequencePanel
    @return  none
    @throws RemoteException
 */
  public void linkSequencePanel (sequencePanel seq) throws RemoteException
  {
     seqPanel=seq;
  }

  /**public void linkUpperPanel (upperPanel u) is a public method to
    link the messageServer with a upperPanel object

    @param upperPanel
    @return  none
    @throws RemoteException
 */
  public void linkUpperPanel (upperPanel u) throws RemoteException {uPanel=u;}

  /**public void linkStatusFrame (UFTIStatus s) t is a public method to
    link the messageServer with a UFTIStatus object

    @param UFTIStatus
    @return  none
    @throws RemoteException
 */
  public void linkUFTIStatus (UFTIStatus s) throws RemoteException
  {
    sFrame=s;
  }

  public void linkCGS4Status (CGS4Status s) throws RemoteException
    {
      sFrame=s;
    }

  public void linkIRCAM3Status (IRCAM3Status s) throws RemoteException
    {
      sFrame=s;
    }

  public void linkMichelleStatus (MichelleStatus s) throws RemoteException
    {
      sFrame=s;
    }

  /** public void linkTargetPanel(targetPanel t) is a public method to
    link the messageServer with a targetPanel object

    @param targetPanel
    @return none
    @throws RemoteException
 */
  public void linkTargetPanel(targetPanel t) throws RemoteException {tPanel=t;}

  /** public void linkDhsPanel(dhsPanel d) is a public method to
    link the messageServer with a dhsPanel object

    @param dhsPanel
    @return  none
    @throws RemoteException
 */
  public void linkDhsPanel(dhsPanel d)  throws RemoteException {dPanel=d;}

  /** public void linkMovieFrame(movie m) is a public method to
    link the messageServer with a movie object

    @param movie
    @return  none
    @throws none
 */
  public void linkMovieFrame(movie m) {_movie=m;}

  /**public void setSeqData(Vector s) is a public method to
    set up sequence data

    @param Vector
    @return  none
    @throws RemoteException
 */
  public void setSeqData(Vector s) throws RemoteException
  {
    seqPanel.setDataList(s);
  }

  /**public Vector getSeqData() is a public method to
    get sequence data

    @param none
    @return  Vector
    @throws RemoteException
 */
  public Vector getSeqData() throws RemoteException  {return seqData;}

  private int breakPoint=0;
  private targetPanel tPanel;
  private dhsPanel dPanel;
  private instrumentStatusPanel sFrame;
  private upperPanel uPanel;
  private sequencePanel seqPanel;
  private itemsShown items;
  private movie _movie;
  private sendCmds cSent;
  private String fileName, instName;
  private Vector seqData=new Vector();
  private FrameList consoleList;
  private int mLink=4;  //no of drama tasks to be linked with the drama monitor task e.g.3
}
