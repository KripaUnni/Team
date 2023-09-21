package avantech.smartapps.team.common;

import android.annotation.SuppressLint;
import android.content.Context;

public class SharedPreferences {
    private android.content.SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor;
    private final String IS_LOGGED_IN="IS_LOGGED_IN";
    private final String EMAIL = "EMAIL";
    private final String EMP_ID = "EMP_ID";
    private final String NAME = "NAME";
    private final String TYPE = "TYPE";
    private final String PHONE = "PHONE";
    private final String PASSWORD = "PASSWORD";
    private Context context;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferences(Context context, boolean isLoggedIn, String emp_id, String emp_name, String emp_phone, String emp_email, String emp_type, String emp_password) {
        this.context = context;
        String PREF_NAME = "login";
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN,isLoggedIn);
        editor.putString(EMP_ID,emp_id);
        editor.putString(NAME,emp_name);
        editor.putString(PHONE,emp_phone);
        editor.putString(EMAIL,emp_email);
        editor.putString(TYPE,emp_type);
        editor.putString(PASSWORD,emp_password);
        editor.commit();
        editor.apply();
    }
    public SharedPreferences(Context context) {
        this.context = context;
        String PREF_NAME = "login";
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean getIS_LOGGED_IN() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL,null);
    }

    public String getNAME() {
        return sharedPreferences.getString(NAME,null);
    }

    public String getEMP_ID() {
        return sharedPreferences.getString(EMP_ID,null);
    }
    public void logout() {
        editor.putBoolean(IS_LOGGED_IN,false).apply();
    }

    public void setName(String name) {
        editor.putString(NAME,name).apply();
    }

    public String getPHONE() {
        return sharedPreferences.getString(PHONE,null);
    }
    public void setPhone(String phone) {
        editor.putString(PHONE,phone).apply();

    }
    public void setPassword(String password) {
        editor.putString(PASSWORD,password).apply();
    }

    public void setEmail(String email) {
        editor.putString(EMAIL,email).apply();
    }

    public String getTYPE() {
        return sharedPreferences.getString(TYPE,null);
    }

    public String getPASSWORD() {
        return sharedPreferences.getString(PASSWORD,null);
    }
}
