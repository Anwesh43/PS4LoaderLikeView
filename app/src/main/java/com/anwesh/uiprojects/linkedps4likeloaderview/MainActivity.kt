package com.anwesh.uiprojects.linkedps4likeloaderview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.ps4likeloaderview.PS4LoaderLikeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PS4LoaderLikeView.create(this)
    }
}
