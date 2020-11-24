package ru.ridkeim.soundstest

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object{
        private val TAG = MainActivity::class.java.canonicalName?.toString()
    }

    private lateinit var soundPool : SoundPool
    private lateinit var assetManager : AssetManager

    private var  catSound : Int = 0
    private var  chickenSound : Int = 0
    private var  cowSound : Int = 0
    private var  dogSound : Int = 0
    private var  duckSound : Int = 0
    private var  sheepSound : Int = 0
    private var streamId : Int = -1

    private val clickListener : View.OnClickListener = View.OnClickListener {

        val sound= when(it.id){
            R.id.buttonCat -> catSound
            R.id.buttonChicken -> chickenSound
            R.id.buttonCow -> cowSound
            R.id.buttonDog -> dogSound
            R.id.buttonDuck -> duckSound
            R.id.buttonSheep -> sheepSound
            else -> 0
        }
        playSound(sound)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ImageButton>(R.id.buttonSheep).setOnClickListener(clickListener)
        findViewById<ImageButton>(R.id.buttonDuck).setOnClickListener(clickListener)
        findViewById<ImageButton>(R.id.buttonDog).setOnClickListener(clickListener)
        findViewById<ImageButton>(R.id.buttonChicken).setOnClickListener(clickListener)
        findViewById<ImageButton>(R.id.buttonCat).setOnClickListener(clickListener)
        findViewById<ImageButton>(R.id.buttonCow).setOnClickListener(clickListener)
    }

    private fun createSoundPool(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            createNewSoundPool()
        }else{
            createOldSoundPool()
        }
    }

    @Suppress("DEPRECATION")
    private fun createOldSoundPool() {
        soundPool = SoundPool(6,AudioManager.STREAM_MUSIC,0)
    }

    private fun createNewSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build()
    }

    private fun loadSound(filename: String): Int{
        val assetFileDescriptor : AssetFileDescriptor
        try {
            assetFileDescriptor = assetManager.openFd(filename)
        } catch (e : IOException){
            e.printStackTrace()
            Toast.makeText(this,"Не мошу загрузить файл $filename", Toast.LENGTH_SHORT).show()
            return -1
        }
        return soundPool.load(assetFileDescriptor,1)
    }

    private fun playSound(id : Int): Int {
        if(id > 0){
            streamId = soundPool.play(id, 1f, 1f, 1, 0, 1f)
            Log.d(TAG,"Played sound: streamID=$streamId")
        }
        return streamId
    }

    override fun onResume() {
        super.onResume()
        createSoundPool()
        assetManager = assets
        catSound = loadSound("cat.ogg")
        chickenSound = loadSound("chicken.ogg")
        cowSound = loadSound("cow.ogg")
        dogSound = loadSound("dog.ogg")
        duckSound = loadSound("duck.ogg")
        sheepSound = loadSound("sheep.ogg")
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()
    }

}