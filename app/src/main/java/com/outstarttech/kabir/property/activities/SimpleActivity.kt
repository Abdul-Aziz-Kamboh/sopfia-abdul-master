package com.outstarttech.kabir.property.activities

import com.inventioncore.kabir.sopfia.R
import com.outstarttech.kabir.property.activities.BaseSimpleActivity

open class SimpleActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
            R.mipmap.ic_launcher
    )

    override fun getAppLauncherName() = getString(R.string.app_name)
}
