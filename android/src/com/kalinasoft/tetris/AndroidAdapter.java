package com.kalinasoft.tetris;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.games.Games;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class AndroidAdapter implements HighScoreAdapter {

    private Activity context;
    private boolean ready = false;
    private static final int RC_LEADERBOARD_UI = 9004;
    static final int RC_SIGN_IN = 31337;
    AndroidAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public boolean signIn() {
        final GoogleSignInClient signInClient = GoogleSignIn.getClient(context,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(context,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            GoogleSignInAccount signedInAccount = task.getResult();
                            if (signedInAccount != null) {
                                Log.d("Google sign-in", "onComplete: ok");
                                ready = true;
                            }
                        } else {
                            ApiException ae = (ApiException) task.getException();
                            // Player will need to sign-in explicitly using via UI
                            if (ae != null && ae.getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
                                startSignInIntent();
                                Log.d("Google sign-in", ae.getMessage());
                            }
                        }
                    }
                });
        return false;
    }

    @Override
    public boolean checkReady() {
        return ready;
    }

    @Override
    public boolean add(long score) {
        if (!ready)
            return false;
        GoogleSignInAccount sa = GoogleSignIn.getLastSignedInAccount(context);
        if (sa == null)
            return false;
        Games.getLeaderboardsClient(context, sa)
                .submitScore(context.getString(R.string.leaderboard_high_score),score);
        return true;
    }

    @Override
    public boolean show() {
        if (!ready)
            return false;
        GoogleSignInAccount sa = GoogleSignIn.getLastSignedInAccount(context);
        if (sa == null)
            return false;
        Games.getLeaderboardsClient(context, sa)
                .getLeaderboardIntent(context.getString(R.string.leaderboard_high_score))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        context.startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
        return true;
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(context,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        context.startActivityForResult(intent, RC_SIGN_IN);
    }

    void setReady() {
        ready = true;
    }
}
