import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import uk.ac.mmu.tdmlab.journalism.Location;
import uk.ac.mmu.tdmlab.journalism.Person;
import uk.ac.mmu.tdmlab.uima.AnnotationSummariser;

public class TestAnnotationSummariser
{
  public static void main(String[] args) throws Exception
  {
    JCas jCas = JCasFactory.createJCas();

    jCas.setDocumentText("Hello World Hello World Hello World");
    
    jCas.addFsToIndexes(new Location(jCas, 0, 5));
    jCas.addFsToIndexes(new Location(jCas, 12, 17));
    jCas.addFsToIndexes(new Location(jCas, 7, 10));
    jCas.addFsToIndexes(new Person(jCas, 7, 10));
    jCas.addFsToIndexes(new Person(jCas, 20, 23));
    jCas.addFsToIndexes(new Person(jCas, 15, 20));
    
    AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
        AnnotationSummariser.class, AnnotationSummariser.PARAM_FILE_NAME,
        "src/main/resources/summary.txt", AnnotationSummariser.PARAM_TYPE_LIST,
        new String[] { "uk.ac.mmu.tdmlab.journalism.Person",
            "uk.ac.mmu.tdmlab.journalism.Location" },
        AnnotationSummariser.PARAM_TOP_N, 2 );

    analysisEngine.process(jCas);
    analysisEngine.collectionProcessComplete();
  }
}
