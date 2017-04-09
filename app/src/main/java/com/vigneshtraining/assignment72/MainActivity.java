package com.vigneshtraining.assignment72;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.vigneshtraining.assignment72.database.DBhandler;
import com.vigneshtraining.assignment72.model.Product;
import com.vigneshtraining.assignment72.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private AutoCompleteTextView autoTxt;
    private Toast toast;
    private ArrayAdapter productList;
    private DBhandler handleDB;
    private String[] initial_products={"HP Injet Printer","Azus Laptop","iPad","Android","Ball"};
    private ArrayAdapter myAdapter;
    private List<String> productNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn= (Button) findViewById(R.id.button);
        autoTxt= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        btn.setOnClickListener(this);

        handleDB=DBhandler.getInstance(this);


        int count= handleDB.getFullCount(Constants.PRODUCT_TABLE_NAME,null);
        if(count==0){
            setProduct();
        }

        getProduct();

    }

    private void getProduct(){

        List<Product> products=handleDB.getAllProduct();
       productNames = new ArrayList<String>();

        for (int i = 0; i < products.size(); i++) {
            productNames.add(i, products.get(i).getName());
        }

        myAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,productNames);
        myAdapter.notifyDataSetChanged();
        autoTxt.setAdapter(myAdapter);
        autoTxt.setThreshold(1);

    }

    private void setProduct(){

        for (int i=0;i<initial_products.length;i++){
            ContentValues vals=new ContentValues();
            vals.put(Constants.PRODUCT_NAME,initial_products[i]);
            handleDB.insertContentVals(Constants.PRODUCT_TABLE_NAME,vals);
        }

    }

    private String trim( String myString ) {

        int len = myString.length();
        int st = 0;

        char[] val = myString.toCharArray();

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return myString.substring(st, len);
    }

    @Override
    public void onClick(View v) {
        if (toast!=null){
            toast.cancel();
        }
        if (autoTxt.getText().toString().isEmpty()){

            toast=Toast.makeText(this,"Please enter the product name.",Toast.LENGTH_LONG);
            toast.show();

        }else if(handleDB.isProductExist(trim(autoTxt.getText().toString()))){
            toast=Toast.makeText(this,"Please try some new product name. Its already exist!",Toast.LENGTH_LONG);
            toast.show();

        }else{
            long row=handleDB.addProduct(trim(autoTxt.getText().toString()));
            productNames.add(productNames.size(),trim(autoTxt.getText().toString()));
            myAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,productNames);
            myAdapter.notifyDataSetChanged();
            autoTxt.setAdapter(myAdapter);
            toast=Toast.makeText(this,"Product added at "+row,Toast.LENGTH_LONG);
            toast.show();

        }

    }
}
