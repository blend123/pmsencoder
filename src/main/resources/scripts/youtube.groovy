// videofeed.Web,YouTube=http://gdata.youtube.com/feeds/base/users/freddiew/uploads?alt=rss&v=2&orderby=published
script {
    profile ('YouTube-DL') {
        pattern {
            match { YOUTUBE_DL_PATH }
            protocol([ 'http', 'https' ])
            match { isYouTubeDLCompatible(YOUTUBE_DL_PATH, uri) }
        }

        action {
            if (YOUTUBE_DL_MAX_QUALITY) {
                downloader = YOUTUBE_DL_PATH + [ '--max-quality', YOUTUBE_DL_MAX_QUALITY, '--quiet', '-o', 'DOWNLOADER_OUT', 'URI' ]
            } else {
                downloader = YOUTUBE_DL_PATH + [ '--quiet', '-o', 'DOWNLOADER_OUT', 'URI' ]
            }
        }
    }

    // fall back to the native handler if youtube-dl is not installed/enabled
    profile ('YouTube') {
        pattern {
            // extract the resource's video_id from the URI of the standard YouTube page
            match uri: '^https?://(?:\\w+\\.)?youtube(-nocookie)?\\.com/watch\\?v=(?<youtube_video_id>[^&]+)'
        }

        // Now, with $video_id defined, call the builtin YouTube handler.
        // Note: the parentheses are required for a no-arg method call
        action {
            youtube()
        }
    }
}
