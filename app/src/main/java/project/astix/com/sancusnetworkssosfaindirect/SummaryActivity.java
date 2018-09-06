package project.astix.com.sancusnetworkssosfaindirect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astix.Common.CommonInfo;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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


public class SummaryActivity extends BaseActivity {
    public int chkFlgForErrorToCloseApp = 0;
    String imei;
    public String fDate;
    LinearLayout parentOfAllDynamicData;
    InputStream inputStream;
    LinkedHashMap<String, String> hmapDSRFromDataBase;
    public String SONameAndSummryLastServerRefreshTime = "";
    public TextView tv_SOName, tv_ServerLastRefreshTime, tv_ToadyStoreSOCount, errorMessageTextView,txt_ln_InfoSectionDetails;
    public Button btn_Proceed;
    public LinearLayout erreorSectionParent,ln_InfoSection;
    DBAdapterKenya dbengine = new DBAdapterKenya(this);
    DatabaseAssistant DA = new DatabaseAssistant(SummaryActivity.this);
    int serverResponseCode = 0;
    public int syncFLAG = 0;
    public String[] xmlForWeb = new String[1];
    public PowerManager pm;
    public PowerManager.WakeLock wl;
    public SimpleDateFormat sdf;
    public ProgressDialog pDialogRefreshReport;
    ServiceWorker newservice = new ServiceWorker();
    public int flgAllDataRefreshOrSummary = 0; //0=All Data Refresh,1=Only Summary
    //LinkedHashMap<String, String> hmapStoreCountCoverageAreaWise = new LinkedHashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        Date date1=new Date();
        sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        fDate = sdf.format(date1).toString().trim();

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tManager.getDeviceId();

        // imei="123456789";
        //  imei="359648069495987";
        if (CommonInfo.imei.equals("")) {
            CommonInfo.imei = imei;
        }
        else
        {
            imei=CommonInfo.imei;
        }

        initializeAllView();
        //getDataFromDatabaseToHashmap();
        pDialogRefreshReport = new ProgressDialog(SummaryActivity.this);
        pDialogRefreshReport.setTitle(getResources().getString(R.string.genTermPleaseWaitNew));
        pDialogRefreshReport.setMessage(getString(R.string.ConnectingServer));
        pDialogRefreshReport.setIndeterminate(false);
        pDialogRefreshReport.setCancelable(false);
        pDialogRefreshReport.setCanceledOnTouchOutside(false);
        pDialogRefreshReport.show();

