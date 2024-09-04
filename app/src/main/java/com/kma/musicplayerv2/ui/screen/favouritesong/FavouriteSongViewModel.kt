package com.kma.musicplayerv2.ui.screen.favouritesong

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.model.SortType

class FavouriteSongViewModel : ViewModel() {
    val songs = mutableListOf<Song>().apply {
        addAll(
            listOf(
                Song(
                    id = 1,
                    title = "Hoa Nở Bên Đường",
                    artist = Artist(
                        id = 1,
                        name = "Quang Đăng Trần",
                        image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                    ),
                    path = "https://a128-z3.zmdcdn.me/4ec8da3d980aaa5d4cf74510efb5ad8f?authen=exp=1720801021~acl=/4ec8da3d980aaa5d4cf74510efb5ad8f/*~hmac=f4be37d6e66c1cc7dbbb85e632ffb4b6",
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
                    path = "https://a128-z3.zmdcdn.me/f93588e1f99f6ad0627844615aaf799c?authen=exp=1720801038~acl=/f93588e1f99f6ad0627844615aaf799c/*~hmac=b5fd8767e196ac21c3299620cb74a3e0",
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
                    path = "https://a128-z3.zmdcdn.me/4ec8da3d980aaa5d4cf74510efb5ad8f?authen=exp=1720801021~acl=/4ec8da3d980aaa5d4cf74510efb5ad8f/*~hmac=f4be37d6e66c1cc7dbbb85e632ffb4b6",
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
                    path = "https://a128-z3.zmdcdn.me/4ec8da3d980aaa5d4cf74510efb5ad8f?authen=exp=1720801021~acl=/4ec8da3d980aaa5d4cf74510efb5ad8f/*~hmac=f4be37d6e66c1cc7dbbb85e632ffb4b6",
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
                    path = "https://a128-z3.zmdcdn.me/ae3c1c054f7e0e0eb1cc0f1eca8905a6?authen=exp=1720801142~acl=/ae3c1c054f7e0e0eb1cc0f1eca8905a6/*~hmac=6f8a91be4272cdd1692012fa938eb93c",
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
                    path = "https://a128-z3.zmdcdn.me/046417bee65ad87bcfd2a708ff00e2e0?authen=exp=1720801315~acl=/046417bee65ad87bcfd2a708ff00e2e0/*~hmac=72c837c2a04f7afc92ce98aab67e9bb8",
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
                    path = "https://a128-z3.zmdcdn.me/066310e91f7f9317fe73dd58f8fdd411?authen=exp=1720801049~acl=/066310e91f7f9317fe73dd58f8fdd411/*~hmac=93cd2aa20dd802f12ab25d183250f8c6",
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
                    path = "https://a128-z3.zmdcdn.me/6f2731d30a15aa8f218f7257548a4b4b?authen=exp=1720801164~acl=/6f2731d30a15aa8f218f7257548a4b4b/*~hmac=cbd747b0f0084930f0cb71973de9aef9",
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
                    path = "https://a128-z3.zmdcdn.me/49e28a31e7fee3089127f25498cad799?authen=exp=1720801275~acl=/49e28a31e7fee3089127f25498cad799/*~hmac=071db5c934244d41b3a0cb4f2302ba9c",
                    thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/f/0/1/bf0182328238f2a252496a63e51f1f74.jpg",
                    isFavourite = true
                )
            ),
        )
    }
    val tempSongs = mutableListOf<Song>().apply {
        addAll(songs)
    }
    val filterByArtists = mutableListOf<Artist>()

    private val _sortBy = MutableLiveData(SortType.NEWEST)
    val sortBy: LiveData<SortType> = _sortBy

    fun sortSongs() {
        when (_sortBy.value) {
            SortType.NEWEST -> tempSongs.sortByDescending { it.createdTime }
            SortType.OLDEST -> tempSongs.sortBy { it.createdTime }
            SortType.SONG_NAME -> tempSongs.sortBy { it.title }
            SortType.ARTIST_NAME -> tempSongs.sortBy { it.artist.name }
            null -> return
        }
    }

    fun setFilterByArtists(artists: List<Artist>) {
        filterByArtists.clear()
        filterByArtists.addAll(artists)
        tempSongs.clear()
        if (artists.isEmpty()) {
            tempSongs.addAll(songs)
        } else {
            tempSongs.addAll(songs.filter { song -> artists.any { it.id == song.artist.id } })
        }
        sortSongs()
    }

    fun unFavouriteSong(song: Song) {
        // call api
        songs.remove(song)
        tempSongs.remove(song)
    }

    fun setSortBy(sortType: SortType) {
        _sortBy.value = sortType
    }
}