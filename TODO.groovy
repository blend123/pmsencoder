/*
    links:

        - *real* (i.e. type-checked/validated) named parameters: http://jira.codehaus.org/browse/GROOVY-3520
        - transitive delegation (via @Delegate): http://jira.codehaus.org/browse/GRECLIPSE-1627
        - non-mavenized dependencies in gradle:
            - https://github.com/Ullink/gradle-repositories-plugin
            - http://issues.gradle.org/browse/GRADLE-2179
            - http://forums.gradle.org/gradle/topics/how_to_use_a_jar_from_a_sourceforge_project_as_a_dependency_in_a_project
            - https://github.com/GradleFx/GradleFx-Examples/blob/master/sdk-autoinstall/build.gradle
*/

// move all of this to GitHub issues

// script management: disable/enable scripts/settings through the Swing UI (cf. Greasemonkey)

/*
  investigate adding seek support for YouTube videos:

      http://stackoverflow.com/questions/3302384/youtubes-hd-video-streaming-server-technology
*/

// WEB.groovy (path is relative to the renderer's root folder):

videostream ('Web/TV') {
    uri  = 'mms://example.com/stream'
    name = 'Example'
    thumbnail = 'http://example.com/rss.jpg' // optional
}

// use the user-specified folder, rather than appending the feed name, so feeds can be merged:

videofeed ('Web/YouTube/Favourites') {
    uri = 'http://youtube.com/api/whatever?1-50'
}

videofeed ('Web/YouTube/Favourites') {
    uri = 'http://youtube.com/api/whatever?50-100'
}

// tests for append

// use a web interface because a) Swing sucks and b) headless servers. Only use swing to enable/disable the web server
// and set the port.

// investigate using busybox-w32/ash instead of cmd.exe on Windows

// Pattern: add extension matcher (use the stash URI):

    extension 'm3u8'
    extension ([ 'mp4', 'm4v' ])

// profile: add 'extension' variable

/*
    script loading order:

        builtin scripts
        user scripts

    script stages/phases:

        BEGIN
        INIT
        DEFAULT
        CHECK
        END

document this:

    replacing a profile with a profile with a different name
    does not change its canonical name. this ensures other replacement profiles
    have a predictable, consistent name to refer to

TODO: determine behaviour (if any) if a replacement has a different stage

*/

// remove rtmpdump protocol and manage everything through pmsencoder://

// No need to expose pms. Just use PMS.get() as normal (automatically add net.PMS import?)

// documentation/example: dlna.getSystemName() can be used to access the original URI e.g.:

    if (dlna.systemName.toURI().protocol == 'concat') { ... }

// bring back reject: e.g.:

    reject uri: '^concat:'
    reject { domain == 'members.example.com' }

/*

    image to video:

        ffmpeg -r 24 -i http://ip:port/plugin/name/imdb_plot?id=42&fmt=png \
            -vcodec mpeg2video -qscale 2 -vframes 1 transcoder.out

*/

// use MPlayer -dumpstream as a downloader/null transcoder
// FIXME: MPlayer can't dump to stdout: http://lists.mplayerhq.hu/pipermail/mplayer-users/2006-April/059898.html

// test Pattern.scrape

// make the rtmp2pms functionality available via a web page (e.g. on github.io) using JavaScript:
// i.e. enter 1) name/path 2) the command line 3) optional thumbnail URI and click to generate the WEB.conf
// line

// unused example/pattern
// TODO: expose guard in scripts

    import static example.getJson

    profile ('Example') {
        pattern {
            // don't match if the query fails
            match {
                json = guard(false) { getJson(uri) }
            }
        }

        action {
            uri = json['uri']
        }
    }

// unused example/pattern: access the renderer (RendererConfiguration):

    def renderer = params.mediaRenderer

// weird Groovy(++) bug:

    println("patternMap class: ${patternMap.class.name}") // fails in stash_objects.groovy
    println("actionMap class: ${patternMap.class.name}") // fails in stash_objects.groovy

// these work:

    println("patternString class: ${patternString.class.name}")
    println("patternList class: ${patternList.class.name}")
    println("patternMap class: ${patternList.getClass().getName()}")

// investigate using gmock: https://code.google.com/p/gmock/

// extension methods (e.g. String.match): figure out why the META-INF method doesn't work

// test nested context blocks

// add XML support to the jSoup methods:

    $(xml: true, 'test')

// http.head(): implement a HeaderMap that implements String getValue() and List<String> getValues()

    def headers = http.head()
    headers.getValue('Content-type') // getValues('Content-type').empty() ? null : getValues('Content-type').get(0)
    headers.getValues('Warning')

// Alternatively/also, return (comma-joined) strings: http://greenbytes.de/tech/webdav/rfc2616.html#message.headers

    headers.get('Warnings')

// log the version of youtube-dl and get-flash-videos if available

// add web audio engine

// http://www.ps3mediaserver.org/forum/viewtopic.php?f=6&t=16828&p=p79334#p79334
// other possible names: SKIP, DISABLE, REJECT, UNSUPPORTED, DECLINE
// XXX this has been implemented: document it

    profile (on: INCOMPATIBLE) {
        match { protocol == 'udp' }
    }

// change pattern/action to when/then? what about profiles with no 'when' block? allow 'then'
// to be called as e.g. 'always'?

    profile ('No When Block 1') {
        then { ... }
    }

    profile ('No When Block 2') {
        always { ... }
    }

// replace hook with:

    exec  stringOrList // blocking
    async stringOrList

// e.g.

    async ([ NOTIFY_SEND, 'PMSEncoder', "Playing ${dlna.getName()}" ]) { rv ->
        // optional callback
    }

    def rv = exec '/usr/bin/foo --bar --baz'
    // rv.stdout, rv.stderr, rv.status

// allow the command contexts to be initialised with an executable?

    downloader ('/usr/bin/mydownloader') {
        set '-foo': 'bar'
    }

// XXX can be done already/better with list/string assignment:

    downloader = '/usr/bin/downloader -foo bar'

// allow script to set common profile settings (removes a common gotcha when writing tests).
// profile gets default values from script:

    script (stopOnMatch: true) {
        profile ('Foo')                     { } // inherit stopOnMatch: true
        profile ('Bar', stopOnMatch: false) { } // override
    }

// test: add testResolve method to test the builtin resolvers:

    testResolve(
        'http://www.example.com/foo.html',
        'http://cdn.example.com/video/foo.mp4'
    )

// automatically update PMSEncoder/YouTube-DL: org.apache.commons.io.FileUtils.copyURLToFile
