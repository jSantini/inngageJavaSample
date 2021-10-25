package com.example.inngageintegrationjavasample;

import android.app.Application;

import com.example.inngageintegrationjavasample.libs.InngageIntentService;
import com.example.inngageintegrationjavasample.libs.InngageUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        startInngage();
    }

    private void startInngage() {

//        String myInngageAppToken = InngageConstants.inngageAppToken;
//        String myInngageEnvironment = InngageConstants.inngageEnvironment;
//        String myGoogleMessageProvider = InngageConstants.googleMessageProvider;
//
//        // caso o app possua campos customizáveis, descomente estas linhas, cheque-os na plataforma, substitua-os abaixo e adicione os valores de acordo com a sua aplicação
////        JSONObject jsonCustomField = new JSONObject();
////
////        try {
////
////            jsonCustomField.put("nome", user.name);
////            jsonCustomField.put("email", user.email);
////            jsonCustomField.put("telefone", "");
////            jsonCustomField.put("dataRegistro", "");
////            jsonCustomField.put("dataNascimento", "");
////
////
////      } catch (JSONException e) {
////
////            e.printStackTrace();
////      }
////        // caso possua campos customizáveis, adicione o "jsonCustomField" como na chamada abaixo
////        InngageIntentService.startInit(
////                this,
////                myInngageAppToken,
////                "Identifier", //Seu identificador
////                myInngageEnvironment,
////                myGoogleMessageProvider,
////    jsonCustomField);
//
//        // caso não possua campos customizáveis até então:
//        InngageIntentService.startInit(
//                this,
//                myInngageAppToken,
//                "userIdentifier", //Seu identificador
//                myInngageEnvironment,
//                myGoogleMessageProvider);
//
//
//        InngageUtils.handleNotification(this, getIntent(), myInngageAppToken, myInngageEnvironment);
    }
}
