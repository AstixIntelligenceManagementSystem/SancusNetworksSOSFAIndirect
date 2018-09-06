package project.astix.com.sancusnetworkssosfaindirect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ActualVisitStock extends Activity implements CategoryCommunicator {

private LinearLayout lLayout_main;
private DBAdapterKenya dbengine;
private Button btnNext;

    private String storeID;
    private String imei;
    private String date;
    private String pickerDate;
    private String selStoreName;
    private List<String> categoryNames;
    private int progressBarStatus=0;

    private LinkedHashMap<String, String> hmapctgry_details= new LinkedHashMap<>();
    private TextView img_ctgry;
    private int StoreCurrentStoreType=0;
    private String previousSlctdCtgry="";

    private LinkedHashMap<String,String> hmapPrdctData=new LinkedHashMap<>();
    private LinkedHashMap<String, String> hmapFilterProductList= new LinkedHashMap<>();

private LinkedHashMap<String,String> hmapFetchPDASavedData=new LinkedHashMap<>();
LinkedHashMap<String,String> hmapSaveDataInPDA=new LinkedHashMap<>();
private LinkedHashMap<String,String> hmapProductStockFromPurchaseTable=new LinkedHashMap<>();




    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            return true;

        }
        if(keyCode==KeyEvent.KEYCODE_HOME)
        {

        }
        if(keyCode==KeyEvent.KEYCODE_MENU)
        {
            return true;
        }
        if(keyCode== KeyEvent.KEYCODE_SEARCH)
        {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_visit_stock);
        dbengine=new DBAdapterKenya(ActualVisitStock.this);

        initializeallViews();
        getDataFromIntent();
        fetchDataFromDatabase();



    }

    private void initializeallViews(){
        lLayout_main= (LinearLayout) findViewById(R.id.lLayout_main);
        ImageView img_back_Btn= (ImageView) findViewById(R.id.img_back_Btn);
        btnNext= (Button) findViewById(R.id.btnNext);


        img_ctgry=(TextView) findViewById(R.id.img_ctgry);

        img_ctgry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                img_ctgry.setEnabled(false);
                customAlertStoreList(categoryNames,"Select Category");
            }
        });


        img_back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fireBackDetPg=new Intent(ActualVisitStock.this,LastVisitDetails.class);
                fireBackDetPg.putExtra("storeID", storeID);
                fireBackDetPg.putExtra("SN", selStoreName);
                fireBackDetPg.putExtra("bck", 1);
                fireBackDetPg.putExtra("imei", imei);
                fireBackDetPg.putExtra("userdate", date);
                fireBackDetPg.putExtra("pickerDate", pickerDate);
                //fireBackDetPg.putExtra("rID", routeID);
                startActivity(fireBackDetPg);
                finish();

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbengine.deleteActualVisitData(storeID);
                if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.size()>0)
                {
                    for (Map.Entry<String,String> entry:hmapFetchPDASavedData.entrySet()){

                        dbengine.saveTblActualVisitStock(storeID,entry.getKey(),entry.getValue(),1);


                    }
                }

                Intent nxtP4 = new Intent(ActualVisitStock.this,ProductOrderFilterSearch.class);
                //Intent nxtP4 = new Intent(LastVisitDetails.this,ProductOrderFilterSearch_RecycleView.class);
                nxtP4.putExtra("storeID", storeID);
                nxtP4.putExtra("SN", selStoreName);
                nxtP4.putExtra("imei", imei);
                nxtP4.putExtra("userdate", date);
                nxtP4.putExtra("pickerDate", pickerDate);
                nxtP4.putExtra("flgOrderType", 1);
                startActivity(nxtP4);
                finish();
            }
        });

    }


    private void inflatePrdctStockData(){


        if(hmapFilterProductList!=null && hmapFilterProductList.size()>0){
            for (Map.Entry<String, String> entry : hmapFilterProductList.entrySet()) {

                String prdId = entry.getKey();
                String value = entry.getValue();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewProduct = inflater.inflate(R.layout.inflate_row_actual_visit, null);
                LinearLayout ll_inflate= (LinearLayout) viewProduct.findViewById(R.id.ll_inflate);

                TextView prdName= (TextView) viewProduct.findViewById(R.id.prdName);
                final EditText et_stckVal= (EditText) viewProduct.findViewById(R.id.et_stckVal);
                prdName.setText(value);
                prdName.setTag(prdId);

                et_stckVal.setTag(prdId+"_Stock");

                if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.containsKey(prdId))
                {
                    et_stckVal.setText(hmapFetchPDASavedData.get(prdId));
                }
                et_stckVal.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                       if(!TextUtils.isEmpty(et_stckVal.getText().toString().trim()))
                       {
                           String tagProductId=et_stckVal.getTag().toString().split(Pattern.quote("_"))[0];
                           hmapFetchPDASavedData.put(tagProductId,et_stckVal.getText().toString().trim());
                       }
                       else
                       {
                           String tagProductId=et_stckVal.getTag().toString().split(Pattern.quote("_"))[0];
                           if(hmapFetchPDASavedData.containsKey(tagProductId))
                           {
                               hmapFetchPDASavedData.remove(tagProductId);
                           }
                       }
                    }
                });
                lLayout_main.addView(viewProduct);

             // btnNextClick(storeID,prdId,et_stckVal);




            }
        }
    }


    private void fetchDataFromDatabase(){
        dbengine.open();
        hmapPrdctData=dbengine.fetchProductDataForActualVisit();
        hmapFetchPDASavedData=dbengine.fetchActualVisitData(storeID);
        hmapProductStockFromPurchaseTable=dbengine.fetchProductStockFromPurchaseTable(storeID);
        StoreCurrentStoreType=Integer.parseInt(dbengine.fnGetStoreTypeOnStoreIdBasis(storeID));
        dbengine.close();

        getCategoryDetail();

        Iterator it11new = hmapProductStockFromPurchaseTable.entrySet().iterator();
        String crntPID="0";
        int cntPsize=0;
        while (it11new.hasNext()) {
            Map.Entry pair = (Map.Entry) it11new.next();

            hmapFetchPDASavedData.put(pair.getKey().toString(),pair.getValue().toString());
        }

        img_ctgry.setText("All");
        searchProduct("All","");
       /* if(hmapFetchPDASavedData!=null && hmapFetchPDASavedData.size()>0) {


            for (Map.Entry<String, String> entry : hmapFetchPDASavedData.entrySet()) {

                String prdId=entry.getKey();
                String stckVal=entry.getValue();


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewProduct = inflater.inflate(R.layout.inflate_row_actual_visit, null);
                LinearLayout ll_inflate= (LinearLayout) viewProduct.findViewById(R.id.ll_inflate);

                TextView prdName= (TextView) viewProduct.findViewById(R.id.prdName);
                EditText et_stckVal= (EditText) findViewById(R.id.et_stckVal);

                lLayout_main.addView(viewProduct);

            }
        }*/
    }


    private void getDataFromIntent() {


        Intent passedvals = getIntent();

        if(passedvals!=null){

            storeID = passedvals.getStringExtra("storeID");
            imei = passedvals.getStringExtra("imei");
            date = passedvals.getStringExtra("userdate");

            pickerDate = passedvals.getStringExtra("pickerDate");
            selStoreName = passedvals.getStringExtra("SN");

        }

    }

    private void customAlertStoreList(final List<String> listOption, String sectionHeader)
    {

        final Dialog listDialog = new Dialog(ActualVisitStock.this);
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listDialog.setContentView(R.layout.search_list);
        listDialog.setCanceledOnTouchOutside(false);
        listDialog.setCancelable(false);
        WindowManager.LayoutParams parms = listDialog.getWindow().getAttributes();
        parms.gravity = Gravity.CENTER;
        //there are a lot of settings, for dialog, check them all out!
        parms.dimAmount = (float) 0.5;




        TextView txt_section=(TextView) listDialog.findViewById(R.id.txt_section);
        txt_section.setText(sectionHeader);
        TextView txtVwCncl=(TextView) listDialog.findViewById(R.id.txtVwCncl);
        //    TextView txtVwSubmit=(TextView) listDialog.findViewById(R.id.txtVwSubmit);


        final ListView list_store=(ListView) listDialog.findViewById(R.id.list_store);
        final CardArrayAdapterCategory cardArrayAdapter = new CardArrayAdapterCategory(ActualVisitStock.this,listOption,listDialog,previousSlctdCtgry);

        //img_ctgry.setText(previousSlctdCtgry);





        list_store.setAdapter(cardArrayAdapter);
        //	editText.setBackgroundResource(R.drawable.et_boundary);
        img_ctgry.setEnabled(true);





        txtVwCncl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                img_ctgry.setEnabled(true);


            }
        });




        //now that the dialog is set up, it's time to show it
        listDialog.show();

    }


    @Override
    public void selectedOption(String selectedCategory, Dialog dialog) {
        dialog.dismiss();
        previousSlctdCtgry=selectedCategory;

        img_ctgry.setText(selectedCategory);

        if(hmapctgry_details.containsKey(selectedCategory))
        {
            searchProduct(selectedCategory,hmapctgry_details.get(selectedCategory));
        }
        else
        {
            searchProduct(selectedCategory,"");
        }



    }



    private void searchProduct(String filterSearchText, String ctgryId)
    {
        progressBarStatus = 0;


        hmapFilterProductList.clear();


        hmapFilterProductList=dbengine.getFileredProductListMap(filterSearchText.trim(),StoreCurrentStoreType,ctgryId);
        //System.out.println("hmapFilterProductListCount :-"+ hmapFilterProductList.size());
        lLayout_main.removeAllViews();

		/*if(hmapFilterProductList.size()<250)
		{*/
        if(hmapFilterProductList.size()>0)
        {
            inflatePrdctStockData();
        }
        else
        {
            allMessageAlert(ActualVisitStock.this.getResources().getString(R.string.AlertFilter));
        }

		/*}

		else
		{
			allMessageAlert("Please put some extra filter on Search-Box to fetch related product");
		}*/




    }

    private void allMessageAlert(String message) {
        AlertDialog.Builder alertDialogNoConn = new AlertDialog.Builder(ActualVisitStock.this);
        alertDialogNoConn.setTitle(ActualVisitStock.this.getResources().getString(R.string.genTermInformation));
        alertDialogNoConn.setMessage(message);
        //alertDialogNoConn.setMessage(getText(R.string.connAlertErrMsg));
        alertDialogNoConn.setNeutralButton(ActualVisitStock.this.getResources().getString(R.string.AlertDialogOkButton),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();


                    }
                });
        alertDialogNoConn.setIcon(R.drawable.info_ico);
        AlertDialog alert = alertDialogNoConn.create();
        alert.show();

    }
    private void getCategoryDetail()
    {

        hmapctgry_details=dbengine.fetch_Category_List();

        int index=0;
        if(hmapctgry_details!=null)
        {
            categoryNames= new ArrayList<>();
            LinkedHashMap<String, String> map = new LinkedHashMap<>(hmapctgry_details);
            Set set2 = map.entrySet();
            Iterator iterator = set2.iterator();
            while(iterator.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator.next();
                categoryNames.add(me2.getKey().toString());
                index=index+1;
            }
        }


    }



}
