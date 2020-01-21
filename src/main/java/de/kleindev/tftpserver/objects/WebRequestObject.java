package de.kleindev.tftpserver.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import com.google.gson.Gson;

public abstract class WebRequestObject {
	String url;
	String params;

	public WebRequestObject(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public abstract void prepare();

	public WebResponseObject send() {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getUrl());

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("eocore_params", getParams()));
		
			try {
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				// Execute and get the response.
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
						InputStream instream = entity.getContent();
						return new Gson().fromJson(convert(instream), WebResponseObject.class);
				} else return null;
			} catch (IOException e) {
				return null;
			}
	}
	
	public String convert(InputStream inputStream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {	
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}
		return stringBuilder.toString();
	}
}
