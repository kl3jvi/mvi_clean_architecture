package com.kl3jvi.feature_home.util

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

fun Fragment.createFragmentMenu(
    @LayoutRes menuLayout: Int,
    selectedItem: (menuItem: MenuItem) -> Boolean
) {
    val menuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuLayout, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return selectedItem(menuItem)
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}
