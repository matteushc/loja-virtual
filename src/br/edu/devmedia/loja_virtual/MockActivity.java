package br.edu.devmedia.loja_virtual;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import br.edu.devmedia.loja_virtual.bo.MockBO;
import br.edu.devmedia.loja_virtual.comum.Profissao;
import br.edu.devmedia.loja_virtual.dominio.ValidacaoMock;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class MockActivity extends Activity{

	private EditText edtNome; 
	private EditText edtEndereco;
	private EditText edtCPF;
	private Spinner spnProfissao;
	private RadioGroup rgpSexo;
	private RadioButton rbtMasc;
	private RadioButton rbtFem;
	private MockBO mockBO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_mock);
		
		mockBO = new MockBO(this);
		
		edtNome = (EditText) findViewById(R.id.edt_nome);
		edtEndereco = (EditText) findViewById(R.id.edt_endereco);
		edtCPF = (EditText) findViewById(R.id.edt_cpf);
		spnProfissao = (Spinner) findViewById(R.id.spn_profissao);
		rgpSexo = (RadioGroup) findViewById(R.id.rgp_sexo);
		rbtMasc = (RadioButton) findViewById(R.id.rbt_masculino);
		rbtFem = (RadioButton) findViewById(R.id.rbt_feminino);
		
		List<String> valores = new ArrayList<String>();
		for(Profissao profissao : Profissao.values()){
			valores.add(profissao.getDescricao());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MockActivity.this, android.R.layout.simple_spinner_item, valores);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnProfissao.setAdapter(adapter);
	}
	
	public void cadastrar(View view){
		
		new LoadingAsync().execute();
		
	}
	
	
	private class LoadingAsync extends AsyncTask<Void, Void, ValidacaoMock>{
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected ValidacaoMock doInBackground(Void... params) {
			
			PessoaDTO pessoaDTO = new PessoaDTO();
			pessoaDTO.setNome(edtNome.getText().toString());
			pessoaDTO.setEndereco(edtEndereco.getText().toString());
			pessoaDTO.setCpf(Long.parseLong(edtCPF.getText().toString()));
			pessoaDTO.setProfissao(spnProfissao.getSelectedItemPosition() + 1);
			pessoaDTO.setSexo(rbtFem.isChecked() ? 'F' : 'M');
			
			ValidacaoMock resultado = mockBO.cadastrarPessoa(pessoaDTO);
			return resultado;
		}
		
		@Override
		protected void onPostExecute(ValidacaoMock result) {
			MensagemUtil.addMsg(MockActivity.this, result.getMensagem());
			
			Intent i = new Intent(MockActivity.this, MockListActivity.class);
			startActivity(i);
			finish();
		}
	}
	
	
}
