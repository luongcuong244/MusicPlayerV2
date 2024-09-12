package com.kma.musicplayerv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.kma.musicplayerv2.R
import com.kma.musicplayerv2.databinding.DialogChangeUserNameBinding
import com.kma.musicplayerv2.network.common.ApiCallback
import com.kma.musicplayerv2.network.retrofit.repository.UserRepository

class ChangeUserNameDialog(
    private val userName: String = "",
    private val onUserNameChanged: (String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogChangeUserNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseDialog)
        binding = DialogChangeUserNameBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etName.setText(userName)
        setupListeners()
    }

    fun setupListeners() {
        binding.rlCancel.setOnClickListener {
            dismiss()
        }
        binding.rlSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(
                    context,
                    context?.getString(R.string.please_enter_your_name),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            UserRepository.changeUserName(name, object : ApiCallback<Boolean> {
                override fun onSuccess(data: Boolean?) {
                    if (data == null) {
                        onFailure("Unknown error")
                        return
                    }
                    onUserNameChanged(name)
                    dismiss()
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        context,
                        "Failed to create playlist: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}