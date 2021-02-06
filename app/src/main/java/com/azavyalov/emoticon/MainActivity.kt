package com.azavyalov.emoticon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azavyalov.emoticon.EmoticonView.Companion.HAPPY
import com.azavyalov.emoticon.EmoticonView.Companion.SAD
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        happyButton.setOnClickListener {
            emoticonView.faceState = HAPPY
        }

        sadButton.setOnClickListener {
            emoticonView.faceState = SAD
        }

        emoticonView.setOnClickListener {
            val currentState = emoticonView.faceState
            if (currentState == HAPPY) {
                emoticonView.faceState = SAD
            } else {
                emoticonView.faceState = HAPPY
            }
        }
    }
}