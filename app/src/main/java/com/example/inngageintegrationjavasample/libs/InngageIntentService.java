package com.example.inngageintegrationjavasample.libs;

import static com.example.inngageintegrationjavasample.libs.IPreferenceConstants.PREF_DEVICE_UUID;
import static com.example.inngageintegrationjavasample.libs.IPreferenceConstants.PREF_INNGAGE_ENV;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.example.inngageintegrationjavasample.libs.AppPreferences;
import com.example.inngageintegrationjavasample.libs.InngageUtils;

/**
 * Maintained by Mohamed Ali Nakouri on 11/05/21.
 */
public class InngageIntentService extends IntentService {

    private static final String TAG = InngageConstants.TAG;

    com.example.inngageintegrationjavasample.libs.InngageUtils utils;
    TelephonyManager telephonyManager;
    LocationManager mLocationManager;
    JSONObject jsonBody, jsonObj, jsonCustomField;
    com.example.inngageintegrationjavasample.libs.AppPreferences appPreferences;

    public InngageIntentService() {
        super("InngageIntentService");
    }

    /**
     * Start subscriber registration service.
     *
     * @param context  Application context
     * @param appToken Application ID on the Inngage Platform
     * @param env      Inngage platform environment
     * @param provider Google cloud messaging platform
     */
    public static void startInit(Context context, String appToken, String env, String provider) {

        Intent intent = new Intent(context, (Class) InngageIntentService.class);
        intent.setAction(InngageConstants.ACTION_REGISTRATION);

        if (!validateAppToken(appToken)) {

            Log.d(TAG, InngageConstants.INVALID_APP_TOKEN);
            return;

        } else if (!validateEnvironment(env)) {

            Log.d(TAG, InngageConstants.INVALID_ENVIRONMENT);
            return;

        } else if (!validateProvider(provider)) {

            Log.d(TAG, InngageConstants.INVALID_PROVIDER);
            return;

        } else {

            intent.putExtra(InngageConstants.EXTRA_TOKEN, appToken);
            intent.putExtra(InngageConstants.EXTRA_ENV, env);
            intent.putExtra(InngageConstants.EXTRA_PROV, provider);

        }
        context.startService(intent);
    }

    /**
     * Start subscriber registration service.
     *
     * @param context    Application context
     * @param appToken   Application ID on the Inngage Platform
     * @param identifier Unique user identifier in your application
     * @param env        Inngage platform environment
     * @param provider   Google cloud messaging platform
     */
    public static void startInit(Context context, String appToken, String identifier, String env, String provider) {

        Intent intent = new Intent(context, (Class) InngageIntentService.class);
        intent.setAction(InngageConstants.ACTION_REGISTRATION);

        if (!validateAppToken(appToken)) {

            Log.d(TAG, InngageConstants.INVALID_APP_TOKEN);
            return;

        } else if (!validateIdentifier(identifier)) {

            Log.d(TAG, InngageConstants.INVALID_IDENTIFIER);
            return;

        } else if (!validateEnvironment(env)) {

            Log.d(TAG, InngageConstants.INVALID_ENVIRONMENT);
            return;

        } else if (!validateProvider(provider)) {

            Log.d(TAG, InngageConstants.INVALID_PROVIDER);
            return;

        } else {

            intent.putExtra(InngageConstants.EXTRA_TOKEN, appToken);
            intent.putExtra(InngageConstants.EXTRA_IDENTIFIER, identifier);
            intent.putExtra(InngageConstants.EXTRA_ENV, env);
            intent.putExtra(InngageConstants.EXTRA_PROV, provider);
        }
        context.startService(intent);
    }

