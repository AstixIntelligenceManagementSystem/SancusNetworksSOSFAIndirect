package project.astix.com.sancusnetworkssosfaindirect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astix.Common.CommonDatabaseAssistant;
import com.astix.Common.CommonInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DistributorTargetActivity extends BaseActivity
{
    private TextView spnr_MnthNames;
    private TextView txt_note;
    private Button btn_submit;
    private ImageView btn_bck;
    private LinearLayout ll_inflateParent;
    private LinearLayout ll_MnthDrpDwn;
    private LinearLayout ll_headerBrand;

    private LinkedHashMap<String,ArrayList<String>> hmapDbrIdAndBrandId;
    private LinkedHashMap<String,String> hmapMnthIdAndName;
    private LinkedHashMap<String,String> hmapMeasureIdAndName;
    private final LinkedHashMap<String,ArrayList<String>> hmapMappingDbrIdBrandId=new LinkedHashMap<>();
    private LinkedHashMap<String,String> hmapSavedIdAndValue=new LinkedHashMap<>();
    private LinkedHashMap<String,Integer> hmapDbrIdNameAndMinValue=new LinkedHashMap<>();
    private LinkedHashMap<String,String> hmapDbrIdAndName;

    private final DBAdapterKenya dbengine=new DBAdapterKenya(DistributorTargetActivity.this);

    private String imei;
    private int chkFlgForErrorToCloseApp=0;
    private Integer selectedMonthID=00;
    private Integer selectedMeasureID=0;

    private String newfullFileName;
    //private final DatabaseAssistantDistributorTarget DA=new DatabaseAssistantDistributorTarget(this);
    private CommonDatabaseAssistant DA=null;
    private String xmlFileName;
    private final String[] xmlForWeb = new String[1];
    private int serverResponseCode = 0;
    private int flgEditableStatus=0;

    ImageView exit_btn;
    TextView spnr_Measures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_target);

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if(CommonInfo.imei.trim().equals(null) || CommonInfo.imei.trim().equals(""))
        {
            imei = tManager.getDeviceId();
            CommonInfo.imei=imei;
        }
        else
        {
            imei= CommonInfo.imei.trim();
        }

        initializeViews();

        getDatabaseData();
        spinnerMnthSelection();
        spinnerMeasureSelection();
    }

    private void initializeViews()
    {
        txt_note= (TextView) findViewById(R.id.txt_note);
        txt_note.setText(R.string.TargetNote);

        spnr_Measures= (TextView) findViewById(R.id.spnr_Measures);
        exit_btn= (ImageView) findViewById(R.id.exit_btn);
        ll_MnthDrpDwn= (LinearLayout) findViewById(R.id.ll_MnthDrpDwn);
        spnr_MnthNames= (TextView) findViewById(R.id.spnr_MnthNames);
        ll_headerBrand= (LinearLayout) findViewById(R.id.ll_headerBrand);

        btn_submit= (Button) findViewById(R.id.btn_save);
        btn_bck= (ImageView) findViewById(R.id.btn_bck);
        ll_inflateParent= (LinearLayout) findViewById(R.id.ll_inflateParent);

        submitBtnClickByHmap();
        backBtnClick();
        exitBtnClick();

    }

    private void getDatabaseData()
    {
        hmapMnthIdAndName=dbengine.fetchTargetMnthPlan();
        hmapMeasureIdAndName=dbengine.fetchTargetMeasureMstr();
        hmapSavedIdAndValue=dbengine.getSavedtargetData();
    }

    private void exitBtnClick()
    {
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(DistributorTargetActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.AlertDialogHeaderMsg));
                alertDialog.setMessage(getResources().getString(R.string.AreYouSureExit));
                alertDialog.setPositiveButton(getResources().getString(R.string.AlertDialogYesButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAndRemoveTask();
                    }
                });

                alertDialog.setNegativeButton(getResources().getString(R.string.AlertDialogNoButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                    }
                });

                AlertDialog alert=alertDialog.create();
                alert.show();
            }
        });
    }

    private void backBtnClick()
    {
       // btn_bck.setVisibility(View.INVISIBLE);
        btn_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(DistributorTargetActivity.this,AllButtonActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void submitBtnClickByHmap()
    {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(selectedMonthID == 00)
                {
                    showAlertSingleButtonError(getResources().getString(R.string.SelectTargetMnth));
                    return;
                }
                else if(validate())
                {
                   if(hmapSavedIdAndValue != null && hmapSavedIdAndValue.size()>0)
                    {
                        dbengine.open();
                        dbengine.deletetblSalesTargetSavingDetail(String.valueOf(selectedMonthID),String.valueOf(selectedMeasureID));
                        for(Map.Entry<String,String> entry:hmapSavedIdAndValue.entrySet())
                        {
                            String distrbtrId=entry.getKey().split(Pattern.quote("^"))[0];
                            String distrbtrNodeType=entry.getKey().split(Pattern.quote("^"))[1];
                            String brandID=entry.getKey().split(Pattern.quote("^"))[2];
                            String brandNodeType=entry.getKey().split(Pattern.quote("^"))[3];
                            String hmapSelectedMonth=entry.getKey().split(Pattern.quote("^"))[4];
                            String hmapSelectedMeasure=entry.getKey().split(Pattern.quote("^"))[5];

                            if(selectedMonthID == Integer.parseInt(hmapSelectedMonth) && selectedMeasureID == Integer.parseInt(hmapSelectedMeasure))
                            {
                                String enteredValue=entry.getValue().split(Pattern.quote("^"))[0];
                                if(!TextUtils.isEmpty(enteredValue))
                                {
                                    if(isOnline())
                                    {
                                        dbengine.savetblSalesTargetSavingDetail(selectedMonthID,selectedMeasureID,distrbtrId,distrbtrNodeType,brandID,brandNodeType,enteredValue,3);
                                    }
                                    else
                                    {
                                        dbengine.savetblSalesTargetSavingDetail(selectedMonthID,selectedMeasureID,distrbtrId,distrbtrNodeType,brandID,brandNodeType,enteredValue,1);
                                    }
                                    System.out.println("SAVING..."+selectedMonthID+"--"+selectedMeasureID+"--"+distrbtrId+"--"+distrbtrNodeType+"--"+brandID+"--"+brandNodeType+"--"+enteredValue);
                                }
                            }
                        }
                        dbengine.close();
                        if(isOnline())
                        {
                            try
                            {
                                String coverageNOdeIDType=dbengine.fnGetPersonNodeIDAndPersonNodeTypeForSO();
                                String coverageNodeID=coverageNOdeIDType.split(Pattern.quote("^"))[0];
                                String coverageNodeType=coverageNOdeIDType.split(Pattern.quote("^"))[1];

                                Date date1 = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.ENGLISH);
                                String fDate = sdf.format(date1).trim();

                                dbengine.savetblSalesTargetUserDetails(imei,selectedMonthID,selectedMeasureID,coverageNodeID,coverageNodeType,fDate,3);
                                System.out.println("USER DETAIL..."+imei+"--"+selectedMonthID+"--"+coverageNodeID+"--"+coverageNodeType+"--"+fDate);

                                FullSyncDataNow task = new FullSyncDataNow(DistributorTargetActivity.this);
                                task.execute();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            showAlertSingleButtonInfo(getResources().getString(R.string.EnsureInternet));
                        }
                    }
                }
            }
        });
    }

    private void spinnerMnthSelection()
    {
        if(hmapMnthIdAndName != null && hmapMnthIdAndName.size()>0)
        {
            if(hmapMnthIdAndName.size() == 2)
            {
                int i=0;
                for(Map.Entry<String,String> entry:hmapMnthIdAndName.entrySet())
                {
                    String monthId = entry.getKey();
                    String monthName = entry.getValue();

                    if(i == 1)
                    {
                        if(isOnline() && selectedMeasureID != 0) // both selected
                        {
                            ll_inflateParent.removeAllViews();
                            ll_headerBrand.removeAllViews();
                            spnr_MnthNames.setText(monthName);
                            selectedMonthID=Integer.parseInt(monthId);
                            FetchDistrbtrTargetData fetchData=new FetchDistrbtrTargetData();
                            fetchData.execute();
                        }
                        else if(isOnline() && selectedMeasureID == 0) //measure not selected
                        {
                            ll_inflateParent.removeAllViews();
                            ll_headerBrand.removeAllViews();
                            spnr_MnthNames.setText(monthName);
                            selectedMonthID=Integer.parseInt(monthId);
                        }
                        else
                        {
                            spnr_MnthNames.setText(monthName);
                            selectedMonthID=00;
                            ll_headerBrand.setVisibility(View.GONE);
                            ll_inflateParent.setVisibility(View.GONE);
                            Toast.makeText(DistributorTargetActivity.this, getResources().getString(R.string.txtErrorInternetConnection), Toast.LENGTH_SHORT).show();
                        }
                    }
                    i++;
                }
            }
        }

        spnr_MnthNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(DistributorTargetActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.custom_alert_for_target, null);
                dialog.setView(convertView);

                TextView text= (TextView) convertView.findViewById(R.id.text);
                text.setText(getText(R.string.SelectAlertTargetMnth));

                LinearLayout ll_spinner= (LinearLayout) convertView.findViewById(R.id.ll_radio_spinner);
                final AlertDialog alert=dialog.create();
                alert.show();

                monthSpinnerAlert(alert,ll_spinner);
            }
        });
    }

    private void spinnerMeasureSelection()
    {
        spnr_Measures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog=new AlertDialog.Builder(DistributorTargetActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.custom_alert_for_target, null);
                dialog.setView(convertView);

                TextView text= (TextView) convertView.findViewById(R.id.text);
                text.setText(getText(R.string.SelectAlertTargetMeasure));

                LinearLayout ll_spinner= (LinearLayout) convertView.findViewById(R.id.ll_radio_spinner);
                final AlertDialog alert=dialog.create();
                alert.show();

                measureSpinnerAlert(alert,ll_spinner);
            }
        });
    }

    private void monthSpinnerAlert(final AlertDialog alert, LinearLayout ll_spinner)
    {
        if(hmapMnthIdAndName != null && hmapMnthIdAndName.size()>0)
        {
            LayoutInflater inflater1 = getLayoutInflater();
            for(Map.Entry<String,String> entry:hmapMnthIdAndName.entrySet())
            {
                String monthId=entry.getKey();
                String monthName=entry.getValue();

                View rowView = inflater1.inflate(R.layout.list_item, null);
                final TextView month_name= (TextView) rowView.findViewById(R.id.product_name);
                month_name.setTag(monthId);
                month_name.setText(monthName);

                month_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        TextView tv= (TextView) view;
                        if(!tv.getText().toString().equals("Select Target Month"))
                        {
                            if(isOnline() && selectedMeasureID != 0) // both selected
                            {
                                ll_inflateParent.removeAllViews();
                                ll_headerBrand.removeAllViews();
                                spnr_MnthNames.setText(tv.getText().toString());
                                selectedMonthID=Integer.parseInt(tv.getTag().toString());
                                FetchDistrbtrTargetData fetchData=new FetchDistrbtrTargetData();
                                fetchData.execute();
                            }
                            else if(isOnline() && selectedMeasureID == 0) //measure not selected
                            {
                                ll_inflateParent.removeAllViews();
                                ll_headerBrand.removeAllViews();
                                spnr_MnthNames.setText(tv.getText().toString());
                                selectedMonthID=Integer.parseInt(tv.getTag().toString());
                            }
                            else
                            {
                                spnr_MnthNames.setText("Select Target Month");
                                selectedMonthID=00;
                                btn_submit.setVisibility(View.GONE);
                                ll_headerBrand.setVisibility(View.GONE);
                                ll_inflateParent.setVisibility(View.GONE);
                                Toast.makeText(DistributorTargetActivity.this, getResources().getString(R.string.txtErrorInternetConnection), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            selectedMonthID=00;
                            spnr_MnthNames.setText(tv.getText().toString());
                            btn_submit.setVisibility(View.GONE);
                            ll_headerBrand.removeAllViews();
                            ll_inflateParent.setVisibility(View.INVISIBLE);
                            ll_inflateParent.removeAllViews();
                        }
                        alert.dismiss();
                    }
                });
                ll_spinner.addView(rowView);
            }
        }
    }

    private void measureSpinnerAlert(final AlertDialog alert, LinearLayout ll_spinner)
    {
        if(hmapMeasureIdAndName != null && hmapMeasureIdAndName.size()>0)
        {
            LayoutInflater inflater1 = getLayoutInflater();
            for(Map.Entry<String,String> entry:hmapMeasureIdAndName.entrySet())
            {
                String measureId=entry.getKey();
                String measureName=entry.getValue().split(Pattern.quote("^"))[0];
                String flgActive=entry.getValue().split(Pattern.quote("^"))[1];

                View rowView = inflater1.inflate(R.layout.list_item, null);
                final TextView month_name= (TextView) rowView.findViewById(R.id.product_name);
                month_name.setTag(measureId);
                month_name.setText(measureName);

                month_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        TextView tv= (TextView) view;
                        if(!tv.getText().toString().equals("Select Measure"))
                        {
                            if(isOnline() && selectedMonthID != 00) //both selected
                            {
                                ll_inflateParent.removeAllViews();
                                ll_headerBrand.removeAllViews();
                                spnr_Measures.setText(tv.getText().toString());
                                selectedMeasureID=Integer.parseInt(tv.getTag().toString());
                                FetchDistrbtrTargetData fetchData=new FetchDistrbtrTargetData();
                                fetchData.execute();
                            }
                            else if(isOnline() && selectedMonthID == 00) //date not selected
                            {
                                ll_inflateParent.removeAllViews();
                                ll_headerBrand.removeAllViews();
                                spnr_Measures.setText(tv.getText().toString());
                                selectedMeasureID=Integer.parseInt(tv.getTag().toString());
                            }
                            else
                            {
                                spnr_Measures.setText("Select Measure");
                                selectedMeasureID=0;
                                btn_submit.setVisibility(View.GONE);
                                ll_headerBrand.setVisibility(View.GONE);
                                ll_inflateParent.setVisibility(View.GONE);
                                Toast.makeText(DistributorTargetActivity.this, getResources().getString(R.string.txtErrorInternetConnection), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            selectedMeasureID=0;
                            spnr_Measures.setText(tv.getText().toString());
                            btn_submit.setVisibility(View.GONE);
                            ll_headerBrand.removeAllViews();
                            ll_inflateParent.setVisibility(View.INVISIBLE);
                            ll_inflateParent.removeAllViews();
                        }
                        alert.dismiss();
                    }
                });
                ll_spinner.addView(rowView);
            }
        }
    }

    class FetchDistrbtrTargetData extends AsyncTask<Integer,Void,Void>
    {
        ProgressDialog pDialogGetStores;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialogGetStores=new ProgressDialog(DistributorTargetActivity.this);
            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getResources().getString(R.string.genTermLoadData));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();

        }

        @Override
        protected Void doInBackground(Integer... objects)
        {
            ServiceWorker newservice = new ServiceWorker();

            newservice=newservice.getPDASalesAreaTargetDetail(getApplicationContext(),selectedMonthID, imei,selectedMeasureID);
            if (!newservice.director.trim().equals("1")) {
                if (chkFlgForErrorToCloseApp == 0) {
                    chkFlgForErrorToCloseApp = 1;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
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
                hmapDbrIdAndName=dbengine.fetchSalesTargetDbrIDAndName();
                hmapDbrIdAndBrandId=dbengine.fetchtblSalesAreaTargetDetail();
                flgEditableStatus=dbengine.fetchtblSalesTargetFlg();
                btn_submit.setVisibility(View.GONE);
                inflateDbrDependntData();

                //this condition is for if data is sent to server but xml processing takes time,
                // then for that time data gets locked and cannot be edited.
                if(hmapSavedIdAndValue !=null && hmapSavedIdAndValue.size()>0)
                {
                    Boolean isSubmitted=false;
                    for(Map.Entry<String,String> entry:hmapSavedIdAndValue.entrySet())
                    {
                        String clickdMonth=entry.getKey().split(Pattern.quote("^"))[4];
                        String clickdMeasure=entry.getKey().split(Pattern.quote("^"))[5];
                        String value=entry.getValue().split(Pattern.quote("^"))[1];
                        if(Integer.parseInt(clickdMonth) == selectedMonthID && Integer.parseInt(clickdMeasure) == selectedMeasureID)
                        {
                            if(value.equals("4")) //Sstat=4 is checked
                            {
                                isSubmitted=true;
                                System.out.println("DATA FETCHED WITH SSTAT=4");
                            }
                        }
                        else
                        {
                            btn_submit.setVisibility(View.GONE);
                        }
                    }
                    if(isSubmitted)
                    {
                        btn_submit.setVisibility(View.GONE);
                        for(int i=0;i<ll_inflateParent.getChildCount();i++)
                        {
                            View child=ll_inflateParent.getChildAt(i);
                            if(child instanceof LinearLayout)
                            {
                                LinearLayout llParent= (LinearLayout) child;
                                for(int j=0;j<llParent.getChildCount();j++)
                                {
                                    if(llParent.getChildAt(j) instanceof EditText)
                                    {
                                        EditText editText= (EditText) llParent.getChildAt(j);
                                        editText.setEnabled(false);
                                        System.out.println("Found EditText WITH SSTAT=4");
                                    }
                                }
                            }
                        }
                    }
                }
                else      //coming for the first time with no filled values
                {
                    btn_submit.setVisibility(View.GONE);
                }
            }
        }
    }

    private void inflateDbrDependntData()
    {
        if(hmapDbrIdAndBrandId != null && hmapDbrIdAndBrandId.size()>0)
        {
            int position=0;
            Boolean isHeaderCreated=false,isEditable=true,isMinTargetVal=false;
            for(Map.Entry<String,ArrayList<String>> entry:hmapDbrIdAndBrandId.entrySet())
            {
                ArrayList<String> list_BrandIds = new ArrayList<>();

                String dbrNodeID=entry.getKey().split(Pattern.quote("^"))[0];
                String dbrNodeType=entry.getKey().split(Pattern.quote("^"))[1];
                String dbrName=entry.getKey().split(Pattern.quote("^"))[2];

                final ArrayList<String> list_brandID=entry.getValue();

                if(!isHeaderCreated)
                {
                    for(int i=0;i<list_brandID.size();i++)
                    {
                        String brandName=list_brandID.get(i).split(Pattern.quote("^"))[2];
                        if (i == 0)
                        {
                            TextView hiddenTxtView = createTextView(2f, "", true);
                            hiddenTxtView.setVisibility(View.INVISIBLE);
                            ll_headerBrand.addView(hiddenTxtView);
                        }

                        TextView TxtView = createTextView(1f, brandName, true);
                        TxtView.setTextSize(14);
                        TxtView.setTypeface(null, Typeface.BOLD);
                        ll_headerBrand.addView(TxtView);
                        if(ll_headerBrand.getVisibility() == View.GONE || ll_headerBrand.getVisibility() == View.INVISIBLE)
                        {
                            ll_headerBrand.setVisibility(View.VISIBLE);
                        }
                        isHeaderCreated=true;
                    }
                }

                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.inflate_dbrtarget_header, null);

                LinearLayout ll_wholeParent= (LinearLayout) convertView.findViewById(R.id.ll_wholeParent);
                if(position != 0)
                {
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,6,0,0);
                    ll_wholeParent.setLayoutParams(params);
                }
                ll_wholeParent.setTag(dbrNodeID+"^"+dbrNodeType);

                if(position%2==0)
                {
                    ll_wholeParent.setBackgroundResource(R.drawable.card_background_graytarget);
                }
                else
                {
                    ll_wholeParent.setBackgroundResource(R.drawable.card_background_lightgray);
                }

                TextView tv=createTextView(2f,dbrName,false);
                tv.setTextSize(13);
                tv.setMinLines(2);
                tv.setGravity(Gravity.LEFT|Gravity.CENTER);
                tv.setPadding(3,3,0,3);
                ll_wholeParent.addView(tv);

                if(list_brandID != null && list_brandID.size()>0)
                {
                    for(int j=0;j<list_brandID.size();j++)
                    {
                        String brandID=list_brandID.get(j).split(Pattern.quote("^"))[0];
                        String brandNodeType=list_brandID.get(j).split(Pattern.quote("^"))[1];
                        String targetValue=list_brandID.get(j).split(Pattern.quote("^"))[3];
                        String tag=dbrNodeID+"^"+dbrNodeType+"^"+brandID+"^"+brandNodeType+"^"+selectedMonthID+"^"+selectedMeasureID;

                        if(targetValue.equals("0") && brandID.equals("0") && brandNodeType.equals("0")) {
                            targetValue="0";
                        }
                        else if(targetValue.equals("0") && !brandID.equals("0") && !brandNodeType.equals("0")) {
                            targetValue="";
                        }
                        else {
                            if(!brandID.equals("0") && !brandNodeType.equals("0")) {
                                hmapSavedIdAndValue.put(tag,targetValue+"^"+"1");
                            }
                        }

                        if(flgEditableStatus == 1)
                        {
                            if(brandID.equals("0") && brandNodeType.equals("0"))
                            {
                                isMinTargetVal=true;
                            }
                            else
                            {
                                isMinTargetVal=false;
                            }
                            isEditable=false;
                            btn_submit.setVisibility(View.GONE);
                        }
                        else
                        {
                            if(brandID.equals("0") && brandNodeType.equals("0"))
                            {
                                isEditable=false;
                                isMinTargetVal=true;
                            }
                            else
                            {
                                isEditable=true;
                                isMinTargetVal=false;
                            }
                            btn_submit.setVisibility(View.VISIBLE);
                        }

                        if(hmapSavedIdAndValue !=null && hmapSavedIdAndValue.containsKey(tag))
                        {
                            String value=hmapSavedIdAndValue.get(tag).split(Pattern.quote("^"))[0];
                            btn_submit.setVisibility(View.VISIBLE);
                            targetValue=value;
                        }

                        list_BrandIds.add(brandID+"^"+brandNodeType);

                        if(j != 0)
                        {
                            ll_wholeParent.addView(createEditText(1f,tag,true,targetValue,isEditable,isMinTargetVal));
                        }
                        else
                        {
                            ll_wholeParent.addView(createEditText(1f,tag,false,targetValue,isEditable,isMinTargetVal));
                        }
                    }
                }
                hmapMappingDbrIdBrandId.put(dbrNodeID+"^"+dbrNodeType,list_BrandIds);
                ll_inflateParent.addView(convertView);
                ll_inflateParent.setVisibility(View.VISIBLE);
                position++;
            }
        }
    }

    private boolean validate()
    {
        if(hmapSavedIdAndValue != null && hmapSavedIdAndValue.size()>0)
        {
            int sum=0;
            String previousDbrID="NA";
            for(Map.Entry<String,String> entry:hmapSavedIdAndValue.entrySet())
            {
                String distrbtrId = entry.getKey().split(Pattern.quote("^"))[0];
                String distrbtrNodeType = entry.getKey().split(Pattern.quote("^"))[1];
                String brandID=entry.getKey().split(Pattern.quote("^"))[2];
                String brandNodeType=entry.getKey().split(Pattern.quote("^"))[3];
                String hmapSelectedMonth=entry.getKey().split(Pattern.quote("^"))[4];
                String hmapSelectedMeasure=entry.getKey().split(Pattern.quote("^"))[5];

                String keyForMinVal=distrbtrId+"^"+distrbtrNodeType+"^"+hmapSelectedMonth+"^"+hmapSelectedMeasure;

                if(selectedMonthID == Integer.parseInt(hmapSelectedMonth) &&
                        selectedMeasureID == Integer.parseInt(hmapSelectedMeasure))
                {
                    String enteredValue = entry.getValue().split(Pattern.quote("^"))[0];

                    //if-else condition to calculate sum of values entered in a single distrbtr
                    if(previousDbrID.equals("NA"))
                    {
                        previousDbrID=distrbtrId;
                        sum = sum + Integer.parseInt(enteredValue);
                        hmapDbrIdNameAndMinValue.put(keyForMinVal,sum);
                    }
                    else if(previousDbrID.equals(distrbtrId))
                    {
                        sum = sum + Integer.parseInt(enteredValue);
                        hmapDbrIdNameAndMinValue.put(keyForMinVal,sum);
                    }
                    else
                    {
                        previousDbrID=distrbtrId;
                        sum=0;
                        sum = sum + Integer.parseInt(enteredValue);
                        hmapDbrIdNameAndMinValue.put(keyForMinVal,sum);
                    }
                }
            }

            if(hmapDbrIdNameAndMinValue != null && hmapDbrIdNameAndMinValue.size()>0)
            {
                for(Map.Entry<String,Integer> entryOne:hmapDbrIdNameAndMinValue.entrySet())
                {
                    int minValue=0;
                    String tagKey=entryOne.getKey();
                    Integer sumValue=entryOne.getValue();

                    String distrbtrId = tagKey.split(Pattern.quote("^"))[0];
                    String distrbtrNodeType = tagKey.split(Pattern.quote("^"))[1];
                    String hmapSelectedMonth=tagKey.split(Pattern.quote("^"))[2];
                    String hmapSelectedMeasure=tagKey.split(Pattern.quote("^"))[3];

                    String keyForMinVal=distrbtrId+"^"+distrbtrNodeType+"^"+"0"+"^"+"0"+"^"+hmapSelectedMonth+"^"+hmapSelectedMeasure;
                    EditText et= (EditText) ll_inflateParent.findViewWithTag(keyForMinVal);
                    if(et != null)
                    {
                        minValue=Integer.parseInt(et.getText().toString().trim());
                    }

                    if(sumValue < minValue)
                    {
                        showValidationError(distrbtrId+"^"+distrbtrNodeType,minValue);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void showValidationError(String dbrID,int minimumTarget)
    {
        String dbrName="";
        if(hmapDbrIdAndName != null && hmapDbrIdAndName.containsKey(dbrID))
        {
            dbrName=hmapDbrIdAndName.get(dbrID);
        }

        AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(DistributorTargetActivity.this);
        alertDialogSyncError.setTitle(getText(R.string.AlertDialogHeaderErrorMsg));
        alertDialogSyncError.setCancelable(false);

        alertDialogSyncError.setMessage(getText(R.string.validateTargetMin)+" "+dbrName+" "+getText(R.string.validateTargetMinVal)+" "+minimumTarget);

        alertDialogSyncError.setNeutralButton(getText(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogSyncError.setIcon(R.drawable.sync_error_ico);
        AlertDialog alert = alertDialogSyncError.create();
        alert.show();
    }

    private TextView createTextView(Float weight, String text, Boolean isHeader)
    {
        TextView tv=new TextView(DistributorTargetActivity.this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,weight);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.BLACK);
        if(isHeader)
        {
            tv.setPadding(0,1,0,1);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.border_allside);
        }
        return  tv;
    }

    private EditText createEditText(Float weight, final String tag, Boolean isMargin, String targetValue, Boolean isEdit, final Boolean isMinTargetVal)
    {
        final EditText editText=new EditText(DistributorTargetActivity.this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,weight);
        if(isMargin)
        {
            params.setMargins(2,0,0,0);
        }
        else
        {
            params.setMargins(0,0,2,0);
        }
        editText.setLayoutParams(params);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setTag(tag);
        editText.setText(targetValue);
        editText.setTextSize(12);
        editText.setPadding(0,2,0,2);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundResource(R.drawable.edit_text_diable_bg_transprent);

        if(!isEdit)
        {
            editText.setEnabled(false);
            btn_submit.setVisibility(View.GONE);
        }
        else
        {
            btn_submit.setVisibility(View.VISIBLE);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String editTextValue=editText.getText().toString().trim();
                if(TextUtils.isEmpty(editTextValue))
                {
                    String dbrID = tag.split(Pattern.quote("^"))[0];
                    String dbrNodeType =tag.split(Pattern.quote("^"))[1];
                    String hmapSelectedMonth = tag.split(Pattern.quote("^"))[4];
                    String hmapSelectedMeasure = tag.split(Pattern.quote("^"))[5];

                    hmapSavedIdAndValue.remove(tag);
                    hmapDbrIdNameAndMinValue.remove(dbrID+"^"+dbrNodeType+"^"+hmapSelectedMonth+"^"+hmapSelectedMeasure);
                }
                else if(isMinTargetVal)
                {

                }
                else
                {
                    hmapSavedIdAndValue.put(tag,editTextValue+"^"+"1");
                }

                if(hmapSavedIdAndValue != null && hmapSavedIdAndValue.size()>0)
                {
                    if(!hmapSavedIdAndValue.containsKey(tag)) //if value is entered for other brand then also btn should be visible
                    {
                        for(Map.Entry<String,String> entry:hmapSavedIdAndValue.entrySet())
                        {
                            String hmapSelectedMonth = entry.getKey().split(Pattern.quote("^"))[4];
                            String hmapSelectedMeasure = entry.getKey().split(Pattern.quote("^"))[5];

                            if(Integer.parseInt(hmapSelectedMonth) == selectedMonthID && Integer.parseInt(hmapSelectedMeasure) == selectedMeasureID)
                            {
                                btn_submit.setVisibility(View.VISIBLE);
                                break;
                            }
                            else
                            {
                                btn_submit.setVisibility(View.GONE);
                            }
                        }
                    }
                    else
                    {
                        btn_submit.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    btn_submit.setVisibility(View.GONE);
                }
            }
        });

        return  editText;
    }

    private class FullSyncDataNow extends AsyncTask<Void, Void, Void>
    {
        final ProgressDialog pDialogGetStores;

        public FullSyncDataNow(DistributorTargetActivity activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getResources().getString(R.string.SubmittingTarget));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            long  syncTIMESTAMP = System.currentTimeMillis();
            Date dateobj = new Date(syncTIMESTAMP);
            SimpleDateFormat df1 = new SimpleDateFormat("dd.MMM.yyyy.HH.mm.ss",Locale.ENGLISH);
            newfullFileName=CommonInfo.imei+"."+ df1.format(dateobj);

            try
            {
                File LTFoodxmlFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);
                if (!LTFoodxmlFolder.exists())
                {
                    LTFoodxmlFolder.mkdirs();
                }

                ArrayList<String> globalList_ForXml=new ArrayList<>();

                DA=new CommonDatabaseAssistant(DistributorTargetActivity.this, CommonInfo.OrderXMLFolder);

                globalList_ForXml.add("tblSalesTargetUserDetails"+"^"+"IMEINo,MonthID,MeasureID,SalesAreaId,SalesAreaNodeType,DateTime");
                globalList_ForXml.add("tblSalesTargetSavingDetail"+"^"+"TargetLevelNodeID,TargetLevelNodeType,PrdNodeID,PrdNodeType,MeasureID,targetValue");

                DA.open();
                DA.export(CommonInfo.DATABASE_NAME, newfullFileName,globalList_ForXml);
                DA.close();

                dbengine.savetbl_XMLfiles(newfullFileName, "3","6");   // 6 for Sales Target
                dbengine.open();
                dbengine.UpdateTargetTblSstatByFlg("tblSalesTargetSavingDetail",4);
                dbengine.UpdateTargetTblSstat("tblSalesTargetUserDetails",4);
                dbengine.close();

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
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialogGetStores.isShowing())
            {
                pDialogGetStores.dismiss();
            }

            try
            {
                xmlForWeb[0]=Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + newfullFileName + ".xml";
                SyncXMLfileData task2 = new SyncXMLfileData(DistributorTargetActivity.this);
                task2.execute();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class SyncXMLfileData extends AsyncTask<Void, Void, Integer>
    {
        final ProgressDialog pDialogGetStores;

        public SyncXMLfileData(DistributorTargetActivity activity)
        {
            pDialogGetStores = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            File OrderXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!OrderXMLFolder.exists())
            {
                OrderXMLFolder.mkdirs();
            }

            pDialogGetStores.setTitle(getText(R.string.genTermPleaseWaitNew));
            pDialogGetStores.setMessage(getResources().getString(R.string.SubmittingTargetDetails));
            pDialogGetStores.setIndeterminate(false);
            pDialogGetStores.setCancelable(false);
            pDialogGetStores.setCanceledOnTouchOutside(false);
            pDialogGetStores.show();

        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            try
            {
                String xmlfileNames = dbengine.fnGetXMLFile("3","6");

                int index=0;
                if(!xmlfileNames.equals(""))
                {
                    String[] xmlfileArray = xmlfileNames.split(Pattern.quote("^"));
                    for (String aXmlfileArray : xmlfileArray) {
                        System.out.println("index" + index);
                        xmlFileName = aXmlfileArray;

                        String newzipfile = Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + xmlFileName + ".zip";
                        xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + xmlFileName + ".xml";

                        try {
                            zip(xmlForWeb, newzipfile);
                        } catch (Exception e1) {
                            e1.printStackTrace();
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

                        //String filetype="0";
                        String urlString = "0";
                        //filetype=dbengine.getfiletype(xmlFileName);

                        urlString = CommonInfo.OrderSyncPathDistributorTarget.trim() + "?CLIENTFILENAME=" + xmlFileName + ".xml";

                        try {
                            FileInputStream fileInputStream = new FileInputStream(file2send);
                            URL url = new URL(urlString);

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("zipFileName", xmlFileName);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                    + xmlFileName + "\"" + lineEnd);

                            dos.writeBytes(lineEnd);

                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            }

                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();

                            Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                            if (serverResponseCode == 200) {

                                dbengine.upDateTblXmlFile(xmlFileName);
                                dbengine.deleteXmlTable("4");

                                deleteViewdXml(CommonInfo.OrderXMLFolder + "/" + xmlFileName + ".xml");
                                deleteViewdXml(CommonInfo.OrderXMLFolder + "/" + xmlFileName + ".zip");
                                System.out.println("ShivamDELETE" + xmlFileName);
                            }

                            fileInputStream.close();
                            dos.flush();
                            dos.close();
                            index++;
                        } catch (MalformedURLException ex) {

                            pDialogGetStores.dismiss();
                            ex.printStackTrace();

                        } catch (Exception e) {
                            pDialogGetStores.dismiss();
                        }
                    }
                }
                else
                {
                    serverResponseCode=200;
                }
            }
            catch (Exception e) {

                e.printStackTrace();
            }
            return serverResponseCode;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            super.onPostExecute(result);
            if(!isFinishing())
            {
                if(pDialogGetStores.isShowing())
                {
                    pDialogGetStores.dismiss();
                }

                if(result!=200)
                {
                    showSyncError();
                }
                else
                {
                    if(pDialogGetStores.isShowing())
                    {
                        pDialogGetStores.dismiss();
                    }
                    showSyncSuccess();
                }
            }}
    }

    private void showSyncError() {
        AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(DistributorTargetActivity.this);
        alertDialogSyncError.setTitle(getText(R.string.genTermSyncErrornew));
        alertDialogSyncError.setCancelable(false);
        alertDialogSyncError.setMessage(getText(R.string.syncAlertErrMsg));

        alertDialogSyncError.setNeutralButton(getText(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                });
        alertDialogSyncError.setIcon(R.drawable.sync_error_ico);
        AlertDialog alert = alertDialogSyncError.create();
        alert.show();
    }

    private void showSyncSuccess() {
        AlertDialog.Builder alertDialogSyncError = new AlertDialog.Builder(DistributorTargetActivity.this);
        //alertDialogSyncError.setTitle(getText(R.string.syncAlertsubmit));
        alertDialogSyncError.setCancelable(false);
        alertDialogSyncError.setMessage(getText(R.string.syncAlertsubmit));

        alertDialogSyncError.setNeutralButton(getText(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Intent submitStoreIntent = new Intent(DistributorTargetActivity.this, DistributorTargetActivity.class);
                        startActivity(submitStoreIntent);
                        finish();
                    }
                });
        //alertDialogSyncError.setIcon(R.drawable.syncdata_icon);
        AlertDialog alert = alertDialogSyncError.create();
        alert.show();
    }

    private void deleteViewdXml(String file_dj_path)
    {
        File dir=   Environment.getExternalStorageDirectory();
        File fdelete=new File(dir,file_dj_path);
        // File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + file_dj_path);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + file_dj_path);
            }
        }
    }

    private void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }



    }

    private static void zip(String[] files, String zipFile) throws IOException
    {
        BufferedInputStream origin = null;
        final int BUFFER_SIZE = 2048;

        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try
        {
            byte data[] = new byte[BUFFER_SIZE];

            for (String file : files) {
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        }
        finally
        {
            out.close();
        }
    }

    /*void submitBtnClickByRows()
    {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Boolean isValidated=false;
                if(selectedMonthID == 00)
                {
                    showAlertSingleButtonError("Please select target month.");
                    return;
                }
                if(ll_inflateParent.getChildCount()>0)
                {
                    dbengine.open();
                    for(int i=0;i<ll_inflateParent.getChildCount();i++)
                    {
                        View view1=ll_inflateParent.getChildAt(i);
                        LinearLayout ll_parent= (LinearLayout) view1;
                        String dbrIDAndNodeType=ll_parent.getTag().toString();
                        String dbrID=dbrIDAndNodeType.split(Pattern.quote("^"))[0];
                        String dbrNodeType=dbrIDAndNodeType.split(Pattern.quote("^"))[1];

                        ArrayList<String> list_brands=hmapMappingDbrIdBrandId.get(dbrIDAndNodeType);
                        for(int j=0;j<list_brands.size();j++)
                        {
                            String brandIDAndNodeType=list_brands.get(j);
                            String brandID=brandIDAndNodeType.split(Pattern.quote("^"))[0];
                            String brandNodeType=brandIDAndNodeType.split(Pattern.quote("^"))[1];
                            EditText editText= (EditText) ll_parent.findViewWithTag(dbrIDAndNodeType+"^"+brandIDAndNodeType);
                            if(editText != null)
                            {
                                String enteredValue=editText.getText().toString();
                                if(!TextUtils.isEmpty(enteredValue))
                                {
                                    int enteredZero=Integer.parseInt(enteredValue);
                                    if(enteredZero != 0)
                                    {
                                        isValidated=true;
                                        dbengine.savetblSalesTargetSavingDetail(selectedMonthID,dbrID,dbrNodeType,brandID,brandNodeType,enteredValue,1);
                                        System.out.println("SAVING..."+selectedMonthID+"--"+dbrID+"--"+dbrNodeType+"--"+brandID+"--"+brandNodeType+"--"+enteredValue);
                                    }
                                }
                            }
                        }
                    }
                    dbengine.close();
                    if(!isValidated)
                    {
                        showAlertSingleButtonError("Please fill atleast one value.");
                        return;
                    }
                }
            }
        });
    }*/

}
