package com.example.germanmemoriserapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.germanmemoriserapp.mechanics.Game;
import com.example.germanmemoriserapp.mechanics.Game.GAME_STATE;
import com.example.germanmemoriserapp.ui.Keyboard;
import com.example.germanmemoriserapp.R;
import com.example.germanmemoriserapp.mechanics.Timer;

import com.example.germanmemoriserapp.ui.Keyboard.BUTTON_STATE;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //10 Digits + 1 Back Button
    final int SIZE_KEYB = 11;
    final int NUM_DIGITS = 10;

    int[] digitIds = new int[]{
            R.id.digitZeroBtn, R.id.digitOneBtn, R.id.digitTwoBtn,
            R.id.digitThreeBtn, R.id.digitFourBtn, R.id.digitFiveBtn,
            R.id.digitSixBtn, R.id.digitSevenBtn, R.id.digitEightBtn,
            R.id.digitNineBtn, R.id.backBtn
    };

    int[] unpressedIds = new int[] {
            R.drawable.keyb_0, R.drawable.keyb_1, R.drawable.keyb_2,
            R.drawable.keyb_3, R.drawable.keyb_4, R.drawable.keyb_5,
            R.drawable.keyb_6, R.drawable.keyb_7, R.drawable.keyb_8,
            R.drawable.keyb_9
    };

    Intent moveToGOScreen;
    EditText enterNumberBox;
    TextView tmpNumberView;
    TextView timerView;
    Keyboard digitKeyboard;
    Timer timer;
    Game GAME;

    /*
    Matches digit ID to digit value
     */
    HashMap<Integer, Integer> digitIdReference;

    /*
    Initialise our handlers. These will update particular
    parts of our game when called.
     */
    GameStateHandler gameStateH = new GameStateHandler();
    InputFieldUIHandler inputFieldH = new InputFieldUIHandler();
    ButtonUIHandler buttonUiH = new ButtonUIHandler();

    /*
    Called when the user presses a button on
    the game's keyboard.
     */
    class GameStateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String userInput = (String) msg.obj;

            //Retrieve next state of the game
            GAME_STATE nextState = GAME.getState(userInput);

            //Execute this action in the backend
            GAME.execute(nextState);

            //Execute this action in the frontend
            if(nextState.equals(GAME_STATE.NEW_TURN)) {
                onNewTurn();
            }
            else if(nextState.equals(GAME_STATE.GAME_OVER)) {
                onGameOver();
            }
        }
    }

    /*
    Called if we want to update the text
    in our input field.
     */
    class InputFieldUIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String newStr = (String) msg.obj;
            enterNumberBox.setText(newStr);
        }
    }

    /*
    Called if we want to update the state of
    a keyboard's button.
     */
    class ButtonUIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle btnData = msg.getData();

            BUTTON_STATE newState = (BUTTON_STATE) btnData.get("STATE");
            int digit = (int) btnData.get("DIGIT");

            ImageButton btn = getBtn(digit);

            switch(newState) {
                case VALID:
                    btn.setImageResource(R.drawable.keyb_1);
                    break;
                case INVALID:
                    btn.setImageResource(R.drawable.keyb_0);
                    break;
                case UNPRESSED:
                    int unpressedImg = unpressedIds[digit];
                    btn.setImageResource(unpressedImg);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Preference: Make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        moveToGOScreen = new Intent(MainActivity.this,
                GameOverScreen.class);

        //Match particular button Ids to their numerical value
        digitIdReference = setupDigitRef(digitIds);

        //Find UI Elements
        enterNumberBox = findViewById(R.id.enterNumberBox);
        tmpNumberView = findViewById(R.id.tmpNumberView);
        timerView = findViewById(R.id.timerView);

        Keyboard keyb = new Keyboard(inputFieldH, buttonUiH, gameStateH,
                digitIdReference, GAME);

        setAllButtonListeners(keyb);

        GAME = new Game();
        GAME.begin();
        onNewTurn();

        timer = new Timer(timerView);
        timer.begin();
    }

    private void onNewTurn() {
        digitKeyboard.updateCorrectNumber(GAME.getNumber());
        digitKeyboard.clear();
        clearAllInput(enterNumberBox);
        updateGfx(enterNumberBox);
    }

    private void onGameOver() {
        timer.stop();

        //Transfer Score Data
        moveToGOScreen.putExtra(getString(R.string.score_key),
                String.valueOf(timer.getPreviousResult()));

        startActivity(moveToGOScreen);
        this.finish();
    }

    private void updateGfx(EditText entryBox) {
        clearAllInput(entryBox);

        //To Be Deleted
        String number = String.valueOf(GAME.getNumber());
        tmpNumberView.setText(number);
    }

    private void clearAllInput(EditText entryBox) {
        entryBox.setText(digitKeyboard.getInput());
    }

    private ImageButton getBtn(int digit) {
        //TODO
        return findViewById(digitIds[digit]);
    }

    private void setAllButtonListeners(View.OnClickListener listener) {
        for(int i = 0; i < SIZE_KEYB; i++) {
            int id = digitIds[i];
            findViewById(id).setOnClickListener(listener);
        }
    }

    /*
    A reference which matches the integer id of a button
    to its numerical digit value.
     */
    private HashMap<Integer, Integer> setupDigitRef(int[] digitIds) {
        HashMap<Integer, Integer> digitReference = new HashMap<>();

        for (int i = 0; i < NUM_DIGITS; i++)
            digitReference.put(digitIds[i], i);

        return digitReference;
    }
}
