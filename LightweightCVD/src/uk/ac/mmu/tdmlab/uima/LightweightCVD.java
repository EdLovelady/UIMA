package uk.ac.mmu.tdmlab.uima;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.tools.cvd.MainFrame;

public class LightweightCVD
{
  public static void launchCVD(CAS cas)
      throws InterruptedException, InvocationTargetException,
      CASRuntimeException, ResourceInitializationException
  {
    SwingUtilities.invokeAndWait(new Runnable()
    {
      public void run()
      {
        MainFrame frame = new MainFrame(null);
        frame.pack();
        frame.setVisible(true);
        frame.setCas(cas);
        frame.handleSofas();
        frame.updateIndexTree(true);
        frame.setRunOnCasEnabled();
        frame.setEnableCasFileReadingAndWriting();
        frame.setTypeSystemViewerEnabled(true);
      }
    });
  }}




