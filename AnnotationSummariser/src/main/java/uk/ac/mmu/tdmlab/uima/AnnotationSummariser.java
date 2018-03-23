package uk.ac.mmu.tdmlab.uima;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class AnnotationSummariser extends JCasAnnotator_ImplBase
{

  private final String CONSOLE_OUPUT = "";
  private final String FULL_OUPUT = "0";
  
  /**
   * The types to detect and summarise. Mandatory. Must provide fully qualified
   * type names (e.g., uk.ac.mmu.tdmlab.uima.MyType)
   */
  public static final String PARAM_TYPE_LIST = "typeListParam";
  @ConfigurationParameter(name = PARAM_TYPE_LIST, mandatory = true)
  private String [] typeListParam;
  
  public static final String PARAM_FILE_NAME = "fileNameParam";
  @ConfigurationParameter(name = PARAM_FILE_NAME, mandatory = false, defaultValue = CONSOLE_OUPUT)
  private String fileNameParam;
  
  public static final String PARAM_TOP_N = "topNParam";
  @ConfigurationParameter(name = PARAM_TOP_N, mandatory = false, defaultValue = FULL_OUPUT)
  private int topNParam;
  
  @Override
  public void initialize(UimaContext aContext)
      throws ResourceInitializationException
  {
      super.initialize(aContext);
  }
    
  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException
  {
  }

}
