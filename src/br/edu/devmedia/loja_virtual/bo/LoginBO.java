package br.edu.devmedia.loja_virtual.bo;

import android.content.Context;
import br.edu.devmedia.loja_virtual.R;
import br.edu.devmedia.loja_virtual.custom.model.Usuario;
import br.edu.devmedia.loja_virtual.dominio.ValidacaoLogin;
import br.edu.devmedia.loja_virtual.util.WebServiceUtil;

public class LoginBO {

	private Context context;
	
//	private LoginOpenHelper loginOpenHelper;
	
	public LoginBO(Context context) {
		this.context = context;
//		loginOpenHelper = new LoginOpenHelper(context);
	}
	
	public ValidacaoLogin validarLogin(String login, String senha){
		ValidacaoLogin retorno = new ValidacaoLogin();
		Usuario usuario = null;
		if (login == null || "".equals(login)){
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_login_obg));
		} else if(senha == null || "".equals(senha)){
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_senha_obg));
		}else if((usuario = WebServiceUtil.validarLoginRest(login, senha)) != null && usuario.isLogado()){
			retorno.setValido(true);
			retorno.setMensagem(context.getString(R.string.msg_login_sucesso));
		} else{
			retorno.setValido(false);
			retorno.setMensagem(context.getString(R.string.msg_login_invalido));
		}
		retorno.setUsuario(usuario);
		return retorno;
	}
}
