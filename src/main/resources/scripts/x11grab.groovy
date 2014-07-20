// videostream.Web,Screen=Screen,pmsencoder://x11grab
// http://ffmpeg.org/trac/ffmpeg/wiki/How%20to%20grab%20the%20desktop%20%28screen%29%20with%20FFmpeg

import com.sun.jna.Platform

script {
    profile ('pmsencoder://x11grab') {
        pattern {
            match { !Platform.isWindows() }
            match uri: '^pmsencoder://x11grab\\b'
        }

        action {
            def query = uri().query
            def params = http.getNameValuePairs(query) // uses URLDecoder.decode to decode the name and value

            uri = ':0.0'
            set '-s': '1024x768'
            set '-r': '25'
            set '-f': 'x11grab'

            for (param in params) {
                def name = param.name
                def value = param.value

                switch (name) {
                    case 'd':
                    case 'display':
                        uri = param.value
                        break

                    case 's':
                    case 'size':
                        set '-s': param.value
                        break

                    case 'r':
                    case 'framerate':
                        set '-r': param.value
                        break

                    default:
                        set([ (name): value ])
                }
            }

            // TODO port this to finalizeTranscoderArgs
            // uncomment these if you're using the stock ffmpeg in Ubuntu 12.04
            /*
                audioBitrateOptions = []
                videoBitrateOptions = []
                videoTranscodeOptions = "-vcodec mpeg2video -acodec ac3 -f vob"
            */
        }
    }
}
