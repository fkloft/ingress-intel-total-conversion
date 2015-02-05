package com.cradle.iitc_mobile.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.cradle.iitc_mobile.IITC_FileManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCheck extends AsyncTask<Void, Void, UpdateCheck.UpdateInfo> {
    private static final Pattern PATTERN_BUILDNAME = Pattern.compile("^\\[([a-z0-9_-])+-\\d{4}-\\d{2}-\\d{2}-\\d{6}",
            Pattern.CASE_INSENSITIVE);

    private final Context mContext;
    private final Callback mCallback;
    private final IITC_FileManager mFileManager;
    private final SharedPreferences mPrefs;

    public UpdateCheck(final Context context, final Callback callback) {
        mContext = context;
        mCallback = callback;
        mFileManager = new IITC_FileManager(mContext);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    protected UpdateCheck.UpdateInfo doInBackground(final Void... p)
    {
        try {
            final HashMap<String, String> info = mFileManager.getIITCInfo();

            final Matcher matcher = PATTERN_BUILDNAME.matcher(info.get("description"));
            if (matcher == null || !matcher.matches())
                return null;

            final String buildName = matcher.group(1);

            final String base = mPrefs.getBoolean("pref_force_https", true)
                    ? "https://secure.jonatkins.com/iitc"
                    : "http://iitc.jonatkins.com";

            final HttpGet request = new HttpGet(base + "/versioncheck.php&mobile=1?build=" + buildName);
            final HttpResponse response = new DefaultHttpClient().execute(request);

            final String content = EntityUtils.toString(response.getEntity());

            return new UpdateInfo(new JSONObject(content));
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final UpdateCheck.UpdateInfo result)
    {
        super.onPostExecute(result);
        if (mCallback != null)
            mCallback.onUpdateInfoLoaded(result);
    }

    public interface Callback {
        public void onUpdateInfoLoaded(UpdateInfo info);
    }

    public class UpdateInfo {
        public final String name, downloadUrl, versionIITC, versionName;
        public final int versionCode;

        private UpdateInfo(final JSONObject data) throws JSONException {
            name = data.getString("name");

            final JSONObject mobile = data.getJSONObject("mobile");
            downloadUrl = mobile.getString("downloadurl");
            versionIITC = mobile.getString("iitc_version");
            versionName = mobile.getString("versionstr");
            versionCode = mobile.optInt("versioncode");
        }
    }
}