    public static void startHandleNotifications(Context context, Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().size() > 0) {
            //SharedPreferences prefs = getSharedPreferences("com.example.inngageintegrationjavasample", Context.MODE_PRIVATE);

            String notifyID = "", title = "", body = "", url = "";

            if (intent.hasExtra("EXTRA_NOTIFICATION_ID")) {
                notifyID = intent.getStringExtra("EXTRA_NOTIFICATION_ID");
            } else if (intent.hasExtra("notId")) {
                notifyID = intent.getStringExtra("notId");
            }

            if (intent.hasExtra("EXTRA_TITLE")) {
                title = intent.getStringExtra("EXTRA_TITLE");
            } else if (intent.hasExtra("title")) {
                title = intent.getStringExtra("title");
            }

            if (intent.hasExtra("EXTRA_BODY")) {

                body = intent.getStringExtra("EXTRA_BODY");
            } else if (intent.hasExtra("body")) {
                body = intent.getStringExtra("body");
            }

            if (intent.hasExtra("EXTRA_URL")) {

                url = intent.getStringExtra("EXTRA_URL");
            } else if (intent.hasExtra("url")) {
                url = intent.getStringExtra("url");
            }

            AppPreferences appPreferences = new AppPreferences(context);
            appPreferences.putString("EXTRA_NOTIFICATION_ID", notifyID);
            appPreferences.putString("EXTRA_TITLE", title);
            appPreferences.putString("EXTRA_BODY", body);
            appPreferences.putString("EXTRA_URL", url);
        }
    }

    /**
     * Start subscriber registration service.
     *
     * @param context      Application context
     * @param appToken     Application ID on the Inngage Platform
     * @param identifier   Unique user identifier in your application
     * @param env          Inngage platform environment
     * @param provider     Google cloud messaging platform
     * @param customFields JSON Object with custom fields
     */
    public static void startInit(Context context, String appToken, String identifier, String env, String provider, JSONObject customFields) {

        Intent intent = new Intent(context, (Class) InngageIntentService.class);
        intent.setAction(InngageConstants.ACTION_REGISTRATION);

        if (!validateAppToken(appToken)) {

            Log.d(TAG, InngageConstants.INVALID_APP_TOKEN);
            return;

        } else if (!validateIdentifier(identifier)) {

            Log.d(TAG, InngageConstants.INVALID_IDENTIFIER);
            return;

        } else if (!validateEnvironment(env)) {

            Log.d(TAG, InngageConstants.INVALID_ENVIRONMENT);
            return;

        } else if (!validateProvider(provider)) {

            Log.d(TAG, InngageConstants.INVALID_PROVIDER);
            return;

        } else if (!validateCustomField(customFields)) {

            Log.d(TAG, InngageConstants.INVALID_CUSTOM_FIELD);
            return;

        } else {

            intent.putExtra(InngageConstants.EXTRA_TOKEN, appToken);
            intent.putExtra(InngageConstants.EXTRA_IDENTIFIER, identifier);
            intent.putExtra(InngageConstants.EXTRA_ENV, env);
            intent.putExtra(InngageConstants.EXTRA_PROV, provider);
            intent.putExtra(InngageConstants.EXTRA_CUSTOM_FIELD, customFields.toString());
        }
        context.startService(intent);
    }

    /**
     * Start subscriber registration service.
     *
     * @param context      Application context
     * @param appToken     Application ID on the Inngage Platform
     * @param env          Inngage platform environment
     * @param provider     Google cloud messaging platform
     * @param customFields JSON Object with custom fields
     */
    public static void startInit(Context context, String appToken, String env, String provider, JSONObject customFields) {

        Intent intent = new Intent(context, InngageIntentService.class);
        intent.setAction(InngageConstants.ACTION_REGISTRATION);

        if (!validateAppToken(appToken)) {

            Log.d(TAG, InngageConstants.INVALID_APP_TOKEN);
            return;

        } else if (!validateEnvironment(env)) {

            Log.d(TAG, InngageConstants.INVALID_ENVIRONMENT);
            return;

        } else if (!validateProvider(provider)) {

            Log.d(TAG, InngageConstants.INVALID_PROVIDER);
            return;

        } else if (!validateCustomField(customFields)) {

            Log.d(TAG, InngageConstants.INVALID_CUSTOM_FIELD);
            return;

        } else {

            intent.putExtra(InngageConstants.EXTRA_TOKEN, appToken);
            intent.putExtra(InngageConstants.EXTRA_ENV, env);
            intent.putExtra(InngageConstants.EXTRA_PROV, provider);
            intent.putExtra(InngageConstants.EXTRA_CUSTOM_FIELD, customFields.toString());
        }

        context.startService(intent);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (BuildConfig.DEBUG) {

            Log.d(TAG, "Starting InngageIntentService");
        }

        if (intent != null) {

            String action = intent.getAction();

            if (InngageConstants.ACTION_REGISTRATION.equals(action)) {

                try {

                    Bundle bundle = intent.getExtras();

                    String[] intentBundle = new String[5];

                    if (bundle.getString(InngageConstants.EXTRA_TOKEN) != null) {

                        intentBundle[0] = bundle.getString(InngageConstants.EXTRA_TOKEN);
                    }
                    if (bundle.getString("IDENTIFIER") != null) {

                        intentBundle[1] = bundle.getString("IDENTIFIER");
                    }
                    if (bundle.getString(InngageConstants.EXTRA_ENV) != null) {

                        intentBundle[2] = bundle.getString(InngageConstants.EXTRA_ENV);

                        appPreferences = new com.example.inngageintegrationjavasample.libs.AppPreferences(this);
                        appPreferences.putString(PREF_INNGAGE_ENV, intentBundle[2]);

                    }
                    if (bundle.getString(InngageConstants.EXTRA_PROV) != null) {

                        intentBundle[3] = bundle.getString(InngageConstants.EXTRA_PROV);
                    }
                    if (bundle.getString("CUSTOM_FIELDS") != null) {

                        intentBundle[4] = bundle.getString("CUSTOM_FIELDS");
                    }
                    this.handleActionSubscriber(intentBundle);

                } catch (ArrayIndexOutOfBoundsException e) {

                    Log.e(TAG, "Error receiving data \n" + e);
                }
            }
        }
    }

    private void handleActionSubscriber(String[] intentBundle) {

        if (BuildConfig.DEBUG) {

            Log.d(TAG, "Calling handleActionSubscriber");

        }

        try {

            String token = "";

            if (intentBundle[3] != null) {

                String provider = intentBundle[3];

                if (InngageConstants.GCM_PLATFORM.equals(provider)) {

                    InstanceID instanceID = InstanceID.getInstance(this);

//                    token = instanceID.getToken(
//                            getString(R.string.gcm_defaultSenderId),
//                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "GCM Token: " + token);

                    }

                } else if (InngageConstants.FCM_PLATFORM.equals(provider)) {

                    // token = FirebaseInstanceId.getInstance().getToken();
                    Task<String> registrationToken = FirebaseMessaging.getInstance().getToken();
                    token = registrationToken.getResult();
//                    FirebaseMessaging.getInstance().getToken()
//                            .addOnCompleteListener(new OnCompleteListener<String>() {
//                                                       @Override
//                                                       public void onComplete(@NonNull Task<String> task) {
//                                                           if (!task.isSuccessful()) {
//                                                               Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                                                               return;
//                                                           }
//
//                                                           // Get new FCM registration token
//                                                            token = task.getResult();
//                                                           // Log and toast
//
//                                                           Log.d(TAG, token);
//
//                                                       }
//                            });

                    Log.d(TAG, "Firebase Token :) : " + token);
                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "Firebase Token DEBUG : " + token);

                    }
                } else {

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "No provider found");

                    }
                    return;
                }
            }
            sendRegistrationToServer(token, intentBundle);

        } catch (NoClassDefFoundError e) {

            Log.e(TAG, "Failed to found GCM class, are you using FCM? \n", e);

        } catch (Exception e) {

            Log.e(TAG, "Failed to complete registration: \n", e);
        }
    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token        The new token.
     * @param intentBundle Array of string with the parameters
     */
    private void sendRegistrationToServer(String token, String[] intentBundle) {

        utils = new com.example.inngageintegrationjavasample.libs.InngageUtils();
        jsonBody = createSubscriberRequest(token, intentBundle);

        String endpoint = InngageConstants.INNGAGE_DEV_ENV.equals(intentBundle[2]) ? InngageConstants.API_DEV_ENDPOINT : InngageConstants.API_PROD_ENDPOINT;

        utils.doPost(jsonBody, endpoint + InngageConstants.PATH_SUBSCRIPTION);
        /*Location location = getLastKnownLocation();

        if (location != null) {

            jsonBody = utils.createLocationRequest(getDeviceId(), location.getLatitude(), location.getLongitude());
            utils.doPost(jsonBody, endpoint + PATH_GEOLOCATION);
        }*/
    }

    public JSONObject createSubscriberRequest(String regId, String[] intentBundle) {

        jsonBody = new JSONObject();
        jsonObj = new JSONObject();

        AppInfo app = getAppInfo();

        try {

            String identifier = "";
            //telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            if (intentBundle[1] != null) {

                identifier = intentBundle[1];

            } else {

                identifier = getDeviceId();
            }

            String _MODEL = Build.MODEL;
            String _MANUFACTURER = Build.MANUFACTURER;
            String _LOCALE = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
            String _LANGUAGE = getApplicationContext().getResources().getConfiguration().locale.getDisplayLanguage();
            String _RELEASE = Build.VERSION.RELEASE;

            jsonBody.put("identifier", identifier);
            jsonBody.put("registration", regId);
            jsonBody.put("platform", InngageConstants.PLATFORM);
            jsonBody.put("sdk", InngageConstants.SDK);
            jsonBody.put("app_token", intentBundle[0]);
            jsonBody.put("device_model", _MODEL);
            jsonBody.put("device_manufacturer", _MANUFACTURER);
            jsonBody.put("os_locale", _LOCALE);
            jsonBody.put("os_language", _LANGUAGE);
            jsonBody.put("os_version", _RELEASE);
            jsonBody.put("app_version", app.getVersionName());
            jsonBody.put("app_installed_in", app.getInstallationDate());
            jsonBody.put("app_updated_in", app.getUpdateDate());
            jsonBody.put("uuid", getDeviceId());

            // Check if api level is more than 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                if (NotificationsUtils.isNotificationEnabled(getApplicationContext())) {

                    jsonBody.put("opt_in", "1");

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "Push notifications is enabled");
                    }

                } else {

                    jsonBody.put("opt_in", "0");

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "Push notifications is disabled");
                    }
                }

            } else {

                if (NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()) {

                    jsonBody.put("opt_in", "1");

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "Push notifications is enabled");
                    }

                } else {

                    jsonBody.put("opt_in", "0");

                    if (BuildConfig.DEBUG) {

                        Log.d(TAG, "Push notifications is disabled");
                    }
                }
            }

            if (intentBundle[4] != null) {
                jsonCustomField = new JSONObject(intentBundle[4]);
                jsonBody.put("custom_field", jsonCustomField);
            }
            jsonObj.put("registerSubscriberRequest", jsonBody);

            if (BuildConfig.DEBUG) {

                Log.d(TAG, "JSON Request: " + jsonObj.toString());

            }

        } catch (JSONException e) {

            Log.e(TAG, "Error in createSubscriptionRequest \n" + e);
        }
        return jsonObj;
    }

    public AppInfo getAppInfo() {

        String packageName = getApplicationContext().getPackageName();
        String updateDate = "";
        String installationDate = "";
        String versionName = "";

        try {

            final PackageManager pm = InngageIntentService.this.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            installationDate = dateFormat.format(new Date(packageInfo.firstInstallTime));
            updateDate = dateFormat.format(new Date(packageInfo.lastUpdateTime));
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            Log.d(TAG, "Failed to get app info: ", e);
        }
        return new AppInfo(installationDate, updateDate, versionName);
    }

    private Location getLastKnownLocation() {

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {

            try {

                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            } catch (SecurityException e) {
                Log.d(TAG, "No permissions to get the user Location");
            }
        }
        return bestLocation;
    }

    private String getDeviceId() {

        if (BuildConfig.DEBUG) {

            Log.d(TAG, "Trying to get the device ID");

        }

        String deviceId = "";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (BuildConfig.DEBUG) {

                Log.d(TAG, "Permission to ACCESS_COARSE_LOCATION was granted, getMacAddress will be used Android API");
            }
            deviceId = getMacAddress();

            if ("02:00:00:00:00:00".equals(deviceId)) {

                if (BuildConfig.DEBUG) {

                    Log.d(TAG, "Device UUID returned is 02:00:00:00:00:00 alternative will be used");

                }
                deviceId = InngageUtils.getMacAddress();
            }

        } else if (!"".equals(InngageUtils.getMacAddress())) {

            if (BuildConfig.DEBUG) {

                Log.d(TAG, "No permission to ACCESS_COARSE_LOCATION was granted, getMacAddress will be used alternative mode");

            }
            deviceId = InngageUtils.getMacAddress();
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            if (BuildConfig.DEBUG) {

                Log.d(TAG, "Permission to READ_PHONE_STATE was granted, device IMEI will be used");

            }
            deviceId = getDeviceImei();

        } else {

            if (BuildConfig.DEBUG) {

                Log.d(TAG, "No permissions granted, ANDROID_ID will be used");
            }
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        appPreferences = new AppPreferences(this);
        appPreferences.putString(PREF_DEVICE_UUID, deviceId);

        if (BuildConfig.DEBUG) {

            Log.d(TAG, "Device UUID: " + deviceId);
        }
        return deviceId;
    }

    private String getMacAddress() {

        //WifiManager manager = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        WifiInfo info = wifi.getConnectionInfo();

        if (BuildConfig.DEBUG) {

            Log.d(TAG, "Getting the device MacAddress by Android API: " + info.getMacAddress());

        }

        return info.getMacAddress();
    }

    private String getDeviceImei() {
        String deviceid = "";
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return ;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Getting the device IMEI: " + deviceid);
            deviceid = telephonyManager.getImei();
            return deviceid;
        } else {
            deviceid = telephonyManager.getDeviceId();
            Log.d(TAG, "Getting the device IMEI: " + deviceid);
            return deviceid;

        }


    }

    /**
     * Validate if the environment was correctly informed by the client
     *
     * @param env Inngage platform environment.
     */
    private static boolean validateEnvironment(String env) {

        String[] environments = {InngageConstants.INNGAGE_DEV_ENV, InngageConstants.INNGAGE_PROD_ENV};

        if ("".equals(env) || !Arrays.asList(environments).contains(env)) {

            return false;
        }
        return true;
    }

    /**
     * Validate if the provider was correctly informed by the client
     *
     * @param provider Google cloud messaging provider
     */
    private static boolean validateProvider(String provider) {

        String[] providers = {InngageConstants.FCM_PLATFORM, InngageConstants.GCM_PLATFORM};

        if ("".equals(provider) || !Arrays.asList(providers).contains(provider)) {

            return false;
        }
        return true;
    }

    /**
     * Validate if the app token was correctly informed by the client
     *
     * @param appToken Inngage application token
     */
    private static boolean validateAppToken(String appToken) {

        if ("".equals(appToken) || appToken.length() < 8) {

            return false;
        }
        return true;
    }

    /**
     * Validate if the identifier was correctly informed by the client
     *
     * @param identifier User identifier
     */
    private static boolean validateIdentifier(String identifier) {

        if ("".equals(identifier)) {

            return false;
        }
        return true;
    }

    /**
     * Validate if the custom fields was correctly informed by the client
     *
     * @param customFields User custom fields
     */
    private static boolean validateCustomField(JSONObject customFields) {

        if (customFields.length() == 0) {

            return false;
        }
        return true;
    }


}

