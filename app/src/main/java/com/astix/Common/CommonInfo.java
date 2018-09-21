package com.astix.Common;

import android.net.Uri;

import java.io.File;

public class CommonInfo
{



	//Its for Live Release


/*

	public static int flgAllRoutesData=1;
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static Uri uriSavedImage_savedInstance=null;

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static String prcID="NULL";

	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebDSRAttendanceUrl="http://103.20.212.194/SancusNetworks/Reports/frmDSRAttendanceReport.aspx";
	public static String WebManageDSRUrl="http://103.20.212.194/SancusNetworks/pda/frmIMEImanagement.aspx";
	public static String WebPageUrlDataReport="http://103.20.212.194/SancusNetworks/Mobile/fnSalesmanWiseSummaryRpt.aspx";
	public static String WebPageUrl="http://103.20.212.194/SancusNetworks/Mobile/frmRouteTracking.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidSancusNetworksSFALive/Service.asmx";
	// public static String WebServicePath="http://103.20.212.194/WebServiceAndroidSancusNetworksDevelopment/Service.asmx";

	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="SancusNetworksSOSFA.apk";

	public static String DATABASE_NAME = "DbSancusNetworksSFAApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 2;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.0";   // put this field value based on value in table on the server
	public static int Application_TypeID = 4; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String OrderSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFALive/DefaultSOSFA.aspx";

	public static String ImageSyncPath="http://103.20.212.194/ReadXML_SancusNetworksImagesLive/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForSancusNetworksSFALive/default.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_SancusNetworksSFALive/DefaultSODistributorMapping.aspx";

	public static String NewStoreSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFALive/DefaultSO.aspx";

	public static String OrderSyncPathDistributorTarget="http://103.20.212.194/ReadXML_SancusNetworksSFALive/DefaultSODistributorTarget.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_SancusNetworksInvoiceLive/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFADistributionLive/Default.aspx";

	public static String WebPurchaseOrderUrl="http://103.20.212.194/SancusNetworks/frmPO.aspx";
	public static String WebAttendanceReport="http://103.20.212.194/SancusNetworks/Mobile/frmTodayAttendence.aspx";
	public static String WebScndryUpdate="http://103.20.212.194/SancusNetworks/Mobile/frmDailySecondaryUpdate.aspx";
	public static String OrderXMLFolder="SancusNetworksSFAXml";
	public static String ImagesFolder="SancusNetworksSFAImages";
	public static String TextFileFolder="SancusNetworksTextFile";
	public static String InvoiceXMLFolder="SancusNetworksInvoiceXml";
	public static String FinalLatLngJsonFile="SancusNetworksSFAFinalLatLngJson";

	public static final String DistributorMapXMLFolder="SancusNetworksDistributorMapXML";
	public static final String DistributorStockXMLFolder="SancusNetworksDistributorStockXML";
	public static final String DistributorCheckInXMLFolder="SancusNetworksDistributorCheckInXML";

	public static String AppLatLngJsonFile="SancusNetworksSFALatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="SancusNetworksPrefrence";
	public static final String AttandancePreference="SancusNetworksAttandancePreference";

	public static int PersonNodeID=0;
	public static int PersonNodeType=0;

	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int FlgDSRSO=0;
	public static int DayStartClick=0;

	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int flgNewStoreORStoreValidation=0;
	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/SancusNetworks/Reports/frmPDAImgs.aspx";
	public static String WebPageUrlDSMWiseReport="http://103.20.212.194/SancusNetworks/Mobile/frmDSMWiseReportCard.aspx?imei=";
	public static String ImagesFolderServer="RMMSFAImagesServer";
*/

