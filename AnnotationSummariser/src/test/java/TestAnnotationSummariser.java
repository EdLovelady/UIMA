import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import uk.ac.mmu.tdmlab.uima.AnnotationSummariser;

public class TestAnnotationSummariser
{
  public static void main(String[] args) throws Exception
  {
    JCas jCas = JCasFactory.createJCas();

    jCas.setDocumentText("Hello World");

    AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
        AnnotationSummariser.class, AnnotationSummariser.PARAM_FILE_NAME,
        "src/main/resources/summary.txt", AnnotationSummariser.PARAM_TYPE_LIST,
        new String[] { "uk.ac.mmu.tdmlab.journalism.Person",
            "uk.ac.mmu.tdmlab.journalism.Location" },
        AnnotationSummariser.PARAM_TOP_N, 5 );

    analysisEngine.process(jCas);
  }
}
