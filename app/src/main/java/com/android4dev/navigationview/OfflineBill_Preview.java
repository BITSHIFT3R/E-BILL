package com.android4dev.navigationview; /**
 * Created by sanjay on 22-Apr-16.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android4dev.navigationview.Mainprint;
import com.android4dev.navigationview.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OfflineBill_Preview extends Fragment {
    SQLiteDatabase db;
    String bill_no,c1;
    int c2;
    String oficeAddress,bookNumber,SCNumber,meterNuber,
            consumerName,consumerAddress,OverALLReading,PreviousReadin,
            currentKVWH,MeteredKVWH,LastPayment,LastPaymentDate,ArrSurChrgs,
            CurSurChrgs,AdjstAmt,REBATE,BILLNO,Previousdate,DiscountDate,PreviousKVWH,BillBasis
            ,EXDamagePanality,ElecticityDuty,cmd1;
    String name,fname,add,mtrno,no,nom,sarea,empid,emp_id,mtr_no,bdate,prevrd,presrd,bamt,mdem,ubill,ddate,ca,la,pa;
    Double a;String formattedDate,output;
    String spcode, arfcode,conload,billperiod="1",minchrg="0.00",rgchrg,arramt;
    String email;
    TextView address,accno,bookno,scno,meterno,consname,conadrs
            ,billno,preDate,billData,dueDate,
            discDate,tarfCode,supType,bilBasis,cotrLoad,mf,cmd,kwhMD,crrKWH,preKWH,mtrKWH,
            crrKVWH,prvKVWH,mtrKVWH,chrgUnit,LPA,LPD,billPeriod,FC,EC,minChrg,elctDuty,regSurChrg
            ,exDmdPnty,arrAmt,arrSurChrg,crrSurChrg,adjAmt,miscChrgs,netAmt,rebt,grsAmt;

    Button print,pdf;
    String Imagepart;
    ImageView image;
    String latitude,longitude,location,grossammount;
    Double arreramt,reb;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bill_preview, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRetainInstance(true);
        setHasOptionsMenu(false);
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
        arreramt=Math.round(Double.valueOf(arramt)*100.0)/100.0;
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
        reb=Double.parseDouble(REBATE);
        BILLNO=bundle.getString("BILLNO");
        Previousdate=bundle.getString("Previousdate");
        DiscountDate=bundle.getString("DiscountDate");
        PreviousKVWH=bundle.getString("PreviousKVWH");
        BillBasis=bundle.getString("BillBasis");
        EXDamagePanality=bundle.getString("EXDamagePanality");
        ElecticityDuty=bundle.getString("ElecticityDuty");
        a=Math.round(Double.valueOf(ElecticityDuty)*100.0)/100.0;
        ElecticityDuty=a.toString();
        cmd1=bundle.getString("cmd");
        a=Math.round(Double.valueOf(cmd1)*100.0)/100.0;
        cmd1=a.toString();
        Imagepart=bundle.getString("Image");
        email=bundle.getString("email");
        latitude=bundle.getString("latitude");
        longitude=bundle.getString("longitude");
        location=bundle.getString("location");
        arfcode=bundle.getString("arfcode");
        spcode=bundle.getString("supplytype");
        conload=bundle.getString("conload");
        grossammount=bundle.getString("GrossAmount");
        System.out.println("helloo1"+grossammount);
        a=Math.round(Double.valueOf(grossammount)*100.0)/100.0;
        grossammount=a.toString();
        System.out.println("helloo2"+grossammount);
        System.out.println("location On bill"+latitude+"-"+longitude+"-"+location);
        System.out.println("presrd"+presrd+"\n"+"prevrd"+prevrd+"\n"
                +"ca"+ca+"\n"+"la"+la+"\n"+"pa"+pa+"\n"
                +"ddate"+ddate+"\n"+"bamt"+bamt+"\n"+"rgchrg"+rgchrg+"\n"
                +"arramt"+arramt+"\n"+"oficeAddress"+oficeAddress+"\n"+"bookNumber"+bookNumber+"\n"
                +"SCNumber"+SCNumber+"\n"+"la"+la+"\n"+"pa"+pa+"\n"
                +"ca"+ca+"\n"+"la"+la+"\n"+"pa"+pa+"\n"
                +"ca"+ca+"\n"+"la"+la+"\n"+"pa"+pa+"\n");
        address=(TextView)v.findViewById(R.id.bplesaaddress);
        accno= (TextView)v.findViewById(R.id.bpan);
        bookno= (TextView)v.findViewById(R.id.bpbn);
        scno= (TextView)v.findViewById(R.id.bpsn);
        meterno= (TextView)v.findViewById(R.id.bpmn);
        consname= (TextView)v.findViewById(R.id.bpcn);
        conadrs= (TextView)v.findViewById(R.id.bpcaddress);
        billno= (TextView)v.findViewById(R.id.bpbino);
        preDate= (TextView)v.findViewById(R.id.bppredate);
        billData= (TextView)v.findViewById(R.id.bpbilldate);
        dueDate= (TextView)v.findViewById(R.id.bpduedate);
        discDate= (TextView)v.findViewById(R.id.bpdiscdate);
        tarfCode= (TextView)v.findViewById(R.id.bptrrifcode);
        supType= (TextView)v.findViewById(R.id.bpsupplytype);
        bilBasis= (TextView)v.findViewById(R.id.bpbillbasis);
        cotrLoad= (TextView)v.findViewById(R.id.bpcontload);
        mf= (TextView)v.findViewById(R.id.bpmf);
        cmd= (TextView)v.findViewById(R.id.bpcmd);
        kwhMD= (TextView)v.findViewById(R.id.bpkwhmd);
        crrKWH= (TextView)v.findViewById(R.id.bpcrrkwh);
        preKWH= (TextView)v.findViewById(R.id.bpprekwh);
        mtrKWH= (TextView)v.findViewById(R.id.bpmtrkwh);
        crrKVWH= (TextView)v.findViewById(R.id.bpcrrkvwh);
        prvKVWH= (TextView)v.findViewById(R.id.bpprvkvwh);
        mtrKVWH= (TextView)v.findViewById(R.id.bpmtrkvwh);
        chrgUnit= (TextView)v.findViewById(R.id.bpchrgunit);
        LPA= (TextView)v.findViewById(R.id.bplpa);
        LPD= (TextView)v.findViewById(R.id.bplpd);
        billPeriod= (TextView)v.findViewById(R.id.bpbillprd);
        FC= (TextView)v.findViewById(R.id.bpfc);
        EC= (TextView)v.findViewById(R.id.bpec);
        minChrg= (TextView)v.findViewById(R.id.bpminchrg);
        elctDuty= (TextView)v.findViewById(R.id.bpelctduty);
        regSurChrg= (TextView)v.findViewById(R.id.bpregchrg);
        exDmdPnty= (TextView)v.findViewById(R.id.bpedp);
        arrAmt= (TextView)v.findViewById(R.id.bparamt);
        arrSurChrg=(TextView)v.findViewById(R.id.bparrSurChrg);
        crrSurChrg= (TextView)v.findViewById(R.id.bpcrrsurchrgs);
        adjAmt= (TextView)v.findViewById(R.id.bpadjamt);
        miscChrgs= (TextView)v.findViewById(R.id.bpmiscchrg);
        netAmt= (TextView)v.findViewById(R.id.bpnetammt);
        rebt= (TextView)v.findViewById(R.id.bprebate);
        grsAmt= (TextView)v.findViewById(R.id.bpgramt);
        // pdf=(Button)v.findViewById(R.id.);
        print=(Button)v.findViewById(R.id.bpprint);
        pdf=(Button)v.findViewById(R.id.bppdf);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        formattedDate = df.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date()); // Now use today date.
        c1.add(Calendar.DATE,7); // Adding 7 days
        String output = sdf.format(c1.getTime());
        image =(ImageView)v.findViewById(R.id.imgview);
        byte[] decodedString = Base64.decode(Imagepart, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);
        address.setText(oficeAddress);
        accno.setText(bdate);
        bookno.setText(bookNumber);
        scno.setText(SCNumber);
        meterno.setText(meterNuber);
        consname.setText(consumerName);
        conadrs.setText(consumerAddress);
        billno.setText(BILLNO);
        preDate.setText(Previousdate);
        billData.setText(formattedDate);
        dueDate.setText(output);
        discDate.setText(DiscountDate);
        tarfCode.setText(arfcode);
        supType.setText(spcode);
        bilBasis.setText(BillBasis);
        cotrLoad.setText(conload);
        mf.setText(cmd1);
        cmd.setText("1.00");
        kwhMD.setText("0.00");
        crrKWH.setText(OverALLReading);
        preKWH.setText(PreviousReadin);
        mtrKWH.setText(prevrd);
        crrKVWH.setText(currentKVWH);
        prvKVWH.setText(PreviousKVWH);
        mtrKVWH.setText(MeteredKVWH);
        chrgUnit.setText(prevrd);
        LPA.setText(LastPayment);
        LPD.setText(LastPaymentDate);
        billPeriod.setText(billperiod);
        FC.setText(ubill);
        EC.setText(ddate);
        minChrg.setText(minchrg);
        elctDuty.setText(ElecticityDuty);
        regSurChrg.setText(rgchrg);
        exDmdPnty.setText(EXDamagePanality);
        arrAmt.setText(arramt);
        arrSurChrg.setText(ArrSurChrgs);
        crrSurChrg.setText(CurSurChrgs);
        adjAmt.setText(AdjstAmt);
        miscChrgs.setText("0.00");
        netAmt.setText(bamt);
        rebt.setText(REBATE);
        grsAmt.setText(grossammount);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store();
                Mainprint fragment1 = new Mainprint();
                Bundle b1 = new Bundle();
                b1.putString("ddate", new SimpleDateFormat("dd/mm/yyyy HH:mm").format(System.currentTimeMillis() * 1000L));
                b1.putString("acno", bdate);
                b1.putString("currentreading", String.valueOf(presrd));
                b1.putString("pre", String.valueOf(prevrd));
                b1.putString("CeilingAmmt", String.valueOf(ca));
                b1.putString("TexAmmt", String.valueOf(la));
                b1.putString("LPCAmt", String.valueOf(pa));
                b1.putString("FC", String.valueOf(ubill));
                b1.putString("EC", String.valueOf(ddate));
                b1.putString("SBMBillArrearAmt", String.valueOf(arramt));
                b1.putString("reg", String.valueOf(rgchrg));
                b1.putString("total", String.valueOf(bamt));
                b1.putString("oficeAddress",oficeAddress);
                b1.putString("bookNumber",bookNumber);
                b1.putString("SCNumber",SCNumber);
                b1.putString("meterNuber", meterNuber);
                b1.putString("consumerName", consumerName);
                b1.putString("consumerAddress",consumerAddress);
                b1.putString("OverALLReading", OverALLReading);
                b1.putString("PreviousReadin", PreviousReadin);
                b1.putString("currentKVWH",currentKVWH);
                b1.putString("MeteredKVWH",MeteredKVWH);
                b1.putString("LastPayment",LastPayment);
                b1.putString("LastPaymentDate",LastPaymentDate);
                b1.putString("ArrSurChrgs",ArrSurChrgs);
                b1.putString("CurSurChrgs", CurSurChrgs);
                b1.putString("AdjstAmt",AdjstAmt);
                b1.putString("REBATE",REBATE);
                b1.putString("BILLNO", BILLNO);
                b1.putString("Previousdate", Previousdate);
                b1.putString("DiscountDate",DiscountDate);
                b1.putString("PreviousKVWH",PreviousKVWH);
                b1.putString("BillBasis",BillBasis);
                b1.putString("EXDamagePanality",EXDamagePanality);
                b1.putString("ElecticityDuty", String.valueOf(ElecticityDuty));
                b1.putString("cmd", String.valueOf(cmd1));
                b1.putString("grossamt",grossammount);
                b1.putString("conload",conload);
                b1.putString("arfcode",arfcode);
                b1.putString("arfcode",spcode);
                fragment1.setArguments(b1);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // int fCount = 0;

                String FILE = Environment.getExternalStorageDirectory().toString()
                        + "/PDF/" +BILLNO + ".pdf";


                // Add Permission into Manifest.xml
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

                // Create New Blank Document
                Document document = new Document(PageSize.A4);

                // Create Directory in External Storage
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/PDF");

                if (!myDir.exists()) {

                    myDir.mkdir();
                }
                if(myDir.exists()){
                    // Create Pdf Writer for Writting into New Created Document
                    try {
                        PdfWriter.getInstance(document, new FileOutputStream(FILE));

                        // Open Document for Writting into document
                        document.open();

                        // User Define Method
                        addMetaData(document);
                        addTitlePage(document);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                // Close Document after writting all content
                document.close();

                Toast.makeText(getActivity(), "PDF File is Created. Location : " + FILE,
                        Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
    // Set PDF document Properties
    public void addMetaData(Document document)

    {
        document.addTitle(oficeAddress);
        document.addSubject("BILL");
        document.addKeywords(bdate);
        document.addAuthor(consumerName);
        document.addCreator("SandeepSingh");
    }

    public void addTitlePage(Document document) throws DocumentException {
        int cols = 9;
        Date now = new Date();
        // Date alsoNow = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(now);
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD
                , BaseColor.DARK_GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Paragraph newline = new Paragraph();
        newline.add("\n");

        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        prHead.add("LESCO");

        PdfPTable table0 = new PdfPTable(2);
        table0.getWidthPercentage();
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell = new PdfPCell(new Phrase(oficeAddress,new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setRowspan(2);
        cell.setColspan(42);
        table.addCell(cell);

        table.addCell("ACCONT No:");
        table.addCell(bdate);
        table.addCell("BOOK NO: ");
        table.addCell(bookNumber);
        table.addCell("SC NO:");
        table.addCell(SCNumber);
        table.addCell("METER NO;");
        table.addCell(meterNuber);
        table.addCell("CONSUMER NAME");
        table.addCell(consumerName);
        table.addCell("CONSUME ADDRESS ");
        table.addCell(consumerAddress);
        table.addCell("BILL NO:");
        table.addCell(BILLNO);
        table.addCell("PREVIOUS DATE:");
        table.addCell(Previousdate);
        table.addCell("BILL DATE:");
        table.addCell(formattedDate);
        table.addCell("DUE DATE");
        table.addCell(output);
        table.addCell("DISC DATE");
        table.addCell(DiscountDate);
        table.addCell("TARRIF CODE");
        table.addCell(arfcode);
        table.addCell("SUPPLY TYPE");
        table.addCell(spcode);
        table.addCell("BILL BASIS");
        table.addCell(BillBasis);
        table.addCell("CONTRACTED LOAD");
        table.addCell(conload);
        table.addCell("MF");
        table.addCell("1.00");
        table.addCell("CMD");
        table.addCell(cmd1);
        table.addCell("KWH MD");
        table.addCell("0.00");
        table.addCell("CURRENR KWH");
        table.addCell(OverALLReading);
        table.addCell("PREVIOUS KWH");
        table.addCell(PreviousReadin);
        table.addCell("METERED KWH");
        table.addCell(prevrd);
        table.addCell("CURRENT KVWH");
        table.addCell(currentKVWH);
        table.addCell("PREVIOUS KVWH");
        table.addCell(PreviousKVWH);
        table.addCell("METERED KVWH");
        table.addCell(MeteredKVWH);
        table.addCell("CHARGE UNIT");
        table.addCell(prevrd);
        table.addCell("LAST PAYENT AMT");
        table.addCell(LastPayment);
        table.addCell("LAST PAYENT DATE");
        table.addCell(LastPaymentDate);
        table.addCell("BILL PERIOD");
        table.addCell(billperiod);
        table.addCell("FIXED CHARGE");
        table.addCell(ubill);
        table.addCell("ENERGY CHARGE");
        table.addCell(ddate);
        table.addCell("MIN CHARGE");
        table.addCell(minchrg);
        table.addCell("ELECTRICITY DUTY");
        table.addCell(ElecticityDuty);
        table.addCell("REG SURCHARGES");
        table.addCell(rgchrg);
        table.addCell("EX DEMAND PANALITY");
        table.addCell(EXDamagePanality);
        table.addCell("AREAR AMOUNT");
        table.addCell(arramt);
        table.addCell("AREAR SURCHARGES");
        table.addCell(ArrSurChrgs);
        table.addCell("CURRENT SURCHARGES");
        table.addCell(CurSurChrgs);
        table.addCell("ADJUSTMENT AMOUNT");
        table.addCell(AdjstAmt);
        table.addCell("MISC CHARGE");
        table.addCell("0.00");
        table.addCell("NET AMOUNT");
        table.addCell(bamt);
        table.addCell("REBATE");
        table.addCell(REBATE);
        table.addCell("GROSS AMOUNT");
        table.addCell(grossammount);
        document.add(prHead);
        document.add(newline);
        document.add(newline);
        document.add(table);
        document.newPage();
        // Create new Page in PDF
        document.newPage();
    }
    public void store()
    {
        c2=c2+1;
        String strI = String.valueOf(c2);
        bill_no="BILL_"+strI;
        System.out.println("billllll"+bill_no);
        String query1=  "update customer_transactions set flag='N' where  Acctid='"+bdate+"' AND flag='Y'";

        Cursor c1 = db.rawQuery(query1, null);
        System.out.println("data"+c1.moveToFirst());
        if(c1.moveToFirst())
        {

            Toast.makeText(getActivity(), "Updated flag",
                    Toast.LENGTH_LONG).show();
        }
        String query= "insert into customer_transactions values ('"+bdate+"','"+bill_no+"','"+formattedDate+"','"+"1.00"+"','"+cmd1+"','"+OverALLReading+"', '"+PreviousReadin+"','"+currentKVWH+"','"+PreviousKVWH+"','"+1+"','"+ubill+"','"+ddate+"','"+minchrg+"','"+ElecticityDuty+"','"+rgchrg+"','"+EXDamagePanality+"', '"+arramt+"','"+ArrSurChrgs+"','"+CurSurChrgs+"','"+AdjstAmt+"','"+bamt+"','"+REBATE+"','"+bamt+"','"+c2+"','"+Previousdate+"','Y','"+email+"','"+Imagepart+"','"+'Y'+"')";
        System.out.println(query);
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())
        {

            Toast.makeText(getActivity(), "Updated database",
                    Toast.LENGTH_LONG).show();
        }

    }

}
