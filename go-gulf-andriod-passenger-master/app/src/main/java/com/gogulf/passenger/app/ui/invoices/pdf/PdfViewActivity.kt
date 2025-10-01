package com.gogulf.passenger.app.ui.invoices.pdf

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityPdfViewBinding

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private lateinit var viewModel: PdfViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.setStatusBarColor(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf_view)
        viewModel = ViewModelProvider(
            this@PdfViewActivity, PdfViewModelFactory(
                intent.getStringExtra("pdf"),
                intent.getSerializableExtra("pdfs") as? GetTheFileData,
                cacheDir
            )
        )[PdfViewModel::class.java]
        binding.btnBack.setOnClickListener {
            finish()
        }
        viewModel.customLoader = CustomLoader(this)
        viewModel.customLoader?.show()



        viewModel.myLoadedFile.observe(this) {
            viewModel.customLoader?.cancel()
            binding.idPDFView.visibility = VISIBLE
            binding.btnDownload.visibility = VISIBLE

            binding.idPDFView.fromFile(it).enableAnnotationRendering(false).disableLongpress()
                .autoSpacing(false).spacing(10).load()
        }

        binding.btnDownload.setOnClickListener {
            viewModel.myLoadedFile.value?.let {
                val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", it!!)

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "application/pdf"
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val chooserIntent = Intent.createChooser(shareIntent, "Share File")
                startActivity(chooserIntent)
            }
        }


    }
}