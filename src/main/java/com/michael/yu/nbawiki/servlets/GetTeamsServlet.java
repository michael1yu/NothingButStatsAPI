
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

public class GetTeamsServlet extends HttpServlet {
    private Firestore db;

    @SuppressWarnings("Duplicates")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonArray response = get(req);
            JsonObject encapsulatingJson = new JsonObject();
            encapsulatingJson.add("teams", response);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println(encapsulatingJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //initialize firestore db
    private JsonArray get(HttpServletRequest req) throws Exception {
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        return iterateCollections();
    }


    //returns list of all teams
    @SuppressWarnings("Duplicates")
    private JsonArray iterateCollections() throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection("List of Teams").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        JsonArray teamArray = new JsonArray();
        for (DocumentSnapshot team : documents) {
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("abbreviation", team.getString("abbreviation"));
            teamObject.addProperty("city", team.getString("city"));
            teamObject.addProperty("division", team.getString("division"));
            teamObject.addProperty("full_name", team.getString("full_name"));
            teamObject.addProperty("name", team.getString("name"));
            teamObject.addProperty("logo", team.getString("logo"));
            teamArray.add(teamObject);
        }
        return teamArray;
    }
}
