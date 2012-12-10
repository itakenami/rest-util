# REST-UTIL

Biblioteca de Classes Java, que vai auxiliar na construção de uma API (Server) e Client REST. Projeto de evolução da LIB play-rest, que foi descontinuada.
Para criar o servidor (API-REST) recomenda-se o uso do PLAY_MODULE restapi. Este contem esta LIB embutida e um conjunto de ferramentas e outras classes para facilitar o desenvolvimento.
Também é possível utilizar o Arhetype Maven restee para criar uma aplicação REST-EE.

## Informações
* **Tipo:** LIB
* **Finalidade:** Auxiliar na criação de Servidores(PLAY e Java EE) e Clientes REST.
* **Gerenciamento e Automação:** Maven

## Exemplos
Clientes REST simples para fazer um POST em uma URL:
``` java
	public static void main(String[] args) {
        
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("usuario.nome", "Igor");
		params.put("usuario.email", "teste@gmail.com");
        
		Request request = new ApacheRequest("http://localhost:9000/api/usuarios");
		String result = request.post(params);
		System.out.println(result);
	}
```
A saída é:

	{"Return":0,"Content":{"nome":"Igor","email":"iotakenami@gmail.com","id":2},"Exception":""}
	
Pode-se utilizar a classe JSONResponse (ou XMLResponse) para tratar o retorno como Objeto. Para isto, primeiro é necessário criar a classe Usuario.java que representa o Objeto que será convertido e os serviços que podem ser acessados:
``` java
	@XMLCast(thisClassFrom="models.Usuario")
	public class Usuario {
    
    	public Long id;
    	public String nome;
   	 	public String email;
    
    	public static Service<Usuario> service = new Service<Usuario>(new ApacheRequest("http://localhost:9000/api/usuarios"), Usuario.class, new TypeToken<List<Usuario>>(){}.getType());
    
    	@Override
    	public String toString(){
        	return nome;
    	}
    
	}
```
Como utilizar o Model:
``` java
	public static void main(String[] args) {
        
        try {
            
            HashMap<String,String> params = new HashMap<String, String>();
            
            Request request = new ApacheRequest("http://localhost:9000/api/usuarios");
            String result = request.get("/2");
            
            Response<Usuario> response = new JSONResponse<Usuario>(result);
            
            Usuario usuario = response.getContent(Usuario.class);
            
            System.out.println("ID: "+usuario.id);
            System.out.println("Nome: "+usuario.nome);
            System.out.println("E-ail: "+usuario.email);
            
        } catch (Exception ex) {
            System.out.println("Erro:"+ex);
        }
        
    }
```	
É possivel utilizar serviços do Model para operar invocar o CRUD de forma automática:
``` java
	public static void main(String[] args) {
        try {
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("usuario.nome", "Igor");
            params.put("usuario.email", "iotakenami@gmail.com");
            
            Usuario u = new Usuario();
            Usuario.service.save(params);
            
        } catch (Exception ex) {
            System.out.println("Erro:"+ex);
        }
        
    }
```