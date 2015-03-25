package mwdindustries.aria;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ARView extends Activity implements SurfaceHolder.Callback{

    final int NEAR = 0;
    final int MID  = 1;
    final int FAR  = 2;
    int scope = -1;

    Camera mCamera;
    SurfaceView mPreview;
    ImageView locateItem;
    TextView infoDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_arview);

        locateItem = (ImageView)findViewById(R.id.locateItems);
        infoDisplay = (TextView)findViewById(R.id.infoWindow);
        getImage("thebent");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mPreview = (SurfaceView)findViewById(R.id.mPreview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCamera = Camera.open();

        //touch to autofocus
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });

        //doesn't let phone lock/go to sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //detect long press -> make invisible
        //locateItem
        locateItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //infoDisplay
        infoDisplay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay.setVisibility(View.INVISIBLE);
                return true;
            }
        });

    }//end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.arview, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //creates camera preview screen (app main screen)
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(0);
        params.setPreviewSize(selected.width,selected.height);
        mCamera.setParameters(params);

        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.i("PREVIEW","surfaceDestroyed");
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }

    //if poi button clicked
    public void sendPOIList(View view) {

        //create buttons to select inside out outside POIs
        Button inside = (Button) findViewById(R.id.BtnIndoor);
        Button outside = (Button) findViewById(R.id.BtnOutdoor);

        //hide or show buttons
        if(inside.getVisibility() == View.VISIBLE && outside.getVisibility() == View.VISIBLE)
        {
            inside.setVisibility(View.INVISIBLE);
            outside.setVisibility(View.INVISIBLE);
        }
        else
        {
            inside.setVisibility(View.VISIBLE);
            outside.setVisibility(View.VISIBLE);
        }

    }//end sendPOIList

    //for indoor POI selection
    public void indoorPOI(View view)
    {
        Intent indoorPOIcheckBoxes = new Intent(this, insidePOI.class);
        startActivity(indoorPOIcheckBoxes);
    }

    //for outdoor POI selection
    public void outdoorPOI(View view)
    {
        Intent outdoorPOIcheckBoxes = new Intent(this, POIActivity.class);
        startActivity(outdoorPOIcheckBoxes);
    }

    //for schedule button click
    public void scheduleInput(View view)
    {
        Intent scheduler = new Intent(this, scheduleActivity.class);
        startActivity(scheduler);
    }

