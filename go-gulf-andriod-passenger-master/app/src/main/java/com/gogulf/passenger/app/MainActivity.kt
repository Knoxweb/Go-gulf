package com.gogulf.passenger.app

import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLoginScreenBinding

class MainActivity : BaseActivity<ActivityLoginScreenBinding>() {
    private lateinit var mViewDataBinding: ActivityLoginScreenBinding
    override fun getLayoutId(): Int= R.layout.activity_login_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityLoginScreenBinding
    }
}