package om.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Utility managing access to .sdf files.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class SdfFiles implements FilenameFilter {

  // fields are initialised to default values.
  private int _numberStartIndex  = 10;
  private int _numberEndIndex    = 15;
  
  private String _sdfSuffix      = ".sdf";

  private String _dataBaseDir    = "/net/alba/data/michelle/data";
  private String _dataTypeSubDir = "reduced";
  private String _dataDir        = null;

  private String [] _sdfFiles    = null;
  private String [] _sdfNumbers  = null;

  /**
   * The following system properties are used.
   *
   * DATA_BASE_DIR
   * DATA_TYPE_SUB_DIR
   * SDF_SUFFIX
   * NUMBER_START_INDEX
   * NUMBER_END_INDEX
   */
  public SdfFiles() {
    Properties properties = System.getProperties();
    
    String dataBaseDir      = properties.getProperty("DATA_BASE_DIR", _dataBaseDir);
    String dataTypeSubDir   = properties.getProperty("DATA_TYPE_SUB_DIR", _dataTypeSubDir);
    String sdfSuffix        = properties.getProperty("SDF_SUFFIX", _sdfSuffix);

    int numberStartIndex;
    try {
      numberStartIndex = Integer.parseInt(properties.getProperty("NUMBER_START_INDEX"));
    }
    catch(NumberFormatException e) {
      numberStartIndex = _numberStartIndex;
      
    }
    
    int numberEndIndex;
    try {
      numberEndIndex = Integer.parseInt(properties.getProperty("NUMBER_END_INDEX"));
    }
    catch(NumberFormatException e) {
      numberEndIndex = _numberEndIndex;
    }
    
    _init(dataBaseDir, dataTypeSubDir, sdfSuffix, numberStartIndex, numberEndIndex);
  
  }
  
  public SdfFiles(String dataBaseDir, String dataTypeSubDir, String sdfSuffix, int numberStartIndex, int numberEndIndex) {
    _init(dataBaseDir, dataTypeSubDir, sdfSuffix, numberStartIndex, numberEndIndex);
  }
  
  public void _init(String dataBaseDir, String dataTypeSubDir, String sdfSuffix, int numberStartIndex, int numberEndIndex) {
    if(dataBaseDir.endsWith("/")) {
      _dataBaseDir    = dataBaseDir.substring(0, dataBaseDir.length() - 1);
    }
    else {
      _dataBaseDir    = dataBaseDir;
    }

    if(dataBaseDir.endsWith("/")) {
      _dataTypeSubDir = dataTypeSubDir.substring(0, dataTypeSubDir.length() - 1);
    }
    else {
      _dataTypeSubDir = dataTypeSubDir;
    }

    _dataDir = _dataBaseDir + "/" + UT.getUT() + "/" + _dataTypeSubDir;
    
    _sdfSuffix        = sdfSuffix;
    _numberStartIndex = numberStartIndex;
    _numberEndIndex   = numberEndIndex;

    _init();
  }

  /**
   * This constructor sets _dataDir directly to dataDir.
   */
  public SdfFiles(String dataDir) {
    _dataDir = dataDir;
    _init();
  }

  private void _init() {
 
    String [] files = (new File(_dataDir)).list(this);
      
    if(files == null) {
      return;
    }
      
    _sdfNumbers = new String[files.length];
    _sdfFiles   = new String[files.length];
      
    for(int i = 0; i < _sdfFiles.length; i++) {
      _sdfFiles[i]   = files[i];
      _sdfNumbers[i] = files[i].substring(_numberStartIndex, _numberEndIndex);
    }
  }

  
  public String [] getAllNumberStrings() {
    return _sdfNumbers;
  }

  public String [] getAllFileNames() {
    return _sdfFiles;
  }

  public String [] getAllFilePaths() {
    String [] paths = new String[_sdfFiles.length];
   
    for(int i = 0; i < _sdfFiles.length; i++) {
      paths[i] = _dataDir + "/" + _sdfFiles[i];
    }
    
    return paths;
  }

  public String getNumberString(int index) throws FileNotFoundException {
    try {
      return _sdfNumbers[index];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      throw new FileNotFoundException("No sdf file corresponding to index " + index + ".");
    }
  }

  
  public String getFileName(int index) throws FileNotFoundException {
    try {
      return _sdfFiles[index];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      throw new FileNotFoundException("No sdf file corresponding to index " + index + ".");
    }
  }

  public String getFilePath(int index) throws FileNotFoundException {
    try {
      return _dataDir + "/" + _sdfFiles[index];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      throw new FileNotFoundException("No sdf file corresponding to index " + index + ".");
    }
  }  

  public boolean accept(File dir, String name) {
    return name.endsWith(_sdfSuffix) && name.startsWith("M");
  }

  public static void main(String [] args) {
    SdfFiles sdfFiles = new SdfFiles();
    
    String [] sdfNumbers = sdfFiles.getAllNumberStrings();
    String [] sdfFileNames = sdfFiles.getAllFileNames();
    String [] sdfPaths = sdfFiles.getAllFilePaths();

     
    for(int i = 0; i < sdfNumbers.length; i++) {
      System.out.print  ("\"" + sdfNumbers[i] + "\", ");
      System.out.print  ("\"" + sdfFileNames[i] + "\", ");
      System.out.println("\"" + sdfPaths[i] + "\"");
    }
  }
}
