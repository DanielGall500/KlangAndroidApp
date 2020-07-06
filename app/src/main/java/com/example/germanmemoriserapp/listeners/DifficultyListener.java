package com.example.germanmemoriserapp.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.germanmemoriserapp.R;
import com.example.germanmemoriserapp.enums.Difficulty;
import com.example.germanmemoriserapp.ui.ButtonResource;

public class DifficultyListener implements View.OnClickListener {

    private final Difficulty INITIAL_DIFFICULTY = Difficulty.NORMAL;

    private Difficulty current = INITIAL_DIFFICULTY;
    private int currentId = R.id.diffNormalBtn;

    private Activity activity;
    private ButtonResource btnRes;

    public DifficultyListener(AppCompatActivity activity) {
        this.activity = activity;
        this.btnRes = new ButtonResource();

        //Set our initial button state
        ImageButton initialBtn = activity.findViewById(currentId);
        setButtonState(initialBtn, current, true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        //Old and new difficulty buttons
        Difficulty oldDifficulty = current;
        ImageButton oldBtn = activity.findViewById(currentId);
        ImageButton newBtn = activity.findViewById(id);

        //Set the new id
        currentId = id;

        switch(id) {
            case R.id.diffBeginnerBtn:
                current = Difficulty.BEGINNER;
                break;
            case R.id.diffNormalBtn:
                current = Difficulty.NORMAL;
                break;
            case R.id.diffMasterBtn:
                current = Difficulty.MASTER;
                break;
        }

        //Unpress our old difficulty
        setButtonState(oldBtn, oldDifficulty, false);

        //Press our new difficulty
        setButtonState(newBtn, current, true);
    }

    public void setButtonState(ImageButton b, Difficulty diff, boolean pressed) {
        int res = btnRes.getMenuBtn(diff, pressed);
        b.setImageResource(res);
    }
}