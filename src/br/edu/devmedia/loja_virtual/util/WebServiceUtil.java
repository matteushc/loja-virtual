package br.edu.devmedia.loja_virtual.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import br.edu.devmedia.loja_virtual.custom.model.Usuario;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;

public class WebServiceUtil {

	public static final String HOST = "http://matteus-home.ddns.net/LoginRestful/";
	private static final String URL_LOGAR = HOST + "login/logar?usuario=%s&senha=%s";
	private static String URL_CADASTRAR = HOST + "login/cadastrar";
	private static String URL_LISTAR = HOST + "login/listar";
	
	
	public static Usuario validarLoginRest(String login, String senha){
		Usuario resultado = null;
		try {
			
			String urlString = String.format(URL_LOGAR, login, senha);
			
			resultado = getUsuario(urlString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	public static List<PessoaDTO> listarPessoasJson() {
		
		StringBuilder stringBuilder = connectAndReturnString(URL_LISTAR);
		List<PessoaDTO> list = new ArrayList<PessoaDTO>();
		try {
			
			JSONArray array = new JSONArray(stringBuilder.toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				
				PessoaDTO pessoa = new PessoaDTO();
				pessoa.setIdPessoa(jsonObject.getInt("idPessoa"));
				pessoa.setNome(jsonObject.getString("nome"));
				pessoa.setEndereco(jsonObject.getString("endereco"));
				pessoa.setCpf(jsonObject.getLong("cpf"));
				pessoa.setProfissao(jsonObject.getInt("profissao"));
				pessoa.setSexo(jsonObject.getString("sexo").charAt(0));
				list.add(pessoa);
			}
		    
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public static Usuario getUsuario(String url) {
		
		StringBuilder stringBuilder = connectAndReturnString(url);
		Usuario user = new Usuario();
		try {
			
			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			user = new Gson().fromJson(jsonObject.toString(), Usuario.class);
			
		    
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		return user;
	}
	
	public static void adicionarPessoa(PessoaDTO pessoa){
		incluir(pessoa, URL_CADASTRAR);
	}
	
	public static void incluir(Object p, String url){
		HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpResponse response;

        try {
            HttpPost post = new HttpPost(url);
            
            Gson gson = new Gson();
			String object = gson.toJson(p);
            
            StringEntity se = new StringEntity(object);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);
            
            if (response != null) {
                InputStream in = response.getEntity().getContent();
    		    boolean result = Boolean.parseBoolean(converterToString(in));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private static String converterToString(InputStream in){
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		
		StringBuilder builder = new StringBuilder();
		String linha = null;
		try {
			while ((linha = buffer.readLine()) != null) {
				builder.append(linha);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	private static StringBuilder connectAndReturnString(String uri) {
	
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
		    response = client.execute(httpGet);
		    HttpEntity entity = response.getEntity();
		    InputStream stream = entity.getContent();
		    int b;
		    while ((b = stream.read()) != -1) {
		        stringBuilder.append((char) b);
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return stringBuilder;
	}

}
