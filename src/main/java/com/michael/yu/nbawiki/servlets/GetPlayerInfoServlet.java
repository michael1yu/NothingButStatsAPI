package com.michael.yu.nbawiki.servlets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.gson.Gson;
import com.michael.yu.nbawiki.models.JSONFirebase;
import com.michael.yu.nbawiki.models.Player;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

public class GetPlayerInfoServlet extends HttpServlet {

    private Firestore db;
    private String teamName;
    private String playerID;

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

    //initialize firestore db access and get team and id parameters
    private String get(HttpServletRequest req) throws IOException, ExecutionException, InterruptedException {
        teamName = req.getParameter("team");
        playerID = req.getParameter("id");
        JSONFirebase jsonFirebase = new JSONFirebase();
        db = jsonFirebase.getDb();
        return getInfo();
    }


    //retrieves player doc from db and converts to player object and then to JSON
    @SuppressWarnings("Duplicates")
    private String getInfo() throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection(teamName).document(playerID);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot doc = future.get();
        Player info = doc.toObject(Player.class);
        Gson gson = new Gson();
        String response = gson.toJson(info);
        System.out.println(response);
        return response;
    }
}
