package com.vision2020.ui.nida

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.vision2020.R
import com.vision2020.utils.AppConstant
import com.vision2020.utils.isNetworkAvailable
import com.vision2020.utils.responseHandler

/**
 * A simple [Fragment] subclass.
 */
class NidaCurriculumFragment : Fragment() {

    lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater!!.inflate(R.layout.fragment_nida_curriculum,container,false)

       // var title = "KotlinApp"
        webView = view.findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        if(isNetworkAvailable(requireActivity()) ==true) {
            webView.loadUrl("https://teens.drugabuse.gov/")
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
        }else{
            requireActivity()!!.responseHandler(AppConstant.ERROR, getString(R.string.internet_error))
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.clearHistory()
        webView.clearCache(true);
        webView.loadUrl("about:blank");
    }


}
