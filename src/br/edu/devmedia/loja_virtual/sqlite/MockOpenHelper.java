package br.edu.devmedia.loja_virtual.sqlite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.edu.devmedia.loja_virtual.comum.Constants;
import br.edu.devmedia.loja_virtual.dto.PessoaDTO;

public class MockOpenHelper extends SQLiteOpenHelper {

	private static ResourceBundle config = ResourceBundle
			.getBundle(Constants.DB_CONFIG_PROPS, Locale.getDefault());

	public MockOpenHelper(Context context) {
		super(context, config.getString(Constants.DB_CONFIG_NOME), null, Integer.parseInt(config.getString(Constants.DB_CONFIG_VERSAO)));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" CREATE TABLE TB_PESSOA ( ");
//		sql.append(" ID_PESSOA INT PRIMARY KEY AUTOINCREMENT, ");
//		sql.append(" NOME TEXT NOT NULL, ");
//		sql.append(" ENDERECO TEXT NOT NULL, ");
//		sql.append(" CPF INT NOT NULL, ");
//		sql.append(" PROFISSAO INT NOT NULL, ");
//		sql.append(" SEXO CHAR NOT NULL) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
	
	public void cadastrar(PessoaDTO pessoaDTO){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("NOME", pessoaDTO.getNome());
		values.put("ENDERECO", pessoaDTO.getEndereco());
		values.put("CPF", pessoaDTO.getCpf());
		values.put("PROFISSAO", pessoaDTO.getProfissao());
		values.put("SEXO", String.valueOf(pessoaDTO.getSexo()));
		
		db.insert("TB_PESSOA", null, values);
	}
	
	
	public List<PessoaDTO> listar(){
		List<PessoaDTO> lista = new ArrayList<PessoaDTO>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(true, "TB_PESSOA", null, null, null, null, null, "NOME", null);
		
		while (cursor.moveToNext()) {
			PessoaDTO pessoaDTO = new PessoaDTO();
			pessoaDTO.setIdPessoa(cursor.getInt(cursor.getColumnIndex("ID_PESSOA")));
			pessoaDTO.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
			pessoaDTO.setEndereco(cursor.getString(cursor.getColumnIndex("ENDERECO")));
			pessoaDTO.setCpf(cursor.getLong(cursor.getColumnIndex("CPF")));
			pessoaDTO.setProfissao(cursor.getInt(cursor.getColumnIndex("PROFISSAO")));
//			pessoaDTO.setSexo(cursor.getString(6).charAt(0));
			
			lista.add(pessoaDTO);
		}
		
		
		return lista;
	}
	
	public PessoaDTO consultarPessoaPorId(Integer idPessoa){
		PessoaDTO pessoaDTO = new PessoaDTO();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("TB_PESSOA", null, "ID_PESSOA=?", new String[]{idPessoa.toString()}, null, null, "ID_PESSOA");
		
		while (cursor.moveToNext()) {
			pessoaDTO.setIdPessoa(cursor.getInt(cursor.getColumnIndex("ID_PESSOA")));
			pessoaDTO.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
			pessoaDTO.setEndereco(cursor.getString(cursor.getColumnIndex("ENDERECO")));
			pessoaDTO.setCpf(cursor.getLong(cursor.getColumnIndex("CPF")));
			pessoaDTO.setProfissao(cursor.getInt(cursor.getColumnIndex("PROFISSAO")));
//			pessoaDTO.setSexo(cursor.getString(6).charAt(0));
			
		}
		return pessoaDTO;
	}
	
	
	public void removerPessoaPorId(Integer idPessoa){
		SQLiteDatabase db = this.getReadableDatabase();
		
		db.delete("TB_PESSOA", "ID_PESSOA=?", new String[]{idPessoa.toString()});
		
	}
	
	public void atualizarPessoa(PessoaDTO pessoaDTO){
		SQLiteDatabase db = this.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("NOME", pessoaDTO.getNome());
		values.put("ENDERECO", pessoaDTO.getEndereco());
		values.put("CPF", pessoaDTO.getCpf());
		values.put("PROFISSAO", pessoaDTO.getProfissao());
		values.put("SEXO", String.valueOf(pessoaDTO.getSexo()));
		
		db.update("TB_PESSOA", values, "ID_PESSOA=?", new String[]{pessoaDTO.getIdPessoa().toString()});
		
	}

}
