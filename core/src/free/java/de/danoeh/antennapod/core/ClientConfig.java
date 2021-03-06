package de.danoeh.antennapod.core;

import android.content.Context;
import java.security.Security;

/*
 * If you get an error here ("package org.conscrypt does not exist"), you are probably doing a free
 * build and didn't pass "-PfreeBuild" to gradle (e.g. "./gradlew assembleFreeRelease -PfreeBuild").
 *
 * If you are doing a non-free build using "assembleRelease" or "assembleDebug" and get this error,
 * use "assemblePlayRelease" or "assemblePlayDebug" instead (e.g. "./gradlew assemblePlayRelease").
 */
import org.conscrypt.Conscrypt;

import de.danoeh.antennapod.core.preferences.PlaybackPreferences;
import de.danoeh.antennapod.core.preferences.SleepTimerPreferences;
import de.danoeh.antennapod.core.preferences.UserPreferences;
import de.danoeh.antennapod.core.storage.PodDBAdapter;
import de.danoeh.antennapod.core.util.NetworkUtils;
import de.danoeh.antennapod.core.util.exception.RxJavaErrorHandlerSetup;
import de.danoeh.antennapod.core.util.gui.NotificationUtils;

/**
 * Stores callbacks for core classes like Services, DB classes etc. and other configuration variables.
 * Apps using the core module of AntennaPod should register implementations of all interfaces here.
 */
public class ClientConfig {

    /**
     * Should be used when setting User-Agent header for HTTP-requests.
     */
    public static String USER_AGENT;

    public static ApplicationCallbacks applicationCallbacks;

    public static DownloadServiceCallbacks downloadServiceCallbacks;

    public static PlaybackServiceCallbacks playbackServiceCallbacks;

    public static GpodnetCallbacks gpodnetCallbacks;

    public static DBTasksCallbacks dbTasksCallbacks;

    public static CastCallbacks castCallbacks;

    private static boolean initialized = false;

    public static synchronized void initialize(Context context) {
        if (initialized) {
            return;
        }
        PodDBAdapter.init(context);
        UserPreferences.init(context);
        PlaybackPreferences.init(context);
        NetworkUtils.init(context);

        // Insert bundled conscrypt as highest security provider (overrides OS version).
        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        SleepTimerPreferences.init(context);
        RxJavaErrorHandlerSetup.setupRxJavaErrorHandler();
        NotificationUtils.createChannels(context);
        initialized = true;
    }

}
