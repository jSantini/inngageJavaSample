package com.example.inngageintegrationjavasample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.inngageintegrationjavasample.libs.InngageIntentService;
import com.example.inngageintegrationjavasample.libs.InngageUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "INNGAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleSubscription();
    }

    private void handleSubscription() {

        String myInngageAppToken = InngageConstants.inngageAppToken;
        String myInngageEnvironment = InngageConstants.inngageEnvironment;
        String myGoogleMessageProvider = InngageConstants.googleMessageProvider;

        /*

        Caso o app possua campos customizáveis, descomente estas linhas, cheque-os na plataforma, substitua-os abaixo e adicione os valores de acordo com a sua aplicação
        JSONObject jsonCustomField = new JSONObject();

        */

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
//                myInngageAppToken,
//                "Identifier", //Seu identificador
//                myInngageEnvironment,
//                myGoogleMessageProvider,
//    jsonCustomField);

        // caso não possua campos customizáveis até então:
        InngageIntentService.startInit(
                this,
                myInngageAppToken,
                "userIdentifier", //Seu identificador
                myInngageEnvironment,
                myGoogleMessageProvider);


        InngageUtils.handleNotification(this, getIntent(), myInngageAppToken, myInngageEnvironment);
    }
}