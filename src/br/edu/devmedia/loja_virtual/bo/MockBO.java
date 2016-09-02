package br.edu.devmedia.loja_virtual.bo;

import java.util.List;

import android.content.Context;
import br.edu.devmedia.loja_virtual.dominio.ValidacaoMock;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;
import br.edu.devmedia.loja_virtual.sqlite.MockOpenHelper;
import br.edu.devmedia.loja_virtual.util.WebServiceUtil;

public class MockBO {

	private MockOpenHelper mockOpenHelper;
	
	public MockBO(Context context) {
		mockOpenHelper = new MockOpenHelper(context);
	}
	
	public ValidacaoMock cadastrarPessoa(PessoaDTO pessoaDTO){
		ValidacaoMock retorno = new ValidacaoMock();
		
//		WebServiceUtil.adicionarPessoa(pessoaDTO);
//		mockOpenHelper.cadastrar(pessoaDTO);
		retorno.setValido(true);
		retorno.setMensagem("Cadastro de Pessoa efetuado com sucesso");
		return retorno;
	}
	
	public List<PessoaDTO> listarPessoas(){
		return WebServiceUtil.listarPessoasJson();
	}
	
	public PessoaDTO consultarPessoaPorId(Integer idPessoa){
		return mockOpenHelper.consultarPessoaPorId(idPessoa);
	}
	
	public void removerPessoaPorId(Integer idPessoa){
		mockOpenHelper.removerPessoaPorId(idPessoa);
	}
	
	public void atualizarPessoa(PessoaDTO pessoaDTO){
		mockOpenHelper.atualizarPessoa(pessoaDTO);
	}
}
