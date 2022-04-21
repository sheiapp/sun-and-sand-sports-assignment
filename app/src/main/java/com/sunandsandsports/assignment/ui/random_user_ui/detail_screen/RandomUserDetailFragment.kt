package com.sunandsandsports.assignment.ui.random_user_ui.detail_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunandsandsports.assignment.R
import com.sunandsandsports.assignment.databinding.FragmentRandomUserDetailBinding
import com.sunandsandsports.assignment.ui.random_user_ui.RandomUserSharedViewModel
import com.sunandsandsports.assignment.ui.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RandomUserDetailFragment : Fragment(R.layout.fragment_random_user_detail) {
    @Inject
    lateinit var glideRequestManager: RequestManager
    private var _binding: FragmentRandomUserDetailBinding? = null
    private val sharedViewModel: RandomUserSharedViewModel by activityViewModels()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRandomUserDetailBinding.bind(view)
        setupRandomUserDetailUi()
    }

    private fun setupRandomUserDetailUi() {
        collectLatestLifecycleFlow(sharedViewModel.selectedRandomUserDetail) { userDetail ->
            setupUserImage(userDetail?.picture?.medium)
            _binding?.apply {
                mobileNumber.text = userDetail?.phone
                userName.text = userDetail?.name?.getFullName()
                cityName.text = userDetail?.location?.city
                stateName.text = userDetail?.location?.state
                countryName.text = userDetail?.location?.country
                userAge.text = userDetail?.dob?.age
                userGender.text = userDetail?.gender
            }
        }
    }

    private fun setupUserImage(imageUrl: String?) {
        _binding?.userPicture?.let {
            glideRequestManager
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(it)
        }
    }

}