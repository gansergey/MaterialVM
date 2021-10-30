package com.example.material.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.material.R
import com.example.material.databinding.FragmentPictureOfTheDayBinding
import com.example.material.model.PictureOfTheDayViewModel
import com.example.material.repository.PODServerResponseData
import com.example.material.repository.PictureOfTheDayData
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PictureOfTheDayFragment : Fragment() {
    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
//        binding.switchTheme.setOnClickListener {
//            changeTheme()
//        }
    }

    private fun changeTheme() {
//        if (binding.switchTheme.isChecked) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                serverResponseData.url?.let {
                    showSuccess(serverResponseData)
                    showLoading(false)
                } ?: showError(getString(R.string.error_message_load_img))
            }
            is PictureOfTheDayData.Loading -> {
                showLoading(true)
            }
            is PictureOfTheDayData.Error -> {
                showLoading(false)
                showError(data.error.message.toString())

            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding.loadingLayout) {
            if (state) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }


    }

    private fun showSuccess(data: PODServerResponseData) {
        binding.imageView.load(data.url) {
            lifecycle(this@PictureOfTheDayFragment)
            // error(R.drawable.ic_baseline_no_image)
            // placeholder(R.drawable.ic_baseline_motion_photos_on_24)
        }
        view?.findViewById<TextView>(R.id.bottom_sheet_description_header)?.text = data.title
        view?.findViewById<TextView>(R.id.bottom_sheet_description)?.text = data.explanation
        binding.imageView.contentDescription = data.title
    }

    private fun showError(error: String) {
        //TO DO
    }

}