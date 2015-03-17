package tk.northpl.klientczatubukkitpl;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import tk.northpl.klientczatubukkitpl.utils.PostExecute;
import tk.northpl.klientczatubukkitpl.utils.XenForoUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity
{
	public ProgressDialog		loginProgress	= null;

	public EditText				loginField		= null;
	public EditText				passwordField	= null;

	public static String		xentoken		= "";
	public static CookieManager	cookieHandler	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginField = (EditText) findViewById(R.id.loginBox);
		passwordField = (EditText) findViewById(R.id.passwordBox);

		PostExecute.setupClient();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void login(View v)
	{
		loginProgress = ProgressDialog.show(this, "Proszę czekać...",
				"Trwa logowanie do bukkit.pl", true);
		new LoginTask().execute(loginField.getText().toString(), passwordField
				.getText().toString());
	}

	private class LoginTask extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... dane)
		{
			String xid = XenForoUtils.loginUser(dane[0], dane[1]);
			xentoken = xid;

			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			loginProgress.dismiss();
			startActivity(i);
			finish();
			return xid;
		}
	}
}
