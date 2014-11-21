package com.xtravel.activities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xtravel.app.R;

/**
 * 该类用于用户进行登陆操作
 * 
 * @author 鲁继文
 */
public class LoginActivity extends XActivity {
	private static final String Tag = "LoginActivity";
	private Button loginButton;
	private EditText usrText, pswText;
	private String username;
	private String password;
	ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		Log.e(Tag, Tag);
		init();
	}

	private void init() {
		usrText = (EditText) findViewById(R.id.username);
		pswText = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login_btn);
		loginButton.setOnClickListener(onClickListener);
		// progressDialog = ProgressDlgUtil.getProgressDlg(this, "登陆",
		// "正在登陆.请耐心等待。。。。。", true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("登陆");
		progressDialog.setMessage("正在登陆.请耐心等待。。。。。");
		progressDialog.setCancelable(true);
		// DataHelper dataHelper = new DataHelper(this);
		// dataHelper.

	}

	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (v == loginButton) {
				login();
			}
		}
	};

	private void login() {

		username = usrText.getText().toString();
		password = pswText.getText().toString();
		if ((username.trim().length() == 0) || (password.trim().length() == 0)) {
			// DialogUtil.showErroAlterdlg(this,"登陆错误", "用户名和密码不得为空，请重新输入");
			new AlertDialog.Builder(this).setIcon(getResources().getDrawable(R.drawable.error_icon)).setMessage("用户名和密码不得为空，请重新输入").setTitle("登陆错误").create().show();
		} else // 登陆成功
		{
			progressDialog.show();
			LoginTask loginTask = new LoginTask();
			loginTask.execute(username, password);
		}
	}

	private boolean isLogin(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		String res = jsonObject.get("message").toString();
		Log.e("res:", res);
		if (res.equals("true")) {
			return true;
		} else
			return false;
	}

	/**
	 * 提交到服务器进行用户名密码的匹配
	 **/
	private class LoginTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", params[0]);
				map.put("passwd", params[1]);
				// String resJson =
				// ComunicationWithServer.comm("userLogin.action", map) ;
				// Log.e(Tag, resJson);
				// return MyApplication.islogIn=isLogin(resJson);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean islogsuccess) {
			/*
			 * if(MyApplication.islogIn) {
			 * ToastUtil.showShortToast(LoginActivity.this, new
			 * String("登陆成功！")); Intent intent = new Intent();
			 * intent.setClass(LoginActivity.this,MainActivity.class);
			 * startActivity(intent); LoginActivity.this.finish(); } else {
			 * //ToastUtil.showShortToast(LoginActivity.this, new
			 * String("登陆成功！"));
			 * DialogUtil.showErroAlterdlg(LoginActivity.this,"登陆错误",
			 * "用户名或密码有误！\n请重新登陆"); } progressDialog.cancel();
			 */
		}
	}

}
