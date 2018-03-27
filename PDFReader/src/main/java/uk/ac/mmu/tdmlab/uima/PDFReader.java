package uk.ac.mmu.tdmlab.uima;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.CasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class PDFReader extends CasCollectionReader_ImplBase
{

  /**
   * The path to the directory containing pdfs. Will pick up all files matching
   * the pattern "DIRECTORY_PATH/*.pdf"
   */
  public static final String PARAM_DIRECTORY = "directoryParam";
  @ConfigurationParameter(name = PARAM_DIRECTORY, mandatory = true)
  private String             directoryParam;

  private List<String>       pdfs;
  private int                counter;

  @Override
  public void initialize(UimaContext context)
      throws ResourceInitializationException
  {
    File directory = new File(directoryParam);

    if (!directory.exists())
      throw new ResourceInitializationException(new FileNotFoundException(
          "The directory: " + directory + " does not exist"));

    if (!directory.isDirectory())
      throw new ResourceInitializationException(
          new Exception("The file: " + directory + " is not a directory"));

    pdfs = Arrays.asList(directory.list(new FilenameFilter()
    {

      @Override
      public boolean accept(File dir, String name)
      {
        return name.matches("^.*\\.pdf$");
      }
    }));

    counter = 0;

  }

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException
  {
    String pdf = pdfs.get(counter);
    counter++;

    File file = new File(directoryParam + "/" + pdf);

    PDFParser parser = new PDFParser(
        new RandomAccessBufferedFileInputStream(new FileInputStream(file)));
    parser.parse();
    COSDocument cosDoc = parser.getDocument();
    PDFTextStripper pdfStripper = new PDFTextStripper();
    PDDocument pdDoc = new PDDocument(cosDoc);
    String parsedText = pdfStripper.getText(pdDoc);

    aCAS.setDocumentText(parsedText);
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException
  {
    return counter < pdfs.size();
  }

  @Override
  public Progress[] getProgress()
  {
    return new Progress[] {
        new ProgressImpl(counter, pdfs.size(), Progress.ENTITIES) };
  }

}
