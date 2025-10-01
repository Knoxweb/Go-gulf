package com.gogulf.passenger.app.ui.notice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.data.model.response.notification.NoticeModel
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.NotificationClickListener
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityNoticeBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeActivity : BaseActivity<ActivityNoticeBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityNoticeBinding
    override fun getLayoutId(): Int = R.layout.activity_notice

    private val noticeVM: NoticeViewModel by viewModel()
    private var mList = ArrayList<NoticeModel>()
    private lateinit var noticeAdapter: NoticeAdapter

    var bookingId: String? = ""
    private var notificationType: String? = ""

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityNoticeBinding
        commonViewModel.navigator = this
        mViewDataBinding.toolbarViewModel = commonViewModel
        mViewDataBinding.activity = this
        notificationObserver()
        bookingId = intent.getStringExtra("bookingId")
        notificationType = intent.getStringExtra("notificationType")
        if (!bookingId.isNullOrEmpty() && !notificationType.isNullOrEmpty()) {
            redirectTo(notificationType!!, bookingId)
        }
        noticeAdapter = NoticeAdapter(mList, object : NotificationClickListener {
            override fun onClicked(model: NoticeModel) {
                noticeVM.readNotification(model.notificationId.toString())
                model.type?.let {
                    redirectTo(it.uppercase(), model.target)
                }
            }

        })
        mViewDataBinding.noticeRecyclerView.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@NoticeActivity)
            adapter = noticeAdapter
        }
    }

    private fun redirectTo(type: String, target: String?) {
        when (type) {
//
//            "BOOKING" -> {
//                val bundle = Bundle()
//                bundle.putString(IntentConstant.BOOKING_ID, bookingId)
//                gotoClass(BookingDetailActivity::class.java, bundle)
//            }

            "BOOKING" -> {
                val bundle = Bundle()
                bundle.putString(IntentConstant.BOOKING_ID, target)
                val intent = Intent(this@NoticeActivity, BookingDetailActivity::class.java)
                intent.putExtra(IntentConstant.BUNDLE, bundle)
                startActivity(intent)
            }

            "SCHEDULED" -> {
                val bundle = Bundle()
                bundle.putString(IntentConstant.BOOKING_ID, target)
                val intent = Intent(this@NoticeActivity, BookingDetailActivity::class.java)
                intent.putExtra(IntentConstant.BUNDLE, bundle)
                startActivity(intent)
            }

//            "INVOICE" -> {
//                gotoClass(InvoiceActivity::class.java)
//            }

            else -> {
                //
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    private fun notificationObserver() {
        lifecycleScope.launch {
//            BaseObserverListener.observe(
//                noticeVM.myNotificationList,
//                this@NoticeActivity,
//                object : ApiListener {
//                    override fun onError(title: String?, message: String?) {
//                        noticeAdapter.setLoading(false)
//                        hideDialog()
//                        checkNotificationList()
//                    }
//
//                    override fun onLoading() {
//                        showDialog()
//                    }
//
//                    override fun onSuccess(data: JsonObject?) {
//                        hideDialog()
//                        val notices = GetResponseModel.toArray<NoticeModel>(data).data
//                        mList.apply {
//                            clear()
//                            addAll(notices)
//                        }
//                        noticeAdapter.setLoading(false)
//                        checkNotificationList()
//                    }
//
//
//                }
//            )

            noticeVM.myNotificationList.observe(this@NoticeActivity) {
                it?.let {
                    when(it.status) {


                        Status.SUCCESS -> {
                            hideDialog()
                            mList.clear()
                            it.data?.let { it1 -> mList.addAll(it1) }
                            noticeAdapter.setLoading(false)
                            checkNotificationList()
                        }
                        Status.ERROR -> {
                            noticeAdapter.setLoading(false)
                            hideDialog()
                            checkNotificationList()
                        }
                        Status.LOADING -> {
                            showDialog()
                        }
                    }
                }

            }

        }


    }

    private fun checkNotificationList() {
        if (mList.size > 0) {
            mViewDataBinding.noticeRecyclerView.visibility = View.VISIBLE
            mViewDataBinding.noDataLayout.root.visibility = View.GONE
        } else {
            mViewDataBinding.noticeRecyclerView.visibility = View.GONE
            mViewDataBinding.noDataLayout.root.visibility = View.VISIBLE
            mViewDataBinding.noData = "No Notification Found"
        }

    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, false)
    }

    override fun onValidated() {

    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }
}