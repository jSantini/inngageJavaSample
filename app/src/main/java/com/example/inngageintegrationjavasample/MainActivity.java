package com.example.inngageintegrationjavasample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import br.com.inngage.sdk.InngageIntentService;
import br.com.inngage.sdk.InngageUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "INNGAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleSubscription();
        handleNotification();
    }

    private void handleSubscription() {

        // caso o app possua campos customizáveis, descomente estas linhas, cheque-os na plataforma, substitua-os abaixo e adicione os valores de acordo com a sua aplicação
//        JSONObject jsonCustomField = new JSONObject();
//
//        try {
//
//            jsonCustomField.put("nome", user.name);
//            jsonCustomField.put("email", user.email);
//            jsonCustomField.put("telefone", "");
//            jsonCustomField.put("dataRegistro", "");
//            jsonCustomField.put("dataNascimento", "");
//
//
//      } catch (JSONException e) {
//
//            e.printStackTrace();
//      }
//        // caso possua campos customizáveis, adicione o "jsonCustomField" como na chamada abaixo
//        InngageIntentService.startInit(
//                this,
//                InngageConstants.inngageAppToken,
//                "Identifier", //Seu identificador
//                InngageConstants.inngageEnvironment,
//                InngageConstants.googleMessageProvider,
//    jsonCustomField);

        // caso não possua campos customizáveis até então:
        InngageIntentService.startInit(
                this,
                InngageConstants.inngageAppToken,
                "jSantiniIdentifier", //Seu identificador
                InngageConstants.inngageEnvironment,
                InngageConstants.googleMessageProvider);
    }

    private void handleNotification() {

        String notifyID = "", title = "", body = "", url = "";

        Bundle bundle = getIntent().getExtras();

        if (getIntent().hasExtra("EXTRA_NOTIFICATION_ID")) {

            notifyID = bundle.getString("EXTRA_NOTIFICATION_ID");
        }
        if (getIntent().hasExtra("EXTRA_TITLE")) {

            title = bundle.getString("EXTRA_TITLE");
        }
        if (getIntent().hasExtra("EXTRA_BODY")) {

            body = bundle.getString("EXTRA_BODY");
        }

        if (getIntent().hasExtra("EXTRA_URL")) {

            url = bundle.getString("EXTRA_URL");
        }

        if (url.isEmpty()) {
            if (!"".equals(notifyID) || !"".equals(title) || !"".equals(body)) {
                Log.d(TAG, "no link: " + url);
                InngageUtils.showDialog(
                        title,
                        body,
                        notifyID,
                        InngageConstants.inngageAppToken,
                        InngageConstants.inngageEnvironment,
                        this);
            }

        } else if (!"".equals(notifyID) || !"".equals(title) || !"".equals(body)) {
            Log.d(TAG, "Link: " + url);
            InngageUtils.showDialogwithLink(
                    title,
                    body,
                    notifyID,
                    InngageConstants.inngageAppToken,
                    InngageConstants.inngageEnvironment,
                    url,
                    this);
        }

    }
}