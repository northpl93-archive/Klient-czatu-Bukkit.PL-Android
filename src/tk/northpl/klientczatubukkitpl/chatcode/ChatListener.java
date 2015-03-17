package tk.northpl.klientczatubukkitpl.chatcode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tk.northpl.klientczatubukkitpl.LoginActivity;
import tk.northpl.klientczatubukkitpl.utils.JsonResponseParser;
import tk.northpl.klientczatubukkitpl.utils.PostExecute;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChatListener extends IntentService
{
	private Gson gson;
	private String lol;
	private String rawData;
	private int latestMessage;
	private Document document         = null;
	private Elements as               = null;
	
	public ChatListener()
	{
		super("ChatListener");
	}

	public String getLoggedUser()
	{
		return LoginActivity.xentoken;
	}

	public void setLoggedUser(String xfToken)
	{
		LoginActivity.xentoken = xfToken;
	}

	public int getLatestMessage()
	{
		return latestMessage;
	}

	public void setLatestMessage(int newLatestMessage)
	{
		latestMessage = newLatestMessage;
	}
	
	@Override
	 public int onStartCommand(Intent intent, int flags, int startId) //sometimes overriding onStartCommand will not call onHandleIntent
	 {
	     super.onStartCommand(intent,flags,startId);
	     System.out.println("ChatListener started");
	     return START_STICKY;
	 }

	@Override
	protected void onHandleIntent(Intent intent)
	{
		gson = new Gson();

		while (true)
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>(15);
			params.add(new BasicNameValuePair("_xfToken", LoginActivity.xentoken));
			params.add(new BasicNameValuePair("lastrefresh", String.valueOf(latestMessage)));

			lol = PostExecute.excutePost(
					"http://bukkit.pl/index.php/taigachat/list.json", params);

			if (lol == null) // Błąd z pobieraniem zawartości
			{
				continue; //Ponowne wykonanie pętli
			}

			JsonResponseParser obj2 = gson.fromJson(lol, JsonResponseParser.class);

			document = Jsoup.parse(obj2.getHtml());
			as = document.select("li");

			for (Element element : as)
			{
				StringBuilder sb = new StringBuilder();

				for (Element el : element.getElementsByClass("username"))
				{
					sb.append(el.text());
					sb.append(": ");
				}

				for (Element el : element
						.getElementsByClass("taigachat_messagetext"))
				{
					sb.append(el.text());

					for (Element ell : el.getElementsByTag("img"))
					{
						sb.append(" ");
						sb.append(ell.attr("alt"));
					}

					sb.append("\n");
				}
				LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.MESSAGE_RECEIVED)
	            .putExtra(Constants.MESSAGE_TEXT, sb.toString()));
			}

			latestMessage = obj2.getLatestPostId();

			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	
	}
	
	public final class Constants
	{
	    public static final String MESSAGE_RECEIVED =
	        "tk.northpl.klientczatubukkitpl.chatcode.ChatListener.MESSAGE_SEND";
	    public static final String MESSAGE_TEXT =
		        "tk.northpl.klientczatubukkitpl.chatcode.ChatListener.MESSAGE_TEXT";
	}
}
