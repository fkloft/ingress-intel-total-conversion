package com.cradle.iitc_mobile.async;

import android.content.*;
import android.os.*;
import com.cradle.iitc_mobile.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class UpdateCheck extends AsyncTask<Void, Void, UpdateCheck.UpdateInfo> {
		private static final Pattern PATTERN_BUILDNAME = Pattern.compile("^\\[([a-z0-9_-])+-\\d{4}-\\d{2}-\\d{2}-\\d{6}", Pattern.CASE_INSENSITIVE);
		
		private final Context mContext;
		private final Callback mCallback;
		private final IITC_FileManager mFileManager;
		
		public UpdateCheck(Context context, Callback callback) {
				mContext = context;
				mCallback = callback;
				mFileManager = new IITC_FileManager(mContext);
		}

		@Override
		protected UpdateCheck.UpdateInfo doInBackground(Void... p)
		{
				HashMap<String, String> info = mFileManager.getIITCInfo();
				Matcher matcher = PATTERN_BUILDNAME.matcher(info.get("description"));
				return null;
		}

		@Override
		protected void onPostExecute(UpdateCheck.UpdateInfo result)
		{
				super.onPostExecute(result);
				if(mCallback != null)
						mCallback.onUpdateInfoLoaded(result);
		}
		
		public interface Callback {
				public void onUpdateInfoLoaded(UpdateInfo info);
		}
		public class UpdateInfo {
				public final String name, downloadUrl, versionIITC, versionName;
				public final int versionCode;
				
				private UpdateInfo(JSONObject data) throws JSONException {
						name = data.getString("name");
						
						JSONObject mobile = data.getJSONObject("mobile");
						downloadUrl = mobile.getString("downloadurl");
						versionIITC = mobile.getString("iitc_version");
						versionName = mobile.getString("versionstr");
						versionCode = mobile.optInt("versioncode");
				}
		}
}