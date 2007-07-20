/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.util;

import java.io.*;
import java.net.*;

/**
 * This implements the reading of instrument config files.
 */
public class InstCfgReader
{
   private BufferedReader cfgFile;

/**
 * The constructor
 */
public InstCfgReader(URL baseURL, String cfgFilename)
{
   URL url=null;
   try {
      url = new URL(baseURL, cfgFilename);
   } catch (MalformedURLException ex) {
      System.out.println("Problem constructing the inst. config file URL: " + ex);
   }

   try {
      cfgFile = new BufferedReader (new InputStreamReader (url.openStream()));
   } catch (IOException ex) {
      System.out.println("Problem opening the inst. config file: " + ex);
   }

}

public InstCfgReader(String cfgFilename)
{
   try {
      cfgFile = new BufferedReader (new FileReader (cfgFilename));
   } catch (FileNotFoundException ex) {
      System.out.println("Problem opening the inst. config file: " + ex);
   }
}

/**
 * Return the next block as a string. A "block" is defined as a section
 * starting with a non-blank, non-comment descriptor and concluding with
 * a comment line. This implies that comment lines are not allowed within
 * "blocks".
*/
public String readBlock () throws IOException {

   String line;
   String block = "";

// while there are more lines
   while ((line = cfgFile.readLine()) != null) {

// ignore blanks
      if (line.length() == 0) continue;

// ignore comments if not yet started block
      if (line.startsWith("#") && block == "") continue;

// if we have started block and its a comment then exit while
      if (line.startsWith("#") && block != "") break;

// append line to block
      block = block + line;

   }
   if (block == "") return null;
   return block;
}

public void close () {
   if (cfgFile != null) {
      try {
         cfgFile.close();
      }catch (IOException e) {}
   cfgFile = null;
   }
}

}







