package orac.ukirt.validation;

import gemini.sp.obsComp.SpInstObsComp;
import orac.ukirt.inst.SpInstUFTI;
import orac.validation.InstrumentValidation;
import orac.validation.ErrorMessage;
import java.util.Vector;

/**
 * Implements the validation of UFTI.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class UFTIValidation implements InstrumentValidation {

  /**
   * readoutAreas, acqModes and minExpTimes are used as follows:
   * Given readoutAreas[i] and acqModes[i] check whether the exposure time is at least minExpTimes[i].
   *
   * @see acqModes
   * @see minExpTimes
   */
  static String [] readoutAreas = {
                                   "1024x1024",
                                   "1024x1024",
				   "1024x1024",
				    "512x512",
				    "512x512",
				    "512x512",
				    "256x256",
				    "256x256",
				    "256x256"
				   };

  /**
   * @see readoutAreas
   */
  static String [] acqModes     = {
                                   "Normal+NDSTARE",
                                   "Normal+10_NDSTARE",
				   "Fast+NDSTARE",
                                   "Normal+NDSTARE",
                                   "Normal+10_NDSTARE",
				   "Fast+NDSTARE",
                                   "Normal+NDSTARE",
                                   "Normal+10_NDSTARE",
				   "Fast+NDSTARE"				   
                                  };
				  
  /**
   * @see readoutAreas
   */
  static double [] minExpTimes  = {
                                    4,
				   20,
				    2,
				    1,
				    5,
				    1.3,
				    0.3,
				    1.3,
				    0.15
                                  };


    public void checkInstrument(SpInstObsComp instObsComp, Vector report) { //throws ValidationException {
      SpInstUFTI spInstUFTI  = (SpInstUFTI)instObsComp;
      String     readoutArea = spInstUFTI.getReadoutArea();
      String     acqMode     = spInstUFTI.getAcqMode();
      double     expTime     = spInstUFTI.getExpTime();
      int        count       = readoutAreas.length;

      if(report == null) {
        report = new Vector();
      }

      if(acqMode.equals("Normal+10_NDSTARE") && (expTime > 750)) {
        report.add(new ErrorMessage(ErrorMessage.ERROR,
	                            "UFTI", "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")",
	                            "<= 750.0 sec",
				    expTime + " sec"));
	return;
      }
      
      if(acqMode.equals("Normal+NDSTARE") && (expTime > 350)) {
        report.add(new ErrorMessage(ErrorMessage.ERROR,
	                            "UFTI", "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")",
	                            "<= 350.0 sec",
				    expTime + " sec"));
	return;
      }
      
      for(int i = 0; i < count; i++) {
        if(readoutArea.equals(readoutAreas[i])) {
          if(acqMode.equals(acqModes[i])) {
            if(expTime < minExpTimes[i]) {
              report.add(new ErrorMessage(ErrorMessage.ERROR,
	                                     "UFTI", "Exposure time (with readout area: \"" + readoutArea + "\", acquisition mode: \"" + acqMode + "\")",
	                                     ">= " + minExpTimes[i] + " sec",
					     expTime + " sec"));
	      return;
	    }
	  }
	}
      }
    }
}

