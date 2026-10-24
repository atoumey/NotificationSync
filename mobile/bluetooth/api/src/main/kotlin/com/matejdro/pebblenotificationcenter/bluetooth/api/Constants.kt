package com.matejdro.pebblenotificationcenter.bluetooth.api

import java.util.UUID

val WATCHAPP_UUID: UUID = UUID.fromString("1c5f3908-e3ea-419b-ae55-7f167ea8fafa")

/** UUID of the Halcyon watchface fork that displays the un-viewed notification dot. */
val HALCYON_WATCHFACE_UUID: UUID = UUID.fromString("c8e97ab8-7c12-45fb-975d-e85d5de61e8b")

/**
 * AppMessage key number of Halcyon's NOTIF_UNVIEWED message key.
 *
 * Pebble assigns message key numbers sequentially starting at 10000 in the order the
 * keys are declared in the watchface's package.json. NOTIF_UNVIEWED is appended last
 * to Halcyon's messageKeys, which lands it on 10051. If Halcyon's key list ever
 * changes, re-check build/js/message_keys.json in the watchface build output.
 */
const val HALCYON_NOTIF_UNVIEWED_KEY: UInt = 10051u
