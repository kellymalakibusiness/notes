package com.malakiapps.notes.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.malakiapps.notes.feature_note.presentation.note_instance.NotesInstanceScreen
import com.malakiapps.notes.feature_note.presentation.notes_home.NotesHomeScreen
import com.malakiapps.notes.feature_note.presentation.util.Screen
import com.malakiapps.notes.ui.theme.NotesTheme
import com.malakiapps.notes.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                Surface(
                    color = MaterialTheme.appColors.screenBackground
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.NotesHome.route){
                        composable(
                            route = Screen.NotesHome.route,
                            exitTransition = {
                                fadeOut(
                                    animationSpec = tween(300)
                                )
                            },
                            popEnterTransition = {
                                fadeIn(
                                    animationSpec = tween(300)
                                )
                            }
                        ){ navBackStackEntry ->
                            NotesHomeScreen(navController = navController, navBackStackEntry = navBackStackEntry)
                        }

                        composable(Screen.NotesInstance.route +
                        "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            ),
                            enterTransition = {
                                scaleIn(
                                    animationSpec = tween(300),
                                    initialScale = 0.7f,
                                    transformOrigin = TransformOrigin(0.2f, 0.1f)
                                ) + fadeIn(
                                    animationSpec = tween(300)
                                )
                            },
                            popExitTransition = {
                                scaleOut(
                                    animationSpec = tween(300),
                                    targetScale = 0.7f,
                                    transformOrigin = TransformOrigin(0.2f, 0.1f)
                                ) + fadeOut(
                                    animationSpec = tween(300)
                                )
                            }
                        ){
                            NotesInstanceScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}