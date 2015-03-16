package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by XXX on 3/13/2015.
 */
public class WonActivity extends Activity {
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(config);

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
        String Uname = userDetails.getString("fbFullName", "");
        String Id = userDetails.getString("fbId", "");

        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_congrats_textview_ganaste), getString(R.string.activity_congrats_ganaste));
        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_congrats_textview_si_respondiste), getString(R.string.activity_congrats_respondiste10));
        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_congrats_textview_compartir), getString(R.string.activity_congrats_btn_compartir));
        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_congrats_textview_volver_a_jugar), getString(R.string.activity_congrats_btn_vuelve));

        ((ImageButton) findViewById(R.id.activity_congrats_imagebutton_boton_volver_a_jugar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WonActivity.this, MainActivity.class));
                finish();
            }
        });

        ((ImageButton) findViewById(R.id.activity_congrats_imagebutton_boton_compartir)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("name", "El Huevo o la Gallina?");
                params.putString("caption", "Demuestra si sabes reconocer entre cierto o falso!");
                params.putString("description", "Tienes 20 segundos para responder la mayor cantidad de afirmaciones reconociendo cuales son verdaderas o falsas! Encuentra una variedad temas muy diferentes para poner a prueba tus conocimientos.\n" +
                        "Juega ahora y pon aprueba tus conocimientos!.");
                params.putString("link", "https://play.google.com/store/apps/details?id=app.eggorchicken.eggorchicken");
//                params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

                WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(WonActivity.this, Session.getActiveSession(), params))
                        .setOnCompleteListener(new WebDialog.OnCompleteListener() {
                            @Override
                            public void onComplete(Bundle values, FacebookException error) {
                                if (error == null) {
                                    // When the story is posted, echo the success
                                    // and the post Id.
                                    final String postId = values.getString("post_id");
                                    if (postId != null) {
                                        Toast.makeText(WonActivity.this, "Posted story, id: " + postId, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // User clicked the Cancel button
                                        Toast.makeText(WonActivity.this, "Publish cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (error instanceof FacebookOperationCanceledException) {
                                    // User clicked the "x" button
                                    Toast.makeText(WonActivity.this.getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Generic, ex: network error
                                    Toast.makeText(WonActivity.this.getApplicationContext(), "Error posting story", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .build();
                feedDialog.show();
            }
        });

        if (!Uname.contentEquals("")) {
            ImageLoader.getInstance().displayImage(String.format("http://graph.facebook.com/%s/picture?type=square", Id), (CircleImageView) findViewById(R.id.activity_congrats_imageview_avatar));
        } else {
            ((CircleImageView) findViewById(R.id.activity_congrats_imageview_avatar)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.activity_congrats_textview_compartir)).setVisibility(View.INVISIBLE);
            ((ImageButton) findViewById(R.id.activity_congrats_imagebutton_boton_compartir)).setVisibility(View.INVISIBLE);
        }


        mAdView = (AdView) findViewById(R.id.adView);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                }
        });

        mAdView.loadAd(new AdRequest.Builder().build());
    }
}
