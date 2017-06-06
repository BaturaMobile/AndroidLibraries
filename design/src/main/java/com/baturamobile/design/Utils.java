package com.baturamobile.design;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by vssnake on 07/11/2016.
 */

public class Utils {

    public static Date getUTCTime(long timeInMilliseconds) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date( timeInMilliseconds - tz.getRawOffset() );

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if ( tz.inDaylightTime( ret )){
            Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if ( tz.inDaylightTime( dstDate )){
                ret = dstDate;
            }
        }
        return ret;
    }

    static long time2001 = 978307200000L;
    public static long getTimeSince2001(){
        return System.currentTimeMillis() - time2001;
    }

    public static void throwError(Throwable throwable){
        //TODO implement this
      //  FirebaseCrash.report(throwable);
    }
    public static void logThrowError(String log){
        //TODO implement this
     //   FirebaseCrash.log(log);
    }


    public static DecimalFormat numberFormat = new DecimalFormat("#,##0.000");
    public static String formatNumber(float number){
       return numberFormat.format(number);
    }



    public static void openBrowser(String url, AppCompatActivity activityCompat){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activityCompat.startActivity(browserIntent);
    }

    /**
     *
     * @param password
     * @return
     */
    public static String fillPassword(String password){

        if (password.length() < 6){
            int passwordLength = password.length();
            for (int x = passwordLength; x < 6; x++){
                password += "0";
            }
        }

        return password;
    }
}
