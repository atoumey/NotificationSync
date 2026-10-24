package com.matejdro.pebblenotificationcenter.bluetooth

class FakeWatchfaceDotNotifier : WatchfaceDotNotifier {
   val receivedStates = mutableListOf<Boolean>()

   override suspend fun updateUnviewed(unviewed: Boolean) {
      receivedStates.add(unviewed)
   }
}
