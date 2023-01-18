package com.assignment.catawiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.assignment.catawiki.navigation.AppNavGraph
import com.assignment.catawiki.ui.theme.CatawikiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CatawikiTheme {
                AppNavGraph()
            }
        }
    }
}
