package com.eventsApp.eventsApp;

import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.annotation.JacksonInject.Value;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        try {
			sendGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "index";
    }

    private void sendGet() throws Exception {

        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=76561198358105030";

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            //System.out.println(response.toString());

            try {
                JSONObject jsonObject = new JSONObject(response.toString());

                

                JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                System.out.println(jsonObject1.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
           }

        }

    }
    
}

