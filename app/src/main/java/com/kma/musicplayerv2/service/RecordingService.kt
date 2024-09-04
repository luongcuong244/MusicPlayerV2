package com.kma.musicplayerv2.service
import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.model.RecordingModel
import com.kma.musicplayerv2.utils.FileUtils
import java.io.File
import java.util.Timer
import java.util.TimerTask

class RecordingService : Service() {
    private val CLASS_NAME = javaClass.simpleName
    private lateinit var mFileName: String
    private lateinit var mFilePath: String
    private var mRecorder: MediaRecorder? = null
    private var mStartingTimeMillis: Long = 0
    private var mElapsedMillis: Long = 0
    private var mIncrementTimerTask: TimerTask? = null
    private val myBinder: IBinder = LocalBinder()
    var isRecording = false
        private set
    var isResumeRecording = false
        private set

    inner class LocalBinder : Binder() {
        val service: RecordingService
            get() = this@RecordingService
    }

    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }

    interface OnRecordingStatusChangedListener {
        fun onRecordingStarted()
        fun onTimerChanged(seconds: Int)
        fun onAmplitudeInfo(amplitude: Int)
        fun onRecordingStopped(recording: RecordingModel?)
        fun onRecordingSkip()
        fun onRecordingPause()
        fun onRecordingResume()
    }

    private var onRecordingStatusChangedListener: OnRecordingStatusChangedListener? = null
    fun setOnRecordingStatusChangedListener(onRecordingStatusChangedListener: OnRecordingStatusChangedListener?) {
        this.onRecordingStatusChangedListener = onRecordingStatusChangedListener
    }

    @SuppressLint("RestrictedApi")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val activityStarter = intent.getBooleanExtra(EXTRA_ACTIVITY_STARTER, false)
        /*if (!activityStarter) { // automatic scheduled recording
            // Get next recording data.
            recordingsRepository.getNextScheduledRecording(
                    new RecordingsRepositoryInterface.GetScheduledRecordingCallback() {
                        @Override
                        public void onSuccess(ScheduledRecording recording) {
                            int duration = (int) (recording.getEnd() - recording.getStart());
                            if (!isRecording && hasPermissions()) {
                                startRecording(duration);
                            }
                            // Remove scheduled recording from database and schedule next recording.
                            recordingsRepository.deleteScheduledRecording(recording, null);
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            startService(ScheduledRecordingService.makeIntent(RecordingService.this));
                                        }
                                    });
                                } else {
                                    startService(ScheduledRecordingService.makeIntent(RecordingService.this));
                                }
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onFailure() {
                            Log.e(TAG, CLASS_NAME + " - getNextScheduledRecording(): " + "error in retrieving next scheduled recording");
                        }
                    }
            );
        }*/return START_NOT_STICKY
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    override fun onDestroy() {
        super.onDestroy()
        if (mRecorder != null) {
            stopRecording()
        }
        if (onRecordingStatusChangedListener != null) onRecordingStatusChangedListener = null
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun startRecording(duration: Int) {
        try {
            startForeground(NOTIFICATION_RECORDING, createNotification())
            setFileNameAndPath()
            mRecorder = MediaRecorder()
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mRecorder!!.setOutputFile(mFilePath)
            mRecorder!!.setMaxDuration(duration) // if this is a scheduled recording, set the max duration, after which the Service is stopped
            mRecorder!!.setAudioChannels(1)
            mRecorder!!.setAudioSamplingRate(44100)
            mRecorder!!.setAudioEncodingBitRate(192000)

            // Called only if a max duration has been set (scheduled recordings).
            mRecorder!!.setOnInfoListener { mediaRecorder: MediaRecorder?, what: Int, extra: Int ->
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecording()
                }
            }
            mRecorder!!.prepare()
            mRecorder!!.start()
            mStartingTimeMillis = System.currentTimeMillis()
            isRecording = true
            isResumeRecording = true
            startTimer()
        } catch (e: Exception) {
            Log.e(
                TAG,
                "$CLASS_NAME - startRecording(): prepare() failed$e"
            )
        }
        if (onRecordingStatusChangedListener != null) {
            onRecordingStatusChangedListener!!.onRecordingStarted()
        }
    }

    private fun setFileNameAndPath() {
        mFileName = "music_playerv2_" + System.currentTimeMillis()
        mFilePath = FileUtils.getDirectoryPath(this) + "/" + mFileName + "." + "mp3"
    }

    private fun startTimer() {
        val mTimer = Timer()
        // Increment seconds.
        mElapsedMillis = 0
        mIncrementTimerTask = object : TimerTask() {
            override fun run() {
                if (mIncrementTimerTask == null) {
                    cancel()
                }
                mElapsedMillis += 100
                if (onRecordingStatusChangedListener != null) {
                    onRecordingStatusChangedListener!!.onTimerChanged(mElapsedMillis.toInt() / 1000)
                }
                if (onRecordingStatusChangedListener != null && mRecorder != null) {
                    try {
                        onRecordingStatusChangedListener!!.onAmplitudeInfo(mRecorder!!.maxAmplitude)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 100, 100)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun stopRecording() {
        try {
            mRecorder!!.stop()
            mRecorder!!.release()
            isRecording = false
            isResumeRecording = false
            mRecorder = null

            // Stop timer.
            if (mIncrementTimerTask != null) {
                mIncrementTimerTask!!.cancel()
                mIncrementTimerTask = null
            }
            val recording =
                RecordingModel(mFileName, mFilePath, mElapsedMillis, System.currentTimeMillis(), 0)
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener!!.onRecordingStopped(recording)
            }
            // No Activity connected -> stop the Service (scheduled recording).
            if (onRecordingStatusChangedListener == null) stopSelf()
            stopForeground(true)

            FileUtils.addAudioToMediaStore(this, mFilePath)
        } catch (e: Exception) {
            println("RecordingService.stopRecording e = $e")
        }
    }

    fun skipFileRecord() {
        try {
            mRecorder!!.stop()
            mRecorder!!.release()
            isRecording = false
            isResumeRecording = false
            mRecorder = null
            mElapsedMillis = 0
            // Communicate the file path to the connected Activity.
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener!!.onRecordingSkip()
            }
            // Stop timer.
            if (mIncrementTimerTask != null) {
                mIncrementTimerTask!!.cancel()
                mIncrementTimerTask = null
            }
            if (mFilePath != null) {
                val file = File(mFilePath)
                if (file.exists()) {
                    file.delete()
                }
            }
            // No Activity connected -> stop the Service (scheduled recording).
            if (onRecordingStatusChangedListener == null) stopSelf()
            stopForeground(true)
        } catch (e: Exception) {
            println("RecordingService.skipFileRecord e = $e")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun pauseRecording() {
        try {
            mRecorder!!.pause()
            isResumeRecording = false
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener!!.onRecordingPause()
            }
            if (mIncrementTimerTask != null) {
                mIncrementTimerTask!!.cancel()
                mIncrementTimerTask = null
            }
        } catch (e: Exception) {
            println("RecordingService.pauseRecording e = $e")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun resumeRecording() {
        try {
            mRecorder!!.resume()
            isResumeRecording = true
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener!!.onRecordingResume()
            }
            val mTimer = Timer()
            // Increment seconds.
            mIncrementTimerTask = object : TimerTask() {
                override fun run() {
                    if (mIncrementTimerTask == null) {
                        cancel()
                    }
                    mElapsedMillis += 100
                    if (onRecordingStatusChangedListener != null) {
                        onRecordingStatusChangedListener!!.onTimerChanged(mElapsedMillis.toInt() / 1000)
                    }
                    if (onRecordingStatusChangedListener != null && mRecorder != null) {
                        try {
                            onRecordingStatusChangedListener!!.onAmplitudeInfo(mRecorder!!.maxAmplitude)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            mTimer.scheduleAtFixedRate(mIncrementTimerTask, 100, 100)
        } catch (e: Exception) {
            println("RecordingService.resumeRecording e = $e")
        }
    }

    private fun createNotification(): Notification {
        val channelId: String
        channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        } else {
            ""
        }
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_mic_white_36dp)
                .setContentTitle("Recording...")
                .setOngoing(true)
        /*try {
            mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                    new Intent[]{new Intent(getApplicationContext(), RecordActivity.class)}, PendingIntent.FLAG_UPDATE_CURRENT));
        } catch (Exception e) {
            mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                    new Intent[]{new Intent(getApplicationContext(), RecordActivity.class)}, PendingIntent.FLAG_MUTABLE));
        }*/return mBuilder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "recording_service"
        val channelName = "Recording Service"
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(chan)
        return channelId
    }

    private fun hasPermissions(): Boolean {
        val writePerm = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val audioPerm = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        return writePerm && audioPerm
    }

    companion object {
        private const val TAG = "SCHEDULED_RECORDER_TAG"
        //        private val EXTRA_ACTIVITY_STARTER: String =
//            BuildConfig.APPLICATION_ID + ".EXTRA_ACTIVITY_STARTER"
        private val EXTRA_ACTIVITY_STARTER: String = ""
        private const val NOTIFICATION_RECORDING = 2

        fun makeIntent(context: Context, activityStarter: Boolean): Intent {
            val intent = Intent(context.applicationContext, RecordingService::class.java)
            intent.putExtra(EXTRA_ACTIVITY_STARTER, activityStarter)
            return intent
        }
    }
}