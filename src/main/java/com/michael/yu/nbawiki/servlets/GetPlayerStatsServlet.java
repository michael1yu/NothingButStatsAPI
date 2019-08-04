package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.gson.Gson;
import com.michael.yu.nbawiki.models.JSONFirebase;
import com.michael.yu.nbawiki.models.Stats;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.ExecutionException;

public class GetPlayerStatsServlet extends HttpServlet {
    private String teamName;
    private String playerID;
    private Firestore db;

    @SuppressWarnings("Duplicates")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String response = get(req);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println(response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //initialize firestore db and get team and id params
    public String get(HttpServletRequest req) throws IOException, ExecutionException, InterruptedException {
        teamName = req.getParameter("team");
        playerID = req.getParameter("id");
        System.out.println(teamName + "--" + playerID);
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        return getStats();
    }

    //gets player doc and retrieves current doc from stats sub collection
    //converts to stats object and then to JSON
    @SuppressWarnings("Duplicates")
    private String getStats() throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection(teamName).document(playerID).collection("stats").document("current");
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot doc = future.get();
        Stats stats = doc.toObject(Stats.class);
        Gson gson = new Gson();
        String response = gson.toJson(stats);
        System.out.println(response);
        return response;
    }
}
