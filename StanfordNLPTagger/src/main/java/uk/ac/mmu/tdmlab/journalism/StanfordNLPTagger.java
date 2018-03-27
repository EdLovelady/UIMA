package uk.ac.mmu.tdmlab.journalism;

import java.util.List;
import java.util.Properties;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import uk.ac.mmu.tdmlab.journalism.Location;
import uk.ac.mmu.tdmlab.journalism.Organisation;
import uk.ac.mmu.tdmlab.journalism.Person;
import uk.ac.mmu.tdmlab.journalism.Time;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StanfordNLPTagger extends JCasAnnotator_ImplBase
{
  // the NER tagger from StanfordNLP
  private StanfordCoreNLP pipeline;

  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException
  {
    super.initialize(context);

    // set up tagger
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
    pipeline = new StanfordCoreNLP(props);
  }

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException
  {
    // get document text and annotate using tagger
    CoreDocument document = new CoreDocument(jcas.getDocumentText());
    pipeline.annotate(document);

    List<CoreSentence> sentences = document.sentences();

    // convert tagger output to UIMA annotations
    for (CoreSentence sentence : sentences)
    {
      List<CoreEntityMention> entities = sentence.entityMentions();

      for (CoreEntityMention entity : entities)
      {
        // System.out.println(entity + " -> " + entity.entityType());

        if (entity.entityType().equals("PERSON"))
        {
          Person personEntity = new Person(jcas, entity.charOffsets().first(),
              entity.charOffsets().second());
          jcas.addFsToIndexes(personEntity);
        } else if (entity.entityType().equals("ORGANIZATION"))
        {
          Organisation organisationEntity = new Organisation(jcas,
              entity.charOffsets().first(), entity.charOffsets().second());
          jcas.addFsToIndexes(organisationEntity);
        } else if (entity.entityType().equals("COUNTRY")
            || entity.entityType().equals("CITY")
            || entity.entityType().equals("LOCATION"))
        {
          Location locationEntity = new Location(jcas,
              entity.charOffsets().first(), entity.charOffsets().second());
          jcas.addFsToIndexes(locationEntity);
        } else if (entity.entityType().equals("DATE")
            || entity.entityType().equals("TIME")
            || entity.entityType().equals("DURATION"))
        {
          Time timeEntity = new Time(jcas, entity.charOffsets().first(),
              entity.charOffsets().second());
          jcas.addFsToIndexes(timeEntity);
        } else
        {
//          System.out.println(
//              "IGNORED: " + entity.entityType() + " -> " + entity.text());
        }
      }
    }
  }
}
