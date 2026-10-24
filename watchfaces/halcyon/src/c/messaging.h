#pragma once
#include <pebble.h>

void messaging_init(void (*message_processed_callback)(void),
                    void (*request_failed_callback)(void));
void inbox_received_callback(DictionaryIterator *iterator, void *context);
void inbox_dropped_callback(AppMessageResult reason, void *context);
void outbox_failed_callback(DictionaryIterator *iterator, AppMessageResult reason, void *context);
void outbox_sent_callback(DictionaryIterator *iterator, void *context);
void messaging_request_update();

// Whether NotificationSync reports any un-viewed notifications. Drives the
// notification dot drawn by main.c; persisted across restarts.
bool messaging_notif_unviewed(void);