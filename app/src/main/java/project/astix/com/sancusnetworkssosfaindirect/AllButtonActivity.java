package project.astix.com.sancusnetworkssosfaindirect;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astix.Common.CommonInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class AllButtonActivity extends BaseActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences sPrefAttandance;
    private boolean serviceException=false;
    private String passDate;
    private SharedPreferences sharedPref;
    private SharedPreferences sharedPrefReport;
    private int valDayEndOrChangeRoute=1; // 1=Clicked on Day End Button, 2=Clicked on Change Route Button
    private String whereTo = "11";
    private String[] StoreList2Procs;
    private ArrayList<String> stIDs;
    private ArrayList<String> stNames;
    private boolean[] checks;
    private int whatTask = 0;
    private long syncTIMESTAMP;
    private static int flgChangeRouteOrDayEnd = 0;
    private ProgressDialog pDialog2;
    private TableLayout tl2;

    private final ArrayList mSelectedItems = new ArrayList();
    private final int powerCheck=0;

    private PowerManager.WakeLock wl;
    private String rID="0";    // Abhinav Sir tell Sunil for set its value zero at 10 October 2017
    private LinearLayout ll_noVisit;
    private LinearLayout ll_marketVisit,ll_Survey;
    private LinearLayout ll_reports;
    private LinearLayout ll_storeVal;
    private LinearLayout ll_distrbtrStock;
    private LinearLayout ll_execution;
    String[] drsNames;
    private final DBAdapterKenya dbengine = new DBAdapterKenya(this);

   // DBAdapterKenya dbengineSo = new DBAdapterKenya(this);
    LinkedHashMap<String, String> hmapdsrIdAndDescr_details= new LinkedHashMap<>();
    public String	SelectedDSRValue="";

    private String imei;
    private int chkFlgForErrorToCloseApp=0;
    private String fDate;
    private SimpleDateFormat sdf;

    private String ReasonId;
    private String ReasonText="NA";
    private static final int RowId=0;


    private int click_but_distribtrStock=0;
    private final int CstmrNodeId=0;
    private final int CstomrNodeType= 0;
    private int battLevel=0;
    SharedPreferences sharedPrefForSurvey;
    public String newfullFileName;
    DatabaseAssistantDistributorEntry DA = new DatabaseAssistantDistributorEntry(this);
    private final DatabaseAssistant DASFA = new DatabaseAssistant(this);
  //  SyncXMLfileData task2;
    public String[] xmlForWeb = new String[1];
    int serverResponseCode = 0;
    public int syncFLAG = 0;

   // public ProgressDialog pDialogGetStores;
   private TextView noVisit_tv;
    private String[] reasonNames;
    private LinkedHashMap<String, String> hmapReasonIdAndDescr_details= new LinkedHashMap<>();

    private Date currDate;
    private SimpleDateFormat currDateFormat;
    private String currSysDate;

    private LinearLayout ll_dsrTracker;
    private LinearLayout ll_distrbtnMap;
    private LinearLayout ll_DayEnd;
    private LinearLayout ll_ChangeLanguage;
    private LinearLayout ll_PurchaseOrder;
    private ImageView imgVw_logout;

    //report alert
    String[] Distribtr_list;
    String DbrNodeId,DbrNodeType,DbrName;
    ArrayList<String> DbrArray= new ArrayList<>();
    LinkedHashMap<String,String> hmapDistrbtrList=new LinkedHashMap<>();
    public String SelectedDistrbtrName="";
    private LinkedHashMap<String, String> hmapOutletListForNear= new LinkedHashMap<>();
    private final LinkedHashMap<String, String> hmapOutletListForNearUpdated= new LinkedHashMap<>();
    private CoundownClass countDownTimer;
    LinkedHashMap<String,String> hmapStoreLatLongDistanceFlgRemap= new LinkedHashMap<>();


    //market visit loc alert
    private LocationManager locationManager;
    private android.app.AlertDialog GPSONOFFAlert=null;
    private AppLocationService appLocationService;
    private PowerManager pm;

    private ProgressDialog pDialog2STANDBY;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private LocationRequest mLocationRequest;
    private Location location;
    //    public AllButtonActivity.CoundownClass countDownTimer;
    private boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;

    private String fnAccurateProvider="";
    private String fnLati="0";
    private String fnLongi="0";
    private Double fnAccuracy=0.0;

    private String FusedLocationLatitudeWithFirstAttempt="0";
    private String FusedLocationLongitudeWithFirstAttempt="0";
    private String FusedLocationAccuracyWithFirstAttempt="0";
    private String AllProvidersLocation="";
    private final String FusedLocationLatitude="0";
    private final String FusedLocationLongitude="0";
    private final String FusedLocationProvider="";
    private final String FusedLocationAccuracy="0";

    private String GPSLocationLatitude="0";
    private String GPSLocationLongitude="0";
    private String GPSLocationProvider="";
    private String GPSLocationAccuracy="0";

    private String NetworkLocationLatitude="0";
    private String NetworkLocationLongitude="0";
    private String NetworkLocationProvider="";
    private String NetworkLocationAccuracy="0";
    public static int flgLocationServicesOnOff=0;
    public static int flgGPSOnOff=0;
    public static int flgNetworkOnOff=0;
    public static int flgFusedOnOff=0;
    public static int flgInternetOnOffWhileLocationTracking=0;
    public static int flgRestart=0;
    public static int flgStoreOrder=0;

    public static String address,pincode,city,state,latitudeToSave,longitudeToSave,accuracyToSave;
    int countSubmitClicked=0;
    private String fusedData;

    private LinearLayout ll_DistrbtrTarget,ll_EDR;


    @Override
    protected void onResume() {
        super.onResume();

        if(CommonInfo.DayStartClick==2)
        {
            SharedPreferences.Editor editor1=sPrefAttandance.edit();
            editor1.clear();
            editor1.commit();
            CommonInfo.DayStartClick=0;
            finish();

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(this.mBatInfoReceiver);

        wl.release();
    }
    private void getReasonDetail() throws IOException
    {

        int check=dbengine.countDataIntblNoVisitReasonMaster();

        hmapReasonIdAndDescr_details=dbengine.fetch_Reason_List();

        int index=0;
        if(hmapReasonIdAndDescr_details!=null)
        {
            reasonNames=new String[hmapReasonIdAndDescr_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<>(hmapReasonIdAndDescr_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                reasonNames[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_button);
        sharedPrefForSurvey=getSharedPreferences("SurveyPref", MODE_PRIVATE);
        sharedPrefForSurvey.edit().putString("FROM", "DASHBOARD").commit();

        sharedPrefReport = getSharedPreferences("Report", MODE_PRIVATE);
        sPrefAttandance=getSharedPreferences(CommonInfo.AttandancePreference, MODE_PRIVATE);


        sharedPref = getSharedPreferences(CommonInfo.Preference, MODE_PRIVATE);
        if(sharedPref.contains("CoverageAreaNodeID"))
        {
            if(sharedPref.getInt("CoverageAreaNodeID",0)!=0)
            {
                CommonInfo.CoverageAreaNodeID=sharedPref.getInt("CoverageAreaNodeID",0);
                CommonInfo.CoverageAreaNodeType=sharedPref.getInt("CoverageAreaNodeType",0);
            }
        }
        if(sharedPref.contains("SalesmanNodeId"))
        {
            if(sharedPref.getInt("SalesmanNodeId",0)!=0)
            {
                CommonInfo.SalesmanNodeId=sharedPref.getInt("SalesmanNodeId",0);
                CommonInfo.SalesmanNodeType=sharedPref.getInt("SalesmanNodeType",0);
            }
        }
        if(sharedPref.contains("flgDataScope"))
        {
            if(sharedPref.getInt("flgDataScope",0)!=0)
            {
                CommonInfo.flgDataScope=sharedPref.getInt("flgDataScope",0);

            }
        }
        if(sharedPref.contains("flgDSRSO"))
        {
            if(sharedPref.getInt("flgDSRSO",0)!=0)
            {
                CommonInfo.FlgDSRSO=sharedPref.getInt("flgDSRSO",0);

            }
        }
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
        {
            //imei = tManager.getDeviceId();
            CommonInfo.imei=imei;
        }
        else
        {
            imei=CommonInfo.imei.trim();
        }

        Date date1=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        passDate = sdf.format(date1);

        //System.out.println("Selctd Date: "+ passDate);

        fDate = passDate.trim();

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);

      /*  //  SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();*/
        shardPrefForSalesman(0,0);

        flgDataScopeSharedPref(0);
        Date date2=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date2).trim();

        currDate = new Date();
        currDateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

        currSysDate = currDateFormat.format(currDate);
        initialiseViews();
        if(powerCheck==0)
        {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
            wl.acquire();
        }
    }

 /*   private void getDSRDetail() throws IOException
    {

        int check=dbengine.countDataIntblNoVisitReasonMaster();

        hmapdsrIdAndDescr_details=dbengine.fetch_DSRCoverage_List();

        int index=0;
        if(hmapdsrIdAndDescr_details!=null)
        {
            drsNames=new String[hmapdsrIdAndDescr_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapdsrIdAndDescr_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator.next();
                drsNames[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }
*/
    private static boolean deleteNon_EmptyDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteNon_EmptyDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

  /*  public void dialogLogout()
    {

        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);

        alertDialog.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialog.setMessage(R.string.LogoutMsg);
        alertDialog.setPositiveButton(R.string.AlertDialogYesButton, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {

                CommonInfo.AnyVisit=0;

                File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!OrderXMLFolder.exists())
                {
                    OrderXMLFolder.mkdirs();
                }

                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                deleteNon_EmptyDir(del);

                File ImagesFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);
                if (!ImagesFolder.exists())
                {
                    ImagesFolder.mkdirs();
                }

                File del1 = new File(Environment.getExternalStorageDirectory(),  CommonInfo.ImagesFolder);
                deleteNon_EmptyDir(del1);

                File TextFileFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.TextFileFolder);
                if (!ImagesFolder.exists())
                {
                    ImagesFolder.mkdirs();
                }

                File del2 = new File(Environment.getExternalStorageDirectory(),  CommonInfo.TextFileFolder);
                deleteNon_EmptyDir(del2);
            try {
                dbengine.deleteViewAddedStore();
                dbengine.deletetblStoreList();
            }
            catch(Exception e)
            {

            }



                dialog.dismiss();
                finishAffinity();
            }
        });

        alertDialog.setNegativeButton(R.string.AlertDialogNoButton, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

*/

    private void initialiseViews()
    {
        ll_noVisit = (LinearLayout) findViewById(R.id.ll_noVisit);
        ll_marketVisit = (LinearLayout) findViewById(R.id.ll_marketVisit);
        ll_Survey = (LinearLayout) findViewById(R.id.ll_Survey);

        ll_reports = (LinearLayout) findViewById(R.id.ll_reports);
        ll_storeVal = (LinearLayout) findViewById(R.id.ll_storeVal);

        ll_distrbtrStock = (LinearLayout) findViewById(R.id.ll_distrbtrStock);
        ll_execution = (LinearLayout) findViewById(R.id.ll_execution);

        ll_distrbtnMap = (LinearLayout) findViewById(R.id.ll_distrbtnMap);

        ll_ChangeLanguage = (LinearLayout) findViewById(R.id.ll_ChangeLanguage);

        ll_dsrTracker = (LinearLayout) findViewById(R.id.ll_dsrTracker);
        ll_DayEnd = (LinearLayout) findViewById(R.id.ll_DayEnd);
        ll_EDR= (LinearLayout) findViewById(R.id.ll_EDR);
        ll_PurchaseOrder = (LinearLayout) findViewById(R.id.ll_PurchaseOrder);

        ll_DistrbtrTarget= (LinearLayout) findViewById(R.id.ll_DistrbtrTarget);

        ll_EDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AllButtonActivity.this,WebViewAttndnc_ScndryUpdt.class);
                intent.putExtra("flgToShow",1);

                startActivity(intent);
            }
        });
        try
        {
            getReasonDetail();
        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        imgVw_logout=(ImageView) findViewById(R.id.imgVw_logout);




        marketVisitWorking();
        SurveyWorking();
        executionWorking();

        storeValidationWorking();
        distributorStockWorking();
        dsrTrackerWorking();
        reportsWorking();

        int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();

        if (checkDataNotSync == 1)
        {

        }
        else
        {
            noVisitWorking();
        }

        changeLanguageWorking();
        distributorMapWorking();
        dayEndWorking();
        purchaseOrderWorking();

        distrbtrTargetWorking();

        imgVw_logout=(ImageView) findViewById(R.id.imgVw_logout);

        imgVw_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!OrderXMLFolder.exists())
                {
                    OrderXMLFolder.mkdirs();
                }

                imgVw_logout.setImageResource(R.drawable.logout_hover);

                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);


                // check number of files in folder
                final String [] AllFilesNameNotSync= checkNumberOfFiles(del);

                String xmlfileNames = dbengine.fnGetXMLFileAll("3");
              //  String xmlfileNamesStrMap=dbengineSo.fnGetXMLFile("3");

                dbengine.open();
                String[] SaveStoreList = dbengine.ProcessStoreReq();
                dbengine.close();

                if (SaveStoreList.length != 0)
                {
                    showAlertSingleButtonInfo(getResources().getString(R.string.DayEndBeforeLogout));

                }
                else if(xmlfileNames.length()>0)
                {
                    showAlertSingleButtonInfo(getResources().getString(R.string.DayEndBeforeLogout));
                }
                  else
                {
                    dialogLogout();

                }


            }
        });


    }

    private void distrbtrTargetWorking()
    {
        ll_DistrbtrTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FetchDistrbtrTargetData fetchData=new FetchDistrbtrTargetData();
                fetchData.execute();
            }
        });
    }

    class FetchDistrbtrTargetData extends AsyncTask
    {
        ProgressDialog pDialogGetStores;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialogGetStores=new ProgressDialog(AllButtonActivity.this);
            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getResources().getString(R.string.genTermLoadData));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();

        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            ServiceWorker newservice = new ServiceWorker();
            for(int mm = 1; mm<3; mm++) {
                if (mm == 1) {
                    int ApplicationID = DBAdapterKenya.Application_TypeID;

                    Date date2 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", Locale.ENGLISH);
                   // SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String newDate = sdf.format(date2).trim();
                    newservice = newservice.getMonthSelectionForPlanning(getApplicationContext(), Integer.parseInt(newDate), imei, String.valueOf(ApplicationID));
                    if (!newservice.director.trim().equals("1")) {
                        if (chkFlgForErrorToCloseApp == 0) {
                            chkFlgForErrorToCloseApp = 1;
                        }
                    }
                }
                if (mm == 2) {
                    int ApplicationID = DBAdapterKenya.Application_TypeID;
                    Date date1 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String fDate = sdf.format(date1).trim();

                    newservice = newservice.getfnGetSpTargetGetSalestargetMeasure(getApplicationContext(), fDate, imei, String.valueOf(ApplicationID));
                    if (!newservice.director.trim().equals("1")) {
                        if (chkFlgForErrorToCloseApp == 0) {
                            chkFlgForErrorToCloseApp = 1;
                            System.out.println("Error in...getfnGetSpTargetGetSalestargetMeasure");
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(pDialogGetStores != null && pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }
            if(chkFlgForErrorToCloseApp==1)
            {
                chkFlgForErrorToCloseApp=0;
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.errorFetchData),Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent intent=new Intent(AllButtonActivity.this,DistributorTargetActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void dsrTrackerWorking()
    {
        ll_dsrTracker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*if(ll_dsrTracker.isSelected())
                    ll_dsrTracker.setSelected(false);
                else
                    ll_dsrTracker.setSelected(true);*/
                /*Intent intent=new Intent(AllButtonActivity.this,WebViewDSRTrackerActivity.class);
                startActivity(intent);*/
                openDSRTrackerAlert();

            }
        });


    }

    private void openDSRTrackerAlert()
    {
        final AlertDialog.Builder alert=new AlertDialog.Builder(AllButtonActivity.this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dsr_tracker_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_dataReport= (RadioButton) view.findViewById(R.id.rb_dataReport);
        final RadioButton rb_onMap= (RadioButton) view.findViewById(R.id.rb_onMap);


        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog=alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if(rb_dataReport.isChecked())
                {
                    Intent i=new Intent(AllButtonActivity.this,WebViewDSRDataReportActivity.class);
                    startActivity(i);

                }
                else if(rb_onMap.isChecked())
                {
                    Intent i = new Intent(AllButtonActivity.this, WebViewDSRTrackerActivity.class);
                    startActivity(i);

                }

                else
                {
                    showAlertSingleButtonInfo(getResources().getString(R.string.selectOptionProceeds));

                }
            }
        });

        rb_dataReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_dataReport.isChecked())
                {
                    rb_onMap.setChecked(false);

                }
            }
        });

        rb_onMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_onMap.isChecked())
                {
                    rb_dataReport.setChecked(false);

                }
            }
        });



        dialog.show();
    }

    private void purchaseOrderWorking()
    {
        ll_PurchaseOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ll_PurchaseOrder.isSelected())
                    ll_PurchaseOrder.setSelected(false);
                else
                    ll_PurchaseOrder.setSelected(true);

                if(isOnline())
                {
                    Intent intent = new Intent(AllButtonActivity.this, WebViewPurchaseOrderActivity.class);
                    startActivity(intent);
                }
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                }


            }
        });
    }
    private void dayEndWorking()
    {


        ll_DayEnd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String	PersonNameAndFlgRegistered=  dbengine.fnGetPersonNameAndFlgRegistered();
                String personName="";
                String FlgRegistered="";
                int DsrRegTableCount=0;
                DsrRegTableCount=dbengine.fngetcounttblDsrRegDetails();
                if(!PersonNameAndFlgRegistered.equals("0")) {
                    personName = PersonNameAndFlgRegistered.split(Pattern.quote("^"))[0];
                    FlgRegistered = PersonNameAndFlgRegistered.split(Pattern.quote("^"))[1];
                }
                if( FlgRegistered.equals("0")&& DsrRegTableCount==0)
                {
                    android.app.AlertDialog.Builder alertDialogNoConn = new android.app.AlertDialog.Builder(AllButtonActivity.this);
                    alertDialogNoConn.setTitle(getResources().getString(R.string.genTermNoDataConnection));
                    alertDialogNoConn.setMessage(getResources().getString(R.string.Dsrmessage));
                    alertDialogNoConn.setCancelable(false);
                    alertDialogNoConn.setNeutralButton(getResources().getString(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            Intent intent=new Intent(AllButtonActivity.this,DSR_Registration.class);
                            intent.putExtra("IntentFrom", "AllButtonActivity");
                            intent.putExtra("imei", imei);
                            intent.putExtra("userDate", "NA");
                            intent.putExtra("pickerDate", "NA");

                            startActivity(intent);
                            finish();

                        }
                    });
                    alertDialogNoConn.setIcon(R.drawable.info_ico);
                    android.app.AlertDialog alert = alertDialogNoConn.create();
                    alert.show();

                }
                else

                {

                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);


                // check number of files in folder
                final String [] AllFilesNameNotSync= checkNumberOfFiles(del);

                String xmlfileNames = dbengine.fnGetXMLFileAll("3");
               // String xmlfileNamesStrMap=dbengineSo.fnGetXMLFile("3");

                dbengine.open();
                String[] SaveStoreList = dbengine.SaveStoreList();
                dbengine.close();
                if(xmlfileNames.length()>0 || SaveStoreList.length != 0)
                {
                    if(isOnline())
                    {



                        whereTo = "11";
                        //////System.out.println("Abhinav store Selection  Step 1");
                        //////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);

                        //////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);
                        dbengine.open();

                        StoreList2Procs = dbengine.ProcessStoreReq();
                        if (StoreList2Procs.length != 0) {
                            //whereTo = "22";
                            //////System.out.println("Abhinav store Selection  Step 2");
                            midPart();
                            dayEndCustomAlert(1);
                            //showPendingStorelist(1);
                            dbengine.close();

                        } else if (dbengine.GetLeftStoresChk())
                        {
                            //////System.out.println("Abhinav store Selection  Step 7");
                            //enableGPSifNot();
                            // showChangeRouteConfirm();
                            DayEnd();
                            dbengine.close();
                        }
                        else {
                            DayEndWithoutalert();
                        }
                    }
                    else
                    {
                        showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                    }
                }
                else
                {
                    //showAlertSingleButtonInfo(getResources().getString(R.string.NoPendingDataMsg));
                    if(isOnline()) {
                        whereTo = "11";
                        DayEndWithoutalert();
                    }else
                    {
                        showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                    }
                }



            }}
        });

    }

    private void dayEndCustomAlert(int flagWhichButtonClicked)
    {
        final Dialog dialog = new Dialog(AllButtonActivity.this,R.style.AlertDialogDayEndTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.day_end_custom_alert);
        if(flagWhichButtonClicked==1)
        {
            dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteDayEnd);
        }
        else if(flagWhichButtonClicked==2)
        {
            dialog.setTitle(R.string.genTermSelectStoresPendingToCompleteChangeRoute);
        }



        LinearLayout ll_product_not_submitted=(LinearLayout) dialog.findViewById(R.id.ll_product_not_submitted);
        mSelectedItems.clear();
        final String[] stNames4List = new String[stNames.size()];
        checks=new boolean[stNames.size()];
        stNames.toArray(stNames4List);

        for(int cntPendingList=0;cntPendingList<stNames4List.length;cntPendingList++)
        {
            LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View viewAlertProduct=inflater.inflate(R.layout.day_end_alrt,null);
            final TextView txtVw_product_name=(TextView) viewAlertProduct.findViewById(R.id.txtVw_product_name);
            txtVw_product_name.setText(stNames4List[cntPendingList]);
            txtVw_product_name.setTextColor(this.getResources().getColor(R.color.green_submitted));
            final ImageView img_to_be_submit=(ImageView) viewAlertProduct.findViewById(R.id.img_to_be_submit);
            img_to_be_submit.setTag(cntPendingList);

            final ImageView img_to_be_cancel=(ImageView) viewAlertProduct.findViewById(R.id.img_to_be_cancel);
            img_to_be_cancel.setTag(cntPendingList);
            img_to_be_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    if(!checks[Integer.valueOf(img_to_be_submit.getTag().toString())])
                    {
                        img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_hover) );
                        img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_normal) );
                        txtVw_product_name.setTextColor(getResources().getColor(R.color.green_submitted));
                        mSelectedItems.add(stNames4List[Integer.valueOf(img_to_be_submit.getTag().toString())]);
                        checks[Integer.valueOf(img_to_be_submit.getTag().toString())]=true;
                    }


                }
            });

            img_to_be_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mSelectedItems.contains(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())]))
                    {
                        if(checks[Integer.valueOf(img_to_be_cancel.getTag().toString())])
                        {

                            img_to_be_submit.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.select_normal) );
                            img_to_be_cancel.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel_hover) );
                            txtVw_product_name.setTextColor(Color.RED);
                            mSelectedItems.remove(stNames4List[Integer.valueOf(img_to_be_cancel.getTag().toString())]);
                            checks[Integer.valueOf(img_to_be_cancel.getTag().toString())]=false;
                        }

                    }

                }
            });
            mSelectedItems.add(stNames4List[cntPendingList]);
            checks[cntPendingList]=true;
            ll_product_not_submitted.addView(viewAlertProduct);
        }


        Button btnSubmit=(Button) dialog.findViewById(R.id.btnSubmit);
        Button btn_cancel_Back=(Button) dialog.findViewById(R.id.btn_cancel_Back);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSelectedItems.size() == 0) {

                    DayEnd();


                }

                else {

                    int countOfOrderNonSelected=0;
                    for (boolean check : checks) {
                        if (!check) {
                            countOfOrderNonSelected++;
                        }

                    }
                    if(countOfOrderNonSelected>0)
                    {
                        confirmationForSubmission(String.valueOf(countOfOrderNonSelected));
                    }

                    else
                    {


                        whatTask = 2;
                        // -- Route Info Exec()
                        try {

                            new bgTasker().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            //System.out.println(e);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            //System.out.println(e);
                        }
                        // --
                    }

                }

            }
        });

        btn_cancel_Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                valDayEndOrChangeRoute=0;
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);


        dialog.show();




    }

    private class bgTasker extends AsyncTask<Void, Void, Void> {

        // obj(s) for services/sync..blah..blah

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //System.out.println("starting bgTasker Exec().....: ");




                dbengine.open();
                String rID=dbengine.GetActiveRouteID();

                dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
                //System.out.println("TblDayStartEndDetails Background: "+ rID);
                //System.out.println("TblDayStartEndDetails Background valDayEndOrChangeRoute: "+ valDayEndOrChangeRoute);
                dbengine.close();

                //System.out.println("Induwati   whatTask :"+whatTask);

                if (whatTask == 2)
                {
                    whatTask = 0;
                    // stores with Sstat = 1 !
                    dbengine.open();
                    // dbengine.fnTruncateTblSelectedStoreIDinChangeRouteCase();
                    for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++)
                    {
                        String valSN = (String) mSelectedItems.get(nosSelected);
                        int valID = stNames.indexOf(valSN);
                        String stIDneeded = stIDs.get(valID);

                        // String[] stIDs;
                        // String[] stNames;

                        dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 3);
                        dbengine.UpdateNewAddedStorephotoFlag(stIDneeded.trim(), 3);
                        //System.out.println("stIDneeded : " + stIDneeded);
                        dbengine.insertTblSelectedStoreIDinChangeRouteCase(stIDneeded);
                        dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);
                        if(dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded)==1)
                        {
                            dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
                        }
                    }
                    // dbengine.ProcessStoreReq();
                    dbengine.close();

                    pDialog2.dismiss();
                    dbengine.open();

                    //dbengine.updateActiveRoute(rID, 0);
                    dbengine.close();
                    // sync here


                    SyncNow();


                }else if (whatTask == 3) {
                    // sync rest
                    whatTask = 0;

                    pDialog2.dismiss();
                    //dbengine.open();
                    //String rID=dbengine.GetActiveRouteID();
                    //dbengine.updateActiveRoute(rID, 0);
                    //dbengine.close();
                    // sync here

                    SyncNow();


					/*
					 * dbengine.open(); dbengine.reCreateDB(); dbengine.close();
					 */
                }else if (whatTask == 1) {
                    // clear all
                    whatTask = 0;

                    SyncNow();

                    dbengine.open();
                    //String rID=dbengine.GetActiveRouteID();
                    //dbengine.updateActiveRoute(rID, 0);
                    dbengine.reCreateDB();

                    dbengine.close();
                }/*else if (whatTask == 0)
				{
					try {
						new FullSyncDataNow().execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/


            } catch (Exception e) {
                Log.i("bgTasker", "bgTasker Execution Failed!", e);

            }

            finally {

                Log.i("bgTasker", "bgTasker Execution Completed...");

            }

            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog2 = ProgressDialog.show(AllButtonActivity.this,getText(R.string.PleaseWaitMsg),getText(R.string.genTermProcessingRequest), true);
            pDialog2.setIndeterminate(true);
            pDialog2.setCancelable(false);
            pDialog2.show();

        }

        @Override
        protected void onCancelled() {
            Log.i("bgTasker", "bgTasker Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.i("bgTasker", "bgTasker Execution cycle completed");
            pDialog2.dismiss();
            whatTask = 0;

        }
    }

    private void SyncNow()
    {

        syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);


        dbengine.open();
        String presentRoute=dbengine.GetActiveRouteID();
        dbengine.close();
        //syncTIMESTAMP = System.currentTimeMillis();
        //Date dateobj = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
        String newfullFileName=imei+"."+presentRoute+"."+df.format(dateobj);



        try
        {

            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!OrderXMLFolder.exists())
            {
                OrderXMLFolder.mkdirs();
            }

            String routeID=dbengine.GetActiveRouteIDSunil();

            DASFA.open();
            DASFA.export(DBAdapterKenya.DATABASE_NAME, newfullFileName,routeID);


            DASFA.close();

            dbengine.savetbl_XMLfiles(newfullFileName, "3","1");
            dbengine.open();
            for (int nosSelected = 0; nosSelected <= mSelectedItems.size() - 1; nosSelected++)
            {
                String valSN = (String) mSelectedItems.get(nosSelected);
                int valID = stNames.indexOf(valSN);
                String stIDneeded = stIDs.get(valID);

                dbengine.UpdateStoreFlagAtDayEndOrChangeRoute(stIDneeded, 4);
                dbengine.UpdateStoreMaterialphotoFlag(stIDneeded.trim(), 5);
                dbengine.UpdateStoreReturnphotoFlag(stIDneeded.trim(), 5);
                dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);
                dbengine.UpdateStoreClosephotoFlag(stIDneeded.trim(), 5);
                dbengine.UpdateNewAddedStorephotoFlag(stIDneeded.trim(), 5);

                if(dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded)==1)
                {
                    dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
                }


            }
            dbengine.close();

            flgChangeRouteOrDayEnd=valDayEndOrChangeRoute;

            Intent syncIntent = new Intent(AllButtonActivity.this, SyncMaster.class);
            //syncIntent.putExtra("xmlPathForSync",Environment.getExternalStorageDirectory() + "/TJUKIndirectSFAxml/" + newfullFileName + ".xml");
            syncIntent.putExtra("xmlPathForSync", Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml");
            syncIntent.putExtra("OrigZipFileName", newfullFileName);
            syncIntent.putExtra("whereTo", whereTo);
            startActivity(syncIntent);
            finish();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private void confirmationForSubmission(String number)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(AllButtonActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.txtConfirmCancel));

        // Setting Dialog Message
        if(1<Integer.valueOf(number))
        {
            alertDialog.setMessage(getResources().getString(R.string.txtYouWant)+ number +getResources().getString(R.string.txtOrderCancel));
        }
        else
        {
            alertDialog.setMessage(getResources().getString(R.string.txtYouWant)+ number +getResources().getString(R.string.txtOrderCancelQues));
        }

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.cancel_hover);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getResources().getString(R.string.AlertDialogYesButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {



                whatTask = 2;
                // -- Route Info Exec()
                try {

                    new bgTasker().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //System.out.println(e);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    //System.out.println(e);
                }
                // --


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.AlertDialogNoButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void DayEnd()
    {


        android.app.AlertDialog.Builder alertDialogSubmitConfirm = new android.app.AlertDialog.Builder(AllButtonActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.titlebar, null);
        alertDialogSubmitConfirm.setCustomTitle(view);
        TextView title_txt = (TextView) view.findViewById(R.id.title_txt);
        title_txt.setText(getText(R.string.PleaseConformMsg));


        View view1=inflater.inflate(R.layout.custom_alert_dialog, null);
        view1.setBackgroundColor(Color.parseColor("#1D2E3C"));
        TextView msg_txt = (TextView) view1.findViewById(R.id.msg_txt);
        msg_txt.setText(getText(R.string.genTermDayEndAlert));
        alertDialogSubmitConfirm.setView(view1);
        alertDialogSubmitConfirm.setInverseBackgroundForced(true);



        alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                //System.out.println("Abhinav store Selection  Step 9");
                // Location_Getting_Service.closeFlag = 1;
                //enableGPSifNot();

                // run bgTasker()!

                // if(!scheduler.isTerminated()){
                // scheduler.shutdownNow();
                // }
                dbengine.open();
                //System.out.println("Day end before");
                if (dbengine.GetLeftStoresChk()) {
                    //System.out.println("Abhinav store Selection  Step 10");
                    //System.out.println("Day end after");
                    // run bgTasker()!

                    // Location_Getting_Service.closeFlag = 1;
                    // scheduler.shutdownNow();

                    //enableGPSifNot();
                    // scheduler.shutdownNow();

                    dbengine.close();

                    whatTask = 3;
                    // -- Route Info Exec()
                    try {

                        new bgTasker().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    }
                    // --
                }
                else {
                    //System.out.println("Abhinav store Selection  Step 11");
                    // show dialog for clear..clear + tranx to launcher

                    // -- Route Info Exec()
                    try {
                        dbengine.close();
                        //System.out.println("Day end before whatTask");
                        whatTask = 1;
                        new bgTasker().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        //System.out.println(e);
                    }
                    // --

							/*dbengine.open();
							String rID=dbengine.GetActiveRouteID();
							//dbengine.updateActiveRoute(rID, 0);
							dbengine.close();
							 Intent revupOldFriend = new Intent(StoreSelection.this,LauncherActivity.class);
							 revupOldFriend.putExtra("imei", imei);
							  startActivity(revupOldFriend);
							  finish();*/

                }

            }
        });

        alertDialogSubmitConfirm.setNegativeButton(R.string.AlertDialogNoButton,new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        alertDialogSubmitConfirm.setIcon(R.drawable.info_ico);
        android.app.AlertDialog alert = alertDialogSubmitConfirm.create();
        alert.show();

    }

    private void DayEndWithoutalert()
    {

        dbengine.open();
        String rID=dbengine.GetActiveRouteID();

        dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
        dbengine.close();

        SyncNow();

    }

    private void dialogLogout()
    {

        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);

        alertDialog.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialog.setMessage(R.string.LogoutMsg);
        alertDialog.setPositiveButton(R.string.AlertDialogYesButton, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {

                CommonInfo.AnyVisit=0;

               /* File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!OrderXMLFolder.exists())
                {
                    OrderXMLFolder.mkdirs();
                }

                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                deleteNon_EmptyDir(del);

                File ImagesFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.ImagesFolder);
                if (!ImagesFolder.exists())
                {
                    ImagesFolder.mkdirs();
                }

                File del1 = new File(Environment.getExternalStorageDirectory(),  CommonInfo.ImagesFolder);
                deleteNon_EmptyDir(del1);

                File TextFileFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.TextFileFolder);
                if (!ImagesFolder.exists())
                {
                    ImagesFolder.mkdirs();
                }

                File del2 = new File(Environment.getExternalStorageDirectory(),  CommonInfo.TextFileFolder);
                deleteNon_EmptyDir(del2);*/
                try {
                    dbengine.deleteViewAddedStore();
                    dbengine.deletetblStoreList();
                }
                catch(Exception e)
                {

                }

                dialog.dismiss();

                    finishAffinity();


            }
        });

        alertDialog.setNegativeButton(R.string.AlertDialogNoButton, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private static String[] checkNumberOfFiles(File dir)
    {
        int NoOfFiles=0;
        String [] Totalfiles = null;

        if (dir.isDirectory())
        {
            String[] children = dir.list();
            NoOfFiles=children.length;
            Totalfiles=new String[children.length];

            System.arraycopy(children, 0, Totalfiles, 0, children.length);
        }
        return Totalfiles;
    }
/*
    void getDistribtrList()
    {
        dbengine.open();

        Distribtr_list=dbengine.getDistributorDataMstr();
        dbengine.close();
        for(int i=0;i<Distribtr_list.length;i++)
        {
            String value=Distribtr_list[i];
            DbrNodeId=value.split(Pattern.quote("^"))[0];
            DbrNodeType=value.split(Pattern.quote("^"))[1];
            DbrName=value.split(Pattern.quote("^"))[2];
            //flgReMap=Integer.parseInt(value.split(Pattern.quote("^"))[3]);

            hmapDistrbtrList.put(DbrName,DbrNodeId+"^"+DbrNodeType);
            DbrArray.add(DbrName);
        }

    }*/

    private void midPart()
    {
        String tempSID;
        String tempSNAME;

        stIDs = new ArrayList<>(StoreList2Procs.length);
        stNames = new ArrayList<>(StoreList2Procs.length);

        for (int x = 0; x < (StoreList2Procs.length); x++)
        {
            StringTokenizer tokens = new StringTokenizer(String.valueOf(StoreList2Procs[x]), "%");
            tempSID = tokens.nextToken().trim();
            tempSNAME = tokens.nextToken().trim();

            stIDs.add(x, tempSID);
            stNames.add(x, tempSNAME);
        }
    }






private void changeLanguageWorking()
{
    ll_ChangeLanguage.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {


            final Dialog dialogLanguage = new Dialog(AllButtonActivity.this);
            dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLanguage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

            dialogLanguage.setCancelable(false);
            dialogLanguage.setContentView(R.layout.language_popup);

            TextView textviewEnglish=(TextView) dialogLanguage.findViewById(R.id.textviewEnglish);
            TextView textviewHindi=(TextView) dialogLanguage.findViewById(R.id.textviewHindi);

            textviewEnglish.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    dialogLanguage.dismiss();
                    setLanguage("en");
                }
            });
            textviewHindi.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    dialogLanguage.dismiss();
                    setLanguage("hi");
                }
            });

            dialogLanguage.show();


        }
    });
}

    private void distributorMapWorking()
    {
        ll_distrbtnMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ll_distrbtnMap.isSelected())
                    ll_distrbtnMap.setSelected(false);
                else
                    ll_distrbtnMap.setSelected(true);

                Intent intent=new Intent(AllButtonActivity.this,DistributorMapActivity.class);
                startActivity(intent);




            }
        });
    }

    private void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
        saveLocale(language);
        // updateTexts();
        //you can refresh or you can settext
       /* Intent refresh = new Intent(StoreSelection.this, LauncherActivity.class);
        startActivity(refresh);
        finish();*/

        finish();
        startActivity(getIntent());

    }

    private void saveLocale(String lang)
    {


        SharedPreferences prefs = getSharedPreferences("LanguagePref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }
    private void noVisitWorking()
    {
        int submitFlag= CommonInfo.AnyVisit;
        noVisit_tv=(TextView) findViewById(R.id.noVisit_tv);

        int check=dbengine.fetchflgHasVisitFromtblNoVisitStoreDetails(""+4);
        if(check==0 && submitFlag==0) // 0 means user did not do any visit or getStore
        {
            ll_noVisit.setEnabled(true);
        }
        else
        {
            ll_noVisit.setEnabled(false);
            String aab=dbengine.fetchReasonDescr();
            if(ReasonText.equals("") || ReasonText.equals("NA") || ReasonText.equals(null))
            {

                noVisit_tv.setText(getResources().getString(R.string.txtNoStoreVisittoday));
            }
            else
            {
                noVisit_tv.setText(getResources().getString(R.string.txtReason)+ReasonText);
            }
        }

        ll_noVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                noVisitAlertDialog();
            }
        });

    }

    private void noVisitAlertDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_nostore, null);
        final EditText et_Reason = (EditText) alertLayout.findViewById(R.id.et_Reason);
        et_Reason.setVisibility(View.INVISIBLE);

        final Spinner spinner_reason=(Spinner) alertLayout.findViewById(R.id.spinner_reason);

        ArrayAdapter adapterCategory=new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item,reasonNames);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_reason.setAdapter(adapterCategory);

        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                String	spinnerReasonSelected = spinner_reason.getSelectedItem().toString();
                ReasonText=spinnerReasonSelected;
                int check=dbengine.fetchFlgToShowTextBox(spinnerReasonSelected);
                ReasonId=dbengine.fetchReasonIdBasedOnReasonDescr(spinnerReasonSelected);
                if(check==0)
                {
                    et_Reason.setVisibility(View.INVISIBLE);
                }
                else
                {
                    et_Reason.setVisibility(View.VISIBLE);
                }


                //ReasonId,ReasonText
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub

            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.AlertDialogHeaderMsg);
        alert.setView(alertLayout);
        //alert.setIcon(R.drawable.info_ico);
        alert.setCancelable(false);
        alert.setNegativeButton(getText(R.string.AlertDialogCancelButton), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        alert.setPositiveButton(getText(R.string.Submit), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                noVisit_tv.setEnabled(false);

                if (ReasonText.equals("")||ReasonText.equals("Select Reason"))
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
                    alertDialog.setTitle(getText(R.string.txtErr));
                    alertDialog.setMessage(getText(R.string.txtSelectReason));
                    alertDialog.setIcon(R.drawable.error);
                    alertDialog.setCancelable(false);

                    alertDialog.setPositiveButton(getResources().getString(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog,int which)
                        {
                            dialog.dismiss();
                            noVisitAlertDialog();

                        }
                    });
                    alertDialog.show();
                }
                else
                {

                    // code for matching password
                    String reason;
                    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imei = tManager.getDeviceId();
                    if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
                    {
                        //imei = tManager.getDeviceId();
                        CommonInfo.imei=imei;
                    }
                    else
                    {
                        imei= CommonInfo.imei.trim();
                    }


                    Date pdaDate=new Date();
                    SimpleDateFormat	sdfPDaDate = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                    String CurDate = sdfPDaDate.format(pdaDate).trim();

                    if(et_Reason.isShown())
                    {

                        if(TextUtils.isEmpty(et_Reason.getText().toString().trim()))
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
                            alertDialog.setTitle(getText(R.string.txtErr));
                            alertDialog.setMessage(getText(R.string.txtEnterReason));
                            alertDialog.setIcon(R.drawable.error);
                            alertDialog.setCancelable(false);

                            alertDialog.setPositiveButton(getResources().getString(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int which)
                                {
                                    dialog.dismiss();
                                    noVisitAlertDialog();

                                }
                            });
                            alertDialog.show();
                        }
                        else
                        {
                            ReasonText = et_Reason.getText().toString();
                            if(isOnline())
                            {
                                GetNoStoreVisitForDay task = new GetNoStoreVisitForDay(AllButtonActivity.this);
                                task.execute();
                            }
                            else
                            {
                                dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId,ReasonText);
                                dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                                dbengine.updateSstattblNoVisitStoreDetails(3);

                                //String[] aa= dbengine.fnGetALLDataInfo();
                                String aab=dbengine.fetchReasonDescr();
                                noVisit_tv.setText(getResources().getString(R.string.txtReason)+ReasonText);



                                showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));

                            }



                        }
                    }
                    else
                    {
                        if(isOnline())
                        {
                            GetNoStoreVisitForDay task = new GetNoStoreVisitForDay(AllButtonActivity.this);
                            task.execute();
                        }
                        else
                        {
                            dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId,ReasonText);
                            dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                            dbengine.updateSstattblNoVisitStoreDetails(3);
                            showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));

                        }


                    }


                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        startLocationUpdates();
    }

    private void startLocationUpdates()
    {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, this);
        }
        catch(SecurityException e)
        {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //updateUI();
    }

    private class GetNoStoreVisitForDay extends AsyncTask<Void, Void, Void>
    {



        GetNoStoreVisitForDay(AllButtonActivity activity)
        {

        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            showProgress(getResources().getString(R.string.SubmittingDataMsg));
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try {


                for(int mm = 1; mm < 2  ; mm++)
                {
                    if(mm==1)
                    {
                        newservice = newservice.getCallspSaveReasonForNoVisit(getApplicationContext(), fDate, imei, ReasonId,ReasonText);

                        if(!newservice.director.trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                            }

                        }

                    }



                }


            } catch (Exception e)
            {
                Log.i("SvcMgr", "Service Execution Failed!", e);
            }

            finally
            {
                Log.i("SvcMgr", "Service Execution Completed...");
            }

            return null;
        }

        @Override
        protected void onCancelled()
        {
            Log.i("SvcMgr", "Service Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            dismissProgress();
             if(chkFlgForErrorToCloseApp==0)
            {
                dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId,ReasonText);
                dbengine.updateCurDatetblNoVisitStoreDetails(fDate);

                dbengine.updateSstattblNoVisitStoreDetails(3);
                String aab=dbengine.fetchReasonDescr();
                noVisit_tv.setText(getResources().getString(R.string.txtReason)+ReasonText);
            }
            else
            {


                if(RowId==0)
                {
                    dbengine.updateReasonIdAndDescrtblNoVisitStoreDetails(ReasonId,ReasonText);
                    dbengine.updateCurDatetblNoVisitStoreDetails(fDate);
                    dbengine.updateSstattblNoVisitStoreDetails(3);
                    String aab=dbengine.fetchReasonDescr();
                    noVisit_tv.setText(getResources().getString(R.string.txtReason)+ReasonText);
                    ll_noVisit.setEnabled(false);

                }
                else
                {
                    dbengine.updateSstattblNoVisitStoreDetailsAfterSync(4);
                    String aab=dbengine.fetchReasonDescr();
                    noVisit_tv.setText(getResources().getString(R.string.txtReason)+ReasonText);
                    ll_noVisit.setEnabled(false);

                }
                finishAffinity();
            }
        }
    }
