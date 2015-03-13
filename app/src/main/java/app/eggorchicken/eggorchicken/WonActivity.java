package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by XXX on 3/13/2015.
 */
public class WonActivity extends Activity {
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
            }
        });

        ImageLoader.getInstance().displayImage(String.format("http://graph.facebook.com/%s/picture?type=square", Id), (CircleImageView) findViewById(R.id.activity_congrats_imageview_avatar));

    }
}
