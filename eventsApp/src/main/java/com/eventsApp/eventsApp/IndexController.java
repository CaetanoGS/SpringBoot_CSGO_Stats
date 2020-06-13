package com.eventsApp.eventsApp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class IndexController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){

        return "index";
    }

    @RequestMapping(value = "/stats" , method = RequestMethod.POST)
    public String stats(@RequestParam("id") String valueOne, Model model) throws Exception{ 

        System.out.println(valueOne);
        model.addAttribute("message", "Oi");

        String id = "76561198358105030";

        // http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=775EBC39D386A0EC87475378886CEEB4&steamids=76561198358105030

        sendGet(id);
        
        return "stats";
    }

    private void sendGet(String id) throws Exception {

        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

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

