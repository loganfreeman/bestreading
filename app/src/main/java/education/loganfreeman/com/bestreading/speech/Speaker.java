package education.loganfreeman.com.bestreading.speech;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import education.loganfreeman.com.bestreading.MainActivity;
import education.loganfreeman.com.bestreading.utils.ToastUtil;

/**
 * Created by shanhong on 4/3/17.
 */

public class Speaker implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = true;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    public void setOnUtteranceProgressListener(UtteranceProgressListener listener) {
        tts.setOnUtteranceProgressListener(listener);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // Change this to match your
            // locale
            tts.setLanguage(Locale.US);
            ready = true;

        }else{
            ready = false;
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void speak(String text, String utteranceId) {
        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle params = new Bundle();

                tts.speak(text, TextToSpeech.QUEUE_ADD, params, utteranceId);
            }else{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                tts.speak(text, TextToSpeech.QUEUE_ADD, map);
            }
        }
    }

    public void speak(String text){

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready && allowed) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle params = new Bundle();

                tts.speak(text, TextToSpeech.QUEUE_ADD, params, "UniqueID");
            }else{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
                tts.speak(text, TextToSpeech.QUEUE_ADD, map);
            }
        }
    }

    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    // Free up resources
    public void destroy(){
        tts.shutdown();
    }

    public void stop() {
        tts.stop();
    }


}