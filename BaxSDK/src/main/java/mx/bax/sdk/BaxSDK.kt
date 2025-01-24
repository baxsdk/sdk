package mx.bax.sdk

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat

class BaxSDK(private val context: Context, environment: String) {
    private var environment: String = environment

    fun openBax(token: String) {
        val intent = Intent(context, BaxActivity::class.java).apply {
            putExtra("URL", getUrl())
            putExtra("TOKEN", token)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_up, R.anim.slide_down)
        context.startActivity(intent, options.toBundle())
    }

    fun setEnvironment(environment: String) {
        this.environment = environment
    }

    private fun getUrl(): String {
        return when (environment) {
            "sandbox" -> "https://websdk.sandbox.bax.mx/view"
            "production" -> "https://websdk.bax.mx/view"
            else -> "https://websdk.bax-dev.com/view"
        }
    }
}