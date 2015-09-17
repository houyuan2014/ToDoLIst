package org.crazyit.utils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.util.Log;

public class HttpUtil {
	// 创建HttpClient对象

		public static HttpClient httpClient = new DefaultHttpClient();
		public static final String BASE_URL ="http://Todolist.f3322.net/JSON_Service/";
		
		public static String syncRequest(final String json_syn) throws Exception, ExecutionException
		{
			final String url = BASE_URL+"/JSON_Tongbu.ashx";
//			httpClient.getParams().setLongParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//			httpClient.getParams().setLongParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
			
			FutureTask<String> task = new FutureTask<String>(new Callable<String>()
					{
						@Override
						public String call() throws Exception
						{
							HttpPost post = new HttpPost(url);
							post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
							post.setEntity(new StringEntity(json_syn, HTTP.UTF_8));
							HttpResponse httpResponse = httpClient.execute(post);
							if (httpResponse.getStatusLine().getStatusCode() == 200)
							{
								// 获取服务器响应字符串
								String jsonstring = EntityUtils.toString(httpResponse.getEntity());
								
								return jsonstring;
							}
							return null;
						}
					});
					new Thread(task).start();
					return task.get();

		}
		 /*
		 * @param url 发送请求的URL
		 * @param params 请求参数
		 * @return 服务器响应字符串
		 * @throws Exception
		 */
		public static String postRequest(final String url, final Map<String ,String> rawParams)throws Exception
		{
//			httpClient.getParams().setLongParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);
//			httpClient.getParams().setLongParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
			
			FutureTask<String> task = new FutureTask<String>(new Callable<String>()
			{
				@Override
				public String call() throws Exception
				{
					String result = null;
					// 创建HttpPost对象。
					HttpPost post = new HttpPost(url);
					post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
					JSONObject json = new JSONObject(rawParams);
					post.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
					post.setEntity(new StringEntity(json.toString(), HTTP.UTF_8));
					
					Log.i("aaaaa", json.toString());
					
					// 发送POST请求
					HttpResponse httpResponse = httpClient.execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200)
					{
						// 获取服务器响应字符串
						result = EntityUtils.toString(httpResponse.getEntity());
						
						Log.i("result", result);

						return result;
					}
//					Map<String ,String> map = new HashMap<String, String>();
//					map.put("email", "hy");
//					JSONObject jsonobj = new JSONObject(map);
//					return jsonobj.toString();
					return result;
				}
			});
			new Thread(task).start();
			return task.get();//.toString();
		}
		

}
