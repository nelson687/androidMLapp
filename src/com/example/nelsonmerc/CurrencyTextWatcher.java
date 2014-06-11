package com.example.nelsonmerc;

import android.text.Editable;
import android.text.TextWatcher;

public class CurrencyTextWatcher implements TextWatcher{

    @Override
    public void afterTextChanged(Editable arg0) {
        String value = "$" + arg0.toString();
        arg0.replace(0, arg0.length() - 1, value);
        String nelson = "";
            
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
            int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

}
