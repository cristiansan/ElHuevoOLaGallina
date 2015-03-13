package app.eggorchicken.eggorchicken;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    DataBaseHelper myDbHelper = new DataBaseHelper(this);

    FasterAnimationsContainer mFasterAnimationsContainer;
    private static final int[] IMAGE_RESOURCES = {R.drawable.reloj20, R.drawable.reloj19, R.drawable.reloj18, R.drawable.reloj17, R.drawable.reloj16,
                                                  R.drawable.reloj15, R.drawable.reloj14, R.drawable.reloj13, R.drawable.reloj12, R.drawable.reloj11,
                                                  R.drawable.reloj10, R.drawable.reloj9, R.drawable.reloj8, R.drawable.reloj7,R.drawable.reloj6,
                                                  R.drawable.reloj5, R.drawable.reloj4, R.drawable.reloj3, R.drawable.reloj2, R.drawable.reloj1,
                                                  R.drawable.reloj0};

    private static final int ANIMATION_INTERVAL = 1000;// 500ms

    private int questionIndex = 1;
    private boolean is_true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        ImageView rocketImage = (ImageView) findViewById(R.id.activity_main_imageview_reloj);
        mFasterAnimationsContainer = FasterAnimationsContainer.getInstance(rocketImage);
        mFasterAnimationsContainer.addAllFrames(IMAGE_RESOURCES, ANIMATION_INTERVAL);
        mFasterAnimationsContainer.start();
        mFasterAnimationsContainer.setOnAnimationFrameChangedListener(new FasterAnimationsContainer.OnAnimationFrameChangedListener() {
            @Override
            public void onAnimationFrameChanged(int index) {
                if (index == 20) {
                    mFasterAnimationsContainer.stop();
                }
            }
        });

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
        String Uname = userDetails.getString("fbFullName", "");
        String Id = userDetails.getString("fbId", "");

        //TRUE
        Button verdadero = (Button) findViewById(R.id.activity_main_imageview_boton_si);
        verdadero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFasterAnimationsContainer.stop();

                int id = getResources().getIdentifier(String.format("activity_main_imageview_answer_waiting%s", questionIndex), "id", getPackageName());

                int idDrawable;
                if (is_true) {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_correct", questionIndex), "drawable", getPackageName());
                } else {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_wrong", questionIndex), "drawable", getPackageName());
                }
                ((ImageView) findViewById(id)).setImageResource(idDrawable);

                questionIndex = questionIndex + 1;

                if (questionIndex > 10) {
                    startActivity(new Intent(MainActivity.this, CongratsActivity.class));
                    startActivity();
                } else {
                    mFasterAnimationsContainer.reset();
                    mFasterAnimationsContainer.start();
                    getQuestionFromDB();
                }
            }
        });

        //FALSE
        Button falso = (Button) findViewById(R.id.activity_main_imageview_boton_no);
        falso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFasterAnimationsContainer.stop();

                int id = getResources().getIdentifier(String.format("activity_main_imageview_answer_waiting%s", questionIndex), "id", getPackageName());

                int idDrawable;
                if (!is_true) {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_correct", questionIndex), "drawable", getPackageName());
                } else {
                    idDrawable = getResources().getIdentifier(String.format("answer_%s_wrong", questionIndex), "drawable", getPackageName());
                }
                ((ImageView) findViewById(id)).setImageResource(idDrawable);

                questionIndex = questionIndex + 1;

                if (questionIndex > 10) {
                    startActivity(new Intent(MainActivity.this, CongratsActivity.class));
                    startActivity();
                } else {
                    mFasterAnimationsContainer.start();
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

        ImageLoader.getInstance().displayImage(String.format("http://graph.facebook.com/%s/picture?type=square", Id), (CircleImageView) findViewById(R.id.activity_main_imageview_avatar));
    }

    @Override
    public void onBackPressed() {
    }

    private void getQuestionFromDB() {
        List<Question> values = myDbHelper.getAllQuestion();
        ((TextView) findViewById(R.id.activity_main_textview_question)).setText(values.get(0).getQuestion());
        is_true = values.get(0).isCorrect();

//        try {
//            myDbHelper.close();
//        } catch (SQLException sqle) {
//            throw sqle;
//        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        rocketAnimation.start();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFasterAnimationsContainer.stop();
    }

    private void startActivity() {
    }
}