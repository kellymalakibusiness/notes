package com.malakiapps.notes.feature_note.presentation.note_instance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.malakiapps.notes.R
import com.malakiapps.notes.feature_note.presentation.note_instance.components.TransparentHintTextField
import com.malakiapps.notes.feature_note.presentation.util.ArgumentNames
import com.malakiapps.notes.ui.theme.Virgil
import com.malakiapps.notes.ui.theme.appColors
import com.malakiapps.notes.ui.theme.spacing
import com.malakiapps.notes.utils.TestTags
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesInstanceScreen(
    navController: NavController,
    viewModel: NoteInstanceViewModel = hiltViewModel()
) {
    val titleState = viewModel.titleState.value
    val titleHint = stringResource(id = R.string.title)
    val bodyHint = stringResource(id = R.string.msg_start_typing)
    val unknownError = stringResource(id = R.string.msg_unknown_error)
    val bodyState = viewModel.bodyState.value

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                is NoteInstanceViewModel.NoteInstanceUIEvent.CloseNoteInstance -> {
                    if(event.deleteNoteId != null){
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(ArgumentNames.deleteNoteId, event.deleteNoteId)
                    }
                    navController.navigateUp()
                }
                is NoteInstanceViewModel.NoteInstanceUIEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message ?: unknownError
                    )
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(NoteInstanceEvent.CloseNote(false))
    }

    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_PAUSE){
                viewModel.onEvent(NoteInstanceEvent.OnPauseDisposableEffect)
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.small)
            ) {
                IconButton(onClick = {
                    viewModel.onEvent(NoteInstanceEvent.CloseNote(false))
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(
                        id = R.string.msg_go_back
                    ))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.onEvent(NoteInstanceEvent.CloseNote(true))
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(
                        id = R.string.delete_note
                    ))
                }
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = MaterialTheme.spacing.kobe, end = MaterialTheme.spacing.medium)
                .background(color = MaterialTheme.appColors.screenBackground)
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
           TransparentHintTextField(text = titleState.text, hint = titleHint,
               onValueChange = { titleValue ->
                   viewModel.onEvent(NoteInstanceEvent.InsertedTitle(titleValue))
                               },
               onFocusChange ={ focus ->
                   viewModel.onEvent(NoteInstanceEvent.ChangeTitleFocus(focus))
               },
               isHintVisible = titleState.isHintVisible,
               singleLine = true,
               textStyle = MaterialTheme.typography.titleLarge.copy(fontSize = MaterialTheme.typography.headlineSmall.fontSize),
               testTag = TestTags.NOTE_TITLE_TEXT_FIELD
           )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.kobe))

            Row {
                val dateRowStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
                Text(text = viewModel.date.value, color = MaterialTheme.appColors.dateLabel, style = dateRowStyle)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = "|", color = MaterialTheme.appColors.dateLabel, style = dateRowStyle)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(text = bodyState.text.replace(" ", "").length.toString(),
                    color = MaterialTheme.appColors.dateLabel,
                    style = dateRowStyle,
                    modifier = Modifier.testTag(TestTags.NOTE_INSTANCE_CHARACTER_COUNT)
                )
                Text(text = " characters", color = MaterialTheme.appColors.dateLabel, style = dateRowStyle)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            TransparentHintTextField(text = bodyState.text, hint = bodyHint,
                onValueChange = { bodyValue ->
                    viewModel.onEvent(NoteInstanceEvent.InsertedBody(bodyValue))
                },
                onFocusChange ={ focus ->
                    viewModel.onEvent(NoteInstanceEvent.ChangeBodyFocus(focus))
                },
                isHintVisible = bodyState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = Virgil),
                modifier = Modifier.weight(1f),
                testTag = TestTags.NOTE_BODY_TEXT_FIELD
                )
        }
    }

}