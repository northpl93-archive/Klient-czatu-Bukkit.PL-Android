package tk.northpl.klientczatubukkitpl.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpStatus;

import android.os.Build;

public class PostExecute
{

	@SuppressWarnings("deprecation")
	public static String excutePost(String targetURL, String urlParameters)
	{
		URL url;
		HttpURLConnection connection = null;
		try
		{
			System.out.println(urlParameters);
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			byte[] bodyData = urlParameters.getBytes("UTF-8"); 
			//connection.setRequestProperty("Content-Length", Integer.toString(bodyData.length));
			connection.setFixedLengthStreamingMode(URLEncoder.encode(urlParameters,"UTF-8").getBytes().length);
			connection.setRequestProperty("charset", "UTF-8");
			connection.setRequestProperty("Accept-Encoding", "UTF-8");
			if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
					connection.setRequestProperty("Connection", "close");
					}

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(URLEncoder.encode(urlParameters,"UTF-8").getBytes());
			wr.flush();
			wr.close();

			// Get Response
			InputStream is;
			int status = connection.getResponseCode();

			if(status >= HttpStatus.SC_BAD_REQUEST)
			    is = connection.getErrorStream();
			else
			    is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}
			rd.close();

			return response.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
		}
	}
}
