package de.kleindev.tftpserver.objects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WebResponseObject {
	int status_code;
	JsonElement data;
	JsonElement error;

	public WebResponseObject(JsonObject jsonObject) {
		status_code = jsonObject.get("status_code").getAsInt();
		data = jsonObject.get("data");
		error = jsonObject.get("error");
	}
	
	public static WebResponseObject create(String jsonString) {
		Gson g = new Gson();
		return g.fromJson(jsonString, WebResponseObject.class);
	}

	public int getStatus_code() {
		return status_code;
	}
	public JsonElement getData() {
		return data;
	}
	public JsonElement getError() {
		return error;
	}
}
