package com.ehaoyao.erp.mailno.normal.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xxl.job.core.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsUtil {
	private static Logger logger=LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager cm;
	private static String EMPTY_STR = "";
	private static String UTF_8 = "UTF-8";
	
	
	private static void init(){
		if(cm == null){
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(400);//整个连接池最大连接数
			cm.setDefaultMaxPerRoute(cm.getMaxTotal());//每路由最大连接数，默认值是2
		}
	}
	
	/**
	 * 通过连接池获取HttpClient
	 * @return
	 */
	private static CloseableHttpClient getHttpClient(){
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}
	
	
	
	
	/**
	 *
	 *  @Description    : 发送https post 请求;
	 *  @Method_Name    : doHttpsPost;
	 *  @param url    	：url 地址
	 *  @param map		：http 参数
	 *  @param charset 	：字符集 例如：UTF-8
	 *  @return         :
	 *  @return         : String;
	 * @throws Exception
	 *  @Creation Date  : 2016年6月17日 下午3:48:49 ;
	 *  @Author         : wujingxiong;
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public static String doHttpsPost(String url,Map<String,String> map,String charset) throws Exception{
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = com.ehaoyao.erp.mailno.normal.util.SSLClient.createSSLClientDefault();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }else{
                	logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,map);
                }
            }else{
            	logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,map);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("发送https 请求出错了url:{},参数：{}，错误信息：{}",url,map,ex);
            throw ex;
        }
        return result;
    }

	/**
	 *	极兔使用
	 *  @Description    : 发送https post 请求;
	 *  @Method_Name    : doHttpsPost;
	 *  @param url    	：url 地址
	 *  @param map		：http 参数
	 *  @param charset 	：字符集 例如：UTF-8
	 *  @return         :
	 *  @return         : String;
	 * @throws Exception
	 *  @Creation Date  : 2022年7月14日 下午5:51:49 ;
	 *  @Author         : wujingxiong;
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public static String JTdoHttpsPost(String url, Map<String,String> map, String charset, String digest, Long apiAccount) throws Exception{
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = com.ehaoyao.erp.mailno.normal.util.SSLClient.createSSLClientDefault();
            httpPost = new HttpPost(url);

			httpPost.setHeader("apiAccount", apiAccount + "");
			httpPost.setHeader("digest", digest);
			httpPost.setHeader("timestamp", System.currentTimeMillis() + "");

            //设置参数
            List<NameValuePair> list = new ArrayList<>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }

			HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }else{
                	logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,map);
                }
            }else{
            	logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,map);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("发送https 请求出错了url:{},参数：{}，错误信息：{}",url,map,ex);
            throw ex;
        }
        return result;
    }


	public static String doHttpsPostJSON(String url, String json, String appKey, String appSecret) throws Exception{
		String result = null;
		HttpPost httpPost = null;
		HttpClient httpClient = null;
		try {
			httpPost = new HttpPost(url);
			httpClient = com.ehaoyao.erp.mailno.normal.util.SSLClient.createSSLClientDefault();
			// 设置请求头
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("app-key", appKey);
			httpPost.setHeader("req-time", String.valueOf(System.currentTimeMillis()));
			httpPost.setHeader("sign", MD5(json + "_" + appSecret, "UTF-8"));
			//设置参数
			StringEntity stringEntity = new StringEntity(json);
			httpPost.setEntity(stringEntity);

			HttpResponse response = httpClient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}else{
					logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,json);
				}
			}else{
				logger.error("发送https请求  url:{},参数：{}  返回结果为空",url,json);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("发送https 请求出错了url:{},参数：{}，错误信息：{}",url,json,ex);
			throw ex;
		}
		return result;
	}


	private static String MD5(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuffer sb = new StringBuffer(32);

		for(int i = 0; i < result.length; ++i) {
			int val = result[i] & 255;
			if (val <= 15) {
				sb.append("0");
			}

			sb.append(Integer.toHexString(val));
		}

		return sb.toString().toLowerCase();
	}

	
}