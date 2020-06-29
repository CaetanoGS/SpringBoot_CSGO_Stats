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
    public String stats(@RequestParam("id") String valueOne, Model model){ 

        // Examples IDs

        //String id = "76561198358105030";
        //String id = "76561198410324369";

        String id = valueOne;


        // Output disires formats

        DecimalFormat df = new DecimalFormat("###,###,###");
        DecimalFormat df1 = new DecimalFormat("###,###,###.00");
        DecimalFormat df2 = new DecimalFormat("0.00");
        

        
        String nickName;

		try {
            nickName = getSteamUser(id);
            String kills = df.format(getUserKills(id));
            String deaths = df.format(getUserDeaths(id));
            String KD = df2.format(getUserKills(id)/getUserDeaths(id));
            String timePlayed = df.format(getUserTimePlayed(id)/3600);
            String wins = df.format(getUserWins(id));
            String MVPs = df.format(getUserMVPs(id));
            String photo = getUserphoto(id);
            String steamProfile = getUserSteamProfile(id);
            float lastMatchKills = getData("last_match_kills", id);
            float lastMatchDeaths = getData("last_match_deaths", id);
            String KDLast = df2.format(lastMatchKills/lastMatchDeaths);
            int lastMvps = (int) getData("last_match_mvps", id);
            int favWeaponLast = (int) getData("last_match_favweapon_id", id);
            String accuracy = df1.format(100*(getData("last_match_favweapon_hits", id)/getData("last_match_favweapon_shots", id)));
            String lastFavWeapon = getWeapon(favWeaponLast);
            

            System.out.println();

            model.addAttribute("message", photo);
            model.addAttribute("KD1", KD);
            model.addAttribute("nickName", " Username: " + nickName);
            model.addAttribute("kills", " Kills: " + kills);
            model.addAttribute("deaths", " Deaths: " + deaths);
            model.addAttribute("KD", " KD: " + KD);
            model.addAttribute("time", " Time Played: " + timePlayed + " h");
            model.addAttribute("wins", " Wins: " + wins);
            model.addAttribute("mvps", " MVP's: " + MVPs);
            model.addAttribute("profile", steamProfile);

            // Cards

            model.addAttribute("lastKills", (int)lastMatchKills);
            model.addAttribute("lastDeaths", (int)lastMatchDeaths);
            model.addAttribute("KDLast", KDLast);
            model.addAttribute("lastMVPs", lastMvps);
            model.addAttribute("favWeaponLast", lastFavWeapon);
            model.addAttribute("accuracy", accuracy + " %");

            return "stats";

		} catch (IOException e) {
            e.printStackTrace();
            
            return "index";
		}
        
    }


    // JSON search function

    private float getData(String obj, String id) throws IOException{
        
        // Insert your Steam Token over XXXXXX...

        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXXXXX&steamid=" + id;


		HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        //int responseCode = httpClient.getResponseCode();

        int responseLong = -1;

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                

                for(int i = 0; i < jsonObject.getJSONObject("playerstats").getJSONArray("stats").length(); i++){

                    JSONObject jsonObject1 = new JSONObject(jsonObject.getJSONObject("playerstats").getJSONArray("stats").get(i).toString());

                    if(jsonObject1.getString("name").equals(obj)){
                        responseLong = jsonObject1.getInt("value");
                    }
                }

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
		return responseLong;
        
    }

    // GET the STEAM User ( I dont use the Search function because the username is in a static position, that^s why I wont use loops)
    
    private String getSteamUser(String id) throws IOException{
        
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=XXXXXXXXXXXXX&steamids=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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

            }catch (JSONException err){
                System.out.println(err);
                return null;
           }

        }
    }

    // GET the Weapon ID (List of Weapons IDs)

    private String getWeapon(int weaponID){

        String[] weapons = {"Desert Eagle", "Dual Berettas", "Five Seven", "Glock", "", "", "AK-47", "AUG", "AWP", "FAMAS",
        "G3SG1", "", "Galil", "M249", "", "M4A4", "MAC-10", "", "P90", "", "", "", "MP5", "UMP", "XM1014", "Bizon", "MAG-7", "Negev", "Sawed-off",
        "Tec-9", "Zeus", "P2000", "MP7", "MP9", "Nova", "P250", "", "SCAR-20", "SG-553", "SSG 08"};



        return (weapons[weaponID-1]);

    }

    // GET the STEAM Profile ( I dont use the Search function because the username is in a static position, that^s why I wont use loops)

    private String getUserSteamProfile(String id) throws IOException{
        
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=XXXXXXXXXXXXXXXXXX&steamids=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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
                

                return jsonObject2.getString("profileurl").toString();


            }catch (JSONException err){
                System.out.println(err);
                return null;
           }

        }
    }

    private float getUserKills(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXXXXXX&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    // GET the User deaths ( I dont use the Search function because the username is in a static position, that^s why I wont use loops)


    private float getUserDeaths(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXXXX&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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
                return jsonObject2.getInt("value");

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    // GET the User Wins ( I dont use the Search function because the username is in a static position, that^s why I wont use loops)


    private long getUserWins(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXXX&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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
                
                return jsonObject2.getLong("value");

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    // GET the User MVPs ( I dont use the Search function because the username is in a static position, that^s why I wont use loops)


    private long getUserMVPs(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXX&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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

                return jsonObject2.getLong("value");

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private long getUserTimePlayed(String id) throws IOException{
        
        String url = "https://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/?appid=730&key=XXXXXXXXXXXXXXXXXXXXXXX&steamid=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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

                return jsonObject2.getLong("value");

            }catch (JSONException err){
                System.out.println(err);
                return -1;
           }

        }
    }

    private String getUserphoto(String id) throws IOException{
        
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=XXXXXXXXXXXXXXXXXX&steamids=" + id;

        HttpURLConnection httpClient =
                (HttpURLConnection) new URL(url).openConnection();

        // optional default is GET
        httpClient.setRequestMethod("GET");

        //add request header
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");


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
                
            }catch (JSONException err){
                System.out.println(err);
                return null;
           }

        }
    }

    

    
}

