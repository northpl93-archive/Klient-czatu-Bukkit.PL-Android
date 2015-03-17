package tk.northpl.klientczatubukkitpl.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class PostExecute
{
	private static HttpClient httpclient = new DefaultHttpClient();

	public static void setupClient()
	{
		ClientConnectionManager mgr = httpclient.getConnectionManager();
		HttpParams params = httpclient.getParams();
		httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
	}

	public static String excutePost(String targetURL, List<NameValuePair> urlParameters)
	{
		HttpPost httppost = new HttpPost(targetURL);

		try
		{
			httppost.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

			HttpResponse response = httpclient.execute(httppost);
		    java.util.Scanner s = new java.util.Scanner(response.getEntity().getContent(), "UTF-8").useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}
		catch (ClientProtocolException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
	}
}
