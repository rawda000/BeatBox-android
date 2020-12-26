package com.rawda.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();
    private float speed;
    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0.99f);
    }
    public void release() {
        mSoundPool.release();
    }
    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }
        for (String filename : soundNames) {
            String assetPath = SOUNDS_FOLDER + "/" +
                    filename;
            Sound sound = new Sound(assetPath);
            try {
                load(sound);
                mSounds.add(sound);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Could not load sound " + filename, e);
            }
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
