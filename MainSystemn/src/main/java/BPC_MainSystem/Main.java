package BPC_MainSystem;

import BPC_MainSystem.controllers.MainSystemCtrl;

public class Main {
    public static void main(String[] args) {

        //System.out.println("Hello, World!");
        MainSystemCtrl ctrl = new MainSystemCtrl();

        ctrl.TopMessage();
        ctrl.startMainSystem();



    }
}