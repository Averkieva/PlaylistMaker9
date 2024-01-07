package com.example.playlistmaker.ui.playlist.fragment

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlaylistBinding
import com.example.playlistmaker.ui.playlist.view_model.CreateNewPlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreateNewPlaylistFragment : Fragment() {
    private lateinit var bottomNavigator: BottomNavigationView
    private val createNewPlaylistViewModel: CreateNewPlaylistViewModel by viewModel()
    private lateinit var createPlaylistBinding: CreatePlaylistBinding
    private var playlistUri: Uri? = null
    var loadingFile = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createPlaylistBinding = CreatePlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE

        createPlaylistBinding.createButton.setOnClickListener {
            if (createPlaylistBinding.namePlaylistEditText.text.toString().isEmpty())
                return@setOnClickListener
            createNewPlaylistViewModel.createPlaylist(
                createPlaylistBinding.namePlaylistEditText.text.toString(),
                createPlaylistBinding.descriptionPlaylistEditText.text.toString(),
                playlistUri.toString()
            )

            val isDark = createNewPlaylistViewModel.isDark()
            val themeColor = if (isDark) Color.BLACK else Color.WHITE

            val playlistNameEditText = createPlaylistBinding.namePlaylistEditText.text
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.playlist_created, playlistNameEditText))
                .setNegativeButton(getString(R.string.ok)) { _, _ ->
                    findNavController().popBackStack()
                }
                .show()
                .apply {
                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(themeColor)
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(themeColor)
                }
        }
        return createPlaylistBinding.root
    }

    @RequiresApi(33)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPlaylistBinding.returnButtonCreatePlaylist.setOnClickListener { interruptCreation() }

        val textWatcher = object : TextWatcher {
            //выключаем
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                createPlaylistBinding.createButton.backgroundTintList =
                    (ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.colorSecondaryLight
                    ))
                createPlaylistBinding.createButton.isEnabled = false
            }

            //включаем
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                createPlaylistBinding.createButton.backgroundTintList =
                    (ContextCompat.getColorStateList(requireContext(), R.color.background))
                createPlaylistBinding.createButton.isEnabled = true
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    createPlaylistBinding.createButton.backgroundTintList =
                        (ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.colorSecondaryLight
                        ))
                    createPlaylistBinding.createButton.isEnabled = false
                } else {
                    createPlaylistBinding.createButton.backgroundTintList =
                        (ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.background
                        ))
                    createPlaylistBinding.createButton.isEnabled = true
                }
            }
        }

        createPlaylistBinding.namePlaylistEditText.addTextChangedListener(textWatcher)

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
                        .into(createPlaylistBinding.dottedFrame)
                    save(uri)
                } else {
                }
            }

        createPlaylistBinding.dottedFrame.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            interruptCreation()
        }
    }

    private fun interruptCreation() {
        val title = createPlaylistBinding.namePlaylistEditText.text
        val description = createPlaylistBinding.descriptionPlaylistEditText.text

        if (!title.isNullOrEmpty() || loadingFile || !description.isNullOrEmpty()) {
            val isDark = createNewPlaylistViewModel.isDark()
            val textColor = if (isDark) Color.BLACK else Color.WHITE

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.end_creation))
                .setMessage(getString(R.string.lost_data))
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.end)) { _, _ ->
                    findNavController().popBackStack()
                }
                .show()
                .apply {
                    getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(textColor)
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(textColor)
                }
        } else {
            findNavController().popBackStack()
        }
    }

    private fun save(uri: Uri) {
        createPlaylistBinding.addPhoto.visibility = View.GONE
        loadingFile = true
        playlistUri = createNewPlaylistViewModel.saveImageToPrivateStorage(uri)
    }

    companion object {
        const val quality = 30
    }

}