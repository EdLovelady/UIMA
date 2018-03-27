import java.util.Arrays;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;

import uk.ac.mmu.tdmlab.uima.PDFReader;

public class TestPDFReader
{

  public static void main(String[] args) throws Exception
  {
    CollectionReader pdfReader = CollectionReaderFactory.createReader(
        PDFReader.class, PDFReader.PARAM_DIRECTORY, "src/test/resources/");
    
    while(pdfReader.hasNext())
    {
      System.out.println(Arrays.toString(pdfReader.getProgress()));
      CAS cas = JCasFactory.createJCas().getCas();
      pdfReader.getNext(cas);    
      System.out.println(cas.getDocumentText());      
    }
  }
}
