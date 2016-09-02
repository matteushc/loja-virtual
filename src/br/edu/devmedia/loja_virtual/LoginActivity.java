package br.edu.devmedia.loja_virtual;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.edu.devmedia.loja_virtual.bo.LoginBO;
import br.edu.devmedia.loja_virtual.dominio.ValidacaoLogin;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class LoginActivity extends Activity {

	private LoginBO loginBO;
	
	private EditText edtLogin;
	private EditText edtSenha;
	private Button btnLogar;
	private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;
	
	private SharedPreferences preferences;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		
		callbackManager = CallbackManager.Factory.create();
		 
		setContentView(R.layout.activity_login);
		
		loginButton = (LoginButton)findViewById(R.id.btn_fblogin);
		 
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            	
            	Editor editor = preferences.edit();
				editor.putString("access_token", loginResult.getAccessToken().getToken());
				editor.putBoolean("access_expires", loginResult.getAccessToken().isExpired());
				editor.commit();
     
                Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
                i.putExtra("msg", LoginActivity.this.getString(R.string.msg_login_sucesso));
    			startActivity(i);
    			finish();
            }
     
            @Override
            public void onCancel() {
                progressDialog.dismiss();
            }
     
            @Override
            public void onError(FacebookException e) {
                progressDialog.dismiss();
            }
        });
     /* Botao comentado nao usado
		//        btnLogin.setOnClickListener(new View.OnClickListener() {
		//            @Override
		//            public void onClick(View v) {
		// 
		//                progressDialog = new ProgressDialog(LoginActivity.this);
		//                progressDialog.setMessage("Loading...");
		//                progressDialog.show();
		// 
		//                loginButton.performClick();
		// 
		//                loginButton.setPressed(true);
		// 
		//                loginButton.invalidate();
		// 
		//                loginButton.setPressed(false);
		// 
		//                loginButton.invalidate();
		// 
		//            }
		//        });
	*/	
		edtLogin = (EditText) findViewById(R.id.edt_login);
		edtSenha = (EditText) findViewById(R.id.edt_senha);
		
		btnLogar = (Button) findViewById(R.id.btn_logar);
		btnLogar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logar();
			}
		});
		
		preferences = getPreferences(MODE_PRIVATE);
		
		String token = preferences.getString("access_token", null);
		Boolean expires = preferences.getBoolean("access_expires", false);
		int usuario = preferences.getInt("usuario_id", 0);
		
		if ((token != null && !expires) || usuario != 0) {
			Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
			startActivity(i);
			finish();
		}
	}
	
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
 
 
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
 
            progressDialog.dismiss();
 
            Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
			startActivity(i);
			finish();
        }
 
        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }
 
        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };
	
	public void logar() {
		new LoadingAsync().execute();
	}
	
	
	private class LoadingAsync extends AsyncTask<Void, Void, ValidacaoLogin>{

		ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
		
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Carregando...");
			progressDialog.show();
		}
		
		
		@Override
		protected ValidacaoLogin doInBackground(Void... params) {
			String login = edtLogin.getText().toString();
			String senha = edtSenha.getText().toString();
			
			loginBO = new LoginBO(LoginActivity.this);
			
			
			return loginBO.validarLogin(login, senha);
		}
		
		
		@Override
		protected void onPostExecute(ValidacaoLogin msg) {
			progressDialog.dismiss();
			
			if(msg.isValido()){
				preferences = getPreferences(MODE_PRIVATE);
				
				Editor editor = preferences.edit();
				editor.putString("usuario_normal", msg.getUsuario().getLogin());
				editor.commit();
				
				Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
				i.putExtra("msg", msg.getMensagem());
				startActivity(i);
				finish();
			}else{
				MensagemUtil.addMsg(LoginActivity.this, msg.getMensagem());
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int idMenuItem = item.getItemId();
		switch (idMenuItem) {
		case R.id.menuSair:
			MensagemUtil.addMsgConfirm(LoginActivity.this, getString(R.string.lbl_sobre), getString(R.string.msg_sobre), 
					R.drawable.ic_launcher, new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			
			break;
		case R.id.menuSobre:
			MensagemUtil.addMsgOk(LoginActivity.this, getString(R.string.lbl_sobre), getString(R.string.msg_sobre), R.drawable.ic_launcher);
			
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
