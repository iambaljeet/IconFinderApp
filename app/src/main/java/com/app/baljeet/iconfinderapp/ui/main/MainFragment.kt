package com.app.baljeet.iconfinderapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.app.baljeet.iconfinderapp.BuildConfig
import com.app.baljeet.iconfinderapp.R
import com.app.baljeet.iconfinderapp.callbacks.OnDialogRetryClickListener
import com.app.baljeet.iconfinderapp.callbacks.OnRecyclerViewItemClickListener
import com.app.baljeet.iconfinderapp.models.Icon
import com.app.baljeet.iconfinderapp.models.IconsModel
import com.app.baljeet.iconfinderapp.models.WrapperData
import com.app.baljeet.iconfinderapp.ui.main.adapters.IconsGridAdapter
import com.app.baljeet.iconfinderapp.ui.main.viewmodel.MainViewModel
import com.app.baljeet.iconfinderapp.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.icons_grid_layout_list_item.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), OnRecyclerViewItemClickListener, OnDialogRetryClickListener {
    override fun onRetry(offset: Int) {
        loadIcons(offset)
    }

    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 125

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private val iconsList = mutableListOf<Icon>()
    private val defaultOffset: Int = 0
    private var offset: Int = defaultOffset
    private var searchQuery: String = "\"\""
    private var downloadUrl: String? = null
    private var mediatorLiveData: MediatorLiveData<WrapperData<IconsModel>> = MediatorLiveData()

    private lateinit var iconsGridAdapter: IconsGridAdapter
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        loadIcons(defaultOffset)
        setDataChangeListener()
    }

    private fun setDataChangeListener() {
        mediatorLiveData.observe(this, Observer {
                wrapperData: WrapperData<IconsModel> ->
            setStateLoading(false)
            isLoading = false
            if (!wrapperData.error) {
                if (wrapperData.data != null && wrapperData.data?.total_count != null) {
                    if (wrapperData.data?.total_count!! > 0) {
                        wrapperData.data?.icons?.let { iconsList.addAll(it) }
                        iconsGridAdapter.newDataInserted(iconsList)

                        val totalCount = wrapperData.data?.total_count

                        if (totalCount != null) {
                            isLastPage = iconsList.size >= totalCount
                        }
                    } else {
                        context?.let { DialogHelper.customErrorDialogBuilder(it, getString(R.string.failed_to_load_data), offset, this) }
                    }
                } else {
                    context?.let { DialogHelper.customErrorDialogBuilder(it, getString(R.string.failed_to_load_data), offset, this) }
                }
            } else {
                context?.let { DialogHelper.customErrorDialogBuilder(it, getString(R.string.failed_to_load_data), offset, this) }
            }
        })
    }

    override fun onItemClick(view: View, position: Int) {
        when(view.id) {
            imageButtonDownload.id -> {
                val icon = iconsGridAdapter.getItemByPosition(position)

                val rasterSize = icon.raster_sizes
                val formats = rasterSize?.get(rasterSize.size - 1)?.formats
                val format = formats?.get(formats.size - 1)
                val downloadUrl = format?.download_url

                downloadFile(downloadUrl)
            }
        }
    }

    private fun loadIcons(offset: Int) {
        context?.let { if (CommonHelper.isNetworkConnected(it)) {
            if (offset == defaultOffset) {
                setStateLoading(true)
            }
            mediatorLiveData = viewModel.getIcons(searchQuery, Constants.REQUEST_ICON_COUNT, offset)
        } else {
            if (offset == defaultOffset) {
                DialogHelper.customErrorDialogBuilder(it,
                    getString(R.string.no_internet_connection_available), offset, this)
            } else {
                Snackbar.make(
                    layoutMain,
                    getString(R.string.no_internet_connection_available),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        }
    }

    private fun setStateLoading(isLoading: Boolean) {
        if (isLoading) {
            iconsList.clear()
            iconsGridAdapter.clearAll()

            progressBarLoading.visibility = View.VISIBLE
            recyclerViewIcons.visibility = View.GONE
        } else {
            progressBarLoading.visibility = View.GONE
            recyclerViewIcons.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iconsGridAdapter = IconsGridAdapter(this)

        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerViewIcons.addOnScrollListener(object : PaginationScrollListener(gridLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                offset = iconsGridAdapter.itemCount + Constants.REQUEST_ICON_COUNT
                loadIcons(offset)
            }
        })

        recyclerViewIcons.layoutManager = gridLayoutManager
        recyclerViewIcons.adapter = iconsGridAdapter
    }

    fun searchIcons(searchText: String?) {
        offset = defaultOffset
        if (searchText.isNullOrEmpty()) {
            this.searchQuery = "\"\""
        } else {
            this.searchQuery = searchText
        }
        loadIcons(offset)
    }

    private fun downloadFile(fileUrl: String?) {
        downloadUrl = "${NetworkConstants.BASE_DOWNLOAD_URL}$fileUrl" +
                "?${Constants.CLIENT_ID}=${BuildConfig.client_id}&${Constants.CLIENT_SECRET}=${BuildConfig.client_secret}"

        if (checkStoragePermission()) {
            viewModel.downloadIcon(downloadUrl)
        } else {
            requestStoragePermissions()
        }
    }

    private fun checkStoragePermission(): Boolean {
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    private fun requestStoragePermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.downloadIcon(downloadUrl)
                } else {
                    Snackbar.make(layoutMain, "Storage permission is required to download", Snackbar.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}