final class AppInfo {

    private final String installationDate;
    private final String updateDate;
    private final String versionName;

    public AppInfo(String installationDate, String updateDate, String versionName) {

        this.installationDate = installationDate;
        this.updateDate = updateDate;
        this.versionName = versionName;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getVersionName() {
        return versionName;
    }
}

final class InngageConstants {

    public static final String PLATFORM = "android";
    public static final String SDK = "1";
    // Endpoints
    public static final String API_ENDPOINT = "https://apid.inngage.com.br/v1";
    public static final String API_DEV_ENDPOINT = "https://apid.inngage.com.br/v1";
    public static final String API_PROD_ENDPOINT = "https://api.inngage.com.br/v1";
    // Paths
    public static final String PATH_SUBSCRIPTION = "/subscription/";
    public static final String PATH_GEOLOCATION = "/geolocation/";
    public static final String PATH_NOTIFICATION_CALLBACK = "/notification/";
    // Log tag
    public static final String TAG = "inngage-lib";
    public static final String SUBSCRIPTION = "SUBSCRIPTION";
    public static final String GEOLOCATION = "GEOLOCATION";
    public static final String NOTIFICATION_CALLBACK = "NOTIFICATION_CALLBACK";

    public static final String INNGAGE_DEV_ENV = "dev";
    public static final String INNGAGE_PROD_ENV = "prod";
    public static final String GCM_PLATFORM = "GCM";
    public static final String FCM_PLATFORM = "FCM";
    public static final String ACTION_REGISTRATION = "br.com.inngage.action.REGISTRATION";

