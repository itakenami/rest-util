package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ResultMsg {

    private static ResultMsg instance;
    private HashMap<Integer, String> MSGS;

    private ResultMsg() {

        MSGS = new HashMap<Integer, String>();
        MSGS.put(0, "Requisição Realizada com Sucesso");
        MSGS.put(1, "Exceção de Sistema");
        MSGS.put(2, "Exceção de Validação");

        try {

            File f = new File("conf/result_messages.properties");

            if (f.exists()) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(f));
                Set<Object> keys = prop.keySet();
                Iterator<Object> it = keys.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    MSGS.put(Integer.parseInt(key), prop.getProperty(key));
                }
            } else {
                //Cria um objeto da classe java.util.Properties
                Properties properties = new Properties();

                //setando as propriedades(key) e os seus valores(value)
                properties.setProperty("3", "Mensagem customizada 3");
                properties.setProperty("4", "Mensagem customizada 4");

                try {
                    
                    (new File("conf")).mkdir();
                    
                    String caminho = (new File(".")).getAbsolutePath();
                    
                    System.out.println("Arquivo de configuração [conf/result_messages.properties] não encontrado. Criando arquivo em "+caminho);
                    FileWriter fos = new FileWriter("conf/result_messages.properties",false);
                    properties.store(fos, "");
                    //fecha o arquivo
                    fos.close();
                } catch (IOException ex) {
                    System.out.println("Não foi possivel criar o arquivo de mensagem.");
                }
            }

        } catch (Exception ex) {
            System.out.println("Erro ao tentar carregar ou criar arquivo conf/result_messages.properties.");
        }

    }

    public static ResultMsg getInstance() {
        if (instance == null) {
            instance = new ResultMsg();
        }
        return instance;
    }

    public String getMessage(int num) {
        if (MSGS.containsKey(num)) {
            return MSGS.get(num);
        } else {
            return "Mensagem não definida no arquivo conf/result_messages.properties";
        }

    }
}
