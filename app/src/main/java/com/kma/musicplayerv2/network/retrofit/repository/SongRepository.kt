package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayer.network.retrofit.api.SongApi
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.model.SongDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SongRepository {
    private val songApi: SongApi = RetrofitClient.getClient().create(SongApi::class.java)

    fun getAllSongs(apiCallback: ApiCallback<List<Song>>) {
        songApi.getAllSongs().enqueue(
            object : Callback<List<SongDto>> {
                override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
                    val songs = response.body()?.map { it.toSong() }
                    if (songs != null) {
                        apiCallback.onSuccess(songs)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getFavouriteSongs(apiCallback: ApiCallback<List<Song>>) {
        apiCallback.onSuccess(
            listOf(
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 2,
                    title = "Không Trọn Vẹn Nữa",
                    artist = Artist(
                        id = 2,
                        name = "Hồ Quang Hiếu",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 3,
                    title = "Như Anh Đã Thấy Em",
                    artist = Artist(
                        id = 3,
                        name = "Hương Giang",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 4,
                    title = "Ngày Mai Người Ta Lấy Chồng",
                    artist = Artist(
                        id = 4,
                        name = "Thành Đạt",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 5,
                    title = "Chàng Trai Bất Tử",
                    artist = Artist(
                        id = 4,
                        name = "An Vũ",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 6,
                    title = "Từng Là",
                    artist = Artist(
                        id = 4,
                        name = "Vũ Cát Tường",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 7,
                    title = "Cắt Đôi Nỗi Sầu",
                    artist = Artist(
                        id = 4,
                        name = "Tăng Duy Tân",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
                    isFavourite = true
                )
            ),
        )
        return
//        getAllSongs(object : ApiCallback<List<Song>> {
//            override fun onSuccess(data: List<Song>) {
//                val favouriteSongs = data.filter { it.isFavourite }
//                apiCallback.onSuccess(favouriteSongs)
//            }
//            override fun onFailure(message: String) {
//                apiCallback.onFailure(message)
//            }
//        })
    }

    fun getUploadSongs(apiCallback: ApiCallback<List<Song>>) {
        apiCallback.onSuccess(
            listOf(
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 2,
                    title = "Không Trọn Vẹn Nữa",
                    artist = Artist(
                        id = 2,
                        name = "Hồ Quang Hiếu",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
            )
        )
        return
        songApi.getUploadedSongs().enqueue(
            object : Callback<List<SongDto>> {
                override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
                    val songs = response.body()?.map { it.toSong() }
                    if (songs != null) {
                        apiCallback.onSuccess(songs)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun addFavouriteSong(song: Song, apiCallback: ApiCallback<Void>) {
        apiCallback.onSuccess(null)
        return
        songApi.addFavouriteSong(song.id).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    apiCallback.onSuccess(null)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun removeFavouriteSong(song: Song, apiCallback: ApiCallback<Void>) {
        apiCallback.onSuccess(null)
        return
        songApi.removeFavouriteSong(song.id).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    apiCallback.onSuccess(null)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun hideSong(song: Song, apiCallback: ApiCallback<Void>) {
        apiCallback.onSuccess(null)
        return
        songApi.hideSong(song.id).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    apiCallback.onSuccess(null)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getSongsById(songIds: List<Long>, apiCallback: ApiCallback<List<Song>>) {
        apiCallback.onSuccess(
            listOf(
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 2,
                    title = "Không Trọn Vẹn Nữa",
                    artist = Artist(
                        id = 2,
                        name = "Hồ Quang Hiếu",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 3,
                    title = "Như Anh Đã Thấy Em",
                    artist = Artist(
                        id = 3,
                        name = "Hương Giang",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 4,
                    title = "Ngày Mai Người Ta Lấy Chồng",
                    artist = Artist(
                        id = 4,
                        name = "Thành Đạt",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 5,
                    title = "Chàng Trai Bất Tử",
                    artist = Artist(
                        id = 4,
                        name = "An Vũ",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 6,
                    title = "Từng Là",
                    artist = Artist(
                        id = 4,
                        name = "Vũ Cát Tường",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg",
                    isFavourite = true
                ),
                Song(
                    id = 7,
                    title = "Cắt Đôi Nỗi Sầu",
                    artist = Artist(
                        id = 4,
                        name = "Tăng Duy Tân",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg"
                    ),
                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
                    isFavourite = true
                )
            ),
        )
        return
        songApi.getSongsById(songIds).enqueue(
            object : Callback<List<SongDto>> {
                override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
                    val songs = response.body()?.map { it.toSong() }
                    if (songs != null) {
                        apiCallback.onSuccess(songs)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }
}