private void SurveyWorking(){
    ll_Survey.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent storeIntent = new Intent(AllButtonActivity.this, DialogActivity_MarketVisit.class);
            storeIntent.putExtra("PageFrom", "0");
            storeIntent.putExtra("FROM", "SURVEY");
            storeIntent.putExtra("imei", imei);
            startActivity(storeIntent);
        }
    });
}
    private void marketVisitWorking()
    {
        ll_marketVisit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                /*  int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();
                    Date date1 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    fDate = sdf.format(date1).toString().trim();
                    if (checkDataNotSync == 1)
                    {
                        dbengine.open();
                        String rID = dbengine.GetActiveRouteID();
                        dbengine.close();

                        // Date date=new Date();
                        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String fDateNew = sdf.format(date1).toString();
                        //fDate = passDate.trim().toString();


                        // In Splash Screen SP, we are sending this Format "dd-MMM-yyyy"
                        // But InLauncher Screen SP, we are sending this Format "dd-MM-yyyy"


                        Intent storeIntent = new Intent(AllButtonActivity.this, StoreSelection.class);
                        storeIntent.putExtra("imei", imei);
                        storeIntent.putExtra("userDate", fDate);
                        storeIntent.putExtra("pickerDate", fDateNew);
                        storeIntent.putExtra("rID", rID);
                        startActivity(storeIntent);
                        finish();

                    }
                    else
                    {
                        if(isOnline())
                        {
                            GetStoresForDay task = new GetStoresForDay(AllButtonActivity.this);
                            task.execute();
                        }
                        else
                        {
                            showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                        }
                    }*/

                Intent storeIntent = new Intent(AllButtonActivity.this, DialogActivity_MarketVisit.class);
                storeIntent.putExtra("PageFrom", "0");
                storeIntent.putExtra("imei", imei);
                storeIntent.putExtra("FROM", "MARKETVISIT");
                startActivity(storeIntent);


            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(GPSONOFFAlert!=null && GPSONOFFAlert.isShowing())
        {
            GPSONOFFAlert.dismiss();
        }
    }


    private void reportsWorking()
    {
        ll_reports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                SharedPreferences.Editor editor = sharedPrefReport .edit();
                editor.putString("fromPage", "1");
                editor.commit();
               /* Intent intent=new Intent(AllButtonActivity.this,DetailReportSummaryActivity.class);

                intent.putExtra("imei", imei);
                intent.putExtra("userDate",currSysDate);
                intent.putExtra("pickerDate", fDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");

                startActivity(intent);
                finish();*/


                Intent storeIntent = new Intent(AllButtonActivity.this, DialogActivity_Report.class);

                startActivity(storeIntent);


            }
        });
    }

  /*  void openReportAlert()
    {
        final AlertDialog.Builder alert=new AlertDialog.Builder(AllButtonActivity.this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.report_visit_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_myReport= (RadioButton) view.findViewById(R.id.rb_myReport);
        final RadioButton rb_dsrReport= (RadioButton) view.findViewById(R.id.rb_dsrReport);
        final RadioButton rb_WholeReport= (RadioButton) view.findViewById(R.id.rb_WholeReport);
        final Spinner spinner_dsrVisit= (Spinner) view.findViewById(R.id.spinner_dsrVisit);

        final RadioButton rb_distrbtrScope= (RadioButton) view.findViewById(R.id.rb_distrbtrScope);
        final Spinner spinner_distrbtrScope= (Spinner) view.findViewById(R.id.spinner_distrbtrScope);

        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

        final AlertDialog dialog=alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if(rb_myReport.isChecked())
                {
                    String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    int tempSalesmanNodeId=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    int tempSalesmanNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                    shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);

                    flgDataScopeSharedPref(1);
                    CommonInfo.SalesmanNodeId=0;
                    CommonInfo.SalesmanNodeType=0;
                    CommonInfo.flgDataScope=1;
                    Intent i=new Intent(AllButtonActivity.this,DetailReportSummaryActivityForAll.class);
                    startActivity(i);
                    finish();
                }
                else if(rb_WholeReport.isChecked())
                {
                    String SONodeIdAndNodeType= dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();

                    CommonInfo.PersonNodeID=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    CommonInfo.PersonNodeType=Integer.parseInt(SONodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                    shardPrefForSalesman(0,0);
                    flgDataScopeSharedPref(3);
                    Intent i=new Intent(AllButtonActivity.this,DetailReportSummaryActivityForAll.class);
                    startActivity(i);
                    finish();
                }
                else if(rb_dsrReport.isChecked())
                {
                    if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSM") && !SelectedDSRValue.equals("No DSM") )
                    {

                        String DSRNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
                        int tempSalesmanNodeId=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);
                        shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);
                        flgDataScopeSharedPref(2);

                        Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                    }
                }
                else if(rb_distrbtrScope.isChecked())
                {
                    if(!SelectedDistrbtrName.equals("") && !SelectedDistrbtrName.equals("Select Distributor") && !SelectedDistrbtrName.equals("No Distributor") )
                    {
                        String DbrNodeIdAndNodeType= hmapDistrbtrList.get(SelectedDistrbtrName);
                        int tempSalesmanNodeId=Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                        int tempSalesmanNodeType=Integer.parseInt(DbrNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                        shardPrefForSalesman(tempSalesmanNodeId,tempSalesmanNodeType);

                        flgDataScopeSharedPref(4);
                        Intent i = new Intent(AllButtonActivity.this, DetailReportSummaryActivityForAll.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        showAlertForEveryOne("Please select Distributor to Proceed.");
                    }
                }
                else
                {
                    showAlertForEveryOne("Please select atleast one option to Proceed.");
                }
            }
        });

        rb_myReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_myReport.isChecked())
                {
                    rb_dsrReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);
                }
            }
        });
        rb_WholeReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_WholeReport.isChecked())
                {
                    rb_dsrReport.setChecked(false);
                    rb_myReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);
                }
            }
        });

        rb_dsrReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_dsrReport.isChecked())
                {
                    rb_myReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    rb_distrbtrScope.setChecked(false);
                    spinner_distrbtrScope.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory=new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item,drsNames);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_dsrVisit.setAdapter(adapterCategory);
                    spinner_dsrVisit.setVisibility(View.VISIBLE);

                    spinner_dsrVisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                        {
                            // TODO Auto-generated method stub
                            SelectedDSRValue = spinner_dsrVisit.getSelectedItem().toString();
                            ReasonText=spinnerReasonSelected;
                            int check=dbengine.fetchFlgToShowTextBox(spinnerReasonSelected);
                            ReasonId=dbengine.fetchReasonIdBasedOnReasonDescr(spinnerReasonSelected);
                            if(check==0)
                            {
                                et_Reason.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                et_Reason.setVisibility(View.VISIBLE);
                            }


                            //ReasonId,ReasonText
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {
                        }
                    });

                }
            }
        });

        rb_distrbtrScope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_distrbtrScope.isChecked())
                {
                    rb_myReport.setChecked(false);
                    rb_WholeReport.setChecked(false);
                    rb_dsrReport.setChecked(false);
                    spinner_dsrVisit.setVisibility(View.GONE);

                    ArrayAdapter adapterCategory=new ArrayAdapter(AllButtonActivity.this, android.R.layout.simple_spinner_item,DbrArray);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_distrbtrScope.setAdapter(adapterCategory);
                    spinner_distrbtrScope.setVisibility(View.VISIBLE);

                    spinner_distrbtrScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                        {
                            SelectedDistrbtrName = spinner_distrbtrScope.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {
                        }
                    });
                }
            }
        });


        dialog.show();
    }
*/
  private void storeValidationWorking()
    {
        ll_storeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                GetStoreAllData getStoreAllDataAsync= new GetStoreAllData();
                getStoreAllDataAsync.execute();
               /* dbengine.open();
                String allLoctionDetails=  dbengine.getLocationDetails();
                dbengine.close();
                if(allLoctionDetails.equals("0"))
                {
                    firstTimeLocationTrack();
                }
                else
                    {
                        Intent intent =new Intent(AllButtonActivity.this,StorelistActivity.class);
                    AllButtonActivity.this.startActivity(intent);
                    finish();

                }*/


            }
        });
    }

    private void distributorStockWorking()
    {
        ll_distrbtrStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dbengine.open();
                dbengine.maintainPDADate();
                String getPDADate=dbengine.fnGetPdaDate();
                String getServerDate=dbengine.fnGetServerDate();

                dbengine.close();


                if(!getServerDate.equals(getPDADate))
                {
                    if(isOnline())
                    {

                        try
                        {
                            click_but_distribtrStock=1;
                           // FullSyncDataNow task = new FullSyncDataNow(AllButtonActivity.this);
                           // task.execute();
                        }
                        catch(Exception e)
                        {

                        }
                    }
                    else
                    {
                        showAlertSingleButtonInfo(getResources().getString(R.string.genTermNoDataConnectionFullMsg));
                    }
                }
                else
                {

                    if(imei==null)
                    {
                        imei=CommonInfo.imei;
                    }
                    if(fDate==null)
                    {
                        Date date1 = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        fDate = sdf.format(date1).trim();
                    }

                    Intent i=new Intent(AllButtonActivity.this,DistributorEntryActivity.class);
                    i.putExtra("imei", imei);
                    i.putExtra("CstmrNodeId", CstmrNodeId);
                    i.putExtra("CstomrNodeType", CstomrNodeType);
                    i.putExtra("fDate", fDate);
                    startActivity(i);
                   // finish();
                }
            }
        });
    }



    private void executionWorking()
    {
        ll_execution.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isOnline())
                {
                    try
                    {
                       GetInvoiceForDay task = new GetInvoiceForDay(AllButtonActivity.this);
                        task.execute();


                    }
                    catch (Exception e)
                    {
                        // e.printStackTrace();
                    }
                }
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                }
            }
        });
    }

    private class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
    {




        GetInvoiceForDay(AllButtonActivity activity)
        {

        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            showProgress(getResources().getString(R.string.RetrivingDataMsg));


        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try {

                HashMap<String,String> hmapInvoiceOrderIDandStatus= new HashMap<>();
                hmapInvoiceOrderIDandStatus=dbengine.fetchHmapInvoiceOrderIDandStatus();

                for(int mm = 1; mm < 5  ; mm++)
                {
                    if(mm==1)
                    {
                        newservice = newservice.callInvoiceButtonStoreMstr(getApplicationContext(), fDate, imei, rID,hmapInvoiceOrderIDandStatus);

                        if(!newservice.director.trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                            }

                        }

                    }
                    if(mm==2)
                    {
                        newservice = newservice.callInvoiceButtonProductMstr(getApplicationContext(), fDate, imei, rID);

                        if(!newservice.director.trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                            }

                        }

                    }
                    if(mm==3)
                    {
                        newservice = newservice.callInvoiceButtonStoreProductwiseOrder(getApplicationContext(), fDate, imei, rID,hmapInvoiceOrderIDandStatus);
                    }
                    if(mm==4)
                    {
                        dbengine.open();
                        int check1=dbengine.counttblCatagoryMstr();
                        dbengine.close();
                        if(check1==0)
                        {
                            newservice = newservice.getCategory(getApplicationContext(), imei);
                        }
                    }



                }


            } catch (Exception e)
            {
                dismissProgress();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            dismissProgress();
            Intent storeIntent = new Intent(AllButtonActivity.this, InvoiceStoreSelection.class);
            storeIntent.putExtra("imei", imei);
            storeIntent.putExtra("userDate", currSysDate);
            storeIntent.putExtra("pickerDate", fDate);


            if(chkFlgForErrorToCloseApp==0)
            {
                chkFlgForErrorToCloseApp=0;
                startActivity(storeIntent);
               // finish();
            }
            else
            {
                AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
                alertDialogNoConn.setTitle(getText(R.string.genTermInformation));
                alertDialogNoConn.setMessage(getText(R.string.txtInvoicePending));
                alertDialogNoConn.setCancelable(false);
                alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                               // but_Invoice.setEnabled(true);
                                chkFlgForErrorToCloseApp=0;
                            }
                        });
                alertDialogNoConn.setIcon(R.drawable.info_ico);
                AlertDialog alert = alertDialogNoConn.create();
                alert.show();
            }
        }
    }







    public void shardPrefForCoverageArea(int coverageAreaNodeID,int coverageAreaNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();

    }


    private void shardPrefForSalesman(int salesmanNodeId, int salesmanNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("SalesmanNodeId", salesmanNodeId);
        editor.putInt("SalesmanNodeType", salesmanNodeType);

        editor.commit();

    }

    private void flgDataScopeSharedPref(int _flgDataScope)
    {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDataScope", _flgDataScope);
        editor.commit();


    }
    public void flgDSRSOSharedPref(int _flgDSRSO)
    {
        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("flgDSRSO", _flgDSRSO);
        editor.commit();


    }



    public void firstTimeLocationTrack()
    {
        if(pDialog2STANDBY!=null)
        {
            if(pDialog2STANDBY.isShowing())
            {


            }
            else
            {
                boolean isGPSok = false;
                boolean isNWok=false;
                isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!isGPSok && !isNWok)
                {
                    try
                    {
                        showSettingsAlert();
                    }
                    catch(Exception e)
                    {

                    }
                    isGPSok = false;
                    isNWok=false;
                }
                else
                {
                    locationRetrievingAndDistanceCalculating();
                }
            }
        }
        else
        {
            boolean isGPSok = false;
            boolean isNWok=false;
            isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNWok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSok && !isNWok)
            {
                try
                {
                    showSettingsAlert();
                }
                catch(Exception e)
                {

                }
                isGPSok = false;
                isNWok=false;
            }
            else
            {
                locationRetrievingAndDistanceCalculating();
            }

        }
    }


    private void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialogGps = new android.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialogGps.setTitle(getResources().getString(R.string.genTermInformation));
        alertDialogGps.setIcon(R.drawable.error_info_ico);
        alertDialogGps.setCancelable(false);
        // Setting Dialog Message
        alertDialogGps.setMessage(getResources().getString(R.string.genTermGPSDisablePleaseEnable));

        // On pressing Settings button
        alertDialogGps.setPositiveButton(getResources().getString(R.string.AlertDialogOkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Showing Alert Message
        GPSONOFFAlert=alertDialogGps.create();
        GPSONOFFAlert.show();
    }

    private void locationRetrievingAndDistanceCalculating()
    {
        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(AllButtonActivity.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.rtrvng_loc), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(AllButtonActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(AllButtonActivity.this)
                    .addOnConnectionFailedListener(AllButtonActivity.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));


        startService(new Intent(AllButtonActivity.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        long interval = 200;
        long startTime = 15000;
        countDownTimer = new AllButtonActivity.CoundownClass(startTime, interval);
        countDownTimer.start();

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public class CoundownClass extends CountDownTimer {

        CoundownClass(long startTime, long interval) {
            super(startTime, interval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish()
        {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            String GpsLat="0";
            String GpsLong="0";
            String GpsAccuracy="0";
            String GpsAddress="0";
            if(isGPSEnabled)
            {

                Location nwLocation=appLocationService.getLocation(locationManager,LocationManager.GPS_PROVIDER,location);

                if(nwLocation!=null){
                    double lattitude=nwLocation.getLatitude();
                    double longitude=nwLocation.getLongitude();
                    double accuracy= nwLocation.getAccuracy();
                    GpsLat=""+lattitude;
                    GpsLong=""+longitude;
                    GpsAccuracy=""+accuracy;
                    if(isOnline())
                    {
                        GpsAddress=getAddressOfProviders(GpsLat, GpsLong);
                    }
                    else
                    {
                        GpsAddress="NA";
                    }
                    GPSLocationLatitude=""+lattitude;
                    GPSLocationLongitude=""+longitude;
                    GPSLocationProvider="GPS";
                    GPSLocationAccuracy=""+accuracy;
                    AllProvidersLocation="GPS=Lat:"+lattitude+"Long:"+longitude+"Acc:"+accuracy;

                }
            }

            Location gpsLocation=appLocationService.getLocation(locationManager,LocationManager.NETWORK_PROVIDER,location);
            String NetwLat="0";
            String NetwLong="0";
            String NetwAccuracy="0";
            String NetwAddress="0";
            if(gpsLocation!=null){
                double lattitude1=gpsLocation.getLatitude();
                double longitude1=gpsLocation.getLongitude();
                double accuracy1= gpsLocation.getAccuracy();

                NetwLat=""+lattitude1;
                NetwLong=""+longitude1;
                NetwAccuracy=""+accuracy1;
                if(isOnline())
                {
                    NetwAddress=getAddressOfProviders(NetwLat, NetwLong);
                }
                else
                {
                    NetwAddress="NA";
                }


                NetworkLocationLatitude=""+lattitude1;
                NetworkLocationLongitude=""+longitude1;
                NetworkLocationProvider="Network";
                NetworkLocationAccuracy=""+accuracy1;
                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }
                else
                {
                    AllProvidersLocation="Network=Lat:"+lattitude1+"Long:"+longitude1+"Acc:"+accuracy1;
                }
                System.out.println("LOCATION(N/W)  LATTITUDE: " +lattitude1 + "LONGITUDE:" + longitude1+ "accuracy:" + accuracy1);

            }
									 /* TextView accurcy=(TextView) findViewById(R.id.Acuracy);
									  accurcy.setText("GPS:"+GPSLocationAccuracy+"\n"+"NETWORK"+NetworkLocationAccuracy+"\n"+"FUSED"+fusedData);*/

            System.out.println("LOCATION Fused"+fusedData);

            String FusedLat="0";
            String FusedLong="0";
            String FusedAccuracy="0";
            String FusedAddress="0";

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);

                FusedLat=FusedLocationLatitude;
                FusedLong=FusedLocationLongitude;
                FusedAccuracy=FusedLocationAccuracy;
                FusedLocationLatitudeWithFirstAttempt=FusedLocationLatitude;
                FusedLocationLongitudeWithFirstAttempt=FusedLocationLongitude;
                FusedLocationAccuracyWithFirstAttempt=FusedLocationAccuracy;
                if(isOnline())
                {
                    FusedAddress=getAddressOfProviders(FusedLat, FusedLong);
                }
                else
                {
                    FusedAddress="NA";
                }

                if(!AllProvidersLocation.equals(""))
                {
                    AllProvidersLocation=AllProvidersLocation+"$Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
                else
                {
                    AllProvidersLocation="Fused=Lat:"+FusedLocationLatitude+"Long:"+FusedLocationLongitude+"Acc:"+fnAccuracy;
                }
            }


            appLocationService.KillServiceLoc(appLocationService, locationManager);

            try {
                if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
                {
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            }
            catch (Exception e){

            }
            //




            fnAccurateProvider="";
            fnLati="0";
            fnLongi="0";
            fnAccuracy=0.0;

            if(!FusedLocationProvider.equals(""))
            {
                fnAccurateProvider="Fused";
                fnLati=FusedLocationLatitude;
                fnLongi=FusedLocationLongitude;
                fnAccuracy= Double.parseDouble(FusedLocationAccuracy);
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!GPSLocationProvider.equals(""))
                {
                    if(Double.parseDouble(GPSLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Gps";
                        fnLati=GPSLocationLatitude;
                        fnLongi=GPSLocationLongitude;
                        fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!GPSLocationProvider.equals(""))
                {
                    fnAccurateProvider="Gps";
                    fnLati=GPSLocationLatitude;
                    fnLongi=GPSLocationLongitude;
                    fnAccuracy= Double.parseDouble(GPSLocationAccuracy);
                }
            }

            if(!fnAccurateProvider.equals(""))
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    if(Double.parseDouble(NetworkLocationAccuracy)<fnAccuracy)
                    {
                        fnAccurateProvider="Network";
                        fnLati=NetworkLocationLatitude;
                        fnLongi=NetworkLocationLongitude;
                        fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                    }
                }
            }
            else
            {
                if(!NetworkLocationProvider.equals(""))
                {
                    fnAccurateProvider="Network";
                    fnLati=NetworkLocationLatitude;
                    fnLongi=NetworkLocationLongitude;
                    fnAccuracy= Double.parseDouble(NetworkLocationAccuracy);
                }
            }
            // fnAccurateProvider="";
            if(fnAccurateProvider.equals(""))
            {
                //because no location found so updating table with NA
                dbengine.open();
                dbengine.deleteLocationTable();
                dbengine.saveTblLocationDetails("NA", "NA", "NA","NA","NA","NA","NA","NA", "NA", "NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA","NA");
                dbengine.close();
                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }



                int flagtoShowStorelistOrAddnewStore=dbengine.fncheckCountNearByStoreExistsOrNot(CommonInfo.DistanceRange);


                if(flagtoShowStorelistOrAddnewStore==1)
                {
                    //getDataFromDatabaseToHashmap();
                    //tl2.removeAllViews();

                    if(tl2.getChildCount()>0){
                        tl2.removeAllViews();
                        // dynamcDtaContnrScrollview.removeAllViews();
                        //addViewIntoTable();
                        //setStoresList();
                    }
                    else
                    {
                        //addViewIntoTable();
                       // setStoresList();
                    }
                    if(pDialog2STANDBY!=null)
                    {
                        if (pDialog2STANDBY.isShowing())
                        {
                            pDialog2STANDBY.dismiss();
                        }
                    }

                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

                }
                else
                {
                    if(pDialog2STANDBY!=null) {
                        if (pDialog2STANDBY.isShowing()) {
                            pDialog2STANDBY.dismiss();
                        }
                    }
                }
                //send direct to dynamic page-------------------------
               /* Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWise.class);
                intent.putExtra("FLAG_NEW_UPDATE","NEW");
                StorelistActivity.this.startActivity(intent);
                finish();*/


                //commenting below error message
                // showAlertForEveryOne("Please try again, No Fused,GPS or Network found.");
            }
            else
            {
                String FullAddress="0";
                if(isOnline())
                {
                    FullAddress=   getAddressForDynamic(fnLati, fnLongi);
                }
                else
                {
                    FullAddress="NA";
                }

                if(!GpsLat.equals("0") )
                {
                    fnCreateLastKnownGPSLoction(GpsLat,GpsLong,GpsAccuracy);
                }
                //now Passing intent to other activity
                String addr="NA";
                String zipcode="NA";
                String city="NA";
                String state="NA";


                if(!FullAddress.equals("NA"))
                {
                    addr = FullAddress.split(Pattern.quote("^"))[0];
                    zipcode = FullAddress.split(Pattern.quote("^"))[1];
                    city = FullAddress.split(Pattern.quote("^"))[2];
                    state = FullAddress.split(Pattern.quote("^"))[3];
                }

                if(fnAccuracy>10000)
                {
                    dbengine.open();
                    dbengine.deleteLocationTable();
                    dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
                    dbengine.close();
                    if(pDialog2STANDBY.isShowing())
                    {
                        pDialog2STANDBY.dismiss();
                    }

                    //send to addstore Dynamic page direct-----------------------------
                   /* Intent intent=new Intent(LauncherActivity.this,AddNewStore_DynamicSectionWise.class);
                    intent.putExtra("FLAG_NEW_UPDATE","NEW");
                    LauncherActivity.this.startActivity(intent);
                    finish();*/


                    //From, addr,zipcode,city,state,errorMessageFlag,username,totaltarget,todayTarget


                }
                else
                {
                    dbengine.open();
                    dbengine.deleteLocationTable();
                    dbengine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state,fnAccurateProvider,GpsLat,GpsLong,GpsAccuracy,NetwLat,NetwLong,NetwAccuracy,FusedLat,FusedLong,FusedAccuracy,AllProvidersLocation,GpsAddress,NetwAddress,FusedAddress,FusedLocationLatitudeWithFirstAttempt,FusedLocationLongitudeWithFirstAttempt,FusedLocationAccuracyWithFirstAttempt);
                    dbengine.close();


                    hmapOutletListForNear=dbengine.fnGetALLOutletMstr();
                    System.out.println("SHIVAM"+hmapOutletListForNear);
                    if(hmapOutletListForNear!=null)
                    {

                        for(Map.Entry<String, String> entry:hmapOutletListForNear.entrySet())
                        {
                            int DistanceBWPoint=1000;
                            String outID= entry.getKey().trim();
                            //  String PrevAccuracy = entry.getValue().split(Pattern.quote("^"))[0];
                            String PrevLatitude = entry.getValue().split(Pattern.quote("^"))[0];
                            String PrevLongitude = entry.getValue().split(Pattern.quote("^"))[1];

                            // if (!PrevAccuracy.equals("0"))
                            // {
                            if (!PrevLatitude.equals("0"))
                            {
                                try
                                {
                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(Double.parseDouble(fnLati));
                                    locationA.setLongitude(Double.parseDouble(fnLongi));

                                    Location locationB = new Location("point B");
                                    locationB.setLatitude(Double.parseDouble(PrevLatitude));
                                    locationB.setLongitude(Double.parseDouble(PrevLongitude));

                                    float distance = locationA.distanceTo(locationB) ;
                                    DistanceBWPoint=(int)distance;

                                    hmapOutletListForNearUpdated.put(outID, ""+DistanceBWPoint);
                                }
                                catch(Exception e)
                                {

                                }
                            }
                            // }
                        }
                    }

                    if(hmapOutletListForNearUpdated!=null)
                    {
                        dbengine.open();
                        for(Map.Entry<String, String> entry:hmapOutletListForNearUpdated.entrySet())
                        {
                            String outID= entry.getKey().trim();
                            String DistanceNear = entry.getValue().trim();
                            if(outID.equals("853399-a1445e87daf4-NA"))
                            {
                                System.out.println("Shvam Distance = "+DistanceNear);
                            }
                            if(!DistanceNear.equals(""))
                            {
                                //853399-81752acdc662-NA
                                if(outID.equals("853399-a1445e87daf4-NA"))
                                {
                                    System.out.println("Shvam Distance = "+DistanceNear);
                                }
                                dbengine.UpdateStoreDistanceNear(outID,Integer.parseInt(DistanceNear));
                            }
                        }
                        dbengine.close();
                    }
                    //send to storeListpage page
                    //From, addr,zipcode,city,state,errorMessageFlag,username,totaltarget,todayTarget
                    int flagtoShowStorelistOrAddnewStore=      dbengine.fncheckCountNearByStoreExistsOrNot(CommonInfo.DistanceRange);


                    if(flagtoShowStorelistOrAddnewStore==1)
                    {
                        //getDataFromDatabaseToHashmap();
                        if(tl2.getChildCount()>0){
                            tl2.removeAllViews();
                            // dynamcDtaContnrScrollview.removeAllViews();
                            //addViewIntoTable();
                            //setStoresList();
                        }
                        else
                        {
                            //addViewIntoTable();
                           // setStoresList();
                        }

                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

                    }
                    else
                    {
                        //send to AddnewStore directly
                       /* Intent intent=new Intent(LauncherActivity.this,AddNewStore_DynamicSectionWise.class);
                        intent.putExtra("FLAG_NEW_UPDATE","NEW");
                        LauncherActivity.this.startActivity(intent);
                        finish();*/



/*
                        if(tl2.getChildCount()>0){
                            tl2.removeAllViews();
                            // dynamcDtaContnrScrollview.removeAllViews();
                            //addViewIntoTable();
                            setStoresList();
                        }

                        else
                        {
                            //addViewIntoTable();
                            setStoresList();
                        }
*/

                    }
                    if(pDialog2STANDBY.isShowing())
                    {
                        pDialog2STANDBY.dismiss();
                    }

                }
               /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
               *//* intent.putExtra("FROM","SPLASH");
                intent.putExtra("errorMessageFlag",errorMessageFlag); // 0 if no error, if error, then error message passes
                intent.putExtra("username",username);//if error then it will 0
                intent.putExtra("totaltarget",totaltarget);////if error then it will 0
                intent.putExtra("todayTarget",todayTarget);//if error then it will 0*//*
                LauncherActivity.this.startActivity(intent);
                finish();
*/
                GpsLat="0";
                GpsLong="0";
                GpsAccuracy="0";
                GpsAddress="0";
                NetwLat="0";
                NetwLong="0";
                NetwAccuracy="0";
                NetwAddress="0";
                FusedLat="0";
                FusedLong="0";
                FusedAccuracy="0";
                FusedAddress="0";

                //code here
            }


            //AddStoreBtn.setEnabled(true);


           /* Intent intent = new Intent(AllButtonActivity.this, AddNewStore_DynamicSectionWiseSO.class);
            //Intent intent = new Intent(StoreSelection.this, Add_New_Store_NewFormat.class);
            //Intent intent = new Intent(StoreSelection.this, Add_New_Store.class);
            intent.putExtra("storeID", "0");
            intent.putExtra("activityFrom", "AllButtonActivity");
            intent.putExtra("userDate",currSysDate);
            intent.putExtra("pickerDate", fDate);
            intent.putExtra("imei", imei);
            intent.putExtra("rID", rID);
            AllButtonActivity.this.startActivity(intent);
            finish();*/

            Intent intent =new Intent(AllButtonActivity.this,StorelistActivity.class);
            AllButtonActivity.this.startActivity(intent);
            finish();
        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub

        }}
  /*  public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());



        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

            if (addresses == null || addresses.size()  == 0)
            {
                FULLADDRESS2=  FULLADDRESS2.append("NA");
            }
            else
            {
                for(Address address : addresses) {
                    //  String outputAddress = "";
                    for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        if(i==1)
                        {
                            FULLADDRESS2.append(address.getAddressLine(i));
                        }
                        else if(i==2)
                        {
                            FULLADDRESS2.append(",").append(address.getAddressLine(i));
                        }
                    }
                }
		      *//* //String address = addresses.get(0).getAddressLine(0);
		       String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		       String city = addresses.get(0).getLocality();
		       String state = addresses.get(0).getAdminArea();
		       String country = addresses.get(0).getCountryName();
		       String postalCode = addresses.get(0).getPostalCode();
		       String knownName = addresses.get(0).getFeatureName();
		       FULLADDRESS=address+","+city+","+state+","+country+","+postalCode;
		      Toast.makeText(contextcopy, "ADDRESS"+address+"city:"+city+"state:"+state+"country:"+country+"postalCode:"+postalCode, Toast.LENGTH_LONG).show();*//*

            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return FULLADDRESS2.toString();

    }*/
  private String getAddressOfProviders(String latti, String longi){

      StringBuilder FULLADDRESS2 =new StringBuilder();
      Geocoder geocoder;
      List<Address> addresses;
      geocoder = new Geocoder(AllButtonActivity.this, Locale.ENGLISH);



      try {
          addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);

          if (addresses == null || addresses.size()  == 0 || addresses.get(0).getAddressLine(0)==null)
          {
              FULLADDRESS2=  FULLADDRESS2.append("NA");
          }
          else
          {
              FULLADDRESS2 =FULLADDRESS2.append(addresses.get(0).getAddressLine(0));
          }

      } catch (NumberFormatException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } // Here 1 represent max location result to returned, by documents it recommended 1 to 5


      return FULLADDRESS2.toString();

  }
    private void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);




    }

    private String getAddressForDynamic(String latti, String longi){


        String areaToMerge="NA";
        Address address=null;
        String addr="NA";
        String zipcode="NA";
        String city="NA";
        String state="NA";
        String fullAddress="";
        StringBuilder FULLADDRESS3 =new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latti), Double.parseDouble(longi), 1);
            if (addresses != null && addresses.size() > 0){
                if(addresses.get(0).getAddressLine(1)!=null){
                    addr=addresses.get(0).getAddressLine(1);
                }

                if(addresses.get(0).getLocality()!=null){
                    city=addresses.get(0).getLocality();
                }

                if(addresses.get(0).getAdminArea()!=null){
                    state=addresses.get(0).getAdminArea();
                }


                for(int i=0 ;i<addresses.size();i++){
                    address = addresses.get(i);
                    if(address.getPostalCode()!=null){
                        zipcode=address.getPostalCode();
                        break;
                    }




                }

                if(addresses.get(0).getAddressLine(0)!=null && addr.equals("NA")){
                    String countryname="NA";
                    if(addresses.get(0).getCountryName()!=null){
                        countryname=addresses.get(0).getCountryName();
                    }

                    addr=  getAddressNewWay(addresses.get(0).getAddressLine(0),city,state,zipcode,countryname);
                }

            }
            else{FULLADDRESS3.append("NA");}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            return addr+"^"+zipcode+"^"+city+"^"+state;
        }
    }
    private void fnCreateLastKnownGPSLoction(String chekLastGPSLat, String chekLastGPSLong, String chekLastGpsAccuracy)
    {

        try {

            JSONArray jArray=new JSONArray();
            JSONObject jsonObjMain=new JSONObject();


            JSONObject jOnew = new JSONObject();
            jOnew.put( "chekLastGPSLat",chekLastGPSLat);
            jOnew.put( "chekLastGPSLong",chekLastGPSLong);
            jOnew.put( "chekLastGpsAccuracy", chekLastGpsAccuracy);


            jArray.put(jOnew);
            jsonObjMain.put("GPSLastLocationDetils", jArray);

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(),CommonInfo.AppLatLngJsonFile);
            if (!jsonTxtFolder.exists())
            {
                jsonTxtFolder.mkdirs();

            }
            String txtFileNamenew="GPSLastLocation.txt";
            File file = new File(jsonTxtFolder,txtFileNamenew);
            String fpath = Environment.getExternalStorageDirectory()+"/"+CommonInfo.AppLatLngJsonFile+"/"+txtFileNamenew;


            // If file does not exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


            FileWriter fw;
            try {
                fw = new FileWriter(file.getAbsoluteFile());

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(jsonObjMain.toString());

                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {

            battLevel = intent.getIntExtra("level", 0);

        }
    };

    private String getAddressNewWay(String ZeroIndexAddress, String city, String State, String pincode, String country){
        String editedAddress=ZeroIndexAddress;
        if(editedAddress.contains(city)){
            editedAddress= editedAddress.replace(city,"");

        }
        if(editedAddress.contains(State)){
            editedAddress=editedAddress.replace(State,"");

        }
        if(editedAddress.contains(pincode)){
            editedAddress= editedAddress.replace(pincode,"");

        }
        if(editedAddress.contains(country)){
            editedAddress=editedAddress.replace(country,"");

        }
        if(editedAddress.contains(",")){
            editedAddress=editedAddress.replace(","," ");

        }

        return editedAddress;
    }

   /* public void showAlertBox(String msg)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(AllButtonActivity.this);
        alertDialogNoConn.setTitle(getResources().getString(R.string.genTermInformation));
        alertDialogNoConn.setMessage(msg);

        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();


            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }*/

    private class GetStoresForDay extends AsyncTask<Void, Void, Void>
    {


        GetStoresForDay(AllButtonActivity activity)
        {

        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


            dbengine.open();
            String getPDADate=dbengine.fnGetPdaDate();
            String getServerDate=dbengine.fnGetServerDate();



            dbengine.close();

            if(!getPDADate.equals(""))  // || !getPDADate.equals("NA") || !getPDADate.equals("Null") || !getPDADate.equals("NULL")
            {
                if(!getServerDate.equals(getPDADate))
                {

                    showAlertSingleButtonInfo(getResources().getString(R.string.txtErrorPhnDate));

                    dbengine.open();
                    dbengine.maintainPDADate();
                    dbengine.reCreateDB();
                    dbengine.close();
                    return;
                }
            }






            dbengine.open();
            rID=dbengine.GetActiveRouteID();

            if(rID.equals("0"))
            {
                rID=dbengine.GetNotActiveRouteID();
            }

            long syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
            String startTS = df.format(dateobj);

            int DayEndFlg=0;
            int ChangeRouteFlg=0;

            int DatabaseVersion= DBAdapterKenya.DATABASE_VERSION;
            String AppVersionID= DBAdapterKenya.AppVersionID;
            dbengine.insertTblDayStartEndDetails(imei,startTS,rID,DayEndFlg,ChangeRouteFlg,fDate,AppVersionID);//DatabaseVersion;//getVersionNumber
            dbengine.close();


            // Base class method for Creating ProgressDialog
            showProgress(getResources().getString(R.string.RetrivingDataMsg));


        }

        @Override
        protected Void doInBackground(Void... args)
        {


            try
            {

                String RouteType="0";
                try
                {
                    dbengine.open();
                    RouteType=dbengine.FetchRouteType(rID);
                    dbengine.close();
                    dbengine.deleteAllSingleCallWebServiceTableWhole();
                }
                catch(Exception e)
                {

                }
                for(int mm = 1; mm < 40  ; mm++)
                {
                   // System.out.println("Excecuted function : "+newservice.flagExecutedServiceSuccesfully);
                    if(mm==1)
                    {

                        newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=1)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==2)
                    {

                        newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=2)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==3)
                    {

                        newservice = newservice.getCategory(getApplicationContext(), imei);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=3)
                        {
                            serviceException=true;
                            break;
                        }

                    }
                    if(mm==4)
                    {

                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew);
                        newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=4)
                        {
                            serviceException=true;
                            break;
                        }

                    }
                    if(mm==5)
                    {

                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew);
                        newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=5)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==6)
                    {

					/*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.getAllPOSMaterialStructure(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*/
                    }
                    if(mm==7)
                    {



					/*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.callGetLastVisitPOSDetails(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*/



                    }
                    if(mm==8)
                    {
                        newservice = newservice.getfnGetStoreWiseTarget(getApplicationContext(), fDate, imei, rID,RouteType);
                    }
                    if(mm==9)
                    {
                        newservice = newservice.fnGetPDACollectionMaster(getApplicationContext(), fDate, imei, rID);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=40)
                        {
                            System.out.println("GRLTyre = "+mm);
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==10)
                    {

                    }
                    if(mm==11)
                    {

                    }
                    if(mm==12)
                    {

                    }
                    if(mm==13)
                    {

                    }
                    if(mm==14)
                    {

                    }
                    if(mm==15)
                    {

                    }
                    if(mm==16)
                    {

                    }
                    if(mm==17)
                    {

                    }
                    if(mm==18)
                    {

                    }
                    if(mm==19)
                    {

                    }
                    if(mm==20)
                    {

                    }
                    if(mm==21)
                    {
                        newservice = newservice.GetPDAIsSchemeApplicable(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=21)
                        {
                            serviceException=true;
                            break;
                        }

                    }

                    if(mm==22)
                    {
						/*newservice = newservice.getallPDAtblSyncSummuryDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=22)
						{
							serviceException=true;
							break;
						}
						*/
                    }
                    if(mm==23)
                    {
                        //newservice = newservice.getallPDAtblSyncSummuryForProductDetails(getApplicationContext(), fDate, imei, rID);
                    }
                    if(mm==24)
                    {
					/*newservice = newservice.GetSchemeCoupon(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=24)
					{
						serviceException="GetSchemeCoupon";
						break;
					}*/
                    }
                    if(mm==25)
                    {
				/*	newservice = newservice.GetSchemeCouponSlab(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=25)
					{
						serviceException="GetSchemeCouponSlab";
						break;
					}*/
                    }
                    if(mm==26)
                    {
				/*	newservice = newservice.GetDaySummaryNew(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=26)
					{
						serviceException="GetDaySummaryNew";
						break;
					}*/
                    }
                    if(mm==27)
                    {/*
					newservice = newservice.GetOrderDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=27)
					{
						serviceException="GetOrderDetailsOnLastSalesSummary";
						break;
					}
					*/}
                    if(mm==28)
                    {
				/*	newservice = newservice.GetVisitDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=28)
					{
						serviceException="GetVisitDetailsOnLastSalesSummary";
						break;
					}*/
                    }
                    if(mm==29)
                    {
                        newservice = newservice.GetLODDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=29)
                        {
                            serviceException=true;
                            break;
                        }
                    }

                    if(mm==31)
                    {
                        newservice = newservice.GetCallspForPDAGetLastVisitDate(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=31)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==32)
                    {
                        newservice = newservice.GetCallspForPDAGetLastOrderDate(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=32)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==33)
                    {
                        newservice = newservice.GetCallspForPDAGetLastVisitDetails(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=33)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==34)
                    {
                        newservice = newservice.GetCallspForPDAGetLastOrderDetails(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=34)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==35)
                    {
                        newservice = newservice.GetCallspForPDAGetLastOrderDetails_TotalValues(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=35)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                    if(mm==36)
                    {
                        newservice = newservice.GetCallspForPDAGetExecutionSummary(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=36)
                        {
                            serviceException=true;
                            break;
                        }
                    }

                    if(mm==37)
                    {
                        newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, rID);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=37)
                        {
                            serviceException=true;
                            break;
                        }
                    }

                    if(mm==38)
                    {
                        newservice=newservice.fnGetDistStockData(getApplicationContext(),imei);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=38)
                        {
                            serviceException=true;
                            break;
                        }

                    }

                    if(mm==39)
                    {

                        newservice = newservice.getStoreWiseOutStandings(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(ServiceWorker.flagExecutedServiceSuccesfully !=39)
                        {
                            serviceException=true;
                            break;
                        }
                    }
                }


            }
            catch (Exception e)
            {
                Log.i("SvcMgr", "Service Execution Failed!", e);
            }
            finally
            {
                Log.i("SvcMgr", "Service Execution Completed...");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);


            dismissProgress();   // Base class method for dismissing ProgressDialog

            if(serviceException)
            {
                dbengine.deleteStoreList();
                serviceException=false;
                try
                {
                    showAlertException(getResources().getString(R.string.txtError),getResources().getString(R.string.txtErrorRetrievingData));
                }

                catch(Exception e)
                {

                }
              /*  dbengine.open();

                dbengine.maintainPDADate();
                dbengine.dropRoutesTBL();
                dbengine.reCreateDB();

                dbengine.close();*/
            }
            else
            {
                Intent storeIntent = new Intent(AllButtonActivity.this, StoreSelection.class);
                storeIntent.putExtra("imei", imei);
                storeIntent.putExtra("userDate", currSysDate);
                storeIntent.putExtra("pickerDate", fDate);
                storeIntent.putExtra("rID", rID);
                startActivity(storeIntent);
                finish();

            }

        }
    }

    private void showAlertException(String title, String msg)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllButtonActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getResources().getString(R.string.txtRetry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which)
            {
                 new GetStoresForDay(AllButtonActivity.this).execute();
                 dialog.dismiss();
            }
        });

       alertDialog.setNegativeButton(getResources().getString(R.string.txtCancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private class GetStoreAllData extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            showProgress(getResources().getString(R.string.RetrivingDataMsg));
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
                int DatabaseVersion = DBAdapterKenya.DATABASE_VERSION;
                int ApplicationID = DBAdapterKenya.Application_TypeID;
                //newservice = newservice.getAvailableAndUpdatedVersionOfApp(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);

                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
                for(int mm = 1; mm<6; mm++)
                {
                    if(mm==2)
                    {
                        newservice = newservice.getStoreAllDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }


                    }
                    if(mm==3)
                    {
                        newservice = newservice.callfnSingleCallAllWebService(getApplicationContext(),ApplicationID,imei);
                        if (!newservice.director.trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if(mm==4)
                    {
                        newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, "0");
                        if (!newservice.director.trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if(mm==1)
                    {
                        newservice = newservice.getSOSummary(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }
                    }

                    if(mm==5)
                    {


                        newservice = newservice.fnGetStateCityListMstr(AllButtonActivity.this,imei, fDate,CommonInfo.Application_TypeID);
                        if(!newservice.director.trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                                break;
                            }

                        }
                    }
                }






            } catch (Exception e)
            {
                dismissProgress();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dismissProgress();

            if (chkFlgForErrorToCloseApp == 1)   // if Webservice showing exception or not excute complete properly
            {
                chkFlgForErrorToCloseApp = 0;
                SharedPreferences sharedPreferences=getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor ed;
                if(sharedPreferences.contains("ServerDate"))
                {
                    ed = sharedPreferences.edit();
                    ed.putString("ServerDate", "0");
                    ed.commit();
                }
                //clear sharedpreferences
               // intentPassToLauncherActivity("Internet connection is slow ,please try again.");//, "0", "0", "0"
                showAlertSingleButtonInfo(getResources().getString(R.string.internetError));
            }
            else
                {

                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(0);
                    Intent intent =new Intent(AllButtonActivity.this,StorelistActivity.class);
                    AllButtonActivity.this.startActivity(intent);
                    finish();


            }
        }

    }

}
