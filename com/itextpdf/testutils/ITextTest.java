package com.itextpdf.testutils;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import java.io.File;
import javax.management.OperationsException;

public abstract class ITextTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ITextTest.class.getName());

    protected abstract String getOutPdf();

    protected abstract void makePdf(String str) throws Exception;

    public void runTest() throws Exception {
        LOGGER.info("Starting test.");
        String outPdf = getOutPdf();
        if (outPdf == null || outPdf.length() == 0) {
            throw new OperationsException("outPdf cannot be empty!");
        }
        makePdf(outPdf);
        assertPdf(outPdf);
        comparePdf(outPdf, getCmpPdf());
        LOGGER.info("Test complete.");
    }

    protected void assertPdf(String outPdf) throws Exception {
    }

    protected void comparePdf(String outPdf, String cmpPdf) throws Exception {
    }

    protected String getCmpPdf() {
        return "";
    }

    protected void deleteDirectory(File path) {
        if (path != null && path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }
    }

    protected void deleteFiles(File path) {
        if (path != null && path.exists()) {
            for (File f : path.listFiles()) {
                f.delete();
            }
        }
    }
}
