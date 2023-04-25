package lk.ijse.ikmanRental.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Mapview {

    private static final String API_KEY = "AIzaSyAXazOpD_kjN93Zs-Byxs8iipDcTwKJDo4"; // Replace with your actual API key

    public static int getDistance(String origin, String destination) throws IOException {
        String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin + "&destinations=" + destination + "&key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        StringBuilder sb = new StringBuilder();

        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();

        JsonElement jelement = new JsonParser().parse(sb.toString());
        JsonObject jobject = jelement.getAsJsonObject();
        String status = jobject.get("status").getAsString();
        if (!status.equals("OK")) {
            throw new RuntimeException("Failed to get distance: " + status);
        }

        int distance = jobject.get("rows").getAsJsonArray().get(0).getAsJsonObject().get("elements").getAsJsonArray().get(0).getAsJsonObject().get("distance").getAsJsonObject().get("value").getAsInt();
        return distance;
    }

}
