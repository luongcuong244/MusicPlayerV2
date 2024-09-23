package com.kma.musicplayerv2.ui.screen.explore

import android.os.Bundle
import android.view.View
import com.kma.musicplayerv2.ui.core.BaseFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.FragmentExploreBinding
import com.kma.musicplayerv2.network.retrofit.model.ExploreResponse
import com.kma.musicplayerv2.network.retrofit.repository.ExploreRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : BaseFragment<FragmentExploreBinding>() {
    override fun getContentView(): Int = R.layout.fragment_explore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ExploreRepository.fetchExplore(
            object : Callback<ExploreResponse> {
                override fun onResponse(
                    call: Call<ExploreResponse>,
                    response: Response<ExploreResponse>
                ) {
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {

                }
            }
        )
    }
}