package com.netlab.loveofmum.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.netlab.loveofmum.model.Order;
import com.netlab.loveofmum.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class LocalAccessor{

    private static final String LOG_TAG = "LocalAccessor";
//	private static final String DATABASE_NAME = "topmd.db";	
//	
//	private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS USER("
//	    + "UserID TEXT PRIMARY KEY,"
//	    + "NickName TEXT,"
//	    + "YuBirthDate TEXT,"
//	    + "PicURL TEXT"
//	    + ");";

	private SharedPreferences prefs = null;	
	private Context ctx;
	
	

	private LocalAccessor(Context ctx){
		this.ctx = ctx;
		prefs = ctx.getSharedPreferences(MMloveConstants.PREFS_FILE, Context.MODE_PRIVATE);
//		SQLiteDatabase db = openDB();
//		db.execSQL(SQL_CREATE_TABLE_USER);
//		db.close();
//		testDBMessage();
//		testDBFavoriteItem();
	}	
	
	public LocalAccessor(Context ctx,String name){
		this.ctx = ctx;
		prefs = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
//		SQLiteDatabase db = openDB();
//		db.execSQL(SQL_CREATE_TABLE_USER);
//		db.close();
//		testDBMessage();
//		testDBFavoriteItem();
	}	

	static private LocalAccessor accessor; 
	
	public static LocalAccessor getInstance(Context context){		
		if(accessor == null){
			accessor = new LocalAccessor(context);
		}
		return accessor;
	}
	
//    
//	private SQLiteDatabase openDB(){		
//		return ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
//	}
	
	public boolean deleteUser() throws Exception{
		SharedPreferences.Editor editor = prefs.edit();
       // editor.remove("username");
        editor.remove("UserID");
        editor.remove("NickName");
        editor.remove("PicURL");
        editor.remove("YuBirthDate");
        editor.remove("UserMobile");
        editor.remove("UserNO");
        editor.remove("RealName");
        editor.remove("HospitalID");
        editor.remove("HospitalName");
        editor.commit();
		return true;
	}
	//insert or update(if exist) user in SharedPreferences
	public boolean updateUser(User user) throws Exception{
		SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("UserID", user.UserID);
        editor.putString("NickName", user.NickName);
        editor.putString("PicURL", user.PicURL);
        editor.putString("UserMobile", user.UserMobile);
        editor.putString("UserNO", user.UserNO);
        editor.putString("RealName", user.RealName);
        editor.putString("YuBirthDate", user.YuBirthDate);
        editor.putInt("HospitalID", user.HospitalID);
        editor.putString("PayId", user.PayId);
        editor.putString("HospitalName", user.HospitalName);
        editor.putInt("IsTwins", user.isTwins);
        editor.commit();
		return true;
	}
	 
	//return null if no user saved
	public User getUser(){
		User ret = new User();
        ret.UserID = prefs.getInt("UserID", 0);
        ret.NickName = prefs.getString("NickName", null);
        ret.PicURL = prefs.getString("PicURL", null);
		ret.YuBirthDate = prefs.getString("YuBirthDate", null);
		ret.RealName = prefs.getString("RealName", null);
		
		ret.UserNO = prefs.getString("UserNO", null);
		ret.UserMobile = prefs.getString("UserMobile", null);
		
		ret.HospitalID = prefs.getInt("HospitalID", 0);
		ret.PayId= prefs.getString("PayId", "");
		ret.HospitalName = prefs.getString("HospitalName", null);
        ret.isTwins = prefs.getInt("IsTwins", 0);
		return ret;
	}	
	
	public boolean deleteOrder() throws Exception{
		SharedPreferences.Editor editor = prefs.edit();
       // editor.remove("username");
        editor.remove("CHKTypeID");
        editor.remove("CHKItemValue");
        editor.remove("HospitalID");
        editor.remove("HospitalName");
        
        editor.remove("DoctorID");
        editor.remove("DoctorName");
        editor.remove("DoctorTime");
        
        editor.remove("DoctorNO");
        editor.remove("DepartNO");
        editor.remove("SchemaID");
        
        editor.remove("DeptCode");
        editor.remove("DeptName");
        
        
        editor.remove("CardNO");
        editor.remove("GuaFee");
        editor.remove("CHKFee");   
        
        editor.remove("TimeStart");     
        editor.remove("TimeEnd");     
        editor.remove("DoctorTitle");  
        editor.remove("Price");     
        editor.remove("CHKTypeName");  
        editor.remove("ImageURL");
        editor.commit();
		return true;
	}
	//insert or update(if exist) user in SharedPreferences
	public boolean updateOrder(Order order) throws Exception{
		SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("CHKTypeID", order.CHKTypeID);
        editor.putString("CHKItemValue", order.CHKItemValue);
        editor.putString("HospitalID", order.HospitalID);
        editor.putString("HospitalName", order.HospitalName);
        
        editor.putString("DoctorID", order.DoctorID);
        editor.putString("DoctorName", order.DoctorName);
        editor.putString("DoctorTime", order.DoctorTime);
        
        
        editor.putString("DoctorNO", order.DoctorNO);
        editor.putString("DepartNO", order.DepartNO);
        editor.putString("SchemaID", order.SchemaID);
        
        editor.putString("DeptCode", order.DeptCode);
        editor.putString("DeptName", order.DeptName);
        
        editor.putString("CardNO", order.CardNO);
        editor.putString("GuaFee", order.GuaFee);
        
        editor.putString("CHKFee", order.CHKFee);
        
        editor.putString("TimeStart", order.TimeStart);
        editor.putString("TimeEnd", order.TimeEnd);
        editor.putString("DoctorTitle", order.DoctorTitle);
        editor.putString("Price", order.Price);
        editor.putString("CHKTypeName", order.CHKTypeName);
        editor.putString("ImageURL", order.ImageURL);
        editor.commit();
		return true;
	}
	 
	//return null if no user saved
	public Order getOrder(){
		Order ret = new Order();
        ret.CHKTypeID = prefs.getInt("CHKTypeID", 0);
        ret.CHKItemValue = prefs.getString("CHKItemValue", null);
        ret.HospitalID = prefs.getString("HospitalID", null);
		ret.HospitalName = prefs.getString("HospitalName", null);
		ret.DoctorID = prefs.getString("DoctorID", null);
		ret.DoctorName = prefs.getString("DoctorName", null);
		ret.DoctorTime = prefs.getString("DoctorTime", null);
		
		ret.DoctorNO = prefs.getString("DoctorNO", null);
		ret.DepartNO = prefs.getString("DepartNO", null);
		ret.SchemaID = prefs.getString("SchemaID", null);
		
		ret.DeptCode = prefs.getString("DeptCode", null);
		ret.DeptName = prefs.getString("DeptName", null);
		
		ret.CardNO = prefs.getString("CardNO", null);
		ret.GuaFee = prefs.getString("GuaFee", null);
		ret.CHKFee = prefs.getString("CHKFee", null);
		
		ret.TimeStart = prefs.getString("TimeStart", null);
		ret.TimeEnd = prefs.getString("TimeEnd", null);
		ret.DoctorTitle = prefs.getString("DoctorTitle", null);
		ret.Price = prefs.getString("Price", null);
		ret.CHKTypeName = prefs.getString("CHKTypeName", null);
		ret.ImageURL = prefs.getString("ImageURL", null);
		return ret;
	}	
}
