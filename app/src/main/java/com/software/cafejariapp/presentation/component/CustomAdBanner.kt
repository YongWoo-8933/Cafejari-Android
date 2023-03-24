package com.software.cafejariapp.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.software.cafejariapp.presentation.util.AdId

@Composable
fun CustomAdBanner(
    modifier: Modifier = Modifier,
    bannerSize: AdSize = AdSize.BANNER,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        AndroidView(factory = { context ->
            AdView(context).apply {
                setAdSize(bannerSize)
                adUnitId = AdId.Banner.id
                loadAd(AdRequest.Builder().build())
            }
        })
    }
}

