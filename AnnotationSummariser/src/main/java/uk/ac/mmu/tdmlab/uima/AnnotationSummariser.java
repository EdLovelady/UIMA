package uk.ac.mmu.tdmlab.uima;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class AnnotationSummariser extends JCasAnnotator_ImplBase
{

  private final String                              CONSOLE_OUPUT   = "";
  private final String                              FULL_OUPUT      = "0";

  /**
   * The types to detect and summarise. Mandatory. Must provide an array of
   * fully qualified type names (e.g., uk.ac.mmu.tdmlab.uima.MyType)
   */
  public static final String                        PARAM_TYPE_LIST =
      "typeListParam";
  @ConfigurationParameter(name = PARAM_TYPE_LIST, mandatory = true)
  private String[]                                  typeListParam;

  /**
   * The name of the file to write the output to. The default value (the empty
   * string) will result in output being directed to the console
   */
  public static final String                        PARAM_FILE_NAME =
      "fileNameParam";
  @ConfigurationParameter(name = PARAM_FILE_NAME, mandatory = false,
      defaultValue = CONSOLE_OUPUT)
  private String                                    fileNameParam;

  /**
   * The top N annotations will be output in the aggregated summary. Setting to
   * the default value (0) will cause all annotations to be output
   */
  public static final String                        PARAM_TOP_N     =
      "topNParam";
  @ConfigurationParameter(name = PARAM_TOP_N, mandatory = false,
      defaultValue = FULL_OUPUT)
  private int                                       topNParam;

  // describes how frequently each type occurs with each annotation
  private HashMap<String, HashMap<String, Integer>> occurrences;

  public void initialize(UimaContext aContext)
      throws ResourceInitializationException
  {
    super.initialize(aContext);

    occurrences = new HashMap<String, HashMap<String, Integer>>();
  }

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException
  {

    List<String> typeNames = new ArrayList<String>();
    List<Type> types = new ArrayList<Type>();
    for (String typeName : typeListParam)
    {
      Type type = jCas.getTypeSystem().getType(typeName);
      if (type != null)
      {
        types.add(type);
        typeNames.add(type.getName());
      } else
        throw new AnalysisEngineProcessException(new Exception("The type '"
            + typeName + "' could not be found in the type system."));
    }

    Collection<TOP> allAnnotations = JCasUtil.selectAll(jCas);
    for (TOP top : allAnnotations)
    {
      Type topType = top.getType();
      String topTypeName = topType.getName();
      if (typeNames.contains(topTypeName))
      {
        HashMap<String, Integer> typeOccurrence = occurrences.get(topTypeName);
        if (typeOccurrence == null)
        {
          typeOccurrence = new HashMap<String, Integer>();
          occurrences.put(topTypeName, typeOccurrence);
        }
        String text = ((Annotation) (top)).getCoveredText();
        Integer frequency = typeOccurrence.get(text);
        if (frequency == null)
        {
          frequency = 0;
        }
        frequency++;

        typeOccurrence.put(text, frequency);
      }
    }
  }// process

  @Override
  public void collectionProcessComplete() throws AnalysisEngineProcessException
  {
    PrintWriter out;
    if (fileNameParam.equals(CONSOLE_OUPUT))
    {
      out = new PrintWriter(System.out);
    } else
    {
      try
      {
        out = new PrintWriter(fileNameParam);
      } catch (FileNotFoundException fnfe)
      {
        throw new AnalysisEngineProcessException(fnfe);
      }
    }
    
    for (String typeName : occurrences.keySet())
    {
      out.println(typeName);
      HashMap<String, Integer> typeOccurrence = occurrences.get(typeName);
      Set<Entry<String, Integer>> keySet = typeOccurrence.entrySet();
      List<Entry<String, Integer>> sortedEntries =
          new ArrayList<Entry<String, Integer>>(keySet);

      Collections.sort(sortedEntries, new Comparator<Entry<String, Integer>>()
      {
        @Override
        public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
        {
          return o2.getValue() - o1.getValue();
        }
      });
      
      int counter = 0;
      int max = topNParam;
      if(max == 0)
        max = sortedEntries.size();

      for (Entry<String, Integer> entry : sortedEntries)
      {
        out.println(entry.getKey() + " --> " + entry.getValue());
        counter++;
        if(counter == max)
          break;
      }
      out.println();
    }
    out.close();
  }

}
