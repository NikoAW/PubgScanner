package com.example.pubgscanner

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
        }

        val button = Button(this).apply {
            text = "Skan et"
        }

        val result = TextView(this).apply {
            textSize = 16f
        }

        val scroll = ScrollView(this).apply {
            addView(result)
        }

        root.addView(
            button,
            LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        )

        root.addView(
            scroll,
            LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
        )

        setContentView(root)

        button.setOnClickListener {
            result.text = "Skan edilir..."

            Thread {
                val scan = Scanner.scan(this)

                runOnUiThread {
                    result.text = buildReport(scan)
                }
            }.start()
        }
    }

    private fun buildReport(scan: ScanResult): String {
        val sb = StringBuilder()

        sb.appendLine("Nəticə: ${scan.verdict}")
        sb.appendLine("Ümumi risk: ${scan.totalRisk}%")
        sb.appendLine("-----------------------")

        if (scan.findings.isEmpty()) {
            sb.appendLine("Heç bir şübhəli tapıntı yoxdur.")
        } else {
            scan.findings.sortedByDescending { it.risk }.forEach {
                sb.appendLine("• ${it.title}: ${it.detail} (${it.risk}%)")
            }
        }

        sb.appendLine()
        sb.appendLine(
            "Qeyd: bu risk göstəricisidir, ban olmayacağına zəmanət deyil."
        )

        return sb.toString()
    }
}
