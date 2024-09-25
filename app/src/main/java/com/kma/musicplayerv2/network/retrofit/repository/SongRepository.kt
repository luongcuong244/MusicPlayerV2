package com.kma.musicplayerv2.network.retrofit.repository

import com.kma.musicplayerv2.globalstate.CurrentUser
import com.kma.musicplayerv2.network.retrofit.RetrofitClient
import com.kma.musicplayerv2.network.retrofit.api.SongApi
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.model.SongComment
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.model.AddCommentRequest
import com.kma.musicplayerv2.network.retrofit.model.GetAllSongResponse
import com.kma.musicplayerv2.network.retrofit.model.GetFavouriteSongResponse
import com.kma.musicplayerv2.network.retrofit.model.GetHiddenSongResponse
import com.kma.musicplayerv2.network.retrofit.model.HideSongRequest
import com.kma.musicplayerv2.network.retrofit.model.HideSongResponse
import com.kma.musicplayerv2.network.retrofit.model.SongCommentDto
import com.kma.musicplayerv2.network.retrofit.model.SongDto
import com.kma.musicplayerv2.network.retrofit.model.UnhideSongRequest
import com.kma.musicplayerv2.network.retrofit.model.UnhideSongResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SongRepository {
    private val songApi: SongApi = RetrofitClient.getClient().create(SongApi::class.java)

    fun getAllSongs(apiCallback: ApiCallback<List<Song>>) {
        songApi.getAllSongs().enqueue(
            object : Callback<GetAllSongResponse> {
                override fun onResponse(call: Call<GetAllSongResponse>, response: Response<GetAllSongResponse>) {
                    val songs = response.body()?.data?.map { it.toSong() }
                    if (songs != null) {
                        apiCallback.onSuccess(songs)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<GetAllSongResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getFavouriteSongs(apiCallback: ApiCallback<List<Song>>) {
//        apiCallback.onSuccess(
//            listOf(
//                Song(
//                    id = 1,
//                    title = "Hoa Nở Bên Đường",
//                    artist = Artist(
//                        id = 1,
//                        name = "Quang Đăng Trần",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 2,
//                    title = "Không Trọn Vẹn Nữa",
//                    artist = Artist(
//                        id = 2,
//                        name = "Hồ Quang Hiếu",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 1,
//                    title = "Hoa Nở Bên Đường",
//                    artist = Artist(
//                        id = 1,
//                        name = "Quang Đăng Trần",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 1,
//                    title = "Hoa Nở Bên Đường",
//                    artist = Artist(
//                        id = 1,
//                        name = "Quang Đăng Trần",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 3,
//                    title = "Như Anh Đã Thấy Em",
//                    artist = Artist(
//                        id = 3,
//                        name = "Hương Giang",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/0/f/b/3/0fb3ab6b2dd74fa6b7741ece6d47c29d.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 4,
//                    title = "Ngày Mai Người Ta Lấy Chồng",
//                    artist = Artist(
//                        id = 4,
//                        name = "Thành Đạt",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/a/7/e/c/a7ecb2ffdd31e10861988998ed91c106.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 5,
//                    title = "Chàng Trai Bất Tử",
//                    artist = Artist(
//                        id = 4,
//                        name = "An Vũ",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 6,
//                    title = "Từng Là",
//                    artist = Artist(
//                        id = 4,
//                        name = "Vũ Cát Tường",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 7,
//                    title = "Cắt Đôi Nỗi Sầu",
//                    artist = Artist(
//                        id = 4,
//                        name = "Tăng Duy Tân",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    isFavourite = true
//                )
//            ),
//        )
        songApi.getFavouriteSongs(CurrentUser.getUser()!!.id).enqueue(
            object : Callback<GetFavouriteSongResponse> {
                override fun onResponse(call: Call<GetFavouriteSongResponse>, response: Response<GetFavouriteSongResponse>) {
                    val songs = response.body()?.data
                    if (songs != null) {
                        apiCallback.onSuccess(songs.map { it["song"]!!.toSong() })
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<GetFavouriteSongResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getUploadSongs(apiCallback: ApiCallback<List<Song>>) {
//        apiCallback.onSuccess(
//            listOf(
//                Song(
//                    id = 1,
//                    title = "Hoa Nở Bên Đường",
//                    artist = Artist(
//                        id = 1,
//                        name = "Quang Đăng Trần",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 2,
//                    title = "Không Trọn Vẹn Nữa",
//                    artist = Artist(
//                        id = 2,
//                        name = "Hồ Quang Hiếu",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
//                    isFavourite = true
//                ),
//                Song(
//                    id = 1,
//                    title = "Hoa Nở Bên Đường",
//                    artist = Artist(
//                        id = 1,
//                        name = "Quang Đăng Trần",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
//                    isFavourite = true
//                ),
//            )
//        )
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

    fun getHiddenSongs(apiCallback: ApiCallback<List<Song>>) {
        songApi.getHiddenSongs(CurrentUser.getUser()!!.id).enqueue(
            object : Callback<GetHiddenSongResponse> {
                override fun onResponse(call: Call<GetHiddenSongResponse>, response: Response<GetHiddenSongResponse>) {
                    val songs = response.body()?.data?.map { it.song.toSong() }
                    if (songs != null) {
                        apiCallback.onSuccess(songs)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<GetHiddenSongResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun hideSong(song: Song, apiCallback: ApiCallback<Void>) {
        songApi.hideSong(HideSongRequest(CurrentUser.getUser()!!.id, song.id)).enqueue(
            object : Callback<HideSongResponse> {
                override fun onResponse(call: Call<HideSongResponse>, response: Response<HideSongResponse>) {
                    apiCallback.onSuccess(null)
                }

                override fun onFailure(call: Call<HideSongResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun unhideSongs(songs: List<Song>, apiCallback: ApiCallback<Boolean>) {
        songApi.unhideSongs(UnhideSongRequest(CurrentUser.getUser()!!.id, songs.map { it.id })).enqueue(
            object : Callback<UnhideSongResponse> {
                override fun onResponse(call: Call<UnhideSongResponse>, response: Response<UnhideSongResponse>) {
                    apiCallback.onSuccess(null)
                }

                override fun onFailure(call: Call<UnhideSongResponse>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getSongsById(songIds: List<String>, apiCallback: ApiCallback<List<Song>>) {
        getAllSongs(
            object : ApiCallback<List<Song>> {
                override fun onSuccess(data: List<Song>?) {
                    if (data != null) {
                        apiCallback.onSuccess(data.filter { songIds.contains(it.id) })
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(message: String) {
                    apiCallback.onFailure(message)
                }
            }
        )
    }

    fun getCommentsBySongId(songId: String, apiCallback: ApiCallback<List<SongComment>>) {
//        apiCallback.onSuccess(
//            listOf(
//                SongComment(
//                    id = 1,
//                    songId = 1,
//                    content = "Bài hát hay quá",
//                    userId = 1,
//                    userAvatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    userName = "Ngô Thanh Vân",
//                    createdAt = "11 ngày trước"
//                ),
//                SongComment(
//                    id = 2,
//                    songId = 1,
//                    content = "nghe cuốn vãi",
//                    userId = 2,
//                    userAvatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    userName = "Nhân Nguyễn",
//                    createdAt = "23 giờ trước"
//                ),
//                SongComment(
//                    id = 3,
//                    songId = 1,
//                    content = "mê nhất các bài trong album mới luôn í",
//                    userId = 3,
//                    userAvatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    userName = "Bống",
//                    createdAt = "1 ngày trước"
//                )
//            )
//        )
        return
        songApi.getCommentsBySongId(songId).enqueue(
            object : Callback<List<SongCommentDto>> {
                override fun onResponse(call: Call<List<SongCommentDto>>, response: Response<List<SongCommentDto>>) {
                    val comments = response.body()?.map { it.toSongComment() }
                    if (comments != null) {
                        apiCallback.onSuccess(comments)
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<List<SongCommentDto>>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun addComment(songId: String, content: String, apiCallback: ApiCallback<SongComment>) {
//        apiCallback.onSuccess(
//            SongComment(
//                id = 1,
//                songId = songId,
//                content = content,
//                userId = 1,
//                userAvatar = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                userName = "Là Tôi Đó",
//                createdAt = "Vừa xong"
//            )
//        )
        return
        songApi.addComment(AddCommentRequest(songId, content)).enqueue(
            object : Callback<SongCommentDto> {
                override fun onResponse(call: Call<SongCommentDto>, response: Response<SongCommentDto>) {
                    if (response.body() != null) {
                        apiCallback.onSuccess(response.body()!!.toSongComment())
                    } else {
                        apiCallback.onFailure("Unknown error")
                    }
                }

                override fun onFailure(call: Call<SongCommentDto>, t: Throwable) {
                    apiCallback.onFailure(t.message ?: "Unknown error")
                }
            }
        )
    }

    fun getSongsByPlaylistId(playlistId: String, apiCallback: ApiCallback<List<Song>>) {
//        apiCallback.onSuccess(
//            listOf(
//                Song(
//                    id = 5,
//                    title = "Chàng Trai Bất Tử",
//                    artist = Artist(
//                        id = 4,
//                        name = "An Vũ",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg",
//                    isFavourite = false
//                ),
//                Song(
//                    id = 7,
//                    title = "Cắt Đôi Nỗi Sầu",
//                    artist = Artist(
//                        id = 6,
//                        name = "Tăng Duy Tân",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    isFavourite = false
//                ),
//                Song(
//                    id = 6,
//                    title = "Từng Là",
//                    artist = Artist(
//                        id = 5,
//                        name = "Vũ Cát Tường",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg",
//                    isFavourite = false
//                ),
//            )
//        )
        return
        songApi.getSongsByPlaylistId(playlistId).enqueue(
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

    fun getRecentlySongs(apiCallback: ApiCallback<List<Song>>) {
//        apiCallback.onSuccess(
//            listOf(
//                Song(
//                    id = 5,
//                    title = "Chàng Trai Bất Tử",
//                    artist = Artist(
//                        id = 4,
//                        name = "An Vũ",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/6/5/6/e/656ed01c7604230fd6c9fc894dfa012d.jpg",
//                    isFavourite = false
//                ),
//                Song(
//                    id = 7,
//                    title = "Cắt Đôi Nỗi Sầu",
//                    artist = Artist(
//                        id = 6,
//                        name = "Tăng Duy Tân",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
//                    isFavourite = false
//                ),
//                Song(
//                    id = 6,
//                    title = "Từng Là",
//                    artist = Artist(
//                        id = 5,
//                        name = "Vũ Cát Tường",
//                        image = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg"
//                    ),
//                    path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
//                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w360_r1x1_jpeg/avatars/d/8/7/3/d87392e24ee10b99988fcea608194751.jpg",
//                    isFavourite = false
//                ),
//            )
//        )
        return
        songApi.getRecentlySongs().enqueue(
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