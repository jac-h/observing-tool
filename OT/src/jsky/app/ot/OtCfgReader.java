// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.StringTokenizer;
import java.util.Vector;
import jsky.util.gui.DialogUtil;

//
// Used to read the OT configuration file.
//
class OtCfgReader
{
    public static final String GUIDE_TAG	= "guide";
    
    // MFO, 19 December 2001
    public static final String BASE_TAG		= "base";

    public static final String PHASE1_TAG	= "phase1";

    public static final String TPE_FEATURE_TAG	= "tpe feature";
    public static final String TPE_TYPE_TAG	= "tpe type";

    public static final String CLASS_TAG		= "class";
    public static final String EDITOR_TAG	= "editor";
    public static final String FEATURE_TAG	= "img feature";
    
    public static final String LIBRARY_TAG	= "library";

    public static final String TELESCOPE_UTIL_TAG	= "telescope util";

    public static final String NAME_RESOLVERS_TAG	= "name resolvers";

    public static final String CHOP_DEFAULTS		= "chop defaults";

    /** Targets which are known by the TCS by name. E.g. planets. */
    public static final String NAMED_TARGETS		= "named targets";

    /** Telescope latitude. Can be used for noise calculations etc. */
    public static final String TELESCOPE_LATITUDE	= "telescope latitude";

    public static final String SCHEMA_LOCATION          = "schemaLocation";

    /**
     * Read the configuration file from the given base URL and file name.
     *
     * @return The OtCfg.Info structure containing the parsed information
     * from the ot.cfg file.
     */
    public static OtCfg.Info load(String cfgFilename)  {
	ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	URL url = classLoader.getResource(cfgFilename);
	if (url == null) {
	    DialogUtil.error("Problem constructing the config file URL: " + cfgFilename);
	    return null;
	}

	try {
	    return load(url.openStream());
	} catch (IOException ex) {
	    DialogUtil.error("Problem reading the config file: " + ex);
	    return null;
	}  
    }

    /**
     * Read the configuration file from the given input stream.
     *
     * @return The OtCfg.Info structure containing the parsed information
     * from the ot.cfg file.
     */
    public static OtCfg.Info load(InputStream is) throws IOException  {
	OtCfg.Info info = new OtCfg.Info();

	Vector tpeFeatureV = new Vector();  // For add-on TpeImageFeatures
	Vector     spItemV = new Vector();  // For add-on SpItems

	BufferedReader  br = null;
	try {
	    br = new BufferedReader( new InputStreamReader(is) );
	    String line;

	    OtCfg.TpeFeatureCfg tfc = null;
	    OtCfg.SpItemCfg     sic = null;

	    while ((line = br.readLine()) != null) {
		if (line.startsWith("#") || (line.length() == 0)) continue;

		// Guide Star Tags
		if (line.startsWith(GUIDE_TAG)) {
		    info.guideTags = _parseCommaList(_getValue(line));
		} else

		  // Base Star Tag
		  // If BASE_TAG is not found in cfg file then the default "Base" is used.
		  // (see SpTelescopePos.BASE_TAG)
//		  if (line.startsWith(BASE_TAG)) {
//		      info.baseTag = _getValue(line);
//		  } else

		    // Phase1 Document Generator
		    if (line.startsWith(PHASE1_TAG)) {
			info.phase1Class = _getValue(line);
		    } else


			// TpeImageFeature add-ons
			if (line.startsWith(TPE_FEATURE_TAG)) {
			    if (tfc != null) tpeFeatureV.addElement(tfc);

			    tfc           = new OtCfg.TpeFeatureCfg();
			    tfc.featClass = _getValue(line);
			} else

			    if (line.startsWith(TPE_TYPE_TAG)) {
				if (tfc != null) tfc.type = _getValue(line);
			    } else


				// SpItem add-ons
				if (line.startsWith(CLASS_TAG)) {
				    if (sic != null) spItemV.addElement(sic);

				    sic         = new OtCfg.SpItemCfg();
				    sic.spClass = _getValue(line);
				} else

				    if (line.startsWith(EDITOR_TAG)) {
					if (sic != null) sic.editorClass = _getValue(line);
				    } else

					if (line.startsWith(FEATURE_TAG)) {
					    if (sic != null) sic.imgFeatureClass = _getValue(line);
					} else

	                                   // Libraries (AB added 1-Aug-00)
                                           if (line.startsWith(LIBRARY_TAG)) {
                                               info.libraryTags = _parseCommaList(_getValue(line));
                                           } else

					      // telescope utility class (added by MFO, 10 January 2002)
					      if (line.startsWith(TELESCOPE_UTIL_TAG)) {
                                                  info.telescopeUtilClass = _getValue(line);
					      } else

					         // name resolvers (added by MFO, May 30, 2001)
						 if(line.startsWith(NAME_RESOLVERS_TAG)) {
                                                    info.nameResolvers = _parseCommaList(_getValue(line));
					         } else

					            // chop defaults (added by MFO, May 13, 2002)
						    if(line.startsWith(CHOP_DEFAULTS)) {
                                                       info.chopDefaults = _parseCommaList(_getValue(line));
						    } else

					            // named targets (added by MFO, June 05, 2002)
						    if(line.startsWith(NAMED_TARGETS)) {
                                                       info.namedTargets = _parseCommaList(_getValue(line));
						    }

						    // Telescope latitude (added by MFO, June 13, 2002) 
						    if(line.startsWith(TELESCOPE_LATITUDE)) {
                                                       info.telescopeLatitude = _getValue(line);
						    }
						    if(line.startsWith(SCHEMA_LOCATION)) {
                                                       info.schemaLocation = _getValue(line);
						    }
	    }

	    // Add the last config class
	    if (tfc != null) tpeFeatureV.addElement(tfc);
	    if (sic != null) spItemV.addElement(sic);

	} finally {
	    if (br != null) br.close();
	}

	// SdW - Add a check to see if a personal nameresolver exists.
	if (System.getProperty("OT_CATALOG_FILE") != null) {
	    File cFile = new File (System.getProperty("OT_CATALOG_FILE"));
	    if (cFile.exists() && cFile.isFile() && cFile.canRead()) {
		String oldNames [] = info.nameResolvers;
		String [] newNames = new String [oldNames.length+1] ;
		newNames = oldNames;
		newNames[newNames.length-1] = "Personal Catalog";
		info.nameResolvers = newNames;
	    }
	}
	// END

	// Copy all the TpeImageFeature configurations into an array in the
	// OtCfg.Info return class.
	info.tpeFeatureCfgA = new OtCfg.TpeFeatureCfg[tpeFeatureV.size()];
	tpeFeatureV.copyInto(info.tpeFeatureCfgA);

	// Copy all the SpItem configurations into an array in the OtCfg.Info
	// return class.
	info.spItemCfgA     = new OtCfg.SpItemCfg[spItemV.size()];
	spItemV.copyInto(info.spItemCfgA);

	return info;
    }

    //
    // Get the value part of an "attribute: value" line of the config file.
    //
    private static String _getValue(String line) {
	// Get the value
	int i = line.indexOf(':');
	if (i == -1) return null;
	return line.substring(i+1).trim();
    }

    //
    // Break a comma separated list of values into an array of strings.
    //
    private static String[] _parseCommaList(String list) {
	StringTokenizer st = new StringTokenizer(list, ",", false);
	String[] values = new String[ st.countTokens() ];
	int i = 0;
	while (st.hasMoreTokens()) {
	    values[i++] = st.nextToken().trim();
	}
	return values;
    }
}

