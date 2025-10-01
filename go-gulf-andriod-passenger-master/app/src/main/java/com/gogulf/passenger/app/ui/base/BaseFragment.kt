package com.gogulf.passenger.app.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomProgressDialog
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant

abstract class BaseFragment<T : ViewDataBinding?> : Fragment() {

    private var mViewDataBinding: T? = null
    lateinit var progressDialog: CustomProgressDialog
//    private var mActivity: BaseActivity<ViewDataBinding> ?= null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*   if (context is BaseActivity<ViewDataBinding>) {
               mActivity = context
           }*/
    }

    protected var baseLiveDataLoading = MutableLiveData<Boolean>()
    /*   val baseActivity: BaseActivity?
           get() = mActivity*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mViewDataBinding!!.root
    }

    fun log(message: String, tag: String = "Base Fragments") {
        DebugMode.e("BaseFragment", message, tag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
//        progressDialog.setTitle("Loading")
        initView(mViewDataBinding!!.root, mViewDataBinding)
        /*  baseLiveDataLoading.observe(
              viewLifecycleOwner
          ) { baseLiveDataLoading: Boolean -> if (baseLiveDataLoading) showLoading() else hideLoading() }*/
    }


/*

    protected fun replaceFragment(
        @IdRes id: Int,
        fragmentName: Fragment?,
        fragmentTag: String?,
        addToBackStack: Boolean
    ) {
        val manager: FragmentManager = mActivity.getSupportFragmentManager()
        val transaction = manager.beginTransaction()
        transaction.replace(id, fragmentName!!, fragmentTag)
        if (addToBackStack) transaction.addToBackStack(fragmentTag)
        transaction.commit()
    }

    protected fun clearFragment() {
        val manager: FragmentManager = mActivity.getSupportFragmentManager()
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
*/

    fun openNewActivity(activity: FragmentActivity, cls: Class<*>?, finishCurrent: Boolean) {
        val intent = Intent(activity, cls)
        startActivity(intent)
        if (finishCurrent) activity.finish()
    }

    fun openNewActivity(
        activity: FragmentActivity,
        cls: Class<*>?,
        bundle: Bundle,
        finishCurrent: Boolean
    ) {
        val intent = Intent(activity, cls)
        intent.putExtra(IntentConstant.BUNDLE, bundle)
        startActivity(intent)
        if (finishCurrent) activity.finish()
    }

    fun errorAlertDialog(title: String?, message: String?) {
        CustomAlertDialog(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
            .setCancellable(false)
            .show()
    }
}
