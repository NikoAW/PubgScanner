package com.example.pubgscanner

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build

data class ScanFinding(
    val title: String,
    val detail: String,
    val risk: Int
)

data class ScanResult(
    val verdict: String,
    val totalRisk: Int,
    val findings: List<ScanFinding>
)

object Scanner {

    fun scan(context: Context): ScanResult {
        val packageManager = context.packageManager

        val apps = if (Build.VERSION.SDK_INT >= 33) {
            packageManager.getInstalledApplications(
                android.content.pm.PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledApplications(0)
        }

        val findings = mutableListOf<ScanFinding>()

        findings.add(
            ScanFinding(
                title = "Tətbiq yoxlaması",
                detail = "${apps.size} tətbiq aşkarlandı",
                risk = 0
            )
        )

        val systemApps = apps.count {
            (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        }

        val userApps = apps.size - systemApps

        findings.add(
            ScanFinding(
                title = "Sistem tətbiqləri",
                detail = "$systemApps tətbiq",
                risk = 0
            )
        )

        findings.add(
            ScanFinding(
                title = "İstifadəçi tətbiqləri",
                detail = "$userApps tətbiq",
                risk = 0
            )
        )

        return ScanResult(
            verdict = "Yoxlama tamamlandı",
            totalRisk = 0,
            findings = findings
        )
    }
}
