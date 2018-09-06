package project.astix.com.sancusnetworkssosfaindirect;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class StorelistActivity extends BaseActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

   public  ProgressDialog pDialogGetStores;
    public String passDate;
    ImageSync task;
    FullSyncDataNow task2;
    public String rID="0";
    public Timer timerForDataSubmission;
    public	MyTimerTaskForDataSubmission myTimerTaskForDataSubmission;
    public int flgUploadOrRefreshButtonClicked=0;

    LinkedHashMap<String, String> hmapCoverageRouteMap_details=new LinkedHashMap<String, String>();
    public int flgAddButtonCliked=0;
    LinkedHashMap<View, String> hmapStoreViewAndName=new LinkedHashMap<View,String>();
    String[] CoverageAreaNames;
    String[] RouteNames;
    LinkedHashMap<String, String> hmapCoverage_details=new LinkedHashMap<String, String>();
    LinkedHashMap<String, String> hmapRoute_details=new LinkedHashMap<String, String>();

    public EditText ed_search;
    ListView listCoverage;
    ArrayAdapter<String> adapterCoverageList;
    public int RouteIDSelectedInSpinner=0;
    public int CoverageIDSelectedInSpinner=0;
    ListView listRoute;
    ArrayAdapter<String> adapterRouteList;
    AlertDialog alertRoute;
    TextView selectRouteSpinner;

    AlertDialog alertCoverage;
    AlertDialog.Builder alertDialog;
    View convertView;

    TextView selectCoverageSpinner;
    RadioGroup rg_mode_of_visit;
    RadioButton rb_offline,rb_online;
    int modeOfVisit=0;

    ImageView logoutIcon,menu_icon;
    InputStream inputStream;
    LinearLayout parentOfAllDynamicData;
    LinkedHashMap<String, String> hmapStoresFromDataBase;
    DBAdapterKenya dbEngine=new DBAdapterKenya(this);
    Button EditBtn , AddStoreBtn,RefreshBtn;
    String tagOfselectedStore="0"+"^"+"0";
    public LocationManager locationManager;

    DatabaseAssistant DA = new DatabaseAssistant(StorelistActivity.this);
    int serverResponseCode = 0;
    public int syncFLAG = 0;
    public String[] xmlForWeb = new String[1];

    public int chkFlgForErrorToCloseApp=0;
    public   PowerManager pm;
    public	 PowerManager.WakeLock wl;

    public SimpleDateFormat sdf;
    public ProgressDialog pDialog2STANDBY;
    public Location location;
    public String FusedLocationLatitude="0";
    public String FusedLocationLongitude="0";
    public String FusedLocationProvider="";
    public String FusedLocationAccuracy="0";

    public String GPSLocationLatitude="0";
    public String GPSLocationLongitude="0";
    public String GPSLocationProvider="";
    public String GPSLocationAccuracy="0";

    public String NetworkLocationLatitude="0";
    public String NetworkLocationLongitude="0";
    public String NetworkLocationProvider="";
    public String NetworkLocationAccuracy="0";

    public AppLocationService appLocationService;
    public boolean isGPSEnabled = false;
    public   boolean isNetworkEnabled = false;
    public Button onlineBtn;
    public Button offlineBtn;

    public CoundownClass countDownTimer;
    private final long startTime = 15000;
    private final long interval = 200;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    LocationRequest mLocationRequest;
    String imei;
    public String fDate;
    public String fnAccurateProvider="";
    public String fnLati="0";
    public String fnLongi="0";
    public Double fnAccuracy=0.0;
    String fusedData;
    Dialog dialog;



    public int CoverageID = 0;
    public int RouteID = 0;

    ServiceWorker newservice = new ServiceWorker();
    LinkedHashMap<String, String> hmapOutletListForNear=new LinkedHashMap<String, String>();

    LinkedHashMap<String, String> hmapOutletListForNearUpdated=new LinkedHashMap<String, String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist_activity);
        getCoverageAndRouteListDetail();
        initializeAllView();
        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);
        Date date1=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date1).toString().trim();
        //code in onResume
        //below code going to Onfinsh
       /* getDataFromDatabaseToHashmap();
        addViewIntoTable();*/

        getDataFromDatabaseToHashmap();
        addViewIntoTable();



    }

    @Override
    protected void onResume() {
        super.onResume();



        if(CommonInfo.flgLTFoodsSOOnlineOffLine==0)
        {
            rb_offline.setChecked(true);

        }
        else
        {
            rb_online.setChecked(true);

        }



    }


    public void getCoverageAndRouteListDetail()
    {

        hmapCoverage_details=dbEngine.fetch_CoverageArea_List(0);//0=Calling this function from StoreListActivity,1=Calling this function from Report Activity
        hmapRoute_details=dbEngine.fetch_Route_List(0);//0=Calling this function from StoreListActivity,1=Calling this function from Report Activity
        hmapCoverageRouteMap_details=dbEngine.fetch_CoverageRouteMap_List(0,0);
        int index=0;
        if(hmapCoverage_details!=null)
        {
            CoverageAreaNames=new String[hmapCoverage_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapCoverage_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                CoverageAreaNames[index]=me2.getKey().toString();
                index=index+1;
            }
        }

        index=0;
        if(hmapRoute_details!=null)
        {
            RouteNames=new String[hmapRoute_details.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapRoute_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                RouteNames[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }
    public void  initializeAllView()
    {

        ed_search=(EditText) findViewById(R.id.ed_search);


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//false for removing back icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);//false disable button
        getSupportActionBar() .setDisplayShowTitleEnabled(false);//false for removing title*/

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0)
                {
                    if (hmapStoreViewAndName != null) {
                        if(hmapStoreViewAndName.size()>0) {
                            for (Map.Entry<View, String> entry : hmapStoreViewAndName.entrySet()) {
                                View storeRow = entry.getKey();
                                storeRow.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                else {
                    if (hmapStoreViewAndName != null) {
                        if(hmapStoreViewAndName.size()>0) {
                            for (Map.Entry<View, String> entry : hmapStoreViewAndName.entrySet()) {
                                View storeRow = entry.getKey();
                                if (entry.getValue().toString().trim().toLowerCase().contains(s.toString().trim().toLowerCase())) {
                                    storeRow.setVisibility(View.VISIBLE);
                                } else {
                                    storeRow.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adapterCoverageList = new ArrayAdapter<String>(StorelistActivity.this, R.layout.list_item_dark, R.id.fso_name, CoverageAreaNames);
        selectCoverageSpinner= (TextView) findViewById(R.id.selectCoverageSpinner);

        String StoreCategoryType=dbEngine.getChannelGroupIdOptIdForAddingStore();
        if(StoreCategoryType.equals("0-3-80"))
        {
            selectCoverageSpinner.setText("All Merchandiser/Coverage Area");
        }

        adapterRouteList = new ArrayAdapter<String>(StorelistActivity.this, R.layout.list_item_dark, R.id.fso_name, RouteNames);
        selectRouteSpinner= (TextView) findViewById(R.id.selectRouteSpinner);
        offlineBtn=(Button) findViewById(R.id.offlineBtn);

        onlineBtn=(Button) findViewById(R.id.onlineBtn);

        setBtnBackgroundOfLineOnline();
        //btn_background
        onlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flgAddButtonCliked=0;
                hmapStoresFromDataBase.clear();
                parentOfAllDynamicData.removeAllViews();
                modeOfVisit=1;

                CommonInfo.flgLTFoodsSOOnlineOffLine=1;
                setBtnBackgroundOfLineOnline();
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
            }
        });
        offlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flgAddButtonCliked=0;
                hmapStoresFromDataBase.clear();
                parentOfAllDynamicData.removeAllViews();
                modeOfVisit=0;
                CommonInfo.flgLTFoodsSOOnlineOffLine=0;
                setBtnBackgroundOfLineOnline();
                getDataFromDatabaseToHashmap();
                addViewIntoTable();
            }
        });
        selectCoverageSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  uomtext = (TextView) arg0;
                alertDialog = new AlertDialog.Builder(StorelistActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                convertView = (View) inflater.inflate(R.layout.list_activity, null);
                EditText inputSearch=	 (EditText) convertView.findViewById(R.id.inputSearch);
                inputSearch.setVisibility(View.GONE);
                listCoverage = (ListView)convertView. findViewById(R.id.list_view);

                listCoverage.setAdapter(adapterCoverageList);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Coverage Area");
                listCoverage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String coverageNameInSpinner=listCoverage.getItemAtPosition(position).toString().trim();
                        CoverageIDSelectedInSpinner=Integer.parseInt(hmapCoverage_details.get(coverageNameInSpinner));
                        selectCoverageSpinner.setText(coverageNameInSpinner);
                        RouteIDSelectedInSpinner=0;
                        //fsoIDSelectedInSpinner

                        hmapCoverageRouteMap_details.clear();
                        RouteNames = new String[0];
                        if(CoverageIDSelectedInSpinner==0)
                        {

                            hmapCoverageRouteMap_details=dbEngine.fetch_CoverageRouteMap_List(0,0);

                        }
                        else
                        {
                            hmapCoverageRouteMap_details=dbEngine.fetch_CoverageRouteMap_List(0,CoverageIDSelectedInSpinner);
                        }

                        int  index=0;
                        if(hmapRoute_details!=null)
                        {
                            RouteNames=new String[hmapCoverageRouteMap_details.size()];
                            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapCoverageRouteMap_details);
                            Set set2 = map.entrySet();
                            Iterator iterator = set2.iterator();
                            while(iterator.hasNext())
                            {
                                Map.Entry me2 = (Map.Entry)iterator.next();
                                RouteNames[index]=me2.getKey().toString();
                                index=index+1;
                            }
                        }
                        adapterRouteList = new ArrayAdapter<String>(StorelistActivity.this, R.layout.list_item_dark, R.id.fso_name, RouteNames);

                        selectRouteSpinner.setText("Select Beat");

                        alertCoverage.dismiss();
                        filterStoreList(CoverageIDSelectedInSpinner,RouteIDSelectedInSpinner);

                     /* String coverageNameInSpinner=listCoverage.getItemAtPosition(position).toString().trim();
                      CoverageIDSelectedInSpinner=Integer.parseInt(hmapCoverage_details.get(coverageNameInSpinner));
                      selectCoverageSpinner.setText(coverageNameInSpinner);

                      //fsoIDSelectedInSpinner

                      alertCoverage.dismiss();
                      filterStoreList(CoverageIDSelectedInSpinner,RouteIDSelectedInSpinner);*/
                    }
                });
                alertCoverage=alertDialog.show();
            }
        });



        selectRouteSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  uomtext = (TextView) arg0;
                alertDialog = new AlertDialog.Builder(StorelistActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                convertView = (View) inflater.inflate(R.layout.list_activity, null);
                EditText inputSearch=	 (EditText) convertView.findViewById(R.id.inputSearch);
                inputSearch.setVisibility(View.GONE);
                listRoute = (ListView)convertView. findViewById(R.id.list_view);

                listRoute.setAdapter(adapterRouteList);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Beats");
                listRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String routeNameInSpinner = listRoute.getItemAtPosition(position).toString().trim();
                        RouteIDSelectedInSpinner = Integer.parseInt(hmapRoute_details.get(routeNameInSpinner));
                        selectRouteSpinner.setText(routeNameInSpinner);

                        //fsoIDSelectedInSpinner

                        alertRoute.dismiss();
                        filterStoreList(CoverageIDSelectedInSpinner,RouteIDSelectedInSpinner);
                    }
                });
                alertRoute=alertDialog.show();
            }
        });
        menu_icon=(ImageView) findViewById(R.id.img_side_popUp);
        menu_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flgAddButtonCliked=0;
                OpenPopUpDialog();
            }
        });

        rg_mode_of_visit=(RadioGroup) findViewById(R.id.rg_mode_of_visit);
        rb_offline= (RadioButton) findViewById(R.id.rb_offline);
        rb_online= (RadioButton) findViewById(R.id.rb_online);
        rg_mode_of_visit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
             /* if(checkedId == R.id.rb_offline) {
                  modeOfVisit=0;
                  CommonInfo.flgLTFoodsSOOnlineOffLine=0;

              } else  {
                  modeOfVisit=1;
                  CommonInfo.flgLTFoodsSOOnlineOffLine=1;
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
              }*/
            }
        });
        logoutIcon= (ImageView) findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flgAddButtonCliked=0;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StorelistActivity.this);
                alertDialog.setTitle("Information");

                alertDialog.setCancelable(false);
                alertDialog.setMessage("Do you really want to close app ");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

