package main.java.constants;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;

public class ParseUtils {

    /**
     * Parse strings from XML entries.
     *
     * @param plist_name    name of the plist file
     * @param resource_name name of the requested resource
     * @return parsed content (string)
     */
    public static String parseString(String plist_name, String resource_name) {

        try {
            Document XMLTree = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().parse(ParseUtils.class.getResourceAsStream(plist_name));
            XMLTree.getDocumentElement().normalize();

            return XMLTree.getElementsByTagName(resource_name).item(0).getTextContent();
        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        }

        return null;

    }

    /**
     * Parse integer from XML entries, using parseString
     * as a helper method.
     *
     * @param plist_name    name of the plist file
     * @param resource_name name of the requested resource
     * @return parsed content (integer)
     */
    public static int parseInt(String plist_name, String resource_name) {
        return Integer.parseInt(Objects.requireNonNull(parseString(plist_name, resource_name)));
    }

    /**
     * Parse float from XML entries, using parseString
     * as a helper method.
     *
     * @param plist_name    name of the plist file
     * @param resource_name name of the requested resource
     * @return parsed content (float)
     */
    public static float parseFloat(String plist_name, String resource_name) {
        return Float.parseFloat(parseString(plist_name, resource_name));
    }


}
