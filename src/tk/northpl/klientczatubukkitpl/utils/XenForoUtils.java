package tk.northpl.klientczatubukkitpl.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import tk.northpl.klientczatubukkitpl.MainActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class XenForoUtils
{
	public static String loginUser(String username, String password)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>(15);
		params.add(new BasicNameValuePair("login", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("cookie_check", "0"));
		params.add(new BasicNameValuePair("register", "0"));
		params.add(new BasicNameValuePair("remember", "0"));
		String lol = PostExecute.excutePost("http://bukkit.pl/login/login", params);

		int positionOfToken = lol.indexOf("_xfToken");
		
		int startPosition = positionOfToken+17;
		int endPosition   = lol.indexOf("\"", startPosition);
		
		return lol.substring(startPosition, endPosition);
	}
	
	public static void sendMessage(String xfToken, String message)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>(15);
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("_xfToken", xfToken));
		params.add(new BasicNameValuePair("_xfResponseType", "xml"));
		String response = PostExecute.excutePost("http://bukkit.pl/taigachat/post.json", params);
		if(response.contains("Security error occurred.") || response.contains("You do not have permission to post messages."))
		{
			MainActivity.instance.runOnUiThread(new Thread()
			{
				@Override
				public void run()
				{
					Toast.makeText(MainActivity.instance.getApplicationContext(), "Wystąpił problem podczas wysyłania wiadomości", Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	public static void logout(String xfToken)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>(15);
		params.add(new BasicNameValuePair("_xfToken", xfToken));
		params.add(new BasicNameValuePair("_xfResponseType", "xml"));
		PostExecute.excutePost("http://bukkit.pl/logout/", params);
	}
}