	// Its for Test Path on 194 Server


/*

	public static int flgAllRoutesData=1;
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static Uri uriSavedImage_savedInstance=null;

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static String prcID="NULL";

	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebDSRAttendanceUrl="http://103.20.212.194/SancusNetworks_test/Reports/frmDSRAttendanceReport.aspx";
	public static String WebManageDSRUrl="http://103.20.212.194/SancusNetworks_test/pda/frmIMEImanagement.aspx";
	public static String WebPageUrlDataReport="http://103.20.212.194/SancusNetworks_test/Mobile/fnSalesmanWiseSummaryRpt.aspx";
	public static String WebPageUrl="http://103.20.212.194/SancusNetworks_test/Mobile/frmRouteTracking.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidSancusNetworksSFATest/Service.asmx";

	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="SancusNetworksSOTest.apk";

	public static String DATABASE_NAME = "DbSancusNetworksSFAApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 1;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.0";   // put this field value based on value in table on the server
	public static int Application_TypeID = 4; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String OrderSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFATest/DefaultSOSFA.aspx";

	public static String ImageSyncPath="http://103.20.212.194/ReadXML_SancusNetworksImagesTest/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForSancusNetworksSFATest/default.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_SancusNetworksSFATest/DefaultSODistributorMapping.aspx";

	public static String NewStoreSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFATest/DefaultSO.aspx";

	public static String OrderSyncPathDistributorTarget="http://103.20.212.194/ReadXML_SancusNetworksSFATest/DefaultSODistributorTarget.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_SancusNetworksInvoiceTest/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFADistributionTest/Default.aspx";

	public static String WebPurchaseOrderUrl="http://103.20.212.194/SancusNetworks_Test/frmPO.aspx";

	public static String OrderXMLFolder="SancusNetworksSFAXml";
	public static String ImagesFolder="SancusNetworksSFAImages";
	public static String TextFileFolder="SancusNetworksTextFile";
	public static String InvoiceXMLFolder="SancusNetworksInvoiceXml";
	public static String FinalLatLngJsonFile="SancusNetworksSFAFinalLatLngJson";

	public static final String DistributorMapXMLFolder="SancusNetworksDistributorMapXML";
	public static final String DistributorStockXMLFolder="SancusNetworksDistributorStockXML";
	public static final String DistributorCheckInXMLFolder="SancusNetworksDistributorCheckInXML";
public static String WebAttendanceReport="http://103.20.212.194/SancusNetworks_test/Mobile/frmTodayAttendence.aspx";
public static String WebScndryUpdate="http://103.20.212.194/SancusNetworks_test/Mobile/frmDailySecondaryUpdate.aspx";
	public static String AppLatLngJsonFile="SancusNetworksSFALatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="SancusNetworksPrefrence";
	public static final String AttandancePreference="SancusNetworksAttandancePreference";

	public static int PersonNodeID=0;
	public static int PersonNodeType=0;

	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int FlgDSRSO=0;
	public static int DayStartClick=0;

	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int flgNewStoreORStoreValidation=0;
	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/SancusNetworks_test/Reports/frmPDAImgsdev.aspx";
	public static String WebPageUrlDSMWiseReport="http://103.20.212.194/SancusNetworks_test/Mobile/frmDSMWiseReportCard.aspx?imei=";
 public static String ImagesFolderServer="RMMSFAImagesServer";
*/


	// Its for Dev Path on 194 Server


	public static int flgAllRoutesData=1;
	public static File imageF_savedInstance=null;
	public static String imageName_savedInstance=null;
	public static String clickedTagPhoto_savedInstance=null;
	public static Uri uriSavedImage_savedInstance=null;

	public static String imei="";
	public static String SalesQuoteId="BLANK";
	public static String quatationFlag="";
	public static String fileContent="";
	public static String prcID="NULL";

	public static String newQuottionID="NULL";
	public static String globalValueOfPaymentStage="0"+"_"+"0"+"_"+"0";

	public static String WebDSRAttendanceUrl="http://103.20.212.194/SancusNetworks_dev/Reports/frmDSRAttendanceReport.aspx";
	public static String WebManageDSRUrl="http://103.20.212.194/SancusNetworks_dev/pda/frmIMEImanagement.aspx";
	public static String WebPageUrlDataReport="http://103.20.212.194/SancusNetworks_dev/Mobile/fnSalesmanWiseSummaryRpt.aspx";
	public static String WebPageUrl="http://103.20.212.194/SancusNetworks_dev/Mobile/frmRouteTracking.aspx";

