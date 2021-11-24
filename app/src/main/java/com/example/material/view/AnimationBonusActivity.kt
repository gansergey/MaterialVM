package com.example.material.view

import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.material.R
import com.example.material.databinding.ActivityAnimationsBonusStartBinding


class AnimationBonusActivity : AppCompatActivity() {
    private var show = false
    private lateinit var binding: ActivityAnimationsBonusStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationsBonusStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backgroundImage.setOnClickListener {
            if (show) hideComponents() else
                showComponents()
        }

        binding.description.typeface =
            Typeface.createFromAsset(assets, "GaliverSansObliquesItalic.ttf")
        binding.title.typeface =
            Typeface.createFromAsset(assets, "MarsMissionItalic.ttf")
        setSpannableTitle()
        setSpannableDescription()
    }

    private fun showComponents() {
        show = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_animations_bonus_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(
            binding.constraintContainer,
            transition
        )
        constraintSet.applyTo(binding.constraintContainer)
    }

    private fun hideComponents() {
        show = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.activity_animations_bonus_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(
            binding.constraintContainer,
            transition
        )
        constraintSet.applyTo(binding.constraintContainer)
    }

    private fun setSpannableTitle() {
        val spannable = SpannableString(getString(R.string.title_text_View_bonus))
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.dark_orange)),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.dark_orange)),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            StyleSpan(BOLD),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            UnderlineSpan(),
            5, 12,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.title.text = spannable
    }

    private fun setSpannableDescription() {

        val spannable = SpannableString(getString(R.string.text_bonus_activity))

        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.dark_orange)),
            33, 49,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.description.text = spannable
    }
}