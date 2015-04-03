package mwdindustries.aria;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ARView extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = "POIActivity CLass";// for debug

    final int NEAR = 0;
    final int MID  = 1;
    final int FAR  = 2;
    int scope = -1;

    Camera mCamera;
    SurfaceView mPreview;
    ImageView locateItem1;
    ImageView locateItem2;
    ImageView locateItem3;
    TextView infoDisplay1;
    TextView infoDisplay2;
    TextView infoDisplay3;

    //create intents
    Intent scheduler = new Intent(this, scheduleActivity.class);
    Intent outdoorPOIcheckBoxes = new Intent(this, POIActivity.class);
    Intent indoorPOIcheckBoxes = new Intent(this, insidePOI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_arview);

        //set imageViews
        locateItem1 = (ImageView)findViewById(R.id.locateItems1);
        locateItem2 = (ImageView)findViewById(R.id.locateItems2);
        locateItem3 = (ImageView)findViewById(R.id.locateItems3);

        //set textViews
        infoDisplay1 = (TextView)findViewById(R.id.infoWindow1);
        infoDisplay2 = (TextView)findViewById(R.id.infoWindow2);
        infoDisplay3 = (TextView)findViewById(R.id.infoWindow3);

        //test call - String: image resource name, ImageView: 1 of 3 to set image to
        getImage("union", locateItem2);

        //force screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //set camera preview
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
        //locateItem1
        locateItem1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem1.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //infoDisplay1
        infoDisplay1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay1.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //locateItem1
        locateItem2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem2.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //infoDisplay1
        infoDisplay2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay2.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //locateItem1
        locateItem3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem3.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        //infoDisplay1
        infoDisplay3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay3.setVisibility(View.INVISIBLE);
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
        //Intent indoorPOIcheckBoxes = new Intent(this, insidePOI.class);
        startActivity(indoorPOIcheckBoxes);
    }

    //for outdoor POI selection
    public void outdoorPOI(View view)
    {
        //Intent outdoorPOIcheckBoxes = new Intent(this, POIActivity.class);
        startActivity(outdoorPOIcheckBoxes);
    }

    //for schedule button click
    public void scheduleInput(View view)
    {
        //Intent scheduler = new Intent(this, scheduleActivity.class);
        startActivity(scheduler);
    }

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
    public void getImage (String imageName, ImageView view)
    {
        if(imageName.equals("allyn"))
        {
            view.setImageResource(R.drawable.allyn);
            view.setTag(imageName);
        }

        else if(imageName.equals("arts"))
        {
            view.setImageResource(R.drawable.arts);
            view.setTag(imageName);
        }

        else if(imageName.equals("backward"))
        {
            view.setImageResource(R.drawable.backward);
            view.setTag(imageName);
        }

        else if(imageName.equals("biosci"))
        {
            view.setImageResource(R.drawable.biosci);
            view.setTag(imageName);
        }

        else if(imageName.equals("biosci1"))
        {
            view.setImageResource(R.drawable.biosci1);
            view.setTag(imageName);
        }

        else if(imageName.equals("biosci2"))
        {
            view.setImageResource(R.drawable.biosci2);
            view.setTag(imageName);
        }

        else if(imageName.equals("brehm"))
        {
            view.setImageResource(R.drawable.brehm);
            view.setTag(imageName);
        }

        else if(imageName.equals("diggs"))
        {
            view.setImageResource(R.drawable.diggs);
            view.setTag(imageName);
        }

        else if(imageName.equals("dunbar"))
        {
            view.setImageResource(R.drawable.dunbar);
            view.setTag(imageName);
        }

        else if(imageName.equals("elevator"))
        {
            view.setImageResource(R.drawable.elevator);
            view.setTag(imageName);
        }

        else if(imageName.equals("exit"))
        {
            view.setImageResource(R.drawable.exit);
            view.setTag(imageName);
        }

        else if(imageName.equals("fawcett"))
        {
            view.setImageResource(R.drawable.fawcett);
            view.setTag(imageName);
        }

        else if(imageName.equals("finearts"))
        {
            view.setImageResource(R.drawable.finearts);
            view.setTag(imageName);
        }

        else if(imageName.equals("forward"))
        {
            view.setImageResource(R.drawable.forward);
            view.setTag(imageName);
        }

        else if(imageName.equals("hamilton"))
        {
            view.setImageResource(R.drawable.hamilton);
            view.setTag(imageName);
        }

        else if(imageName.equals("health"))
        {
            view.setImageResource(R.drawable.health);
            view.setTag(imageName);
        }

        else if(imageName.equals("joshi"))
        {
            view.setImageResource(R.drawable.joshi);
            view.setTag(imageName);
        }

        else if(imageName.equals("left"))
        {
            view.setImageResource(R.drawable.left);
            view.setTag(imageName);
        }

        else if(imageName.equals("mathmicro"))
        {
            view.setImageResource(R.drawable.mathmicro);
            view.setTag(imageName);
        }

        else if(imageName.equals("medsci"))
        {
            view.setImageResource(R.drawable.medsci);
            view.setTag(imageName);
        }

        else if(imageName.equals("millet"))
        {
            view.setImageResource(R.drawable.millet);
            view.setTag(imageName);
        }

        else if(imageName.equals("oelman"))
        {
            view.setImageResource(R.drawable.oelman);
            view.setTag(imageName);
        }

        else if(imageName.equals("restroom"))
        {
            view.setImageResource(R.drawable.restroom);
            view.setTag(imageName);
        }

        else if(imageName.equals("right"))
        {
            view.setImageResource(R.drawable.right);
            view.setTag(imageName);
        }

        else if(imageName.equals("rike"))
        {
            view.setImageResource(R.drawable.rike);
            view.setTag(imageName);
        }

        else if(imageName.equals("russ"))
        {
            view.setImageResource(R.drawable.russ);
            view.setTag(imageName);
        }

        else if(imageName.equals("stairs"))
        {
            view.setImageResource(R.drawable.stairs);
            view.setTag(imageName);
        }

        else if(imageName.equals("thebent"))
        {
            view.setImageResource(R.drawable.thebent);
            view.setTag(imageName);
        }

        else if(imageName.equals("union"))
        {
            view.setImageResource(R.drawable.union);
            view.setTag(imageName);
        }

        else if(imageName.equals("university"))
        {
            view.setImageResource(R.drawable.university);
            view.setTag(imageName);
        }

        else if(imageName.equals("white"))
        {
            view.setImageResource(R.drawable.white);
            view.setTag(imageName);
        }


        //set imageview visible
        view.setVisibility(View.VISIBLE);

        return;

    }//end getImage

    //used to get extra information about selected label for textview 1
    public void getDataFirst(View view)
    {
        //ImageView selected = (ImageView) findViewById(R.id.locateItems);
        TextView myTextView = (TextView)findViewById(R.id.infoWindow1);
        myTextView.setMovementMethod(new ScrollingMovementMethod());

        if(locateItem1.getTag().equals("thebent"))
        {
            myTextView.setText(R.string.theBent);
        }
        else if(locateItem1.getTag().equals("russ"))
        {
            myTextView.setText(R.string.RussEngineering);
        }
        else if(locateItem1.getTag().equals("union"))
        {
            myTextView.setText(R.string.studentUnion);
        }
        //else if... rest of objects

        myTextView.setVisibility(View.VISIBLE);

    }

    //used to get extra information about selected label for textview 2
    public void getDataSecond(View view)
    {
        //ImageView selected = (ImageView) findViewById(R.id.locateItems);
        TextView myTextView = (TextView)findViewById(R.id.infoWindow2);
        myTextView.setMovementMethod(new ScrollingMovementMethod());

        if(locateItem2.getTag().equals("thebent"))
        {
            myTextView.setText(R.string.theBent);
        }
        else if(locateItem2.getTag().equals("russ"))
        {
            myTextView.setText(R.string.RussEngineering);
        }
        else if(locateItem2.getTag().equals("union"))
        {
            myTextView.setText(R.string.studentUnion);
        }
        //else if... rest of objects

        myTextView.setVisibility(View.VISIBLE);

    }

    //used to get extra information about selected label for textview 3
    public void getDataThird(View view)
    {
        //ImageView selected = (ImageView) findViewById(R.id.locateItems);
        TextView myTextView = (TextView)findViewById(R.id.infoWindow3);
        myTextView.setMovementMethod(new ScrollingMovementMethod());

        if(locateItem3.getTag().equals("thebent"))
        {
            myTextView.setText(R.string.theBent);
        }
        else if(locateItem3.getTag().equals("russ"))
        {
            myTextView.setText(R.string.RussEngineering);
        }
        else if(locateItem3.getTag().equals("union"))
        {
            myTextView.setText(R.string.studentUnion);
        }
        //else if... rest of objects

        myTextView.setVisibility(View.VISIBLE);

    }


}//end main class




