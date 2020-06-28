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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;

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

        //String id = "76561198358105030";
        //String id = "76561198410324369";
        String id = "76561199006311912";


        // http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=775EBC39D386A0EC87475378886CEEB4&steamids=76561198358105030

        //sendGet(id);
        DecimalFormat df = new DecimalFormat("###,###,###");
        DecimalFormat df1 = new DecimalFormat("###,###,###.000");

        String nickName = getSteamUser(id);
        String kills = df.format(getUserKills(id));
        String deaths = df.format(getUserDeaths(id));
        String KD = df1.format(getUserKills(id)/getUserDeaths(id));
        String timePlayed = df.format(getUserTimePlayed(id)/3600);
        String wins = df.format(getUserWins(id));
        String MVPs = df.format(getUserMVPs(id));
        String photo = getUserphoto(id);

        sendGet(id);

        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=775EBC39D386A0EC87475378886CEEB4&steamids=" + id;

        model.addAttribute("message", photo);
        model.addAttribute("nickName", " Username: " + nickName);
        model.addAttribute("kills", " Kills: " + kills);
        model.addAttribute("deaths", " Deaths: " + deaths);
        model.addAttribute("KD", " Deaths: " + KD);
        model.addAttribute("time", " Time Played: " + timePlayed + " h");
        model.addAttribute("wins", " Wins: " + wins);
        model.addAttribute("mvps", " MVP's: " + MVPs);

        
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

    private String getSteamUser(String id) throws IOException{
        
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=775EBC39D386A0EC87475378886CEEB4&steamids=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("response").getJSONArray("players").get(0).toString());
                

                return jsonObject2.getString("personaname").toString();

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject1.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return null;
           }

        }
    }

    private float getUserKills(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                
                System.out.println();
                return jsonObject2.getInt("value");

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject1.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private float getUserDeaths(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(1).toString());
                //System.out.println(jsonObject2.getInt("value"));
                return jsonObject2.getInt("value");

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject2.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }


    private long getUserWins(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(5).toString());
                //System.out.println(jsonObject2.getInt("value"));
                return jsonObject2.getLong("value");

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject2.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private long getUserMVPs(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(99).toString());
                //System.out.println(jsonObject2.getInt("value"));
                return jsonObject2.getLong("value");

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject2.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private long getUserTimePlayed(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=775EBC39D386A0EC87475378886CEEB4&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(2).toString());
                //System.out.println(jsonObject2.getInt("value"));
                return jsonObject2.getLong("value");

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject2.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private String getUserphoto(String id) throws IOException{
        
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=775EBC39D386A0EC87475378886CEEB4&steamids=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();


        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonObject2 = new JSONObject(jsonObject.getJSONObject("response").getJSONArray("players").get(0).toString());
                

                return jsonObject2.getString("avatarfull").toString();

                

                //JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(0).toString());
                //System.out.println(jsonObject2.getInt("value"));
            }catch (JSONException err){
                System.out.println(err);
                return null;
           }

        }
    }

    
}

