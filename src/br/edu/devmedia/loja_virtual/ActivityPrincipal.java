package br.edu.devmedia.loja_virtual;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.edu.devmedia.loja_virtual.custom.adapter.NavDrawerListAdapter;
import br.edu.devmedia.loja_virtual.custom.model.NavDrawerItem;
import br.edu.devmedia.loja_virtual.fragment.DashListFragment;
import br.edu.devmedia.loja_virtual.fragment.HomeFragment;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class ActivityPrincipal extends Activity {

	private List<NavDrawerItem> navDrawItens;
	
	private CharSequence titulo;

	private String[] navTitulosMenu;
	private TypedArray navMenuIcones;

	private DrawerLayout drawerLayout;
	private ListView lstSliderMenu;
	
	NavDrawerListAdapter adapter;

	private ActionBarDrawerToggle drawerToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
		titulo = getTitle();
		
		navTitulosMenu = getResources().getStringArray(R.array.array_opcoes_menu);
		
		navMenuIcones = getResources().obtainTypedArray(R.array.nav_drawer_icones);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		lstSliderMenu = (ListView) findViewById(R.id.lst_slidermenu);
		
		navDrawItens = new ArrayList<NavDrawerItem>();
		
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[0], navMenuIcones.getResourceId(0, -1)));
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[1], navMenuIcones.getResourceId(1, -1)));
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[2], navMenuIcones.getResourceId(2, -1)));
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[3], navMenuIcones.getResourceId(3, -1), "54", true));
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[4], navMenuIcones.getResourceId(4, -1)));
		navDrawItens.add(new NavDrawerItem(navTitulosMenu[5], navMenuIcones.getResourceId(5, -1)));
		
		navMenuIcones.recycle();
		
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawItens);
		lstSliderMenu.setAdapter(adapter);
		lstSliderMenu.setOnItemClickListener(new SlideMenuClickListerner());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 
				R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(titulo);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(titulo);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		
		if (savedInstanceState == null) {
			exibirView(0);
		}
	}
	
	public void setTitulo(CharSequence titulo) {
		this.titulo = titulo;
		getActionBar().setTitle(titulo);
	}
	
	private void exibirView(int posicao) {
		Fragment fragment = null;
		switch (posicao) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new DashListFragment();
			break;
		default:
			break;
		}
		
		if (fragment != null) {
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.frame_principal, fragment).commit();
			lstSliderMenu.setItemChecked(posicao, true);
			lstSliderMenu.setSelection(posicao);
			setTitulo(navTitulosMenu[posicao]);
			drawerLayout.closeDrawer(lstSliderMenu);
		} else {
			Log.e("ActivityPrincipal", "Erro ao criar um fragment");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dash_board_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId()) {
		case R.id.versao:
			MensagemUtil.addMsg(this, "Loja Virtual DevMedia v2.1.0");
			return true;
		case 1:
			Fragment fragment = new DashListFragment();
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.frame_principal, fragment).commit();
			return true;
		case 2:
			fragment = new DashListFragment();
			manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.frame_principal, fragment).commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = drawerLayout.isDrawerOpen(lstSliderMenu);
		menu.findItem(R.id.versao).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
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
	
	private class SlideMenuClickListerner implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			exibirView(position);
		}
		
	}
}
