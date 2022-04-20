/**
 * 
 * @author Roger Johnson
 *
 * @date 4/24/2016
 *
 * @info Course COP4601
 */
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class XmlBase implements IXmlSerializable {
    //region IXmlSerializable
    @Override
    public void ReadXml(String text) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db= dbf.newDocumentBuilder();

            Document _doc = db.parse(new InputSource(new StringReader(text)));

            Element ele = _doc.getDocumentElement();

            ProcessElement(ele);

        } catch (Exception exc) {
            Logger.Error("ReadXml: " + exc.getMessage());
        }
    }

    @Override
    public String WriteXml() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (Exception exc) {
            Logger.Error("WriteXml: " + exc.getMessage());
            return "";
        }

        // create instance of DOM
        Document doc = db.newDocument();

        // Get root element from implementation
        Element rootEle = BuildElement(doc);

        // Place into document
        doc.appendChild(rootEle);

        // Build string output
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (Exception exc) {
            Logger.Error("WriteXml: " + exc.getMessage());
            return "";
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (Exception exc) {
            Logger.Error("WriteXml: " + exc.getMessage());
        }
        return writer.getBuffer().toString();
    }
    //endregion

    //region Protected
    protected abstract void ProcessElement(Element ele);
    protected abstract Element BuildElement(Document parentDoc);
    //endregion
}
