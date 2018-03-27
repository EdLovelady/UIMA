import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import uk.ac.mmu.tdmlab.uima.LightweightCVD;

import uk.ac.mmu.tdmlab.journalism.StanfordNLPTagger;

public class TestStanfordNLPTagger
{
  public static void main(String[] args) throws Exception
  {

    JCas jCas = JCasFactory.createJCas();

    jCas.setDocumentText(
        "Labour leader Jeremy Corbyn has defended his reluctance to blame Russia categorically for the Salisbury nerve agent attack, insisting the government must avoid \"hasty judgements\".");

    AnalysisEngine analysisEngine =
        AnalysisEngineFactory.createEngine(StanfordNLPTagger.class);

    analysisEngine.process(jCas);
    
    LightweightCVD.launchCVD(jCas.getCas());

  }
}
