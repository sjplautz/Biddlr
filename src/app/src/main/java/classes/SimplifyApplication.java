package classes;

import android.app.Application;

import com.simplify.android.sdk.Simplify;

public class SimplifyApplication extends Application {

    Simplify simplify;

    @Override
    public void onCreate(){
        super.onCreate();
        simplify = new Simplify();

        try{
            //setting api key to public API key
            //may want to set it to the private for dev purposes
            simplify.setApiKey("sbpb_NDA5ZWE0NzEtNTliZC00ZjgzLWI3Y2YtNjZhM2NiYjQyNWE5");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