//--------------------------------------------------------------------------------------------------------------
                        finishAffinity();

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });
        // Screen handling while hiding Actionbar title.
        // getSupportActionBar().setTitle("Store List ");
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        // imei="123456789";
        // imei="352117068344254"; // rspl so test imei
        if (CommonInfo.imei.equals("")) {
            CommonInfo.imei = imei;
        }
        else
        {
            imei=CommonInfo.imei;
        }
        //imei="359648069495987";
        // CommonInfo.imei=imei;
        parentOfAllDynamicData=(LinearLayout) findViewById(R.id.parentOfAllDynamicData);
        EditBtn= (Button) findViewById(R.id.EditBtn);
        //EditBtn is now not working for this project
        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flgAddButtonCliked=0;
                boolean selectFlag=   checkStoreSelectededOrNot();
                if(selectFlag){

                    Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWiseSO.class);

                    String storeID = tagOfselectedStore.split(Pattern.quote("^"))[0];
                    String   storeName = tagOfselectedStore.split(Pattern.quote("^"))[1];

                    int CoverageAreaID=Integer.parseInt(tagOfselectedStore.split(Pattern.quote("^"))[2]);
                    int RouteNodeID=Integer.parseInt(tagOfselectedStore.split(Pattern.quote("^"))[3]);
                    String StoreCategoryType=tagOfselectedStore.split(Pattern.quote("^"))[4];
                    int StoreSectionCount=Integer.parseInt(tagOfselectedStore.split(Pattern.quote("^"))[5]);

                    intent.putExtra("FLAG_NEW_UPDATE","UPDATE");


                    intent.putExtra("StoreID",storeID);
                    intent.putExtra("StoreName", storeName);
                    intent.putExtra("CoverageAreaID", CoverageAreaID);
                    intent.putExtra("RouteNodeID", RouteNodeID);
                    intent.putExtra("StoreCategoryType", StoreCategoryType);
                    intent.putExtra("StoreSectionCount", StoreSectionCount);
                    StorelistActivity.this.startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please select store ",Toast.LENGTH_LONG).show();

                }
            }
        });
        RefreshBtn=(Button) findViewById(R.id.RefreshBtn);
        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flgAddButtonCliked=0;
                boolean isGPSok = false;
                boolean isNWok=false;
                RefreshBtn.setEnabled(false);
                AddStoreBtn.setEnabled(false);
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
        });
        AddStoreBtn= (Button) findViewById(R.id.AddStoreBtn);
        AddStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RefreshBtn.setEnabled(false);
                AddStoreBtn.setEnabled(false);
                flgAddButtonCliked=1;
                CommonInfo.flgLTFoodsSOOnlineOffLine=1;

                CommonInfo.flgNewStoreORStoreValidation=1;

                setBtnBackgroundOfLineOnline();
                {
                    int chkFlgValue=dbEngine.fnCheckTableFlagValue("tblAllServicesCalledSuccessfull","flgAllServicesCalledOrNot");
                    if(chkFlgValue==1)
                    {
                        fnshowalertHalfDataRefreshed();
                    }
                    else
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
                                    locationRetrievingWhilwAddingNewOutlet();
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
                                locationRetrievingWhilwAddingNewOutlet();
                            }

                        }
                    }
                }








            /*  String StoreCategoryType=dbEngine.getChannelGroupIdOptIdForAddingStore();
              int StoreSectionCount=dbEngine.getsectionCountWhileAddingStore();
              Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWise.class);
              intent.putExtra("FLAG_NEW_UPDATE","NEW");


              intent.putExtra("CoverageAreaID", 0);
              intent.putExtra("RouteNodeID", 0);
              intent.putExtra("StoreCategoryType", StoreCategoryType);
              intent.putExtra("StoreSectionCount", ""+StoreSectionCount);


              StorelistActivity.this.startActivity(intent);
              finish();*/

            }
        });



    }
    public boolean checkStoreSelectededOrNot()
    {
        boolean selectedFlag=false;
        tagOfselectedStore="0"+"^"+"0";
        if(parentOfAllDynamicData!=null && parentOfAllDynamicData.getChildCount()>0){
            for(int i=0;parentOfAllDynamicData.getChildCount()>i;i++)
            {
                LinearLayout parentOfRadiobtn= (LinearLayout) parentOfAllDynamicData.getChildAt(i);
                RadioButton childRadiobtn= (RadioButton) parentOfRadiobtn.getChildAt(0);
                if(childRadiobtn.isChecked()){
                    selectedFlag=true;
                    tagOfselectedStore= childRadiobtn.getTag().toString().trim();
                    break;
                }


            }

        }
        else
        {
            selectedFlag=false;
        }
        return selectedFlag;
    }

    public void  getDataFromDatabaseToHashmap()
    {
        hmapStoresFromDataBase =dbEngine.fnGeStoreListAllForSO(CoverageID,RouteID);

      /*if(CommonInfo.flgLTFoodsSOOnlineOffLine==0)
      {
          hmapStoresFromDataBase =dbEngine.fnGeStoreListAllForSO(CoverageID,RouteID);// dbEngine.fnGeStoreList(CommonInfo.DistanceRange);
      }
      else
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
      }*/

     /* hmapStoresFromDataBase.put("1","Store Croma Gurgaon");
      hmapStoresFromDataBase.put("2", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("3", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("4", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("5", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("6", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("7","Store Croma Gurgaon");
      hmapStoresFromDataBase.put("8", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("9", "Store Croma Gurgaon");
      hmapStoresFromDataBase.put("10","Store Croma Gurgaon");*/
    }
    public void fnshowalertHalfDataRefreshed()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StorelistActivity.this);
        alertDialog.setTitle("Information");

        alertDialog.setCancelable(false);
        alertDialog.setMessage("Data is not completely Refreshed last time,Click Yes to Refresh it Completely first.... ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


                dialog.dismiss();
                GetStoreAllData getStoreAllDataAsync= new GetStoreAllData(StorelistActivity.this);
                getStoreAllDataAsync.execute();


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void addViewIntoTable()
    {
        hmapStoreViewAndName.clear();
        for(Map.Entry<String, String> entry:hmapStoresFromDataBase.entrySet())
        {
            String storeID=entry.getKey().toString().trim();
            //StoreID,StoreName,DateAdded,CoverageAreaID,RouteNodeID,StoreCategoryType,StoreSectionCount,flgApproveOrRejectOrNoActionOrReVisit
            ////StoreName,DateAdded,CoverageAreaID,RouteNodeID,StoreCategoryType,StoreSectionCount,flgApproveOrRejectOrNoActionOrReVisit
            String StoreDetails=entry.getValue().toString().trim();
            String StoreName = StoreDetails.split(Pattern.quote("^"))[0];
            String DateAdded = StoreDetails.split(Pattern.quote("^"))[1];
            int CoverageAreaID=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[2]);
            int RouteNodeID=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[3]);
            String StoreCategoryType=StoreDetails.split(Pattern.quote("^"))[4];
            int StoreSectionCount=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[5]);
            int flgApproveOrRejectOrNoActionOrReVisit=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[6]);
            int sStat=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[7]);
            int flgOldNewStore=Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[8]);
            /*String LatCode = StoreDetails.split(Pattern.quote("^"))[1];
            String LongCode = StoreDetails.split(Pattern.quote("^"))[2];
            */

            View dynamic_container=getLayoutInflater().inflate(R.layout.store_single_row,null);
            TextView storeTextview= (TextView) dynamic_container.findViewById(R.id.storeTextview);


            storeTextview.setTag(storeID+"^"+StoreName+"^"+CoverageAreaID+"^"+RouteNodeID+"^"+StoreCategoryType+"^"+StoreSectionCount);
            storeTextview.setText(StoreName);
            dynamic_container.setTag(storeID+"^"+CoverageAreaID+"^"+RouteNodeID+"^"+StoreCategoryType);
            dynamic_container.setVisibility(View.VISIBLE);

            hmapStoreViewAndName.put(dynamic_container,StoreName);

            if(flgApproveOrRejectOrNoActionOrReVisit==1)
            {
                storeTextview.setTextColor(getResources().getColor(R.color.green));
            }
            if(flgApproveOrRejectOrNoActionOrReVisit==2)
            {
                storeTextview.setTextColor(getResources().getColor(R.color.red));
            }
            if(flgApproveOrRejectOrNoActionOrReVisit==3)
            {
                storeTextview.setTextColor(getResources().getColor(R.color.blue));
            }
            if(sStat==1)
            {
                if(flgOldNewStore==1)
                {
                    storeTextview.setText(StoreName +"  :(Newly Added)");
                }

                storeTextview.setTextColor(getResources().getColor(R.color.mdtp_accent_color_dark));
            }



            storeTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view)
                {
                    final TextView tvStores= (TextView) view;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(StorelistActivity.this);
                    alertDialog.setTitle(getText(R.string.genTermInformation));

                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(getText(R.string.editStoreAlert));
                    alertDialog.setPositiveButton(getText(R.string.AlertDialogYesButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CommonInfo.flgNewStoreORStoreValidation=2;

                            int chkFlgValue=dbEngine.fnCheckTableFlagValue("tblAllServicesCalledSuccessfull","flgAllServicesCalledOrNot");
                            if(chkFlgValue==1)
                            {
                                dialog.dismiss();
                                fnshowalertHalfDataRefreshed();
                            }
                            else
                            {
                                String tagFromStore=   tvStores.getTag().toString().trim();
                                Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWiseSO.class);

                                //  storeTextview.setTag(storeID+"^"+StoreName+"^"+CoverageAreaID+"^"+RouteNodeID+"^"+StoreCategoryType+"^"+StoreSectionCount);
                                String storeID = tagFromStore.split(Pattern.quote("^"))[0];
                                String   storeName = tagFromStore.split(Pattern.quote("^"))[1];

                                int CoverageAreaID=Integer.parseInt(tagFromStore.split(Pattern.quote("^"))[2]);
                                int RouteNodeID=Integer.parseInt(tagFromStore.split(Pattern.quote("^"))[3]);
                                String StoreCategoryType=tagFromStore.split(Pattern.quote("^"))[4];
                                int StoreSectionCount=Integer.parseInt(tagFromStore.split(Pattern.quote("^"))[5]);

                                intent.putExtra("FLAG_NEW_UPDATE","UPDATE");


                                intent.putExtra("StoreID",storeID);
                                intent.putExtra("StoreName", storeName);
                                intent.putExtra("CoverageAreaID", ""+CoverageAreaID);
                                intent.putExtra("RouteNodeID", ""+RouteNodeID);
                                intent.putExtra("StoreCategoryType", StoreCategoryType);
                                intent.putExtra("StoreSectionCount", ""+StoreSectionCount);
                                dialog.dismiss();
                                StorelistActivity.this.startActivity(intent);

                                finish();

                            }


//--------------------------------------------------------------------------------------------------------------
                            //finishAffinity();

                        }
                    });
                    alertDialog.setNegativeButton(getText(R.string.AlertDialogNoButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });

            RadioButton radioButton= (RadioButton) dynamic_container.findViewById(R.id.radiobtn);
            radioButton.setTag(storeID+"^"+StoreName);
            radioButton.setText(StoreName);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unCheckRadioButton();
                    RadioButton rb= (RadioButton) view;
                    rb.setChecked(true);
                }
            });
            parentOfAllDynamicData.addView(dynamic_container);
        }
    }
    public void unCheckRadioButton()
    {
        for(int i=0;parentOfAllDynamicData.getChildCount()>i;i++)
        {
            LinearLayout dd= (LinearLayout) parentOfAllDynamicData.getChildAt(i);
            RadioButton ff= (RadioButton) dd.getChildAt(0);
            ff.setChecked(false);
            int ss=parentOfAllDynamicData.getChildCount();

        }

    }
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Information");
        alertDialog.setIcon(R.drawable.error_info_ico);
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. \nPlease select all settings on the next page!");

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();

    }
    protected void startLocationUpdates() {
        try
        {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
        catch (SecurityException e)
        {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        locationManager.removeUpdates(appLocationService);

    }

    @Override
    public void onLocationChanged(Location args0) {
        mCurrentLocation = args0;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();

    }
    private void updateUI() {
        Location loc =mCurrentLocation;
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            FusedLocationLatitude=lat;
            FusedLocationLongitude=lng;
            FusedLocationProvider=mCurrentLocation.getProvider();
            FusedLocationAccuracy=""+mCurrentLocation.getAccuracy();
            fusedData="At Time: " + mLastUpdateTime  +
                    "Latitude: " + lat  +
                    "Longitude: " + lng  +
                    "Accuracy: " + mCurrentLocation.getAccuracy() +
                    "Provider: " + mCurrentLocation.getProvider();

        } else {

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        locationManager.removeUpdates(appLocationService);

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }
    public void locationRetrievingAndDistanceCalculating()
    {
        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(StorelistActivity.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.searchingnearbystores), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(StorelistActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(StorelistActivity.this)
                    .addOnConnectionFailedListener(StorelistActivity.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));
        startService(new Intent(StorelistActivity.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        countDownTimer = new CoundownClass(startTime, interval);
        countDownTimer.start();

    }



    public void locationRetrievingWhilwAddingNewOutlet()
    {
        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(StorelistActivity.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.searchingcurrentposition), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(StorelistActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(StorelistActivity.this)
                    .addOnConnectionFailedListener(StorelistActivity.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));
        startService(new Intent(StorelistActivity.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);

        countDownTimer = new CoundownClass(10000, interval);
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

    public class CoundownClass extends CountDownTimer {

        public CoundownClass(long startTime, long interval) {
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

                    GPSLocationLatitude=""+lattitude;
                    GPSLocationLongitude=""+longitude;
                    GPSLocationProvider="GPS";
                    GPSLocationAccuracy=""+accuracy;

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

                NetworkLocationLatitude=""+lattitude1;
                NetworkLocationLongitude=""+longitude1;
                NetworkLocationProvider="Network";
                NetworkLocationAccuracy=""+accuracy1;
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
            }


            appLocationService.KillServiceLoc(appLocationService, locationManager);
           /* stopLocationUpdates();
            mGoogleApiClient.disconnect();*/
            try {
                if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
                {
                    stopLocationUpdates();
                    mGoogleApiClient.disconnect();
                }
            }
            catch (Exception e){

            }


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
                dbEngine.open();
                dbEngine.deleteLocationTable();
               // dbEngine.saveTblLocationDetails("NA", "NA", "NA","NA","NA","NA","NA");
                dbEngine.close();
                if(pDialog2STANDBY.isShowing())
                {
                    pDialog2STANDBY.dismiss();
                }


                if(flgAddButtonCliked==0)
                {
                    int flagtoShowStorelistOrAddnewStore=dbEngine.fncheckCountNearByStoreExistsOrNotSO(CommonInfo.DistanceRange);


                    if(flagtoShowStorelistOrAddnewStore==1)
                    {
                        getDataFromDatabaseToHashmap();
                        if(parentOfAllDynamicData.getChildCount()>0){
                            parentOfAllDynamicData.removeAllViews();
                            // dynamcDtaContnrScrollview.removeAllViews();
                            addViewIntoTable();
                        }
                        else
                        {
                            addViewIntoTable();
                        }

                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

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
                    dbEngine.open();
                    dbEngine.deleteLocationTable();
                   // dbEngine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state);
                    dbEngine.close();
                    if(pDialog2STANDBY.isShowing())
                    {
                        pDialog2STANDBY.dismiss();
                    }


                }
                else
                {
                    dbEngine.open();
                    dbEngine.deleteLocationTable();
                   // dbEngine.saveTblLocationDetails(fnLati, fnLongi, String.valueOf(fnAccuracy), addr, city, zipcode, state);
                    dbEngine.close();
                    if(flgAddButtonCliked==0)
                    {
                        hmapOutletListForNear=dbEngine.fnGetALLOutletMstr();
                       // System.out.println("SHIVAM"+hmapOutletListForNear);
                        if(hmapOutletListForNear!=null)
                        {

                            for(Map.Entry<String, String> entry:hmapOutletListForNear.entrySet())
                            {
                                int DistanceBWPoint=1000;
                                String outID=entry.getKey().toString().trim();
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
                            dbEngine.open();
                            for(Map.Entry<String, String> entry:hmapOutletListForNearUpdated.entrySet())
                            {
                                String outID=entry.getKey().toString().trim();
                                String DistanceNear = entry.getValue().trim();
                           /* if(outID.equals("853399-a1445e87daf4-NA"))
                            {
                                System.out.println("Shvam Distance = "+DistanceNear);
                            }*/
                                if(!DistanceNear.equals(""))
                                {
                                    //853399-81752acdc662-NA
                               /* if(outID.equals("853399-a1445e87daf4-NA"))
                                {
                                    System.out.println("Shvam Distance = "+DistanceNear);
                                }*/
                                    dbEngine.UpdateStoreDistanceNearSO(outID,Integer.parseInt(DistanceNear));
                                }
                            }
                            dbEngine.close();
                        }
                        //send to storeListpage page
                        //From, addr,zipcode,city,state,errorMessageFlag,username,totaltarget,todayTarget
                        int flagtoShowStorelistOrAddnewStore=dbEngine.fncheckCountNearByStoreExistsOrNotSO(CommonInfo.DistanceRange);
                        if(flagtoShowStorelistOrAddnewStore==1)
                        {

                            getDataFromDatabaseToHashmap();
                            if(parentOfAllDynamicData.getChildCount()>0){
                                parentOfAllDynamicData.removeAllViews();
                                // dynamcDtaContnrScrollview.removeAllViews();
                                addViewIntoTable();
                            }
                            else
                            {
                                addViewIntoTable();
                            }
                            if(pDialog2STANDBY.isShowing())
                            {
                                pDialog2STANDBY.dismiss();
                            }
                       /* Intent intent =new Intent(LauncherActivity.this,StorelistActivity.class);
                        LauncherActivity.this.startActivity(intent);
                        finish();*/

                        }
                        else
                        {
                            if(pDialog2STANDBY.isShowing())
                            {
                                pDialog2STANDBY.dismiss();
                            }
                        }
                    }
                    else
                    {
                        //send to AddnewStore directly

                       /* Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWise.class);
                        intent.putExtra("FLAG_NEW_UPDATE","NEW");


                        StorelistActivity.this.startActivity(intent);
                        finish();*/



                        String StoreCategoryType=dbEngine.getChannelGroupIdOptIdForAddingStore();
                        int StoreSectionCount=dbEngine.getsectionCountWhileAddingStore();
                        Intent intent=new Intent(StorelistActivity.this,AddNewStore_DynamicSectionWiseSO.class);
                        intent.putExtra("FLAG_NEW_UPDATE","NEW");


                        intent.putExtra("CoverageAreaID", ""+0);
                        intent.putExtra("RouteNodeID", ""+0);
                        intent.putExtra("StoreCategoryType", StoreCategoryType);
                        intent.putExtra("StoreSectionCount", ""+StoreSectionCount);
                        if(pDialog2STANDBY.isShowing())
                        {
                            pDialog2STANDBY.dismiss();
                        }

                        StorelistActivity.this.startActivity(intent);
                        finish();
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

            RefreshBtn.setEnabled(true);
            AddStoreBtn.setEnabled(true);


        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub

        }}
    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            return true;
        }
        return false;
    }
    public String getAddressForDynamic(String latti,String longi){



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
            }
            else{FULLADDRESS3.append("NA");}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            return fullAddress=addr+"^"+zipcode+"^"+city+"^"+state;
        }
    }
    public void fnCreateLastKnownGPSLoction(String chekLastGPSLat,String chekLastGPSLong,String chekLastGpsAccuracy)
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

            File jsonTxtFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.AppLatLngJsonFile);
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
				 /*  file=contextcopy.getFilesDir();
				//fileOutputStream=contextcopy.openFileOutput("GPSLastLocation.txt", Context.MODE_PRIVATE);
				fileOutputStream.write(jsonObjMain.toString().getBytes());
				fileOutputStream.close();*/
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{

        }
    }

    void openDSRTrackerAlert()
    {
        final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(StorelistActivity.this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dsr_tracker_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        final RadioButton rb_dataReport= (RadioButton) view.findViewById(R.id.rb_dataReport);
        final RadioButton rb_onMap= (RadioButton) view.findViewById(R.id.rb_onMap);


        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

        final android.support.v7.app.AlertDialog dialog=alert.create();
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
                    Intent i=new Intent(StorelistActivity.this,WebViewDSRDataReportActivity.class);
                    startActivity(i);

                }
                else if(rb_onMap.isChecked())
                {
                    Intent i = new Intent(StorelistActivity.this, WebViewDSRTrackerActivity.class);
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

    protected void OpenPopUpDialog()
    {
        dialog = new Dialog(StorelistActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_bar);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();

        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.height=parms.MATCH_PARENT;
        parms.dimAmount = (float) 0.5;

        final Button btnManageDSR = (Button) dialog.findViewById(R.id.btnManageDSR);
        btnManageDSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(StorelistActivity.this,WebViewManageDSRActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        final   Button butn_PurchaseOrder = (Button) dialog.findViewById(R.id.butn_PurchaseOrder);
        butn_PurchaseOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isOnline())
                {
                    Intent intent=new Intent(StorelistActivity.this,WebViewPurchaseOrderActivity.class);
                    startActivity(intent);


                }
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                }
            }
        });

        final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
        butHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(StorelistActivity.this,AllButtonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final   Button butMarketVisit = (Button) dialog.findViewById(R.id.butMarketVisit);
        butMarketVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent storeIntent = new Intent(StorelistActivity.this, DialogActivity_MarketVisit.class);
                storeIntent.putExtra("PageFrom", "0");
                storeIntent.putExtra("FROM", "MARKETVISIT");
                storeIntent.putExtra(imei, imei);

                startActivity(storeIntent);
            }
        });

        final   Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
        btnExecution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    GetInvoiceForDay task = new GetInvoiceForDay(StorelistActivity.this);
                    task.execute();
                }
                catch (Exception e)
                {

                }
            }
        });


        final Button but_SalesSummray = (Button) dialog.findViewById(R.id.btnSalesSummary);
        but_SalesSummray.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


               String userDate;

                String pickerDate;

                Date currDate = new Date();
                SimpleDateFormat currDateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

                pickerDate = currDateFormat.format(currDate).toString();


                Date date1=new Date();
                sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                passDate = sdf.format(date1).toString();
                fDate = passDate.trim().toString();

                but_SalesSummray.setBackgroundColor(Color.GREEN);
                dialog.dismiss();

                SharedPreferences sharedPrefReport = getSharedPreferences("Report", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefReport .edit();
                editor.putString("fromPage", "3");
                editor.commit();

                Intent intent = new Intent(StorelistActivity.this, DetailReportSummaryActivity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", fDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", "0");
                intent.putExtra("back", "0");
                intent.putExtra("fromPage","StorelistActivity");
                startActivity(intent);
                finish();

            }
        });

        final   Button btndistrbtrStock = (Button) dialog.findViewById(R.id.btndistrbtrStock);
        btndistrbtrStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(imei==null)
                {
                    imei=CommonInfo.imei;
                }
                if(fDate==null)
                {
                    Date date1 = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    fDate = sdf.format(date1).toString().trim();
                }
                int CstmrNodeId=0,CstomrNodeType= 0;

                Intent i=new Intent(StorelistActivity.this,DistributorEntryActivity.class);
                i.putExtra("imei", imei);
                i.putExtra("CstmrNodeId", CstmrNodeId);
                i.putExtra("CstomrNodeType", CstomrNodeType);
                i.putExtra("fDate", fDate);
                startActivity(i);
                //finish();
            }
        });






        final   Button btndsrTracker = (Button) dialog.findViewById(R.id.btndsrTracker);
        btndsrTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openDSRTrackerAlert();
            }
        });

        final   Button btnMapDistributor = (Button) dialog.findViewById(R.id.btnMapDistributor);
        btnMapDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(StorelistActivity.this,DistributorMapActivity.class);
                startActivity(intent);
            }
        });



        final   Button butn_refresh_data = (Button) dialog.findViewById(R.id.butn_refresh_data);
        final   Button btn_upload_data = (Button) dialog.findViewById(R.id.btnSubmit);
        Button btnSummary= (Button) dialog.findViewById(R.id.btnSummary);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(StorelistActivity.this,SummaryActivity.class);
                startActivity(i);
                finish();

            }
        });



       /* if(AllFilesNameNotSync.length>0)
        {
            btn_upload_data.setVisibility(View.VISIBLE);


        }
        else
        {
            btn_upload_data.setVisibility(View.GONE);
        }*/





        final Button btnVersion = (Button) dialog.findViewById(R.id.btnVersion);
        btnVersion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub




                dialog.dismiss();
            }
        });



        // Version No-V12

        btn_upload_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flgUploadOrRefreshButtonClicked=1;
                if(isOnline())
                {



                    try
                    {

                        if (timerForDataSubmission != null) {
                            timerForDataSubmission.cancel();
                            timerForDataSubmission = null;
                        }

                        timerForDataSubmission = new Timer();
                        myTimerTaskForDataSubmission = new MyTimerTaskForDataSubmission();
                        timerForDataSubmission.schedule(myTimerTaskForDataSubmission, 180000);

                       /* if(dbEngine.fnCheckForPendingImages()==1)
                        {
                            task = new ImageSync(StorelistActivity.this);
                            task.execute();
                        }
                        else if(dbEngine.fnCheckForPendingXMLFilesInTable()==1)
                        {
                            task2= new FullSyncDataNow(StorelistActivity.this);
                            task2.execute();
                        }*/



                    }
                    catch(Exception e)
                    {

                    }



                } else
                {
                    showNoConnAlert();
                    return;

                }
               /* if(isOnline())
                {

                   *//* ImageSync task = new ImageSync();
                    task.execute();*//*
                   // dialog.dismiss();




                }
                else
                {
                    showNoConnAlert();
                   // dialog.dismiss();
                }*/
            }
        });

        butn_refresh_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                flgUploadOrRefreshButtonClicked=2;
               /* if(AllFilesNameNotSync.length>0)
                {

                    alertSubmitPendingData();

                }
                else
                {*/
                if(isOnline())
                {

                    AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(StorelistActivity.this);

                    // set title
                    alertDialogBuilderNEw11.setTitle("Information");

                    // set dialog message
                    alertDialogBuilderNEw11.setMessage("Are you sure to refresh complete Data?");
                    alertDialogBuilderNEw11.setCancelable(false);
                    alertDialogBuilderNEw11.setPositiveButton("Yes",new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialogintrfc,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialogintrfc.cancel();

                            if (timerForDataSubmission != null) {
                                timerForDataSubmission.cancel();
                                timerForDataSubmission = null;
                            }

                            timerForDataSubmission = new Timer();
                            myTimerTaskForDataSubmission = new MyTimerTaskForDataSubmission();
                            timerForDataSubmission.schedule(myTimerTaskForDataSubmission, 180000);

                            if(dbEngine.fnCheckForPendingImages()==1)
                            {
                                task = new ImageSync(StorelistActivity.this);
                                task.execute();
                            }
                            else if(dbEngine.fnCheckForPendingXMLFilesInTable()==1)
                            {
                                // new FullSyncDataNow(StorelistActivity.this).execute();
                                task2= new FullSyncDataNow(StorelistActivity.this);
                                task2.execute();
                            }
                            else
                            {
                                fnUploadDataAndGetFreshData();
                            }


                            //onCreate(new Bundle());
                        }
                    });

                    alertDialogBuilderNEw11.setNegativeButton(R.string.txtNo,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogintrfc, int which) {
                                    // //System.out.println("value ofwhatTask after no button pressed by sunil"+whatTask);

                                    dialogintrfc.dismiss();
                                }
                            });

                    alertDialogBuilderNEw11.setIcon(R.drawable.info_ico);
                    AlertDialog alert121 = alertDialogBuilderNEw11.create();
                    alert121.show();
                } else
                {
                    showNoConnAlert();
                    return;

                }
                // }



                dialog.dismiss();

            }

        });



        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public void fnUploadDataAndGetFreshData()
    {
        try
        {
            GetStoreAllData getStoreAllDataAsync= new GetStoreAllData(StorelistActivity.this);
            getStoreAllDataAsync.execute();
            //////System.out.println("SRVC-OK: "+ new GetStoresForDay().execute().get());
        }
        catch(Exception e)
        {

        }
    }

    private class GetStoreAllData extends AsyncTask<Void, Void, Void> {
        public GetStoreAllData(StorelistActivity activity)
        {
            pDialog2STANDBY = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2STANDBY.setTitle("Please Wait");
            pDialog2STANDBY.setMessage("Refreshing Complete data...");
            pDialog2STANDBY.setIndeterminate(false);
            pDialog2STANDBY.setCancelable(false);
            pDialog2STANDBY.setCanceledOnTouchOutside(false);
            pDialog2STANDBY.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                int DatabaseVersion = dbEngine.DATABASE_VERSION;
                int ApplicationID = dbEngine.Application_TypeID;
                //newservice = newservice.getAvailableAndUpdatedVersionOfApp(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);

                dbEngine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);

                for(int mm = 1; mm<6; mm++)
                {
                    if(mm==2)
                    {
                        //(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
                        newservice = newservice.getStoreAllDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }


                    }
                    if(mm==3)
                    {
                        newservice = newservice.callfnSingleCallAllWebService(getApplicationContext(),ApplicationID,imei);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if(mm==4)
                    {
                        newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, "0");
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if(mm==1)
                    {
                        newservice = newservice.getSOSummary(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }
                    }
                    if(mm==5)
                    {


                        newservice = newservice.fnGetStateCityListMstr(StorelistActivity.this,imei, fDate,CommonInfo.Application_TypeID);
                        if(!newservice.director.toString().trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                                break;
                            }

                        }
                    }
                }






            } catch (Exception e) {
            } finally {
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(pDialog2STANDBY.isShowing())
            {
                pDialog2STANDBY.dismiss();
            }
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
                showErrorAlert("Error while retrieving data");
                //clear sharedpreferences

            }
            else {
                dbEngine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(0);
                String userName=   dbEngine.getUsername();
                String storeCountDeatails=   dbEngine.getTodatAndTotalStores();
                String   TotalStores = storeCountDeatails.split(Pattern.quote("^"))[0];
                String   TodayStores = storeCountDeatails.split(Pattern.quote("^"))[1];


                //if

                Intent intent =new Intent(StorelistActivity.this,StorelistActivity.class);
                StorelistActivity.this.startActivity(intent);
                finish();



                //intentPassToLauncherActivity("0", userName, TotalStores, TodayStores);
                //else


                //send to storelist or launcher
                //next code is here
            }
        }

    }



    private class FullSyncDataNow extends AsyncTask<Void, Void, Void>
    {



        int responseCode=0;
        public FullSyncDataNow(StorelistActivity activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            File LTFoodXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!LTFoodXMLFolder.exists())
            {
                LTFoodXMLFolder.mkdirs();
            }


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage("Before Refreshing list Uploading pending Stores Data...");
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();


        }

        @Override

        protected Void doInBackground(Void... params)
        {


            try
            {



                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

                // check number of files in folder
                String [] AllFilesName= checkNumberOfFiles(del);


                if(AllFilesName.length>0)
                {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


                    for(int vdo=0;vdo<AllFilesName.length;vdo++)
                    {
                        String fileUri=  AllFilesName[vdo];


                        //System.out.println("Sunil Again each file Name :" +fileUri);

                        if(fileUri.contains(".zip"))
                        {
                            File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/" + CommonInfo.OrderXMLFolder + "/" +fileUri);
                            file.delete();
                        }
                        else
                        {
                            String f1=Environment.getExternalStorageDirectory().getPath()+ "/" + CommonInfo.OrderXMLFolder + "/" +fileUri;
                            // System.out.println("Sunil Again each file full path"+f1);
                            try
                            {
                                responseCode= upLoad2Server(f1,fileUri);
                            }
                            catch (Exception e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if(responseCode!=200)
                        {
                            break;
                        }

                    }

                }
                else
                {
                    responseCode=200;
                }







            } catch (Exception e)
            {

                e.printStackTrace();
                if(pDialogGetStores.isShowing())
                {
                    pDialogGetStores.dismiss();
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            if (myTimerTaskForDataSubmission != null) {
                myTimerTaskForDataSubmission.cancel();
                myTimerTaskForDataSubmission=null;
            }
            if (timerForDataSubmission!=null)
            {
                timerForDataSubmission.cancel();
                timerForDataSubmission = null;
            }

            if(responseCode == 200)
            {

                dbEngine.fndeleteSbumittedStoreList(5);
                dbEngine.fndeleteSbumittedStoreList(4);
                dbEngine.deleteXmlTable("4");
                if(flgUploadOrRefreshButtonClicked==1)
                {
                    showErrorAlert(getString(R.string.saveAlertOKMsg));
                }
                else
                {
                    fnUploadDataAndGetFreshData();
                }

            }
            else
            {
                showErrorAlert(getString(R.string.uploading_errorXMLFiles));
            }


        }
    }

    public void delXML(String delPath)
    {
        File file = new File(delPath);
        file.delete();
        File file1 = new File(delPath.toString().replace(".xml", ".zip"));
        file1.delete();
    }

    public static String[] checkNumberOfFiles(File dir)
    {
        int NoOfFiles=0;
        String [] Totalfiles = null;

        if (dir.isDirectory())
        {
            String[] children = dir.list();
            NoOfFiles=children.length;
            Totalfiles=new String[children.length];

            for (int i=0; i<children.length; i++)
            {
                Totalfiles[i]=children[i];
            }
        }
        return Totalfiles;
    }

    public static void zip(String[] files, String zipFile) throws IOException
    {
        BufferedInputStream origin = null;
        final int BUFFER_SIZE = 2048;

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                finally {
                    origin.close();
                }
            }
        }

        finally {
            out.close();
        }
    }



    public  int upLoad2Server(String sourceFileUri,String fileUri)
    {

        fileUri=fileUri.replace(".xml", "");

        String fileName = fileUri;
        String zipFileName=fileUri;

        String newzipfile = Environment.getExternalStorageDirectory() + "/"+CommonInfo.OrderXMLFolder+"/" + fileName + ".zip";

        sourceFileUri=newzipfile;

        xmlForWeb[0]=         Environment.getExternalStorageDirectory() + "/"+CommonInfo.OrderXMLFolder+"/" + fileName + ".xml";


        try
        {
            zip(xmlForWeb,newzipfile);
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            //java.io.FileNotFoundException: /359648069495987.2.21.04.2016.12.44.02: open failed: EROFS (Read-only file system)
        }


        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        File file2send = new File(newzipfile);

        String urlString = CommonInfo.OrderSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;

        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(file2send);
            URL url = new URL(urlString);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("zipFileName", zipFileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + zipFileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            //Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200)
            {
                syncFLAG = 1;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                // editor.remove(xmlForWeb[0]);
                editor.putString(fileUri, ""+4);
                editor.commit();

                String FileSyncFlag=pref.getString(fileUri, ""+1);
                dbEngine.upDateTblXmlFile(fileName);
                delXML(xmlForWeb[0].toString());


            }
            else
            {
                syncFLAG = 0;
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex)
        {
            ex.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }




        return serverResponseCode;

    }


    public void showErrorAlert(String msg)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(StorelistActivity.this);
        alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setNeutralButton(R.string.txtOk,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                        // finish();
                    }
                });
        alertDialogNoConn.setIcon(R.drawable.error_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }
    public void showNoConnAlert()
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(StorelistActivity.this);
        alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
        alertDialogNoConn.setMessage(R.string.genTermNoDataConnectionFullMsg);
        alertDialogNoConn.setNeutralButton(R.string.txtOk,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                        // finish();
                    }
                });
        alertDialogNoConn.setIcon(R.drawable.error_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }







    private class ImageSync extends AsyncTask<Void,Void,Boolean>
    {
        // ProgressDialog pDialogGetStores;
        public ImageSync(StorelistActivity activity)
        {
            pDialog2STANDBY = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();




            pDialog2STANDBY.setTitle("Please Wait");
            pDialog2STANDBY.setMessage("Before Refreshing list Uploading pending Image...");
            pDialog2STANDBY.setIndeterminate(false);
            pDialog2STANDBY.setCancelable(false);
            pDialog2STANDBY.setCanceledOnTouchOutside(false);
            /*pDialog2STANDBY.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    imgSyncTask.cancel(true);
                    dialog.dismiss();
                }
            });*/
            pDialog2STANDBY.show();
        }
        @Override
        protected Boolean doInBackground(Void... args)
        {
            boolean isErrorExist=false;


            try
            {
                //dbEngine.upDateCancelTask("0");
                ArrayList<String> listImageDetails=new ArrayList<String>();

                listImageDetails=dbEngine.getImageDetails(5);

                if(listImageDetails!=null && listImageDetails.size()>0)
                {
                    for(String imageDetail:listImageDetails)
                    {
                        String tempIdImage=imageDetail.split(Pattern.quote("^"))[0].toString();
                        String imagePath=imageDetail.split(Pattern.quote("^"))[1].toString();
                        String imageName=imageDetail.split(Pattern.quote("^"))[2].toString();
                        String file_dj_path = Environment.getExternalStorageDirectory() + "/"+CommonInfo.ImagesFolder+"/"+imageName;
                        File fImage = new File(file_dj_path);
                        if (fImage.exists())
                        {
                            uploadImage(imagePath, imageName, tempIdImage);
                        }



                    }
                }


            }
            catch (Exception e)
            {
                isErrorExist=true;
            }

            finally
            {
                Log.i("SvcMgr", "Service Execution Completed...");
            }

            return isErrorExist;
        }

        @Override
        protected void onPostExecute(Boolean resultError)
        {
            super.onPostExecute(resultError);

            if(pDialog2STANDBY.isShowing())
            {
                pDialog2STANDBY.dismiss();

            }


            if(resultError)   // if Webservice showing exception or not excute complete properly
            {


                if (myTimerTaskForDataSubmission != null) {
                    myTimerTaskForDataSubmission.cancel();
                    myTimerTaskForDataSubmission=null;
                }
                if (timerForDataSubmission!=null)
                {
                    timerForDataSubmission.cancel();
                    timerForDataSubmission = null;
                }
                showErrorAlert(getString(R.string.uploading_error));
            }
            else
            {
                dbEngine.fndeleteSbumittedStoreImagesOfSotre(4);
               /* if(dbEngine.fnCheckForPendingXMLFilesInTable()==1)
                {
                    new FullSyncDataNow(StorelistActivity.this).execute();
                }
                else {
                    if(flgUploadOrRefreshButtonClicked==2)
                    {
                        fnUploadDataAndGetFreshData();
                    }
                    else if(flgUploadOrRefreshButtonClicked==1)
                    {
                        showErrorAlert(getString(R.string.saveAlertOKMsg));
                    }
                }*/

            }

        }
    }



    public void uploadImage(String sourceFileUri,String fileName,String tempIdImage) throws IOException
    {
        BitmapFactory.Options IMGoptions01 = new BitmapFactory.Options();
        IMGoptions01.inDither=false;
        IMGoptions01.inPurgeable=true;
        IMGoptions01.inInputShareable=true;
        IMGoptions01.inTempStorage = new byte[16*1024];

        //finalBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fnameIMG,IMGoptions01), 640, 480, false);

        Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(sourceFileUri).getPath(),IMGoptions01);

