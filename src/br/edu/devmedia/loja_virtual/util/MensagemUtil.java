package br.edu.devmedia.loja_virtual.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class MensagemUtil {

	
	public static void addMsg(Activity activity, String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void addMsgOk(Activity activity, String title, String msg, int icone){
		AlertDialog.Builder builderDialog = new AlertDialog.Builder(activity);
		builderDialog.setTitle(title);
		builderDialog.setMessage(msg);
		builderDialog.setNeutralButton("OK", null);
		builderDialog.setIcon(icone);
		builderDialog.show();
	}
	
	public static void addMsgConfirm(Activity activity, String title, String msg, int icone, OnClickListener listener){
		AlertDialog.Builder builderDialog = new AlertDialog.Builder(activity);
		builderDialog.setTitle(title);
		builderDialog.setMessage(msg);
		builderDialog.setPositiveButton("Sim", listener);
		builderDialog.setNegativeButton("Não", null);
		builderDialog.setIcon(icone);
		builderDialog.show();
	}
	
}
