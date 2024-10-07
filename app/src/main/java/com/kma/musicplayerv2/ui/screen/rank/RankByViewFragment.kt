package com.kma.musicplayerv2.ui.screen.rank

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentRankByViewBinding
import com.kma.musicplayerv2.model.Artist
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.network.retrofit.model.GetRankByViewResponse
import com.kma.musicplayerv2.network.retrofit.repository.RankRepository
import com.kma.musicplayerv2.ui.adapter.RankAdapter
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankByViewFragment : BaseFragment<FragmentRankByViewBinding>() {

    private var rankAdapter: RankAdapter? = null
    private var rankList: MutableList<Song> = mutableListOf()

    override fun getContentView(): Int = R.layout.fragment_rank_by_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        RankRepository.getRankByView(
//            object : Callback<GetRankByViewResponse> {
//                override fun onResponse(
//                    call: Call<GetRankByViewResponse>,
//                    response: Response<GetRankByViewResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val data = response.body()
//                        if (data != null) {
//                            rankList.clear()
//                            rankList.addAll(data.data)
//                            setupAdapter()
//                        } else {
//                            binding.tvNoData.visibility = View.VISIBLE
//                            binding.rvRankByView.visibility = View.GONE
//                        }
//                    } else {
//                        binding.tvNoData.visibility = View.VISIBLE
//                        binding.rvRankByView.visibility = View.GONE
//                        Toast.makeText(
//                            requireContext(),
//                            "Error: ${response.message()}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<GetRankByViewResponse>, t: Throwable) {
//                    t.printStackTrace()
//                    binding.tvNoData.visibility = View.VISIBLE
//                    binding.rvRankByView.visibility = View.GONE
//                    Toast.makeText(
//                        requireContext(),
//                        "Error: ${t.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        )
        rankList.clear()
        rankList.apply {
            addAll(
                listOf(
                    Song(
                        id = "1",
                        title = "Hoa Nở Bên Đường",
                        artist = Artist(
                            id = "1",
                            name = "Quang Đăng Trần",
                            image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                        ),
                        path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                        thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                        videoUrl = "",
                        createdTime = 0,
                    ),
                    Song(
                        id = "2",
                        title = "Không Trọn Vẹn Nữa",
                        artist = Artist(
                            id = "2",
                            name = "Hồ Quang Hiếu",
                            image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg"
                        ),
                        path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                        thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/b/9/5/8/b9585640f130b885953eb5a8355697a9.jpg",
                        videoUrl = "",
                        createdTime = 0,
                    ),
                    Song(
                        id = "3",
                        title = "Hoa Nở Bên Đường",
                        artist = Artist(
                            id = "3",
                            name = "Quang Đăng Trần",
                            image = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg"
                        ),
                        path = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3",
                        thumbnail = "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/f/f/d/9/ffd9b78fae7fa10bde459b331fe382f6.jpg",
                        videoUrl = "",
                        createdTime = 0,
                    ),
                )
            )
        }
        setupAdapter()
    }

    private fun setupAdapter() {
        rankAdapter = RankAdapter(
            rankList,
            {
                // onClickMore
            },
            {
                // onClickItem
            }
        )
        binding.rvRankByView.apply {
            adapter = rankAdapter
        }
        if (binding.rvRankByView.itemDecorationCount == 0) {
            binding.rvRankByView.addItemDecoration(
                VerticalSpaceItemDecoration(
                    this.resources.getDimension(
                        com.intuit.sdp.R.dimen._13sdp
                    ).toInt()
                )
            )
        }
        binding.rvRankByView.layoutManager = LinearLayoutManager(requireActivity())
    }
}