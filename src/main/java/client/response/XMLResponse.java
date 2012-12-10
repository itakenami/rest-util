package client.response;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import client.exception.ValidationError;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author itakenami
 */
public class XMLResponse<T> implements Response<T> {

    private String xml;
    private Class objclass;
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private ByteArrayInputStream is;
    private Document doc;
    private Element elem;
    private Map<String, Class> alias;

    class ResultObjCollection {

        public int Return;
        public List<T> Content;
        public String Exception;
    }

    class ResultObj {

        public int Return;
        public T Content;
        public String Exception;
    }

    class ResultException {

        public int Return;
        public String Content;
        public List<play.data.validation.Error> Exception;
    }

    class ResultClientException {

        public int Return;
        public String Content;
        public List<ValidationError> Exception;
    }

    public XMLResponse(String xml) {

        this.xml = xml;
        this.alias = new HashMap<String, Class>();

        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            is = new ByteArrayInputStream(xml.getBytes());
            doc = db.parse(is);
            elem = doc.getDocumentElement();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao cerregar XML");
        }

    }

    public void addAlias(String key, Class value) {
        alias.put(key, value);
    }

    private String getField(String field) {

        try {
            NodeList nl = elem.getElementsByTagName(field);
            Element child = (Element) nl.item(0);
            String out = child.getFirstChild().getNodeValue();
            return out;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Nao foi possivel obter o campo " + field + ". XML de retorno: " + xml);
        }


    }

    @Override
    public boolean isOK() {
        try {
            String cod_result = getField("Return");
            if ("0".equals(cod_result)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean SYSERROR() {
        try {
            String cod_result = getField("Return");
            if ("1".equals(cod_result)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean VALIDATIONERROR() {
        try {
            String cod_result = getField("Return");
            if ("2".equals(cod_result)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getException() {
        try {
            if (!isOK()) {
                return getField("Exception");
            } else {
                return "Não foi possivel obter o campo Exception da requisição";
            }
        } catch (Exception ex) {
            return "Não foi possivel obter o campo Exception da requisição";
        }
    }

    @Override
    public String getContent() throws Exception {
        if (!isOK()) {
            throw new Exception("A solicitação não foi executada com sucesso.");
        }
        return getField("Content");
    }

    @Override
    public T getContent(Class<?> c) throws Exception {

        if (!isOK()) {
            throw new Exception("A solicitação não foi executada com sucesso.");
        }

        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("Result", ResultObj.class);

            Iterator<String> its = alias.keySet().iterator();
            while (its.hasNext()) {
                String key = its.next();
                xstream.alias(key, alias.get(key));
            }

            ResultObj result = (ResultObj) xstream.fromXML(xml);
            return result.Content;
        } catch (Exception ex) {
            throw new Exception("Erro ao fazer o parse do objeto: " + ex.getMessage());
        }


    }

    @Override
    public List<T> getCollectionContent(Type c) throws Exception {

        if (!isOK()) {
            throw new Exception("A solicitação não foi executada com sucesso.");
        }

        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("Result", ResultObjCollection.class);

            Iterator<String> its = alias.keySet().iterator();
            while (its.hasNext()) {
                String key = its.next();
                xstream.alias(key, alias.get(key));
            }

            ResultObjCollection result = (ResultObjCollection) xstream.fromXML(xml);
            return result.Content;
        } catch (Exception ex) {
            throw new Exception("Erro ao fazer o parse do objeto: " + ex.getMessage());
        }
    }

    public List<ValidationError> getResultException() {

        XStream xstream = new XStream(new DomDriver());
        xstream.alias("Result", ResultException.class);
        xstream.alias("client.exception.ValidationError", client.exception.ValidationError.class);
        ResultClientException result = (ResultClientException) xstream.fromXML(xml);
        return result.Exception;

    }
}
