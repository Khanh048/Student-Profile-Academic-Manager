package com.example.studentprofile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.studentprofile.databinding.ActivityMainBinding
import com.example.studentprofile.model.Student
import com.example.studentprofile.utils.gone
import com.example.studentprofile.utils.show
import com.example.studentprofile.utils.toAcademicRanking
import com.example.studentprofile.utils.toRankingColor
import com.example.studentprofile.utils.toast
import com.example.studentprofile.utils.trimmedText
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val defaultStudent = Student(
        id = "22505120005",
        name = "Nguyễn Văn An",
        className = "22CT111",
        email = "an.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentStudent = savedInstanceState?.let(::restoreStudent) ?: defaultStudent
        bindStudentData(currentStudent)
        setupListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_STUDENT_DATA, currentStudent)
    }

    private fun setupListeners() = with(binding) {
        btnUpdateGpa.setOnClickListener {
            updateGpa()
        }
        btnReset.setOnClickListener {
            showResetConfirmation()
        }
        btnSendReport.setOnClickListener {
            sendAcademicReport()
        }

        edtGpaInput.doOnTextChanged { input, _, _, _ ->
            edtGpaInput.error = null

            input?.toString()
                ?.trim()
                ?.takeIf(String::isNotEmpty)
                ?.toDoubleOrNull()
                ?.takeIf { it in GPA_MIN..GPA_MAX }
                ?.let { previewGpa ->
                    tvPreviewRanking.apply {
                        text = getString(
                            R.string.ranking_preview,
                            previewGpa.toAcademicRanking()
                        )
                        setTextColor(previewGpa.toRankingColor())
                        show()
                    }
                } ?: tvPreviewRanking.gone()
        }
    }

    private fun updateGpa() {
        val rawInput = binding.edtGpaInput.trimmedText()
        val newGpa = rawInput.toDoubleOrNull()

        if (newGpa == null || newGpa !in GPA_MIN..GPA_MAX) {
            binding.edtGpaInput.error = getString(R.string.gpa_validation_error)
            binding.edtGpaInput.requestFocus()
            toast(getString(R.string.invalid_gpa_message))
            return
        }

        newGpa.let { validGpa ->
            currentStudent = currentStudent.copy(gpa = validGpa).also { updatedStudent ->
                Log.d(TAG, "Updated GPA to ${updatedStudent.gpa}")
            }
        }

        bindStudentData(currentStudent)
        binding.edtGpaInput.apply {
            error = null
            text.clear()
        }
        toast(getString(R.string.update_success))
    }

    private fun showResetConfirmation() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.reset_dialog_title)
            setMessage(getString(R.string.reset_dialog_message, defaultStudent.gpa))
            setNegativeButton(R.string.cancel, null)
            setPositiveButton(R.string.confirm) { _, _ ->
                currentStudent = defaultStudent
                bindStudentData(currentStudent)
                binding.edtGpaInput.apply {
                    error = null
                    text.clear()
                }
                toast(getString(R.string.reset_success))
            }
        }.show()
    }

    private fun sendAcademicReport() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${currentStudent.email}")
            putExtra(
                Intent.EXTRA_SUBJECT,
                getString(
                    R.string.email_subject,
                    currentStudent.name,
                    currentStudent.id
                )
            )
            putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.email_body,
                    currentStudent.name,
                    currentStudent.className,
                    currentStudent.gpa,
                    currentStudent.gpa.toAcademicRanking()
                )
            )
        }

        try {
            startActivity(emailIntent)
        } catch (exception: ActivityNotFoundException) {
            Log.w(TAG, "No email application available", exception)
            toast(getString(R.string.no_email_app))
        }
    }

    private fun bindStudentData(student: Student) = with(binding) {
        student.run {
            tvStudentName.text = name
            tvStudentDetails.text = getString(R.string.student_details_format, id, className)
            tvStudentEmail.text = email
            tvGpaBadge.apply {
                text = String.format(
                    Locale.US,
                    getString(R.string.gpa_badge_format),
                    gpa,
                    gpa.toAcademicRanking()
                )
                setTextColor(gpa.toRankingColor())
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun restoreStudent(savedState: Bundle): Student? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedState.getSerializable(KEY_STUDENT_DATA, Student::class.java)
        } else {
            savedState.getSerializable(KEY_STUDENT_DATA) as? Student
        }

    private companion object {
        const val TAG = "StudentProfile"
        const val KEY_STUDENT_DATA = "student_data"
        const val GPA_MIN = 0.0
        const val GPA_MAX = 4.0
    }
}
