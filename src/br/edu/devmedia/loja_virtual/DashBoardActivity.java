package br.edu.devmedia.loja_virtual;

import br.edu.devmedia.loja_virtual.DashBoardListActivity;
import br.edu.devmedia.loja_virtual.MockActivity;
import br.edu.devmedia.loja_virtual.MockListActivity;
import br.edu.devmedia.loja_virtual.R;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DashBoardActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		if(getIntent().getExtras() != null && getIntent().getExtras().getString("msg") != null){
			String msg = getIntent().getExtras().getString("msg");
			MensagemUtil.addMsg(DashBoardActivity.this, msg);
		}
		
	}
	
	public void cadastrar(View v){
		Intent intent = new Intent(this, MockActivity.class);
		startActivity(intent);
	}
	
	public void listarProdutos(View v){
		Intent intent = new Intent(this, ListaProdutosActivity.class);
		startActivity(intent);
	}
	
	public void listarPessoas(View v){
		Intent i = new Intent(this, MockListActivity.class);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Lista");
		menu.add(0, 2, 2, "Cadastro");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case 1:
			Intent intent = new Intent(this, DashBoardListActivity.class);
			startActivity(intent);
			finish();
			break;
		case 2:
			intent = new Intent(this, MockActivity.class);
			startActivity(intent);
			finish();
			break;
		}	
		
		return true;
	}
}