//			/Uri.parse(sourceFileUri).getPath()
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); //compress to which format you want.

        //b is the Bitmap
        //int bytes = bitmap.getWidth()*bitmap.getHeight()*4; //calculate how many bytes our image consists of. Use a different value than 4 if you don't use 32bit images.

        //ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        //bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        //byte [] byte_arr = buffer.array();


        //     byte [] byte_arr = stream.toByteArray();
        String image_str = BitMapToString(bitmap);
        ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

        ////System.out.println("image_str: "+image_str);

        stream.flush();
        stream.close();
        //buffer.clear();
        //buffer = null;
        bitmap.recycle();
        nameValuePairs.add(new BasicNameValuePair("image",image_str));
        nameValuePairs.add(new BasicNameValuePair("FileName", fileName));
        nameValuePairs.add(new BasicNameValuePair("TempID", tempIdImage));
        try
        {

            HttpParams httpParams = new BasicHttpParams();
            int some_reasonable_timeout = (int) (30 * DateUtils.SECOND_IN_MILLIS);

            //HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);

            HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout+2000);


            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpPost httppost = new HttpPost(CommonInfo.ImageSyncPath);



            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            String the_string_response = convertResponseToString(response);
            if(the_string_response.equals("Abhinav"))
            {
                dbEngine.updateSSttImage(fileName, 4);
                dbEngine.fndeleteSbumittedStoreImagesOfSotre(4);

                String file_dj_path = Environment.getExternalStorageDirectory() + "/"+CommonInfo.ImagesFolder+"/"+fileName;
                File fdelete = new File(file_dj_path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {

                        callBroadCast();
                    } else {

                    }
                }

            }

        }catch(Exception e)
        {

            System.out.println(e);
            //	IMGsyOK = 1;

        }
    }
    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(StorelistActivity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            StorelistActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException
    {

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        //System.out.println("contentLength : " + contentLength);
        //Toast.makeText(MainActivity.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
        if (contentLength < 0)
        {
        }
        else
        {
            byte[] data = new byte[512];
            int len = 0;
            try
            {
                while (-1 != (len = inputStream.read(data)) )
                {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream.close(); // closing the stream…..
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string…..

            //System.out.println("Result : " + res);
            //Toast.makeText(MainActivity.this, "Result : " + res, Toast.LENGTH_LONG).show();
            ////System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }


  /*  public String BitMapToString(Bitmap bitmap)
    {
        int h1=bitmap.getHeight();
        int w1=bitmap.getWidth();
        h1=h1/8;
        w1=w1/8;
        bitmap=Bitmap.createScaledBitmap(bitmap, w1, h1, true);

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }*/
  public String BitMapToString(Bitmap bitmap)
  {
      int h1=bitmap.getHeight();
      int w1=bitmap.getWidth();

      if(w1 > 768 || h1 > 1024){
          bitmap=Bitmap.createScaledBitmap(bitmap,1024,768,true);

      }


      else {

          bitmap=Bitmap.createScaledBitmap(bitmap,w1,h1,true);
      }

      ByteArrayOutputStream baos=new  ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
      byte [] arr=baos.toByteArray();
      String result=Base64.encodeToString(arr, Base64.DEFAULT);
      return result;
  }


    public void filterStoreList(int CoverageIDOfSpinner,int RouteIDOfSpinner)
    {
        if (parentOfAllDynamicData != null && parentOfAllDynamicData.getChildCount() > 0) {
            // int rowCunt=parentOfAllDynamicData.getChildCount();
            for(Map.Entry<String, String> entry:hmapStoresFromDataBase.entrySet()) {
                //StoreID,StoreName,DateAdded,CoverageAreaID,RouteNodeID,StoreCategoryType,StoreSectionCount,flgApproveOrRejectOrNoActionOrReVisit

                //   ////StoreName,DateAdded,CoverageAreaID,RouteNodeID,StoreCategoryType,StoreSectionCount,flgApproveOrRejectOrNoActionOrReVisit
                String storeID=entry.getKey().toString().trim();
                String StoreDetails=entry.getValue().toString().trim();
                int CoverageAreaIDIdOfStore = Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[2]);
                int RouteNodeIDIdOfStore = Integer.parseInt(StoreDetails.split(Pattern.quote("^"))[3]);
                String StoreCategoryTypeOfStore=StoreDetails.split(Pattern.quote("^"))[4];
                //storeID+"^"+CoverageAreaID+"^"+RouteNodeID+"^"+StoreCategoryType

                //dynamic_container.setTag(storeID+"^"+CoverageAreaID+"^"+RouteNodeID+"^"+StoreCategoryType);

                View dynamic_container=(View) parentOfAllDynamicData.findViewWithTag(storeID +"^"+CoverageAreaIDIdOfStore +"^"+RouteNodeIDIdOfStore +"^"+StoreCategoryTypeOfStore);
                // String asdasdad=" dynamic_container tag got is :-" + dynamic_container.getTag();
                if(CoverageIDOfSpinner==0 && RouteIDOfSpinner==0)
                {
                    dynamic_container.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(CoverageIDOfSpinner!=0 && RouteIDOfSpinner!=0)
                    {
                        if(CoverageAreaIDIdOfStore==CoverageIDOfSpinner && RouteNodeIDIdOfStore==RouteIDOfSpinner)
                        {
                            dynamic_container.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            dynamic_container.setVisibility(View.GONE);
                        }
                    }
                    if(CoverageIDOfSpinner!=0 && RouteIDOfSpinner==0)
                    {
                        if(CoverageAreaIDIdOfStore==CoverageIDOfSpinner)
                        {
                            dynamic_container.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            dynamic_container.setVisibility(View.GONE);
                        }
                    }
                    if(CoverageIDOfSpinner==0 && RouteIDOfSpinner!=0)
                    {
                        if(RouteNodeIDIdOfStore==RouteIDOfSpinner)
                        {
                            dynamic_container.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            dynamic_container.setVisibility(View.GONE);
                        }
                    }
                }
            }

        }

    }

    public void setBtnBackgroundOfLineOnline()
    {
        if(CommonInfo.flgLTFoodsSOOnlineOffLine==0)
        {
            offlineBtn.setBackground(getResources().getDrawable(R.drawable.btn_background));
            offlineBtn.setTextColor(Color.parseColor("#FFFFFF"));
            onlineBtn.setBackground(getResources().getDrawable(R.drawable.logo_background));
            onlineBtn.setTextColor(Color.parseColor("#FFFF4424"));

        }
        else
        {
            onlineBtn.setBackground(getResources().getDrawable(R.drawable.btn_background));
            onlineBtn.setTextColor(Color.parseColor("#FFFFFF"));
            offlineBtn.setBackground(getResources().getDrawable(R.drawable.logo_background));
            offlineBtn.setTextColor(Color.parseColor("#FFFF4424"));
        }
    }
    class MyTimerTaskForDataSubmission extends TimerTask
    {

        @Override
        public void run()
        {

            StorelistActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (timerForDataSubmission != null) {
                        timerForDataSubmission.cancel();
                        timerForDataSubmission = null;
                    }
                    if(task!=null){
                        if(task.getStatus()==AsyncTask.Status.RUNNING)
                        {
                            task.cancel(true);

                        }
                    }
                    if(task2!=null) {
                        if (task2.getStatus() == AsyncTask.Status.RUNNING) {
                            task2.cancel(true);

                        }
                    }

                    if(pDialog2STANDBY!=null) {
                        if (pDialog2STANDBY.isShowing()) {
                            pDialog2STANDBY.dismiss();

                        }

                    }
                    if(pDialogGetStores!=null) {
                        if (pDialogGetStores.isShowing()) {
                            pDialogGetStores.dismiss();

                        }

                    }


                }});
        }

    }

    private class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
    {




        public GetInvoiceForDay(StorelistActivity activity)
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

                HashMap<String,String> hmapInvoiceOrderIDandStatus=new HashMap<String, String>();
                hmapInvoiceOrderIDandStatus=dbEngine.fetchHmapInvoiceOrderIDandStatus();

                for(int mm = 1; mm < 5  ; mm++)
                {
                    if(mm==1)
                    {
                        newservice = newservice.callInvoiceButtonStoreMstr(getApplicationContext(), fDate, imei, rID,hmapInvoiceOrderIDandStatus);

                        if(!newservice.director.toString().trim().equals("1"))
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

                        if(!newservice.director.toString().trim().equals("1"))
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
                        dbEngine.open();
                        int check1=dbEngine.counttblCatagoryMstr();
                        dbEngine.close();
                        if(check1==0)
                        {
                            newservice = newservice.getCategory(getApplicationContext(), imei);
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
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            dismissProgress();

            Date currDate = new Date();
            SimpleDateFormat currDateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

           String  currSysDate = currDateFormat.format(currDate);
            Intent storeIntent = new Intent(StorelistActivity.this, InvoiceStoreSelection.class);
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
                android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(StorelistActivity.this);
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
                android.support.v7.app.AlertDialog alert = alertDialogNoConn.create();
                alert.show();
                return;

            }
        }
    }
}
