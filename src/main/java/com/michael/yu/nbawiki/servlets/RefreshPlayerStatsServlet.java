package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.michael.yu.nbawiki.models.JSONFirebase;
import com.michael.yu.nbawiki.models.Stats;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefreshPlayerStatsServlet extends HttpServlet {
    private Firestore db;
    private final String requestUrl = "https://www.balldontlie.io/api/v1/season_averages?player_ids[]=";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            update();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println("Refresh Player Stats Endpoint");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //initialize firestore db
    public void update() throws Exception {
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        iterateCollections();
    }

    //iterate through list of teams
    //then get query of players
    private void iterateCollections() throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection("List of Teams").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (DocumentSnapshot team : documents) {
            ApiFuture<QuerySnapshot> playerQuery = db.collection(team.getString("full_name")).whereEqualTo("docType", "player").get();
            List<QueryDocumentSnapshot> players = playerQuery.get().getDocuments();
            for (DocumentSnapshot document : players) {
                getPlayerStats(team.getString("full_name"), document.getString("id"));
            }
        }
    }

    //gets player stats from balldontlie API and generates JSON string
    private void getPlayerStats(String team, String id) throws IOException, InterruptedException {
        URL url = new URL(requestUrl + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int rCode = connection.getResponseCode();
        //sleep for 10 seconds while getting error code 429 "too many requests"
        while (rCode == 429) {
            Thread.sleep(10000);
            connection = (HttpURLConnection) url.openConnection();
            rCode = connection.getResponseCode();
            System.out.println("Error code 429 - Handling");
        }
        System.out.println(rCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        convertToModel(team, id, response.toString());
    }

    //converts balldontlie API response to data object
    private void convertToModel(String team, String id, String response) {
        Gson gson = new Gson();
        Data data = gson.fromJson(response, Data.class);
        Stats stats = data.getStats().get(0);
        updateFirestore(team, id, stats);
    }

    //creates subcollection "stats" under player doc
    //creates doc under subcollection "stats" with player stats
    private void updateFirestore(String team, String id, Stats stats) {
        DocumentReference documentReference = db.collection(team).document(id).collection("stats").document("current");
        Map<String, Object> data = new HashMap<>();

        data.put("games_played", stats.getGames_played());
        data.put("player_id", stats.getPlayer_id());
        data.put("season", stats.getSeason());
        data.put("min", stats.getMin());
        data.put("fgm", stats.getFgm());
        data.put("fga", stats.getFga());
        data.put("fg3m", stats.getFg3m());
        data.put("fg3a", stats.getFg3a());
        data.put("ftm", stats.getFtm());
        data.put("fta", stats.getFta());
        data.put("oreb", stats.getOreb());
        data.put("dreb", stats.getDreb());
        data.put("reb", stats.getReb());
        data.put("ast", stats.getAst());
        data.put("stl", stats.getStl());
        data.put("blk", stats.getBlk());
        data.put("turnover", stats.getTurnover());
        data.put("pf", stats.getPf());
        data.put("pts", stats.getPts());
        data.put("fg_pct", stats.getFg_pct());
        data.put("fg3_pct", stats.getFg3_pct());
        data.put("ft_pct", stats.getFt_pct());

        ApiFuture<WriteResult> future = documentReference.set(data, SetOptions.merge());

        //if games_played field == 0 then player is not current
        if (stats.getGames_played() != null && Integer.valueOf(stats.getGames_played()) > 0) {
            documentReference = db.collection(team).document(id);
            Map<String, Object> player = new HashMap<>();
            player.put("current", true);
            ApiFuture<WriteResult> playerFuture = documentReference.set(player, SetOptions.merge());
        } else {
            documentReference = db.collection(team).document(id);
            Map<String, Object> player = new HashMap<>();
            player.put("current", false);
            ApiFuture<WriteResult> playerFuture = documentReference.set(player, SetOptions.merge());
        }
    }


    class Data {
        @SerializedName("data")
        ArrayList<Stats> stats;

        public ArrayList<Stats> getStats() {
            if (stats.isEmpty()) {
                stats.add(new Stats());
            }
            return stats;
        }
    }
}
