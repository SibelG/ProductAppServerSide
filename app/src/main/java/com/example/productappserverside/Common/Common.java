package com.example.productappserverside.Common;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.productappserverside.model.Request;
import com.example.productappserverside.model.User;

import java.net.NetworkInterface;

public class Common {
    public static User CurrentUser;
    public static Request CurrentRequest;
    public static final String productId="productId";
    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static final int pickImage=71;
    public static String convertToStatus(String status){
        if(status=="0"){
            return "Placed";
        }else if(status=="1"){
            return "On my one";
        }else
            return "Shipped";

    }
    public static boolean connectivityInternet(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null){
           @SuppressLint("MissingPermission") NetworkInfo[] info=manager.getAllNetworkInfo();
            if(info!=null){
                for(int i=0;i<info.length;i++){
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;

                }
            }
        }

        return false;
    }

}
