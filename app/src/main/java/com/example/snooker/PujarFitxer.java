package com.example.snooker;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PujarFitxer {

    public boolean esperant = true;
    private final CredentialManager credentialManager;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final AppCompatActivity activity;

    // Variable per guardar l'email i reintentar la pujada després de l'autorització
    private String currentEmail;

    // Launcher per gestionar la finestra d'autorització de Google
    private final ActivityResultLauncher<Intent> driveAuthLauncher;

    //private static final String WEB_CLIENT_ID = "874628293746-n19a7qm9c5821k15kfchd26s0rdcjp8s.apps.googleusercontent.com";
    private String WEB_CLIENT_ID;

    public PujarFitxer(AppCompatActivity activity) {
        Log.i("PujarFitxer","Inici");
        this.activity = activity;
        this.WEB_CLIENT_ID = activity.getString(R.string.web_client_id);
        this.credentialManager = CredentialManager.create(activity);
        this.driveAuthLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Log.i("PujarFitxer", "Autorització concedida. Reintentant pujada...");
                        if (currentEmail != null) {
                            Log.i("PujarFitxer","Reintentant pujada... Anem a upload");
                            esperant = false;
                            uploadFileToDrive(currentEmail);
                        }
                    } else {
                        Log.e("PujarFitxer", "L'usuari ha denegat l'autorització.");
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Cal autoritzar l'accés a Drive per continuar", Toast.LENGTH_SHORT).show());
                    }
                });
        Log.i("PujarFitxer","Final");

    }

    // Pass the listener into the method
    public void signInWithGoogle(OnSignInListener listener) {
        Log.i("signInWithGoogle", "Inici");

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                activity,
                request,
                null,
                executor,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        // Extract email logic...
                        String email = handleSignInResponse(response);
                        if (email != null) {
                            listener.onSignInSuccess(email);
                        } else {
                            listener.onSignInFailure("No s'ha pogut obtenir l'email");
                        }
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("signInWithGoogle", "Error: " + e.getMessage());
                        listener.onSignInFailure(e.getMessage());
                    }
                }
        );
    }

    public void signInWithGoogle() {
        Log.i("signInWithGoogle", "Inici");
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                activity,
                request,
                null,
                executor,
                new androidx.credentials.CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse response) {
                        handleSignInResponse(response);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("signInWithGoogle", "Sign-in error: " + e.getClass().getName(), e);
                        activity.runOnUiThread(() -> {
                            String errorMsg = e.getMessage();
                            if (e instanceof NoCredentialException) {
                                errorMsg = "No s'han trobat comptes.";
                            }
                            Toast.makeText(activity, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                        });
                    }
                }
        );
        Log.i("signInWithGoogle", "Final");
    }

    private String handleSignInResponse(GetCredentialResponse response) {
        Credential credential = response.getCredential();
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(customCredential.getType())) {
                GoogleIdTokenCredential googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(customCredential.getData());

                try {
                    GoogleIdToken parsedToken = GoogleIdToken.parse(GsonFactory.getDefaultInstance(), googleIdTokenCredential.getIdToken());
                    return parsedToken.getPayload().getEmail();
                } catch (Exception e) {
                    Log.e("handleSignInResponse", "Error parsejant", e);
                }
            }
        }
        return null;
    }

    public void uploadFileToDrive(String email) {
        executor.execute(() -> {
            Log.i("uploadFileToDrive", "Inici");
            try {
                Log.i("uploadFileToDrive", "Iniciant pujada a Drive per a: " + email);

                GoogleAccountCredential driveCredential = GoogleAccountCredential.usingOAuth2(
                        activity, Collections.singletonList(DriveScopes.DRIVE));
                driveCredential.setSelectedAccount(new Account(email, "com.google"));

                Drive service = new Drive.Builder(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),
                        driveCredential)
                        //.setApplicationName("ProvaGoogle")
                        .setApplicationName("Snooker")
                        .build();

                // Preparar fitxer temporal

                java.io.File tempFile = new java.io.File(activity.getCacheDir(), "upload.txt");
                try (InputStream is = activity.getResources().openRawResource(R.raw.provesgoogledrive);
                     FileOutputStream os = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                }

                String folderId = "1UJ6V3TXhzsurtQJEgNx28dJ0PF3jtPOi";  // ID de la carpeta Snooker

                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fds = new SimpleDateFormat("yyyyMMddHHmm");
                String data_fitxer = fds.format(date);

                File fileMetadata = new File();
                fileMetadata.setName(data_fitxer + "_upload.txt");
                fileMetadata.setMimeType("text/plain");
                FileContent mediaContent = new FileContent("text/plain", tempFile);
                fileMetadata.setParents(Collections.singletonList(folderId)); //Indiquem la carpeta destí

                try {
                    File uploadedFile = service.files().create(fileMetadata, mediaContent)
                            .setFields("id, parents")
                            .execute();

                    if (uploadedFile != null) {
                        Log.i("uploadFileToDrive", "Fitxer pujat! ID: " + uploadedFile.getId() + ", Parents: " + uploadedFile.getParents());
                        activity.runOnUiThread(() -> Toast.makeText(activity, "Fitxer pujat amb èxit!", Toast.LENGTH_LONG).show());
                    }
                } catch (UserRecoverableAuthIOException e) {
                    // Captura l'error d'autorització i llança el flux per demanar permisos a l'usuari
                    Log.w("uploadFileToDrive", "UserRecoverableAuthIOException: Cal autorització de Drive.");
                    currentEmail = email;
                    activity.runOnUiThread(() -> driveAuthLauncher.launch(e.getIntent()));
                } catch (Exception e) {
                    Log.e("uploadFileToDrive", "Error Drive (execute): " + e.getMessage(), e);
                }
            } catch (Exception e) {
                Log.e("uploadFileToDrive", "Error Drive (setup): " + e.getMessage(), e);
            }
        });
    }

    public interface OnSignInListener {
        void onSignInSuccess(String email);

        void onSignInFailure(String error);
    }

}
