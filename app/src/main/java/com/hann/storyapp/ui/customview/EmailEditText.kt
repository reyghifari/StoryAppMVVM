package com.hann.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.hann.storyapp.R

class EmailEditText : TextInputEditText, View.OnTouchListener {

    private lateinit var warningButtonImg : Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun showWarningButton(){
        setButtonDrawables(endOfTheText = warningButtonImg)
    }

    private fun hideWarningButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    private fun init() {
        warningButtonImg = ContextCompat.getDrawable(context, R.drawable.baseline_warning_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches() && s.toString().isNotEmpty()) showWarningButton() else hideWarningButton()
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val warningButtonStart: Float
            val warningButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                warningButtonEnd = (warningButtonImg.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < warningButtonEnd -> isClearButtonClicked = true
                }
            } else {
                warningButtonStart = (width - paddingEnd - warningButtonImg.intrinsicWidth).toFloat()
                when {
                    event.x > warningButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        warningButtonImg = ContextCompat.getDrawable(context, R.drawable.baseline_warning_24) as Drawable
                        showWarningButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        warningButtonImg = ContextCompat.getDrawable(context, R.drawable.baseline_warning_24) as Drawable
                        error = "Masukan Email Yang Valid"
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}