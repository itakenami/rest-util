package client.crud;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import api.wadl.annotation.XMLCast;
import client.exception.ValidationError;
import client.exception.ValidationException;
import client.request.PlayRequest;
import client.request.Request;
import client.response.JSONResponse;
import client.response.Response;
import client.response.XMLResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author itakenami
 */
public class Service<T> {

    private Request request;
    private Type type;
    private Class objclass;
    private Map<String, Class> alias;

    public Service(Request request, Class objclass, Type type_collection) {
        this.request = request;
        this.objclass = objclass;
        this.type = type_collection;
        this.alias = new HashMap<String, Class>();
    }

    private void configAlias() {

        /*
        if (objclass.isAnnotationPresent(XMLCast.class)) {
            XMLCast cast = (XMLCast) objclass.getAnnotation(XMLCast.class);
            System.out.println("ADICIONANDO PRINCIPAL: " + cast.thisClassFrom() + " PARA " + objclass);
            addAlias(cast.thisClassFrom(), objclass);
        }*/
        
        Field[] campos = objclass.getDeclaredFields();
        for (int x = 0; x < campos.length; x++) {

            Type type = campos[x].getGenericType();
            if (type instanceof ParameterizedType) {
                
                ParameterizedType pType = (ParameterizedType) type;
                Class cls = (Class)pType.getActualTypeArguments()[0];                
                
                if (cls.isAnnotationPresent(XMLCast.class)) {
                    XMLCast cast = (XMLCast) cls.getAnnotation(XMLCast.class);
                    addAlias(cast.thisClassFrom(), cls);
                }
            }

            /*
            if (campos[x].isAnnotationPresent(XMLCast.class)) {
                XMLCast cast = (XMLCast) campos[x].getAnnotation(XMLCast.class);
                System.out.println("ADICIONANDO: " + cast.thisClassFrom() + " PARA " + campos[x].getClass());
                addAlias(cast.thisClassFrom(), campos[x].getClass());
            }*/



        }
    }

    /*
     * public Service(Request request, Class objclass, Type type_collection,
     * String strclass) { this.request = request; this.objclass = objclass;
     * this.type = type_collection; this.alias = new HashMap<String, Class>();
     * addAlias(strclass, objclass); }
     */
    public void addAlias(String key, Class value) {
        alias.put(key, value);
    }

    private void injectAlias(XMLResponse resp) {
        Iterator<String> its = alias.keySet().iterator();
        while (its.hasNext()) {
            String key = its.next();
            resp.addAlias(key, alias.get(key));
        }
    }

    public Request getRequest() {

        return request;
    }

    public List<T> findAll() {

        try {

            String json = request.get();

            Response<T> response;

            if (request.isXML()) {
                configAlias();
                response = new XMLResponse<T>(json);
                injectAlias((XMLResponse) response);
            } else {
                response = new JSONResponse<T>(json);
            }

            if (response.isOK()) {
                return response.getCollectionContent(type);
            } else {
                return null;
            }


        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public T findById(Long id) {

        try {

            String json = request.get("/" + id);


            Response<T> response;
            if (request.isXML()) {
                configAlias();
                response = new XMLResponse<T>(json);
                injectAlias((XMLResponse) response);
            } else {
                response = new JSONResponse<T>(json);
            }


            if (response.isOK()) {
                return response.getContent(objclass);
            } else {
                return null;
            }


        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean delete(Long id) {

        try {

            String json = request.delete("/" + id);
            Response<T> response;
            if (request.isXML()) {
                configAlias();
                response = new XMLResponse<T>(json);
                injectAlias((XMLResponse) response);
            } else {
                response = new JSONResponse<T>(json);
            }

            if (response.isOK()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            return false;
        }
    }

    public T save(Map<String, String> obj) {
        return save(null, obj);
    }

    public T save(Long id, Map<String, String> obj) {

        String json;
        Response<T> response;

        try {
            if (id == null) {
                json = request.post(obj);
                if (request.isXML()) {
                    configAlias();
                    response = new XMLResponse<T>(json);
                    injectAlias((XMLResponse) response);
                } else {
                    response = new JSONResponse<T>(json);
                }
            } else {
                json = request.put("/" + id, obj);
                if (request.isXML()) {
                    configAlias();
                    response = new XMLResponse<T>(json);
                    injectAlias((XMLResponse) response);
                } else {
                    response = new JSONResponse<T>(json);
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erro LOCAL ao obter URL: " + ex.getMessage());
        }

        if (response.isOK()) {

            try {
                return response.getContent(objclass);
            } catch (Exception ex) {
                throw new RuntimeException("Erro LOCAL ao converter objeto: " + ex.getMessage());
            }


        } else {

            if (response.VALIDATIONERROR()) {

                if (request.isXML()) {
                    throw new ValidationException(((XMLResponse<T>) response).getResultException());
                } else {
                    List<ValidationError> err = (new Gson()).fromJson(response.getException(), new TypeToken<List<ValidationError>>() {}.getType());
                    throw new ValidationException(err);
                }
            } else {
                throw new RuntimeException("Erro REMOTO ao salvar: " + response.getException());
            }


        }


    }
}
