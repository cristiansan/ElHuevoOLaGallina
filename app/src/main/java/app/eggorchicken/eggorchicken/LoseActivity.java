package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by XXX on 3/13/2015.
 */
public class LoseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loose);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(config);

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
        String Uname = userDetails.getString("fbFullName", "");
        String Id = userDetails.getString("fbId", "");

        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_loose_textview_perdiste), getString(R.string.activity_loose_perdiste));
        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_loose_textview_btncompartir), getString(R.string.activity_congrats_btn_compartir));
        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_loose_textview_btn_volver_a_jugar), getString(R.string.activity_congrats_btn_vuelve));

        ((ImageButton) findViewById(R.id.activity_loose_imagebutton_boton_volver_a_jugar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoseActivity.this, MainActivity.class));
                finish();
            }
        });

        ImageLoader.getInstance().displayImage(String.format("http://graph.facebook.com/%s/picture?type=square", Id), (CircleImageView) findViewById(R.id.activity_loose_imageview_avatar));
    }
}
