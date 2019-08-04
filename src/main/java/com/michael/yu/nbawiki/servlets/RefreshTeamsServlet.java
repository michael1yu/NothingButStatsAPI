package com.michael.yu.nbawiki.servlets;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.michael.yu.nbawiki.models.JSONFirebase;
import com.michael.yu.nbawiki.models.Team;
import com.michael.yu.nbawiki.models.TeamList;

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


public class RefreshTeamsServlet extends HttpServlet {
    private final String requestUrl = "https://www.balldontlie.io/api/v1/teams";
    private final String logoUrl = "https://www.nba.com/assets/logos/teams/primary/web/";
    private String responseJson;
    private TeamList teamList = new TeamList();
    private Firestore db;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        update();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.println("Refresh Teams Endpoint");
    }

    //initialize firestore db
    public void update() throws IOException {
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        getTeams();
        createTeamList();
        iterateTeamList();

    }

    //gets list of teams from balldontlie API and generates JSON string form response
    private void getTeams() throws IOException {
        URL url = new URL(requestUrl);
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
        responseJson = response.toString();
    }

    //creates list of teams from JSON response
    private void createTeamList() {
        Gson gson = new Gson();
        teamList = gson.fromJson(responseJson, TeamList.class);
    }

    //iterates through teams in list of teams
    private void iterateTeamList() throws IOException {
        ArrayList<Team> teams = teamList.getTeams();
        db = FirestoreClient.getFirestore();
        for (Team team : teams) {
            System.out.println(team);
            refreshCloudDb(team);
        }

    }

    //writes team data to db
    private void refreshCloudDb(Team team) {
        String teamId = team.getFull_name().toLowerCase().replaceAll(" ", "_").trim();
        DocumentReference docRef = db.collection(team.getFull_name()).document(teamId);
        Map<String, Object> data = new HashMap<>();
        data.put("id", team.getId());
        data.put("abbreviation", team.getAbbreviation());
        data.put("city", team.getCity());
        data.put("conference", team.getConference());
        data.put("division", team.getDivision());
        data.put("full_name", team.getFull_name());
        data.put("name", team.getName());
        data.put("logo", logoUrl + team.getAbbreviation() + ".svg");
        data.put("docType", "team");
        ApiFuture<WriteResult> result = docRef.set(data);
        docRef = db.collection("List of Teams").document(teamId);
        result = docRef.set(data, SetOptions.merge());
    }
}
