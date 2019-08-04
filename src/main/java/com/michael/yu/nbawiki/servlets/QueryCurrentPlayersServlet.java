package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.michael.yu.nbawiki.models.JSONFirebase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class QueryCurrentPlayersServlet extends HttpServlet {
    private String team;
    private boolean current;
    private Firestore db;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonArray response = get(req);
            JsonObject encapsulatingJson = new JsonObject();
            encapsulatingJson.add("players", response);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println(encapsulatingJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //initialize firestore db and get team and current params
    private JsonArray get(HttpServletRequest req) throws Exception {
        team = req.getParameter("team");
        current = Boolean.valueOf(req.getParameter("current"));
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        return iterateCollections();
    }

    //returns all players in x team that match "current" value
    @SuppressWarnings("Duplicates")
    private JsonArray iterateCollections() throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection(team).whereEqualTo("current", current).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        JsonArray playerArray = new JsonArray();
        for (DocumentSnapshot player : documents) {
            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("name", player.getString("first_name")+" "+player.getString("last_name"));
            playerObject.addProperty("id", player.getString("id"));
            playerArray.add(playerObject);
        }
        return playerArray;
    }
}
