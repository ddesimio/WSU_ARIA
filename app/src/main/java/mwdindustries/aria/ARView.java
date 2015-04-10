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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mwdindustries.aria.util.AdvancedLocation;
import mwdindustries.aria.util.AdvancedLocationService;


public class ARView extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = "POIActivity CLass";// for debug
    AdvancedLocationService als;
    ArrayList<AdvancedLocation> al;

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

        //********** TESTING *******************************************************
        //also an example of how to move and show a label
            moveImageOne(-150,  //topDP - move up 150dp
                            90, //leftDp - move right 90dp
                            0,  //scaleX - doesnt matter -> flag is 0 so method is move image
                            0,  //scaleY - doesnt matter -> flag is 0 so method is move image
                            0); //flag - use method to move image

            getImage("thebent", locateItem1);
        //********** TESTING *******************************************************

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
        locateItem1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem1.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        infoDisplay1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay1.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        locateItem2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem2.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        infoDisplay2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay2.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        locateItem3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locateItem3.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        //detect long press -> make invisible
        infoDisplay3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                infoDisplay3.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        als = new AdvancedLocationService(this);
        al = new ArrayList<AdvancedLocation>(3);

        al.add(new AdvancedLocation());
        al.get(0).setShortname("union");
        al.get(0).setName("Student Union");
        al.get(0).setLongitude(-84.0649);
        al.get(0).setLatitude(39.780225);
        al.get(0).setBuildingDistance(300);
        al.get(0).setInformation(this.getResources().getString(R.string.studentUnion));

        al.add(new AdvancedLocation());
        al.get(1).setShortname("russ");
        al.get(1).setName("Russ Engineering Center");
        al.get(1).setLongitude(-84.06324);
        al.get(1).setLatitude(39.77946);
        al.get(1).setBuildingDistance(300);
        al.get(1).setInformation(this.getResources().getString(R.string.RussEngineering));

        al.add(new AdvancedLocation());
        al.get(2).setShortname("theBent");
        al.get(2).setName("The Bent");
        al.get(2).setLongitude(-84.06340);
        al.get(2).setLatitude(39.77985);
        al.get(2).setBuildingDistance(30);
        al.get(2).setInformation(this.getResources().getString(R.string.theBent));

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

    /** sendPOIList: called with triple dot button is pushed to display buttons for
     *              inside and outside POI selection options
     * @param view: this is what needed to be here for using the 'onclick' from the xml file
     */
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


    /** indoorPOI: starts new activity to allow users to select indoor points of interest
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
    public void indoorPOI(View view)
    {
        Intent indoorPOIcheckBoxes = new Intent(this, insidePOI.class);
        startActivity(indoorPOIcheckBoxes);
    }

    /** outdoorPOI: starts a new activity to allow users to select outdoor points of interest
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml files
     */
    public void outdoorPOI(View view)
    {
        Intent outdoorPOIcheckBoxes = new Intent(this, POIActivity.class);
        startActivity(outdoorPOIcheckBoxes);
    }


    /** scheduleInput: starts activity to let user input schedule building name and room number
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
    public void scheduleInput(View view)
    {
        Intent scheduler = new Intent(this, scheduleActivity.class);
        startActivity(scheduler);
    }


    /** gearButton: displays schedule and scope buttons
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
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

    }// end gearButton

    /** scopePOI: display scope options for user to select notification distances
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
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

        //when near button clicked => scope = 0
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = NEAR;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

        //when mid button clicked => scope = 1
        mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = MID;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

        //when far button clicked => scope = 2
        far.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scope = FAR;
                near.setVisibility(View.INVISIBLE);
                mid.setVisibility(View.INVISIBLE);
                far.setVisibility(View.INVISIBLE);
            }
        });

    }


    /** hideButtons: hides scope option buttons
     *
     * @param button1: first button passed in to hide
     * @param button2: second button passed in to hide
     * @param button3: third button passed in to hide
     */
    public void hideButtons(Button button1, Button button2, Button button3)
    {
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
    }


    /** getImage: get and set image to imageview
     *
     * @param imageName: string name of image to use set to the imageview
     * @param view 1 of 3 imageview options to use
     *
     * Pre-condition: pass in name of image to use and desired imageview to set image to
     * Post-condition: a clickable label will appear showing the image whose name was passed in
     */
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


    /** getDataFirst: used to get extra information about selected label for textview 2
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
    public void getDataFirst(View view)
    {
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


    /** getDataSecond: used to get extra information about selected label for textview 2
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
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


    /** getDataThird: used to get extra information about selected label for textview 3
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
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

    /** rotateImage: used to rotate onscreen label so they remain accurate with user movement
     *
     * @param view: which imageview is to be rotated
     * @param angle: the angle to rotate passed in imageview to
     */
    public void rotateImage(ImageView view, float angle)
    {
        //view.rotate
    }

    /** moveImageOne: allows first imageView (locateItems1) to be moved around the screen, all labels start
     *                  in the center. Also allows imageView to be moved if flag == 0,
     *                  scale only if flag == 1, move and scale if flag == 2
     *
     *                  -scale starts at 1 (eg half size would be .5)
     *                  -all images start at screen center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     * @param scaleX: float to rescale X value
     * @param scaleY: float to rescale Y value
     * @param flag: used to determine method action
     */
    public void moveImageOne(int topDP, int leftDP, float scaleX, float scaleY, int flag)
    {
        ImageView myImage = (ImageView)findViewById(R.id.locateItems1);

        //move image
        if(flag == 0)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);
        }
        //scale image
        else if(flag == 1)
        {
            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }
        //move and scale image
        else if(flag == 2)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);

            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }

    }

    /** moveImageTwo: allows second imageView (locateItems2) to be moved around the screen, all labels start
     *                  in the center. Also allows imageView to be moved if flag == 0,
     *                  scale only if flag == 1, move and scale if flag == 2
     *
     *                  -scale starts at 1 (eg half size would be .5)
     *                  -all images start at screen center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     * @param scaleX: float to rescale X value
     * @param scaleY: float to rescale Y value
     * @param flag: used to determine method action
     */
    public void moveImageTwo(int topDP, int leftDP, float scaleX, float scaleY, int flag)
    {
        ImageView myImage = (ImageView)findViewById(R.id.locateItems2);

        //move image
        if(flag == 0)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);
        }
        //scale image
        else if(flag == 1)
        {
            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }
        //move and scale image
        else if(flag == 2)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);

            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }

    }

    /** moveImageThree: allows third imageView (locateItems3) to be moved around the screen, all labels start
     *                  in the center. Also allows imageView to be moved if flag == 0,
     *                  scale only if flag == 1, move and scale if flag == 2
     *
     *                  -scale starts at 1 (eg half size would be .5)
     *                  -all images start at screen center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     * @param scaleX: float to rescale X value
     * @param scaleY: float to rescale Y value
     * @param flag: used to determine method action
     */
    public void moveImageThree(int topDP, int leftDP, float scaleX, float scaleY, int flag)
    {
        ImageView myImage = (ImageView)findViewById(R.id.locateItems3);

        //move image
        if(flag == 0)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);
        }
        //scale image
        else if(flag == 1)
        {
            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }
        //move and scale image
        else if(flag == 2)
        {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myImage.getLayoutParams();
            lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
            myImage.setLayoutParams(lp);

            myImage.setScaleX(scaleX);
            myImage.setScaleY(scaleY);
        }

    }

    /** moveTextOne: allows first TextView (infoWindow1) to be moved around the screen, all labels start
     *                  in the center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     */
    public void moveTextOne(int topDP, int leftDP)
    {
        TextView myText = (TextView)findViewById(R.id.infoWindow1);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myText.getLayoutParams();
        lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
        myText.setLayoutParams(lp);
    }

    /** moveTextTwo: allows first TextView (infoWindow2) to be moved around the screen, all labels start
     *                  in the center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     */
    public void moveTextTwo(int topDP, int leftDP)
    {
        TextView myText = (TextView)findViewById(R.id.infoWindow2);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myText.getLayoutParams();
        lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
        myText.setLayoutParams(lp);
    }

    /** moveTextThree: allows third TextView (infoWindow3) to be moved around the screen, all labels start
     *                  in the center
     *
     * @param topDP: margin from top, negative to move up, positive to move down
     * @param leftDP: margin from left, negative to move left, positive to move right
     */
    public void moveTextThree(int topDP, int leftDP)
    {
        TextView myText = (TextView)findViewById(R.id.infoWindow3);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myText.getLayoutParams();
        lp.setMargins(leftDP, topDP, 0, 0);// (left, top, right, bottom)
        myText.setLayoutParams(lp);
    }

    /** refresh: when refresh button clicked => manually update location and angle
     *
     * @param view this is what needed to be here for using the 'onclick' from the xml file
     */
    public void refresh(View view)
    {
//        float f1 = als.distanceTo(al.get(0));
//        float f2 = als.bearingTo(al.get(0));
//        CharSequence cs = "Distance to " + al.get(0).getName() + ": " + Float.toString(f1) + "\nBearing to " + al.get(0).getName() + ": " + Float.toString(f2);
//        Toast.makeText(this, cs, Toast.LENGTH_SHORT).show();
    }

    public int isInScopeAndRange(AdvancedLocation al) {
        int returnVal = View.INVISIBLE;
        if(als.distanceTo(al) <= al.getBuildingDistance())
            if(Math.abs(als.bearingTo(al)) <= 30)
                returnVal = View.VISIBLE;
        return returnVal;
    }
}//end main class




