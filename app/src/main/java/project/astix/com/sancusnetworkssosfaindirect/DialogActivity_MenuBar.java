package project.astix.com.sancusnetworkssosfaindirect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class DialogActivity_MenuBar extends BaseActivity
{
    ServiceWorker newservice = new ServiceWorker();
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
    StoreSelection strSelection=new StoreSelection();

    public String fDate;
    public String userDate;
    public String pickerDate;
    public String imei;
    public String rID;
    String clickCheck="NA";

    Dialog dialog=null;
    public String passDate;
    public SimpleDateFormat sdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getStorei = getIntent();
        if(getStorei !=null)
        {
            imei = getStorei.getStringExtra("imei").trim();
            pickerDate = getStorei.getStringExtra("pickerDate").trim();
            userDate = getStorei.getStringExtra("userDate");
            //rID = getStorei.getStringExtra("rID");
        }

        Date date1=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        passDate = sdf.format(date1);

        //System.out.println("Selctd Date: "+ passDate);

        fDate = passDate.trim();

        open_pop_up();
    }

    protected void open_pop_up()
    {
        dialog = new Dialog(DialogActivity_MenuBar.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selection_header_custom);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.side_dialog_animation;
        final WindowManager.LayoutParams parms = dialog.getWindow().getAttributes();
        parms.gravity = Gravity.TOP | Gravity.LEFT;
        parms.height=parms.MATCH_PARENT;
        parms.dimAmount = (float) 0.5;
        //setFinishOnTouchOutside(true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogin)
            {
                if(clickCheck.equals("ChangeLang") || clickCheck.equals("refresh"))
                {
                    dialogin.dismiss();
                }
                else
                {
                    finish();
                }
            }
        });

        final  Button butn_refresh_data = (Button) dialog.findViewById(R.id.butn_refresh_data);
        final  Button but_day_end = (Button) dialog.findViewById(R.id.mainImg1);
        final  Button changeRoute = (Button) dialog.findViewById(R.id.changeRoute);
        changeRoute.setVisibility(View.GONE);
        final  Button btnewAddedStore = (Button) dialog.findViewById(R.id.btnewAddedStore);

        final  Button btnExecution = (Button) dialog.findViewById(R.id.btnExecution);
        btnExecution.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new GetInvoiceForDay().execute();
            }
        });

        final   Button butHome = (Button) dialog.findViewById(R.id.butHome);
        butHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(DialogActivity_MenuBar.this,AllButtonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button btnTargetVsAchieved=(Button) dialog.findViewById(R.id.btnTargetVsAchieved);
        btnTargetVsAchieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(DialogActivity_MenuBar.this, TargetVsAchievedActivity.class);
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

        btnewAddedStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DialogActivity_MenuBar.this, ViewAddedStore.class);
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
        btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        but_SalesSummray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                but_SalesSummray.setBackgroundColor(Color.GREEN);
                dialog.dismiss();

                SharedPreferences sharedPrefReport = getSharedPreferences("Report", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefReport .edit();
                editor.putString("fromPage", "2");
                editor.commit();

                Intent intent = new Intent(DialogActivity_MenuBar.this, DetailReportSummaryActivity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");
                intent.putExtra("fromPage","StoreSelection");
                startActivity(intent);
                finish();

            }
        });


        final Button btnChangeLanguage = (Button) dialog.findViewById(R.id.btnChangeLanguage);
        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clickCheck="ChangeLang";
                dialog.dismiss();
                final Dialog dialogLanguage = new Dialog(DialogActivity_MenuBar.this);
                dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogLanguage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

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

        final Button btnCheckTodayOrder = (Button) dialog.findViewById(R.id.btnCheckTodayOrder);
        btnCheckTodayOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                but_SalesSummray.setBackgroundColor(Color.GREEN);
                dialog.dismiss();
                Intent intent = new Intent(DialogActivity_MenuBar.this, CheckDatabaseData.class);
                // Intent intent = new Intent(StoreSelection.this, DetailReport_Activity.class);
                intent.putExtra("imei", imei);
                intent.putExtra("userDate", userDate);
                intent.putExtra("pickerDate", pickerDate);
                intent.putExtra("rID", rID);
                intent.putExtra("back", "0");
                startActivity(intent);
                finish();

            }
        });

        but_day_end.setOnClickListener(new View.OnClickListener() {
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
                    AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(DialogActivity_MenuBar.this);
                    alertDialogNoConn.setTitle(getResources().getString(R.string.genTermNoDataConnection));
                    alertDialogNoConn.setMessage(getResources().getString(R.string.Dsrmessage));
                    alertDialogNoConn.setCancelable(false);
                    alertDialogNoConn.setNeutralButton(getResources().getString(R.string.AlertDialogOkButton),new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            Intent intent=new Intent(DialogActivity_MenuBar.this,DSR_Registration.class);
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
                    strSelection.closeList = 0;
                    strSelection.valDayEndOrChangeRoute=1;
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
                    strSelection.whereTo = "11";
                    //////System.out.println("Abhinav store Selection  Step 1");
                    //////System.out.println("StoreList2Procs(before): " + StoreList2Procs.length);
                    strSelection.StoreList2Procs = dbengine.ProcessStoreReq();
                    //////System.out.println("StoreList2Procs(after): " + StoreList2Procs.length);

                    if (strSelection.StoreList2Procs.length != 0) {
                        //whereTo = "22";
                        //////System.out.println("Abhinav store Selection  Step 2");
                        strSelection.midPart();
                        strSelection.dayEndCustomAlert(1);
                        //showPendingStorelist(1);
                        dbengine.close();

                    } else if (dbengine.GetLeftStoresChk() == true)
                    {
                        //////System.out.println("Abhinav store Selection  Step 7");
                        //enableGPSifNot();
                        // showChangeRouteConfirm();
                        strSelection.DayEnd();
                        dbengine.close();
                    }

                    else {
                        strSelection.DayEndWithoutalert();
                    }

                    dialog.dismiss();
                }


            }
        });


        changeRoute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeRoute.setBackgroundColor(Color.GREEN);
                strSelection.valDayEndOrChangeRoute=2;

                if(isOnline())
                {}
                else
                {
                    showAlertSingleButtonError(getResources().getString(R.string.NoDataConnectionFullMsg));
                    return;

                }
                strSelection.closeList = 0;
                strSelection.whereTo = "11";
                //checkbuttonclick=1;

                // ////System.out.println("closeList: "+closeList);
                // chk if flag 2/3 found
                dbengine.open();
                strSelection.StoreList2Procs = dbengine.ProcessStoreReq();

                // int picsCHK = dbengine.getExistingPicNosOnRemStore();
                // String[] sIDs2Alert =
                // dbengine.getStoreNameExistingPicNosOnRemStore();

                if (strSelection.StoreList2Procs.length != 0) {// && picsCHK <= 0

                    strSelection.midPart();
                    strSelection.dayEndCustomAlert(2);
                    //showPendingStorelist(2);

                } else if (dbengine.GetLeftStoresChk() == true) {// && picsCHK
                    // <= 0

                    //enableGPSifNot();

                    strSelection.showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
                    //showChangeRouteConfirm();
                }
                else {
                    // show dialog for clear..clear + tranx to launcher
                    //showChangeRouteConfirmWhenNoStoreisLeftToSubmit();
                    strSelection.DayEndWithoutalert();
                    //showChangeRouteConfirm();
                }

                dbengine.close();
                dialog.dismiss();
            }
        });


        butn_refresh_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                butn_refresh_data.setBackgroundColor(Color.GREEN);
                clickCheck="refresh";
                dialog.dismiss();
                if(isOnline())
                {
                    AlertDialog.Builder alertDialogBuilderNEw11 = new AlertDialog.Builder(DialogActivity_MenuBar.this);
                    alertDialogBuilderNEw11.setTitle(getResources().getString(R.string.genTermNoDataConnection));
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
                                //new getStore.execute();
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
                                    finish();
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

            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        saveLocale(language);
        // updateTexts();
        //you can refresh or you can settext
        Intent refresh = new Intent(DialogActivity_MenuBar.this, AllButtonActivity.class);
        startActivity(refresh);
        finish();

    }

    public void saveLocale(String lang)
    {
        SharedPreferences prefs = getSharedPreferences("LanguagePref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }

    @Override
    public void finish() {
        if(dialog != null) {
            dialog.dismiss();
        }
        super.finish();
    }

   class GetStoresForDay extends AsyncTask<Void, Void, Void>
    {
        ServiceWorker newservice = new ServiceWorker();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            strSelection.mProgressDialog.show();

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
                                strSelection.serviceException=true;
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
                                strSelection.serviceException=true;
                                break;
                            }
                        }
                    }
                    if(mm==3)
                    {
                        newservice = newservice.getCategory(getApplicationContext(), imei);
                        if(newservice.flagExecutedServiceSuccesfully!=3)
                        {
                            strSelection.serviceException=true;
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
                            strSelection.serviceException=true;
                            break;
                        }
                    }

                    if(mm==5)
                    {
                        dbengine.open();
                        strSelection.hmapStoreIdSstat=dbengine.checkForStoreIdSstat();

                        dbengine.close();
                        newservice = newservice.getallStores(getApplicationContext(), fDate, imei, rID,RouteType);
                        if(newservice.flagExecutedServiceSuccesfully!=1)
                        {
                            strSelection.serviceException=true;
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
                            strSelection.serviceException=true;
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

            if(strSelection.mProgressDialog != null)
            {
                if(strSelection.mProgressDialog.isShowing())
                {
                    strSelection.mProgressDialog.dismiss();
                }
            }

            if(strSelection.serviceException)
            {
                showAlertException(getResources().getString(R.string.txtError),getResources().getString(R.string.txtErrRetrieving));
                strSelection.serviceException=false;
            }

          /*  strSelection.tl2.removeAllViews();
            strSelection.setStoresList();
            */

            Intent i=new Intent(DialogActivity_MenuBar.this,StoreSelection.class);
            i.putExtra("imei",imei);
            i.putExtra("pickerDate",pickerDate);
            i.putExtra("userDate",userDate);
            startActivity(i);
            finish();
        }

        public void showAlertException(String title,String msg)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DialogActivity_MenuBar.this);

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
    }

    class GetInvoiceForDay extends AsyncTask<Void, Void, Void>
    {
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

                        if(!newservice.director.trim().equals("1"))
                        {
                            if(strSelection.chkFlgForErrorToCloseApp==0)
                            {
                                strSelection.chkFlgForErrorToCloseApp=1;
                            }
                        }
                    }
                    if(mm==2)
                    {
                        newservice = newservice.callInvoiceButtonProductMstr(getApplicationContext(), fDate, imei, rID);

                        if(!newservice.director.trim().equals("1"))
                        {
                            if(strSelection.chkFlgForErrorToCloseApp==0)
                            {
                                strSelection.chkFlgForErrorToCloseApp=1;
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
            Date currDate = new Date();
            SimpleDateFormat currDateFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);

            String currSysDate = currDateFormat.format(currDate);
            Intent storeIntent = new Intent(DialogActivity_MenuBar.this, InvoiceStoreSelection.class);
            storeIntent.putExtra("imei", imei);
            storeIntent.putExtra("userDate", currSysDate);
            storeIntent.putExtra("pickerDate", fDate);

            if(strSelection.chkFlgForErrorToCloseApp==0)
            {
                strSelection.chkFlgForErrorToCloseApp=0;
                startActivity(storeIntent);
                // finish();
            }
            else
            {
                android.support.v7.app.AlertDialog.Builder alertDialogNoConn = new android.support.v7.app.AlertDialog.Builder(DialogActivity_MenuBar.this);
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
                                strSelection.chkFlgForErrorToCloseApp=0;
                            }
                        });
                alertDialogNoConn.setIcon(R.drawable.info_ico);
                android.support.v7.app.AlertDialog alert = alertDialogNoConn.create();
                alert.show();
            }
        }
    }


}
