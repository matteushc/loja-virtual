package br.edu.devmedia.loja_virtual;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edu.devmedia.loja_virtual.bo.MockBO;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class MockListActivity extends Activity{

	
	private MockBO mockBO;
	private List<PessoaDTO> lista; 
	private ListView lstPessoas;
	private Long posicao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_mock);
		setTitle("Listagem de Pessoas");
		
		mockBO = new MockBO(this);
		new LoadingAsync().execute();
//		listarPessoas();
		
	}

	public void novoCadastro(View view){
		Intent intent = new Intent(this, MockActivity.class);
		startActivity(intent);
		finish();
	}
	
	private class LoadingAsync extends AsyncTask<Void, Void, List<PessoaDTO>>{
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected List<PessoaDTO> doInBackground(Void... params) {
			return listarPessoas();
		}
		
		@Override
		protected void onPostExecute(List<PessoaDTO> result) {
			adapter(result);
		}
	}

	private List<PessoaDTO> listarPessoas() {
		lista = mockBO.listarPessoas();
		
		lstPessoas = (ListView) findViewById(R.id.lst_pessoas);
		
		return lista;
	}
	
	private void adapter(List<PessoaDTO> listaPessoas){
		
		List<CharSequence> valores = new ArrayList<CharSequence>();
		for (PessoaDTO pessoaDTO : listaPessoas) {
			valores.add(pessoaDTO.getNome());
		}
		
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, valores);
		lstPessoas.setAdapter(adapter);
		
		lstPessoas.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				setPosicao(id);
				return false;
			}
			
		});
		
		lstPessoas.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 1, 1, "Editar");
				menu.add(0, 2, 2, "Deletar");
			}
		});
		
		lstPessoas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int id, long position) {
				PessoaDTO pessoaDTO = lista.get((int)position);
//				PessoaDTO pessoaDTO = mockBO.consultarPessoaPorId(id + 1);
				
				String msg = "Nome: " + pessoaDTO.getNome() + "Endereço: " + pessoaDTO.getEndereco() +
						"\nCpf : " + pessoaDTO.getCpf();
				
				MensagemUtil.addMsgOk(MockListActivity.this, "INFO", msg, R.drawable.info);
			}
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			PessoaDTO pessoaDTO = lista.get(posicao.intValue());
			
			Intent i = new Intent(this, MockEditActivity.class);
			i.putExtra("pessoa", pessoaDTO);
			startActivity(i);
			finish();
			break;
		case 2:
			MensagemUtil.addMsgConfirm(this, "Alerta", "Deseja realmente remover esta pessoa?", 
					R.drawable.ic_launcher, new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							PessoaDTO pessoaDTO = lista.get(posicao.intValue());
							mockBO.removerPessoaPorId(pessoaDTO.getIdPessoa());
							MensagemUtil.addMsg(MockListActivity.this, "Pessoa removida com sucesso");
							
							Intent i = new Intent(MockListActivity.this, MockListActivity.class);
							startActivity(i);
							finish();
						}
					});
			
			
			
			break;
		}
		return super.onContextItemSelected(item);
	}

	public Long getPosicao() {
		return posicao;
	}

	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}
	
	
	
}
