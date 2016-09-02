package br.edu.devmedia.loja_virtual.paypal;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

import br.edu.devmedia.loja_virtual.util.WebServiceUtil;

public class PayPalConfig {
	
	public static final String PAYPAL_CLIENT_ID = "";
	public static final String SENHA = "";
	
	public static final String PAYPAL_ENV = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	public static final String PAYPAL_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
	
	public static final String MOEDA_PADRAO = "BRL";
	
	public static final String URL_PRODUTOS = WebServiceUtil.HOST + "produto/listarTitulo";
	public static final String URL_VERIFICA_PAGTO = WebServiceUtil.HOST + "produto/checkPagto";
}
