package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.gson.Gson;
import com.michael.yu.nbawiki.models.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefreshPlayersServlet extends HttpServlet {
    final String requestUrl = "https://www.balldontlie.io/api/v1/players?per_page=100&page=";
    private PlayerList playerList;
    private Firestore db;
    private String responseJson;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        update();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("Refresh Players Endpoint");
    }

    //initialize firestore db
    //iterate through player pages
    public void update() throws IOException {
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        getPlayers(1);
        createPlayerList();
        iteratePlayerList();
        int count = Integer.parseInt(playerList.getPlayerPageMeta().getTotal_pages());
        System.out.println("total count = " + count);
        for (int i = 1; i < count; i++) {
            getPlayers(i + 1);
            createPlayerList();
            iteratePlayerList();
        }
    }

    //iterate through players and create JSON string of balldontlie API response
    public void getPlayers(int page) throws IOException {
        URL url = new URL(requestUrl + page);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int rCode = conn.getResponseCode();
        System.out.println(rCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response);
        responseJson = response.toString();
    }

    //generates list of players
    private void createPlayerList() {
        Gson gson = new Gson();
        playerList = gson.fromJson(responseJson, PlayerList.class);
    }

    //goes through list of players
    private void iteratePlayerList() throws IOException {
        ArrayList<Player> players = playerList.getPlayers();
        for (Player player : players) {
            System.out.println(player);
            refreshCloudDb(player);
        }
    }

    //writes player info to firestore db
    private void refreshCloudDb(Player player) {
        DocumentReference docRef = db.collection(player.getTeam().getFull_name()).document(player.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("id", player.getId());
        data.put("first_name", player.getFirst_name());
        data.put("last_name", player.getLast_name());
        data.put("position", player.getPosition());
        data.put("height_feet", player.getHeight_feet());
        data.put("height_inches", player.getHeight_inches());
        data.put("weight_pounds", player.getWeight_pounds());
        data.put("docType", "player");

        ApiFuture<WriteResult> result = docRef.set(data, SetOptions.merge());

        docRef = db.collection("Players").document(player.getId());
        Map<String, Object> dataPlayers = new HashMap<>();
        dataPlayers.put("id", player.getId());
        dataPlayers.put("team", player.getTeam().getFull_name());

        ApiFuture<WriteResult> resultPlayers = docRef.set(dataPlayers, SetOptions.merge());
    }

}
