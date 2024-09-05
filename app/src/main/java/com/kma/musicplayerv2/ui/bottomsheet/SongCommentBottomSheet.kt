package com.kma.musicplayerv2.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.BottomSheetSongCommentBinding
import com.kma.musicplayerv2.model.Song
import com.kma.musicplayerv2.model.SongComment
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.SongRepository
import com.kma.musicplayerv2.ui.adapter.SongCommentAdapter
import com.kma.musicplayerv2.ui.customview.VerticalSpaceItemDecoration

class SongCommentBottomSheet(
    private val song: Song
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSongCommentBinding
    private val songComments = mutableListOf<SongComment>()
    private var songCommentAdapter: SongCommentAdapter? = null

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        binding = BottomSheetSongCommentBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        SongRepository.getCommentsBySongId(
            song.id,
            object : ApiCallback<List<SongComment>> {
                override fun onSuccess(data: List<SongComment>?) {
                    if (data.isNullOrEmpty()) {
                        binding.rvSongComment.visibility = View.GONE
                        binding.tvNoComment.visibility = View.VISIBLE
                        binding.tvNumberOfComments.text = "0 ${getString(R.string.comment)}"
                        return
                    }
                    binding.tvNumberOfComments.text = "${data.size} ${getString(R.string.comment)}"
                    songComments.addAll(data)
                    songCommentAdapter = SongCommentAdapter(songComments)
                    binding.rvSongComment.adapter = songCommentAdapter
                    binding.rvSongComment.layoutManager = LinearLayoutManager(context)
                    binding.rvSongComment.addItemDecoration(
                        VerticalSpaceItemDecoration(
                            requireActivity().resources.getDimension(
                                com.intuit.sdp.R.dimen._13sdp
                            ).toInt()
                        )
                    )
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.ivSend.setOnClickListener {
            val text = binding.etComment.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(context, getString(R.string.comment_cannot_be_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SongRepository.addComment(
                song.id,
                text,
                object : ApiCallback<SongComment> {
                    override fun onSuccess(data: SongComment?) {
                        binding.etComment.text.clear()
                        songComments.add(0, data!!)
                        songCommentAdapter?.notifyItemInserted(0)
                        binding.tvNumberOfComments.text = "${songComments.size} ${getString(R.string.comment)}"
                    }

                    override fun onFailure(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // set window input mode to adjust resize to avoid the keyboard
        dialog?.window?.setSoftInputMode(16)
    }
}