package xyz.teamgravity.kichkinashahzoda.data.repository

import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.data.model.SongModel

class MainRepository {

    companion object {
        private val data = listOf(
            SongModel(
                id = 0,
                name = R.string.first_part,
                song = R.raw.audio_1
            ),
            SongModel(
                id = 1,
                name = R.string.second_part,
                song = R.raw.audio_2
            ),
            SongModel(
                id = 2,
                name = R.string.third_part,
                song = R.raw.audio_3
            )
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////

    fun getSong(id: Int): SongModel? {
        return data.find { it.id == id }
    }

    fun getSongs(): List<SongModel> {
        return data
    }
}