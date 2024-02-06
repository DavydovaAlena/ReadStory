package ru.adavydova.booksmart.presentation.permission_logic

import android.Manifest
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import java.util.Locale


interface PermissionProvider{
    operator fun invoke (isPermanentDeclined: Boolean): String
    val permissionName: String
}

sealed class PermissionTextProvider: PermissionProvider {
     object RecordAudioTextProvider : PermissionTextProvider() {

        override val permissionName: String
            get() = Manifest.permission.RECORD_AUDIO


        override operator fun invoke(isPermanentDeclined: Boolean): String {
            return if (isPermanentDeclined) {
                "There is no permission for voice search." +
                        " Your can go to the app settings to grant it"
            } else {
                "This app needs access to your audio record to perform " +
                        "voice search"
            }
        }
    }

    object ReadMediaTextProvider: PermissionTextProvider(){
        override val permissionName: String
            get() = when{
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> Manifest.permission.READ_MEDIA_AUDIO
                else ->  Manifest.permission.READ_EXTERNAL_STORAGE
            }

        override fun invoke(isPermanentDeclined: Boolean): String {
            return if (isPermanentDeclined){
                "There is no permission to access media data. " +
                        "Your can go to the app settings to grant it"
            } else {
                "This app needs access to media channels to listen to them"
            }
        }
    }
}

fun launchIntent(
    permissionTextProvider: PermissionTextProvider?,
    isGranted: Boolean,
    work: (Intent)-> Unit){
    when(permissionTextProvider){
        PermissionTextProvider.RecordAudioTextProvider -> {
            if (isGranted){
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")
                }
                work(intent)
            }
        }
        else -> return
    }
}
