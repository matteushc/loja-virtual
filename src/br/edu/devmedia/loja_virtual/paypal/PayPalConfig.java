package br.edu.devmedia.loja_virtual.paypal;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

import br.edu.devmedia.loja_virtual.util.WebServiceUtil;

public class PayPalConfig {
	
	public static final String PAYPAL_CLIENT_ID = "AdlXmRzVHgpvqPvAjOqPyR2SXpVKpfSKBrm-V7k3ynvRfUfSzOE3OEhNQzl1psmJMoScunyEUdZnr0Zc";
	public static final String SENHA = "EPnMEfmdX6gXg_mofBDbJ7T4p22IbogioTFwlYYGa8YkLJQ8wsHLlDRyypTWyvqGl52uD_A1pn1hthEM";
	
	public static final String PAYPAL_ENV = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	public static final String PAYPAL_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
	
	public static final String MOEDA_PADRAO = "BRL";
	
	public static final String URL_PRODUTOS = WebServiceUtil.HOST + "produto/listarTitulo";
	public static final String URL_VERIFICA_PAGTO = WebServiceUtil.HOST + "produto/checkPagto";
}
