package dreamlander.dreamland.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import dreamlander.dreamland.R;
import dreamlander.dreamland.helpers.Logger;
import dreamlander.dreamland.network.UserRegistration;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, UserRegistration.ResponseListener {

    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setGoogleSigninButton();
    }

    private void setGoogleSigninButton() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        styleGoogleSigninButton();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    private void styleGoogleSigninButton() {
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            registerUser(account);
        } else {
            Logger.error(result.toString());
            Toast.makeText(this, "Sign in failed. Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(GoogleSignInAccount account) {
        UserRegistration userRegistration = new UserRegistration(this, this);
        userRegistration.sendRegistrationRequest(account.getIdToken());
    }

    private void saveProfileImage(final String imageUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                Bitmap bitmap = null;
                try {
                    imageurl = new URL(imageUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileOutputStream fileOutputStream = null;
                File file = new File(getFilesDir(), "profile_picture.jpg");
                try {
                    fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.error(connectionResult.getErrorMessage());
        Toast.makeText(this, "Connection failed. Please try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationSuccess(@NotNull JSONObject response) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("email", response.getString("email"));
            editor.putString("name", response.getString("name"));
            editor.putString("auth_token", response.getString("auth_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
        finish();
    }

    @Override
    public void onRegistrationFailure(@Nullable String message) {
        Logger.error(message);
        Toast.makeText(this, "Registration failed. Please try again later", Toast.LENGTH_SHORT).show();
    }
}
