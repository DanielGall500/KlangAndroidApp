package ucd.danielgall.klangapp.sound;

import android.content.Context;
import android.media.MediaPlayer;

import ucd.danielgall.klangapp.sound.elements.NumberClip;
import ucd.danielgall.klangapp.sound.elements.SoundElement;

public class SoundSystem implements SoundPlayer {

    MediaPlayer mMediaPlayer;
    SoundDirectory sDir;
    Context appContext;

    public SoundSystem(Context context) {
        appContext = context;
        mMediaPlayer = new MediaPlayer();
        sDir = new SoundDirectory(appContext);
    }

    private void playResource(Context context, int resId) {
        release();
        mMediaPlayer = android.media.MediaPlayer.create(context, resId);
        mMediaPlayer.start();
    }

    @Override
    public void play(SoundElement element) {
        int resId = sDir.getId(element);
        playResource(appContext,resId);
    }

    @Override
    public void play(int n) {
        SoundElement numberClip = new NumberClip(n);
        int resId = sDir.getId(numberClip);
        playResource(appContext,resId);
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }
}
