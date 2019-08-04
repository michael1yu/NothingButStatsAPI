package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.gson.JsonObject;
import com.michael.yu.nbawiki.models.JSONFirebase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

public class GetPlayerTeamServlet extends HttpServlet {
    private Firestore db;
    private String id;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JsonObject response = get(req);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.println(response.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //initialize firestore db and get id params
    private JsonObject get(HttpServletRequest req) throws IOException, ExecutionException, InterruptedException {
        id = req.getParameter("id");
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        return getTeam();
    }


    //access players collection and retrieves player doc and return team name
    private JsonObject getTeam() throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection("Players").document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot doc = future.get();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("team", doc.getString("team"));
        return jsonObject;
    }
}