//    //test to show image
//    public void showImage (View view)
//    {
//        ImageView russImage = (ImageView) findViewById(R.id.russ);
//
//        //int resID = getResources().getIdentifier(imageName , "drawable", getPackageName());
//
//        if(russImage.getVisibility() == View.VISIBLE)
//        {
//            russImage.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            russImage.setVisibility(View.VISIBLE);
//        }
//
//    }



    //gear button clicked
    public void gearButton(View view)
    {
        //create buttons
        Button scopeBtn = (Button) findViewById(R.id.BtnScope);
        Button schedule = (Button) findViewById(R.id.BtnSchedule);

        //hide or show buttons
        if(scopeBtn.getVisibility() == View.VISIBLE && schedule.getVisibility() == View.VISIBLE)
        {
            scopeBtn.setVisibility(View.INVISIBLE);
            schedule.setVisibility(View.INVISIBLE);

            Button near = (Button) findViewById(R.id.BtnNear);
            Button mid = (Button) findViewById(R.id.BtnMid);
            Button far = (Button) findViewById(R.id.BtnFar);

            hideButtons(near, mid, far);
        }
        else
        {
            scopeBtn.setVisibility(View.VISIBLE);
            schedule.setVisibility(View.VISIBLE);
        }

    }

    //scope button clicked
    public void scopePOI(View view)
    {
        //create buttons
        final Button near = (Button) findViewById(R.id.BtnNear);
        final Button mid = (Button) findViewById(R.id.BtnMid);
        final Button far = (Button) findViewById(R.id.BtnFar);

        //hide or show buttons
        if(near.getVisibility() == View.VISIBLE &&
            mid.getVisibility() == View.VISIBLE &&
            far.getVisibility() == View.VISIBLE)
        {
            near.setVisibility(View.INVISIBLE);
            mid.setVisibility(View.INVISIBLE);
            far.setVisibility(View.INVISIBLE);
        }
        else
        {
            near.setVisibility(View.VISIBLE);
            mid.setVisibility(View.VISIBLE);
            far.setVisibility(View.VISIBLE);
        }

        //when near button clicked
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = 0;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

        //when mid button clicked
        mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = 1;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

        //when far button clicked
        far.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = 2;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

    }

    //called to hide buttons on scope selection
    public void hideButtons(Button button1, Button button2, Button button3)
    {
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
    }

    //refresh button clicked
    public void refresh(View view)
    {
        /**
         * ANDY!!! CODE RIGHT IN HERE!
         */
    }

    //method to access images from drawable-mdpi
    //only will display one at a time (NEEDS TO BE MORE)
    public void getImage (String imageName)
    {
        if(imageName.equals("allyn"))
            locateItem.setImageResource(R.drawable.allyn);
        else if(imageName.equals("arts"))
            locateItem.setImageResource(R.drawable.arts);
        else if(imageName.equals("backward"))
            locateItem.setImageResource(R.drawable.backward);
        else if(imageName.equals("biosci"))
            locateItem.setImageResource(R.drawable.biosci);
        else if(imageName.equals("biosci1"))
            locateItem.setImageResource(R.drawable.biosci1);
        else if(imageName.equals("biosci2"))
            locateItem.setImageResource(R.drawable.biosci2);
        else if(imageName.equals("brehm"))
            locateItem.setImageResource(R.drawable.brehm);
        else if(imageName.equals("diggs"))
            locateItem.setImageResource(R.drawable.diggs);
        else if(imageName.equals("dunbar"))
            locateItem.setImageResource(R.drawable.dunbar);
        else if(imageName.equals("elevator"))
            locateItem.setImageResource(R.drawable.elevator);
        else if(imageName.equals("exit"))
            locateItem.setImageResource(R.drawable.exit);
        else if(imageName.equals("fawcett"))
            locateItem.setImageResource(R.drawable.fawcett);
        else if(imageName.equals("finearts"))
            locateItem.setImageResource(R.drawable.finearts);
        else if(imageName.equals("forward"))
            locateItem.setImageResource(R.drawable.forward);
        else if(imageName.equals("hamilton"))
            locateItem.setImageResource(R.drawable.hamilton);
        else if(imageName.equals("health"))
            locateItem.setImageResource(R.drawable.health);
        else if(imageName.equals("joshi"))
            locateItem.setImageResource(R.drawable.joshi);
        else if(imageName.equals("left"))
            locateItem.setImageResource(R.drawable.left);
        else if(imageName.equals("mathmicro"))
            locateItem.setImageResource(R.drawable.mathmicro);
        else if(imageName.equals("medsci"))
            locateItem.setImageResource(R.drawable.medsci);
        else if(imageName.equals("millet"))
            locateItem.setImageResource(R.drawable.millet);
        else if(imageName.equals("oelman"))
            locateItem.setImageResource(R.drawable.oelman);
        else if(imageName.equals("restroom"))
            locateItem.setImageResource(R.drawable.restroom);
        else if(imageName.equals("right"))
            locateItem.setImageResource(R.drawable.right);
        else if(imageName.equals("rike"))
            locateItem.setImageResource(R.drawable.rike);
        else if(imageName.equals("russ"))
            locateItem.setImageResource(R.drawable.russ);
        else if(imageName.equals("stairs"))
            locateItem.setImageResource(R.drawable.stairs);
        else if(imageName.equals("thebent"))
            locateItem.setImageResource(R.drawable.thebent);
        else if(imageName.equals("union"))
            locateItem.setImageResource(R.drawable.union);
        else if(imageName.equals("university"))
            locateItem.setImageResource(R.drawable.university);
        else if(imageName.equals("white"))
            locateItem.setImageResource(R.drawable.white);


        locateItem.setVisibility(View.VISIBLE);

        return;

    }//end getImage

    //used to get extra information about selected label
    public void getData(View view)
    {
        //ImageView selected = (ImageView) findViewById(R.id.locateItems);
        TextView myTextView = (TextView)findViewById(R.id.infoWindow);
        myTextView.setMovementMethod(new ScrollingMovementMethod());

        //if( selected.getResources().equals(R.drawable.thebent)) {
            //infoDisplay.setText(R.string.theBent);
            infoDisplay.setText(R.string.theBent);
        //}
        //else if... rest of art

        //show text view
        infoDisplay.setVisibility(View.VISIBLE);

    }


}//end main class




