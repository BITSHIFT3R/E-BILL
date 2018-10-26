package com.android4dev.navigationview;


import com.android4dev.navigationview.pockdata.PocketPos;
import com.android4dev.navigationview.util.DataConstants;
import com.android4dev.navigationview.util.DateUtil;
import com.android4dev.navigationview.util.FontDefine;
import com.android4dev.navigationview.util.Printer;
import com.android4dev.navigationview.util.StringUtil;
import com.android4dev.navigationview.util.Util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;



import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


public class Mainprint extends Fragment {
	String oficeAddress,bookNumber,SCNumber,meterNuber,
			consumerName,consumerAddress,OverALLReading,PreviousReadin,
			currentKVWH,MeteredKVWH,LastPayment,LastPaymentDate,ArrSurChrgs,
			CurSurChrgs,AdjstAmt,REBATE,BILLNO,Previousdate,DiscountDate,PreviousKVWH,BillBasis
			,EXDamagePanality,ElecticityDuty,cmd,grossamt;
	private Button mConnectBtn;
	private Button mEnableBtn;
	private Button mPrintDemoBtn;
	private Button mPrintBarcodeBtn;
	private Button mPrintImageBtn;
	private Button mPrintReceiptBtn;
	private Button mPrintTextBtn;
	private Spinner mDeviceSp;	
	  String name,fname,add,mtrno,no,nom,sarea,empid,emp_id,mtr_no,bdate,prevrd,presrd,bamt,mdem,ubill,ddate,ca,la,pa;
		Double a;
	String spcode, arfcode,conload,billperiod="1",minchrg="0.00",rgchrg,arramt;
	private ProgressDialog mProgressDlg;
	private ProgressDialog mConnectingDlg;
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private P25Connector mConnector;
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.printmain, container, false);
		setRetainInstance(true);
		setHasOptionsMenu(true);

		Bundle bundle=getArguments();
		//Intent intent = getActivity().getIntent();
		///mtrno= intent.getStringExtra("ddate");
		DecimalFormat df2 = new DecimalFormat("###.##");
		   bdate = bundle.getString("acno");

		   presrd= bundle.getString("currentreading");
			a=Math.round(Double.valueOf(presrd)*100.0)/100.0;
			presrd=a.toString();
		   prevrd= bundle.getString("pre");
			a=Math.round(Double.valueOf(prevrd)*100.0)/100.0;
			prevrd=a.toString();
		ca=bundle.getString("CeilingAmmt");
		a=Math.round(Double.valueOf(ca)*100.0)/100.0;
		ca=a.toString();
		la=bundle.getString("TexAmmt");
		a=Math.round(Double.valueOf(la)*100.0)/100.0;
		la=a.toString();
		pa=bundle.getString("LPCAmt");
		a=Math.round(Double.valueOf(pa)*100.0)/100.0;
		pa=a.toString();
		   ubill= bundle.getString("FC");
		a=Math.round(Double.valueOf(ubill)*100.0)/100.0;
		ubill=a.toString();
		   ddate= bundle.getString("EC");
		a=Math.round(Double.valueOf(ddate)*100.0)/100.0;
		ddate=a.toString();
		bamt= bundle.getString("total");
		a=Math.round(Double.valueOf(bamt)*100.0)/100.0;
		bamt=a.toString();
		rgchrg= bundle.getString("reg");
		a=Math.round(Double.valueOf(rgchrg)*100.0)/100.0;
		rgchrg=a.toString();
		arramt=bundle.getString("SBMBillArrearAmt");
		a=Math.round(Double.valueOf(arramt)*100.0)/100.0;
		arramt=a.toString();
		oficeAddress= bundle.getString("oficeAddress");
		bookNumber=bundle.getString("bookNumber");
		SCNumber=bundle.getString("SCNumber");
		meterNuber=bundle.getString("meterNuber");
		consumerName=bundle.getString("consumerName");
		consumerAddress=bundle.getString("consumerAddress");
		OverALLReading=bundle.getString("OverALLReading");
		PreviousReadin=bundle.getString("PreviousReadin");
		currentKVWH=bundle.getString("currentKVWH");
		MeteredKVWH=bundle.getString("MeteredKVWH");
		LastPayment=bundle.getString("LastPayment");
		LastPaymentDate=bundle.getString("LastPaymentDate");
		ArrSurChrgs=bundle.getString("ArrSurChrgs");
		CurSurChrgs=bundle.getString("CurSurChrgs");
		AdjstAmt=bundle.getString("AdjstAmt");
		REBATE=bundle.getString("REBATE");
		BILLNO=bundle.getString("BILLNO");
		Previousdate=bundle.getString("Previousdate");
		DiscountDate=bundle.getString("DiscountDate");
		PreviousKVWH=bundle.getString("PreviousKVWH");
		BillBasis=bundle.getString("BillBasis");
		EXDamagePanality=bundle.getString("EXDamagePanality");
		ElecticityDuty=bundle.getString("ElecticityDuty");
		a=Math.round(Double.valueOf(ElecticityDuty)*100.0)/100.0;
		ElecticityDuty=a.toString();
		grossamt=bundle.getString("grossamt");
		conload=bundle.getString("conload");
		cmd=bundle.getString("cmd");
		spcode=bundle.getString("spcode");
		arfcode=bundle.getString("arfcode");
		a=Math.round(Double.valueOf(cmd)*100.0)/100.0;
		cmd=a.toString();
		System.out.println("mtrno" + mtrno + "\n" + "bdate" + bdate + "\n"
				+ "presrd" + presrd + "\n" + "prevrd" + prevrd + "\n" +
				"ca" + ca + "\n" + "la" + la + "\n" + "pa" + pa + "\n"
				+ "ubill" + ubill + "\n" + "ddate" + ddate + "\n");
		   Toast.makeText(getActivity().getApplicationContext(), mtrno+bdate , Toast.LENGTH_SHORT).show();
		mConnectBtn			= (Button)v.findViewById(R.id.btn_connect);
		mEnableBtn			= (Button)v.findViewById(R.id.btn_enable);
		mPrintDemoBtn 		= (Button)v.findViewById(R.id.btn_print_demo);
		mPrintBarcodeBtn 	= (Button)v.findViewById(R.id.btn_print_barcode);
		mPrintImageBtn 		= (Button)v.findViewById(R.id.btn_print_image);
		mPrintReceiptBtn 	= (Button)v.findViewById(R.id.btn_print_receipt);
		mPrintTextBtn		= (Button)v.findViewById(R.id.btn_print_text);
		mDeviceSp 			= (Spinner)v.findViewById(R.id.sp_device);
		
		mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
				
		if (mBluetoothAdapter == null) {
			showUnsupported();
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				showDisabled();
			} else {
				showEnabled();
				
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				
				if (pairedDevices != null) {
					mDeviceList.addAll(pairedDevices);
					
					updateDeviceList();
				}
			}
			
			mProgressDlg 	= new ProgressDialog(getActivity());
			
			mProgressDlg.setMessage("Scanning...");
			mProgressDlg.setCancelable(false);
			mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.dismiss();
			        
			        mBluetoothAdapter.cancelDiscovery();
			    }
			});
			
			mConnectingDlg 	= new ProgressDialog(getActivity());
			
			mConnectingDlg.setMessage("Connecting...");
			mConnectingDlg.setCancelable(false);
			
			mConnector 		= new P25Connector(new P25Connector.P25ConnectionListener() {
				
				@Override
				public void onStartConnecting() {
					mConnectingDlg.show();
				}
				
				@Override
				public void onConnectionSuccess() {
					mConnectingDlg.dismiss();
					
					showConnected();
				}
				
				@Override
				public void onConnectionFailed(String error) {
					mConnectingDlg.dismiss();
				}
				
				@Override
				public void onConnectionCancelled() {
					mConnectingDlg.dismiss();
				}
				
				@Override
				public void onDisconnected() {
					showDisonnected();
				}
			});
			
			//enable bluetooth
			mEnableBtn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {					
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						
					startActivityForResult(intent, 1000);					
				}
			});
			
			//connect/disconnect
			mConnectBtn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					connect();			
				}
			});
			
			//print demo text

			
			//print text

			
			//print image bitmap

			//print barcode 1D or 2D

			
			//print struk
			mPrintReceiptBtn.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					printStruk();
				}
			});
		}
		
		IntentFilter filter = new IntentFilter();
		
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		
		getActivity().registerReceiver(mReceiver, filter);
	return v;
	}


	@Override
	public void onPause() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}			
		}
		
		if (mConnector != null) {
			try {
				mConnector.disconnect();
			} catch (P25ConnectionException e) {
				e.printStackTrace();
			}
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}
	
	private String[] getArray(ArrayList<BluetoothDevice> data) {
		String[] list = new String[0];
		
		if (data == null) return list;
		
		int size	= data.size();
		list		= new String[size];
		
		for (int i = 0; i < size; i++) {
			list[i] = data.get(i).getName();
		}

		return list;	
	}
	
	private void showToast(String message) {
		Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	private void updateDeviceList() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, getArray(mDeviceList));
		
		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		
		mDeviceSp.setAdapter(adapter);
		mDeviceSp.setSelection(0);
	}

	private void showDisabled() {
		showToast("Bluetooth disabled");
		
		mEnableBtn.setVisibility(View.VISIBLE);		
		mConnectBtn.setVisibility(View.GONE);
		mDeviceSp.setVisibility(View.GONE);
	}

	private void showEnabled() {
		showToast("Bluetooth enabled");
		
		mEnableBtn.setVisibility(View.GONE);		
		mConnectBtn.setVisibility(View.VISIBLE);
		mDeviceSp.setVisibility(View.VISIBLE);
	}
	
	private void showUnsupported() {
		showToast("Bluetooth is unsupported by this device");

		mConnectBtn.setEnabled(false);		
		mPrintDemoBtn.setEnabled(false);
		mPrintBarcodeBtn.setEnabled(false);
		mPrintImageBtn.setEnabled(false);
		mPrintReceiptBtn.setEnabled(false);
		mPrintTextBtn.setEnabled(false);
		mDeviceSp.setEnabled(false);
	}
	
	private void showConnected() {
		showToast("Connected");
		
		mConnectBtn.setText("Disconnect");
		
		mPrintDemoBtn.setEnabled(true);
		mPrintBarcodeBtn.setEnabled(true);
		mPrintImageBtn.setEnabled(true);
		mPrintReceiptBtn.setEnabled(true);
		mPrintTextBtn.setEnabled(true);
		
		mDeviceSp.setEnabled(false);
	}
	
	private void showDisonnected() {
		showToast("Disconnected");
		
		mConnectBtn.setText("Connect");
		
		mPrintDemoBtn.setEnabled(false);
		mPrintBarcodeBtn.setEnabled(false);
		mPrintImageBtn.setEnabled(false);
		mPrintReceiptBtn.setEnabled(false);
		mPrintTextBtn.setEnabled(false);
		
		mDeviceSp.setEnabled(true);
	}
	
	private void connect() {
		if (mDeviceList == null || mDeviceList.size() == 0) {
			return;
		}
		
		BluetoothDevice device = mDeviceList.get(mDeviceSp.getSelectedItemPosition());
		
		if (device.getBondState() == BluetoothDevice.BOND_NONE) {
			try {
				createBond(device);
			} catch (Exception e) {
				showToast("Failed to pair device");
				
				return;
			}
		}
		
		try {
			if (!mConnector.isConnected()) {
				mConnector.connect(device);
			} else {
				mConnector.disconnect();
				
				showDisonnected();
			}
		} catch (P25ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	private void createBond(BluetoothDevice device) throws Exception { 
        
        try {
            Class<?> cl 	= Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par 	= {};
            
            Method method 	= cl.getMethod("createBond", par);
            
            method.invoke(device);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            throw e;
        }
    }
	
	private void sendData(byte[] bytes) {
		try {
			mConnector.sendData(bytes);
		} catch (P25ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	private void printStruk() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date()); // Now use today date.
		c1.add(Calendar.DATE,7); // Adding 7 days
		String output = sdf.format(c1.getTime());

		String OfficeAddress=oficeAddress;

		String jpaRef	= "XXXX-XXXX-XXXX-XXXX";
		String message	= "MSSPL Private Limited";
		//String message2	= "" + "\n";
		StringBuilder address=new StringBuilder();
		address.append("\n");
		address.append("\n");
		address.append(Util.center(OfficeAddress, DataConstants.RECEIPT_WIDTH) + "\n");

		StringBuilder printRecpt=new StringBuilder();
		printRecpt.append(Util.nameLeftValueRightJustify("ACCOUNT NO:" + "\t", bdate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Book NO:",bookNumber, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("SC NO:",SCNumber, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Meter NO:",meterNuber, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CONSUMER NAME:",consumerName, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("ADDRESS:" , consumerAddress, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("BILL NO :" ,BILLNO, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("PREVIOUS DATE:",Previousdate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Bill DATE:",formattedDate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("DUE DATE:",output, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("DISC DATE:",DiscountDate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("TARIFF CODE:",arfcode, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("SUPPLY TYPE:",spcode, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("BILL BASIS:",BillBasis, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Contracted Load:",conload, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("MF:","1.00", DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CMD:",cmd, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("KWH MD:","0.00", DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CURRENT KWH:",OverALLReading, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("PREVIOUS KWH:",PreviousReadin, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("METERED KWH:",prevrd, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CURRENT KVWH:",currentKVWH, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("PREVIOUS KVWH:",PreviousKVWH, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("METERED KVWH:",MeteredKVWH, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CHARGED UNIT:",prevrd, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("LAST PAYMENT AMT:",LastPayment, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("LAST PAYMENT DATE:",LastPaymentDate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Bill Period:",billperiod, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("FIXED CHARGES:",ubill, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("ENERGY CHARGES:",ddate, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("Min Charges:",minchrg, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("ELECTRICITY DUTY:",ElecticityDuty, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("REG SURCHARGES:",rgchrg, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("EX DEMAND PENALTY:",EXDamagePanality, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("AREAR AMMOUNT:",arramt, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("AREAR SURCHARGES:",ArrSurChrgs, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("CURRENT SURCHARGES:",CurSurChrgs, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("ADJUSTMENT AMMOUNT:",AdjstAmt, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("MISC CHARGE :","0.00", DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("NET AMOUNT:",bamt, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.nameLeftValueRightJustify("REBATE:",REBATE, DataConstants.RECEIPT_WIDTH) + "\n");
		//printRecpt.append(Util.nameLeftValueRightJustify("GROSS AMOUNT:",grossamt, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append("\n");
		printRecpt.append("\n");
		printRecpt.append(Util.center(jpaRef, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append(Util.center(message, DataConstants.RECEIPT_WIDTH) + "\n");
		printRecpt.append("\n");
		printRecpt.append("\n");
		//	long milis		= System.currentTimeMillis();
		//String date		= DateUtil.timeMilisToString(milis, "dd-MM-yy / HH:mm")  + "\n\n";


		byte[] AddressByte	= Printer.printfont(address.toString(), FontDefine.FONT_24PX,FontDefine.Align_CENTER,
				(byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

		byte[] OfficeAddressByte	= Printer.printfont(printRecpt.toString(), FontDefine.FONT_24PX,FontDefine.Align_RIGHT,
				(byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

		

	//	byte[] message2Byte	= Printer.printfont(message2, FontDefine.FONT_24PX,FontDefine.Align_CENTER,  (byte)0x1A, 
		//						PocketPos.LANGUAGE_ENGLISH);
		
	//	byte[] dateByte		= Printer.printfont(date, FontDefine.FONT_24PX,FontDefine.Align_LEFT, (byte)0x1A, 
		//						PocketPos.LANGUAGE_ENGLISH);
		
		byte[] totalByte	= new byte[AddressByte.length+ +OfficeAddressByte.length ];
		
		
		int offset = 0;

		System.arraycopy(AddressByte, 0, totalByte, offset, AddressByte.length);
		offset += AddressByte.length;

		System.arraycopy(OfficeAddressByte, 0, totalByte, offset, OfficeAddressByte.length);
		offset += OfficeAddressByte.length;



		
		//System.arraycopy(message2Byte, 0, totalByte, offset, message2Byte.length);
		//offset += message2Byte.length;
		
		//System.arraycopy(dateByte, 0, totalByte, offset, dateByte.length);
		
		byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totalByte, 0, totalByte.length);

		sendData(senddata);	
	}






	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();

	        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
	        	final int state 	= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

	        	if (state == BluetoothAdapter.STATE_ON) {
	        		showEnabled();
	        	} else if (state == BluetoothAdapter.STATE_OFF) {
		        	showDisabled();
	        	}
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	mDeviceList = new ArrayList<BluetoothDevice>();

				mProgressDlg.show();
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	mProgressDlg.dismiss();

	        	updateDeviceList();
	        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	        	mDeviceList.add(device);

	        	showToast("Found device " + device.getName());
	        } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
	        	 final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

	        	 if (state == BluetoothDevice.BOND_BONDED) {
	        		 showToast("Paired");

	        		 connect();
	        	 }
	        }
	    }
	};
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

		getActivity().getMenuInflater().inflate(R.menu.main, menu);

		MenuItem Import = menu.findItem(R.id.action_settings1);
		Import.setVisible(false);
		MenuItem Import2 = menu.findItem(R.id.action_settings3);
		Import2.setVisible(false);
		MenuItem Import3 = menu.findItem(R.id.action_home);
		Import3.setVisible(false);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("in options");
		if (item.getItemId() == R.id.action_scan) {
			System.out.println("in if 1");
			mBluetoothAdapter.startDiscovery();
		}


		return super.onOptionsItemSelected(item);
	}

}