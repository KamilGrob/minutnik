package com.example.minutnik

import android.annotation.SuppressLint
import android.media.MediaPlayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var mindzies: TextView
    private lateinit var minjedn: TextView
    private lateinit var sekdzies: TextView
    private lateinit var sekjedn: TextView
    private lateinit var start: Button
    private lateinit var stop: Button
    private lateinit var reset: Button
    private lateinit var dodajmindzies: Button
    private lateinit var dodajmindjedn: Button
    private lateinit var dodajsekdzies: Button
    private lateinit var dodajsekjedn: Button
    private lateinit var odejmnijmindzies: Button
    private lateinit var odejmnijmindjedn: Button
    private lateinit var odejmnijsekdzies: Button
    private lateinit var odejmnijsekjedn: Button
    private var countDownTimer: CountDownTimer? = null
    private lateinit var mediaPlayer: MediaPlayer
    var odliczanie = false
    private var remainingTime: Long = 0

    companion object {
        private const val KEY_ODLICZANIE = "odliczanie"
        private const val KEY_REMAINING_TIME = "remainingTime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mindzies = findViewById(R.id.mindzies)
        minjedn = findViewById(R.id.minjedn)
        sekdzies = findViewById(R.id.sekdzies)
        sekjedn = findViewById(R.id.sekjedn)
        start = findViewById(R.id.startbut)
        stop = findViewById(R.id.stopbut)
        reset = findViewById(R.id.resetbut)
        dodajmindzies = findViewById(R.id.minjednp)
        dodajmindjedn = findViewById(R.id.mindziesp)
        dodajsekdzies = findViewById(R.id.sekdziesp)
        dodajsekjedn = findViewById(R.id.sekjednp)
        odejmnijmindzies = findViewById(R.id.minjednm)
        odejmnijmindjedn = findViewById(R.id.mindziesm)
        odejmnijsekdzies = findViewById(R.id.sekdziesm)
        odejmnijsekjedn = findViewById(R.id.sekjednm)



        start.setOnClickListener {
            start()
        }
        stop.setOnClickListener{
            stop()
        }
        reset.setOnClickListener {
            reset()
        }

            dodajmindzies.setOnClickListener {
                dodaj(mindzies)
            }
            dodajmindjedn.setOnClickListener {
                dodaj(minjedn)
            }
            dodajsekdzies.setOnClickListener {
                dodaj(sekdzies)
            }
            dodajsekjedn.setOnClickListener {
                dodaj(sekjedn)
            }
            odejmnijmindzies.setOnClickListener {
                odejmnij(mindzies)
            }
            odejmnijmindjedn.setOnClickListener {
                odejmnij(minjedn)
            }
            odejmnijsekdzies.setOnClickListener {
                odejmnij(sekdzies)
            }
            odejmnijsekjedn.setOnClickListener {
                odejmnij(sekjedn)
            }
        savedInstanceState?.let {

            odliczanie = it.getBoolean(KEY_ODLICZANIE, false)
            remainingTime = it.getLong(KEY_REMAINING_TIME, 0)
            val minutes = remainingTime / 1000 / 60
            val seconds = (remainingTime / 1000) % 60

            mindzies.text = (minutes / 10).toString()
            minjedn.text = (minutes % 10).toString()
            sekdzies.text = (seconds / 10).toString()
            sekjedn.text = (seconds % 10).toString()


            if (odliczanie) {
                // Przywróć odliczanie
                stop()
                start()
            }
        }


    }

    @SuppressLint("SetTextI18n")
    fun dodaj(zmienna: TextView){
        if(!odliczanie) {
            val zmiannaa = zmienna.text.toString().toIntOrNull() ?: 0
            if (zmiannaa < 9 && zmienna != sekdzies) zmienna.text = (zmiannaa + 1).toString()
            else if (zmiannaa < 5) zmienna.text = (zmiannaa + 1).toString()
        }

    }

    fun odejmnij(zmienna: TextView){
        if(!odliczanie) {
            val zmiannaa = zmienna.text.toString().toIntOrNull() ?: 0
            if (zmiannaa > 0) zmienna.text = (zmiannaa - 1).toString()
        }
    }

    private fun start(){

        val mindziess = mindzies.text.toString().toIntOrNull() ?: 0
        val minjednn = minjedn.text.toString().toIntOrNull() ?: 0
        val sekdziess = sekdzies.text.toString().toIntOrNull() ?: 0
        val sekjednn = sekjedn.text.toString().toIntOrNull() ?: 0
        val czas: Long = mindziess.toLong() * 600 + minjednn.toLong() * 60 + sekdziess.toLong() * 10 + sekjednn.toLong()




        if(!odliczanie) {
            odliczanie = true
            countDownTimer = object : CountDownTimer(czas * 1000, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    remainingTime = millisUntilFinished
                    val minutes = millisUntilFinished / 1000 / 60
                    val seconds = (millisUntilFinished / 1000) % 60

                    mindzies.text = (minutes / 10).toString()
                    minjedn.text = (minutes % 10).toString()
                    sekdzies.text = (seconds / 10).toString()
                    sekjedn.text = (seconds % 10).toString()
                }

                override fun onFinish() {
                    playAlarmSound()
                    showToast("Minutnik zakończony!")
                    odliczanie = false
                }
            }
        }
        countDownTimer?.start()
    }
    private fun stop() {
        countDownTimer?.cancel()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare()
        }
        odliczanie = false
    }
    private fun reset() {
        stop()
        mindzies.text = 0.toString()
        minjedn.text = 0.toString()
        sekdzies.text = 0.toString()
        sekjedn.text = 0.toString()
    }

    private fun playAlarmSound() {
        mediaPlayer.start()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ODLICZANIE, odliczanie)
        outState.putLong(KEY_REMAINING_TIME, remainingTime)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        mediaPlayer.release()
        super.onDestroy()
    }
}