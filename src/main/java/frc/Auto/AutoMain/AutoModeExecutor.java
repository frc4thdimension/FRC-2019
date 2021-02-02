package frc.Auto.AutoMain;

import frc.subSystems.Gamepad;


public class AutoModeExecutor {

    /*
    Throw the selected mode into thread order and run or stop depending to the match situation
     */
    private AutoMain  autoM;
    private Thread mThread = null;


    public void setAutoMode(AutoMain newAutoM){
        autoM = newAutoM;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(autoM!=null){
                
                    autoM.run();

                }


            }
        });



    }
    public void start(){
        if(mThread !=null){
            mThread.start();
        }
    }

    public void stop(){
        if(autoM!=null){
            autoM.stop();
        }
        mThread=null;
    }

    public AutoMain getAutoM(){
        return autoM;
    }
}
