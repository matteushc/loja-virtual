package br.edu.devmedia.loja_virtual.custom.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.devmedia.loja_virtual.R;
import br.edu.devmedia.loja_virtual.custom.model.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter{

	private Context context;
	private List<NavDrawerItem> itens;
	
	
	public NavDrawerListAdapter(Context context, List<NavDrawerItem> itens) {
		this.context = context;
		this.itens = itens;
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public Object getItem(int position) {
		return itens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_lista_item, null);
		}
		ImageView icone = (ImageView) convertView.findViewById(R.id.icone);
		TextView txtTitulo = (TextView) convertView.findViewById(R.id.txtTituloDrawer);
		TextView txtCont = (TextView) convertView.findViewById(R.id.txtCont);
		
		
		icone.setImageResource(itens.get(position).getIcone());
		txtTitulo.setText(itens.get(position).getTitulo());
		
		if(itens.get(position).isContVisivel()){
			txtCont.setText(itens.get(position).getCont());
		}else{
			txtCont.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}

}
