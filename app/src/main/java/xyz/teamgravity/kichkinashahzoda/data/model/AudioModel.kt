package xyz.teamgravity.kichkinashahzoda.data.model

import androidx.annotation.RawRes
import xyz.teamgravity.kichkinashahzoda.R

enum class AudioModel(
    val id: String,
    val title: String,
    @RawRes val audio: Int
) {
    Part1(
        id = "1",
        title = "Birinchi qism",
        audio = R.raw.audio_1
    ),
    Part2(
        id = "2",
        title = "Ikkinchi qism",
        audio = R.raw.audio_2
    ),
    Part3(
        id = "3",
        title = "Yakuniy qism",
        audio = R.raw.audio_3
    );
}