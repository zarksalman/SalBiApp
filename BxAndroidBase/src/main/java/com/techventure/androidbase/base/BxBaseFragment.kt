package com.techventure.androidbase.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.techventure.androidbase.network.stateManager.NetState
import com.techventure.androidbase.network.stateManager.NetworkStateManager

abstract class BxBaseFragment<VM : ViewModel, VB : ViewDataBinding> : Fragment() {

    //region Properties
    protected lateinit var mViewBinding: VB
    //endregion

    //region LifeCycle
    protected abstract val mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getViewBinding().apply {
            lifecycleOwner = this@BxBaseFragment
            mViewBinding = this
        }
        customOnViewCreated(savedInstanceState)
        setObservers()
        return mViewBinding.root
    }

    abstract fun getViewBinding(): VB

    abstract fun customOnViewCreated(savedInstanceState: Bundle?)

    //endregion

    //region Open Method
    open fun onNetworkStateChanged(netState: NetState) {}
    //endregion


    //region private method
    private fun setObservers() {
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }
    //endregion
}
