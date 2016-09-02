package br.edu.devmedia.loja_virtual.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import br.edu.devmedia.loja_virtual.R;
import br.edu.devmedia.loja_virtual.custom.AdapterListViewCustom;
import br.edu.devmedia.loja_virtual.custom.ItemDash;
import br.edu.devmedia.loja_virtual.dominio.DashBoardListItem;

public class DashListFragment extends Fragment{

	
	public DashListFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_dashboard_list, container, false);
		setHasOptionsMenu(true);
		
		ListView lstDash = (ListView) rootView.findViewById(android.R.id.list);
		
		List<ItemDash> listaItens = new ArrayList<ItemDash>();
		for(DashBoardListItem itens: DashBoardListItem.values()){
			ItemDash itemDash = new ItemDash(itens.getIdImg(), itens.getTitulo());
			listaItens.add(itemDash);
		}
		
		AdapterListViewCustom adapter = new AdapterListViewCustom(getActivity(), R.layout.item_listview_dash, listaItens);

		lstDash.setAdapter(adapter);
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, 2, 1, "Em quadros");
		super.onCreateOptionsMenu(menu, inflater);
	}
}
