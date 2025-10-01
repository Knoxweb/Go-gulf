package com.gogulf.passenger.app.di.module

import com.gogulf.passenger.app.ui.addextras.AddExtrasVM
import com.gogulf.passenger.app.ui.auth.cards.AddCardsVM
import com.gogulf.passenger.app.ui.auth.otp.OTPVM
import com.gogulf.passenger.app.ui.auth.otpv2.OTPV2ViewModel
import com.gogulf.passenger.app.ui.auth.register.RegisterVM
import com.gogulf.passenger.app.ui.base.CommonViewModel
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailVM
import com.gogulf.passenger.app.ui.bookinghistory.vms.HistoryVM
import com.gogulf.passenger.app.ui.currentride.CurrentRideVM
import com.gogulf.passenger.app.ui.dashboard.DashboardVM
import com.gogulf.passenger.app.ui.getaride.GetARideVM
import com.gogulf.passenger.app.ui.getaridev2.GetARideV2ViewModel
import com.gogulf.passenger.app.ui.notice.NoticeViewModel
import com.gogulf.passenger.app.ui.plantrip.PlanTripVM
import com.gogulf.passenger.app.ui.ratings.RatingVM
import com.gogulf.passenger.app.ui.schedulebooking.fragment.pending.PendingBookingVM
import com.gogulf.passenger.app.ui.settings.mycards.MyCardVM
import com.gogulf.passenger.app.ui.settings.profile.EditProfileVM
import com.gogulf.passenger.app.ui.settings.setting.SettingVM
import com.gogulf.passenger.app.ui.support.supports.SupportVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { CommonViewModel(get(), get(), get()) }
    viewModel { OTPVM(get(), get()) }
    viewModel { RegisterVM(get(), get()) }
    viewModel { AddCardsVM(get(), get()) }
    viewModel { DashboardVM(get(), get()) }
    viewModel { EditProfileVM(get(), get()) }
    viewModel { SupportVM(get(), get()) }
    viewModel { PlanTripVM(get(), get()) }
    viewModel { AddExtrasVM(get(), get()) }
    viewModel { PendingBookingVM(get(), get()) }
    viewModel { HistoryVM(get(), get()) }
    viewModel { BookingDetailVM(get(), get()) }
    viewModel { MyCardVM(get(), get()) }
    viewModel { CurrentRideVM(get(), get(),get()) }
    viewModel { RatingVM(get(), get()) }
    viewModel { GetARideVM(get(), get(), get()) }
    viewModel { GetARideV2ViewModel(get(), get(), get()) }
    viewModel { SettingVM(get(), get()) }
    viewModel { NoticeViewModel(get(), get(), get()) }
    viewModel { OTPV2ViewModel(get(), get()) }
}