    // Extras
    public static final String EXTRA_PROV = "PROVIDER";
    public static final String EXTRA_ENV = "ENVIRONMENT";
    public static final String EXTRA_TOKEN = "APP_TOKEN";
    public static final String EXTRA_IDENTIFIER = "IDENTIFIER";
    public static final String EXTRA_CUSTOM_FIELD = "CUSTOM_FIELDS";
    // Messages
    public static final String INVALID_APP_TOKEN = "Verify if the value of APP_TOKEN was informed";
    public static final String INVALID_ENVIRONMENT = "Verify if the value of ENVIRONMENT was informed";
    public static final String INVALID_PROVIDER = "Verify if the value of PROVIDER was informed";
    public static final String INVALID_IDENTIFIER = "Verify if the value of IDENTIFIER was informed";
    public static final String INVALID_CUSTOM_FIELD = "Verify if the value of CUSTOM_FIELD was informed";
    public static final String INVALID_UPDATE_INTERVAL = "Error starting location service: verify if the value of updateInterval is a integer";
    public static final String INVALID_PRIORITY_ACCURACY = "Error starting location service: verify if the value of updateInterval is valid (100, 102, 104 or 105)";
    public static final String INVALID_DISPLACEMENT = "Error starting location service: verify if the value of displacement is valid";
    public static final String INVALID_APP_TOKEN_LENGHT = "Verify if the value of APP_TOKEN is correct";

    public static final String UNABLE_FIND_LOCATION = "Não foi possível obter a sua localização";

}

class NotificationsUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";


    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        }

        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            // Check if api level is more than 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                appOpsClass = Class.forName(AppOpsManager.class.getName());

                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);

                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


}