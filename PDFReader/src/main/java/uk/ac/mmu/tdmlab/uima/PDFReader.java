package uk.ac.mmu.tdmlab.uima;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.CasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

public class PDFReader extends CasCollectionReader_ImplBase
{

  /**
   * The path to the directory containing pdfs. Will pick up all files matching
   * the pattern "DIRECTORY_PATH/*.pdf"
   */
  public static final String                        PARAM_DIRECTORY =
      "directoryParam";
  @ConfigurationParameter(name = PARAM_DIRECTORY, mandatory = true)
  private String                                  directoryParam;

  List<File> pdfs;
  int counter;
  
  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException
  {
    
  }

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException
  {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean hasNext() throws IOException, CollectionException
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Progress[] getProgress()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
