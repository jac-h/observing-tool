/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.ukirt.util;

import gemini.sp.SpTelescopePos;
import gemini.util.CoordSys;
import orac.util.TelescopeUtil;
import orac.util.PreTranslator;
import orac.util.SpItemDOM;
import orac.validation.SpValidation;
import orac.ukirt.validation.UkirtSpValidation;

/**
 * Used for UKIRT specific features.
 *
 * @author Martin Folger
 */
public class UkirtUtil implements TelescopeUtil {

  private static final String [] COORD_SYS_FK5_FK4 = { CoordSys.COORD_SYS[CoordSys.FK5],
                                                       CoordSys.COORD_SYS[CoordSys.FK4]};


  private static final String [] CHOP_SYSTEM = { CoordSys.COORD_SYS[CoordSys.FK5] };

  private UkirtSpValidation _ukirtSpValidation = new UkirtSpValidation();


  public SpValidation getValidationTool() {
    return _ukirtSpValidation;
  }

  public String getBaseTag() {
    return SpTelescopePos.BASE_TAG;
  }

  /**
   * @return always false for UKIRT
   */
  public boolean isOffsetTarget(String targetTag) {
    return false;
  }

  public boolean supports(int feature) {
    switch(feature) {
      case FEATURE_TARGET_INFO_CHOP:        return true;
      case FEATURE_FLAG_AS_STANDARD:        return true;
      case FEATURE_TARGET_INFO_PROP_MOTION: return false;
      case FEATURE_TARGET_INFO_TRACKING:    return false;
      default:                              return false;
    }
  }

  /**
   * Sets PreTranslator in SpItemDOM.
   *
   * Make sure at the time this method is called SpTelescopePos.BASE_TAG and
   * SpTelescopePos.GUIDE_TAGS[0] are set to correct values.
   */
  public void installPreTranslator() throws Exception {
    SpItemDOM.setPreTranslator(new UkirtPreTranslator(SpTelescopePos.BASE_TAG, SpTelescopePos.GUIDE_TAGS[0]));
 }

  public String [] getCoordSys() {
    return COORD_SYS_FK5_FK4;
  }

  public String [] getCoordSysFor(String purpose) {
    if(purpose.equals(CHOP)) {
      return CHOP_SYSTEM;
    }

    return null;
  }
}

