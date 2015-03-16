package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    DataBaseHelper myDbHelper = new DataBaseHelper(this);

    private int questionIndex = 1;
    private boolean is_true;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mal_conestado;
    private AnimationsContainer.FramesSequenceAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(config);

        ImageView rocketImage = (ImageView) findViewById(R.id.activity_main_imageview_reloj);
        anim = AnimationsContainer.getInstance().createProgressDialogAnim(rocketImage);
        anim.setOnClickListener(new AnimationsContainer.OnAnimationStoppedListener() {
            @Override
            public void AnimationStopped() {
            }

            @Override
            public void AnimationFinished() {
                startActivity(new Intent(MainActivity.this, LoseActivity.class));
                finish();
            }
        });
        anim.start();

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
        String Uname = userDetails.getString("fbFullName", "");
        String Id = userDetails.getString("fbId", "");

        //TRUE
        Button verdadero = (Button) findViewById(R.id.activity_main_imageview_boton_si);
        verdadero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getResources().getIdentifier(String.format("activity_main_imageview_answer_waiting%s", questionIndex), "id", getPackageName());
                int idDrawable;

                if (is_true) {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_correct", questionIndex), "drawable", getPackageName());
                } else {
                    mal_conestado.start();
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_wrong", questionIndex), "drawable", getPackageName());
                }
                ((ImageView) findViewById(id)).setImageResource(idDrawable);

                questionIndex = questionIndex + 1;

                if (questionIndex > 10) {
                    mediaPlayer.stop();
                    anim.stop();
                    startActivity(new Intent(MainActivity.this, WonActivity.class));
                    finish();
                    startActivity();
                } else {
                    anim.start();
                    getQuestionFromDB();
                }
            }
        });

        //FALSE
        Button falso = (Button) findViewById(R.id.activity_main_imageview_boton_no);
        falso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getResources().getIdentifier(String.format("activity_main_imageview_answer_waiting%s", questionIndex), "id", getPackageName());
                int idDrawable;

                if (is_true == false) {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_correct", questionIndex), "drawable", getPackageName());
                } else {
                    mal_conestado.start();
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_wrong", questionIndex), "drawable", getPackageName());
                }
                ((ImageView) findViewById(id)).setImageResource(idDrawable);

                questionIndex = questionIndex + 1;

                if (questionIndex > 10) {
                    mediaPlayer.stop();
                    anim.stop();
                    startActivity(new Intent(MainActivity.this, WonActivity.class));
                    finish();
                    startActivity();
                } else {
                    anim.start();
                    getQuestionFromDB();
                }
            }
        });

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        getQuestionFromDB();

        if (!Uname.contentEquals("")) {
            ImageLoader.getInstance().displayImage(String.format("http://graph.facebook.com/%s/picture?type=square", Id), (CircleImageView) findViewById(R.id.activity_main_imageview_avatar));
        } else {
            ((CircleImageView) findViewById(R.id.activity_main_imageview_avatar)).setVisibility(View.INVISIBLE);
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.reloj);
        mal_conestado = MediaPlayer.create(this, R.raw.mal_conestado);
    }

    @Override
    public void onBackPressed() {
    }

    private void getQuestionFromDB() {
        List<Question> values = myDbHelper.getAllQuestion();

        Common.SetFontTextView(this, (TextView) findViewById(R.id.activity_main_textview_question), values.get(0).getQuestion());
//        ((TextView) findViewById(R.id.activity_main_textview_question)).setText(values.get(0).getQuestion());
        is_true = values.get(0).isCorrect();
        Log.d("D", "is_true = " + is_true);

//        try {
//            myDbHelper.close();
//        } catch (SQLException sqle) {
//            throw sqle;
//        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        anim.stop();
        System.gc();
    }

    private void startActivity() {
    }
}