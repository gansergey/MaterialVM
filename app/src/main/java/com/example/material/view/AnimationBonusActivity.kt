package com.example.material.view

import android.os.Bundle
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
}