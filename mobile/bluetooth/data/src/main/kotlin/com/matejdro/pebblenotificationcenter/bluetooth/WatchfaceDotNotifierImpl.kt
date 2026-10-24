package com.matejdro.pebblenotificationcenter.bluetooth

import com.matejdro.pebblenotificationcenter.bluetooth.api.HALCYON_NOTIF_UNVIEWED_KEY
import com.matejdro.pebblenotificationcenter.bluetooth.api.HALCYON_WATCHFACE_UUID
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dispatch.core.DefaultCoroutineScope
import io.rebble.pebblekit2.client.PebbleInfoRetriever
import io.rebble.pebblekit2.client.PebbleSender
import io.rebble.pebblekit2.common.model.PebbleDictionaryItem
import io.rebble.pebblekit2.common.model.WatchIdentifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logcat.logcat

/**
 * Sends the un-viewed notification state to the Halcyon watchface fork, which draws
 * a dot while the state is true. See [WatchfaceDotNotifier].
 *
 * The state is re-sent whenever a watch (re)connects, so the dot recovers after a
 * disconnect or watch reboot without waiting for the next notification change.
 * Nothing is sent while the notification listener state is unknown — in particular,
 * losing notification-listener access must not clear the dot, so this class is only
 * ever fed definitive states computed from the mirrored notifications.
 */
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
@Suppress("MagicNumber") // 1/0 are the protocol values of the NOTIF_UNVIEWED key
class WatchfaceDotNotifierImpl(
   private val pebbleInfoRetriever: PebbleInfoRetriever,
   private val pebbleSender: PebbleSender,
   private val defaultScope: DefaultCoroutineScope,
) : WatchfaceDotNotifier {

   @Volatile
   private var lastUnviewed: Boolean? = null

   init {
      defaultScope.launch {
         var hadConnectedWatch = false
         pebbleInfoRetriever.getConnectedWatches().collect { watches ->
            val hasWatch = watches.isNotEmpty()
            if (hasWatch && !hadConnectedWatch) {
               lastUnviewed?.let { state ->
                  sendToWatches(watches.map { it.id }, state)
               }
            }
            hadConnectedWatch = hasWatch
         }
      }
   }

   override suspend fun updateUnviewed(unviewed: Boolean) {
      if (unviewed == lastUnviewed) {
         return
      }
      lastUnviewed = unviewed

      val connectedWatches = pebbleInfoRetriever.getConnectedWatches().first()
         .map { it.id }
      if (connectedWatches.isEmpty()) {
         return
      }

      sendToWatches(connectedWatches, unviewed)
   }

   private suspend fun sendToWatches(watches: List<WatchIdentifier>, unviewed: Boolean) {
      val result = pebbleSender.sendDataToPebble(
         HALCYON_WATCHFACE_UUID,
         mapOf(
            HALCYON_NOTIF_UNVIEWED_KEY to PebbleDictionaryItem.UInt8(if (unviewed) 1u else 0u)
         ),
         watches,
      )
      logcat { "Watchface dot state (unviewed=$unviewed) send result: $result" }
   }
}
