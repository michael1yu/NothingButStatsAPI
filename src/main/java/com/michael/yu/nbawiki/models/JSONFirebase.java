package com.michael.yu.nbawiki.models;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JSONFirebase {
    String jsonCredentials;
    @SerializedName("type")
    private String type;
    @SerializedName("project_id")
    private String project_id;
    @SerializedName("private_key_id")
    private String private_key_id;
    @SerializedName("private_key")
    private String private_key;
    @SerializedName("client_email")
    private String client_email;
    @SerializedName("client_id")
    private String client_id;
    @SerializedName("auth_uri")
    private String auth_uri;
    @SerializedName("token_uri")
    private String token_uri;
    @SerializedName("auth_provider_x509_cert_url")
    private String auth_provider_x509_cert_url;
    @SerializedName("client_x509_cert_url")
    private String client_x509_cert_url;
    private Firestore db;

    public JSONFirebase() {
        type = System.getenv("TYPE");
        project_id = System.getenv("PROJECT_ID");
        private_key_id = System.getenv("PRIVATE_KEY_ID");
        private_key = System.getenv("PRIVATE_KEY");
        client_email = System.getenv("CLIENT_EMAIL");
        client_id = System.getenv("CLIENT_ID");
        auth_uri = System.getenv("AUTH_URI");
        token_uri = System.getenv("TOKEN_URI");
        auth_provider_x509_cert_url = System.getenv("AUTH_PROVIDER_X509_CERT_URL");
        client_x509_cert_url = System.getenv("CLIENT_X509_CERT_URL");
    }

    private String getCredentials() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private void initializeFirestore() throws IOException {
        InputStream serviceAccount = new ByteArrayInputStream(getCredentials().getBytes());
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(System.getenv("PROJECT_ID"))
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        db = FirestoreClient.getFirestore();
    }

    public Firestore getDb() throws IOException {
        initializeFirestore();
        return db;
    }
}
