package br.edu.devmedia.loja_virtual;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import br.edu.devmedia.loja_virtual.bo.MockBO;
import br.edu.devmedia.loja_virtual.comum.Profissao;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class MockEditActivity extends Activity{

	private MockBO mockBO;
	
	private EditText edtNome; 
	private EditText edtEndereco;
	private EditText edtCPF;
	private Spinner spnProfissao;
	private RadioGroup rgpSexo;
	private RadioButton rbtMasc;
	private RadioButton rbtFem;
	private Button btnAtualizar = null;
	
	private PessoaDTO pessoaDTO;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro_mock);
		setTitle("Atualizar Pessoa");
		
		mockBO = new MockBO(this);
		initEdit();
		montarEdicao();
		
	}
	
	private void initEdit(){
		pessoaDTO = (PessoaDTO) getIntent().getExtras().get("pessoa");
		
		edtNome = (EditText) findViewById(R.id.edt_nome);
		edtEndereco = (EditText) findViewById(R.id.edt_endereco);
		edtCPF = (EditText) findViewById(R.id.edt_cpf);
		spnProfissao = (Spinner) findViewById(R.id.spn_profissao);
		rgpSexo = (RadioGroup) findViewById(R.id.rgp_sexo);
		rbtMasc = (RadioButton) findViewById(R.id.rbt_masculino);
		rbtFem = (RadioButton) findViewById(R.id.rbt_feminino);
		btnAtualizar = (Button) findViewById(R.id.btn_cadastrar);
		
		List<String> valores = new ArrayList<String>();
		for(Profissao profissao : Profissao.values()){
			valores.add(profissao.getDescricao());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnProfissao.setAdapter(adapter);
		
		btnAtualizar.setText("Atualizar");
		btnAtualizar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				atualizar();
			}
		});
	}
	
	private void montarEdicao(){
		
		edtNome.setText(pessoaDTO.getNome());
		edtEndereco.setText(pessoaDTO.getEndereco());
		edtCPF.setText(pessoaDTO.getCpf().toString());
		spnProfissao.setSelection(pessoaDTO.getProfissao() - 1);
		
		if(pessoaDTO.getSexo() == 'M'){
			rbtMasc.setChecked(true);
		}else{
			rbtFem.setChecked(true);
		}
	}
	
	private void atualizar(){
		PessoaDTO pessoa = new PessoaDTO();
		pessoa.setIdPessoa(pessoaDTO.getIdPessoa());
		pessoa.setNome(edtNome.getText().toString());
		pessoa.setEndereco(edtEndereco.getText().toString());
		pessoa.setCpf(Long.parseLong(edtCPF.getText().toString()));
		pessoa.setProfissao(spnProfissao.getSelectedItemPosition() + 1);
		pessoa.setSexo(rbtFem.isChecked() ? 'F' : 'M');
		
		mockBO.atualizarPessoa(pessoa);
		
		MensagemUtil.addMsg(this, "Pessoa atualizada com sucesso!");
		
		Intent i = new Intent(this, MockListActivity.class);
		startActivity(i);
		finish();
	}

	
	
}
