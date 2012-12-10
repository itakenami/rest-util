package client.response;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author itakenami
 */
public class JSONResponse<T> implements Response<T> {

    private String json;
    private JsonParser parser;

    public JSONResponse(String json) {
        this.parser = new JsonParser();
        this.json = json;
    }

    private String getField(String field) {
        try {
            return parser.parse(json).getAsJsonObject().get(field).toString();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Nao foi possivel obter o campo " + field + ". JSON de retorno: " + json);
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
            Gson gson = new Gson();
            T obj = (T) gson.fromJson(getField("Content"), c);
            return obj;
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
            Gson gson = new Gson();
            String t = getField("Content");
            List<T> ints2 = gson.fromJson(t, c);
            return ints2;
        } catch (Exception ex) {
            throw new Exception("Erro ao fazer o parse do objeto: " + ex.getMessage());
        }
    }
}
