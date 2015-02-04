package com.cradle.iitc_mobile.async;

import android.content.Context;
import android.os.AsyncTask;

import com.cradle.iitc_mobile.IITC_FileManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCheck extends AsyncTask<Void, Void, UpdateCheck.UpdateInfo> {
    private static final Pattern PATTERN_BUILDNAME = Pattern.compile("^\\[([a-z0-9_-])+-\\d{4}-\\d{2}-\\d{2}-\\d{6}",
            Pattern.CASE_INSENSITIVE);

    private final Context mContext;
    private final Callback mCallback;
    private final IITC_FileManager mFileManager;

    public UpdateCheck(final Context context, final Callback callback) {
        mContext = context;
        mCallback = callback;
        mFileManager = new IITC_FileManager(mContext);
    }

    @Override
    protected UpdateCheck.UpdateInfo doInBackground(final Void... p)
    {
        HashMap<String, String> info;
        try {
            info = mFileManager.getIITCInfo();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }

        final Matcher matcher = PATTERN_BUILDNAME.matcher(info.get("description"));
        return null;
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