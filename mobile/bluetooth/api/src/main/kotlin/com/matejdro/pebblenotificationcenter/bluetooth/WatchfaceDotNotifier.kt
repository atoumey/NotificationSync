package com.matejdro.pebblenotificationcenter.bluetooth

/**
 * Pushes the "there are un-viewed notifications" state to watchfaces that show a
 * notification dot (currently the Halcyon fork, see HALCYON_WATCHFACE_UUID).
 *
 * The watchface cannot ask for the state itself — a watchface's outbound
 * AppMessages are handled by its own PebbleKit JS — so the state is pushed from
 * the phone on every change and re-sent when a watch reconnects.
 */
interface WatchfaceDotNotifier {
   suspend fun updateUnviewed(unviewed: Boolean)
}
