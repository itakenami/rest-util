/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.request;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author itakenami
 */
public class FormParam {

    private String date_format;
    private Map<String, String> params;

    public FormParam() {
        params = new HashMap<String, String>();
        date_format = "dd/MM/yyyy";
    }

    public FormParam(String name, Object obj) {
        params = new HashMap<String, String>();
        date_format = "dd/MM/yyyy";
        addObject(name, obj);
    }

    public void add(String key, String value) {
        params.put(key, value);
    }

    public void add(String key, Date value) {
        SimpleDateFormat formatBra;
        formatBra = new SimpleDateFormat(date_format);
        params.put(key, formatBra.format(value));
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void addObject(String nome, Object obj) {

        Field[] fld = obj.getClass().getDeclaredFields();
        for (int x = 0; x < fld.length; x++) {

            try {
                Object campo = fld[x].get(obj);
                String campo_nome = fld[x].getName();

                if (campo != null) {
                    if (campo instanceof String || campo instanceof Long) {
                        add(nome + "." + campo_nome, (String) campo);
                    } else {
                        if (campo instanceof Set) {
                            Set lista = (Set) campo;
                            for (Object obj_lista : lista) {
                                try {
                                    Long id = (Long) obj_lista.getClass().getDeclaredField("id").get(obj_lista);
                                    add(nome + "." + campo_nome + "[" + id + "].id", id.toString());
                                } catch (Exception ex) {
                                }

                            }
                        } else {
                            if (campo instanceof Date) {
                                add(nome + "." + campo_nome, (Date) campo);
                            } else {
                                try {
                                    Long id = (Long) campo.getClass().getDeclaredField("id").get(campo);
                                    add(nome + "." + campo_nome + ".id", id.toString());
                                } catch (Exception ex) {
                                }


                            }
                        }

                    }

                }
            } catch (Exception ex) {
            }


        }


    }
}
