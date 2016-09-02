package br.edu.devmedia.loja_virtual.custom.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.edu.devmedia.loja_virtual.R;
import br.edu.devmedia.loja_virtual.custom.app.AppController;
import br.edu.devmedia.loja_virtual.custom.model.Produto;

public class ProdutoCustomListAdapter extends BaseAdapter {

	
	private Activity activity;
	private List<Produto> produtos = new ArrayList<Produto>();	
	
	private LayoutInflater inflater;
	
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	private ListAdapterListenerProduto listener;
	
	public ProdutoCustomListAdapter(Activity activity, List<Produto> produtos, ListAdapterListenerProduto listener) {
		this.activity = activity;
		this.produtos = produtos;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return produtos.size();
	}

	@Override
	public Object getItem(int position) {
		return produtos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(inflater == null){
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if(convertView == null){
			convertView = inflater.inflate(R.layout.linha_lista, null);
		}
		if(imageLoader == null){
			imageLoader = AppController.getInstance().getImageLoader();
		}
		NetworkImageView imageProd = (NetworkImageView) convertView.findViewById(R.id.imgProduto);
		TextView txtTitulo = (TextView) convertView.findViewById(R.id.txtTitulo);
		TextView txtQtde = (TextView) convertView.findViewById(R.id.txtQtde);
		TextView txtCategoria = (TextView) convertView.findViewById(R.id.txtCategoria);
		TextView txtValor = (TextView) convertView.findViewById(R.id.txtValor);
		
		final Produto produto = produtos.get(position);
		
		imageProd.setImageUrl(produto.getUrlImg(), imageLoader);
		
		txtTitulo.setText(produto.getTitulo());
		txtQtde.setText("Qtde vendidos " + String.valueOf(produto.getQtde()));
		
		NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt"));
		txtValor.setText("R$ " + String.valueOf(format.format(produto.getValor())));
		
		String concat = "";
		for(String categoria : produto.getCategorias()){
			concat += categoria + ", ";
		}
		concat = concat.length() > 0 ? concat.substring(0, concat.length() - 2) : concat;
		
		txtCategoria.setText(concat);
		
		Button btnCarrinho = (Button) convertView.findViewById(R.id.btnAddCarrinho);
		
		btnCarrinho.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onAddCarrinhoPressed(produto);
			}
		});
		
		return convertView;
	}

}
