package project.astix.com.sancusnetworkssosfaindirect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;


import com.astix.Common.CommonInfo;
import com.example.gcm.ApplicationConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class SplashScreen extends AppCompatActivity
{
    SharedPreferences sharedPrefForSurvey;
    SyncXMLfileData task2;
    public String[] xmlForWeb = new String[1];
    DatabaseAssistantDistributorEntry DA = new DatabaseAssistantDistributorEntry(this);
    public String newfullFileName;
    int serverResponseCode = 0;
    public int syncFLAG = 0;
    public ProgressDialog pDialogGetStores;
    String serverDateForSPref;
    SharedPreferences sPref,sPrefAttandance;
    public int flgTodaySalesTargetToShow=0;

    DBAdapterKenya dbengine; //= new DBAdapterKenya(this);
    ServiceWorker newservice = new ServiceWorker();
    public String imei;
    public SimpleDateFormat sdf;
    public String fDate;
    public int chkFlgForErrorToCloseApp=0;


    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    AsyncTask<Void, Void, String> createRegIdTask;
    public static final String REG_ID = "regId";
    public String RegistrationID="NotGettingFromServer";
    SharedPreferences sharedPref;

    public void onCreateFunctionalityAllcode()
    {
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

        // imei="354010084603910";  // New Testing Phoine
        // imei="911383400331938";  // Test User Anil Kumar
         //imei="866924020382358";   // Live user
       //  imei="354010084603910";
        // imei="351976087770067";   // TSI imei with RSPL Test Release DB
        // imei="866924027228117";    // hard code given to Alok Sir
        // imei="353202065143237";

        // imei="864449033669429"; //by avinash sir
        // imei="357328070587765";
        // imei="351976082609146";   // For training App
        // imei="351976082609153";

        // imei="354010084603910_OLd"; //for test
        // imei="359473079352535";    //Ramesh as TSI
        // imei="866924020381699";
        // imei="866924028228595";
        // imei="351976087620171";
        // imei="359473079352536";    //Ramesh as TSI
        // imei="351976082609146";
      //  imei="351893080684874"; // test release

       // imei="359632061313398";

       // imei="352801088236109";

       //imei="866924020424622"; // Test release

      //  imei="862103031525560"; //given by gaurav sir
      //  imei="358958066686533"; //for development given by avinash
        //imei="359473079352536";
        CommonInfo.imei = imei;


       /* Intent storeIntent1 = new Intent(SplashScreen.this, AllButtonActivity.class);
        startActivity(storeIntent1);
        finish();*/

        sPref=getSharedPreferences(CommonInfo.Preference, MODE_PRIVATE);
        sPrefAttandance=getSharedPreferences(CommonInfo.AttandancePreference, MODE_PRIVATE);
        Date date1 = new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date1).trim();

        int checkDataNotSync = dbengine.CheckUserDoneGetStoreOrNot();

        if (checkDataNotSync == 1)
        {
            dbengine.open();
            String rID = dbengine.GetActiveRouteID();
            dbengine.close();

            // Date date=new Date();
            sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String fDateNew = sdf.format(date1);
            //fDate = passDate.trim().toString();


            // In Splash Screen SP, we are sending this Format "dd-MMM-yyyy"
            // But InLauncher Screen SP, we are sending this Format "dd-MM-yyyy"
            Intent storeIntent=null;
            if(sharedPrefForSurvey.contains("FROM")){
                if( sharedPrefForSurvey.getString("FROM", "").equals("DASHBOARD")){
                    storeIntent = new Intent(SplashScreen.this, AllButtonActivity.class);
                }
                else {
                    storeIntent = new Intent(SplashScreen.this, StoreSelection.class);
                }
            }

            else{
                storeIntent = new Intent(SplashScreen.this, StoreSelection.class);
            }

            storeIntent.putExtra("imei", imei);
            storeIntent.putExtra("userDate", fDate);
            storeIntent.putExtra("pickerDate", fDateNew);
            storeIntent.putExtra("rID", rID);
            startActivity(storeIntent);
            finish();

        } else
        {
            if (isOnline())
            {
                try
                {
                    CheckUpdateVersion cuv = new CheckUpdateVersion();
                    cuv.execute();
                }
                catch (Exception e) {
                    e.printStackTrace();

                }
            }
            else
            {
                showNoConnAlert();

            }
        }
    }




    public void onCreateFunctionality()

    {

        setLanguage("en");
        onCreateFunctionalityAllcode();
/*
        SharedPreferences sharedPreferences=getSharedPreferences("LanguagePref", MODE_PRIVATE);
        SharedPreferences.Editor ed;
        if(!sharedPreferences.contains("Language")){
            android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(SplashScreen.this);
            dialog.setTitle("Language");
            dialog.setMessage("Please Select Language ");
            dialog.setPositiveButton("Hindi", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {



                    try
                    { dialog.dismiss();
                        setLanguage("hi");
                        onCreateFunctionalityAllcode();

                    }
                    catch(Exception e)
                    {

                    }



                }
            });
            dialog.setNegativeButton("English", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    setLanguage("en");
                    onCreateFunctionalityAllcode();
                }
            });

            android.app.AlertDialog alert=dialog.create();
            alert.show();

        }
        else
        {
            loadLocale();
            onCreateFunctionalityAllcode();

        }*/


    }
    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("LanguagePref", Activity.MODE_PRIVATE);
        String language = prefs.getString("Language", "");
        changeLang(language);
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale  myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // updateTexts();

    }
    public void saveLocale(String lang)
    {


        SharedPreferences prefs = getSharedPreferences("LanguagePref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
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
      /*  Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();*/

    }



    public void fnShowAlertBeforeRedirectingToLauncher()
    {


        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(SplashScreen.this);
        alertDialogNoConn.setTitle(R.string.genTermNoDataConnection);
        alertDialogNoConn.setMessage(Html.fromHtml(CommonInfo.SalesPersonTodaysTargetMsg));
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                        int checkNoDataInTable=0;
                        try {
                            checkNoDataInTable=dbengine.counttblDistributorSavedData();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        try
                        {
                            if(checkNoDataInTable==0)
                            {
                                dbengine.deleteDistributorStockTbles();
                                Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                intent.putExtra("imei", imei);
                                SplashScreen.this.startActivity(intent);
                                finish();
                            }
                            else
                            {
                                FullSyncDataNow task = new FullSyncDataNow(SplashScreen.this);
                                task.execute();
                            }
                        }
                        catch(Exception e)
                        {

                        }


                    }
                });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbengine = new DBAdapterKenya(this);

        sharedPrefForSurvey=getSharedPreferences("SurveyPref", MODE_PRIVATE);
  //      BugSenseHandler.setup(this, "1f12b9fe");

        // Initalization New Relic Tool
       // NewRelic.withApplicationToken("AA650dd750f0d8d87061719815bd7514decb5fb129").start(this.getApplication());

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= 23)
        {

            if(checkingPermission())
            {
                onCreateFunctionality();
            }



            else
            {
                ActivityCompat.requestPermissions(SplashScreen.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else{
            onCreateFunctionality();

        }



    }

    public  boolean checkingPermission()
    {

            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) )
            {
                return false;
                //onCreateFunctionality();
            }
            else if((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
                return false;
            }
            else if((checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)){
                return false;
            }
            else if((checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)){
                return false;
            }
            else if((checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                return false;
            }
            else if((checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                return false;
            }
            else{
                return true;
            }


    }

    private class CheckUpdateVersion extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                int DatabaseVersion=dbengine.DATABASE_VERSION;
                int ApplicationID=dbengine.Application_TypeID;
                newservice = newservice.getAvailableAndUpdatedVersionOfAppNew(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);
                if(!newservice.director.toString().trim().equals("1"))
                {
                    if(chkFlgForErrorToCloseApp==0)
                    {
                        chkFlgForErrorToCloseApp=1;
                    }
                }

            }
            catch (Exception e)
            {}

            finally
            {}

            return null;
        }


        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if(chkFlgForErrorToCloseApp==1)   // if Webservice showing exception or not excute complete properly
            {
                chkFlgForErrorToCloseApp=0;
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.internetError), Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                dbengine.open();
                int checkUserAuthenticate=dbengine.FetchflgUserAuthenticated();
                dbengine.close();

                if(checkUserAuthenticate==0)   // 0 means-->New user        1 means-->Exist User
                {
                    showAlertForEveryOne(getResources().getString(R.string.phnRegisterError));
                    return;

                }
                dbengine.open();
                int check=dbengine.FetchVersionDownloadStatus();  // 0 means-->new version installed  1 means-->new version not install
                dbengine.close();
                if(check==1)
                {
                    showNewVersionAvailableAlert();
                }
                else
                {
                    dbengine.open();
                    dbengine.maintainPDADate();
                    int cntRoute=dbengine.counttblCountRoute();
                    int chkIfPDADaeExistOrNot=dbengine.fnCheckPdaDateExistOrNot();
                    dbengine.close();

                    dbengine.open();

                    String getPDADate=dbengine.fnGetPdaDate();
                    String getServerDate=dbengine.fnGetServerDate();

                    dbengine.close();
                    if(!getServerDate.equals(getPDADate))
                    {
                        //show message and close
                        showAlertBox(getResources().getString(R.string.phnDateError));
                        return;
                    }

                    dbengine.open();
                    dbengine.fnFinaldropRoutesTBL();
                    dbengine.createRouteTBL();
                    dbengine.close();
                    try
                    {
                        funGetRegistrationID(getResources().getString(R.string.regID));

                        checkPlayServices();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }

        }
    }

    private void funGetRegistrationID(final String emailID)
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (gcmObj == null)
                    {
                        gcmObj = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = regId;
                }
                catch(IOException ex)
                {
                    msg = getResources().getString(R.string.errorTxt) + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {

                RegistrationID=msg;
                // System.out.println("Sunil Reg id msg SplashScreen:"+RegistrationID);
                try
                {
                    new GetRouteInfo().execute();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.errorPlayServices),Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        else
        {
            //Toast.makeText(applicationContext,"This device supports Play services, App will work normally",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void showAlertBox(String msg)
    {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(SplashScreen.this);
        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);

        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                dbengine.open();
                dbengine.reTruncateRouteMstrTbl();
                dbengine.close();
                finish();

            }
        });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }

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

    public void showAlertForEveryOne(String msg)
    {
        // AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(SplashScreen.this, R.style.Dialog));
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(SplashScreen.this);

        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(msg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
            }
        });
        //alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }

    public void showNoConnAlert()
    {
        //AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(SplashScreen.this, R.style.Dialog));
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(SplashScreen.this);

        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setMessage(R.string.NoDataConnectionFullMsg);
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        dbengine.open();
                        int cntRoute=dbengine.counttblCountRoute();
                        dbengine.close();
                        if(cntRoute==0)
                        {
                            finish();
                        }
                        else
                        {
                            dbengine.open();
                            serverDateForSPref=	dbengine.fnGetServerDate();
                            dbengine.close();

                            if(sPref.contains("DatePref"))
                            {

                                if(sPref.getString("DatePref", "").equals(serverDateForSPref))
                                {
                                    Date date1 = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    fDate = sdf.format(date1).trim();

                                    int chkIfStoreExists=dbengine.fntableExists("tblStoreList");
                                    int checkDataNotSync =0;

                                    if(chkIfStoreExists==1)
                                    {
                                        checkDataNotSync=dbengine.CheckUserDoneGetStoreOrNot();
                                    }
                                    else
                                    {
                                        checkDataNotSync=0;
                                    }

                                    // checkDataNotSync=0;
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

                                        Intent storeIntent=null;
                                        if(sharedPrefForSurvey.contains("FROM")){
                                            if( sharedPrefForSurvey.getString("FROM", "").equals("DASHBOARD")){
                                                storeIntent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                            }
                                            else {
                                                storeIntent = new Intent(SplashScreen.this, StoreSelection.class);
                                            }
                                        }

                                        else{
                                            storeIntent = new Intent(SplashScreen.this, StoreSelection.class);
                                        }

                                        storeIntent.putExtra("imei", imei);
                                        storeIntent.putExtra("userDate", fDate);
                                        storeIntent.putExtra("pickerDate", fDateNew);
                                        storeIntent.putExtra("rID", rID);
                                        startActivity(storeIntent);
                                        finish();

                                    }
                                    else
                                    {
                                        finish();
                                    }

                                   /* Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                    intent.putExtra("imei", imei);
                                    SplashScreen.this.startActivity(intent);
                                    finish();*/

                                }
                                else
                                {
                                    finish();
                                   /* SharedPreferences.Editor editor=sPref.edit();
                                    editor.clear();
                                    editor.commit();
                                    sPref.edit().putString("DatePref", serverDateForSPref).commit();
                                    fnShowAlertBeforeRedirectingToLauncher();*/
                                }
                            }
                            else
                            {
                                finish();;
                               // sPref.edit().putString("DatePref", serverDateForSPref).commit();
                               // fnShowAlertBeforeRedirectingToLauncher();
                            }
                        }

                    }
                });
        // alertDialogNoConn.setIcon(R.drawable.error_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();
    }

    public void showNewVersionAvailableAlert()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(new ContextThemeWrapper(SplashScreen.this, R.style.Dialog));
        alertDialogNoConn.setTitle(R.string.AlertDialogHeaderMsg);
        alertDialogNoConn.setCancelable(false);
        alertDialogNoConn.setMessage(getText(R.string.NewVersionMsg));
        alertDialogNoConn.setNeutralButton(R.string.AlertDialogOkButton,new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                GetUpdateInfo task = new GetUpdateInfo(SplashScreen.this);
                task.execute();
                dialog.dismiss();
            }
        });

        // alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }
    private class GetUpdateInfo extends AsyncTask<Void, Void, Void>
    {

        ProgressDialog pDialogGetStores;
        public GetUpdateInfo(SplashScreen activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getText(R.string.UpdatingNewVersionMsg));
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
                downloadapk();
            }
            catch(Exception e)
            {}

            finally
            {}

            return null;
        }


        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            installApk();
        }
    }

    private void downloadapk()
    {
        try
        {

            //ParagIndirectTest
            // URL url = new URL("http://115.124.126.184/downloads/ParagIndirect.apk");
            //  URL url = new URL("http://115.124.126.184/downloads/ParagIndirectTest.apk");
            URL url = new URL(CommonInfo.VersionDownloadPath.trim()+CommonInfo.VersionDownloadAPKName);
            URLConnection connection = url.openConnection();
            HttpURLConnection urlConnection = (HttpURLConnection) connection;
            //urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(false);
            // urlConnection.setInstanceFollowRedirects(false);
            urlConnection.connect();

            //if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            // {
            File sdcard = Environment.getExternalStorageDirectory();

            //  //System.out.println("sunil downloadapk sdcard :" +sdcard);
            //File file = new File(sdcard, "neo.apk");

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            // File file2 = new File(PATH+"ParagIndirect.apk");
            File file2 = new File(PATH+CommonInfo.VersionDownloadAPKName);
            if(file2.exists())
            {
                file2.delete();
            }

            File file1 = new File(PATH);
            file1.mkdirs();


            File file = new File(file1, CommonInfo.VersionDownloadAPKName);

            int size = connection.getContentLength();


            FileOutputStream fileOutput = new FileOutputStream(file);

            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[10240];
            int bufferLength = 0;
            int current = 0;
            while ( (bufferLength = inputStream.read(buffer)) != -1 )
            {
                fileOutput.write(buffer, 0, bufferLength);
            }

            fileOutput.close();




        } catch (MalformedURLException e)
        {

        } catch (IOException e)
        {

        }
    }

   /* private void installApk()
    {
        this.deleteDatabase(DBAdapterKenya.DATABASE_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + CommonInfo.VersionDownloadAPKName));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
        finish();


    }
*/
   private void installApk()
   {
       this.deleteDatabase(DBAdapterKenya.DATABASE_NAME);
       File file = new File(Environment.getExternalStorageDirectory() + "/download/" + CommonInfo.VersionDownloadAPKName);
       Intent intent = new Intent(Intent.ACTION_VIEW);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           Uri contentUri = FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", file);
           //  Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),"project.astix.com.rsplsosfaindirect.fileprovider" , );
           // Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + CommonInfo.VersionDownloadAPKName));

           intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
       } else {
           Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + CommonInfo.VersionDownloadAPKName));
           intent.setDataAndType(uri, "application/vnd.android.package-archive");
       }

       startActivity(intent);
       finish();


   }


    private class GetRouteInfo extends AsyncTask<Void, Void, Void>
    {
        ServiceWorker getRouteservice = new ServiceWorker();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {
                for(int mm = 1; mm<11; mm++)
                {
                    System.out.println("mm Value:-"+mm);
                    if(mm==1)
                    {
                        getRouteservice = getRouteservice.getAvailbRoutes(getApplicationContext(), imei,fDate,RegistrationID,flgTodaySalesTargetToShow);

                        if(!getRouteservice.director.toString().trim().equals("1")){
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.routeNoAvail),Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                    if(mm==2)
                    {
                        getRouteservice = getRouteservice.getAvailbNotification(getApplicationContext(),imei,fDate);

                    }
                    if(mm==3)
                    {
                        int DatabaseVersion=CommonInfo.DATABASE_VERSIONID;
                        int ApplicationID=CommonInfo.Application_TypeID;
                        newservice = newservice.callfnSingleCallAllWebService(getApplicationContext(),ApplicationID,imei);
                        if(!newservice.director.toString().trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                            }

                        }



                    }
                    if(mm==4)
                    {
                        getRouteservice = getRouteservice.getCallspToGetReasonMasterForNoVisit(getApplicationContext(), fDate, imei);

                    }

                    if(mm==5)
                    {

                        int check =dbengine.countDataIntblNoVisitStoreDetails();
                        if(check==0)
                        {
                            getRouteservice = getRouteservice.getCallspToCheckForVisit(getApplicationContext(), fDate, imei);
                            if(!newservice.director.toString().trim().equals("1"))
                            {
                                if(chkFlgForErrorToCloseApp==0)
                                {
                                    chkFlgForErrorToCloseApp=1;
                                }

                            }
                        }
                        else
                        {

                        }


                    }
                    if(mm==6)
                    {
                       // getRouteservice = getRouteservice.fnGetIncentiveData(getApplicationContext(), fDate, imei);

                    }

                    if(mm==7)
                    {
                        newservice = newservice.getDistributorMstr(getApplicationContext(), imei, fDate);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }
                        }
                    }
                    if(mm==8)
                    {
                        int DatabaseVersion = dbengine.DATABASE_VERSION;
                        int ApplicationID = dbengine.Application_TypeID;
                        newservice = newservice.getfnGetLTFoodsSODSRRoutesTempByAbhinavDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }


                    }

                    if(mm==9)
                    {


                        newservice = newservice.fnGetStateCityListMstr(SplashScreen.this,imei, fDate,CommonInfo.Application_TypeID);
                        if(!newservice.director.toString().trim().equals("1"))
                        {
                            if(chkFlgForErrorToCloseApp==0)
                            {
                                chkFlgForErrorToCloseApp=1;
                                break;
                            }

                        }
                    }

                    if(mm==10)
                    {


                        newservice = newservice.fnGetSurveyData(SplashScreen.this,imei, fDate,CommonInfo.Application_TypeID);
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

            }
            finally
            {

            }

            return null;
        }

        @Override
        protected void onCancelled()
        {

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if(chkFlgForErrorToCloseApp==1)
            {
                chkFlgForErrorToCloseApp=0;
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.errorFetchData),Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Handler handler = new Handler();

                // run a thread after 2 seconds to start the home screen
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dbengine.open();
                        serverDateForSPref=	dbengine.fnGetServerDate();
                        dbengine.close();

                        if(sPref.contains("DatePref"))
                        {

                            if(sPref.getString("DatePref", "").equals(serverDateForSPref))
                            {

                                if(!sPrefAttandance.contains("AttandancePref"))
                                {
                                    //callDayStartActivity();
                                    Intent i = new Intent(SplashScreen.this, DSR_Registration.class);
                                    i.putExtra("IntentFrom", "SPLASH");
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                    intent.putExtra("imei", imei);
                                    SplashScreen.this.startActivity(intent);
                                    finish();
                                }

                            }
                            else
                            {	SharedPreferences.Editor editor=sPref.edit();
                                editor.clear();
                                editor.commit();
                                sPref.edit().putString("DatePref", serverDateForSPref).commit();
                                SharedPreferences.Editor editor1=sPrefAttandance.edit();
                                editor1.clear();
                                editor1.commit();

                                if(!sPrefAttandance.contains("AttandancePref"))
                                {
                                   // callDayStartActivity();
                                    Intent i = new Intent(SplashScreen.this, DSR_Registration.class);
                                    i.putExtra("IntentFrom", "SPLASH");
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                    intent.putExtra("imei", imei);
                                    SplashScreen.this.startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        else
                        {
                            sPref.edit().putString("DatePref", serverDateForSPref).commit();
                            if(!sPrefAttandance.contains("AttandancePref"))
                            {
                                //callDayStartActivity();
                                Intent i = new Intent(SplashScreen.this, DSR_Registration.class);
                                i.putExtra("IntentFrom", "SPLASH");
                                startActivity(i);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
                                intent.putExtra("imei", imei);
                                SplashScreen.this.startActivity(intent);
                                finish();
                            }
                        }

                    }

                }, 1000);
            }





        }
    }

    public void callDayStartActivity()
    {
        dbengine.open();
        int flgPersonTodaysAtt=dbengine.FetchflgPersonTodaysAtt();
        dbengine.close();

        if(flgPersonTodaysAtt==0)
        {
            Intent intent=new Intent(this,DayStartActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
            intent.putExtra("imei", imei);
            SplashScreen.this.startActivity(intent);
            finish();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if((grantResults[0]== PackageManager.PERMISSION_GRANTED) && (grantResults[1]== PackageManager.PERMISSION_GRANTED) && (grantResults[2]== PackageManager.PERMISSION_GRANTED) && (grantResults[3]== PackageManager.PERMISSION_GRANTED)&& (grantResults[4]== PackageManager.PERMISSION_GRANTED))
        {
            onCreateFunctionality();
        }
        else
        {
            finish();

        }
    }


    private class FullSyncDataNow extends AsyncTask<Void, Void, Void>
    {


        ProgressDialog pDialogGetStores;
        public FullSyncDataNow(SplashScreen activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getResources().getString(R.string.genTermLoadData));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();


        }

        @Override

        protected Void doInBackground(Void... params)
        {

            int Outstat=3;

            long  syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss",Locale.ENGLISH);
            String StampEndsTime = df.format(dateobj);



            dbengine.open();
            String presentRoute="0";
            dbengine.close();

            SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
            newfullFileName=imei+"."+presentRoute+"."+ df1.format(dateobj);

            try
            {

                File MeijiDistributorEntryXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorStockXMLFolder);
                if (!MeijiDistributorEntryXMLFolder.exists())
                {
                    MeijiDistributorEntryXMLFolder.mkdirs();
                }

                int checkNoFiles=dbengine.counttblDistributorSavedData();
                if(checkNoFiles==1)
                {
                    String routeID=dbengine.GetActiveRouteIDSunil();
                    DA.open();
                    DA.export(CommonInfo.DATABASE_NAME, newfullFileName,routeID);
                    DA.close();

                }



                //dbengine.deleteDistributorStockTbles();




            }
            catch (Exception e)
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
        protected void onCancelled()
        {

        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            try
            {

                task2 = new SyncXMLfileData(SplashScreen.this);
                task2.execute();
            }
            catch(Exception e)
            {

            }


        }
    }




    private class SyncXMLfileData extends AsyncTask<Void, Void, Integer>
    {



        public SyncXMLfileData(SplashScreen activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


            File MeijiIndirectSFAxmlFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorStockXMLFolder);

            if (!MeijiIndirectSFAxmlFolder.exists())
            {
                MeijiIndirectSFAxmlFolder.mkdirs();
            }

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));

            pDialogGetStores.setMessage(getText(R.string.genTermPleaseWaitNew));

            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();

        }

        @Override
        protected Integer doInBackground(Void... params)
        {


            // This method used for sending xml from Folder without taking records in DB.

            // Sending only one xml at a times

            File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.DistributorStockXMLFolder);

            // check number of files in folder
            String [] AllFilesName= checkNumberOfFiles(del);


            if(AllFilesName.length>0)
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


                for(int vdo=0;vdo<AllFilesName.length;vdo++)
                {
                    String fileUri=  AllFilesName[vdo];


                    System.out.println("Sunil Again each file Name :" +fileUri);

                    if(fileUri.contains(".zip"))
                    {
                        File file = new File(fileUri);
                        file.delete();
                    }
                    else
                    {
                        String f1=Environment.getExternalStorageDirectory().getPath()+"/"+CommonInfo.DistributorStockXMLFolder+"/"+fileUri;
                        System.out.println("Sunil Again each file full path"+f1);
                        try
                        {
                            upLoad2ServerXmlFiles(f1,fileUri);
                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            }
            else
            {

            }



            // pDialogGetStores.dismiss();

            return serverResponseCode;
        }

        @Override
        protected void onCancelled()
        {
            // Log.i("SyncMasterForDistributor","Sync Cancelled");
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            super.onPostExecute(result);
            if(!isFinishing())
            {

                // Log.i("SyncMasterForDistributor", "Sync cycle completed");


                if(pDialogGetStores.isShowing())
                {
                    pDialogGetStores.dismiss();
                }




            }
            dbengine.deleteDistributorStockTbles();
				/* showAlertBox("Your Phone  Date is not correct.Please Correct it.");
					return;*/

          /*  dbengine.open();
            dbengine.reCreateDB();
            dbengine.close();*/
            Intent intent = new Intent(SplashScreen.this, AllButtonActivity.class);
            intent.putExtra("imei", imei);
            SplashScreen.this.startActivity(intent);
            finish();


        }





    }



    public  int upLoad2ServerXmlFiles(String sourceFileUri,String fileUri)
    {

        fileUri=fileUri.replace(".xml", "");

        String fileName = fileUri;
        String zipFileName=fileUri;

        String newzipfile = Environment.getExternalStorageDirectory() + "/"+CommonInfo.DistributorStockXMLFolder+"/" + fileName + ".zip";
        ///storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.zip

        sourceFileUri=newzipfile;

        xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/"+CommonInfo.DistributorStockXMLFolder+"/" + fileName + ".xml";
        //[/storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.xml]

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

        String urlString = CommonInfo.DistributorSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;

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

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200)
            {
							  /* dbengine.open();
							   dbengine.upDateXMLFileFlag(fileUri, 4);
							   dbengine.close();*/

                //new File(dir, fileUri).delete();
                syncFLAG=1;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                // editor.remove(xmlForWeb[0]);
                editor.putString(fileUri, ""+4);
                editor.commit();

                String FileSyncFlag=pref.getString(fileUri, ""+1);

                delXML(xmlForWeb[0].toString());
							   		/*dbengine.open();
						            dbengine.deleteXMLFileRow(fileUri);
						            dbengine.close();*/

            }
            else
            {
                syncFLAG=0;
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

    public void delXML(String delPath)
    {
        File file = new File(delPath);
        file.delete();
        File file1 = new File(delPath.toString().replace(".xml", ".zip"));
        file1.delete();
    }

    public static void zip(String[] files, String zipFile) throws IOException
    {
        BufferedInputStream origin = null;
        final int BUFFER_SIZE = 2048;

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try
        {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++)
            {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try
                {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1)
                    {
                        out.write(data, 0, count);
                    }
                }
                finally
                {
                    origin.close();
                }
            }
        }
        finally
        {
            out.close();
        }
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
}
