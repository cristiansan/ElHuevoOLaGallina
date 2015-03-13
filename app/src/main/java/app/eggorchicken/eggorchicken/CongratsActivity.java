package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by cnc on 11/03/2015.
 */
public class CongratsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);


        //btn JUGAR DE NUEVO
        /*ImageButton again = (ImageButton) findViewById(R.id.activity_congrats_imagebutton_boton_volver_a_jugar);
        again.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity (new Intent(CongratsActivity.this, MainActivity.class));
                startActivity();

            }

        });*/
    }

    private void startActivity() {
    }

}

