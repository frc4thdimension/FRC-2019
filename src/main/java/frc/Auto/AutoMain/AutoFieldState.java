package frc.Auto.AutoMain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoFieldState {

    static AutoFieldState afstateInstance = new AutoFieldState();
    public enum Side {LEFT,RIGHT};

    public Side getOurSwitchSide() {
        return ourSwitchSide;
    }

    private Side ourSwitchSide;

    public Side getScaleSide() {
        return ScaleSide;
    }
    public Side getOpponentSwitchSide() {
        return opponentSwitchSide;
    }


    private Side ScaleSide;
    private Side opponentSwitchSide;
    private Side overrideourSwitchSide,overrideScaleSide,overrideopponentSwitchSide;

    public static  AutoFieldState getInstance(){
        return afstateInstance;
    }

    public AutoFieldState(){

    }

    public synchronized boolean setSides(String gameData){
        if(gameData == null){
            return false;
        }
        gameData = gameData.trim();
        if(gameData.length() != 3){
            return false;
        }

        Side s0 = getCharSide(gameData.charAt(0));
        Side s1 = getCharSide(gameData.charAt(1));
        Side s2 = getCharSide(gameData.charAt(2));

        if (s0 == null || s1 == null || s2 == null) {
            return false;
        }
        ourSwitchSide = s0;
        ScaleSide = s1;
        opponentSwitchSide = s2;
        return true;

    }
    public synchronized boolean overrideSides(String gameData){
        if(gameData == null){
            return false;
        }
        gameData = gameData.trim();
        if(gameData.length() != 3){
            return false;
        }

        Side s0 = getCharSide(gameData.charAt(0));
        Side s1 = getCharSide(gameData.charAt(1));
        Side s2 = getCharSide(gameData.charAt(2));

        if (s0 == null || s1 == null || s2 == null) {
            return false;
        }
        overrideourSwitchSide = s0;
        overrideScaleSide = s1;
        overrideopponentSwitchSide = s2;
        return true;

    }

    public void outPutToSmartDashboard(){
        SmartDashboard.putString("ourSwitch",getOurSwitchSide().toString());
        SmartDashboard.putString("ourScala",getScaleSide().toString());
    }
    private Side getCharSide(char c){
        return c == 'L' ? Side.LEFT : c == 'R' ? Side.RIGHT : null;
    }



}
