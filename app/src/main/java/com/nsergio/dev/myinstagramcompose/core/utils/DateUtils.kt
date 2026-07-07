package com.nsergio.dev.myinstagramcompose.core.utils
/**
 * Returns a human-readable relative time in English
 * (e.g. “Just now”, “1 minute ago”, “3 hours ago”, “2 days ago”).
 *
 * @param epochMillis UTC milliseconds when the post was created
 * @param nowMillis   Current time in millis (default = current clock)
 */
fun relativeTimeString(
    epochMillis: Long,
    nowMillis: Long = Utils.generateRandomFloat()
): String {
    val diff      = nowMillis - epochMillis
    val minute    = 60_000L
    val hour      = 60 * minute
    val day       = 24 * hour

    return when {
        diff < minute -> "Just now"

        diff < hour -> {
            val mins = diff / minute
            if (mins == 1L) "1 minute ago" else "$mins minutes ago"
        }

        diff < day -> {
            val hrs = diff / hour
            if (hrs == 1L) "1 hour ago" else "$hrs hours ago"
        }

        else -> {
            val days = diff / day
            if (days == 1L) "1 day ago" else "$days days ago"
        }
    }
}