	public static String WebServicePath="http://103.20.212.194/WebServiceAndroidSancusNetworksSFADevelopment/Service.asmx";
	// public static String WebServicePath="http://103.20.212.194/WebServiceAndroidSancusNetworksDevelopment/Service.asmx";

	public static String VersionDownloadPath="http://103.20.212.194/downloads/";
	public static String VersionDownloadAPKName="SancusNetworksSOSFADev.apk";

	public static String DATABASE_NAME = "DbSancusNetworksSFAApp";

	public static int AnyVisit = 0;

	public static int DATABASE_VERSIONID = 22;      // put this field value based on value in table on the server
	public static String AppVersionID = "1.8";   // put this field value based on value in table on the server
	public static int Application_TypeID = 4; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct

	public static String OrderSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFADevelopment/DefaultSOSFA.aspx";

	public static String ImageSyncPath="http://103.20.212.194/ReadXML_SancusNetworksImagesDevelopment/Default.aspx";

	public static String OrderTextSyncPath="http://103.20.212.194/ReadTxtFileForSancusNetworksSFADevelopment/default.aspx";

	public static String OrderSyncPathDistributorMap="http://103.20.212.194/ReadXML_SancusNetworksSFADevelopment/DefaultSODistributorMapping.aspx";

	public static String NewStoreSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFADevelopment/DefaultSO.aspx";

	public static String OrderSyncPathDistributorTarget="http://103.20.212.194/ReadXML_SancusNetworksSFADevelopment/DefaultSODistributorTarget.aspx";

	public static String InvoiceSyncPath="http://103.20.212.194/ReadXML_SancusNetworksInvoiceDevelopment/Default.aspx";

	public static String DistributorSyncPath="http://103.20.212.194/ReadXML_SancusNetworksSFADistributionDevelopment/Default.aspx";

	public static String WebPurchaseOrderUrl="http://103.20.212.194/SancusNetworks_Development/frmPO.aspx";
	public static String WebAttendanceReport="http://103.20.212.194/SancusNetworks_dev/Mobile/frmTodayAttendence.aspx";
	public static String WebScndryUpdate="http://103.20.212.194/SancusNetworks_dev/Mobile/frmDailySecondaryUpdate.aspx";
	public static String OrderXMLFolder="SancusNetworksSFAXml";
	public static String ImagesFolder="SancusNetworksSFAImages";
	public static String TextFileFolder="SancusNetworksTextFile";
	public static String InvoiceXMLFolder="SancusNetworksInvoiceXml";
	public static String FinalLatLngJsonFile="SancusNetworksSFAFinalLatLngJson";

	public static final String DistributorMapXMLFolder="SancusNetworksDistributorMapXML";
	public static final String DistributorStockXMLFolder="SancusNetworksDistributorStockXML";
	public static final String DistributorCheckInXMLFolder="SancusNetworksDistributorCheckInXML";

	public static String AppLatLngJsonFile="SancusNetworksSFALatLngJson";

	public static int DistanceRange=3000;
	public static String SalesPersonTodaysTargetMsg="";
	public static final String Preference="SancusNetworksPrefrence";
	public static final String AttandancePreference="SancusNetworksAttandancePreference";

	public static int PersonNodeID=0;
	public static int PersonNodeType=0;

	public static int CoverageAreaNodeID=0;
	public static int CoverageAreaNodeType=0;
	public static int SalesmanNodeId=0;
	public static int SalesmanNodeType=0;
	public static int flgDataScope=0;
	public static int FlgDSRSO=0;
	public static int DayStartClick=0;

	public static int flgLTFoodsSOOnlineOffLine=0;
	public static int flgNewStoreORStoreValidation=0;
	public static String URLImageLinkToViewStoreOverWebProtal="http://103.20.212.194/SancusNetworks_dev/Reports/frmPDAImgsdev.aspx";
	public static String WebPageUrlDSMWiseReport="http://103.20.212.194/SancusNetworks_dev/Mobile/frmDSMWiseReportCard.aspx?imei=";
	public static String ImagesFolderServer="RMMSFAImagesServer";
}
