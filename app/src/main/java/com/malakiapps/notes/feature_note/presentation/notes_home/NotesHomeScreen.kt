package com.malakiapps.notes.feature_note.presentation.notes_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.presentation.notes_home.components.NoteItem
import com.malakiapps.notes.feature_note.presentation.notes_home.components.SortingSelector
import com.malakiapps.notes.feature_note.presentation.util.ArgumentNames
import com.malakiapps.notes.feature_note.presentation.util.Screen
import com.malakiapps.notes.feature_note.presentation.util.UiText
import com.malakiapps.notes.ui.theme.Virgil
import com.malakiapps.notes.ui.theme.appColors
import com.malakiapps.notes.ui.theme.spacing
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesHomeScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    viewModel: NotesHomeViewModel = hiltViewModel()
) {
    val state = viewModel.homeState.value
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { homeEvent ->
            when(homeEvent){
                is HomeUIEvent.ShowSnackBar -> {
                    val message = homeEvent.message.asString(context)
                    val actionLabel = homeEvent.actionLabel.asString(context)
                    val result = snackBarHostState.showSnackbar(
                            message = message,
                            actionLabel = actionLabel,
                            duration = SnackbarDuration.Short
                    )
                        if(result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(homeEvent.snackBarAction)
                        }
                }
            }
        }
    }

    val deleteSelectedNote = navBackStackEntry.savedStateHandle.get<Int>(ArgumentNames.deleteNoteId)
    deleteSelectedNote?.let { noteId ->
        viewModel.onEvent(NotesHomeEvent.GetThenDeleteNote(noteId))
        //Reset our savedState argument back to default
        navBackStackEntry.savedStateHandle[ArgumentNames.deleteNoteId] = null
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          navController.navigate(Screen.NotesInstance.route)
                },
                containerColor = MaterialTheme.colorScheme.secondary
                ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.msg_add_note))
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState)}
    ){
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = MaterialTheme.spacing.medium)
                .background(color = MaterialTheme.appColors.screenBackground)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(start = MaterialTheme.spacing.medium)) {
                Text(text = "Notes", color = MaterialTheme.appColors.textColor, style = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.headlineSmall.fontSize, fontFamily = Virgil))
                IconButton(onClick = {
                    viewModel.onEvent(NotesHomeEvent.ToggleOrderSelector)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = stringResource(id = R.string.msg_sort_notes))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
                ) {

                AnimatedVisibility(
                    visible = state.isOrderSelectorVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    SortingSelector(
                        modifier = Modifier
                            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                            .background(
                                color = Color(ColorUtils.blendARGB(MaterialTheme.colorScheme.secondary.hashCode(), MaterialTheme.appColors.cardBackground.hashCode(), 0.8f)).copy(alpha = 0.6f)
                            )
                            .padding(MaterialTheme.spacing.extraSmall),
                        orderType = state.selectedOrder,
                        onOrderChange = { orderType ->
                            viewModel.onEvent(NotesHomeEvent.Order(orderType))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.kobe))
            if(state.notes.isNotEmpty()){
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    verticalItemSpacing = MaterialTheme.spacing.medium,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ){
                    items(state.notes){ note ->
                        NoteItem(
                            note = note,
                            modifier = Modifier
                                .clip(RoundedCornerShape(MaterialTheme.spacing.smallMedium))
                                .background(color = MaterialTheme.appColors.cardBackground)
                                .clickable {
                                    navController.navigate(Screen.NotesInstance.route + "?noteId=${note.id}")
                                },
                            dateInstances = viewModel.dateInstances,
                            onDeleteClick = {
                                viewModel.onEvent(NotesHomeEvent.DeleteNote(note))
                            }
                        )
                    }
                }
            }else{
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.spacing.large, vertical = MaterialTheme.spacing.medium)
                ) {
                    Text(text = stringResource(id = R.string.msg_no_notes_found),
                        color = MaterialTheme.appColors.dateLabel,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }
    }

}