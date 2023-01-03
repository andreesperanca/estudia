package com.andreesperanca.estudia.ui.control

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.andreesperanca.estudia.R
import com.andreesperanca.estudia.data.ControlPanelScreenState
import com.andreesperanca.estudia.services.sendNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ControlPanelViewModel(
    private val app: Application
    ) : AndroidViewModel(app) {

    private var _uiState =
        MutableStateFlow<ControlPanelScreenState>(ControlPanelScreenState.Study)
    val uiState: StateFlow<ControlPanelScreenState> = _uiState.asStateFlow()

    private var _countPomodoro: Int = 0
    val countPomodoro: Int
        get() = _countPomodoro

    fun autoChangeState() {
        when (_uiState.value) {
            ControlPanelScreenState.Study -> {
                if (_countPomodoro == 4) {
                    _uiState.value = ControlPanelScreenState.LongPause
                    pomodoroCounter()
                } else {
                    _countPomodoro++
                    _uiState.value = ControlPanelScreenState.ShortPause
                }
            }
            ControlPanelScreenState.ShortPause -> {
                _uiState.value = ControlPanelScreenState.Study
            }
            else -> {
                _uiState.value = ControlPanelScreenState.Study
            }
        }
    }

    fun manualChangeState() {
        when (_uiState.value) {
            ControlPanelScreenState.Study -> {
                _uiState.value = ControlPanelScreenState.ShortPause
            }
            ControlPanelScreenState.ShortPause -> {
                _uiState.value = ControlPanelScreenState.LongPause
            }
            ControlPanelScreenState.LongPause -> {
                _uiState.value = ControlPanelScreenState.Study
            }
        }
    }

    private fun pomodoroCounter() {
        if (_countPomodoro >= 4) {
            _countPomodoro = 0
        }
    }

    fun showNotification(state: ControlPanelScreenState) {
        val notificationManager = ContextCompat.getSystemService(
            app,
            NotificationManager::class.java
        ) as NotificationManager

        if (state == ControlPanelScreenState.ShortPause) {
            showStudyNotification(notificationManager)
        } else {
            showPauseNotification(notificationManager)
        }
    }

    private fun showStudyNotification(notificationManager: NotificationManager) {
        notificationManager.sendNotification(
            app.getString(R.string.notificationStudyDescription),
            app
        )
    }

    private fun showPauseNotification(notificationManager: NotificationManager) {
        notificationManager.sendNotification(
            app.getString(R.string.notificationPauseDescription),
            app
        )
    }

}