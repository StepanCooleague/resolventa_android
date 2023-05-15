package com.staple.resolventa.prosol;

import android.content.Context;

import com.staple.resolventa.R;

public class ProSolTyper {
    public static void check_prosol_type(Context context, Solution response) throws ErrorProSolTypeException {
        if(response.solution_type.equals(context.getString(R.string.errt)))
            throw new ErrorProSolTypeException(context.getString(R.string.server_err) + response.solution_content);
    }
}
