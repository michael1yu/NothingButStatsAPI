package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.michael.yu.nbawiki.models.JSONFirebase;
import com.michael.yu.nbawiki.models.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PruneDatabaseServlet extends HttpServlet {
    private Firestore db;
    private String[] teams = new String[]{
            "Atlanta Hawks",
            "Boston Celtics",
            "Brooklyn Nets",
            "Charlotte Hornets",
            "Chicago Bulls",
            "Cleveland Cavaliers",
            "Dallas Mavericks",
            "Denver Nuggets",
            "Detroit Pistons",
            "Golden State Warriors",
            "Houston Rockets",
            "Indiana Pacers",
            "LA Clippers",
            "Los Angeles Lakers",
            "Memphis Grizzlies",
            "Miami Heat",
            "Milwaukee Bucks",
            "Minnesota Timberwolves",
            "New Orleans Pelicans",
            "New York Knicks",
            "Oklahoma City Thunder",
            "Orlando Magic",
            "Philadelphia 76ers",
            "Phoenix Suns",
            "Portland Trail Blazers",
            "Sacramento Kings",
            "San Antonio Spurs",
            "Toronto Raptors",
            "Utah Jazz",
            "Washington Wizards"
    };

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //initialize firestore db
    private void update() throws Exception {
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        for(String team : teams){
            iterateCollections(team);
        }
    }

    //goes through each team collection
    //queries players with field "current"=false and deletes them and their subcollections
    //then also removes their player id from the master list of all players
    @SuppressWarnings("Duplicates")
    private void iterateCollections(String team) throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection(team).whereEqualTo("current", false).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (DocumentSnapshot player : documents) {
            String id = player.toObject(Player.class).getId();
            player.getReference().collection("stats").document("current").delete();
            player.getReference().delete();
            db.collection("Players").document(id).delete();
        }
    }
}
