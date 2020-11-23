package com.example.layouttopdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layouttopdf.databinding.ActivityMainBinding;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private float a4PageSizeLimit=60;
    private ActivityMainBinding mDataBinding;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);

        pdfGenerator();



    }



    private void pdfGenerator(){


        float totalRows=129;
        float numberOfPages= (float) Math.ceil(totalRows/a4PageSizeLimit);
        List<TableLayout> viewList=new ArrayList<>();
        int realNum1 = 0;
        LinearLayout linearLayout=mDataBinding.linearLay;


        for(int i=0;i<numberOfPages;i++){

            TableLayout table= getTable();
            TableRow tableRow=new TableRow(MainActivity.this);
            tableRow.setBackgroundColor(Color.parseColor("#669900"));

            tableRow.addView(getHeaderTextTable(getContext(),"Sr.No"));
            tableRow.addView(getHeaderTextTable(getContext(),"Student Name"));
            tableRow.addView(getHeaderTextTable(getContext(),"Attendance In"));
            tableRow.addView(getHeaderTextTable(getContext(),"Attendance Out"));
            tableRow.addView(getHeaderTextTable(getContext(),"Date"));

            table.addView(tableRow);
            viewList.add(table);


            for(int j=0;j<a4PageSizeLimit;j++){

                if(realNum1>=totalRows){
                    break;
                }
                TableRow tbrow = new TableRow(this);

                realNum1++;

                if(realNum1%2==1){
                    tbrow.setBackgroundColor(Color.parseColor("#ECECEC"));
                }

                tbrow.addView(getTableContentText(getContext(),String.valueOf(realNum1)));
                tbrow.addView(getTableContentText(getContext(),"Product " + realNum1));
                tbrow.addView(getTableContentText(getContext(),"Rs." + j));
                tbrow.addView(getTableContentText(getContext(),"" + j * 15 / 32 * 10));
                tbrow.addView(getTableContentText(getContext(),"" + j * 15 / 32 * 10));
                viewList.get(i).addView(tbrow);
            }
            linearLayout.addView(viewList.get(i));
        }


        PdfGenerator.getBuilder()
                .setContext(MainActivity.this)
                .fromViewSource()
                .fromViewList(new ArrayList<>(viewList))
                .setDefaultPageSize(PdfGenerator.PageSize.A4)
                .setFileName("report1")
                .setFolderName("School Now/Attendance Report")
                .openPDFafterGeneration(false)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        Toast.makeText(MainActivity.this, "Saved "+response.getPath(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private TextView getHeaderTextTable(Context context, String text){
        TextView c1=new TextView(context);
        c1.setText(text);
        c1.setTextSize(18);
        c1.setTextColor(Color.WHITE);
        c1.setGravity(Gravity.CENTER);
        c1.setTypeface(Typeface.DEFAULT_BOLD);

        return c1;
    }
    private TextView getTableContentText(Context context,String text){
        TextView t1v = new TextView(context);
        t1v.setText(text);
        t1v.setTextSize(14);
        t1v.setTextColor(Color.BLACK);
        t1v.setGravity(Gravity.CENTER);
        return t1v;

    }
    private TableLayout getTable(){
        TableLayout.LayoutParams tablePram=new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TableLayout tableLayout= new TableLayout(MainActivity.this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setLayoutParams(tablePram);

        return tableLayout;
    }


    private Context getContext(){
        return MainActivity.this;
    }

}