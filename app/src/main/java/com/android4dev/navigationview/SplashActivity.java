package com.android4dev.navigationview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SplashActivity extends Activity {
	private String url = "http://www.aditya.multifacet-software.com/php/check/offline.php";
	Button online,offline;String id,s;
	String response1="";
	String ctdata;
	// Splash screen timer
	SQLiteDatabase offlinedb,db;
	private  String jsonResult;
	private List bookTitles;
	String change_MaINTEGERChg7_LightPoINTEGERs7_BOOKNO13;
	String KNo,ServiceConnectionNo,ACCTID,GroupCd,GroupId,Div,SubDiv,DTCode,RouteCd,RouteSeq,ServiceId,SPId,PerName,MailAdd1,MailAdd2
			,MailAdd3,MailAdd4,City,PinCode,TariffCateg,STCode,TotSLKW,SupplyDt,EmployeeClass,MtrBdgNo,MtrSrlNo
			,CapInst,TVMFlg,MultFactor,ProvEstUnitsKWH,TotAvgEstUnits,CeilLimUnits,PrevReadDt,FirstBill,PrevKWH
			,PrevKVAH,PrevKVA,PrePF,PrevBillId,LPCPrevAmt,LastBillType,PrevBillBal,LPCCurAmt,LPCAmt,AdjAftLastBill,CorrAftLastBill
			,ProvBillCancelAmt,NSFAftLastBill,LPSCExtDt,Rule80120,OtherBsegAmt,SDAmt,ArrearPrevYr,ArrearCurYr,ArrearLastBill
			,LPCRate,TempDisFlg,TempDisDT,TempRecDT,MultipleUtility,RKWH,RKWHSeq,RKWHDigits,RKVAH,RKVAHSeq
			,RKVAHDigits,RKVA,RKVASeq,RKVADigits,RPF,RPFSeq,RPFDigits,MaINTEGERChg,LightPoINTEGERs,ACChg,ACNos,
			MultipleSPs,PrevBillDt,CurrMtrStatus,MtrChgTotUnitsKwh,CeilByPass,MtrRemovalDTTM,MtrChangeFlg,LocType,CeilingDefective
			,PrevMRRemark,PwrLoomNos,BillCycleStartDt,BillCycleEndDt,ProvisionalKWH,ProvisionalKVAH,ProvisionalKVA,ProvisionalPF
			,ProvisionalReadDt,LastMtrChgMRRemark,TotSLKVA,TotSLBHP,CeilLimUnitsKVAH,OffSeasonFlg,SeasonBenAllow
			,SeasonBenViolCnt,ProvEstUnitsKVAH,TotAvgEstUnitsKVAH,MtrChgTotUnitsKVAH,MtrChgAvgMD,PayByCash,OffSeasonDecMD
			,TdDisturb,ArrLpscLastPY,ArrLpscLastCY,ArrLpscLastBILL,MdiPenalty,BookNo,edchgrate,gispoleid,ID,PoleID,CreatedBy,UpDatedBy
			,CreatedDate,UpdatedDate,OfficeID,flag;
	String uid, name,email,mobile_no,division,encrypted_password,salt,created_at,updated_at,company,imei,status,logindate,flag1,image ;
	String Acctid,BILLNO,BILL_date,CURRENT_KWH,PREVIOUS_KWH,CURRENT_KVWH,PREVIOUS_KVWH,BILL_PERIOD,FIXED_CHARGE,ENERGY_CHARGES,Min_charge,ELECTRICITY_DUTY,REG_SURCHARGES,EX_DEMAND_PANALITY,AREAR_AMOUNT,AREAR_SURCHARGES,ADJUSTMENT_AMOUNT,NET_AMOUNT,Rebate,Gross_amount,ctID,Previousdate,ctflag,SubmitBy,ctimage,
	 ctMF,ctCMD,ctCURRENR_KWH,ctENERGY_CHARGE,ctCURRENT_SURCHARGES;
	String dmID ,dmCode,dmName,dmOfficeID,dmflag;
	String saemail,div,curnt_date;
	String sid,stype,slvmid;
	String rid,rrate,rsupplyid,rminchrg,rrangefrom,rrangeto,rmeterkwh,rfcrate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Intent intent = getIntent();
		saemail= intent.getStringExtra("email");
		div= intent.getStringExtra("div");
		System.out.println("splash" + id + "-" + s);
		db=openOrCreateDatabase("Lesco_data", Context.MODE_PRIVATE, null);
		Cursor ldc = db.rawQuery("SELECT uid,Div FROM emp", null);
		System.out.println("sql" + ldc.moveToFirst());
		if(ldc.moveToFirst())
		{
			saemail=ldc.getString(0);
			div=ldc.getString(1);
			System.out.println(" database division" + saemail + "-" + div);
		}
		else
		{
			Toast.makeText(getApplicationContext(),"Error ", Toast.LENGTH_LONG)
					.show();
		}
		online = (Button) findViewById(R.id.splashonline);
		offline = (Button) findViewById(R.id.splashooffline);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		online.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				System.out.println("splash");
				Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
				intent1.putExtra("email",saemail);
				intent1.putExtra("div", div);
				startActivity(intent1);


			}

		});

		offline.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				try {
					isInternetOn();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		});
	}

	public final boolean isInternetOn() throws ParseException {

		// get Connectivity Manager object to check connection
		ConnectivityManager connec =
				(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

		// Check for network connections
		if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
				connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
				connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
				connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
			boolean count;
			offlinedb=openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
			offlinedb.execSQL("CREATE TABLE IF NOT EXISTS updated_date(ID INTEGER PRIMARY KEY   AUTOINCREMENT,downloaded_at date)");
			Cursor ck = offlinedb.rawQuery("SELECT downloaded_at from updated_date", null);
			System.out.println("sql" + ck.moveToFirst());
			if(ck.moveToFirst())
			{
				curnt_date=ck.getString(0);
				System.out.println("insidecurr "+curnt_date);
				count=true;
			}
			else
			{
				count=false;
			}
			if(count==true) {
				Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				String predate = df.format(c.getTime());
				Date d1 = df.parse(curnt_date);
				Date d2 = df.parse(predate);
				System.out.println("d1"+d1+"&"+d2+"PRE"+predate);
				if (d2.compareTo(d1)>=0)
				{
					System.out.println("Current time1"+d1.compareTo(d2));
					callactivity();
				}
				else {
					System.out.println("Current time1"+d1.compareTo(d2));
					downloading();
				}
			}
			else{
				downloading();
			}

			//Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
			return true;

		} else if (
				connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
						connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

			Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "No INTERNET", Snackbar.LENGTH_LONG);
			View view = snack.getView();
			TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
			tv.setTextColor(Color.WHITE);
			snack.show();
		}
		return false;
	}

	public void downloading() throws ParseException {
		System.out.println("Called Downloading");
		offlinedb = openOrCreateDatabase("LESCO", Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS Customer_Details");
		db.execSQL("DROP TABLE IF EXISTS updated_date");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS customer_transactions");
		db.execSQL("DROP TABLE IF EXISTS Division_Master");
		db.execSQL("DROP TABLE IF EXISTS lvm_Fixed_chargeMaster");
		db.execSQL("DROP TABLE IF EXISTS lvm_supply");
		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS Customer_Details (KNo nvarchar  ,ServiceConnectionNo nvarchar  ,ACCTID nvarchar  ,GroupCd nvarchar  ,GroupId nvarchar  ,Div nvarchar  ,SubDiv nvarchar  ,DTCode nvarchar ,RouteCd nvarchar  ,RouteSeq nvarchar  ,ServiceId nvarchar  ,SPId nvarchar  ,PerName nvarchar  ,MailAdd1 nvarchar  ,MailAdd2 nvarchar  ,MailAdd3 nvarchar  ,MailAdd4 nvarchar  ,City nvarchar  ,PinCode nvarchar  ,TariffCateg nvarchar  ,STCode nvarchar  ,TotSLKW nvarchar  ,	SupplyDt varchar ,EmployeeClass nvarchar  ,MtrBdgNo nvarchar  ,MtrSrlNo nvarchar  ,CapInst nvarchar  ,TVMFlg nvarchar  ,MultFactor nvarchar  ,ProvEstUnitsKWH nvarchar  ,TotAvgEstUnits nvarchar  ,CeilLimUnits nvarchar  ,PrevReadDt nvarchar  ,FirstBill nvarchar  ,PrevKWH nvarchar  ,PrevKVAH nvarchar  ,PrevKVA nvarchar  ,PrePF nvarchar  ,PrevBillId nvarchar  ,LPCPrevAmt nvarchar  ,LastBillType nvarchar  ,PrevBillBal nvarchar  ,LPCCurAmt nvarchar  ,LPCAmt nvarchar  ,AdjAftLastBill nvarchar  ,CorrAftLastBill nvarchar  ,ProvBillCancelAmt nvarchar  ,NSFAftLastBill nvarchar  ,LPSCExtDt smalldatetime ,Rule80120 nvarchar  ,OtherBsegAmt nvarchar  ,SDAmt nvarchar  ,ArrearPrevYr nvarchar  ,ArrearCurYr nvarchar  ,ArrearLastBill nvarchar  ,LPCRate nvarchar  ,TempDisFlg nvarchar  ,TempDisDT nvarchar  ,TempRecDT nvarchar  ,MultipleUtility nvarchar  ,RKWH nvarchar  ,RKWHSeq nvarchar  ,RKWHDigits nvarchar  ,RKVAH nvarchar  ,RKVAHSeq nvarchar  ,RKVAHDigits nvarchar  ,RKVA nvarchar  ,RKVASeq nvarchar  ,RKVADigits nvarchar ,RPF nvarchar  , " +
				"RPFSeq nvarchar  , " +
				"RPFDigits nvarchar  , " +
				"MaINTEGERChg nvarchar  , " +
				"LightPoINTEGERs nvarchar  , " +
				"ACChg nvarchar  , " +
				"ACNos nvarchar  , " +
				"MultipleSPs nvarchar  , " +
				"PrevBillDt nvarchar  , " +
				"CurrMtrStatus nvarchar  , " +
				"MtrChgTotUnitsKwh nvarchar  , " +
				"CeilByPass nvarchar  , " +
				"MtrRemovalDTTM nvarchar  , " +
				"MtrChangeFlg nvarchar  , " +
				"LocType nvarchar  , " +
				"CeilingDefective nvarchar  , " +
				"PrevMRRemark nvarchar  , " +
				"PwrLoomNos nvarchar  , " +
				"BillCycleStartDt smalldatetime , " +
				"BillCycleEndDt nvarchar  , " +
				"ProvisionalKWH nvarchar  , " +
				"ProvisionalKVAH nvarchar  , " +
				"ProvisionalKVA nvarchar  , " +
				"ProvisionalPF nvarchar  , " +
				"ProvisionalReadDt nvarchar  , " +
				"LastMtrChgMRRemark nvarchar  , " +
				"TotSLKVA nvarchar  , " +
				"TotSLBHP nvarchar  , " +
				"CeilLimUnitsKVAH nvarchar  , " +
				"OffSeasonFlg nvarchar  , " +
				"SeasonBenAllow nvarchar  , " +
				"SeasonBenViolCnt nvarchar  , " +
				"ProvEstUnitsKVAH nvarchar  , " +
				"TotAvgEstUnitsKVAH nvarchar  , " +
				"MtrChgTotUnitsKVAH nvarchar  , " +
				"MtrChgAvgMD nvarchar  , " +
				"PayByCash nvarchar  , " +
				"OffSeasonDecMD nvarchar  , " +
				"TdDisturb nvarchar  , " +
				"ArrLpscLastPY nvarchar  , " +
				"ArrLpscLastCY nvarchar  , " +
				"ArrLpscLastBILL nvarchar  , " +
				"MdiPenalty nvarchar  , " +
				"Book No nvarchar  , " +
				"edchgrate nvarchar  , " +
				"gispoleid nvarchar  , " +
				"ID INTEGER  , " +
				"PoleID INTEGER , " +
				"CreatedBy INTEGER , " +
				"UpDatedBy INTEGER , " +
				"CreatedDate varchar , " +
				"UpdatedDate varchar, " +
				"OfficeID INTEGER , " +
				"flag varchar )");
		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS updated_date(ID INTEGER PRIMARY KEY   AUTOINCREMENT,downloaded_at date)");
		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS users(uid int NOT NULL,name varchar(50)not null,email varchar(50) NOT NULL,mobile_no varchar(50) NOT NULL,division varchar(50)NOT NULL,encrypted_password varchar(80) NOT NULL,salt varchar(10)NOT NULL,created_at datetime NOT NULL,updated_at datetime NOT NULL,company int NOT NULL,imei varchar(20) NOT NULL,status varchar(5),logindate date,flag varchar(5))" );

		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS customer_transactions(Acctid varchar(20),BILLNO varchar(20),BILL_date date,MF float,CMD float,CURRENT_KWH float,PREVIOUS_KWH float,CURRENT_KVWH float,PREVIOUS_KVWH float,BILL_PERIOD int,FIXED_CHARGE float,ENERGY_CHARGES float,Min_charge float,ELECTRICITY_DUTY float,REG_SURCHARGES float,EX_DEMAND_PANALITY float,AREAR_AMOUNT float,AREAR_SURCHARGES float,CURRENT_SURCHARGES float,ADJUSTMENT_AMOUNT float,NET_AMOUNT float,Rebate float,Gross_amount float,ID int,Previousdate varchar(20),flag varchar(5),SubmitBy int,image text,sql_flag varchar(5) DEFAULT N )" );

		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS Division_Master(ID  int,Code varchar(30),Name varchar(30),OfficeID int,flag char(1))");
		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS lvm_Fixed_chargeMaster(ID  int NOT NULL,rate real,supplyid varchar(2),MinCharge real,rangefrom int,rangeto int,meter_kwh float,fc_rate float)");
		offlinedb.execSQL("CREATE TABLE IF NOT EXISTS lvm_supply(ID  int,type  varchar(10),lmvid varchar(10))");

		calljson();

	}

	private class JsonReadTask extends AsyncTask<String, Void, String> {
		private final String DIALOG_MESSAGE = "Loading Data";

		private ProgressDialog mDialog = null;

		private void setDialog(Context context) {
			this.mDialog = new ProgressDialog(context);
			this.mDialog.setMessage(DIALOG_MESSAGE);
			this.mDialog.setCancelable(false);
		}

		public JsonReadTask(Context context) {
			this.setDialog(context);
		}

		@Override
		protected void onPreExecute() {
			this.mDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			System.out.println("email123"+saemail);
			nameValuePairs.add(new BasicNameValuePair("user",saemail));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 response1 = httpclient.execute(httppost, responseHandler);
				System.out.println("Response : " + response1);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();

			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}



		@Override
		protected void onPostExecute(String result) {
			ListDrwaer();
			System.out.println("calling list");

			if(this.mDialog.isShowing())
			{
				this.mDialog.dismiss();
			}

		}
	}// end async task

	public void calljson() {
		System.out.println("Called json");
		JsonReadTask task = new JsonReadTask(this);
		// passes values for the urls string array
		task.execute(new String[] { url });
	}

	// build hash set for list view
	public void ListDrwaer() {


		try {
			response1=response1.substring(3);
			JSONObject jsonResponse = new JSONObject(response1);
			System.out.println("JSON IN LIST"+jsonResponse);

			JSONArray jsonMainNode = jsonResponse.optJSONArray("CD_data");
				System.out.println("uhyg"+jsonMainNode.length());
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

					KNo =jsonChildNode.optString("KNo");
					ServiceConnectionNo=jsonChildNode.optString("ServiceConnectionNo");
					ACCTID=jsonChildNode.optString("ACCTID");
					GroupCd=jsonChildNode.optString("GroupCd");
					GroupId=jsonChildNode.optString("GroupId");
					Div = jsonChildNode.optString("Div");
					SubDiv= jsonChildNode.optString("SubDiv");
					DTCode = jsonChildNode.optString("DTCode");
					RouteCd= jsonChildNode.optString("RouteCd");
					RouteSeq= jsonChildNode.optString("RouteSeq");
					ServiceId= jsonChildNode.optString("ServiceId");
					SPId= jsonChildNode.optString("SPId");
					PerName= jsonChildNode.optString("PerName");
					MailAdd1= jsonChildNode.optString("MailAdd1");
					MailAdd2= jsonChildNode.optString("MailAdd2");
					MailAdd3= jsonChildNode.optString("MailAdd3");
					MailAdd4= jsonChildNode.optString("MailAdd4");
					City= jsonChildNode.optString("City");
					PinCode= jsonChildNode.optString("PinCode");
					TariffCateg= jsonChildNode.optString("TariffCateg");
					STCode= jsonChildNode.optString("STCode");
					TotSLKW= jsonChildNode.optString("TotSLKW");
					SupplyDt= jsonChildNode.optString("SupplyDt");
					EmployeeClass= jsonChildNode.optString("EmployeeClass");
					MtrBdgNo= jsonChildNode.optString("MtrBdgNo");
					MtrSrlNo= jsonChildNode.optString("MtrSrlNo");
					CapInst= jsonChildNode.optString("CapInst");
					TVMFlg= jsonChildNode.optString("TVMFlg");
					MultFactor= jsonChildNode.optString("MultFactor");
					ProvEstUnitsKWH= jsonChildNode.optString("ProvEstUnitsKWH");
					TotAvgEstUnits= jsonChildNode.optString("TotAvgEstUnits");
					CeilLimUnits= jsonChildNode.optString("CeilLimUnits");
					PrevReadDt= jsonChildNode.optString("PrevReadDt");
					FirstBill= jsonChildNode.optString("FirstBill");
					PrevKWH= jsonChildNode.optString("PrevKWH");
					PrevKVAH= jsonChildNode.optString("PrevKVAH");
					PrevKVA= jsonChildNode.optString("PrevKVA");
					PrePF= jsonChildNode.optString("PrePF");
					PrevBillId= jsonChildNode.optString("PrevBillId");
					LPCPrevAmt= jsonChildNode.optString("LPCPrevAmt");
					LastBillType= jsonChildNode.optString("LastBillType");
					PrevBillBal= jsonChildNode.optString("PrevBillBal");
					LPCCurAmt= jsonChildNode.optString("LPCCurAmt");
					LPCAmt= jsonChildNode.optString("LPCAmt");
					AdjAftLastBill= jsonChildNode.optString("AdjAftLastBill");
					CorrAftLastBill= jsonChildNode.optString("CorrAftLastBill");
					ProvBillCancelAmt= jsonChildNode.optString("ProvBillCancelAmt");
					NSFAftLastBill= jsonChildNode.optString("NSFAftLastBill");
					LPSCExtDt= jsonChildNode.optString("LPSCExtDt");
					Rule80120= jsonChildNode.optString("Rule80120");
					OtherBsegAmt= jsonChildNode.optString("OtherBsegAmt");
					SDAmt= jsonChildNode.optString("SDAmt");
					ArrearPrevYr= jsonChildNode.optString("ArrearPrevYr");
					ArrearCurYr= jsonChildNode.optString("ArrearCurYr");
					ArrearLastBill= jsonChildNode.optString("ArrearLastBill");
					LPCRate= jsonChildNode.optString("LPCRate");
					TempDisFlg= jsonChildNode.optString("TempDisFlg");
					TempDisDT= jsonChildNode.optString("TempDisDT");
					TempRecDT= jsonChildNode.optString("TempRecDT");
					MultipleUtility= jsonChildNode.optString("MultipleUtility");
					RKWH = jsonChildNode.optString("RKWH");
					RKWHSeq= jsonChildNode.optString("RKWHSeq");
					RKWHDigits= jsonChildNode.optString("RKWHDigits");
					RKVAH= jsonChildNode.optString("RKVAH");
					RKVAHSeq= jsonChildNode.optString("RKVAHSeq");
					RKVAHDigits= jsonChildNode.optString("RKVAHDigits");
					RKVA= jsonChildNode.optString("RKVA");
					RKVASeq= jsonChildNode.optString("RKVASeq");
					RKVADigits= jsonChildNode.optString("RKVADigits");
					RPF = jsonChildNode.optString("RPF");
					RPFSeq= jsonChildNode.optString("RPFSeq");
					RPFDigits= jsonChildNode.optString("RPFDigits");
					MaINTEGERChg= jsonChildNode.optString("maintChg");
					LightPoINTEGERs= jsonChildNode.optString("LightPoints");
					ACChg= jsonChildNode.optString("ACChg");
					ACNos = jsonChildNode.optString("ACNos");
					MultipleSPs= jsonChildNode.optString("MultipleSPs");
					PrevBillDt= jsonChildNode.optString("PrevBillDt");
					CurrMtrStatus= jsonChildNode.optString("CurrMtrStatus");
					MtrChgTotUnitsKwh= jsonChildNode.optString("MtrChgTotUnitsKwh");
					CeilByPass= jsonChildNode.optString("CeilByPass");
					MtrRemovalDTTM= jsonChildNode.optString("MtrRemovalDTTM");
					MtrChangeFlg= jsonChildNode.optString("MtrChangeFlg");
					LocType = jsonChildNode.optString("Div");
					CeilingDefective = jsonChildNode.optString("CeilingDefective");
					PrevMRRemark= jsonChildNode.optString("PrevMRRemark");
					PwrLoomNos= jsonChildNode.optString("PwrLoomNos");
					BillCycleStartDt= jsonChildNode.optString("BillCycleStartDt");
					BillCycleEndDt= jsonChildNode.optString("BillCycleEndDt");
					ProvisionalKWH= jsonChildNode.optString("ProvisionalKWH");
					ProvisionalKVAH= jsonChildNode.optString("ProvisionalKVAH");
					ProvisionalKVA= jsonChildNode.optString("ProvisionalKVA");
					ProvisionalPF = jsonChildNode.optString("ProvisionalPF");
					ProvisionalReadDt= jsonChildNode.optString("ProvisionalReadDt");
					LastMtrChgMRRemark= jsonChildNode.optString("LastMtrChgMRRemark");
					TotSLKVA= jsonChildNode.optString("TotSLKVA");
					TotSLBHP = jsonChildNode.optString("TotSLBHP");
					CeilLimUnitsKVAH = jsonChildNode.optString("CeilLimUnitsKVAH");
					OffSeasonFlg= jsonChildNode.optString("OffSeasonFlg");
					SeasonBenAllow = jsonChildNode.optString("SeasonBenAllow");
					SeasonBenViolCnt = jsonChildNode.optString("SeasonBenViolCnt");
					ProvEstUnitsKVAH= jsonChildNode.optString("ProvEstUnitsKVAH");
					TotAvgEstUnitsKVAH= jsonChildNode.optString("TotAvgEstUnitsKVAH");
					MtrChgTotUnitsKVAH= jsonChildNode.optString("MtrChgTotUnitsKVAH");
					MtrChgAvgMD= jsonChildNode.optString("MtrChgAvgMD");
					PayByCash= jsonChildNode.optString("PayByCash");
					OffSeasonDecMD= jsonChildNode.optString("OffSeasonDecMD");
					TdDisturb= jsonChildNode.optString("TdDisturb");
					ArrLpscLastPY = jsonChildNode.optString("ArrLpscLastPY");
					ArrLpscLastCY = jsonChildNode.optString("ArrLpscLastCY");
					ArrLpscLastBILL= jsonChildNode.optString("ArrLpscLastBILL");
					MdiPenalty= jsonChildNode.optString("MdiPenalty");
					BookNo= jsonChildNode.optString("Book No");
					edchgrate= jsonChildNode.optString("edchgrate");
					gispoleid= jsonChildNode.optString("gispoleid");
					ID = jsonChildNode.optString("ID");
					PoleID = jsonChildNode.optString("PoleID");
					CreatedBy= jsonChildNode.optString("CreatedBy");
					UpDatedBy= jsonChildNode.optString("UpDatedBy");
					CreatedDate= jsonChildNode.optString("CreatedDate");
					UpdatedDate= jsonChildNode.optString("UpdatedDate");
					OfficeID= jsonChildNode.optString("OfficeID");
					flag= jsonChildNode.optString("flag");

					offlinedb.execSQL("INSERT INTO Customer_Details VALUES('"+KNo+"','"+ServiceConnectionNo+"','"+ACCTID+"','"+GroupCd+"','"+GroupId+"','"+Div+"','"+SubDiv+"','"+DTCode+"','"+RouteCd+"','"+RouteSeq+"','"+ServiceId+"','"+SPId+"','"+PerName+"','"+MailAdd1+"','"+MailAdd2
							+"','"+MailAdd3+"','"+MailAdd4+"','"+City+"','"+PinCode+"','"+TariffCateg+"','"+STCode+"','"+TotSLKW+"','"+SupplyDt+"','"+EmployeeClass+"','"+MtrBdgNo+"','"+MtrSrlNo
							+"','"+CapInst+"','"+TVMFlg+"','"+MultFactor+"','"+ProvEstUnitsKWH+"','"+TotAvgEstUnits+"','"+CeilLimUnits+"','"+PrevReadDt+"','"+FirstBill+"','"+PrevKWH
							+"','"+PrevKVAH+"','"+PrevKVA+"','"+PrePF+"','"+PrevBillId+"','"+LPCPrevAmt+"','"+LastBillType+"','"+PrevBillBal+"','"+LPCCurAmt+"','"+LPCAmt+"','"+AdjAftLastBill+"','"+CorrAftLastBill
							+"','"+ProvBillCancelAmt+"','"+NSFAftLastBill+"','"+LPSCExtDt+"','"+Rule80120+"','"+OtherBsegAmt+"','"+SDAmt+"','"+ArrearPrevYr+"','"+ArrearCurYr+"','"+ArrearLastBill
							+"','"+LPCRate+"','"+TempDisFlg+"','"+TempDisDT+"','"+TempRecDT+"','"+MultipleUtility+"','"+RKWH+"','"+RKWHSeq+"','"+RKWHDigits+"','"+RKVAH+"','"+RKVAHSeq
							+"','"+RKVAHDigits+"','"+RKVA+"','"+RKVASeq+"','"+RKVADigits+"','"+RPF+"','"+RPFSeq+"','"+RPFDigits+"','"+MaINTEGERChg+"','"+LightPoINTEGERs+"','"+ACChg+"','"+ACNos+"','"+
							MultipleSPs+"','"+PrevBillDt+"','"+CurrMtrStatus+"','"+MtrChgTotUnitsKwh+"','"+CeilByPass+"','"+MtrRemovalDTTM+"','"+MtrChangeFlg+"','"+LocType+"','"+CeilingDefective
							+"','"+PrevMRRemark+"','"+PwrLoomNos+"','"+BillCycleStartDt+"','"+BillCycleEndDt+"','"+ProvisionalKWH+"','"+ProvisionalKVAH+"','"+ProvisionalKVA+"','"+ProvisionalPF
							+"','"+ProvisionalReadDt+"','"+LastMtrChgMRRemark+"','"+TotSLKVA+"','"+TotSLBHP+"','"+CeilLimUnitsKVAH+"','"+OffSeasonFlg+"','"+SeasonBenAllow
							+"','"+SeasonBenViolCnt+"','"+ProvEstUnitsKVAH+"','"+TotAvgEstUnitsKVAH+"','"+MtrChgTotUnitsKVAH+"','"+MtrChgAvgMD+"','"+PayByCash+"','"+OffSeasonDecMD
							+"','"+TdDisturb+"','"+ArrLpscLastPY+"','"+ArrLpscLastCY+"','"+ArrLpscLastBILL+"','"+MdiPenalty+"','"+BookNo+"','"+edchgrate+"','"+gispoleid+"','"+ID+"','"+PoleID+"','"+CreatedBy+"','"+UpDatedBy
							+"','"+CreatedDate+"','"+UpdatedDate+"','"+OfficeID+"','"+flag+"');");

				}
			JSONArray jsonMainNode1 = jsonResponse.optJSONArray("users");
			System.out.println("2"+jsonMainNode1.length());
			for (int i = 0; i < jsonMainNode1.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode1.getJSONObject(i);
				uid= jsonChildNode.optString("uid");
				name= jsonChildNode.optString("name");
				email= jsonChildNode.optString("email");
				mobile_no= jsonChildNode.optString("mobile_no");
				division= jsonChildNode.optString("division");
				encrypted_password= jsonChildNode.optString("encrypted_password");
				salt= jsonChildNode.optString("salt");
				created_at= jsonChildNode.optString("created_at");
				updated_at= jsonChildNode.optString("updated_at");
				company= jsonChildNode.optString("company");
				imei= jsonChildNode.optString("imei");
				status= jsonChildNode.optString("status");
				logindate= jsonChildNode.optString("logindate");
				flag1= jsonChildNode.optString("flag");
				offlinedb.execSQL("INSERT INTO users VALUES('"+uid+"','"+name+"','"+email+"','"+mobile_no+"','"+division+"','"+encrypted_password+"','"+salt+"','"+created_at+"','"+updated_at+"','"+company+"','"+imei+"','"+status+"','"+logindate+"','"+flag1+"');");
			}

			JSONArray jsonMainNode2 = jsonResponse.optJSONArray("CT_data");
			System.out.println("2"+jsonMainNode2.length());
			for (int i = 0; i < jsonMainNode2.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode2.getJSONObject(i);
				Acctid = jsonChildNode.optString("Acctid");
				BILLNO = jsonChildNode.optString("BILLNO");
				BILL_date = jsonChildNode.optString("BILL_date");
				BILL_date=BILL_date.substring(9, 19);
				ctMF = jsonChildNode.optString("MF");
				ctCMD = jsonChildNode.optString("CMD");
				CURRENT_KWH = jsonChildNode.optString("CURRENR_KWH");
				PREVIOUS_KWH = jsonChildNode.optString("PREVIOUS_KWH");
				CURRENT_KVWH = jsonChildNode.optString("CURRENT_KVWH");
				PREVIOUS_KVWH = jsonChildNode.optString("PREVIOUS_KVWH");
				BILL_PERIOD = jsonChildNode.optString("BILL_PERIOD");
				FIXED_CHARGE = jsonChildNode.optString("FIXED_CHARGE");
				ENERGY_CHARGES = jsonChildNode.optString("ENERGY_CHARGE");
				Min_charge = jsonChildNode.optString("Min_charge");
				ctCURRENT_SURCHARGES = jsonChildNode.optString("CURRENT_SURCHARGES");
				ELECTRICITY_DUTY = jsonChildNode.optString("ELECTRICITY_DUTY");
				REG_SURCHARGES = jsonChildNode.optString("REG_SURCHARGES");
				EX_DEMAND_PANALITY = jsonChildNode.optString("EX_DEMAND_PANALITY");
				AREAR_AMOUNT = jsonChildNode.optString("AREAR_AMOUNT");
				AREAR_SURCHARGES = jsonChildNode.optString("AREAR_SURCHARGES");
				ADJUSTMENT_AMOUNT = jsonChildNode.optString("ADJUSTMENT_AMOUNT");
				NET_AMOUNT = jsonChildNode.optString("NET_AMOUNT");
				Rebate = jsonChildNode.optString("Rebate");
				Gross_amount = jsonChildNode.optString("Gross_amount");
				ctID = jsonChildNode.optString("ID");
				Previousdate = jsonChildNode.optString("Previousdate");
				ctflag = jsonChildNode.optString("flag");
				SubmitBy = jsonChildNode.optString("SubmitBy");
				ctimage = jsonChildNode.optString("image");
				System.out.println("falgin"+ctflag);
				offlinedb.execSQL("INSERT INTO customer_transactions VALUES('" + Acctid + "','" + BILLNO + "','" + BILL_date + "','" + ctMF + "','" + ctCMD + "','" + CURRENT_KWH + "','" + PREVIOUS_KWH + "','" + CURRENT_KVWH + "','" + PREVIOUS_KVWH + "','" + BILL_PERIOD + "','" + FIXED_CHARGE + "','" + ENERGY_CHARGES + "','" + Min_charge + "','" + ELECTRICITY_DUTY + "','" + REG_SURCHARGES + "','" + EX_DEMAND_PANALITY + "','" + AREAR_AMOUNT + "','" + AREAR_SURCHARGES + "','" + ctCURRENT_SURCHARGES + "','" + ADJUSTMENT_AMOUNT + "','" + NET_AMOUNT + "','" + Rebate + "','" + Gross_amount + "','" + ctID + "','" + Previousdate + "','" + ctflag + "','" + SubmitBy + "','" + ctimage + "','"+'N'+"');");
			}

				JSONArray jsonMainNode3 = jsonResponse.optJSONArray("div_master");
				System.out.println("2" + jsonMainNode3.length());
				for (int i = 0; i < jsonMainNode3.length(); i++) {
					JSONObject jsonChildNode3 = jsonMainNode3.getJSONObject(i);

					dmID = jsonChildNode3.optString("ID");
					dmCode = jsonChildNode3.optString("Code");
					dmName = jsonChildNode3.optString("Name");
					dmOfficeID = jsonChildNode3.optString("OfficeID");
					dmflag = jsonChildNode3.optString("flag");

					offlinedb.execSQL("INSERT INTO Division_Master VALUES('"+dmID+"','"+dmCode+"','"+dmName+"','"+dmOfficeID+"','"+dmflag+"');");

				}
			JSONArray jsonMainNode4 = jsonResponse.optJSONArray("supply");
			System.out.println("4"+jsonMainNode4.length());
			for (int i = 0; i < jsonMainNode4.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode4.getJSONObject(i);
				sid= jsonChildNode.optString("id");
				stype= jsonChildNode.optString("type");
				slvmid= jsonChildNode.optString("lvmid");
				offlinedb.execSQL("INSERT INTO lvm_supply VALUES('"+sid+"','"+stype+"','"+slvmid+"');");
			}
			JSONArray jsonMainNode5 = jsonResponse.optJSONArray("rate");
			System.out.println("5"+jsonMainNode5.length());
			for (int i = 0; i < jsonMainNode5.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode5.getJSONObject(i);
				rid= jsonChildNode.optString("id");
				rrate= jsonChildNode.optString("rate");
				rsupplyid= jsonChildNode.optString("supplyid");
				rminchrg= jsonChildNode.optString("MinChage");
				rrangefrom= jsonChildNode.optString("rangefrom");
				rrangeto= jsonChildNode.optString("rangeto");
				rmeterkwh= jsonChildNode.optString("meter_kwh");
				rfcrate= jsonChildNode.optString("fc_rate");
				offlinedb.execSQL("INSERT INTO lvm_Fixed_chargeMaster VALUES('"+rid+"','"+rrate+"','"+rsupplyid+"','"+rminchrg+"','"+rrangefrom+"','"+rrangeto+"','"+rmeterkwh+"','"+rfcrate+"');");
			}
			Calendar c = Calendar.getInstance();
			System.out.println("InsertTime => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String in1 = df.format(c.getTime());
			System.out.println("hello"+in1);

			offlinedb.execSQL("INSERT INTO updated_date VALUES(NULL,'" + in1 + "');");
			callactivity();

		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("Exception listdr"+e);
		}

	}

	public StringBuilder inputStreamToString(InputStream is) {
		String rLine = "";
		StringBuilder answer = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((rLine = rd.readLine()) != null) {
				answer.append(rLine);

			}
		}

		catch (IOException e) {
			// e.printStackTrace();
			e.printStackTrace();
			System.out.println("Exception"+e);
		}
		return answer;
	}
	void callactivity()
	{
		//System.out.println("data on ct"+ctdata);
		Intent intent1 = new Intent(SplashActivity.this,OfflineMain.class);
		intent1.putExtra("email",saemail);
		intent1.putExtra("div",div);
		startActivity(intent1);

	}
}
