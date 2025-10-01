package com.gogulf.passenger.app.utils.animations


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.gogulf.passenger.app.R


object SlideAnimation {
    fun slideUp(view: View) {
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            0f,  // fromYDelta
            -110f
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = false
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            -100f,  // fromYDelta
            0f
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = false
        view.startAnimation(animate)
    }

    fun slideUpReturn(view: View) {
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            -view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {


            override fun onAnimationStart(animation: Animation?) {

                view.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    // slide the view from its current position to below itself
    fun slideDownReturn(view: View) {
        val animate = TranslateAnimation(
            0f,  // fromXDelta
            0f,  // toXDelta
            -view.height.toFloat(),  // fromYDelta
            0f
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener {


            override fun onAnimationStart(animation: Animation?) {

                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {

                view.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    fun fadeOutAnimation(viewToFadeOut: View) {
        val fadeOut = ObjectAnimator.ofFloat(viewToFadeOut, "alpha", 1f, 0f)
        fadeOut.duration = 520
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // We wanna set the view to GONE, after it's fade out. so it actually disappear from the layout & don't take up space.

                viewToFadeOut.visibility = View.GONE
            }
        })
        fadeOut.start()
    }

    fun fadeInAnimation(viewToFadeIn: View) {
        val fadeIn = ObjectAnimator.ofFloat(viewToFadeIn, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                // We wanna set the view to VISIBLE, but with alpha 0. So it appear invisible in the layout.
                viewToFadeIn.visibility = View.VISIBLE
                viewToFadeIn.alpha = 0f
            }

        })
        fadeIn.start()
    }


//    fun expand(v: View) {
//        val matchParentMeasureSpec =
//            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
//        val wrapContentMeasureSpec =
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
//        val targetHeight = v.measuredHeight
//
//        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
//        v.layoutParams.height = 1
////        v.visibility = View.VISIBLE
//        val a: Animation = object : Animation() {
//            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//
////                v.visibility = View.VISIBLE
//                v.layoutParams.height =
//                    if (interpolatedTime == 1f) ConstraintLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
//                v.requestLayout()
//            }
//
//
//            override fun willChangeBounds(): Boolean {
//                return true
//            }
//        }
//
//        // Expansion speed of 1dp/ms
//        val time = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
//        a.duration = if (time > 500) 500 else 500
//        a.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation?) {
//                v.visibility = View.VISIBLE
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                v.visibility = View.VISIBLE
//            }
//
//            override fun onAnimationRepeat(animation: Animation?) {
//            }
//
//        })
//        v.startAnimation(a)
//    }

//    fun expand(v: View) {
//        val matchParentMeasureSpec =
//            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
//        val wrapContentMeasureSpec =
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
//        val targetHeight = v.measuredHeight
//
//        // Set initial height to 1 and make the view visible
//        v.layoutParams.height = 1
//        v.visibility = View.VISIBLE
//
//        val a: Animation = object : Animation() {
//            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//                v.layoutParams.height =
//                    if (interpolatedTime == 1f) ConstraintLayout.LayoutParams.WRAP_CONTENT
//                    else (targetHeight * interpolatedTime).toInt()
//                v.requestLayout()
//            }
//
//            override fun willChangeBounds(): Boolean = true
//        }
//
//        // Adjust the duration for a faster expansion
//        val time = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
//        a.duration = if (time > 300) 300 else time // Set max duration to 300ms
//
//        // Use a smoother interpolator for better visual effect
//        a.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
//
//        v.startAnimation(a)
//    }


//    fun collapse(v: View) {
//        val initialHeight = v.measuredHeight
//
//        val a: Animation = object : Animation() {
//            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//                if (interpolatedTime == 1f) {
//                    v.visibility = View.GONE
//                } else {
//                    v.layoutParams.height =
//                        (initialHeight * (1 - interpolatedTime)).toInt()
//                    v.requestLayout()
//                }
//            }
//
//            override fun willChangeBounds(): Boolean {
//                return true
//            }
//        }
//
//        // Adjust the duration for a faster collapse
//        val time = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
//        a.duration = if (time > 300) 300 else time // Set max duration to 300ms
//
//        // Use a smoother interpolator for better visual effect
//        a.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
//
//        v.startAnimation(a)
//    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        // Use ValueAnimator to animate the height change
        val heightAnimator = ValueAnimator.ofInt(initialHeight, 1).apply {
            addUpdateListener { animation ->
                v.layoutParams.height = animation.animatedValue as Int
                v.requestLayout()
            }
            duration = 300L
            interpolator = FastOutSlowInInterpolator()
        }

        val fadeOutAnimator = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f).apply {
            duration = 300L
            interpolator = FastOutSlowInInterpolator()
        }

        AnimatorSet().apply {
            playTogether(heightAnimator, fadeOutAnimator)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                }
            })
            start()
        }
    }

    fun expand(v: View) {
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight

        // Set initial height to 1 and initial alpha to 0 for fade-in effect
        v.layoutParams.height = 1
        v.alpha = 0f
        v.visibility = View.VISIBLE

        // Use ValueAnimator to animate the height change
        val heightAnimator = ValueAnimator.ofInt(1, targetHeight).apply {
            addUpdateListener { animation ->
                v.layoutParams.height = animation.animatedValue as Int
                v.requestLayout()
            }
            duration = 300L
            interpolator = FastOutSlowInInterpolator()
        }

        val fadeInAnimator = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f).apply {
            duration = 300L
            interpolator = FastOutSlowInInterpolator()
        }

        AnimatorSet().apply {
            playTogether(heightAnimator, fadeInAnimator)
            start()
        }
    }



    fun slideUpXML(view: View) {

        val slide_up: Animation = AnimationUtils.loadAnimation(
            view.context,
            R.anim.slide_up
        )
        view.startAnimation(slide_up)
    }

    fun slideDownXML(view: View) {
        val slide_down: Animation = AnimationUtils.loadAnimation(
            view.context,
            R.anim.slide_down
        )

        view.startAnimation(slide_down)
    }

}