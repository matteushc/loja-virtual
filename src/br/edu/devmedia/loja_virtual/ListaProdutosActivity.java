package br.edu.devmedia.loja_virtual;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import br.edu.devmedia.loja_virtual.custom.UTF8ParseJson;
import br.edu.devmedia.loja_virtual.custom.adapter.CustomListAdapter;
import br.edu.devmedia.loja_virtual.custom.adapter.ListAdapterListenerProduto;
import br.edu.devmedia.loja_virtual.custom.app.AppController;
import br.edu.devmedia.loja_virtual.custom.model.Produto;
import br.edu.devmedia.loja_virtual.paypal.PayPalConfig;
import br.edu.devmedia.loja_virtual.util.MensagemUtil;

public class ListaProdutosActivity extends Activity implements SearchView.OnQueryTextListener, ListAdapterListenerProduto{

	private static final String TAG = ListaProdutosActivity.class.getSimpleName();
	
	private ListView lstProd;
	private List<Produto> produtos = new ArrayList<Produto>();
	private CustomListAdapter adapter;
	private SearchView searchView;
	
	
	private ProgressDialog progressDialog;
	private Button btnCheckout;
	private List<PayPalItem> carrinho = new ArrayList<PayPalItem>();
	
	private static final int CODIGO_PAGTO = 1;
	
	private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
			.environment(PayPalConfig.PAYPAL_ENV)
			.clientId(PayPalConfig.PAYPAL_CLIENT_ID).languageOrLocale("pt_BR");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_produtos);
		
		lstProd = (ListView) findViewById(R.id.listaProd);
		adapter = new CustomListAdapter(this, produtos, this);
		lstProd.setAdapter(adapter);
		
		btnCheckout = (Button) findViewById(R.id.btnCheckout);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Carregando...");
		progressDialog.show();
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
		
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
		startService(intent);
		
		btnCheckout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(carrinho.isEmpty()){
					MensagemUtil.addMsg(ListaProdutosActivity.this, "Carrinho vazio! Favor adicionar produtos");
				}else{
					executarPagtoPayPal();
				}
			}
		});
		
		executarRequestProdutos(null);
	}

	
	private void executarRequestProdutos(String str) {
		if(str != null){
			try {
				str = URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		JsonArrayRequest requestProds = new UTF8ParseJson(str != null ? PayPalConfig.URL_PRODUTOS + "?str=" + str : PayPalConfig.URL_PRODUTOS, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray jsonArray) {
				produtos.clear();
				esconderDialog();
				
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						
						Gson gson = new Gson();
						Produto produto = gson.fromJson(jsonObj.toString(), Produto.class);
						
						produtos.add(produto);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				adapter.notifyDataSetChanged();
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Erro: " + error.getMessage());
				esconderDialog();
			}
		});
		
		AppController.getInstance().addToRequestQueue(requestProds);
	}
	
	private void esconderDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		esconderDialog();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.produtos, menu);
		
		MenuItem item = menu.findItem(R.id.pesquisaProdutos);
		searchView = (SearchView) item.getActionView();
		
		configurarWidgetSearch();
		return true;
	}
	private void configurarWidgetSearch(){
		searchView.setIconifiedByDefault(true);
		
		SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		
		if(manager != null){
			List<SearchableInfo> global = manager.getSearchablesInGlobalSearch();
			
			
			SearchableInfo info = manager.getSearchableInfo(getComponentName());
			for (SearchableInfo searchableInfo : global) {
				if(searchableInfo.getSuggestAuthority() != null && searchableInfo.getSuggestAuthority().startsWith("applications")){
					info = searchableInfo;
				}

			}
			
			searchView.setSearchableInfo(info);
		}
		
		searchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if(newText != null){
			executarRequestProdutos(newText);
		}
		return false;
	}
	
	private void verificarPagtoServidor(final String idPagto, final String jsonClientePagto){
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Verificando pagamento...");
		progressDialog.show();

		StringRequest verificaReq = new StringRequest(Method.POST, PayPalConfig.URL_VERIFICA_PAGTO, new Response.Listener<String>() {
			@Override
			public void onResponse(String reponse) {
				JSONObject res;
				try {
					res = new JSONObject(reponse);
					boolean erro = res.getBoolean("erro");
					String msg = res.getString("msg");

					MensagemUtil.addMsg(ListaProdutosActivity.this, msg);

					if (!erro) {
						carrinho.clear();
					}
					esconderDialog();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError erro) {
				MensagemUtil.addMsg(ListaProdutosActivity.this, erro.getMessage());
				esconderDialog();
			}
		}) {
			
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				
				SharedPreferences pref = getPreferences(MODE_PRIVATE);
				
				int idUsuario = pref.getInt("usuario_id", 0);
				
				params.put("idPagto", idPagto);
				params.put("jsonClientePagto", jsonClientePagto);
				params.put("idUsuario", idUsuario != 0 ? String.valueOf(idUsuario) : "1");
				
				return params;
			}
		};
		
		int socketTimeout = 60000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		verificaReq.setRetryPolicy(policy);
		
		AppController.getInstance().addToRequestQueue(verificaReq);
	}

	@Override
	public void onAddCarrinhoPressed(Produto produto) {
		PayPalItem item = new PayPalItem(produto.getTitulo(), 1,
				new BigDecimal(produto.getValor()).setScale(2, RoundingMode.CEILING), 
				PayPalConfig.MOEDA_PADRAO, produto.getSku());
		
		carrinho.add(item);
		
		MensagemUtil.addMsg(ListaProdutosActivity.this, item.getName() + " adicionado ao carrinho!");
		
	}
	
	private PayPalPayment prepararCarrinhoFinal() {
		PayPalItem[] itens = new PayPalItem[carrinho.size()];
		itens = carrinho.toArray(itens);
		
		// Quantia total
		BigDecimal total = PayPalItem.getItemTotal(itens);
		
		// Frete
		BigDecimal frete = new BigDecimal("0.0");
		
		// Frete
		BigDecimal taxa = new BigDecimal("0.0");
		
		PayPalPaymentDetails detalhesPagto = new PayPalPaymentDetails(frete, total, taxa);
		
		BigDecimal quantia = total.add(taxa).add(frete);
		
		PayPalPayment pagto = new PayPalPayment(quantia, PayPalConfig.MOEDA_PADRAO, "Loja Virtual DevMedia", PayPalConfig.PAYPAL_INTENT);
		
		pagto.items(itens).paymentDetails(detalhesPagto);
		
		pagto.custom("Loja Virtual DevMedia");
		
		return pagto;
	}
	
	private void executarPagtoPayPal() {
		PayPalPayment coisasAComprar = prepararCarrinhoFinal();
		
		Intent i = new Intent(ListaProdutosActivity.this, PaymentActivity.class);
		i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
		i.putExtra(PaymentActivity.EXTRA_PAYMENT, coisasAComprar);
		
		startActivityForResult(i, CODIGO_PAGTO);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODIGO_PAGTO) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				
				if (confirm != null) {
					try {
						String pagtoId = confirm.toJSONObject().getJSONObject("response").getString("id");
						
						String jsonClientePagto = confirm.getPayment().toJSONObject().toString();
						
						verificarPagtoServidor(pagtoId, jsonClientePagto);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
}
