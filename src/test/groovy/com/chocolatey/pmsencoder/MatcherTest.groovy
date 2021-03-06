package com.chocolatey.pmsencoder

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class MatcherTest extends PMSEncoderTestCase {
    // no match - change nothing
    private void noMatch() {
        assertMatch([
            loadDefaultScripts: true,
            uri: 'http://www.example.com',
            wantMatches: []
        ])
    }

    // confirm that there are no side-effects that prevent this returning the same result for the same input
    void testIdempotent() {
        noMatch()
        noMatch()
    }

    // XXX CompileStatic error:
    //
    //     Cannot find matching method com.chocolatey.pmsencoder.MatcherTest#assertMatch(
    //         java.util.LinkedHashMap <java.lang.String,
    //         java.io.Serializable>
    //     )
    @CompileStatic(TypeCheckingMode.SKIP)
    void testInterpolationInDefaultTranscoderArgs() {
        assertMatch([
            loadDefaultScripts: true,
            uri: 'http://www.example.com',
            // make sure nbcores is interpolated here as 3 in -threads 3
            // (this is mocked to 3 in PMSEncoderTestCase)
            wantTranscoder: { List<String> transcoder ->
                transcoder[4] == '-threads' && transcoder[5] == '3'
            },
            useDefaultTranscoder: true
        ])
    }

    void testApple() {
        assertMatch([
            loadDefaultScripts: true,
            uri: 'http://www.apple.com/foobar.mov',
            wantTranscoder: { List<String> transcoder ->
                transcoder[-2] == '-user-agent' && transcoder[-1] == 'QuickTime/7.6.2'
            },
            wantMatches: [ 'Apple Trailers' ]
        ])
    }

    void testYouTube() {
        youTubeCommon('22')
    }

    // verify that globally modifying YOUTUBE_ACCEPT works
    void testYOUTUBE_ACCEPT() {
        def script = this.getClass().getResource('/youtube_accept.groovy')
        youTubeCommon('18', script)
    }

    private void youTubeCommon(String fmt, URL script = null) {
        def youtube = 'http://www.youtube.com'
        def uri = "$youtube/watch?v=031Dshcnso4"
        assertMatch([
            loadDefaultScripts: true,
            uri: uri,
            script: script,
            wantMatches: [ 'YouTube' ],
            wantStash: { Stash stash ->
                assert stash.keySet().toList().sort() == [
                    'uri',
                    'youtube_fmt',
                    'youtube_uri',
                    'youtube_video_id',
                ]

                def video_id = stash.get('youtube_video_id')
                assert video_id == '031Dshcnso4'
                assert stash.get('youtube_fmt') == fmt
                assert stash.get('youtube_uri') == uri
                assert stash.get('uri') =~ '\\.(?:youtube|googlevideo)\\.com/videoplayback\\?'
                return true
            },
            wantTranscoder: []
        ])
    }

    void testGameTrailers() {
        def uri = 'http://www.gametrailers.com/video/educate-interview-ufc-2009/48298'
        def wantURI = 'http://download.gametrailers.com/gt_vault/48298/t_ufc09u_educate_int_gt.flv'

        assertMatch([
            loadDefaultScripts: true,
            uri: uri,
            wantURI: wantURI,
            wantMatches: [ 'GameTrailers' ]
        ])
    }
}
