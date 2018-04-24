package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.security.XmlLocator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XfaXmlLocator implements XmlLocator {
    private String encoding;
    private PdfStamper stamper;
    private XfaForm xfaForm;

    public XfaXmlLocator(PdfStamper stamper) throws DocumentException, IOException {
        this.stamper = stamper;
        try {
            createXfaForm();
        } catch (Exception e) {
            throw new DocumentException(e);
        } catch (Exception e2) {
            throw new DocumentException(e2);
        }
    }

    protected void createXfaForm() throws ParserConfigurationException, SAXException, IOException {
        this.xfaForm = new XfaForm(this.stamper.getReader());
    }

    public Document getDocument() {
        return this.xfaForm.getDomDocument();
    }

    public void setDocument(Document document) throws IOException, DocumentException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(outputStream));
            this.stamper.getReader().getAcroForm().put(PdfName.XFA, this.stamper.getWriter().addToBody(new PdfStream(outputStream.toByteArray())).getIndirectReference());
        } catch (Exception e) {
            throw new DocumentException(e);
        } catch (Exception e2) {
            throw new DocumentException(e2);
        }
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
