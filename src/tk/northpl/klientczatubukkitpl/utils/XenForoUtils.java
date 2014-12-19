package tk.northpl.klientczatubukkitpl.utils;

import tk.northpl.klientczatubukkitpl.MainActivity;
import android.widget.Toast;

public class XenForoUtils
{
	public static String loginUser(String username, String password)
	{
		String rawData =
				  "login="+username+"&"
				+ "password="+password+"&"
				+ "cookie_check=0&"
				+ "register=0&"
				+ "remember=0";
		String lol = PostExecute.excutePost("http://bukkit.pl/login/login", rawData);
		System.out.println(lol);
		int positionOfToken = lol.indexOf("_xfToken");
		System.out.println(positionOfToken);
		
		int startPosition = positionOfToken+17;
		int endPosition   = lol.indexOf("\"", startPosition);
		
		return lol.substring(startPosition, endPosition);
	}
	
	public static void sendMessage(String xfToken, String message)
	{
		String rawData =
				  "message="+message+"&"
				+ "_xfToken="+xfToken+"&"
				+ "_xfResponseType=xml";
		String response = PostExecute.excutePost("http://bukkit.pl/taigachat/post.json", rawData);
		System.out.println(response);
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
		String rawData =
				"_xfToken="+xfToken+"&"
				+ "_xfResponseType=json";
		PostExecute.excutePost("http://bukkit.pl/logout/", rawData);
	}
}
