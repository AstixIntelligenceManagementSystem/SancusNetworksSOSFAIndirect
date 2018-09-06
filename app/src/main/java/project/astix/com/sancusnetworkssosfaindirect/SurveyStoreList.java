package project.astix.com.sancusnetworkssosfaindirect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.astix.Common.CommonInfo;

import com.example.gcm.NotificationActivity;


//import com.astix.sfatju.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SurveyStoreList extends BaseActivity implements com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{
    ImageView backIcon;
     EditText inputSearch;
       ListView   listDistributor;
     ArrayAdapter<String>     adapterDistributor;
    LinkedHashMap<String, String> hmapdsrIdAndDescr_details=new LinkedHashMap<String, String>();
    String[] drsNames;
    SharedPreferences sharedPref;
    int slctdCoverageAreaNodeID=0,slctdCoverageAreaNodeType=0,slctdDSrSalesmanNodeId=0,slctdDSrSalesmanNodeType=0;
    public String	SelectedDSRValue="";
    public String currSysDate;
    public int chkFlgForErrorToCloseApp=0;
    Spinner spinner_manager;
    Spinner spinner_RouteList;

    String[] Manager_names=null;
    String[] Route_names=null;
    //String[] Manager_names= { "Select Market Location", "sec-20", "sec-24", "other"};
    static String selected_manager="NA";
    static String seleted_routeIDType="0";
    RelativeLayout rl_for_other;
    EditText ed_Street;
    static int Selected_manager_Id=0;



    HashMap<String, String> hmapManagerNameManagerIdDetails=new HashMap<String, String>();
    LinkedHashMap<String, String> hmapStoreNameID=new LinkedHashMap<String, String>();
    HashMap<String, String> hmapRouteIdNameDetails=new HashMap<String, String>();



    boolean serviceException=false;

    public static final String DATASUBDIRECTORYForText = CommonInfo.TextFileFolder;

    public String passDate;
    public SimpleDateFormat sdf;
    public String fDate;
    public String userDate;

    public String pickerDate;
    public String imei;
    public String[] storeList;
    public String[] storeRouteIdType;
    Dialog dialog;

    CheckBox check1, check2;
    public TableLayout tl2;
    RelativeLayout relativeLayout1;
    int battLevel=0;

    public static HashMap<String, String> hmapStoreIdSstat=new HashMap<String, String>();
    public static HashMap<String, String> hmapStoreIdForDate=new HashMap<String, String>();
    public static HashMap<String, String> hmapStoreIdflgOrderType=new HashMap<String, String>();
    public int flgDayEndOrChangeRoutenew=0;
    LinkedHashMap<String, String> hmapOutletListForNear=new LinkedHashMap<String, String>();
    LinkedHashMap<String, String> hmapOutletListForNearUpdated=new LinkedHashMap<String, String>();

    LinkedHashMap<String, String> hmapTestSubmitOrNot=new LinkedHashMap<String, String>();

    static int flgChangeRouteOrDayEnd = 0;

    ProgressDialog pDialogGetStores;
    ProgressDialog mProgressDialog;
    public String Noti_text="Null";
    public int MsgServerID=0;

    public boolean[] checks;
    ServiceWorker newservice = new ServiceWorker();
    static ScheduledExecutorService scheduler;
    public static ScheduledExecutorService schPHSTATS;

    public int noLOCflag = 0;
    boolean bool = true;
    DatabaseAssistant DA = new DatabaseAssistant(this);
    public ProgressDialog pDialogSync;

    ImageView img_side_popUp;
    int closeList = 0;
    int whatTask = 0;
    String whereTo = "11";

    ArrayList mSelectedItems = new ArrayList();

    int prevSel = 0;
    int prevID;
    public long syncTIMESTAMP;
    public String fullFileName1;

    public String[] storeCode;
    public String[] storeName;
    ArrayList<String> stIDs;
    ArrayList<String> stNames;

    public String[] storeStatus;

    public String[] StoreflgSubmitFromQuotation;


    public String[] storeCloseStatus;

    public String[] storeNextDayStatus;
    public ListView listView;
    public ProgressDialog pDialog2STANDBY;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);


    public TableRow tr;
    public String selStoreID = "";
    public String selStoreName = "";
    public String prevSelStoreID;

    public Double myCurrentLon; // removed "static"
    public Double myCurrentLat;
    public Double finalLatNow;
    public Double finalLonNow;

    public int gotLoc = 0;
    public int locStat = 0;
    ProgressDialog pDialog2;
    String FWDCLname;

    String BCKCLname;
    public Location firstLoc;
    public float acc;


    public Location location2;
    public String[] StoreList2Procs;
    public Location finalLocation;
    standBYtask task_STANDBY = new standBYtask();


    private final Context mContext = this;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude;
    double longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1; // 1 second

    // Declaring a Location Manager
    protected LocationManager locationManager;
    public int valDayEndOrChangeRoute=0; // 1=Clicked on Day End Button, 2=Clicked on Change Route Button


    public String[] route_name;
    public String[] route_name_id;
    public String selected_route_id="0";

    private int selected = 0;
    public String temp_select_routename="NA";
    public String temp_select_routeid="NA";
    public String rID;
    public   PowerManager pm;
    public	 PowerManager.WakeLock wl;
    public Location location;
    public String AllProvidersLocation="";
    public String FusedLocationLatitudeWithFirstAttempt="0";
    public String FusedLocationLongitudeWithFirstAttempt="0";
    public String FusedLocationAccuracyWithFirstAttempt="0";
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
    public CoundownClass countDownTimer;
    private final long startTime = 15000;
    private final long interval = 200;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    String fusedData;
    public String fnAccurateProvider="";
    public String fnLati="0";
    public String fnLongi="0";
    public Double fnAccuracy=0.0;
    ShivAdapter adapterCusto;
    ArrayList<String> listItem;
    ArrayList<String> listStoreId;
    LocationRequest mLocationRequest;
    LinkedHashMap<String,String> hmapStoreLatLongDistanceFlgRemap=new LinkedHashMap<String,String>();
    SharedPreferences sharedPrefForSurvey;
    private void getDSRDetail() throws IOException
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



	/*@Override
	public void onWindowFocusChanged(boolean hasFocus)   // Force PDA donot show Statusbar to the user
    {
		// TODO Auto-generated method stub
		// super.onWindowFocusChanged(hasFocus);

		try
		{
			if (!hasFocus)
			{
				Object service = getSystemService("statusbar");
				Class<?> statusbarManager = Class
						.forName("android.app.StatusBarManager");
				Method collapse = statusbarManager.getMethod("collapse");
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		}
		catch (Exception ex)
		{
		}

	}
	*/



    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {

            battLevel = intent.getIntExtra("level", 0);

        }
    };
    public void locationRetrievingAndDistanceCalculating()
    {
        appLocationService = new AppLocationService();

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire();


        pDialog2STANDBY = ProgressDialog.show(SurveyStoreList.this, getText(R.string.genTermPleaseWaitNew), getText(R.string.searchingnearbystores), true);
        pDialog2STANDBY.setIndeterminate(true);

        pDialog2STANDBY.setCancelable(false);
        pDialog2STANDBY.show();

        if (isGooglePlayServicesAvailable()) {
            createLocationRequest();

            mGoogleApiClient = new GoogleApiClient.Builder(SurveyStoreList.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(SurveyStoreList.this)
                    .addOnConnectionFailedListener(SurveyStoreList.this)
                    .build();
            mGoogleApiClient.connect();
        }
        //startService(new Intent(DynamicActivity.this, AppLocationService.class));


        startService(new Intent(SurveyStoreList.this, AppLocationService.class));
        Location nwLocation = appLocationService.getLocation(locationManager, LocationManager.GPS_PROVIDER, location);
        Location gpsLocation = appLocationService.getLocation(locationManager, LocationManager.NETWORK_PROVIDER, location);
        countDownTimer = new CoundownClass(startTime, interval);
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
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }
    protected void startLocationUpdates()
    {
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

    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);




    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, appLocationService);
        locationManager.removeUpdates(appLocationService);
    }

    @Override
    public void onLocationChanged(Location args0) {
        mCurrentLocation = args0;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    public class standBYtask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }



    // *****SYNC******

    public void SyncNow()
    {

        syncTIMESTAMP = System.currentTimeMillis();
        Date dateobj = new Date(syncTIMESTAMP);


        dbengine.open();
        String presentRoute=dbengine.GetActiveRouteID();
        dbengine.close();
        //syncTIMESTAMP = System.currentTimeMillis();
        //Date dateobj = new Date(syncTIMESTAMP);
        SimpleDateFormat df = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
        //fullFileName1 = df.format(dateobj);
        String newfullFileName=imei+"."+presentRoute+"."+ df.format(dateobj);



        try
        {

            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!OrderXMLFolder.exists())
            {
                OrderXMLFolder.mkdirs();
            }

            String routeID=dbengine.GetActiveRouteIDSunil();

            DA.open();
            DA.export(dbengine.DATABASE_NAME, newfullFileName,routeID);


            DA.close();

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
                dbengine.UpdateNewAddedStorephotoFlag(stIDneeded.trim(), 5);
                dbengine.updateflgFromWhereSubmitStatusAgainstStore(stIDneeded, 1);

                if(dbengine.fnchkIfStoreHasInvoiceEntry(stIDneeded)==1)
                {
                    dbengine.updateStoreQuoteSubmitFlgInStoreMstrInChangeRouteCase(stIDneeded, 0);
                }


            }
            dbengine.close();

            flgChangeRouteOrDayEnd=valDayEndOrChangeRoute;

            Intent syncIntent = new Intent(SurveyStoreList.this, SyncMaster.class);
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

	/*private class FullSyncDataNow extends AsyncTask<Void, Void, Void> {



		@Override
		protected Void doInBackground(Void... params) {

			try {

			}

			catch (Exception e) {
			//	Log.i("Sync ASync", "Sync ASync Failed!", e);

			}

			finally {

			}
			return null;
		}

		@Override
		protected void onCancelled() {

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			 pDialogSync.dismiss();

			//finish();
		}
	}*/

    // *****SYNC******


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

            pDialog2 = ProgressDialog.show(SurveyStoreList.this,getText(R.string.genTermPleaseWaitNew),getText(R.string.genTermProcessingRequest), true);
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



    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialog.setIcon(R.drawable.error_info_ico);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(R.string.genTermGPSDisablePleaseEnable);

        alertDialog.setPositiveButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.show();
    }


    public void enableGPSifNot()
    {

        boolean isGPSok = false;
        isGPSok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSok)
        {
            showSettingsAlert();
            isGPSok = false;
        }
    }




    public void DayEnd()
    {


        AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(SurveyStoreList.this);

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
                if (dbengine.GetLeftStoresChk() == true) {
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
							 Intent revupOldFriend = new Intent(SurveyStoreList.this,LauncherActivity.class);
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
        AlertDialog alert = alertDialogSubmitConfirm.create();
        alert.show();

    }


    public void DayEndWithoutalert()
    {

        dbengine.open();
        String rID=dbengine.GetActiveRouteID();

        dbengine.UpdateTblDayStartEndDetails(Integer.parseInt(rID), valDayEndOrChangeRoute);
        dbengine.close();

        SyncNow();

    }



    public void showChangeRouteConfirm()
    {

        AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(SurveyStoreList.this);
        alertDialogSubmitConfirm.setTitle(R.string.PleaseConformMsg);
        if(flgDayEndOrChangeRoutenew==1)
        {
            alertDialogSubmitConfirm.setMessage(getText(R.string.genTermDayEndAlertWithoutStoreSubmit));
        }
        else if(flgDayEndOrChangeRoutenew==2)
        {
            alertDialogSubmitConfirm.setMessage(getText(R.string.genTermchangeRouteAlert));
        }

        alertDialogSubmitConfirm.setCancelable(false);

        alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                // Location_Getting_Service.closeFlag = 1;
                //enableGPSifNot();

                // run bgTasker()!

                // if(!scheduler.isTerminated()){
                // scheduler.shutdownNow();
                // }
                dbengine.open();

                if (dbengine.GetLeftStoresChk() == true) {
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
                } else {
                    // show dialog for clear..clear + tranx to launcher

                    // -- Route Info Exec()
                    try {
                        dbengine.close();

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

							/*Intent revupOldFriend = new Intent(SurveyStoreList.this, LauncherActivity.class);
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
        AlertDialog alert = alertDialogSubmitConfirm.create();
        alert.show();

    }




    public void showChangeRouteConfirmWhenNoStoreisLeftToSubmit()
    {

        AlertDialog.Builder alertDialogSubmitConfirm = new AlertDialog.Builder(SurveyStoreList.this);
        alertDialogSubmitConfirm.setTitle(R.string.PleaseConformMsg);
        alertDialogSubmitConfirm.setMessage(getText(R.string.genTermchangeRouteAlertWhenNoStoreisLeftToSubmit));
        alertDialogSubmitConfirm.setCancelable(false);

        alertDialogSubmitConfirm.setNeutralButton(R.string.AlertDialogYesButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                // Location_Getting_Service.closeFlag = 1;
                //enableGPSifNot();

                // run bgTasker()!

                // if(!scheduler.isTerminated()){
                // scheduler.shutdownNow();
                // }
                dbengine.open();

                if (dbengine.GetLeftStoresChk() == true) {
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
                } else {
                    // show dialog for clear..clear + tranx to launcher

                    // -- Route Info Exec()
                    try {
                        dbengine.close();

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

							/*Intent revupOldFriend = new Intent(
									SurveyStoreList.this, LauncherActivity.class);
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
        AlertDialog alert = alertDialogSubmitConfirm.create();
        alert.show();

    }


    public void showPendingStorelist(int flgDayEndOrChangeRoute)
    {

        // final CharSequence[] items =
        // {"cat1","cat2","cat3","cat4","cat5","cat6","cat7","cat8","cat9","cat10","cat11","cat12","cat13","cat14","cat15","cat16","cat17","cat18","cat19","cat20","cat21","cat22","cat23","cat24"
        // };

        //flgDayEndOrChangeRoutenew=1-DayEnd,2-Change Route
        flgDayEndOrChangeRoutenew=flgDayEndOrChangeRoute;
        ContextThemeWrapper cw = new ContextThemeWrapper(this,R.style.AlertDialogTheme);


        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        //builder.setTitle(R.string.genTermSelectStoresPendingToComplete);
        TextView content = new TextView(this);
        if(flgDayEndOrChangeRoutenew==1)
        {
            content.setText(R.string.genTermSelectStoresPendingToCompleteDayEnd);
        }
        else if(flgDayEndOrChangeRoutenew==2)
        {
            content.setText(R.string.genTermSelectStoresPendingToCompleteChangeRoute);
        }
        //content.setText(R.string.genTermSelectStoresPendingToComplete);
        content.setTextSize(16);
        content.setTextColor(Color.WHITE);
        builder.setCustomTitle(content);
        mSelectedItems.clear();

        final String[] stNames4List = new String[stNames.size()];
        checks=new boolean[stNames.size()];
        stNames.toArray(stNames4List);
        for(int cntPendingList=0;cntPendingList<stNames4List.length;cntPendingList++)
        {
            mSelectedItems.add(stNames4List[cntPendingList]);
            checks[cntPendingList]=true;
        }

        builder.setMultiChoiceItems(stNames4List, checks,new DialogInterface.OnMultiChoiceClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which,boolean isChecked)
            {

                if (isChecked)
                {
                    mSelectedItems.add(stNames4List[which]);

                }
                else if (mSelectedItems.contains(stNames4List[which]))
                {
                    ////System.out.println("Abhinav store Selection  Step 5");
                    mSelectedItems.remove(stNames4List[which]);

                }
            }
        });

        builder.setPositiveButton(R.string.genTermSubmitSelected,new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                if (mSelectedItems.size() == 0)
                {
                    Toast.makeText(getApplicationContext(),R.string.genTermNoStroeSelectedOnSubmit,Toast.LENGTH_SHORT).show();
                    showPendingStorelist(flgDayEndOrChangeRoutenew);
                }

                else
                {
                    // TODO Auto-generated method stub
                    // Toast.makeText(getApplicationContext(),
                    // "User Selected : " + mSelectedItems.toString(),
                    // Toast.LENGTH_SHORT).show();
                    //System.out.println("User Selected : "+ mSelectedItems.toString());

                    // Location_Getting_Service.closeFlag = 1;
                    //enableGPSifNot();
                    // doing stuff here

                    // scheduler.shutdownNow();
                    // if(!scheduler.isTerminated()){
                    // scheduler.shutdownNow();
                    // }
                    // run bgTasker()!
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
        });
        builder.setNeutralButton(R.string.genTermDirectlyChangeRoute,new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                // TODO Auto-generated method stub
                closeList = 1;
                // showChangeRouteConfirm();
                dbengine.open();

                if (dbengine.GetLeftStoresChk() == true)
                {
                    // run bgTasker()!

                    // Location_Getting_Service.closeFlag = 1;
                    // scheduler.shutdownNow();
                    // if(!scheduler.isTerminated()){
                    //enableGPSifNot();
                    // scheduler.shutdownNow();
                    // }

                    whatTask = 3;
                    // -- Route Info Exec()
                    try
                    {
                        new bgTasker().execute().get();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }

                    dbengine.close();

                }
                else
                {
                    dbengine.close();
                    // show dialog for clear..clear + tranx to launcher
                    showChangeRouteConfirm();
                }
            }
        });
        builder.setNegativeButton(R.string.txtOnChangeRouteDayEndCancel,new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                // TODO Auto-generated method stub
                closeList = 1;
                valDayEndOrChangeRoute=0;
            }
        });

        AlertDialog alert = builder.create();
        if (closeList == 1)
        {
            closeList = 0;
            alert.dismiss();

        }
        else
        {
            alert.show();
            alert.setCancelable(false);
        }
    }

    public void midPart()
    {
        String tempSID;
        String tempSNAME;

        stIDs = new ArrayList<String>(StoreList2Procs.length);
        stNames = new ArrayList<String>(StoreList2Procs.length);

        for (int x = 0; x < (StoreList2Procs.length); x++)
        {
            StringTokenizer tokens = new StringTokenizer(String.valueOf(StoreList2Procs[x]), "%");
            tempSID = tokens.nextToken().trim();
            tempSNAME = tokens.nextToken().trim();

            stIDs.add(x, tempSID);
            stNames.add(x, tempSNAME);
        }
    }



    public void onDestroy()
    {
        super.onDestroy();
        // unregister receiver
        this.unregisterReceiver(this.mBatInfoReceiver);

        //this.unregisterReceiver(this.KillME);
    }





    private class GetDataForChangeRoute extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pDialogGetStores;
        public GetDataForChangeRoute(SurveyStoreList activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();



            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getText(R.string.genTermRetrivingData));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... args)
        {
            ServiceWorker newservice = new ServiceWorker();


            try {

                for(int mm = 1; mm < 2  ; mm++)
                {

                    if(mm==1)
                    {
                        //	newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID);
                    }


                }


            } catch (Exception e) {
                Log.i("SvcMgr", "Service Execution Failed!", e);

            }

            finally {

                Log.i("SvcMgr", "Service Execution Completed...");

            }
            //return newservice.director;
            return null;
        }


        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            Log.i("SvcMgr", "Service Execution cycle completed");

            System.out.println("on Post execute called");
            if (pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            Bundle   tempBundle = new Bundle();
            onCreate(tempBundle);

        }
    }

	/*private class GetStoresForDay extends AsyncTask<Void, Void, Void>
	{


		public GetStoresForDay(SurveyStoreList activity)
		{
			pDialogGetStores = new ProgressDialog(activity);
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

				*//*	showAlertBox(getResources().getString(R.string.txtErrorPhnDate));

					dbengine.open();
					dbengine.maintainPDADate();
					dbengine.reCreateDB();
					dbengine.close();
					return;*//*
				}
			}






			dbengine.open();
			dbengine.fnSetAllRouteActiveStatus();

			//rID="17^18^19";

			StringTokenizer st = new StringTokenizer(rID, "^");

			while (st.hasMoreElements())
			{
				//System.out.println("Anand StringTokenizer Output: ");
				dbengine.updateActiveRoute(st.nextElement().toString(), 1);
			}




			long syncTIMESTAMP = System.currentTimeMillis();
			Date dateobj = new Date(syncTIMESTAMP);
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
			String startTS = df.format(dateobj);

			int DayEndFlg=0;
			int ChangeRouteFlg=0;

			int DatabaseVersion=dbengine.DATABASE_VERSION;
			String AppVersionID=dbengine.AppVersionID;
			dbengine.insertTblDayStartEndDetails(imei,startTS,rID,DayEndFlg,ChangeRouteFlg,fDate,AppVersionID);//DatabaseVersion;//getVersionNumber
			dbengine.close();


			pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
			pDialogGetStores.setMessage(getText(R.string.RetrivingDataMsg));
			pDialogGetStores.setIndeterminate(false);
			pDialogGetStores.setCancelable(false);
			pDialogGetStores.setCanceledOnTouchOutside(false);
			pDialogGetStores.show();
		}

		@Override
		protected Void doInBackground(Void... args)
		{
			ServiceWorker newservice = new ServiceWorker();

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
				for(int mm = 1; mm < 39  ; mm++)
				{

					if(mm==1)
					{

						System.out.println("bywww");


						newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=1)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==2)
					{

						newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=2)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==3)
					{

						newservice = newservice.getCategory(getApplicationContext(), imei);
						if(newservice.flagExecutedServiceSuccesfully!=3)
						{
							serviceException=true;
							break;
						}

					}
					if(mm==4)
					{

						Date currDateNew = new Date();
						SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

						String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=4)
						{
							serviceException=true;
							break;
						}

					}
					if(mm==5)
					{

						Date currDateNew = new Date();
						SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

						String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
						newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=5)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==6)
					{

					*//*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.getAllPOSMaterialStructure(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*//*
					}
					if(mm==7)
					{



					*//*Date currDateNew = new Date();
					SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

					String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
					newservice = newservice.callGetLastVisitPOSDetails(getApplicationContext(), currSysDateNew, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=4)
					{
						serviceException=true;
						break;
					}*//*



					}
					if(mm==8)
					{
						newservice = newservice.getfnGetStoreWiseTarget(getApplicationContext(), fDate, imei, rID,RouteType);
					}
					if(mm==9)
					{

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
						if(newservice.flagExecutedServiceSuccesfully!=21)
						{
							serviceException=true;
							break;
						}

					}

					if(mm==22)
					{
						*//*newservice = newservice.getallPDAtblSyncSummuryDetails(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=22)
						{
							serviceException=true;
							break;
						}
						*//*
					}
					if(mm==23)
					{
						//newservice = newservice.getallPDAtblSyncSummuryForProductDetails(getApplicationContext(), fDate, imei, rID);
					}
					if(mm==24)
					{
					*//*newservice = newservice.GetSchemeCoupon(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=24)
					{
						serviceException="GetSchemeCoupon";
						break;
					}*//*
					}
					if(mm==25)
					{
				*//*	newservice = newservice.GetSchemeCouponSlab(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=25)
					{
						serviceException="GetSchemeCouponSlab";
						break;
					}*//*
					}
					if(mm==26)
					{
				*//*	newservice = newservice.GetDaySummaryNew(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=26)
					{
						serviceException="GetDaySummaryNew";
						break;
					}*//*
					}
					if(mm==27)
					{*//*
					newservice = newservice.GetOrderDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=27)
					{
						serviceException="GetOrderDetailsOnLastSalesSummary";
						break;
					}
					*//*}
					if(mm==28)
					{
				*//*	newservice = newservice.GetVisitDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID);
					if(newservice.flagExecutedServiceSuccesfully!=28)
					{
						serviceException="GetVisitDetailsOnLastSalesSummary";
						break;
					}*//*
					}
					if(mm==29)
					{
						newservice = newservice.GetLODDetailsOnLastSalesSummary(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=29)
						{
							serviceException=true;
							break;
						}
					}

					if(mm==31)
					{
						newservice = newservice.GetCallspForPDAGetLastVisitDate(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=31)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==32)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDate(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=32)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==33)
					{
						newservice = newservice.GetCallspForPDAGetLastVisitDetails(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=33)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==34)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDetails(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=34)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==35)
					{
						newservice = newservice.GetCallspForPDAGetLastOrderDetails_TotalValues(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=35)
						{
							serviceException=true;
							break;
						}
					}
					if(mm==36)
					{
						newservice = newservice.GetCallspForPDAGetExecutionSummary(getApplicationContext(), fDate, imei, rID,RouteType);
						if(newservice.flagExecutedServiceSuccesfully!=36)
						{
							serviceException=true;
							break;
						}
					}

					if(mm==37)
					{
						newservice = newservice.getQuotationDataFromServer(getApplicationContext(), fDate, imei, rID);
						if(newservice.flagExecutedServiceSuccesfully!=37)
						{
							serviceException=true;
							break;
						}
					}

					if(mm==38)
					{

						newservice=newservice.fnGetDistStockData(getApplicationContext(),imei);
						if(newservice.flagExecutedServiceSuccesfully!=38)
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

			Log.i("SvcMgr", "Service Execution cycle completed");

			try
			{
				if(pDialogGetStores.isShowing())
				{
					pDialogGetStores.dismiss();
				}
			}
			catch(Exception e)
			{

			}
			if(serviceException)
			{
				try
				{
					//but_GetStore.setEnabled(true);

					showAlertException(getResources().getString(R.string.txtError),getResources().getString(R.string.txtErrorRetrievingData));
				}

				catch(Exception e)
				{

				}
				dbengine.open();
				serviceException=false;
				dbengine.maintainPDADate();
				dbengine.dropRoutesTBL();
				dbengine.reCreateDB();

				dbengine.close();
			}
			else
			{
				shardPrefForCoverageArea(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);

				shardPrefForSalesman(slctdDSrSalesmanNodeId,slctdDSrSalesmanNodeType);

				flgDataScopeSharedPref(2);
				flgDSRSOSharedPref(2);
				//onCreate(new Bundle());

				if(dbengine.isDBOpen())
				{
					dbengine.close();
				}


				Intent storeIntent = new Intent(StoreSelection.this, LauncherActivity.class);
				storeIntent.putExtra("imei", imei);
				storeIntent.putExtra("userDate", userDate);
				storeIntent.putExtra("pickerDate", fDate);
				storeIntent.putExtra("rID", rID);


				startActivity(storeIntent);
				finish();



				*//*Intent storeIntent = new Intent(LauncherActivity.this, StoreSelection.class);
				storeIntent.putExtra("imei", imei);
				storeIntent.putExtra("userDate", currSysDate);
				storeIntent.putExtra("pickerDate", fDate);
				storeIntent.putExtra("rID", rID);


				startActivity(storeIntent);
				finish();*//*

			}

		}
	}*/

    public void shardPrefForCoverageArea(int coverageAreaNodeID,int coverageAreaNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("CoverageAreaNodeID", coverageAreaNodeID);
        editor.putInt("CoverageAreaNodeType", coverageAreaNodeType);


        editor.commit();

    }
    public void shardPrefForSalesman(int salesmanNodeId,int salesmanNodeType) {




        SharedPreferences.Editor editor = sharedPref.edit();


        editor.putInt("SalesmanNodeId", salesmanNodeId);
        editor.putInt("SalesmanNodeType", salesmanNodeType);

        editor.commit();

    }

    public void flgDataScopeSharedPref(int _flgDataScope)
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
    class GetStoresForDay extends AsyncTask<Void, Void, Void>
    {
        ServiceWorker newservice = new ServiceWorker();


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            mProgressDialog.show();



        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try {
                String RouteType="0";
                dbengine.open();
                String rID=dbengine.GetActiveRouteID();
                RouteType=dbengine.FetchRouteType(rID);
                dbengine.close();
                for(int mm = 1; mm < 10  ; mm++)
                {

                    if(mm==1)
                    {

                        if(isOnline())
                        {
                            newservice = newservice.getallProduct(getApplicationContext(), fDate, imei, rID,RouteType);
                            if(newservice.flagExecutedServiceSuccesfully!=2)
                            {
                                serviceException=true;
                                break;
                            }
                        }

                    }

                    if(mm==2)
                    {
                        if(isOnline())
                        {

                            Date currDateNew = new Date();
                            SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

                            String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
                            newservice = newservice.getAllNewSchemeStructure(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
                            if(newservice.flagExecutedServiceSuccesfully!=4)
                            {
                                serviceException=true;
                                break;
                            }

                        }

                    }
                    if(mm==3)
                    {

                        newservice = newservice.getCategory(getApplicationContext(), imei);
                        if(newservice.flagExecutedServiceSuccesfully!=3)
                        {

                            serviceException=true;
                            break;

                        }
                    }
                    if(mm==4)
                    {
                        Date currDateNew = new Date();
                        SimpleDateFormat currDateFormatNew = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

                        String currSysDateNew = currDateFormatNew.format(currDateNew).toString();
                        newservice = newservice.getallPDASchAppListForSecondPage(getApplicationContext(), currSysDateNew, imei, rID,RouteType);
                        if(newservice.flagExecutedServiceSuccesfully!=5)
                        {
                            serviceException=true;
                            break;
                        }
                    }

                    if(mm==5)
                    {

                        dbengine.open();
                        hmapStoreIdSstat=dbengine.checkForStoreIdSstat();
                        hmapStoreIdForDate=dbengine.checkForStoreIdForDate();
                        hmapStoreIdflgOrderType=dbengine.checkForStoreIdFlgOrderType();
                        dbengine.close();
                        newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(newservice.flagExecutedServiceSuccesfully!=1)
                        {
                            serviceException=true;
                            break;
                        }

                    }
                    if(mm==6)
                    {
						/*newservice = newservice.getStoreTypeMstr(getApplicationContext(), fDate, imei);
						if(newservice.flagExecutedServiceSuccesfully!=37)
						{

								serviceException=true;
								break;

						}*/

                    }
                    if(mm==7)
                    {
                        newservice = newservice.gettblTradeChannelMstr(getApplicationContext(), fDate, imei);
                        if(newservice.flagExecutedServiceSuccesfully!=38)
                        {
                            serviceException=true;
                            break;
                        }

                    }


                    if(mm==8)
                    {
						/*newservice = newservice.getStoreProductClassificationTypeListMstr(getApplicationContext(), fDate, imei);
						if(newservice.flagExecutedServiceSuccesfully!=39)
						{
							    serviceException=true;
								break;
						}*/


                    }

                    if(mm==9)
                    {
                        newservice = newservice.fnGetPDACollectionMaster(getApplicationContext(), fDate, imei, rID);
                        if(newservice.flagExecutedServiceSuccesfully!=40)
                        {
                            System.out.println("GRLTyre = "+mm);
                            serviceException=true;
                            break;
                        }
                    }




                }


            } catch (Exception e) {
                Log.i("SvcMgr", "Service Execution Failed!", e);

            }

            finally {

                Log.i("SvcMgr", "Service Execution Completed...");

            }
            //return newservice.director;
            return null;
        }

        @Override
        protected void onCancelled() {
            Log.i("SvcMgr", "Service Execution Cancelled");
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            Log.i("SvcMgr", "Service Execution cycle completed");

            if(mProgressDialog != null)
            {
                if(mProgressDialog.isShowing())
                {
                    mProgressDialog.dismiss();
                }
            }

            if(serviceException)
            {
                showAlertException(getResources().getString(R.string.txtError),getResources().getString(R.string.txtErrRetrieving));
                serviceException=false;
            }
            tl2.removeAllViews();
            setStoresList();

        }
    }

    public void setUpVariable()
    {



        Button btn_nearStores = (Button) findViewById(R.id.btn_nearStores);
        btn_nearStores.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
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
        });

        ImageView image_Notification = (ImageView) findViewById(R.id.image_Notification);
        image_Notification.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(SurveyStoreList.this, NotificationActivity.class);

                SurveyStoreList.this.startActivity(intent);
                finish();

            }
        });


		/* Button butn_refresh_data = (Button) findViewById(R.id.butn_refresh_data);
		 butn_refresh_data.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(isOnline())
				{

				System.out.println("Testing abcs ");
				AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(StoreSelection.this);

					// set title
				alertDialogBuilderNEw11.setTitle("Information");

					// set dialog message
				alertDialogBuilderNEw11.setMessage("Are you sure to refresh Data?");
				alertDialogBuilderNEw11.setCancelable(false);
				alertDialogBuilderNEw11.setPositiveButton("Ok",new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
								try
		    				    {
		    						new GetStoresForDay().execute();
		    						////System.out.println("SRVC-OK: "+ new GetStoresForDay().execute().get());
		    					} catch (Exception e) {
		    						// TODO Autouuid-generated catch block
		    						e.printStackTrace();
		    						//System.out.println("onGetStoresForDayCLICK: Exec(). EX: "+e);
		    					}

								//onCreate(new Bundle());
							}
						  });

				alertDialogBuilderNEw11.setNegativeButton(R.string.txtNo,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// System.out.println("value ofwhatTask after no button pressed by sunil"+whatTask);

										dialog.dismiss();
									}
								});

				alertDialogBuilderNEw11.setIcon(R.drawable.info_ico);
				AlertDialog alert121 = alertDialogBuilderNEw11.create();
				alert121.show();
				}
				else
				 {
					 showNoConnAlert();
					 return;

				 }

			}
		});
		 */

        Button add_new_store = (Button) findViewById(R.id.but_add_store);
        add_new_store.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
		/*		Intent intent = new Intent(StoreSelection.this, AddNewStore_DynamicSectionWiseSO.class);
				//Intent intent = new Intent(StoreSelection.this, Add_New_Store_NewFormat.class);
				//Intent intent = new Intent(StoreSelection.this, Add_New_Store.class);
				intent.putExtra("storeID", "0");
				intent.putExtra("activityFrom", "StoreSelection");
				intent.putExtra("userdate", userDate);
				intent.putExtra("pickerDate", pickerDate);
				intent.putExtra("imei", imei);
				intent.putExtra("rID", rID);
				StoreSelection.this.startActivity(intent);
				finish();
*/
					/*// TODO Auto-generated method stub
					Intent intent = new Intent(StoreSelection.this, Add_New_Store_DynamicSectionWise.class);
					intent.putExtra("activityFrom", "StoreSelection");
					intent.putExtra("userdate", userDate);
					intent.putExtra("pickerDate", pickerDate);
					intent.putExtra("imei", imei);
					intent.putExtra("rID", rID);
					StoreSelection.this.startActivity(intent);
					finish();
*/				}
        });
			/*Button but_SalesSummray = (Button) findViewById(R.id.btnSalesSummary);
			but_SalesSummray.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub


				// Intent intent = new Intent(StoreSelection.this, My_Summary.class);
				 Intent intent = new Intent(StoreSelection.this, DetailReport_Activity.class);
					intent.putExtra("imei", imei);
					intent.putExtra("userDate", userDate);
					intent.putExtra("pickerDate", pickerDate);
					StoreSelection.this.startActivity(intent);
					finish();

				}
			});

			Button but_day_end = (Button) findViewById(R.id.mainImg1);
			but_day_end.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					closeList = 0;
					valDayEndOrChangeRoute=1;

					if(isOnline())
					{

					}
					 else
					 {
						showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
						 return;

					 }

					dbengine.open();
					whereTo = "11";
					////System.out.println("Abhinav store Selection  Step 1");
						////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);
					StoreList2Procs = dbengine.ProcessStoreReq();
					////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);

					if (StoreList2Procs.length != 0) {
						//whereTo = "22";
						////System.out.println("Abhinav store Selection  Step 2");
						midPart();

						showPendingStorelist();

					} else if (dbengine.GetLeftStoresChk() == true)
					{
						////System.out.println("Abhinav store Selection  Step 7");
						//enableGPSifNot();
						// showChangeRouteConfirm();
						DayEnd();

					}

					else {
						DayEnd();
					}

					dbengine.close();

				}
			});*/



        Button btnActualVisit = (Button) findViewById(R.id.btnActualVisit);

        btnActualVisit.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                if (!selStoreID.isEmpty())
                {

                    long StartClickTime = System.currentTimeMillis();
                    Date dateobj1 = new Date(StartClickTime);
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                    String StartClickTimeFinal = df1.format(dateobj1);

                    CommonInfo.fileContent=imei+"_"+selStoreID+"_"+"Start Button Click on Store Selection "+StartClickTimeFinal;

                    File dirORIGimg = new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORYForText);
                    if (!dirORIGimg.exists())
                    {
                        dirORIGimg.mkdirs();
                    }

                    dbengine.updateflgOrderTypeIntblStoreList(selStoreID,1);

                    if(Selected_manager_Id!=-99)
                    {
                        String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

                        StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
                        dbengine.open();
                        int chk=dbengine.counttblSelectedManagerDetails();
                        dbengine.close();
                        if(chk==1)
                        {
                            dbengine.deletetblSelectedManagerDetails();
                            dbengine.open();
                            dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                    token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                    ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
                            dbengine.close();
                        }
                        else
                        {
                            dbengine.open();
                            dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                    token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                    ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
                            dbengine.close();
                        }
                    }
                    else if(Selected_manager_Id==-99)
                    {

                        if(TextUtils.isEmpty(ed_Street.getText().toString().trim()))
                        {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SurveyStoreList.this);
                            alertDialogBuilder.setTitle(getResources().getString(R.string.genTermNoDataConnection));
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setIcon(R.drawable.info_ico);

                            // set dialog message
                            alertDialogBuilder

                                    .setMessage(getResources().getString(R.string.txtEnterManagerName))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog,int id)
                                        {
                                            dialog.cancel();
                                        }
                                    });


                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            alertDialog.show();
                            return;

                        }
                        else
                        {
                            String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

                            StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
                            dbengine.open();
                            int chk=dbengine.counttblSelectedManagerDetails();
                            dbengine.close();
                            if(chk==1)
                            {
                                dbengine.deletetblSelectedManagerDetails();
                                dbengine.open();
                                dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                        token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                        ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
                                dbengine.close();
                            }
                            else
                            {
                                dbengine.open();
                                dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                        token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                        ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
                                dbengine.close();
                            }
                        }

                    }

                    whereTo = "11";

                    syncTIMESTAMP = System.currentTimeMillis();
                    Date dateobj = new Date(syncTIMESTAMP);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                    fullFileName1 = df.format(dateobj);

                    dbengine.open();
                    String checkClosrOrNext[] = dbengine.checkStoreCloseOrNextMethod(selStoreID);
                    dbengine.close();

                    StringTokenizer tokensInvoice = new StringTokenizer(String.valueOf(checkClosrOrNext[0]), "_");

                    int close = Integer.parseInt(tokensInvoice.nextToken().toString().trim());
                    int next = Integer.parseInt(tokensInvoice.nextToken().toString().trim());


                    if (close == 0 && next == 0)
                    {

                        if (!selStoreID.isEmpty())
                        {

							/*if(isMyServiceRunning())
		            		{

		            		}
							else
							{
								startService(new Intent(StoreSelection.this,GPSTrackerService.class));
							}
							*/
                            //startService(new Intent(StoreSelection.this,FusedTrackerService.class));
                            //System.out.println("Sun new Value 0");
                            int valSstatValueAgainstStore=0;
                            int ISNewStore=0;
                            int IsNewStoreDataCompleteSaved=0;
                            try
                            {
                                //dbengine.open();
                                ISNewStore =dbengine.fncheckStoreIsNewOrOld(selStoreID);
                                IsNewStoreDataCompleteSaved =dbengine.fncheckStoreIsNewStoreDataCompleteSaved(selStoreID);
                                valSstatValueAgainstStore=dbengine.fnGetStatValueagainstStore(selStoreID);
                            }catch(Exception e)
                            {

                            }finally
                            {
                                //dbengine.close();
                            }
						/*	String chID = ((dbengine
									.getChainIDBasedOnStoreID(selStoreID)) + "")
									.toString().trim();

							int pgFWDCLname2getID = dbengine
									.getFwdPgIdonNextBtnClick(selStoreID, "2", chID);
							FWDCLname = dbengine.getCustomPGid(pgFWDCLname2getID);

							int pgBCKCLname2getID = dbengine
									.getFwdPgIdonBackBtnClick(selStoreID, "2", chID);
							BCKCLname = dbengine.getCustomPGid(pgBCKCLname2getID);*/

                            //System.out.println("PREV. LOC CHK sop: "+ dbengine.PrevLocChk(selStoreID.trim()));

							/*if ((dbengine.PrevLocChk(selStoreID.trim()))
									|| locStat == 1)
							{*/
							 	/*dbengine.open();
							 	System.out.println("DtateTimeNitish3");
						        dbengine.UpdateStoreStartVisit(selStoreID, fullFileName1);
						        String passdLevel = battLevel+"%";
								dbengine.UpdateStoreVisitBatt(selStoreID, passdLevel);
						        dbengine.close();*/

                            if(ISNewStore==0)
                            {
                                //Code If Starts Here
								/*if(valSstatValueAgainstStore==1)
								{
									//Intent nxtP4 = new Intent(StoreSelection.this,ProductList.class);
									//ProductOrderSearch
									Intent nxtP4 = new Intent(StoreSelection.this,ProductOrderFilterSearch.class);
									nxtP4.putExtra("storeID", selStoreID);
									nxtP4.putExtra("SN", selStoreName);
									nxtP4.putExtra("imei", imei);
									nxtP4.putExtra("userdate", userDate);
									nxtP4.putExtra("pickerDate", pickerDate);
									startActivity(nxtP4);
									finish();
								}
								else
								{*/

                                long syncTIMESTAMP = System.currentTimeMillis();
                                Date dateobjNew = new Date(syncTIMESTAMP);
                                SimpleDateFormat dfnew = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                                String startTS = dfnew.format(dateobjNew);
                                dbengine.open();

                                dbengine.UpdateStoreStartVisit(selStoreID,startTS);
                                // dbengine.UpdateStoreEndVisit(selStoreID,
                                // fullFileName1);

                                //dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

                                String passdLevel = battLevel + "%";
                                dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

                                dbengine.UpdateStoreEndVisit(selStoreID,startTS);
                                dbengine.close();






                                Intent ready4GetLoc = new Intent(SurveyStoreList.this,LastVisitDetails.class);

                                //enableGPSifNot();


                                ready4GetLoc.putExtra("storeID", selStoreID);
                                ready4GetLoc.putExtra("selStoreName", selStoreName);
                                ready4GetLoc.putExtra("imei", imei);
                                ready4GetLoc.putExtra("userDate", userDate);
                                ready4GetLoc.putExtra("pickerDate", pickerDate);
                                ready4GetLoc.putExtra("startTS", fullFileName1);
                                ready4GetLoc.putExtra("bck", 0);

                                ready4GetLoc.putExtra("fromPage","SurveyStoreList");

                                startActivity(ready4GetLoc);
                                finish();
                                //}
                                //Code If Ends Here
                            }
                            else
                            {
                                //Code Else Starts Here

                                if(IsNewStoreDataCompleteSaved==1)
                                {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(SurveyStoreList.this, AddNewStore_DynamicSectionWiseSO.class);
                                    //Intent intent = new Intent(SurveyStoreList.this, Add_New_Store_NewFormat.class);
                                    //Intent intent = new Intent(SurveyStoreList.this, Add_New_Store.class);
                                    intent.putExtra("storeID",selStoreID);
                                    intent.putExtra("activityFrom", "SurveyStoreList");
                                    intent.putExtra("userdate", userDate);
                                    intent.putExtra("pickerDate", pickerDate);
                                    intent.putExtra("imei", imei);
                                    intent.putExtra("rID", rID);
                                    SurveyStoreList.this.startActivity(intent);
                                    finish();
                                }
                                else if(IsNewStoreDataCompleteSaved==0)
                                {
									/*if(valSstatValueAgainstStore==1)
									{
										//Intent nxtP4 = new Intent(SurveyStoreList.this,ProductList.class);
										//ProductOrderSearch
										Intent nxtP4 = new Intent(SurveyStoreList.this,ProductOrderFilterSearch.class);
										nxtP4.putExtra("storeID", selStoreID);
										nxtP4.putExtra("SN", selStoreName);
										nxtP4.putExtra("imei", imei);
										nxtP4.putExtra("userdate", userDate);
										nxtP4.putExtra("pickerDate", pickerDate);
										startActivity(nxtP4);
										finish();
									}
									else
									{*/

                                    long syncTIMESTAMP = System.currentTimeMillis();
                                    Date dateobjNew = new Date(syncTIMESTAMP);
                                    SimpleDateFormat dfnew = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                                    String startTS = dfnew.format(dateobjNew);
                                    dbengine.open();

                                    dbengine.UpdateStoreStartVisit(selStoreID,startTS);
                                    // dbengine.UpdateStoreEndVisit(selStoreID,
                                    // fullFileName1);

                                    //dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

                                    String passdLevel = battLevel + "%";
                                    dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

                                    dbengine.UpdateStoreEndVisit(selStoreID,startTS);
                                    dbengine.close();



                                    Intent ready4GetLoc = new Intent(SurveyStoreList.this,LastVisitDetails.class);

                                    //enableGPSifNot();


                                    ready4GetLoc.putExtra("storeID", selStoreID);
                                    ready4GetLoc.putExtra("selStoreName", selStoreName);
                                    ready4GetLoc.putExtra("imei", imei);
                                    ready4GetLoc.putExtra("userDate", userDate);
                                    ready4GetLoc.putExtra("pickerDate", pickerDate);
                                    ready4GetLoc.putExtra("startTS", fullFileName1);
                                    ready4GetLoc.putExtra("bck", 0);

                                    locStat = 0;


                                    startActivity(ready4GetLoc);
                                    finish();
                                    //}
                                }

                                //Code Else Ends Here
                            }







                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
                        }
                        // end else
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectDiffrentStore,Toast.LENGTH_SHORT).show();
                        showAlertSingleButtonInfo(getResources().getString(R.string.genTermPleaseSelectDiffrentStore));
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
                }

            }
        });













        Button btnStart = (Button) findViewById(R.id.btn_telephonic);

        btnStart.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                if (!selStoreID.isEmpty())
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SurveyStoreList.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(getText(R.string.ConfirmOrderType));

                    // Setting Dialog Message
                    alertDialog.setMessage(getText(R.string.AlertTelephonic));

                    alertDialog.setCancelable(false);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton(getText(R.string.txtProceed), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            long StartClickTime = System.currentTimeMillis();
                            Date dateobj1 = new Date(StartClickTime);
                            SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                            String StartClickTimeFinal = df1.format(dateobj1);

                            CommonInfo.fileContent=imei+"_"+selStoreID+"_"+"Start Button Click on Store Selection "+StartClickTimeFinal;

                            File dirORIGimg = new File(Environment.getExternalStorageDirectory(),DATASUBDIRECTORYForText);
                            if (!dirORIGimg.exists())
                            {
                                dirORIGimg.mkdirs();
                            }

                            dbengine.updateflgOrderTypeIntblStoreList(selStoreID,0);

                            if(Selected_manager_Id!=-99)
                            {
                                String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

                                StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
                                dbengine.open();
                                int chk=dbengine.counttblSelectedManagerDetails();
                                dbengine.close();
                                if(chk==1)
                                {
                                    dbengine.deletetblSelectedManagerDetails();
                                    dbengine.open();
                                    dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                            token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                            ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
                                    dbengine.close();
                                }
                                else
                                {
                                    dbengine.open();
                                    dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                            token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                            ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),"NA");
                                    dbengine.close();
                                }
                            }
                            else if(Selected_manager_Id==-99)
                            {

                                if(TextUtils.isEmpty(ed_Street.getText().toString().trim()))
                                {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SurveyStoreList.this);
                                    alertDialogBuilder.setTitle(getResources().getString(R.string.genTermNoDataConnection));
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setIcon(R.drawable.info_ico);

                                    // set dialog message
                                    alertDialogBuilder

                                            .setMessage(getResources().getString(R.string.txtEnterManagerName))
                                            .setCancelable(false)
                                            .setPositiveButton(getResources().getString(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
                                            {
                                                public void onClick(DialogInterface dialog,int id)
                                                {
                                                    dialog.cancel();
                                                }
                                            });


                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    alertDialog.show();
                                    return;

                                }
                                else
                                {
                                    String allData=dbengine.fetchtblManagerMstr(""+Selected_manager_Id);

                                    StringTokenizer token = new StringTokenizer(String.valueOf(allData), "^");
                                    dbengine.open();
                                    int chk=dbengine.counttblSelectedManagerDetails();
                                    dbengine.close();
                                    if(chk==1)
                                    {
                                        dbengine.deletetblSelectedManagerDetails();
                                        dbengine.open();
                                        dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                                token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                                ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
                                        dbengine.close();
                                    }
                                    else
                                    {
                                        dbengine.open();
                                        dbengine.savetblSelectedManagerDetails(imei,StartClickTimeFinal,token.nextToken().toString().trim(),
                                                token.nextToken().toString().trim(),token.nextToken().toString().trim(),token.nextToken().toString().trim()
                                                ,token.nextToken().toString().trim(),token.nextToken().toString().trim(),ed_Street.getText().toString().trim());
                                        dbengine.close();
                                    }
                                }

                            }

                            whereTo = "11";

                            syncTIMESTAMP = System.currentTimeMillis();
                            Date dateobj = new Date(syncTIMESTAMP);
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                            fullFileName1 = df.format(dateobj);

                            dbengine.open();
                            String checkClosrOrNext[] = dbengine.checkStoreCloseOrNextMethod(selStoreID);
                            dbengine.close();

                            StringTokenizer tokensInvoice = new StringTokenizer(String.valueOf(checkClosrOrNext[0]), "_");

                            int close = Integer.parseInt(tokensInvoice.nextToken().toString().trim());
                            int next = Integer.parseInt(tokensInvoice.nextToken().toString().trim());


                            if (close == 0 && next == 0)
                            {

                                if (!selStoreID.isEmpty())
                                {

							/*if(isMyServiceRunning())
		            		{

		            		}
							else
							{
								startService(new Intent(StoreSelection.this,GPSTrackerService.class));
							}
							*/
                                    //startService(new Intent(StoreSelection.this,FusedTrackerService.class));
                                    //System.out.println("Sun new Value 0");
                                    int valSstatValueAgainstStore=0;
                                    int ISNewStore=0;
                                    int IsNewStoreDataCompleteSaved=0;
                                    try
                                    {
                                        //dbengine.open();
                                        ISNewStore =dbengine.fncheckStoreIsNewOrOld(selStoreID);
                                        IsNewStoreDataCompleteSaved =dbengine.fncheckStoreIsNewStoreDataCompleteSaved(selStoreID);
                                        valSstatValueAgainstStore=dbengine.fnGetStatValueagainstStore(selStoreID);
                                    }catch(Exception e)
                                    {

                                    }finally
                                    {
                                        //dbengine.close();
                                    }
						/*	String chID = ((dbengine
									.getChainIDBasedOnStoreID(selStoreID)) + "")
									.toString().trim();

							int pgFWDCLname2getID = dbengine
									.getFwdPgIdonNextBtnClick(selStoreID, "2", chID);
							FWDCLname = dbengine.getCustomPGid(pgFWDCLname2getID);

							int pgBCKCLname2getID = dbengine
									.getFwdPgIdonBackBtnClick(selStoreID, "2", chID);
							BCKCLname = dbengine.getCustomPGid(pgBCKCLname2getID);*/

                                    //System.out.println("PREV. LOC CHK sop: "+ dbengine.PrevLocChk(selStoreID.trim()));

							/*if ((dbengine.PrevLocChk(selStoreID.trim()))
									|| locStat == 1)
							{*/
							 	/*dbengine.open();
							 	System.out.println("DtateTimeNitish3");
						        dbengine.UpdateStoreStartVisit(selStoreID, fullFileName1);
						        String passdLevel = battLevel+"%";
								dbengine.UpdateStoreVisitBatt(selStoreID, passdLevel);
						        dbengine.close();*/

                                    if(ISNewStore==0)
                                    {
                                        //Code If Starts Here
								/*if(valSstatValueAgainstStore==1)
								{
									//Intent nxtP4 = new Intent(StoreSelection.this,ProductList.class);
									//ProductOrderSearch
									Intent nxtP4 = new Intent(StoreSelection.this,ProductOrderFilterSearch.class);
									nxtP4.putExtra("storeID", selStoreID);
									nxtP4.putExtra("SN", selStoreName);
									nxtP4.putExtra("imei", imei);
									nxtP4.putExtra("userdate", userDate);
									nxtP4.putExtra("pickerDate", pickerDate);
									startActivity(nxtP4);
									finish();
								}
								else
								{*/

                                        long syncTIMESTAMP = System.currentTimeMillis();
                                        Date dateobjNew = new Date(syncTIMESTAMP);
                                        SimpleDateFormat dfnew = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                                        String startTS = dfnew.format(dateobjNew);
                                        dbengine.open();

                                        dbengine.UpdateStoreStartVisit(selStoreID,startTS);
                                        // dbengine.UpdateStoreEndVisit(selStoreID,
                                        // fullFileName1);

                                        //dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

                                        String passdLevel = battLevel + "%";
                                        dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

                                        dbengine.UpdateStoreEndVisit(selStoreID,startTS);
                                        dbengine.close();

                                        Intent nxtP4 = new Intent(SurveyStoreList.this,ProductOrderFilterSearch.class);
                                        //Intent nxtP4 = new Intent(LastVisitDetails.this,ProductOrderFilterSearch_RecycleView.class);
                                        nxtP4.putExtra("storeID", selStoreID);
                                        nxtP4.putExtra("SN", selStoreName);
                                        nxtP4.putExtra("imei", imei);
                                        nxtP4.putExtra("userdate", userDate);
                                        nxtP4.putExtra("pickerDate", pickerDate);
                                        nxtP4.putExtra("flgOrderType", 0);
                                        nxtP4.putExtra("fromPage","SurveyStoreList");

                                        locStat = 0;
                                        startActivity(nxtP4);
                                        finish();
									/*Intent ready4GetLoc = new Intent(SurveyStoreList.this,LastVisitDetails.class);

									//enableGPSifNot();


									ready4GetLoc.putExtra("storeID", selStoreID);
									ready4GetLoc.putExtra("selStoreName", selStoreName);
									ready4GetLoc.putExtra("imei", imei);
									ready4GetLoc.putExtra("userDate", userDate);
									ready4GetLoc.putExtra("pickerDate", pickerDate);
									ready4GetLoc.putExtra("startTS", fullFileName1);
									ready4GetLoc.putExtra("bck", 0);

									locStat = 0;


									startActivity(ready4GetLoc);
									finish();*/
                                        //}
                                        //Code If Ends Here
                                    }
                                    else
                                    {
                                        //Code Else Starts Here

                                        if(IsNewStoreDataCompleteSaved==1)
                                        {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(SurveyStoreList.this, AddNewStore_DynamicSectionWiseSO.class);
                                            //Intent intent = new Intent(StoreSelection.this, Add_New_Store_NewFormat.class);
                                            //Intent intent = new Intent(StoreSelection.this, Add_New_Store.class);
                                            intent.putExtra("storeID",selStoreID);
                                            intent.putExtra("activityFrom", "SurveyStoreList");
                                            intent.putExtra("userdate", userDate);
                                            intent.putExtra("pickerDate", pickerDate);
                                            intent.putExtra("imei", imei);
                                            intent.putExtra("rID", rID);
                                            SurveyStoreList.this.startActivity(intent);
                                            finish();
                                        }
                                        else if(IsNewStoreDataCompleteSaved==0)
                                        {
									/*if(valSstatValueAgainstStore==1)
									{
										//Intent nxtP4 = new Intent(StoreSelection.this,ProductList.class);
										//ProductOrderSearch
										Intent nxtP4 = new Intent(StoreSelection.this,ProductOrderFilterSearch.class);
										nxtP4.putExtra("storeID", selStoreID);
										nxtP4.putExtra("SN", selStoreName);
										nxtP4.putExtra("imei", imei);
										nxtP4.putExtra("userdate", userDate);
										nxtP4.putExtra("pickerDate", pickerDate);
										startActivity(nxtP4);
										finish();
									}
									else
									{*/

                                            long syncTIMESTAMP = System.currentTimeMillis();
                                            Date dateobjNew = new Date(syncTIMESTAMP);
                                            SimpleDateFormat dfnew = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                                            String startTS = dfnew.format(dateobjNew);
                                            dbengine.open();

                                            dbengine.UpdateStoreStartVisit(selStoreID,startTS);
                                            // dbengine.UpdateStoreEndVisit(selStoreID,
                                            // fullFileName1);

                                            //dbengine.UpdateStoreActualLatLongi(selStoreID,"" + "0.00", "" + "0.00", "" + "0.00","" + "NA");

                                            String passdLevel = battLevel + "%";
                                            dbengine.UpdateStoreVisitBatt(selStoreID,passdLevel);

                                            dbengine.UpdateStoreEndVisit(selStoreID,startTS);
                                            dbengine.close();
                                            Intent ready4GetLoc = new Intent(SurveyStoreList.this,ProductOrderFilterSearch.class);

                                            //enableGPSifNot();


                                            ready4GetLoc.putExtra("storeID", selStoreID);
                                            ready4GetLoc.putExtra("selStoreName", selStoreName);
                                            ready4GetLoc.putExtra("imei", imei);
                                            ready4GetLoc.putExtra("userDate", userDate);
                                            ready4GetLoc.putExtra("pickerDate", pickerDate);
                                            ready4GetLoc.putExtra("startTS", fullFileName1);
                                            ready4GetLoc.putExtra("bck", 0);
                                            ready4GetLoc.putExtra("flgOrderType", 0);
                                            ready4GetLoc.putExtra("fromPage","SurveyStoreList");


                                            locStat = 0;


                                            startActivity(ready4GetLoc);
                                            finish();
                                            //}
                                        }

                                        //Code Else Ends Here
                                    }







                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
                                }
                                // end else
                            }
                            else
                            {
                                //Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectDiffrentStore,Toast.LENGTH_SHORT).show();
                                showAlertSingleButtonInfo(getResources().getString(R.string.genTermPleaseSelectDiffrentStore));
                            }

                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton(getText(R.string.AlertDialogCancelButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dbengine.updateflgOrderTypeIntblStoreList(selStoreID,-1);

                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.genTermPleaseSelectStore, Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
public void backButtonFuntion(){

    backIcon=(ImageView) findViewById(R.id.backIcon);
    backIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent     intent=null;
            if(sharedPrefForSurvey.contains("FROM")){
                if( sharedPrefForSurvey.getString("FROM", "").equals("DASHBOARD")){
                         intent=new Intent(SurveyStoreList.this,AllButtonActivity.class);
                }
                else if( sharedPrefForSurvey.getString("FROM", "").equals("STORESELECTION")){
                        intent=new Intent(SurveyStoreList.this,StoreSelection.class);
                    intent.putExtra("imei", imei);
                    intent.putExtra("userDate", userDate);
                    intent.putExtra("pickerDate", pickerDate);
                }
                else {
                    intent=new Intent(SurveyStoreList.this,AllButtonActivity.class);
                }

            }
            else{
                intent=new Intent(SurveyStoreList.this,AllButtonActivity.class);
            }

            startActivity(intent);
            finish();
        }
    });
}
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_storelist);
        hmapTestSubmitOrNot=dbengine.fngetSubmittedSurvey();
        sharedPrefForSurvey=getSharedPreferences("SurveyPref", MODE_PRIVATE);

        inputSearch           =	 (EditText) findViewById(R.id.inputSearch);
        listDistributor      = (ListView) findViewById(R.id.list_view);
        backButtonFuntion();


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

        tl2 = (TableLayout) findViewById(R.id.dynprodtable);
        Intent getStorei = getIntent();
        if(getStorei !=null)
        {
            imei = getStorei.getStringExtra("imei").trim();
            userDate = getStorei.getStringExtra("userDate");
            pickerDate = getStorei.getStringExtra("pickerDate").trim();

        }

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        relativeLayout1=(RelativeLayout) findViewById(R.id.relativeLayout1);

        dbengine.open();

        rID= dbengine.GetActiveRouteIDCrntDSR(CommonInfo.CoverageAreaNodeID,CommonInfo.CoverageAreaNodeType);
        if(rID.equals("0"))
        {
            rID=dbengine.GetNotActiveRouteID();
        }
        dbengine.updateActiveRoute(rID, 1);
        dbengine.close();
        mProgressDialog = new ProgressDialog(SurveyStoreList.this);
        mProgressDialog.setTitle(getResources().getString(R.string.genTermPleaseWaitNew));
        mProgressDialog.setMessage(getResources().getString(R.string.txtRefreshingData));

        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
		/*
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = tManager.getDeviceId();

		if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
		{
			imei = tManager.getDeviceId();
			CommonInfo.imei=imei;
		}
		else
		{
			imei=CommonInfo.imei.trim();
		}*/

        try {
            getDSRDetail();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Date date1=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        passDate = sdf.format(date1).toString();



        fDate = passDate.trim().toString();

        img_side_popUp=(ImageView) findViewById(R.id.img_side_popUp);
        img_side_popUp.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                open_pop_up();
            }
        });




        getManagersDetail();
        getRouteDetail();

        spinner_manager = (Spinner)findViewById(R.id.spinner_manager);
        ArrayAdapter adapterCategory=new ArrayAdapter(this, android.R.layout.simple_spinner_item,Manager_names);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_manager.setAdapter(adapterCategory);

        spinner_RouteList = (Spinner)findViewById(R.id.spinner_RouteList);
        ArrayAdapter adapterRouteList=new ArrayAdapter(this, android.R.layout.simple_spinner_item,Route_names);
        adapterRouteList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RouteList.setAdapter(adapterRouteList);

        spinner_RouteList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                seleted_routeIDType=hmapRouteIdNameDetails.get(arg0.getItemAtPosition(arg2).toString());
                dbengine.open();
                dbengine.fnSetAllRouteActiveStatus();
                dbengine.updateActiveRoute(seleted_routeIDType.split(Pattern.quote("_"))[0], 1);
                dbengine.close();
                rID=seleted_routeIDType.split(Pattern.quote("_"))[0];

                try {
                    //fnCreateStoreListOnLoad();

                    tl2.removeAllViews();
                    setStoresList();
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        rl_for_other = (RelativeLayout) findViewById(R.id.rl_for_other);
        rl_for_other.setVisibility(RelativeLayout.GONE);

        ed_Street=(EditText)findViewById(R.id.streetid);

        spinner_manager.setOnItemSelectedListener(new OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {

                selected_manager=arg0.getItemAtPosition(arg2).toString();

                String ManagerID=hmapManagerNameManagerIdDetails.get(selected_manager);


                if(ManagerID.equals("0"))
                {
                    rl_for_other.setVisibility(RelativeLayout.GONE);
                    ed_Street.setText("");
                    Selected_manager_Id=0;

                }
                else if(ManagerID.equals("-99"))
                {
                    Selected_manager_Id=-99;
                    rl_for_other.setVisibility(RelativeLayout.VISIBLE);
                }
                else
                {
                    Selected_manager_Id=Integer.parseInt(ManagerID);

                    ed_Street.setText("");

                    rl_for_other.setVisibility(RelativeLayout.GONE);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

                //selected_location=arg0.getItemAtPosition(0).toString();
                //System.out.println("selected_location in resume1111111111 "+selected_location);
            }
        });


        dbengine.open();
        int chk=dbengine.counttblSelectedManagerDetails();
        dbengine.close();
        if(chk==1)
        {
            String abcd=dbengine.Fetch_tblSelectedManagerDetails();

            StringTokenizer tokens = new StringTokenizer(String.valueOf(abcd), "_");

            String as=tokens.nextToken().toString().trim();


            String ManagerName = tokens.nextToken().toString().trim();

            int ManagerID =  Integer.parseInt(as);

            int selected_choice_index=0;

            if(ManagerID==0)
            {
                spinner_manager.setSelection(0);

            }
            else if(ManagerID!=-99)
            {
                for(int i1=0;i1<Manager_names.length;i1++)
                {
                    if(Manager_names[i1].equals(ManagerName))
                    {
                        selected_choice_index=i1;
                    }
                }
                spinner_manager.setSelection(selected_choice_index);

            }

            else
            {
                for(int i1=0;i1<Manager_names.length;i1++)
                {
                    if(Manager_names[i1].equals(ManagerName))
                    {
                        selected_choice_index=i1;
                    }
                }
                spinner_manager.setSelection(selected_choice_index);

                dbengine.open();
                String OtherName=dbengine.fetchOtherNameBasicOfManagerID(ManagerID);
                dbengine.close();
                rl_for_other.setVisibility(RelativeLayout.VISIBLE);
                ed_Street.setText(OtherName);



            }
        }




        setUpVariable();


        //
        String routeNametobeSelectedInSpinner=dbengine.GetActiveRouteDescrBasedCoverageIDandNodeTyep();
        //String routeNametobeSelectedInSpinner=dbengine.GetActiveRouteDescr();
        int index=0;
        if(hmapRouteIdNameDetails!=null)
        {

            Set set2 = hmapRouteIdNameDetails.entrySet();
            Iterator iterator = set2.iterator();
            Boolean isrouteSelected=false;

            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                if(routeNametobeSelectedInSpinner.trim().equals(me2.getKey().toString().trim()))
                {
                    isrouteSelected=true;
                    break;
                }
                index=index+1;
            }
            if(isrouteSelected)
            {
                spinner_RouteList.setSelection(index);
            }
            else
            {
                spinner_RouteList.setSelection(0);
            }

        }

        setStoresList();



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


    public void setStoresList()
    {

        dbengine.open();



        storeList = dbengine.FetchStoreListAllRoute();
     /*   storeRouteIdType=dbengine.FetchStoreRouteIdType(rID);
        storeStatus = dbengine.FetchStoreStatus(rID);

        storeCloseStatus = dbengine.FetchStoreStoreCloseStatus(rID);

        storeNextDayStatus = dbengine.FetchStoreStoreNextDayStatus();
        StoreflgSubmitFromQuotation= dbengine.FetchStoreStatusflgSubmitFromQuotation();
        hmapStoreLatLongDistanceFlgRemap=dbengine.fnGeStoreList(CommonInfo.DistanceRange);*/
        dbengine.close();

        storeCode = new String[storeList.length];
        storeName = new String[storeList.length];
        hmapStoreNameID.clear();
        for (int splitval = 0; splitval <= (storeList.length - 1); splitval++)
        {
            StringTokenizer tokens = new StringTokenizer(String.valueOf(storeList[splitval]), "_");

            storeCode[splitval] = tokens.nextToken().trim();

            storeName[splitval] = tokens.nextToken().trim();
            hmapStoreNameID.put(storeName[splitval],storeCode[splitval]);


        }
        listStoreId=new ArrayList<String>(Arrays.asList(storeCode));
        listItem=new ArrayList<String>(Arrays.asList(storeName));
        adapterCusto=new ShivAdapter(getApplicationContext(), listItem);

           // adapterDistributor = new ArrayAdapter<String>(SurveyStoreList.this, R.layout.survey_list_row, R.id.product_name, storeName);
        //listDistributor.setAdapter(adapterDistributor);
        listDistributor.setAdapter(adapterCusto);


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
               // adapterDistributor.getFilter().filter(arg0.toString().trim());

                String text = arg0.toString().toLowerCase(Locale.ENGLISH);
                adapterCusto.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        listDistributor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String abc=listDistributor.getItemAtPosition(arg2).toString().trim();
                inputSearch.setText("");
                if(hmapStoreNameID.containsKey(abc)){
                 String storeID=   hmapStoreNameID.get(abc);
                    Intent intent=new Intent(SurveyStoreList.this,SurveyActivity.class);
                    intent.putExtra("imei", imei);
                    intent.putExtra("userDate", userDate);
                    intent.putExtra("pickerDate", pickerDate);
                    intent.putExtra("StoreID",storeID);
                    intent.putExtra("StoreName",abc);
                    startActivity(intent);
                }



            }
        });
    }

    private void showDialogButtonClick() {

        dbengine.open();
        route_name = dbengine.fnGetRouteInfoForDDL();
        route_name_id = dbengine.fnGetRouteIdsForDDL();
        dbengine.close();


        ContextThemeWrapper cw= new ContextThemeWrapper(this,R.style.AlertDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle(R.string.genTermAvailableRoute);

        //final CharSequence[] choiceList ={"MR-Monday", "MR-Tuesday" ,"MR-Wednesday" ,"MR-Thursday","MR-Friday","MR-Saturday","MR-Sunday" };

        builder.setSingleChoiceItems(
                route_name,
                selected,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        selected = which;
                        temp_select_routename=route_name[selected];
                        temp_select_routeid=route_name_id[selected];
                        rID=temp_select_routeid;

                    }
                })
                .setCancelable(false)
                .setPositiveButton(R.string.AlertDialogOkButton,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //not the same as 'which' above
                                // Log.d(TAG,"Which value="+which);
                                // Log.d(TAG,"Selected value="+selected);
                                // Toast.makeText(LauncherActivity.this,"Select "+route_name[selected],Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                //	tv_Route.setText(""+temp_select_routename);
                                selected_route_id=temp_select_routeid;
                                rID=selected_route_id;


                                dbengine.open();
                                int checkRouteIDExistOrNot=dbengine.checkRouteIDExistInStoreListTable(Integer.parseInt(rID));
                                dbengine.close();

                                if(checkRouteIDExistOrNot==0)
                                {
                                    GetDataForChangeRoute task = new GetDataForChangeRoute(SurveyStoreList.this);
                                    task.execute();
                                }
                                else
                                {

                                }


                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        try
        {

            dbengine.open();
            String Noti_textWithMsgServerID=dbengine.fetchNoti_textFromtblNotificationMstr();
            dbengine.close();
            System.out.println("Sunil Tty Noti_textWithMsgServerID :"+Noti_textWithMsgServerID);
            if(!Noti_textWithMsgServerID.equals("Null"))
            {
                StringTokenizer token = new StringTokenizer(String.valueOf(Noti_textWithMsgServerID), "_");

                MsgServerID= Integer.parseInt(token.nextToken().trim());
                Noti_text= token.nextToken().trim();


                if(Noti_text.equals("") || Noti_text.equals("Null"))
                {

                }
                else
                {



                    final AlertDialog builder = new AlertDialog.Builder(SurveyStoreList.this).create();


                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View openDialog = inflater.inflate(R.layout.custom_dialog, null);
                    openDialog.setBackgroundColor(Color.parseColor("#ffffff"));

                    builder.setCancelable(false);
                    TextView header_text=(TextView)openDialog. findViewById(R.id.txt_header);
                    final TextView msg=(TextView)openDialog. findViewById(R.id.msg);

                    final Button ok_but=(Button)openDialog. findViewById(R.id.but_yes);
                    final Button cancel=(Button)openDialog. findViewById(R.id.but_no);

                    cancel.setVisibility(View.GONE);
                    header_text.setText(getResources().getString(R.string.AlertDialogHeaderMsg));
                    msg.setText(Noti_text);

                    ok_but.setText(getResources().getString(R.string.AlertDialogOkButton));

                    builder.setView(openDialog,0,0,0,0);

                    ok_but.setOnClickListener(new OnClickListener()
                    {

                        @Override
                        public void onClick(View arg0)
                        {
                            // TODO Auto-generated method stub

                            long syncTIMESTAMP = System.currentTimeMillis();
                            Date dateobj = new Date(syncTIMESTAMP);
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
                            String Noti_ReadDateTime = df.format(dateobj);

                            dbengine.open();
                            dbengine.updatetblNotificationMstr(MsgServerID,Noti_text,0,Noti_ReadDateTime,3);
                            dbengine.close();

                            try
                            {
                                dbengine.open();
                                int checkleftNoti=dbengine.countNumberOFNotificationtblNotificationMstr();
                                if(checkleftNoti>0)
                                {
                                    String Noti_textWithMsgServerID=dbengine.fetchNoti_textFromtblNotificationMstr();
                                    System.out.println("Sunil Tty Noti_textWithMsgServerID :"+Noti_textWithMsgServerID);
                                    if(!Noti_textWithMsgServerID.equals("Null"))
                                    {
                                        StringTokenizer token = new StringTokenizer(String.valueOf(Noti_textWithMsgServerID), "_");

                                        MsgServerID= Integer.parseInt(token.nextToken().trim());
                                        Noti_text= token.nextToken().trim();

                                        dbengine.close();
                                        if(Noti_text.equals("") || Noti_text.equals("Null"))
                                        {

                                        }
                                        else
                                        {
                                            msg.setText(Noti_text);
                                        }
                                    }

                                }
                                else
                                {
                                    builder.dismiss();
                                }

                            }
                            catch(Exception e)
                            {

                            }
                            finally
                            {
                                dbengine.close();

                            }


                        }
                    });




                    builder.show();






                }
            }
        }
        catch(Exception e)
        {

        }

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
        {
            imei = tManager.getDeviceId();
            CommonInfo.imei=imei;
        }
        else
        {
            imei=CommonInfo.imei.trim();
        }


        dbengine.open();
        String allLoctionDetails=  dbengine.getLocationDetails();
        dbengine.close();
        if(allLoctionDetails.equals("0"))
        {
            firstTimeLocationTrack();
        }



    }



	/*void open_pop_up()
	{
		Intent i=new Intent(StoreSelection.this,DialogActivity_MenuBar.class);
		i.putExtra("userdate", userDate);
		i.putExtra("pickerDate", pickerDate);
		i.putExtra("imei", imei);
		startActivity(i);
	}*/


    void openDSRTrackerAlert()
    {
        final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(SurveyStoreList.this);
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
                    Intent i=new Intent(SurveyStoreList.this,WebViewDSRDataReportActivity.class);
                    startActivity(i);

                }
                else if(rb_onMap.isChecked())
                {
                    Intent i = new Intent(SurveyStoreList.this, WebViewDSRTrackerActivity.class);
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


    void openMarketVisitAlert()
    {
        final android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(SurveyStoreList.this);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.market_visit_alert, null);
        alert.setView(view);

        alert.setCancelable(false);

        TextView txt_header=(TextView)view.findViewById(R.id.txt_HdrVst);
        txt_header.setText("Change DSM");
        final RadioButton rb_myVisit= (RadioButton) view.findViewById(R.id.rb_myVisit);
        rb_myVisit.setVisibility(View.GONE);
        final RadioButton rb_dsrVisit= (RadioButton) view.findViewById(R.id.rb_dsrVisit);

        final RadioButton rb_jointWorking= (RadioButton) view.findViewById(R.id.rb_jointWorking);
        rb_jointWorking.setVisibility(View.GONE);
        final Spinner spinner_dsrVisit= (Spinner) view.findViewById(R.id.spinner_dsrVisit);
        final Spinner spinner_jointWorking= (Spinner) view.findViewById(R.id.spinner_jointWorking);
        Button btn_proceed= (Button) view.findViewById(R.id.btn_proceed);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

        final android.support.v7.app.AlertDialog dialog=alert.create();
        dialog.show();

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_proceed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();



                if(!SelectedDSRValue.equals("") && !SelectedDSRValue.equals("Select DSR") && !SelectedDSRValue.equals("No DSR") )
                {

                    String DSRNodeIdAndNodeType= dbengine.fnGetDSRNodeIdAndNodeType(SelectedDSRValue);
                    slctdCoverageAreaNodeID=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    slctdCoverageAreaNodeType=Integer.parseInt(DSRNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                    CommonInfo.CoverageAreaNodeID=slctdCoverageAreaNodeID;
                    CommonInfo.CoverageAreaNodeType=slctdCoverageAreaNodeType;
                    CommonInfo.FlgDSRSO=2;

                    String DSRPersonNodeIdAndNodeType= dbengine.fnGetDSRPersonNodeIdAndNodeType(SelectedDSRValue);
                    slctdDSrSalesmanNodeId=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[0]);
                    slctdDSrSalesmanNodeType=Integer.parseInt(DSRPersonNodeIdAndNodeType.split(Pattern.quote("^"))[1]);

                    dbengine.open();

                    rID= dbengine.GetActiveRouteIDCrntDSR(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);
                    dbengine.close();


                    if(rID.equals("0"))
                    {

                    }

                    if(dbengine.isDataAlreadyExist(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType))
                    {
                        shardPrefForCoverageArea(slctdCoverageAreaNodeID,slctdCoverageAreaNodeType);

                        shardPrefForSalesman(slctdDSrSalesmanNodeId,slctdDSrSalesmanNodeType);

                        flgDataScopeSharedPref(2);
                        flgDSRSOSharedPref(2);
                        Intent intent=new Intent(SurveyStoreList.this,SurveyStoreList.class);
                        intent.putExtra("imei", imei);
                        intent.putExtra("userDate", userDate);
                        intent.putExtra("pickerDate", fDate);
                        intent.putExtra("rID", rID);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if(dbengine.isDBOpen())
                        {
                            dbengine.close();
                        }


                        new GetStoresForDay().execute();
                    }



                }
                else
                {
                    showAlertForEveryOne("Please select DSR to Proceeds.");
                }

            }
        });



        rb_dsrVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(rb_dsrVisit.isChecked())
                {
                    rb_myVisit.setChecked(false);
                    rb_jointWorking.setChecked(false);
                    ArrayAdapter adapterCategory=new ArrayAdapter(SurveyStoreList.this, android.R.layout.simple_spinner_item,drsNames);
                    adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_dsrVisit.setAdapter(adapterCategory);
                    spinner_dsrVisit.setVisibility(View.VISIBLE);

                    spinner_dsrVisit.setOnItemSelectedListener(new OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                        {
                            // TODO Auto-generated method stub
                            SelectedDSRValue = spinner_dsrVisit.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0)
                        {
                            // TODO Auto-generated method stub

                        }
                    });

                }
            }
        });

        rb_dsrVisit.setChecked(true);
        dialog.show();
    }

    public void showAlertForEveryOne(String msg)
    {
        //AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(LauncherActivity.this, R.style.Dialog));
        android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(SurveyStoreList.this);

        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //finish();
            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        android.support.v7.app.AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }
    protected void open_pop_up()
    {
        dialog = new Dialog(SurveyStoreList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selection_header_custom);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
        WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.height=parms.MATCH_PARENT;
        parms.dimAmount = (float) 0.5;


        Button btnDSMWiseReport= (Button) dialog.findViewById(R.id.btnDSMWiseReport);
        btnDSMWiseReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

                Intent i=new Intent(SurveyStoreList.this,WebViewDSMWiseReport.class);
                startActivity(i);

            }
        });

        final Button btnManageDSR = (Button) dialog.findViewById(R.id.btnManageDSR);
        btnManageDSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(SurveyStoreList.this,WebViewManageDSRActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        final   Button butn_Change_dsr = (Button) dialog.findViewById(R.id.butn_Change_dsr);
        // butn_Change_dsr.setVisibility(View.GONE);
        butn_Change_dsr.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                dialog.dismiss();
                Intent storeIntent = new Intent(SurveyStoreList.this, DialogActivity_MarketVisit.class);
                storeIntent.putExtra("PageFrom", "1");
                storeIntent.putExtra("imei", imei);
                storeIntent.putExtra("FROM", "SURVEY");


                startActivity(storeIntent);
            }
        });

        final   Button butn_PurchaseOrder = (Button) dialog.findViewById(R.id.butn_PurchaseOrder);
        butn_PurchaseOrder.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(isOnline())
                {
                    Intent intent=new Intent(SurveyStoreList.this,WebViewPurchaseOrderActivity.class);
                    startActivity(intent);

                }
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                }
            }
        });


        final   Button butn_refresh_data = (Button) dialog.findViewById(R.id.butn_refresh_data);
        final  Button but_day_end = (Button) dialog.findViewById(R.id.mainImg1);
        final  Button changeRoute = (Button) dialog.findViewById(R.id.changeRoute);
        changeRoute.setVisibility(View.GONE);
        final  Button btnewAddedStore = (Button) dialog.findViewById(R.id.btnewAddedStore);


        final   Button btnstoreMapping = (Button) dialog.findViewById(R.id.btnstoreMapping);
        btnstoreMapping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GetStoreAllData getStoreAllDataAsync= new GetStoreAllData();
                getStoreAllDataAsync.execute();
            }
        });

        final   Button btndistrbtrStock = (Button) dialog.findViewById(R.id.btndistrbtrStock);
        btndistrbtrStock.setOnClickListener(new OnClickListener() {
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

                Intent i=new Intent(SurveyStoreList.this,DistributorEntryActivity.class);
                i.putExtra("imei", imei);
                i.putExtra("CstmrNodeId", CstmrNodeId);
                i.putExtra("CstomrNodeType", CstomrNodeType);
                i.putExtra("fDate", fDate);
                startActivity(i);
                finish();
            }
        });

        final   Button btndsrTracker = (Button) dialog.findViewById(R.id.btndsrTracker);
        btndsrTracker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openDSRTrackerAlert();
            }
        });

        final   Button btnMapDistributor = (Button) dialog.findViewById(R.id.btnMapDistributor);
        btnMapDistributor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(SurveyStoreList.this,DistributorMapActivity.class);
                startActivity(intent);
            }
        });





        final  Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
        btnExecution.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try {
                    GetInvoiceForDay task = new GetInvoiceForDay(SurveyStoreList.this);
                    task.execute();
                }
                catch (Exception e)
                {

                }
            }
        });


        final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
        butHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(SurveyStoreList.this,AllButtonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button btnTargetVsAchieved=(Button) dialog.findViewById(R.id.btnTargetVsAchieved);

        btnTargetVsAchieved.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SurveyStoreList.this, TargetVsAchievedActivity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("Pagefrom", "1");
                //intent.putExtra("back", "0");
                startActivity(intent);
                finish();


            }
        });


        btnewAddedStore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SurveyStoreList.this, ViewAddedStore.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                //intent.putExtra("back", "0");
                startActivity(intent);
                finish();

            }
        });
        final Button btnVersion = (Button) dialog.findViewById(R.id.btnVersion);
        btnVersion.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                btnVersion.setBackgroundColor(Color.GREEN);
                dialog.dismiss();
            }
        });

        dbengine.open();
        String ApplicationVersion=dbengine.AppVersionID;
        dbengine.close();
        btnVersion.setText(getResources().getString(R.string.VersionNo)+ApplicationVersion);

        // Version No-V12

        final Button but_SalesSummray = (Button) dialog.findViewById(R.id.btnSalesSummary);
        but_SalesSummray.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                but_SalesSummray.setBackgroundColor(Color.GREEN);
                dialog.dismiss();

                SharedPreferences sharedPrefReport = getSharedPreferences("Report", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefReport .edit();
                editor.putString("fromPage", "2");
                editor.commit();

                Intent intent = new Intent(SurveyStoreList.this, DetailReportSummaryActivity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");
                intent.putExtra("fromPage","SurveyStoreList");
                startActivity(intent);
                finish();

            }
        });


        final Button btnChangeLanguage = (Button) dialog.findViewById(R.id.btnChangeLanguage);
        btnChangeLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Dialog dialogLanguage = new Dialog(SurveyStoreList.this);
                dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLanguage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

                dialogLanguage.setCancelable(false);
                dialogLanguage.setContentView(R.layout.language_popup);

                TextView textviewEnglish=(TextView) dialogLanguage.findViewById(R.id.textviewEnglish);
                TextView textviewHindi=(TextView) dialogLanguage.findViewById(R.id.textviewHindi);

                textviewEnglish.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        dialogLanguage.dismiss();
                        setLanguage("en");
                    }
                });
                textviewHindi.setOnClickListener(new OnClickListener()
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




        final Button btnCheckTodayOrder = (Button) dialog.findViewById(R.id.btnCheckTodayOrder);
        btnCheckTodayOrder.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                but_SalesSummray.setBackgroundColor(Color.GREEN);
                dialog.dismiss();
                Intent intent = new Intent(SurveyStoreList.this, CheckDatabaseData.class);
                // Intent intent = new Intent(SurveyStoreList.this, DetailReport_Activity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");
                startActivity(intent);
                finish();

            }
        });



        but_day_end.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //checking that dsr fill registration form or not for flag 0
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
                    AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(SurveyStoreList.this);
                    alertDialogNoConn.setTitle(getResources().getString(R.string.genTermNoDataConnection));
                    alertDialogNoConn.setMessage(getResources().getString(R.string.Dsrmessage));
                    alertDialogNoConn.setCancelable(false);
                    alertDialogNoConn.setNeutralButton(getResources().getString(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            Intent intent=new Intent(SurveyStoreList.this,DSR_Registration.class);
                            intent.putExtra("IntentFrom", "DAYEND");
                            intent.putExtra("imei", imei);
                            intent.putExtra("userDate", userDate);
                            intent.putExtra("pickerDate", pickerDate);

                            startActivity(intent);
                            finish();

                        }
                    });
                    alertDialogNoConn.setIcon(R.drawable.info_ico);
                    AlertDialog alert = alertDialogNoConn.create();
                    alert.show();

                }
                else{
                    but_day_end.setBackgroundColor(Color.GREEN);
                    closeList = 0;
                    valDayEndOrChangeRoute=1;
                    //checkbuttonclick=2;

                    if(isOnline())
                    {

                    }
                    else
                    {
                        showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                        return;

                    }
                    dbengine.open();
                    whereTo = "11";
                    //////System.out.println("Abhinav store Selection  Step 1");
                    //////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);
                    StoreList2Procs = dbengine.ProcessStoreReq();
                    //////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);

                    if (StoreList2Procs.length != 0) {
                        //whereTo = "22";
                        //////System.out.println("Abhinav store Selection  Step 2");
                        midPart();
                        dayEndCustomAlert(1);
                        //showPendingStorelist(1);
                        dbengine.close();

                    } else if (dbengine.GetLeftStoresChk() == true)
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



                    dialog.dismiss();
                }


            }
        });


        changeRoute.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeRoute.setBackgroundColor(Color.GREEN);
                valDayEndOrChangeRoute=2;

                if(isOnline())
                {}
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                    return;

                }
                closeList = 0;
                whereTo = "11";
                //checkbuttonclick=1;

                // ////System.out.println("closeList: "+closeList);
                // chk if flag 2/3 found
                dbengine.open();
                StoreList2Procs = dbengine.ProcessStoreReq();

                // int picsCHK = dbengine.getExistingPicNosOnRemStore();
                // String[] sIDs2Alert =
                // dbengine.getStoreNameExistingPicNosOnRemStore();

                if (StoreList2Procs.length != 0) {// && picsCHK <= 0


                    midPart();
                    dayEndCustomAlert(2);
                    //showPendingStorelist(2);

                } else if (dbengine.GetLeftStoresChk() == true) {// && picsCHK
                    // <= 0

                    //enableGPSifNot();


                    showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
                    //showChangeRouteConfirm();

                }

                else {
                    // show dialog for clear..clear + tranx to launcher
                    //showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
                    DayEndWithoutalert();
                    //showChangeRouteConfirm();
                }

                dbengine.close();
                dialog.dismiss();
            }
        });



        butn_refresh_data.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                butn_refresh_data.setBackgroundColor(Color.GREEN);

                if(isOnline())
                {

                    AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(SurveyStoreList.this);

                    // set title
                    alertDialogBuilderNEw11.setTitle(getResources().getString(R.string.genTermNoDataConnection));

                    // set dialog message
                    alertDialogBuilderNEw11.setMessage(getResources().getString(R.string.RefreshDataMsg));
                    alertDialogBuilderNEw11.setCancelable(false);
                    alertDialogBuilderNEw11.setPositiveButton(getResources().getString(R.string.AlertDialogYesButton),new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialogintrfc,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialogintrfc.cancel();
                            try
                            {
                                new GetStoresForDay().execute();
                                //////System.out.println("SRVC-OK: "+ new GetStoresForDay().execute().get());
                            }
                            catch(Exception e)
                            {

                            }

                            //onCreate(new Bundle());
                        }
                    });

                    alertDialogBuilderNEw11.setNegativeButton(getResources().getString(R.string.AlertDialogNoButton),
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
                }
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                    return;

                }

                dialog.dismiss();

            }

        });



        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    //nitika
    public void dayEndCustomAlert(int flagWhichButtonClicked)
    {
        final Dialog dialog = new Dialog(SurveyStoreList.this,R.style.AlertDialogDayEndTheme);

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
            img_to_be_submit.setOnClickListener(new OnClickListener() {

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

            img_to_be_cancel.setOnClickListener(new OnClickListener() {

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
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSelectedItems.size() == 0) {

                    DayEnd();
                    //surbhi

                }

                else {

                    int countOfOrderNonSelected=0;
                    for(int countForFalse=0;countForFalse<checks.length;countForFalse++)
                    {
                        if(checks[countForFalse]==false)
                        {
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

        btn_cancel_Back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                valDayEndOrChangeRoute=0;
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);


        dialog.show();




    }


    public void confirmationForSubmission(String number)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SurveyStoreList.this);

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


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
	/*	if(storeList.length>0)
		{

		}

		else
		{
			new GetStoresForDay().execute();
		}*/
    }

    public void showAlertException(String title,String msg)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SurveyStoreList.this);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getResources().getString(R.string.txtRetry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                new GetStoresForDay().execute();
                dialog.dismiss();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getResources().getString(R.string.AlertDialogCancelButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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
                        setStoresList();
                    }
                    else
                    {
                        //addViewIntoTable();
                        setStoresList();
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
                        dbengine.open();
                        for(Map.Entry<String, String> entry:hmapOutletListForNearUpdated.entrySet())
                        {
                            String outID=entry.getKey().toString().trim();
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
                            setStoresList();
                        }
                        else
                        {
                            //addViewIntoTable();
                            setStoresList();
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

        }

        @Override
        public void onTick(long arg0) {
            // TODO Auto-generated method stub

        }}




    public String getAddressForDynamic(String latti,String longi){


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
        finally{

        }
    }
    private void getManagersDetail()
    {

        hmapManagerNameManagerIdDetails=dbengine.fetch_Manager_List();

        int index=0;
        if(hmapManagerNameManagerIdDetails!=null)
        {
            Manager_names=new String[hmapManagerNameManagerIdDetails.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapManagerNameManagerIdDetails);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                Manager_names[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }

	/*public String getAddressOfProviders(String latti, String longi){

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

    public String getAddressOfProviders(String latti, String longi){

        StringBuilder FULLADDRESS2 =new StringBuilder();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(SurveyStoreList.this, Locale.ENGLISH);



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
    public void saveLocale(String lang)
    {


        SharedPreferences prefs = getSharedPreferences("LanguagePref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }

    void setLanguage(String language) {
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
        Intent refresh = new Intent(SurveyStoreList.this, AllButtonActivity.class);
        startActivity(refresh);
        finish();

    }
    public String getAddressNewWay(String ZeroIndexAddress,String city,String State,String pincode,String country){
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

    private void getRouteDetail()
    {

        hmapRouteIdNameDetails=dbengine.fetch_Route_List();

        int index=0;
        if(hmapRouteIdNameDetails!=null)
        {
            Route_names=new String[hmapRouteIdNameDetails.size()];
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(hmapRouteIdNameDetails);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext())
            {
                Map.Entry me2 = (Map.Entry)iterator.next();
                Route_names[index]=me2.getKey().toString();
                index=index+1;
            }
        }


    }
    public void fnCreateStoreListOnLoad() throws Exception
    {



        for(int i = 0; i < tl2.getChildCount(); i++)
        {
            TableRow row = (TableRow) tl2.getChildAt(i);
            if(seleted_routeIDType.equals(row.getTag()))
            {
                row.setVisibility(View.VISIBLE);
            }
            else
            {
                row.setVisibility(View.GONE);
            }
        }

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
                int DatabaseVersion = dbengine.DATABASE_VERSION;
                int ApplicationID = dbengine.Application_TypeID;
                //newservice = newservice.getAvailableAndUpdatedVersionOfApp(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);

                dbengine.fnInsertOrUpdate_tblAllServicesCalledSuccessfull(1);
                for(int mm = 1; mm<6; mm++)
                {
                    if(mm==2)
                    {
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


                        newservice = newservice.fnGetStateCityListMstr(SurveyStoreList.this,imei, fDate,CommonInfo.Application_TypeID);
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






            }
            catch (Exception e)
            {
                dismissProgress();
            }
            finally
            {
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
                Intent intent =new Intent(SurveyStoreList.this,StorelistActivity.class);
                SurveyStoreList.this.startActivity(intent);
                finish();


            }
        }

    }

    private class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
    {




        public GetInvoiceForDay(SurveyStoreList activity)
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
                hmapInvoiceOrderIDandStatus=dbengine.fetchHmapInvoiceOrderIDandStatus();

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
            Intent storeIntent = new Intent(SurveyStoreList.this, InvoiceStoreSelection.class);
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
                android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(SurveyStoreList.this);
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

    class ShivAdapter extends ArrayAdapter<String> implements Filterable
    {
        Context context;
        ArrayList<String> ttt;
        ArrayList<String> arraylst;
        ShivAdapter(Context c, ArrayList<String> ttt )
        {

            super(c,R.layout.survey_list_row,R.id.product_name,ttt);
            this.arraylst=new ArrayList<String>();
            this.arraylst.addAll(ttt);
            this.context=c;
            this.ttt=ttt;
            //this.titleArray=titles;
            //this.descriptionArray=description;
        }

        class MyViewHolder
        {
            ImageView myImage;
            TextView myTitle;
            CheckBox myDescription;
            MyViewHolder(View v)
            {

                //  myImage=(ImageView) v.findViewById(R.id.imageView1);
                myTitle=(TextView) v.findViewById(R.id.product_name);
                // myDescription=(CheckBox) v.findViewById(R.id.checkbox);
            }


        }

        public View getView(int position, View convertView, ViewGroup parent){
            View row=convertView;
            final MyViewHolder holder;
            //if you want to put some data in row from outside hashmap and you getting problem in scrolling then comment uncomment row=null;
            row=null;
            if(row==null)
            {

                LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.survey_list_row, parent,false);
                holder=new MyViewHolder(row);
                row.setTag(holder);

            }
            else{
                holder=(MyViewHolder) row.getTag();
            }

            //holder.myImage.setImageResource(images[position]);
            //holder.myTitle.setText(titleArray[position]);

            holder.myTitle.setText(ttt.get(position).toString().trim());
            holder.myTitle.setTag(listStoreId.get(position).toString().trim());

            if(hmapTestSubmitOrNot!=null){
                if((!hmapTestSubmitOrNot.isEmpty()) && hmapTestSubmitOrNot.containsKey(holder.myTitle.getTag().toString()) ){
                    holder.myTitle.setTextColor(getResources().getColor(R.color.green));
                    holder.myTitle.setEnabled(false);
                }
            }





            // holder.myDescription.setTag(ttt.get(position).toString().trim());
            // String valuess=	hmapBooleanValues.get(ttt.get(position).toString().trim());
            //  holder.myDescription.setButtonDrawable(getResources().getDrawable(R.drawable.checkbox_button_image));
            /*if(valuess.equals("0")){
                holder.myDescription.setChecked(false);
            }

            if(valuess.equals("1")){
                holder.myDescription.setChecked(true);
            }*/
		/*if(position==2){
			holder.myTitle.setTextColor(Color.GREEN);

		}*/
            //holder.myDescription.setChecked(descriptionArray.get(position));
            holder.myTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //String mm=		holder.myDescription.getTag().toString();
                    //System.out.println("fff"+mm);

                  //  int mmm=(Integer) holder.myTitle.getTag();
                    String  abc=(String) holder.myTitle.getText();

                    inputSearch.setText("");
                    if(hmapStoreNameID.containsKey(abc)){
                        String storeID=   hmapStoreNameID.get(abc);
                        Intent intent=new Intent(SurveyStoreList.this,SurveyActivity.class);
                        intent.putExtra("imei", imei);
                        intent.putExtra("userDate", userDate);
                        intent.putExtra("pickerDate", pickerDate);
                        intent.putExtra("StoreID",storeID);
                        intent.putExtra("StoreName",abc);
                        startActivity(intent);
                    }
                /*    if(holder.myDescription.isChecked()){
                        holder.myDescription.getTag().toString().trim();
                        hmapBooleanValues.put(holder.myDescription.getTag().toString().trim(), "1");
                        holder.myDescription.setChecked(true);
                        adapterCusto.notifyDataSetChanged();
                        if(holder.myDescription.getTag().toString().trim().equals("Others")){
                            othersEditText.setVisibility(View.VISIBLE);
                        }
                        cal25[mmm]=true;
                        listItemBoolean.set(mmm, true);
                        adapterCusto.notifyDataSetChanged();
                        holder.myDescription.setChecked(true);
                        String abc=lv.getItemAtPosition(mmm).toString().trim();
                        hmapCheckedValues.put(abc+"^"+mmm, abc);
                        //System.out.println("Printt"+ii);
                    }
                    else{
                        holder.myDescription.getTag().toString().trim();
                        hmapBooleanValues.put(holder.myDescription.getTag().toString().trim(), "0");
                        holder.myDescription.setChecked(false);
                        adapterCusto.notifyDataSetChanged();
                        if(holder.myDescription.getTag().toString().trim().equals("Others")){
                            othersEditText.setVisibility(View.GONE);


                        }

                        cal25[mmm]=false;
                        listItemBoolean.set(mmm, false);
                        adapterCusto.notifyDataSetChanged();
                        holder.myDescription.setChecked(false);
                        String abc=lv.getItemAtPosition(mmm).toString().trim();
                        hmapCheckedValues.put(abc+"^"+mmm, "");
                    }*/

                }
            });
            return row;
        }
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            ttt.clear();
            if (charText.length() == 0) {
                ttt.addAll(arraylst);
            }
            else
            {
                for (int i=0;i<arraylst.size();i++)
                {
                    if (arraylst.get(i).toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        ttt.add(arraylst.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