        if (isOnline()) {

            try {
                if (flgAllDataRefreshOrSummary == 0) {
                    if (dbengine.fnCheckForPendingImages() == 1) {
                        ImageSync task = new ImageSync(SummaryActivity.this);
                        task.execute();
                    } else if (dbengine.fnCheckForPendingXMLFilesInTable() == 1) {
                        new FullSyncDataNow(SummaryActivity.this).execute();
                    } else {
                        fnUploadDataAndGetFreshData();
                    }
                } else {

                }
            } catch (Exception ex) {


            } finally {

            }
        } else {
            fnGetSummaryDataAndAddView();

        }


    }

    public void fnGetSummaryDataAndAddView()
    {
        getDataFromDatabaseToHashmap();
        if (parentOfAllDynamicData.getChildCount() > 0) {
            parentOfAllDynamicData.removeAllViews();
            // dynamcDtaContnrScrollview.removeAllViews();
            addViewIntoTable();
        } else {
            addViewIntoTable();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void getDataFromDatabaseToHashmap() {
        SONameAndSummryLastServerRefreshTime = dbengine.fetch_SONameAndSummryLastServerRefreshTime();
        // hmapStoreCountCoverageAreaWise=dbengine.fetch_StoreCountCoverageAreaWise();
        hmapDSRFromDataBase = dbengine.fetch_SODSRSummary();
        tv_SOName.setText(SONameAndSummryLastServerRefreshTime.split(Pattern.quote("^"))[0]);
        tv_ServerLastRefreshTime.setText(SONameAndSummryLastServerRefreshTime.split(Pattern.quote("^"))[1]);

    }

    public void initializeAllView() {

        erreorSectionParent = (LinearLayout) findViewById(R.id.erreorSectionParent);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessageTextView);

        ln_InfoSection = (LinearLayout) findViewById(R.id.ln_InfoSection);
        txt_ln_InfoSectionDetails = (TextView) findViewById(R.id.txt_ln_InfoSectionDetails);
        txt_ln_InfoSectionDetails.setText(R.string.so_summary_point_note_text);

        tv_SOName = (TextView) findViewById(R.id.tv_SOName);
        tv_ServerLastRefreshTime = (TextView) findViewById(R.id.tv_ServerLastRefreshTime);
        tv_ToadyStoreSOCount = (TextView) findViewById(R.id.tv_ToadyStoreSOCount);
        btn_Proceed = (Button) findViewById(R.id.btn_Proceed);
        parentOfAllDynamicData = (LinearLayout) findViewById(R.id.parentOfAllDynamicData);
        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SummaryActivity.this,StorelistActivity.class);
                SummaryActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    public void addViewIntoTable() {
        erreorSectionParent.setVisibility(View.GONE);
        ln_InfoSection.setVisibility(View.GONE);
        if (hmapDSRFromDataBase.size() > 0) {
            ln_InfoSection.setVisibility(View.VISIBLE);
            for (Map.Entry<String, String> entry : hmapDSRFromDataBase.entrySet()) {

                //DSRID,DSRName,OutletCount,OutletCountValidated,StoresOnServer,flgDSROrSO

                String dsrID = entry.getKey().toString().trim();

                String strOtherDetails = entry.getValue().toString().trim();

                String DSRName = strOtherDetails.split(Pattern.quote("^"))[0].toString().trim();
                int TotStoreAdded = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[1].toString().trim());
                int Approved = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[2].toString().trim());
                int Rejected = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[3].toString().trim());
                int ReMap = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[4].toString().trim());
                int Pending = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[5].toString().trim());
                int flgDSROrSO = Integer.parseInt(strOtherDetails.split(Pattern.quote("^"))[6].toString().trim());
                if (flgDSROrSO == 1) {
                    View dynamic_container = getLayoutInflater().inflate(R.layout.single_row_summary, null);
                    TextView dsrNameTextview = (TextView) dynamic_container.findViewById(R.id.dsrNameTextview);
                    dsrNameTextview.setText(DSRName);
                    TextView txt_TotStoreAdded = (TextView) dynamic_container.findViewById(R.id.txt_TotStoreAdded);
                    TextView txt_Approved = (TextView) dynamic_container.findViewById(R.id.txt_Approved);
                    TextView txt_Rejected = (TextView) dynamic_container.findViewById(R.id.txt_Rejected);
                    TextView txt_ReMap = (TextView) dynamic_container.findViewById(R.id.txt_ReMap);
                    TextView txt_Pending = (TextView) dynamic_container.findViewById(R.id.txt_Pending);

                    txt_TotStoreAdded.setText("" + TotStoreAdded);
                    txt_Approved.setText("" + Approved);
                    txt_Rejected.setText("" + Rejected);
                    txt_ReMap.setText("" + ReMap);
                    txt_Pending.setText("" + Pending);

                    /*todayAddedTextview.setText("" + TotStoreAdded);
                    storeOnserverTextview.setText("" + Approved);
                    validatedTextview.setText("" + Rejected);*/
                    parentOfAllDynamicData.addView(dynamic_container);
                } else if (flgDSROrSO == 0) {
                    tv_ToadyStoreSOCount.setText("" + TotStoreAdded);
                }

            }
        } else {
            erreorSectionParent.setVisibility(View.VISIBLE);
            errorMessageTextView.setText("Currently No DSR Mapped  With You.....");
        }
        if (pDialogRefreshReport.isShowing()) {
            pDialogRefreshReport.dismiss();
        }
    }


    private class ImageSync extends AsyncTask<Void, Void, Boolean> {
        // ProgressDialog pDialogGetStores;
        public ImageSync(SummaryActivity activity) {
            // pDialog2STANDBY = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            boolean isErrorExist = false;


            try {
                //dbengine.upDateCancelTask("0");
                ArrayList<String> listImageDetails = new ArrayList<String>();

                listImageDetails = dbengine.getImageDetails(5);

                if (listImageDetails != null && listImageDetails.size() > 0) {
                    for (String imageDetail : listImageDetails) {
                        String tempIdImage = imageDetail.split(Pattern.quote("^"))[0].toString();
                        String imagePath = imageDetail.split(Pattern.quote("^"))[1].toString();
                        String imageName = imageDetail.split(Pattern.quote("^"))[2].toString();
                        String file_dj_path = Environment.getExternalStorageDirectory() + "/" + CommonInfo.ImagesFolder + "/" + imageName;
                        File fImage = new File(file_dj_path);
                        if (fImage.exists()) {
                            uploadImage(imagePath, imageName, tempIdImage);
                        }


                    }
                }


            } catch (Exception e) {
                isErrorExist = true;
            } finally {
                Log.i("SvcMgr", "Service Execution Completed...");
            }

            return isErrorExist;
        }

        @Override
        protected void onPostExecute(Boolean resultError) {
            super.onPostExecute(resultError);

            dbengine.fndeleteSbumittedStoreImagesOfSotre(4);
            if (dbengine.fnCheckForPendingXMLFilesInTable() == 1) {
                new FullSyncDataNow(SummaryActivity.this).execute();
            } else {
                fnUploadDataAndGetFreshData();
            }
        }
    }


    public void uploadImage(String sourceFileUri, String fileName, String tempIdImage) throws IOException {
        BitmapFactory.Options IMGoptions01 = new BitmapFactory.Options();
        IMGoptions01.inDither = false;
        IMGoptions01.inPurgeable = true;
        IMGoptions01.inInputShareable = true;
        IMGoptions01.inTempStorage = new byte[16 * 1024];

        //finalBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fnameIMG,IMGoptions01), 640, 480, false);

        Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(sourceFileUri).getPath(), IMGoptions01);

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
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        ////System.out.println("image_str: "+image_str);

        stream.flush();
        stream.close();
        //buffer.clear();
        //buffer = null;
        bitmap.recycle();
        nameValuePairs.add(new BasicNameValuePair("image", image_str));
        nameValuePairs.add(new BasicNameValuePair("FileName", fileName));
        nameValuePairs.add(new BasicNameValuePair("TempID", tempIdImage));
        try {

            HttpParams httpParams = new BasicHttpParams();
            int some_reasonable_timeout = (int) (30 * DateUtils.SECOND_IN_MILLIS);

            //HttpConnectionParams.setConnectionTimeout(httpParams, some_reasonable_timeout);

            HttpConnectionParams.setSoTimeout(httpParams, some_reasonable_timeout + 2000);


            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpPost httppost = new HttpPost(CommonInfo.ImageSyncPath);


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            String the_string_response = convertResponseToString(response);
            if (the_string_response.equals("Abhinav")) {
                dbengine.updateSSttImage(fileName, 4);
                dbengine.fndeleteSbumittedStoreImagesOfSotre(4);

                String file_dj_path = Environment.getExternalStorageDirectory() + "/" + CommonInfo.ImagesFolder + "/" + fileName;
                File fdelete = new File(file_dj_path);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {

                        callBroadCast();
                    } else {

                    }
                }

            }

        } catch (Exception e) {

            System.out.println(e);
            //	IMGsyOK = 1;

        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(SummaryActivity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri) {

                }
            });
        } else {
            Log.e("-->", " < 14");
            SummaryActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException {

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        //System.out.println("contentLength : " + contentLength);
        //Toast.makeText(MainActivity.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
        if (contentLength < 0) {
        } else {
            byte[] data = new byte[512];
            int len = 0;
            try {
                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close(); // closing the stream…..
            } catch (IOException e) {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string…..

            //System.out.println("Result : " + res);
            //Toast.makeText(MainActivity.this, "Result : " + res, Toast.LENGTH_LONG).show();
            ////System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }


   /* public String BitMapToString(Bitmap bitmap) {
        int h1 = bitmap.getHeight();
        int w1 = bitmap.getWidth();
        h1 = h1 / 8;
        w1 = w1 / 8;
        bitmap = Bitmap.createScaledBitmap(bitmap, w1, h1, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
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
    private class FullSyncDataNow extends AsyncTask<Void, Void, Void> {


        ProgressDialog pDialogGetStores;
        int responseCode = 0;

        public FullSyncDataNow(SummaryActivity activity) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            File LTFoodXMLFolder = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

            if (!LTFoodXMLFolder.exists()) {
                LTFoodXMLFolder.mkdirs();
            }


        }

        @Override

        protected Void doInBackground(Void... params) {


            try {


                File del = new File(Environment.getExternalStorageDirectory(), CommonInfo.OrderXMLFolder);

                // check number of files in folder
                String[] AllFilesName = checkNumberOfFiles(del);


                if (AllFilesName.length > 0) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


                    for (int vdo = 0; vdo < AllFilesName.length; vdo++) {
                        String fileUri = AllFilesName[vdo];


                        //System.out.println("Sunil Again each file Name :" +fileUri);

                        if (fileUri.contains(".zip")) {
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + CommonInfo.OrderXMLFolder + "/" + fileUri);
                            file.delete();
                        } else {
                            String f1 = Environment.getExternalStorageDirectory().getPath() + "/" + CommonInfo.OrderXMLFolder + "/" + fileUri;
                            // System.out.println("Sunil Again each file full path"+f1);
                            try {
                                responseCode = upLoad2Server(f1, fileUri);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (responseCode != 200) {
                            break;
                        }

                    }

                } else {
                    responseCode = 200;
                }


            } catch (Exception e) {

                e.printStackTrace();
                if (pDialogGetStores.isShowing()) {
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

            if (responseCode == 200) {

                dbengine.fndeleteSbumittedStoreList(5);
                dbengine.fndeleteSbumittedStoreList(4);
                dbengine.deleteXmlTable("4");
                fnUploadDataAndGetFreshData();


            }

        }
    }

    public void delXML(String delPath) {
        File file = new File(delPath);
        file.delete();
        File file1 = new File(delPath.toString().replace(".xml", ".zip"));
        file1.delete();
    }

    public static String[] checkNumberOfFiles(File dir) {
        int NoOfFiles = 0;
        String[] Totalfiles = null;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            NoOfFiles = children.length;
            Totalfiles = new String[children.length];

            for (int i = 0; i < children.length; i++) {
                Totalfiles[i] = children[i];
            }
        }
        return Totalfiles;
    }

    public static void zip(String[] files, String zipFile) throws IOException {
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
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }


    public int upLoad2Server(String sourceFileUri, String fileUri) {

        fileUri = fileUri.replace(".xml", "");

        String fileName = fileUri;
        String zipFileName = fileUri;

        String newzipfile = Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + fileName + ".zip";

        sourceFileUri = newzipfile;

        xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/" + CommonInfo.OrderXMLFolder + "/" + fileName + ".xml";


        try {
            zip(xmlForWeb, newzipfile);
        } catch (Exception e1) {
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

        String urlString = CommonInfo.OrderSyncPath.trim() + "?CLIENTFILENAME=" + zipFileName;

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

            while (bytesRead > 0) {
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

            if (serverResponseCode == 200) {
                syncFLAG = 1;

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                // editor.remove(xmlForWeb[0]);
                editor.putString(fileUri, "" + 4);
                editor.commit();

                String FileSyncFlag = pref.getString(fileUri, "" + 1);
                dbengine.upDateTblXmlFile(fileName);
                delXML(xmlForWeb[0].toString());


            } else {
                syncFLAG = 0;
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return serverResponseCode;

    }


    public void fnUploadDataAndGetFreshData() {
        try {
            GetStoreAllData getStoreAllDataAsync = new GetStoreAllData(SummaryActivity.this);
            getStoreAllDataAsync.execute();
            //////System.out.println("SRVC-OK: "+ new GetStoresForDay().execute().get());
        } catch (Exception e) {

        }
    }

    private class GetStoreAllData extends AsyncTask<Void, Void, Void> {

        public GetStoreAllData(SummaryActivity activity) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                int DatabaseVersion = dbengine.DATABASE_VERSION;
                int ApplicationID = dbengine.Application_TypeID;
                //newservice = newservice.getAvailableAndUpdatedVersionOfApp(getApplicationContext(), imei,fDate,DatabaseVersion,ApplicationID);


                for (int mm = 1; mm < 6; mm++) {
                    if (mm == 2) {
                        //(Context ctx,String uuid,String CurDate,int DatabaseVersion,int ApplicationID)
                        newservice = newservice.getStoreAllDetails(getApplicationContext(), imei, fDate, DatabaseVersion, ApplicationID);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }


                    }
                    if (mm == 3) {
                        newservice = newservice.callfnSingleCallAllWebService(getApplicationContext(), ApplicationID, imei);
                        if (!newservice.director.toString().trim().equals("1")) {
                            if (chkFlgForErrorToCloseApp == 0) {
                                chkFlgForErrorToCloseApp = 1;
                                break;
                            }

                        }

                    }
                    if (mm == 4) {
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


                        newservice = newservice.fnGetStateCityListMstr(SummaryActivity.this,imei, fDate,CommonInfo.Application_TypeID);
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
            if (pDialogRefreshReport.isShowing()) {
                pDialogRefreshReport.dismiss();
            }
            if (chkFlgForErrorToCloseApp == 1)   // if Webservice showing exception or not excute complete properly
            {
                chkFlgForErrorToCloseApp = 0;

            } else {

                //Write Code To Load Summuray Details Now Here Below
                fnGetSummaryDataAndAddView();
            }

        }


    }
}