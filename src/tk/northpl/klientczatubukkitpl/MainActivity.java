package tk.northpl.klientczatubukkitpl;

import tk.northpl.klientczatubukkitpl.chatcode.ChatListener;
import tk.northpl.klientczatubukkitpl.utils.XenForoUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	public static MainActivity	instance			= null;
	public static ChatListener	chatListenThread	= null;

	public static Button		sendMsgButton		= null;

	public static TextView		text				= null;
	public static EditText		message				= null;
	public static ScrollView	scroll				= null;

	public static Intent		mServiceIntent		= null;
	public static AlertDialog	messageFailedDialog	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_main);
		setupStuff();

		text = (TextView) findViewById(R.id.messages);
		message = (EditText) findViewById(R.id.messageToSend);
		sendMsgButton = (Button) findViewById(R.id.messageSendButton);
		scroll = (ScrollView) findViewById(R.id.messagesScroll);
		text.setFocusable(false);
		sendMsgButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				sendmsg();
			}
		});

		mServiceIntent = new Intent(this, ChatListener.class);
		mServiceIntent.setData(Uri.parse(""));
		startService(mServiceIntent);

		LocalBroadcastManager.getInstance(this).registerReceiver(
				new ChatMessageReceiver(),
				new IntentFilter(ChatListener.Constants.MESSAGE_RECEIVED));
		text.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendmsg()
	{
		System.out
				.println("[Klient czatu bukkit.pl] Rozpoczeto wysylanie wiadomosci: "
						+ message.getText());
		new MessageSendTask().execute(message.getText().toString());
		System.out.println("= = = = = =");
		System.out.println(LoginActivity.xentoken);
		System.out.println("= = = = = =");
		message.setText("");
	}

	public static void appendMessage(String msg)
	{
		text.append(msg);
		scroll.post(new Runnable()
		{
			public void run()
			{
				scroll.smoothScrollTo(0, text.getBottom());
			}
		});
	}

	class MessageSendTask extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... dane)
		{
			XenForoUtils.sendMessage(LoginActivity.xentoken, dane[0]);
			return "";
		}
	}

	class ChatMessageReceiver extends BroadcastReceiver
	{
		private ChatMessageReceiver()
		{
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			appendMessage(bundle.getString(ChatListener.Constants.MESSAGE_TEXT));
		}
	}

	@SuppressWarnings("deprecation")
	private void setupStuff()
	{
		messageFailedDialog = new AlertDialog.Builder(this).create();
		messageFailedDialog.setTitle("Houston, mamy problem");
		messageFailedDialog
				.setMessage("Wystąpił problem podczas wysyłania wiadomości. Prawdopodobnie podałeś zły login lub hasło podczas logowania");
		messageFailedDialog.setButton("OK",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						messageFailedDialog.dismiss();
					}
				});
	}
}
