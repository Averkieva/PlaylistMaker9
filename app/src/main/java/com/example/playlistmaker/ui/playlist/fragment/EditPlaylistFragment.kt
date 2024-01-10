package com.example.playlistmaker.ui.playlist.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EditPlaylistBinding
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.ui.playlist.view_model.EditPlaylistViewModel
import com.example.playlistmaker.ui.playlistInfo.fragment.PlaylistInfoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : Fragment() {

    private lateinit var bottomNavigator: BottomNavigationView
    private lateinit var editPlaylistBinding: EditPlaylistBinding
    private val editPlaylistViewModel: EditPlaylistViewModel by viewModel()
    private var playlistUri: Uri? = null
    var loadingFile = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editPlaylistBinding = EditPlaylistBinding.inflate(inflater, container, false)

        bottomNavigator.visibility = View.GONE
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)

        return editPlaylistBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        val name = editPlaylistBinding.namePlaylistEditText
        if (playlist != null) {
            name.setText(playlist.playlistName)
            editPlaylistBinding.descriptionPlaylistEditText.setText(playlist.playlistDescription)
        }

        editPlaylistBinding.returnButtonCreatePlaylist.setOnClickListener {
            findNavController().popBackStack()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                editPlaylistBinding.saveButton.backgroundTintList =
                    (ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.colorSecondaryLight
                    ))
                editPlaylistBinding.saveButton.isEnabled = false
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                editPlaylistBinding.saveButton.backgroundTintList =
                    (ContextCompat.getColorStateList(requireContext(), R.color.background))
                editPlaylistBinding.saveButton.isEnabled = true
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editPlaylistBinding.saveButton.backgroundTintList =
                        (ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.colorSecondaryLight
                        ))
                    editPlaylistBinding.saveButton.isEnabled = false
                } else {
                    editPlaylistBinding.saveButton.backgroundTintList =
                        (ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.background
                        ))
                    editPlaylistBinding.saveButton.isEnabled = true
                }
            }
        }

        editPlaylistBinding.namePlaylistEditText.addTextChangedListener(textWatcher)

        val getImage = (playlist?.playlistUri ?: "Unknown Cover")

        if (getImage != "Unknown Cover") {
            editPlaylistBinding.addPhoto.visibility = View.GONE
            Glide.with(this)
                .load(getImage)
                .centerCrop()
                .transform(CenterCrop())
                .placeholder(R.drawable.placeholder_media)
                .override(
                    resources.getDimensionPixelSize(R.dimen.width_and_height_media),
                    resources.getDimensionPixelSize(R.dimen.width_and_height_media)
                )
                .into(editPlaylistBinding.dottedFrame)
            playlistUri = getImage.toUri()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(resources.getDimensionPixelSize(R.dimen.icon_padding))
                        )
                        .override(
                            resources.getDimensionPixelSize(R.dimen.width_and_height_media),
                            resources.getDimensionPixelSize(R.dimen.width_and_height_media)
                        )
                        .into(editPlaylistBinding.dottedFrame)
                    save(uri)
                } else {
                }
            }

        editPlaylistBinding.dottedFrame.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        editPlaylistBinding.saveButton.setOnClickListener {
            if (editPlaylistBinding.namePlaylistEditText.text.toString()
                    .isEmpty()
            ) return@setOnClickListener

            if (playlist != null) {
                savePlaylist(playlist)
            }
        }
    }

    private fun save(uri: Uri) {
        editPlaylistBinding.addPhoto.visibility = View.GONE
        loadingFile = true
        playlistUri = editPlaylistViewModel.saveImageToPrivateStorage(uri)
    }

    private fun savePlaylist(playlist: Playlist) {
        editPlaylistViewModel.savePlaylist(
            playlist,
            editPlaylistBinding.namePlaylistEditText.text.toString(),
            editPlaylistBinding.descriptionPlaylistEditText.text.toString(),
            playlistUri.toString(),
        )
        findNavController().popBackStack()
    }

    companion object {
        fun newInstance() = EditPlaylistFragment()
    }

}