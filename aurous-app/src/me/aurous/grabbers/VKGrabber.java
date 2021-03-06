package me.aurous.grabbers;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import me.aurous.ui.UISession;
import me.aurous.utils.Constants;
import me.aurous.utils.Utils;
import me.aurous.vkapi.VkAuth;
import me.aurous.vkapi.audio.AudioApi;

import org.json.JSONArray;
import org.json.JSONObject;

public class VKGrabber {
	private static String getVKStream(final String json) {
		final JSONObject jsonObj = new JSONObject(json);
		final JSONArray response = jsonObj.getJSONArray("response");
		for (final int i = 0; i < response.length();) {
			final Object jsonObject = response.get(i);
			final JSONObject jsonResults = new JSONObject(jsonObject.toString());
			final String stream = jsonResults.getString("url");
			return stream;

		}
		return "";

	}

	public static String grab(final String url) {
		String json = null;
		final int pos = url.lastIndexOf("/");
		final String ids = url.substring(pos + "/".length());
		final String formData = Utils.readFile(Constants.DATA_PATH
				+ "settings/vkauth.dat", Charset.defaultCharset());

		final AudioApi api = new AudioApi(VkAuth.VK_APP_ID, formData.trim());
		final String parameters = String.format("audios=%s", ids);

		try {
			json = api.searchAudioByIdJson(parameters);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		if (json.contains("\"response\":[0]")) {
			JOptionPane.showMessageDialog(UISession.getSearchWidget()
					.getWidget(), "No Stream Found!", "Error",
					JOptionPane.ERROR_MESSAGE);

			return "nil";
		}
		final String streamURL = getVKStream(json);

		return streamURL.trim();
	}
}
