@file:Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")

package com.art.privacy.policy

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class AndroidXPrivacyPolicyDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments ?: throw NullPointerException("$KEY_APP_NAME and $KEY_PRIVACY_POLICY_URL should be specified")
        val privacyPolicyUrl = arguments.getString(KEY_PRIVACY_POLICY_URL)
        val appName = arguments.getString(KEY_APP_NAME, "This app")
        if (privacyPolicyUrl.isNullOrEmpty()) {
            throw NullPointerException("$KEY_PRIVACY_POLICY_URL should be specified and not be empty")
        }

        return AlertDialog.Builder(activity)
            .setTitle(R.string.privacy_policy_title)
            .setMessage(getString(R.string.privacy_policy_message, appName))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                setPrivacyPolicyPrompted(requireContext().applicationContext)
                dismissAllowingStateLoss()
            }
            .setNegativeButton(R.string.privacy_policy_title) { _, _ ->
                open(requireContext(), privacyPolicyUrl)
            }
            .setOnKeyListener { _, keyCode, _ ->
                keyCode == KeyEvent.KEYCODE_BACK
            }
            .setCancelable(false)
            .create().also { dialog -> dialog.setCanceledOnTouchOutside(false) }
    }

    companion object {

        private const val PRIVACY_POLICY_PROMPTED = "privacy_policy_prompted"
        private const val KEY_APP_NAME = "app_name"
        private const val KEY_PRIVACY_POLICY_URL = "privacy_policy_url"

        fun showIfNeverPrompted(activity: FragmentActivity, appName: String, privacyPolicyUrl: String) {
            if (privacyPolicyPrompted(activity.applicationContext)) return

            show(activity, appName, privacyPolicyUrl)
        }

        fun show(activity: FragmentActivity, appName: String, privacyPolicyUrl: String) {
            try {
                PrivacyPolicyDialog().apply {
                    arguments = Bundle().apply {
                        putString(KEY_APP_NAME, appName)
                        putString(KEY_PRIVACY_POLICY_URL, privacyPolicyUrl)
                    }
                    show(activity.fragmentManager, "privacy_policy")
                }
            } catch (ignore: Exception) {
            }
        }

        private fun privacyPolicyPrompted(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PRIVACY_POLICY_PROMPTED, false)

        private fun setPrivacyPolicyPrompted(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PRIVACY_POLICY_PROMPTED, true).apply()

        private fun open(context: Context, url: String?): Boolean {
            if (url.isNullOrEmpty()) return false

            return try {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse(url)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    }

}