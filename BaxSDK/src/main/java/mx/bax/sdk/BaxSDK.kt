package mx.bax.sdk

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat

class BaxSDK(private val context: Context, private val environment: String) {

    fun openBax(apiKey: String) {
        val intent = Intent(context, BaxActivity::class.java).apply {
            putExtra("URL", getUrl())
            putExtra("API_KEY", apiKey)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_up, R.anim.slide_down)
        context.startActivity(intent, options.toBundle())
    }

    private fun getUrl(): String {
        return when (environment) {
            "sandbox" -> "https://websdk.stage.bax.mx"
            "production" -> "https://websdk.bax.mx"
            else -> "https://websdk.bax-dev.com"
        }
    }
}