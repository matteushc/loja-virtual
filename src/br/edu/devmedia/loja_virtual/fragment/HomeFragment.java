package br.edu.devmedia.loja_virtual.fragment;

import br.edu.devmedia.loja_virtual.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_dashboard, container, false);
		
		setHasOptionsMenu(true);
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(0, 1, 1, "Lista");
		super.onCreateOptionsMenu(menu, inflater);
	}
	
}
