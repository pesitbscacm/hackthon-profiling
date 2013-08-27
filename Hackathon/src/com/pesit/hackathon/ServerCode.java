
package com.pesit.hackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ServerCode {

	static String toReturn;
	static String serverLink = "http://pesseacm.org/ingeniusapp/";

	public static String serverInteract(final String file, final String send)
			throws IOException, InterruptedException {
		toReturn = null;
		Thread thread = new Thread() {
			public void run() {
				try {
					toReturn = serverInteract2(file, send);
				} catch (IOException e) {

					e.printStackTrace();
					System.out.println("Exception");
				}
			}
		};
		thread.start();
		thread.join();
		return toReturn;

	}

	public static String serverInteract2(String file, String send)
			throws IOException {

		send = serverLink + file + ".php?" + send;
		String res = "";
		 send=send.replaceAll(" ", "~");
		System.out.println(send);
		URL aroha = new URL(send);
		URLConnection aroha_connection;
		BufferedReader in;
		while (res.equalsIgnoreCase("")) {
			aroha_connection = aroha.openConnection();
			in = new BufferedReader(new InputStreamReader(aroha_connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				res = res+inputLine;
				System.out.println("inside loop");

			}
			in.close();
		}
		// System.out.println(res);

		System.out.println(res);
		return res;
	}

	public final static boolean isInternetOn(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			Toast.makeText(context, "No Internet Connection Found...",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}
}
