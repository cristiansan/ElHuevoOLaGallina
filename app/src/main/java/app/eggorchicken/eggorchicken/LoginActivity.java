package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.List;

/**
 * Created by cnc on 11/03/2015.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton facebook = (ImageButton) findViewById(R.id.activity_login_imagebutton_boton_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((ProgressBar)findViewById(R.id.progressBarSplash)).setVisibility(View.VISIBLE);

                Session.openActiveSession(LoginActivity.this, true, new Session.StatusCallback() {
                    @Override
                    public void call(Session session, SessionState state, Exception exception) {
                        if (session.getState() == SessionState.OPENED) {
                            Request.newMeRequest(session, new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser graphUser, Response response) {
                                    SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = userDetails.edit();
                                    edit.clear();
                                    edit.putString("fbFullName", graphUser.getName().toString().trim());
                                    edit.putString("fbId", graphUser.getId().toString().trim());
                                    edit.commit();

                                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                                    startActivity();
                                }
                            }).executeAsync();
                        }
                    }
                });

            }

        });

        ImageButton google = (ImageButton) findViewById(R.id.activity_login_imagebutton_boton_google);
        google.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ProgressBar)findViewById(R.id.progressBarSplash)).setVisibility(View.VISIBLE);

                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                startActivity();

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

        ((ProgressBar)findViewById(R.id.progressBarSplash)).setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
    }

    private void startActivity() {
    }
}